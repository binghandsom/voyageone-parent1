package com.voyageone.wms.service;

import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.wms.modelbean.ItemDetailBean;
import com.voyageone.wms.modelbean.external.WmsProductBean;
import com.voyageone.wms.modelbean.external.WmsProductTypeBean;

import java.util.List;
import java.util.Map;

/**
 * Created by Tester on 5/21/2015.
 *
 * @author Jonas
 */
public interface WmsItemDetailService {

    Map<String, Object> getProduct(String code, String order_channel_id);

    WmsProductBean saveProduct(WmsProductBean productBean, UserSessionBean user);

    List<String> getAllSize(String order_channel_id, int product_type_id);

    ItemDetailBean saveItemDetail(ItemDetailBean itemDetailBean, UserSessionBean user);

    List<WmsProductTypeBean> getAllProductTypes(String order_channel_id);
}
