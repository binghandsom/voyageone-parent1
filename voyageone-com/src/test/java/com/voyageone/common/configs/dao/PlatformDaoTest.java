package com.voyageone.common.configs.dao;

import com.voyageone.BaseTest;
import com.voyageone.common.configs.beans.PlatformBean;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description
 * @author: holysky
 * @date: 2016/4/15 18:49
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class PlatformDaoTest extends BaseTest{

    @Resource
    PlatformDao platformDao ;

    @Test
    public void testGetAll() throws Exception {

        List<PlatformBean> platforms = platformDao.getAll();
        Assert.assertTrue(platforms.size()>0);
    }
}
