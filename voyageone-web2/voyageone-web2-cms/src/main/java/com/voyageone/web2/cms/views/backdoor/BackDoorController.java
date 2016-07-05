package com.voyageone.web2.cms.views.backdoor;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.CategoryTreeAllService;
import com.voyageone.service.impl.cms.CmsMtBrandService;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.jumei2.JmBtDealImportService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsMtBrandsMappingModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeAllModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeAllModel_Platform;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.web2.cms.CmsController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Edward
 * @version 2.0.0, 16/7/1
 */
@RestController
@RequestMapping(value = "/cms/backdoor", method = RequestMethod.POST)
public class BackDoorController extends CmsController {

    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private JmBtDealImportService serviceJmBtDealImport;
    @Autowired
    private CategoryTreeAllService categoryTreeAllService;
    @Autowired
    private CmsMtBrandService cmsMtBrandService;
    @Autowired
    private FeedInfoService feedInfoService;
    @Autowired
    private PlatformCategoryService platformCategoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;


    /**
     * 临时使用,用于清除聚美下的多个产品在一个group的数据(/cms/backdoor/splitGroup/015)
     * @param channelId 渠道Id
     * @return
     */
    @RequestMapping(value = "splitGroup", method = RequestMethod.GET)
    public Object splitJmProductGroup(@RequestParam("channelId") String channelId) {
        return productGroupService.splitJmProductGroup(channelId);
    }

    /**
     * 将老的聚美数据导入都cms(/cms/backdoor/importJM/015)
     * @param channelId 渠道Id
     * @return
     */
    @RequestMapping(value = "importJM", method = RequestMethod.GET)
    public Object importChannel(@RequestParam("channelId") String channelId) {
        return serviceJmBtDealImport.importJM(channelId);
    }

    /**
     * 创建996的测试数据(/cms/backdoor/createTestData)
     * @return
     */
    @RequestMapping(value = "createTestData", method = RequestMethod.GET)
    public Object createTestData () {

        List<CmsBtFeedInfoModel> allFeedInfoList = new ArrayList<CmsBtFeedInfoModel>();

        allFeedInfoList.addAll(cmsBtFeedInfoDao.selectAll("018"));
//        allFeedInfoList.addAll(cmsBtFeedInfoDao.selectAll("017"));


        allFeedInfoList.forEach(cmsBtFeedInfoModel -> {
            CmsBtFeedInfoModel newFeedInfo = new CmsBtFeedInfoModel();
            BeanUtils.copyProperties(cmsBtFeedInfoModel, newFeedInfo);
            newFeedInfo.set_id(null);
            newFeedInfo.setUpdFlg(9);
            newFeedInfo.setChannelId("996");

            cmsBtFeedInfoDao.insert(newFeedInfo);
        });

        return "本次处理的feed数据总数:" + allFeedInfoList.size();
    }

