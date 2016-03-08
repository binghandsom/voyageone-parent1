package com.voyageone.service.dao.cms;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.service.model.cms.CmsMtCommonPropActionDefModel;
import com.voyageone.service.model.cms.CmsMtCommonPropModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by james.li on 2015/12/7.
 */
@Repository
public class CmsMtCommonPropDao extends BaseDao {
    public List<CmsMtCommonPropModel> selectCommonProp(){
        return selectList("select_cms_mt_common_prop_all");
    }

    public List<CmsMtCommonPropActionDefModel> getActionModelList(){
        return  selectList("select_cms_mt_common_prop_actionDefAll");
    }
}
