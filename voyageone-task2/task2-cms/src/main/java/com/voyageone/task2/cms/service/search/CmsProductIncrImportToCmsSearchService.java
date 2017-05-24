package com.voyageone.task2.cms.service.search;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.components.solr.bean.SolrUpdateBean;
import com.voyageone.components.solr.service.CmsProductSearchService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
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

    private final ProductService productService;

    private final CmsProductSearchService cmsProductSearchService;
    private final static String CORE_NAME_PRODUCT = "cms_product";
    @Autowired
    public CmsProductIncrImportToCmsSearchService(ProductService productService, CmsProductSearchService cmsProductSearchService) {
        this.productService = productService;
        this.cmsProductSearchService = cmsProductSearchService;
    }

    /**
     * handleInsert
     */
    @Override
    protected boolean handleInsert(Document document) {
        $debug("CmsProductIncrImportToSearchService.handleInsert" + document.toJson());
        String id = "";
        Document objectDoc = (Document) document.get("o");
        if (objectDoc != null) {
            Object idObject = objectDoc.get("_id");
            if (idObject != null) {
                id = idObject.toString();
            }
        }
        String channelId = "";
        String nsStr = ((String) document.get("ns"));
        if (nsStr != null && nsStr.length() > 4) {
            channelId = nsStr.substring(nsStr.length() - 3, nsStr.length());
        }


        if (!StringUtil.isEmpty(id) && !StringUtil.isEmpty(channelId)) {
//            $info(String.format("Insert channel=%s id=%s", channelId, id));
            CmsBtProductModel cmsBtProductModel = productService.getProductByObjectId(channelId, id);
            if (cmsBtProductModel != null) {
                SolrUpdateBean update = cmsProductSearchService.createSolrBeanForNew(cmsBtProductModel, null);

                if (update != null) {
                    String response = cmsProductSearchService.saveBean(CORE_NAME_PRODUCT, update);
                    $debug("CmsProductIncrImportToSearchService.handleInsert commit ; response:" + response);
                    return true;
                }
            }
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

        String channelId = "";
        String nsStr = ((String) document.get("ns"));
        if (nsStr != null && nsStr.length() > 4) {
            channelId = nsStr.substring(nsStr.length() - 3, nsStr.length());
        }

        String id = null;

        SolrUpdateBean update;
        if (objectDoc.containsKey("$set")) {
            Document object2Doc = ((Document) document.get("o2"));
            if (object2Doc != null) {
                Object idObject = object2Doc.get("_id");
                if (idObject != null) {
                    id = idObject.toString();
                }
            }
        } else {
            id = objectDoc.get("_id").toString();
        }
        if (!StringUtil.isEmpty(id) && !StringUtil.isEmpty(channelId)) {
//            $info(String.format("update channel=%s id=%s", channelId, id));
            CmsBtProductModel cmsBtProductModel = productService.getProductByObjectId(channelId, id);
            if (cmsBtProductModel != null) {
                update = cmsProductSearchService.createSolrBeanForNew(cmsBtProductModel, null);

                if (update != null) {
                    String response = cmsProductSearchService.saveBean(CORE_NAME_PRODUCT, update);
                    $debug("CmsProductIncrImportToSearchService.handleInsert commit ; response:" + response);
                    return true;
                }
            }
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

        String response = cmsProductSearchService.deleteById(CORE_NAME_PRODUCT, id);
        $debug("CmsProductIncrImportToSearchService.handleDelete commit ; response:" + response);
        return true;
    }

}
