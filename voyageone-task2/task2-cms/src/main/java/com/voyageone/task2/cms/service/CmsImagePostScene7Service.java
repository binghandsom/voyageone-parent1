package com.voyageone.task2.cms.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.FtpUtil;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.daoext.cms.CmsBtImagesDaoExt;
import com.voyageone.service.model.cms.CmsBtImagesModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
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
public class CmsImagePostScene7Service extends BaseTaskService {

    // Scene7FTP设置
    private static final String S7FTP_CONFIG = "S7FTP_CONFIG";

    @Autowired
    CmsBtImagesDaoExt cmsBtImagesDaoExt;
//    @Autowired
//    ImagesService imagesService;

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

                ExecutorService es  = Executors.newFixedThreadPool(10);
                try {
                    // 获得该渠道要上传Scene7的图片url列表
                    List<CmsBtImagesModel> imageUrlList = cmsBtImagesDaoExt.selectImages(feedImage);
                    $info(channelId + String.format("渠道本次有%d要推送scene7的图片", imageUrlList.size()));
                    if (!imageUrlList.isEmpty()) {
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
        List<CmsBtImagesModel> subSuccessImageUrlList = new ArrayList<CmsBtImagesModel>();
        List<CmsBtImagesModel> urlErrorList = new ArrayList<CmsBtImagesModel>();
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

            subSuccessImageUrlList.forEach(CmsBtImagesModel -> {
                CmsBtImagesModel.setUpdFlg(1);
                CmsBtImagesModel.setModifier(getTaskName());
                cmsBtImagesDaoExt.updateImage(CmsBtImagesModel);
            });
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

    public boolean getAndSendImage(String orderChannelId, List<CmsBtImagesModel> imageUrlList, List<CmsBtImagesModel> successImageUrlList,
                                   List<CmsBtImagesModel> urlErrorList, long threadNo) throws Exception {
        boolean isSuccess = true;

        if (imageUrlList != null && imageUrlList.size() > 0) {

            InputStream inputStream = null;

            FtpBean ftpBean = new FtpBean();
            // ftp连接port
            String port = Codes.getCodeName(S7FTP_CONFIG, "Port");
            ftpBean.setPort(port);
            // ftp连接url
            String url = Codes.getCodeName(S7FTP_CONFIG, "Url");
            ftpBean.setUrl(url);
            // ftp连接usernmae
            String userName = Codes.getCodeName(S7FTP_CONFIG, "UserName");
            ftpBean.setUsername(userName);
            // ftp连接password
            String password = Codes.getCodeName(S7FTP_CONFIG, "Password");
            ftpBean.setPassword(password);
            // ftp连接上传文件编码
            String fileEncode = Codes.getCodeName(S7FTP_CONFIG, "FileCoding");
            ftpBean.setFile_coding(fileEncode);

            //FTP服务器保存目录设定
            String uploadPath = ChannelConfigs.getVal1(orderChannelId, ChannelConfigEnums.Name.scene7_image_folder);
            if(StringUtils.isEmpty(uploadPath)){
                String err = String.format("channelId(%s)的scene7上的路径没有配置 请配置tm_order_channel_config表",orderChannelId);
                $error(err);
                throw new BusinessException(err);
            }
            ftpBean.setUpload_path(uploadPath);

            FtpUtil ftpUtil = new FtpUtil();
            FTPClient ftpClient = new FTPClient();

            String imageUrl = "";

            try {
                //建立连接
                ftpClient = ftpUtil.linkFtp(ftpBean);
                if (ftpClient != null) {

                    boolean change = ftpClient.changeWorkingDirectory(ftpBean.getUpload_path());
                    if (change) {
                        ftpClient.enterLocalPassiveMode();
                        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                        ftpClient.setConnectTimeout(120000);

                        for (int i = 0; i < imageUrlList.size(); i++) {
                            imageUrl = String.valueOf(imageUrlList.get(i).getOriginalUrl());

                            if (StringUtils.isNullOrBlank2(imageUrl)) {
                                successImageUrlList.add(imageUrlList.get(i));
                                continue;
                            }

                            try {
                                inputStream = HttpUtils.getInputStream(imageUrl);
                            } catch (Exception ex) {
                                // 图片url错误
                                $error(ex.getMessage(), ex);
                                imageUrlList.get(i).setUpdFlg(3);
                                imageUrlList.get(i).setModifier(getTaskName());

                                cmsBtImagesDaoExt.updateImage(imageUrlList.get(i));
                                // 记录url错误图片以便删除这张图片相关记录
//                                urlErrorList.add(imageUrlList.get(i));

                                continue;
                            }

                            int lastSlash = imageUrl.lastIndexOf("/");
                            String fileName = imageUrlList.get(i).getImgName();

                            boolean result = ftpClient.storeFile(fileName, inputStream);

                            if (result) {
                                successImageUrlList.add(imageUrlList.get(i));

                                $info("thread-" + threadNo + ":" + imageUrl + "上传成功!");

                            } else {
                                isSuccess = false;

                                break;
                            }

                            if (inputStream != null) {
                                inputStream.close();
                            }
                        }
                    }
                }

            } catch (Exception ex) {
                $error(ex.getMessage(), ex);
                issueLog.log(ex, ErrorType.BatchJob, SubSystem.CMS);

                isSuccess = false;

            } finally {
                //断开连接
                if (ftpClient != null) {
                    ftpUtil.disconnectFtp(ftpClient);
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }

        return isSuccess;
    }
}

