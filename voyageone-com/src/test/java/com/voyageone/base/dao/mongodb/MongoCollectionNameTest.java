package com.voyageone.base.dao.mongodb;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jonas on 2016/10/9.
 *
 * @version 2.8.0
 * @since 2.8.0
 */
public class MongoCollectionNameTest {
    @Test
    public void getCollectionName() throws Exception {
        String lowerUnderscoreName = MongoCollectionName.getCollectionName(MongoCollectionNameTest.class);
        assertEquals("根据类名转换出来的名称不正确！", lowerUnderscoreName, "mongo_collection_name_test");
    }
}