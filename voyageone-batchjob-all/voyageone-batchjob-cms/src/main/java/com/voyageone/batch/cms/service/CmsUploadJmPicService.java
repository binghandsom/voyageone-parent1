package com.voyageone.batch.cms.service;

import com.mysql.jdbc.log.LogFactory;
import com.sun.org.apache.xpath.internal.SourceTree;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.bean.JmPicBean;
import com.voyageone.batch.cms.dao.ImageDao;
import com.voyageone.batch.cms.dao.JmPicDao;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.jumei.Bean.JmImageFileBean;
import com.voyageone.common.components.jumei.JumeiImageFileService;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.beans.ShopConfigBean;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.StringUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;
import javax.xml.rpc.ServiceException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author james.li on 2016/1/25.
 * @version 2.0.0
 */
@Service
public class CmsUploadJmPicService extends BaseTaskService {

    /* slf4j Log */
    private static final Logger LOG= LoggerFactory.getLogger(CmsUploadJmPicService.class);

    /* 线程总数 */
    /*private static final int THREAD_COUNT=10;*/

    /* 调用聚美Api相同是否替换 */
    private static final boolean NEED_REPLACE=true;

    /* 聚美dir斜杠分隔符 */
    private static final String SLASH="/";

    /* 获取图片下载流重试次数 */
    private static final int GET_IMG_INPUTSTREAM_RETRY=5;

    /* SHOPBEAN */
    private static final ShopBean SHOPBEAN = ShopConfigs.getShop(ChannelConfigEnums.Channel.SN.getId(), CartEnums.Cart.JM.getId());

    /* 图片后缀 */
    private static final String IMGTYPE=".jpg";

    @Autowired
    private JmPicDao jmPicDao;

