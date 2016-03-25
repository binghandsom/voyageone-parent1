package com.voyageone.task2.cms.service.putaway;

import com.voyageone.service.dao.cms.mongo.CmsMtPlatformMappingDao;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.Context;
import com.voyageone.task2.cms.bean.SxProductBean;
import com.voyageone.task2.cms.bean.UpJobParamBean;
import com.voyageone.task2.cms.bean.tcb.*;
import com.voyageone.task2.cms.dao.SkuInventoryDao;
import com.voyageone.task2.cms.enums.PlatformWorkloadStatus;
import com.voyageone.task2.cms.bean.WorkLoadBean;
import com.voyageone.task2.cms.service.putaway.tmall.TmallProductService;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Leo on 2015/5/28.
 */
public class UploadProductHandler extends UploadWorkloadHandler{

    private UploadJob uploadJob;
    private CmsMtPlatformMappingDao cmsMtPlatformMappingDao;
    private SkuInventoryDao skuInventoryDao;
    private Log logger = LogFactory.getLog(UploadProductHandler.class);

    private TmallProductService tmallProductService;

    @Override
    protected void doJob(TaskControlBlock tcb) {
        UploadProductTcb uploadProductTcb = (UploadProductTcb) tcb;
        WorkLoadBean workLoadBean = uploadProductTcb.getWorkLoadBean();
        PlatformWorkloadStatus workloadStatus = workLoadBean.getWorkload_status();
        String channelId = workLoadBean.getOrder_channel_id();

        //如果任务是第一次开始，那么首先要初始化任务
        if (workloadStatus == null ||
                workloadStatus.getValue() == PlatformWorkloadStatus.JOB_INIT) {

            if (workLoadBean.getProcessProducts() == null || workLoadBean.getProcessProducts().isEmpty()) {
                abortJob(workLoadBean, workloadStatus, "没有要上新的product或sku");
                return;
            }

            if (workLoadBean.getMainProduct() == null) {
                abortJob(workLoadBean, workloadStatus, "没有找到主商品");
                return;
            }

            if (workloadStatus == null)
            {
                workloadStatus = new PlatformWorkloadStatus(PlatformWorkloadStatus.JOB_INIT);
                workLoadBean.setWorkload_status(workloadStatus);
            }

            String masterCId = workLoadBean.getCatId();
            if (masterCId == null || "".equals(masterCId))
            {
                String failCause = "No master category id";
                abortJob(workLoadBean, workloadStatus, failCause);
                return;
            }

            //设置平台分类
            //进一步初始化上新的数据，放在此处必放在UploadProductService中要好，因为此处已经分线程，速度更快，UploadProductService只做最基本的任务初始化
            CmsMtPlatformMappingModel cmsMtPlatformMappingModel = cmsMtPlatformMappingDao.getMappingByMainCatId(workLoadBean.getOrder_channel_id(), uploadJob.getCart_id(), masterCId);
            workLoadBean.setCmsMtPlatformMappingModel(cmsMtPlatformMappingModel);

            List<String> skus = new ArrayList<>();
            for (SxProductBean sxProductBean : workLoadBean.getProcessProducts()) {
                skus.addAll(sxProductBean.getCmsBtProductModel().getSkus().stream().map(CmsBtProductModel_Sku::getSkuCode).collect(Collectors.toList()));
            }
            Map<String, Integer> skuInventoryMap = skuInventoryDao.getSkuInventory(channelId, skus);
            workLoadBean.setSkuInventoryMap(skuInventoryMap);

            Map<CmsBtProductModel_Sku, SxProductBean> skuProductMap = new HashMap<>();
            for (SxProductBean sxProductBean : workLoadBean.getProcessProducts()) {
                for (CmsBtProductModel_Sku sku : sxProductBean.getCmsBtProductModel().getSkus()) {
                    skuProductMap.put(sku, sxProductBean);
                }
            }
            workLoadBean.setSkuProductMap(skuProductMap);

            String platformCId = cmsMtPlatformMappingModel.getPlatformCategoryId();

            if (platformCId == null)
            {
                String failCause  = "Can't find platformCid for cartId:" +  uploadJob.getCart_id() +
                        ", category_id:" + workLoadBean.getCatId();
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
                    String failCause = "Unexpected upJob Param method: " + workLoadBean.getUpJobParam().getMethod();
                    abortJob(workLoadBean, workloadStatus, failCause);
                    return;
            }
        }
        //如果任务已经初始化过，那么直接交由平台的处理逻辑来处理
        ShopBean shopBean = Shops.getShop(uploadJob.getChannel_id(), uploadJob.getCart_id());

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


    public UploadProductHandler(UploadJob uploadJob, CmsMtPlatformMappingDao cmsMtPlatformMappingDao, SkuInventoryDao skuInventoryDao, IssueLog issueLog) {
        super(issueLog);

        this.uploadJob = uploadJob;
        this.cmsMtPlatformMappingDao = cmsMtPlatformMappingDao;
        this.skuInventoryDao = skuInventoryDao;

        ChannelConfigEnums.Channel channel = ChannelConfigEnums.Channel.valueOfId(uploadJob.getChannel_id());
        CartEnums.Cart cart = CartEnums.Cart.getValueByID(String.valueOf(uploadJob.getCart_id()));
        this.setName(this.getClass().getSimpleName() + "_" + (channel!=null?channel.getFullName():"unknownChannel") + "_" +
                (cart!=null?cart.name():"unknownCart") + "_" + uploadJob.getIdentifer());

        ApplicationContext springContext = (ApplicationContext) Context.getContext().getAttribute("springContext");
        tmallProductService = springContext.getBean(TmallProductService.class);
    }

}
