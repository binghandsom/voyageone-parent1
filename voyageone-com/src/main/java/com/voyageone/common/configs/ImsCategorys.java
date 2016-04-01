package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.beans.ImsCategoryBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.ImsCategoryDao;
import com.voyageone.common.redis.CacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImsCategorys {

    private static final Class selfClass = ImsCategorys.class;

    private final static Logger logger = LoggerFactory.getLogger(selfClass);

    /* redis key */
    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_ImsCategoryConfigs.toString();

    public static void reload() {
        ImsCategoryDao imsCategoryDao = ConfigDaoFactory.getImsCategoryDao();
        Map<String, ImsCategoryBean> ImsChannelBeanMap = new HashMap<>();
        ImsCategoryBean topBean = new ImsCategoryBean();
        List<ImsCategoryBean> categoryBeans = imsCategoryDao.getMtCategories();
        categoryBeans.forEach(bean -> {
            List<ImsCategoryBean> subCategoryBeans = new ArrayList<>();
            categoryBeans.forEach(subBean -> {
                if (subBean.getParentCid() == bean.getCategoryId()) {
                    subCategoryBeans.add(subBean);
                }
            });
            bean.setSubCategories(subCategoryBeans);

            ImsChannelBeanMap.put(buildKey(bean.getCategoryId()), bean);
            if (bean.getParentCid() == 0) {
                if (topBean.getSubCategories() == null)
                    topBean.setSubCategories(new ArrayList<>());
                topBean.getSubCategories().add(bean);
            }
        });

        ImsChannelBeanMap.put(buildKey(0), topBean);
        CacheHelper.reFreshSSB(KEY, ImsChannelBeanMap);

        logger.info("ImsCategory 读取数量: " + CacheHelper.getSize(KEY));
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(int categoryId) {
        return String.valueOf(categoryId) + CacheKeyEnums.SKIP;
    }

    public static ImsCategoryBean getMtCategoryBeanById(int categoryId) {
        return CacheHelper.getBean(KEY, buildKey(categoryId), selfClass);
    }

}
