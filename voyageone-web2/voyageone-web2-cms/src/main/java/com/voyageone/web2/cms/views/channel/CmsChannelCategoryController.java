package com.voyageone.web2.cms.views.channel;

import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.impl.cms.SellerCatService;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//        String channelId = this.getUser().getSelChannelId();
        String channelId = "010";
        Integer cartId = (Integer) param.get("cartId") ;
        List<CmsBtSellerCatModel> list = sellerCatService.getSellerCatsByChannelCart(channelId, cartId);
        Map<String, Object> result = new HashMap<>();
        result.put("catTree",list);
        return success(result);
    }

}
