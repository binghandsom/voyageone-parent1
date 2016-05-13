package com.voyageone.web2.cms.views.pop.image;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.util.FtpUtil;
import com.voyageone.common.util.ImgUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.imagecreate.bean.ImageCreateAddListRequest;
import com.voyageone.components.imagecreate.bean.ImageCreateAddListResponse;
import com.voyageone.components.imagecreate.service.ImageCreateService;
import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.bean.openapi.image.CreateImageParameter;
import com.voyageone.service.impl.cms.ImageTemplateService;
import com.voyageone.service.impl.cms.ImagesService;
import com.voyageone.service.impl.cms.PlatformImagesService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtImagesModel;
import com.voyageone.service.model.cms.CmsBtPlatformImagesModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
    private ImageTemplateService imageTemplateService;
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
        String uploadPath = ChannelConfigs.getVal1(user.getSelChannelId(), ChannelConfigEnums.Name.scene7_image_folder);
        if (StringUtils.isEmpty(uploadPath)) {
            String err = String.format("channelId(%s)的scene7上的路径没有配置 请配置tm_order_channel_config表", orderChannelId);
            $error(orderChannelId);
            throw new BusinessException(err);
        }
        ftpBean.setUpload_path(uploadPath);

        CmsBtProductModel cmsBtProductModel = productService.getProductById(user.getSelChannelId(), productId);

        // 获取图片名字
        String imageName = getImageName(cmsBtProductModel, imageType, user);

        // 上传图片到Ftp
        if (uplodFtp(ftpBean, file.getInputStream(), imageName + imageExtend)) {

            // 插入图片表
            CmsBtImagesModel newModel = new CmsBtImagesModel();
            newModel.setChannelId(orderChannelId);
            newModel.setOriginalUrl("本地图片上传:" + file.getOriginalFilename());
            newModel.setCode(cmsBtProductModel.getFields().getCode());
            newModel.setUpdFlg(1);
            newModel.setCreater(userName);
            newModel.setImgName(imageName);
            imagesService.insert(newModel);

            // 调用图片生成API

            // 取得CMS系统展示用的产品图片模板
            CmsBtImageTemplateModel commonTemplate = imageTemplateService.getCommonTemplate();

            // 获取该code对应的所有模板
            List<CreateImageParameter> imageDatas = new ArrayList<>();

            // CMS显示用共通模板
            CreateImageParameter commonTemplateParameter = new CreateImageParameter();
            commonTemplateParameter.setChannelId(orderChannelId);
            commonTemplateParameter.setTemplateId(commonTemplate.getImageTemplateId());
            commonTemplateParameter.setFile(imageName);
            commonTemplateParameter.setVParam(new String[]{orderChannelId, imageName + imageExtend});
            imageDatas.add(commonTemplateParameter);

            List<CmsBtImageTemplateModel> templateModels = imageTemplateService.getTemplateListWithNoParams(orderChannelId
                    , cmsBtProductModel.getFields().getBrand()
                    , cmsBtProductModel.getFields().getProductType()
                    , cmsBtProductModel.getFields().getSizeType());

            // 返回需要调用图片生成api的对象
            for (CmsBtImageTemplateModel templateModel : templateModels) {

                CreateImageParameter createImageParameter = new CreateImageParameter();
                createImageParameter.setChannelId(orderChannelId);
                createImageParameter.setTemplateId(templateModel.getImageTemplateId());
                createImageParameter.setFile(imageName);
                createImageParameter.setVParam(new String[]{imageName + imageExtend});
                imageDatas.add(createImageParameter);

                // 获取产品对应的group信息
                CmsBtProductGroupModel groupModel = productGroupService.selectProductGroupByCode(orderChannelId, cmsBtProductModel.getFields().getCode(), templateModel.getCartId());

                // 返回templateImageUrl
                String templateImageUrl = imageTemplateService.getTemplateImageUrl(orderChannelId, templateModel.getImageTemplateId().toString(), imageName + imageExtend);

                // 将模板图片插入到platformImage
                CmsBtPlatformImagesModel platformImage = platformImagesService.selectByImageNameWithTemplate(orderChannelId, templateModel.getCartId(), imageName, templateModel.getImageTemplateId());
                platformImage.setSearchId(groupModel.getGroupId().toString());
                platformImage.setOriginalImgUrl(templateImageUrl);
                platformImage.setUpdFlg(0);
                platformImage.setCreater(userName);
                platformImage.setModifier(userName);
                platformImagesService.save(platformImage);

                // 上新
                if (CmsConstants.ProductStatus.Approved.name().equals(cmsBtProductModel.getFields().getStatus()))
                    productService.insertSxWorkLoad(orderChannelId, cmsBtProductModel, userName);
            }

            // 调用图片生成API
            ImageCreateAddListRequest request = new ImageCreateAddListRequest();
            request.setData(imageDatas);
            ImageCreateAddListResponse imageCreateResponse = imageCreateService.addList(request);
            if (imageCreateResponse.getErrorCode() > 0)
                $error(imageDatas + " 调用图片生成API失败:" + imageCreateResponse.getErrorMsg());

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

        images = images.stream().filter(cmsBtProductModel_field_image -> cmsBtProductModel_field_image.size() > 0).collect(Collectors.toList());

        String imageName = String.format("%s-%s-%s%d", user.getSelChannelId(), cmsBtProductModel.getFields().getCode(), imageType.substring(imageType.length() - 1), images.size() + 1);

        images.add(new CmsBtProductModel_Field_Image(imageType, imageName));

        cmsBtProductModel.getFields().setImages(fieldImageType, images);

        return imageName;

    }

    private boolean uplodFtp(FtpBean ftpBean, InputStream imageStream, String imageName) throws IOException {
        FtpUtil ftpUtil = new FtpUtil();
        FTPClient ftpClient = new FTPClient();
        boolean isSuccess = true;

        try {
            //建立连接
            ftpClient = ftpUtil.linkFtp(ftpBean);
            if (ftpClient != null) {

                boolean change = ftpClient.changeWorkingDirectory(ftpBean.getUpload_path());
                if (change) {
                    ftpClient.enterLocalPassiveMode();
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    ftpClient.setConnectTimeout(120000);
                    boolean result = ftpClient.storeFile(imageName, imageStream);

                    if (!result) {
                        isSuccess = false;
                    }
                }
            }

        } catch (Exception ex) {
            $error(ex.getMessage(), ex);
            isSuccess = false;

        } finally {
            //断开连接
            if (ftpClient != null) {
                ftpUtil.disconnectFtp(ftpClient);
            }
        }

        return isSuccess;
    }
}
