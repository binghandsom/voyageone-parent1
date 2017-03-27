package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
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
public class CmsBtProductModel_Field extends BaseMongoMap<String, Object> {

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

    public String getMpn() {
        return getStringAttribute("mpn");
    }

    public void setMpn(String mpn) {
        setStringAttribute("mpn", mpn);
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

    //商品图片
    public List<CmsBtProductModel_Field_Image> getImages(CmsBtProductConstants.FieldImageType imageType) {
        List<CmsBtProductModel_Field_Image> result = null;
        if (imageType != null) {
            switch (imageType) {
                case PRODUCT_IMAGE:
                    result = getImages1();
                    break;
                case PACKAGE_IMAGE:
                    result = getImages2();
                    break;
                case ANGLE_IMAGE:
                    result = getImages3();
                    break;
                case CUSTOM_IMAGE:
                    result = getImages4();
                    break;
                case MOBILE_CUSTOM_IMAGE:
                    result = getImages5();
                    break;
                case CUSTOM_PRODUCT_IMAGE:
                    result = getImages6();
                    break;
                case M_CUSTOM_PRODUCT_IMAGE:
                    result = getImages7();
                    break;
                case HANG_TAG_IMAGE:
                    result = getImages8();
                    break;
                case DURABILITY_TAG_IMAGE:
                    result = getImages9();
                    break;
            }
        }
        return result;
    }

    public void setImages(CmsBtProductConstants.FieldImageType imageType, List<CmsBtProductModel_Field_Image> images) {
        if (imageType != null) {
            switch (imageType) {
                case PRODUCT_IMAGE:
                    setImages1(images);
                    break;
                case PACKAGE_IMAGE:
                    setImages2(images);
                    break;
                case ANGLE_IMAGE:
                    setImages3(images);
                    break;
                case CUSTOM_IMAGE:
                    setImages4(images);
                    break;
                case MOBILE_CUSTOM_IMAGE:
                    setImages5(images);
                    break;
                case CUSTOM_PRODUCT_IMAGE:
                    setImages6(images);
                    break;
                case M_CUSTOM_PRODUCT_IMAGE:
                    setImages7(images);
                    break;
                case HANG_TAG_IMAGE:
                    setImages8(images);
                    break;
                case DURABILITY_TAG_IMAGE:
                    setImages9(images);
                    break;
            }
        }
    }

    //商品图片
    public List<CmsBtProductModel_Field_Image> getImages1() {
        if (!this.containsKey("images1") || getStringAttribute("images1") == null) {
            setAttribute("images1", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images1");
    }

    public void setImages1(List<CmsBtProductModel_Field_Image> images1) {
        setAttribute("images1", images1);
    }

    //包装图片
    public List<CmsBtProductModel_Field_Image> getImages2() {
        if (!this.containsKey("images2") || getStringAttribute("images2") == null) {
            setAttribute("images2", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images2");
    }

    public void setImages2(List<CmsBtProductModel_Field_Image> images2) {
        setAttribute("images2", images2);
    }

    //带角度图片
    public List<CmsBtProductModel_Field_Image> getImages3() {
        if (!this.containsKey("images3") || getStringAttribute("images3") == null) {
            setAttribute("images3", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images3");
    }

    public void setImages3(List<CmsBtProductModel_Field_Image> images3) {
        setAttribute("images3", images3);
    }

    //自定义图片
    public List<CmsBtProductModel_Field_Image> getImages4() {
        if (!this.containsKey("images4") || getStringAttribute("images4") == null) {
            setAttribute("images4", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images4");
    }

    public void setImages4(List<CmsBtProductModel_Field_Image> images4) {
        setAttribute("images4", images4);
    }

    //手机端自定义图片
    public List<CmsBtProductModel_Field_Image> getImages5() {
        if (!this.containsKey("images5") || getStringAttribute("images5") == null) {
            setAttribute("images5", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images5");
    }

    public void setImages5(List<CmsBtProductModel_Field_Image> images5) {
        setAttribute("images5", images5);
    }

    //商品自定义图片
    public List<CmsBtProductModel_Field_Image> getImages6() {
        if (!this.containsKey("images6") || getStringAttribute("images6") == null) {
            setAttribute("images6", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images6");
    }

    public void setImages6(List<CmsBtProductModel_Field_Image> images6) {
        setAttribute("images6", images6);
    }

    //商品自定义M_CUSTOM_PRODUCT_IMAGE
    public List<CmsBtProductModel_Field_Image> getImages7() {
        if (!this.containsKey("images7") || getStringAttribute("images7") == null) {
            setAttribute("images7", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images7");
    }

    public void setImages7(List<CmsBtProductModel_Field_Image> images7) {
        setAttribute("images7", images7);
    }

    //商品自定义HANG_TAG_IMAGE
    public List<CmsBtProductModel_Field_Image> getImages8() {
        if (!this.containsKey("images8") || getStringAttribute("images8") == null) {
            setAttribute("images8", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images8");
    }

    public void setImages8(List<CmsBtProductModel_Field_Image> images8) {
        setAttribute("images8", images8);
    }

    public List<CmsBtProductModel_Field_Image> getImages9() {
        if (!this.containsKey("images9") || getStringAttribute("images9") == null) {
            setAttribute("images9", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images9");
    }

    public void setImages9(List<CmsBtProductModel_Field_Image> images9) {
        setAttribute("images9", images9);
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