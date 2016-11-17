package com.voyageone.task2.cms.service;

import com.jd.open.api.sdk.request.imgzone.ImgzonePictureUploadRequest;
import com.jd.open.api.sdk.response.AbstractResponse;
import com.jd.open.api.sdk.response.imgzone.ImgzonePictureUploadResponse;
import com.taobao.api.ApiException;
import com.taobao.api.response.PictureReplaceResponse;
import com.taobao.api.response.PictureUploadResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.components.jd.service.JdImgzoneService;
import com.voyageone.components.tmall.service.TbPictureService;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.bean.cms.CmsBtShelvesInfoBean;
import com.voyageone.service.bean.cms.CmsBtShelvesProductBean;
import com.voyageone.service.impl.cms.CmsBtShelvesProductService;
import com.voyageone.service.impl.cms.CmsBtShelvesService;
import com.voyageone.service.impl.cms.CmsBtShelvesTemplateService;
import com.voyageone.service.impl.cms.promotion.PromotionCodeService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import com.voyageone.service.model.cms.CmsBtShelvesTemplateModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by james on 2016/11/14.
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_ShelvesImageUploadJob)
public class CmsShelvesImageUploadMQService extends BaseMQCmsService {

    private final CmsBtShelvesProductService cmsBtShelvesProductService;

    private final TbPictureService tbPictureService;

    private final CmsBtShelvesTemplateService cmsBtShelvesTemplateService;

    private final JdImgzoneService jdImgzoneService;


    @Autowired
    public CmsShelvesImageUploadMQService(CmsBtShelvesProductService cmsBtShelvesProductService, TbPictureService tbPictureService, CmsBtShelvesTemplateService cmsBtShelvesTemplateService,JdImgzoneService jdImgzoneService) {
        this.cmsBtShelvesProductService = cmsBtShelvesProductService;
        this.tbPictureService = tbPictureService;
        this.cmsBtShelvesTemplateService = cmsBtShelvesTemplateService;
        this.jdImgzoneService = jdImgzoneService;
    }

    @Override
    protected void onStartup(Map<String, Object> messageMap) throws Exception {
        Integer shelvesId = (Integer) messageMap.get("shelvesId");

        if (shelvesId != null) {
            CmsBtShelvesInfoBean cmsBtShelvesInfoBean = cmsBtShelvesProductService.getShelvesInfo(shelvesId,true);
            CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(cmsBtShelvesInfoBean.getShelvesModel().getChannelId(), CmsConstants.ChannelConfig.PLATFORM_IMAGE_DIRECTORY_ID, cmsBtShelvesInfoBean.getShelvesModel().getCartId().toString());
            if (cmsChannelConfigBean == null || StringUtil.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
                throw new BusinessException("图片分类目录没有配置");
            }
            final String picCatId = cmsChannelConfigBean.getConfigValue1();
            final CmsBtShelvesTemplateModel cmsBtShelvesTemplateModel = cmsBtShelvesTemplateService.select(cmsBtShelvesInfoBean.getShelvesModel().getSingleTemplateId());
            if (cmsBtShelvesTemplateModel != null) {
                final ShopBean shopBean = Shops.getShop(cmsBtShelvesInfoBean.getShelvesModel().getChannelId(), cmsBtShelvesInfoBean.getShelvesModel().getCartId());
                List<CmsBtShelvesProductModel> cmsBtShelvesProductModels = cmsBtShelvesInfoBean.getShelvesProductModels();
                cmsBtShelvesProductModels = cmsBtShelvesProductModels.stream().filter(cmsBtShelvesProductModel -> StringUtil.isEmpty(cmsBtShelvesProductModel.getPlatformImageUrl())).collect(Collectors.toList());
                ExecutorService es = Executors.newFixedThreadPool(5);
                cmsBtShelvesProductModels.forEach(item -> es.execute(()->uploadImage(shopBean, (CmsBtShelvesProductBean)item, cmsBtShelvesTemplateModel, picCatId)));
                es.shutdown();
                es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
            }
        }
    }

    private void uploadImage(ShopBean shopBean, CmsBtShelvesProductBean cmsBtShelvesProductModel, CmsBtShelvesTemplateModel cmsBtShelvesTemplateModel, String picCatId) {

        if (shopBean.getPlatform_id().equalsIgnoreCase(PlatFormEnums.PlatForm.TM.getId())) {
            uploadImageTm(shopBean, cmsBtShelvesProductModel, cmsBtShelvesTemplateModel,picCatId);
        }else if(shopBean.getPlatform_id().equalsIgnoreCase(PlatFormEnums.PlatForm.JD.getId())){
            uploadImageJd(shopBean, cmsBtShelvesProductModel, cmsBtShelvesTemplateModel,picCatId);
        }
    }

