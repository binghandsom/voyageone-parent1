package com.voyageone.task2.cms.service.promotion.beat;

import com.taobao.api.ApiException;
import com.taobao.api.domain.PictureCategory;
import com.taobao.api.response.PictureCategoryAddResponse;
import com.taobao.api.response.PictureCategoryGetResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.model.cms.enums.ImageCategoryType;
import com.voyageone.components.tmall.service.TbPictureService;
import com.voyageone.components.tmall.bean.TbGetPicCategoryParam;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.util.StringUtils;
import com.voyageone.task2.cms.dao.CmsMtImageCategoryDao;
import com.voyageone.task2.cms.model.CmsMtImageCategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public CmsMtImageCategoryModel getCategory(ShopBean shopBean, ImageCategoryType type) {

        CmsMtImageCategoryModel categoryModel = imageCategoryDao.select(shopBean, type);

        if (categoryModel == null) return null;

        if (!StringUtils.isEmpty(categoryModel.getCategory_tid())) return categoryModel;

        TbGetPicCategoryParam param = new TbGetPicCategoryParam();
        param.setPictureCategoryName(categoryModel.getCategory_name());

        try {
            PictureCategoryGetResponse res = tbPictureService.getCategories(shopBean, param);
            List<PictureCategory> categories = res.getPictureCategories();
            if (res.isSuccess() && categories != null && categories.size() > 0) {
                String tid = String.valueOf(categories.get(0).getPictureCategoryId());
                categoryModel.setCategory_tid(tid);
                imageCategoryDao.updateTid(categoryModel);
                return categoryModel;
            }
            $info("图片空间服务 -> 调用接口尝试获取目录失败 [ %s ] [ %s ] [ %s ]", res.getSubCode(),
                    res.getSubMsg(), categoryModel.getCategory_name());
            return null;
        } catch (ApiException e) {
            $info("图片空间服务 -> 调用接口尝试获取目录出现错误 [ %s ] [ %s ] [ %s ] [ %s ] [ %s ]",
                    e.getLocalizedMessage(), e.getErrMsg(), shopBean.getOrder_channel_id(), shopBean.getCart_id(),
                    categoryModel.getCategory_name());
            return null;
        }
    }

    public CmsMtImageCategoryModel createImageCategory(ShopBean shopBean, ImageCategoryType type, String userName) throws ApiException {

        String categoryId = "0";
        String CategoryName = type.name() + "_" + shopBean.getCart_id() + "_" + shopBean.getOrder_channel_id();

        // 获取目录
        TbGetPicCategoryParam param = new TbGetPicCategoryParam();
        param.setPictureCategoryName(CategoryName);
        try {
            PictureCategoryGetResponse getResponse = tbPictureService.getCategories(shopBean, param);
            if (getResponse.isSuccess()) {
                if (getResponse.getPictureCategories() != null && getResponse.getPictureCategories().size() > 0) {
                    categoryId = String.valueOf(getResponse.getPictureCategories().get(0).getPictureCategoryId());
                } else {
                    // 获取目录不存在的情况下，增加目录
                    PictureCategoryAddResponse addResponse = null;
                    try {
                        addResponse = tbPictureService.addCategory(shopBean, CategoryName);
                        if (addResponse.isSuccess() && addResponse!= null && addResponse.getPictureCategory() != null && addResponse.getPictureCategory().getPictureCategoryId() != null) {
                            categoryId =  String.valueOf(addResponse.getPictureCategory().getPictureCategoryId());
                        } else if (!addResponse.isSuccess()) {
                            String msg = "图片空间服务 -> 调用接口尝试新增图片分类信息失败，错误:" + addResponse.getErrorCode() + ", " + addResponse.getMsg();
                            if (!StringUtils.isEmpty(addResponse.getSubMsg())) {
                                msg += addResponse.getSubMsg();
                            }
                            throw new BusinessException(msg);
                        }
                    }  catch (ApiException e) {
                        throw new BusinessException("图片空间服务 -> 调用接口尝试新增图片分类信息失败，错误:" + e.getErrCode() + ", " + e.getErrMsg());
                    }
                }
            } else {
                String msg = "图片空间服务 -> 调用接口尝试获取目录出现错误，错误:" + getResponse.getErrorCode() + ", " + getResponse.getMsg();
                if (!StringUtils.isEmpty(getResponse.getSubMsg())) {
                    msg += getResponse.getSubMsg();
                }
                throw new BusinessException(msg);
            }
        } catch (ApiException e) {
            throw new BusinessException("图片空间服务 -> 调用接口尝试获取目录出现错误，错误:" + e.getErrCode() + ", " + e.getErrMsg());
        }

        CmsMtImageCategoryModel model = new CmsMtImageCategoryModel();
        model.setCart_id(Integer.parseInt(shopBean.getCart_id()));
        model.setCategory_tid(categoryId);
        model.setCategory_name(CategoryName);
        model.setChannel_id(shopBean.getOrder_channel_id());
        model.setType(type.getVal());
        model.setCreater(userName);
        model.setModifier(userName);
        imageCategoryDao.insert(model);
        return model;
    }
}
