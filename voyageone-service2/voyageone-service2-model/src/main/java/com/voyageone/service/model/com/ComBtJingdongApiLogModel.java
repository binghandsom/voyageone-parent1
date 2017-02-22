package com.voyageone.service.model.com;

import com.voyageone.base.dao.mysql.BaseModel;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.CommonUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * com_bt_jiingdong_api_log
 * Created by jonasvlag on 16/3/14.
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class ComBtJingdongApiLogModel extends BaseModel {

    private int seq;

    private String apiMethodName;

    private String channel_id;

    private String cart_id;

    private Date call_time;

    private String time_zone;

    private String username;

    private String computer_name;

    private String ip;

    private String mac;

    public ComBtJingdongApiLogModel() {
        setCreater("System");
        setModifier("System");
    }

    public ComBtJingdongApiLogModel(String apiMethodName, ShopBean shopBean) {

        this();
        this.apiMethodName = apiMethodName;
        this.channel_id = shopBean.getOrder_channel_id();
        this.cart_id = shopBean.getCart_id();

        Calendar calendar = Calendar.getInstance();

//        this.call_time = DateTimeUtil.getDateTime(calendar.getTime(), null);
        this.call_time = new Date();
        this.time_zone = calendar.getTimeZone().getDisplayName();

        Map<String, String> local = CommonUtil.getLocalInfo();

        this.username = local.get(CommonUtil.USERNAME);
        this.computer_name = local.get(CommonUtil.COMPUTERNAME);
        this.ip = local.get(CommonUtil.IP);
        this.mac = local.get(CommonUtil.MAC);

        setCreated(this.call_time);
        setModified(this.call_time);
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getApiMethodName() {
        return apiMethodName;
    }

    public void setApiMethodName(String apiMethodName) {
        this.apiMethodName = apiMethodName;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public Date getCall_time() {
        return call_time;
    }

    public void setCall_time(Date call_time) {
        this.call_time = call_time;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComputer_name() {
        return computer_name;
    }

    public void setComputer_name(String computer_name) {
        this.computer_name = computer_name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
