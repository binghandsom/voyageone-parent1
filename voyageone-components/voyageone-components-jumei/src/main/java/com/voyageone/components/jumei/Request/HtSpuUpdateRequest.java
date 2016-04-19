package com.voyageone.components.jumei.Request;

import com.voyageone.common.util.JacksonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2016/3/29.
 */
public class HtSpuUpdateRequest implements JMRequest {
    public String Url = "/v1/htSpu/add";
    public String getUrl() {
        return Url;
    }
    String  jumei_spu_id;	//Number  聚美spu ID.

    //update_data	Json  修改数据.参数范围: 只传需要修改的字段

   String  upc_code;// 可选	Number 商品自带条码
   String  propery; // 可选	Number   规格 :FORMAL 正装 MS 中小样 OTHER 其他
   String  size;// 可选	String 容量/尺码
   String  attribute; //可选	String  型号/颜色

    double  abroad_price;// 可选	Number   海外官网价

   int  area_code;// 可选	Number  货币符号Id

   String  abroad_url;// 可选	Number  海外地址
    //选	String  白底方图(全量修改).  参数范围: 注：可不传,最多10张，1000*1000格式jpg,jpeg,单张不超过1m，多张图片以","隔开
    //  verticalImage 可选	String     竖图 (全量修改) // 参数范围: 注：可不传,最多10张，750*1000jpg,jpeg,单张不超过1m，多张图片以","隔开
   String  normalImage;

    public void setUrl(String url) {
        Url = url;
    }

    public String getJumei_spu_id() {
        return jumei_spu_id;
    }

    public void setJumei_spu_id(String jumei_spu_id) {
        this.jumei_spu_id = jumei_spu_id;
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


    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_spu_id", this.getJumei_spu_id());

        Map<String, Object> update_data = new HashMap<>();
        update_data.put("upc_code", this.getUpc_code());
        update_data.put("propery", this.getPropery());
        update_data.put("size", this.getSize());
        update_data.put("attribute", this.getAttribute());
        update_data.put("abroad_price", this.getAbroad_price());
        update_data.put("area_code", Integer.toString(this.getArea_code()));
        update_data.put("abroad_url", this.getAbroad_url());
        update_data.put("normalImage", this.getNormalImage());
        params.put("update_data", JacksonUtil.bean2JsonNotNull(update_data));
        return params;
    }
}
