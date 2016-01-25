package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Bean.JmCategoryBean;
import com.voyageone.common.components.jumei.base.JmBase;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chuanyu.laing on 2016/1/25.
 */
public class JumeiCategoryService extends JmBase {

    private static List<JmCategoryBean> categoryListLevel4 = null;

    private static String CATEGORY_URL = "/v1/category/query";
    /**
     * 初始化分类
     */
    public void initCategoryListLevel4(ShopBean shopBean) throws Exception {
        Map<String, String> param = new HashMap<>();
        param.put("level", "4");
        param.put("fields", "category_id,name,level,parent_category_id");
        String result = reqJmApi(shopBean, CATEGORY_URL, param);
        List<JmCategoryBean> categorysList = JsonUtil.jsonToBeanList(result, JmCategoryBean.class);
        if (categorysList != null) {
            categoryListLevel4 = categorysList;
        } else {
            categoryListLevel4 = new ArrayList<>();
        }
    }


    /**
     * 获取全部商品分类
     */
    public List<JmCategoryBean> getCategoryListLevel4(ShopBean shopBean) throws Exception {
        if (categoryListLevel4 == null) {
            initCategoryListLevel4(shopBean);
        }
        return categoryListLevel4;
    }

    /**
     * 根据名称查找分类4Model
     */
    public JmCategoryBean getCategory4ByName(ShopBean shopBean, String name) throws Exception {
        if (name == null) {
            return null;
        }

        if (categoryListLevel4 == null) {
            initCategoryListLevel4(shopBean);
        }

        for (JmCategoryBean category : categoryListLevel4) {
            if (category != null && category.getName() != null
                    && category.getName().equals(name)) {
                return category;
            }
        }

        return null;
    }




}
