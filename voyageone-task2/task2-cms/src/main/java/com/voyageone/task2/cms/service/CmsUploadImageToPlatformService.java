package com.voyageone.task2.cms.service;

import com.jd.open.api.sdk.response.AbstractResponse;
import com.jd.open.api.sdk.response.imgzone.ImgzonePictureUploadResponse;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.domain.Picture;
import com.taobao.api.response.PictureUploadResponse;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.service.JdImgzoneService;
import com.voyageone.components.jumei.bean.JmImageFileBean;
import com.voyageone.components.jumei.service.JumeiImageFileService;
import com.voyageone.components.tmall.service.TbPictureService;
import com.voyageone.ecerp.interfaces.third.koala.KoalaItemService;
import com.voyageone.ecerp.interfaces.third.koala.beans.ItemImgUploadResponse;
import com.voyageone.ecerp.interfaces.third.koala.beans.KoalaConfig;
import com.voyageone.service.dao.cms.mongo.CmsBtImageGroupDao;
import com.voyageone.service.model.cms.enums.ImageCategoryType;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel_Image;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.dao.CmsMtImageCategoryDao;
import com.voyageone.task2.cms.model.CmsMtImageCategoryModel;
import com.voyageone.task2.cms.service.promotion.beat.ImageCategoryService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;


/**
 * 图片上传到平台（暂时只支持淘宝/天猫/天猫国际/聚美/京东/京东国际/京东国际匠心界/京东国际悦境）
 * @author jeff.duan on 2016/5/10.
 * @version 2.0.0
 */
@Service
public class CmsUploadImageToPlatformService extends BaseCronTaskService {


    /* 斜杠分隔符 */
    private static final String SLASH = "/";

    @Autowired
    private CmsBtImageGroupDao cmsBtImageGroupDao;

    @Autowired
    private CmsMtImageCategoryDao imageCategoryDao;

    @Autowired
    private ImageCategoryService imageCategoryService;

    @Autowired
    private JumeiImageFileService jumeiImageFileService;

    @Autowired
    private TbPictureService tbPictureService;

    @Autowired
    private JdImgzoneService jdImgzoneService;

    @Autowired
    private KoalaItemService koalaItemService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsUploadImageToPlatformJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // 取得图片上传状态为2：等待上传的对象
        JongoQuery queryObject = new JongoQuery();
        // 暂时只支持淘宝/天猫/天猫国际/聚美/京东/京东国际/京东国际匠心界/京东国际悦境
        queryObject.setQuery("{\"image.status\":"
                + CmsConstants.ImageUploadStatus.WAITING_UPLOAD + ",\"active\":1,\"cartId\":{$in:[" + CartEnums.Cart.TM.getId() + ","
                + CartEnums.Cart.TB.getId() + "," + CartEnums.Cart.TG.getId() + "," + CartEnums.Cart.JM.getId() + ","
                + CartEnums.Cart.TT.getId() + "," + CartEnums.Cart.LTT.getId() + "," + CartEnums.Cart.KL.getId() + ","
                + CartEnums.Cart.JD.getId() + "," + CartEnums.Cart.JG.getId() + "," + CartEnums.Cart.JGJ.getId() + "," + CartEnums.Cart.JGY.getId() + "]}}");

