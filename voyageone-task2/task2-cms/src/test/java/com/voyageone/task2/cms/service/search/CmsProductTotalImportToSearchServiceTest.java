package com.voyageone.task2.cms.service.search;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:context-cms-test.xml")
public class CmsProductTotalImportToSearchServiceTest {
    @Autowired
    ProductService productService;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Test
    public void onStartup() throws Exception {

    }

    @Autowired
    private CmsProductTotalImportToSearchService cmsProductTotalImportToSearchService;

    @Test
    public void testImportDataToSearchFromMongo() {

        String channelId = "001";
        cmsProductTotalImportToSearchService.importDataToSearchFromMongo(channelId);
    }

    @Test
    public void testOnStartup() throws Exception {
        if("{ \"sales.P23\" : \"1\" }".matches("sales.P[0-9]{1,2}.*")){

        }
    }
}
