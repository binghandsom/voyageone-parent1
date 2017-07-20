package com.voyageone.task2.cms.mqjob;

import com.jd.open.api.sdk.response.imgzone.ImgzonePictureUploadResponse;
import com.taobao.api.ApiException;
import com.taobao.api.response.PictureUploadResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.ImageServer;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.components.jd.service.JdImgzoneService;
import com.voyageone.components.tmall.service.TbPictureService;
import com.voyageone.service.bean.cms.CmsBtShelvesInfoBean;
import com.voyageone.service.bean.cms.CmsBtShelvesProductBean;
import com.voyageone.service.fields.cms.CmsBtShelvesModelClientType;
import com.voyageone.service.impl.cms.CmsBtShelvesProductService;
import com.voyageone.service.impl.cms.CmsBtShelvesService;
import com.voyageone.service.impl.cms.CmsBtShelvesTemplateService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsShelvesImageUploadMQMessageBody;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import com.voyageone.service.model.cms.CmsBtShelvesTemplateModel;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by james on 2016/11/14.
 *
 * @version 2.10.0
 * @since 2.10.0
 */
@Service
@RabbitListener()
public class CmsShelvesImageUploadMQJob extends TBaseMQCmsService<CmsShelvesImageUploadMQMessageBody> {
    private final CmsBtShelvesProductService cmsBtShelvesProductService;
    private final TbPictureService tbPictureService;
    private final CmsBtShelvesTemplateService cmsBtShelvesTemplateService;
    private final CmsBtShelvesService cmsBtShelvesService;
    private final JdImgzoneService jdImgzoneService;
    private StringBuffer failMessage = new StringBuffer();

    @Autowired
    public CmsShelvesImageUploadMQJob(CmsBtShelvesProductService cmsBtShelvesProductService,
                                      TbPictureService tbPictureService,
                                      CmsBtShelvesTemplateService cmsBtShelvesTemplateService,
                                      JdImgzoneService jdImgzoneService, CmsBtShelvesService cmsBtShelvesService) {
        this.cmsBtShelvesProductService = cmsBtShelvesProductService;
        this.tbPictureService = tbPictureService;
        this.cmsBtShelvesTemplateService = cmsBtShelvesTemplateService;
        this.jdImgzoneService = jdImgzoneService;
        this.cmsBtShelvesService = cmsBtShelvesService;
    }

    @Override
    public void onStartup(CmsShelvesImageUploadMQMessageBody messageMap) throws Exception {
        Integer shelvesId = messageMap.getShelvesId();

        if (shelvesId != null) {
            CmsBtShelvesInfoBean cmsBtShelvesInfoBean = cmsBtShelvesProductService.getShelvesInfo(cmsBtShelvesService.getId(shelvesId), true);
            CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(cmsBtShelvesInfoBean.getShelvesModel().getChannelId(), CmsConstants.ChannelConfig.PLATFORM_IMAGE_DIRECTORY_ID, cmsBtShelvesInfoBean.getShelvesModel().getCartId().toString());
            if (cmsChannelConfigBean == null || StringUtil.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
                cmsConfigExLog(messageMap,"图片分类目录没有配置:PLATFORM_IMAGE_DIRECTORY_ID");
            }

            final String picCatId = cmsChannelConfigBean.getConfigValue1();
            final CmsBtShelvesTemplateModel cmsBtShelvesTemplateModel = cmsBtShelvesTemplateService.select(cmsBtShelvesInfoBean.getShelvesModel().getSingleTemplateId());
            if (cmsBtShelvesTemplateModel != null) {
                final ShopBean shopBean = Shops.getShop(cmsBtShelvesInfoBean.getShelvesModel().getChannelId(), cmsBtShelvesInfoBean.getShelvesModel().getCartId());
                List<CmsBtShelvesProductModel> cmsBtShelvesProductModels = cmsBtShelvesInfoBean.getShelvesProductModels();
                cmsBtShelvesProductModels = cmsBtShelvesProductModels.stream().filter(cmsBtShelvesProductModel -> StringUtil.isEmpty(cmsBtShelvesProductModel.getPlatformImageUrl())).collect(Collectors.toList());
                ExecutorService es = Executors.newFixedThreadPool(5);
                if (cmsBtShelvesInfoBean.getShelvesModel().getClientType() == CmsBtShelvesModelClientType.APP) {
                    String path = String.format("%s/shelves%d", CmsBtShelvesProductService.getShelvesImagePath(), shelvesId);
                    FileUtils.mkdirPath(path);
                }
                cmsBtShelvesProductModels.forEach(item -> es.execute(() -> uploadImage(shopBean, cmsBtShelvesInfoBean.getShelvesModel(), (CmsBtShelvesProductBean) item, cmsBtShelvesTemplateModel, picCatId)));
                es.shutdown();
                es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
                if(failMessage.length()>0){
                    throw new BusinessException(failMessage.toString());
                }
            }
        }
    }

