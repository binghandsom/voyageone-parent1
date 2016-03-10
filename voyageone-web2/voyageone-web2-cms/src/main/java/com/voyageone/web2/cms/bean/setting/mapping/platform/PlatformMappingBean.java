package com.voyageone.web2.cms.bean.setting.mapping.platform;

import com.voyageone.service.bean.cms.MappingBean;

import java.util.List;

/**
 * @author Jonas, 1/26/16.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PlatformMappingBean {

    private String mainCategoryId;

    private String platformCategoryId;

    private Integer cartId;

    private MappingBean mappingBean;

    private List<String> mappingPath;

    private Integer matchOver;

    public String getMainCategoryId() {
        return mainCategoryId;
    }

    public void setMainCategoryId(String mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public String getPlatformCategoryId() {
        return platformCategoryId;
    }

    public void setPlatformCategoryId(String platformCategoryId) {
        this.platformCategoryId = platformCategoryId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public MappingBean getMappingBean() {
        return mappingBean;
    }

    public void setMappingBean(MappingBean mappingBean) {
        this.mappingBean = mappingBean;
    }

    public List<String> getMappingPath() {
        return mappingPath;
    }

    public void setMappingPath(List<String> mappingPath) {
        this.mappingPath = mappingPath;
    }

    public Integer getMatchOver() {
        return matchOver;
    }

    public void setMatchOver(Integer matchOver) {
        this.matchOver = matchOver;
    }
}
