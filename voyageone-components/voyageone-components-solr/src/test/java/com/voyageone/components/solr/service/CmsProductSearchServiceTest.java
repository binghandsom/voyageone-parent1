package com.voyageone.components.solr.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.solr.bean.CommIdSearchModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.Cursor;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-solr.xml")
public class CmsProductSearchServiceTest {

    @Autowired
    private CmsProductSearchService cmsProductSearchService;

    @Test
    public void testQueryIdsForCursorNotLastVer() {
        Cursor<CommIdSearchModel> cursor = cmsProductSearchService.queryIdsForCursorNotLastVer("010", 1475988631703L);
        while (cursor.hasNext()) {
            CommIdSearchModel row = cursor.next();
            System.out.println(JacksonUtil.bean2Json(row));
        }
    }

    @Test
    public void testQueryForPage() {
        SimpleQuery query = new SimpleQuery("*:*");
        query.addSort(new Sort(Sort.Direction.DESC, "id"));
        query.setRows(20);
        query.setOffset(100);
        Page<CommIdSearchModel> page = cmsProductSearchService.queryForPage("cms_product", query, CommIdSearchModel.class);
        System.out.println("Total:" + page.getTotalElements());
        for (CommIdSearchModel idModel : page.getContent()) {
            System.out.println(JacksonUtil.bean2Json(idModel));
        }
    }

    @Test
    public void testQueryGroupForPage() {
        SimpleQuery groupQuery = new SimpleQuery("keywords:(\"A10BD2920AG3C18\", \"S7952391AQCZCY\")");
        //groupQuery.addSort(new Sort(Sort.Direction.DESC, "id"));
        groupQuery.setRows(10);
        groupQuery.setOffset(0);

        GroupOptions groupOptions = new GroupOptions();
        groupQuery.setGroupOptions(groupOptions);
        //groupOptions.addSort(new Sort("productCode"));
        groupOptions.addGroupByField("productCode");

        SolrResultPage<CommIdSearchModel> page = cmsProductSearchService.queryForSolrResultPage(groupQuery, CommIdSearchModel.class);
        System.out.println("Total:" + page.getGroupResult("productCode").getGroupEntries());
        for (CommIdSearchModel idModel : page.getContent()) {
            System.out.println(JacksonUtil.bean2Json(idModel));
        }
    }
}
