package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Bean.JmWarehouseBean;
import com.voyageone.common.components.jumei.base.JmBase;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chuanyu.laing on 2016/1/25.
 */
@Service
public class JumeiWarehouseService extends JmBase {

    private static List<JmWarehouseBean> warehouses = null;

    private static String WARE_HOUSE_URL = "v1/htWarehouse/query";
    /**
     * 初始化商家仓库
     */
    public void initWarehouse(ShopBean shopBean) throws Exception {
        Map<String, String> param = new HashMap<>();
        param.put("fields", "shipping_system_id,shipping_system_name");
        String result = reqJmApi(shopBean, WARE_HOUSE_URL, param);
        List<JmWarehouseBean> warehouseList = JsonUtil.jsonToBeanList(result, JmWarehouseBean.class);
        if (warehouseList != null) {
            warehouses = warehouseList;
        } else {
            warehouses = new ArrayList<>();
        }
    }


    /**
     * 获取全部商品商家仓库
     */
    public List<JmWarehouseBean> getWarehouseList(ShopBean shopBean) throws Exception {
        if (warehouses == null) {
            initWarehouse(shopBean);
        }
        return warehouses;
    }

    /**
     * 根据名称查找商家仓库4Model
     */
    public JmWarehouseBean getWarehouseByName(ShopBean shopBean, String name) throws Exception {
        if (name == null) {
            return null;
        }

        if (warehouses == null) {
            initWarehouse(shopBean);
        }

        for (JmWarehouseBean warehouse : warehouses) {
            if (warehouse != null && warehouse.getShipping_system_name() != null
                    && warehouse.getShipping_system_name().equals(name)) {
                return warehouse;
            }
        }

        return null;
    }




}
