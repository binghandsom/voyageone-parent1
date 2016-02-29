package com.voyageone.cms.service.dao.mongodb;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsMtComSchemaModel;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.Field;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author lewis, 16-1-6
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class CmsMtCommonSchemaDao extends BaseMongoDao {

    @Override
    public Class getEntityClass() {
        return CmsMtComSchemaModel.class;
    }

    /**
     * 根据category id 查询对应的Schema.
     */
    public CmsMtComSchemaModel getComSchema() {

        JSONObject comSchmeaJson = mongoTemplate.findOne(collectionName);

        CmsMtComSchemaModel comSchemaModel = null;

        if (comSchmeaJson != null) {

            comSchemaModel = new CmsMtComSchemaModel();

            List fieldList = (List) comSchmeaJson.get("fields");

            List<Field> fields = SchemaJsonReader.readJsonForList(fieldList);

            comSchemaModel.setFields(fields);
        }

        return comSchemaModel;
    }

    public List findAllProps() {

        Map json = mongoTemplate.findOne("", "{fields:1}", Map.class, collectionName);

        return (List) json.get("fields");
    }
}
