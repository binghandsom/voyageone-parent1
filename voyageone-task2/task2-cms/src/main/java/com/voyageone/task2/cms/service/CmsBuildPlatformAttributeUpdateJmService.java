package com.voyageone.task2.cms.service;

import com.google.common.collect.Lists;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.common.util.baidu.translate.BaiduTranslateUtil;
import com.voyageone.components.jumei.JumeiHtDealService;
import com.voyageone.components.jumei.JumeiHtMallService;
import com.voyageone.components.jumei.JumeiHtProductService;
import com.voyageone.components.jumei.bean.HtDealUpdate_DealInfo;
import com.voyageone.components.jumei.bean.HtMallUpdateInfo;
import com.voyageone.components.jumei.bean.HtProductUpdate_ProductInfo;
import com.voyageone.components.jumei.reponse.HtDealUpdateResponse;
import com.voyageone.components.jumei.reponse.HtProductUpdateResponse;
import com.voyageone.components.jumei.request.HtDealUpdateRequest;
import com.voyageone.components.jumei.request.HtProductUpdateRequest;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.sx.PlatformWorkloadAttribute;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Charis on 2017/4/5.
 */
@SuppressWarnings("ALL")

@Service
public class CmsBuildPlatformAttributeUpdateJmService extends BaseCronTaskService{
    private final static List<String> cartList = Lists.newArrayList(String.valueOf(CartEnums.Cart.JM.getValue()));

