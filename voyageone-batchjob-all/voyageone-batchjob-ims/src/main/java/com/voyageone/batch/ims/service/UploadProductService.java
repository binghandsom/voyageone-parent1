package com.voyageone.batch.ims.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.ims.bean.UpJobParamBean;
import com.voyageone.batch.ims.bean.tcb.AbortTaskSignalInfo;
import com.voyageone.batch.ims.bean.tcb.TaskSignal;
import com.voyageone.batch.ims.dao.ProductPublishDao;
import com.voyageone.batch.ims.enums.PlatformWorkloadStatus;
import com.voyageone.batch.ims.modelbean.ProductPublishBean;
import com.voyageone.batch.ims.modelbean.WorkLoadBean;
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

    private static final int PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE = 100;
    private Set<WorkLoadBean> workLoadBeans;
    private Map<WorkLoadBean, List<ProductPublishBean>> workLoadBeanListMap;

    private static Log logger = LogFactory.getLog(UploadProductService.class);
    @Autowired
    private UploadWorkloadDispatcher workloadDispatcher;

    @Autowired
    private ProductPublishDao productPublishDao;

    @Autowired
    private CmsDataProvider cmsDataProvider;

    public UploadProductService() {}

    public String do_upload() {
        List<ProductPublishBean> productPublishBeans = productPublishDao.selectNoPublishBeansByTime
                (PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE);
        workLoadBeanListMap = buildWorkloadProductPublishBeansMap
                (productPublishBeans);
        workLoadBeans = workLoadBeanListMap.keySet();
        completeWorkloadProductPublishBeansMap(workLoadBeanListMap);

        if (workLoadBeanListMap.isEmpty())
        {
            logger.info("No Workloads Found!");
        }
        else
        {
            logger.debug("Find " + workLoadBeanListMap.size() + " Workloads!");
        }
        try {
            for (Iterator<WorkLoadBean> $i = workLoadBeans.iterator(); $i.hasNext(); ) {
                StringBuffer failCause = new StringBuffer();
                WorkLoadBean workload = $i.next();
                if (!cmsDataProvider.fillCmsProp(workload, failCause)) {
                    workload.setFailCause(failCause.toString());
                    workload.setWorkload_status(new PlatformWorkloadStatus(PlatformWorkloadStatus.JOB_ABORT) {});
                    logger.error(failCause.toString());
                    //logIssue(failCause.toString());
                    onComplete(workload);
                    $i.remove();
                    continue;
                }
                if (null == workload.getLevelValue() || "".equals(workload.getLevelValue())) {
                    workload.setFailCause("Master prop is not set, workload: " + workload);
                    $i.remove();
                }
                //sku
            }
        } catch (TaskSignal taskSignal) {
            logger.error(((AbortTaskSignalInfo)taskSignal.getSignalInfo()).getAbortCause());
            logIssue(((AbortTaskSignalInfo)taskSignal.getSignalInfo()).getAbortCause());
            return null;
        }
        workloadDispatcher.dispatchAndRun(workLoadBeans, this);
        logger.info("=================Main Thread is termined===========================");
        return null;
    }

    /**
     * 再次从ims_mt_product 表中读取model下，未被加入上新任务的产品，并设置主商品
     * @param workLoadBeanListMap
     */
    private void completeWorkloadProductPublishBeansMap(Map<WorkLoadBean, List<ProductPublishBean>> workLoadBeanListMap) {
        for (Map.Entry<WorkLoadBean, List<ProductPublishBean>> entry : workLoadBeanListMap.entrySet())
        {
            WorkLoadBean workload = entry.getKey();
            boolean hasPublishedProduct = false;
            //查询所有该model下同一个group的产品
            List<ProductPublishBean> extraProductPublishBeans = productPublishDao.selectProductPublishByCondition
                    (workload.getCart_id(), workload.getOrder_channel_id(), workload.getModelId(), workload.getGroupId(),
                            null);
            List<ProductPublishBean> beansWillBeAdd = new ArrayList<>();
            if (extraProductPublishBeans != null && !extraProductPublishBeans.isEmpty()) {
                for (ProductPublishBean productPublishBean : entry.getValue()) {
                    for (ProductPublishBean extraProductPublishBean : extraProductPublishBeans) {
                        if (extraProductPublishBean.getMain_product_flg() == 1) {
                            workload.setMainCode(extraProductPublishBean.getCode());
                        }
                        if (extraProductPublishBean.getIs_published() == 1) {
                            hasPublishedProduct = true;
                        }
                        if (productPublishBean.getProduct_id() != extraProductPublishBean.getProduct_id()) {
                            beansWillBeAdd.add(extraProductPublishBean);
                        }
                    }
                }
                entry.getValue().addAll(beansWillBeAdd);
            }

            if (hasPublishedProduct)
            {
                UpJobParamBean upJobParam = new UpJobParamBean();
                upJobParam.setForceAdd(false);
                upJobParam.setMethod(UpJobParamBean.METHOD_UPDATE);
                workload.setUpJobParam(upJobParam);
            }
            else {
                UpJobParamBean upJobParam = new UpJobParamBean();
                upJobParam.setForceAdd(false);
                upJobParam.setMethod(UpJobParamBean.METHOD_ADD);
                workload.setUpJobParam(upJobParam);
            }
        }
    }

    /**
     * 构造workload对象，并维护workload和productPublishBean的对应关系
     * @param productPublishBeans
     * @return workload和与他相关的productPublishBean的对应关系
     */
    private Map<WorkLoadBean, List<ProductPublishBean>> buildWorkloadProductPublishBeansMap
            (List<ProductPublishBean> productPublishBeans) {

        Map<WorkLoadBean, List<ProductPublishBean>> workLoadBeanListMap = new HashMap<>();
        for (ProductPublishBean productPublishBean : productPublishBeans)
        {
            WorkLoadBean workLoadSelected = null;
            for (Map.Entry<WorkLoadBean, List<ProductPublishBean>> entry : workLoadBeanListMap.entrySet())
            {
                WorkLoadBean workLoad = entry.getKey();
                if (workLoad.getOrder_channel_id().equals(productPublishBean.getChannel_id())
                        && workLoad.getCart_id() == productPublishBean.getCart_id()
                        && workLoad.getModelId() == productPublishBean.getModel_id()
                        && workLoad.getGroupId() == productPublishBean.getCn_group_id())
                {
                    workLoadSelected = workLoad;
                    break;
                }
            }

            if (workLoadSelected == null)
            {
                workLoadSelected = new WorkLoadBean();
                workLoadSelected.setOrder_channel_id(productPublishBean.getChannel_id());
                workLoadSelected.setCart_id(productPublishBean.getCart_id());
                workLoadSelected.setGroupId(productPublishBean.getCn_group_id());
                workLoadSelected.setModelId(productPublishBean.getModel_id());
                workLoadSelected.setNumId(productPublishBean.getNum_iid());
                workLoadSelected.setProductId(String.valueOf(productPublishBean.getPublish_product_id()));
                workLoadSelected.setIsPublished(productPublishBean.getIs_published());
                //默认没有sku属性，当检测到有sku时，会置为true
                workLoadSelected.setHasSku(false);
                if (productPublishBean.getMain_product_flg() == 1) {
                    workLoadSelected.setMainCode(productPublishBean.getCode());
                }
                workLoadBeanListMap.put(workLoadSelected, new ArrayList<>());
            }

            if (workLoadSelected.getIsPublished() == 0 && productPublishBean.getIs_published() == 1) {
                workLoadSelected.setNumId(productPublishBean.getNum_iid());
                workLoadSelected.setProductId(String.valueOf(productPublishBean.getProduct_id()));
                workLoadSelected.setIsPublished(productPublishBean.getIs_published());
            }
            workLoadBeanListMap.get(workLoadSelected).add(productPublishBean);
        }
        return workLoadBeanListMap;
    }

    @Override
    public void onComplete(WorkLoadBean workLoadBean) {
        List<ProductPublishBean> productPublishBeans = workLoadBeanListMap.get(workLoadBean);
        switch (workLoadBean.getWorkload_status().getValue())
        {
            case PlatformWorkloadStatus.JOB_DONE:
                logger.info("==== JOB Done Begin ===!!!");
                logger.info(workLoadBean);
                logger.info("==== JOB Done End ====!!!");
                if (productPublishBeans == null)
                {
                    logger.info("current workload:" + workLoadBean);
                    for (WorkLoadBean workLoadBean1 : workLoadBeans)
                    {
                        logger.info("inter workload:" + workLoadBean1);
                    }

                    for (Map.Entry<WorkLoadBean, List<ProductPublishBean>> entry: workLoadBeanListMap.entrySet())
                    {
                        logger.info("key:" + entry.getKey() + "   value:" + entry.getValue());
                    }
                }

                boolean hasMainProduct = false;
                for (ProductPublishBean productPublishBean : productPublishBeans)
                {
                    if (productPublishBean.getCode().equals(workLoadBean.getMainCode())) {
                        productPublishBean.setMain_product_flg(1);
                        hasMainProduct = true;
                    } else {
                        productPublishBean.setMain_product_flg(0);
                    }
                    productPublishBean.setNum_iid(workLoadBean.getNumId());
                    if (workLoadBean.getNumId() != null && !"".equals(workLoadBean.getNumId())) {
                        productPublishBean.setIs_published(1);
                    }
                    productPublishBean.setPublish_date_time(DateTimeUtil.getNow());

                    String productId = workLoadBean.getProductId();
                    if (productId != null && !"".equals(productId)) {
                        productPublishBean.setPublish_product_status(1);
                        productPublishBean.setPublish_product_id(productId);
                    } else {
                        productPublishBean.setPublish_product_status(0);
                    }
                    if (workLoadBean.isHasSku()) {
                        productPublishBean.setQuantity_update_type("s");
                    } else {
                        productPublishBean.setQuantity_update_type("p");
                    }

                    //成功时，publish_status设为1
                    productPublishBean.setPublish_status(1);
                    productPublishBean.setModifier(getTaskName());
                    productPublishBean.setModified(DateTimeUtil.getNow());
                    productPublishDao.updateProductPublish(productPublishBean);
                }
                if (!hasMainProduct) {
                    logger.error("No main product find, there must be some errors! workload:" + workLoadBean);
                }
                break;
            case PlatformWorkloadStatus.JOB_ABORT:
                logger.error("==== JOB Abort Begin ====!!!");
                logger.error(workLoadBean);
                logger.error("==== JOB Abort End ====!!!");
                for (ProductPublishBean productPublishBean : productPublishBeans)
                {
                    productPublishBean.setIs_published(workLoadBean.getIsPublished());

                    String productId = workLoadBean.getProductId();
                    if (productId != null && !"".equals(productId)) {
                        productPublishBean.setPublish_product_status(1);
                        productPublishBean.setPublish_product_id(productId);
                    } else {
                        productPublishBean.setPublish_product_status(0);
                    }
                    //失败时，如果下次需要执行，publish_status设为0，否则publish_status设为2
                    if (workLoadBean.isNextProcess()) {
                        productPublishBean.setPublish_status(0);
                    } else {
                        productPublishBean.setPublish_status(2);
                    }
                    productPublishBean.setPublish_failed_comment(workLoadBean.getFailCause());
                    productPublishBean.setIs_published(workLoadBean.getIsPublished());
                    productPublishBean.setModifier(getTaskName());
                    productPublishBean.setModified(DateTimeUtil.getNow());
                    productPublishDao.updateProductPublish(productPublishBean);
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
            if ("cms_webservice_url".equals(taskControlBean.getCfg_name())) {
                cmsDataProvider.setCmsWebServiceUrl(taskControlBean.getCfg_val2());
                break;
            }
        }
        //开始上新任务
        do_upload();
    }
}
