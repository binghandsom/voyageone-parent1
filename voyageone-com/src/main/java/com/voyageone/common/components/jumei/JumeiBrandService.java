package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Bean.JmBrandBean;
import com.voyageone.common.components.jumei.base.JmBase;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JacksonUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuanyu.laing on 2016/1/25.
 */
@Service
public class JumeiBrandService extends JmBase {

    private List<JmBrandBean> brands = null;

    private static String BRAND_URL = "v1/htBrand/query";
    /**
     * 初始化品牌
     */
    public void initBrands(ShopBean shopBean) throws Exception {
        String result = reqJmApi(shopBean, BRAND_URL);
        List<JmBrandBean> brandList = JacksonUtil.jsonToBeanList(result, JmBrandBean.class);
        if (brandList != null) {
            brands = brandList;
        } else {
            brands = new ArrayList<>();
        }
    }

    /**
     * 获取全部商品品牌
     */
    public List<JmBrandBean> getBrands(ShopBean shopBean) throws Exception {
        if (brands == null) {
            initBrands(shopBean);
        }
        return brands;
    }

    /**
     * 根据名称查找品牌Model
     */
    public JmBrandBean getBrandLikeName(ShopBean shopBean, String name) throws Exception {
        if (name == null) {
            return null;
        }

        if (brands == null) {
            initBrands(shopBean);
        }

        for (JmBrandBean brand : brands) {
            if (brand != null && brand.getName() != null
                    && brand.getName().startsWith(name)) {
                return brand;
            }
        }

        return null;
    }

}