    @Autowired
    private PlatformProductUploadService platformProductUploadService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private JumeiHtDealService jumeiHtDealService;
    @Autowired
    private JumeiHtMallService jumeiHtMallService;
    @Autowired
    private JumeiHtProductService jumeiHtProductService;
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformAttributeUpdateJmJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        //获取Workload列表
        List<CmsBtSxWorkloadModel> groupList = new ArrayList<>();
        // 获取该任务可以运行的销售渠道
        List<String> channels = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);
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
            doJmAttributeUpdate(workloadModel);
        }

    }

    public void doJmAttributeUpdate(CmsBtSxWorkloadModel workloadModel){
        String channelId = workloadModel.getChannelId();
        int cartId = workloadModel.getCartId();
        Long groupId = workloadModel.getGroupId();
        SxData sxData = null;
        String workloadName = workloadModel.getWorkloadName();
        ShopBean shop = new ShopBean();
        // 开始时间
        long prodStartTime = System.currentTimeMillis();
        workloadModel.setModified(new Date(prodStartTime));
        try {
            shop = Shops.getShop(channelId, cartId);
            if (shop == null) {
                $error("获取到店铺信息失败! [ChannelId:%s] [CartId:%s]", channelId, cartId);
                throw new Exception(String.format("获取到店铺信息失败! [ChannelId:%s] [CartId:%s]", channelId, cartId));
            }
            //是否为智能上新
            boolean blnIsSmartSx = sxProductService.isSmartSx(shop.getOrder_channel_id(), Integer.parseInt(shop.getCart_id()));

            sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);
            if (sxData == null) {
                String errorMsg = String.format("(SxData)信息失败！[sxData=null][workloadId:%s][groupId:%s]:", workloadModel.getId(), workloadModel.getGroupId());
                $error(errorMsg);
                throw new BusinessException(errorMsg);
            }
            // 如果取得上新对象商品信息出错时，报错
            if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
                String errorMsg = sxData.getErrorMessage();
                // 有错误的时候，直接报错
                throw new BusinessException(errorMsg);
            }
            // 主产品等列表取得
            CmsBtProductModel mainProduct = sxData.getMainProduct();
            // 主产品取得结果判断
            if (mainProduct == null) {
                String errMsg = String.format("取得主商品信息失败！[ChannelId:%s] [GroupId:%s]", channelId, groupId);
                $error(errMsg);
                sxData.setErrorMessage(errMsg);
                throw new BusinessException(errMsg);
            }
            ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
            CmsBtProductModel_Platform_Cart platform_cart = mainProduct.getPlatform(CartEnums.Cart.JM.getValue());
            CmsBtProductModel_Field fields = mainProduct.getCommon().getFields();
            BaseMongoMap<String, Object> jmFields = platform_cart.getFields();
            String result = "";
            if (PlatformWorkloadAttribute.TITLE.getValue().equals(workloadName)) {
                result = updateTitle(jmFields, channelId, groupId, mainProduct, shop, fields, blnIsSmartSx, sxData, platform_cart);

            } else if (PlatformWorkloadAttribute.DESCRIPTION.getValue().equals(workloadName)) {
                result = updateDescription(jmFields, channelId, groupId, mainProduct, shop, fields, blnIsSmartSx, sxData, platform_cart, expressionParser);

            } else if (PlatformWorkloadAttribute.ITEM_IMAGES.getValue().equals(workloadName)) {
                result = updateImage(jmFields, channelId, groupId, mainProduct, shop, fields, blnIsSmartSx, sxData, platform_cart, expressionParser);
            }

            if (!StringUtils.isEmpty(result)) {
                String msg = String.format("更新聚美Deal属性或更新聚美商城属性失败![WorkloadName:%s], [ProductId:%s], [HashId:%s], [MallId:%s], [Message:%s]",
                        workloadName, mainProduct.getProdId(), platform_cart.getpNumIId(), platform_cart.getpPlatformMallId(), result);
                $error(msg);
                throw new BusinessException(msg);
            } else {
                // 回写workload表(成功1)
                sxProductService.updatePlatformWorkload(workloadModel, CmsConstants.SxWorkloadPublishStatusNum.okNum, getTaskName());
            }

        }catch (Exception e) {
            if (sxData == null) {
                sxData = new SxData();
                sxData.setChannelId(channelId);
                sxData.setCartId(cartId);
                sxData.setGroupId(groupId);
                sxData.setErrorMessage(String.format("取得上新用的商品数据信息失败！[ChannelId:%s] [GroupId:%s]", channelId, groupId));
            }
            String errMsg = String.format("聚美平台更新商品标题异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [%s]",
                    channelId, cartId, groupId, e.getMessage());
            $error(errMsg);
            e.printStackTrace();
            // 如果上新数据中的errorMessage为空
            if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                sxData.setErrorMessage(errMsg);
            }
            // 回写workload表(失败2)
            sxProductService.updatePlatformWorkload(workloadModel, CmsConstants.SxWorkloadPublishStatusNum.errorNum, getTaskName());
            // 回写详细错误信息表(cms_bt_business_log)
            sxProductService.insertBusinessLog(sxData, getTaskName());
            $error(String.format("聚美平台更新商品标题信息异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [耗时:%s]",
                    channelId, cartId, groupId, (System.currentTimeMillis() - prodStartTime)));
            return;
        }
    }
    public String updateTitle(BaseMongoMap<String, Object> jmFields, String channelId, Long groupId ,CmsBtProductModel mainProduct, ShopBean shop,
                            CmsBtProductModel_Field fields, boolean blnIsSmartSx, SxData sxData, CmsBtProductModel_Platform_Cart platform_cart) throws Exception{
        StringBuffer failInfo = new StringBuffer();
        String jmHashId = platform_cart.getpNumIId(); // 聚美hashId
        String mallId = platform_cart.getpPlatformMallId(); // 聚美Mall Id.
        if (StringUtils.isEmpty(jmHashId)) {
            String error = String.format("取得聚美hashId为空！[ChannelId:%s] [GroupId:%s]", channelId, groupId);
            $error(error);
            sxData.setErrorMessage(error);
            throw new BusinessException(error);
        }

        //商品共通属性 - 产品名称
        String commonTitle = fields.getOriginalTitleCn();
        String pBrandName = platform_cart.getpBrandName();
        if (StringUtils.isEmpty(pBrandName)) {
            pBrandName = fields.getBrand();
        }
        String suitPeople;
        String productType;
        if (!StringUtils.isEmpty(fields.getProductTypeCn())) {
            productType = fields.getProductTypeCn();
        } else {
            productType = BaiduTranslateUtil.translate(fields.getProductType());
        }
        if (!StringUtils.isEmpty(fields.getSizeTypeCn())) {
            suitPeople = fields.getSizeTypeCn();
        } else {
            suitPeople = BaiduTranslateUtil.translate(fields.getSizeType());
        }
        HtDealUpdate_DealInfo dealInfo = new HtDealUpdate_DealInfo();
        // 产品长标题 charis update
        if (!StringUtils.isEmpty(jmFields.getStringAttribute("productLongName"))) {
            dealInfo.setProduct_long_name(jmFields.getStringAttribute("productLongName"));
        } else if (blnIsSmartSx) {
            if (!StringUtils.isEmpty(commonTitle)) {
                dealInfo.setProduct_long_name(commonTitle);
            } else {
                dealInfo.setProduct_long_name(pBrandName + " " + suitPeople + " " + productType + " " + fields.getCode());
            }
        }
        // 产品中标题 charis update
        if (!StringUtils.isEmpty(jmFields.getStringAttribute("productMediumName"))) {
            dealInfo.setProduct_medium_name(jmFields.getStringAttribute("productMediumName"));
        } else if (blnIsSmartSx) {
            if (!StringUtils.isEmpty(commonTitle)) {
                dealInfo.setProduct_medium_name(commonTitle);
            } else {
                dealInfo.setProduct_medium_name(pBrandName + " " + suitPeople + " " + productType + " " + fields.getModel());
            }
        }
        // 产品短标题 charis update
        if (!StringUtils.isEmpty(jmFields.getStringAttribute("productShortName"))) {
            dealInfo.setProduct_short_name(jmFields.getStringAttribute("productShortName"));
        } else if (blnIsSmartSx) {
            if (!StringUtils.isEmpty(commonTitle)) {
                dealInfo.setProduct_short_name(commonTitle);
            } else {
                dealInfo.setProduct_short_name(pBrandName + " " + suitPeople + " " + productType + " " + fields.getModel());
            }
        }
        HtDealUpdateRequest htDealUpdateRequest = new HtDealUpdateRequest();
        htDealUpdateRequest.setJumei_hash_id(jmHashId);
        htDealUpdateRequest.setUpdate_data(dealInfo);
        HtDealUpdateResponse htDealUpdateResponse = jumeiHtDealService.update(shop, htDealUpdateRequest);
        if (htDealUpdateResponse != null && htDealUpdateResponse.is_Success()) {
            $info("聚美更新Deal成功！[ProductId:%s]", mainProduct.getProdId());
        }
        //更新Deal失败
        else {
            String msg = String.format("聚美更新Deal标题失败！[ProductId:%s], [HashId:%s], [Message:%s]", mainProduct.getProdId(), jmHashId, htDealUpdateResponse.getErrorMsg());
            $error(msg);
            failInfo.append(msg + ",");
        }
        //更新聚美商城的标题
        HtMallUpdateInfo mallUpdateInfo = new HtMallUpdateInfo();
        // 聚美Mall Id
        mallUpdateInfo.setJumeiMallId(mallId);
        HtMallUpdateInfo.UpdateDataInfo updateDataInfo = mallUpdateInfo.getUpdateDataInfo();
        // 产品长标题
        updateDataInfo.setProduct_long_name(jmFields.getStringAttribute("productLongName"));
        // 产品中标题
        updateDataInfo.setProduct_medium_name(jmFields.getStringAttribute("productMediumName"));
        // 产品短标题
        updateDataInfo.setProduct_short_name(jmFields.getStringAttribute("productShortName"));
        StringBuffer sb = new StringBuffer("");
        boolean isSuccess = jumeiHtMallService.updateMall(shop, mallUpdateInfo, sb);
        if (!isSuccess) {
            // 上传失败
            String msg = String.format("聚美商城的商品标题更新失败! [ProductId:%s], [MallId:%s], [Message:%s]", mainProduct.getProdId(), mallId, sb.toString());
            $error(msg);
            failInfo.append(msg);
        } else {
            $info("聚美更新商城标题成功！[ProductId:%s]", mainProduct.getProdId());
        }

        return failInfo.toString();

    }

    public String updateDescription(BaseMongoMap<String, Object> jmFields, String channelId, Long groupId ,CmsBtProductModel mainProduct, ShopBean shop,
                            CmsBtProductModel_Field fields, boolean blnIsSmartSx, SxData sxData, CmsBtProductModel_Platform_Cart platform_cart, ExpressionParser expressionParser) throws Exception{
        StringBuffer failInfo = new StringBuffer();
        String jmHashId = platform_cart.getpNumIId(); // 聚美hashId
        String mallId = platform_cart.getpPlatformMallId(); // 聚美Mall Id.
        if (StringUtils.isEmpty(jmHashId)) {
            String error = String.format("取得聚美hashId为空！[ChannelId:%s] [GroupId:%s]", channelId, groupId);
            $error(error);
            sxData.setErrorMessage(error);
            throw new BusinessException(error);
        }
        // 判断一下聚美详情， 用哪套模板
        String strJumeiDetailTemplateName = "聚美详情";
        if (mainProduct.getChannelId().equals("928")) {
            if (mainProduct.getPlatformNotNull(CartEnums.Cart.LTT.getValue()).getFieldsNotNull().containsKey("details")) {
                String detailName = mainProduct.getPlatformNotNull(CartEnums.Cart.LTT.getValue()).getFieldsNotNull().getStringAttribute("details");

                if (StringUtils.isEmpty(detailName)) detailName = "";
                if (detailName.equals("天猫同购描述-重点商品")) {
                    strJumeiDetailTemplateName = "聚美详情-重点商品";
                } else if (detailName.equals("天猫同购描述-无属性图")) {
                    strJumeiDetailTemplateName = "聚美详情"; // 注： 这里不是写错了， 确实要这样做
                } else if (detailName.equals("天猫同购描述-非重点之英文长描述")) {
                    strJumeiDetailTemplateName = "聚美详情-非重点之英文长描述";
                } else if (detailName.equals("天猫同购描述-非重点之中文长描述")) {
                    strJumeiDetailTemplateName = "聚美详情-非重点之中文长描述";
                } else if (detailName.equals("天猫同购描述-非重点之中文使用说明")) {
                    strJumeiDetailTemplateName = "聚美详情-非重点之中文使用说明";
                } else if (detailName.equals("天猫同购描述-爆款商品")) {
                    strJumeiDetailTemplateName = "聚美详情"; // 注： 这里不是写错了， 确实要这样做
                }
            }
        }
        String jmDetailTemplate = getTemplate(strJumeiDetailTemplateName, expressionParser, shop);
        String jmProductTemplate = getTemplate("聚美实拍", expressionParser, shop);
        String jmUseageTemplate = getTemplate("聚美使用方法", expressionParser, shop);
        {
            HtDealUpdate_DealInfo dealInfo = new HtDealUpdate_DealInfo();
            dealInfo.setDescription_properties(jmDetailTemplate);
            dealInfo.setDescription_images(jmProductTemplate);
            dealInfo.setDescription_usage(jmUseageTemplate);

            HtDealUpdateRequest htDealUpdateRequest = new HtDealUpdateRequest();
            htDealUpdateRequest.setJumei_hash_id(jmHashId);
            htDealUpdateRequest.setUpdate_data(dealInfo);
            HtDealUpdateResponse htDealUpdateResponse = jumeiHtDealService.update(shop, htDealUpdateRequest);
            if (htDealUpdateResponse != null && htDealUpdateResponse.is_Success()) {
                $info("聚美更新Deal商品描述成功！[ProductId:%s]", mainProduct.getProdId());
            }
            //更新Deal失败
            else {
                String msg = String.format("聚美更新Deal商品描述失败！[ProductId:%s], [HashId:%s], [Message:%s]", mainProduct.getProdId(), jmHashId, htDealUpdateResponse.getErrorMsg());
                $error(msg);
                failInfo.append(msg + ";");
            }
        }
        {
            //更新聚美商城的商品描述
            HtMallUpdateInfo mallUpdateInfo = new HtMallUpdateInfo();
            // 聚美Mall Id
            mallUpdateInfo.setJumeiMallId(mallId);
            HtMallUpdateInfo.UpdateDataInfo updateDataInfo = mallUpdateInfo.getUpdateDataInfo();

            updateDataInfo.setDescription_properties(jmDetailTemplate);
            // 使用方法
            updateDataInfo.setDescription_usage(jmUseageTemplate);
            // 商品实拍
            updateDataInfo.setDescription_images(jmProductTemplate);
            StringBuffer sb = new StringBuffer("");
            boolean isSuccess = jumeiHtMallService.updateMall(shop, mallUpdateInfo, sb);
            if (!isSuccess) {
                // 上传失败
//                    throw new BusinessException("聚美商城的商品描述更新失败!" + sb.toString());
                String msg = String.format("聚美商城的商品描述更新失败! [ProductId:%s], [MallId:%s], [Message:%s]", mainProduct.getProdId(), mallId, sb.toString());
                $error(msg);
                failInfo.append(msg);
            }
        }

        return failInfo.toString();


    }

    public String updateImage(BaseMongoMap<String, Object> jmFields, String channelId, Long groupId ,CmsBtProductModel mainProduct, ShopBean shop,
                            CmsBtProductModel_Field fields, boolean blnIsSmartSx, SxData sxData, CmsBtProductModel_Platform_Cart platform_cart, ExpressionParser expressionParser) throws Exception{
        StringBuffer failInfo = new StringBuffer();
        String jmProductId = platform_cart.getpProductId(); // 聚美ProductId
        if (StringUtils.isEmpty(jmProductId)) {
            String error = String.format("取得聚美jmProductId为空！[ChannelId:%s] [GroupId:%s]", channelId, groupId);
            $error(error);
            sxData.setErrorMessage(error);
            throw new BusinessException(error);
        }
        HtProductUpdate_ProductInfo productInfo = new HtProductUpdate_ProductInfo();
        //商品主图
        String picTemplate = getTemplate("聚美白底方图", expressionParser, shop);

        if (!StringUtils.isNullOrBlank2(picTemplate)) {
            picTemplate = picTemplate.substring(0, picTemplate.lastIndexOf(","));
            productInfo.setNormalImage(picTemplate);
        }
        HtProductUpdateRequest htProductUpdateRequest = new HtProductUpdateRequest();
        htProductUpdateRequest.setJumei_product_id(jmProductId);
        htProductUpdateRequest.setUpdate_data(productInfo);
        HtProductUpdateResponse htProductUpdateResponse = jumeiHtProductService.update(shop, htProductUpdateRequest);
        if (htProductUpdateResponse != null && htProductUpdateResponse.getIs_Success()) {
            $info("聚美更新商品主图成功！[ProductId:%s]", mainProduct.getProdId());
        }

        //更新商品主图失败
        else {
            String msg = String.format("聚美更新商品主图失败！[ProductId:%s], [HashId:%s], [Message:%s]", mainProduct.getProdId(), jmProductId, htProductUpdateResponse.getErrorMsg());
            $error(msg);
            failInfo.append(msg);
        }
        return failInfo.toString();
    }


    /**
     * 读取字典的模板，如果返回空字符串则抛出异常
     *
     * @param dictName
     * @param expressionParser
     * @param shopProp
     * @return
     * @throws Exception
     */
    private String getTemplate(String dictName, ExpressionParser expressionParser, ShopBean shopProp) throws Exception {
        String result = sxProductService.resolveDict(dictName, expressionParser, shopProp, getTaskName(), null);
        if(StringUtils.isNullOrBlank2(result))
        {
            String errorMsg = String.format("字典解析器说:解析的结果是空的! (猜测有可能是素材管理里的共通图片啥的没有一张图片成功上传到平台) [dictName:%s],[ProdId:%s]:", dictName, expressionParser.getSxData().getMainProduct().getProdId());
            throw new BusinessException(errorMsg);
        }
        return  result;
    }

}
