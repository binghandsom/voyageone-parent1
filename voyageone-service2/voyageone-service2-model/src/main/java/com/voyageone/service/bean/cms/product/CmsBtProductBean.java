package com.voyageone.service.bean.cms.product;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Carts;

/**
 * {@link CmsBtProductModel} 商品Model的扩展，其中的扩展项目必须手工设值
 *
 * @author jiangjusheng, 2016/04/22
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductBean extends CmsBtProductModel {

    private CmsBtProductGroupModel groupBean = new CmsBtProductGroupModel();
    private CmsBtProductModel_Carts cartBean = new CmsBtProductModel_Carts();

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
}