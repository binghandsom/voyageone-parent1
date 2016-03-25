package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.beans.ImsCategoryBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.ImsCategoryDao;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.redis.CacheTemplateFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.HashOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImsCategorys {

    private static final Log logger = LogFactory.getLog(ImsCategorys.class);

    /* redis key */
    private static final String KEY = CacheKeyEnums.ConfigData_ImsCategoryConfigs.toString();

    private static HashOperations<String, String, ImsCategoryBean> hashOperations = CacheTemplateFactory.getHashOperation();

    static {
        if (!CacheTemplateFactory.getCacheTemplate().hasKey(KEY)) {
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

            logger.info("ImsCategory 读取数量: " + hashOperations.size(KEY));
        }
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(int categoryId) {
        return String.valueOf(categoryId) + CacheHelper.SKIP;
    }

    public static ImsCategoryBean getMtCategoryBeanById(int categoryId) {
        return hashOperations.get(KEY, buildKey(categoryId));
    }

}
