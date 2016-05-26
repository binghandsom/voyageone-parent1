package com.voyageone.task2.cms.service.putaway;

import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.promotion.PromotionDetailService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.bean.ProductPublishBean;
import com.voyageone.task2.cms.bean.SxProductBean;
import com.voyageone.task2.cms.bean.UpJobParamBean;
import com.voyageone.task2.cms.bean.WorkLoadBean;
import com.voyageone.task2.cms.dao.CmsBusinessLogDao;
import com.voyageone.task2.cms.dao.ProductPublishDao;
import com.voyageone.task2.cms.enums.PlatformWorkloadStatus;
import com.voyageone.task2.cms.model.CmsBusinessLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

//import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;

/**
 * Created by Leo on 2015/5/27.
 */
@Repository
public class UploadProductService extends BaseTaskService implements WorkloadCompleteIntf {
    private static final int PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE = 100000;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private UploadWorkloadDispatcher workloadDispatcher;
    @Autowired
    private CmsBtSxWorkloadDaoExt sxWorkloadDao;
    @Autowired
    private CmsBusinessLogDao cmsBusinessLogDao;
    @Autowired
    private ProductPublishDao productPublishDao;
    @Autowired
    private PromotionDetailService promotionDetailService;
    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;
    private Map<WorkLoadBean, List<SxProductBean>> workLoadBeanListMap;
    private Set<WorkLoadBean> workLoadBeans;

    public UploadProductService() {}

    public String do_upload() {
        List<CmsBtSxWorkloadModel> sxWorkloadModels = sxWorkloadDao.selectSxWorkloadModel(PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE);
        workLoadBeanListMap = buildWorkloadMap(sxWorkloadModels);
        workLoadBeans = workLoadBeanListMap.keySet();

        if (workLoadBeanListMap.isEmpty()) {
            $info("No Workloads Found!");
        }
        else {
            $debug("Find " + workLoadBeanListMap.size() + " Workloads!");
        }

        workloadDispatcher.dispatchAndRun(workLoadBeans, this);
        $info("=================Main Thread is termined===========================");
        return null;
    }

    /**
     * 构造workload对象，并维护workload和SxWorkloadModel的对应关系
     * @param sxWorkloadModels 需要上新的任务列表
     * @return workload和与他相关的SxWorkloadModel的对应关系
     */
    private Map<WorkLoadBean, List<SxProductBean>> buildWorkloadMap
            (List<CmsBtSxWorkloadModel> sxWorkloadModels) {

        Map<WorkLoadBean, List<SxProductBean>> workloadBeanListMap = new HashMap<>();
        for (CmsBtSxWorkloadModel sxWorkloadModel : sxWorkloadModels)
        {
            WorkLoadBean workload = new WorkLoadBean();

            workload.setSxWorkloadModel(sxWorkloadModel);

            Long groupId = new Long(sxWorkloadModel.getGroupId());
            String channelId = sxWorkloadModel.getChannelId();

            workload.setOrder_channel_id(channelId);
            workload.setGroupId(groupId);

            List<CmsBtProductModel> cmsBtProductModels = productService.getProductByGroupId(channelId, groupId, false);
            List<SxProductBean> sxProductBeans = new ArrayList<>();
            CmsBtProductModel mainProductModel = null;
            CmsBtProductGroupModel mainProductPlatform = null;
            SxProductBean mainSxProduct = null;

            for (CmsBtProductModel cmsBtProductModel : cmsBtProductModels) {
                CmsBtProductGroupModel productPlatform = cmsBtProductModel.getGroups();
                String prodCode = cmsBtProductModel.getFields().getCode();
                // tom 获取feed info的数据 START
                CmsBtFeedInfoModel feedInfo = cmsBtFeedInfoDao.selectProductByCode(channelId, prodCode);
                // tom 获取feed info的数据 END
                SxProductBean sxProductBean = new SxProductBean(cmsBtProductModel, productPlatform, feedInfo);
                if (filtProductsByPlatform(sxProductBean)) {
                    sxProductBeans.add(sxProductBean);
                    if (prodCode.equals(productPlatform.getMainProductCode())) {
                        mainProductModel = cmsBtProductModel;
                        mainProductPlatform = productPlatform;
                        mainSxProduct = sxProductBean;
                    }
                }
            }

            // tom 增加一个判断, 防止非天猫国际的数据进来, 这段代码也就是临时用用, 2016年5月中旬就会被废掉 START
            if (mainSxProduct != null) {
                if (mainSxProduct.getCmsBtProductModelGroupPlatform() != null) {
                    if (23 != mainSxProduct.getCmsBtProductModelGroupPlatform().getCartId()) {
                        continue;
                    }
                }
            }
            // tom 增加一个判断, 防止非天猫国际的数据进来, 这段代码也就是临时用用, 2016年5月中旬就会被废掉 END

            workload.setMainProduct(mainSxProduct);

            if (mainSxProduct != null) {
                workload.setCart_id(mainProductPlatform.getCartId());
                workload.setCatId(mainProductModel.getCatId());

                UpJobParamBean upJobParam = new UpJobParamBean();
                upJobParam.setForceAdd(false);
                workload.setUpJobParam(upJobParam);

                if (mainProductPlatform.getNumIId() != null && !"".equals(mainProductPlatform.getNumIId())) {
                    workload.setIsPublished(1);
                    workload.setNumId(mainProductPlatform.getNumIId());
                    upJobParam.setMethod(UpJobParamBean.METHOD_UPDATE);
                } else {
                    workload.setIsPublished(0);
                    upJobParam.setMethod(UpJobParamBean.METHOD_ADD);
                }
                workload.setProductId(mainProductPlatform.getPlatformPid());
            }
            workload.setWorkload_status(new PlatformWorkloadStatus(PlatformWorkloadStatus.JOB_INIT));

            workload.setHasSku(false);
            workloadBeanListMap.put(workload, sxProductBeans);
            workload.setProcessProducts(sxProductBeans);
        }
        return workloadBeanListMap;
    }

