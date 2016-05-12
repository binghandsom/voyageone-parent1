package com.voyageone.components.tmall.service;

import com.taobao.api.ApiException;
import com.taobao.api.request.ItemQuantityUpdateRequest;
import com.taobao.api.request.SkusQuantityUpdateRequest;
import com.taobao.api.response.ItemQuantityUpdateResponse;
import com.taobao.api.response.SkusQuantityUpdateResponse;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.TbBase;
import com.voyageone.components.tmall.bean.ItemQuantityBean;
import com.voyageone.components.tmall.bean.SkusQuantityBean;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 用于调用处理库存 API 的服务类
 * Created by jonas on 15/6/4.
 */
@Component
public class TbInventoryService extends TbBase {

    /**
     * 更新淘宝，单个商品的数量
     *
     * @param shopBean         店铺信息
     * @param itemQuantityBean 商品和数量
     * @return 响应结果
     */
    public ItemQuantityUpdateResponse itemQuantityUpdate(ShopBean shopBean, ItemQuantityBean itemQuantityBean) throws ApiException {

        ItemQuantityUpdateRequest req = new ItemQuantityUpdateRequest();

        req.setNumIid(itemQuantityBean.getNum_iid());
        req.setQuantity(itemQuantityBean.getQuantity());
        req.setOuterId(itemQuantityBean.getSku());
        req.setType(itemQuantityBean.isTotal() ? 1L : 2L);

        return reqTaobaoApi(shopBean, req);
    }

    /**
     * 批量更新淘宝的库存数量
     *
     * @param shopBean         店铺信息
     * @param skusQuantityBean 商品和数量
     * @return 响应结果
     */
    public SkusQuantityUpdateResponse skusQuantityUpdate(ShopBean shopBean, SkusQuantityBean skusQuantityBean) throws ApiException {

        Map<String, Integer> skusQuantities = skusQuantityBean.getSkuQuantities();

        if (skusQuantities.size() > 20) {
            throw new IllegalArgumentException("批量更新的 SKU 总共不能超过20个。");
        }

        // 根据信息拼接更新的 SKU 和数量
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, Integer> entry : skusQuantities.entrySet()) {
            builder.append(entry.getKey());
            builder.append(":");
            builder.append(entry.getValue());
            builder.append(";");
        }

        // 移除最后的分号
        builder.deleteCharAt(builder.length() - 1);

        // 创建 request 准备调用
        SkusQuantityUpdateRequest req = new SkusQuantityUpdateRequest();

        req.setNumIid(skusQuantityBean.getNum_iid());
        req.setType(skusQuantityBean.isTotal() ? 1L : 2L);
        req.setSkuidQuantities(builder.toString());

        return reqTaobaoApi(shopBean, req);

    }
}
