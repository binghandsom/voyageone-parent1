package com.voyageone.components.jumei.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.JmBase;
import com.voyageone.components.jumei.bean.HtMallStatusUpdateInfo;
import com.voyageone.components.jumei.enums.JmMallStatusType;
import com.voyageone.components.jumei.reponse.HtMallStatusUpdateBatchResponse;
import com.voyageone.components.jumei.reponse.HtProductGetByStatusResponse;
import com.voyageone.components.jumei.request.HtMallStatusUpdateBatchRequest;
import com.voyageone.components.jumei.request.HtProductGetByStatusRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 聚美商城-商品上下架， 以及查询商品的在售/在库状态
 *
 * @author morse on 2016/8/29
 * @version 2.5.0
 */
@Service
public class JumeiSaleService extends JmBase {

    /**
     * 上架商品
     */
    public HtMallStatusUpdateBatchResponse doWareUpdateListing(ShopBean shopBean, String mallId) {
        return doWareUpdate(shopBean, mallId, JmMallStatusType.ToOnSale.getVal());
    }

    /**
     * 下架商品
     */
    public HtMallStatusUpdateBatchResponse doWareUpdateDelisting(ShopBean shopBean, String mallId) {
        return doWareUpdate(shopBean, mallId, JmMallStatusType.ToInStock.getVal());
    }

    /**
     * 上下架商品
     */
    private HtMallStatusUpdateBatchResponse doWareUpdate(ShopBean shopBean, String mallId, String opeType) {
        List<HtMallStatusUpdateInfo> goodsJson = new ArrayList<>();
        HtMallStatusUpdateInfo mallStatusUpdateInfo = new HtMallStatusUpdateInfo();
        mallStatusUpdateInfo.setJumei_mall_id(mallId);
        mallStatusUpdateInfo.setStatus(opeType);
        goodsJson.add(mallStatusUpdateInfo);

        HtMallStatusUpdateBatchRequest request = new HtMallStatusUpdateBatchRequest();
        request.setGoodsJson(goodsJson);
        try {
            String reqResult = reqJmApi(shopBean, request.getUrl(), request.getParameter());
            HtMallStatusUpdateBatchResponse response = new HtMallStatusUpdateBatchResponse();
            response.setBody(reqResult);
            return response;
        } catch (Exception e) {
            logger.error("MallId[" + mallId + "]:调用API时发生异常! ", e);
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 获取上架/在售状态的产品列表
     * 只返回 ware_id (即num_iid)
     */
    public List<String> getOnListProduct(String strOrderChannelId, String strCardId, Integer page, Integer pageSize) {
        ShopBean shopInfo = Shops.getShop(strOrderChannelId, strCardId);

        return getProductList(shopInfo, JmMallStatusType.ToOnSale.getVal(), page, pageSize);
    }

    /**
     * 获取下架/在库状态的产品列表
     * 只返回wareId(即num_iid)
     */
    public List<String> getDeListProduct(String strOrderChannelId, String strCardId, Integer page, Integer pageSize) {
        ShopBean shopInfo = Shops.getShop(strOrderChannelId, strCardId);

        return getProductList(shopInfo, JmMallStatusType.ToInStock.getVal(), page, pageSize);
    }

    /**
     * 查询指定状态的商品
     */
    private List<String> getProductList(ShopBean shopBean, String statusType, Integer page, Integer pageSize) {
        HtProductGetByStatusRequest request = new HtProductGetByStatusRequest();
        request.setStatus(statusType);
        request.setPage(page);
        request.setPageSize(pageSize);

        try {
            String reqResult = reqJmApi(shopBean, request.getUrl(), request.getParameter());
            HtProductGetByStatusResponse response = new HtProductGetByStatusResponse();
            response.setBody(reqResult);
            List<Map<String, Object>> prodInfos = response.getProdInfos();
            Object[] objs = { statusType, shopBean.getOrder_channel_id(), shopBean.getCart_id(), prodInfos == null ? "total=" + "空" : prodInfos.size() };
            logger.info("get{}ProductList调用结果 channelid={}, cartid={}, 结果={}", objs);
            if (prodInfos == null || prodInfos.isEmpty()) {
                return null;
            }
            List<String> numIIdList = prodInfos.stream().map(tmItem -> (String) tmItem.get("jumei_mall_id")).collect(Collectors.toList());
            return numIIdList;
        } catch (Exception e) {
            logger.error("调用API时发生异常! ", e);
        }
        return null;
    }
}
