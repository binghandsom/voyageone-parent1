package com.voyageone.task2.cms.service.promotion.beat;

import com.jd.open.api.sdk.response.imgzone.ImgzoneCategoryAddResponse;
import com.jd.open.api.sdk.response.imgzone.ImgzoneCategoryQueryResponse;
import com.taobao.api.ApiException;
import com.taobao.api.domain.PictureCategory;
import com.taobao.api.response.PictureCategoryAddResponse;
import com.taobao.api.response.PictureCategoryGetResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.service.JdImgzoneService;
import com.voyageone.components.tmall.bean.TbGetPicCategoryParam;
import com.voyageone.components.tmall.service.TbPictureService;
import com.voyageone.service.model.cms.enums.ImageCategoryType;
import com.voyageone.task2.cms.dao.CmsMtImageCategoryDao;
import com.voyageone.task2.cms.model.CmsMtImageCategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by jonasvlag on 16/3/7.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class ImageCategoryService extends VOAbsLoggable {

    @Autowired
    private CmsMtImageCategoryDao imageCategoryDao;

    @Autowired
    private TbPictureService tbPictureService;

    @Autowired
    private JdImgzoneService jdImgzoneService;

    public CmsMtImageCategoryModel getCategory(ShopBean shopBean, ImageCategoryType type) throws ApiException {

        CmsMtImageCategoryModel categoryModel = imageCategoryDao.select(shopBean, type);

        if (categoryModel != null) {
            if (isNotBlank(categoryModel.getCategory_tid())) {
                return categoryModel;
            }
            // category_tid 为空，那么这个配置就毫无作用
            // 这里直接删除数据，交由下一步自动创建
            imageCategoryDao.delete(categoryModel.getCategory_id());
        }

        return createImageCategory(shopBean, type, "ImageCategoryService#getCategory");
    }

    public CmsMtImageCategoryModel createImageCategory(ShopBean shopBean, ImageCategoryType type, String userName) throws ApiException {

        String categoryId;
        String categoryName = type.name() + "_" + shopBean.getCart_id() + "_" + shopBean.getOrder_channel_id();

        // 获取目录
        TbGetPicCategoryParam param = new TbGetPicCategoryParam();
        param.setPictureCategoryName(categoryName);
        try {
            PictureCategoryGetResponse getResponse = tbPictureService.getCategories(shopBean, param);
            if (getResponse.isSuccess()) {
                if (getResponse.getPictureCategories() != null && getResponse.getPictureCategories().size() > 0) {
                    categoryId = String.valueOf(getResponse.getPictureCategories().get(0).getPictureCategoryId());
                } else {
                    categoryId = createImageCategory(shopBean, categoryName);
                }
            } else {
                String msg = "图片空间服务 -> 调用接口尝试获取目录出现错误，错误:" + getResponse.getErrorCode() + ", " + getResponse.getMsg();

                if (!StringUtils.isEmpty(getResponse.getSubMsg()))
                    msg += getResponse.getSubMsg();

                throw new BusinessException(msg);
            }
        } catch (ApiException e) {

            throw new BusinessException("图片空间服务 -> 调用接口尝试获取目录出现错误，错误:" + e.getErrCode() + ", " + e.getErrMsg());
        }

        CmsMtImageCategoryModel model = new CmsMtImageCategoryModel();
        model.setCart_id(Integer.parseInt(shopBean.getCart_id()));
        model.setCategory_tid(categoryId);
        model.setCategory_name(categoryName);
        model.setChannel_id(shopBean.getOrder_channel_id());
        model.setType(type.getVal());
        model.setCreater(userName);
        model.setModifier(userName);
        imageCategoryDao.insert(model);

        return model;
    }

    private String createImageCategory(ShopBean shopBean, String categoryName) {

        try {
            PictureCategoryAddResponse addResponse = tbPictureService.addCategory(shopBean, categoryName);

            if (addResponse == null)
                return null;

            if (addResponse.isSuccess()) {

                PictureCategory pictureCategory = addResponse.getPictureCategory();

                if (pictureCategory != null)
                    return String.valueOf(pictureCategory.getPictureCategoryId());
                else
                    throw new BusinessException("图片空间服务 -> 调用接口尝试新增图片分类信息失败。");

            } else {

                String msg = "图片空间服务 -> 调用接口尝试新增图片分类信息失败，错误:" + addResponse.getErrorCode() + ", " + addResponse.getMsg();

                if (!StringUtils.isEmpty(addResponse.getSubMsg())) {
                    msg += addResponse.getSubMsg();
                }
                throw new BusinessException(msg);
            }
        } catch (ApiException e) {
            throw new BusinessException("图片空间服务 -> 调用接口尝试新增图片分类信息失败，错误:" + e.getErrCode() + ", " + e.getErrMsg());
        }
    }

    /**
     * 取得京东图片空间图片分类
     *
     * @param shopBean ShopBean        店铺信息
     * @param type     ImageCategoryType  图片分类（京东图片分类名不能超过15个字符）
     * @param userName String  userName
     * @return CmsMtImageCategoryModel 京东图片分类
     */
    public CmsMtImageCategoryModel createJdImageCategory(ShopBean shopBean, ImageCategoryType type, String userName) {

        String categoryId;
        // 京东图片分类名不能超过15个字符
        String categoryName = type.name();

        // 根据图片分类名称查询京东图片空间是否已经存在该图片分类
        try {
            ImgzoneCategoryQueryResponse getResponse = jdImgzoneService.getPictureCategories(shopBean, null, categoryName, null);
            if (getResponse != null && getResponse.getReturnCode() == 1) {
                // 查询图片分类成功的时候
                if (ListUtils.notNull(getResponse.getCateList())) {
                    // 京东图片空间已经存在该图片分类时，直接取得该图片分类ID
                    categoryId = String.valueOf(getResponse.getCateList().get(0).getCateId());
                } else {
                    // 京东图片空间不存在该图片分类时，向平台添加该图片分类并取得平台返回的图片分类ID
                    categoryId = createJdImageCategory(shopBean, categoryName);
                }
            } else {
                String msg = "京东图片空间服务 -> 调用接口尝试获取图片分类ID出现错误，错误:" + getResponse.getCode() + ", " + getResponse.getDesc1();
                throw new BusinessException(msg);
            }
        } catch (Exception e) {
            String errMsg = "京东图片空间服务 -> 调用接口尝试获取图片分类ID出现异常，异常信息:";
            if (StringUtils.isNullOrBlank2(e.getMessage())) {
                errMsg = errMsg + e.getStackTrace()[0].toString();
            } else {
                errMsg = e.getMessage();
            }

            throw new BusinessException(errMsg);
        }

        // 取得或添加京东平台图片分类ID成功时，回写cms_mt_image_category表
        CmsMtImageCategoryModel model = new CmsMtImageCategoryModel();
        model.setCart_id(Integer.parseInt(shopBean.getCart_id()));
        model.setCategory_tid(categoryId);
        model.setCategory_name(categoryName);
        model.setChannel_id(shopBean.getOrder_channel_id());
        model.setType(type.getVal());
        model.setCreater(userName);
        model.setModifier(userName);
        imageCategoryDao.insert(model);

        return model;
    }

    /**
     * 京东图片空间添加图片分类
     *
     * @param shopBean     ShopBean        店铺信息
     * @param categoryName String  图片分类名
     * @return String 京东图片分类id
     */
    private String createJdImageCategory(ShopBean shopBean, String categoryName) {

        try {
            // 向京东图片空间添加该图片分类
            ImgzoneCategoryAddResponse addResponse = jdImgzoneService.addPictureCategory(shopBean, categoryName, null);

            if (addResponse != null && addResponse.getReturnCode() == 1) {
                // 添加图片分类成功时
                String pictureCategoryId = String.valueOf(addResponse.getCateId());

                if (!StringUtils.isEmpty(pictureCategoryId))
                    return pictureCategoryId;
                else
                    throw new BusinessException("京东图片空间服务 -> 调用接口尝试新增图片分类信息失败。");

            } else {
                // 添加图片分类失败时
                String msg = "京东图片空间服务 -> 调用接口尝试新增图片分类信息失败，错误:" + addResponse.getCode() + ", " + addResponse.getDesc();
                throw new BusinessException(msg);
            }
        } catch (Exception e) {
            String errMsg = "京东图片空间服务 -> 调用接口尝试新增图片分类信息失败，异常信息:";
            if (StringUtils.isNullOrBlank2(e.getMessage())) {
                errMsg = errMsg + e.getStackTrace()[0].toString();
            } else {
                errMsg = e.getMessage();
            }

            throw new BusinessException(errMsg);
        }
    }
}
