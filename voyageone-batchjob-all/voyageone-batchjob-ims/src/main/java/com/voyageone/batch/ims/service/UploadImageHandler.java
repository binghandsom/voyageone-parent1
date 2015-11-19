package com.voyageone.batch.ims.service;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Picture;
import com.taobao.api.response.PictureUploadResponse;
import com.voyageone.batch.Context;
import com.voyageone.batch.ims.bean.UploadImageParam;
import com.voyageone.batch.ims.bean.UploadImageResult;
import com.voyageone.batch.ims.bean.tcb.*;
import com.voyageone.batch.ims.dao.PlatformImageUrlMappingDao;
import com.voyageone.batch.ims.enums.TmallWorkloadStatus;
import com.voyageone.batch.ims.modelbean.ImageUrlMappingModel;
import com.voyageone.batch.ims.modelbean.WorkLoadBean;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.tmall.TbPictureService;
import com.voyageone.common.configs.beans.ShopBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Created by Leo on 2015/5/28.
 */
public class UploadImageHandler extends UploadWorkloadHandler {
    private static Log logger = LogFactory.getLog(UploadProductHandler.class);
    private UploadJob uploadJob;
    private TbPictureService tbPictureService;

    private PlatformImageUrlMappingDao imageUrlMappingDao;

    public UploadImageHandler(UploadJob uploadJob, IssueLog issueLog) {
        super(issueLog);
        this.uploadJob = uploadJob;
        tbPictureService = new TbPictureService();

        ApplicationContext springContext = (ApplicationContext) Context.getContext().getAttribute("springContext");
        imageUrlMappingDao = springContext.getBean(PlatformImageUrlMappingDao.class);

        this.setName(this.getClass().getSimpleName() + "_" + uploadJob.getChannel_id() + "_" + uploadJob.getCart_id());
    }

    //添加一个suspend的job防止该线程一启动，就结束
    public void startJob()
    {
        TaskControlBlock suspendTcb = new TaskControlBlock();
        addSuspendTask(suspendTcb);
        start();
    }

    @Override
    protected void doJob(TaskControlBlock tcb) {
        UploadImageTcb uploadImageTcb = (UploadImageTcb) tcb;
        UploadImageParam uploadImageParam = uploadImageTcb.getUploadImageParam();

        logger.debug(String.format("Start uploadImage Job(%s): %s", uploadImageTcb.getUploadProductTcb().getWorkLoadBean().getModelId(), uploadImageParam));

        uploadImage(tcb);
        logger.debug("after uploadImage");
        stopCurrentTcb();
        logger.debug("after stopCurrentTcb");

        UploadImageResult uploadImageResult = uploadImageTcb.getUploadImageResult();
        logger.debug(String.format("End uploadImage Job(%s): %s", uploadImageTcb.getUploadProductTcb().getWorkLoadBean().getModelId(), uploadImageResult));
        uploadJob.uploadImageComplete(uploadImageTcb);
    }

    public void uploadImage(TaskControlBlock tcb)
    {
        UploadImageTcb uploadImageTcb = (UploadImageTcb) tcb;
        UploadImageParam uploadImageParam = uploadImageTcb.getUploadImageParam();
        Set<String> imageUrlSet = uploadImageParam.getSrcUrlSet();
        ShopBean shopBean = uploadImageParam.getShopBean();


        UploadImageResult uploadImageResult = new UploadImageResult();

        try {
            // add by lewis 2015/11/16 start...
            // check images
            List<ImageUrlMappingModel> imageUrlMappingBeans = imageUrlMappingDao.getImageUrlMap(Integer.valueOf(uploadJob.getCart_id()),uploadJob.getChannel_id());

            Map<String,String> imageUrlMap = new HashMap<>();

            for (ImageUrlMappingModel bean:imageUrlMappingBeans){
                imageUrlMap.put(bean.getOrgImageUrl(),bean.getPlatformImageUrl());
            }
            List<ImageUrlMappingModel> imageUrlModels = new ArrayList<>();
            // add by lewis 2015/11/16 end...

            for (String srcUrl : imageUrlSet) {
                String decodeSrcUrl = decodeImageUrl(srcUrl);
                // modified by lewis 2015/11/16 start...
                if(imageUrlMap.get(decodeSrcUrl)==null){
                    String destUrl = uploadImageByUrl(decodeSrcUrl, shopBean);
                    uploadImageResult.add(srcUrl, destUrl);

                    ImageUrlMappingModel imageUrlInfo = new ImageUrlMappingModel();
                    imageUrlInfo.setCartId(Integer.valueOf(uploadJob.getCart_id()));
                    imageUrlInfo.setChannelID(uploadJob.getChannel_id());
                    imageUrlInfo.setOrgImageUrl(decodeSrcUrl);
                    imageUrlInfo.setPlatformImageUrl(destUrl);
                    imageUrlInfo.setCreater("uploadProductJob");
                    imageUrlInfo.setModifier("uploadProductJob");
                    imageUrlModels.add(imageUrlInfo);
                }else {
                    uploadImageResult.add(srcUrl, imageUrlMap.get(decodeSrcUrl));
                }
                // modified by lewis 2015/11/16 end...
            }

            // add by lewis 2015/11/16 start...
            if(imageUrlModels.size()>0)
            {
                //insert image url
                imageUrlMappingDao.insertPlatformSkuInfo(imageUrlModels);
            }
            // add by lewis 2015/11/16 end...

            uploadImageResult.setUploadSuccess(true);
        } catch (TaskSignal taskSignal) {
            String failCause;
            uploadImageResult.setUploadSuccess(false);

            if (taskSignal.getSignalType() != TaskSignalType.ABORT) {
                failCause = "状态错误，上传图片结束后收到的taskSignal只能时AbortSignal";
                uploadImageResult.setNextProcess(false);
            } else {
                AbortTaskSignalInfo abortTaskSignalInfo = (AbortTaskSignalInfo)taskSignal.getSignalInfo();
                if (abortTaskSignalInfo.isProcessNextTime())  {
                    uploadImageResult.setNextProcess(true);
                } else {
                    uploadImageResult.setNextProcess(false);
                }
                failCause = abortTaskSignalInfo.getAbortCause();
            }
            uploadImageResult.setFailCause(failCause);
            logger.error("Fail to upload image: " + failCause);
        }

        uploadImageTcb.setUploadImageResult(uploadImageResult);
        uploadImageTcb.getUploadProductTcb().setUploadImageResult(uploadImageResult);
        updateWorkloadStatus(uploadImageTcb.getUploadProductTcb().getWorkLoadBean());
    }

