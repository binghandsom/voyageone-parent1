package com.voyageone.service.daoext.core;


import com.voyageone.service.bean.com.AdminResourceBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminResourceDaoExt {
    List<AdminResourceBean> selectResByUser(Integer userId);

    List<AdminResourceBean> selectResByRoles(List<Integer> list);

}