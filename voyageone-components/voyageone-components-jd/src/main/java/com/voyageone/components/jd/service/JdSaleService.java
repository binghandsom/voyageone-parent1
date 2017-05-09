package com.voyageone.components.jd.service;

import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.jd.open.api.sdk.request.ware.WareDelistingGetRequest;
import com.jd.open.api.sdk.request.ware.WareListingGetRequest;
import com.jd.open.api.sdk.request.ware.WareUpdateDelistingRequest;
import com.jd.open.api.sdk.request.ware.WareUpdateListingRequest;
import com.jd.open.api.sdk.response.ware.WareDelistingGetResponse;
import com.jd.open.api.sdk.response.ware.WareListingGetResponse;
import com.jd.open.api.sdk.response.ware.WareUpdateDelistingResponse;
import com.jd.open.api.sdk.response.ware.WareUpdateListingResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.components.jd.JdBase;
import com.voyageone.components.jd.JdConstants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 京东运营类 api 调用服务 商品上下架，以及查询商品的在售/在库状态
 * <p/>
 * Created by Kylin on 2015/7/15.
 */
@Component
public class JdSaleService extends JdBase {

    private static final int PAGE_SIZE = 100;

    /**
     * 商品上架
     */
    public WareUpdateListingResponse doWareUpdateListing(ShopBean shop, String wareId)  {
        WareUpdateListingRequest request = new WareUpdateListingRequest();
        // 商品id(必须)
        request.setWareId(wareId);
        // 流水号（无实际意义，不重复即可）
        request.setTradeNo(DateTimeUtil.getNowTimeStamp());

        // 调用京东商品上架API(360buy.ware.update.listing)
        WareUpdateListingResponse response = reqApi(shop, request);
        return response;
    }

    /**
     * 商品上架
     *
     * @param shop ShopBean  店铺信息
     * @param wareId String  京东商品id
     * @param updateFlg boolean 新增/更新商品flg
     * @return boolean  商品上架结果
     */
    public boolean doWareUpdateListing(ShopBean shop, long wareId, boolean updateFlg) throws BusinessException {
        String errMsg = updateFlg ? "更新商品成功之后上架操作失败" : "新增商品成功之后上架操作失败";

        WareUpdateListingRequest request = new WareUpdateListingRequest();
        // 商品id(必须)
        request.setWareId(String.valueOf(wareId));
        // 流水号（无实际意义，不重复即可）
        request.setTradeNo(DateTimeUtil.getNowTimeStamp());

        try {
            // 调用京东商品上架API(360buy.ware.update.listing)
            WareUpdateListingResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回商品上架成功
                    return true;
                } else {
                    // 京东返回失败的场合
                    throw new BusinessException(response.getZhDesc());
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API商品上架操作失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:"
                    + shop.getCart_id() + ",ware_id:" + wareId + ",errorMsg:" + ex.getMessage());

            throw new BusinessException(errMsg + "[商品ID:" + wareId + "] " + ex.getMessage());
        }

