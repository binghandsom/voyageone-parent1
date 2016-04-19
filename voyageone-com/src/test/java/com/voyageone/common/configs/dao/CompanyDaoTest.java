package com.voyageone.common.configs.dao;

import com.voyageone.BaseTest;
import com.voyageone.common.configs.beans.CompanyBean;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description
 * @author: holysky
 * @date: 2016/4/18 17:55
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class CompanyDaoTest extends BaseTest{
    @Resource
    CompanyDao dao;

    @Test
    public void testGetAllActives() throws Exception {
        List<CompanyBean> companys = dao.getAllActives();
        Assert.assertTrue(companys.size()>0);
    }
}
