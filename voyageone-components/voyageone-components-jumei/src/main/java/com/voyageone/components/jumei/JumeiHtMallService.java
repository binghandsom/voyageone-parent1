package com.voyageone.components.jumei;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jumei.bean.HtMallSkuAddInfo;
import com.voyageone.components.jumei.bean.HtMallSkuPriceUpdateInfo;
import com.voyageone.components.jumei.bean.HtMallUpdateInfo;
import com.voyageone.components.jumei.reponse.*;
import com.voyageone.components.jumei.request.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String reqResult;
        String errorMallId = null;
        try {
            reqResult = reqJmApi(shopBean, request.getUrl(), request.getParameter());
        } catch (BusinessException bex) {
            if (bex.getInfo() != null && bex.getInfo().length > 0) {
                reqResult = (String) bex.getInfo()[0];
                if (!StringUtils.isEmpty(reqResult)) {
                    // 判断一下,是不是有add成功并生成了mallId,只是有别的错误
                    String[] regexs = {"MALLID", "MALL_ID", "MALL ID"};
                    String error = reqResult.toUpperCase();
                    for (String regex : regexs) {
                        Pattern pattern = Pattern.compile(regex + ":\\d+");
                        Matcher matcher = pattern.matcher(error);
                        if (matcher.find()) {
                            String matchString = error.substring(matcher.start(), matcher.end());
                            Pattern mallIdPattern = Pattern.compile("\\d+");
                            Matcher mallIdMatcher = mallIdPattern.matcher(matchString);
                            if (mallIdMatcher.find()) {
                                errorMallId = matchString.substring(mallIdMatcher.start(), mallIdMatcher.end());
                            }
                        }
                    }
                }
            } else {
                throw bex;
            }
        }
        HtMallAddResponse response = new HtMallAddResponse();
        response.setBody(reqResult);
        if (!response.isSuccess()) {
            failCause.append(response.getErrorMsg());
            return errorMallId;
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
        String reqResult;
        try {
            reqResult = reqJmApi(shopBean, request.getUrl(), request.getParameter());
        } catch (BusinessException bex) {
            if (bex.getInfo() != null && bex.getInfo().length > 0) {
                reqResult = (String) bex.getInfo()[0];
            } else {
                throw bex;
            }
        }
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
     * 批量修改聚美城SKU价格[MALL]
     * 如果更新对象sku件数大于20件，自动循环多次每次更新20件sku
     *
     * @param shopBean 店铺信息
     * @param updateData 更新内容
     * @param failCause 用于保存错误信息
     * @return 是否更新成功
     */
    public boolean updateMallSkuPriceBatch(ShopBean shopBean, List<HtMallSkuPriceUpdateInfo> updateData, StringBuffer failCause) throws Exception {

        if (ListUtils.isNull(updateData)) {
            String errMsg = "批量修改聚美商城SKU价格时，通过参数传入的更新对象SKU内容为空!";
            failCause.append(errMsg);
            return false;
        }

        // 批量修改商城SKU价格时，一次最多允许20个sku
        boolean result = true;
        List<List<HtMallSkuPriceUpdateInfo>> pageList = CommonUtil.splitList(updateData, 20);
        for(List<HtMallSkuPriceUpdateInfo> page : pageList) {
            try {
                boolean currentResult = updateMallSkuPrice(shopBean, page, failCause);
                result = result && currentResult;
            } catch (Exception e) {
                failCause.append(e.getMessage());
            }
        }

        if (result && !StringUtils.isEmpty(failCause.toString())) {
            result = false;
        }

        return result;
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
        String reqResult;
        try {
            reqResult = reqJmApi(shopBean, request.getUrl(), request.getParameter());
        } catch (BusinessException bex) {
            if (bex.getInfo() != null && bex.getInfo().length > 0) {
                reqResult = (String) bex.getInfo()[0];
            } else {
                throw bex;
            }
        }
        logger.info(JacksonUtil.bean2Json(reqResult));
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
     * 商城商品追加sku[MALL]
     *
     * @param shopBean 店铺信息
     * @param mallSkuAddInfo 更新内容
     * @param failCause 用于保存错误信息
     * @return 是否更新成功
     */
    public String addMallSku(ShopBean shopBean, HtMallSkuAddInfo mallSkuAddInfo, StringBuffer failCause) throws Exception {
        HtMallSkuAddRequest request = new HtMallSkuAddRequest();
        request.setMallSkuAddInfo(mallSkuAddInfo);
        String reqResult;
        try {
            reqResult = reqJmApi(shopBean, request.getUrl(), request.getParameter());
        } catch (BusinessException bex) {
            if (bex.getInfo() != null && bex.getInfo().length > 0) {
                reqResult = (String) bex.getInfo()[0];
            } else {
                throw bex;
            }
        }
        HtMallSkuAddResponse response = new HtMallSkuAddResponse();
        response.setBody(reqResult);
        if (!response.isSuccess()) {
            failCause.append(response.getErrorMsg());
            return null;
        } else {
            return response.getJumeiSkuNo();
        }
    }

    /**
     * 编辑商城的sku[MALL] 上下架，商家商品编码等
     *
     * @param shopBean 店铺信息
     * @param jumeiSkuNo 聚美Sku_No(必须)
     * @param status 是否启用，enabled-是，disabled-否(非必须)
     * @param customsProductNumber 海关备案商品编码(非必须) 参数范围: 注:获取仓库接口返回bonded_area_id字段 大于０表示保税区仓库
     * @param businessmanNum 商家商品编码(非必须)
     * @param failCause 用于保存错误信息
     * @return 是否更新成功
     */
    public boolean updateSkuForMall(ShopBean shopBean, String jumeiSkuNo, String status, String customsProductNumber, String businessmanNum, StringBuffer failCause) throws Exception {
        HtMallUpdateSkuForMallRequest request = new HtMallUpdateSkuForMallRequest();
        request.setJumei_sku_no(jumeiSkuNo);
        request.setStatus(status);
        request.setCustoms_product_number(customsProductNumber);
        request.setBusinessman_num(businessmanNum);

        String reqResult;
        try {
            reqResult = reqJmApi(shopBean, request.getUrl(), request.getParameter());
        } catch (BusinessException bex) {
            if (bex.getInfo() != null && bex.getInfo().length > 0) {
                reqResult = (String) bex.getInfo()[0];
            } else {
                throw bex;
            }
        }
        HtMallUpdateSkuForMallResponse response = new HtMallUpdateSkuForMallResponse();
        response.setBody(reqResult);
        if (!response.isSuccess()) {
            failCause.append(response.getErrorMsg());
            return false;
        } else {
            return true;
        }
    }

    /**
     * 编辑商城Sku属性[MALL]
     *
     * @param shopBean 店铺信息
     * @param failCause 用于保存错误信息
     * @return 是否更新成功
     */
    public boolean updateMallSku(ShopBean shopBean, String jumei_sku_no, boolean enabled, StringBuffer failCause) throws Exception {
        HtMallSkuUpdateRequest request = new HtMallSkuUpdateRequest();
        request.setJumei_sku_no(jumei_sku_no);
        request.setEnabled(enabled);
        String reqResult = reqJmApi(shopBean, request.getUrl(), request.getParameter());
        HtMallSkuUpdateResponse response = new HtMallSkuUpdateResponse();
        response.setBody(reqResult);
        if (!response.isSuccess()) {
            failCause.append(response.getErrorMsg());
            return false;
        } else {
            return true;
        }
    }

}
