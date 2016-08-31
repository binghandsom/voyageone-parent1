package com.voyageone.components.jumei;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.components.jumei.bean.HtMallSkuPriceUpdateInfo;
import com.voyageone.components.jumei.bean.HtMallStatusUpdateInfo;
import com.voyageone.components.jumei.bean.HtMallUpdateInfo;
import com.voyageone.components.jumei.enums.JmMallStatusType;
import com.voyageone.components.jumei.reponse.*;
import com.voyageone.components.jumei.request.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 聚美商城
 *
 * @author morse on 2016/8/29
 * @version 2.5.0
 */
@Service
public class JumeiHtMallService extends JmBase {

    /**
     * 特卖商品绑定到商城[MALL]
     *
     * @param shopBean 店铺信息
     * @param jumeiHashId 聚美Deal ID.
     * @param failCause 用于保存错误信息
     * @return 聚美生成的MallId
     */
    public String addMall(ShopBean shopBean, String jumeiHashId, StringBuffer failCause) throws Exception {
        HtMallAddRequest request = new HtMallAddRequest();
        request.setJumeiHashId(jumeiHashId);
        String reqResult = reqJmApi(shopBean, request.getUrl(), request.getParameter());
        HtMallAddResponse response = new HtMallAddResponse();
        response.setBody(reqResult);
        if (!response.isSuccess()) {
            failCause.append(response.getErrorMsg());
            return null;
        } else {
            return response.getJumeiMallId();
        }
    }

    /**
     * 编辑商城属性[MALL]
     *
     * @param shopBean 店铺信息
     * @param mallUpdateInfo 更新内容
     * @param failCause 用于保存错误信息
     * @return 是否更新成功
     */
    public boolean updateMall(ShopBean shopBean, HtMallUpdateInfo mallUpdateInfo, StringBuffer failCause) throws Exception {
        HtMallUpdateRequest request = new HtMallUpdateRequest();
        request.setMallUpdateInfo(mallUpdateInfo);
        String reqResult = reqJmApi(shopBean, request.getUrl(), request.getParameter());
        HtMallUpdateResponse response = new HtMallUpdateResponse();
        response.setBody(reqResult);
        if (!response.isSuccess()) {
            failCause.append(response.getErrorMsg());
            return false;
        } else {
            return true;
        }
    }

    /**
     * 批量修改商城价格[MALL]
     *
     * @param shopBean 店铺信息
     * @param updateData 更新内容
     * @param failCause 用于保存错误信息
     * @return 是否更新成功
     */
    public boolean updateMallSkuPrice(ShopBean shopBean, List<HtMallSkuPriceUpdateInfo> updateData, StringBuffer failCause) throws Exception {
        HtMallSkuPriceUpdateRequest request = new HtMallSkuPriceUpdateRequest();
        request.setUpdateData(updateData);
        String reqResult = reqJmApi(shopBean, request.getUrl(), request.getParameter());
        HtMallSkuPriceUpdateResponse response = new HtMallSkuPriceUpdateResponse();
        response.setBody(reqResult);
        if (!response.isSuccess()) {
            failCause.append(response.getErrorMsg());
            return false;
        } else {
            return true;
        }
    }

    /**
     * 上下架商城商品
     *
     * @param shopBean 店铺信息
     * @param mallId 聚美Mall Id
     * @param status 上下架动作
     * @param failCause 用于保存错误信息
     * @return 更新件数
     */
    public int updateMallStatus(ShopBean shopBean, String mallId, JmMallStatusType status, StringBuffer failCause) {
        List<String> listMallId = new ArrayList<>();
        listMallId.add(mallId);
        return updateMallStatusBatch(shopBean, listMallId, status, failCause);
    }

    /**
     * 批量上下架商城商品
     *
     * @param shopBean 店铺信息
     * @param listMallId 聚美Mall Id
     * @param status 上下架动作
     * @param failCause 用于保存错误信息
     * @return 更新件数
     */
    public int updateMallStatusBatch(ShopBean shopBean, List<String> listMallId, JmMallStatusType status, StringBuffer failCause) {
        int updateCnt = 0;
        int buffer = 20; // 目前这个API一次最多处理20个mall_id
        List<HtMallStatusUpdateInfo> goodsJson;
        HtMallStatusUpdateBatchRequest request;
        HtMallStatusUpdateBatchResponse response;

        List<List<String>> splitList = CommonUtil.splitList(listMallId, buffer);
        for (List<String> splitListMallId : splitList) {
            goodsJson = new ArrayList<>();
            for (String mallId : splitListMallId) {
                HtMallStatusUpdateInfo mallStatusUpdateInfo = new HtMallStatusUpdateInfo();
                mallStatusUpdateInfo.setJumeiMallId(mallId);
                mallStatusUpdateInfo.setStatus(status.getVal());
                goodsJson.add(mallStatusUpdateInfo);
            }

            request = new HtMallStatusUpdateBatchRequest();
            request.setGoodsJson(goodsJson);
            try {
                String reqResult = reqJmApi(shopBean, request.getUrl(), request.getParameter());
                response = new HtMallStatusUpdateBatchResponse();
                response.setBody(reqResult);
                if (!response.isSuccess()) {
                    failCause.append("MallId[" + splitListMallId + "]:" + response.getErrorMsg() + "! ");
                } else {
                    updateCnt += splitListMallId.size();
                }
            } catch (Exception e) {
                failCause.append("MallId[" + splitListMallId + "]:调用API时发生异常! ");
            }
        }

        return updateCnt;
    }
}
