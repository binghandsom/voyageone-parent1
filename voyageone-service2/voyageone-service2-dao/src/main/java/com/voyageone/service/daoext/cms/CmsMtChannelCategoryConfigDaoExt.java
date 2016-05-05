package com.voyageone.service.daoext.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsMtChannelCategoryConfigModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsMtChannelCategoryConfigDaoExt extends ServiceBaseDao {

    public List<CmsMtChannelCategoryConfigModel> selectbyChannelId(String channelId) {
        CmsMtChannelCategoryConfigModel model = new CmsMtChannelCategoryConfigModel();
        model.setChannelId(channelId);
        return selectList("select_cms_mt_channel_category_config", model);
    }

    public void insert(CmsMtChannelCategoryConfigModel model) {
        insert("insert_cms_mt_channel_category_config", model);
    }

    public void insertWithList(List<CmsMtChannelCategoryConfigModel> models) {
        models.forEach(this::insert);
    }
}