    private void uploadImageTm(ShopBean shopBean, CmsBtShelvesProductBean cmsBtShelvesProductModel, CmsBtShelvesTemplateModel cmsBtShelvesTemplateModel, String picCatId) {
//        shopBean.setAppKey("21008948");
//        shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
//        shopBean.setSessionKey("620272892e6145ee7c3ed73c555b4309f748ZZ9427ff3412641101981");
        try {
            PictureUploadResponse pictureUploadResponse = null;

            String imageUrl = getImageUrl(cmsBtShelvesProductModel,cmsBtShelvesTemplateModel);
            // 图片是否传过
            if (StringUtil.isEmpty(cmsBtShelvesProductModel.getPlatformImageId())) {
                //没有传过 上传
                pictureUploadResponse = tbPictureService.uploadPicture(shopBean, downImage(imageUrl), cmsBtShelvesProductModel.getShelvesId() + "-" + cmsBtShelvesProductModel.getImage(), picCatId);
            } else {
                try {
                    // 删除
                    tbPictureService.deletePictures(shopBean, Long.parseLong(cmsBtShelvesProductModel.getPlatformImageId()));
                } catch (Exception e) {
                    $error(e);
                }
                pictureUploadResponse = tbPictureService.uploadPicture(shopBean, downImage(imageUrl), cmsBtShelvesProductModel.getShelvesId() + "-" + cmsBtShelvesProductModel.getImage(), picCatId);
            }
            if (pictureUploadResponse != null && pictureUploadResponse.getPicture() != null) {
                cmsBtShelvesProductModel.setPlatformImageId(pictureUploadResponse.getPicture().getPictureId() + "");
                cmsBtShelvesProductModel.setPlatformImageUrl(pictureUploadResponse.getPicture().getPicturePath());
                cmsBtShelvesProductService.updatePlatformImage(cmsBtShelvesProductModel);
            }
        } catch (ApiException e) {
            e.printStackTrace();
            $error(e);
        }

    }

    private void uploadImageJd(ShopBean shopBean, CmsBtShelvesProductBean cmsBtShelvesProductModel, CmsBtShelvesTemplateModel cmsBtShelvesTemplateModel, String picCatId) {
//        shopBean.setApp_url("https://api.jd.com/routerjson");
//        shopBean.setAppKey("BFA3102EFD4B981E9EEC2BE32DF1E44E");
//        shopBean.setAppSecret("90742900899f49a5acfaf3ec1040a35c");
//        shopBean.setSessionKey("8bac1a4d-3853-446b-832d-060ed9d8bb8c");
        try {
            ImgzonePictureUploadResponse pictureUploadResponse = null;

            String imageUrl = getImageUrl(cmsBtShelvesProductModel,cmsBtShelvesTemplateModel);
            // 图片是否传过
            if (!StringUtil.isEmpty(cmsBtShelvesProductModel.getPlatformImageId())) {
                try {
                    // 删除
                    jdImgzoneService.deletePictures(shopBean, cmsBtShelvesProductModel.getPlatformImageId());
                } catch (Exception e) {
                    $error(e);
                }
            }
             pictureUploadResponse = jdImgzoneService.uploadPicture(shopBean, downImage(imageUrl), picCatId,cmsBtShelvesProductModel.getShelvesId() + "-" + cmsBtShelvesProductModel.getImage());

            if (pictureUploadResponse != null && !StringUtil.isEmpty(pictureUploadResponse.getPictureId())) {
                cmsBtShelvesProductModel.setPlatformImageId(pictureUploadResponse.getPictureId() + "");
                cmsBtShelvesProductModel.setPlatformImageUrl(pictureUploadResponse.getPictureUrl());
                cmsBtShelvesProductService.updatePlatformImage(cmsBtShelvesProductModel);
            }
        } catch (Exception e) {
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
            if (tmeplate.contains("@img-1")) {
                tmeplate = tmeplate.replace("@img-1", cmsBtShelvesProductModel.getImage());
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

    private byte[] downImage(String imageUrl) {
        long threadNo = Thread.currentThread().getId();
        //如果promotionImagesList为空的时，不做处理
        byte[] buffer = new byte[1024 * 10];
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();) {
            int len;
            URL url = new URL(imageUrl);
            $info("threadNo:" + threadNo + " url:" + imageUrl + "下载开始");
            //Url
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try (InputStream inputStream = conn.getInputStream()) {
                while ((len = inputStream.read(buffer)) > 0) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            $info("threadNo:" + threadNo + " url:" + imageUrl + "下载结束");
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            $error(e);
        }
        return null;
    }
}
