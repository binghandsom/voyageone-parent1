package com.voyageone.components.sneakerhead.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * category的模型 含子model列表、商品列表
 * Created by vantis on 2016/11/23.
 * 闲舟江流夕照晚 =。=
 */
public class SneakerheadCategoryModel extends SneakerheadSimpleCategoryModel {
    private List<String> codeList = new ArrayList<>();
    private List<SneakerheadCategoryModel> subCategory = new ArrayList<>();

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }

    public List<SneakerheadCategoryModel> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(List<SneakerheadCategoryModel> subCategory) {
        this.subCategory = subCategory;
    }
}