    @RequestMapping(value = "changeDataByNewModel", method = RequestMethod.GET)
    public Object changeDataBy20160708 (@RequestParam("channelId") String channelId, @RequestParam("platformId") String platformId) {

        List<OldCmsBtProductModel> oldProductInfo = cmsBtProductDao.selectOldProduct(channelId);

        List<String> errorCode = new ArrayList<>();

        System.out.print("开始处理:" + oldProductInfo.size());

        oldProductInfo.forEach(oldCmsBtProductModel -> {
            System.out.print("开始处理:" + oldCmsBtProductModel.getFields().getCode());
            try {
                CmsBtProductModel cmsBtProductModel = new CmsBtProductModel();
                // 设计基础属性
                cmsBtProductModel.set_id(oldCmsBtProductModel.get_id());
                cmsBtProductModel.setCreated(oldCmsBtProductModel.getCreated());
                cmsBtProductModel.setCreater(oldCmsBtProductModel.getCreater());
                cmsBtProductModel.setModified(oldCmsBtProductModel.getModified());
                cmsBtProductModel.setModifier(oldCmsBtProductModel.getModifier());
                cmsBtProductModel.setChannelId(oldCmsBtProductModel.getChannelId());
                cmsBtProductModel.setOrgChannelId(oldCmsBtProductModel.getOrgChannelId());
                cmsBtProductModel.setProdId(oldCmsBtProductModel.getProdId());
                cmsBtProductModel.setLock(StringUtils.isEmpty(oldCmsBtProductModel.getFields().getLock()) ? "0" : oldCmsBtProductModel.getFields().getLock());

                // 设置common属性
                CmsBtProductModel_Common common = cmsBtProductModel.getCommonNotNull();


                CmsBtProductModel_Field newField = new CmsBtProductModel_Field();
                CmsMtCategoryTreeAllModel masterCategoryTree = categoryTreeAllService.findCategoryByPlatformId(channelId, oldCmsBtProductModel.getCatPath(), platformId);

                // 设置主类目
                if (masterCategoryTree != null) {
                    common.setCatId(masterCategoryTree.getCatId());
                    common.setCatPath(masterCategoryTree.getCatPath());

                    newField.setCategoryStatus("1");
                    newField.setCategorySetter("数据移行设置");
                    newField.setCategorySetTime(DateTimeUtil.getNowTimeStamp());
                } else {
                    newField.setCategoryStatus("0");
                }

                // 设置Fields属性
                newField.setAppSwitch(0);
                newField.setCode(oldCmsBtProductModel.getFields().getCode());
                newField.setOriginalCode(StringUtils.isEmpty(oldCmsBtProductModel.getFields().getOriginalCode())? oldCmsBtProductModel.getFields().getCode() : oldCmsBtProductModel.getFields().getOriginalCode());
                newField.setClientProductUrl(oldCmsBtProductModel.getFields().getClientProductUrl());
                newField.setIsMasterMain(oldCmsBtProductModel.getFields().getIsMasterMain());
                newField.setProductNameEn(oldCmsBtProductModel.getFields().getProductNameEn());
                newField.setOriginalTitleCn(oldCmsBtProductModel.getFields().getOriginalTitleCn());
                newField.setBrand(oldCmsBtProductModel.getFields().getBrand());
                newField.setModel(oldCmsBtProductModel.getFields().getModel());
                newField.setColor(oldCmsBtProductModel.getFields().getColor());
                newField.setProductType(oldCmsBtProductModel.getFields().getProductType());
                newField.setSizeType(oldCmsBtProductModel.getFields().getSizeType());
                newField.setHsCodeCrop("");
                String hsCodeInfo = oldCmsBtProductModel.getFields().getHsCodePrivate();
                if (StringUtils.isEmpty(hsCodeInfo)) {
                    newField.setHsCodeStatus("0");
                } else {
                    newField.setHsCodePrivate(hsCodeInfo);
                    newField.setHsCodeStatus("1");
                    newField.setHsCodeSetter("数据移行设置");
                    newField.setHsCodeSetTime(DateTimeUtil.getNowTimeStamp());
                }
                newField.setHsCodeCross("");
                newField.setMaterialEn(oldCmsBtProductModel.getFields().getMaterialEn());
                newField.setMaterialCn(oldCmsBtProductModel.getFields().getMaterialCn());
                newField.setOrigin(oldCmsBtProductModel.getFields().getOrigin());
                newField.setSizeChart("");
                newField.setQuantity(oldCmsBtProductModel.getFields().getQuantity());
                newField.setImages1(oldCmsBtProductModel.getFields().getImages1());
                newField.setImages2(oldCmsBtProductModel.getFields().getImages2());
                newField.setImages3(oldCmsBtProductModel.getFields().getImages3());
                newField.setImages4(oldCmsBtProductModel.getFields().getImages4());
                newField.setImages5(oldCmsBtProductModel.getFields().getImages5());
                newField.setImages6(oldCmsBtProductModel.getFields().getImages6());
                newField.setImages7(oldCmsBtProductModel.getFields().getImages7());
                newField.setImages8(oldCmsBtProductModel.getFields().getImages8());
                newField.setPriceMsrpSt(oldCmsBtProductModel.getFields().getPriceMsrpSt());
                newField.setPriceMsrpEd(oldCmsBtProductModel.getFields().getPriceMsrpEd());
                newField.setPriceRetailSt(oldCmsBtProductModel.getFields().getPriceRetailSt());
                newField.setPriceRetailEd(oldCmsBtProductModel.getFields().getPriceRetailEd());
                newField.setShortDesEn(oldCmsBtProductModel.getFields().getShortDesEn());
                newField.setShortDesCn(oldCmsBtProductModel.getFields().getShortDesCn());
                newField.setLongDesEn(oldCmsBtProductModel.getFields().getLongDesEn());
                newField.setLongDesCn(oldCmsBtProductModel.getFields().getLongDesCn());
                newField.setUsageEn(oldCmsBtProductModel.getFields().getUsageEn());
                newField.setUsageCn(oldCmsBtProductModel.getFields().getUsageCn());
                String translateInfo = oldCmsBtProductModel.getFields().getTranslateStatus();
                if (StringUtils.isEmpty(translateInfo)) {
                    newField.setTranslateStatus("0");
                    newField.setTranslator("");
                    newField.setTranslateTime("");
                } else {
                    newField.setTranslateStatus(translateInfo);
                    newField.setTranslator(oldCmsBtProductModel.getFields().getTranslator());
                    newField.setTranslateTime(oldCmsBtProductModel.getFields().getTranslateTime());
                }
                common.setFields(newField);

                // 设置skus属性
                List<CmsBtProductModel_Sku> newSkuList = new ArrayList<CmsBtProductModel_Sku>();
                oldCmsBtProductModel.getSkus().forEach(skuInfo -> {
                    CmsBtProductModel_Sku newSku = new CmsBtProductModel_Sku();
                    newSku.setSkuCode(skuInfo.getSkuCode());
                    newSku.setBarcode(skuInfo.getBarcode());
                    newSku.setClientSkuCode(skuInfo.getClientSkuCode());
                    newSku.setClientSize(skuInfo.getClientSize());
                    newSku.setSize(skuInfo.getSize());
                    newSku.setSizeNick("");
                    newSku.setClientMsrpPrice(skuInfo.getClientMsrpPrice());
                    newSku.setClientRetailPrice(skuInfo.getClientRetailPrice());
                    newSku.setClientNetPrice(skuInfo.getClientNetPrice());
                    newSku.setPriceMsrp(skuInfo.getPriceMsrp());
                    newSku.setPriceRetail(skuInfo.getPriceRetail());
                    newSkuList.add(newSku);
                });
                common.setSkus(newSkuList);

                //设置平台属性

                // 添加默认的P0属性
                CmsBtProductModel_Platform_Cart p0PlatformInfo = new CmsBtProductModel_Platform_Cart();
                p0PlatformInfo.setCartId(0);
                CmsBtProductGroupModel groupModel = productGroupService.selectProductGroupByCode(channelId, oldCmsBtProductModel.getFields().getCode(), 0);
                p0PlatformInfo.setMainProductCode(groupModel.getMainProductCode());

                cmsBtProductModel.getPlatforms().put("P0", p0PlatformInfo);

                // 添加真实平台属性
                List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");

                cartList.forEach(channelBean -> {

                    String cartId = channelBean.getValue();

                    CmsBtProductModel_Platform_Cart platformInfo = oldCmsBtProductModel.getPlatform(Integer.valueOf(cartId));

                    CmsBtProductGroupModel platformGroupInfo = productGroupService.selectProductGroupByCode(channelId, oldCmsBtProductModel.getFields().getCode(), Integer.valueOf(cartId));
                    if (platformGroupInfo == null)
                        platformGroupInfo = productGroupService.selectProductGroupByCode(channelId, oldCmsBtProductModel.getFields().getCode(), 0);

                    OldCmsBtProductModel_Carts cartInfo = oldCmsBtProductModel.getCartById(Integer.valueOf(cartId));

                    final Double[] priceMsrpSt = {0.00};
                    final Double[] priceMsrpEd = {0.00};
                    final Double[] priceRetailSt = {0.00};
                    final Double[] priceRetailEd = {0.00};
                    final Double[] priceSaleSt = {0.00};
                    final Double[] priceSaleEd = {0.00};

                    if (platformInfo == null) {

                        platformInfo = new CmsBtProductModel_Platform_Cart();
                        platformInfo.setStatus(CmsConstants.ProductStatus.Pending.name());
                        platformInfo.setpCatStatus("0");
                        platformInfo.setpAttributeStatus("0");
                        platformInfo.setModified(DateTimeUtil.getNowTimeStamp());

                        platformInfo.setFields(null);

                        // 设置sku数据
                        List<BaseMongoMap<String, Object>> skuInfo1 = new ArrayList<BaseMongoMap<String, Object>>();
                        oldCmsBtProductModel.getSkus().forEach(sku -> {
                            BaseMongoMap<String, Object> newSku = new BaseMongoMap<String, Object>();

                            if (priceMsrpSt[0].compareTo(sku.getPriceMsrp()) > 0)
                                priceMsrpSt[0] = sku.getPriceMsrp();
                            if (priceMsrpEd[0].compareTo(sku.getPriceMsrp()) < 0)
                                priceMsrpEd[0] = sku.getPriceMsrp();
                            if (priceRetailSt[0].compareTo(sku.getPriceRetail()) > 0)
                                priceRetailSt[0] = sku.getPriceRetail();
                            if (priceRetailEd[0].compareTo(sku.getPriceRetail()) < 0)
                                priceRetailEd[0] = sku.getPriceRetail();
                            priceSaleSt[0] = priceRetailSt[0];
                            priceSaleEd[0] = priceRetailEd[0];


                            newSku.put("skuCdoe", sku.getSkuCode());
                            newSku.put("priceMsrp", sku.getPriceMsrp());
                            newSku.put("priceRetail", sku.getPriceRetail());
                            newSku.put("PriceSale", sku.getPriceRetail());
                            newSku.put("isSale", 1);
                            newSku.put("priceDiffFlg", "1");

                            if (!skuInfo1.contains(newSku))
                                skuInfo1.add(newSku);
                        });
                        platformInfo.setSkus(skuInfo1);

                        platformInfo.setpPriceMsrpSt(priceMsrpSt[0]);
                        platformInfo.setpPriceMsrpEd(priceMsrpEd[0]);
                        platformInfo.setpPriceRetailSt(priceRetailSt[0]);
                        platformInfo.setpPriceRetailEd(priceRetailEd[0]);
                        platformInfo.setpPriceSaleSt(priceSaleSt[0]);
                        platformInfo.setpPriceSaleEd(priceSaleEd[0]);
                    }

                    if (masterCategoryTree != null) {

                        // 设置平台的类目
                        for (CmsMtCategoryTreeAllModel_Platform platformCategory : masterCategoryTree.getPlatformCategory()) {
                            CartBean cartBean = Carts.getCart(channelBean.getValue());
                            if (cartBean != null && platformCategory.getPlatformId().equals(cartBean.getPlatform_id())) {
                                platformInfo.setpCatId(platformCategory.getCatId());
                                platformInfo.setpCatPath(platformCategory.getCatPath());
                                break;
                            }
                        }
                    }

                    OldCmsBtProductModel_Field oldField = oldCmsBtProductModel.getFields();
                    if (oldField.getStatus().equals(CmsConstants.ProductStatus.New.name())
                            || oldField.getStatus().equals(CmsConstants.ProductStatus.Pending.name())) {
                        platformInfo.setStatus(CmsConstants.ProductStatus.Pending.name());
                    }

                    if (cartInfo != null) {
                        platformInfo.setStatus(CmsConstants.ProductStatus.Approved.name());
                        platformInfo.setpProductId(platformGroupInfo.getPlatformPid());
                        platformInfo.setpNumIId(StringUtils.isEmpty(cartInfo.getNumIid()) ? platformGroupInfo .getNumIId(): cartInfo.getNumIid());
                        platformInfo.setpPublishTime(cartInfo.getPublishTime());
                        platformInfo.setpStatus(cartInfo.getPlatformStatus() == null ? platformGroupInfo.getPlatformStatus(): cartInfo.getPlatformStatus());
                    }

                    if (platformInfo.getStatus().equals(CmsConstants.ProductStatus.Ready.name())
                            || platformInfo.getStatus().equals(CmsConstants.ProductStatus.Approved.name())) {
                        platformInfo.setpAttributeStatus("1");
                        platformInfo.setpAttributeSetter("数据移行设置");
                        platformInfo.setpAttributeSetTime(DateTimeUtil.getNowTimeStamp());
                    } else {
                        platformInfo.setpAttributeStatus("0");
                    }
                    platformInfo.setModified(oldCmsBtProductModel.getModified());

                    // 该店铺的Fields数据保存的天猫国际的数据
                    if (channelBean.getValue().equals(CartEnums.Cart.TG.getId())
                            && platformId.equals("1")) {

                        if (!StringUtils.isEmpty(oldCmsBtProductModel.getCatPath())) {
                            platformInfo.setpCatPath(oldCmsBtProductModel.getCatPath());
                            CmsMtPlatformCategoryTreeModel platformCategory = platformCategoryService.getCategoryByCatPath(oldCmsBtProductModel.getCatPath(), Integer.valueOf(cartId));
                            platformInfo.setpCatId(platformCategory != null ? platformCategory.getCatId(): "未找到平台类目");
                            platformInfo.setpCatStatus("1");
                        } else {
                            platformInfo.setpCatStatus("0");
                        }

                        // 插入Fields数据
                        BaseMongoMap<String, Object> p23Fields = oldCmsBtProductModel.getFields();
                        p23Fields.setAttribute("title", oldCmsBtProductModel.getFields().getLongTitle());
                        p23Fields.remove("longTitle");
                        platformInfo.setFields(p23Fields);

                        // 设置sku数据
                        List<BaseMongoMap<String, Object>> skuInfo = new ArrayList<BaseMongoMap<String, Object>>();
                        oldCmsBtProductModel.getSkus().forEach(sku -> {
                            BaseMongoMap<String, Object> newSku = new BaseMongoMap<String, Object>();
                            newSku.putAll(sku);

                            if (priceMsrpSt[0].compareTo(sku.getPriceMsrp()) > 0)
                                priceMsrpSt[0] = sku.getPriceMsrp();
                            if (priceMsrpEd[0].compareTo(sku.getPriceMsrp()) < 0)
                                priceMsrpEd[0] = sku.getPriceMsrp();
                            if (priceRetailSt[0].compareTo(sku.getPriceRetail()) > 0)
                                priceRetailSt[0] = sku.getPriceRetail();
                            if (priceRetailEd[0].compareTo(sku.getPriceRetail()) < 0)
                                priceRetailEd[0] = sku.getPriceRetail();
                            priceSaleSt[0] = priceRetailSt[0];
                            priceSaleEd[0] = priceRetailEd[0];

                            newSku.remove("clientSkuCode");
                            newSku.remove("barcode");
                            newSku.remove("size");
                            newSku.remove("clientSize");
                            newSku.remove("skuCarts");
                            newSku.remove("qty");
                            newSku.remove("client_msrp_price");
                            newSku.remove("client_retail_price");
                            newSku.remove("client_net_price");

//                            newSku.put("skuCdoe", sku.getSkuCode());
//                            newSku.put("priceMsrp", sku.getPriceMsrp());
//                            newSku.put("priceRetail", sku.getPriceRetail());
//                            newSku.put("PriceSale", sku.getPriceRetail());
                            newSku.put("isSale", sku.getSkuCarts().contains(23) ? 1 : 0);
                            newSku.put("priceChgFlg", sku.getPriceChgFlg());
                            String diffFlg = "1";
                            if(sku.getPriceSale() < sku.getPriceRetail()){
                                diffFlg = "2";
                            }else if(sku.getPriceSale() < sku.getPriceRetail()){
                                diffFlg = "3";
                            }
                            newSku.put("priceDiffFlg", diffFlg);

                            if (!skuInfo.contains(newSku))
                                skuInfo.add(newSku);
                        });
                        platformInfo.setSkus(skuInfo);

                    } else if (channelBean.getValue().equals(CartEnums.Cart.JG.getId())
                            && platformId.equals("2")) {

                        if (!StringUtils.isEmpty(oldCmsBtProductModel.getCatPath())) {
                            platformInfo.setpCatPath(oldCmsBtProductModel.getCatPath());
                            CmsMtPlatformCategoryTreeModel platformCategory = platformCategoryService.getCategoryByCatPath(oldCmsBtProductModel.getCatPath(), Integer.valueOf(cartId));
                            platformInfo.setpCatId(platformCategory != null ? platformCategory.getCatId(): "未找到平台类目");
                            platformInfo.setpCatStatus("1");
                        } else {
                            platformInfo.setpCatStatus("0");
                        }

                        //// TODO: 16/7/4 和tom确认京东的数据如何回写
                        // 插入Fields数据 
//                        BaseMongoMap<String, Object> p23Fields = oldCmsBtProductModel.getFields();
                        platformInfo.setFields(oldCmsBtProductModel.getFields());
                    }

                    // 如果平台未设置对应的平台品牌
                    if (StringUtils.isEmpty(platformInfo.getpBrandId())) {
                        CmsMtBrandsMappingModel brandInfo = cmsMtBrandService.getModelByName(oldCmsBtProductModel.getFields().getBrand(), cartId, channelId);
                        if (brandInfo != null) {
                            platformInfo.setpBrandId(brandInfo.getBrandId());
                            platformInfo.setpBrandName(brandInfo.getCmsBrand());
                        } else {
                            platformInfo.setpBrandId("0");
                        }
                    }

                    platformInfo.setCartId(Integer.valueOf(cartId));

                    // 设置是否该平台的主商品

                    Integer isMain = platformGroupInfo.getMainProductCode().equals(oldCmsBtProductModel.getFields().getCode()) ? 1 : 0;
                    platformInfo.setpIsMain(isMain);
                    platformInfo.setMainProductCode(platformGroupInfo.getMainProductCode());

                    cmsBtProductModel.getPlatforms().put("P" + cartId, platformInfo);
                });


                // 设置feed数据
                CmsBtFeedInfoModel feedInfo = feedInfoService.getProductByCode(channelId, newField.getCode());
                cmsBtProductModel.getFeed().setOrgAtts(oldCmsBtProductModel.getFeed().getOrgAtts());
                cmsBtProductModel.getFeed().setCnAtts(oldCmsBtProductModel.getFeed().getCnAtts());
                cmsBtProductModel.getFeed().setCustomIds(oldCmsBtProductModel.getFeed().getCustomIds());
                cmsBtProductModel.getFeed().setCustomIdsCn(oldCmsBtProductModel.getFeed().getCustomIdsCn());
                cmsBtProductModel.getFeed().setCatId(feedInfo.getCatId());
                cmsBtProductModel.getFeed().setCatPath(feedInfo.getCategory());


                // 设置销售数据,让job执行一下就有了

                // 设置tags
                cmsBtProductModel.setTags(oldCmsBtProductModel.getTags());

                // 设置定义标签
                cmsBtProductModel.setFreeTags(oldCmsBtProductModel.getFreeTags());

//                ProductUpdateBean updatebean = new ProductUpdateBean();
//                updatebean.setProductModel(cmsBtProductModel);
//                updatebean.setIsCheckModifed(false);
//                updatebean.setModifier(cmsBtProductModel.getModifier());
//                updatebean.setModified(cmsBtProductModel.getModified());
//                productService.updateProduct(channelId, updatebean);

                cmsBtProductDao.deleteById(oldCmsBtProductModel.get_id(), channelId);
                cmsBtProductModel.set_id(null);
                productService.insert(cmsBtProductModel);
//                cmsBtProductDao.update(cmsBtProductModel);
                System.out.println("处理结束:" + cmsBtProductModel.getProdId());
            } catch (Exception ex) {

                System.out.println(ex);
                errorCode.add(oldCmsBtProductModel.getFields().getCode());
            }
        });

        return "成功处理数据件数:" + oldProductInfo.size() + ",错误列表:" + errorCode.toString();
    }
}
