package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.ims.modelbean.CategoryModelMapBean;
import com.voyageone.common.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 2015/5/28.
 */
@Repository
public class CategoryModelMapDao extends BaseDao{
    private Log logger = LogFactory.getLog(this.getClass());

    /**
     * 根据条件{channel_id, platform_id, model_id}查找tmallCategory，对应表ims_mt_categories_model_mapping
     */
    public Long getCategoryCodeByModel(String channel_id, int platform_id, String model)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("channel_id", channel_id);
        dataMap.put("platform_id", platform_id);
        dataMap.put("model", model);
        System.out.println("channel_id:" +channel_id);
        System.out.println("platform_id:" +platform_id);
        System.out.println("model:" +model);
        List<CategoryModelMapBean> categoryModelMapBeans = (List)selectList(Constants.DAO_NAME_SPACE_IMS + "ims_mt_categories_model_mapping_selectBeanByModel", dataMap);
        if (categoryModelMapBeans == null || categoryModelMapBeans.size() == 0)
            return Long.valueOf(-1);
        return Long.parseLong(categoryModelMapBeans.get(0).getCategory_code());
    }
}
