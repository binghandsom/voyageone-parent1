package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjl on 2016/5/24.
 */
@Deprecated
public class CmsBtProductModel_SellerCats  extends BaseMongoMap<String, Object> {
    public int getCartId() {
        return getAttribute("cartId");
    }

    public void setCartId(int cartId) {
        setAttribute("cartId", cartId);
    }

    public List<String> getCid() {
        if (!this.containsKey("cIds") || getAttribute("cIds") == null) {
            setAttribute("cIds", new ArrayList<String>());
        }
        return (List<String>) getAttribute("cIds");
    }

    public void setCid(List<String> cIds) {
        setAttribute("cIds", cIds);
    }

    public List<String> getCNames() {
        if (!this.containsKey("cNames") || getAttribute("cNames") == null) {
            setAttribute("cNames", new ArrayList<String>());
        }
        return (List<String>) getAttribute("cNames");
    }

    public void setCNames(List<String> cNames) {
        setAttribute("cNames", cNames);
    }

    public List<String> getFullCNames() {
        if (!this.containsKey("fullCNames") || getAttribute("fullCNames") == null) {
            setAttribute("fullCNames", new ArrayList<String>());
        }
        return (List<String>) getAttribute("fullCNames");
    }

    public void setFullCNames(List<String> fullCNames) {
        setAttribute("fullCNames", fullCNames);
    }

    public List<String> getFullCIds() {
        if (!this.containsKey("fullCIds") || getAttribute("fullCIds") == null) {
            setAttribute("fullCIds", new ArrayList<String>());
        }
        return (List<String>) getAttribute("fullCIds");
    }

    public void setFullCIds(List<String> fullCIds) {
        setAttribute("fullCIds", fullCIds);
    }
}
