package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.ims.rule_expression.CustomModuleUserParamGetPaddingImageKey;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueGetPaddingImageKey;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.daoext.cms.PaddingImageDaoExt;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class CustomWordModuleGetPaddingImageKey extends CustomWordModule {
    public final static String moduleName = "GetPaddingImageKey";
    @Autowired
    private PaddingImageDaoExt paddingImageDaoExt;

    public CustomWordModuleGetPaddingImageKey() {
        super(moduleName);
    }

//    public CustomWordModuleGetPaddingImageKey(PaddingImageDao paddingImageDao) {
//        this();
//        this.paddingImageDao = paddingImageDao;
//    }

//    @Override
//    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user) {
//        return parse(customWord, expressionParser, sxData, shopBean, user, null);
//    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user) throws Exception {
        String channelId = sxData.getChannelId();
        int cartId = sxData.getCartId();

        //user param
        CustomModuleUserParamGetPaddingImageKey customModuleUserParamGetPaddingImageKey = ((CustomWordValueGetPaddingImageKey) customWord.getValue()).getUserParam();

        RuleExpression paddingPropNameExpression = customModuleUserParamGetPaddingImageKey.getPaddingPropName();
        RuleExpression imageIndexExpression = customModuleUserParamGetPaddingImageKey.getImageIndex();

        String paddingPropName = expressionParser.parse(paddingPropNameExpression, shopBean, user);
        int imageIndex = Integer.valueOf(expressionParser.parse(imageIndexExpression, shopBean, user));

        return paddingImageDaoExt.selectByCriteria(channelId, cartId, paddingPropName, imageIndex);
    }
}
