package com.voyageone.service.impl.cms.sx.rule_parser;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.DictWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.TextWord;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;

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
    private MasterHtmlWordParser masterHtmlWordParser;
    private MasterClrHtmlWordParser masterClrHtmlWordParser;
    private FeedCnWordParser feedCnWordParser;
    private FeedOrgWordParser feedOrgWordParser;
    private SkuWordParser skuWordParser;
    // added by morse.lu 2016/06/27 start
    private CommonWordParser commonWordParser;
    // added by morse.lu 2016/06/27 end
    private SubCodeWordParser subCodeWordParser;

    public ExpressionParser(SxProductService sxProductService, SxData sxData) {
        this.sxProductService = sxProductService;
        this.sxData = sxData;
        this.dictWordParser = new DictWordParser(sxProductService, sxData.getChannelId(), sxData.getCartId());
//        this.dictWordParser = new DictWordParser(sxProductService, sxData.getMainProduct().getOrgChannelId(), sxData.getCartId());
        this.textWordParser = new TextWordParser();
        this.customWordParser = new CustomWordParser(this, sxProductService, sxData);

        // modified by morse.lu 2016/11/22 start
//        this.masterWordParser = new MasterWordParser(sxData.getMainProduct(), sxData.getCartId());
//        this.masterHtmlWordParser = new MasterHtmlWordParser(sxData.getMainProduct(), sxData.getCartId());
//        this.masterClrHtmlWordParser = new MasterClrHtmlWordParser(sxData.getMainProduct(), sxData.getCartId());
        this.masterWordParser = new MasterWordParser(sxData.getMainProduct(), sxData.getCartId(), this);
        this.masterHtmlWordParser = new MasterHtmlWordParser(sxData.getMainProduct(), sxData.getCartId(), this);
        this.masterClrHtmlWordParser = new MasterClrHtmlWordParser(sxData.getMainProduct(), sxData.getCartId(), this);
        // modified by morse.lu 2016/11/22 end
        this.feedCnWordParser = new FeedCnWordParser(sxData.getMainProduct());
        this.feedOrgWordParser = new FeedOrgWordParser(sxData.getMainProduct(), sxData.getCmsBtFeedInfoModel());
        this.skuWordParser = new SkuWordParser();
        // added by morse.lu 2016/06/27 start
        this.commonWordParser = new CommonWordParser(sxData.getMainProduct());
        // added by morse.lu 2016/06/27 end
        this.subCodeWordParser = new SubCodeWordParser(sxData);
    }

    public String parse(RuleExpression ruleExpression, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        StringBuilder resultStr = new StringBuilder();

        if (ruleExpression != null) {
            for (RuleWord ruleWord : ruleExpression.getRuleWordList()) {
                String plainValue = parseWord(ruleWord, shopBean, user, extParameter);

                // modified by morse.lu 2016/09/18 start
                // TODO：即使null也继续做下去,可能会有较大影响范围,有问题产生了,以后一点点修正别的地方的逻辑吧
//                if (plainValue != null) {
//                    resultStr.append(plainValue);
//                }
//                else {
//                    return null;
//                }
                resultStr.append(StringUtils.null2Space2(plainValue));
                // modified by morse.lu 2016/09/18 end
            }
        }
        else
            return null;
        return resultStr.toString();
    }

    public String parseWord(RuleWord ruleWord, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        String plainValue = "";
        switch (ruleWord.getWordType()) {
            case TEXT:
                plainValue = textWordParser.parse(ruleWord);
                if (((TextWord)ruleWord).isUrl()) {
                    if (shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM.getId())
                            || shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.JD.getId())
                            || shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.JM.getId())) {
//                                plainValue = sxProductService.encodeImageUrl(plainValue);
                        Set<String> url = new HashSet<>();
                        url.add(plainValue);
                        // 上传图片到天猫图片空间
                        Map<String, String> map = sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, url, user);
                        // added by morse.lu 2016/06/29 start
                        // 返回上传后的url
                        if (!StringUtils.isEmpty(map.get(plainValue))) {
                            plainValue = map.get(plainValue);
                        }
                        // added by morse.lu 2016/06/29 end
                    }
                }
                break;
            case MASTER:
                // modified by morse.lu 2016/11/22 start
//                plainValue = masterWordParser.parse(ruleWord);
                plainValue = masterWordParser.parse(ruleWord, shopBean, user, extParameter);
                // modified by morse.lu 2016/11/22 end
                break;
            case MASTER_HTML:
                // modified by morse.lu 2016/11/22 start
//                plainValue = masterHtmlWordParser.parse(ruleWord);
                plainValue = masterHtmlWordParser.parse(ruleWord, shopBean, user, extParameter);
                // modified by morse.lu 2016/11/22 end
                break;
            case MASTER_CLR_HTML:
                // modified by morse.lu 2016/11/22 start
//                plainValue = masterClrHtmlWordParser.parse(ruleWord);
                plainValue = masterClrHtmlWordParser.parse(ruleWord, shopBean, user, extParameter);
                // modified by morse.lu 2016/11/22 end
                break;
            case FEED_ORG:
                plainValue = feedOrgWordParser.parse(ruleWord);
                break;
            case FEED_CN:
                plainValue = feedCnWordParser.parse(ruleWord);
                break;
            case DICT:
                DictWord dictWordDefine = dictWordParser.parseToDefineDict(ruleWord);
                if (dictWordDefine == null)
                {
                    $error("字典不存在:" + ruleWord);
                    return null;
                }

                plainValue = parse(dictWordDefine.getExpression(), shopBean, user, extParameter);

                // deleted by morse.lu 2016/06/29 start
                // 图片上传在各自字典里做了
//                        if (!StringUtils.isEmpty(plainValue) && dictWordDefine.getIsUrl()) {
//                            if (shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM.getId())) {
////                                plainValue = sxProductService.encodeImageUrl(plainValue);
//                                Set<String> url = new HashSet<>();
//                                url.add(plainValue);
//                                // 上传图片到天猫图片空间
//                                sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, url, user);
//                            }
//                        }
                // deleted by morse.lu 2016/06/29 end

                break;
            case CUSTOM:
                plainValue = customWordParser.parse(ruleWord, shopBean, user, extParameter);
                break;
            case SKU:
                plainValue = skuWordParser.parse(ruleWord);
                break;
            // added by morse.lu 2016/06/27 start
            case COMMON:
                // 从product表的common下去取
                plainValue = commonWordParser.parse(ruleWord);
                break;
                // added by morse.lu 2016/06/27 end
            case SUBCODE:
                plainValue = subCodeWordParser.parse(ruleWord);
                break;
        }

        return plainValue;
    }

    public Map<String, Object> popMasterPropContext() {
        return masterWordParser.popEvaluationContext();
    }

    public void pushMasterPropContext(Map<String, Object> masterPropContext) {
        masterWordParser.pushEvaluationContext(masterPropContext);
    }

    public Map<String, Object> getLastMasterPropContext() {
        return masterWordParser.getLastEvaluationContext();
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

    public SxProductService getSxProductService() {
        return sxProductService;
    }

    public void setSxProductService(SxProductService sxProductService) {
        this.sxProductService = sxProductService;
    }

    public SxData getSxData() {
        return sxData;
    }

    public void setSxData(SxData sxData) {
        this.sxData = sxData;
    }
}
