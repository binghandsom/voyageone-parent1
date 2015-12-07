package com.voyageone.cms.service.model;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.List;

/**
 * Created by zhujiaye on 15/12/7.
 */
public class CmsBtFeedMappingModel extends ChannelPartitionModel {
    public CmsBtFeedMappingModel() {}

    public CmsBtFeedMappingModel(String channelId) {
        super(channelId);
    }

    private Scope scope;
    private int defaultMapping;
    private int matchOver;
    private List<Prop> propList;

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public int getDefaultMapping() {
        return defaultMapping;
    }

    public void setDefaultMapping(int defaultMapping) {
        this.defaultMapping = defaultMapping;
    }

    public int getMatchOver() {
        return matchOver;
    }

    public void setMatchOver(int matchOver) {
        this.matchOver = matchOver;
    }

    public List<Prop> getPropList() {
        return propList;
    }

    public void setPropList(List<Prop> propList) {
        this.propList = propList;
    }

    public class Scope {
        private String channelId;
        private String feedCategory;
        private String mainCategoryId;

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getFeedCategory() {
            return feedCategory;
        }

        public void setFeedCategory(String feedCategory) {
            this.feedCategory = feedCategory;
        }

        public String getMainCategoryId() {
            return mainCategoryId;
        }

        public void setMainCategoryId(String mainCategoryId) {
            this.mainCategoryId = mainCategoryId;
        }
    }

    public class Prop {
        private String propName;
        private Condition condition;
        private List<Prop> propList;

        public String getPropName() {
            return propName;
        }

        public void setPropName(String propName) {
            this.propName = propName;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }

        public List<Prop> getPropList() {
            return propList;
        }

        public void setPropList(List<Prop> propList) {
            this.propList = propList;
        }
    }

    public class Condition {

        private List<Chk> chkList;
        private SrcType srcType;
        private String srcVal;

        public List<Chk> getChkList() {
            return chkList;
        }

        public void setChkList(List<Chk> chkList) {
            this.chkList = chkList;
        }

        public SrcType getSrcType() {
            return srcType;
        }

        public void setSrcType(SrcType srcType) {
            this.srcType = srcType;
        }

        public String getSrcVal() {
            return srcVal;
        }

        public void setSrcVal(String srcVal) {
            this.srcVal = srcVal;
        }
    }

    public class Chk {

        private String chkSrc;
        private ChkSymbolEnum chkSymbol;
        private String chkVal;

        public String getChkSrc() {
            return chkSrc;
        }

        public void setChkSrc(String chkSrc) {
            this.chkSrc = chkSrc;
        }

        public ChkSymbolEnum getChkSymbol() {
            return chkSymbol;
        }

        public void setChkSymbol(ChkSymbolEnum chkSymbol) {
            this.chkSymbol = chkSymbol;
        }

        public String getChkVal() {
            return chkVal;
        }

        public void setChkVal(String chkVal) {
            this.chkVal = chkVal;
        }
    }

    public enum ChkSymbolEnum {
        isNull("isNull"),
        isNotNull("isNotNull"),
        equals("=="),
        notEquals("!=");

        private String value;

        ChkSymbolEnum(String desc) {
            this.value = desc;
        }

        public String value() {
            return value;
        }

    }

    public enum SrcType {
        text("text"),
        propFeed("propFeed"),
        propMain("propMain"),
        optionMain("optionMain"),
        optionPlatform("optionPlatform"),
        dict("dict");

        private String value;

        SrcType(String desc) {
            this.value = desc;
        }

        public String value() {
            return value;
        }

    }


}
