package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.category.match.*;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.ProductPlatformService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductStatusHistoryService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * if 主类目人工设置FLG（common.catConf = 1 ）
    -》只修改主类目
    else if 主类目人工设置FLG（common.catConf 不存在 或 = 0）
    -》更新 主类目，产品分类，使用人群，税号个人，税号跨境申报
    -》设置 主类目人工设置FLG（common.catConf = 1 ）
 * Created by james on 2016/12/19.
 */
@Service
@RabbitListener(queues = CmsMqRoutingKey.CMS_BATCH_CmsBatchSetMainCategoryJob)
public class CmsBatchSetMainCategoryMqService extends BaseMQCmsService {

    final ProductService productService;

    final CmsBtProductDao cmsBtProductDao;

    private final ProductStatusHistoryService productStatusHistoryService;

    private final PriceService priceService;

    private final ProductPlatformService productPlatformService;

    private final Searcher searcher;



    @Autowired
    public CmsBatchSetMainCategoryMqService(ProductService productService, CmsBtProductDao cmsBtProductDao, ProductStatusHistoryService productStatusHistoryService, PriceService priceService, ProductPlatformService productPlatformService, Searcher searcher) {
        this.productService = productService;
        this.cmsBtProductDao = cmsBtProductDao;
        this.productStatusHistoryService = productStatusHistoryService;
        this.priceService = priceService;
        this.productPlatformService = productPlatformService;
        this.searcher = searcher;
    }

//    if 主类目人工设置FLG（common.catConf = 1 ）
//            -》只修改主类目
//else if 主类目人工设置FLG（common.catConf 不存在 或 = 0）
//            -》更新 主类目，产品分类，使用人群，税号个人，税号跨境申报
//-》设置 主类目人工设置FLG（common.catConf = 1 ）
    @Override
    protected void onStartup(Map<String, Object> requestMap) throws Exception {
        // 获取参数
        String mCatId = StringUtils.trimToNull((String) requestMap.get("catId"));
        String mCatPathCn = StringUtils.trimToNull((String) requestMap.get("catPath"));
        String mCatPathEn = StringUtils.trimToNull((String) requestMap.get("catPathEn"));
        List<String> productCodes = (List<String>) requestMap.get("prodIds");
        String userName = (String) requestMap.get("userName");
        String channelId = (String) requestMap.get("channelId");
        String productTypeEn = (String) requestMap.get("productType");
        String sizeTypeEn = (String) requestMap.get("sizeType");
        String productTypeCn = (String) requestMap.get("productTypeCn");
        String sizeTypeCn = (String) requestMap.get("sizeTypeCn");
        String hscodeName8 = (String) requestMap.get("hscodeName8");
        String hscodeName10 = (String) requestMap.get("hscodeName10");



        if (mCatId == null || mCatPathCn == null || ListUtils.isNull(productCodes)) {
            $warn("切换类目 缺少参数 params=" + requestMap.toString());
            return;
        }
        $info(String.format("channel=%s mCatPath=%s prodCodes size=%d", channelId, mCatPathCn, productCodes.size()));
        productCodes.forEach(code -> {
                    String sizeType = "";
                    CmsBtProductModel cmsBtProductModel = productService.getProductByCode(channelId, code);
                    if (cmsBtProductModel != null) {

                        HashMap<String, Object> queryMap = new HashMap<>();
                        queryMap.put("common.fields.code", code);
                        List<BulkUpdateModel> bulkList = new ArrayList<>();
                        HashMap<String, Object> updateMap = new HashMap<>();

                        updateMap.put("common.catId", mCatId);
                        updateMap.put("common.catPath", mCatPathCn);
                        updateMap.put("common.catPathEn", mCatPathEn);
                        updateMap.put("common.catConf", "1");
                        updateMap.put("common.fields.categoryStatus", "1");
                        updateMap.put("common.fields.categorySetter", userName);
                        updateMap.put("common.fields.categorySetTime", DateTimeUtil.getNow());
                        if("0".equalsIgnoreCase(cmsBtProductModel.getCommon().getCatConf())){
                            updateMap.put("common.fields.productType", getString(productTypeEn));
                            updateMap.put("common.fields.productTypeCn", getString(productTypeCn));

                            MatchResult matchResult = getSizeType(cmsBtProductModel);
                            if(matchResult != null){
                                updateMap.put("common.fields.sizeType", StringUtil.isEmpty(matchResult.getSizeTypeEn())?sizeTypeEn:matchResult.getSizeTypeEn());
                                updateMap.put("common.fields.sizeTypeCn", StringUtil.isEmpty(matchResult.getSizeTypeCn())?sizeTypeCn:matchResult.getSizeTypeCn());
                                sizeType = StringUtil.isEmpty(matchResult.getSizeTypeCn())?sizeTypeCn:matchResult.getSizeTypeCn();
                            }else{
                                updateMap.put("common.fields.sizeType", getString(sizeTypeEn));
                                updateMap.put("common.fields.sizeTypeCn", getString(sizeTypeCn));
                                sizeType = sizeTypeCn;
                            }
                            updateMap.put("common.fields.hsCodePrivate", getString(hscodeName8));
                            updateMap.put("common.fields.hsCodeCross", getString(hscodeName10));
                            if (!StringUtil.isEmpty(hscodeName8)) {
                                updateMap.put("common.fields.hsCodeStatus", "1");
                                updateMap.put("common.fields.hsCodeSetter", userName);
                                updateMap.put("common.fields.hsCodeSetTime", DateTimeUtil.getNow());
                            }else{
                                updateMap.put("common.fields.hsCodeStatus", "0");
                                updateMap.put("common.fields.hsCodeSetter", "");
                                updateMap.put("common.fields.hsCodeSetTime", "");
                            }
                        }else {
                            if(StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getHsCodePrivate()) && !StringUtil.isEmpty(hscodeName8)){
                                updateMap.put("common.fields.hsCodePrivate", getString(hscodeName8));
                                updateMap.put("common.fields.hsCodeStatus", "1");
                                updateMap.put("common.fields.hsCodeSetter", userName);
                                updateMap.put("common.fields.hsCodeSetTime", DateTimeUtil.getNow());
                            }
                            if(StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getHsCodeCross()) && !StringUtil.isEmpty(hscodeName10)){
                                updateMap.put("common.fields.hsCodeCross", getString(hscodeName10));
                            }
                            sizeType = cmsBtProductModel.getCommonNotNull().getFieldsNotNull().getSizeTypeCn();
                        }

                        if (StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getOriginalTitleCn()) || !"1".equalsIgnoreCase(cmsBtProductModel.getCommon().getFields().getTranslateStatus())) {
                            // 设置商品中文名称（品牌 + 空格 + Size Type中文 + 空格 + 主类目叶子级中文名称）
                            String[] temp = mCatPathCn.split(">");
                            String titleCn = String.format("%s %s %s",getString(cmsBtProductModel.getCommon().getFields().getBrand()), getString(sizeType), temp[temp.length-1]);
                            updateMap.put("common.fields.originalTitleCn", titleCn);
                        }
                        BulkUpdateModel model = new BulkUpdateModel();
                        model.setUpdateMap(updateMap);
                        model.setQueryMap(queryMap);
                        bulkList.add(model);
                        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, userName, "$set");
                        if(!productService.compareHsCode(hscodeName8, cmsBtProductModel.getCommon().getFields().getHsCodePrivate())) {
                            try{
                                CmsBtProductModel newProduct = productService.getProductByCode(channelId, code);
                                // 税号从无到有的场后同步最终售价
                                if (StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getHsCodePrivate())) {
                                    priceService.setPrice(newProduct, true);
                                } else {
                                    priceService.setPrice(newProduct, false);
                                }
                                newProduct.getPlatforms().forEach((s, platform) -> {
                                    if (platform.getCartId() != 0) {
                                        productPlatformService.updateProductPlatform(channelId, newProduct.getProdId(), platform, userName, false, EnumProductOperationType.changeMainCategory, "批量修改主类目税号变更", true, 1);
                                    }
                                });
                            }catch (Exception e){
                                $error(e);
                            }
                        }
                        productStatusHistoryService.insert(channelId, code, "", 0, EnumProductOperationType.changeMainCategory, "修改主类目为" + mCatPathCn, userName);
                    }
                }
        );
    }

    private String getString(String str){
        if(str == null) return "";
        return str;
    }

    public MatchResult getSizeType(CmsBtProductModel cmsBtProductModel){
        FeedQuery query = getFeedQuery(cmsBtProductModel.getFeed().getCatPath(), cmsBtProductModel.getCommonNotNull().getFieldsNotNull().getOrigProductType(), cmsBtProductModel.getCommonNotNull().getFieldsNotNull().getOrigSizeType(), cmsBtProductModel.getCommonNotNull().getFieldsNotNull().getProductNameEn(), "");

        // 调用主类目匹配接口，取得匹配度最高的一个主类目和sizeType
        MatchResult searchResult = searcher.search(query, true);

        return searchResult;
    }

    private FeedQuery getFeedQuery(String feedCategoryPath, String productType, String sizeType, String productNameEn, String brand) {
        // 调用Feed到主数据的匹配程序匹配主类目
        StopWordCleaner cleaner = new StopWordCleaner(StopWordCleaner.STOPWORD_LIST);
        // 子店feed类目path分隔符(由于导入feedInfo表时全部替换成用"-"来分隔了，所以这里写固定值就可以了)
        List<String> categoryPathSplit = new ArrayList<>();
        categoryPathSplit.add("-");
        Tokenizer tokenizer = new Tokenizer(categoryPathSplit);

        FeedQuery query = new FeedQuery(feedCategoryPath, cleaner, tokenizer);
        query.setProductType(productType);
        query.setSizeType(sizeType);
        query.setProductName(productNameEn, brand);

        return query;
    }
}
