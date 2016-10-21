package com.voyageone.task2.cms.service.search;

import com.voyageone.components.solr.bean.SolrUpdateBean;
import com.voyageone.components.solr.service.CmsProductSearchService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Cms Product Data 增量导入收索服务器 Service
 *
 * @author chuanyu.liang 2016/9/30.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
class CmsProductIncrImportToCmsSearchService extends CmsBaseIncrImportSearchSubService {

    @Autowired
    private CmsProductSearchService cmsProductSearchService;

    /**
     * handleInsert
     */
    @Override
    protected boolean handleInsert(Document document) {
        $debug("CmsProductIncrImportToSearchService.handleInsert" + document.toJson());
        Document objectDoc = (Document) document.get("o");
        SolrUpdateBean update = cmsProductSearchService.createSolrBeanForNew(objectDoc, null);

        if (update != null) {
            String response = cmsProductSearchService.saveBean(update);
            $debug("CmsProductIncrImportToSearchService.handleInsert commit ; response:" + response);
            return true;
        }
        return false;
    }

    /**
     * handleUpdate
     */
    @Override
    @SuppressWarnings("Duplicates")
    protected boolean handleUpdate(Document document) {
        $debug("CmsProductIncrImportToSearchService.handleUpdate:" + document.toJson());
        Document objectDoc = (Document) document.get("o");
        if (objectDoc == null) {
            return false;
        }

        SolrUpdateBean update;
        if (objectDoc.containsKey("$set")) {
            update = cmsProductSearchService.createSolrBeanForUpdate(document, null);
        } else {
            update = cmsProductSearchService.createSolrBeanForNew(objectDoc, null);
        }

        if (update != null) {
            String response = cmsProductSearchService.saveBean(update);
            $debug("CmsProductIncrImportToSearchService.handleUpdate commit ; response" + response);
            return true;
        }
        return false;
    }

    /**
     * handleDelete
     */
    @Override
    protected boolean handleDelete(Document document) {
        $debug("CmsProductIncrImportToSearchService.handleDelete:" + document.toJson());
        Document objectDoc = (Document) document.get("o");
        if (objectDoc == null) {
            return false;
        }

        String id = null;
        Object idObject = objectDoc.get("_id");
        if (idObject != null) {
            id = idObject.toString();
        }
        if (id == null) {
            return false;
        }

        String response = cmsProductSearchService.deleteById(id);
        $debug("CmsProductIncrImportToSearchService.handleDelete commit ; response:" + response);
        return true;
    }

}
