package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.model.CategoryModel;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CategoryDao extends BaseDao {

    /**
     * 插入主数据（类目）
     * @return 新插入的类目id
     */
    public int insertCategory(CategoryModel categoryModel, String jobName) {
        Map<String, Object> params = new HashMap<>();

        params.put("cat", categoryModel);
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "ims_insertCategories", params);

        CategoryModel categoryModelResult = (CategoryModel)params.get("cat");

        return categoryModelResult.getCategoryId();
    }


}
