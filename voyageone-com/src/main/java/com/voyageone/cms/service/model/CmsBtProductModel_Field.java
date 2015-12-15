package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.*;

public class CmsBtProductModel_Field extends BaseMongoMap<Object, Object> {

    public String getCode() {
        return getAttribute("code");
    }

    public void setCode(String code) {
        setAttribute("code", code);
    }

    public String getName() {
        return getAttribute("name");
    }

    public void setName(String name) {
        setAttribute("name", name);
    }

    public String getColor() {
        return getAttribute("color");
    }

    public void setColor(String color) {
        setAttribute("color", color);
    }

    public String getOrigin() {
        return getAttribute("origin");
    }

    public void setOrigin(String origin) {
        setAttribute("origin", origin);
    }

    public int getYear() {
        return getAttribute("year");
    }

    public void setYear(int year) {
        setAttribute("year", year);
    }

    public String getSeason() {
        return getAttribute("season");
    }

    public void setSeason(String season) {
        setAttribute("season", season);
    }

    public String getMaterial1() {
        return getAttribute("material1");
    }

    public void setMaterial1(String material1) {
        setAttribute("material1", material1);
    }

    public String getMaterial2() {
        return getAttribute("material2");
    }

    public void setMaterial2(String material2) {
        setAttribute("material2", material2);
    }

    public String getMaterial3() {
        return getAttribute("material3");
    }

    public void setMaterial3(String material3) {
        setAttribute("material3", material3);
    }

    public double getMsrpStart() {
        return getAttribute("msrpStart");
    }

    public void setMsrpStart(double msrpStart) {
        setAttribute("msrpStart", msrpStart);
    }

    public double getMsrpEnd() {
        return getAttribute("msrpEnd");
    }

    public void setMsrpEnd(double msrpEnd) {
        setAttribute("msrpEnd", msrpEnd);
    }

    public double getRetailPriceStart() {
        return getAttribute("retailPriceStart");
    }

    public void setRetailPriceStart(double retailPriceStart) {
        setAttribute("retailPriceStart", retailPriceStart);
    }

    public double getRetailPriceEnd() {
        return getAttribute("retailPriceEnd");
    }

    public void setRetailPriceEnd(double retailPriceEnd) {
        setAttribute("retailPriceEnd", retailPriceEnd);
    }

    public double getSalePriceStart() {
        return getAttribute("salePriceStart");
    }

    public void setSalePriceStart(double salePriceStart) {
        setAttribute("salePriceStart", salePriceStart);
    }

    public double getSalePriceEnd() {
        return getAttribute("salePriceEnd");
    }

    public void setSalePriceEnd(double salePriceEnd) {
        setAttribute("salePriceEnd", salePriceEnd);
    }

    public double getCurrentPriceStart() {
        return getAttribute("currentPriceStart");
    }

    public void setCurrentPriceStart(double currentPriceStart) {
        setAttribute("currentPriceStart", currentPriceStart);
    }

    public double getCurrentPriceEnd() {
        return getAttribute("currentPriceEnd");
    }

    public void setCurrentPriceEnd(double currentPriceEnd) {
        setAttribute("currentPriceEnd", currentPriceEnd);
    }

    public String getStatus() {
        return getAttribute("status");
    }

    public void setStatus(String status) {
        setAttribute("status", status);
    }

    public String getBrand() {
        return getAttribute("brand");
    }

    public void setBrand(String brand) {
        setAttribute("brand", brand);
    }

    public String getSizeType() {
        return getAttribute("sizeType");
    }

    public void setSizeType(String sizeType) {
        setAttribute("sizeType", sizeType);
    }

    public int getInventory() {
        return getAttribute("inventory");
    }

    public void setInventory(int inventory) {
        setAttribute("inventory", inventory);
    }

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
                case CUSTOM_IMAGE3:
                    result = getImages3();
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
                case CUSTOM_IMAGE3:
                    setImages3(images);
                    break;
            }
        }
    }

    public List<CmsBtProductModel_Field_Image> getImages1() {
        if (!this.containsKey("images1") || getAttribute("images1") == null) {
            setAttribute("images1", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images1");
    }

    public void setImages1(List<CmsBtProductModel_Field_Image> images1) {
        setAttribute("images1", images1);
    }

    public List<CmsBtProductModel_Field_Image> getImages2() {
        if (!this.containsKey("images2") || getAttribute("images2") == null) {
            setAttribute("images2", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images2");
    }

    public void setImages2(List<CmsBtProductModel_Field_Image> images2) {
        setAttribute("images2", images2);
    }

    public List<CmsBtProductModel_Field_Image> getImages3() {
        if (!this.containsKey("images3") || getAttribute("images3") == null) {
            setAttribute("images3", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return getAttribute("images3");
    }

    public void setImages3(List<CmsBtProductModel_Field_Image> images3) {
        setAttribute("images3", images3);
    }


    public boolean getLock() {
        boolean result = false;
        Integer lock = (Integer)getAttribute("lock");
        if (lock != null && lock == 1) {
            result = true;
        }
        return result;
    }

    public void setLock(boolean lock) {
        int value = 0;
        if (lock) {
            value = 1;
        }
        setAttribute("lock", value);
    }

    public int getPriceChange() {
        return getAttribute("priceChange");
    }

    public void setPriceChange(int priceChange) {
        setAttribute("priceChange", priceChange);
    }

    @Override
    public Object put(Object key, Object value) {
        if (key != null && key.toString().startsWith("images")) {
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
        return value;
    }

}