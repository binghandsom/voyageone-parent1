package com.voyageone.web2.cms.views.search;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.search.index.CmsSearchInfoBean;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 15/12/14
 */
@RestController
@RequestMapping(
        value = CmsUrlConstants.SEARCH.ADVANCE.ROOT,
        method = RequestMethod.POST
)
public class CmsSearchAdvanceController extends CmsController {

    @Autowired
    private CmsSearchAdvanceService searchIndexService;

    /**
     * 初始化,获取master数据
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.INIT)
    public AjaxResponse init() throws Exception {
        return success(searchIndexService.getMasterData(getUser(), getLang()));
    }

    /**
     * 检索出group和product数据
     * @param params
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.SEARCH)
    public AjaxResponse search(@RequestBody CmsSearchInfoBean params) {

        Map<String, Object> resultBean = new HashMap<>();

        // 获取product列表
        ProductsGetResponse productList = searchIndexService.GetProductList(params, getUser(), getCmsSession());
        resultBean.put("productList", productList.getProducts());
        resultBean.put("productListTotal", productList.getTotalCount());

        // 获取group列表
        ProductsGetResponse groupList = searchIndexService.getGroupList(params, getUser(), getCmsSession());
        resultBean.put("groupList", groupList.getProducts());
        resultBean.put("groupListTotal", groupList.getTotalCount());

        // 返回用户信息
        return success(resultBean);
    }

    /**
     * 分页检索group数据
     * @param params
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.GET_GROUP_LIST)
    public AjaxResponse getGroupList(@RequestBody CmsSearchInfoBean params) {

        Map<String, Object> resultBean = new HashMap<>();

        ProductsGetResponse groupList = searchIndexService.getGroupList(params, getUser(), getCmsSession());
        resultBean.put("groupList", groupList.getProducts());
        resultBean.put("groupListTotal", groupList.getTotalCount());

        // 返回用户信息
        return success(resultBean);
    }

    /**
     * 分页检索product数据
     * @param params
     * @return
     */
    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.GET_PRODUCT_LIST)
    public AjaxResponse getProductList(@RequestBody CmsSearchInfoBean params) {

        Map<String, Object> resultBean = new HashMap<>();

        ProductsGetResponse productList = searchIndexService.GetProductList(params, getUser(), getCmsSession());
        resultBean.put("productList", productList.getProducts());
        resultBean.put("productListTotal", productList.getTotalCount());

        // 返回用户信息
        return success(resultBean);
    }

    @RequestMapping(CmsUrlConstants.SEARCH.ADVANCE.EXPORT_PRODUCTS)
    public ResponseEntity<byte[]> doExport(HttpServletRequest request, HttpServletResponse response, @RequestBody CmsSearchInfoBean params)
            throws Exception {

        byte[] data = searchIndexService.getCodeExcelFile(params, getUser(), getCmsSession());
        return genResponseEntityFromBytes("product_" + DateTimeUtil.getLocalTime(getUserTimeZone())+".xlsx", data);

    }

}