    private void uploadImage(ShopBean shopBean, CmsBtShelvesModel shelvesModel, CmsBtShelvesProductBean cmsBtShelvesProductModel, CmsBtShelvesTemplateModel cmsBtShelvesTemplateModel, String picCatId) {

        if (shelvesModel.getClientType() == CmsBtShelvesModelClientType.PC) {
            if (shopBean.getPlatform_id().equalsIgnoreCase(PlatFormEnums.PlatForm.TM.getId())) {
                uploadImageTm(shopBean, cmsBtShelvesProductModel, cmsBtShelvesTemplateModel, picCatId);
            } else if (shopBean.getPlatform_id().equalsIgnoreCase(PlatFormEnums.PlatForm.JD.getId())) {
                uploadImageJd(shopBean, cmsBtShelvesProductModel, cmsBtShelvesTemplateModel, picCatId);
            }
        } else {
            String path = String.format("%s/shelves%d", CmsBtShelvesProductService.getShelvesImagePath(), shelvesModel.getId());
            uploadLocal(path, cmsBtShelvesProductModel, cmsBtShelvesTemplateModel);
        }

    }

    private void uploadLocal(String path, CmsBtShelvesProductBean cmsBtShelvesProductModel, CmsBtShelvesTemplateModel cmsBtShelvesTemplateModel) {

        String saveFile = String.format("%s/%s.jpg", path, cmsBtShelvesProductModel.getProductCode());

        String imageUrl = getImageUrl(cmsBtShelvesProductModel, cmsBtShelvesTemplateModel);

        String channelId = cmsBtShelvesTemplateModel.getChannelId();
        byte[] imageBuf = downImage(imageUrl, channelId);

        if(imageBuf == null || imageBuf.length == 0) {
            failMessage.append("图片下载失败url=").append(imageUrl);
            return;
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream((new File(saveFile)))) {
            fileOutputStream.write(imageBuf);
            cmsBtShelvesProductModel.setPlatformImageId(cmsBtShelvesProductModel.getProductCode());
            cmsBtShelvesProductModel.setPlatformImageUrl(saveFile);
            cmsBtShelvesProductService.updatePlatformImage(cmsBtShelvesProductModel);
        } catch (Exception e) {
            e.printStackTrace();
            failMessage.append(e.getMessage());
            $error(e);
        }
    }

    private void uploadImageTm(ShopBean shopBean, CmsBtShelvesProductBean cmsBtShelvesProductModel, CmsBtShelvesTemplateModel cmsBtShelvesTemplateModel, String picCatId) {
        String imageUrl = getImageUrl(cmsBtShelvesProductModel, cmsBtShelvesTemplateModel);
        String channel = cmsBtShelvesTemplateModel.getChannelId();
        try {
            PictureUploadResponse pictureUploadResponse;

            // 图片是否传过
            if (StringUtil.isEmpty(cmsBtShelvesProductModel.getPlatformImageId())) {
                //没有传过 上传
                pictureUploadResponse = tbPictureService.uploadPicture(shopBean, downImage(imageUrl, channel),
                        cmsBtShelvesProductModel.getShelvesId() + "-" + cmsBtShelvesProductModel.getImage(), picCatId);
            } else {
                try {
                    // 删除
                    tbPictureService.deletePictures(shopBean, Long.parseLong(cmsBtShelvesProductModel.getPlatformImageId()));
                } catch (Exception e) {
                    $error(e);
                }
                pictureUploadResponse = tbPictureService.uploadPicture(shopBean, downImage(imageUrl, channel),
                        cmsBtShelvesProductModel.getShelvesId() + "-" + cmsBtShelvesProductModel.getImage(), picCatId);
            }
            if (pictureUploadResponse != null && pictureUploadResponse.getPicture() != null) {
                cmsBtShelvesProductModel.setPlatformImageId(pictureUploadResponse.getPicture().getPictureId() + "");
                cmsBtShelvesProductModel.setPlatformImageUrl(pictureUploadResponse.getPicture().getPicturePath());
                cmsBtShelvesProductService.updatePlatformImage(cmsBtShelvesProductModel);
            }
        } catch (ApiException e) {
            failMessage.append("图片上传TM失败url=").append(imageUrl).append("\t");
            e.printStackTrace();
            $error(e);
        }

    }

