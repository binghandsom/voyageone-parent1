package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.model.JmBtProductImportModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author james.li on 2016/1/25.
 * @version 2.0.0
 */
@Repository
public class JMUploadProductDao extends BaseDao {

    public List<JmBtProductImportModel> getNotUploadProduct(Integer count){
        return selectList("get_jm_upload_product", count);
    }
}
