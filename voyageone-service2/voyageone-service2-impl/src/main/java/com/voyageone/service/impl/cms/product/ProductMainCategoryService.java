package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.category.match.FeedQuery;
import com.voyageone.category.match.MatchResult;
import com.voyageone.category.match.Searcher;
import com.voyageone.category.match.Tokenizer;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsBatchSetMainCategoryMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * product Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 */
@Service
public class ProductMainCategoryService extends BaseService {

    final ProductService productService;

    final CmsBtProductDao cmsBtProductDao;

    private final ProductStatusHistoryService productStatusHistoryService;

    private final PriceService priceService;

    private final ProductPlatformService productPlatformService;

    private final Searcher searcher;

    @Autowired
    public ProductMainCategoryService(ProductService productService, CmsBtProductDao cmsBtProductDao, ProductStatusHistoryService productStatusHistoryService, PriceService priceService, ProductPlatformService productPlatformService, Searcher searcher) {
        this.productService = productService;
        this.cmsBtProductDao = cmsBtProductDao;
        this.productStatusHistoryService = productStatusHistoryService;
        this.priceService = priceService;
        this.productPlatformService = productPlatformService;
        this.searcher = searcher;
    }

    /**
     * 这只商品主类目
     *
     * @param messageBody messageBody
     * @throws Exception
     */
    public List<CmsBtOperationLogModel_Msg> setMainCategory(CmsBatchSetMainCategoryMQMessageBody messageBody) {
        // 获取参数
        String mCatId = messageBody.getCatId();
        String mCatPathCn = messageBody.getCatPath();
        String mCatPathEn = messageBody.getCatPathEn();
        List<String> productCodes = messageBody.getProductCodes();
        String userName = messageBody.getSender();
        String channelId = messageBody.getChannelId();
        String productTypeEn = messageBody.getProductType();
        String sizeTypeEn = messageBody.getSizeType();
        String productTypeCn = messageBody.getProductTypeCn();
        String sizeTypeCn = messageBody.getSizeTypeCn();
        String hsCodeName8 = messageBody.getHscodeName8();
        String hsCodeName10 = messageBody.getHscodeName10();

        if (mCatId == null || mCatPathCn == null || ListUtils.isNull(productCodes)) {
            $warn("切换类目 缺少参数 params=" + JsonUtil.bean2Json(messageBody));
            throw new BusinessException("切换类目 缺少参数");
        }

        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();
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
                // 如果更新前主类目为未确定状态,则更新
                if ("0".equalsIgnoreCase(cmsBtProductModel.getCommon().getCatConf())) {
                    updateMap.put("common.fields.productType", com.voyageone.common.util.StringUtils.toString(productTypeEn));
                    updateMap.put("common.fields.productTypeCn", com.voyageone.common.util.StringUtils.toString(productTypeCn));

                    MatchResult matchResult = getSizeType(cmsBtProductModel);
                    if (matchResult != null) {
                        updateMap.put("common.fields.sizeType", StringUtil.isEmpty(matchResult.getSizeTypeEn()) ? sizeTypeEn : matchResult.getSizeTypeEn().toLowerCase());
                        updateMap.put("common.fields.sizeTypeCn", StringUtil.isEmpty(matchResult.getSizeTypeCn()) ? sizeTypeCn : matchResult.getSizeTypeCn());
                        sizeType = StringUtil.isEmpty(matchResult.getSizeTypeCn()) ? sizeTypeCn : matchResult.getSizeTypeCn().toLowerCase();
                    } else {
                        updateMap.put("common.fields.sizeType", com.voyageone.common.util.StringUtils.toString(sizeTypeEn));
                        updateMap.put("common.fields.sizeTypeCn", com.voyageone.common.util.StringUtils.toString(sizeTypeCn));
                        sizeType = sizeTypeCn;
                    }
                    updateMap.put("common.fields.hsCodePrivate", com.voyageone.common.util.StringUtils.toString(hsCodeName8));
                    updateMap.put("common.fields.hsCodeCross", com.voyageone.common.util.StringUtils.toString(hsCodeName10));
                    if (!StringUtil.isEmpty(hsCodeName8)) {
                        updateMap.put("common.fields.hsCodeStatus", "1");
                        updateMap.put("common.fields.hsCodeSetter", userName);
                        updateMap.put("common.fields.hsCodeSetTime", DateTimeUtil.getNow());
                    } else {
                        updateMap.put("common.fields.hsCodeStatus", "0");
                        updateMap.put("common.fields.hsCodeSetter", "");
                        updateMap.put("common.fields.hsCodeSetTime", "");
                    }
                } else {
                    if (StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getHsCodePrivate()) && !StringUtil.isEmpty(hsCodeName8)) {
                        updateMap.put("common.fields.hsCodePrivate", com.voyageone.common.util.StringUtils.toString(hsCodeName8));
                        updateMap.put("common.fields.hsCodeStatus", "1");
                        updateMap.put("common.fields.hsCodeSetter", userName);
                        updateMap.put("common.fields.hsCodeSetTime", DateTimeUtil.getNow());
                    }
                    if (StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getHsCodeCross()) && !StringUtil.isEmpty(hsCodeName10)) {
                        updateMap.put("common.fields.hsCodeCross", com.voyageone.common.util.StringUtils.toString(hsCodeName10));
                    }
                    sizeType = cmsBtProductModel.getCommonNotNull().getFieldsNotNull().getSizeTypeCn();
                }

                if (StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getOriginalTitleCn()) || !"1".equalsIgnoreCase(cmsBtProductModel.getCommon().getFields().getTranslateStatus())) {
                    // 设置商品中文名称（品牌 + 空格 + Size Type中文 + 空格 + 主类目叶子级中文名称）
                    String[] temp = mCatPathCn.split(">");

                    TypeChannelBean typeChannelBean = null;
                    String brand;
                    if(!StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getBrand())) {
                        typeChannelBean = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.BRAND_41, "928", cmsBtProductModel.getCommon().getFields().getBrand(), "cn");
                    }
                    if(typeChannelBean != null && !StringUtil.isEmpty(typeChannelBean.getName())){
                        brand = typeChannelBean.getName();
                    }else{
                        brand = cmsBtProductModel.getCommon().getFields().getBrand();
                    }
                    String titleCn = String.format("%s %s %s", com.voyageone.common.util.StringUtils.toString(brand), com.voyageone.common.util.StringUtils.toString(sizeType), temp[temp.length - 1]);
                    updateMap.put("common.fields.originalTitleCn", titleCn);
                }
                BulkUpdateModel model = new BulkUpdateModel();
                model.setUpdateMap(updateMap);
                model.setQueryMap(queryMap);
                bulkList.add(model);
                cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, userName, "$set");
                if (!productService.compareHsCode(hsCodeName8, cmsBtProductModel.getCommon().getFields().getHsCodePrivate())) {
                    try {
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
                    } catch (Exception e) {
                        $error(e);
                        CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                        errorInfo.setSkuCode(code);
                        errorInfo.setMsg(e.getMessage());
                        failList.add(errorInfo);
                    }
                }
                productStatusHistoryService.insert(channelId, code, "", 0, EnumProductOperationType.changeMainCategory, "修改主类目为" + mCatPathCn, userName);
            } else {
                CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                errorInfo.setSkuCode(code);
                errorInfo.setMsg("产品信息不存在");
                failList.add(errorInfo);
            }
        });
        return failList;
    }

    private MatchResult getSizeType(CmsBtProductModel cmsBtProductModel) {
        FeedQuery query = getFeedQuery(cmsBtProductModel.getFeed().getCatPath(), cmsBtProductModel.getCommonNotNull().getFieldsNotNull().getOrigProductType(), cmsBtProductModel.getCommonNotNull().getFieldsNotNull().getOrigSizeType(), cmsBtProductModel.getCommonNotNull().getFieldsNotNull().getProductNameEn(), "");

        // 调用主类目匹配接口，取得匹配度最高的一个主类目和sizeType
        MatchResult searchResult = searcher.search(query, true);

        return searchResult;
    }

    private FeedQuery getFeedQuery(String feedCategoryPath, String productType, String sizeType, String productNameEn, String brand) {
        // 调用Feed到主数据的匹配程序匹配主类目
        // 子店feed类目path分隔符(由于导入feedInfo表时全部替换成用"-"来分隔了，所以这里写固定值就可以了)
        List<String> categoryPathSplit = new ArrayList<>();
        categoryPathSplit.add("-");
        Tokenizer tokenizer = new Tokenizer(categoryPathSplit);

        FeedQuery query = new FeedQuery(feedCategoryPath, null, tokenizer);
        query.setProductType(productType);
        query.setSizeType(sizeType);
        query.setProductName(productNameEn, brand);

        return query;
    }

}