package com.voyageone.security.daoext;


import com.voyageone.security.bean.ComResourceBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComResourceDaoExt {
    List<ComResourceBean> selectResByUser(Integer userId);


    List<ComResourceBean> selectAll();
}