package com.voyageone.components.solr.bean;

import org.springframework.data.solr.core.query.Field;
import org.springframework.data.solr.core.query.PartialUpdate;

/**
 * CmsProductSearchModel
 *
 * @author chuanyu.liang 2016/10/8
 * @version 2.0.0
 * @since 2.0.0
 */
public class SolrUpdateBean extends PartialUpdate {

    public SolrUpdateBean(String idFieldName, Object idFieldValue) {
        super(idFieldName, idFieldValue);
    }

    public SolrUpdateBean(Field idField, Object idFieldValue) {
        super(idField, idFieldValue);
    }

}
