package com.voyageone.service.daoext.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtChannelCategoryModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsBtChannelCategoryDaoExt extends ServiceBaseDao {

    public List<CmsBtChannelCategoryModel> selectbyChannelId(String channelId) {
        CmsBtChannelCategoryModel model = new CmsBtChannelCategoryModel();
        model.setChannelId(channelId);
        return selectList("select_cms_bt_channel_category", model);
    }

    public void insert(CmsBtChannelCategoryModel model) {
        insert("insert_cms_bt_channel_category", model);
    }

    public void insertWithList(List<CmsBtChannelCategoryModel> models) {
        models.forEach(s -> {
            insert(s);
        });
    }
}
