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
        @JsonSubTypes.Type(value = CustomWordValueGetDescImage.class, name = CustomWordValueGetDescImage.moduleName),
        @JsonSubTypes.Type(value = CustomWordValueImageWithParam.class, name = CustomWordValueImageWithParam.moduleName),
        @JsonSubTypes.Type(value = CustomWordValueGetProductFieldInfo.class, name = CustomWordValueGetProductFieldInfo.moduleName),
        @JsonSubTypes.Type(value = CustomWordValueImageFormat.class, name = CustomWordValueImageFormat.moduleName),
        @JsonSubTypes.Type(value = CustomWordValueGetPaddingImageKey.class, name = CustomWordValueGetPaddingImageKey.moduleName),
        @JsonSubTypes.Type(value = CustomWordValueIf.class, name = CustomWordValueIf.moduleName),
        @JsonSubTypes.Type(value = CustomWordValueConditionAnd.class, name = CustomWordValueConditionAnd.moduleName),
        @JsonSubTypes.Type(value = CustomWordValueConditionEq.class, name = CustomWordValueConditionEq.moduleName),
        @JsonSubTypes.Type(value = CustomWordValueConditionNeq.class, name = CustomWordValueConditionNeq.moduleName),
        @JsonSubTypes.Type(value = CustomWordValueConditionLike.class, name = CustomWordValueConditionLike.moduleName),
        @JsonSubTypes.Type(value = CustomWordValueConditionNLike.class, name = CustomWordValueConditionNLike.moduleName),
        @JsonSubTypes.Type(value = CustomWordValueGetCommonImages.class, name = CustomWordValueGetCommonImages.moduleName),
        @JsonSubTypes.Type(value = CustomWordValueTranslateBaidu.class, name = CustomWordValueTranslateBaidu.moduleName),
        @JsonSubTypes.Type(value = CustomWordValueTableWithParam.class, name = CustomWordValueTableWithParam.moduleName)
})
public abstract class CustomWordValue {
        public CustomWordValue() {}

        @JsonIgnore
        abstract public String getModuleName();
}
