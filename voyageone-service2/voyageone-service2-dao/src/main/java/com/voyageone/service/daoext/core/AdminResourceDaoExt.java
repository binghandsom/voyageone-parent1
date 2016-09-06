package com.voyageone.service.daoext.core;

import com.voyageone.service.bean.com.AdminResourceBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminResourceDaoExt {
    List<AdminResourceBean> selectResByUser(Object map);

    List<AdminResourceBean> selectResByRoles(List<Integer> list);

    List<AdminResourceBean> selectMenu(Object map);

}