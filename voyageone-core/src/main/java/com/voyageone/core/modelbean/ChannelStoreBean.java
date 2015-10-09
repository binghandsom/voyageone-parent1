package com.voyageone.core.modelbean;

import java.io.Serializable;

/**
 * Created by Jonas on 4/13/2015.
 */
public class ChannelStoreBean implements Serializable {
    private int store_id;
    private String store_name;

    private String order_channel_id;
    private String store_type;
    private String store_location;
    private String store_kind;
    private String parent_store_id;
    private String label_type;
    private String rsv_sort;
    
    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

	/**
	 * @return the order_channel_id
	 */
	public String getOrder_channel_id() {
		return order_channel_id;
	}

	/**
	 * @param order_channel_id the order_channel_id to set
	 */
	public void setOrder_channel_id(String order_channel_id) {
		this.order_channel_id = order_channel_id;
	}

	/**
	 * @return the store_type
	 */
	public String getStore_type() {
		return store_type;
	}

	/**
	 * @param store_type the store_type to set
	 */
	public void setStore_type(String store_type) {
		this.store_type = store_type;
	}

	/**
	 * @return the store_location
	 */
	public String getStore_location() {
		return store_location;
	}

	/**
	 * @param store_location the store_location to set
	 */
	public void setStore_location(String store_location) {
		this.store_location = store_location;
	}

	/**
	 * @return the store_kind
	 */
	public String getStore_kind() {
		return store_kind;
	}

	/**
	 * @param store_kind the store_kind to set
	 */
	public void setStore_kind(String store_kind) {
		this.store_kind = store_kind;
	}

	/**
	 * @return the parent_store_id
	 */
	public String getParent_store_id() {
		return parent_store_id;
	}

	/**
	 * @param parent_store_id the parent_store_id to set
	 */
	public void setParent_store_id(String parent_store_id) {
		this.parent_store_id = parent_store_id;
	}

	/**
	 * @return the label_type
	 */
	public String getLabel_type() {
		return label_type;
	}

	/**
	 * @param label_type the label_type to set
	 */
	public void setLabel_type(String label_type) {
		this.label_type = label_type;
	}

	/**
	 * @return the rsv_sort
	 */
	public String getRsv_sort() {
		return rsv_sort;
	}

	/**
	 * @param rsv_sort the rsv_sort to set
	 */
	public void setRsv_sort(String rsv_sort) {
		this.rsv_sort = rsv_sort;
	}
    
    
}
