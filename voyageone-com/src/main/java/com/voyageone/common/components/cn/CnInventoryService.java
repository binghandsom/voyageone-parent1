package com.voyageone.common.components.cn;

import com.google.gson.Gson;
import com.voyageone.common.components.cn.base.CnBase;
import com.voyageone.common.components.cn.beans.InventoryUpdateBean;
import com.voyageone.common.components.cn.enums.InventorySynType;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.HttpUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 为独立域名的库存修改 API 提供封装调用
 *
 * Created by jonas on 15/6/8.
 */
@Component
public class CnInventoryService extends CnBase {

    public final String trustStore_jc = "/opt/app-shared/voyageone_web/contents/other/third_party/004/cn_key/JuicyCouture.crt";

    /**
     * 更新独立域名的商品库存
     *
     * @param synType 同步类型
     * @param shopBean 店铺信息
     * @param beans 商品信息
     * @return response
     * @throws UnsupportedEncodingException
     */
    public String UpdateInventory(InventorySynType synType, ShopBean shopBean, List<InventoryUpdateBean> beans) throws UnsupportedEncodingException {

        String sign = shopBean.getAppKey();

        String md5 = DigestUtils.md5DigestAsHex(sign.getBytes("UTF-8"));

        Map<String, Object> jsonMap = new HashMap<>();

        List<Map<String, Object>> p = new ArrayList<>();

        Map<String, Object> b;

        // JC官网使用https
        if (shopBean.getOrder_channel_id().equals(ChannelConfigEnums.Channel.JC.getId())) {
            System.setProperty("javax.net.ssl.trustStore", trustStore_jc);
            System.setProperty("javax.net.ssl.trustStorePassword", "voyage1#");
        }

        jsonMap.put("t", synType.val()); // 设定同步的类型为全量

        jsonMap.put("p", p); // 设定商品集合

        jsonMap.put("k", md5); // 设定 key

        for (InventoryUpdateBean bean: beans) {
            b = new HashMap<>();

            b.put("s", bean.getSku());
            b.put("q", bean.getQty());

            p.add(b);
        }

        return post("/catalog_request_json.php", jsonMap, shopBean);
    }
}
