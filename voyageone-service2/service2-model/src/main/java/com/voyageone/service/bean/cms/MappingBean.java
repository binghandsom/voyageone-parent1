package com.voyageone.service.bean.cms;

/**
 * Created by Leo on 15-12-9.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.voyageone.common.util.StringUtils;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME
        , include = JsonTypeInfo.As.PROPERTY
        , property = "mappingType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = com.voyageone.service.bean.cms.SimpleMappingBean.class, name = MappingBean.MAPPING_SIMPLE),
        @JsonSubTypes.Type(value = ComplexMappingBean.class, name = com.voyageone.service.bean.cms.MappingBean.MAPPING_COMPLEX),
        @JsonSubTypes.Type(value = MultiComplexCustomMappingBean.class, name = com.voyageone.service.bean.cms.MappingBean.MAPPING_MULTICOMPLEX_CUSTOM)
})
public class MappingBean {
    public static final String MAPPING_SIMPLE = "0";
    public static final String MAPPING_COMPLEX = "1";
    public static final String MAPPING_MULTICOMPLEX_CUSTOM = "2";

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
        return StringUtils.replaceDot(platformPropId);
    }

    @JsonProperty("platformPropId")
    public void setPlatformIdWithConvert(String platformPropId) {
        this.platformPropId = StringUtils.replaceToDot(platformPropId);
    }

    public String getMappingType() {
        return mappingType;
    }
}
