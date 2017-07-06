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
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.TbBase;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品上下架，以及查询商品的在售/在库状态
 * Created by Kylin on 2015/7/15.
 */
@Component
public class TbSaleService extends TbBase {

    private static final int PAGE_SIZE = 200;

    @Autowired
    private TbProductService tbProductService;

    /**
     * 天猫/淘宝商品上架
     *
     * @param shopBean 店铺信息
     * @param numIid String 商品数字ID，该参数必须
     */
    public ItemUpdateListingResponse doWareUpdateListing(ShopBean shopBean, String numIid) {
        String quantityCnt = "0";
        logger.info("商品上架 " + numIid);
        ItemUpdateListingRequest request = new ItemUpdateListingRequest();
        request.setNumIid(NumberUtils.toLong(numIid));
        // 需要上架的商品的数量。取值范围:大于零的整数。如果商品有sku，则上架数量默认为所有sku数量总和，不可修改。否则商品数量根据设置数量调整为num
        request.setNum(NumberUtils.toLong(quantityCnt));
        ItemUpdateListingResponse response = null;

        try {
            response = reqTaobaoApi(shopBean, request);
        } catch (ApiException apiExp) {
            logger.error(String.format("调用淘宝API商品上架时API出错 [numIId:%s] [quantity:%s]", numIid, quantityCnt), apiExp);
        } catch (Exception exp) {
            logger.error(String.format("调用淘宝API商品上架时出错 [numIId:%s] [quantity:%s]", numIid, quantityCnt), exp);
        }
        return response;
    }

