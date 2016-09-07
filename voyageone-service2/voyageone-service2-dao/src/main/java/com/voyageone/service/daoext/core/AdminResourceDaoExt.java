package com.voyageone.service.daoext.core;

import com.voyageone.service.bean.com.AdminResourceBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AdminResourceDaoExt {
    List<AdminResourceBean> selectResByUser(Object map);

    List<AdminResourceBean> selectResByRoles(@Param("roleIds")List<Integer> list, @Param("application") String app);

    List<AdminResourceBean> selectMenu(Object map);

    List<AdminResourceBean> selectRes(@Param("application") String app);

    List<Map> selectAllPermConfig(@Param("roleIds")List<Integer> list);

}