    /**
     * 如果sxProductBean中含有要在该平台中上新的sku, 返回true
     * 如果没有sku要上新，那么返回false
     */
    private boolean filtProductsByPlatform(SxProductBean sxProductBean) {
        CmsBtProductModel cmsBtProductModel = sxProductBean.getCmsBtProductModel();
        CmsBtProductGroupModel cmsBtProductModelGroupPlatform = sxProductBean.getCmsBtProductModelGroupPlatform();
        List<CmsBtProductModel_Sku> cmsBtProductModelSkus = cmsBtProductModel.getSkus();
        int cartId = cmsBtProductModelGroupPlatform.getCartId();

        if (cmsBtProductModelSkus == null) {
            return false;
        }

        for (Iterator<CmsBtProductModel_Sku> productSkuIterator = cmsBtProductModelSkus.iterator(); productSkuIterator.hasNext();) {
            CmsBtProductModel_Sku cmsBtProductModel_sku = productSkuIterator.next();
            if (!cmsBtProductModel_sku.isIncludeCart(cartId)) {
                productSkuIterator.remove();
            }
        }
        return !cmsBtProductModelSkus.isEmpty();
    }

    @Override
    public void onComplete(WorkLoadBean workLoadBean) {
        List<SxProductBean> sxProductBeans = workLoadBeanListMap.get(workLoadBean);
        SxProductBean mainSxProduct = workLoadBean.getMainProduct();
        CmsBtProductModel mainCmsProductModel = null;
        if (mainSxProduct != null) {
            mainCmsProductModel = mainSxProduct.getCmsBtProductModel();
        }
        switch (workLoadBean.getWorkload_status().getValue())
        {
            case PlatformWorkloadStatus.JOB_DONE: {
                $info("==== JOB Done Begin ===!!!");
                $info(workLoadBean.toString());
                $info("==== JOB Done End ====!!!");
                if (sxProductBeans == null) {
                    $info("current workload:" + workLoadBean);
                    for (WorkLoadBean workLoadBean1 : workLoadBeans) {
                        $info("inter workload:" + workLoadBean1);
                    }

                    for (Map.Entry<WorkLoadBean, List<SxProductBean>> entry : workLoadBeanListMap.entrySet()) {
                        $info("key:" + entry.getKey() + "   value:" + entry.getValue());
                    }
                    break;
                }

                List<String> codeList = new ArrayList<>();
                for (SxProductBean sxProductBean : sxProductBeans) {
                    CmsBtProductModel cmsBtProductModel = sxProductBean.getCmsBtProductModel();

                    //成功时，publish_status设为1
                    codeList.add(cmsBtProductModel.getFields().getCode());
                }

                assert mainCmsProductModel != null;
                CmsBtProductGroupModel mainProductPlatform = mainCmsProductModel.getGroups();

                CmsConstants.PlatformStatus oldPlatformStatus = mainProductPlatform.getPlatformStatus();
                CmsConstants.PlatformActive platformActive = mainProductPlatform.getPlatformActive();

//                String inStockTime = null, onSaleTime = null, publishTime = null;

                if (workLoadBean.getUpJobParam().getMethod().equals(UpJobParamBean.METHOD_ADD)) {
//                    publishTime = DateTimeUtil.getNow();
                    mainProductPlatform.setPublishTime(DateTimeUtil.getNow());
                }

                if ((workLoadBean.getUpJobParam().getMethod().equals(UpJobParamBean.METHOD_ADD) || oldPlatformStatus != CmsConstants.PlatformStatus.OnSale)
                        && platformActive == CmsConstants.PlatformActive.ToOnSale) {
//                    onSaleTime = DateTimeUtil.getNow();
                    mainProductPlatform.setOnSaleTime(DateTimeUtil.getNow());
                }
                if ((workLoadBean.getUpJobParam().getMethod().equals(UpJobParamBean.METHOD_ADD) || oldPlatformStatus != CmsConstants.PlatformStatus.InStock)
                        && platformActive == CmsConstants.PlatformActive.ToInStock) {
//                    inStockTime = DateTimeUtil.getNow();
                    mainProductPlatform.setInStockTime(DateTimeUtil.getNow());
                }

                CmsConstants.PlatformStatus newPlatformStatus = null;
                if (platformActive == CmsConstants.PlatformActive.ToInStock) {
                    newPlatformStatus = CmsConstants.PlatformStatus.InStock;
                } else {
                    newPlatformStatus = CmsConstants.PlatformStatus.OnSale;
                }
                // 16/4/23 这个方法是不是以前的,产品上新成功了的话,是否应该已group的方法来更新-> 是的 (回写group表和product表)
//                productService.bathUpdateWithSXResult(workLoadBean.getOrder_channel_id(), workLoadBean.getCart_id(), workLoadBean.getGroupId(),
//                        codeList, workLoadBean.getNumId(), workLoadBean.getProductId(), publishTime, onSaleTime, inStockTime, newPlatformStatus);
                // added by morse.lu 2016/05/15 start
                // 回写group表和product表
                System.out.println("上新成功, 产品id是：" + workLoadBean.getProductId() + ". 商品id是：" + workLoadBean.getNumId());
                mainProductPlatform.setNumIId(workLoadBean.getNumId());
                mainProductPlatform.setPlatformPid(workLoadBean.getProductId());
                mainProductPlatform.setPlatformStatus(newPlatformStatus);
                productGroupService.updateGroupsPlatformStatus(mainProductPlatform);
                // added by morse.lu 2016/05/15 end

                CmsBtSxWorkloadModel sxWorkloadModel = workLoadBean.getSxWorkloadModel();
                sxWorkloadModel.setPublishStatus(1);
                sxWorkloadDao.updateSxWorkloadModel(sxWorkloadModel);

                // 增加voyageone_ims.ims_bt_product表的更新, 用来给wms更新库存时候用的 START
                List<SxProductBean> sxProductBeenList = workLoadBean.getProcessProducts();
                for (SxProductBean sxProductBean : sxProductBeenList) {
                    String code = sxProductBean.getCmsBtProductModel().getFields().getCode();

                    ProductPublishBean productPublishBean = productPublishDao.selectByChannelCartCode(
                            workLoadBean.getOrder_channel_id(),
                            workLoadBean.getCart_id(),
                            code);
                    if (productPublishBean == null) {
                        // 没找到就插入
                        productPublishBean = new ProductPublishBean();
                        productPublishBean.setChannel_id(workLoadBean.getOrder_channel_id());
                        productPublishBean.setCart_id(workLoadBean.getCart_id());
                        productPublishBean.setCode(code);
                        productPublishBean.setNum_iid(workLoadBean.getNumId());
                        if (workLoadBean.isHasSku()) {
                            productPublishBean.setQuantity_update_type("s");
                        } else {
                            productPublishBean.setQuantity_update_type("p");
                        }

                        productPublishDao.insertProductPublish(productPublishBean, getTaskName());

                    } else {
                        // 找到了, 更新
                        productPublishBean.setNum_iid(workLoadBean.getNumId());
                        if (workLoadBean.isHasSku()) {
                            productPublishBean.setQuantity_update_type("s");
                        } else {
                            productPublishBean.setQuantity_update_type("p");
                        }
                        productPublishDao.updateProductPublish(productPublishBean);
                    }
                }
                // 增加voyageone_ims.ims_bt_product表的更新, 用来给wms更新库存时候用的 END

                // 增加特价宝的调用 tom START
                // 价格有可能是用priceSale, 也有可能用priceMsrp, 所以需要判断一下
                CmsChannelConfigBean tejiabaoOpenConfig = CmsChannelConfigs.getConfigBean(workLoadBean.getOrder_channel_id()
                        , com.voyageone.common.CmsConstants.ChannelConfig.PRICE
                        , String.valueOf(workLoadBean.getCart_id()) + CmsConstants.ChannelConfig.PRICE_TEJIABAO_OPEN);
                CmsChannelConfigBean tejiabaoPriceConfig = CmsChannelConfigs.getConfigBean(workLoadBean.getOrder_channel_id()
                        , com.voyageone.common.CmsConstants.ChannelConfig.PRICE
                        , String.valueOf(workLoadBean.getCart_id()) + CmsConstants.ChannelConfig.PRICE_TEJIABAO_PRICE);

                // 检查一下
                String tejiabaoOpenFlag = null;
                String tejiabaoPricePropName = null;

                if (tejiabaoOpenConfig != null && !StringUtils.isEmpty(tejiabaoOpenConfig.getConfigValue1())) {
                    if ("0".equals(tejiabaoOpenConfig.getConfigValue1()) || "1".equals(tejiabaoOpenConfig.getConfigValue1())) {
                        tejiabaoOpenFlag = tejiabaoOpenConfig.getConfigValue1();
                    }
                }
                if (tejiabaoPriceConfig != null && !StringUtils.isEmpty(tejiabaoPriceConfig.getConfigValue1())) {
                    tejiabaoPricePropName = tejiabaoPriceConfig.getConfigValue1();
                }

                if (tejiabaoOpenFlag != null && "1".equals(tejiabaoOpenFlag)) {
                    for (SxProductBean sxProductBean : sxProductBeenList) {
                        // 获取价格
                        if (sxProductBean.getCmsBtProductModel().getSkus() == null || sxProductBean.getCmsBtProductModel().getSkus().size() == 0) {
                            // 没有sku的code, 跳过
                            continue;
                        }
                        Double dblPrice = Double.parseDouble(sxProductBean.getCmsBtProductModel().getSkus().get(0).getAttribute(tejiabaoPricePropName).toString());

                        // 设置特价宝
                        CmsBtPromotionCodesBean cmsBtPromotionCodesBean = new CmsBtPromotionCodesBean();
                        cmsBtPromotionCodesBean.setPromotionId(0); // 设置为0的场合,李俊代码里会去处理
                        cmsBtPromotionCodesBean.setChannelId(workLoadBean.getOrder_channel_id());
                        cmsBtPromotionCodesBean.setCartId(workLoadBean.getCart_id());
                        cmsBtPromotionCodesBean.setProductCode(sxProductBean.getCmsBtProductModel().getFields().getCode());
                        cmsBtPromotionCodesBean.setProductId(sxProductBean.getCmsBtProductModel().getProdId());
                        cmsBtPromotionCodesBean.setPromotionPrice(dblPrice); // 真实售价
                        cmsBtPromotionCodesBean.setNumIid(workLoadBean.getNumId());
                        cmsBtPromotionCodesBean.setModifier(getTaskName());
                        promotionDetailService.teJiaBaoPromotionUpdate(cmsBtPromotionCodesBean); // 这里只需要调用更新接口就可以了, 里面会有判断如果没有的话就插入
                    }
                }

                // 增加特价宝的调用 tom END

                break;
            }
            case PlatformWorkloadStatus.JOB_ABORT: {
                $error("==== JOB Abort Begin ====!!!");
                $error(workLoadBean.toString());
                $error("==== JOB Abort End ====!!!");

                List<String> codeList = new ArrayList<>();
                for (SxProductBean sxProductBean : sxProductBeans) {
                    CmsBtProductModel cmsBtProductModel = sxProductBean.getCmsBtProductModel();

                    //成功时，publish_status设为1
                    codeList.add(cmsBtProductModel.getFields().getCode());
                }
                // 16/4/23 这个方法是不是以前的,产品上新成功了的话,是否应该已group的方法来更新-> 失败的场合就不要更新了
//                productService.bathUpdateWithSXResult(workLoadBean.getOrder_channel_id(), workLoadBean.getCart_id(), workLoadBean.getGroupId(),
//                        codeList, workLoadBean.getNumId(), workLoadBean.getProductId(), null, null, null, null);

                //保存错误的日志
                CmsBusinessLogModel cmsBusinessLogModel = new CmsBusinessLogModel();
                cmsBusinessLogModel.setChannelId(workLoadBean.getOrder_channel_id());
                cmsBusinessLogModel.setCatId(workLoadBean.getCatId());
                cmsBusinessLogModel.setCartId(workLoadBean.getCart_id());
                String productId = "";
                String mainCode = "";
                String model = "";
                if (mainCmsProductModel != null) {
                    productId = String.valueOf(mainCmsProductModel.getProdId());
                    mainCode = mainCmsProductModel.getFields().getCode();
                    model = mainCmsProductModel.getFields().getModel();
                }
                if (workLoadBean.getMainProduct() != null
                        && workLoadBean.getMainProduct().getCmsBtProductModel() != null
                        && workLoadBean.getMainProduct().getCmsBtProductModel().getFields() != null
                        && workLoadBean.getMainProduct().getCmsBtProductModel().getFields().size() > 0
                        && workLoadBean.getMainProduct().getCmsBtProductModel().getFields().getLongTitle() != null
                        ) {
                    cmsBusinessLogModel.setProductName(workLoadBean.getMainProduct().getCmsBtProductModel().getFields().getLongTitle());
                }
                cmsBusinessLogModel.setProductId(productId);
                cmsBusinessLogModel.setCode(mainCode);
                cmsBusinessLogModel.setSku("");
                cmsBusinessLogModel.setErrMsg(workLoadBean.getFailCause());
                cmsBusinessLogModel.setErrType(1);
                cmsBusinessLogModel.setStatus(0);
                cmsBusinessLogModel.setModel(model);
                cmsBusinessLogModel.setCreater(getTaskName());
                cmsBusinessLogModel.setCreated(DateTimeUtil.getNow());
                cmsBusinessLogModel.setModifier(getTaskName());
                cmsBusinessLogModel.setModified(DateTimeUtil.getNow());
                cmsBusinessLogDao.insertBusinessLog(cmsBusinessLogModel);

                CmsBtSxWorkloadModel sxWorkloadModel = workLoadBean.getSxWorkloadModel();
                sxWorkloadModel.setPublishStatus(2);
                sxWorkloadDao.updateSxWorkloadModel(sxWorkloadModel);
                break;
            }
            default:
                $error("Unknown Status");
        }


    }

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "UploadProductJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        //设置webservice的url
        for (TaskControlBean taskControlBean : taskControlList) {
            $info(taskControlBean.getCfg_name());
            $info(taskControlBean.getCfg_val1());
            $info(taskControlBean.getCfg_val2());
        }
        //开始上新任务
        do_upload();
    }
}
