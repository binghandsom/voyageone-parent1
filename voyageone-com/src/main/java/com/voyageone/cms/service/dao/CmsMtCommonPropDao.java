package com.voyageone.cms.service.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.cms.service.model.CmsMtCategoryTreeModel;
import com.voyageone.cms.service.model.CmsMtCommonPropModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by james.li on 2015/12/7.
 */
@Repository
public class CmsMtCommonPropDao extends BaseDao {
    public List<CmsMtCommonPropModel> selectCommonProp(){
        return selectList("select_CmsMtCommonProp");
    }
}
