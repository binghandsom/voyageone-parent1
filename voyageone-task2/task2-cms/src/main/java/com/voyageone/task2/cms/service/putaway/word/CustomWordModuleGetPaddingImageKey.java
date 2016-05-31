package com.voyageone.task2.cms.service.putaway.word;

import com.voyageone.task2.cms.bean.CustomValueSystemParam;
import com.voyageone.task2.cms.bean.tcb.TaskSignal;
import com.voyageone.task2.cms.dao.PaddingImageDao;
import com.voyageone.task2.cms.service.putaway.rule_parser.ExpressionParser;
import com.voyageone.ims.rule_expression.CustomModuleUserParamGetPaddingImageKey;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueGetPaddingImageKey;
import com.voyageone.ims.rule_expression.RuleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by Leo on 15-6-26.
 */
@Repository
public class CustomWordModuleGetPaddingImageKey extends CustomWordModule {
    public final static String moduleName = "GetPaddingImageKey";
    @Autowired
    private PaddingImageDao paddingImageDao;

    public CustomWordModuleGetPaddingImageKey() {
        super(moduleName);
    }

    public CustomWordModuleGetPaddingImageKey(PaddingImageDao paddingImageDao) {
        this();
        this.paddingImageDao = paddingImageDao;
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam) throws TaskSignal {
        return parse(customWord, expressionParser, systemParam, null);
    }

    //public Str
    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, CustomValueSystemParam systemParam, Set<String> imageSet) throws TaskSignal {
        String orderChannelId = systemParam.getOrderChannelId();
        int cartId = systemParam.getCartId();

        //user param
        CustomModuleUserParamGetPaddingImageKey customModuleUserParamGetPaddingImageKey = ((CustomWordValueGetPaddingImageKey) customWord.getValue()).getUserParam();

        RuleExpression paddingPropNameExpression = customModuleUserParamGetPaddingImageKey.getPaddingPropName();
        RuleExpression imageIndexExpression = customModuleUserParamGetPaddingImageKey.getImageIndex();

        String paddingPropName = expressionParser.parse(paddingPropNameExpression, null);
        int imageIndex = Integer.valueOf(expressionParser.parse(imageIndexExpression, null));

        return paddingImageDao.selectByCriteria(orderChannelId, cartId, paddingPropName, imageIndex);
    }
}
