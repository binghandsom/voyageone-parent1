package com.voyageone.task2.cms.bean;

import com.voyageone.ims.enums.MasterPropTypeEnum;

/**
 * Created by Leo on 15-8-31.
 */
public class MasterPropBean {
    private String propId;
    private String propName;
    private MasterPropTypeEnum masterPropType;
    private PlatformPropBean platformProp;

    public String getPropId() {
        return propId;
    }

    public void setPropId(String propId) {
        this.propId = propId;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public MasterPropTypeEnum getMasterPropType() {
        return masterPropType;
    }

    public void setMasterPropType(MasterPropTypeEnum masterPropType) {
        this.masterPropType = masterPropType;
    }

    public PlatformPropBean getPlatformProp() {
        return platformProp;
    }

    public void setPlatformProp(PlatformPropBean platformProp) {
        this.platformProp = platformProp;
    }
}
