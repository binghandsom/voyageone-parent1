package com.voyageone.service.impl.cms.jumei.platefrom;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.MD5;

import com.voyageone.components.jumei.bean.JmImageFileBean;
import com.voyageone.components.jumei.enums.JumeiImageType;
import com.voyageone.components.jumei.service.JumeiImageFileService;
import com.voyageone.service.model.jumei.CmsBtJmProductImagesModel;
import com.voyageone.service.model.jumei.CmsMtMasterInfoModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.util.regex.Pattern;
@Service
public class JuMeiUploadImageService {
    /* 获取图片下载流重试次数 */
    private static final int GET_IMG_INPUTSTREAM_RETRY = 5;
    /* 聚美dir斜杠分隔符 */
    private static final String SLASH = "/";
    /* 调用聚美Api相同是否替换 */
    private static final boolean NEED_REPLACE = true;
    private static final Pattern special_symbol= Pattern.compile("[~@'\\s.:#$%&*_''/‘’^\\()]");
    @Autowired
    private JumeiImageFileService jumeiImageFileService;
@Autowired
JMShopBeanService serviceJMShopBean;
    @Autowired
    JuMeiProductUpdateService serviceJuMeiProductUpdate;
    private static final Logger LOG = LoggerFactory.getLogger(JuMeiUploadImageService.class);

