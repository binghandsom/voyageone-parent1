package com.voyageone.task2.cms.service;

import com.google.common.collect.Lists;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.service.JdWareService;
import com.voyageone.ims.rule_expression.MasterWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_SellerCat;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.enums.PlatformWorkloadAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Charis on 2017/3/17.
 */
@Service
public class CmsBuildPlatformAttributeUpdateJdService extends BaseCronTaskService {

    private final static List<String> cartList = Lists.newArrayList("24","26","28","29");

    @Autowired
    private PlatformProductUploadService platformProductUploadService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private JdWareService jdWareService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformAttributeUpdateJdJob";
    }


    @Override
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        //获取Workload列表
        List<CmsBtSxWorkloadModel> groupList = new ArrayList<>();
        // 获取该任务可以运行的销售渠道
        List<String> channels = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);
        // 京东系平台ID

        // 从上新的任务表中获取该平台及渠道需要上新的任务列表(group by channel_id, cart_id, group_id) TODO
        List<CmsBtSxWorkloadModel> workloadList = platformProductUploadService.getSxWorkloadWithChannelIdListCartIdList(
                CmsConstants.PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE, channels, cartList);
        groupList.addAll(workloadList);
        if (groupList.size() == 0) {
            $error("更新任务表中没有该平台对应的任务列表信息！[ChannelIdList:%s]", channels);
            return;
        }
        $info("从更新任务表中共读取共读取[%d]条更新任务！[ChannelIdList:%s]", groupList.size(), channels);

        for(CmsBtSxWorkloadModel workloadModel : groupList) {
            doJdAttributeUpdate(workloadModel);
        }

    }

    /**
     * 平台产品 部分属性更新处理
     * @param work 需要更新的数据
     */
    public void doJdAttributeUpdate(CmsBtSxWorkloadModel work) throws Exception  {
        ShopBean shop = new ShopBean();
        SxData sxData = null;
        String channelId = work.getChannelId();
        int cartId = work.getCartId();
        Long groupId = work.getGroupId();
        String workloadName = work.getWorkloadName();
        try {


            sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);

            if (sxData == null) {
                String errorMsg = String.format("取得上新用的商品数据(SxData)信息失败！请向管理员确认 [sxData=null][workloadId:%s][groupId:%s]:", work.getId(), work.getGroupId());
                $error(errorMsg);
                throw new BusinessException(errorMsg);
            }
            // 如果取得上新对象商品信息出错时，报错
            if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
                String errorMsg = sxData.getErrorMessage();
                // 有错误的时候，直接报错
                throw new BusinessException(errorMsg);
            }
            //读店铺信息
            shop = Shops.getShop(channelId, cartId);

            if (shop == null) {
                $error("获取到店铺信息失败! [ChannelId:%s] [CartId:%s]", channelId, cartId);
                throw new Exception(String.format("获取到店铺信息失败! [ChannelId:%s] [CartId:%s]", channelId, cartId));
            }
            // 表达式解析子
            ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
            CmsBtProductModel_Platform_Cart cartData = sxData.getMainProduct().getPlatform(Integer.parseInt(shop.getCart_id()));
            String wareId = cartData.getpNumIId();
            if (StringUtils.isEmpty(wareId)) {
                String errorMsg = String.format("取得该平台wareId失败！[ChannelId:%s] [CartId；%s] [GroupId:%s]", channelId, cartId, groupId);
                logger.error(errorMsg);
                throw new BusinessException(errorMsg);
            }
            com.jd.open.api.sdk.domain.Ware ware = new com.jd.open.api.sdk.domain.Ware();
            ware.setWareId(Long.parseLong(wareId));
            // 店内分类
            if (PlatformWorkloadAttribute.SELLER_CIDS.equals(workloadName)) {
                ware.setShopCategorys(getShopCat(cartData));
            }
            // 商品标题
            else if (PlatformWorkloadAttribute.TITLE.equals(workloadName)) {
                ware.setTitle(getTitle(sxData, cartData));
            }
            // 商品描述
            else if (PlatformWorkloadAttribute.DESCRIPTION.equals(workloadName)) {
                ware.setIntroduction(getNote(expressionParser, shop, sxData));
            }

            jdWareService.updateJdAttribute(shop, ware, workloadName);

        }
        catch (Exception e) {

            if(sxData == null)
            {
                sxData = new SxData();
                sxData.setCartId(work.getCartId());
                sxData.setChannelId(work.getChannelId());
                sxData.setGroupId(work.getGroupId());
            }
            $error("异常信息显示为1调查", e);

            if (e instanceof BusinessException && StringUtils.isEmpty(sxData.getErrorMessage())) {
                sxData.setErrorMessage(e.getMessage());
            }

            //保存错误log
            // 如果上新数据中的errorMessage为空
            if (StringUtils.isNullOrBlank2(sxData.getErrorMessage())) {
                if(StringUtils.isNullOrBlank2(e.getMessage())) {
                    sxData.setErrorMessage("京东更新属性出现不可预知的错误，请跟管理员联系 " + e.getStackTrace()[0].toString());
                    $error(sxData.getErrorMessage());
                } else {
                    sxData.setErrorMessage(e.getMessage());
                }
            }

            // 更新失败后回写product表pPublishError的值("Error")和pPublishMessage(上新错误信息)
//            productGroupService.updateUploadErrorStatus(sxData.getPlatform(), listSxCode, sxData.getErrorMessage());
            // 上新出错时状态回写操作
            sxProductService.doUploadFinalProc(shop, false, sxData, work, "", null, "", getTaskName());
            // 插入错误消息
            sxProductService.insertBusinessLog(sxData, getTaskName());
            //保存workload
//            saveWorkload(work, WORK_LOAD_FAIL);

            e.printStackTrace();
            if (!StringUtils.isEmpty(sxData.getErrorMessage())) $error(sxData.getErrorMessage());
            $error("workload更新失败！[workId:%s][groupId:%s]", work.getId(), work.getGroupId());
        }
    }

    public String getTitle(SxData sxData, CmsBtProductModel_Platform_Cart cartData){
        String title = cartData.getFields().getStringAttribute("productTitle");
        if (StringUtils.isEmpty(title) || title.length() > 45) {
            String tmpBrand = sxData.getMainProduct().getCommonNotNull().getFieldsNotNull().getBrand();
            String tmpSizeType = sxData.getMainProduct().getCommonNotNull().getFieldsNotNull().getSizeTypeCn();
            if (StringUtils.isEmpty(tmpSizeType)) {
                tmpSizeType = sxData.getMainProduct().getCommonNotNull().getFieldsNotNull().getSizeType();
            }
            String tmpProductType = sxData.getMainProduct().getCommonNotNull().getFieldsNotNull().getProductTypeCn();
            if (StringUtils.isEmpty(tmpProductType)) {
                tmpProductType = sxData.getMainProduct().getCommonNotNull().getFieldsNotNull().getProductType();
            }
            title = tmpBrand + " " + tmpSizeType + " " + tmpProductType;
            if (StringUtils.isEmpty(title) || title.length() > 45) {
                title = tmpBrand + " " + tmpProductType;
            }
        }
        return title;
    }

    public String getNote(ExpressionParser expressionParser, ShopBean shopBean, SxData sxData){
        String strNotes;
        try {
            MasterWord masterWord = new MasterWord("details");
            RuleExpression ruleDetails = new RuleExpression();
            ruleDetails.addRuleWord(masterWord);
            String details = expressionParser.parse(ruleDetails, shopBean, getTaskName(), null);
            if (!StringUtils.isEmpty(details)) {
                strNotes = sxProductService.resolveDict(details, expressionParser, shopBean, getTaskName(), null);
                if (StringUtils.isEmpty(strNotes)) {
                    String errorMsg = String.format("详情页描述[%s]在dict表里未设置!", details);
                    sxData.setErrorMessage(errorMsg);
                    throw new BusinessException(errorMsg);
                }
            } else {
                strNotes = sxProductService.resolveDict("京东详情页描述", expressionParser, shopBean, getTaskName(), null);
            }
        } catch (Exception ex) {
            String errMsg = String.format("京东取得详情页描述信息失败！[errMsg:%s]", ex.getMessage());
            $error(errMsg);
            throw new BusinessException(errMsg);
        }
        return strNotes;
    }

    public Set<Long> getShopCat(CmsBtProductModel_Platform_Cart cartData) {
        Set<Long> shopCatSet = new HashSet<>();

        if (ListUtils.notNull(cartData.getSellerCats())) {
            cartData.getSellerCats().stream()
                    .forEach(sellerCat -> {
                        if (sellerCat != null && !StringUtils.isEmpty(sellerCat.getcId())) {
                            shopCatSet.add(Long.parseLong(sellerCat.getcId()));
                        }
                    });
        }
        return shopCatSet;
    }
}
