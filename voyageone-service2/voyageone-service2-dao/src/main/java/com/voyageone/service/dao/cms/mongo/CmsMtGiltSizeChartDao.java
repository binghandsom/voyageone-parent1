package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.model.cms.mongo.CmsMtGiltSizeChartModel;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

/**
 *
 * Created by james on 2017/1/24.
 */
@Repository
public class CmsMtGiltSizeChartDao extends BaseMongoDao<CmsMtGiltSizeChartModel> {

    public CmsMtGiltSizeChartModel getGitlSizeChartById(Long id){
        JongoQuery query = new JongoQuery();
        query.setQuery(new Criteria("id").is(id));
        return selectOneWithQuery(query);
    }

    public void insertGiltSizeChart(CmsMtGiltSizeChartModel cmsMtGiltSizeChartModel){
        insert(cmsMtGiltSizeChartModel);
    }
}
