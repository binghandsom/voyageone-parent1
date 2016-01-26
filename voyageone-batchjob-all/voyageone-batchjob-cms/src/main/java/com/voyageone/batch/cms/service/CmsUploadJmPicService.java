package com.voyageone.batch.cms.service;

import com.sun.org.apache.xpath.internal.SourceTree;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.bean.JmPicBean;
import com.voyageone.batch.cms.dao.ImageDao;
import com.voyageone.batch.cms.dao.JmPicDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.xml.rpc.ServiceException;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author james.li on 2016/1/25.
 * @version 2.0.0
 */
@Service
public class CmsUploadJmPicService extends BaseTaskService {

    /* 线程总数 */
    private static final int THREAD_COUNT=10;

    /* 调用聚美Api相同是否替换 */
    private static final boolean NEED_REPLACE=true;

    /* 聚美dir斜杠分隔符 */
    private static final String SLASH="/";

    @Autowired
    private JmPicDao jmPicDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsUploadJmPicJob";
    }

    /**
     * 启动多线程，上传图片
     * @param taskControlList job 配置
     * @throws Exception
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        List<Map<String, String>> jmpickeys= jmPicDao.getJmPicImageKeyGroup();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        ShopBean shopBean = ShopConfigs.getShop(ChannelConfigEnums.Channel.SN.getId(), CartEnums.Cart.JM.getId());
        for (Map<String,String> picMap:jmpickeys){
            String imageKey=picMap.get("imageKey");
            if(StringUtils.isEmpty(imageKey))
                continue;
            executor.execute(new UploadTask(imageKey,shopBean));
        }
        executor.shutdown();
        while (!executor.isTerminated())
            Thread.sleep(1000);
    }

    /**
     * 上传任务
     */
    private class UploadTask implements Runnable{
        @Autowired
        private JumeiImageFileService jumeiImageFileService;

        /** image_key (product_code Or brand) */
        private String imageKey;

        private ShopBean shopBean;

        @Override
        public void run() {
            try{
                List<JmPicBean> jmPicBeanList=jmPicDao.getJmPicsByImgKey(imageKey);
                for (JmPicBean jmPicBean:jmPicBeanList){
                    //String juUrl=mockImageFileUpload(shopBean,convertJmPicToImageFileBean(jmPicBean));
                    String juUrl=jumeiImageFileService.imageFileUpload(shopBean,convertJmPicToImageFileBean(jmPicBean));
                    jmPicDao.updateJmpicUploaded(juUrl,jmPicBean.getSeq());
                }
                jmPicDao.updateJmProductImportUploaded(imageKey);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        public UploadTask(String imageKey,ShopBean shopBean) {
            this.imageKey = imageKey;
            this.shopBean = shopBean;
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
    private static String mockImageFileUpload(ShopBean shopBean, JmImageFileBean fileBean)throws IOException {
        /* 聚美dir斜杠分隔符 */
        final String SLASH="/";
        File file=new File("F:/jumei"+fileBean.getDirName());
        if(!file.exists()){
            file.mkdirs();
        }
        InputStream inputStream=fileBean.getInputStream();
        String imgPath="F:/jumei"+fileBean.getDirName()+SLASH+fileBean.getImgName();
        FileOutputStream fileOutputStream = new FileOutputStream(new File(imgPath));
        byte[] b = new byte[1024];
        while((inputStream.read(b)) != -1){
            fileOutputStream.write(b);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        inputStream.close();
        return imgPath;
    }

    /**
     * 转化bean
     * @param jmPicBean jmPicBean
     * @return JmImageFileBean
     * @throws IOException
     */
    private static JmImageFileBean convertJmPicToImageFileBean(JmPicBean jmPicBean) throws IOException {
        JmImageFileBean jmImageFileBean=new JmImageFileBean();
        File imageFile=new File(jmPicBean.getOriginUrl());
        jmImageFileBean.setInputStream(HttpUtils.getInputStream(jmPicBean.getOriginUrl(),null));
        jmImageFileBean.setDirName(buildDirName(jmPicBean));
        jmImageFileBean.setImgName(imageFile.getName());
        jmImageFileBean.setNeedReplace(NEED_REPLACE);
        return jmImageFileBean;
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

}
