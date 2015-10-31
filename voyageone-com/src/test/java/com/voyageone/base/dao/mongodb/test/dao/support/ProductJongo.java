package com.voyageone.base.dao.mongodb.test.dao.support;

import com.voyageone.common.util.JsonUtil;
//import org.jongo.marshall.jackson.oid.MongoObjectId;

/**
 * Created by DELL on 2015/10/29.
 */
public class ProductJongo {

//    @MongoObjectId
    private String _id;
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }

    protected String channel_id = null;
    public String getChannel_id() {
        return channel_id;
    }
    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    private int cat_id;
    public int getCat_id() {
        return cat_id;
    }
    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    private int product_id;
    public int getProduct_id() {
        return product_id;
    }
    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public ProductJongo() {
    }

    public ProductJongo(String channel_id, int cat_id, int product_id) {
        this.channel_id = channel_id;
        this.cat_id = cat_id;
        this.product_id = product_id;
    }

    public String getCollectionName() {
        return "product_j001";
    }

    /**
     *(non-Javadoc)
     * @return JsonUtil.getJsonString
     */
    @Override
    public String toString() {
        return JsonUtil.getJsonString(this);
    }
}