    private void uploadImageJd(ShopBean shopBean, CmsBtShelvesProductBean cmsBtShelvesProductModel, CmsBtShelvesTemplateModel cmsBtShelvesTemplateModel, String picCatId) {
        String imageUrl = getImageUrl(cmsBtShelvesProductModel, cmsBtShelvesTemplateModel);
        try {
            ImgzonePictureUploadResponse pictureUploadResponse;

            // 图片是否传过
            if (!StringUtil.isEmpty(cmsBtShelvesProductModel.getPlatformImageId())) {
                try {
                    // 删除
                    jdImgzoneService.deletePictures(shopBean, cmsBtShelvesProductModel.getPlatformImageId());
                } catch (Exception e) {
                    $error(e);
                }
            }
            String channel = cmsBtShelvesTemplateModel.getChannelId();

            pictureUploadResponse = jdImgzoneService.uploadPicture("MQ", imageUrl, shopBean,
                    downImage(imageUrl, channel), picCatId, cmsBtShelvesProductModel.getShelvesId() + "-" +
                            cmsBtShelvesProductModel.getImage());

            if (pictureUploadResponse != null && !StringUtil.isEmpty(pictureUploadResponse.getPictureId())) {
                cmsBtShelvesProductModel.setPlatformImageId(pictureUploadResponse.getPictureId() + "");
                cmsBtShelvesProductModel.setPlatformImageUrl(pictureUploadResponse.getPictureUrl());
                cmsBtShelvesProductService.updatePlatformImage(cmsBtShelvesProductModel);
            }
        } catch (Exception e) {
            failMessage.append("图片上传TM失败url=").append(imageUrl).append("\t");
            e.printStackTrace();
            $error(e);
        }

    }

    private String getImageUrl(CmsBtShelvesProductBean cmsBtShelvesProductModel, CmsBtShelvesTemplateModel cmsBtShelvesTemplateModel) {
        String tmeplate = "";
        if (!StringUtil.isEmpty(cmsBtShelvesTemplateModel.getHtmlImageTemplate())) {
            tmeplate = cmsBtShelvesTemplateModel.getHtmlImageTemplate();
            if (tmeplate.contains("@price")) {
                tmeplate = tmeplate.replace("@price", cmsBtShelvesProductModel.getSalePrice().intValue() + "");
            }
            if (tmeplate.contains("@img")) {
                tmeplate = tmeplate.replace("@img", cmsBtShelvesProductModel.getImage());
            }
            if (tmeplate.contains("@name")) {
                try {
                    tmeplate = tmeplate.replace("@name", URLEncoder.encode(cmsBtShelvesProductModel.getProductName(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (tmeplate.contains("@sale_price")) {
                Double promitonPrice = 0.0;
                if (cmsBtShelvesProductModel.getPromotionPrice() != null) {
                    promitonPrice = cmsBtShelvesProductModel.getPromotionPrice();
                }
                tmeplate = tmeplate.replace("@sale_price", promitonPrice.intValue() + "");
            }
        }
        return tmeplate;
    }

    public byte[] downImage(String imageUrl, String channel) {
        $info("下载货架图片 %s", imageUrl);

        try (InputStream inputStream = ImageServer.proxyDownloadImage(imageUrl, channel)) {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            $error(e);
        }

        return null;
    }
}
