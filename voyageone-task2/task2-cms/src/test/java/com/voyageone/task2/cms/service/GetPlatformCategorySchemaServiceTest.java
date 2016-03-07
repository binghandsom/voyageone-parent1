package com.voyageone.task2.cms.service;

import com.voyageone.task2.cms.service.GetPlatformCategorySchemaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformCategoryDao;

/**
 * Created by lewis on 15-11-28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class GetPlatformCategorySchemaServiceTest {

	@Autowired
	CmsMtPlatformCategoryDao cmsMtPlatformCategoryDao;

	@Autowired
	GetPlatformCategorySchemaService getPlatformCategorySchemaService;

	@Test
	public void testOnStartup() throws Exception {

		// 插入类目信息
		getPlatformCategorySchemaService.startup();

	}

}
