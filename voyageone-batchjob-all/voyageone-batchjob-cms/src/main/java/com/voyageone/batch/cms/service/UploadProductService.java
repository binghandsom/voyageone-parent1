package com.voyageone.batch.cms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.bean.SxProductBean;
import com.voyageone.batch.cms.bean.UpJobParamBean;
import com.voyageone.batch.cms.dao.SxWorkloadDao;
import com.voyageone.batch.cms.enums.PlatformWorkloadStatus;
import com.voyageone.batch.cms.model.SxWorkloadModel;
import com.voyageone.batch.cms.model.WorkLoadBean;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.common.components.issueLog.enums.SubSystem;
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

    private static final int PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE = 100;
    private Set<WorkLoadBean> workLoadBeans;
    @Autowired
    private CmsProductService cmsProductService;
    private Map<WorkLoadBean, List<SxProductBean>> workLoadBeanListMap;

    private static Log logger = LogFactory.getLog(UploadProductService.class);
    @Autowired
    private UploadWorkloadDispatcher workloadDispatcher;

    @Autowired
    private SxWorkloadDao sxWorkloadDao;

    public UploadProductService() {}

    public String do_upload() {
        List<SxWorkloadModel> sxWorkloadModels = sxWorkloadDao.getSxWorkloadModel (PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE);
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
            (List<SxWorkloadModel> sxWorkloadModels) {

        Map<WorkLoadBean, List<SxProductBean>> workloadBeanListMap = new HashMap<>();
        for (SxWorkloadModel sxWorkloadModel : sxWorkloadModels)
        {
            WorkLoadBean workload = new WorkLoadBean();

            int groupId = sxWorkloadModel.getGroupId();
            String channelId = sxWorkloadModel.getChannelId();

            workload.setOrder_channel_id(channelId);
            workload.setGroupId(groupId);

            List<CmsBtProductModel> cmsBtProductModels = cmsProductService.getProductByGroupId(channelId, groupId);
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

            if (mainSxProduct == null) {
                logger.warn("No main product found, can't upload group: " + groupId);
                continue;
            }
            workload.setMainProduct(mainSxProduct);
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
        switch (workLoadBean.getWorkload_status().getValue())
        {
            case PlatformWorkloadStatus.JOB_DONE:
                logger.info("==== JOB Done Begin ===!!!");
                logger.info(workLoadBean);
                logger.info("==== JOB Done End ====!!!");
                if (sxProductBeans == null)
                {
                    logger.info("current workload:" + workLoadBean);
                    for (WorkLoadBean workLoadBean1 : workLoadBeans)
                    {
                        logger.info("inter workload:" + workLoadBean1);
                    }

                    for (Map.Entry<WorkLoadBean, List<SxProductBean>> entry: workLoadBeanListMap.entrySet())
                    {
                        logger.info("key:" + entry.getKey() + "   value:" + entry.getValue());
                    }
                    break;
                }

                for (SxProductBean sxProductBean : sxProductBeans)
                {
                    CmsBtProductModel cmsBtProductModel = sxProductBean.getCmsBtProductModel();
                    CmsBtProductModel_Group_Platform cmsGroupPlatform = cmsBtProductModel.getGroups().getPlatformByGroupId(workLoadBean.getGroupId());
                    cmsGroupPlatform.setNumIId(workLoadBean.getNumId());
                    cmsGroupPlatform.setProductId(workLoadBean.getProductId());

                    /* todo 将来可能会要
                   if (workLoadBean.isHasSku()) {
                        cmsBtProductModel.setQuantity_update_type("s");
                    } else {
                        cmsBtProductModel.setQuantity_update_type("p");
                    }
                    */

                    //成功时，publish_status设为1
                    cmsGroupPlatform.setPublishStatus("已上新");
                    cmsProductService.update(cmsBtProductModel);
                }
                break;
            case PlatformWorkloadStatus.JOB_ABORT:
                logger.error("==== JOB Abort Begin ====!!!");
                logger.error(workLoadBean);
                logger.error("==== JOB Abort End ====!!!");
                for (SxProductBean sxProductBean : sxProductBeans)
                {
                    CmsBtProductModel_Group_Platform cmsGroupPlatform = sxProductBean.getCmsBtProductModelGroupPlatform();
                    String productId = workLoadBean.getProductId();
                    if (productId != null && !"".equals(productId)) {
                        cmsGroupPlatform.setProductId(productId);
                    }
                    //失败时，如果下次需要执行，publish_status设为0，否则publish_status设为2
                    if (workLoadBean.isNextProcess()) {
                        cmsGroupPlatform.setPublishStatus("等待上新");
                    } else {
                        cmsGroupPlatform.setPublishStatus("上新成功为");
                    }
                    cmsGroupPlatform.setComment(workLoadBean.getFailCause());
                    cmsProductService.update(sxProductBean.getCmsBtProductModel());
                }
                break;
            default:
                logger.error("Unknown Status");
        }


    }

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.IMS;
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
