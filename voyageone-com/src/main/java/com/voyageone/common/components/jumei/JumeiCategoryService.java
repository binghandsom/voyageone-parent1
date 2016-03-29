package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Bean.JmCategoryBean;
import com.voyageone.common.components.jumei.base.JmBase;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JacksonUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chuanyu.laing on 2016/1/25.
 */
@Service
public class JumeiCategoryService extends JmBase {

    private List<JmCategoryBean> categoryListLevel4 = null;

    private static String CATEGORY_URL = "v1/category/query";

    /**
     * initCategoryListLevel4
     */
    public void initCategoryListLevel4(ShopBean shopBean) throws Exception {
        List<JmCategoryBean> categorysList = getCategoryListLevel(shopBean, "4");
        if (categorysList != null) {
            categoryListLevel4 = categorysList;
        } else {
            categoryListLevel4 = new ArrayList<>();
        }
    }
    /**
     * 初始化分类
     */
    public List<JmCategoryBean> getCategoryListLevel(ShopBean shopBean, String level) {
        List<JmCategoryBean> result = new ArrayList<>();
        try {
            int i = 1;
            while (true) {
                Map<String, Object> param = new HashMap<>();
                param.put("page", String.valueOf(i));
                param.put("level", level);
                param.put("fields", "category_id,name,level,parent_category_id");
                String reqResult = reqJmApi(shopBean, CATEGORY_URL, param);
                List<JmCategoryBean> categoryList = JacksonUtil.jsonToBeanList(reqResult, JmCategoryBean.class);
                result.addAll(categoryList);
                i++;
            }
        } catch (Exception ignored) {
        }
        return result;
    }

    /**
     * getCategoryListALL
     */
    public List<JmCategoryBean> getCategoryListALL(ShopBean shopBean) throws Exception {
        List<JmCategoryBean> result = new ArrayList<>();
        for (int i=1; i<=4; i++) {
            List<JmCategoryBean> categorysList = getCategoryListLevel(shopBean, i+"");
            if (categorysList != null) {
                result.addAll(categorysList);
            }
            if (i == 4) {
                if (categorysList != null) {
                    categoryListLevel4 = categorysList;
                } else {
                    categoryListLevel4 = new ArrayList<>();
                }
            }
        }
        return result;
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
