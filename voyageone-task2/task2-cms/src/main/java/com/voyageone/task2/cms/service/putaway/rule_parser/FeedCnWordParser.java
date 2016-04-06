package com.voyageone.task2.cms.service.putaway.rule_parser;

import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.FeedCnWord;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.WordType;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 15-6-18.
 */
public class FeedCnWordParser {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private CmsBtProductModel cmsBtProductModel;

    public FeedCnWordParser(CmsBtProductModel cmsBtProductModel) {
        this.cmsBtProductModel = cmsBtProductModel;
    }

    //目前只支持解析model级别的属性
    public String parse(RuleWord ruleWord)
    {
        if (!WordType.FEED_CN.equals(ruleWord.getWordType())) {
            return null;
        }
        else
        {
            FeedCnWord feedCnWord = (FeedCnWord) ruleWord;
            String propName = feedCnWord.getValue();

            // 如果propName为空的话, 就说明是应该看index, 而不是看propName
            if (StringUtils.isEmpty(propName)) {
                // 看的内容是feed.customIds 和 feed.customIdsCn, 配合 feed.cnAtts的内容, 进行返回
                if (feedCnWord.isTitle()) {
                    // 返回中文标题
                    try {
                        return cmsBtProductModel.getFeed().getCustomIdsCn().get(feedCnWord.getFeedIndex());
                    } catch (Exception e) {
                        // 没有的话直接返回一个有值得空字符串
                        return "";
                    }
                } else {
                    // 返回中文的值
                    try {
                        String key = cmsBtProductModel.getFeed().getCustomIds().get(feedCnWord.getFeedIndex());
                        return cmsBtProductModel.getFeed().getCnAtts().getAttribute(key);
                    } catch (Exception e) {
                        // 没有的话直接返回一个有值得空字符串
                        return "";
                    }
                }
            }
            // 如果propName有内容的话, 那就按照原来的逻辑进行处理
            Map<String, String> extra = feedCnWord.getExtra();
            Object plainPropValueObj = cmsBtProductModel.getFeed().getCnAtts().getAttribute(propName);

            if (plainPropValueObj == null) {
                return null;
            }

            if (extra == null) {
                return String.valueOf(plainPropValueObj);
            } else {
                if (plainPropValueObj instanceof String) {
                    return extra.get(plainPropValueObj);
                } else if (plainPropValueObj instanceof  String[]) {
                    String[] plainPropValues = (String[]) plainPropValueObj;
                    List<String> mappedPropValues = new ArrayList<>();
                    for (String plainPropValue : plainPropValues) {
                        mappedPropValues.add(extra.get(plainPropValue));
                    }
                    return ExpressionParser.encodeStringArray(mappedPropValues);
                } else {
                    logger.error("Master value must be String or String[]");
                    return null;
                }
            }
        }
    }
}
