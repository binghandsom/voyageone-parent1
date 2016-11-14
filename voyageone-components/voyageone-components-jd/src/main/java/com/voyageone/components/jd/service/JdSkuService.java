package com.voyageone.components.jd.service;

import com.jd.open.api.sdk.request.ware.WareSkuPriceUpdateRequest;
import com.jd.open.api.sdk.response.ware.WareSkuPriceUpdateResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.JdBase;
import com.voyageone.components.jd.JdConstants;
import org.springframework.stereotype.Component;

/**
 * 京东商品sku相关API(更新sku价格等)调用服务
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

}
