package com.voyageone.task2.cms.service.putaway.rule_parser;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.cms.bean.CustomValueSystemParam;
import com.voyageone.task2.cms.bean.SxProductBean;
import com.voyageone.task2.cms.bean.tcb.TaskSignal;
import com.voyageone.task2.cms.service.putaway.UploadImageHandler;
import com.voyageone.ims.rule_expression.DictWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.TextWord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Leo on 15-6-2.
 */
public class ExpressionParser {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private TextWordParser textWordParser;
    private DictWordParser dictWordParser;
    private CustomWordParser customWordParser;
    private MasterWordParser masterWordParser;
    private FeedCnWordParser feedCnWordParser;
    private FeedOrgWordParser feedOrgWordParser;
    private SkuWordParser skuWordParser;
    private CustomValueSystemParam customValueSystemParam;

    public ExpressionParser(String orderChannelId, int cartId, SxProductBean mainSxProduct, List<SxProductBean> sxProductBeans) {
        customValueSystemParam = new CustomValueSystemParam();
        customValueSystemParam.setOrderChannelId(orderChannelId);
        customValueSystemParam.setCartId(cartId);
        customValueSystemParam.setMainSxProduct(mainSxProduct);
        customValueSystemParam.setSxProductBeans(sxProductBeans);

        this.dictWordParser = new DictWordParser(orderChannelId);
        this.textWordParser = new TextWordParser();
        this.customWordParser = new CustomWordParser(this, customValueSystemParam);

        this.masterWordParser = new MasterWordParser(mainSxProduct.getCmsBtProductModel());
        this.feedCnWordParser = new FeedCnWordParser(mainSxProduct.getCmsBtProductModel());
        this.feedOrgWordParser = new FeedOrgWordParser(mainSxProduct.getCmsBtProductModel(), mainSxProduct.getCmsBtFeedInfoModel());
        this.skuWordParser = new SkuWordParser();
    }

    public String parse(RuleExpression ruleExpression, Set<String> imageSet) throws TaskSignal {
        StringBuilder resultStr = new StringBuilder();

        if (ruleExpression != null) {
            for (RuleWord ruleWord : ruleExpression.getRuleWordList()) {
                String plainValue = "";
                switch (ruleWord.getWordType()) {
                    case TEXT:
                        plainValue = textWordParser.parse(ruleWord);
                        if (((TextWord)ruleWord).isUrl()) {
                            plainValue = UploadImageHandler.encodeImageUrl(plainValue);
                            imageSet.add(plainValue);
                        }
                        break;
                    case MASTER: {
                        plainValue = masterWordParser.parse(ruleWord);
                        break;
                    }
                    case FEED_ORG: {
                        plainValue = feedOrgWordParser.parse(ruleWord);
                        break;
                    }
                    case FEED_CN: {
                        plainValue = feedCnWordParser.parse(ruleWord);
                        break;
                    }
                    case DICT: {
                        DictWord dictWordDefine = dictWordParser.parseToDefineDict(ruleWord);
                        if (dictWordDefine == null)
                        {
                            logger.error("unknown dict word:" + ruleWord);
                            return null;
                        }

                        plainValue = parse(dictWordDefine.getExpression(), imageSet);

                        if (plainValue != null && imageSet != null && dictWordDefine.getIsUrl()) {
                            plainValue = UploadImageHandler.encodeImageUrl(plainValue);
                            imageSet.add(plainValue);
                        }

                        break;
                    }
                    case CUSTOM: {
                        plainValue = customWordParser.parse(ruleWord, imageSet);
                        break;
                    }
                    case SKU: {
                        plainValue = skuWordParser.parse(ruleWord);
                        break;
                    }
                }

                if (plainValue != null) {
                    resultStr.append(plainValue);
                }
                else {
                    return null;
                }
            }
        }
        else
            return null;
        return resultStr.toString();
    }

    public Map<String, Object> popMasterPropContext() {
        return masterWordParser.popEvaluationContext();
    }

    public void pushMasterPropContext(Map<String, Object> masterPropContext) {
        masterWordParser.pushEvaluationContext(masterPropContext);
    }

    public void setSkuPropContext(Map<String, Object> skuPropContext) {
        skuWordParser.setEvaluationContext(skuPropContext);
    }

    public void setMasterWordCmsBtProduct(CmsBtProductModel cmsBtProduct) {
        masterWordParser.setCmsBtProductModel(cmsBtProduct);
    }

    public CmsBtProductModel getMasterWordCmsBtProduct() {
        return masterWordParser.getCmsBtProductModel();
    }

    public static String encodeStringArray(List<String> mappedPropValues) {
        final String seperator = "~~";
        StringBuilder encodedString = new StringBuilder();

        for (String mappedPropValue : mappedPropValues) {
            encodedString.append(mappedPropValue).append(seperator);
        }
        return encodedString.substring(0, encodedString.length() - seperator.length());
    }

    public static String[] decodeString(String encodedString) {
        final String seperator = "~~";
        return encodedString.split(seperator);
    }
}
