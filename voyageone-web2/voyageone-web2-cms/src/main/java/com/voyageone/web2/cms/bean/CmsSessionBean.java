package com.voyageone.web2.cms.bean;

import com.voyageone.cms.enums.CartType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 15/12/3
 */
public class CmsSessionBean implements Serializable {

    // categoryType
    private Map<String, Object> platformType;
    // 其他附加信息
    private Map<String, Object> extraInfo;

    public Map<String, Object> getPlatformType() {
        if (platformType != null) {
            return platformType;
        } else {
            Map<String, Object> newCategoryType = new HashMap<String, Object>();
            newCategoryType.put("cTypeId", CartType.MASTER.getShortName());
            newCategoryType.put("cartId", CartType.MASTER.getCartId());
            return newCategoryType;
        }
    }

    public void setPlatformType(Map<String, Object> platformType) {
        this.platformType = platformType;
    }

    public Object getAttribute(String key) {
        if (extraInfo == null) {
            return null;
        } else {
            return extraInfo.get(key);
        }
    }

    public void putAttribute(String key, Object value) {
        if (extraInfo == null) {
            extraInfo = new HashMap<>();
        }
        extraInfo.put(key, value);
    }

}
