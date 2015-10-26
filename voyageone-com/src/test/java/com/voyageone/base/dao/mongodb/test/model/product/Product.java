package com.voyageone.base.dao.mongodb.test.model.product;


import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Document
public class Product extends BaseMongoModel {

    private String channel_id, cat_id, product_id;
    private List<Map<String, Object>> field = new ArrayList<>();
    //private Map<String, String> field = new HashMap<String, String>();

    public Product(String channel_id, String cat_id, String product_id) {
        this.channel_id = channel_id;
        this.cat_id = cat_id;
        this.product_id = product_id;
    }

    public void setAttribute(String name, Object value) {
        setAttribute(name, value, true);
    }

    public void setAttribute(String name, Object value, boolean isCheck) {

        Assert.hasText(name);

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
                field.remove(value);
            }
        } else {
            if (attributeCurrent == null) {
                attributeCurrent = new HashMap<>();
                field.add(attributeCurrent);
            }
            attributeCurrent.put(name, value);
        }
    }

    public Map<String, Object> getField() {
        Map<String, Object> result = new HashMap<>();
        for(Map<String, Object> attribute : field) {
            if (attribute != null) {
                result.putAll(attribute);
            }
        }
        return result;
    }
}