package com.voyageone.batch.ims.modelbean;

import java.util.ArrayList;
import java.util.List;

public class PlatformPropBean {
    // 第三方平台类目的属性的hash
    private String platformPropHash;
    // 第三方平台渠道id（天猫，天猫国际，京东，京东国际之类的）
    private int platformCartId;
    // 第三方平台的类目id
    private String platformCid;
    // 第三方平台的属性id
    private String platformPropId;
    // 第三方平台的属性路径
    private String platformPropPath;
    // 第三方平台的属性名称
    private String platformPropName;
    // 第三方平台的属性类型
    private int platformPropType;
    // 第三方平台的属性默认值
    private String platformPropValueDefault;
    // 第三方平台的属性的父属性id
    private String parentPropHash;
    // 是否是顶层属性
    private int isTopProp;
    // 是否是父亲（是否包含子属性）
    private int isParent;
    // 是否是产品（产品：1； 商品：0）
    private int isProduct;
    // 预留
    private String content;

    // 可选项列表
    private List<PlatformPropOptionBean> platformPropOptionBeanList = new ArrayList<>();
    // 规则列表
    private List<PlatformPropRuleBean> platformPropRuleBeanList = new ArrayList<>();

    public String getPlatformPropHash() {
        return platformPropHash;
    }

    public void setPlatformPropHash(String platformPropHash) {
        this.platformPropHash = platformPropHash;
    }

    public int getPlatformCartId() {
        return platformCartId;
    }

    public void setPlatformCartId(int platformCartId) {
        this.platformCartId = platformCartId;
    }

    public String getPlatformCid() {
        return platformCid;
    }

    public void setPlatformCid(String platformCid) {
        this.platformCid = platformCid;
    }

    public String getPlatformPropId() {
        return platformPropId;
    }

    public void setPlatformPropId(String platformPropId) {
        this.platformPropId = platformPropId;
    }

    public String getPlatformPropPath() {
        return platformPropPath;
    }

    public void setPlatformPropPath(String platformPropPath) {
        this.platformPropPath = platformPropPath;
    }

    public String getPlatformPropName() {
        return platformPropName;
    }

    public void setPlatformPropName(String platformPropName) {
        this.platformPropName = platformPropName;
    }

    public int getPlatformPropType() {
        return platformPropType;
    }

    public void setPlatformPropType(int platformPropType) {
        this.platformPropType = platformPropType;
    }

    public String getPlatformPropValueDefault() {
        return platformPropValueDefault;
    }

    public void setPlatformPropValueDefault(String platformPropValueDefault) {
        this.platformPropValueDefault = platformPropValueDefault;
    }

    public String getParentPropHash() {
        return parentPropHash;
    }

    public void setParentPropHash(String parentPropHash) {
        this.parentPropHash = parentPropHash;
    }

    public int getIsTopProp() {
        return isTopProp;
    }

    public void setIsTopProp(int isTopProp) {
        this.isTopProp = isTopProp;
    }

    public int getIsParent() {
        return isParent;
    }

    public void setIsParent(int isParent) {
        this.isParent = isParent;
    }

    public int getIsProduct() {
        return isProduct;
    }

    public void setIsProduct(int isProduct) {
        this.isProduct = isProduct;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<PlatformPropOptionBean> getPlatformPropOptionBeanList() {
        return platformPropOptionBeanList;
    }

    public void setPlatformPropOptionBeanList(List<PlatformPropOptionBean> platformPropOptionBeanList) {
        this.platformPropOptionBeanList = platformPropOptionBeanList;
    }

    public List<PlatformPropRuleBean> getPlatformPropRuleBeanList() {
        return platformPropRuleBeanList;
    }

    public void setPlatformPropRuleBeanList(List<PlatformPropRuleBean> platformPropRuleBeanList) {
        this.platformPropRuleBeanList = platformPropRuleBeanList;
    }
}
