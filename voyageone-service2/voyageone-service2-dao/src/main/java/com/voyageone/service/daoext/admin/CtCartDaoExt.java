package com.voyageone.service.daoext.admin;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.voyageone.service.model.admin.CtCartModel;

@Repository
public interface CtCartDaoExt {

	List<CtCartModel> selectCartByIds(@Param("cartIds") List<Integer> cartIds);

}
