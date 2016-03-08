package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 的商品Model Group
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_Group extends BaseMongoMap<String, Object> {

    public Double getMsrpStart() {
        return convertToDoubel(getAttribute("msrpStart"));
    }

    public void setMsrpStart(Double msrpStart) {
        setAttribute("msrpStart", msrpStart);
    }

    public Double getMsrpEnd() {
        return convertToDoubel(getAttribute("msrpEnd"));
    }

    public void setMsrpEnd(Double msrpEnd) {
        setAttribute("msrpEnd", msrpEnd);
    }

    public Double getRetailPriceStart() {
        return convertToDoubel(getAttribute("retailPriceStart"));
    }

    public void setRetailPriceStart(Double retailPriceStart) {
        setAttribute("retailPriceStart", retailPriceStart);
    }

    public Double getRetailPriceEnd() {
        return convertToDoubel(getAttribute("retailPriceEnd"));
    }

    public void setRetailPriceEnd(Double retailPriceEnd) {
        setAttribute("retailPriceEnd", retailPriceEnd);
    }

    public Double getSalePriceStart() {
        return convertToDoubel(getAttribute("salePriceStart"));
    }

    public void setSalePriceStart(Double salePriceStart) {
        setAttribute("salePriceStart", salePriceStart);
    }

    public Double getSalePriceEnd() {
        return convertToDoubel(getAttribute("salePriceEnd"));
    }

    public void setSalePriceEnd(Double salePriceEnd) {
        setAttribute("salePriceEnd", salePriceEnd);
    }

    public Double getCurrentPriceStart() {
        return convertToDoubel(getAttribute("currentPriceStart"));
    }

    public void setCurrentPriceStart(Double currentPriceStart) {
        setAttribute("currentPriceStart", currentPriceStart);
    }

    public Double getCurrentPriceEnd() {
        return convertToDoubel(getAttribute("currentPriceEnd"));
    }

    public void setCurrentPriceEnd(Double currentPriceEnd) {
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

    public CmsBtProductModel_Group_Platform getPlatformByGroupId(Long groupId) {
        CmsBtProductModel_Group_Platform result = null;
        List<CmsBtProductModel_Group_Platform> platforms = getPlatforms();
        for (CmsBtProductModel_Group_Platform platform : platforms) {
            if (Objects.equals(platform.getGroupId(), groupId)) {
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
    public Object put(String key, Object value) {
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