package com.voyageone.batch.bi.bean.formbean;

/**
 * Created by Kylin on 2015/6/5.
 */
public class FormUser {

    private int channel_id;
    private String channel_code;

	private int ecomm_id;
    private int shop_id;
    private String shop_code;
    private String user_name;
    private String user_ps;
    private String db_name;
    private String table_title_name;
    private int enable;

    public int getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(int channel_id) {
        this.channel_id = channel_id;
    }

    public String getChannel_code() {
		return channel_code;
	}

	public void setChannel_code(String channel_code) {
		this.channel_code = channel_code;
	}
	
    public int getEcomm_id() {
        return ecomm_id;
    }

    public void setEcomm_id(int ecomm_id) {
        this.ecomm_id = ecomm_id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_ps() {
        return user_ps;
    }

    public void setUser_ps(String user_ps) {
        this.user_ps = user_ps;
    }

    public String getDb_name() {
        return db_name;
    }

    public void setDb_name(String db_name) {
        this.db_name = db_name;
    }

    public String getTable_title_name() {
        return table_title_name;
    }

    public void setTable_title_name(String table_title_name) {
        this.table_title_name = table_title_name;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getShop_code() {
        return shop_code;
    }

    public void setShop_code(String shop_code) {
        this.shop_code = shop_code;
    }
}

