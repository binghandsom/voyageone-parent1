package com.voyageone.service.dao.cms.mongo;

import com.mongodb.DBObject;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.model.cms.mongo.meta.CmsZiIndexModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class CmsZiIndexDao extends BaseMongoDao<CmsZiIndexModel> {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Set<String> getAllCollections(String exCol) {
        Set<String> colList = mongoTemplate.getCollectionNames();
        colList.remove(exCol);
        return colList;
    }

    public List<String> getAllDefinedCollections() {

        List<String> allDefinedCollList = new ArrayList<>();
        List<CmsZiIndexModel> allDefined = selectAll();
        allDefinedCollList.addAll(allDefined.stream().filter(idx -> !"exclude".equals(idx.getMatch_type())).map(CmsZiIndexModel::getColl_name).collect(Collectors.toList()));
        return allDefinedCollList;
    }

    public List<String> getAllExcludeCollections() {

        List<String> allDefinedCollList = new ArrayList<>();
        List<CmsZiIndexModel> allDefined = selectAll();
        allDefinedCollList.addAll(allDefined.stream().filter(idx -> "exclude".equals(idx.getMatch_type())).map(CmsZiIndexModel::getColl_name).collect(Collectors.toList()));
        return allDefinedCollList;
    }

    public List<DBObject> getCollectionIndexesFromDB(String collName) {
        List<DBObject> indexInfoList = mongoTemplate.getCollection(collName).getIndexInfo();

        Iterator<DBObject> indexIter = indexInfoList.iterator();
        while (indexIter.hasNext()) {
            DBObject idxObj = indexIter.next();
            String name = idxObj.get("name").toString();
            if ("_id_".equals(name)) {
                indexIter.remove();
                // 仅有一个_id索引
                break;
            }
        }

        return indexInfoList;
    }

    public CmsZiIndexModel getCollectionIndexesFromMeta(String collName) {
        JongoQuery queryObject = new JongoQuery();
        queryObject.setQuery("{\"coll_name\": # }");
        // 用定义的名字去检索索引
        queryObject.setParameters(collName);
        List<CmsZiIndexModel> datas = select(queryObject);
        if (datas != null && !datas.isEmpty()) {
            return datas.get(0);
        }
        return null;
    }


    public void dropCollectionIndexFromDB(String collName, String indexName) {
        mongoTemplate.getDb().getCollection(collName).dropIndex(indexName);
    }

    public void createCollectionIndexFromDB(String collName, String indexName, DBObject keys, boolean unique) {
        mongoTemplate.getDb().getCollection(collName).createIndex(keys, indexName, unique);
    }

    public void createCollectionIndexFromDB(String collName, DBObject keys, DBObject options) {
        mongoTemplate.getDb().getCollection(collName).createIndex(keys, options);
    }

}
