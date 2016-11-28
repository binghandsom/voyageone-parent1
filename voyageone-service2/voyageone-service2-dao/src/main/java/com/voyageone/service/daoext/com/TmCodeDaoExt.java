package com.voyageone.service.daoext.com;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.voyageone.service.model.com.TmCodeModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/23
 */
@Repository
public interface TmCodeDaoExt {

	Integer selectCodeCount(Map<String, Object> params);

	List<TmCodeModel> selectCodeByPage(Map<String, Object> params);

	List<TmCodeModel> selectAllPort(@Param("id") String portCode);

}
