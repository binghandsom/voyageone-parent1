package com.voyageone.components.jumei;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.reponse.HtSpuAddResponse;
import com.voyageone.components.jumei.reponse.HtSpuUpdateResponse;
import com.voyageone.components.jumei.request.HtSpuAddRequest;
import com.voyageone.components.jumei.request.HtSpuUpdateRequest;
import org.springframework.stereotype.Service;
import java.util.Map;
@Service
public class JumeiHtSpuService extends JmBase {
    public HtSpuAddResponse add(ShopBean shopBean, HtSpuAddRequest request) throws Exception {
        Map<String, Object> params = request.getParameter();
        String reqResult;
        try {
            reqResult = reqJmApi(shopBean, request.getUrl(), params);
        } catch (BusinessException bex) {
            if (bex.getInfo() != null && bex.getInfo().length>0) {
                reqResult = (String) bex.getInfo()[0];
            } else {
                throw bex;
            }
        }
        logger.info("添加Spu信息返回：" + reqResult);
        HtSpuAddResponse response = new HtSpuAddResponse();
        response.setBody(reqResult);
        return response;
    }
    public HtSpuUpdateResponse update(ShopBean shopBean, HtSpuUpdateRequest request) throws Exception {
        Map<String, Object> params = request.getParameter();
        String reqResult;
        try {
            reqResult = reqJmApi(shopBean, request.getUrl(), params);
        } catch (BusinessException bex) {
            if (bex.getInfo() != null && bex.getInfo().length>0) {
                reqResult = (String) bex.getInfo()[0];
            } else {
                throw bex;
            }
        }
        logger.info("更新Spu信息返回：" + reqResult);
        HtSpuUpdateResponse response = new HtSpuUpdateResponse();
        response.setBody(reqResult);
        return response;
    }
}