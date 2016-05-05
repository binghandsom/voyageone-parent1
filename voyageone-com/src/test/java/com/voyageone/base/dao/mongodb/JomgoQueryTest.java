package com.voyageone.base.dao.mongodb;

import org.junit.Test;

/**
 * @author Jonas, 12/18/15.
 * @version 2.0.1
 * @since 2.0.0
 */
public class JomgoQueryTest {

    @Test
    public void testSetProjection() {

        JomgoQuery query1 = new JomgoQuery() .setProjectionExt("a", "b", "c");
        assert query1.getProjection().equals("{\"a\":1,\"b\":1,\"c\":1}");

        JomgoQuery query2 = new JomgoQuery();
        query2.setProjection("{\"a\":1,\"b\":1,\"c\":1}");
        assert query2.getProjection().equals("{\"a\":1,\"b\":1,\"c\":1}");
    }
}