package com.voyageone.common.components.jumei;
import com.voyageone.common.components.jumei.Reponse.HtDealCopyDealResponse;
import com.voyageone.common.components.jumei.Request.HtDealCopyDealRequest;
import com.voyageone.common.components.jumei.Request.HtDealUpdateRequest;
import com.voyageone.common.components.jumei.Reponse.HtDealUpdateResponse;
import com.voyageone.common.components.jumei.base.JmBase;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Map;
@Service
public class JumeiHtDealService extends JmBase {
    public HtDealUpdateResponse update(ShopBean shopBean, HtDealUpdateRequest request) throws Exception {
        Map<String, Object> params = request.getParameter();
        String reqResult = reqJmApi(shopBean, request.getUrl(), params);
        logger.info("修改Deal(特卖)信息返回：" + reqResult);
        HtDealUpdateResponse response = new HtDealUpdateResponse();
        response.setBody(reqResult);
        return response;
    }
    public HtDealCopyDealResponse copyDeal(ShopBean shopBean, HtDealCopyDealRequest request) throws Exception {
        Map<String, Object> params = request.getParameter();
        String reqResult = reqJmApi(shopBean, request.getUrl(), params);
        logger.info("复制Deal(特卖)信息返回：" + reqResult);
        HtDealCopyDealResponse response = new HtDealCopyDealResponse();
        response.setBody(reqResult);
        return response;
    }
}