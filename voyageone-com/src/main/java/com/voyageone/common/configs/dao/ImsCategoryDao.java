package com.voyageone.common.configs.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.ImsCategoryBean;
@Repository
public class ImsCategoryDao extends BaseDao {
	
	public List<ImsCategoryBean> getMtCategories() {
		return selectList(Constants.DAO_NAME_SPACE_COMMON+"ims_categories_getAll");
	}

}
