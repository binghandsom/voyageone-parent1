package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.ims.modelbean.CategoryBean;
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
    public int insertCategory( CategoryBean categoryBean, String jobName) {
        Map<String, Object> params = new HashMap<>();

        params.put("cat", categoryBean);
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS + "ims_insertCategories", params);

        CategoryBean categoryBeanResult = (CategoryBean)params.get("cat");

        return categoryBeanResult.getCategoryId();
    }


}
