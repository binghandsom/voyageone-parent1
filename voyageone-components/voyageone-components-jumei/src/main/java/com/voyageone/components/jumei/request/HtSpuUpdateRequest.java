package com.voyageone.components.jumei.request;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * HtSpuUpdateRequest
 *
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtSpuUpdateRequest implements BaseJMRequest {
    private String url = "/v1/htSpu/update";
    private String jumei_spu_no;    //Number  聚美Spu_No.
    //update_data	Json  修改数据.参数范围: 只传需要修改的字段
    private String upc_code;// 可选	Number 商品自带条码
    private String property; // 可选	Number   规格 :FORMAL 正装 MS 中小样 OTHER 其他
    private String size;// 可选	String 容量/尺码
    private String attribute; //可选	String  型号/颜色
    private double abroad_price;// 可选	Number   海外官网价
    private int area_code;// 可选	Number  货币符号Id
    private String abroad_url;// 可选	Number  海外地址
    // 白底方图(全量修改).  参数范围: 注：可不传,最多10张，1000*1000格式jpg,jpeg,单张不超过1m，多张图片以","隔开
    // verticalImage 可选	竖图 (全量修改) // 参数范围: 注：可不传,最多10张，750*1000jpg,jpeg,单张不超过1m，多张图片以","隔开
    private String normalImage;

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJumei_spu_no() {
        return jumei_spu_no;
    }

    public void setJumei_spu_no(String jumei_spu_no) {
        this.jumei_spu_no = jumei_spu_no;
    }

    public String getUpc_code() {
        return upc_code;
    }

    public void setUpc_code(String upc_code) {
        this.upc_code = upc_code;
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

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_spu_no", this.getJumei_spu_no());

        Map<String, Object> update_data = new HashMap<>();
        if (!StringUtils.isEmpty(getUpc_code()))    update_data.put("upc_code", this.getUpc_code());
        if (!StringUtils.isEmpty(property) )    update_data.put("property",property);
        if (!StringUtils.isEmpty(getSize()))        update_data.put("size", this.getSize());
        if (!StringUtils.isEmpty(getAttribute()))   update_data.put("attribute", this.getAttribute());
        if (getAbroad_price() != 0.0d)              update_data.put("abroad_price", this.getAbroad_price());
        if (getArea_code() != 0)                    update_data.put("area_code", Integer.toString(this.getArea_code()));
        if (!StringUtils.isEmpty(getAbroad_url()))  update_data.put("abroad_url", this.getAbroad_url());
        if (!StringUtils.isEmpty(getNormalImage())) update_data.put("normalImage", this.getNormalImage());
        params.put("update_data", JacksonUtil.bean2JsonNotNull(update_data));

        return params;
    }
}
