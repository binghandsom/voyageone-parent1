package com.voyageone.components.tmall.service;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Item;
import com.taobao.api.request.ItemsInventoryGetRequest;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.response.ItemsInventoryGetResponse;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.components.tmall.TbBase;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Kylin on 2015/7/15.
 */
@Component
public class TbSaleService extends TbBase {

    /**
     * 获取当前会话用户出售中的商品列表(上架时间在前一天)
     * 只返回 num_iid
     */
    public List<Item> getOnsaleProduct(String strOrderChannelId, String strCardId, Long lPageIndex, Long pageSize) throws ApiException {
        ShopBean shopInfo = Shops.getShop(strOrderChannelId, strCardId);
        ItemsOnsaleGetRequest req = new ItemsOnsaleGetRequest();

        req.setPageNo(lPageIndex);
        req.setPageSize(pageSize);
        req.setFields("num_iid");

        String staDate = DateTimeUtil.format(DateUtils.addDays(DateTimeUtilBeijing.getCurrentBeiJingDate(), -1), DateTimeUtil.DEFAULT_DATE_FORMAT) + " 00:00:00";
        req.setStartModified(DateTimeUtil.parse(staDate));
        req.setEndModified(DateTimeUtilBeijing.getCurrentBeiJingDate());

        ItemsOnsaleGetResponse response = reqTaobaoApi(shopInfo, req);
        if (response == null) {
            return null;
        }
        Object[] objs = { strOrderChannelId, strCardId, response.getErrorCode()==null ? "total=" + response.getTotalResults() : response.getBody() };
        logger.info("getOnsaleProduct调用结果 channelid={}, cartid={}, 结果={}", objs);
        return response.getItems();
    }

    /**
     * 获取当前会话用户在库的商品列表(下架时间在前一天)
     * (包含所有库存分类状态：for_shelved(regular_shelved、never_on_shelf、off_shelf)、sold_out、violation_off_shelf)
     * 只返回 num_iid
     */
    public List<Item> getInventoryProduct(String strOrderChannelId, String strCardId, Long lPageIndex, Long pageSize) throws ApiException {
        ShopBean shopInfo = Shops.getShop(strOrderChannelId, strCardId);
        ItemsInventoryGetRequest req = new ItemsInventoryGetRequest();

        req.setPageNo(lPageIndex);
        req.setPageSize(pageSize);
        req.setFields("num_iid");
        req.setBanner("for_shelved,sold_out,violation_off_shelf");

        String staDate = DateTimeUtil.format(DateUtils.addDays(DateTimeUtilBeijing.getCurrentBeiJingDate(), -1), DateTimeUtil.DEFAULT_DATE_FORMAT) + " 00:00:00";
        req.setStartModified(DateTimeUtil.parse(staDate));
        req.setEndModified(DateTimeUtilBeijing.getCurrentBeiJingDate());

        ItemsInventoryGetResponse response = reqTaobaoApi(shopInfo, req);
        if (response == null) {
            return null;
        }
        Object[] objs = { strOrderChannelId, strCardId, response.getErrorCode()==null ? "total=" + response.getTotalResults() : response.getBody() };
        logger.info("getInventoryProduct调用结果 channelid={}, cartid={}, 结果={}", objs);
        return response.getItems();
    }
}
