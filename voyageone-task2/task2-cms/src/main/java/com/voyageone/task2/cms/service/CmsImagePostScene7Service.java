package com.voyageone.task2.cms.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.ImgUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.ftp.FtpComponentFactory;
import com.voyageone.components.ftp.FtpConstants;
import com.voyageone.components.ftp.bean.FtpFileBean;
import com.voyageone.components.ftp.service.BaseFtpComponent;
import com.voyageone.components.imagecreate.bean.ImageCreateAddListRequest;
import com.voyageone.components.imagecreate.bean.ImageCreateAddListResponse;
import com.voyageone.components.imagecreate.service.ImageCreateService;
import com.voyageone.service.bean.openapi.image.CreateImageParameter;
import com.voyageone.service.daoext.cms.CmsBtImagesDaoExt;
import com.voyageone.service.impl.cms.ImageTemplateService;
import com.voyageone.service.impl.cms.PlatformImagesService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtImagesModel;
import com.voyageone.service.model.cms.CmsBtPlatformImagesModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
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

    @Autowired
    private CmsBtImagesDaoExt cmsBtImagesDaoExt;

    @Autowired
    private PlatformImagesService platformImagesService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductGroupService productGroupService;
//    @Autowired
//    ImagesService imagesService;

    @Autowired
    private ImageTemplateService imageTemplateService;

    @Autowired
    private ImageCreateService imageCreateService;

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

                ExecutorService es  = Executors.newFixedThreadPool(3);
                try {
                    // 获得该渠道要上传Scene7的图片url列表
                    List<CmsBtImagesModel> imageUrlList = cmsBtImagesDaoExt.selectImages(feedImage);
                    $info(channelId + String.format("渠道本次有%d要推送NEXCESS图片服务器的图片", imageUrlList.size()));
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

    private void imageTemplate(String channelId , List<CmsBtImagesModel> imageUrlList) throws Exception {
        // 取得CMS系统展示用的产品图片模板
        CmsBtImageTemplateModel commonTemplate = imageTemplateService.getCommonTemplate();

        // 获取该code对应的所有模板
        List<CreateImageParameter> imageDatas = new ArrayList<>();

        for(CmsBtImagesModel image : imageUrlList) {

//            String imageExtend = ImgUtils.getImageExtend(image.getOriginalUrl());
            String imageExtend = ".jpg";
            // CMS显示用共通模板
            CreateImageParameter commonTemplateParameter = new CreateImageParameter();
            commonTemplateParameter.setChannelId(channelId);
            commonTemplateParameter.setTemplateId(commonTemplate.getImageTemplateId());
            commonTemplateParameter.setFile(image.getImgName());
            commonTemplateParameter.setVParam(new String[]{channelId, image.getImgName() + imageExtend});
            imageDatas.add(commonTemplateParameter);

            // 获取所有模板数据
            CmsBtProductModel product = productService.getProductByCode(channelId, image.getCode());
            if(product == null) {
                issueLog.log("图片上传","ChannelId:"+channelId+" Code:" +image.getCode()+"产品数据不存在",ErrorType.BatchJob,SubSystem.CMS);
                continue;
            }
            List<CmsBtImageTemplateModel> templateModels = imageTemplateService.getTemplateListWithNoParams(channelId
                    , product.getFields().getBrand()
                    , product.getFields().getProductType()
                    , product.getFields().getSizeType());

            // 返回需要调用图片生成api的对象
            for (CmsBtImageTemplateModel templateModel : templateModels) {

                CreateImageParameter createImageParameter = new CreateImageParameter();
                createImageParameter.setChannelId(channelId);
                createImageParameter.setTemplateId(templateModel.getImageTemplateId());
                createImageParameter.setFile(ImgUtils.getImageName(image.getImgName()));
                createImageParameter.setVParam(new String[]{image.getImgName() + imageExtend});
                imageDatas.add(createImageParameter);

                // 获取产品对应的group信息
                CmsBtProductGroupModel groupModel = productGroupService.selectProductGroupByCode(channelId, product.getFields().getCode(), templateModel.getCartId());

                // 返回templateImageUrl
                String templateImageUrl = imageTemplateService.getTemplateImageUrl(channelId, templateModel.getImageTemplateId().toString(), image.getImgName() + imageExtend);

                // 将模板图片插入到platformImage
                CmsBtPlatformImagesModel platformImage = platformImagesService.selectByImageNameWithTemplate(channelId, templateModel.getCartId(), image.getImgName(), templateModel.getImageTemplateId());
                platformImage.setSearchId(groupModel.getGroupId().toString());
                platformImage.setOriginalImgUrl(templateImageUrl);
                platformImage.setUpdFlg(0);
                platformImage.setCreater(getTaskName());
                platformImage.setModifier(getTaskName());
                platformImagesService.save(platformImage);

                // 上新
                if (CmsConstants.ProductStatus.Approved.name().equals(product.getFields().getStatus()))
                    productService.insertSxWorkLoad(channelId, product, getTaskName());
            }
        }

        $info("本次生成图片模板的数量: " + imageDatas.size());
        // 按每次可以批量生成图片的最大数拆分图片列表
        List<List<CreateImageParameter>> splitImageDatas = CommonUtil.splitList(imageDatas, 200);

        for (List<CreateImageParameter> imageData : splitImageDatas) {

            // 调用图片生成API
            ImageCreateAddListRequest request = new ImageCreateAddListRequest();
            request.setData(imageData);
            $info("本次处理的图片信息:" + request);
            ImageCreateAddListResponse response = imageCreateService.addList(request);
            if (response.getErrorCode() > 0)
                $error(imageData + " 调用图片生成API失败:" + response.getErrorMsg());
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

            //FTP服务器保存目录设定
            String uploadPath = ChannelConfigs.getVal1(orderChannelId, ChannelConfigEnums.Name.scene7_image_folder);
            if(StringUtils.isEmpty(uploadPath)){
                String err = String.format("channelId(%s)的scene7上的路径没有配置 请配置tm_order_channel_config表",orderChannelId);
                $error(err);
                throw new BusinessException(err);
            }

            // FtpBean初期化
            BaseFtpComponent ftpComponent = FtpComponentFactory.getFtpComponent(FtpConstants.FtpConnectEnum.SCENE7_FTP);

            InputStream inputStream;
            String imageUrl = null;
            try {
                //建立连接
                ftpComponent.openConnect();
                ftpComponent.enterLocalPassiveMode();

                for (CmsBtImagesModel imagesModel : imageUrlList) {
                    imageUrl = imagesModel.getOriginalUrl();

                    if (StringUtils.isNullOrBlank2(imageUrl)) {
                        successImageUrlList.add(imagesModel);
                        continue;
                    }

                    try {
                        $info("thread-" + threadNo + ":" + imageUrl + "流取得开始");
                        inputStream = HttpUtils.getInputStream(imageUrl);
                    } catch (Exception ex) {
                        // 图片url错误
                        $error(ex.getMessage(), ex);
                        imagesModel.setUpdFlg(3);
                        imagesModel.setModifier(getTaskName());
                        cmsBtImagesDaoExt.updateImage(imagesModel);
                        continue;
                    }

                    String fileName = imagesModel.getImgName() + ".jpg";
                    $info("thread-" + threadNo + ":" + imageUrl + "ftp上传开始");
                    FtpFileBean ftpFileBean = new FtpFileBean(inputStream, uploadPath, fileName);
                    ftpComponent.uploadFile(ftpFileBean);
                    $info("thread-" + threadNo + ":" + imageUrl + "ftp上传结束");
                    successImageUrlList.add(imagesModel);
                    $info("thread-" + threadNo + ":" + imageUrl + "上传成功!");
                }
            } catch (Exception ex) {
                $info("thread-" + threadNo + ":" + imageUrl + "上传失败!");
                $error(ex.getMessage(), ex);
                issueLog.log(ex, ErrorType.BatchJob, SubSystem.CMS);
                isSuccess = false;
            } finally {
                //断开连接
                ftpComponent.closeConnect();
            }
        }

        return isSuccess;
    }
}

