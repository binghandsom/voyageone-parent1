package com.voyageone.web2.cms.views.product;

import com.voyageone.service.bean.cms.producttop.AddTopProductParameter;
import com.voyageone.service.bean.cms.producttop.GetTopListParameter;
import com.voyageone.service.bean.cms.producttop.ProductPageParameter;
import com.voyageone.service.bean.cms.producttop.SaveTopProductParameter;
import com.voyageone.service.impl.cms.product.ProductTopService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * Created by dell on 2016/11/28.
 *
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = CmsUrlConstants.ProductTop.ROOT)
public class CmsProductTopController extends CmsController {

    private final ProductTopService service;

    @Autowired
    public CmsProductTopController(ProductTopService service) {
        this.service = service;
    }

    //获取初始化数据
    @RequestMapping(CmsUrlConstants.ProductTop.Init)
    public AjaxResponse init(@RequestBody Map<String, Object> map) throws IOException {
        String catId = map.get("catId").toString();
        Integer cartId = (Integer) map.get("cartId");
        UserSessionBean userSessionBean = getUser();
        return success(service.init(userSessionBean.getSelChannelId(), cartId, catId, getLang()));
    }

    //普通区查询 获取指定页
//    @RequestMapping(CmsUrlConstants.ProductTop.GetPage)
//    public AjaxResponse getPage(@RequestBody ProductPageParameter param) {
//        UserSessionBean userSessionBean = getUser();
//
//        /**vo项目中默认分页字段为curr size , 字段转换*/
//        param.setPageIndex(param.getCurr());
//        param.setPageSize(param.getSize());
//
//        return success(service.getPage(param, userSessionBean.getSelChannelId(), userSessionBean.getUserName()));
//    }

    //普通区查询 获取数量
//    @RequestMapping(CmsUrlConstants.ProductTop.GetCount)
//    public Object getCount(@RequestBody ProductPageParameter param) {
//
//        return success(service.getCount(param, getUser().getSelChannelId()));
//    }

    //获取置顶区 列表
    @RequestMapping(CmsUrlConstants.ProductTop.GetTopList)
    public AjaxResponse getTopList(@RequestBody GetTopListParameter parameter) {
        return success(service.getTopList(parameter, getUser().getSelChannelId()));

    }

    //加入置顶区
    @RequestMapping(CmsUrlConstants.ProductTop.AddTopProduct)
    public AjaxResponse addTopProduct(@RequestBody AddTopProductParameter param) {
        UserSessionBean userSessionBean = getUser();
        service.addTopProduct(param, userSessionBean.getSelChannelId(), userSessionBean.getUserName());
        return success(null);
    }

    //保存置顶区
    @RequestMapping(CmsUrlConstants.ProductTop.SaveTopProduct)
    public AjaxResponse saveTopProduct(@RequestBody SaveTopProductParameter param) {
        UserSessionBean userSessionBean = getUser();
        service.saveTopProduct(param, userSessionBean.getSelChannelId(), userSessionBean.getUserName());

        return success(null);
    }

    //排序 保存
    @RequestMapping(CmsUrlConstants.ProductTop.SaveSortColumnName)
    public AjaxResponse saveSortColumnName(@RequestBody ProductPageParameter param) {
        service.saveSortColumnName(param,getUser().getSelChannelId(), getUser().getUserName());
        return success(null);
    }
}
