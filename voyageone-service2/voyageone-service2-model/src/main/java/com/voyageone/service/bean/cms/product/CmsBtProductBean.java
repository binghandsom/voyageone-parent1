package com.voyageone.service.bean.cms.product;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Carts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link CmsBtProductModel} 商品Model的扩展，其中的扩展项目必须手工设值
 *
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductBean extends CmsBtProductModel {

    private CmsBtProductGroupModel groupBean = new CmsBtProductGroupModel();
    private CmsBtProductModel_Carts cartBean = new CmsBtProductModel_Carts();
    private List<Map<String, Object>> cartStsList = new ArrayList<>();

    public CmsBtProductBean() {
    }

    public CmsBtProductBean(String channelId) {
        super(channelId);
    }

    public CmsBtProductGroupModel getGroupBean() {
        return groupBean;
    }

    public void setGroupBean(CmsBtProductGroupModel groups) {
        this.groupBean = groups;
    }

    public CmsBtProductModel_Carts getCartBean() {
        return cartBean;
    }

    public void setCartBean(CmsBtProductModel_Carts productCart) {
        this.cartBean = productCart;
    }

    public List<Map<String, Object>> getCartStsList() {
        return cartStsList;
    }

    public void setCartStsList(List<Map<String, Object>> cartStsList) {
        this.cartStsList = cartStsList;
    }

}