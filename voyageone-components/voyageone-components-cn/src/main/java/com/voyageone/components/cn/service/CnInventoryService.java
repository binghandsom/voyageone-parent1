package com.voyageone.components.cn.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.cn.CnBase;
import com.voyageone.components.cn.beans.InventoryUpdateBean;
import com.voyageone.components.cn.enums.InventorySynType;
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

    /**
     * 更新独立域名的商品库存
     *
     * @param synType 同步类型
     * @param shopBean 店铺信息
     * @param beans 商品信息
     * @return response
     * @throws UnsupportedEncodingException
     */
    public String UpdateInventory(InventorySynType synType, ShopBean shopBean, List<InventoryUpdateBean> beans) throws Exception {

        String sign = shopBean.getAppKey();

        String md5 = DigestUtils.md5DigestAsHex(sign.getBytes("UTF-8"));

        Map<String, Object> jsonMap = new HashMap<>();

        List<Map<String, Object>> p = new ArrayList<>();

        Map<String, Object> b;

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
