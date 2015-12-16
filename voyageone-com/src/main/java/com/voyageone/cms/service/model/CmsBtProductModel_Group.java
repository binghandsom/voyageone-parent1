package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 的商品Model Group
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_Group extends BaseMongoMap<Object, Object> {

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

    public List<CmsBtProductModel_Group_Platform> getPlatforms() {
        if (!this.containsKey("platforms") || getAttribute("platforms") == null) {
            setAttribute("platforms", new ArrayList<CmsBtProductModel_Group_Platform>());
        }
        return (List<CmsBtProductModel_Group_Platform>) getAttribute("platforms");
    }

    public void setPlatforms(List<CmsBtProductModel_Group_Platform> platforms) {
        setAttribute("platforms", platforms);
    }

    public CmsBtProductModel_Group_Platform getPlatformByGroupId(int groupId) {
        CmsBtProductModel_Group_Platform result = null;
        List<CmsBtProductModel_Group_Platform> platforms = getPlatforms();
        for (CmsBtProductModel_Group_Platform platform : platforms) {
            if (platform.getGroupId() == groupId) {
                result = platform;
                break;
            }
        }
        return  result;
    }

    public CmsBtProductModel_Group_Platform getPlatformByCartId(int cartId) {
        CmsBtProductModel_Group_Platform result = null;
        List<CmsBtProductModel_Group_Platform> platforms = getPlatforms();
        for (CmsBtProductModel_Group_Platform platform : platforms) {
            if (platform.getCartId() == cartId) {
                result = platform;
                break;
            }
        }
        return  result;
    }

    @Override
    public Object put(Object key, Object value) {
        if ("platforms".equals(key)) {
            List<Map> platformMaps = (List<Map>)value;
            if (platformMaps != null) {
                List<CmsBtProductModel_Group_Platform> platforms = new ArrayList<>();
                for (Map map : platformMaps) {
                    if (map != null) {
                        CmsBtProductModel_Group_Platform platform = new CmsBtProductModel_Group_Platform(map);
                        platforms.add(platform);
                    }
                }
                value = platforms;
            }
        }
        return super.put(key, value);
    }

}