    @Autowired
    private JumeiImageFileService jumeiImageFileService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsUploadJmPicJob";
    }

    /* 监控上传 */
    private MonitorUpload monitor=null;

    /**
     * 启动多线程，上传图片
     * @param taskControlList job 配置
     * @throws Exception
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        monitor=new MonitorUpload();
        monitor.setTaskStart();
        List<Map<String, Object>> jmpickeys= jmPicDao.getJmPicImageKeyGroup();
        monitor.setImageKeyCountMap(jmpickeys);
        monitor.setTaskName(getTaskName());
        //monitor.setThreadCount(THREAD_COUNT);
        List<Runnable> threads = new ArrayList<>();
        for (Map<String,Object> picMap:jmpickeys){
            String imageKey=picMap.get("imageKey").toString();
            if(StringUtils.isEmpty(imageKey))
                continue;
            threads.add(new UploadTask(imageKey));
        }
        runWithThreadPool(threads, taskControlList);
        monitor.setTaskEnd();
        LOG.info(monitor.toString());
    }

    /**
     * 上传任务
     */
    private class UploadTask implements Runnable{

        /** image_key (product_code Or brand) */
        private String imageKey;

        @Override
        public void run() {
            List<JmPicBean> jmPicBeanList=jmPicDao.getJmPicsByImgKey(imageKey);
            if(CollectionUtils.isEmpty(jmPicBeanList)){
                LOG.warn("UploadTask -> run() -> jmPicBeanList为空");
            }else{
                boolean noError=true;
                for (JmPicBean jmPicBean:jmPicBeanList){
                    try {
                        String juUrl=mockImageFileUpload(SHOPBEAN,convertJmPicToImageFileBean(jmPicBean));
                        //String juUrl= jumeiImageFileService.imageFileUpload(SHOPBEAN,convertJmPicToImageFileBean(jmPicBean));
                        jmPicDao.updateJmpicUploaded(juUrl,jmPicBean.getSeq(),getTaskName());
                        monitor.addSuccsseOne();
                    } catch (Exception e) {
                        noError=false;
                        monitor.addErrorOne();
                        LOG.error("UploadTask -> run() -> exception:"+e);
                    }
                }
                if(noError){
                    jmPicDao.updateJmProductImportUploaded(imageKey,getTaskName());
                    monitor.addProductSucessOne();
                }
            }
        }
        public UploadTask(String imageKey) {
            this.imageKey = imageKey;
        }
    }

    /**
     * MOCKTest
     * mock 图片上传
     * @param shopBean shopBean
     * @param fileBean fileBean
     * @return jm_url
     * @throws IOException
     */
    private static String mockImageFileUpload(ShopBean shopBean, JmImageFileBean fileBean) throws IOException {
        File file=new File("F:/jumei"+fileBean.getDirName());
        if(!file.exists()){
            file.mkdirs();
        }
        try {
            InputStream inputStream=fileBean.getInputStream();
            Assert.notNull(inputStream);
            String imgPath="F:/jumei"+fileBean.getDirName()+SLASH+fileBean.getImgName();
            FileUtils.copyInputStreamToFile(fileBean.getInputStream(),new File(imgPath));
            return imgPath;
        } catch (IOException e) {
            LOG.error("CmsUploadJmPicService -> mockImageFileUpload() Error:"+e);
            return null;
        }
    }

    /**
     * 转化bean
     * @param jmPicBean jmPicBean
     * @return JmImageFileBean
     */
    private static JmImageFileBean convertJmPicToImageFileBean(JmPicBean jmPicBean) {
        try {
            JmImageFileBean jmImageFileBean=new JmImageFileBean();
//            File imageFile=new File(jmPicBean.getOriginUrl());
            int retryCount=GET_IMG_INPUTSTREAM_RETRY;
            InputStream inputStream = getImgInputStream(jmPicBean.getOriginUrl(),retryCount);
            Assert.notNull(inputStream,"inputStream为null，图片流获取失败！"+jmPicBean.getOriginUrl());
            jmImageFileBean.setInputStream(inputStream);
            jmImageFileBean.setDirName(buildDirName(jmPicBean));
            jmImageFileBean.setImgName(jmPicBean.getImageKey()+jmPicBean.getImageType()+jmPicBean.getImageIndex()+IMGTYPE);
            jmImageFileBean.setNeedReplace(NEED_REPLACE);
            return jmImageFileBean;
        } catch (Exception e) {
            LOG.error("CmsUploadJmPicService -> convertJmPicToImageFileBean() Error:"+e);
            return null;
        }
    }

    /**
     * 获取网络图片流，遇错重试
     * @param url imgUrl
     * @param retry retrycount
     * @return inputStream
     */
    private static InputStream getImgInputStream(String url,int retry){
        if(retry-->0){
            try {
                return HttpUtils.getInputStream(url,null);
            } catch (Exception e) {
                getImgInputStream(url,retry);
            }
        }
        return null;
    }

    /***
     * 按照规则构造远程路径
     * @param jmPicBean jmPicBean
     * @return 远程dir
     */
    private static String buildDirName(JmPicBean jmPicBean){
        Assert.notNull(jmPicBean);
        checkFiled(jmPicBean.getChannelId(),jmPicBean.getImageKey());
        if(jmPicBean.getImageType()<=3){
            return SLASH+jmPicBean.getChannelId()+SLASH+jmPicBean.getPicType()+SLASH+jmPicBean.getImageType()+SLASH+jmPicBean.getImageKey().replace(SLASH,"_");
        }else if(jmPicBean.getImageType()<=5){
            return SLASH+jmPicBean.getChannelId()+SLASH+jmPicBean.getPicType()+SLASH+jmPicBean.getImageKey().replace(SLASH,"_")+SLASH+jmPicBean.getImageType();
        }
        return SLASH+jmPicBean.getChannelId()+SLASH+"channel";
    }

    /**
     * 校验
     * @param fileds fileds
     */
    private static void checkFiled(String... fileds){
        for (String filed:fileds){
            if(StringUtils.isEmpty(filed)){
                throw new IllegalArgumentException("参数校验不通过filed:"+filed);
            }
        }
    }

    /**
     * 监控上传任务
     */
    private class MonitorUpload{

        /* 任务查询的imageKey,count集合 */
        private Map<String,Integer> imageKeyCountMap=new HashMap<String,Integer>();

        /* 成功上传的图片总数 */
        private Integer successUploadCount=0;

        /* 失败的图片总数 */
        private Integer errorUploadCount=0;

        /* 图片全部上传完成的产品总数 */
        private Integer productSuccessCount=0;

        /* 任务名称 */
        private String taskName;

        /* 任务线程数 */
        private Integer threadCount;

        /* 任务启动时间 */
        private long taskStartTime;

        /* 任务结束时间 */
        private long taskEndTime;

        /* 设置imageKeyCount */
        public void setImageKeyCountMap(List<Map<String, Object>> jmpickeys) {
            for(Map<String,Object> map:jmpickeys){
                imageKeyCountMap.put(map.get("imageKey").toString(),Integer.parseInt(map.get("count").toString()));
            }
        }

        /**
         * 本次需要上传的图片总数，从imageKeyCountMap统计得到
         * 不应支持外部修改
         */
        public Integer getNeedUploadCount() {
            Assert.notNull(imageKeyCountMap);
            int needUploadCount=0;
            for(Map.Entry<String,Integer> entry: imageKeyCountMap.entrySet()){
                needUploadCount+=entry.getValue();
            }
            return needUploadCount;
        }

        public synchronized void addSuccsseOne() {
            this.successUploadCount+=1;
        }

        public synchronized void addErrorOne() {
            this.errorUploadCount +=1;
        }

        public synchronized void addProductSucessOne(){this.productSuccessCount+=1;}

        public Integer getProductFailedCount(){return imageKeyCountMap.size()-this.productSuccessCount;}

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        /* 任务耗时 */
        public long getTaskUsedTime() {
            return taskEndTime-taskStartTime;
        }

        /* 私有方法，提供内部方法记录用*/
        private Date getRecordTime() {
            return new Date();
        }

        public void setTaskStart() {
            this.taskStartTime = System.currentTimeMillis();
        }

        public void setTaskEnd() {
            this.taskEndTime = System.currentTimeMillis();
        }

        public void setThreadCount(Integer threadCount) {
            this.threadCount = threadCount;
        }

        /**
         * 重写toString方法
         * @return string 监控结果描述
         */
        public String toString(){
            return "\n【"+taskName+"】任务耗时:"+getTaskUsedTime()+",需要上传图片"+getNeedUploadCount()+"个,实际上传"+successUploadCount+"个,失败上传"+errorUploadCount
                    +"个\n已完成图片上传的产品总数："+productSuccessCount+"\t未完全上传完图片的产品总数："+getProductFailedCount()
                    +"\nImgKeyMap详细信息->总数:"+imageKeyCountMap.size()+"\tDataMap:"+imageKeyCountMap
                    +"\n\t\t****recordTime："+getRecordTime()+" end****";
        }
    }
}
