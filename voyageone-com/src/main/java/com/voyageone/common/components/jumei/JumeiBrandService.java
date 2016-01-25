package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Bean.JmBrand;
import com.voyageone.common.components.jumei.base.JmBase;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JsonUtil;

import java.util.List;

/**
 * Created by chuanyu.laing on 2016/1/25.
 */
public class JumeiBrandService extends JmBase {

    private static List<JmBrand> brands = null;

    private static String BRAND_URL = "/v1/htBrand/query";
    /**
     * 初始化品牌
     */
    public void initBrands(ShopBean shopBean) throws Exception {
        String result = reqJmApi(shopBean, BRAND_URL);
        List<JmBrand> brandList = JsonUtil.jsonToBeanList(result, JmBrand.class);
        if (brandList != null) {
            brands = brandList;
        }
    }


    /**
     * 获取全部商品品牌
     */
    public List<JmBrand> getBrands(ShopBean shopBean) throws Exception {
        if (brands == null) {
            initBrands(shopBean);
        }
        return brands;
    }

    /**
     * 根据名称查找品牌ID
     */
    public JmBrand getBrandLikeName(ShopBean shopBean, String name) throws Exception {
        if (name == null) {
            return null;
        }

        if (brands == null) {
            initBrands(shopBean);
        }

        for (JmBrand brand : brands) {
            if (brand != null && brand.getName() != null
                    && brand.getName().startsWith(name)) {
                return brand;
            }
        }

        return null;
    }




}
