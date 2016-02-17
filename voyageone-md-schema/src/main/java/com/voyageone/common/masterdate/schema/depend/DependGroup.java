package com.voyageone.common.masterdate.schema.depend;

import com.voyageone.common.masterdate.schema.utils.XmlUtils;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.field.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Element;

public class DependGroup {

    public static final String OPERATOR_AND = "and";
    public static final String OPERATOR_OR = "or";
    protected List<DependExpress> dependExpressList = new ArrayList<>();
    protected String operator = "and";
    protected List<DependGroup> dependGroupList = new ArrayList<>();

    public DependGroup() {
    }

    public Element toElement() throws TopSchemaException {
        Element dependGroup = XmlUtils.createRootElement("depend-group");
        dependGroup.addAttribute("operator", this.operator);
        Iterator i$ = this.dependExpressList.iterator();

        Element dependGroupElement;
        while(i$.hasNext()) {
            DependExpress dependGroupNode = (DependExpress)i$.next();
            dependGroupElement = XmlUtils.appendElement(dependGroup, "depend-express");
            dependGroupElement.addAttribute("fieldId", dependGroupNode.getFieldId());
            dependGroupElement.addAttribute("value", dependGroupNode.getValue());
            dependGroupElement.addAttribute("symbol", dependGroupNode.getSymbol());
        }

        i$ = this.dependGroupList.iterator();

        while(i$.hasNext()) {
            DependGroup dependGroupNode1 = (DependGroup)i$.next();
            dependGroupElement = dependGroupNode1.toElement();
            XmlUtils.appendElement(dependGroup, dependGroupElement);
        }

        return dependGroup;
    }

    public boolean isEmpty() {
        return this.dependExpressList.isEmpty() && this.dependGroupList.isEmpty();
    }

    public String toExpress() {
        String exp = "";

        Iterator i$;
        String expLocal;
        for(i$ = this.dependExpressList.iterator(); i$.hasNext(); exp = exp + expLocal) {
            DependExpress dependGroupNode = (DependExpress)i$.next();
            expLocal = "";
            if(exp.length() > 0) {
                expLocal = expLocal + this.operator;
            }

            expLocal = expLocal + " " + dependGroupNode.getFieldId();
            expLocal = expLocal + dependGroupNode.getSymbol();
            expLocal = expLocal + dependGroupNode.getValue() + " ";
        }

        for(i$ = this.dependGroupList.iterator(); i$.hasNext(); exp = exp + expLocal) {
            DependGroup dependGroupNode1 = (DependGroup)i$.next();
            expLocal = "";
            if(exp.length() > 0) {
                expLocal = expLocal + this.operator;
            }

            expLocal = expLocal + " (" + dependGroupNode1.toExpress() + ") ";
        }

        return exp;
    }

    public boolean excuteExpress(Map<String, Field> fieldMap) {
        Iterator i$;
        DependExpress dependGroup;
        DependGroup dependGroup1;
        if("and".equals(this.operator)) {
            i$ = this.dependExpressList.iterator();

            do {
                if(!i$.hasNext()) {
                    i$ = this.dependGroupList.iterator();

                    do {
                        if(!i$.hasNext()) {
                            return true;
                        }

                        dependGroup1 = (DependGroup)i$.next();
                    } while(dependGroup1.excuteExpress(fieldMap));

                    return false;
                }

                dependGroup = (DependExpress)i$.next();
            } while(dependGroup.excuteExpress(fieldMap));

            return false;
        } else if("or".equals(this.operator)) {
            i$ = this.dependExpressList.iterator();

            do {
                if(!i$.hasNext()) {
                    i$ = this.dependGroupList.iterator();

                    do {
                        if(!i$.hasNext()) {
                            return false;
                        }

                        dependGroup1 = (DependGroup)i$.next();
                    } while(!dependGroup1.excuteExpress(fieldMap));

                    return true;
                }

                dependGroup = (DependExpress)i$.next();
            } while(!dependGroup.excuteExpress(fieldMap));

            return true;
        } else {
            return false;
        }
    }

    public DependExpress addDependExpress() {
        DependExpress de = new DependExpress();
        this.add(de);
        return de;
    }

    public DependExpress addDependExpress(String fieldId, String value) {
        DependExpress de = new DependExpress();
        de.setFieldId(fieldId);
        de.setValue(value);
        this.add(de);
        return de;
    }

    public DependGroup addDependGroup() {
        DependGroup dg = new DependGroup();
        this.add(dg);
        return dg;
    }

    public void add(DependExpress de) {
        this.dependExpressList.add(de);
    }

    public void add(DependGroup de) {
        this.dependGroupList.add(de);
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<DependExpress> getDependExpressList() {
        return this.dependExpressList;
    }

    public void setDependExpressList(List<DependExpress> dependExpressList) {
        this.dependExpressList = dependExpressList;
    }

    public List<DependGroup> getDependGroupList() {
        return this.dependGroupList;
    }

    public void setDependGroupList(List<DependGroup> dependGroupList) {
        this.dependGroupList = dependGroupList;
    }
}
