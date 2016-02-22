package com.voyageone.wms.service;

import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.wms.formbean.ItemLocationFormBean;
import com.voyageone.wms.formbean.ItemLocationLogFormBean;
import com.voyageone.wms.formbean.LocationFormBean;
import com.voyageone.wms.modelbean.ItemLocationBean;
import com.voyageone.wms.modelbean.ItemLocationLogBean;
import com.voyageone.wms.modelbean.LocationBean;

import java.util.List;
import java.util.Map;

/**
 * Created by Tester on 5/14/2015.
 *
 * @author Jonas
 */
public interface WmsLocationService {
    List<LocationFormBean> searchByName(String name, int store_id, int page, int size, UserSessionBean user);

    int searchCountByName(String name, int store_id, UserSessionBean user);

    void deleteLocation(int location_id, int store_id, String modified);

    void addLocation(String location_name, int store_id, UserSessionBean user);

    Map<String, Object> searchItemLocations(String code, int store_id, UserSessionBean user);

    Map<String, Object> searchItemLocationsBySku(String sku, int store_id, UserSessionBean user);

    Map<String, Object> searchItemLocationsByLocationId(int location_id, int store_id, UserSessionBean user);

    Map<String, Object> addItemLocation(int store_id, String code, String sku, String location_name, UserSessionBean user);

    ItemLocationLogFormBean deleteItemLocation(int item_location_id, String modified, UserSessionBean user);

    Map<String, Object> doListInit(UserSessionBean user);

    Map<String, Object> doBindInit(UserSessionBean user);
}
