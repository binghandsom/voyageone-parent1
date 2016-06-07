package com.voyageone.web2.cms.views.pop.image;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.util.ImgUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.ftp.FtpComponentFactory;
import com.voyageone.components.ftp.FtpConstants;
import com.voyageone.components.ftp.bean.FtpFileBean;
import com.voyageone.components.ftp.service.BaseFtpComponent;
import com.voyageone.components.imagecreate.service.ImageCreateService;
import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.impl.cms.ImageTemplateService;
import com.voyageone.service.impl.cms.ImagesService;
import com.voyageone.service.impl.cms.PlatformImagesService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtImagesModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gubuchun 15/12/18
 * @version 2.0.0
 */
@Service
public class CmsImageSettingService extends BaseAppService {

    @Autowired
    private ProductService productService;
    @Autowired
    ProductGroupService productGroupService;
    @Autowired
    PlatformImagesService platformImagesService;
    @Autowired
    ImageCreateService imageCreateService;
    @Autowired
    ImagesService imagesService;

    public Map<String, Object> uploadImage(MultipartFile file, Long productId, String imageType, UserSessionBean user, String imageExtend) throws Exception {

        Map<String, Object> response = new HashMap<>();

        String orderChannelId = user.getSelChannelId();

        //FTP服务器保存目录设定
        String uploadPath = ChannelConfigs.getVal1(user.getSelChannelId(), ChannelConfigEnums.Name.scene7_image_folder);
        if (StringUtils.isEmpty(uploadPath)) {
            String err = String.format("channelId(%s)的scene7上的路径没有配置 请配置tm_order_channel_config表", orderChannelId);
            $error(orderChannelId);
            throw new BusinessException(err);
        }

        CmsBtProductModel cmsBtProductModel = productService.getProductById(user.getSelChannelId(), productId);

        // 获取图片名字
        String imageName = getImageName(cmsBtProductModel, imageType, user);

        // FtpBean初期化
        BaseFtpComponent ftpComponent = FtpComponentFactory.getFtpComponent(FtpConstants.FtpConnectEnum.SCENE7_FTP);

        // ftp连接usernmae
        String userName = ftpComponent.getFtpConnectBean().getUsername();

        // 上传图片到Ftp
        if (uploadFtp(ftpComponent, uploadPath, file.getInputStream(), imageName + imageExtend)) {

            // 插入图片表
            CmsBtImagesModel newModel = new CmsBtImagesModel();
            newModel.setChannelId(orderChannelId);
            newModel.setOriginalUrl("本地图片上传:" + file.getOriginalFilename());
            newModel.setCode(cmsBtProductModel.getFields().getCode());
            newModel.setUpdFlg(1);
            newModel.setCreater(userName);
            newModel.setImgName(imageName);
            imagesService.insert(newModel);

            // 上新
            if (CmsConstants.ProductStatus.Approved.name().equals(cmsBtProductModel.getFields().getStatus()))
                productService.insertSxWorkLoad(orderChannelId, cmsBtProductModel, userName);

            // 更新产品数据
            ProductUpdateBean requestModel = new ProductUpdateBean();
            requestModel.setProductModel(cmsBtProductModel);
            requestModel.setModifier(user.getUserName());
            requestModel.setIsCheckModifed(false); // 不做最新修改时间ｃｈｅｃｋ
            productService.updateProduct(user.getSelChannelId(), requestModel);
            response.put("imageName", imageName);
            response.put("base64", ImgUtils.encodeToString(file.getInputStream(), ""));
            return response;
        }

        return null;
    }

    // 根据imagetype计算出新上传的图片名
    private String getImageName(CmsBtProductModel cmsBtProductModel, String imageType, UserSessionBean user) {

        CmsBtProductConstants.FieldImageType fieldImageType = CmsBtProductConstants.FieldImageType.getFieldImageTypeByName(imageType);
        List<CmsBtProductModel_Field_Image> images = cmsBtProductModel.getFields().getImages(fieldImageType);

        images = images.stream().filter(cmsBtProductModel_field_image -> cmsBtProductModel_field_image.size() > 0).filter(cmsBtProductModel_field_image1 -> !StringUtils.isEmpty(cmsBtProductModel_field_image1.getName())).collect(Collectors.toList());

        String imageName;
        int size = images.size();
        while (true){
            size++;
            imageName = String.format("%s-%s-%s%d", user.getSelChannelId(), cmsBtProductModel.getFields().getCode(), imageType.substring(imageType.length() - 1), size);
            final String finalImageName = imageName;
            if(images.stream().filter(cmsBtProductModel_field_image -> finalImageName.equalsIgnoreCase(cmsBtProductModel_field_image.getName())).collect(Collectors.toList()).size() == 0){
                break;
            }
        }

        images.add(new CmsBtProductModel_Field_Image(imageType, imageName));

        cmsBtProductModel.getFields().setImages(fieldImageType, images);

        return imageName;

    }

    private boolean uploadFtp(BaseFtpComponent ftpComponent, String uploadPath, InputStream imageStream, String imageName) throws IOException {
        boolean isSuccess = true;

        try {
            //建立连接
            ftpComponent.openConnect();
            ftpComponent.enterLocalPassiveMode();

            FtpFileBean ftpFileBean = new FtpFileBean(imageStream, uploadPath, imageName);
            ftpComponent.uploadFile(ftpFileBean);

        } catch (Exception ex) {
            $error(ex.getMessage(), ex);
            isSuccess = false;
        } finally {
            //断开连接
            ftpComponent.closeConnect();
        }

        return isSuccess;
    }
}
