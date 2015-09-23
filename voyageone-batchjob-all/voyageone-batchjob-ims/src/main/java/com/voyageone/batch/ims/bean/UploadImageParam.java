package com.voyageone.batch.ims.bean;

import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.beans.ShopBean;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Leo on 15-6-8.
 */
public class UploadImageParam {
    private Set<String> srcUrlSet;
    private ShopBean shopBean;

    @Override
    public String toString() {
        String beanString = "cart:" + CartEnums.Cart.getValueByID(shopBean.getCart_id())
                + ", shop name: " + shopBean.getShop_name()
                + ", src url: [";

        for (String srcUrl : srcUrlSet)
        {
            beanString += srcUrl + ",";
        }
        beanString += "]";
        return beanString;
    }

    public void addSrcUrl(String srcUrl)
    {
        srcUrlSet.add(srcUrl);
    }

    public Set<String> getSrcUrlSet() {
        return srcUrlSet;
    }

    public ShopBean getShopBean() {
        return shopBean;
    }

    public void setShopBean(ShopBean shopBean) {
        this.shopBean = shopBean;
    }

    public UploadImageParam() {
        this.srcUrlSet = new HashSet<>();
    }

    public void setSrcUrlSet(Set<String> srcUrlSet) {
        this.srcUrlSet = srcUrlSet;
    }
}
