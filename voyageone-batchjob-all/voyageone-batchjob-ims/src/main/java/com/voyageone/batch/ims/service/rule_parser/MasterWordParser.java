package com.voyageone.batch.ims.service.rule_parser;

import com.voyageone.batch.ims.dao.PropDao;
import com.voyageone.batch.ims.dao.PropValueDao;
import com.voyageone.batch.ims.modelbean.PropBean;
import com.voyageone.batch.ims.modelbean.PropValueBean;
import com.voyageone.ims.rule_expression.MasterWord;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.WordType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Created by Leo on 15-6-18.
 */
public class MasterWordParser {
    private PropDao propDao;
    private PropValueDao propValueDao;
    private String categoryId;
    private String orderChannelId;
    private int level;
    private String levelValue;

    private Log logger = LogFactory.getLog(MasterWordParser.class);

    public MasterWordParser(String categoryId, PropDao propDao, PropValueDao propValueDao,
                            String orderChannelId, int level, String levelValue) {
        this.categoryId = categoryId;
        this.propDao = propDao;
        this.propValueDao = propValueDao;
        this.level = level;
        this.orderChannelId = orderChannelId;
        this.levelValue = levelValue;
    }

    //目前只支持解析model级别的属性
    public String parse(RuleWord ruleWord)
    {
        if (!WordType.MASTER.equals(ruleWord.getWordType()))
        {
            return null;
        }
        else
        {
            MasterWord masterWord = (MasterWord) ruleWord;
            String propName = masterWord.getValue();
            PropBean propBean = propDao.selectPropByPropName(categoryId, propName);
            List<PropValueBean> propValueBeans = propValueDao.selectPropValue(orderChannelId, level, levelValue, String.valueOf(propBean.getPropId()));
            if (propValueBeans != null && propValueBeans.size() != 1) {
                logger.error("Count of MasterWord's value should be one!");
                return null;
            }
            if (propValueBeans == null) {
                logger.warn("Count of MasterWord's value should be one!");
                return "";
            }
            return propValueBeans.get(0).getProp_value();
        }
    }
}
