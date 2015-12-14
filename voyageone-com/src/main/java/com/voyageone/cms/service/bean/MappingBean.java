package com.voyageone.cms.service.bean;

/**
 * Created by Leo on 15-12-9.
 */

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME
        , include = JsonTypeInfo.As.PROPERTY
        , property = "mappingType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleMappingBean.class, name = MappingBean.MAPPING_SIMPLE),
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

    public String getMappingType() {
        return mappingType;
    }

    /*
    public static void main(String[] args) throws IOException {
        ObjectMapper om = new ObjectMapper();

        SingleMappingBean singleMappingBean = new SingleMappingBean();
        singleMappingBean.setPlatformPropId("aaaaa");
        RuleExpression ruleExpression = new RuleExpression();

        MasterWord masterWord = new MasterWord("infos");
        Map<String, String> optionMapping = new HashMap<>();
        masterWord.setExtra(optionMapping);
        optionMapping.put("red", "green");
        optionMapping.put("blue", "white");
        ruleExpression.addRuleWord(masterWord);
        RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
        String expression = ruleJsonMapper.serializeRuleExpression(ruleExpression);
        singleMappingBean.setExpression(expression);
        String singleMappingValue = om.writeValueAsString(singleMappingBean);
        System.out.println(singleMappingValue);
        MappingBean smb = om.readValue(singleMappingValue, MappingBean.class);

        ComplexMappingBean complexMappingBean = new ComplexMappingBean();
        complexMappingBean.setPlatformPropId("product_images");
        complexMappingBean.setMasterPropId("product_images");
        List<MappingBean> subMappings = new ArrayList<>();
        complexMappingBean.setSubMappings(subMappings);

        subMappings.add(new SingleMappingBean("product_image1", "expression1"));
        subMappings.add(new SingleMappingBean("product_image2", "expression2"));

        String complexMappingValue = om.writeValueAsString(complexMappingBean);
        smb = om.readValue(complexMappingValue, MappingBean.class);

        System.out.println(complexMappingValue);
    }
    */
}
