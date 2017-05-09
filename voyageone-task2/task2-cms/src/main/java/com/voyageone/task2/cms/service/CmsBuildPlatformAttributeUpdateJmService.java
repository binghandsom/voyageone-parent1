package com.voyageone.task2.cms.service;

import com.google.common.collect.Lists;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ListUtils;
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
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Charis on 2017/4/5.
 */
@SuppressWarnings("ALL")

@Service
public class CmsBuildPlatformAttributeUpdateJmService extends BaseCronTaskService{
    // 抱团一起更新回写的属性
    private final static List<String> dealAndMallAttrs = Lists.newArrayList(PlatformWorkloadAttribute.DESCRIPTION.getValue(),
            PlatformWorkloadAttribute.TITLE.getValue());
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

        doUpdateMain(taskControlList);

    }

    public void doUpdateMain(List<TaskControlBean> taskControlList) {
        // 由于这个方法可能会自己调用自己循环很多很多次， 不一定会跳出循环， 但又希望能获取到最新的TaskControl的信息， 所以不使用基类里的这个方法了
        // 为了调试方便， 允许作为参数传入， 但是理想中实际运行中， 基本上还是自主获取的场合比较多
        if (taskControlList == null) {
            taskControlList = taskDao.getTaskControlList(getTaskName());

            if (taskControlList.isEmpty()) {
//                $info("没有找到任何配置。");
                logIssue("没有找到任何配置！！！", getTaskName());
                return;
            }

            // 是否可以运行的判断
            if (!TaskControlUtils.isRunnable(taskControlList)) {
                $info("Runnable is false");
                return;
            }

        }
        // 抽出件数(默认为500)
        int rowCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.row_count, "500"));

        // 每个小组， 最多允许的线程数量
        int threadCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.thread_count, "5"));

        // 获取该任务可以运行的销售渠道
        List<TaskControlBean> taskControlBeanList = TaskControlUtils.getVal1s(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 准备按组分配线程（相同的组， 会共用相同的一组线程通道， 不同的组， 线程通道互不干涉）
        Map<String, List<String>> mapTaskControl = new HashMap<>();
        taskControlBeanList.forEach((l)->{
            String key = l.getCfg_val2();
            if (StringUtils.isEmpty(key)) {
                key = "0";
            }
            if (mapTaskControl.containsKey(key)) {
                mapTaskControl.get(key).add(l.getCfg_val1());
            } else {
                List<String> channelList = new ArrayList<>();
                channelList.add(l.getCfg_val1());
                mapTaskControl.put(key, channelList);
            }
        });

        Map<String, ExecutorService> mapThread = new HashMap<>();

        while (true) {

            mapTaskControl.forEach((k, v)->{
                boolean blnCreateThread = false;

                if (mapThread.containsKey(k)) {
                    ExecutorService t = mapThread.get(k);
                    if (t.isTerminated()) {
                        // 可以新做一个线程
                        blnCreateThread = true;
                    }
                } else {
                    // 可以新做一个线程
                    blnCreateThread = true;
                }

                if (blnCreateThread) {
                    ExecutorService t = Executors.newSingleThreadExecutor();

                    List<String> channelIdList = v;
                    if (channelIdList != null) {
                        for (String channelId : channelIdList) {
                            t.execute(() -> {
                                try {
                                    doProductUpdate(channelId, CartEnums.Cart.JM.getValue(), threadCount, rowCount);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });

                        }
                    }
                    t.shutdown();

                    mapThread.put(k, t);

                }
            });

            boolean blnAllOver = true;
            for (Map.Entry<String, ExecutorService> entry : mapThread.entrySet()) {
                if (!entry.getValue().isTerminated()) {
                    blnAllOver = false;
                    break;
                }
            }
            if (blnAllOver) {
                break;
            }
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        // TODO: 所有渠道处理总件数为0的场合， 就跳出不继续做了。 以外的场合， 说明可能还有别的未完成的数据， 继续自己调用自己一下
        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doUpdateMain(null);

    }

    public void doProductUpdate(String channelId, int cartId, int threadPoolCnt, int rowCount) throws Exception {

        // 获取店铺信息
        ShopBean shopProp = Shops.getShop(channelId, cartId);
        if (shopProp == null) {
            return;
        }
        // 从上新的任务表中获取该平台及渠道需要上新的任务列表(group by channel_id, cart_id, group_id)
        List<CmsBtSxWorkloadModel> workloadList = platformProductUploadService.getSxWorkloadWithChannelIdListCartIdList(
                rowCount, channelId, cartId);
        if (ListUtils.isNull(workloadList)) {
            return;
        }

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolCnt);
        // 根据上新任务列表中的groupid循环上新处理
        for(CmsBtSxWorkloadModel cmsBtSxWorkloadModel : workloadList) {
            // 启动多线程
            executor.execute(() -> doJmAttributeUpdate(cmsBtSxWorkloadModel, shopProp));
        }
        // ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
        // 当所有已经提交的任务执行完毕后将会关闭ExecutorService。
        executor.shutdown(); // 并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        try {
            // 阻塞，直到线程池里所有任务结束
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public void doJmAttributeUpdate(CmsBtSxWorkloadModel workloadModel, ShopBean shop) {
        String channelId = workloadModel.getChannelId();
        int cartId = workloadModel.getCartId();
        Long groupId = workloadModel.getGroupId();
        SxData sxData = null;
        String workloadName = workloadModel.getWorkloadName();
        // 开始时间
        long prodStartTime = System.currentTimeMillis();
        workloadModel.setModified(new Date(prodStartTime));
        try {
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
            if (dealAndMallAttrs.contains(workloadName)) {
                workloadModel.setAttributeList(dealAndMallAttrs);
                result = updateDealAndMallAttribute(jmFields, channelId, groupId, mainProduct, shop, fields, blnIsSmartSx, sxData, platform_cart, expressionParser);
            } else if (PlatformWorkloadAttribute.ITEM_IMAGES.getValue().equals(workloadName)) {
                result = updateProductImage(jmFields, channelId, groupId, mainProduct, shop, fields, blnIsSmartSx, sxData, platform_cart, expressionParser);
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
            String errMsg = String.format("聚美平台更新商品异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [%s]",
                    channelId, cartId, groupId, e.getMessage());
            $error(errMsg);
            e.printStackTrace();
            // 如果上新数据中的errorMessage为空
            if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                sxData.setErrorMessage(errMsg);
            }
            // 回写workload表(失败2)
            if (dealAndMallAttrs.contains(workloadName)) {
                workloadModel.setAttributeList(dealAndMallAttrs);
                sxProductService.updatePlatformWorkload(workloadModel, CmsConstants.SxWorkloadPublishStatusNum.errorNum, getTaskName());
            } else {
                sxProductService.updatePlatformWorkload(workloadModel, CmsConstants.SxWorkloadPublishStatusNum.errorNum, getTaskName());
            }
            // 回写详细错误信息表(cms_bt_business_log)
            sxProductService.insertBusinessLog(sxData, getTaskName());
            $error(String.format("聚美平台更新商品信息异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [耗时:%s]",
                    channelId, cartId, groupId, (System.currentTimeMillis() - prodStartTime)));
            return;
        }
    }

    public String updateDealAndMallAttribute(BaseMongoMap<String, Object> jmFields, String channelId, Long groupId ,CmsBtProductModel mainProduct, ShopBean shop,
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
        HtDealUpdate_DealInfo dealInfo = new HtDealUpdate_DealInfo();
        // 判断一下聚美详情， 用哪套模板
        String strJumeiDetailTemplateName = "聚美详情";
        if (ChannelConfigEnums.Channel.USJGJ.getId().equals(mainProduct.getChannelId())) {
            strJumeiDetailTemplateName = "聚美详情-非重点";
            if (mainProduct.getPlatformNotNull(CartEnums.Cart.LTT.getValue()).getFieldsNotNull().containsKey("details")) {
                String detailName = mainProduct.getPlatformNotNull(CartEnums.Cart.LTT.getValue()).getFieldsNotNull().getStringAttribute("details");

                if (StringUtils.isEmpty(detailName)) detailName = "";
                if (detailName.equals("天猫同购描述-重点")) {
                    strJumeiDetailTemplateName = "聚美详情-重点";
                } else if (detailName.equals("天猫同购描述-非重点")) {
                    strJumeiDetailTemplateName = "聚美详情-非重点";
                } else if (detailName.equals("天猫同购描述-爆款")) {
                    strJumeiDetailTemplateName = "聚美详情-非重点"; // 注： 这里不是写错了， 确实要这样做
                }
            }
        }
        String jmDetailTemplate = getTemplate(strJumeiDetailTemplateName, expressionParser, shop);
        String jmProductTemplate = getTemplate("聚美实拍", expressionParser, shop);
        String jmUseageTemplate = getTemplate("聚美使用方法", expressionParser, shop);

        dealInfo.setDescription_properties(jmDetailTemplate);
        dealInfo.setDescription_images(jmProductTemplate);
        dealInfo.setDescription_usage(jmUseageTemplate);

        // 商品共通属性 - 产品名称
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

        // 产品长标题 charis update
        if (jmFields != null && !StringUtils.isEmpty(jmFields.getStringAttribute("productLongName"))) {
            dealInfo.setProduct_long_name(jmFields.getStringAttribute("productLongName"));
        } else if (blnIsSmartSx) {
            if(!StringUtils.isEmpty(commonTitle) && commonTitle.length() < 130) {
                dealInfo.setProduct_long_name(commonTitle);
            } else {
                dealInfo.setProduct_long_name(pBrandName + " " + suitPeople + " " + productType + " " + fields.getCode());
                if (dealInfo.getProduct_long_name().length() > 130) {
                    dealInfo.setProduct_long_name(pBrandName + " " + productType + " " + fields.getCode());
                }
            }
        }
        // 产品中标题 charis update
        if (jmFields != null && !StringUtils.isEmpty(jmFields.getStringAttribute("productMediumName"))) {
            dealInfo.setProduct_medium_name(jmFields.getStringAttribute("productMediumName"));
        } else if (blnIsSmartSx) {
            if(!StringUtils.isEmpty(commonTitle) && commonTitle.length() < 35) {
                dealInfo.setProduct_medium_name(commonTitle);
            } else {
                dealInfo.setProduct_medium_name(pBrandName + " " + suitPeople + " " + productType + " " + fields.getModel());
                if (dealInfo.getProduct_medium_name().length() > 35) {
                    dealInfo.setProduct_medium_name(pBrandName + " " + suitPeople + " " + productType);
                }
            }
        }
        // 产品短标题 charis update
        if (jmFields != null && !StringUtils.isEmpty(jmFields.getStringAttribute("productShortName"))) {
            dealInfo.setProduct_short_name(jmFields.getStringAttribute("productShortName"));
        } else if (blnIsSmartSx) {
            if(!StringUtils.isEmpty(commonTitle) && commonTitle.length() < 15) {
                dealInfo.setProduct_short_name(commonTitle);
            } else {
                dealInfo.setProduct_short_name(pBrandName + " " + suitPeople + " " + productType + " " + fields.getModel());
                if (dealInfo.getProduct_short_name().length() > 15) {
                    dealInfo.setProduct_short_name(pBrandName + " " + productType);
                }
            }
        }

        {
            HtDealUpdateRequest htDealUpdateRequest = new HtDealUpdateRequest();
            htDealUpdateRequest.setJumei_hash_id(jmHashId);
            htDealUpdateRequest.setUpdate_data(dealInfo);
            HtDealUpdateResponse htDealUpdateResponse = jumeiHtDealService.update(shop, htDealUpdateRequest);
            if (htDealUpdateResponse != null && htDealUpdateResponse.is_Success()) {
                $info("聚美更新Deal商品属性成功！[ProductId:%s]", mainProduct.getProdId());
            }
            //更新Deal失败
            else {
                String msg = String.format("聚美更新Deal商品属性更新失败！[ProductId:%s], [HashId:%s], [Message:%s]", mainProduct.getProdId(), jmHashId, htDealUpdateResponse.getErrorMsg());
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

            // 产品长标题
            updateDataInfo.setProduct_long_name(dealInfo.getProduct_long_name());
            // 产品中标题
            updateDataInfo.setProduct_medium_name(dealInfo.getProduct_medium_name());
            // 产品短标题
            updateDataInfo.setProduct_short_name(dealInfo.getProduct_short_name());

            StringBuffer sb = new StringBuffer("");
            boolean isSuccess = jumeiHtMallService.updateMall(shop, mallUpdateInfo, sb);
            if (!isSuccess) {
                // 上传失败
                String msg = String.format("聚美商城的商品属性更新失败! [ProductId:%s], [MallId:%s], [Message:%s]", mainProduct.getProdId(), mallId, sb.toString());
                $error(msg);
                failInfo.append(msg);
            }
        }

        return failInfo.toString();


    }

    public String updateProductImage(BaseMongoMap<String, Object> jmFields, String channelId, Long groupId ,CmsBtProductModel mainProduct, ShopBean shop,
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
