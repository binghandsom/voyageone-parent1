package com.voyageone.base.dao.mongodb.test.model.product;


import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Document
public class Product extends ChannelPartitionModel {

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



    private List<Map<String, Object>> field;

    public void setField(List<Map<String, Object>> field) {
        this.field = field;
    }
    public List<Map<String, Object>> getField() {
        return field;
    }



//private Map<String, String> field = new HashMap<String, String>();

    public Product() {
        super("");
    }

    public Product(String channelId, int catId, int productId) {
        super(channelId);
        this.cat_id = catId;
        this.product_id = productId;
    }

    public void setAttribute(String name, Object value) {
        setAttribute(name, value, true);
    }

    public void setAttribute(String name, Object value, boolean isCheck) {

        Assert.hasText(name);
        if (field == null) {
            field = new ArrayList<>();
        }

        Map<String, Object> attributeCurrent = null;
        if (isCheck) {
            for (Map<String, Object> attribute : field) {
                if (attribute.containsKey(name)) {
                    attributeCurrent = attribute;
                    break;
                }
            }
        }
        if (value == null) {
            if (attributeCurrent != null) {
                field.remove(attributeCurrent);
            }
        } else {
            if (attributeCurrent == null) {
                attributeCurrent = new HashMap<>();
                field.add(attributeCurrent);
            }
            attributeCurrent.put(name, value);
        }
    }

    public Map<String, Object> getFields() {
        Map<String, Object> result = new HashMap<>();
        for(Map<String, Object> attribute : field) {
            if (attribute != null) {
                result.putAll(attribute);
            }
        }
        return result;
    }

    public String getCollectionName() {
        return getCollectionName(this.channelId);
    }

    public static String collectionName = "product";
    public static String getCollectionName(String channelId) {
        return collectionName;// + getPartitionValue(channelId);
    }

}