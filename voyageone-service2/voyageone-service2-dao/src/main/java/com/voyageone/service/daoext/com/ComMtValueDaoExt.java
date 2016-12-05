package com.voyageone.service.daoext.com;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.voyageone.service.bean.com.ComMtValueBean;
import com.voyageone.service.model.com.ComMtValueModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/19
 */
@Repository
public interface ComMtValueDaoExt {

	List<ComMtValueModel> selectTypeAttributeByTypeName(@Param("typeName") String typeName);

	Integer selectTypeAttributeCount(Map<String, Object> params);

	List<ComMtValueBean> selectTypeAttributeByPage(Map<String, Object> params);

}
