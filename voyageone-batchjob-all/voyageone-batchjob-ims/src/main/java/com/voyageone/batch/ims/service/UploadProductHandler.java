package com.voyageone.batch.ims.service;

import com.voyageone.batch.Context;
import com.voyageone.batch.ims.bean.UpJobParamBean;
import com.voyageone.batch.ims.bean.tcb.*;
import com.voyageone.batch.ims.dao.CategoryMappingDao;
import com.voyageone.batch.ims.enums.PlatformWorkloadStatus;
import com.voyageone.batch.ims.modelbean.CmsModelPropBean;
import com.voyageone.batch.ims.modelbean.WorkLoadBean;
import com.voyageone.batch.ims.service.tmall.TmallProductService;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.ims.enums.CmsFieldEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import java.util.Set;

/**
 * Created by Leo on 2015/5/28.
 */
public class UploadProductHandler extends UploadWorkloadHandler{

    private UploadJob uploadJob;
    private CategoryMappingDao categoryMappingDao;
    private Log logger = LogFactory.getLog(UploadProductHandler.class);

    private TmallProductService tmallProductService;

    @Override
    protected void doJob(TaskControlBlock tcb) {
        UploadProductTcb uploadProductTcb = (UploadProductTcb) tcb;
        WorkLoadBean workLoadBean = uploadProductTcb.getWorkLoadBean();
        CmsModelPropBean cmsModelProp = workLoadBean.getCmsModelProp();
        PlatformWorkloadStatus workloadStatus = workLoadBean.getWorkload_status();

        //如果任务是第一次开始，那么首先要初始化任务
        if (workloadStatus == null ||
                workloadStatus.getValue() == PlatformWorkloadStatus.JOB_INIT) {
            if (workloadStatus == null)
            {
                workloadStatus = new PlatformWorkloadStatus(PlatformWorkloadStatus.JOB_INIT);
                workLoadBean.setWorkload_status(workloadStatus);
            }

            String masterCId = cmsModelProp.getProp(CmsFieldEnum.CmsModelEnum.category_id);
            if (masterCId == null || "".equals(masterCId))
            {
                String failCause = "No master category id";
                abortJob(workLoadBean, workloadStatus, failCause);
                return;
            }

            //设置平台分类
            //TODO: 暂不考虑店级别的分类映射
            String platformCId = categoryMappingDao.selectPlatformCidByMasterCategoryId(
                    Integer.parseInt(uploadJob.getCart_id()), masterCId);

            if (platformCId == null)
            {
                String failCause  = "Can't find platformCid for cartId:" +  uploadJob.getCart_id() +
                        ", category_id:" + cmsModelProp.getProp(CmsFieldEnum.CmsModelEnum.category_id);
                abortJob(workLoadBean, workloadStatus, failCause);
                return;
            }
            uploadProductTcb.setPlatformCId(platformCId);

            switch (workLoadBean.getUpJobParam().getMethod()) {
                case UpJobParamBean.METHOD_ADD:
                    workloadStatus.setValue(PlatformWorkloadStatus.ADD_START);
                    break;
                case UpJobParamBean.METHOD_UPDATE:
                    workloadStatus.setValue(PlatformWorkloadStatus.UPDATE_START);
                    break;
                default:
                    String failCause = "unexpect upJob Param method: " + workLoadBean.getUpJobParam().getMethod();
                    abortJob(workLoadBean, workloadStatus, failCause);
                    return;
            }
        }
        //如果任务已经初始化过，那么直接交由平台的处理逻辑来处理
        ShopBean shopBean = ShopConfigs.getShop(uploadJob.getChannel_id(), uploadJob.getCart_id());

        if (shopBean == null)
        {
            String failCause = "Can't find shopBean for cartId:" +  uploadJob.getCart_id() +
                    ", channelId:" + uploadJob.getChannel_id();
            abortJob(workLoadBean, workloadStatus, failCause);
            logIssue(failCause);
            return;
        }

        try {
            switch (PlatFormEnums.PlatForm.getValueByID(shopBean.getPlatform_id())) {
                case TM:
                    tmallProductService.doJob(uploadProductTcb, this);
                    break;
                default:
            }
        }
        catch (TaskSignal signal)
        {
            switch (signal.getSignalType())
            {
                case MainTaskToUploadImage:
                {
                    MainToUploadImageTaskSignalInfo signalInfo = (MainToUploadImageTaskSignalInfo)
                            signal.getSignalInfo();
                    suspendCurrentTask();
                    uploadJob.addUploadImageTask(signalInfo.getUploadImageTcb());
                    logger.info(String.format("tcb:%s is paused, add new image tcb!", signalInfo.getUploadProductTcb().getWorkLoadBean().toString()));
                    return;
                }
                case ABORT:
                {
                    //TODO save abort status
                    workLoadBean.getWorkload_status().setValue(PlatformWorkloadStatus.JOB_ABORT);
                    AbortTaskSignalInfo abortTaskSignalInfo = (AbortTaskSignalInfo) signal.getSignalInfo();
                    workLoadBean.setFailCause(abortTaskSignalInfo.getAbortCause());
                    if (abortTaskSignalInfo.isProcessNextTime()) {
                        workLoadBean.setNextProcess(true);
                    }
                    uploadJob.workloadComplete(workLoadBean);
                    stopCurrentTcb();
                    break;
                }
                case DONE:
                {
                    //TODO save done status
                    workloadStatus.setValue(PlatformWorkloadStatus.JOB_DONE);
                    workLoadBean.setNumId(((UploadProductTcb) tcb).getNumId());
                    uploadJob.workloadComplete(workLoadBean);
                    stopCurrentTcb();
                    break;
                }
                case DIVISION:
                {
                    //不需要通知workload complete，因为实际的子任务尚未完成
                    workloadStatus.setValue(PlatformWorkloadStatus.JOB_DONE);
                    stopCurrentTcb();
                    DivideTaskSignalInfo divideTaskSignalInfo = (DivideTaskSignalInfo) signal.getSignalInfo();

                    Set<WorkLoadBean> subWorkLoads = divideTaskSignalInfo.getSubWorkloads();
                    for (WorkLoadBean subWorkload : subWorkLoads) {
                        uploadJob.addWorkLoad(subWorkload);
                        logger.debug("sub workload: " + subWorkload.toString());
                    }
                    logger.info(workLoadBean.toString() + " has been divided to " + subWorkLoads.size() + " sub workloads.");
                    stopCurrentTcb();
                    break;
                }
            }
        }
    }

