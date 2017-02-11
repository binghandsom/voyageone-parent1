package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.List;

/**
 * Created by james on 2017/1/24.
 */
public class CmsMtGiltSizeChartModel extends BaseMongoModel {
    Long id;
    String name;
    List<entrie> entries;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<entrie> getEntries() {
        return entries;
    }

    public void setEntries(List<entrie> entries) {
        this.entries = entries;
    }

    public static class entrie{
        String size_type;
        List<String> values;

        public String getSize_type() {
            return size_type;
        }

        public void setSize_type(String size_type) {
            this.size_type = size_type;
        }

        public List<String> getValues() {
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }
    }
}
