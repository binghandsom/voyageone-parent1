package com.voyageone.components.solr.utils;

import java.util.HashMap;
import java.util.Map;

public class CmsProductBrandConvertUtils {

    private static Map<String, String> brandConvertMap = new HashMap<>();

    static {
        brandConvertMap.put("bcbg", "bcbgmaxazria");
        brandConvertMap.put("gucci clothing & accessories", "gucci");
    }


    public static String convertBrand(String brand) {
        if (brand != null) {
            String result = brandConvertMap.get(brand.toLowerCase());
            if (result != null) {
                return result;
            }
        }
        return brand;
    }

}
