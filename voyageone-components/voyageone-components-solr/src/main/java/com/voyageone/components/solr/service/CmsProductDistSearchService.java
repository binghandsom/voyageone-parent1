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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final Pattern stringFormatP = Pattern.compile("\t|\r|\n");

    @Override
    protected String getSolrTemplateName() {
        return SOLR_TEMPLATE_NAME;
    }

    /**
     * create Update Bean
     */
    public SolrUpdateBean createSolrBeanForNew(CmsBtProductModel cmsBtProductModel, Long lastVer) {
        CmsProductDistSearchModel model = createSolrSearchModelForNew(cmsBtProductModel, lastVer);
        return createSolrBean(model, cmsBtProductModel.get_id(), true);
    }

    /**
     * create Solr Search Model For New
     */
    public CmsProductDistSearchModel createSolrSearchModelForNew(CmsBtProductModel cmsBtProductModel, Long lastVer) {
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
                    model.setNameEn(replaceBlank(fields.getProductNameEn()));
                }
                if (!StringUtils.isEmpty(fields.getOriginalTitleCn())) {
                    model.setNameCn(replaceBlank(fields.getOriginalTitleCn()));
                }
                if (!StringUtils.isEmpty(fields.getLongDesEn())) {
                    model.setDescEn(replaceBlank(fields.getLongDesEn()));
                }
                if (!StringUtils.isEmpty(fields.getLongDesCn())) {
                    model.setDescCn(replaceBlank(fields.getLongDesCn()));
                }
                if (!StringUtils.isEmpty(fields.getColor())) {
                    model.setColor(replaceBlank(fields.getColor()));
                }
                if (!StringUtils.isEmpty(fields.getBrand())) {
                    model.setBrandEn(replaceBlank(fields.getBrand()));
                }

                //private String brandCn;
                //private String catEn;

                //String catCode = null;
                String catCn = "其他";
                String hsCodePrivate = fields.getHsCodePrivate();
                //noinspection Duplicates
                if (!StringUtils.isEmpty(hsCodePrivate)) {
                    String[] hsCodePrivateArr = hsCodePrivate.split(",");
                    if (hsCodePrivateArr.length > 2) {
                        //catCode = hsCodePrivateArr[0];
                        catCn = hsCodePrivateArr[1];
                    }
                }
                model.addCatCn(replaceBlank(catCn));

                model.setSalePrice(fields.getPriceRetailSt());
                List<CmsBtProductModel_Field_Image> productImageList = fields.getImages(CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE);
                if (productImageList != null && !productImageList.isEmpty()) {
                    model.setImageLink(productImageList.get(0).getName());
                }
            }

            //saleCount;
            int saleCount = 0;
            if (cmsBtProductModel.getSales() != null) {
                CmsBtProductModel_Sales salesModel = cmsBtProductModel.getSales();
                @SuppressWarnings("unchecked")
                Map<String, Object> codeSum30Model = salesModel.getAttribute(CmsBtProductModel_Sales.CODE_SUM_30);
                //noinspection Duplicates
                if (codeSum30Model != null) {
                    Object count = codeSum30Model.get("cartId0");
                    if (count instanceof Integer) {
                        saleCount = saleCount + ((Integer)count);
                    } else if (count instanceof Long) {
                        saleCount = saleCount + ((Long) count).intValue();
                    }
                }
            }
            model.setSaleCount(saleCount);

            //pv sum
            int pv = 0;
            //uv sum
            int uv = 0;
            if (cmsBtProductModel.getBi() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> biModel = cmsBtProductModel.getBi();
                @SuppressWarnings("unchecked")
                Map<String, Object> sum30Map = (Map<String, Object>) biModel.get("sum30");
                //noinspection Duplicates
                if (sum30Map != null) {
                    long pvSum = 0;
                    long uvSum = 0;
                    //pv
                    @SuppressWarnings("unchecked")
                    Map<String, Object> pvMap = (Map<String, Object>) sum30Map.get("pv");
                    //noinspection Duplicates
                    if (pvMap != null) {
                        for (Object pvObj : pvMap.values()) {
                            if (pvObj instanceof Integer) {
                                pvSum = pvSum + ((Integer)pvObj);
                            } else if (pvObj instanceof Long) {
                                pvSum = pvSum + (Long) pvObj;
                            }
                        }
                    }
                    //uv
                    @SuppressWarnings("unchecked")
                    Map<String, Object> uvMap = (Map<String, Object>) sum30Map.get("uv");
                    //noinspection Duplicates
                    if (uvMap != null) {
                        for (Object uvObj : uvMap.values()) {
                            if (uvObj instanceof Integer) {
                                uvSum = uvSum + ((Integer)uvObj);
                            } else if (uvObj instanceof Long) {
                                uvSum = uvSum + (Long) uvObj;
                            }
                        }
                    }

                    pv = ((Long)pvSum).intValue();
                    uv = ((Long)pvSum).intValue();
                }
            }
            model.setPv(pv);
            model.setUv(uv);
        }

        if (lastVer != null) {
            model.setLastVer(lastVer);
        }

        reorganizeModel(model);
        return model;
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
                    model.setNameEn(replaceBlank(productNameEn));
                }

                String originalTitleCn = (String) fieldsDoc.get("originalTitleCn");
                if (!StringUtils.isEmpty(originalTitleCn)) {
                    model.setNameCn(replaceBlank(originalTitleCn));
                }

                String longDesEn = (String) fieldsDoc.get("longDesEn");
                if (!StringUtils.isEmpty(longDesEn)) {
                    model.setDescEn(replaceBlank(longDesEn));
                }

                String longDesCn = (String) fieldsDoc.get("longDesCn");
                if (!StringUtils.isEmpty(longDesCn)) {
                    model.setDescCn(replaceBlank(longDesCn));
                }

                String color = (String) fieldsDoc.get("color");
                if (!StringUtils.isEmpty(color)) {
                    model.setColor(replaceBlank(color));
                }

                String brand = (String) fieldsDoc.get("brand");
                if (!StringUtils.isEmpty(brand)) {
                    model.setBrandEn(replaceBlank(brand));
                }

                //private String brandCn;
                //private String catEn;

                //String catCode = null;
                String catCn = "其他";
                String hsCodePrivate = (String) fieldsDoc.get("hsCodePrivate");
                //noinspection Duplicates
                if (!StringUtils.isEmpty(hsCodePrivate)) {
                    String[] hsCodePrivateArr = hsCodePrivate.split(",");
                    if (hsCodePrivateArr.length > 2) {
                        //catCode = hsCodePrivateArr[0];
                        catCn = hsCodePrivateArr[1];
                    }
                }
                model.addCatCn(replaceBlank(catCn));

                Double priceRetailSt = convertToDouble(fieldsDoc.get("priceRetailSt"));
                if (priceRetailSt != null) {
                    model.setSalePrice(priceRetailSt);
                }

                @SuppressWarnings("unchecked")
                List<Document> productImageList = (List<Document>) fieldsDoc.get("images1");
                if (productImageList != null && !productImageList.isEmpty()) {
                    Document productImage = productImageList.get(0);
                    model.setImageLink((String) productImage.get("image1"));
                }
            }

            //saleCount;
            int saleCount = 0;
            Document salesDoc = (Document) objectDoc.get("sales");
            if (salesDoc != null) {
                @SuppressWarnings("unchecked")
                Document codeSum30Model = (Document)salesDoc.get(CmsBtProductModel_Sales.CODE_SUM_30);
                //noinspection Duplicates
                if (codeSum30Model != null) {
                    Object count = codeSum30Model.get("cartId0");
                    if (count instanceof Integer) {
                        saleCount = saleCount + ((Integer)count);
                    } else if (count instanceof Long) {
                        saleCount = saleCount + ((Long) count).intValue();
                    }
                }
            }
            model.setSaleCount(saleCount);

            //pv sum
            int pv = 0;
            //uv sum
            int uv = 0;
            Document biDoc = (Document) objectDoc.get("bi");
            if (biDoc != null) {
                @SuppressWarnings("unchecked")
                Document sum30Map = (Document) biDoc.get("sum30");
                //noinspection Duplicates
                if (sum30Map != null) {
                    long pvSum = 0;
                    long uvSum = 0;
                    //pv
                    @SuppressWarnings("unchecked")
                    Map<String, Object> pvMap = (Map<String, Object>) sum30Map.get("pv");
                    //noinspection Duplicates
                    if (pvMap != null) {
                        for (Object pvObj : pvMap.values()) {
                            if (pvObj instanceof Integer) {
                                pvSum = pvSum + ((Integer)pvObj);
                            } else if (pvObj instanceof Long) {
                                pvSum = pvSum + (Long) pvObj;
                            }
                        }
                    }
                    //uv
                    @SuppressWarnings("unchecked")
                    Map<String, Object> uvMap = (Map<String, Object>) sum30Map.get("uv");
                    //noinspection Duplicates
                    if (uvMap != null) {
                        for (Object uvObj : uvMap.values()) {
                            if (uvObj instanceof Integer) {
                                uvSum = uvSum + ((Integer)uvObj);
                            } else if (uvObj instanceof Long) {
                                uvSum = uvSum + (Long) uvObj;
                            }
                        }
                    }

                    pv = ((Long)pvSum).intValue();
                    uv = ((Long)pvSum).intValue();
                }
            }
            model.setPv(pv);
            model.setUv(uv);
        }

        if (lastVer != null) {
            model.setLastVer(lastVer);
        }

        reorganizeModel(model);
        return createSolrBean(model, id, false);
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
                model.setNameEn(replaceBlank(productNameEn));
            }

            String originalTitleCn = (String) getDataFromDocument(setDoc, "common.fields.originalTitleCn");
            if (!StringUtils.isEmpty(originalTitleCn)) {
                model.setNameCn(replaceBlank(originalTitleCn));
            }

            String longDesEn = (String) getDataFromDocument(setDoc, "common.fields.longDesEn");
            if (!StringUtils.isEmpty(longDesEn)) {
                model.setDescEn(replaceBlank(longDesEn));
            }

            String longDesCn = (String) getDataFromDocument(setDoc, "common.fields.longDesCn");
            if (!StringUtils.isEmpty(longDesCn)) {
                model.setDescCn(replaceBlank(longDesCn));
            }

            String color = (String) getDataFromDocument(setDoc, "common.fields.color");
            if (!StringUtils.isEmpty(color)) {
                model.setColor(replaceBlank(color));
            }

            String brand = (String) getDataFromDocument(setDoc, "common.fields.brand");
            if (!StringUtils.isEmpty(brand)) {
                model.setBrandEn(replaceBlank(brand));
            }

            //private String brandCn;
            //private String catEn;

            //String catCode = null;
            String catCn = "其他";
            String hsCodePrivate = (String) getDataFromDocument(setDoc, "common.fields.hsCodePrivate");
            //noinspection Duplicates
            if (!StringUtils.isEmpty(hsCodePrivate)) {
                String[] hsCodePrivateArr = hsCodePrivate.split(",");
                if (hsCodePrivateArr.length > 2) {
                    //catCode = hsCodePrivateArr[0];
                    catCn = hsCodePrivateArr[1];
                }
            }
            model.addCatCn(replaceBlank(catCn));

            Double priceRetailSt = (Double) getDataFromDocument(setDoc, "common.fields.priceRetailSt");
            if (priceRetailSt != null) {
                model.setSalePrice(priceRetailSt);
            }

            @SuppressWarnings("unchecked")
            List<Document> productImageList = (List<Document>) getDataFromDocument(setDoc, "common.fields.images1");
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

        reorganizeModel(model);
        return createSolrBean(model, id, false);
    }


    /**
     * create Update Bean
     */
    public SolrUpdateBean createSolrBean(CmsProductDistSearchModel model, String id, boolean isNew) {
        if (model == null || id == null) {
            return null;
        }

        Map<String, Object> modelMap = BeanUtils.toMap(model);

        SolrUpdateBean update = new SolrUpdateBean("id", id);
        modelMap.entrySet().stream().filter(entry -> entry.getValue() != null).forEach(entry -> {
            if (!isNew && entry.getValue() instanceof Collection) {
                for (Object coll : (Collection) entry.getValue()) {
                    update.addValueToField(entry.getKey(), coll);
                }
            } else {
                update.add(entry.getKey(), entry.getValue());
            }
        });

        return update;
    }

    private void reorganizeModel(CmsProductDistSearchModel model) {
        Set<String> brandCatSet = new HashSet<>();
        List<String> brands = new ArrayList<>();
        if (model.getBrandEn() != null) {
            brandCatSet.add(model.getBrandEn());
            brands.add(model.getBrandEn());
        }
        if (model.getBrandCn() != null) {
            brandCatSet.add(model.getBrandCn());
            brands.add(model.getBrandCn());
        }

        List<String> cats = new ArrayList<>();
        if (model.getCatEns() != null && !model.getCatEns().isEmpty()) {
            brandCatSet.addAll(model.getCatEns());
            cats.addAll(model.getCatEns());
        }
        if (model.getCatCns() != null && !model.getCatCns().isEmpty()) {
            brandCatSet.addAll(model.getCatCns());
            cats.addAll(model.getCatCns());
        }

        for (String brand : brands) {
            for (String cat : cats) {
                brandCatSet.add(brand + cat);
                brandCatSet.add(cat + brand);
            }
        }

        if (!brandCatSet.isEmpty()) {
            model.setBrandCats(brandCatSet);
        }

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

    private String replaceBlank(String str) {
        if (str != null) {
            Matcher m = stringFormatP.matcher(str);
            return m.replaceAll(" ");
        }
        return null;
    }

    private Double convertToDouble(Object data) {
        if (data != null) {
            if (data instanceof Double) {
                return (Double) data;
            } else {
                try {
                    return Double.parseDouble(String.valueOf(data));
                } catch (Exception ex) {
                    return null;
                }
            }
        }
        return null;
    }
}
