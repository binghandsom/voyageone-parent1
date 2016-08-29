package com.voyageone.task2.cms.service.putaway;

import com.voyageone.service.dao.cms.mongo.CmsMtPlatformMappingDeprecatedDao;
import com.voyageone.task2.cms.bean.WorkLoadBean;
import com.voyageone.task2.cms.bean.tcb.TaskControlBlock;
import com.voyageone.task2.cms.bean.tcb.UploadImageTcb;
import com.voyageone.task2.cms.bean.tcb.UploadProductTcb;
import com.voyageone.task2.cms.dao.SkuInventoryDao;
import com.voyageone.common.components.issueLog.IssueLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 15-6-8.
 */
public class UploadJob implements Comparable<UploadJob> {
    private String channel_id;
    private int cart_id;
    private String channel_name;
    private String platform_name;
    private UploadWorkloadDispatcher.JobStateCb jobStateCb;

    private boolean isRunning;

    //TODO 预留，将来要实时查看Job状态时使用
    private List<WorkLoadBean> workLoadBeanDoneList;
    //TODO 预留，将来要实时查看Job状态时使用
    private List<WorkLoadBean> workLoadBeanHandleList;

    //主任务
    private UploadProductHandler uploadProductHandler;
    //子任务
    private UploadImageHandler uploadImageHandler;
    private String identifer;

    public UploadJob(String channel_id, int cart_id, String identifer, UploadWorkloadDispatcher.JobStateCb jobStateCb,
                     CmsMtPlatformMappingDeprecatedDao cmsMtPlatformMappingDao, SkuInventoryDao skuInventoryDao, IssueLog issueLog) {
        this.channel_id = channel_id;
        this.cart_id = cart_id;
        this.jobStateCb = jobStateCb;
        isRunning = false;
        workLoadBeanDoneList = new ArrayList<>();
        workLoadBeanHandleList = new ArrayList<>();

        this.identifer = identifer;
        uploadProductHandler = new UploadProductHandler(this, cmsMtPlatformMappingDao, skuInventoryDao, issueLog);
        uploadImageHandler = new UploadImageHandler(this, issueLog);
    }

    public void run()
    {
        if (!isRunning) {
            uploadProductHandler.start();
            uploadImageHandler.startJob();
            isRunning = true;
        }
    }

    public void stopUploadImageHandler()
    {
        uploadImageHandler.stopRunning();
    }

    public void stopUploadProductHandler()
    {
        uploadProductHandler.stopRunning();
    }

    public void addWorkLoad(WorkLoadBean workLoadBean)
    {
        TaskControlBlock tcb = new UploadProductTcb(workLoadBean);
        uploadProductHandler.addRunningTask(tcb);
        synchronized (workLoadBeanHandleList) {
            workLoadBeanHandleList.add(workLoadBean);
        }
    }

    public void suspendMainTaskAndWakeImageTask(UploadProductTcb mainTcb, UploadImageTcb imageTcb)
    {
        uploadProductHandler.suspendCurrentTask();
        addUploadImageTask(imageTcb);
    }

    public void addUploadImageTask (UploadImageTcb tcb)
    {
        uploadImageHandler.addRunningTask(tcb);
    }

    public void uploadImageComplete(UploadImageTcb tcb)
    {
        uploadProductHandler.resumeTaskToPriQueue(tcb.getUploadProductTcb());
    }

    public void workloadComplete(WorkLoadBean workLoadBean) {
        jobStateCb.onWorkloadComplete(workLoadBean);
        //add workload to done list and remove workload from handle list
        synchronized (workLoadBeanHandleList) {
            synchronized (workLoadBeanDoneList) {
                workLoadBeanHandleList.remove(workLoadBean);
                workLoadBeanDoneList.add(workLoadBean);
            }
        }
    }

    //主线程(UploadProductHandler)结束后通知UploadJob
    public void jobDone()
    {
        /**
         * 通知子线程也要停止运行，关键，因为子线程往往在没事可做时进入睡眠，
         * 因此主线程结束后一定要通知子线程结束
        */
        uploadImageHandler.stopRunning();

        jobStateCb.onJobDone(this);
    }

    public UploadProductHandler getUploadProductHandler() {
        return uploadProductHandler;
    }

    public UploadImageHandler getUploadImageHandler() {
        return uploadImageHandler;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getPlatform_name() {
        return platform_name;
    }

    public void setPlatform_name(String platform_name) {
        this.platform_name = platform_name;
    }

    public List<WorkLoadBean> getWorkLoadBeanDoneList() {
        return workLoadBeanDoneList;
    }

    public void setWorkLoadBeanDoneList(List<WorkLoadBean> workLoadBeanDoneList) {
        this.workLoadBeanDoneList = workLoadBeanDoneList;
    }

    public List<WorkLoadBean> getWorkLoadBeanHandleList() {
        return workLoadBeanHandleList;
    }

    public void setWorkLoadBeanHandleList(List<WorkLoadBean> workLoadBeanHandleList) {
        this.workLoadBeanHandleList = workLoadBeanHandleList;
    }

    @Override
    public int compareTo(UploadJob compareUploadJob) {
        int compareRs = getWorkLoadBeanHandleList().size() - compareUploadJob.getWorkLoadBeanHandleList().size();
        if (compareRs == 0) {
            return hashCode() - compareUploadJob.hashCode();
        } else {
            return compareRs;
        }
    }

    public String getIdentifer() {
        return identifer;
    }

    public void setIdentifer(String identifer) {
        this.identifer = identifer;
    }
}
