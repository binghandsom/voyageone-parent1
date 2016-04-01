package com.voyageone.service.dao.cms.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import org.springframework.stereotype.Repository;


@Repository
public class CommSequenceDao extends BaseMongoDao<Long> {

    public CommSequenceDao() {
        collectionName = "com_sequence";
    }

    public long getNextSequence(String name) {
        DBObject dbObject = getNextSequence_(name);

        if (dbObject == null) {
            createSequence(name);
            dbObject = getNextSequence_(name);
        }

        Object seqObj = dbObject.get("seq");
        if (seqObj instanceof Double) {
            Double seqDouble = (Double)seqObj;
            return seqDouble.longValue();
        }

        return  Long.parseLong(seqObj.toString());
    }

    public DBObject getNextSequence_(String name) {
        DBCollection coll = getDBCollection();
        BasicDBObject searchQuery = new BasicDBObject("_id", name);
        BasicDBObject increase = new BasicDBObject("seq", 1);
        BasicDBObject updateQuery = new BasicDBObject("$inc", increase);
        return coll.findAndModify(searchQuery, null, null, false, updateQuery, true, false);
    }

    private void createSequence(String name) {
        try {
            DBCollection coll = getDBCollection();
            DBObject insertObject = new BasicDBObject("_id", name);
            insertObject.put("seq", 0);
            coll.save(insertObject);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
