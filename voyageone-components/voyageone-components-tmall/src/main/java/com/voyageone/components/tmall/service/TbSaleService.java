package com.voyageone.components.tmall.service;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Item;
import com.taobao.api.request.ItemUpdateDelistingRequest;
import com.taobao.api.request.ItemUpdateListingRequest;
import com.taobao.api.request.ItemsInventoryGetRequest;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.response.ItemUpdateDelistingResponse;
import com.taobao.api.response.ItemUpdateListingResponse;
import com.taobao.api.response.ItemsInventoryGetResponse;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.components.tmall.TbBase;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品上下架，以及查询商品的在售/在库状态
 * Created by Kylin on 2015/7/15.
 */
@Component
public class TbSaleService extends TbBase {

    /**
     * 商品上架
     */
    public ItemUpdateListingResponse doWareUpdateListing(ShopBean shopBean, String numIid) {
        logger.info("商品上架 " + numIid);
        ItemUpdateListingRequest request = new ItemUpdateListingRequest ();
        request.setNumIid(NumberUtils.toLong(numIid));
        request.setNum(1L);
        ItemUpdateListingResponse response = null;

        try {
            response = reqTaobaoApi(shopBean, request);
        } catch (ApiException apiExp) {
            logger.error("调用淘宝API商品上架时API出错", apiExp);
        } catch (Exception exp) {
            logger.error("调用淘宝API商品上架时出错", exp);
        }
        return response;
    }

    /**
     * 商品下架
     */
    public ItemUpdateDelistingResponse doWareUpdateDelisting(ShopBean shopBean, String numIid) {
        logger.info("商品下架 " + numIid);
        ItemUpdateDelistingRequest request = new ItemUpdateDelistingRequest();
        request.setNumIid(NumberUtils.toLong(numIid));
        ItemUpdateDelistingResponse response = null;

        try {
            response = reqTaobaoApi(shopBean, request);
        } catch (ApiException apiExp) {
            logger.error("调用淘宝API商品下架时API出错", apiExp);
        } catch (Exception exp) {
            logger.error("调用淘宝API商品下架时出错", exp);
        }
        return response;
    }

    /**
     * 获取当前会话用户出售中的商品列表(目前获取所有时间段，不作过滤，不考虑优化，下同)
     * 只返回 num_iid
     */
    public List<Item> getOnsaleProduct(String strOrderChannelId, String strCardId, Long lPageIndex, Long pageSize) throws ApiException {
        return getOnsaleProduct(strOrderChannelId, strCardId,"num_iid",lPageIndex,pageSize);
    }

    public List<Item> getOnsaleProduct(String strOrderChannelId, String strCardId, String fields, Long lPageIndex, Long pageSize) throws ApiException {
        ShopBean shopInfo = Shops.getShop(strOrderChannelId, strCardId);
        ItemsOnsaleGetRequest req = new ItemsOnsaleGetRequest();

        req.setPageNo(lPageIndex);
        req.setPageSize(pageSize);
        req.setFields(fields);

//        String staDate = DateTimeUtil.format(DateUtils.addDays(DateTimeUtilBeijing.getCurrentBeiJingDate(), -1), DateTimeUtil.DEFAULT_DATE_FORMAT) + " 00:00:00";
//        req.setStartModified(DateTimeUtil.parse(staDate));
//        req.setEndModified(DateTimeUtilBeijing.getCurrentBeiJingDate());

        ItemsOnsaleGetResponse response = reqTaobaoApi(shopInfo, req);
        if (response == null) {
            return null;
        }
        Object[] objs = { strOrderChannelId, strCardId, response.getErrorCode()==null ? "total=" + response.getTotalResults() : response.getBody() };
        logger.info("getOnsaleProduct调用结果 channelid={}, cartid={}, 结果={}", objs);
        return response.getItems();
    }
    /**
     * 获取当前会话用户在库的商品列表
     * (包含所有库存分类状态：for_shelved(regular_shelved、never_on_shelf、off_shelf)、sold_out、violation_off_shelf)
     * 只返回 num_iid
     */
    public List<Item> getInventoryProduct(String strOrderChannelId, String strCardId, Long lPageIndex, Long pageSize) throws ApiException {
        ShopBean shopInfo = Shops.getShop(strOrderChannelId, strCardId);
        shopInfo.setAppKey("21008948");
        shopInfo.setAppSecret("0a16bd08019790b269322e000e52a19f");
        shopInfo.setSessionKey("620230429acceg4103a72932e22e4d53856b145a192140b2854639042");
        ItemsInventoryGetRequest req = new ItemsInventoryGetRequest();

        req.setPageNo(lPageIndex);
        req.setPageSize(pageSize);
        req.setFields("num_iid,outer_id,title");
        req.setBanner("for_shelved,sold_out,violation_off_shelf");

//        String staDate = DateTimeUtil.format(DateUtils.addDays(DateTimeUtilBeijing.getCurrentBeiJingDate(), -1), DateTimeUtil.DEFAULT_DATE_FORMAT) + " 00:00:00";
//        req.setStartModified(DateTimeUtil.parse(staDate));
//        req.setEndModified(DateTimeUtilBeijing.getCurrentBeiJingDate());

        ItemsInventoryGetResponse response = reqTaobaoApi(shopInfo, req);
        if (response == null) {
            return null;
        }
        Object[] objs = { strOrderChannelId, strCardId, response.getErrorCode()==null ? "total=" + response.getTotalResults() : response.getBody() };
        logger.info("getInventoryProduct调用结果 channelid={}, cartid={}, 结果={}", objs);
        return response.getItems();
    }
}
