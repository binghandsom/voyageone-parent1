package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.common.configs.Codes;
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
        //取得类目达标下面的个数
        String cnt = Codes.getCodeName("MAX_SELLER_CAT_CNT", cartId);
        data.put("cnt",cnt);
        if(codeList.size()==1){
            //选择一条记录．根据code在cms_bt_product取得对应的属性记录
            for(int i=0;i<codeList.size();i++){
                //取得商品code
                String code = codeList.get(i).toString();
                //根据商品code取得对应的类目达标属性
                CmsBtProductModel cmsBtProductModel = productService.getProductByCode(channelId, code);
                //取得叶子类目的catId
                List isSelectCidList = (List) cmsBtProductModel.getSellerCats().get("cIds");
                Map isSelectCidMap = new HashMap<>();
                for(int j=0;j<isSelectCidList.size();j++){
                    //取得选择的cid
                    isSelectCidMap.put(isSelectCidList.get(j).toString(),true);
                }
                data.put("isSelectCid",isSelectCidMap);
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

    /**
     * 保存数据到cms_bt_product
     * @param params
     */
    public void saveChannelCategory(Map<String, Object> params){
        //
        List<String> cIdsList = (List) params.get("cIds");
        List<String> cNamesList = (List) params.get("cNames");
        List<String> fullCNamesList = (List) params.get("fullCNames");
        List<String> fullCatCIdList = (List) params.get("fullCIds");
        List<String> codeList = (List) params.get("code");
        //check

        //公共处理

        //区分
        productService.updateSellerCat(cIdsList,cNamesList,fullCNamesList,fullCatCIdList,codeList);

    }
}
