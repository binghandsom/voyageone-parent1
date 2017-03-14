package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.components.jd.service.JdProductService;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductLogDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.prices.PlatformPriceService;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_UsPlatform_Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * product Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 */
@Service
public class ProductPlatformService extends BaseService {

    @Autowired
    protected CmsBtProductLogDao cmsBtProductLogDao;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    private CmsBtPriceLogService cmsBtPriceLogService;

    @Autowired
    private TbProductService tbProductService;

    @Autowired
    private JdProductService jdProductService;

    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;

    @Autowired
    private PriceService priceService;

    @Autowired
    private PlatformPriceService platformPriceService;

    @Autowired
    private ProductService productService;

    /**
     * 更新平台产品信息,并且不做上新也不触发价格变更
     * @param channelId
     * @param prodId
     * @param platformModel
     * @param modifier
     * @param Comment
     * @param isModifiedChk
     * @return
     */
    public String updateProductPlatformNoSx(String channelId, Long prodId, CmsBtProductModel_Platform_Cart platformModel, String modifier, String Comment, boolean isModifiedChk) {
        return updateProductPlatform(channelId, prodId, platformModel, modifier, isModifiedChk, EnumProductOperationType.WebEdit, Comment, false, 3);
    }

    /**
     * 更新平台产品信息,并且智能上新
     * @param channelId
     * @param prodId
     * @param platformModel
     * @param modifier
     * @param Comment
     * @param isModifiedChk
     * @return
     */
    public String updateProductPlatformWithSmartSx(String channelId, Long prodId, CmsBtProductModel_Platform_Cart platformModel, String modifier, String Comment, boolean isModifiedChk) {
        return updateProductPlatform(channelId, prodId, platformModel, modifier, isModifiedChk, EnumProductOperationType.IntelligentPublish, Comment, true, 1);
    }

    /**
     * 更新平台产品信息,并且强制上新
     * @param channelId
     * @param prodId
     * @param platformModel
     * @param modifier
     * @param Comment
     * @param isModifiedChk
     * @return
     */
    public String updateProductPlatformWithSx(String channelId, Long prodId, CmsBtProductModel_Platform_Cart platformModel, String modifier, String Comment, boolean isModifiedChk) {
        return updateProductPlatform(channelId, prodId, platformModel, modifier, isModifiedChk, EnumProductOperationType.WebEdit, Comment, false, 1);
    }

    /**
     * 更新平台产品信息,更新是正常上新,还是走价格更新
     * @param channelId
     * @param prodId
     * @param platformModel
     * @param modifier
     * @param Comment
     * @param isModifiedChk
     * @return
     */
    public String updateProductPlatform(String channelId, Long prodId, CmsBtProductModel_Platform_Cart platformModel, String modifier, String Comment, boolean isModifiedChk) {
        return updateProductPlatform(channelId, prodId, platformModel, modifier, isModifiedChk, EnumProductOperationType.WebEdit, Comment, false, 2);
    }

//
//    /**
//     * 单纯更新平台属性内容,不触发任何上新和价格刷新
//     * @param channelId
//     * @param prodId
//     * @param platformModel
//     * @param modifier
//     * @return
//     */
//    public String updateProductPlatform(String channelId, Long prodId, CmsBtProductModel_Platform_Cart platformModel, String modifier, String Comment) {
//        return updateProductPlatform(channelId, prodId, platformModel, modifier, false, EnumProductOperationType.BatchSetPlatformAttr, Comment, false, false);
//    }
//
//    /**
//     * 不做变更check,根据isSx来决定是否强制上新
//     * @param channelId
//     * @param prodId
//     * @param platformModel
//     * @param modifier
//     * @return
//     */
//    public String updateProductPlatformNoCheck(String channelId, Long prodId, CmsBtProductModel_Platform_Cart platformModel, String modifier, boolean isSx) {
//        return updateProductPlatform(channelId, prodId, platformModel, modifier, false, EnumProductOperationType.WebEdit, "页面编辑", false, isSx);
//    }
//
//    /**
//     * 产品编辑页面属性发生变更,调用approve
//     * @param channelId
//     * @param prodId
//     * @param platformModel
//     * @param modifier
//     * @param isModifiedChk
//     * @return
//     */
//    public String updateProductPlatform(String channelId, Long prodId, CmsBtProductModel_Platform_Cart platformModel, String modifier, Boolean isModifiedChk) {
//        return updateProductPlatform(channelId, prodId, platformModel, modifier, isModifiedChk, EnumProductOperationType.WebEdit, "页面编辑", true, true);
//    }
//
//    /**
//     * updateProductPlatform
//     * @param channelId
//     * @param prodId
//     * @param platformModel
//     * @param modifier
//     * @param isModifiedChk
//     * @param blnSmartSx
//     * @return
//     */
//    public String updateProductPlatform(String channelId, Long prodId, CmsBtProductModel_Platform_Cart platformModel, String modifier, Boolean isModifiedChk, boolean blnSmartSx) {
//        return updateProductPlatform(channelId, prodId, platformModel, modifier, isModifiedChk, EnumProductOperationType.WebEdit, "页面编辑", blnSmartSx, true);
//    }
//
//    /**
//     *
//     * @param channelId
//     * @param prodId
//     * @param platformModel
//     * @param modifier
//     * @param isModifiedChk
//     * @param blnSmartSx
//     * @return
//     */
//    public String updateProductPlatform(String channelId, Long prodId, CmsBtProductModel_Platform_Cart platformModel
//            , String modifier, Boolean isModifiedChk, EnumProductOperationType opeType, String comment, boolean blnSmartSx, Integer isSx) {
//        return updateProductPlatform(channelId, prodId, platformModel, modifier, isModifiedChk, opeType, comment, blnSmartSx, isSx);
//    }

