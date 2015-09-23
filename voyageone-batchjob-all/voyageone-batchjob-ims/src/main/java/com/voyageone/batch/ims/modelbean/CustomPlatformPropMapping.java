package com.voyageone.batch.ims.modelbean;

import com.voyageone.batch.ims.bean.CustomMappingType;

/**
 * Created by Leo on 15-7-10.
 */
public class CustomPlatformPropMapping {
    private CustomMappingType customMappingType;
    private String platformPropId;
    private int cartId;

    public int getCustomMappingTypeEnum() {
        return customMappingType.value();
    }

    public void setCustomMappingTypeEnum(int customMappingTypeValue) {
        this.customMappingType = CustomMappingType.valueOf(customMappingTypeValue);
    }

    public CustomMappingType getCustomMappingType() {
        return customMappingType;
    }

    public void setCustomMappingType(CustomMappingType customMappingType) {
        this.customMappingType = customMappingType;
    }

    public String getPlatformPropId() {
        return platformPropId;
    }

    public void setPlatformPropId(String platformPropId) {
        this.platformPropId = platformPropId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
