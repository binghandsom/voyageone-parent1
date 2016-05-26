package com.voyageone.web2.cms.views.channel;

import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.impl.cms.SellerCatService;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.SellerCatRequestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Ethan Shi on 2016/5/25.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.CHANNEL.SELLER_CAT.ROOT, method = RequestMethod.POST)
public class CmsChannelCategoryController  extends CmsController {

    @Autowired
    private SellerCatService sellerCatService;

    @RequestMapping(value = CmsUrlConstants.CHANNEL.SELLER_CAT.GET_SELLER_CAT)
    public AjaxResponse getSellerCat(@RequestBody Map param) {
        String channelId = this.getUser().getSelChannelId();
        Integer cartId = Integer.valueOf(param.get("cartId").toString()) ;
        List<CmsBtSellerCatModel> list = sellerCatService.getSellerCatsByChannelCart(channelId, cartId);
        Map<String, Object> result = new HashMap<>();
        result.put("catTree",list);
        return success(result);
    }

    @RequestMapping(value = CmsUrlConstants.CHANNEL.SELLER_CAT.ADD_SELLER_CAT)
    public AjaxResponse addSellerCat(@RequestBody Map param) {
        String channelId = this.getUser().getSelChannelId();
        Integer cartId =  Integer.valueOf( param.get("cartId").toString()) ;
        String cName = String.valueOf(param.get("catName")) ;
        String parentCId =  String.valueOf(param.get("parentCatId")) ;


        //创建者/更新者
        String creator =this.getUser().getUserName();

        sellerCatService.addSellerCat(channelId,cartId,cName,parentCId,creator);

        List<CmsBtSellerCatModel> list = sellerCatService.getSellerCatsByChannelCart(channelId, cartId);
        Map<String, Object> result = new HashMap<>();
        result.put("catTree",list);

        //返回数据的类型
        return success(result);
    }

    @RequestMapping(value = CmsUrlConstants.CHANNEL.SELLER_CAT.UPDATE_SELLER_CAT)
    public AjaxResponse updateSellerCat(@RequestBody Map param) {
        String channelId = this.getUser().getSelChannelId();
        Integer cartId =  Integer.valueOf( param.get("cartId").toString()) ;
        String cName = String.valueOf(param.get("catName")) ;
        String cId = String.valueOf(param.get("catId")) ;
        //创建者/更新者
        String modifier =this.getUser().getUserName();

        sellerCatService.updateSellerCat(channelId,cartId,cName,cId,modifier);

        List<CmsBtSellerCatModel> list = sellerCatService.getSellerCatsByChannelCart(channelId, cartId);
        Map<String, Object> result = new HashMap<>();
        result.put("catTree",list);

        //返回数据的类型
        return success(result);
    }


    @RequestMapping(value = CmsUrlConstants.CHANNEL.SELLER_CAT.REMOVE_SELLER_CAT)
    public AjaxResponse deleteSellerCat(@RequestBody Map param) {
        String channelId = this.getUser().getSelChannelId();
        Integer cartId = Integer.valueOf(param.get("cartId").toString()) ;
        String parentCId = String.valueOf(param.get("parentCatId")) ;
        String cId = String.valueOf(param.get("catId")) ;

        sellerCatService.deleteSellerCat(channelId,cartId,parentCId,cId);

        List<CmsBtSellerCatModel> list = sellerCatService.getSellerCatsByChannelCart(channelId, cartId);
        Map<String, Object> result = new HashMap<>();
        result.put("catTree",list);

        //返回数据的类型
        return success(result);
    }


    @RequestMapping(value = CmsUrlConstants.CHANNEL.SELLER_CAT.GET_SELLER_CAT_CONFIG)
    public AjaxResponse getSellerCatConfig(@RequestBody Map param) {

        Integer cartId = Integer.valueOf(param.get("cartId").toString()) ;
        return success(sellerCatService.getSellerCatConfig(cartId));
    }


}
