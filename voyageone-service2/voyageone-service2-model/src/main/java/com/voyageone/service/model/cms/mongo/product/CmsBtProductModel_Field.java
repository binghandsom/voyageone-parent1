package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 的商品Model Fields
 *
 * @author edward.lin 2016/06/29
 * @version 2.2.0
 */
public class CmsBtProductModel_Field extends CmsBtProductModel_Images {

    //model 款号
    public String getModel() {
        return getStringAttribute("model");
    }

    public void setModel(String model) {
        setStringAttribute("model", model);
    }

    public String getLastReceivedOn() {
        return getStringAttribute("lastReceivedOn");
    }

    public void setLastReceivedOn(String lastReceivedOn) {
        setStringAttribute("lastReceivedOn", lastReceivedOn);
    }

    //code 产品code
    public String getCode() {
        return getStringAttribute("code");
    }

    public void setCode(String code) {
        setStringAttribute("code", code);
    }

    //originalCode 产品originalCode
    public String getOriginalCode() {
        return getStringAttribute("originalCode");
    }

    public void setOriginalCode(String originalCode) {
        setStringAttribute("originalCode", originalCode);
    }

    //brand 品牌
    public String getBrand() {
        return getStringAttribute("brand");
    }

    public void setBrand(String brand) {
        setStringAttribute("brand", brand);
    }

    //主数据平台是否为主商品
    public Integer getIsMasterMain() {
        return getIntAttribute("isMasterMain");
    }

    public void setIsMasterMain(Integer isMasterMain) {
        setAttribute("isMasterMain", isMasterMain == null ? 0 : isMasterMain);
    }

//    尺码表位置有人写错了， 导致这边用不了了， 所以删了（目前是在common下面的sizeChart）
//    //尺码表
//    public String getSizeChart() {
//        return getStringAttribute("sizeChart");
//    }
//    public void setSizeChart(String sizeChart) {
//        setStringAttribute("sizeChart", sizeChart);
//    }

    //产品名称（英文）
    public String getProductNameEn() {
        return getStringAttribute("productNameEn");
    }

    public void setProductNameEn(String productNameEn) {
        setStringAttribute("productNameEn", productNameEn);
    }

    //产品名称（原始中文标题）
    public String getOriginalTitleCn() {
        return getStringAttribute("originalTitleCn");
    }

    public void setOriginalTitleCn(String originalTitleCn) {
        setStringAttribute("originalTitleCn", originalTitleCn);
    }

    //shortDesEn 简短描述(英语)
    public String getShortDesEn() {
        return getStringAttribute("shortDesEn");
    }

    public void setShortDesEn(String shortDesEn) {
        setStringAttribute("shortDesEn", shortDesEn);
    }

    //shortDesCn 简短描述(中文)
    public String getShortDesCn() {
        return getStringAttribute("shortDesCn");
    }

    public void setShortDesCn(String shortDesCn) {
        setStringAttribute("shortDesCn", shortDesCn);
    }

    //longDesEn 详情描述(英语)
    public String getLongDesEn() {
        return getStringAttribute("longDesEn");
    }

    public void setLongDesEn(String longDesEn) {
        setStringAttribute("longDesEn", longDesEn);
    }

    //longDesCn 详情描述(中文)
    public String getLongDesCn() {
        return getStringAttribute("longDesCn");
    }

    public void setLongDesCn(String longDesCn) {
        setStringAttribute("longDesCn", longDesCn);
    }

    //materialEn
    public String getMaterialEn() {
        return getStringAttribute("materialEn");
    }

    public void setMaterialEn(String materialEn) {
        setStringAttribute("materialEn", materialEn);
    }

    //materialCn
    public String getMaterialCn() {
        return getStringAttribute("materialCn");
    }

    public void setMaterialCn(String materialCn) {
        setStringAttribute("materialCn", materialCn);
    }

    //color 颜色
    public String getColor() {
        return getStringAttribute("color");
    }

    public void setColor(String color) {
        setStringAttribute("color", color);
    }

    //origin 产地
    public String getOrigin() {
        return getStringAttribute("origin");
    }

    public void setOrigin(String origin) {
        setStringAttribute("origin", origin);
    }

    //产品分类(英文)
    public String getProductType() {
        return getStringAttribute("productType");
    }

    public void setProductType(String productType) {
        setStringAttribute("productType", productType);
    }

    //产品分类(中文)
    public String getProductTypeCn() {
        return getStringAttribute("productTypeCn");
    }

    public void setProductTypeCn(String productTypeCn) {
        setStringAttribute("productTypeCn", productTypeCn);
    }

