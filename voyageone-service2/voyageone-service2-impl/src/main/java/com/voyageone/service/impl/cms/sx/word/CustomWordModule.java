package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public abstract class CustomWordModule extends VOAbsLoggable {

    private String moduleName;

    public CustomWordModule(String moduleName) {
        this.moduleName = moduleName;
    }

    abstract public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user) throws Exception;
//    abstract public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user, Set<String> imageSet);

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