        logger.error("调用京东API商品上架操作失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:"
                + shop.getCart_id() + ",ware_id:" + wareId + ",errorMsg:" + ",response=null");
        return false;
    }

    /**
     * 商品下架
     */
    public WareUpdateDelistingResponse doWareUpdateDelisting(ShopBean shop, String wareId) {
        WareUpdateDelistingRequest request = new WareUpdateDelistingRequest();
        // 商品id(必须)
        request.setWareId(wareId);
        // 流水号（无实际意义，不重复即可）
        request.setTradeNo(DateTimeUtil.getNowTimeStamp());

        // 调用京东商品下架API(360buy.ware.update.delisting)
        WareUpdateDelistingResponse response = reqApi(shop, request);
        return response;
    }

    /**
     * 商品下架
     *
     * @param shop ShopBean  店铺信息
     * @param wareId String  京东商品id
     * @param updateFlg boolean 新增/更新商品flg
     * @return boolean  商品下架结果
     */
    public boolean doWareUpdateDelisting(ShopBean shop, long wareId, boolean updateFlg) throws BusinessException {
        String errMsg = updateFlg ? "更新商品成功之后下架操作失败" : "新增商品成功之后下架操作失败";

        WareUpdateDelistingRequest request = new WareUpdateDelistingRequest();
        // 商品id(必须)
        request.setWareId(String.valueOf(wareId));
        // 流水号（无实际意义，不重复即可）
        request.setTradeNo(DateTimeUtil.getNowTimeStamp());

        try {
            // 调用京东商品下架API(360buy.ware.update.delisting)
            WareUpdateDelistingResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回商品下架成功
                    return true;
                } else {
                    // 京东返回失败的场合
                    throw new BusinessException(response.getZhDesc());
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API商品下架操作失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:"
                    + shop.getCart_id() + ",ware_id:" + wareId + ",errorMsg:" + ex.getMessage());

            throw new BusinessException(errMsg + "[商品ID:" + wareId + "] " + ex.getMessage());
        }

        logger.error("调用京东API商品下架操作失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:"
                + shop.getCart_id() + ",ware_id:" + wareId + ",errorMsg:" + ",response=null");
        return false;
    }

    /**
     * 获取上架/在售状态的产品列表(目前获取所有时间段，不作过滤，不考虑优化，下同)
     * 只返回 ware_id (即num_iid)
     */
    public List<Ware> getOnListProduct(String strOrderChannelId, String strCardId, String strPageIndex, String pageSize) throws JdException {
        ShopBean shopInfo = Shops.getShop(strOrderChannelId, strCardId);
        WareListingGetRequest request = new WareListingGetRequest();

        request.setPage(strPageIndex);
        request.setPageSize(pageSize);
        request.setFields("ware_id,stock_num");
//        request.setStartModified(DateTimeUtil.format(DateUtils.addDays(DateTimeUtilBeijing.getCurrentBeiJingDate(), -1), DateTimeUtil.DEFAULT_DATE_FORMAT) + " 00:00:00");
//        request.setEndModified(DateTimeUtil.format(DateTimeUtilBeijing.getCurrentBeiJingDate(), DateTimeUtil.DEFAULT_DATETIME_FORMAT));

        WareListingGetResponse response = reqApi(shopInfo, request);
        if (response == null) {
            return null;
        }
        Object[] objs = { strOrderChannelId, strCardId, "0".equals(response.getCode()) ? "total=" + response.getTotal() : response.getMsg() };
        logger.info("getOnListProduct调用结果 channelid={}, cartid={}, 结果={}", objs);
        return response.getWareInfos();
    }

    /**
     * 获取下架/在库状态的产品列表
     * 只返回wareId(即num_iid)
     */
    public List<Ware> getDeListProduct(String strOrderChannelId, String strCardId, String strPageIndex, String pageSize) throws JdException {
        ShopBean shopInfo = Shops.getShop(strOrderChannelId, strCardId);
        WareDelistingGetRequest request = new WareDelistingGetRequest();

        request.setPage(strPageIndex);
        request.setPageSize(pageSize);
        request.setFields("ware_id,stock_num");
//        request.setStartModified(DateTimeUtil.format(DateUtils.addDays(DateTimeUtilBeijing.getCurrentBeiJingDate(), -1), DateTimeUtil.DEFAULT_DATE_FORMAT) + " 00:00:00");
//        request.setEndModified(DateTimeUtil.format(DateTimeUtilBeijing.getCurrentBeiJingDate(), DateTimeUtil.DEFAULT_DATETIME_FORMAT));

        WareDelistingGetResponse response = reqApi(shopInfo, request);
        if (response == null) {
            return null;
        }
        Object[] objs = { strOrderChannelId, strCardId, "0".equals(response.getCode()) ? "total=" + response.getTotal() : response.getMsg() };
        logger.info("getDeListProduct调用结果 channelid={}, cartid={}, 结果={}", objs);
        return response.getWareInfos();
    }

    /**
     * 获取店铺全部的wareId
     */
    public Map<CmsConstants.PlatformStatus, List<String>> getJdWareIdList(String channelId, String cartId) {
        List<String> inStockWareIdList = new ArrayList<>();
        long pageNo = 1;
        while(true) {
            List<Ware> jdList;
            try {
                // 查询下架
                jdList = getDeListProduct(channelId, cartId, Long.toString(pageNo), String.valueOf(PAGE_SIZE));
                pageNo++;
            } catch (JdException apiExp) {
                throw new BusinessException(String.format("调用京东API获取下架商品时API出错 channelId=%s, cartId=%s", channelId, cartId), apiExp);
            } catch (Exception exp) {
                throw new BusinessException(String.format("调用京东API获取下架商品时出错 channelId=%s, cartId=%s", channelId, cartId), exp);
            }
            if (jdList != null && jdList.size() > 0) {
                inStockWareIdList.addAll(jdList.stream().map(ware -> ware.getWareId().toString()).collect(Collectors.toList()));
            }
            if (jdList == null || jdList.size() < PAGE_SIZE) {
                break;
            }
        }

        List<String> onSaleWareIdList = new ArrayList<>();
        pageNo = 1;
        while(true) {
            List<Ware> jdList;
            try {
                // 查询上架
                jdList = getOnListProduct(channelId, cartId, Long.toString(pageNo), String.valueOf(PAGE_SIZE));
                pageNo++;
            } catch (JdException apiExp) {
                throw new BusinessException(String.format("调用京东API获取上架商品时API出错 channelId=%s, cartId=%s", channelId, cartId), apiExp);
            } catch (Exception exp) {
                throw new BusinessException(String.format("调用京东API获取上架商品时出错 channelId=%s, cartId=%s", channelId, cartId), exp);
            }
            if (jdList != null && jdList.size() > 0) {
                onSaleWareIdList.addAll(jdList.stream().map(ware -> ware.getWareId().toString()).collect(Collectors.toList()));
            }
            if (jdList == null || jdList.size() < PAGE_SIZE) {
                break;
            }
        }

        Map<CmsConstants.PlatformStatus, List<String>> retMap = new HashMap<>();
        retMap.put(CmsConstants.PlatformStatus.InStock, inStockWareIdList);
        retMap.put(CmsConstants.PlatformStatus.OnSale, onSaleWareIdList);
        return retMap;
    }

    /**
     *  获取已上架的商品
     * @param channelId     店铺
     * @param cartId        渠道
     * @param wareStatus    商品状态
     * @param checkStockNum 商品库存状态（0: 不做check， 1: 仅抽出有库存， -1：仅抽出无库存）
     * @return
     */

    public List<String> getJdWareIdList(String channelId, String cartId, String wareStatus, int checkStockNum) {
        List<String> wareIdList = new ArrayList<>();
        long pageNo = 1;
        if (CmsConstants.PlatformStatus.InStock.name().equals(wareStatus)) {
            while(true) {
                List<Ware> jdList;
                try {
                    // 查询下架
                    jdList = getDeListProduct(channelId, cartId, Long.toString(pageNo), String.valueOf(PAGE_SIZE));
                    pageNo++;
                } catch (JdException apiExp) {
                    throw new BusinessException(String.format("调用京东API获取下架商品时API出错! channelId=%s, cartId=%s", channelId, cartId), apiExp);
                } catch (Exception exp) {
                    throw new BusinessException(String.format("调用京东API获取下架商品时出错! channelId=%s, cartId=%s", channelId, cartId), exp);
                }
                if (jdList != null && jdList.size() > 0) {
                    wareIdList.addAll(jdList.stream().map(ware -> ware.getWareId().toString()).collect(Collectors.toList()));
                }
                if (jdList == null || jdList.size() < PAGE_SIZE) {
                    break;
                }
            }
        } else if (CmsConstants.PlatformStatus.OnSale.name().equals(wareStatus)) {
            while(true) {
                List<Ware> jdList;
                try {
                    // 查询上架
                    jdList = getOnListProduct(channelId, cartId, Long.toString(pageNo), String.valueOf(PAGE_SIZE));
                    pageNo++;
                } catch (JdException apiExp) {
                    throw new BusinessException(String.format("调用京东API获取上架商品时API出错! channelId=%s, cartId=%s", channelId, cartId), apiExp);
                } catch (Exception exp) {
                    throw new BusinessException(String.format("调用京东API获取上架商品时出错! channelId=%s, cartId=%s", channelId, cartId), exp);
                }
                if (jdList != null && jdList.size() > 0) {
                    switch (checkStockNum) {
                        case 0:
                            wareIdList.addAll(jdList.stream()
                                    .map(ware -> ware.getWareId().toString())
                                    .collect(Collectors.toList())
                            );
                            break;
                        case 1:
                            wareIdList.addAll(jdList.stream()
                                    .filter(ware -> ware.getStockNum() > 0)
                                    .map(ware -> ware.getWareId().toString())
                                    .collect(Collectors.toList())
                            );
                            break;
                        case -1:
                            wareIdList.addAll(jdList.stream()
                                    .filter(ware -> ware.getStockNum() <= 0)
                                    .map(ware -> ware.getWareId().toString())
                                    .collect(Collectors.toList())
                            );
                            break;
                    }
                }
                if (jdList == null || jdList.size() < PAGE_SIZE) {
                    break;
                }
            }
        }
        return wareIdList;
    }

}
