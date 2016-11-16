package com.voyageone.task2.cms.service;

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
import com.voyageone.components.tmall.service.TbPictureService;
import com.voyageone.service.impl.cms.CmsBtShelvesProductService;
import com.voyageone.service.impl.cms.CmsBtShelvesService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    private final CmsBtShelvesService cmsBtShelvesService;

    private final TbPictureService tbPictureService;

    @Autowired
    public CmsShelvesImageUploadMQService(CmsBtShelvesService cmsBtShelvesService, CmsBtShelvesProductService cmsBtShelvesProductService, TbPictureService tbPictureService) {
        this.cmsBtShelvesService = cmsBtShelvesService;
        this.cmsBtShelvesProductService = cmsBtShelvesProductService;
        this.tbPictureService = tbPictureService;
    }

    @Override
    protected void onStartup(Map<String, Object> messageMap) throws Exception {
        Integer shelvesId = (Integer) messageMap.get("shelvesId");

        if(shelvesId != null) {
            CmsBtShelvesModel cmsBtShelvesModel = cmsBtShelvesService.getId(shelvesId);
            List<CmsBtShelvesProductModel> cmsBtShelvesProductModels = cmsBtShelvesProductService.getByShelvesId(shelvesId);
            cmsBtShelvesProductModels = cmsBtShelvesProductModels.stream().filter(cmsBtShelvesProductModel -> StringUtil.isEmpty(cmsBtShelvesProductModel.getPlatformImageUrl())).collect(Collectors.toList());
            ExecutorService es  = Executors.newFixedThreadPool(5);
            cmsBtShelvesProductModels.forEach(item -> uploadImage(cmsBtShelvesModel,item));
            es.shutdown();
            es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        }
    }

    private void uploadImage(CmsBtShelvesModel cmsBtShelvesModel, CmsBtShelvesProductModel cmsBtShelvesProductModel){
        ShopBean shopBean = Shops.getShop(cmsBtShelvesModel.getChannelId(), cmsBtShelvesModel.getCartId());

        if(shopBean.getPlatform_id().equalsIgnoreCase(PlatFormEnums.PlatForm.TM.getId())){
            uploadImageTm(shopBean, cmsBtShelvesProductModel);
        }
    }

    private void uploadImageTm(ShopBean shopBean, CmsBtShelvesProductModel cmsBtShelvesProductModel){
        CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(shopBean.getOrder_channel_id(), CmsConstants.ChannelConfig.PLATFORM_IMAGE_DIRECTORY_ID,shopBean.getCart_id());
        if(cmsChannelConfigBean == null || StringUtil.isEmpty(cmsChannelConfigBean.getConfigValue1())){
            throw new BusinessException("图片分类目录没有配置");
        }
        try {
            PictureUploadResponse pictureUploadResponse = null;
            String imageUrl=cmsBtShelvesProductModel.getImage();
            // 图片是否传过
            if(StringUtil.isEmpty(cmsBtShelvesProductModel.getPlatformImageId())){
                //没有传过 上传
                pictureUploadResponse = tbPictureService.uploadPicture(shopBean,downImage(imageUrl),cmsBtShelvesProductModel.getShelvesId()+ "-" +cmsBtShelvesProductModel.getImage(), cmsChannelConfigBean.getConfigValue1());
                cmsBtShelvesProductModel.setPlatformImageId(pictureUploadResponse.getPicture().getPictureId()+"");
                cmsBtShelvesProductModel.setPlatformImageUrl(pictureUploadResponse.getPicture().getPicturePath());
            }else{
                try {
                    // 删除
                    tbPictureService.deletePictures(shopBean, Long.parseLong(cmsBtShelvesProductModel.getPlatformImageId()));
                }catch (Exception e){
                    $error(e);
                }
                pictureUploadResponse = tbPictureService.uploadPicture(shopBean,downImage(imageUrl),cmsBtShelvesProductModel.getShelvesId()+ "-" +cmsBtShelvesProductModel.getImage(), cmsChannelConfigBean.getConfigValue1());
            }
            if(pictureUploadResponse != null && pictureUploadResponse.getPicture() != null) {
                cmsBtShelvesProductModel.setPlatformImageId(pictureUploadResponse.getPicture().getPictureId() + "");
                cmsBtShelvesProductModel.setPlatformImageUrl(pictureUploadResponse.getPicture().getPicturePath());
                cmsBtShelvesProductService.updatePlatformImage(cmsBtShelvesProductModel);
            }
        } catch (ApiException e) {
            e.printStackTrace();
            $error(e);
        }

    }

    public byte[] downImage(String imageUrl) {
        long threadNo =  Thread.currentThread().getId();
        //如果promotionImagesList为空的时，不做处理
        byte[] buffer = new byte[1024 * 10];
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();) {
            int len;
            URL url = new URL(imageUrl);
            $info("threadNo:"+ threadNo + " url:" + imageUrl + "下载开始");
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

            $info("threadNo:"+ threadNo + " url:" + imageUrl + "下载结束");
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            $error(e);
        }
        return null;
    }
}
