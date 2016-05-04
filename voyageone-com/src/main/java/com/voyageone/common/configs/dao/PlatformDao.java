package com.voyageone.common.configs.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.PlatformBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: holysky
 * @date: 2016/4/15 18:41
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
@Repository
public class PlatformDao extends BaseDao{

//    @Select(value = "select * from Synship.tm_platform")
    public List<PlatformBean> getAll(){
        return selectList(Constants.DAO_NAME_SPACE_COMMON+"tm_platform_getAll");
    }

}
