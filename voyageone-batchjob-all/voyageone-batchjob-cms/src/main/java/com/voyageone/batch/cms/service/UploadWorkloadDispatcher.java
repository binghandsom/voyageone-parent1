package com.voyageone.batch.cms.service;

import com.voyageone.batch.cms.dao.SkuInventoryDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformMappingDao;
import com.voyageone.batch.cms.model.WorkLoadBean;
import com.voyageone.common.components.issueLog.IssueLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 上传任务分为两个线程handler，一个是上传商品handler，一个是上传图片handler
 * 该类会根据任务的状态，交由相应的线程handler来处理
 * Created by Leo on 2015/5/28.
 */
@Repository
public class UploadWorkloadDispatcher {
    private Map<StoreKey, UploadJob> upJobMap;
    private static Log logger = LogFactory.getLog(UploadWorkloadDispatcher.class);

    //Job回调函数
    private WorkloadCompleteIntf workloadComplete;

    //任务各状态回调函数
    private JobStateCb jobStateCb;

    //用于向主线程发送信号
    private Object mainThreadSingal;

    @Autowired
    private CmsMtPlatformMappingDao cmsMtPlatformMappingDao;
    @Autowired
    private SkuInventoryDao skuInventoryDao;
    @Autowired
    private IssueLog issueLog;

    private static final int MAX_JOB_COUNT = 3;

    public UploadWorkloadDispatcher()
    {
        upJobMap = new HashMap<>();
        jobStateCb = new JobStateCb();
        mainThreadSingal = new Object();
    }
    private class StoreKey
    {
        private String _channel_id;
        private int _cart_id;

        public StoreKey(String channel_id, int cart_id)
        {
            this._channel_id = channel_id;
            this._cart_id = cart_id;
        }

        public int getCart_id() {
            return _cart_id;
        }

        public String getChannel_id() {
            return _channel_id;
        }

        @Override
        public boolean equals(Object obj) {
            StoreKey cmpObj = (StoreKey) obj;
            return (_channel_id.equals(cmpObj.getChannel_id()) && _cart_id == cmpObj.getCart_id());

        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 37 * result + _channel_id.hashCode();
            result = 37 * result + _cart_id;
            return result;
        }

        @Override
        public String toString()
        {
            return "[channel_id:" + _channel_id + " cart_id:" + _cart_id + "]";
        }
    }


    /**
     * 分发任务的主要函数
     * 根据store和任务状态分发，一个store的多个任务根据任务状态要配给两个线程
     * 如果任务处于上传图片状态，交由UploadImageHandler处理，否则，交由UploadProductHandler
     * */
   public void dispatchWorkload(WorkLoadBean workLoadBean)
   {
       StoreKey storeKey;
       storeKey = new StoreKey(workLoadBean.getOrder_channel_id(), workLoadBean.getCart_id());
       UploadJob uploadJob = upJobMap.get(storeKey);
       if (uploadJob == null)
       {
           if (upJobMap.size() < MAX_JOB_COUNT) {
               uploadJob = new UploadJob(workLoadBean.getOrder_channel_id(), workLoadBean.getCart_id(),
                       jobStateCb, cmsMtPlatformMappingDao, skuInventoryDao, issueLog);

               synchronized (upJobMap) {
                   upJobMap.put(storeKey, uploadJob);
               }
           }
           else {
               logger.info("Count of upload job has reached to " + MAX_JOB_COUNT + " ignore workload" + workLoadBean);
               return;
           }
       }
       uploadJob.addWorkLoad(workLoadBean);
       logger.debug("add workload to UploadProductHandler, storeKey:" + storeKey + ", workloadBean:" + workLoadBean);

   }

    /**
     * 当某job结束时，必须在回调函数中从Map中将job移除，否则，该值将不能代表该时刻正在运行的Job个数
     * @return
     * 返回正在运行的Job个数
     */
    public int getJobCount()
    {
        return upJobMap.size();
    }

    /**
     * 启动所有的线程
     */
    public void runAllJobs()
    {
        for (Map.Entry<StoreKey, UploadJob> upJobEntry : upJobMap.entrySet())
        {
            upJobEntry.getValue().run();
        }
    }

    /**
     * 分发任务到线程，并启动线程
     * @param workLoadBeans  任务列表
     */
    public void dispatchAndRun(Set<WorkLoadBean> workLoadBeans, WorkloadCompleteIntf workloadComplete)
    {
        if (workLoadBeans == null || workLoadBeans.isEmpty()) {
            return;
        }
        for (WorkLoadBean workLoadBean : workLoadBeans)
        {
            dispatchWorkload(workLoadBean);
        }
        this.workloadComplete = workloadComplete;
        runAllJobs();

        synchronized (mainThreadSingal) {
            try {
                mainThreadSingal.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeUpJob(UploadJob uploadJob)
    {
        StoreKey storeKeySelected = null;
        synchronized (upJobMap) {
            for (Map.Entry<StoreKey, UploadJob> entry : upJobMap.entrySet()) {
                if (entry.getValue() == uploadJob) {
                    storeKeySelected = entry.getKey();
                    break;
                }
            }
            upJobMap.remove(storeKeySelected);
        }

        synchronized (mainThreadSingal) {
            // 任务队列为空时，通知主线程结束
            if (upJobMap.isEmpty()) {
                mainThreadSingal.notify();
            }
        }
    }

    public class JobStateCb
    {
        public void onJobDone(UploadJob uploadJob)
        {
            removeUpJob(uploadJob);
        }

        public void onWorkloadComplete(WorkLoadBean workLoadBean)
        {
            workloadComplete.onComplete(workLoadBean);
        }
    }
}
