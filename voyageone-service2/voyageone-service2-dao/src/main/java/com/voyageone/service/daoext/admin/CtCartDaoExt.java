package com.voyageone.service.daoext.admin;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.voyageone.service.model.admin.CtCartModel;

@Repository
public interface CtCartDaoExt {

	List<CtCartModel> selectCartByIds(@Param("cartIds") List<Integer> cartIds);

	Integer selectCartCount(Map<String, Object> params);

	List<CtCartModel> selectCartByPage(Map<String, Object> params);

	List<Map<String, Object>> selectAllPlatform();

}
