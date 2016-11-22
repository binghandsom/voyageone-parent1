package com.voyageone.service.bean.cms.shelves;

import java.util.List;
import java.util.Map;

/**
 * Created by rex.wu on 2016/11/11.
 */
public class CmsBtShelvesTemplateBean {

    private Integer id;
    private Integer templateType;
    private Integer clientType;
    private Integer cartId;
    private String channelId;
    private String templateName;

    private List<Integer> templateTypes;
    private List<Integer> clientTypes;
    private List<Integer> carts;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public List<Integer> getTemplateTypes() {
        return templateTypes;
    }

    public void setTemplateTypes(List<Integer> templateTypes) {
        this.templateTypes = templateTypes;
    }

    public List<Integer> getClientTypes() {
        return clientTypes;
    }

    public void setClientTypes(List<Integer> clientTypes) {
        this.clientTypes = clientTypes;
    }

    public List<Integer> getCarts() {
        return carts;
    }

    public void setCarts(List<Integer> carts) {
        this.carts = carts;
    }
}
