package com.voyageone.components.jumei.request;

import com.voyageone.common.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * HtSpuAddRequest
 *
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtSpuAddRequest implements BaseJMRequest {
    private String url = "/v1/htSpu/add";

    private String jumei_product_id;//	String; 聚美产品ID
    private String upc_code;// 可选	String        商品自带条码
    private String property;//	String 规格 :FORMAL 正装 MS 中小样 OTHER 其他
    private String size;//	String     容量/尺寸 /
    private String attribute;// 可选	String 型号/颜色
    private double abroad_price;//float  海外价格
    private String area_code;//	;Number 货币符号Id
    private String abroad_url;// 可选	String   海外地址(可不传)
    // 白底方图 参数范围: 注：可不传,最多10张，1000*1000格式jpg,jpeg,单张不超过1m，多张图片以","隔开
    private String normalImage;
    // 竖图 参数范围: 注：可不传,最多10张，750*1000jpg,jpeg,单张不超过1m，多张图片以","隔开
    private String verticalImage;// 可选	String

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

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

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
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

    public double getAbroad_price() {
        return abroad_price;
    }

    public void setAbroad_price(double abroad_price) {
        this.abroad_price = abroad_price;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
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

    @Override
    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(getJumei_product_id())) params.put("jumei_product_id", this.getJumei_product_id());
        if (!StringUtils.isEmpty(getUpc_code()))         params.put("upc_code", this.getUpc_code());
        if (!StringUtils.isEmpty(getProperty()))         params.put("property", this.getProperty());
        if (!StringUtils.isEmpty(getSize()))             params.put("size", this.getSize());
        if (!StringUtils.isEmpty(getAttribute()))        params.put("attribute", this.getAttribute());
        params.put("abroad_price", this.getAbroad_price());
        if (!StringUtils.isEmpty(getArea_code()))        params.put("area_code", this.getArea_code());
        if (!StringUtils.isEmpty(getAbroad_url()))       params.put("abroad_url", this.getAbroad_url());
        if (!StringUtils.isEmpty(getNormalImage()))      params.put("normalImage", this.getNormalImage());
        if (!StringUtils.isEmpty(getVerticalImage()))    params.put("verticalImage", this.getVerticalImage());
        return params;
    }
}
