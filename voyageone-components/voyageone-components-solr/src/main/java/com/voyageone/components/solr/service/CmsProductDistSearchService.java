package com.voyageone.components.solr.service;

import com.voyageone.common.util.BeanUtils;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.solr.BaseSearchService;
import com.voyageone.components.solr.bean.CmsProductDistSearchModel;
import com.voyageone.components.solr.bean.CommIdSearchModel;
import com.voyageone.components.solr.bean.SolrUpdateBean;
import com.voyageone.components.solr.query.SimpleQueryBean;
import com.voyageone.components.solr.query.SimpleQueryCursor;
import com.voyageone.service.model.cms.mongo.product.*;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.result.Cursor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Cms Product Distribution Search Service
 *
 * @author chuanyu.liang 2016/10/20
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CmsProductDistSearchService extends BaseSearchService {

    private static final String SOLR_TEMPLATE_NAME = "cmsProductSolrDistTemplate";

    @Override
    protected String getSolrTemplateName() {
        return SOLR_TEMPLATE_NAME;
    }

    /**
     * create Update Bean
     */
    public SolrUpdateBean createSolrBeanForNew(CmsBtProductModel cmsBtProductModel, Long lastVer) {
        if (cmsBtProductModel == null) {
            return null;
        }
        CmsProductDistSearchModel model = new CmsProductDistSearchModel();
        //channelId
        model.setChannelId(cmsBtProductModel.getChannelId());
        //onDate
        if (cmsBtProductModel.getCreated() != null) {
            String created = cmsBtProductModel.getCreated();
            String createdDate = DateTimeUtil.format(DateTimeUtil.parse(created), "yyyyMMdd");
            model.setOnDate(Integer.parseInt(createdDate));
        }

        if (cmsBtProductModel.getCommon() != null) {
            CmsBtProductModel_Common common = cmsBtProductModel.getCommon();
            if (common.getFields() != null) {
                CmsBtProductModel_Field fields = common.getFields();

                //product code
                model.setCode(fields.getCode());
                if (!StringUtils.isEmpty(fields.getProductNameEn())) {
                    model.setNameEn(fields.getProductNameEn());
                }
                if (!StringUtils.isEmpty(fields.getOriginalTitleCn())) {
                    model.setNameCn(fields.getOriginalTitleCn());
                }
                if (!StringUtils.isEmpty(fields.getLongDesEn())) {
                    model.setDescEn(fields.getLongDesEn());
                }
                if (!StringUtils.isEmpty(fields.getLongDesCn())) {
                    model.setDescCn(fields.getLongDesCn());
                }
                if (!StringUtils.isEmpty(fields.getColor())) {
                    model.setColor(fields.getColor());
                }
                if (!StringUtils.isEmpty(fields.getBrand())) {
                    model.setBrandEn(fields.getBrand());
                }
                //private String brandCn;
                model.setSalePrice(fields.getPriceRetailSt());
                List<CmsBtProductModel_Field_Image> productImageList = fields.getImages(CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE);
                if (productImageList != null && !productImageList.isEmpty()) {
                    model.setImageLink(productImageList.get(0).getName());
                }

                //private Integer pv;
                //private Integer uv;
                //private Integer saleCount;
            }

//            CmsBtProductModel_Platform_Cart tgPlatformCart = cmsBtProductModel.getPlatform(CartEnums.Cart.TG);
//            if (tgPlatformCart != null) {
//                BaseMongoMap<String, Object> fields = tgPlatformCart.getFields();
//                if (fields != null) {
//                    //private String catEn;
//                    //private String catCn;
//                }
//            }

        }

        if (lastVer != null) {
            model.setLastVer(lastVer);
        }

        return createSolrBean(model, cmsBtProductModel.get_id());
    }

    /**
     * create Update Bean
     */
    public SolrUpdateBean createSolrBeanForNew(Document objectDoc, Long lastVer) {
        if (objectDoc == null) {
            return null;
        }
        CmsProductDistSearchModel model = new CmsProductDistSearchModel();
        String id = objectDoc.get("_id").toString();
        model.setChannelId((String) objectDoc.get("channelId"));

        //onDate
        if (objectDoc.get("created") != null) {
            String created = (String) objectDoc.get("created");
            String createdDate = DateTimeUtil.format(DateTimeUtil.parse(created), "yyyyMMdd");
            model.setOnDate(Integer.parseInt(createdDate));
        }

        Document commonDoc = (Document) objectDoc.get("common");
        //noinspection Duplicates
        if (commonDoc != null) {
            Document fieldsDoc = (Document) commonDoc.get("fields");
            if (fieldsDoc != null) {
                model.setCode((String) fieldsDoc.get("code"));

                String productNameEn = (String) fieldsDoc.get("productNameEn");
                if (!StringUtils.isEmpty(productNameEn)) {
                    model.setNameEn(productNameEn);
                }

                String originalTitleCn = (String) fieldsDoc.get("originalTitleCn");
                if (!StringUtils.isEmpty(originalTitleCn)) {
                    model.setNameCn(originalTitleCn);
                }

                String longDesEn = (String) fieldsDoc.get("longDesEn");
                if (!StringUtils.isEmpty(longDesEn)) {
                    model.setDescEn(longDesEn);
                }

                String longDesCn = (String) fieldsDoc.get("longDesCn");
                if (!StringUtils.isEmpty(longDesCn)) {
                    model.setDescCn(longDesCn);
                }

                String color = (String) fieldsDoc.get("color");
                if (!StringUtils.isEmpty(color)) {
                    model.setColor(color);
                }

                String brand = (String) fieldsDoc.get("brand");
                if (!StringUtils.isEmpty(brand)) {
                    model.setBrandEn(brand);
                }

                //private String brandCn;


                Double priceRetailSt = null;
                if(fieldsDoc.get("priceRetailSt") instanceof Integer){
                    priceRetailSt = Double.parseDouble(fieldsDoc.get("priceRetailSt").toString());
                }else{
                    priceRetailSt = (Double) fieldsDoc.get("priceRetailSt");
                }
                if (priceRetailSt != null) {
                    model.setSalePrice(priceRetailSt);
                }

                @SuppressWarnings("unchecked")
                List<Document> productImageList = (List<Document>) fieldsDoc.get("images1");
                if (productImageList != null && !productImageList.isEmpty()) {
                    Document productImage = productImageList.get(0);
                    model.setImageLink((String) productImage.get("image1"));
                }

                //private Integer pv;
                //private Integer uv;
                //private Integer saleCount;
            }
        }

//            CmsBtProductModel_Platform_Cart tgPlatformCart = cmsBtProductModel.getPlatform(CartEnums.Cart.TG);
//            if (tgPlatformCart != null) {
//                BaseMongoMap<String, Object> fields = tgPlatformCart.getFields();
//                if (fields != null) {
//                    //private String catEn;
//                    //private String catCn;
//                }
//            }
        if (lastVer != null) {
            model.setLastVer(lastVer);
        }

        return createSolrBean(model, id);
    }

    /**
     * create Solr Update Bean
     */
    public SolrUpdateBean createSolrBeanForUpdate(Document document, Long lastVer) {
        if (document == null) {
            return null;
        }
        CmsProductDistSearchModel model = new CmsProductDistSearchModel();

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
        model.setChannelId(productChannel);

        Document objectDoc = (Document) document.get("o");
        Document setDoc = (Document) objectDoc.get("$set");
        if (setDoc != null) {

            //onDate
            if (setDoc.get("created") != null) {
                String created = (String) setDoc.get("created");
                String createdDate = DateTimeUtil.format(DateTimeUtil.parse(created), "yyyyMMdd");
                model.setOnDate(Integer.parseInt(createdDate));
            }

            String productCode = (String) getDataFromDocument(setDoc, "common.fields.code");
            if (!StringUtils.isEmpty(productCode)) {
                model.setCode(productCode);
            }

            String productNameEn = (String) getDataFromDocument(setDoc, "common.fields.productNameEn");
            if (!StringUtils.isEmpty(productNameEn)) {
                model.setNameEn(productNameEn);
            }

            String originalTitleCn = (String) getDataFromDocument(setDoc, "common.fields.originalTitleCn");
            if (!StringUtils.isEmpty(originalTitleCn)) {
                model.setNameCn(originalTitleCn);
            }

            String longDesEn = (String) getDataFromDocument(setDoc, "common.fields.longDesEn");
            if (!StringUtils.isEmpty(longDesEn)) {
                model.setDescEn(longDesEn);
            }

            String longDesCn = (String) getDataFromDocument(setDoc, "common.fields.longDesCn");
            if (!StringUtils.isEmpty(longDesCn)) {
                model.setDescCn(longDesCn);
            }

            String color = (String) getDataFromDocument(setDoc, "common.fields.color");
            if (!StringUtils.isEmpty(color)) {
                model.setColor(color);
            }

            String brand = (String) getDataFromDocument(setDoc, "common.fields.brand");
            if (!StringUtils.isEmpty(brand)) {
                model.setBrandEn(brand);
            }

            //private String brandCn;

            Double priceRetailSt = (Double) getDataFromDocument(setDoc, "common.fields.priceRetailSt");
            if (priceRetailSt != null) {
                model.setSalePrice(priceRetailSt);
            }

            @SuppressWarnings("unchecked")
            List<Document> productImageList = (List<Document>)getDataFromDocument(setDoc, "common.fields.images1");
            if (productImageList != null && !productImageList.isEmpty()) {
                Document productImage = productImageList.get(0);
                model.setImageLink((String) productImage.get("image1"));
            } else {
                for (Map.Entry<String, Object> entry : setDoc.entrySet()) {
                    String key = entry.getKey();
                    if (key != null && key.startsWith("common.fields.images1.0") && key.endsWith(".image1")) {
                        model.setImageLink((String.valueOf(entry.getValue())));
                    }
                }
            }

            //private Integer pv;
            //private Integer uv;
            //private Integer saleCount;
        }

        if (lastVer != null) {
            model.setLastVer(lastVer);
        }

        return createSolrBean(model, id);
    }


    /**
     * create Update Bean
     */
    private SolrUpdateBean createSolrBean(CmsProductDistSearchModel model, String id) {
        if (model == null || id == null) {
            return null;
        }

        Map<String, Object> modelMap = BeanUtils.toMap(model);

        SolrUpdateBean update = new SolrUpdateBean("id", id);
        modelMap.entrySet().stream().filter(entry -> entry.getValue() != null).forEach(entry -> {
            update.add(entry.getKey(), entry.getValue());
        });

        return update;
    }

    /**
     * queryForCursorByLastVer
     */
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

//    public <T> Page<T> queryForPage(Query query, final Class<T> clazz) {
//        return getSolrTemplate().queryForPage(query, clazz);
//    }
//
//    public <T> SolrResultPage<T> queryForSolrResultPage(Query query, final Class<T> clazz) {
//        //noinspection unchecked
//        return (SolrResultPage) getSolrTemplate().queryForPage(query, clazz);
//    }
}