        List<CmsBtImageGroupModel> imageGroupList = cmsBtImageGroupDao.select(queryObject);
        for (CmsBtImageGroupModel imageGroup : imageGroupList) {
            List<CmsBtImageGroupModel_Image> images = imageGroup.getImage();
            for (CmsBtImageGroupModel_Image image : images) {
                if (CmsConstants.ImageUploadStatus.WAITING_UPLOAD.equals(String.valueOf(image.getStatus()))) {
                    uploadImageToPlatform(imageGroup.getChannelId(), String.valueOf(imageGroup.getCartId()), imageGroup.getImageType(), image);
                    imageGroup.setModifier(getTaskName());
                    cmsBtImageGroupDao.update(imageGroup);
                }
            }
        }
    }

    /**
     * 图片上传到平台
     *
     * @param channelId 渠道id
     * @param cartId 平台id
     * @param imageType 图片类型
     * @param image ImageModel
     */
    private void uploadImageToPlatform(String channelId, String cartId, int imageType, CmsBtImageGroupModel_Image image) {
        if (cartId.equals(CartEnums.Cart.TM.getId()) || cartId.equals(CartEnums.Cart.TB.getId()) ||cartId.equals(CartEnums.Cart.TG.getId())
                || cartId.equals(CartEnums.Cart.TT.getId()) || cartId.equals(CartEnums.Cart.LTT.getId())) {
            uploadImageToTB(channelId, cartId, imageType, image);
        } else if (cartId.equals(CartEnums.Cart.JM.getId())) {
            uploadImageToJM(channelId, imageType, image);
        } else if (cartId.equals(CartEnums.Cart.JD.getId()) || cartId.equals(CartEnums.Cart.JG.getId())
                || cartId.equals(CartEnums.Cart.JGJ.getId()) || cartId.equals(CartEnums.Cart.JGY.getId())) {
            uploadImageToJD(channelId, cartId, imageType, image);
        } else if (cartId.equals(CartEnums.Cart.KL.getId())) {
            uploadImageToKL(channelId, imageType, image);
        }
        // TODO 淘宝/天猫/天猫国际/聚美/京东/京东国际/京东国际匠心界/京东国际悦境以外 以后往这里加
    }

    private void uploadImageToKL(String channelId, int imageType, CmsBtImageGroupModel_Image image) {
        String originUrl = image.getOriginUrl();
        $info("开始上传平台图片---图片原始Url:" + originUrl + ", 平台id;" + 34 + ", 渠道id:" + channelId);
        InputStream inputStream = null;
        byte[] bytes = null;
        try {
            URL url = new URL(originUrl);
            HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            inputStream = new BufferedInputStream(httpUrl.getInputStream());
            bytes = IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            $error("原始Url非法");
            // 设置返回的错误信息
            image.setErrorMsg("原始Url非法");
            // 设置图片上传状态为上传成功
            image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_FAIL));
            return;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ignored) {
            }
        }

        KoalaConfig koalaConfig = Shops.getShopKoala(channelId, CartEnums.Cart.KL.getId());
        ItemImgUploadResponse itemImgUploadResponse = null;
        try{
            itemImgUploadResponse = koalaItemService.imgUpload(koalaConfig, bytes, new Date().getTime() + ".jpg");
            if(itemImgUploadResponse != null){
                // 设置平台返回的Url
                image.setPlatformUrl(itemImgUploadResponse.getUrl());
                // 设置平台返回的PictureId
                image.setPlatformImageId(itemImgUploadResponse.getUrl());
                // 设置图片上传状态为上传成功
                image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_SUCCESS));
                // 清除errorMsg
                image.setErrorMsg(null);
            }
        }catch (Exception e){
            // 设置图片上传状态为上传成功
            image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_FAIL));
            // 清除errorMsg
            image.setErrorMsg(e.getMessage());
        }
    }


    /**
     * 图片上传到淘宝/天猫/天猫国际
     *
     * @param channelId 渠道id
     * @param cartId 平台id
     * @param imageType 图片类型
     * @param image ImageModel
     */
    private void uploadImageToTB(String channelId, String cartId, int imageType, CmsBtImageGroupModel_Image image) {
        String originUrl = image.getOriginUrl();
        String platformUrl = image.getPlatformUrl();
        $info("开始上传平台图片---图片原始Url:" + originUrl + ", 平台id;" + cartId + ", 渠道id:" + channelId);
        InputStream inputStream = null;
        byte[] bytes = null;
        try {
            URL url = new URL(originUrl);
            HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            inputStream = new BufferedInputStream(httpUrl.getInputStream());
            bytes = IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            $error("原始Url非法");
            // 设置返回的错误信息
            image.setErrorMsg("原始Url非法");
            // 设置图片上传状态为上传成功
            image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_FAIL));
            return;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ignored) {
            }
        }

        ShopBean shopBean = null;
        String imageName = "";
        TaobaoResponse uploadResponse = null;
        try {
            shopBean = Shops.getShop(channelId, cartId);
            if (shopBean == null) {
                throw new BusinessException("appkey信息取得失败");
            }
            // 取得类目id
            String categoryId = "0";
            ImageCategoryType imageCategoryType = getImageCategoryType(String.valueOf(imageType));
            if (imageCategoryType != null) {
                CmsMtImageCategoryModel model = imageCategoryDao.select(shopBean, imageCategoryType);
                if (model == null) {
                    model = imageCategoryService.createImageCategory(shopBean, imageCategoryType, getTaskName());
                }
                categoryId = model.getCategory_tid();
            }

            if (StringUtils.isEmpty(platformUrl)) {
                // 新建的场合
                imageName = originUrl.substring(originUrl.lastIndexOf("/") + 1);
                uploadResponse = tbPictureService.uploadPicture(shopBean, bytes, imageName, categoryId);

            } else {
                // 更新的场合
                imageName = platformUrl.substring(platformUrl.lastIndexOf("/") + 1);
                uploadResponse = tbPictureService.replacePicture(shopBean, bytes, imageName, Long.parseLong(image.getPlatformImageId()));
            }
            if (uploadResponse == null || !uploadResponse.isSuccess()) {
                // 设置返回的错误信息
                if (uploadResponse == null) {
                    $error("上传图片到天猫时，超时, response为空");
                    image.setErrorMsg("上传图片到天猫时，超时, response为空");
                } else{
                    String msg = "上传图片到天猫时，错误:" + uploadResponse.getErrorCode() + ", " + uploadResponse.getMsg();
                    if (!StringUtils.isEmpty(uploadResponse.getSubMsg())) {
                        msg += uploadResponse.getSubMsg();
                    }
                    $error(msg);
                    image.setErrorMsg(msg);
                }
                // 设置图片上传状态为上传成功
                image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_FAIL));
                return;
            } else {
                // 新建的场合
                if (StringUtils.isEmpty(platformUrl)) {
                    Picture picture = ((PictureUploadResponse)uploadResponse).getPicture();
                    String pictureUrl = "";
                    Long platformImageId = null;
                    if (picture != null) {
                        pictureUrl = picture.getPicturePath();
                        platformImageId = picture.getPictureId();
                    }
                    // 设置平台返回的Url
                    image.setPlatformUrl(pictureUrl);
                    // 设置平台返回的PictureId
                    image.setPlatformImageId(String.valueOf(platformImageId));
                    // 设置图片上传状态为上传成功
                    image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_SUCCESS));
                    // 清除errorMsg
                    image.setErrorMsg(null);
                } else {
                    // 更新的场合
                    // 设置图片上传状态为上传成功
                    image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_SUCCESS));
                    // 清除errorMsg
                    image.setErrorMsg(null);
                }
            }

        } catch (Exception e) {
            $error(e.getMessage());
            // 设置返回的错误信息
            image.setErrorMsg(e.getMessage());
            // 设置图片上传状态为上传成功
            image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_FAIL));
        }
    }

    /**
     * 图片上传到聚美
     *
     * @param channelId 渠道id
     * @param imageType 图片类型
     * @param image ImageModel
     */
    private void uploadImageToJM(String channelId, int imageType, CmsBtImageGroupModel_Image image) {
        String originUrl = image.getOriginUrl();
        String platformUrl = image.getPlatformUrl();
        $info("开始上传平台图片---图片原始Url:" + originUrl + ", 平台id;27, 渠道id:" + channelId );
        InputStream inputStream = null;
        try {
            URL url = new URL(originUrl);
            HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            inputStream = new BufferedInputStream(httpUrl.getInputStream());
        } catch (Exception e) {
            $error("原始Url非法");
            // 设置返回的错误信息
            image.setErrorMsg("原始Url非法");
            // 设置图片上传状态为上传成功
            image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_FAIL));
            return;
        }

        ShopBean shopBean = null;
        try {
            JmImageFileBean jmImageFileBean = new JmImageFileBean();
            if (StringUtils.isEmpty(platformUrl)) {
                // 新建的场合
                String imageName = originUrl.substring(originUrl.lastIndexOf("/") + 1, originUrl.lastIndexOf("."));
                jmImageFileBean.setImgName(imageName);
                jmImageFileBean.setDirName(SLASH + channelId + SLASH + imageType);
            } else {
                // 更新的场合
                String imageName = platformUrl.substring(platformUrl.lastIndexOf("/") + 1, platformUrl.lastIndexOf("."));
                jmImageFileBean.setImgName(imageName);
                String [] temp = platformUrl.split("/");
                if (temp.length > 4) {
                    jmImageFileBean.setDirName(SLASH + temp[temp.length - 3] + SLASH + temp[temp.length - 2]);
                }else {
                    jmImageFileBean.setDirName(SLASH + channelId + SLASH + imageType);
                }
            }

            jmImageFileBean.setInputStream(inputStream);
            jmImageFileBean.setNeedReplace(true);
            jmImageFileBean.setExtName("jpg");

            shopBean = Shops.getShop(channelId, CartEnums.Cart.JM.getId());
            if (shopBean == null) {
                throw new BusinessException("appkey信息取得失败");
            }
            String platFormUrl = jumeiImageFileService.imageFileUpload(shopBean, jmImageFileBean);
            // 设置平台返回的Url
            image.setPlatformUrl(platFormUrl);
            // 设置图片上传状态为上传成功
            image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_SUCCESS));
            // 清除errorMsg
            image.setErrorMsg(null);
        } catch (Exception e) {
            String errMsg = e.getMessage();
            if (errMsg.indexOf("调用聚美API错误")  > -1 ) {
                // 去除错误信息里面的提交URL，否则图片的话实在太长了
                if (errMsg.indexOf(shopBean.getApp_url()) > -1) {
                    errMsg = errMsg.substring(0, errMsg.lastIndexOf(shopBean.getApp_url()));
                } else if (errMsg.lastIndexOf("}") > -1) {
                    errMsg = errMsg.substring(0, errMsg.lastIndexOf("}") + 1);
                }
                image.setErrorMsg(errMsg);
            }
           else if(errMsg.indexOf("response code: 413")>0) {
                image.setErrorMsg("1.图片不要使用中国模特，如有代言人必须是当年的。\n" +
                        "2.PC端上传图片宽660 - 790px（推荐790px），高度660 - 1000px。\n" +
                        "3.APP端上传图片宽750-1200px（推荐750px），高度750-1200px。\n" +
                        "4.聚美优品用图片，单张体积必须小于457KB。");
            }
            else
            {
                image.setErrorMsg(errMsg);
            }
            $error(errMsg);
            // 设置返回的错误信息

            // 设置图片上传状态为上传成功
            image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_FAIL));
        }
    }

    /**
     * 图片上传到京东/京东国际/京东国际匠心界/京东国际悦境
     *
     * @param channelId 渠道id
     * @param cartId 平台id
     * @param imageType 图片类型
     * @param image ImageModel
     */
    private void uploadImageToJD(String channelId, String cartId, int imageType, CmsBtImageGroupModel_Image image) {
        String originUrl = image.getOriginUrl();
        String platformUrl = image.getPlatformUrl();
        $info("开始上传京东平台图片---图片原始Url:" + originUrl + ", 平台id;" + cartId + ", 渠道id:" + channelId);
        InputStream inputStream = null;
        byte[] bytes = null;
        try {
            URL url = new URL(originUrl);
            HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            inputStream = new BufferedInputStream(httpUrl.getInputStream());
            bytes = IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            $error("原始Url非法");
            // 设置返回的错误信息
            image.setErrorMsg("原始Url非法");
            // 设置图片上传状态为上传成功
            image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_FAIL));
            return;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ignored) {
            }
        }

        ShopBean shopBean = null;
        String imageName = "";
        AbstractResponse uploadResponse = null;
        try {
            shopBean = Shops.getShop(channelId, cartId);
            if (shopBean == null) {
                throw new BusinessException("appkey信息取得失败");
            }
            // 取得类目id
            String categoryId = "0";
            ImageCategoryType imageCategoryType = getImageCategoryType(String.valueOf(imageType));
            if (imageCategoryType != null) {
                // 看看cms_mt_image_category表里面是否已经存在该图片分类，不存在要在京东图片空间上新建（京东图片分类名不能超过15个字符）
                CmsMtImageCategoryModel model = imageCategoryDao.select(shopBean, imageCategoryType);
                if (model == null) {
                    model = imageCategoryService.createJdImageCategory(shopBean, imageCategoryType, getTaskName());
                }
                categoryId = model.getCategory_tid();
            }

            if (StringUtils.isEmpty(platformUrl)) {
                // 新建的场合
                imageName = originUrl.substring(originUrl.lastIndexOf("/") + 1);
                uploadResponse = jdImgzoneService.uploadPicture("COMMONIMG", originUrl, shopBean, bytes, categoryId, imageName);
            } else {
                // 更新(替换图片)的场合
                uploadResponse = jdImgzoneService.replacePicture(shopBean, bytes, String.valueOf(image.getPlatformImageId()));
            }
            if (uploadResponse == null || !"0".equals(uploadResponse.getCode())) {
                // 新增或替换图片失败的场合
                // 设置返回的错误信息
                if (uploadResponse == null) {
                    $error("上传图片到京东图片空间时，超时, response为空");
                    image.setErrorMsg("上传图片到京东图片空间时，超时, response为空");
                } else{
                    String msg = "上传图片到京东图片空间时，错误:" + uploadResponse.getCode() + ", " + uploadResponse.getZhDesc();
                    $error(msg);
                    image.setErrorMsg(msg);
                }
                // 设置图片上传状态为上传失败
                image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_FAIL));
                return;
            } else {
                // 新增或替换图片成功的场合，回写mongoDB的cms_bt_image_group表
                // 新建的场合
                if (StringUtils.isEmpty(platformUrl)) {
                    String pictureId = ((ImgzonePictureUploadResponse)uploadResponse).getPictureId();
                    String pictureUrl = ((ImgzonePictureUploadResponse)uploadResponse).getPictureUrl();
                    // 加上京东图片地址前缀
                    pictureUrl = "https://img10.360buyimg.com/imgzone/" + pictureUrl;
                    // 设置平台返回的Url
                    image.setPlatformUrl(pictureUrl);
                    // 设置平台返回的PictureId
                    image.setPlatformImageId(pictureId);
                    // 设置图片上传状态为上传成功
                    image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_SUCCESS));
                    // 清除errorMsg
                    image.setErrorMsg(null);
                } else {
                    // 更新(替换图片)的场合
                    // 设置图片上传状态为上传成功
                    image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_SUCCESS));
                    // 清除errorMsg
                    image.setErrorMsg(null);
                }
            }

        } catch (Exception e) {
            $error(e.getMessage());
            // 设置返回的错误信息
            image.setErrorMsg(e.getMessage());
            // 设置图片上传状态为上传成功
            image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_FAIL));
        }
    }

    /**
     * 根据图片类型取得平台上目录类型
     *
     * @param imageType 图片类型
     * @return 平台上目录类型
     */
    private ImageCategoryType getImageCategoryType(String imageType) {
        ImageCategoryType imageCategoryType = null;
        if (CmsConstants.ImageType.SIZE_CHART_IMAGE.equals(imageType)) {
            imageCategoryType = ImageCategoryType.SizeChart;
        } else if (CmsConstants.ImageType.BRAND_STORY_IMAGE.equals(imageType)) {
            imageCategoryType = ImageCategoryType.BrandStory;
        } else if (CmsConstants.ImageType.SHIPPING_DESCRIPTION_IMAGE.equals(imageType)) {
            imageCategoryType = ImageCategoryType.Shipping;
        } else if (CmsConstants.ImageType.STORE_DESCRIPTION_IMAGE.equals(imageType)) {
            imageCategoryType = ImageCategoryType.Store;
        }
        return imageCategoryType;
    }
}