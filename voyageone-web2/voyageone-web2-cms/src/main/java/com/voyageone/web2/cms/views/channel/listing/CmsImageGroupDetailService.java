package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.service.SFtpService;
import com.voyageone.service.impl.cms.ImageGroupService;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel_Image;
import com.voyageone.web2.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

;


/**
 * Created by jeff.duan on 2016/5/5.
 */
@Service
public class CmsImageGroupDetailService extends BaseAppService {

    @Autowired
    private ImageGroupService imageGroupService;

    @Autowired
    MongoSequenceService commSequenceMongoService; // DAO: Sequence

    @Autowired
    private SFtpService sFtpService;

    /**
     * 取得检索条件信息
     *
     * @param param 客户端参数
     * @return 检索条件信息
     */
    public Map<String, Object> init(Map<String, Object> param) {

        Map<String, Object> result = new HashMap<>();

        // 取得当前channel, 有多少个platform(Approve平台)
        result.put("platformList", TypeChannels.getTypeListSkuCarts((String) param.get("channelId"), "A", (String) param.get("lang")));
        // 品牌下拉列表
        result.put("brandNameList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, (String) param.get("channelId"), (String) param.get("lang")));
        // 产品类型下拉列表
        result.put("productTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, (String) param.get("channelId"), (String) param.get("lang")));
        // 尺寸类型下拉列表
        result.put("sizeTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, (String) param.get("channelId"), (String) param.get("lang")));

        String imageGroupId = (String) param.get("imageGroupId");
        CmsBtImageGroupModel imageGroupInfo = imageGroupService.getImageGroupModel(imageGroupId);

        result.put("imageGroupInfo", imageGroupInfo);
        return result;
    }

    /**
     * 检索图片
     *
     * @param param 客户端参数
     * @return 检索结果
     */
    public List<CmsBtImageGroupModel_Image> search(Map<String, Object> param) {
        String imageGroupId = (String) param.get("imageGroupId");
        CmsBtImageGroupModel imageGroupInfo = imageGroupService.getImageGroupModel(imageGroupId);
        editImageModel(imageGroupInfo.getImage(), (String) param.get("lang"));
        return imageGroupInfo.getImage();
    }

    /**
     * 检索结果转换
     *
     * @param images 图片列表
     * @param lang   语言
     */
    private void editImageModel(List<CmsBtImageGroupModel_Image> images, String lang) {
        if (images != null) {
            for (CmsBtImageGroupModel_Image image : images) {
                if ("cn".equals(lang)) {
                    // ImageStatusName
                    if (image.getStatus() == 1) {
                        image.setStatusName("等待上传");
                    } else if (image.getStatus() == 2) {
                        image.setStatusName("上传成功");
                    } else if (image.getStatus() == 3) {
                        image.setStatusName("上传失败");
                    }
                } else {
                    // ImageStatusName
                    if (image.getStatus() == 1) {
                        image.setStatusName("Waiting Upload");
                    } else if (image.getStatus() == 2) {
                        image.setStatusName("Upload Success");
                    } else if (image.getStatus() == 3) {
                        image.setStatusName("Upload Fail");
                    }
                }
            }
        }
    }

    /**
     * 编辑ImageGroup信息
     *
     * @param param 客户端参数
     * @return 检索结果
     */
    public void save(Map<String, Object> param) {
        String imageGroupId = (String) param.get("imageGroupId");
        String cartId = (String) param.get("platform");
        String imageGroupName = (String) param.get("imageGroupName");
        String imageType = (String) param.get("imageType");
        String viewType = (String) param.get("viewType");
        List<String> brandNameList = (List<String>) param.get("brandName");
        List<String> productTypeList = (List<String>) param.get("productType");
        List<String> sizeTypeList = (List<String>) param.get("sizeType");
        // 必须输入check
        if (StringUtils.isEmpty(cartId) || StringUtils.isEmpty(imageGroupName)
                || StringUtils.isEmpty(imageType) || StringUtils.isEmpty(viewType)) {
            throw new BusinessException("7000080");
        }
        imageGroupService.update(imageGroupId, cartId, imageGroupName, imageType, viewType,
                brandNameList, productTypeList, sizeTypeList);
    }

    /**
     * 保存ImageGroup信息
     *
     * @param param 客户端参数
     * @param file  导入文件
     */
    public void saveImage(Map<String, Object> param, MultipartFile file) {

        // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
        String types = Arrays.toString(ImageIO.getReaderFormatNames());
        String originUrl = (String) param.get("originUrl");

        String fileName = "";
        InputStream inputStream = null;
        if (file == null) {
            if (originUrl.lastIndexOf("/") > -1) {
                fileName = originUrl.substring(originUrl.lastIndexOf("/") + 1);
            }
            try {
                // 网络文件的场合
                URL url = new URL(originUrl);
                HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
                httpUrl.connect();
                inputStream = new BufferedInputStream(httpUrl.getInputStream());
            } catch (Exception e) {
                throw new BusinessException("Sorry, the url image is illegal.");
            }
        } else {
            fileName = file.getOriginalFilename();
            try {
                inputStream = file.getInputStream();
            } catch (IOException e) {
            }
        }
        String suffix = null;
        // 获取图片后缀
        if (fileName.lastIndexOf(".") > -1) {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }// 类型和图片后缀全部小写，然后判断后缀是否合法
        if (suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()) < 0) {
            throw new BusinessException("Sorry, the image suffix is illegal.");
        }


        try {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            if (bufferedImage == null) {
                throw new BusinessException("Sorry, the image content is illegal.");
            }
        } catch (IOException e) {
            throw new BusinessException("Sorry, the image content is illegal.");
        }


        if (file != null) {

            try {
                boolean isSuccess = sFtpService.storeFile("image.voyageone.com.cn",
                        "22",
                        "voyageone-cms-sftp",
                        "Li48I-22aBz",
                        DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_2) + "." + "jpg",
                        "/size/",
                        file.getInputStream(), "iso-8859-1");
                if (!isSuccess) {
                    throw new Exception("upload error");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                boolean isSuccess = sFtpService.storeFile("image.voyageone.com.cn",
                        "22",
                        "voyageone-cms-sftp",
                        "Li48I-22aBz",
                        "test22.jpg",
                        "/size/",
                        new URL("http://www.sinaimg.cn/dy/slidenews/2_img/2016_18/789_1781785_209090.jpg").openStream(),
                        "iso-8859-1");
                if (!isSuccess) {
                    throw new Exception("upload error");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 逻辑删除ImageGroup信息
     *
     * @param param 客户端参数
     */
    public void delete(Map<String, Object> param) {
        String imageGroupId = (String) param.get("imageGroupId");
        String originUrl = (String) param.get("originUrl");
        imageGroupService.logicDeleteImage(imageGroupId, originUrl);
    }

}