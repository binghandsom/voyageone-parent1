package com.voyageone.batch.ims.modelbean;

/**
 * Created by Leo on 15-7-21.
 */
public class CmsWorkLoad {
    private CmsModelPropBean modelProp;
    private int level;
    private String levelValue;

    public CmsModelPropBean getModelProp() {
        return modelProp;
    }

    public void setModelProp(CmsModelPropBean modelProp) {
        this.modelProp = modelProp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevelValue() {
        return levelValue;
    }

    public void setLevelValue(String levelValue) {
        this.levelValue = levelValue;
    }
}
