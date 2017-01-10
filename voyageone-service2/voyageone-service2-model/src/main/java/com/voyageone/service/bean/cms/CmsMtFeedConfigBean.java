package com.voyageone.service.bean.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * Created by gjl on 2016/12/21.
 */
public class CmsMtFeedConfigBean extends BaseModel {

    private String orderChannelId;

    private String cfgName;

    private String cfgVal1;

    private String cfgVal2;

    private String cfgVal3;

    private String comment;

    private String cmsCfgName;

    private Integer cmsCfgNameLev;

    private Integer cmsCfgNameOrder;

    private Boolean cmsIsCfgVal1Display;

    private Boolean cmsIsCfgVal2Display;

    private Boolean cmsIsCfgVal3Display;

    public String getOrderChannelId() {
        return orderChannelId;
    }

    public void setOrderChannelId(String orderChannelId) {
        this.orderChannelId = orderChannelId;
    }

    public String getCfgName() {
        return cfgName;
    }

    public void setCfgName(String cfgName) {
        this.cfgName = cfgName;
    }

    public String getCfgVal1() {
        return cfgVal1;
    }

    public void setCfgVal1(String cfgVal1) {
        this.cfgVal1 = cfgVal1;
    }

    public String getCfgVal2() {
        return cfgVal2;
    }

    public void setCfgVal2(String cfgVal2) {
        this.cfgVal2 = cfgVal2;
    }

    public String getCfgVal3() {
        return cfgVal3;
    }

    public void setCfgVal3(String cfgVal3) {
        this.cfgVal3 = cfgVal3;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCmsCfgName() {
        return cmsCfgName;
    }

    public void setCmsCfgName(String cmsCfgName) {
        this.cmsCfgName = cmsCfgName;
    }

    public Integer getCmsCfgNameLev() {
        return cmsCfgNameLev;
    }

    public void setCmsCfgNameLev(Integer cmsCfgNameLev) {
        this.cmsCfgNameLev = cmsCfgNameLev;
    }

    public Integer getCmsCfgNameOrder() {
        return cmsCfgNameOrder;
    }

    public void setCmsCfgNameOrder(Integer cmsCfgNameOrder) {
        this.cmsCfgNameOrder = cmsCfgNameOrder;
    }

    public Boolean getCmsIsCfgVal1Display() {
        return cmsIsCfgVal1Display;
    }

    public void setCmsIsCfgVal1Display(Boolean cmsIsCfgVal1Display) {
        this.cmsIsCfgVal1Display = cmsIsCfgVal1Display;
    }

    public Boolean getCmsIsCfgVal2Display() {
        return cmsIsCfgVal2Display;
    }

    public void setCmsIsCfgVal2Display(Boolean cmsIsCfgVal2Display) {
        this.cmsIsCfgVal2Display = cmsIsCfgVal2Display;
    }

    public Boolean getCmsIsCfgVal3Display() {
        return cmsIsCfgVal3Display;
    }

    public void setCmsIsCfgVal3Display(Boolean cmsIsCfgVal3Display) {
        this.cmsIsCfgVal3Display = cmsIsCfgVal3Display;
    }
}
