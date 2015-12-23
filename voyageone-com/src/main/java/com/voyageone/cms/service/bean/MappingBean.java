package com.voyageone.cms.service.bean;

/**
 * Created by Leo on 15-12-9.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.voyageone.common.masterdate.schema.Util.StringUtil;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME
        , include = JsonTypeInfo.As.PROPERTY
        , property = "mappingType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SimpleMappingBean.class, name = MappingBean.MAPPING_SIMPLE),
        @JsonSubTypes.Type(value = ComplexMappingBean.class, name = MappingBean.MAPPING_COMPLEX)
})
public class MappingBean {
    public static final String MAPPING_SIMPLE = "0";
    public static final String MAPPING_COMPLEX = "1";

    protected String platformPropId;
    protected String mappingType;

    public String getPlatformPropId() {
        return platformPropId;
    }

    public void setPlatformPropId(String platformPropId) {
        this.platformPropId = platformPropId;
    }

    @JsonProperty("platformPropId")
    public String getPlatformIdWithConvert() {
        return StringUtil.replaceDot(platformPropId);
    }

    @JsonProperty("platformPropId")
    public void setPlatformIdWithConvert(String platformPropId) {
        this.platformPropId = StringUtil.replaceToDot(platformPropId);
    }

    public String getMappingType() {
        return mappingType;
    }
}
