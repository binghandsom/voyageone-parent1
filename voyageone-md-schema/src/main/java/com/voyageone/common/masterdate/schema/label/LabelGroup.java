package com.voyageone.common.masterdate.schema.label;

import com.voyageone.common.masterdate.schema.Util.XmlUtils;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Element;

public class LabelGroup {
    private String name;
    private List<LabelGroup> labelGroupList = new ArrayList();
    private List<Label> labelList = new ArrayList();

    public LabelGroup() {
    }

    public Element toElement() throws TopSchemaException {
        Element labelGroupEle = XmlUtils.createRootElement("label-group");
        labelGroupEle.addAttribute("name", this.name);
        Iterator i$ = this.labelList.iterator();

        Element subLabelGroupElement;
        while(i$.hasNext()) {
            Label labelGroup = (Label)i$.next();
            subLabelGroupElement = XmlUtils.appendElement(labelGroupEle, "label");
            subLabelGroupElement.addAttribute("name", labelGroup.getName());
            subLabelGroupElement.addAttribute("value", labelGroup.getValue());
            subLabelGroupElement.addAttribute("desc", labelGroup.getDesc());
        }

        i$ = this.labelGroupList.iterator();

        while(i$.hasNext()) {
            LabelGroup labelGroup1 = (LabelGroup)i$.next();
            subLabelGroupElement = labelGroup1.toElement();
            XmlUtils.appendElement(labelGroupEle, subLabelGroupElement);
        }

        return labelGroupEle;
    }

    public void addLabel(String name, String value, String desc) {
        Label label = new Label();
        label.setName(name);
        label.setValue(value);
        label.setDesc(desc);
        this.labelList.add(label);
    }

    public void add(Label label) {
        this.labelList.add(label);
    }

    public void add(LabelGroup labelGroup) {
        this.labelGroupList.add(labelGroup);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LabelGroup> getLabelGroupList() {
        return this.labelGroupList;
    }

    public void setLabelGroupList(List<LabelGroup> labelGroupList) {
        this.labelGroupList = labelGroupList;
    }

    public List<Label> getLabelList() {
        return this.labelList;
    }

    public void setLabelList(List<Label> labelList) {
        this.labelList = labelList;
    }
}