    //feed原始产品分类(英文)
    public String getOrigProductType() {
        return getStringAttribute("origProductType");
    }

    public void setOrigProductType(String origProductType) {
        setStringAttribute("origProductType", origProductType);
    }

    //适合人群(英文)
    public String getSizeType() {
        return getStringAttribute("sizeType");
    }

    public void setSizeType(String sizeType) {
        setStringAttribute("sizeType", sizeType);
    }

    //适合人群(中文)
    public String getSizeTypeCn() {
        return getStringAttribute("sizeTypeCn");
    }

    public void setSizeTypeCn(String sizeTypeCn) {
        setStringAttribute("sizeTypeCn", sizeTypeCn);
    }

    //feed原始适合人群(英文)
    public String getOrigSizeType() {
        return getStringAttribute("origSizeType");
    }

    public void setOrigSizeType(String origSizeType) {
        setStringAttribute("origSizeType", origSizeType);
    }

    //hsCodeCrop 税号集货
    public String getHsCodeCrop() {
        return getStringAttribute("hsCodeCrop");
    }

    public void setHsCodeCrop(String hsCodeCrop) {
        setStringAttribute("hsCodeCrop", hsCodeCrop);
    }

    //hsCodePrivate 税号个人
    public String getHsCodePrivate() {
        return getStringAttribute("hsCodePrivate");
    }

    public void setHsCodePrivate(String hsCodePrivate) {
        setStringAttribute("hsCodePrivate", hsCodePrivate);
    }

    //hsCodeCross 税号跨境申报（10位）
    public String getHsCodeCross() {
        return getStringAttribute("hsCodeCross");
    }

    public void setHsCodeCross(String hsCodeCross) {
        setStringAttribute("hsCodeCross", hsCodeCross);
    }

    //产品库存
    public Integer getQuantity() {
        return getIntAttribute("quantity");
    }

    public void setQuantity(Integer quantity) {
        setAttribute("quantity", quantity == null ? 0 : quantity);
    }

    // clientProductUrl 官方网站链接
    public String getClientProductUrl() {
        return getStringAttribute("clientProductUrl");
    }

    public void setClientProductUrl(String clientProductUrl) {
        setStringAttribute("clientProductUrl", clientProductUrl);
    }

    public Integer getSkuCnt() {
        return getIntAttribute("skuCnt");
    }

    public void setSkuCnt(Integer skuCnt) {
        setAttribute("skuCnt", skuCnt == null ? 0 : skuCnt);
    }

    //msrp价格区间
    public Double getPriceMsrpSt() {
        return getDoubleAttribute("priceMsrpSt");
    }

    public void setPriceMsrpSt(Double priceMsrpSt) {
        setAttribute("priceMsrpSt", priceMsrpSt);
    }

    public Double getPriceMsrpEd() {
        return getDoubleAttribute("priceMsrpEd");
    }

    public void setPriceMsrpEd(Double priceMsrpEd) {
        setAttribute("priceMsrpEd", priceMsrpEd);
    }

    //建议市场价格区间
    public Double getPriceRetailSt() {
        return getDoubleAttribute("priceRetailSt");
    }

    public void setPriceRetailSt(Double priceRetailSt) {
        setAttribute("priceRetailSt", priceRetailSt);
    }

    public Double getPriceRetailEd() {
        return getDoubleAttribute("priceRetailEd");
    }

    public void setPriceRetailEd(Double priceRetailEd) {
        setAttribute("priceRetailEd", priceRetailEd);
    }

    //categoryStatus "0":未完成；"1":完成
    public String getCategoryStatus() {
        String categoryStatus = getStringAttribute("categoryStatus");
        return StringUtils.isEmpty(categoryStatus) ? "0" : categoryStatus;
    }

    public void setCategoryStatus(String categoryStatus) {
        setStringAttribute("categoryStatus", StringUtils.isEmpty(categoryStatus) ? "0" : categoryStatus);
    }

    //categorySetter
    public String getCategorySetter() {
        return getStringAttribute("categorySetter");
    }

    public void setCategorySetter(String categorySetter) {
        setStringAttribute("categorySetter", categorySetter);
    }

    // categorySetTime
    public String getCategorySetTime() {
        return getStringAttribute("categorySetTime");
    }

    public void setCategorySetTime(String categorySetTime) {
        setStringAttribute("categorySetTime", categorySetTime);
    }

    //translateStatus "0":未完成；"1":完成
    public String getTranslateStatus() {
        String translateStatus = getStringAttribute("translateStatus");
        return StringUtils.isEmpty(translateStatus) ? "0" : translateStatus;
    }

