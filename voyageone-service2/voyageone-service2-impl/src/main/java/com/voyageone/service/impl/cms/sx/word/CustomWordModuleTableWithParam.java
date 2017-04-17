package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.*;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class CustomWordModuleTableWithParam extends CustomWordModule {

    public final static String moduleName = "TableWithParam";

    public CustomWordModuleTableWithParam() {
        super(moduleName);
    }

//    @Override
//    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user) {
//        return parse(customWord, expressionParser, sxData, shopBean, user, null);
//    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxProductService sxProductService, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        //user param
        CustomModuleUserParamTableWithParam customModuleUserParamTableWithParam = ((CustomWordValueTableWithParam) customWord.getValue()).getUserParam();

        RuleExpression tableTemplateExpression = customModuleUserParamTableWithParam.getTableTemplate();
        List<RuleExpression> tableParamExpressions = customModuleUserParamTableWithParam.getTableParams();
        int columnCount = customModuleUserParamTableWithParam.getColumnCount();
        String tableTemplate = expressionParser.parse(tableTemplateExpression, shopBean, user, extParameter);
        List<String> tableParams = new ArrayList<>();
        for (RuleExpression tableParamExpression : tableParamExpressions) {
            String tableParam = expressionParser.parse(tableParamExpression, shopBean, user, extParameter);
            if (StringUtils.isEmpty(tableParam.trim())) {
                continue;
            }
//            try {
//                tableParam = URLEncoder.encode(tableParam, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.fillInStackTrace();
//                $error(e.getMessage(), e);
//            }
            tableParams.add(tableParam);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(tableTemplate);
        for (int paramIdx = 0; paramIdx < tableParams.size();) {
            stringBuffer.append("<tr>");
            for (int colIdx = 0; colIdx < columnCount; colIdx++) {
                String tableParamItem;
                if (paramIdx > tableParams.size() - 1) {
                    tableParamItem = "&nbsp;";
                } else {
                    tableParamItem = tableParams.get(paramIdx);
                }
                stringBuffer.append(String.format("<td>%s</td>", tableParamItem));
                paramIdx++;
            }
            stringBuffer.append("</tr>");
        }
        stringBuffer.append("</table></div>");

        return stringBuffer.toString();
    }
}
