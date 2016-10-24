package com.voyageone.components.solr.service;

import com.voyageone.components.solr.BaseSearchService;
import com.voyageone.components.solr.bean.CommIdSearchModel;
import com.voyageone.components.solr.bean.SolrUpdateBean;
import com.voyageone.components.solr.query.SimpleQueryBean;
import com.voyageone.components.solr.query.SimpleQueryCursor;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.result.Cursor;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CmsProductSearchService
 *
 * @author chuanyu.liang 2016/10/8
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CmsProductSearchService extends BaseSearchService {

    private static final String SOLR_TEMPLATE_NAME = "cmsProductSolrTemplate";

    @Override
    protected String getSolrTemplateName() {
        return SOLR_TEMPLATE_NAME;
    }

    /**
     * create Update Bean
     */
    public SolrUpdateBean createSolrBeanForNew(CmsBtProductModel cmsBtProductModel, Long lastVer) {
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

        return createSolrBean(id, productChannel, productCode, productModel, skuCodeList, null, lastVer);
    }

    /**
     * create Update Bean
     */
    public SolrUpdateBean createSolrBeanForNew(Document objectDoc, Long lastVer) {
        if (objectDoc == null) {
            return null;
        }

        String id = objectDoc.get("_id").toString();
        String productChannel = (String) objectDoc.get("channelId");
        String productCode = null;
        String productModel = null;

        Document commonDoc = (Document) objectDoc.get("common");
        List<String> skuCodeList = new ArrayList<>();
        //noinspection Duplicates
        if (commonDoc != null) {
            Document fieldsDoc = (Document) commonDoc.get("fields");
            if (fieldsDoc != null) {
                productCode = (String) fieldsDoc.get("code");
                productModel = (String) fieldsDoc.get("model");
            }
            Object skusObj = commonDoc.get("skus");
            if (skusObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Document> skuListDoc = (List<Document>) commonDoc.get("skus");
                //noinspection Duplicates
                if (skuListDoc != null) {
                    skuListDoc.stream().filter(sku -> sku != null && sku.get("skuCode") != null).forEach(sku -> {
                        String skuCode = (String) sku.get("skuCode");
                        if (skuCode.length() > 0) {
                            skuCodeList.add(skuCode);
                        }
                    });
                }
            }
        }

        return createSolrBean(id, productChannel, productCode, productModel, skuCodeList, null, null);
    }

    /**
     * create Solr Update Bean
     */
    public SolrUpdateBean createSolrBeanForUpdate(Document document, Long lastVer) {
        String id = null;
        Document object2Doc = ((Document) document.get("o2"));
        if (object2Doc != null) {
            Object idObject = object2Doc.get("_id");
            if (idObject != null) {
                id = idObject.toString();
            }
        }
        if (id == null) {
            return null;
        }

        String productChannel = null;
        String nsStr = ((String) document.get("ns"));
        if (nsStr != null && nsStr.length() > 4) {
            productChannel = nsStr.substring(nsStr.length() - 3, nsStr.length());
        }
        if (productChannel == null) {
            return null;
        }

        String productCode = null;
        String productModel = null;
        List<String> skuCodeListTmp = new ArrayList<>();
        List<String> skuCodeAddListTmp = new ArrayList<>();

        Document objectDoc = (Document) document.get("o");
        Document setDoc = (Document) objectDoc.get("$set");
        if (setDoc != null) {
            // common.fields.code
            productCode = (String) getDataFromDocument(setDoc, "common.fields.code");
            // common.fields.model
            productModel = (String) getDataFromDocument(setDoc, "common.fields.model");

            // common.fields.skus
            @SuppressWarnings("unchecked")
            List<Document> skuListDoc = (List<Document>) setDoc.get("common.skus");
            if (skuListDoc != null) {
                skuListDoc.stream().filter(sku -> sku != null && sku.get("skuCode") != null).forEach(sku -> {
                    String skuCode = (String) sku.get("skuCode");
                    if (skuCode.length() > 0) {
                        skuCodeListTmp.add(skuCode);
                    }
                });
            } else {
                for (Map.Entry<String, Object> entry : setDoc.entrySet()) {
                    String key = entry.getKey();
                    if (key != null && key.startsWith("common.skus.") && key.endsWith(".skuCode")) {
                        skuCodeAddListTmp.add(String.valueOf(entry.getValue()));
                    }
                }
            }
        }

        List<String> skuCodeList = null;
        if (!skuCodeListTmp.isEmpty()) {
            skuCodeList = new ArrayList<>();
            skuCodeList.addAll(skuCodeListTmp);
        }

        List<String> skuCodeAddList = null;
        if (!skuCodeAddListTmp.isEmpty()) {
            skuCodeAddList = new ArrayList<>();
            skuCodeAddList.addAll(skuCodeAddListTmp);
        }
        return createSolrBean(id, productChannel, productCode, productModel, skuCodeList, skuCodeAddList, null);
    }

    /**
     * create Update Bean
     */
    private SolrUpdateBean createSolrBean(String id, String productChannel,
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

    <T> SolrResultPage<T> queryForSolrResultPage(Query query, final Class<T> clazz) {
        //noinspection unchecked
        return (SolrResultPage) getSolrTemplate().queryForPage(query, clazz);
    }
}
