package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.service.impl.cms.SellerCatService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.views.home.menu.CmsMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/5/23.
 */
@Service
public class CmsAddChannelCategoryService extends BaseAppService {
    /**
     *
     * @param params
     * @return
     */
    @Autowired
    private ProductService productService;

    @Autowired
    private SellerCatService sellerCatService;

    @Autowired
    private CmsMenuService menuService;

    /**
     * 数据页面初始化
     * @param params
     * @param lang
     * @return data
     */
    public Map getChannelCategory(Map<String, Object> params,String lang) {
        Map data = new HashMap<>();
        //产品code
        List<String> codeList = (List) params.get("code");
        //channelId
        String channelId = (String) params.get("channelId");
        //cartId
        String cartId= (String) params.get("cartId");
        CmsBtProductModel cmsBtProductModel = new CmsBtProductModel();
        if(codeList.size()==1){
            //选择一条记录．根据code在cms_bt_product取得对应的属性记录
            for(int i=0;i<codeList.size();i++){
                //取得商品code
                String code = codeList.get(i).toString();
                //根据商品code取得对应的类目达标属性
                cmsBtProductModel = productService.getProductByCode(channelId, code);
                //取得叶子类目的catId

                data.put("isSelectCid",cmsBtProductModel.getSellerCats().get("cIds"));
            }
        }
        //取得店铺渠道
        List<TypeChannelBean> cartList= menuService.getPlatformTypeList(channelId, lang);
        data.put("cartList",cartList);
        //根据channelId在cms_bt_seller_cat取得对应的达标数据
        List<CmsBtSellerCatModel> channelCategoryList = sellerCatService.getSellerCatsByChannelCart(channelId, Integer.parseInt(cartId));
        data.put("channelCategoryList",channelCategoryList);

        return data;
    }
}
