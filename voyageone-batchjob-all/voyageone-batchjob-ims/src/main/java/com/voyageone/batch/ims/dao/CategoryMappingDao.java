package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.ims.modelbean.CategoryMappingBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CategoryMappingDao extends BaseDao {

    /**
     * 获取所有主数据中的类目匹配关系
     * @return 所有主数据中的类目匹配关系
     */
    public List<CategoryMappingBean> selectCategoryMapping() {

        return super.selectList(Constants.DAO_NAME_SPACE_IMS + "ims_selectCategoryMapping");

    }

    /**
     * 插入主数据（类目匹配）
     */
    public void insertCategoryMapping(CategoryMappingBean categoryMappingBean, String jobName) {
        Map<String, Object> params = new HashMap<>();

        params.put("catMapping", categoryMappingBean);
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS + "ims_insertCategoryMapping", params);

    }

    public String selectPlatformCidByMasterCategoryId(int cartId, String categoryId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("cart_id", cartId);
        dataMap.put("category_id", categoryId);
        return updateTemplate.selectOne(Constants.DAO_NAME_SPACE_IMS + "ims_selectPlatformCidByMasterCategoryId", dataMap);
    }

}
