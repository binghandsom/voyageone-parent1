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

    private final String IS_REQUIRED = "required";
    private final String IS_DISPLAY = "isDisplay";
    private final String DATASOURCE = "dataSource";
    private final String READONLY = "readOnlyRule";
    private final String OPTIONS = "options";
    private final String TEXT = "text";
    private final String VALUE = "value";
    private final String VALUE_TYPE = "valueTypeRule";

    private Map<String, Object> rulMap;

    public CommonPropActionDefRuleBean(String actionRules) {
        rulMap = getActionMap(actionRules);
    }

    public Object getValue(String ruleKey) {
        return rulMap.get(ruleKey);
    }


    private Map getActionMap(String actionRules){
        return JsonUtil.jsonToMap(actionRules);
    }


    /**
     * 添加共通属性到主数据
     * @param field
     */
    public void setFieldComProperties(Field field){
        if (this.rulMap != null){

            List<Rule> rules = field.getRules();

            if (this.rulMap.get(this.IS_REQUIRED) != null){


                boolean isRequire = false;

                for (Rule r:rules){
                    if ("requiredRule".equals(r.getName()) && "true".equals(r.getValue())){
                        isRequire = true;
                        break;
                    }
                }

                if (!isRequire){
                    field.setFieldRequired();
                }

            }

            if (this.rulMap.get(this.DATASOURCE) != null){
                field.setDataSource(this.rulMap.get(this.DATASOURCE).toString());
            }

            if(this.rulMap.get(this.IS_DISPLAY) != null){
                if (new Boolean(this.rulMap.get(this.IS_DISPLAY).toString())){
                    field.setIsDisplay(1);
                }else {
                    field.setIsDisplay(0);
                }

            }

            if(this.rulMap.get(this.READONLY) != null){

                boolean isReadOnly = false;

                for (Rule r:rules){
                    if ("readOnlyRule".equals(r.getName()) && "true".equals(r.getValue())){
                        isReadOnly = true;
                        break;
                    }
                }

                if (!isReadOnly){
                    ReadOnlyRule rule = new ReadOnlyRule("true");
                    field.add(rule);
                }
            }
            if(this.rulMap.get(this.VALUE_TYPE) != null){
                ValueTypeRule rule = new ValueTypeRule(this.rulMap.get(this.VALUE_TYPE).toString());
                field.add(rule);
            }
            if (rulMap.get(this.OPTIONS) != null && field instanceof OptionsField){

                OptionsField optField = (OptionsField)field;

                List<Map> opts = (List<Map>)rulMap.get(this.OPTIONS);

                List<Option> options = new ArrayList<>();

                for (Map opMap:opts){
                    Option option = new Option();
                    option.setDisplayName(opMap.get(this.TEXT).toString());
                    option.setValue(opMap.get(this.VALUE).toString());
                    options.add(option);

                }

                optField.setOptions(options);

            }

        }

    }

    /**
     * 创建共通属性层次关系
     * @param defModels
     * @return
     */
    public static List<CommonPropActionDefBean> buildComPropHierarchical(List<CommonPropActionDefBean> defModels) {

        List<CommonPropActionDefBean> assistDefModels = new ArrayList<>(defModels);


        for (int i = 0; i < defModels.size(); i++) {
            CommonPropActionDefBean defModelItem = defModels.get(i);
            ActionType actionType = ActionType.valueOf(Integer.valueOf(defModelItem.getActionType()));
            List<CommonPropActionDefBean> sunDefModels = new ArrayList<>();
            for (Iterator<CommonPropActionDefBean> assIterator = assistDefModels.iterator(); assIterator.hasNext();) {

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
