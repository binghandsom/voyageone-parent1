package com.voyageone.task2.cms.bean;

/*
 * FeedDealObject
 * Created by zero on 7/28/2015.
 *
 * @author zero
 */
public class FeedDealObject {

    private String channelId;

    private String url_key;

    private String parent_url_key;

    private String category_url_key;

    private String model_url_key;

    private String model;

    private String code;

    private String sku;

    private String client_sku;

    /**
     * @return the channelId
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * @param channelId the channelId to set
     */
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    /**
     * @return the url_key
     */
    public String getUrl_key() {
        return url_key;
    }

    /**
     * @param url_key the url_key to set
     */
    public void setUrl_key(String url_key) {
        this.url_key = url_key;
    }

    /**
     * @return the parent_url_key
     */
    public String getParent_url_key() {
        return parent_url_key;
    }

    /**
     * @param parent_url_key the parent_url_key to set
     */
    public void setParent_url_key(String parent_url_key) {
        this.parent_url_key = parent_url_key;
    }

    /**
     * @return the category_url_key
     */
    public String getCategory_url_key() {
        return category_url_key;
    }

    /**
     * @param category_url_key the category_url_key to set
     */
    public void setCategory_url_key(String category_url_key) {
        this.category_url_key = category_url_key;
    }

    /**
     * @return the model_url_key
     */
    public String getModel_url_key() {
        return model_url_key;
    }

    /**
     * @param model_url_key the model_url_key to set
     */
    public void setModel_url_key(String model_url_key) {
        this.model_url_key = model_url_key;
    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * @param sku the sku to set
     */
    public void setSku(String sku) {
        this.sku = sku;
    }
    public String getClient_sku() {
        return client_sku;
    }

    public void setClient_sku(String client_sku) {
        this.client_sku = client_sku;
    }

}