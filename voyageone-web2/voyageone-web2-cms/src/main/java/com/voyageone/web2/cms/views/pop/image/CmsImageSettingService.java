package com.voyageone.web2.cms.views.pop.image;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.util.ImgUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.service.FtpService;
import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    // Scene7FTP设置
    private static final String S7FTP_CONFIG = "S7FTP_CONFIG";

    @Autowired
    private ProductService productService;

    @Autowired
    private FtpService ftpService;

    public Map<String, Object> uploadImage(MultipartFile file, Long productId, String imageType, UserSessionBean user) throws IOException {

        Map<String, Object> reponse = new HashMap<>();

        String orderChannelId = user.getSelChannelId();

        //FTP服务器保存目录设定
        String uploadPath = ChannelConfigs.getVal1(user.getSelChannelId(), ChannelConfigEnums.Name.scene7_image_folder);
        if (StringUtils.isEmpty(uploadPath)) {
            String err = String.format("channelId(%s)的scene7上的路径没有配置 请配置tm_order_channel_config表", orderChannelId);
            $error(orderChannelId);
            throw new BusinessException(err);
        }

        CmsBtProductModel cmsBtProductModel = productService.getProductById(user.getSelChannelId(), productId);

        // 扩展名
        String extensions = file.getOriginalFilename().lastIndexOf(".") != -1 ? file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")) : "";
        String imageName = getImageName(cmsBtProductModel, imageType, user);

        if (ftpService.storeFile(Codes.getCodeName(S7FTP_CONFIG, "Url"),
                Codes.getCodeName(S7FTP_CONFIG, "Port"),
                Codes.getCodeName(S7FTP_CONFIG, "UserName"),
                Codes.getCodeName(S7FTP_CONFIG, "Password"),
                imageName + extensions,
                uploadPath,
                file.getInputStream(),
                Codes.getCodeName(S7FTP_CONFIG, "FileCoding"), 120000)) {
            // 更新产品数据
            ProductUpdateBean requestModel = new ProductUpdateBean();
            requestModel.setProductModel(cmsBtProductModel);
            requestModel.setModifier(user.getUserName());
            requestModel.setIsCheckModifed(false); // 不做最新修改时间ｃｈｅｃｋ
            productService.updateProduct(user.getSelChannelId(), requestModel);
            reponse.put("imageName", imageName);
            reponse.put("base64", ImgUtils.encodeToString(file.getInputStream(), ""));
            return reponse;
        }

        return null;
    }

    // 根据imagetype计算出新上传的图片名
    private String getImageName(CmsBtProductModel cmsBtProductModel, String imageType, UserSessionBean user) {

        CmsBtProductConstants.FieldImageType fieldImageType = CmsBtProductConstants.FieldImageType.getFieldImageTypeByName(imageType);
        List<CmsBtProductModel_Field_Image> images = cmsBtProductModel.getFields().getImages(fieldImageType);

        images = images.stream().filter(cmsBtProductModel_field_image -> cmsBtProductModel_field_image.size() > 0).collect(Collectors.toList());

        String imageName = String.format("%s-%s-%s%d", user.getSelChannelId(), cmsBtProductModel.getFields().getCode(), imageType.substring(imageType.length() - 1), images.size() + 1);

        images.add(new CmsBtProductModel_Field_Image(imageType, imageName));

        cmsBtProductModel.getFields().setImages(fieldImageType, images);

        return imageName;

    }

}
