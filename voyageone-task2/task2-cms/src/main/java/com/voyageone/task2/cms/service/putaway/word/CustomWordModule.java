package com.voyageone.task2.cms.service.putaway.word;

import com.voyageone.task2.cms.bean.CustomValueSystemParam;
import com.voyageone.task2.cms.service.putaway.rule_parser.ExpressionParser;
import com.voyageone.ims.rule_expression.CustomWord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by Leo on 15-6-16.
 */
public abstract class CustomWordModule {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String moduleName;

    public CustomWordModule(String moduleName) {
        this.moduleName = moduleName;
    }

    abstract public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam);
    abstract public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam, Set<String> imageSet);

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CustomWordModule))
            return false;

        CustomWordModule customWordModule = (CustomWordModule) obj;

        return customWordModule.getModuleName().equals(moduleName);

    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