    /**
     * updateProductPlatform
     * @param channelId
     * @param prodId
     * @param platformModel
     * @param modifier
     * @param isModifiedChk
     * @param opeType
     * @param comment
     * @param blnSmartSx
     * @return
     */
    public String updateProductPlatform(String channelId, Long prodId, CmsBtProductModel_Platform_Cart platformModel
            , String modifier, Boolean isModifiedChk, EnumProductOperationType opeType, String comment
            , boolean blnSmartSx, Integer isSx) {
        CmsBtProductModel oldProduct = productService.getProductById(channelId, prodId);

        // 判断是否执行修改check
        if (isModifiedChk) {
            CmsBtProductModel_Platform_Cart cmsBtProductModel_platform_cart = oldProduct.getPlatform(platformModel.getCartId());
            String oldModified = null;
            if (cmsBtProductModel_platform_cart != null) {
                oldModified = cmsBtProductModel_platform_cart.getModified();
            }
            if (oldModified != null) {
                if (!oldModified.equalsIgnoreCase(platformModel.getModified())) {
                    throw new BusinessException("200011");
                }
            } else if (platformModel.getModified() != null) {
                throw new BusinessException("200011");
            }
        }

        // 根据中国最终售价来判断 中国建议售价是否需要自动提高价格
        try {
            priceService.unifySkuPriceMsrp(platformModel.getSkus(), channelId, platformModel.getCartId());
        }catch (Exception e) {
            throw new BusinessException(String.format("产品编辑页面-价格一览,修改商品价格　调用priceService.unifySkuPriceMsrp失败 channelId=%s, cartId=%s", channelId, platformModel.getCartId()), e);
        }

        // 设定类目状态
        if (!StringUtil.isEmpty(platformModel.getpCatId()) && !StringUtil.isEmpty(platformModel.getpCatPath())) {
            platformModel.setpCatStatus("1");
        } else {
            platformModel.setpCatStatus("0");
        }
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", prodId);

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();

        platformModel.setModified(DateTimeUtil.getNowTimeStamp());
        updateMap.put("platforms.P" + platformModel.getCartId(), platformModel);
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);

        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, modifier, "$set");

        // 更新平台价格(因为批量修改价格,不存在修改sku的isSale的情况,默认调用API刷新价格)
        CmsBtProductModel newProduct = productService.getProductById(channelId, prodId);
        // (isSx : 1: 强制上新, 2: 调用价格上新,如果未上新成功的,调用上新, 3: 不上新也不修改价格)
        platformPriceService.publishPlatFormPrice(channelId, isSx, newProduct, platformModel.getCartId(), modifier, true, blnSmartSx);


        List<String> skus = new ArrayList<>();
        platformModel.getSkus().forEach(sku -> skus.add(sku.getStringAttribute("skuCode")));
        cmsBtPriceLogService.addLogForSkuListAndCallSyncPriceJob(skus, channelId, prodId, platformModel.getCartId(), modifier, comment);
        productStatusHistoryService.insert(channelId, oldProduct.getCommon().getFields().getCode(), platformModel.getStatus(), platformModel.getCartId(), opeType, comment, modifier);

        return platformModel.getModified();
    }


    /**
     * 更新 usPlatforms
     *
     * @param channelId   channelId
     * @param code        code
     * @param usPlatforms 美国平台信息
     * @param modifier    更正人
     */
    public void updateUsPlatforms(String channelId,
                                  String code,
                                  Map<String, CmsBtProductModel_UsPlatform_Cart> usPlatforms,
                                  String modifier) {
        cmsBtProductDao.updateUsPlatforms(channelId, code, usPlatforms, modifier);
    }

    /**
     * delPlatfromProduct
     * @param channelId
     * @param cartId
     * @param numIid
     */
    public void delPlatfromProduct(String channelId, Integer cartId, String numIid) {
        if (StringUtil.isEmpty(numIid)) return;
        ShopBean shopBean = Shops.getShop(channelId, cartId);
        CartEnums.Cart cartEnum = CartEnums.Cart.getValueByID(cartId.toString());
        try {
            switch (cartEnum) {
                case TM:
                case TG:
                case TT:
                case LTT:
                    tbProductService.delItem(shopBean, numIid);
                    break;
                case JD:
                case JG:
                case JGY:
                case JGJ:
                    jdProductService.delItem(shopBean, numIid);
                    break;
            }
        } catch (Exception e) {
            throw new BusinessException("商品删除失败：" + e.getMessage());
        }
    }

    /**
     * updateProductPlatformIsMain
     * @param mainProductCode
     * @param channelId
     * @param productCode
     * @param cartId
     * @param modifier
     */
    public void updateProductPlatformIsMain(String mainProductCode, String channelId, String productCode, Integer cartId, String modifier) {
        Integer isMain = mainProductCode.equals(productCode) ? 1 : 0;
        //更新mongo数据
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("common.fields.code", productCode);

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();

        updateMap.put("platforms.P" + cartId + ".pIsMain", isMain);
        updateMap.put("platforms.P" + cartId + ".mainProductCode", mainProductCode);
        updateMap.put("platforms.P" + cartId + ".modified", DateTimeUtil.getNowTimeStamp());
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, modifier, "$set");
    }
}