package com.voyageone.cms.service.model;

import com.voyageone.common.configs.Enums.ActionType;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JsonUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lewis on 15-12-7.
 */
public class MtCommPropActionDefRuleModel {

    private final String IS_REQUIRED = "required";
    private final String IS_DISPLAY = "isDisplay";
    private final String DATASOURCE = "dataSource";

    private Map<String, Object> rulMap;

    public MtCommPropActionDefRuleModel(String actionRules) {
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
            if (this.rulMap.get(this.IS_REQUIRED) != null){
                field.setFieldRequired();
            }

            if (this.rulMap.get(this.DATASOURCE) !=null){
                field.setDataSource(this.rulMap.get(this.DATASOURCE).toString());
            }

            if(this.rulMap.get(this.IS_DISPLAY) != null){
                if (new Boolean(this.rulMap.get(this.IS_DISPLAY).toString())){
                    field.setIsDisplay(1);
                }else {
                    field.setIsDisplay(0);
                }

            }
        }

    }

    /**
     * 创建共通属性层次关系
     * @param defModels
     * @return
     */
    public static List<MtCommPropActionDefModel> buildComPropHierarchical(List<MtCommPropActionDefModel> defModels) {

        List<MtCommPropActionDefModel> assistDefModels = new ArrayList<>(defModels);

        List<MtCommPropActionDefModel> removeDefModels = new ArrayList<>();

        for (int i = 0; i < defModels.size(); i++) {
            MtCommPropActionDefModel defModelItem = defModels.get(i);
            ActionType actionType = ActionType.valueOf(Integer.valueOf(defModelItem.getActionType()));
            List<MtCommPropActionDefModel> sunDefModels = new ArrayList<>();
            for (Iterator<MtCommPropActionDefModel> assIterator = assistDefModels.iterator(); assIterator.hasNext();) {

                MtCommPropActionDefModel subPlatformCatItem = assIterator.next();
                if (ActionType.Add.equals(actionType) && defModelItem.getPropId().equals(subPlatformCatItem.getParentPropId()) ) {
                    sunDefModels.add(subPlatformCatItem);
                    assIterator.remove();
                }

            }
            defModelItem.setDefModels(sunDefModels);

        }

        //删除掉所有非顶层属性引用,只留下最顶层属性
//        defModels.removeAll(removeDefModels);

        return defModels;
    }

}