    public void setTranslateStatus(String translateStatus) {
        setStringAttribute("translateStatus", StringUtils.isEmpty(translateStatus) ? "0" : translateStatus);
    }

    //translator
    public String getTranslator() {
        return getStringAttribute("translator");
    }

    public void setTranslator(String translator) {
        setStringAttribute("translator", translator);
    }

    // translateTime
    public String getTranslateTime() {
        return getStringAttribute("translateTime");
    }

    public void setTranslateTime(String translateTime) {
        setStringAttribute("translateTime", translateTime);
    }

    //hsCodeStatus "0":未完成；"1":完成
    public String getHsCodeStatus() {
        String hsCodeStatus = getStringAttribute("hsCodeStatus");
        return StringUtils.isEmpty(hsCodeStatus) ? "0" : hsCodeStatus;
    }

    public void setHsCodeStatus(String hsCodeStatus) {
        setStringAttribute("hsCodeStatus", StringUtils.isEmpty(hsCodeStatus) ? "0" : hsCodeStatus);
    }

    //hsCodeSetter
    public String getHsCodeSetter() {
        return getStringAttribute("hsCodeSetter");
    }

    public void setHsCodeSetter(String hsCodeSetter) {
        setStringAttribute("hsCodeSetter", hsCodeSetter);
    }

    // translateTime
    public String getHsCodeSetTime() {
        return getStringAttribute("hsCodeSetTime");
    }

    public void setHsCodeSetTime(String hsCodeSetTime) {
        setStringAttribute("hsCodeSetTime", hsCodeSetTime);
    }

    // codeDiff
    public String getCodeDiff() {
        return getStringAttribute("codeDiff");
    }

    public void setCodeDiff(String codeDiff) {
        setStringAttribute("codeDiff", codeDiff);
    }

    // usageEn 使用方法英文
    public String getUsageEn() {
        return getStringAttribute("usageEn");
    }

    public void setUsageEn(String usageEn) {
        setStringAttribute("usageEn", usageEn);
    }

    // usageCn 使用方法中文
    public String getUsageCn() {
        return getStringAttribute("usageCn");
    }

    public void setUsageCn(String usageCn) {
        setStringAttribute("usageCn", usageCn);
    }

    // appSwitch
    public Integer getAppSwitch() {
        return getIntAttribute("appSwitch");
    }

    public void setAppSwitch(Integer appSwitch) {
        setAttribute("appSwitch", appSwitch == null ? 0 : appSwitch);
    }

    //VO佣金费率,商品级
    public Double getCommissionRate() {
        return getDoubleAttribute("commissionRate");
    }

    public void setCommissionRate(Double commissionRate) {
        setAttribute("commissionRate", commissionRate);
    }

    //磅
    public Double getWeightLb() {
        return getDoubleAttribute("weightLB");
    }

    public void setWeightLb(Double weightLB) {
        setAttribute("weightLB", weightLB == null ? 0.0 : weightLB);
    }

    //克
    public Integer getWeightG() {
        return getIntAttribute("weightG");
    }

    public void setWeightG(Integer weightG) {
        setAttribute("weightG", weightG == null ? 0 : weightG);
    }

    //千克
    public Double getWeightKG() {
        return getDoubleAttribute("weightKG");
    }

    public void setWeightKG(Double weightKG) {
        setAttribute("weightKG", weightKG == null ? 0.0 : weightKG);
    }

    //->客户建议零售价(double)
    public String getClientMsrpPrice() {
        return getStringAttribute("clientMsrpPrice");
    }

    public void setClientMsrpPrice(String clientMsrpPrice) {
        setAttribute("clientMsrpPrice", clientMsrpPrice);
    }

    //->客户成本价(double)
    public String getClientNetPrice() {
        return getStringAttribute("clientNetPrice");
    }

    public void setClientNetPrice(String clientNetPrice) {
        setAttribute("clientNetPrice", clientNetPrice);
    }


    @Override
    public Object put(String key, Object value) {
        if (key != null && key.startsWith("images")) {
            if (value != null) {
                List<Map> imageMaps = (List<Map>) value;
                List<CmsBtProductModel_Field_Image> images = new ArrayList<>();
                for (Map map : imageMaps) {
                    if (map != null) {
                        CmsBtProductModel_Field_Image image = new CmsBtProductModel_Field_Image(map);
                        images.add(image);
                    }
                }
                value = images;
            }
        }
        return super.put(key, value);
    }
}