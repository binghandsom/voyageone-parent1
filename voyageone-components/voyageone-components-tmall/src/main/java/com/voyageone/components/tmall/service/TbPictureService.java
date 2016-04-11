package com.voyageone.components.tmall.service;

import com.taobao.api.ApiException;
import com.taobao.api.FileItem;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.TbBase;
import com.voyageone.components.tmall.bean.TbGetPicCategoryParam;
import com.voyageone.components.tmall.bean.TbGetPictureParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 淘宝图片类接口
 * <p>
 * Created by Jonas on 8/10/15.
 */
@Component
public class TbPictureService extends TbBase {
    /**
     * 提交图片到指定目录。
     * <p>
     * 淘宝接口名：taobao.picture.upload
     * 文档地址：http://open.taobao.com/apidoc/api.htm?path=cid:10122-apiId:140
     *
     * @param shopBean    店铺
     * @param file        图片文件
     * @param title       图片标题
     * @param category_id 分类 ID
     * @return 淘宝结果，包含图片信息
     * @throws ApiException
     */
    public PictureUploadResponse uploadPicture(ShopBean shopBean, byte[] file, String title, String category_id) throws ApiException {

        FileItem image = new FileItem(title, file);

        PictureUploadRequest request = new PictureUploadRequest();
        request.setPictureCategoryId(Long.valueOf(category_id));
        request.setImg(image);
        request.setImageInputTitle(title);

        return reqTaobaoApi(shopBean, request);
    }

    /**
     * 获取图片信息
     * 淘宝接口名：taobao.picture.get
     * 文档地址：http://open.taobao.com/apidoc/api.htm?path=cid:10122-apiId:138
     *
     * @param shopBean 店铺
     * @param param    查询参数
     * @return 接口的返回信息
     * @throws ApiException
     */
    public PictureGetResponse getPictures(ShopBean shopBean, TbGetPictureParam param) throws ApiException {

        PictureGetRequest req = new PictureGetRequest();

        req.setTitle(param.getTitle());

        req.setUrls(param.getUrls());

        req.setPageNo(param.getPageNo());

        req.setPageSize(param.getPageSize());

        req.setPictureCategoryId(param.getPictureCategoryId());

        req.setPictureId(param.getPictureId());

        return reqTaobaoApi(shopBean, req);
    }

    /**
     * 获取图片信息
     * 淘宝接口名：taobao.picture.get
     *
     * @param shopBean    店铺
     * @param title       图片名
     * @param category_id 目录 id
     * @return 接口的返回信息
     * @throws ApiException
     */
    public PictureGetResponse getPictures(ShopBean shopBean, String title, Long category_id) throws ApiException {

        TbGetPictureParam param = new TbGetPictureParam();

        param.setTitle(title);

        param.setPictureCategoryId(category_id);

        return getPictures(shopBean, param);
    }

    /**
     * 获取图片信息
     * 淘宝接口名：taobao.picture.delete
     * 文档地址：http://open.taobao.com/apidoc/api.htm?path=cid:10122-apiId:139
     */
    public PictureDeleteResponse deletePictures(ShopBean shopBean, Long... pictureIds) throws ApiException {
        if (pictureIds.length < 1)
            return null;

        PictureDeleteRequest req = new PictureDeleteRequest();
        req.setPictureIds(StringUtils.join(pictureIds, ","));

        return reqTaobaoApi(shopBean, req);
    }

    /**
     * 查询淘宝的图片分类
     * 淘宝接口名：taobao.picture.category.get
     *
     * @param shopBean 店铺
     * @param param    参数
     * @return 接口的返回信息
     * @throws ApiException
     */
    public PictureCategoryGetResponse getCategories(ShopBean shopBean, TbGetPicCategoryParam param) throws ApiException {

        PictureCategoryGetRequest req = new PictureCategoryGetRequest();

        req.setPictureCategoryId(param.getPictureCategoryId());

        req.setPictureCategoryName(param.getPictureCategoryName());

        req.setType(param.getType());

        req.setParentId(param.getParentId());

        return reqTaobaoApi(shopBean, req);
    }

    /**
     * 在淘宝的图片空间中创建目录
     * 淘宝接口名：taobao.picture.category.add
     *
     * @param shopBean 店铺
     * @param name     目录名称
     * @param parent   父目录 id
     * @return 接口的返回结果
     */
    public PictureCategoryAddResponse addCategory(ShopBean shopBean, String name, Long parent) throws ApiException {

        PictureCategoryAddRequest req = new PictureCategoryAddRequest();

        req.setPictureCategoryName(name);
        req.setParentId(parent);

        return reqTaobaoApi(shopBean, req);
    }

    /**
     * 在淘宝的图片空间中创建目录
     * 淘宝接口名：taobao.picture.category.add
     *
     * @param shopBean 店铺
     * @param name     目录名称
     * @return 接口的返回结果
     */
    public PictureCategoryAddResponse addCategory(ShopBean shopBean, String name) throws ApiException {
        return addCategory(shopBean, name, 0L);
    }

    /**
     * 添加商品图片
     * <p>
     * 淘宝接口名：taobao.item.img.upload
     * 文档地址：http://open.taobao.com/doc2/apiDetail.htm?apiId=23
     *
     * @throws ApiException
     */
    public ItemImgUploadResponse uploadItemPicture(ShopBean shopBean, ItemImgUploadRequest request) throws ApiException {
        return reqTaobaoApi(shopBean, request);
    }

    /**
     * 删除商品图片
     * <p>
     * 淘宝接口名：taobao.item.img.delete
     * 文档地址：http://open.taobao.com/doc2/apiDetail.htm?apiId=24
     *
     * @throws ApiException
     */
    public ItemImgDeleteResponse deleteItemPicture(ShopBean shopBean, ItemImgDeleteRequest request) throws ApiException {
        return reqTaobaoApi(shopBean, request);
    }
}