    /**
     * 天猫/淘宝商品下架
     *
     * @param shopBean 店铺信息
     * @param numIid String 商品数字ID，该参数必须
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

    public List<Item> getOnsaleProduct(String strOrderChannelId, String strCardId, String fields, Long lPageIndex, Long pageSize, String title) throws ApiException {
        ShopBean shopInfo = Shops.getShop(strOrderChannelId, strCardId);
        ItemsOnsaleGetRequest req = new ItemsOnsaleGetRequest();

        req.setQ(title);
        req.setPageNo(lPageIndex);
        req.setPageSize(pageSize);
        req.setFields(fields);

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
//    public List<Item> getInventoryProduct(String strOrderChannelId, String strCardId, Long lPageIndex, Long pageSize) throws ApiException {
//        return getInventoryProduct(strOrderChannelId,strCardId,"for_shelved,sold_out,violation_off_shelf",lPageIndex,pageSize);
//    }

    public List<Item> getInventoryProductForShelved(String strOrderChannelId, String strCardId, Long lPageIndex, Long pageSize) throws ApiException {
        return getInventoryProduct(strOrderChannelId,strCardId,"for_shelved",lPageIndex,pageSize);
    }

    public List<Item> getInventoryProductSoldOut(String strOrderChannelId, String strCardId, Long lPageIndex, Long pageSize) throws ApiException {
        return getInventoryProduct(strOrderChannelId,strCardId,"sold_out",lPageIndex,pageSize);
    }

    public List<Item> getInventoryProductViolationOffShelf(String strOrderChannelId, String strCardId, Long lPageIndex, Long pageSize) throws ApiException {
        return getInventoryProduct(strOrderChannelId,strCardId,"violation_off_shelf",lPageIndex,pageSize);
    }

    public List<Item> getInventoryProduct(String strOrderChannelId, String strCardId, String banner, Long lPageIndex, Long pageSize) throws ApiException {
        ShopBean shopInfo = Shops.getShop(strOrderChannelId, strCardId);
        ItemsInventoryGetRequest req = new ItemsInventoryGetRequest();

        req.setPageNo(lPageIndex);
        req.setPageSize(pageSize);
        req.setFields("num_iid,outer_id,title,delist_time");
        req.setBanner(banner);

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

    public List<Item> getInventoryProduct(String strOrderChannelId, String strCardId, String banner, Long lPageIndex, Long pageSize, String title) throws ApiException {
        ShopBean shopInfo = Shops.getShop(strOrderChannelId, strCardId);
        ItemsInventoryGetRequest req = new ItemsInventoryGetRequest();

        req.setPageNo(lPageIndex);
        req.setPageSize(pageSize);
        req.setQ(title);
        req.setFields("num_iid,outer_id,title,delist_time");
        req.setBanner(banner);


        ItemsInventoryGetResponse response = reqTaobaoApi(shopInfo, req);
        if (response == null) {
            return null;
        }
        Object[] objs = { strOrderChannelId, strCardId, response.getErrorCode()==null ? "total=" + response.getTotalResults() : response.getBody() };
        logger.info("getInventoryProduct调用结果 channelid={}, cartid={}, 结果={}", objs);
        return response.getItems();
    }

    /**
     * 获取店铺全部的numIId
     */
    public Map<CmsConstants.PlatformStatus, List<String>> getTmNumIIdList(String channelId, String cartId) {
        List<String> inStockNumIIdList = new ArrayList<>();
        long pageNo = 1;
        while(true) {
            List<Item> rsList;
            try {
                // 查询下架
                rsList = getInventoryProductForShelved(channelId, cartId, pageNo++, Long.valueOf(PAGE_SIZE));
            } catch (ApiException apiExp) {
                throw new BusinessException(String.format("调用淘宝API获取下架商品时API出错 channelId=%s, cartId=%s", channelId, cartId), apiExp);
            } catch (Exception exp) {
                throw new BusinessException(String.format("调用淘宝API获取下架商品时出错 channelId=%s, cartId=%s", channelId, cartId), exp);
            }
            if (rsList != null && rsList.size() > 0) {
                inStockNumIIdList.addAll(rsList.stream().map(tmItem -> tmItem.getNumIid().toString()).collect(Collectors.toList()));
            }
            if (rsList == null || rsList.size() < PAGE_SIZE) {
                break;
            }
        }

        pageNo = 1;
        while(true) {
            List<Item> rsList;
            try {
                // 查询卖完
                rsList = getInventoryProductSoldOut(channelId, cartId, pageNo++, Long.valueOf(PAGE_SIZE));
            } catch (ApiException apiExp) {
                throw new BusinessException(String.format("调用淘宝API获取下架商品时API出错 channelId=%s, cartId=%s", channelId, cartId), apiExp);
            } catch (Exception exp) {
                throw new BusinessException(String.format("调用淘宝API获取下架商品时出错 channelId=%s, cartId=%s", channelId, cartId), exp);
            }
            if (rsList != null && rsList.size() > 0) {
                inStockNumIIdList.addAll(rsList.stream().map(tmItem -> tmItem.getNumIid().toString()).collect(Collectors.toList()));
            }
            if (rsList == null || rsList.size() < PAGE_SIZE) {
                break;
            }
        }

        pageNo = 1;
        while(true) {
            List<Item> rsList;
            try {
                // 查询违规下架
                rsList = getInventoryProductViolationOffShelf(channelId, cartId, pageNo++, Long.valueOf(PAGE_SIZE));
            } catch (ApiException apiExp) {
                throw new BusinessException(String.format("调用淘宝API获取下架商品时API出错 channelId=%s, cartId=%s", channelId, cartId), apiExp);
            } catch (Exception exp) {
                throw new BusinessException(String.format("调用淘宝API获取下架商品时出错 channelId=%s, cartId=%s", channelId, cartId), exp);
            }
            if (rsList != null && rsList.size() > 0) {
                inStockNumIIdList.addAll(rsList.stream().map(tmItem -> tmItem.getNumIid().toString()).collect(Collectors.toList()));
            }
            if (rsList == null || rsList.size() < PAGE_SIZE) {
                break;
            }
        }

        List<String> onSaleNumIIdList = new ArrayList<>();
        pageNo = 1;
        while(true) {
            List<Item> rsList;
            try {
                // 查询上架
                rsList = getOnsaleProduct(channelId, cartId, pageNo++, Long.valueOf(PAGE_SIZE));
            } catch (ApiException apiExp) {
                throw new BusinessException(String.format("调用淘宝API获取上架商品时API出错 channelId=%s, cartId=%s", channelId, cartId), apiExp);
            } catch (Exception exp) {
                throw new BusinessException(String.format("调用淘宝API获取上架商品时出错 channelId=%s, cartId=%s", channelId, cartId), exp);
            }
            if (rsList != null && rsList.size() > 0) {
                onSaleNumIIdList.addAll(rsList.stream().map(tmItem -> tmItem.getNumIid().toString()).collect(Collectors.toList()));
            }
            if (rsList == null || rsList.size() < PAGE_SIZE) {
                break;
            }
        }

        Map<CmsConstants.PlatformStatus, List<String>> retMap = new HashMap<>();
        retMap.put(CmsConstants.PlatformStatus.InStock, inStockNumIIdList);
        retMap.put(CmsConstants.PlatformStatus.OnSale, onSaleNumIIdList);
        return retMap;
    }
}
