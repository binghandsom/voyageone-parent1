package com.voyageone.components.jumei;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jumei.bean.HtMallSkuAddInfo;
import com.voyageone.components.jumei.bean.HtMallSkuPriceUpdateInfo;
import com.voyageone.components.jumei.bean.HtMallUpdateInfo;
import com.voyageone.components.jumei.reponse.HtMallAddResponse;
import com.voyageone.components.jumei.reponse.HtMallSkuAddResponse;
import com.voyageone.components.jumei.reponse.HtMallSkuPriceUpdateResponse;
import com.voyageone.components.jumei.reponse.HtMallUpdateResponse;
import com.voyageone.components.jumei.request.HtMallAddRequest;
import com.voyageone.components.jumei.request.HtMallSkuAddRequest;
import com.voyageone.components.jumei.request.HtMallSkuPriceUpdateRequest;
import com.voyageone.components.jumei.request.HtMallUpdateRequest;
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
    public boolean addMallSku(ShopBean shopBean, HtMallSkuAddInfo mallSkuAddInfo, StringBuffer failCause) throws Exception {
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
            return false;
        } else {
            return true;
        }
    }

}
