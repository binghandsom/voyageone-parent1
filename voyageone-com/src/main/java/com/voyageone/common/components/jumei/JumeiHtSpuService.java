package com.voyageone.common.components.jumei;
import com.voyageone.common.components.jumei.Reponse.HtProductUpdateResponse;
import com.voyageone.common.components.jumei.Reponse.HtSpuAddResponse;
import com.voyageone.common.components.jumei.Request.HtProductUpdateRequest;
import com.voyageone.common.components.jumei.Request.HtSpuAddRequest;
import com.voyageone.common.components.jumei.base.JmBase;
import com.voyageone.common.configs.beans.ShopBean;

import java.util.Map;

/**
 * Created by dell on 2016/3/29.
 */
public class JumeiHtSpuService extends JmBase {
    public HtSpuAddResponse add(ShopBean shopBean, HtSpuAddRequest request) throws Exception {
        Map<String, Object> params = request.getParameter();
        String reqResult = reqJmApi(shopBean, request.getUrl(), params);
        logger.info("添加Spu信息返回：" + reqResult);
        HtSpuAddResponse response = new HtSpuAddResponse();
        response.setBody(reqResult);
        return response;
    }

}
