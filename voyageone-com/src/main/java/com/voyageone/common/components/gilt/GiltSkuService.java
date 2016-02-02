package com.voyageone.common.components.gilt;

import com.voyageone.common.components.gilt.base.GiltBase;
import com.voyageone.common.components.gilt.bean.GiltPageGetSkusRequest;
import com.voyageone.common.components.gilt.bean.GiltRealTimeInventory;
import com.voyageone.common.components.gilt.bean.GiltSku;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class GiltSkuService extends GiltBase {

    private static final String URL = "skus";


    /**
     *  分页获取Skus
     * @param shopBean shopBean
     * @param request request
     * @return List
     * @throws Exception
     */
    public List<GiltSku> pageGetSkus(ShopBean shopBean, GiltPageGetSkusRequest request) throws Exception {
        request.check();
        Map map=JacksonUtil.jsonToMap(JacksonUtil.bean2Json(request));
        Map<String,String> params=new HashMap<String,String>();
        params.putAll(map);
        String result=reqGiltApi(shopBean,URL,params);
        return JacksonUtil.jsonToBeanList(result,GiltSku.class);
    }

    /**
     *  根据Id 获取Sku
     * @param shopBean shopBean
     * @param skuId skuId
     * @return GiltSku
     * @throws Exception
     */
    public GiltSku getSkuById(ShopBean shopBean, String skuId) throws Exception {
        if(StringUtils.isNullOrBlank2(skuId))
            throw new IllegalArgumentException("skuId不能为空");
        String result=reqGiltApi(shopBean,URL+"/"+skuId,new HashMap<String,String>());
        return JacksonUtil.json2Bean(result,GiltSku.class);
    }

}