    private void updateWorkloadStatus(WorkLoadBean workLoadBean)
    {
        TmallWorkloadStatus tmallWorkloadStatus = (TmallWorkloadStatus)workLoadBean.getWorkload_status();
        switch (tmallWorkloadStatus.getValue())
        {
            case TmallWorkloadStatus.ADD_WAIT_PRODUCT_PIC:
                workLoadBean.setWorkload_status(new TmallWorkloadStatus(TmallWorkloadStatus.ADD_PRODUCT_PIC_UPLOADED));
                break;
            case TmallWorkloadStatus.ADD_WAIT_ITEM_PIC:
                workLoadBean.setWorkload_status(new TmallWorkloadStatus(TmallWorkloadStatus.ADD_ITEM_PIC_UPLOADED));
                break;
            case TmallWorkloadStatus.UPDATE_WAIT_ITEM_PIC:
                workLoadBean.setWorkload_status(new TmallWorkloadStatus(TmallWorkloadStatus.UPDATE_ITEM_PIC_UPLOADED));
                break;
        }
    }
    public String uploadImageByUrl(String url, ShopBean shopBean) throws TaskSignal {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int TIMEOUT_TIME = 10*1000;
        int waitTime = 0;
        int retry_times = 0;
        int max_retry_times = 3;
        InputStream is = null;
        do {
            try {
                URL imgUrl = new URL(url);
                is = imgUrl.openStream();
                byte[] byte_buf = new byte[1024];
                int readBytes = 0;

                while (true) {
                    while (is.available() >= 0) {
                        readBytes = is.read(byte_buf, 0, 1024);
                        if (readBytes < 0)
                            break;
                        logger.debug("read " + readBytes + " bytes");
                        waitTime = 0;
                        baos.write(byte_buf, 0, readBytes);
                    }
                    if (readBytes < 0)
                        break;

                    Thread.sleep(1000);
                    waitTime += 1000;

                    if (waitTime >= TIMEOUT_TIME) {
                        logger.error("fail to download image:" + url);
                        return null;
                    }
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                logger.error("exception when upload image", e);
                if ("Connection reset".equals(e.getMessage())) {
                    if (++retry_times < max_retry_times)
                        continue;
                }
                throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(String.format("Fail to upload image[%s]: %s", url, e.getMessage())));
            }
            break;
        } while (true);

        logger.info("read complete, begin to upload image");

        //上传到天猫
        String pictureUrl = null;
        try {
            logger.info("upload image, wait Tmall response...");
            PictureUploadResponse pictureUploadResponse = tbPictureService.uploadPicture(shopBean, baos.toByteArray(), "image_title", "0");
            logger.info("response comes");
            if (pictureUploadResponse == null) {
                String failCause = "上传图片到天猫时，超时, tmall response为空";
                logger.error(failCause);
                throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause, true));
            } else if (pictureUploadResponse.getErrorCode() != null) {
                String failCause = "上传图片到天猫时，错误:" + pictureUploadResponse.getErrorCode() + ", " + pictureUploadResponse.getMsg();
                    logger.error(failCause);
                    logger.error("上传图片到天猫时，sub错误:" + pictureUploadResponse.getSubCode() + ", " + pictureUploadResponse.getSubMsg());
                throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause));
            }
            Picture picture = pictureUploadResponse.getPicture();
            if (picture != null)
                pictureUrl = picture.getPicturePath();
        } catch (ApiException e) {
            String failCause = "上传图片到天猫国际时出错！ msg:" + e.getMessage();
            logIssue(failCause);
            logger.error("errCode: " + e.getErrCode());
            logger.error("errMsg: " + e.getErrMsg());
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause, false));
        }
        logger.info(String.format("Success to upload image[%s -> %s]", url, pictureUrl));

        return pictureUrl;
    }

    @Override
    protected void abortTcb(TaskControlBlock currentTcb, RuntimeException re) {
        super.abortTcb(currentTcb, re);
        UploadImageTcb tcb = (UploadImageTcb) currentTcb;
        UploadProductTcb uploadProductTcb = tcb.getUploadProductTcb();

        tcb.setUploadProductTcb(null);
        if (uploadProductTcb != null) {
            uploadJob.getUploadProductHandler().stopTcb(uploadProductTcb);
        }
    }

    public static void main(String[] args) {
        String plain = "http://s7d5.scene7.com/is/image/sneakerhead/bcbg_1200_1200?$1200x1200$&$big=IZD1U885_001";
        String encode = encodeImageUrl(plain);
        System.out.println(encode);
        System.out.println(decodeImageUrl(encode));
    }

    public static String encodeImageUrl(String plainValue) {
        String endStr = "%&";
        if (!plainValue.endsWith(endStr))
            return plainValue + endStr;
        return plainValue;
    }

    public static String decodeImageUrl(String encodedValue) {
        return encodedValue.substring(0, encodedValue.length() - 2);
    }
}
