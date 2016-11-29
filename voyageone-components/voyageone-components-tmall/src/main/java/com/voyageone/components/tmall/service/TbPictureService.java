package com.voyageone.components.tmall.service;

import com.taobao.api.ApiException;
import com.taobao.api.FileItem;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.components.tmall.TbBase;
import com.voyageone.components.tmall.bean.TbGetPicCategoryParam;
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
     * 普通的Appkey转官网同购上新用的Appkey
     * @param shopBean 普通的Appkey
     * @return 官网同购的Appkey
     */
    private ShopBean getTonggouShopBean(ShopBean shopBean) {
        // 这里需要判断一下， 如果不是官网同购， 那么就直接原样返回
        if (!CartEnums.Cart.isSimple(CartEnums.Cart.getValueByID(shopBean.getCart_id()))) {
            return shopBean;
        }

        ShopBean newShopBean = new ShopBean();

        String tm_tt_sx = "tm_tt_sx";
        ThirdPartyConfigBean shopConfig = ThirdPartyConfigs.getThirdPartyConfig(shopBean.getOrder_channel_id(), tm_tt_sx);

        newShopBean.setCart_id(shopBean.getCart_id());
        newShopBean.setCart_type(shopBean.getCart_type());
        newShopBean.setPlatform_id(shopBean.getPlatform_id());
        newShopBean.setOrder_channel_id(shopBean.getOrder_channel_id());
        if (shopConfig != null) {
            newShopBean.setApp_url(shopConfig.getProp_val1());
            newShopBean.setAppKey(shopConfig.getProp_val2());
            newShopBean.setAppSecret(shopConfig.getProp_val3());
            newShopBean.setSessionKey(shopConfig.getProp_val4());
        }
        newShopBean.setShop_name(shopBean.getShop_name());
        newShopBean.setPlatform(shopBean.getPlatform());
        newShopBean.setComment(shopBean.getComment());
        newShopBean.setCart_name(shopBean.getCart_name());

        return newShopBean;
    }

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

        return reqTaobaoApi(getTonggouShopBean(shopBean), request);
    }

    /**
     * 替换图片
     * <p>
     * 淘宝接口名：taobao.picture.replace
     * 文档地址：http://open.taobao.com/doc2/apiDetail.htm?spm=a219a.7395905.0.0.Ik1I5r&apiId=10910
     *
     * @param shopBean    店铺
     * @param title       图片标题
     * @param pictureId 图片ID
     * @return 淘宝结果，包含图片信息
     * @throws ApiException
     */
    public PictureReplaceResponse replacePicture(ShopBean shopBean, byte[] file, String title, Long pictureId) throws ApiException {

        FileItem image = new FileItem(title, file);

        PictureReplaceRequest request = new PictureReplaceRequest();
        request.setPictureId(pictureId);
        request.setImageData(image);
        return reqTaobaoApi(getTonggouShopBean(shopBean), request);
    }

    /**
     * 获取图片信息
     * 淘宝接口名：taobao.picture.get
     * 文档地址：http://open.taobao.com/apidoc/api.htm?path=cid:10122-apiId:138
     */
    public PictureGetResponse getPictures(ShopBean shopBean, String title, Long category_id) throws ApiException {

        PictureGetRequest req = new PictureGetRequest();

        req.setTitle(title);

        req.setPictureCategoryId(category_id);

        return reqTaobaoApi(getTonggouShopBean(shopBean), req);
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

        return reqTaobaoApi(getTonggouShopBean(shopBean), req);
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

        return reqTaobaoApi(getTonggouShopBean(shopBean), req);
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

        return reqTaobaoApi(getTonggouShopBean(shopBean), req);
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
        return addCategory(getTonggouShopBean(shopBean), name, 0L);
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
        return reqTaobaoApi(getTonggouShopBean(shopBean), request);
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
        return reqTaobaoApi(getTonggouShopBean(shopBean), request);
    }
}
