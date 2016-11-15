package com.voyageone.components.tmall.service;

import com.taobao.api.ApiException;
import com.taobao.api.domain.SellerCat;
import com.taobao.api.request.SellercatsListAddRequest;
import com.taobao.api.request.SellercatsListGetRequest;
import com.taobao.api.request.SellercatsListUpdateRequest;
import com.taobao.api.response.SellercatsListAddResponse;
import com.taobao.api.response.SellercatsListGetResponse;
import com.taobao.api.response.SellercatsListUpdateResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.TbBase;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Ethan Shi on 2016/5/24.
 */
@Component
public class TbSellerCatService extends TbBase {


    /**
     *
     * @param shop 店铺信息
     * @return 店铺内自定义分类
     * @throws ApiException
     */
    public List<SellerCat> getSellerCat(ShopBean shop) throws BusinessException {
        SellercatsListGetRequest request = new SellercatsListGetRequest();

        request.setNick(shop.getShop_name());

        try {
            SellercatsListGetResponse  response  = reqTaobaoApi(shop, request);
            if(StringUtils.isNullOrBlank2(response.getErrorCode())) {
                return response.getSellerCats();
            }

            logger.error("调用天猫API:获取该店铺自定义类目:" + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());
            logger.error("ERROR CODE:" + response.getErrorCode() +",ERROR MSG:" + response.getMsg());
            logger.error("RESPONSE BODY:" + response.getBody());
            throw new BusinessException(shop.getShop_name() + "获取前台展示的商家自定义店内分类信息失败 " + response.getMsg());

        } catch (ApiException e) {
            logger.error("调用天猫API获取前台展示的商家自定义店内分类信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());
            throw new BusinessException(shop.getShop_name() + "获取前台展示的商家自定义店内分类信息失败 " + e.getMessage());
        }
    }


    /**
     *
     * @param shop 店铺信息
     * @param cId 店铺内自定义分类Id
     * @param cName 店铺内自定义分类名
     * @return
     * @throws ApiException
     */
    public void updateSellerCat(ShopBean shop, String cId, String cName) throws  BusinessException {
        SellercatsListUpdateRequest request = new SellercatsListUpdateRequest();

        request.setCid(Long.parseLong(cId));
        request.setName(cName);


        try {
            SellercatsListUpdateResponse response = reqTaobaoApi(shop, request);

            if (!StringUtils.isNullOrBlank2(response.getErrorCode())) {
                logger.error("调用天猫API:更新该店铺自定义类目:" + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());
                logger.error("ERROR CODE:" + response.getErrorCode() + ",ERROR MSG:" + response.getMsg());
                logger.error("RESPONSE BODY:" + response.getBody());
                throw new BusinessException(shop.getShop_name() + "获取前台展示的商家自定义店内分类信息失败 " + response.getMsg());
            }
        }
        catch (ApiException e) {
            logger.error("调用天猫API更新前台展示的商家自定义店内分类信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());
            throw new BusinessException(shop.getShop_name() + "获取更新展示的商家自定义店内分类信息失败 " + e.getMessage());
        }
    }

    /**
     *
     * @param shop 店铺信息
     * @param cid 需要修改的类目id
     * @param sortOrder 在当前类目的父类目中的所有子类目中， 排在第几位（大于0）
     * @return
     * @throws ApiException
     */
    public void updateSellerCatSortOrder(ShopBean shop, String cid, long sortOrder) throws  BusinessException {
        SellercatsListUpdateRequest request = new SellercatsListUpdateRequest();

        request.setCid(Long.parseLong(cid));
        request.setSortOrder(sortOrder);

        try {
            SellercatsListUpdateResponse response = reqTaobaoApi(shop, request);

            if (!StringUtils.isNullOrBlank2(response.getErrorCode())) {
                logger.error("调用天猫API:更新该店铺自定义类目(的顺序):" + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());
                logger.error("ERROR CODE:" + response.getErrorCode() + ",ERROR MSG:" + response.getMsg());
                logger.error("RESPONSE BODY:" + response.getBody());
                throw new BusinessException(shop.getShop_name() + "更新店铺自定义类目（顺序）失败 " + response.getMsg());
            }
        }
        catch (ApiException e) {
            logger.error("调用天猫API更新前台展示的商家自定义店内分类（的顺序）信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());
            throw new BusinessException(shop.getShop_name() + "更新店铺自定义类目（顺序）失败 " + e.getMessage());
        }
    }

    /**
     *
     * @param shop 店铺信息
     * @param cName 店铺内自定义分类名
     * @param parentCId 店铺内自定义分类父Id
     * @return
     * @throws ApiException
     */
    public String addSellerCat(ShopBean shop, String cName, String parentCId) throws  BusinessException {
        SellercatsListAddRequest request = new SellercatsListAddRequest();
        request.setName(cName);
        request.setParentCid(Long.parseLong(parentCId));

        try {
            SellercatsListAddResponse response = reqTaobaoApi(shop, request);
            if (!StringUtils.isNullOrBlank2(response.getErrorCode())) {
                logger.error("调用天猫API:更新该店铺自定义类目:" + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());
                logger.error("ERROR CODE:" + response.getErrorCode() + ",ERROR MSG:" + response.getMsg());
                logger.error("RESPONSE BODY:" + response.getBody());
                throw new BusinessException(response.getSubMsg());
            } else {
                return "" + response.getSellerCat().getCid();
            }
        }
        catch (ApiException e) {
            logger.error("调用天猫API添加前台展示的商家自定义店内分类信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());
            throw new BusinessException(shop.getShop_name() + "添加前台展示的商家自定义店内分类信息失败 " + e.getMessage());
        }
    }
}
