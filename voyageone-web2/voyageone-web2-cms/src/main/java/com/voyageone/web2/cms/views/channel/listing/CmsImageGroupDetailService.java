package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.ImageGroupService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CmsImageGroupDetail Service
 *
 * @author jeff.duan 2016/5/5.
 * @version 2.0.0
 */
@Service
public class CmsImageGroupDetailService extends BaseAppService {

    private final static String URL_PREFIX = "http://image.voyageone.com.cn/cms";

    private final static int FILE_LIMIT_SIZE = 3145728;

    private final static String IMAGE_TYPE = "jpg,png";

    @Autowired
    private ImageGroupService imageGroupService;

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
        // 图片类型
        result.put("imageTypeList", Types.getTypeList(71, (String) param.get("lang")));

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
     * 检索结果编辑
     *
     * @param images 图片列表
     * @param lang   语言
     */
    private void editImageModel(List<CmsBtImageGroupModel_Image> images, String lang) {
        if (images != null) {
            for (CmsBtImageGroupModel_Image image : images) {
                // ImageType
                image.setStatusName(Types.getTypeName(72, lang, String.valueOf(image.getStatus())));
            }
        }
    }

    /**
     * 更新ImageGroup信息
     *
     * @param param 客户端参数
     */
    public void save(Map<String, Object> param) {
        String userName = (String) param.get("userName");
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
            // 请输入必填项目
            throw new BusinessException("7000080");
        }

        // 如果存在图片那么平台不能变更
        CmsBtImageGroupModel model = imageGroupService.getImageGroupModel(imageGroupId);
        if (model != null && model.getImage() != null
                && model.getImage().size() > 0
                && model.getCartId() != Integer.parseInt(cartId)) {
            // 图片已经存在，不能修改平台
            throw new BusinessException("7000088");
        }

        imageGroupService.update(userName, imageGroupId, cartId, imageGroupName, imageType, viewType,
                brandNameList, productTypeList, sizeTypeList);
    }

    /**
     * 保存ImageGroup中的图片信息
     *
     * @param param 客户端参数
     * @param file  导入文件
     */
    public void saveImage(Map<String, Object> param, MultipartFile file) {

        String userName = (String) param.get("userName");
        String imageGroupId = (String) param.get("imageGroupId");
        String key = (String) param.get("key");
        String originUrl = (String) param.get("originUrl");
        String imageType = (String) param.get("imageType");
        // 编辑的场合，并且没有发生任何变化
        if (!StringUtils.isEmpty(key) && key.equals(originUrl) && file == null) {
            return;
        }
        // check
        String suffix = doSaveImageCheck(imageGroupId, originUrl, file);

        boolean uploadFlg = true;
        // 网络上传的场合
        if (file == null) {
            // 指定URL开头的场合，不进行FTP上传
            if (originUrl.startsWith(URL_PREFIX)) {
                uploadFlg = false;
            }
        }

        // 上传文件名
        String uploadUrl = originUrl;

        if (uploadFlg) {
            // 上传文件流
            InputStream inputStream = null;
            try {
                // 本地上传的场合
                if (file != null) {
                    inputStream = file.getInputStream();
                } else {
                    // 网络URL的场合
                    URL url = new URL(originUrl);
                    inputStream = url.openStream();
                }
            } catch (IOException ignored) {
            }

            // 上传文件
            uploadUrl = imageGroupService.uploadFile((String) param.get("channelId"), imageType, suffix, inputStream);
        }

        //更新cms_bt_image_group表
        if (StringUtils.isEmpty(key)) {
            // 新建的场合
            imageGroupService.addImage(userName, imageGroupId, uploadUrl);
        } else {
            // 编辑的场合
            imageGroupService.updateImage(userName, imageGroupId, key, uploadUrl);
        }
    }

    /**
     * 图片保存check
     *
     * @param imageGroupId 图片组ID
     * @param originUrl    原始URL
     * @param file         导入文件
     *                     return 图片扩展名
     */
    private String doSaveImageCheck(String imageGroupId, String originUrl, MultipartFile file) {

        // 文件名
        String fileName = "";
        InputStream inputStream = null;
        // 网络上传的场合
        if (file == null) {
            // 必须输入
            if (StringUtils.isEmpty(originUrl)) {
                // 请输入必填项目
                throw new BusinessException("7000080");
            }
            // ImageGroup里存在check
            if (originUrl.startsWith(URL_PREFIX)) {
                CmsBtImageGroupModel model = imageGroupService.getImageGroupModel(imageGroupId);
                if (model != null) {
                    if (model.getImage() != null) {
                        for (CmsBtImageGroupModel_Image image : model.getImage()) {
                            if (originUrl.equals(image.getOriginUrl())) {
                                // 原始Url已经存在于这个图片组中
                                throw new BusinessException("7000082");
                            }
                        }
                    }
                }
            }
            try {
                // 网络文件的场合
                URL url = new URL(originUrl);
                HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
                httpUrl.connect();
                inputStream = new BufferedInputStream(httpUrl.getInputStream());
                if (httpUrl.getContentLength() >= FILE_LIMIT_SIZE) {
                    // 图片大小不能超过3M
                    throw new BusinessException("7000087");
                }
            } catch (Exception e) {
                // 输入的原始Url非法
                throw new BusinessException("7000083");
            }
            if (originUrl.lastIndexOf("/") > -1) {
                fileName = originUrl.substring(originUrl.lastIndexOf("/") + 1);
            }
        } else {
            // 本地上传的场合
            if (file.getSize() >= FILE_LIMIT_SIZE) {
                // 图片大小不能超过3M
                throw new BusinessException("7000087");
            }
            fileName = file.getOriginalFilename();
            try {
                inputStream = file.getInputStream();
            } catch (IOException ignored) {
            }
        }
        String suffix = null;
        // 获取图片后缀
        if (fileName.lastIndexOf(".") > -1) {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        // 类型和图片后缀全部小写，然后判断后缀是否合法
        if (suffix == null || !IMAGE_TYPE.toLowerCase().contains(suffix.toLowerCase())) {
            // 文件扩展名非法
            throw new BusinessException("7000084");
        }

        try {
            if (inputStream == null) {
                // 文件的内容不是图片
                throw new BusinessException("7000085");
            }
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            if (bufferedImage == null) {
                // 文件的内容不是图片
                throw new BusinessException("7000085");
            }
        } catch (Exception e) {
            // 文件的内容不是图片
            throw new BusinessException("7000085");
        }

        return suffix;
    }

    /**
     * 删除Image信息
     *
     * @param param 客户端参数
     */
    public void delete(Map<String, Object> param) {
        String userName = (String) param.get("userName");
        String imageGroupId = (String) param.get("imageGroupId");
        String originUrl = (String) param.get("originUrl");
        imageGroupService.deleteImage(userName, imageGroupId, originUrl);
    }

    /**
     * 移动Image
     *
     * @param param 客户端参数
     */
    public void move(Map<String, Object> param) {
        String userName = (String) param.get("userName");
        String imageGroupId = (String) param.get("imageGroupId");
        String originUrl = (String) param.get("originUrl");
        String direction = (String) param.get("direction");
        imageGroupService.move(userName, imageGroupId, originUrl, direction);
    }

    /**
     * 重刷Image
     *
     * @param param 客户端参数
     */
    public void refresh(Map<String, Object> param) {
        String userName = (String) param.get("userName");
        String imageGroupId = (String) param.get("imageGroupId");
        String originUrl = (String) param.get("originUrl");
        imageGroupService.refresh(userName, imageGroupId, originUrl);
    }
}