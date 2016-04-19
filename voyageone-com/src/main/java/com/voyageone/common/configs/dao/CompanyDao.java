package com.voyageone.common.configs.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.CompanyBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: holysky
 * @date: 2016/4/18 17:52
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
@Repository
public class CompanyDao extends BaseDao{


    public List<CompanyBean> getAllActives() {
         return selectList(Constants.DAO_NAME_SPACE_COMMON + "ct_company_getAllActives");
    }
}
