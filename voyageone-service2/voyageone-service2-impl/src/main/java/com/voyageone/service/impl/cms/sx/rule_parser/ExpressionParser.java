package com.voyageone.service.impl.cms.sx.rule_parser;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.ims.rule_expression.DictWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.TextWord;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class ExpressionParser extends VOAbsLoggable {

    private SxProductService sxProductService;

    private SxData sxData;
    private TextWordParser textWordParser;
    private DictWordParser dictWordParser;
    private CustomWordParser customWordParser;
    private MasterWordParser masterWordParser;
    private FeedCnWordParser feedCnWordParser;
    private FeedOrgWordParser feedOrgWordParser;
    private SkuWordParser skuWordParser;

    public ExpressionParser(SxProductService sxProductService, SxData sxData) {
        this.sxProductService = sxProductService;
        this.sxData = sxData;
        this.dictWordParser = new DictWordParser(sxProductService, sxData.getChannelId());
        this.textWordParser = new TextWordParser();
        this.customWordParser = new CustomWordParser(this, sxData);

        this.masterWordParser = new MasterWordParser(sxData.getMainProduct());
        this.feedCnWordParser = new FeedCnWordParser(sxData.getMainProduct());
        this.feedOrgWordParser = new FeedOrgWordParser(sxData.getMainProduct(), sxData.getCmsBtFeedInfoModel());
        this.skuWordParser = new SkuWordParser();
    }

    public String parse(RuleExpression ruleExpression, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        StringBuilder resultStr = new StringBuilder();

        if (ruleExpression != null) {
            for (RuleWord ruleWord : ruleExpression.getRuleWordList()) {
                String plainValue = "";
                switch (ruleWord.getWordType()) {
                    case TEXT:
                        plainValue = textWordParser.parse(ruleWord);
                        if (((TextWord)ruleWord).isUrl()) {
                            if (shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM.getId())) {
//                                plainValue = sxProductService.encodeImageUrl(plainValue);
                                Set<String> url = new HashSet<>();
                                url.add(plainValue);
                                // 上传图片到天猫图片空间
                                sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, url, user);
                            }
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
                            $error("unknown dict word:" + ruleWord);
                            return null;
                        }

                        plainValue = parse(dictWordDefine.getExpression(), shopBean, user, extParameter);

                        if (plainValue != null && dictWordDefine.getIsUrl()) {
                            if (shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM.getId())) {
//                                plainValue = sxProductService.encodeImageUrl(plainValue);
                                Set<String> url = new HashSet<>();
                                url.add(plainValue);
                                // 上传图片到天猫图片空间
                                sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, url, user);
                            }
                        }

                        break;
                    }
                    case CUSTOM: {
                        plainValue = customWordParser.parse(ruleWord, shopBean, user, extParameter);
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
