package com.voyageone.batch.cms.model;

/**
 * Created by zhujiaye on 15/6/28.
 */
public class PropOptionMappingBean {

    // 第三方平台属性可选项hash
    private String platformPropOptionHash;
    // 主数据属性可选项id
    private int propOptionId;

    public String getPlatformPropOptionHash() {
        return platformPropOptionHash;
    }

    public void setPlatformPropOptionHash(String platformPropOptionHash) {
        this.platformPropOptionHash = platformPropOptionHash;
    }

    public int getPropOptionId() {
        return propOptionId;
    }

    public void setPropOptionId(int propOptionId) {
        this.propOptionId = propOptionId;
    }
}
