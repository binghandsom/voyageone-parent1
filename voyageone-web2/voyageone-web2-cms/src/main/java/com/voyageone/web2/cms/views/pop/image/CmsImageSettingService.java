package com.voyageone.web2.cms.views.pop.image;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.util.FtpUtil;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.ImgUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtFeedProductImageModel;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
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

    public Map<String,Object>  uploadImage(MultipartFile file, Long productId, String imageType, UserSessionBean user) throws IOException {

        Map<String,Object> reponse = new HashMap<>();

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

        // 扩展名
        String extensions = file.getOriginalFilename().lastIndexOf(".") != -1?file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")):"";
        String imageName = getImageName(cmsBtProductModel, imageType, user);


        if (uplodFtp(ftpBean, file.getInputStream(), imageName + extensions)) {
            // 更新产品数据
            ProductUpdateBean requestModel = new ProductUpdateBean();
            requestModel.setProductModel(cmsBtProductModel);
            requestModel.setModifier(user.getUserName());
            requestModel.setIsCheckModifed(false); // 不做最新修改时间ｃｈｅｃｋ
            productService.updateProduct(user.getSelChannelId(), requestModel);
            reponse.put("imageName", imageName);
            reponse.put("base64",ImgUtils.encodeToString(file.getInputStream(), ""));
            return reponse;
        }

        return null;
    }

    private String getImageName(CmsBtProductModel cmsBtProductModel, String imageType, UserSessionBean user) {

        CmsBtProductConstants.FieldImageType fieldImageType = CmsBtProductConstants.FieldImageType.getFieldImageTypeByName(imageType);
        List<CmsBtProductModel_Field_Image> images = cmsBtProductModel.getFields().getImages(fieldImageType);

        images = images.stream().filter(cmsBtProductModel_field_image -> cmsBtProductModel_field_image.size() > 0).collect(Collectors.toList());

        String imageName = String.format("%s-%s-%s%d", user.getSelChannelId(), cmsBtProductModel.getFields().getCode(), imageType.substring(imageType.length()-1), images.size() + 1);

        CmsBtProductModel_Field_Image cmsBtProductModel_Field_Image = new CmsBtProductModel_Field_Image(imageType,imageName);

        images.add(cmsBtProductModel_Field_Image);

        cmsBtProductModel.getFields().setImages(fieldImageType,images);

        return imageName;

    }

    private boolean uplodFtp(FtpBean ftpBean, InputStream imageStream, String imageName) throws IOException {
        FtpUtil ftpUtil = new FtpUtil();
        FTPClient ftpClient = new FTPClient();
        boolean isSuccess = true;

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
                    boolean result = ftpClient.storeFile(imageName, imageStream);

                    if (result) {

                    } else {
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
