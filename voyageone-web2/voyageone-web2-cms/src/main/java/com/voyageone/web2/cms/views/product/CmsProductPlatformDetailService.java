package com.voyageone.web2.cms.views.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.utils.FieldUtil;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.ConvertUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsProductPlatformDetail.*;
import com.voyageone.service.bean.cms.product.CmsMtBrandsMappingBean;
import com.voyageone.service.bean.cms.product.DelistingParameter;
import com.voyageone.service.impl.cms.CmsMtBrandService;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.PlatformSchemaService;
import com.voyageone.service.impl.cms.prices.IllegalPriceConfigException;
import com.voyageone.service.impl.cms.prices.PriceCalculateException;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.tools.PlatformMappingService;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author james.li on 2016/6/3.
 * @version 2.0.0
 */
@Service
public class CmsProductPlatformDetailService extends BaseViewService {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private CmsMtBrandService cmsMtBrandService;
    @Autowired
    private PlatformSchemaService platformSchemaService;
    @Autowired
    private PlatformMappingService platformMappingService;
    @Autowired
    private PlatformCategoryService platformCategoryService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private  CmsProductDetailService cmsProductDetailService;
    @Autowired
    PriceService priceService;

    //设置isSale
    public  void  setCartSkuIsSale(SetCartSkuIsSaleParameter parameter,String channelId,String userName) {

        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, parameter.getProdId());
        CmsBtProductModel_Platform_Cart platform = cmsBtProduct.getPlatform(parameter.getCartId());
        if (parameter.getIsSale()) {
            // 如果该已经approve，则插入上新work表
            setCartSkuIsSale_True(parameter, cmsBtProduct, platform, userName);
        }
        else {
            if (!StringUtils.isEmpty(platform.getpNumIId())) {
                //如果numiid存在则判断调用group下线，还是code下线 中同时清空pReallyStatus的值

                platform.setpReallyStatus("");
                setCartSkuIsSale_False(parameter, cmsBtProduct, platform, userName);

            }
        }
        platform.getSkus().forEach(f -> {
            f.setAttribute("isSale", parameter.getIsSale());
        });
        productService.updateProductPlatform(channelId, parameter.getProdId(), platform, userName);

    }

    void  setCartSkuIsSale_True(SetCartSkuIsSaleParameter parameter, CmsBtProductModel cmsBtProduct,CmsBtProductModel_Platform_Cart platform,String userName)
    {
        // 如果该已经approve，则插入上新work表
        if("Approved".equals(platform.getStatus())) {

            List<String> cartIdList = new ArrayList<>();

            cartIdList.add(Integer.toString(parameter.getCartId()));

            //则插入上新work表
            sxProductService.insertSxWorkLoad(cmsBtProduct, cartIdList, userName);
        }
    }
    void  setCartSkuIsSale_False(SetCartSkuIsSaleParameter parameter, CmsBtProductModel cmsBtProduct,CmsBtProductModel_Platform_Cart platform,String userName) {
        //如果numiid存在则判断调用group下线，还是code下线
        DelistingParameter delistingParameter = new DelistingParameter();
        delistingParameter.setCartId(parameter.getCartId());
        delistingParameter.setChannelId(cmsBtProduct.getChannelId());
        delistingParameter.setProductCode(cmsBtProduct.getCommon().getFields().getCode());
        delistingParameter.setComment("");
        if (!StringUtils.isEmpty(platform.getpNumIId())) {

            if (platform.getpIsMain() == 1) {

                cmsProductDetailService.delistinGroup(delistingParameter, userName);
            } else {
                cmsProductDetailService.delisting(delistingParameter, userName);
            }

        }
    }
    // 保存价格
    public  void  saveCartSkuPrice(SaveCartSkuPriceParameter parameter,String channelId,String userName) throws Exception {
        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, parameter.getProdId());
        CmsBtProductModel_Platform_Cart platform = cmsBtProduct.getPlatform(parameter.getCartId());
        if(parameter.getPriceMsrp()>0) {
            platform.setpPriceMsrpSt(parameter.getPriceMsrp());
            platform.setpPriceMsrpEd(parameter.getPriceMsrp());
        }
        if(parameter.getPriceSale()>0) {
            platform.setpPriceSaleSt(parameter.getPriceSale());
            platform.setpPriceSaleEd(parameter.getPriceSale());
        }

        if("Approved".equals(platform.getStatus())) {
            if(StringUtils.isEmpty(platform.getpNumIId()))
            {
                //已经approve但是无NumIID，则插入上新work表,
                List<String> cartIdList = new ArrayList<>();
                cartIdList.add(Integer.toString(parameter.getCartId()));
                //则插入上新work表
                sxProductService.insertSxWorkLoad(cmsBtProduct, cartIdList, userName);
            }
            else
            {
                //有NumIID，则价格变更API
                priceService.updateSkuPrice(channelId,parameter.getCartId(), cmsBtProduct,true);

            }
        }
        platform.getSkus().forEach(f -> {
            if(parameter.getPriceMsrp()>0) {
                f.setAttribute("priceMsrp", parameter.getPriceMsrp());
            }
            if(parameter.getPriceSale()>0) {
                f.setAttribute("priceSale", parameter.getPriceSale());
            }
        });
        productService.updateProductPlatform(channelId, parameter.getProdId(), platform, userName);
    }

    //返回计算后的Msrp价格
    public  List<CartMsrpInfo>  getCalculateCartMsrp(String channelId, Long prodId) throws PriceCalculateException, IllegalPriceConfigException {
        //PriceService   获取价格
        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
        priceService.setPrice(cmsBtProduct, false);
        List<CartMsrpInfo> list = new ArrayList<>();

        cmsBtProduct.getPlatforms().values().forEach(f -> {
            if (f.getCartId() > 0) {
                double msrp = f.getSkus().stream().mapToDouble(m -> m.getDoubleAttribute("priceMsrp")).max().getAsDouble();
                CartMsrpInfo info = new CartMsrpInfo();
                info.setCartId(f.getCartId());
                info.setMsrp(msrp);
                list.add(info);
            }

        });
        return list;
    }

    public ProductPriceSalesInfo getProductPriceSales(String channelId, Long prodId) {

        ProductPriceSalesInfo productPriceSalesInfo = new ProductPriceSalesInfo();

        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);

        productPriceSalesInfo.setBrand(cmsBtProduct.getCommon().getFields().getBrand());
        productPriceSalesInfo.setProductNameEn(cmsBtProduct.getCommon().getFields().getProductNameEn());

        productPriceSalesInfo.setQuantity(cmsBtProduct.getCommon().getFields().getQuantity());

        List<CmsBtProductModel_Field_Image> imgList = cmsBtProduct.getCommonNotNull().getFieldsNotNull().getImages1();

        if (!imgList.isEmpty()) {
            productPriceSalesInfo.setImage1(imgList.get(0).getName());
        }


        List<ProductPrice> productPriceList = new ArrayList<>();

        cmsBtProduct.getPlatforms().values().forEach(f -> {

            ProductPrice productPrice = getProductPrice(f);
            if (productPrice != null) {
                productPrice.setClientMsrpPrice(cmsBtProduct.getCommon().getFields().getClientMsrpPrice());
                productPrice.setClientNetPrice(cmsBtProduct.getCommon().getFields().getClientNetPrice());
                productPriceList.add(productPrice);
            }

        });


        Map<String, Map<String, Object>> map = new HashedMap();

        map.put(CmsBtProductModel_Sales.CODE_SUM_7, cmsBtProduct.getSales().getCodeSum7());

        map.put(CmsBtProductModel_Sales.CODE_SUM_30, cmsBtProduct.getSales().getCodeSum30());

        map.put(CmsBtProductModel_Sales.CODE_SUM_ALL, cmsBtProduct.getSales().getCodeSumAll());

        map.put(CmsBtProductModel_Sales.CODE_SUM_YEAR, cmsBtProduct.getSales().getCodeSumYear());

        productPriceSalesInfo.setSales(map);

        productPriceSalesInfo.setProductPriceList(productPriceList);

        return productPriceSalesInfo;
    }

    private ProductPrice getProductPrice( CmsBtProductModel_Platform_Cart f) {
        if (f.getCartId() != 0) {
            ProductPrice productPrice = new ProductPrice();
            productPrice.setStatus(f.getStatus());
            if (f.getpStatus() != null) {
                productPrice.setpStatus(f.getpStatus().name());
            }
            productPrice.setpPublishError(f.getpPublishError());
            productPrice.setStatus(f.getStatus());
            productPrice.setpReallyStatus(f.getpReallyStatus());
            productPrice.setNumberId(f.getpNumIId());

            productPrice.setPriceMsrpEd(f.getpPriceMsrpEd());
            productPrice.setPriceMsrpSt(f.getpPriceMsrpSt());

            productPrice.setPriceRetailEd(f.getpPriceRetailEd());
            productPrice.setPriceRetailSt(f.getpPriceRetailSt());

            productPrice.setPriceSaleEd(f.getpPriceSaleEd());
            productPrice.setPriceSaleSt(f.getpPriceSaleSt());

            productPrice.setCartId(f.getCartId());

            int count = f.getSkus().size();
            int isSaleTrueCount = 0;

            for (BaseMongoMap<String, Object> sku : f.getSkus()) {
                if (ConvertUtil.toBoolean(sku.get("isSale"))) {
                    isSaleTrueCount = isSaleTrueCount + 1;
                }
            }


            if (count > 0 && count == isSaleTrueCount) {
                productPrice.setChecked(2);//选中
            } else if (count > isSaleTrueCount && isSaleTrueCount > 0) {
                productPrice.setChecked(1);//半选
            }

            CartEnums.Cart cart = CartEnums.Cart.getValueByID(f.getCartId().toString());
            if (cart != null) {
                productPrice.setCartName(cart.name());

                return productPrice;
            }
        }
        return null;
    }
    public String getAutoSyncPriceMsrpOption(String channelId, Integer cartId) {
        String autoSyncPriceMsrpOption = CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_MSRP_AUTO; // 默认配置
        CmsChannelConfigBean autoSyncPriceMsrp = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_MSRP, cartId + "");
        if (autoSyncPriceMsrp != null
                && !CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_MSRP_NO.equals(autoSyncPriceMsrp.getConfigValue1())
                && !CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_MSRP_DIRECT.equals(autoSyncPriceMsrp.getConfigValue1())
                && !CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_MSRP_AUTO.equals(autoSyncPriceMsrp.getConfigValue1())) {
            throw new BusinessException("中国建议售价联动配置选项值错误: %s, %s", channelId, autoSyncPriceMsrp.getConfigValue1());
        }
        if (autoSyncPriceMsrp != null)
            autoSyncPriceMsrpOption = autoSyncPriceMsrp.getConfigValue1();
        return autoSyncPriceMsrpOption;
    }

    public String getAutoSyncPriceSaleOption(String channelId, Integer cartId) {
        String autoSyncPriceSaleOption = CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_SALE; // 默认配置
        CmsChannelConfigBean autoSyncPriceSale = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_SALE, cartId + "");
        if (autoSyncPriceSale == null){
                autoSyncPriceSale = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_SALE, 0 + "");
        }
        if (autoSyncPriceSale != null)
            autoSyncPriceSaleOption = autoSyncPriceSale.getConfigValue1();
        return autoSyncPriceSaleOption;
    }
    /**
         * 获取产品平台信息
         *
         * @param channelId channelId
         * @param prodId    prodId
         * @param cartId    cartId
         * @return 产品平台信息
         */
    public Map<String, Object> getProductPlatform(String channelId, Long prodId, int cartId, String language) {
        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
        CmsBtProductModel_Platform_Cart platformCart = cmsBtProduct.getPlatform(cartId);
        if (platformCart == null) {
            platformCart = new CmsBtProductModel_Platform_Cart();
            platformCart.setCartId(cartId);
        }

        // platform 品牌名
//        if (StringUtil.isEmpty(platformCart.getpBrandId()) || StringUtil.isEmpty(platformCart.getpBrandName())) {
            if (cartId != CartEnums.Cart.USJGJ.getValue()
                    && cartId != CartEnums.Cart.USJGY.getValue()
                    && cartId != CartEnums.Cart.USJGT.getValue()) {
                Map<String, Object> parm = new HashMap<>();
                parm.put("channelId", channelId);
                parm.put("cartId", cartId);
                parm.put("cmsBrand", cmsBtProduct.getCommon().getFields().getBrand());
                parm.put("active", 1);
                CmsMtBrandsMappingBean cmsMtBrandsMappingModel = cmsMtBrandService.getModelByMap(parm);
                if (cmsMtBrandsMappingModel != null) {
                    platformCart.setpBrandId(cmsMtBrandsMappingModel.getBrandId());
                    platformCart.setpBrandName(cmsMtBrandsMappingModel.getpBrand());
                }
            }

        if (cartId != CartEnums.Cart.USJGJ.getValue()
                && cartId != CartEnums.Cart.USJGY.getValue()
                && cartId != CartEnums.Cart.USJGT.getValue()) {
            // 非主商品的平台类目跟这个主商品走
            if (platformCart.getpIsMain() != 1 && (cartId != CartEnums.Cart.JM.getValue() && !CartEnums.Cart.isSimple(CartEnums.Cart.getValueByID(cartId+"")))) {
                CmsBtProductGroupModel cmsBtProductGroup = productGroupService.selectProductGroupByCode(channelId, cmsBtProduct.getCommon().getFields().getCode(), cartId);
                //updated by piao 不在页面做弹出
                if (cmsBtProductGroup == null) {
                    Map<String, Object> noMainResult = new HashedMap();
                    noMainResult.put("noMain", true);
                    noMainResult.put("cartId", cartId);
                    noMainResult.put("mainCode", cmsBtProduct.getCommon().getFields().getCode());
                    return noMainResult;
                    //throw new BusinessException(CartEnums.Cart.getValueByID(cartId + "") + "该商品的没有设置主商品，请先设置主商品：" + cmsBtProduct.getCommon().getFields().getCode());
                }
                CmsBtProductModel mainProduct = productService.getProductByCode(channelId, cmsBtProductGroup.getMainProductCode());
                CmsBtProductModel_Platform_Cart mainPlatform = mainProduct.getPlatform(cartId);
                if (mainPlatform == null || StringUtil.isEmpty(mainPlatform.getpCatId())) {
                    throw new BusinessException(CartEnums.Cart.getValueByID(cartId + "") + "该商品的主商品类目没有设置，请先设置主商品：" + mainProduct.getCommon().getFields().getCode());
                }
                platformCart.setpCatPath(mainPlatform.getpCatPath());
                platformCart.setpCatId(mainPlatform.getpCatId());
            }

            if(platformCart.getFields() == null) platformCart.setFields(new BaseMongoMap<>());
            // added by morse.lu 2016/09/13 start
            // 天猫国际sku级属性，设值下默认商家编码为skuCode
            if (cartId ==  CartEnums.Cart.TG.getValue()) {
                String skuKey = "sku_outerId"; // 商家编码对应skuCode
                try {
                    List<Map<String, Object>> listSkuVal = platformCart.getFields().getAttribute("sku");
                    if (ListUtils.isNull(listSkuVal)) {
                        listSkuVal = new ArrayList<>();
                        platformCart.getFields().setAttribute("sku", listSkuVal);
                    }
                    List<String> listValSkuCode = listSkuVal.stream().map(v -> (String) v.get(skuKey)).collect(Collectors.toList());
                    List<String> listCommSkucode = new ArrayList<>();
                    for (BaseMongoMap<String, Object> commonSku : platformCart.getSkus()) {
                        String skuCode = commonSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
                        listCommSkucode.add(skuCode);
                        if (!listValSkuCode.contains(skuCode)) {
                            // fields.sku里没有，追加下默认值
                            Map<String, Object> mapSkuVal = new HashMap<>();
                            mapSkuVal.put(skuKey, skuCode);
                            listSkuVal.add(mapSkuVal);
                        }
                    }
                    Iterator<Map<String, Object>> iterator = listSkuVal.iterator();
                    while (iterator.hasNext()) {
                        Map<String, Object> platSku = iterator.next();
                        if (!listCommSkucode.contains(platSku.get(skuKey))) {
                            // sku有删除的情况,把fields.sku里也删掉
                            iterator.remove();
                        }
                    }
                } catch (Exception e) {
                    $warn("天猫国际sku商家编码默认值设值失败!" + e.getMessage());
                }
            }
            // added by morse.lu 2016/09/13 end
            platformCart.put("schemaFields", getSchemaFields(platformCart.getFields(), platformCart.getpCatId(), channelId, cartId, prodId, language,null, platformCart.getpBrandId()));
        }
        return platformCart;
    }

    /**
     * 获取产品的基础数据给平台展示用
     *
     * @param channelId
     * @param prodId
     * @param cartId
     * @return
     */
    public Map<String, Object> getProductMastData(String channelId, Long prodId, int cartId) {
        Map<String, Object> mastData = new HashMap<>();
        // 取得产品信息
        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
        // 取得该商品的所在group的其他商品的图片
        CmsBtProductGroupModel cmsBtProductGroup = productGroupService.selectProductGroupByCode(channelId, cmsBtProduct.getCommon().getFields().getCode(), cartId);
        if (cmsBtProductGroup == null) {
            cmsBtProductGroup = productGroupService.selectProductGroupByCode(channelId, cmsBtProduct.getCommon().getFields().getCode(), 0);
        }
        List<Map<String, Object>> images = new ArrayList<>();
        final CmsBtProductGroupModel finalCmsBtProductGroup = cmsBtProductGroup;
        cmsBtProductGroup.getProductCodes().forEach(s1 -> {
            CmsBtProductModel product = cmsBtProduct.getCommon().getFields().getCode().equalsIgnoreCase(s1) ? cmsBtProduct : productService.getProductByCode(channelId, s1);
            if (product != null) {
                Map<String, Object> image = new HashMap<String, Object>();
                image.put("productCode", s1);
                String imageName ="";

                if(!ListUtils.isNull(product.getCommon().getFields().getImages1()) && product.getCommon().getFields().getImages1().get(0).size()>0){
                    imageName = (String) product.getCommon().getFields().getImages1().get(0).get("image1");
                }
                if(StringUtil.isEmpty(imageName) && !ListUtils.isNull(product.getCommon().getFields().getImages6()) && product.getCommon().getFields().getImages6().get(0).size()>0){
                    imageName = (String) product.getCommon().getFields().getImages6().get(0).get("image6");
                }
                image.put("imageName", imageName);
                image.put("isMain", finalCmsBtProductGroup.getMainProductCode().equalsIgnoreCase(s1));
                image.put("prodId", product.getProdId());
                image.put("qty",product.getCommon().getFields().getQuantity());
                if(product.getPlatform(cartId) != null) {
                    image.put("priceSaleSt", product.getPlatform(cartId).getpPriceSaleSt());
                    image.put("priceSaleEd", product.getPlatform(cartId).getpPriceSaleEd());
                }
                images.add(image);
            }
        });

        mastData.put("productCode", cmsBtProduct.getCommon().getFields().getCode());
        mastData.put("productName", StringUtil.isEmpty(cmsBtProduct.getCommon().getFields().getProductNameEn()) ? cmsBtProduct.getCommon().getFields().getProductNameEn() : cmsBtProduct.getCommon().getFields().getOriginalTitleCn());
        mastData.put("model", cmsBtProduct.getCommon().getFields().getModel());
        mastData.put("groupId", cmsBtProductGroup.getGroupId());
        mastData.put("skus", cmsBtProduct.getCommon().getSkus());
        mastData.put("isMain", finalCmsBtProductGroup.getMainProductCode().equalsIgnoreCase(cmsBtProduct.getCommon().getFields().getCode()));
        try{
            Map<String, String> sizeMap = sxProductService.getSizeMap(channelId, cmsBtProduct.getCommon().getFields().getBrand(), cmsBtProduct.getCommon().getFields().getProductType(), cmsBtProduct.getCommon().getFields().getSizeType());
            if (sizeMap != null && sizeMap.size() > 0) {
                cmsBtProduct.getCommon().getSkus().forEach(sku -> {
                    sku.setAttribute("platformSize",sizeMap.get(sku.getSize().trim()));
                });
            }
        }catch (Exception e){

        }

        // TODO 取得Sku的库存
        String skuChannelId = StringUtils.isEmpty(cmsBtProduct.getOrgChannelId()) ? channelId : cmsBtProduct.getOrgChannelId();
        Map<String, Integer> skuInventoryList = productService.getProductSkuQty(skuChannelId, cmsBtProduct.getCommon().getFields().getCode());
        cmsBtProduct.getCommon().getSkus().forEach(cmsBtProductModel_sku -> cmsBtProductModel_sku.setQty(skuInventoryList.get(cmsBtProductModel_sku.getSkuCode()) == null ? 0 : skuInventoryList.get(cmsBtProductModel_sku.getSkuCode())));

//        if (cmsBtProduct.getCommon().getFields() != null) {
//            mastData.put("translateStatus", cmsBtProduct.getCommon().getFields().getTranslateStatus());
//            mastData.put("hsCodeStatus", StringUtil.isEmpty(cmsBtProduct.getCommon().getFields().getHsCodePrivate()) ? 0 : 1);
//        }
        mastData.put("images", images);
        return mastData;
    }

    /**
     * 平台类目变更
     *
     * @param channelId
     * @param prodId
     * @param cartId
     * @param catId
     * @return
     */
    public Map<String, Object> changePlatformCategory(String channelId, Long prodId, int cartId, String catId, String catPath, String language) {
        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
        CmsBtProductModel_Platform_Cart platformCart = cmsBtProduct.getPlatform(cartId);
        if (platformCart != null) {
            if(platformCart.getFields() == null) platformCart.setFields(new BaseMongoMap<>());
            platformCart.put("schemaFields", getSchemaFields(platformCart.getFields(), catId, channelId, cartId, prodId, language, catPath, platformCart.getpBrandId()));
            platformCart.setpCatId(catId);
            // platform 品牌名
//            if (StringUtil.isEmpty(platformCart.getpBrandId()) || StringUtil.isEmpty(platformCart.getpBrandName())) {
                Map<String, Object> parm = new HashMap<>();
                parm.put("channelId", channelId);
                parm.put("cartId", cartId);
                parm.put("cmsBrand", cmsBtProduct.getCommon().getFields().getBrand());
                parm.put("active", 1);
                CmsMtBrandsMappingBean cmsMtBrandsMappingModel = cmsMtBrandService.getModelByMap(parm);
                if (cmsMtBrandsMappingModel != null) {
                    platformCart.setpBrandId(cmsMtBrandsMappingModel.getBrandId());
                    platformCart.setpBrandName(cmsMtBrandsMappingModel.getpBrand());
                }
//            }
        } else {
            platformCart = new CmsBtProductModel_Platform_Cart();
            if(platformCart.getFields() == null) platformCart.setFields(new BaseMongoMap<>());
            platformCart.put("schemaFields", getSchemaFields(platformCart.getFields(), catId, channelId, cartId, prodId, language, catPath, platformCart.getpBrandId()));

            Map<String, Object> parm = new HashMap<>();
            parm.put("channelId", channelId);
            parm.put("cartId", cartId);
            parm.put("cmsBrand", cmsBtProduct.getCommon().getFields().getBrand());
            parm.put("active", 1);
            CmsMtBrandsMappingBean cmsMtBrandsMappingModel = cmsMtBrandService.getModelByMap(parm);
            if (cmsMtBrandsMappingModel != null) {
                platformCart.setpBrandId(cmsMtBrandsMappingModel.getBrandId());
                platformCart.setpBrandName(cmsMtBrandsMappingModel.getpBrand());
            }
            platformCart.setpCatId(catId);
        }
        return platformCart;
    }

    public String updateProductPlatform(String channelId, Long prodId, Map<String, Object> platform, String modifier, Boolean blnSmartSx) {

        if (platform.get("schemaFields") != null) {
            List<Field> masterFields = buildMasterFields((Map<String, Object>) platform.get("schemaFields"));

            platform.put("fields", FieldUtil.getFieldsValueToMap(masterFields));
            platform.remove("schemaFields");
        }
        CmsBtProductModel_Platform_Cart platformModel = new CmsBtProductModel_Platform_Cart(platform);

        if(platformModel.getCartId() == 27){
            blnSmartSx = false;
        }

        Boolean isCatPathChg = false;
        CmsBtProductModel cmsBtProductModel = null;
        // 天猫的场合如果平台类目发生变化 清空platforms.Pxx.pProductId    CMSDOC-262
        if(platformModel.getCartId() == CartEnums.Cart.TG.getValue() || platformModel.getCartId() == CartEnums.Cart.TM.getValue()){
            cmsBtProductModel = productService.getProductById(channelId, prodId);
            CmsBtProductModel_Platform_Cart oldPlatForm = cmsBtProductModel.getPlatform(platformModel.getCartId());
            if(platformModel.getpCatId() != null && !platformModel.getpCatId().equalsIgnoreCase(oldPlatForm.getpCatId())){
                isCatPathChg = true;
            }
        }

        String modified  = productService.updateProductPlatform(channelId, prodId, platformModel, modifier, true, blnSmartSx);
        if(isCatPathChg){
            productService.resetProductAndGroupPlatformPid(channelId, platformModel.getCartId(), cmsBtProductModel.getCommon().getFields().getCode());
        }
        return modified;

    }

    public String priceChk(String channelId, Long prodId, Map<String, Object> platform) {

        // 阀值
        CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId
                , CmsConstants.ChannelConfig.MANDATORY_BREAK_THRESHOLD);

        Double breakThreshold = null;
        if (cmsChannelConfigBean != null) {
            breakThreshold = Double.parseDouble(cmsChannelConfigBean.getConfigValue1()) / 100D + 1.0;
        }

        if (platform.get("skus") != null) {
            CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
            List<BaseMongoMap<String, Object>> cmsBtProductModel_skus = cmsBtProduct.getPlatform((int) platform.get("cartId")).getSkus();

            Map<String, Double> comPrice = new HashMap<>();
            cmsBtProductModel_skus.forEach(item -> comPrice.put(item.getStringAttribute("skuCode"), item.getDoubleAttribute("priceRetail")));

            for (Map stringObjectBaseMongoMap : (List<Map<String, Object>>) platform.get("skus")) {
                if(stringObjectBaseMongoMap.get("isSale") != null && (boolean)stringObjectBaseMongoMap.get("isSale")) {
                    String sku = (String) stringObjectBaseMongoMap.get("skuCode");
                    if (stringObjectBaseMongoMap.get("priceSale") == null || StringUtil.isEmpty(stringObjectBaseMongoMap.get("priceSale").toString())) {
                        throw new BusinessException("价格不能为空");
                    }
                    Double newPriceSale = Double.parseDouble(stringObjectBaseMongoMap.get("priceSale").toString());
                    if (breakThreshold != null && comPrice.containsKey(sku) && ((Double) (newPriceSale / (2 - breakThreshold))).compareTo(comPrice.get(sku)) < 0) {
                        throw new BusinessException("4000094", ((Double) Math.ceil(comPrice.get(sku) * (2 - breakThreshold))).intValue());
                    }

                    if (comPrice.containsKey(sku) && comPrice.get(sku).compareTo(newPriceSale) > 0) {
                        throw new BusinessException("4000091");
                    }
                }
                // DOC-161 价格向上击穿的阀值检查 取消
//                if (breakThreshold != null && comPrice.containsKey(sku) && ((Double) (comPrice.get(sku) * breakThreshold)).compareTo(newPriceSale) < 0) {
//                    throw new BusinessException("4000092");
//                }
            }
        }
        return null;
    }

    /**
     * 构建masterFields.
     */
    private List<Field> buildMasterFields(Map<String, Object> masterFieldsList) {

        List<Map<String, Object>> item = new ArrayList<>();
        if (masterFieldsList.get(PlatformSchemaService.KEY_ITEM) != null) {
            item.addAll((Collection<? extends Map<String, Object>>) masterFieldsList.get(PlatformSchemaService.KEY_ITEM));
        }
        if (masterFieldsList.get(PlatformSchemaService.KEY_PRODUCT) != null) {
            item.addAll((Collection<? extends Map<String, Object>>) masterFieldsList.get(PlatformSchemaService.KEY_PRODUCT));
        }
        List<Field> masterFields = SchemaJsonReader.readJsonForList(item);

        // setComplexValue
        for (Field field : masterFields) {

            if (field instanceof ComplexField) {
                ComplexField complexField = (ComplexField) field;
                List<Field> complexFields = complexField.getFields();
                ComplexValue complexValue = complexField.getComplexValue();
                setComplexValue(complexFields, complexValue);
            }

        }

        return masterFields;
    }

    /**
     * set complex value.
     */
    private void setComplexValue(List<Field> fields, ComplexValue complexValue) {

        for (Field fieldItem : fields) {

            complexValue.put(fieldItem);

            FieldTypeEnum fieldType = fieldItem.getType();

            switch (fieldType) {
                case INPUT:
                    InputField inputField = (InputField) fieldItem;
                    String inputValue = inputField.getValue();
                    complexValue.setInputFieldValue(inputField.getId(), inputValue);
                    break;
                case SINGLECHECK:
                    SingleCheckField singleCheckField = (SingleCheckField) fieldItem;
                    Value checkValue = singleCheckField.getValue();
                    complexValue.setSingleCheckFieldValue(singleCheckField.getId(), checkValue);
                    break;
                case MULTICHECK:
                    MultiCheckField multiCheckField = (MultiCheckField) fieldItem;
                    List<Value> checkValues = multiCheckField.getValues();
                    complexValue.setMultiCheckFieldValues(multiCheckField.getId(), checkValues);
                    break;
                case MULTIINPUT:
                    MultiInputField multiInputField = (MultiInputField) fieldItem;
                    List<String> inputValues = multiInputField.getStringValues();
                    complexValue.setMultiInputFieldValues(multiInputField.getId(), inputValues);
                    break;
                case COMPLEX:
                    ComplexField complexField = (ComplexField) fieldItem;
                    List<Field> subFields = complexField.getFields();
                    ComplexValue subComplexValue = complexField.getComplexValue();
                    setComplexValue(subFields, subComplexValue);
                    break;
                case MULTICOMPLEX:
                    MultiComplexField multiComplexField = (MultiComplexField) fieldItem;
                    List<ComplexValue> complexValueList = multiComplexField.getComplexValues();
                    complexValue.setMultiComplexFieldValues(multiComplexField.getId(), complexValueList);
                    break;

                default:
                    break;
            }

        }
    }

    private Map<String, List<Field>> getSchemaFields(BaseMongoMap<String, Object> fieldsValue, String catId, String channelId, Integer cartId, Long productId, String language, String catPath, String platformBrandId) {
        Map<String, List<Field>> fields = null;

        // 从mapping 来的默认值合并到商品属性中
        Map<String, Object> mppingFields = platformMappingService.getValueMap(channelId, productId, cartId, catPath);

        setDefaultValue(fieldsValue, mppingFields);

        // JM的场合schema就一条
        if (cartId == Integer.parseInt(CartEnums.Cart.JM.getId())) {
            if (!StringUtil.isEmpty(catId)) {
                fields = platformSchemaService.getFieldForProductImage("1", channelId, cartId, language, platformBrandId);
            }
        } else {
            fields = platformSchemaService.getFieldForProductImage(catId, channelId, cartId, language, platformBrandId);
        }
        if (fieldsValue != null && fields != null && fields.get(PlatformSchemaService.KEY_ITEM) != null) {
            FieldUtil.setFieldsValueFromMap(fields.get(PlatformSchemaService.KEY_ITEM), fieldsValue);
        }
        if (fieldsValue != null && fields != null && fields.get(PlatformSchemaService.KEY_PRODUCT) != null) {
            FieldUtil.setFieldsValueFromMap(fields.get(PlatformSchemaService.KEY_PRODUCT), fieldsValue);
        }
        return fields;
    }

    public Map<String, Object> copyPropertyFromMainProduct(String channelId, Long prodId, Integer cartId, String language) {
        CmsBtProductModel cmsBtProductModel = productService.getProductById(channelId, prodId);
        CmsBtProductModel_Platform_Cart platform = cmsBtProductModel.getPlatform(cartId);

        CmsBtProductModel mainProduct = productService.getProductByCode(channelId, platform.getMainProductCode());
        CmsBtProductModel_Platform_Cart mainPlatform = mainProduct.getPlatform(cartId);

        if (CmsConstants.ProductStatus.Pending.toString().equalsIgnoreCase(mainPlatform.getStatus())) {
            throw new BusinessException("主商品没有编辑完成 请先编辑主商品");
        }

        platform.setpCatId(mainPlatform.getpCatId());
        platform.setpCatPath(mainPlatform.getpCatPath());
        if(platform.getFields() == null) platform.setFields(new BaseMongoMap<>());
        mainPlatform.getFields().forEach((s, o) -> {
            if (platform.getFields().containsKey(s)) {
                System.out.println(s);
                if (platform.getFields().get(s) == null || StringUtils.isEmpty(platform.getFields().get(s).toString())) {
                    // 天猫的场合 属性ID是 sku darwin_sku不复制
                    if (cartId == CartEnums.Cart.TG.getValue() && !"sku".equalsIgnoreCase(s) && !"darwin_sku".equalsIgnoreCase(s)) {
                        platform.getFields().put(s, o);
                    }
                }
            } else {
                platform.getFields().put(s, o);
            }
        });

        platform.put("schemaFields", getSchemaFields(platform.getFields(), platform.getpCatId(), channelId, cartId, prodId, language, null, platform.getpBrandId()));

        return platform;
    }

    /**
     * 获取平台所有类目
     *
     * @param user   用户配置
     * @param cartId 平台 ID
     * @return 类目集合
     */
    List<CmsMtPlatformCategoryTreeModel> getPlatformCategories(UserSessionBean user, Integer cartId) {
        return platformCategoryService.getPlatformCategories(user.getSelChannelId(), cartId);
    }

    /**
     * 从共同属性mapping来的属性合并
     *
     * @param fieldMap
     * @param valueMap
     */
    public void setDefaultValue(Map<String, Object> fieldMap, Map<String, Object> valueMap) {
        if (valueMap == null || valueMap.size() == 0) return;
        valueMap.forEach((s, v) -> {
            Object o = fieldMap.get(s);
            if (o == null) {
                fieldMap.put(s, v);
            } else if (o instanceof List) {
                if (((List) o).size() == 0) {
                    fieldMap.put(s, v);
                }
            } else if (o instanceof Map) {
                setDefaultValue((Map<String, Object>) o, (Map<String, Object>) v);
            } else if (StringUtil.isEmpty((String) o)) {
                fieldMap.put(s, v);
            }
        });
    }

}
