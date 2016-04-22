package com.voyageone.components.jd.service;

import com.jd.open.api.sdk.domain.sellercat.ShopCategory;
import com.jd.open.api.sdk.request.sellercat.SellerCatsGetRequest;
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
            }
        } catch (Exception ex) {
            logger.error("调用京东API获取前台展示的商家自定义店内分类信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());

            throw new BusinessException(shop.getShop_name() + "获取前台展示的商家自定义店内分类信息失败 " + ex.getMessage());
        }


        return shopCategoryList;
    }

}
