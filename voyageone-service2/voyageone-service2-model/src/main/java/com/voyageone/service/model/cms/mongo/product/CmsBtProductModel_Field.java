package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.cms.CmsConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 的商品Model Fields
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_Field extends BaseMongoMap<String, Object> {

    //code 产品code
    public String getCode() {
        return getAttribute("code");
    }

    public void setCode(String code) {
        setAttribute("code", code);
    }

    //brand 品牌
    public String getBrand() {
        return getAttribute("brand");
    }

    public void setBrand(String brand) {
        setAttribute("brand", brand);
    }

    //产品名称（英文）
    public String getProductNameEn() {
        return getAttribute("productNameEn");
    }

    public void setProductNameEn(String productNameEn) {
        setAttribute("productNameEn", productNameEn);
    }

    //产品名称（中文）
    public String getProductNameCn() {
        return getAttribute("productNameCn");
    }

    public void setProductNameCn(String productNameCn) {
        setAttribute("productNameCn", productNameCn);
    }

    //产品名称（原始中文标题）
    public String getOriginalTitleCn() {
        return getAttribute("originalTitleCn");
    }

    public void setOriginalTitleCn(String originalTitleCn) {
        setAttribute("originalTitleCn", originalTitleCn);
    }

    //longTitle 长标题
    public String getLongTitle() {
        return getAttribute("longTitle");
    }

    public void setLongTitle(String longTitle) {
        setAttribute("longTitle", longTitle);
    }

    //middleTitle 中标题
    public String getMiddleTitle() {
        return getAttribute("middleTitle");
    }

    public void setMiddleTitle(String middleTitle) {
        setAttribute("middleTitle", middleTitle);
    }

    //shortTitle 短标题
    public String getShortTitle() {
        return getAttribute("shortTitle");
    }

    public void setShortTitle(String shortTitle) {
        setAttribute("shortTitle", shortTitle);
    }

    //model 款号
    public String getModel() {
        return getAttribute("model");
    }

    public void setModel(String model) {
        setAttribute("model",model);
    }

    //color 颜色
    public String getColor() {
        return getAttribute("color");
    }

    public void setColor(String color) {
        setAttribute("color", color);
    }

    //origin 产地
    public String getOrigin() {
        return getAttribute("origin");
    }

    public void setOrigin(String origin) {
        setAttribute("origin", origin);
    }

    //originalDesCn 原始中文描述
    public String getOriginalDesCn() {
        return getAttribute("originalDesCn");
    }

    public void setOriginalDesCn(String originalDesCn) {
        setAttribute("originalDesCn", originalDesCn);
    }

    //shortDesCn 简短描述中文
    public String getShortDesCn() {
        return getAttribute("shortDesCn");
    }

    public void setShortDesCn(String shortDesCn) {
        setAttribute("shortDesCn", shortDesCn);
    }

    //longDesCn 详情描述中文
    public String getLongDesCn() {
        return getAttribute("longDesCn");
    }

    public void setLongDesCn(String longDesCn) {
        setAttribute("longDesCn", longDesCn);
    }

    //shortDesEn 简短描述英语
    public String getShortDesEn() {
        return getAttribute("shortDesEn");
    }

    public void setShortDesEn(String shortDesEn) {
        setAttribute("shortDesEn", shortDesEn);
    }

    //longDesEn 详情描述英语
    public String getLongDesEn() {
        return getAttribute("longDesEn");
    }

    public void setLongDesEn(String longDesEn) {
        setAttribute("longDesEn", longDesEn);
    }

    //hsCodeCrop 税号集货
    public String getHsCodeCrop() {
        return getAttribute("hsCodeCrop");
    }

    public void setHsCodeCrop(String hsCodeCrop) {
        setAttribute("hsCodeCrop", hsCodeCrop);
    }

    //hsCodePrivate 税号个人
    public String getHsCodePrivate() {
        return getAttribute("hsCodePrivate");
    }

    public void setHsCodePrivate(String hsCodePrivate) {
        setAttribute("hsCodePrivate", hsCodePrivate);
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

    //销售价格价格区间
    public Double getPriceSaleSt() {
        return getDoubleAttribute("priceSaleSt");
    }
    public void setPriceSaleSt(Double priceSaleSt) {
        setAttribute("priceSaleSt", priceSaleSt);
    }
    public Double getPriceSaleEd() {
        return getDoubleAttribute("priceSaleEd");
    }
    public void setPriceSaleEd(Double priceSaleEd) {
        setAttribute("priceSaleEd", priceSaleEd);
    }

    //当前销售价格价格区间 暂时不使用
    public Double getCurPriceSt() {
        return getDoubleAttribute("curPriceSt");
    }
    public void setCurPriceSt(Double curPriceSt) {
        setAttribute("curPriceSt", curPriceSt);
    }
    public Double getCurPriceEd() {
        return getDoubleAttribute("curPriceSt");
    }
    public void setCurPriceEd(Double curPriceSt) {
        setAttribute("curPriceSt", curPriceSt);
    }

    //priceChange 价格审批flg 0:变更（未审批） 1:审批完成   Feed过来的新商品，就认为审批完成
    public Integer getPriceChange() {
        return getAttribute("priceChange");
    }

    public void setPriceChange(Integer priceChange) {
        setAttribute("priceChange", priceChange);
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
            }
        }
    }

    //商品图片
    public List<CmsBtProductModel_Field_Image> getImages1() {
        if (!this.containsKey("images1") || getAttribute("images1") == null) {
            setAttribute("images1", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images1");
    }
    public void setImages1(List<CmsBtProductModel_Field_Image> images1) {
        setAttribute("images1", images1);
    }

    //包装图片
    public List<CmsBtProductModel_Field_Image> getImages2() {
        if (!this.containsKey("images2") || getAttribute("images2") == null) {
            setAttribute("images2", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images2");
    }
    public void setImages2(List<CmsBtProductModel_Field_Image> images2) {
        setAttribute("images2", images2);
    }

    //带角度图片
    public List<CmsBtProductModel_Field_Image> getImages3() {
        if (!this.containsKey("images3") || getAttribute("images3") == null) {
            setAttribute("images3", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images3");
    }
    public void setImages3(List<CmsBtProductModel_Field_Image> images3) {
        setAttribute("images3", images3);
    }

    //自定义图片
    public List<CmsBtProductModel_Field_Image> getImages4() {
        if (!this.containsKey("images4") || getAttribute("images4") == null) {
            setAttribute("images4", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images4");
    }
    public void setImages4(List<CmsBtProductModel_Field_Image> images4) {
        setAttribute("images4", images4);
    }

    //lock商品
    public String getLock() {
        Object lock = getAttribute("lock");
        if (lock == null) {
            return "";
        }
        return lock.toString();
    }

    public void setLock(String lock) {
        if (lock == null) {
            lock = "";
        }
        setAttribute("lock", lock);
    }

    //状态 new/pending/ready/approved/deleted
    public String getStatus() {
        return getAttribute("status");
    }
    public void setStatus(String status) {
        setAttribute("status", status);
    }
    public void setStatus(CmsConstants.ProductStatus status) {
        String value = null;
        if (status != null) {
            value = status.toString();
        }
        setAttribute("status", value);
    }

    //translateStatus "0":未完成；"1":完成
    public String getTranslateStatus() {
        return getAttribute("translateStatus");
    }
    public void setTranslateStatus(String status) {
        setAttribute("translateStatus", status);
    }

    public String getTranslator() {
        return getAttribute("translator");
    }
    public void setTranslator(String translator) {
        setAttribute("translator", translator);
    }

    public String getTranslateTime() {
        return getAttribute("translateTime");
    }
    public void setTranslateTime(String translateTime) {
        setAttribute("translateTime", translateTime);
    }


    //editStatus "0":未完成；"1":完成
    public String getEditStatus() {
        return getAttribute("editStatus");
    }
    public void setEditStatus(String editStatus) {
        setAttribute("editStatus", editStatus);
    }

    //适合人群
    public String getSizeType() {
        return getAttribute("sizeType");
    }
    public void setSizeType(String sizeType) {
        setAttribute("sizeType", sizeType);
    }

    //产品分类
    public String getProductType() {
        return getAttribute("productType");
    }
    public void setProductType(String productType) {
        setAttribute("productType", productType);
    }

    //产品库存
    public Integer getQuantity() {
        return getAttribute("quantity");
    }
    public void setQuantity(Integer quantity) {
        setAttribute("quantity", quantity);
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