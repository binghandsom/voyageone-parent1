package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Leo on 15-6-24.
 */
public class RuleJsonMapper {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private ObjectMapper om;

    public RuleJsonMapper() {
        om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String serializeRuleWord(RuleWord word) {
        try {
            return om.writeValueAsString(word);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public String serializeRuleExpression(RuleExpression expression) {
        try {
            return om.writeValueAsString(expression);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public RuleWord deserializeRuleWord(String json) {
        try {
            return om.readValue(json, RuleWord.class);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public RuleExpression deserializeRuleExpression(String json) {
        try {
            return om.readValue(json, RuleExpression.class);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            logger.error("json:" + json);
            return null;
        }
    }
}
