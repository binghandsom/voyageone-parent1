package com.voyageone.components.cnn.response.data;

import java.util.List;

/**
 * Created by morse on 2017/7/31.
 */
public class CategoryGetResDataBean implements DataBean {

    private String name;
    private List<SubCategory> subList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubCategory> getSubList() {
        return subList;
    }

    public void setSubList(List<SubCategory> subList) {
        this.subList = subList;
    }

    public class SubCategory {
        private String subId; // 子分类节点ID
        private String subName; // 子分类名称

        public String getSubId() {
            return subId;
        }

        public void setSubId(String subId) {
            this.subId = subId;
        }

        public String getSubName() {
            return subName;
        }

        public void setSubName(String subName) {
            this.subName = subName;
        }
    }

}
