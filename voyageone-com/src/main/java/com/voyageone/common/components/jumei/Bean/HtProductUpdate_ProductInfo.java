package com.voyageone.common.components.jumei.Bean;

/**
 * Created by dell on 2016/3/29.
 */
public class HtProductUpdate_ProductInfo {
    int category_v3_4_id;//	Number  分类id   参数范围: V3版四级分类
    int brand_id;//	Number  品牌id
    //    参数范围: 注：100字以内，不能出现容量、规格、颜色等信息，这些信息在子型号中设置产品名上不能填写除了“（）”“/”“+”“*” 以外的特殊符号，
//    如-，<>，· ，空格等符号必须是英文半角符号，套装产品名以“+”号连接
    String name;//	String  产品名

    //参数范围: 注：请输入产品外文名称，支持中文繁体、英文、法文、德文、韩文、日文，100个字符以内
    String foreign_language_name;//	String  外文名

    String function_ids;//可选	String  产品功效ID，多个ID用 ","隔开

    //    参数范围: 注：第一张必填,最多10张，1000*1000格式jpg,jpeg,单张不超过1m，多张图片以","隔开；图片上除了产品外不得出现任何其他信息
//    （如水印、商标、优惠信息等)
    String normalImage;//	String    白底方图
    //            竖图
//
//    参数范围: 注：可不传,最多10张，750*1000格式jpg,jpeg,单张不超过1m，多张图片以","隔开
    String verticalImage;// 可选	String

    public int getCategory_v3_4_id() {
        return category_v3_4_id;
    }

    public void setCategory_v3_4_id(int category_v3_4_id) {
        this.category_v3_4_id = category_v3_4_id;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getForeign_language_name() {
        return foreign_language_name;
    }

    public void setForeign_language_name(String foreign_language_name) {
        this.foreign_language_name = foreign_language_name;
    }

    public String getFunction_ids() {
        return function_ids;
    }

    public void setFunction_ids(String function_ids) {
        this.function_ids = function_ids;
    }

    public String getNormalImage() {
        return normalImage;
    }

    public void setNormalImage(String normalImage) {
        this.normalImage = normalImage;
    }

    public String getVerticalImage() {
        return verticalImage;
    }

    public void setVerticalImage(String verticalImage) {
        this.verticalImage = verticalImage;
    }
}
