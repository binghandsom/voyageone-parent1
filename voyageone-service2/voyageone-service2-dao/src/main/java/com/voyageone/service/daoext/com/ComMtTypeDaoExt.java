package com.voyageone.service.daoext.com;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.service.model.com.ComMtTypeModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/17
 */
@Repository
public interface ComMtTypeDaoExt {

	Integer searchTypeCount(Map<String, Object> params);

	List<ComMtTypeModel> searchTypeByPage(Map<String, Object> params);

}