    public void uploadImage(CmsBtJmProductImagesModel imageModel, ShopBean shopBean) throws Exception {
        String jmUrl = jumeiImageFileService.imageFileUpload(shopBean, convertJmPicToImageFileBean(imageModel));
        imageModel.setJmUrl(jmUrl);
    }
    public void uploadImage(CmsMtMasterInfoModel imageModel, ShopBean shopBean) throws Exception {
        String jmUrl = jumeiImageFileService.imageFileUpload(shopBean, convertJmPicToImageFileBean(imageModel));
        imageModel.setValue2(jmUrl);
    }
    public void uploadImage(CmsMtMasterInfoModel imageModel) throws Exception {
        ShopBean shopBean = serviceJMShopBean.getShopBean(imageModel.getChannelId());
        String jmUrl = jumeiImageFileService.imageFileUpload(shopBean, convertJmPicToImageFileBean(imageModel));
        imageModel.setValue2(jmUrl);
    }
    private static JmImageFileBean convertJmPicToImageFileBean(CmsBtJmProductImagesModel imageModel) throws Exception {
        JmImageFileBean jmImageFileBean = new JmImageFileBean();
        int retryCount = GET_IMG_INPUTSTREAM_RETRY;
        InputStream inputStream = getImgInputStream(imageModel.getOriginUrl(), retryCount);
        Assert.notNull(inputStream, "inputStream为null，图片流获取失败！" + imageModel.getOriginUrl());
        jmImageFileBean.setInputStream(inputStream);
        jmImageFileBean.setDirName(buildDirName(imageModel));
        jmImageFileBean.setImgName(special_symbol.matcher(imageModel.getProductCode()).replaceAll("") + imageModel.getImageType() + "_" + imageModel.getImageIndex()/*+IMGTYPE*/);
        jmImageFileBean.setNeedReplace(NEED_REPLACE);
        jmImageFileBean.setExtName("jpg");
        return jmImageFileBean;
    }
    private static JmImageFileBean convertJmPicToImageFileBean(CmsMtMasterInfoModel imageModel) throws Exception {
        JmImageFileBean jmImageFileBean = new JmImageFileBean();
//            File imageFile=new File(jmPicBean.getOriginUrl());
        int retryCount = GET_IMG_INPUTSTREAM_RETRY;
       System.out.println("id:"+imageModel.getId()+"  value1"+imageModel.getValue1());
        InputStream inputStream = getImgInputStream(imageModel.getValue1(), retryCount);
        Assert.notNull(inputStream, "inputStream为null，图片流获取失败！" + imageModel.getValue1());
        jmImageFileBean.setInputStream(inputStream);
        jmImageFileBean.setDirName(buildDirName(imageModel));
        if (imageModel.getDataType() == JumeiImageType.BRANDSTORY.getId() || imageModel.getDataType() == JumeiImageType.SIZE.getId()) {
            jmImageFileBean.setImgName(MD5.getMD5(imageModel.getBrandName()+imageModel.getSizeType()) + "_" + imageModel.getDataType() + "_" + imageModel.getImageIndex() /*+IMGTYPE*/);
        } else {
            jmImageFileBean.setImgName(MD5.getMD5(special_symbol.matcher(imageModel.getBrandName()).replaceAll("")) + imageModel.getDataType() + "_" + imageModel.getImageIndex()/*+IMGTYPE*/);
        }
        jmImageFileBean.setNeedReplace(NEED_REPLACE);
        jmImageFileBean.setExtName("jpg");
        return jmImageFileBean;
    }

//    private static JmImageFileBean convertJmPicToImageFileBean(JmPicBean jmPicBean) throws Exception {
//        JmImageFileBean jmImageFileBean = new JmImageFileBean();
////            File imageFile=new File(jmPicBean.getOriginUrl());
//        int retryCount = GET_IMG_INPUTSTREAM_RETRY;
//        InputStream inputStream = getImgInputStream(jmPicBean.getOriginUrl(), retryCount);
//        Assert.notNull(inputStream, "inputStream为null，图片流获取失败！" + jmPicBean.getOriginUrl());
//        jmImageFileBean.setInputStream(inputStream);
//        jmImageFileBean.setDirName(buildDirName(jmPicBean));
//        if (jmPicBean.getImageType() == JumeiImageType.BRANDSTORY.getId() || jmPicBean.getImageType() == JumeiImageType.SIZE.getId()) {
//            jmImageFileBean.setImgName(MD5.getMD5(jmPicBean.getImageKey()+jmPicBean.getImageTypeExtend()) + "_" + jmPicBean.getImageType() + "_" + jmPicBean.getImageIndex() /*+IMGTYPE*/);
//        } else {
//            jmImageFileBean.setImgName(special_symbol.matcher(jmPicBean.getImageKey()).replaceAll("") + jmPicBean.getImageType() + "_" + jmPicBean.getImageIndex()/*+IMGTYPE*/);
//        }
//        jmImageFileBean.setNeedReplace(NEED_REPLACE);
//        jmImageFileBean.setExtName("jpg");
//        return jmImageFileBean;
//    }
    /***
     * 按照规则构造远程路径
     *
     * @param  imageModel
     * @return 远程dir
     */
    private static String buildDirName(CmsMtMasterInfoModel imageModel) {
        Assert.notNull(imageModel);
       // checkFiled(imageModel.getChannelId());
        if(org.springframework.util.StringUtils.isEmpty(imageModel.getChannelId()))
        {
            throw  new IllegalArgumentException("CmsMtMasterInfoModel.ChannelId"+"不能为空,id="+imageModel.getId());
        }
        if (imageModel.getDataType() <= 3) {
            return SLASH + imageModel.getChannelId() + SLASH + "product" + SLASH + imageModel.getDataType() + SLASH + special_symbol.matcher(imageModel.getBrandName()).replaceAll("");
        } else if (imageModel.getDataType() <= 5) {
//            return SLASH+jmPicBean.getChannelId()+SLASH+jmPicBean.getPicType()+SLASH+jmPicBean.getImageKey().replace(SLASH,"_")+SLASH+jmPicBean.getImageType();
            return SLASH + imageModel.getChannelId() + SLASH + "brand" + SLASH + imageModel.getDataType();
        }
        return SLASH + imageModel.getChannelId() + SLASH + "channel";
    }
    /**
     * 获取网络图片流，遇错重试
     *
     * @param url   imgUrl
     * @param retry retrycount
     * @return inputStream / throw Exception
     */
    private static InputStream getImgInputStream(String url, int retry) throws Exception {
        Exception exception = null;
        if (--retry > 0) {
            try {
                return HttpUtils.getInputStream(url, null);
            } catch (Exception e) {
                exception = e;
                getImgInputStream(url, retry);
            }
        }
        throw new Exception("获取网络图片流失败,url:"+url);
    }
    /***
     * 按照规则构造远程路径
     *
     * @param  imageModel
     * @return 远程dir
     */
    private static String buildDirName(CmsBtJmProductImagesModel imageModel) {
        Assert.notNull(imageModel);
       // checkFiled(imageModel.getChannelId(), imageModel.getImageTypeName());
        if(org.springframework.util.StringUtils.isEmpty(imageModel.getChannelId()))
        {
            throw  new IllegalArgumentException("CmsBtJmProductImagesModel.ChannelId"+"不能为空,id="+imageModel.getId());
        }
        if (imageModel.getImageType() <= 3) {
            return SLASH + imageModel.getChannelId() + SLASH + "product" + SLASH + imageModel.getImageType() + SLASH + special_symbol.matcher(imageModel.getImageTypeName()).replaceAll("");
        } else if (imageModel.getImageType() <= 5) {
            return SLASH + imageModel.getChannelId() + SLASH + "brand" + SLASH + imageModel.getImageType();
        }
        return SLASH + imageModel.getChannelId() + SLASH + "channel";
    }
//    /***
//     * 按照规则构造远程路径
//     *
//     * @param jmPicBean jmPicBean
//     * @return 远程dir
//     */
//    private static String buildDirName(JmPicBean jmPicBean) {
//        Assert.notNull(jmPicBean);
//        checkFiled(jmPicBean.getChannelId(), jmPicBean.getImageKey());
//        if (jmPicBean.getImageType() <= 3) {
//            return SLASH + jmPicBean.getChannelId() + SLASH + "product" + SLASH + jmPicBean.getImageType() + SLASH + special_symbol.matcher(jmPicBean.getImageKey()).replaceAll("");
//        } else if (jmPicBean.getImageType() <= 5) {
////            return SLASH+jmPicBean.getChannelId()+SLASH+jmPicBean.getPicType()+SLASH+jmPicBean.getImageKey().replace(SLASH,"_")+SLASH+jmPicBean.getImageType();
//            return SLASH + jmPicBean.getChannelId() + SLASH + "brand" + SLASH + jmPicBean.getImageType();
//        }
//        return SLASH + jmPicBean.getChannelId() + SLASH + "channel";
//    }
    /**
     * 校验
     *
     * @param fileds fileds
     */
//    private static void checkFiled(String... fileds) {
//        for (String filed : fileds) {
//            if (StringUtils.isEmpty(filed)) {
//                throw new IllegalArgumentException("参数校验不通过filed:" + filed);
//            }
//        }
//    }
}
