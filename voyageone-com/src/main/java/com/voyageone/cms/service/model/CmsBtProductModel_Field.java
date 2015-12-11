package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.*;

public class CmsBtProductModel_Field extends BaseMongoMap {
    public CmsBtProductModel_Field() {
        if (!this.containsKey("images1")) {
            setAttribute("images1", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        if (!this.containsKey("images2")) {
            setAttribute("images2", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        if (!this.containsKey("images3")) {
            setAttribute("images3", new ArrayList<CmsBtProductModel_Field_Image>());
        }
    }

    public String getCode() {
        return (String) getAttribute("code");
    }

    public void setCode(String code) {
        setAttribute("code", code);
    }

    public String getName() {
        return (String) getAttribute("name");
    }

    public void setName(String name) {
        setAttribute("name", name);
    }

    public String getColor() {
        return (String) getAttribute("color");
    }

    public void setColor(String color) {
        setAttribute("color", color);
    }

    public String getOrigin() {
        return (String) getAttribute("origin");
    }

    public void setOrigin(String origin) {
        setAttribute("origin", origin);
    }

    public int getYear() {
        return (Integer) getAttribute("year");
    }

    public void setYear(int year) {
        setAttribute("year", year);
    }

    public String getSeason() {
        return (String) getAttribute("season");
    }

    public void setSeason(String season) {
        setAttribute("season", season);
    }

    public String getMaterial1() {
        return (String) getAttribute("material1");
    }

    public void setMaterial1(String material1) {
        setAttribute("material1", material1);
    }

    public String getMaterial2() {
        return (String) getAttribute("material2");
    }

    public void setMaterial2(String material2) {
        setAttribute("material2", material2);
    }

    public String getMaterial3() {
        return (String) getAttribute("material3");
    }

    public void setMaterial3(String material3) {
        setAttribute("material3", material3);
    }

    public double getMsrpStart() {
        return (Double) getAttribute("msrpStart");
    }

    public void setMsrpStart(double msrpStart) {
        setAttribute("msrpStart", msrpStart);
    }

    public double getMsrpEnd() {
        return (Double) getAttribute("msrpEnd");
    }

    public void setMsrpEnd(double msrpEnd) {
        setAttribute("msrpEnd", msrpEnd);
    }

    public double getRetailPriceStart() {
        return (Double) getAttribute("retailPriceStart");
    }

    public void setRetailPriceStart(double retailPriceStart) {
        setAttribute("retailPriceStart", retailPriceStart);
    }

    public double getRetailPriceEnd() {
        return (Double) getAttribute("retailPriceEnd");
    }

    public void setRetailPriceEnd(double retailPriceEnd) {
        setAttribute("retailPriceEnd", retailPriceEnd);
    }

    public double getSalePriceStart() {
        return (Double) getAttribute("salePriceStart");
    }

    public void setSalePriceStart(double salePriceStart) {
        setAttribute("salePriceStart", salePriceStart);
    }

    public double getSalePriceEnd() {
        return (Double) getAttribute("salePriceEnd");
    }

    public void setSalePriceEnd(double salePriceEnd) {
        setAttribute("salePriceEnd", salePriceEnd);
    }

    public double getCurrentPriceStart() {
        return (Double) getAttribute("currentPriceStart");
    }

    public void setCurrentPriceStart(double currentPriceStart) {
        setAttribute("currentPriceStart", currentPriceStart);
    }

    public double getCurrentPriceEnd() {
        return (Double) getAttribute("currentPriceEnd");
    }

    public void setCurrentPriceEnd(double currentPriceEnd) {
        setAttribute("currentPriceEnd", currentPriceEnd);
    }

    public String getStatus() {
        return (String) getAttribute("status");
    }

    public void setStatus(String status) {
        setAttribute("status", status);
    }

    public String getBrand() {
        return (String) getAttribute("brand");
    }

    public void setBrand(String brand) {
        setAttribute("brand", brand);
    }

    public String getSizeType() {
        return (String) getAttribute("sizeType");
    }

    public void setSizeType(String sizeType) {
        setAttribute("sizeType", sizeType);
    }

    public int getInventory() {
        return (Integer) getAttribute("inventory");
    }

    public void setInventory(int inventory) {
        setAttribute("inventory", inventory);
    }

    public List<CmsBtProductModel_Field_Image> getImages(CmsBtProductConstants.FieldImageType imageType) {
        List<CmsBtProductModel_Field_Image> result = null;
        if (imageType != null) {
            switch(imageType) {
                case IMAGE1:
                    result = getImages1();
                    break;
                case IMAGE2:
                    result = getImages2();
                    break;
                case IMAGE3:
                    result = getImages3();
                    break;
            }
        }
        return result;
    }

    public void setImages(CmsBtProductConstants.FieldImageType imageType, List<CmsBtProductModel_Field_Image> images) {
        if (imageType != null) {
            switch(imageType) {
                case IMAGE1:
                    setImages1(images);
                    break;
                case IMAGE2:
                    setImages2(images);
                    break;
                case IMAGE3:
                    setImages3(images);
                    break;
            }
        }
    }

    public List<CmsBtProductModel_Field_Image> getImages1() {
        if (!this.containsKey("images1") || getAttribute("images1") == null) {
            setAttribute("images1", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return (List<CmsBtProductModel_Field_Image>) getAttribute("images1");
    }

    public void setImages1(List<CmsBtProductModel_Field_Image> images1) {
        setAttribute("images1", images1);
    }

    public List<CmsBtProductModel_Field_Image> getImages2() {
        if (!this.containsKey("images2") || getAttribute("images2") == null) {
            setAttribute("images2", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return (List<CmsBtProductModel_Field_Image>) getAttribute("images2");
    }

    public void setImages2(List<CmsBtProductModel_Field_Image> images2) {
        setAttribute("images2", images2);
    }

    public List<CmsBtProductModel_Field_Image> getImages3() {
        if (!this.containsKey("images3") || getAttribute("images3") == null) {
            setAttribute("images3", new ArrayList<CmsBtProductModel_Field_Image>());
        }
        return (List<CmsBtProductModel_Field_Image>) getAttribute("images3");
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
        return (Integer) getAttribute("priceChange");
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
        return super.put(key, value);
    }

}