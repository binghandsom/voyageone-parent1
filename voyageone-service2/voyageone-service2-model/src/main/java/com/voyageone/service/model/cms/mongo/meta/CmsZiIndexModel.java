package com.voyageone.service.model.cms.mongo.meta;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import com.voyageone.base.dao.mysql.BaseModel;
import com.voyageone.service.model.cms.mongo.product.*;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link CmsZiIndexModel} 的商品Model
 *
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsZiIndexModel extends BaseMongoModel {

    private String coll_name;
    private String match_type;
    private List<CmsZiIndexModel_Index> indexes = new ArrayList<>();

    public String getColl_name() {
        return coll_name;
    }

    public void setColl_name(String coll_name) {
        this.coll_name = coll_name;
    }

    public String getMatch_type() {
        return match_type;
    }

    public void setMatch_type(String match_type) {
        this.match_type = match_type;
    }

    public void setIndexes(List<CmsZiIndexModel_Index> indexes) {
        this.indexes = indexes;
    }

    public List<CmsZiIndexModel_Index> getIndexes() {
        return indexes;
    }
}