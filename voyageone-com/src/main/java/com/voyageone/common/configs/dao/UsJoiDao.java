package com.voyageone.common.configs.dao;


import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.CodeBean;
import com.voyageone.common.configs.beans.UsJoiBean;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UsJoiDao extends BaseDao {

    public List<UsJoiBean> getAll() {
        return selectList(Constants.DAO_NAME_SPACE_COMMON + "us_joi_getAll");
    }
}
