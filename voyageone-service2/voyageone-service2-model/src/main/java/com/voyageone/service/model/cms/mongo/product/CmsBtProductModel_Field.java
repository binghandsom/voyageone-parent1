package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 的商品Model Fields
 * @author edward.lin 2016/06/29
 * @version 2.2.0
 */
public class CmsBtProductModel_Field extends BaseMongoMap<String, Object> {

    //model 款号
    public String getModel() {
        return getStringAttribute("model");
    }
    public void setModel(String model) {
        setStringAttribute("model",model);
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

    //尺码表
    public String getSizeChart() {
        return getStringAttribute("sizeChart");
    }
    public void setSizeChart(String sizeChart) {
        setStringAttribute("sizeChart", sizeChart);
    }

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

    //产品分类
    public String getProductType() {
        return getStringAttribute("productType");
    }
    public void setProductType(String productType) {
        setStringAttribute("productType", productType);
    }

    //适合人群
    public String getSizeType() {
        return getStringAttribute("sizeType");
    }
    public void setSizeType(String sizeType) {
        setStringAttribute("sizeType", sizeType);
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
        setStringAttribute("quantity", quantity == null ? 0 : quantity);
    }

    // clientProductUrl 官方网站链接
    public String getClientProductUrl() {
        return getStringAttribute("clientProductUrl");
    }
    public void setClientProductUrl(String clientProductUrl) {
        setStringAttribute("clientProductUrl", clientProductUrl);
    }

    //商品图片
    public List<CmsBtProductModel_Field_Image> getImages(CmsBtProductConstants.FieldImageType imageType) {
        List<CmsBtProductModel_Field_Image> result = null;
        if (imageType != null) {
            switch(imageType) {
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
            }
        }
        return result;
    }
    public void setImages(CmsBtProductConstants.FieldImageType imageType, List<CmsBtProductModel_Field_Image> images) {
        if (imageType != null) {
            switch(imageType) {
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
        setStringAttribute("images1", images1);
    }

    //包装图片
    public List<CmsBtProductModel_Field_Image> getImages2() {
        if (!this.containsKey("images2") || getStringAttribute("images2") == null) {
            setAttribute("images2", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images2");
    }
    public void setImages2(List<CmsBtProductModel_Field_Image> images2) {
        setStringAttribute("images2", images2);
    }

    //带角度图片
    public List<CmsBtProductModel_Field_Image> getImages3() {
        if (!this.containsKey("images3") || getStringAttribute("images3") == null) {
            setAttribute("images3", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images3");
    }
    public void setImages3(List<CmsBtProductModel_Field_Image> images3) {
        setStringAttribute("images3", images3);
    }

    //自定义图片
    public List<CmsBtProductModel_Field_Image> getImages4() {
        if (!this.containsKey("images4") || getStringAttribute("images4") == null) {
            setAttribute("images4", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images4");
    }
    public void setImages4(List<CmsBtProductModel_Field_Image> images4) {
        setStringAttribute("images4", images4);
    }

    //手机端自定义图片
    public List<CmsBtProductModel_Field_Image> getImages5() {
        if (!this.containsKey("images5") || getStringAttribute("images5") == null) {
            setAttribute("images5", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images5");
    }
    public void setImages5(List<CmsBtProductModel_Field_Image> images5) {
        setStringAttribute("images5", images5);
    }

    //商品自定义图片
    public List<CmsBtProductModel_Field_Image> getImages6() {
        if (!this.containsKey("images6") || getStringAttribute("images6") == null) {
            setAttribute("images6", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images6");
    }
    public void setImages6(List<CmsBtProductModel_Field_Image> images6) {
        setStringAttribute("images6", images6);
    }

    //商品自定义M_CUSTOM_PRODUCT_IMAGE
    public List<CmsBtProductModel_Field_Image> getImages7() {
        if (!this.containsKey("images7") || getStringAttribute("images7") == null) {
            setAttribute("images7", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images7");
    }
    public void setImages7(List<CmsBtProductModel_Field_Image> images7) {
        setStringAttribute("images7", images7);
    }

    //商品自定义HANG_TAG_IMAGE
    public List<CmsBtProductModel_Field_Image> getImages8() {
        if (!this.containsKey("images8") || getStringAttribute("images8") == null) {
            setAttribute("images8", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images8");
    }
    public void setImages8(List<CmsBtProductModel_Field_Image> images8) {
        setStringAttribute("images8", images8);
    }

    //msrp价格区间
    public Double getPriceMsrpSt() {
        return getDoubleAttribute("priceMsrpSt");
    }
    public void setPriceMsrpSt(Double priceMsrpSt) {
        setStringAttribute("priceMsrpSt", priceMsrpSt);
    }
    public Double getPriceMsrpEd() {
        return getDoubleAttribute("priceMsrpEd");
    }
    public void setPriceMsrpEd(Double priceMsrpEd) {
        setStringAttribute("priceMsrpEd", priceMsrpEd);
    }

    //建议市场价格区间
    public Double getPriceRetailSt() {
        return getDoubleAttribute("priceRetailSt");
    }
    public void setPriceRetailSt(Double priceRetailSt) {
        setStringAttribute("priceRetailSt", priceRetailSt);
    }
    public Double getPriceRetailEd() {
        return getDoubleAttribute("priceRetailEd");
    }
    public void setPriceRetailEd(Double priceRetailEd) {
        setStringAttribute("priceRetailEd", priceRetailEd);
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

    // tmallWirelessActive
    public Integer getTmallWirelessActive() {
        return getIntAttribute("tmallWirelessActive");
    }
    public void setTmallWirelessActive(Integer tmallWirelessActive) {
        setStringAttribute("tmallWirelessActive", tmallWirelessActive == null ? 0 : tmallWirelessActive);
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