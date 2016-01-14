package com.voyageone.wms.controller;

import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.base.BaseController;
import com.voyageone.wms.WmsUrlConstants.UpcUrls;
import com.voyageone.wms.modelbean.ItemDetailBean;
import com.voyageone.wms.modelbean.external.WmsProductBean;
import com.voyageone.wms.modelbean.external.WmsProductTypeBean;
import com.voyageone.wms.service.WmsItemDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Upc 管理相关的数据操作 Controller
 * Created by Tester on 5/21/2015.
 *
 * @author Jonas
 */
@Controller
@RequestMapping(method = RequestMethod.POST)
public class WmsUpcManageController extends BaseController {
    @Autowired
    private WmsItemDetailService itemDetailService;

    /**
     * 使用 code 在指定渠道下，搜索商品，返回该 code 的所有 Item_Detail 信息，和 code 对应的 Product 信息
     */
    @RequestMapping(UpcUrls.GET_PRODUCT)
    public void getProduct(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        String order_channel_id = (String) params.get("order_channel_id");
        String code = (String) params.get("code");

        Map<String, Object> result = itemDetailService.getProduct(code, order_channel_id);

        AjaxResponseBean.newResult(true)
                .setResultInfo(result)
                .writeTo(getRequest(), response);
    }

    /**
     * 当查询不到商品时，或在手动触发保存时，只保存商品的信息。
     */
    @RequestMapping(UpcUrls.SAVE_PRODUCT)
    public void saveProduct(@RequestBody WmsProductBean productBean, HttpServletResponse response) {
        WmsProductBean beanInDb = itemDetailService.saveProduct(productBean, getUser());

        AjaxResponseBean.newResult(true)
                .setResultInfo(beanInDb)
                .writeTo(getRequest(), response);
    }

    /**
     * 获取渠道下的所有的尺码名称
     */
    @RequestMapping(UpcUrls.GET_ALL_SIZE)
    public void getAllSize(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        String order_channel_id = (String) params.get("order_channel_id");
        int product_type_id = (int) params.get("product_type_id");

        List<String> sizes = itemDetailService.getAllSize(order_channel_id, product_type_id);

        AjaxResponseBean.newResult(true)
                .setResultInfo(sizes)
                .writeTo(getRequest(), response);
    }

    /**
     * 用户输入 UPC 后，绑定保存这个信息（或更新）
     */
    @RequestMapping(UpcUrls.SAVE_ITEM_DETAIL)
    public void saveItemDetail(@RequestBody ItemDetailBean itemDetailBean, HttpServletResponse response) {

        itemDetailBean = itemDetailService.saveItemDetail(itemDetailBean, getUser());

        AjaxResponseBean.newResult(true)
                .setResultInfo(itemDetailBean)
                .writeTo(getRequest(), response);
    }

    /**
     * 在新增商品的时候，需要选择商品的 product_type
     */
    @RequestMapping(UpcUrls.GET_ALL_PRODUCT_TYPE)
    public void getAllProductTypes(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        String order_channel_id = (String) params.get("order_channel_id");

        List<WmsProductTypeBean> productTypeBeans = itemDetailService.getAllProductTypes(order_channel_id);

        AjaxResponseBean.newResult(true)
                .setResultInfo(productTypeBeans)
                .writeTo(getRequest(), response);
    }
}
