package com.voyageone.components.solr.service;

import com.voyageone.components.solr.BaseSearchService;
import com.voyageone.components.solr.bean.CommIdSearchModel;
import com.voyageone.components.solr.bean.SolrUpdateBean;
import com.voyageone.components.solr.query.SimpleQueryBean;
import com.voyageone.components.solr.query.SimpleQueryCursor;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.result.Cursor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Cms Brand Cats Dist Search Service
 *
 * @author chuanyu.liang 2016/10/20
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CmsBrandCatsDistSearchService extends BaseSearchService {

    private static final String SOLR_TEMPLATE_NAME = "cmsBrandCatsSolrDistTemplate";

    @Override
    protected String getSolrTemplateName() {
        return SOLR_TEMPLATE_NAME;
    }

    /**
     * create Update Bean
     */
    public List<SolrUpdateBean> createSolrBeanForNew(Map<String, Integer> brandCatsCntMap, String channelId, Long lastVer) {
        List<SolrUpdateBean> result = new ArrayList<>();
        if (brandCatsCntMap == null || brandCatsCntMap.isEmpty()) {
            return result;
        }

        int i = 0;
        for (Map.Entry<String, Integer> entry : brandCatsCntMap.entrySet()) {
            SolrUpdateBean update = new SolrUpdateBean("id", String.valueOf(i));
            update.add("channelId", channelId);
            update.add("keyword", entry.getKey());
            update.add("productCnt", entry.getValue());
            if (lastVer != null) {
                update.add("lastVer", lastVer);
            }
            result.add(update);
            i++;
        }
        return result;
    }

    /**
     * queryForCursorByLastVer
     */
    @SuppressWarnings("Duplicates")
    public SimpleQueryCursor<CommIdSearchModel> queryIdsForCursorNotLastVer(String channelId, long lasVer) {
        String queryString = String.format("channelId:\"%s\" && -lastVer:\"%s\"", channelId, lasVer);
        SimpleQueryBean query = new SimpleQueryBean(queryString);
        query.addSort(new Sort(Sort.Direction.DESC, "id"));
        query.addProjectionOnField("id");
        //noinspection unchecked
        return new SimpleQueryCursor(queryForCursor(query, CommIdSearchModel.class));
    }


    private <T> Cursor<T> queryForCursor(Query query, final Class<T> clazz) {
        return getSolrTemplate().queryForCursor(query, clazz);
    }

}
