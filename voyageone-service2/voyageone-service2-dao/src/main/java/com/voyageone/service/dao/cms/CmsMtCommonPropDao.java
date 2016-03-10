package com.voyageone.service.dao.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsMtCommonPropActionDefModel;
import com.voyageone.service.model.cms.CmsMtCommonPropModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by james.li on 2015/12/7.
 */
@Repository
public class CmsMtCommonPropDao extends ServiceBaseDao {
    public List<CmsMtCommonPropModel> selectCommonProp(){
        return selectList("select_cms_mt_common_prop_all");
    }

    public List<CmsMtCommonPropActionDefModel> getActionModelList(){
        return  selectList("select_cms_mt_common_prop_actionDefAll");
    }
}
