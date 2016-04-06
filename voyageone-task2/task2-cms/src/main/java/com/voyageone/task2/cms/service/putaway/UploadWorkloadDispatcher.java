package com.voyageone.task2.cms.service.putaway;

import com.voyageone.service.dao.cms.mongo.CmsMtPlatformMappingDao;
import com.voyageone.task2.cms.bean.WorkLoadBean;
import com.voyageone.task2.cms.dao.SkuInventoryDao;
import com.voyageone.common.components.issueLog.IssueLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * 上传任务分为两个线程handler，一个是上传商品handler，一个是上传图片handler
 * 该类会根据任务的状态，交由相应的线程handler来处理
 * Created by Leo on 2015/5/28.
 */
@Repository
public class UploadWorkloadDispatcher {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<StoreKey, List<UploadJob>> upJobMap;
    private int jobCountPerCartChannel;
    private final static int DEFAULT_JOB_COUNT_PER_CART_CHANNEL = 3;

    //Job回调函数
    private WorkloadCompleteIntf workloadComplete;

    //任务各状态回调函数
    private JobStateCb jobStateCb;

    //用于向主线程发送信号
    private final Object mainThreadSingal;

    @Autowired
    private CmsMtPlatformMappingDao cmsMtPlatformMappingDao;
    @Autowired
    private SkuInventoryDao skuInventoryDao;
    @Autowired
    private IssueLog issueLog;

    public UploadWorkloadDispatcher() {
        this(DEFAULT_JOB_COUNT_PER_CART_CHANNEL);
    }

    public UploadWorkloadDispatcher(int jobCountPerCartChannel)
    {
        upJobMap = new HashMap<>();
        jobStateCb = new JobStateCb();
        mainThreadSingal = new Object();
        this.jobCountPerCartChannel = jobCountPerCartChannel;
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
   public void dispatchWorkload(WorkLoadBean workLoadBean) {
       StoreKey storeKey;
       storeKey = new StoreKey(workLoadBean.getOrder_channel_id(), workLoadBean.getCart_id());
       List<UploadJob> uploadJobs = upJobMap.get(storeKey);
       synchronized (upJobMap) {
           if (uploadJobs == null) {
               uploadJobs = new ArrayList<>();
               upJobMap.put(storeKey, uploadJobs);
           }
           if (uploadJobs.size() < jobCountPerCartChannel) {
               UploadJob uploadJob = new UploadJob(workLoadBean.getOrder_channel_id(), workLoadBean.getCart_id(), String.valueOf(uploadJobs.size()),
                       jobStateCb, cmsMtPlatformMappingDao, skuInventoryDao, issueLog);

               uploadJobs.add(uploadJob);
               uploadJob.addWorkLoad(workLoadBean);
           }
           else {
               uploadJobs.get(0).addWorkLoad(workLoadBean);
               Collections.sort(uploadJobs);
           }
       }
       logger.debug("add workload to UploadProductHandler, storeKey:" + storeKey + ", workloadBean:" + workLoadBean);
   }

    /**
     * 当某job结束时，必须在回调函数中从Map中将job移除，否则，该值将不能代表该时刻正在运行的Job个数
     * @return
     * 返回正在运行的Job个数
     */
    public int getJobCount()
    {
        int jobCount = 0;
        for (Map.Entry<StoreKey, List<UploadJob>> upJobEntry : upJobMap.entrySet()) {
            List<UploadJob> uploadJobs = upJobEntry.getValue();
            jobCount += uploadJobs.size();
        }
        return jobCount;
    }

    /**
     * 启动所有的线程
     */
    public void runAllJobs()
    {
        synchronized (upJobMap) {
            for (Map.Entry<StoreKey, List<UploadJob>> upJobEntry : upJobMap.entrySet()) {
                List<UploadJob> uploadJobs = upJobEntry.getValue();
                for (UploadJob uploadJob : uploadJobs) {
                    uploadJob.run();
                }
            }
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
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void removeUpJob(UploadJob uploadJob)
    {
        StoreKey storeKeySelected = null;
        synchronized (upJobMap) {
            for (Map.Entry<StoreKey, List<UploadJob>> entry : upJobMap.entrySet()) {
                 List<UploadJob> uploadJobs = entry.getValue();
                if (uploadJobs.contains(uploadJob)) {
                    uploadJobs.remove(uploadJob);
                    if (uploadJobs.isEmpty()) {
                        storeKeySelected = entry.getKey();
                    }
                    break;
                }
            }
            if (storeKeySelected != null) {
                upJobMap.remove(storeKeySelected);
            }
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
