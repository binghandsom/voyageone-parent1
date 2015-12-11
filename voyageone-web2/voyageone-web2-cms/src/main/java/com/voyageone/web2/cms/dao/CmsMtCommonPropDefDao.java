package com.voyageone.web2.cms.dao;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.web2.cms.model.CmsMtCommonPropDefModel;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gubuchun 15/12/9
 * @version 2.0.0
 */
@Repository
public class CmsMtCommonPropDefDao extends BaseMongoDao {

    @Override
    public Class getEntityClass() {
        return CmsMtCommonPropDefModel.class;
    }

    public List<CmsMtCommonPropDefModel> selectPropAll() {
        return selectAll();
    }

    public List<JSONObject> selectPropAllWithJson() {
        String collectionName = mongoTemplate.getCollectionName(this.getEntityClass());
        return mongoTemplate.findAll(collectionName);
    }
}
