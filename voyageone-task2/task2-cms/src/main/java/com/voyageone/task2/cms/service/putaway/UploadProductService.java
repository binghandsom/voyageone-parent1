package com.voyageone.task2.cms.service.putaway;

import com.voyageone.service.dao.cms.CmsBtSxWorkloadDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.bean.ProductPublishBean;
import com.voyageone.task2.cms.bean.SxProductBean;
import com.voyageone.task2.cms.bean.UpJobParamBean;
import com.voyageone.task2.cms.dao.CmsBusinessLogDao;
import com.voyageone.task2.cms.dao.ProductPublishDao;
import com.voyageone.task2.cms.enums.PlatformWorkloadStatus;
import com.voyageone.task2.cms.model.CmsBusinessLogModel;
import com.voyageone.task2.cms.bean.WorkLoadBean;
import com.voyageone.cms.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.util.DateTimeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by Leo on 2015/5/27.
 */
@Repository
public class UploadProductService extends BaseTaskService implements WorkloadCompleteIntf {
    @Autowired
    private ProductService productService;
    @Autowired
    private UploadWorkloadDispatcher workloadDispatcher;
    @Autowired
    private CmsBtSxWorkloadDao sxWorkloadDao;
    @Autowired
    private CmsBusinessLogDao cmsBusinessLogDao;
    @Autowired
    private ProductPublishDao productPublishDao;

    private static final int PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE = 100;
    private Map<WorkLoadBean, List<SxProductBean>> workLoadBeanListMap;
    private Set<WorkLoadBean> workLoadBeans;
    private static Log logger = LogFactory.getLog(UploadProductService.class);

    public UploadProductService() {}

    public String do_upload() {
        List<CmsBtSxWorkloadModel> sxWorkloadModels = sxWorkloadDao.getSxWorkloadModel (PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE);
        workLoadBeanListMap = buildWorkloadMap(sxWorkloadModels);
        workLoadBeans = workLoadBeanListMap.keySet();

        if (workLoadBeanListMap.isEmpty()) {
            logger.info("No Workloads Found!");
        }
        else {
            logger.debug("Find " + workLoadBeanListMap.size() + " Workloads!");
        }

        workloadDispatcher.dispatchAndRun(workLoadBeans, this);
        logger.info("=================Main Thread is termined===========================");
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

            Long groupId = sxWorkloadModel.getGroupId();
            String channelId = sxWorkloadModel.getChannelId();

            workload.setOrder_channel_id(channelId);
            workload.setGroupId(groupId);

            List<CmsBtProductModel> cmsBtProductModels = productService.getProductByGroupId(channelId, groupId);
            List<SxProductBean> sxProductBeans = new ArrayList<>();
            CmsBtProductModel mainProductModel = null;
            CmsBtProductModel_Group_Platform mainProductPlatform = null;
            SxProductBean mainSxProduct = null;

            for (CmsBtProductModel cmsBtProductModel : cmsBtProductModels) {
                CmsBtProductModel_Group_Platform productPlatform = cmsBtProductModel.getGroups().getPlatformByGroupId(groupId);
                SxProductBean sxProductBean = new SxProductBean(cmsBtProductModel, productPlatform);
                if (filtProductsByPlatform(sxProductBean)) {
                    sxProductBeans.add(sxProductBean);
                    if (productPlatform.getIsMain()) {
                        mainProductModel = cmsBtProductModel;
                        mainProductPlatform = productPlatform;
                        mainSxProduct = sxProductBean;
                    }
                }
            }

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
                workload.setProductId(mainProductPlatform.getProductId());
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
        CmsBtProductModel_Group_Platform cmsBtProductModelGroupPlatform = sxProductBean.getCmsBtProductModelGroupPlatform();
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
                logger.info("==== JOB Done Begin ===!!!");
                logger.info(workLoadBean);
                logger.info("==== JOB Done End ====!!!");
                if (sxProductBeans == null) {
                    logger.info("current workload:" + workLoadBean);
                    for (WorkLoadBean workLoadBean1 : workLoadBeans) {
                        logger.info("inter workload:" + workLoadBean1);
                    }

                    for (Map.Entry<WorkLoadBean, List<SxProductBean>> entry : workLoadBeanListMap.entrySet()) {
                        logger.info("key:" + entry.getKey() + "   value:" + entry.getValue());
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
                CmsBtProductModel_Group_Platform mainProductPlatform = mainCmsProductModel.getGroups().getPlatformByGroupId(workLoadBean.getGroupId());

                CmsConstants.PlatformStatus oldPlatformStatus = mainProductPlatform.getPlatformStatus();
                CmsConstants.PlatformActive platformActive = mainProductPlatform.getPlatformActive();

                String instockTime = null, onSaleTime = null, publishTime = null;

                if (workLoadBean.getUpJobParam().getMethod().equals(UpJobParamBean.METHOD_ADD)) {
                    publishTime = DateTimeUtil.getNow();
                }

                if ((workLoadBean.getUpJobParam().getMethod().equals(UpJobParamBean.METHOD_ADD) || oldPlatformStatus != CmsConstants.PlatformStatus.Onsale)
                        && platformActive == CmsConstants.PlatformActive.Onsale) {
                    onSaleTime = DateTimeUtil.getNow();
                }
                if ((workLoadBean.getUpJobParam().getMethod().equals(UpJobParamBean.METHOD_ADD) || oldPlatformStatus != CmsConstants.PlatformStatus.Instock)
                        && platformActive == CmsConstants.PlatformActive.Instock) {
                    instockTime = DateTimeUtil.getNow();
                }

                CmsConstants.PlatformStatus newPlatformStatus;
                if (platformActive == CmsConstants.PlatformActive.Instock) {
                    newPlatformStatus = CmsConstants.PlatformStatus.Instock;
                } else {
                    newPlatformStatus = CmsConstants.PlatformStatus.Onsale;
                }
                productService.bathUpdateWithSXResult(workLoadBean.getOrder_channel_id(), workLoadBean.getCart_id(), workLoadBean.getGroupId(),
                        codeList, workLoadBean.getNumId(), workLoadBean.getProductId(), publishTime, onSaleTime, instockTime, newPlatformStatus);

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

                break;
            }
            case PlatformWorkloadStatus.JOB_ABORT: {
                logger.error("==== JOB Abort Begin ====!!!");
                logger.error(workLoadBean);
                logger.error("==== JOB Abort End ====!!!");

                List<String> codeList = new ArrayList<>();
                for (SxProductBean sxProductBean : sxProductBeans) {
                    CmsBtProductModel cmsBtProductModel = sxProductBean.getCmsBtProductModel();

                    //成功时，publish_status设为1
                    codeList.add(cmsBtProductModel.getFields().getCode());
                }
                productService.bathUpdateWithSXResult(workLoadBean.getOrder_channel_id(), workLoadBean.getCart_id(), workLoadBean.getGroupId(),
                        codeList, workLoadBean.getNumId(), workLoadBean.getProductId(), null, null, null, null);

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
                logger.error("Unknown Status");
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
            logger.info(taskControlBean.getCfg_name());
            logger.info(taskControlBean.getCfg_val1());
            logger.info(taskControlBean.getCfg_val2());
        }
        //开始上新任务
        do_upload();
    }
}
