package com.voyageone.task2.cms.service;

import com.voyageone.common.ImageServer;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.daoext.cms.CmsBtImagesDaoExt;
import com.voyageone.service.model.cms.CmsBtImagesModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author james.li on 2016/1/6.
 * @version 2.0.0
 */
@Service
public class CmsImagePostScene7Service extends BaseCronTaskService {

    @Autowired
    private CmsBtImagesDaoExt cmsBtImagesDaoExt;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsImagePostScene7Job";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        for (TaskControlBean taskControl : taskControlList) {
            if ("order_channel_id".equalsIgnoreCase(taskControl.getCfg_name())) {
                String channelId = taskControl.getCfg_val1();
                $info("渠道"+channelId);
                CmsBtImagesModel feedImage = new CmsBtImagesModel();
                feedImage.setUpdFlg(0);
                feedImage.setChannelId(channelId);

                ExecutorService es  = Executors.newFixedThreadPool(5);
                try {
                    List<CmsBtImagesModel> imageUrlList;

                    // 获得该渠道要上传Scene7的图片url列表
                    feedImage.setUpdFlg(4);
                    imageUrlList = cmsBtImagesDaoExt.selectImages(feedImage);
                    if(imageUrlList.size() == 0) {
                        feedImage.setUpdFlg(0);
                        imageUrlList.addAll(cmsBtImagesDaoExt.selectImages(feedImage));
                    }
                    $debug(channelId + String.format("渠道本次有%d要推送NEXCESS图片服务器的图片", imageUrlList.size()));
                    if (!imageUrlList.isEmpty()) {

                        // 上传图片到图片服务器
                        // TODO: 16/5/9 等待梁兄ftp访问的共通方法
                        List<List<CmsBtImagesModel>> imageSplitList = CommonUtil.splitList(imageUrlList,10);

                        for (List<CmsBtImagesModel> subImageUrlList :imageSplitList ){
                            es.execute(() -> ImageGetAndSendTask(channelId, subImageUrlList));
                        }
                        es.shutdown();
                        es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

                    } else {
                        $debug(channelId + "渠道本次没有要推送scene7的图片");
                    }

                } catch (Exception ex) {
                    $error(ex.getMessage(), ex);
                    issueLog.log(ex, ErrorType.BatchJob, SubSystem.CMS);
                }
            }
        }
    }

    private String ImageGetAndSendTask(String orderChannelId, List<CmsBtImagesModel> subImageUrlList) {

        long threadNo =  Thread.currentThread().getId();

        $info("thread-" + threadNo + " start");

        //  成功处理的图片url列表
        List<CmsBtImagesModel> subSuccessImageUrlList = new ArrayList<>();
        List<CmsBtImagesModel> urlErrorList = new ArrayList<>();
        boolean isSuccess = false;
        try {
            isSuccess = getAndSendImage(orderChannelId, subImageUrlList, subSuccessImageUrlList, urlErrorList, threadNo);
        } catch (Exception ignored) {

        }

        if (isSuccess) {
            $info(orderChannelId + "渠道本次推送scene7图片任务thread-" + threadNo + "成功");
        } else {
            $info(orderChannelId + "渠道本次推送scene7图片任务thread-" + threadNo + "有错误发生");
        }

        // 已上传成功图片处理标志置位
        int returnValue = 0;
        if (subSuccessImageUrlList.size() > 0) {
            try {
//                imageTemplate(orderChannelId,subSuccessImageUrlList);
                subSuccessImageUrlList.forEach(CmsBtImagesModel -> {
                    CmsBtImagesModel.setUpdFlg(1);
                    CmsBtImagesModel.setModifier(getTaskName());
                    cmsBtImagesDaoExt.updateImage(CmsBtImagesModel);
                });
            } catch (Exception e) {
                e.printStackTrace();
                issueLog.log(e,ErrorType.BatchJob,SubSystem.CMS);
            }
        }

        if (urlErrorList.size() > 0) {
            urlErrorList.forEach(CmsBtImagesModel -> {
                CmsBtImagesModel.setUpdFlg(3);
                CmsBtImagesModel.setModifier(getTaskName());
                cmsBtImagesDaoExt.updateImage(CmsBtImagesModel);
            });
        }

        return "thread-" + threadNo + "上传scene7图片成功个数：" + subSuccessImageUrlList.size() +
                System.lineSeparator() + "已上传成功图片处理标志置位成功个数：" + returnValue +
                System.lineSeparator() + "图片URL错误个数：" + urlErrorList.size();
    }

    public boolean getAndSendImage(String orderChannelId,
                                   List<CmsBtImagesModel> imageUrlList,
                                   List<CmsBtImagesModel> successImageUrlList,
                                   List<CmsBtImagesModel> urlErrorList,
                                   long threadNo) {
        boolean isSuccess = true;

        if (imageUrlList != null && imageUrlList.size() > 0) {

            String imageUrl = null;
            try {
                for (CmsBtImagesModel imagesModel : imageUrlList) {
                    imageUrl = imagesModel.getOriginalUrl();

                    if (StringUtils.isNullOrBlank2(imageUrl)) {
                        successImageUrlList.add(imagesModel);
                        continue;
                    }

                    if(usingHttps(orderChannelId)){
                        imageUrl = imageUrl.replace("https","http");
                    }

                    $info("thread-" + threadNo + ":" + imageUrl + "流取得开始");

                    URL url = new URL(imageUrl);
                    try (InputStream stream = url.openStream()) {
                        String fileName = imagesModel.getImgName();
                        $info("thread-" + threadNo + ":" + imageUrl + "http上传开始");
                        ImageServer.uploadImage(orderChannelId, fileName, stream);
                    } catch (Exception ex) {
                        // 图片url错误
                        $error("上传图片到 imageServer 时出现了错误", ex);
                        urlErrorList.add(imagesModel);
                        continue;
                    }

                    successImageUrlList.add(imagesModel);
                    $info("thread-" + threadNo + ":" + imageUrl + "上传成功!");
                }
            } catch (Exception ex) {
                $info("thread-" + threadNo + ":" + imageUrl + "上传失败!");
                $error(ex.getMessage(), ex);
                issueLog.log(ex, ErrorType.BatchJob, SubSystem.CMS);
                isSuccess = false;
            }
        }

        return isSuccess;
    }

    private boolean usingHttps(String channel) {
        return ChannelConfigEnums.Channel.Modotex.getId().equalsIgnoreCase(channel) ||
                ChannelConfigEnums.Channel.WMF.getId().equalsIgnoreCase(channel);
    }
}

