package com.voyageone.cms.service.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.cms.service.model.CmsBtCategoryMappingModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsBtCategoryMappingDao extends BaseDao {

    public List<CmsBtCategoryMappingModel> selectbyChannelId(String channelId) {
        CmsBtCategoryMappingModel model = new CmsBtCategoryMappingModel();
        model.setChannelId(channelId);
        return selectList("select_cms_bt_category_mapping", model);
    }

    public void insert(CmsBtCategoryMappingModel model) {
        insert("insert_cms_bt_category_mapping", model);
    }

    public void insertWithList(List<CmsBtCategoryMappingModel> models) {
        models.forEach(s -> {
            insert(s);
        });
    }
}
