package com.voyageone.cms.service.bean;

/**
 * Created by Leo on 15-12-9.
 */

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyageone.ims.rule_expression.MasterWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME
        , include = JsonTypeInfo.As.PROPERTY
        , property = "mappingType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleMappingBean.class, name = MappingBean.MAPPING_SINGLE),
        @JsonSubTypes.Type(value = ComplexMappingBean.class, name = MappingBean.MAPPING_COMPLEX)
})
public class MappingBean {
    public static final String MAPPING_SINGLE = "0";
    public static final String MAPPING_COMPLEX = "1";

    protected String platformPropId;
    protected String mappingType;

    public String getPlatformPropId() {
        return platformPropId;
    }

    public void setPlatformPropId(String platformPropId) {
        this.platformPropId = platformPropId;
    }

}
