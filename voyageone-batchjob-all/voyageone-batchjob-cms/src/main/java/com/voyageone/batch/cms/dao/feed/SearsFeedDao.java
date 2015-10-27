package com.voyageone.batch.cms.dao.feed;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.sears.bean.ProductBean;
import com.voyageone.common.components.sears.bean.ProductResponse;
import org.springframework.stereotype.Repository;

/**
 * Created by james on 2015/10/26.
 */
@Repository
public class SearsFeedDao extends BaseDao{
    @Override
    protected String namespace() {
        return Constants.getDaoNameSpace(SubSystem.CMS);
    }

    public int delete() {
        return delete("cms_zz_worktable_sears_superfeed_delete", null);
    }

    public int insert(ProductResponse product){
        return  insert("cms_zz_worktable_sears_superfeed_insert", product);
    }
    public int insertAattribute(ProductResponse product){
        return  insert("cms_zz_worktable_sears_attribute_insert",product);
    }

    public int deleteAattribute(){
        return  delete("cms_zz_worktable_sears_attribute_delete",null);
    }
}
