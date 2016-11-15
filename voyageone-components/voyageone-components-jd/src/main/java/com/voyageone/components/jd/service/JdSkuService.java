package com.voyageone.components.jd.service;

import com.google.common.base.Joiner;
import com.jd.open.api.sdk.domain.ware.Sku;
import com.jd.open.api.sdk.request.ware.SkuCustomGetRequest;
import com.jd.open.api.sdk.request.ware.WareSkuPriceUpdateRequest;
import com.jd.open.api.sdk.request.ware.WareSkusGetRequest;
import com.jd.open.api.sdk.response.ware.SkuCustomGetResponse;
import com.jd.open.api.sdk.response.ware.WareSkuPriceUpdateResponse;
import com.jd.open.api.sdk.response.ware.WareSkusGetResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.JdBase;
import com.voyageone.components.jd.JdConstants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 京东商品sku相关API调用服务
 * 包括更新sku价格,取得sku信息等服务
 * <p/>
 * @author desmond on 2016/11/14.
 * @version 2.8.0
 * @since 2.8.0
 */
@Component
public class JdSkuService extends JdBase {

    /**
     * 根据sku_id修改京东商品SKU价格信息
     *
     * @param shop        ShopBean  店铺信息
     * @param skuId       String    sku的id
     * @param price       String    sku京东价
     * @return String               修改时间
     */
    public String updateSkuPriceBySkuId(ShopBean shop, String skuId, String price) {
        return updateSkuPrice(shop, skuId, null, price, null, null, null);
    }

    /**
     * 根据外部id修改京东商品SKU价格信息
     *
     * @param shop        ShopBean  店铺信息
     * @param outerId     String    外部id
     * @param price       String    sku京东价
     * @return String               修改时间
     */
    public String updateSkuPriceByOuterId(ShopBean shop, String outerId, String price) {
        return updateSkuPrice(shop, null, outerId, price, null, null, null);
    }

