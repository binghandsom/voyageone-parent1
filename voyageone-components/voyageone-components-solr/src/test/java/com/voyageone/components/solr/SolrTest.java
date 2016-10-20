package com.voyageone.components.solr;

import com.voyageone.common.util.JacksonUtil;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.FacetQuery;
import org.springframework.data.solr.core.query.PartialUpdate;
import org.springframework.data.solr.core.query.SimpleFacetQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author aooer 2016/8/26.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-solr.xml")
public class SolrTest {

    @Autowired
    private SolrTemplate solrTemplate;

//    @Test
//    public void testAddDocument() throws IOException, SolrServerException {
//        solrTemplate.getSolrClient().add(new SolrInputDocument() {{
//            setField("id", 3);
//            setField("name", "王五");
//        }});
//
//        solrTemplate.commit();
//    }

    @Test
    public void testAddBean() {
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            solrTemplate.saveDocument(new SolrInputDocument() {{
                setField("id", 20 + finalI);
                setField("name", String.valueOf(finalI) + "张散发" + String.valueOf(finalI));
            }});
        }

        solrTemplate.commit();
    }

    @Test
    public void testGetById() {

        for (int i = 0; i < 100; i++) {
            SolrUser solrUser = solrTemplate.getById(i, SolrUser.class);
            System.out.println(String.format("id=%s;value=%s", i, JacksonUtil.bean2Json(solrUser)));
        }

    }

    @Test
    public void testGetByIds() {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ids.add(i);
        }

        Collection<SolrUser> solrUsers = solrTemplate.getById(ids, SolrUser.class);
        for (SolrUser solrUser : solrUsers) {
            System.out.println(String.format("value=%s", JacksonUtil.bean2Json(solrUser)));
        }

    }

    @Test
    public void testGetCount() {
        FacetQuery facetQuery = new SimpleFacetQuery(new SimpleStringCriteria("name:张散发a*"));
        long count1 = solrTemplate.count(facetQuery);
        System.out.println(String.format("count=%s", count1));
    }

    @Test
    public void testSaveBean() {
        for (int i = 20; i < 30; i++) {
            PartialUpdate update = new PartialUpdate("id", i);
            update.add("name", "张散发中国中共忠告" + String.valueOf(i));
            solrTemplate.saveBean(update);
        }

        solrTemplate.commit();
    }

    @Test
    public void testSaveBeans() {
        List<PartialUpdate> beans = new ArrayList<>();
        for (int i = 21; i < 30; i++) {
            PartialUpdate update = new PartialUpdate("id", i);
            update.add("name", "张散发a" + String.valueOf(i));
            beans.add(update);
        }
        UpdateResponse response = solrTemplate.saveBeans(beans);
        System.out.println(response);
    }

    @Test
    public void testDeleteByIDObject() {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            ids.add(String.valueOf(i));
        }

        solrTemplate.deleteById(ids);

        solrTemplate.commit();
    }


    public class SolrUser {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
