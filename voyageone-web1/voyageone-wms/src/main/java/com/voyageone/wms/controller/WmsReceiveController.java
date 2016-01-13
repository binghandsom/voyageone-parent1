package com.voyageone.wms.controller;

import com.voyageone.base.BaseController;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.wms.WmsConstants;
import com.voyageone.wms.WmsUrlConstants.ReceiveUrls;
import com.voyageone.wms.formbean.FormReservation;
import com.voyageone.wms.service.WmsPickupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Jack on 5/20/2015.
 *
 * @author Jack
 */
@Controller
@RequestMapping(method = RequestMethod.POST)
public class WmsReceiveController extends BaseController {
    @Autowired
    private WmsPickupService pickupService;

    /**
     * 收货画面初始化
     *
     * @param response status/storeList/fromDate/toDate
     * @param paramMap 入力参数
     */
    @RequestMapping(ReceiveUrls.INIT)
    public void doInit(HttpServletResponse response, @RequestBody Map<String, Object> paramMap) {

        // 取得初始化的必要项目
        Map<String, Object> result = pickupService.doInit(getUser(),WmsConstants.ReserveType.Receive);

        // 设置返回画面的值
        AjaxResponseBean
                .newResult(true)
                .setResultInfo(result)
                .writeTo(getRequest(), response);
    }

    /**
     * 根据检索条件取得抽出记录
     *
     * @param paramMap   order_number/ id/ sku/ status/ store_id/ from/ to/ page/ size
     * @param response data/ count
     */
    @RequestMapping(ReceiveUrls.SEARCH)
    public void searchPickup(@RequestBody Map<String, Object> paramMap, HttpServletResponse response) {

        // 根据检索条件取得抽出记录
        List<FormReservation> pickupList = pickupService.getPickupInfo(paramMap, getUser(),WmsConstants.ReserveType.Receive);

        // 根据检索条件取得抽出记录的件数
        int pickupCount = pickupService.getPickupCount(paramMap, getUser(),WmsConstants.ReserveType.Receive);

        // 设置返回画面的值
        JsonObj result = new JsonObj()
                .add("data", pickupList)
                .add("count", pickupCount);

        AjaxResponseBean
                .newResult(true)
                .setResultInfo(result)
                .writeTo(getRequest(), response);
    }

    /**
     * 根据检索条件取得抽出记录
     *
     * @param paramMap scanNo
     * @param response data
     */
    @RequestMapping(ReceiveUrls.SCAN)
    public void scanPickup(@RequestBody Map<String, Object> paramMap, HttpServletResponse response) {

        Map<String, Object> result = pickupService.getScanInfo(paramMap, getUser(),WmsConstants.ReserveType.Receive);

        AjaxResponseBean
                .newResult(true)
                .setResultInfo(result)
                .writeTo(getRequest(), response);
    }

}