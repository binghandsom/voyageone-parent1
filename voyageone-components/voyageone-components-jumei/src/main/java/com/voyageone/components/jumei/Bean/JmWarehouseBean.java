package com.voyageone.components.jumei.bean;

/**
 * 商家仓库结构。
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public class JmWarehouseBean extends JmBaseBean {

    //仓库ID.
    private Integer shipping_system_id;

    //仓库名.
    private String shipping_system_name;

    //发货地区.
    private String area_name;

    public Integer getShipping_system_id() {
        return shipping_system_id;
    }

    public void setShipping_system_id(Integer shipping_system_id) {
        this.shipping_system_id = shipping_system_id;
    }

    public String getShipping_system_name() {
        return shipping_system_name;
    }

    public void setShipping_system_name(String shipping_system_name) {
        this.shipping_system_name = shipping_system_name;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }
}
