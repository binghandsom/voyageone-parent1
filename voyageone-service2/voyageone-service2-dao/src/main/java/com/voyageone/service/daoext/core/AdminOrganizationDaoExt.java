package com.voyageone.service.daoext.core;

import com.voyageone.service.bean.com.AdminOrgBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminOrganizationDaoExt {
    List<AdminOrgBean> selectList(Object map);

    int selectCount(Object map);


}