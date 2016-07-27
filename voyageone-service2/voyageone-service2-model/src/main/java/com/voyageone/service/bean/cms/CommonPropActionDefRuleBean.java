package com.voyageone.service.bean.cms;

import com.voyageone.common.configs.Enums.ActionType;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.OptionsField;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.rule.ReadOnlyRule;
import com.voyageone.common.masterdate.schema.rule.Rule;
import com.voyageone.common.masterdate.schema.rule.ValueTypeRule;
import com.voyageone.common.util.JsonUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lewis on 15-12-7.
 */
public class CommonPropActionDefRuleBean {

    private static final String IS_REQUIRED = "required";
    private static final String IS_DISPLAY = "isDisplay";
    private static final String DATASOURCE = "dataSource";
    private static final String READONLY = "readOnlyRule";
    private static final String OPTIONS = "options";
    private static final String TEXT = "text";
    private static final String VALUE = "value";
    private static final String VALUE_TYPE = "valueTypeRule";

    private Map<String, Object> rulMap;

    public CommonPropActionDefRuleBean(String actionRules) {
        rulMap = getActionMap(actionRules);
    }

    public Object getValue(String ruleKey) {
        return rulMap.get(ruleKey);
    }


    private Map getActionMap(String actionRules) {
        return JsonUtil.jsonToMap(actionRules);
    }


    /**
     * 添加共通属性到主数据
     *
     * @param field Field
     */
    public void setFieldComProperties(Field field) {
        if (this.rulMap != null) {

            List<Rule> rules = field.getRules();

            if (this.rulMap.get(IS_REQUIRED) != null) {


                boolean isRequire = false;

                for (Rule r : rules) {
                    if ("requiredRule".equals(r.getName()) && "true".equals(r.getValue())) {
                        isRequire = true;
                        break;
                    }
                }

                if (!isRequire) {
                    field.setFieldRequired();
                }

            }

            if (this.rulMap.get(DATASOURCE) != null) {
                field.setDataSource(this.rulMap.get(DATASOURCE).toString());
            }

            if (this.rulMap.get(IS_DISPLAY) != null) {
                if (Boolean.parseBoolean(this.rulMap.get(IS_DISPLAY).toString())) {
                    field.setIsDisplay(1);
                } else {
                    field.setIsDisplay(0);
                }

            }

            if (this.rulMap.get(READONLY) != null) {

                boolean isReadOnly = false;

                for (Rule r : rules) {
                    if ("readOnlyRule".equals(r.getName()) && "true".equals(r.getValue())) {
                        isReadOnly = true;
                        break;
                    }
                }

                if (!isReadOnly) {
                    ReadOnlyRule rule = new ReadOnlyRule("true");
                    field.add(rule);
                }
            }
            if (this.rulMap.get(VALUE_TYPE) != null) {
                ValueTypeRule rule = new ValueTypeRule(this.rulMap.get(VALUE_TYPE).toString());
                field.add(rule);
            }
            if (rulMap.get(OPTIONS) != null && field instanceof OptionsField) {

                OptionsField optField = (OptionsField) field;

                List<Map> opts = (List<Map>) rulMap.get(OPTIONS);

                List<Option> options = new ArrayList<>();

                for (Map opMap : opts) {
                    Option option = new Option();
                    option.setDisplayName(opMap.get(TEXT).toString());
                    option.setValue(opMap.get(VALUE).toString());
                    options.add(option);

                }

                optField.setOptions(options);

            }

        }

    }

    /**
     * 创建共通属性层次关系
     */
    public static List<CommonPropActionDefBean> buildComPropHierarchical(List<CommonPropActionDefBean> defModels) {

        List<CommonPropActionDefBean> assistDefModels = new ArrayList<>(defModels);


        for (CommonPropActionDefBean defModelItem : defModels) {
            ActionType actionType = ActionType.valueOf(defModelItem.getActionType());
            List<CommonPropActionDefBean> sunDefModels = new ArrayList<>();
            for (Iterator<CommonPropActionDefBean> assIterator = assistDefModels.iterator(); assIterator.hasNext(); ) {

                CommonPropActionDefBean subPlatformCatItem = assIterator.next();
                if (ActionType.ADD.equals(actionType) && defModelItem.getPropId().equals(subPlatformCatItem.getParentPropId())) {
                    sunDefModels.add(subPlatformCatItem);
                    assIterator.remove();
                }

            }
            defModelItem.setDefModels(sunDefModels);

        }

        return defModels;
    }

}