    private void abortJob(WorkLoadBean workLoadBean, PlatformWorkloadStatus workloadStatus, String failCause) {
        workloadStatus.setValue(PlatformWorkloadStatus.JOB_ABORT);
        workLoadBean.setFailCause(failCause);
        uploadJob.workloadComplete(workLoadBean);
        stopCurrentTcb();
    }

    @Override
    protected void abortTcb(TaskControlBlock currentTcb, RuntimeException re) {
        super.abortTcb(currentTcb, re);

        UploadProductTcb tcb = (UploadProductTcb) currentTcb;
        UploadImageTcb uploadImageTcb = tcb.getUploadImageTcb();

        tcb.setUploadImageTcb(null);

        if (uploadImageTcb != null) {
            uploadJob.getUploadImageHandler().stopTcb(uploadImageTcb);
        }
        re.printStackTrace();
        logger.error(re.fillInStackTrace());
        //logIssue(re.getMessage());
    }

    @Override
    protected void threadComplete() {
        //当主业务线程结束时，证明整个job也结束了
        uploadJob.jobDone();
    }


    public UploadProductHandler(UploadJob uploadJob, CategoryMappingDao categoryMappingDao, IssueLog issueLog) {
        super(issueLog);

        this.uploadJob = uploadJob;
        this.categoryMappingDao = categoryMappingDao;

        this.setName(this.getClass().getSimpleName() + "_" + uploadJob.getChannel_id() + "_" + uploadJob.getCart_id());

        ApplicationContext springContext = (ApplicationContext) Context.getContext().getAttribute("springContext");
        tmallProductService = springContext.getBean(TmallProductService.class);
    }

}
