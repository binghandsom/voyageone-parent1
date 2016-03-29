package com.voyageone.common.components.jumei.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2016/3/29.
 */
public class HtSpuAddRequest implements JMRequest {
    public String Url = "/v1/htSpu/add";
    public String getUrl() {
        return Url;
    }
String    jumei_product_id;//	String; 聚美产品ID
String     upc_code;// 可选	String        商品自带条码
String     propery;//	String 规格 :FORMAL 正装 MS 中小样 OTHER 其他
String    size;//	String     容量/尺寸 /
String     attribute;// 可选	String 型号/颜色
Float     abroad_price;//float  海外价格
int   area_code;//	;Number 货币符号Id
String     abroad_url;// 可选	String   海外地址(可不传)
    //            白底方图
//
//    参数范围: 注：可不传,最多10张，1000*1000格式jpg,jpeg,单张不超过1m，多张图片以","隔开
//
String     normalImage;// 可选	String
    //            竖图
//
//    参数范围: 注：可不传,最多10张，750*1000jpg,jpeg,单张不超过1m，多张图片以","隔开
String     verticalImage;// 可选	String

    public String getJumei_product_id() {
        return jumei_product_id;
    }
    public void setJumei_product_id(String jumei_product_id) {
        this.jumei_product_id = jumei_product_id;
    }
    public String getUpc_code() {
        return upc_code;
    }
    public void setUpc_code(String upc_code) {
        this.upc_code = upc_code;
    }
    public String getPropery() {
        return propery;
    }
    public void setPropery(String propery) {
        this.propery = propery;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getAttribute() {
        return attribute;
    }
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
    public Float getAbroad_price() {
        return abroad_price;
    }
    public void setAbroad_price(Float abroad_price) {
        this.abroad_price = abroad_price;
    }
    public int getArea_code() {
        return area_code;
    }
    public void setArea_code(int area_code) {
        this.area_code = area_code;
    }
    public String getAbroad_url() {
        return abroad_url;
    }
    public void setAbroad_url(String abroad_url) {
        this.abroad_url = abroad_url;
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
    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_product_id", this.getJumei_product_id());
        params.put("upc_code", this.getUpc_code());
        params.put("propery", this.getPropery());
        params.put("size", this.getSize());
        params.put("attribute", this.getAttribute());
        params.put("abroad_price", this.getAbroad_price());
        params.put("area_code",Integer.toString(this.getArea_code()));
        params.put("abroad_url", this.getAbroad_url());
        params.put("normalImage", this.getNormalImage());
        params.put("verticalImage", this.getVerticalImage());
        return params;
    }
}
