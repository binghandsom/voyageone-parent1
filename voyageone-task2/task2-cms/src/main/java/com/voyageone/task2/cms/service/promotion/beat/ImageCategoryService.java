package com.voyageone.task2.cms.service.promotion.beat;

import com.taobao.api.ApiException;
import com.taobao.api.domain.PictureCategory;
import com.taobao.api.response.PictureCategoryGetResponse;
import com.voyageone.cms.enums.ImageCategoryType;
import com.voyageone.common.components.tmall.TbPictureService;
import com.voyageone.common.components.tmall.bean.TbGetPicCategoryParam;
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
}