    /**
     * 修改京东商品SKU价格信息
     * 通过api(360buy.sku.price.update) 根据sku_id /outer_id修改价格接口，skuId和outerId 至少填一个，如果都有则以sku_id为准
     *
     * @param shop        ShopBean  店铺信息
     * @param skuId       String    sku的id
     * @param outerId     String    外部id
     * @param price       String    sku京东价
     * @param tradeNo     String    流水号
     * @param marketPrice String    市场价
     * @param jdPrice     String    品京东价
     * @return String               修改时间
     */
    public String updateSkuPrice(ShopBean shop, String skuId, String outerId, String price, String tradeNo, String marketPrice, String jdPrice) {
        // 如果skuId和outerId都为空时报错
        if (StringUtils.isEmpty(skuId) && StringUtils.isEmpty(outerId)) {
           String errMsg = String.format(shop.getShop_name() + "调用updateSkuPrice方法更新京东商品SKU价格时，" +
                   "通过参数传入skuId和outerId不能同时为空!! [channelId:%s] [cartId:%s] [skuId:%s] [outerId:%s] ",
                   shop.getOrder_channel_id(), shop.getCart_id(), skuId, outerId);
            logger.error(errMsg);
            throw new BusinessException(errMsg);
        }

        WareSkuPriceUpdateRequest request = new WareSkuPriceUpdateRequest();
        // sku的id（sku_id与outer_id至少填写一项，两者都填写时，以sku_id为准）
        if (!StringUtils.isEmpty(skuId))       request.setSkuId(skuId);
        // 外部id（sku_id与outer_id至少填写一项，两者都填写时，以sku_id为准）
        if (!StringUtils.isEmpty(outerId))     request.setOuterId(outerId);
        // sku京东价
        // 1、只有【虚拟类 】商家以及以下实物类目支持小数点价格。（1）“图书”（1713）;（2）“音乐”（4051）;（3）“影视”（4052）;
        //                                              （4）“影视音像“（4053）;（5）“个护化妆“（1316）
        // 2、其他实物类目，小于100元的，可以支持小数点价格（精确到2位），大于等于100元的，不支持小数点价格。
        if (!StringUtils.isEmpty(price))       request.setPrice(price);
        // 流水号
        if (!StringUtils.isEmpty(tradeNo))     request.setTradeNo(tradeNo);
        // 市场价
        if (!StringUtils.isEmpty(marketPrice)) request.setMarketPrice(marketPrice);
        // 商品京东价
        if (!StringUtils.isEmpty(jdPrice))     request.setJdPrice(jdPrice);

        try {
            // 调用京东修改SKU价格信息API(360buy.sku.price.update)
            WareSkuPriceUpdateResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回更新时间
                    return response.getModified();
                } else {
                    throw new BusinessException(response.getMsg());
                }
            } else {
                // response = null（https://api.jd.com/routerjson）不能访问的可能原因是服务器禁掉了https端口
                // 或app_url,app_key等不正确
                throw new BusinessException("京东更新商品SKU价格API返回应答为空(response = null)");
            }
        } catch (Exception ex) {
            String errMsg = String.format(shop.getShop_name() + "调用京东API更新商品SKU价格失败! [channelId:%s] [cartId:%s] [skuId:%s] [outerId:%s] " +
                    "[errMsg:%s]", shop.getOrder_channel_id(), shop.getCart_id(), skuId, outerId, ex.getMessage());
            logger.error(errMsg);
            throw new BusinessException(errMsg);
        }
    }

    /**
     * 根据商品ID列表获取商品SKU信息
     * 通过api(360buy.ware.skus.get) 获取多个商品下的所有sku信息
     *
     * @param shop ShopBean  店铺信息
     * @param wareId String  京东商品id列表，sku所属商品id，必选。ware_ids个数不能超过10个
     * @return skus  指定商品列表的所有sku信息列表
     */
    public List<Sku> getSkusByWareId(ShopBean shop, String wareId, StringBuffer failCause) throws BusinessException {
        List<String> wareIds = new ArrayList<>();
        wareIds.add(wareId);

        return getSkusByWareIds(shop, wareIds, failCause);
    }

    /**
     * 根据商品ID列表获取商品SKU信息
     * 通过api(360buy.ware.skus.get) 获取多个商品下的所有sku信息
     *
     * @param shop ShopBean  店铺信息
     * @param wareIds List<String>  京东商品id列表，sku所属商品id，必选。ware_ids个数不能超过10个
     * @return skus  指定商品列表的所有sku信息列表
     */
    public List<Sku> getSkusByWareIds(ShopBean shop, List<String> wareIds, StringBuffer failCause) throws BusinessException {
        List<Sku> skus = new ArrayList<>();
        if (ListUtils.isNull(wareIds)) return null;

        // 批量取得商品wareId对应的sku信息时，ware_ids个数不能超过10个
        List<List<String>> pageList = CommonUtil.splitList(wareIds, 10);
        WareSkusGetRequest request = new WareSkusGetRequest();
        String strWareIds;
        for(List<String> page : pageList) {
            // sku所属商品id
            strWareIds = Joiner.on(",").join(page);
            try {
                // sku所属商品id，必选。ware_ids个数不能超过10个
                request.setWareIds(strWareIds);
                // 需返回的字段列表(现在选的是返回全部字段)。可选值：Sku结构体中的所有字段；字段之间用“,”分隔
                request.setFields("shop_id,cost_price,status,outer_id,color_value,stock_num,modified,jd_price,market_price,ware_id,created,sku_id,attributes,size_value");

                // 调用京东根据商品ID列表获取商品SKU信息API(360buy.ware.skus.get)
                WareSkusGetResponse response = reqApi(shop, request);
                if (response != null) {
                    // 京东返回正常的场合
                    if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                        // 返回图片列表
                        skus.addAll(response.getSkus());
                    } else {
                        // 京东返回失败的场合
                        throw new BusinessException(response.getZhDesc());
                    }
                } else {
                    // response = null（https://api.jd.com/routerjson）不能访问的可能原因是服务器禁掉了https端口
                    // 或app_url,app_key等不正确
                    throw new BusinessException("京东根据商品ID列表获取商品SKU信息API返回应答为空(response = null)");
                }
            } catch (Exception e) {
                String errMsg = String.format(shop.getShop_name() + "调用京东API根据商品ID列表获取商品SKU信息失败! [channelId:%s] [cartId:%s] [wareIds:%s] " +
                        "[errMsg:%s]", shop.getOrder_channel_id(), shop.getCart_id(), strWareIds, e.getMessage());
                logger.error(errMsg);
                failCause.append(errMsg);
            }
        }

        return skus;
    }

    /**
     * 根据外部商家ID(skuCode)获取商品SKU信息
     * 根据商家设定的sku的外部id获取所对应的sku数据，一个sku的外部id对应一个sku数据
     * 通过api(360buy.sku.custom.get) 获取多个商品下的所有sku信息
     *
     * @param shop ShopBean  店铺信息
     * @param outerId String  sku的外部商家ID(skuCode)
     * @return sku  外部商家ID对应的sku信息
     */
    public Sku getSkuByOuterId(ShopBean shop, String outerId, StringBuffer failCause) {
        if (StringUtils.isEmpty(outerId)) {
            failCause.append("根据外部商家ID(skuCode)获取商品SKU信息时，参数传入的outerid为空！");
            return null;
        }

        SkuCustomGetRequest request = new SkuCustomGetRequest();
        // sku的外部商家ID 对应商家后台“商家SKU”字段(skuCode)
        request.setOuterId(outerId);
        // 需返回的字段列表(现在选的是返回全部字段)。可选值：Sku结构体中的所有字段；字段之间用“,”分隔
        request.setFields("shop_id,cost_price,status,outer_id,color_value,stock_num,modified,jd_price,market_price,ware_id,created,sku_id,attributes,size_value");

        Sku resultSku = null;
        try {
            // 调用京东根据外部商家ID(skuCode)获取商品SKU信息API(360buy.ware.skus.get)
            SkuCustomGetResponse response = reqApi(shop, request);
            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回图片列表
                    resultSku = response.getSku();
                } else {
                    // 京东返回失败的场合
                    throw new BusinessException(response.getZhDesc());
                }
            } else {
                // response = null（https://api.jd.com/routerjson）不能访问的可能原因是服务器禁掉了https端口
                // 或app_url,app_key等不正确
                throw new BusinessException("京东根据根据外部商家ID(skuCode)获取商品SKU信息API返回应答为空(response = null)");
            }
        } catch (Exception e) {
            String errMsg = String.format(shop.getShop_name() + "调用京东API根据外部商家ID(skuCode)获取商品SKU信息失败! [channelId:%s] [cartId:%s] [outerId:%s] " +
                    "[errMsg:%s]", shop.getOrder_channel_id(), shop.getCart_id(), outerId, e.getMessage());
            logger.error(errMsg);
            failCause.append(errMsg);
        }

        return resultSku;
    }

}
