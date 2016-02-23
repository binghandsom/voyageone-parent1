package com.voyageone.wms.controller;

import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.base.BaseController;
import com.voyageone.wms.WmsUrlConstants.LocationUrls;
import com.voyageone.wms.formbean.ItemLocationLogFormBean;
import com.voyageone.wms.formbean.LocationFormBean;
import com.voyageone.wms.service.WmsLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Tester on 5/14/2015.
 *
 * @author Jonas
 */
@Controller
@RequestMapping(method = RequestMethod.POST)
public class WmsLocationController extends BaseController {
    @Autowired
    private WmsLocationService locationService;

    /**
     * 货架一览画面初始化
     *
     * @param response status/storeList/fromDate/toDate
     * @param paramMap 入力参数
     */
    @RequestMapping(LocationUrls.List.INIT)
    public void doListInit(HttpServletResponse response, @RequestBody Map<String, Object> paramMap) {

        // 取得初始化的必要项目
        Map<String, Object> result = locationService.doListInit(getUser());

        // 设置返回画面的值
        AjaxResponseBean
                .newResult(true)
                .setResultInfo(result)
                .writeTo(getRequest(), response);
    }

    /**
     * 搜索 Location
     *
     * @param params   location_name/ store_id/ page/ size
     * @param response data/ count
     */
    @RequestMapping(LocationUrls.List.SEARCH)
    public void searchLocation(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        String location_name = (String) params.get("location_name");
        int store_id = (int) params.get("store_id");
        int page = (int) params.get("page");
        int size = (int) params.get("size");

        List<LocationFormBean> locations = locationService.searchByName(location_name, store_id, page, size, getUser());

        int count = locationService.searchCountByName(location_name, store_id, getUser());

        JsonObj result = new JsonObj()
                .add("data", locations)
                .add("count", count);

        AjaxResponseBean
                .newResult(true)
                .setResultInfo(result)
                .writeTo(getRequest(), response);
    }

    /**
     * 删除 Location
     *
     * @param params   location_id/ store_id/ modified
     * @param response ""
     */
    @RequestMapping(LocationUrls.List.DELETE)
    public void deleteLocation(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        int location_id = (int) params.get("location_id");
        int store_id = (int) params.get("store_id");
        String modified = (String) params.get("modified");

        locationService.deleteLocation(location_id, store_id, modified);

        AjaxResponseBean
                .newResult(true)
                .writeTo(getRequest(), response);
    }

    /**
     * 添加 Location
     *
     * @param params   location_name/ store_id
     * @param response ""
     */
    @RequestMapping(LocationUrls.List.ADD)
    public void addLocation(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        String location_name = (String) params.get("location_name");
        int store_id = (int) params.get("store_id");

        locationService.addLocation(location_name, store_id, getUser());

        AjaxResponseBean
                .newResult(true)
                .writeTo(getRequest(), response);
    }

    /**
     * 货架绑定画面初始化
     *
     * @param response status/storeList/fromDate/toDate
     * @param paramMap 入力参数
     */
    @RequestMapping(LocationUrls.Bind.INIT)
    public void doBindInit(HttpServletResponse response, @RequestBody Map<String, Object> paramMap) {

        // 取得初始化的必要项目
        Map<String, Object> result = locationService.doBindInit(getUser());

        // 设置返回画面的值
        AjaxResponseBean
                .newResult(true)
                .setResultInfo(result)
                .writeTo(getRequest(), response);
    }

    @RequestMapping(LocationUrls.Bind.SEARCH)
    public void searchItemLocations(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        String code = (String) params.get("code");
        int store_id = (int) params.get("store_id");

        Map<String, Object> result = locationService.searchItemLocations(code, store_id, getUser());

        AjaxResponseBean
                .newResult(true)
                .setResultInfo(result)
                .writeTo(getRequest(), response);
    }

    @RequestMapping(LocationUrls.Bind.SEARCH_BY_SKU)
    public void searchItemLocationsBySku(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        String sku = (String) params.get("sku");
        int store_id = (int) params.get("store_id");

        Map<String, Object> result = locationService.searchItemLocationsBySku(sku, store_id, getUser());

        AjaxResponseBean
                .newResult(true)
                .setResultInfo(result)
                .writeTo(getRequest(), response);
    }

    @RequestMapping(LocationUrls.Bind.SEARCH_BY_LOCATION_ID)
    public void searchItemLocationsByLocationId(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        int location_id = Integer.valueOf((String) params.get("location_id"));
        int store_id = Integer.valueOf((String) params.get("store_id"));
        String location_name = (String) params.get("location_name");

        Map<String, Object> result = locationService.searchItemLocationsByLocationId(location_id, store_id, location_name, getUser());

        AjaxResponseBean
                .newResult(true)
                .setResultInfo(result)
                .writeTo(getRequest(), response);
    }

    @RequestMapping(LocationUrls.Bind.ADD)
    public void addItemLocation(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        String location_name = (String) params.get("location_name");
        String code = (String) params.get("code");
        String sku = (String) params.get("sku");
//        int store_id = (int) params.get("store_id");
        int store_id = Integer.valueOf(getObjValue(params.get("store_id")));
        // 绑定区分（根据货架，绑定对应的物品）
        String bind_by_Location = (String) params.get("bind_by_Location");

        Map<String, Object> result = locationService.addItemLocation(store_id, code, sku, location_name, bind_by_Location, getUser());

        AjaxResponseBean
                .newResult(true)
                .setResultInfo(result)
                .writeTo(getRequest(), response);
    }

    @RequestMapping(LocationUrls.Bind.DELETE)
    public void deleteItemLocation(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        int item_location_id = (int) params.get("item_location_id");
        String modified = (String) params.get("modified");

        ItemLocationLogFormBean itemLocationLogFormBean = locationService.deleteItemLocation(item_location_id, modified, getUser());

        AjaxResponseBean
                .newResult(true)
                .setResultInfo(itemLocationLogFormBean)
                .writeTo(getRequest(), response);
    }

    /**
     * 根据传递对象类型，取得对应的String
     *
     * @param para 画面传递对象
     * @return 对应的String值
     */
    private String getObjValue(Object para) {
        String ret = "";
        if (para instanceof java.lang.String) {
            ret = (String) para;
        } else {
            ret = String.valueOf((Integer) para);
        }

        return ret;
    }
}
