package com.voyageone.components.solr.service;

import com.voyageone.common.spring.SpringContext;
import com.voyageone.components.ComponentBase;
import com.voyageone.components.solr.bean.CommIdSearchModel;
import com.voyageone.components.solr.bean.SolrUpdateBean;
import com.voyageone.components.solr.query.SimpleQueryBean;
import com.voyageone.components.solr.query.SimpleQueryCursor;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.result.Cursor;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CmsProductSearchService
 *
 * @author chuanyu.liang 2016/10/8
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CmsProductSearchService extends ComponentBase {

    private SolrTemplate getSolrTemplate() {
        return SpringContext.getBean(SolrTemplate.class);
    }

    /**
     * saveBean
     */
    public String saveBean(SolrUpdateBean bean) {
        UpdateResponse response = getSolrTemplate().saveBean(bean);
        return response.toString();
    }

    /**
     * saveBeans
     */
    public String saveBeans(List<SolrUpdateBean> beans) {
        UpdateResponse response = getSolrTemplate().saveBeans(beans);
        return response.toString();
    }

    /**
     * saveBean
     */
    public String deleteById(String id) {
        UpdateResponse response = getSolrTemplate().deleteById(id);
        return response.toString();
    }

    /**
     * saveBeans
     */
    public String deleteByIds(List<String> ids) {
        UpdateResponse response = getSolrTemplate().deleteById(ids);
        return response.toString();
    }

    /**
     * commit
     */
    public void commit() {
        getSolrTemplate().commit();
    }


    /**
     * create Update Bean
     */
    public SolrUpdateBean createUpdate(CmsBtProductModel cmsBtProductModel, Long lastVer) {
        if (cmsBtProductModel == null || cmsBtProductModel.getChannelId() == null
                || cmsBtProductModel.getCommon() == null || cmsBtProductModel.getCommon().getFields() == null) {
            return null;
        }

        //id
        String id = cmsBtProductModel.get_id();
        //channelId
        String productChannel = cmsBtProductModel.getChannelId();
        //product code
        String productCode = cmsBtProductModel.getCommon().getFields().getCode();
        //product model
        String productModel = cmsBtProductModel.getCommon().getFields().getModel();
        // sku code
        List<CmsBtProductModel_Sku> skuList = cmsBtProductModel.getCommon().getSkus();
        List<String> skuCodeList = new ArrayList<>();
        if (skuList != null && !skuList.isEmpty()) {
            skuCodeList.addAll(skuList.stream().map(CmsBtProductModel_Sku::getSkuCode).collect(Collectors.toList()));
        }

        return createUpdate(id, productChannel, productCode, productModel, skuCodeList, null, lastVer);
    }

    /**
     * create Update Bean
     */
    public SolrUpdateBean createUpdate(String id, String productChannel,
                                       String productCode, String productModel, List<String> skuCodeList, List<String> skuCodeAddList,
                                       Long lastVer) {
        if (id == null || productChannel == null
                || (productCode == null && productModel == null && skuCodeList == null && skuCodeAddList == null)) {
            return null;
        }

        SolrUpdateBean update = new SolrUpdateBean("id", id);

        update.add("productChannel", productChannel);
        if (productCode != null) {
            update.add("productCode", productCode);
        }

        if (productModel != null) {
            update.add("productModel", productModel);
        }

        if (skuCodeList != null) {
            update.add("skuCode", skuCodeList);
        }

        if (skuCodeAddList != null) {
            for (String skuCode : skuCodeAddList) {
                update.addValueToField("skuCode", skuCode);
            }
        }

        if (lastVer != null) {
            update.add("lastVer", lastVer);
        }

        return update;
    }

    /**
     * queryForCursorByLastVer
     */
    public SimpleQueryCursor<CommIdSearchModel> queryIdsForCursorNotLastVer(String channelId, long lasVer) {
        String queryString = String.format("productChannel:\"%s\" && -lastVer:\"%s\"", channelId, lasVer);
        SimpleQueryBean query = new SimpleQueryBean(queryString);
        query.addSort(new Sort(Sort.Direction.DESC, "id"));
        query.addProjectionOnField("id");
        //noinspection unchecked
        return new SimpleQueryCursor(queryForCursor(query, CommIdSearchModel.class));
    }


    private <T> Cursor<T> queryForCursor(Query query, final Class<T> clazz) {
        return getSolrTemplate().queryForCursor(query, clazz);
    }

    public <T> Page<T> queryForPage(Query query, final Class<T> clazz) {
        return getSolrTemplate().queryForPage(query, clazz);
    }

    public <T> SolrResultPage<T> queryForSolrResultPage(Query query, final Class<T> clazz) {
        //noinspection unchecked
        return (SolrResultPage)getSolrTemplate().queryForPage(query, clazz);
    }
}
