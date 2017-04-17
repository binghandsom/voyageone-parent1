package com.voyageone.components.jd.service;

import com.jd.open.api.sdk.domain.sellercat.ShopCategory;
import com.jd.open.api.sdk.request.sellercat.SellerCatAddRequest;
import com.jd.open.api.sdk.request.sellercat.SellerCatDeleteRequest;
import com.jd.open.api.sdk.request.sellercat.SellerCatUpdateRequest;
import com.jd.open.api.sdk.request.sellercat.SellerCatsGetRequest;
import com.jd.open.api.sdk.response.sellercat.SellerCatAddResponse;
import com.jd.open.api.sdk.response.sellercat.SellerCatDeleteResponse;
import com.jd.open.api.sdk.response.sellercat.SellerCatUpdateResponse;
import com.jd.open.api.sdk.response.sellercat.SellerCatsGetResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jd.JdBase;
import com.voyageone.components.jd.JdConstants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 京东店铺类API调用服务
 * <p/>
 * Created by desmond on 2016/4/19.
 */
@Component
public class JdShopService extends JdBase {

    /**
     * 京东获取前台展示的商家自定义店内分类
     *
     * @param shop ShopBean      店铺信息
     * @return List<ShopCategory>  京东店铺分类列表
     */
    public List<ShopCategory> getShopCategoryList(ShopBean shop) throws BusinessException {
        SellerCatsGetRequest request = new SellerCatsGetRequest();

        List<ShopCategory> shopCategoryList = new ArrayList<>();

        try {
            // 调用京东获取前台展示的商家自定义店内分类API(360buy.sellercats.get)
            SellerCatsGetResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回店铺分类列表
                    shopCategoryList = response.getShopCatList();
                }
                else
                {
                    logger.error("调用京东API获取前台展示的商家自定义店内分类信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());

                    throw new BusinessException(shop.getShop_name() + "获取前台展示的商家自定义店内分类信息失败 " + response.getMsg());
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API获取前台展示的商家自定义店内分类信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());

            throw new BusinessException(shop.getShop_name() + "获取前台展示的商家自定义店内分类信息失败 " + ex.getMessage());
        }


        return shopCategoryList;
    }


    /**
     * 京东添加前台展示的商家自定义店内分类
     *
     * @param shop ShopBean      店铺信息
     * @param cName String 分类名
     * @param parentCId String 父分类Id
     * @return cId String 分类Id
     * @throws BusinessException
     */
    public String addShopCategory(ShopBean shop, String cName, String parentCId) throws BusinessException {

        SellerCatAddRequest request = new SellerCatAddRequest();
        String cId = "";

        request.setName(cName);
        request.setParentId(parentCId);
        request.setOpen(false);
        request.setHomeShow(false);

        try {
            // 调用京东添加前台商家自定义店内分类API(360buy.sellercats.add)
            SellerCatAddResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回店铺分类的cid
                    cId = response.getCid();
                }
                else
                {
                    logger.error("调用京东API添加前台展示的商家自定义店内分类信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());

                    throw new BusinessException(shop.getShop_name() + "添加前台展示的商家自定义店内分类信息失败 " + response);
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API添加前台展示的商家自定义店内分类信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());

            throw new BusinessException(shop.getShop_name() + "添加前台展示的商家自定义店内分类信息失败 " + ex.getMessage());
        }

        return cId;

    }

    /**
     *
     * @param shop ShopBean 店铺信息
     * @param cId String 分类Id
     * @throws BusinessException
     */
    public void deleteShopCategory(ShopBean shop, String cId) throws BusinessException {
        SellerCatDeleteRequest request = new SellerCatDeleteRequest();
        request.setCid(cId);

        try {
            // 调用京东删除前台商家自定义店内分类API(360buy.sellercats.delete)
            SellerCatDeleteResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回异常的场合, 并且error_code != 12300008:需要删除的店铺分类不存在时, 抛出异常
                if (!JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())
                        && !"12300008".equals(response.getCode())) {
                    logger.error("调用京东API删除前台展示的商家自定义店内分类信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());
                    throw new BusinessException(shop.getShop_name() + "删除前台展示的商家自定义店内分类信息失败 " +  response.getMsg());
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API删除前台展示的商家自定义店内分类信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());

            throw new BusinessException(shop.getShop_name() + "删除前台展示的商家自定义店内分类信息失败 " + ex.getMessage());
        }
    }

    public void updateShopCategory(ShopBean shop, String cId, String cName ) throws BusinessException {
        SellerCatUpdateRequest request = new SellerCatUpdateRequest();
        request.setCid(cId);
        request.setName(cName);

        try{
            // 调用京东修改前台商家自定义店内分类API(360buy.sellercats.update)
            SellerCatUpdateResponse  response = reqApi(shop, request);
            if (response != null) {
                // 京东返回异常的场合
                if (!JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    logger.error("调用京东API修改前台展示的商家自定义店内分类信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());
                    throw new BusinessException(shop.getShop_name() + "修改前台展示的商家自定义店内分类信息失败 " + response.getMsg());
                }
            }


        }catch (Exception ex) {
            logger.error("调用京东API修改前台展示的商家自定义店内分类信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());

            throw new BusinessException(shop.getShop_name() + "修改前台展示的商家自定义店内分类信息失败 " + ex.getMessage());
        }

    }
}
