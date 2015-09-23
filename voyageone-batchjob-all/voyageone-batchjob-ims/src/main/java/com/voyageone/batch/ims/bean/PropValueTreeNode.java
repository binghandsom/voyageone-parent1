package com.voyageone.batch.ims.bean;

import com.voyageone.batch.ims.modelbean.PlatformPropBean;
import com.voyageone.batch.ims.modelbean.PropValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 15-7-9.
 */
public class PropValueTreeNode {
    private PropValueBean propValue;
    private PropValueTreeNode parentNode;
    private List<PropValueTreeNode> childNodes;
    private PlatformPropBean platformProp;
    private Object platformField;

    public void addChildNode(PropValueTreeNode propValueTreeNode)
    {
        if (childNodes == null)
            childNodes = new ArrayList<>();

        childNodes.add(propValueTreeNode);
    }
    public PropValueTreeNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(PropValueTreeNode parentNode) {
        this.parentNode = parentNode;
    }

    public List<PropValueTreeNode> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<PropValueTreeNode> childNodes) {
        this.childNodes = childNodes;
    }

    public PropValueBean getPropValue() {
        return propValue;
    }

    public void setPropValue(PropValueBean propValue) {
        this.propValue = propValue;
    }

    public PlatformPropBean getPlatformProp() {
        return platformProp;
    }

    public void setPlatformProp(PlatformPropBean platformProp) {
        this.platformProp = platformProp;
    }

    public Object getPlatformField() {
        return platformField;
    }

    public void setPlatformField(Object platformField) {
        this.platformField = platformField;
    }
}
