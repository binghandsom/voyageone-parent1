package com.voyageone.components.solr.query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;

/**
 * SimpleQueryBean
 *
 * @author chuanyu.liang 2016/10/8
 * @version 2.0.0
 * @since 2.0.0
 */
public class SimpleQueryBean extends SimpleQuery {

    public SimpleQueryBean() {
        super();
    }

    public SimpleQueryBean(Criteria criteria) {
        super(criteria);
    }

    public SimpleQueryBean(String queryString) {
        super(queryString);
    }

    public SimpleQueryBean(Criteria criteria, Pageable pageable) {
        super(criteria, pageable);
    }

    public SimpleQueryBean(String queryString, Pageable pageable) {
        super(queryString, pageable);
    }
}
