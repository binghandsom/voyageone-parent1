package com.voyageone.components.jd.service;

import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.request.imgzone.*;
import com.jd.open.api.sdk.response.AbstractResponse;
import com.jd.open.api.sdk.response.imgzone.*;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.JdBase;
import org.springframework.stereotype.Component;

/**
 * 京东图片空间系统API调用服务
 *
 * @author desmond on 2016/07/25.
 * @version 2.3.0
 * @since 2.3.0
 */
@Component
public class JdImgzoneService extends JdBase {

    /**
     * 上传图片到京东图片空间
     *
     * @param shop ShopBean        店铺信息
     * @param imageData byte[]     上传图片文件流
     * @param pictureCateId String 图片分类ID
     * @param pictureName String   图片名
     * @return ImgzonePictureUploadResponse 京东图片上传结果，包含图片信息
     * @throws JdException
     */
    public ImgzonePictureUploadResponse  uploadPicture(ShopBean shop, byte[] imageData, String pictureCateId, String pictureName) throws JdException {

        if (imageData == null || imageData.length == 0) {
            throw new BusinessException("要上传到京东图片空间的图片imageData为空!");
        }

        ImgzonePictureUploadRequest request = new ImgzonePictureUploadRequest();
        // 图片二进制文件流，允许png、jpg、gif、jpeg、bmp图片格式，1M以内 (必须)
        request.setImageData(imageData);
        // 上传到的图片分类ID，为空上传至 默认分类 (非必须)
        if (!StringUtils.isEmpty(pictureCateId))   request.setPictureCateId(Long.parseLong(pictureCateId));
        // 图片名称，不超过64字节，为空默认 未命名 (非必须)
        if (!StringUtils.isEmpty(pictureName))     request.setPictureName(pictureName);


        // 调用京东上传单张图片API(jingdong.imgzone.picture.upload)
        ImgzonePictureUploadResponse response = reqApi(shop, request);

        return response;
    }

    /**
     * 替换京东图片空间上的图片
     *
     * @param shop ShopBean        店铺信息
     * @param imageData byte[]     上传图片文件流
     * @param pictureId String     图片ID
     * @return ImgzonePictureReplaceResponse 京东图片替换结果
     * @throws JdException
     */
    public AbstractResponse replacePicture(ShopBean shop, byte[] imageData, String pictureId) throws JdException {

        if (imageData == null || imageData.length == 0) {
            throw new BusinessException("要替换到京东图片空间的图片imageData为空!");
        }

        ImgzonePictureReplaceRequest request = new ImgzonePictureReplaceRequest();
        // 图片二进制文件流，允许png、jpg、gif、jpeg、bmp图片格式，1M以内 (必须)
        request.setImageData(imageData);
        // 上传到的图片分类ID，为空上传至 默认分类 (非必须)
        if (!StringUtils.isEmpty(pictureId))      request.setPictureId(pictureId);

        // 调用京东替换单张图片API(jingdong.imgzone.picture.replace)
        ImgzonePictureReplaceResponse response = reqApi(shop, request);

        return response;
    }

    public ImgzonePictureDeleteResponse deletePictures(ShopBean shop, String pictureId) throws JdException {

        ImgzonePictureDeleteRequest request=new ImgzonePictureDeleteRequest();

        request.setPictureIds( pictureId );

        ImgzonePictureDeleteResponse response = reqApi(shop, request);

        return response;
    }

    /**
     * 京东图片空间查询图片分类
     *
     * @param shop ShopBean        店铺信息
     * @param cateId String        分类ID
     * @param cateName String      分类名称，不支持模糊查询
     * @param parentCateId String  父分类ID，查询二级分类时为对应父分类id，查询一级分类时为0，查询全部分类的时候为空
     * @return ImgzoneCategoryQueryResponse 京东查询图片分类结果
     * @throws JdException
     */
    public ImgzoneCategoryQueryResponse getPictureCategories(ShopBean shop, String cateId, String cateName, String parentCateId) throws JdException {
        ImgzoneCategoryQueryRequest request = new ImgzoneCategoryQueryRequest();

        // 分类ID(非必须)
        if (!StringUtils.isEmpty(cateId))        request.setCateId(Long.parseLong(cateId));
        // 分类名称，不支持模糊查询 (非必须)
        if (!StringUtils.isEmpty(cateName))      request.setCateName(cateName);
        // 父分类ID，查询二级分类时为对应父分类id，查询一级分类时为0，查询全部分类的时候为空(非必须)
        if (!StringUtils.isEmpty(parentCateId))  request.setParentCateId(Long.parseLong(parentCateId));

        // 调用京东查询图片分类API(jingdong.imgzone.category.query)
        ImgzoneCategoryQueryResponse response = reqApi(shop, request);

        return response;
    }

    /**
     * 京东图片空间添加图片分类
     *
     * @param shop ShopBean        店铺信息
     * @param cateName String      分类名称，自动过滤特殊字符
     * @param parentCateId String  父分类ID，为空默认添加顶级分类
     * @return ImgzoneCategoryAddResponse 京东添加图片分类结果
     * @throws JdException
     */
    public ImgzoneCategoryAddResponse addPictureCategory(ShopBean shop, String cateName, String parentCateId) throws JdException {
        ImgzoneCategoryAddRequest request = new ImgzoneCategoryAddRequest();

        // 分类名称，自动过滤特殊字符(非必须)
        if (!StringUtils.isEmpty(cateName))      request.setCateName(cateName);
        // 父分类ID，为空默认添加顶级分类(非必须)
        if (!StringUtils.isEmpty(parentCateId))  request.setParentCateId(Long.parseLong(parentCateId));

        // 调用京东添加图片分类API(jingdong.imgzone.category.add)
        ImgzoneCategoryAddResponse response = reqApi(shop, request);

        return response;
    }

}
