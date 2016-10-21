package com.voyageone.task2.cms.service.search;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.solr.bean.SolrUpdateBean;
import com.voyageone.components.solr.service.CmsProductDistSearchService;
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
class CmsProductIncrImportToDistSearchService extends CmsBaseIncrImportSearchSubService {

    private static final String CMS_CHANNEL_ID = "928";

    @Autowired
    private CmsProductDistSearchService cmsProductDistSearchService;

    private boolean checkChannelId(String channelId) {
        return CMS_CHANNEL_ID.equals(channelId);
    }
    /**
     * handleInsert
     */
    @Override
    protected boolean handleInsert(Document document) {
        $debug("CmsProductIncrImportToDistSearchService.handleInsert" + document.toJson());
        Document objectDoc = (Document) document.get("o");
        // check channelId
        String channelId = (String)objectDoc.get("channelId");
        if (!checkChannelId(channelId)) {
            return false;
        }
        // create update
        SolrUpdateBean update = cmsProductDistSearchService.createSolrBeanForNew(objectDoc, null);
        if (update != null) {
            String response = cmsProductDistSearchService.saveBean(update);
            $debug("CmsProductIncrImportToDistSearchService.handleInsert commit ; response:" + response);
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
        $debug("CmsProductIncrImportToDistSearchService.handleUpdate:" + document.toJson());
        Document objectDoc = (Document) document.get("o");
        if (objectDoc == null) {
            return false;
        }

        // check channelId
        String channelId = null;
        String nsStr = ((String) document.get("ns"));
        if (nsStr != null && nsStr.length() > 4) {
            channelId = nsStr.substring(nsStr.length() - 3, nsStr.length());
        }
        if (!checkChannelId(channelId)) {
            return false;
        }
        // create update

        SolrUpdateBean update;
        if (objectDoc.containsKey("$set")) {
            update = cmsProductDistSearchService.createSolrBeanForUpdate(document, null);
        } else {
            // create update
            update = cmsProductDistSearchService.createSolrBeanForNew(objectDoc, null);
        }

        if (update != null) {
            String response = cmsProductDistSearchService.saveBean(update);
            $debug("CmsProductIncrImportToDistSearchService.handleUpdate commit ; response" + response);
            return true;
        }
        return false;
    }

    /**
     * handleDelete
     */
    @Override
    protected boolean handleDelete(Document document) {
        $debug("CmsProductIncrImportToDistSearchService.handleDelete:" + document.toJson());
        Document objectDoc = (Document) document.get("o");
        if (objectDoc == null) {
            return false;
        }

//        // check channelId
//        String channelId = (String)objectDoc.get("channelId");
//        if (!checkChannelId(channelId)) {
//            return false;
//        }
//        // create update

        String id = null;
        Object idObject = objectDoc.get("_id");
        if (idObject != null) {
            id = idObject.toString();
        }
        if (id == null) {
            return false;
        }

        String response = cmsProductDistSearchService.deleteById(id);
        $debug("CmsProductIncrImportToDistSearchService.handleDelete commit ; response:" + response);
        return true;
    }

}
