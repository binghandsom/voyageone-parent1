package com.voyageone.ims.rule_expression;

/**
 * Created by Leo on 15-6-26.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME
        , include = JsonTypeInfo.As.PROPERTY
        , property = "moduleName")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CustomWordValueGetAllImages.class, name = CustomWordValueGetAllImages.moduleName),
        @JsonSubTypes.Type(value = CustomWordValueGetMainProductImages.class, name = CustomWordValueGetMainProductImages.moduleName),
        @JsonSubTypes.Type(value = CustomWordValueImageWithParam.class, name = CustomWordValueImageWithParam.moduleName),
        @JsonSubTypes.Type(value = CustomWordValueImageFormat.class, name = CustomWordValueImageFormat.moduleName),
        @JsonSubTypes.Type(value = CustomWordValueGetPaddingImageKey.class, name = CustomWordValueGetPaddingImageKey.moduleName),
})
public abstract class CustomWordValue {
        public CustomWordValue() {}

        @JsonIgnore
        abstract public String getModuleName();
}
