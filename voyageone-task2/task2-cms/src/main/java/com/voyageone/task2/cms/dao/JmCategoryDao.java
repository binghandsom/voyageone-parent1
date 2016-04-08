package com.voyageone.task2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.components.jumei.bean.JmCategoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author james.li on 2016/1/25.
 * @version 2.0.0
 */
@Repository
public class JmCategoryDao extends BaseDao {

    public List<JmCategoryBean> getJmCategoryById(Integer categoryId){
        return selectList("select_jm_mt_category",categoryId);
    }

    public int insertJmCategory(List<JmCategoryBean> categorys){
        return updateTemplate.insert("insert_jm_mt_category", categorys);
    }

    public int clearJmCategory(){
        return updateTemplate.delete("delete_jm_mt_category");
    }
}
