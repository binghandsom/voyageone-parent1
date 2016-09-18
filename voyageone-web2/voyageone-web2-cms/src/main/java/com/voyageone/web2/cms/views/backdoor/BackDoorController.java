package com.voyageone.web2.cms.views.backdoor;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionDaoExt;
import com.voyageone.service.impl.cms.CategoryTreeAllService;
import com.voyageone.service.impl.cms.CmsMtBrandService;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.jumei2.JmBtDealImportService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductSkuService;
import com.voyageone.service.model.cms.CmsMtBrandsMappingModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeAllModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.service.model.util.MapModel;
import com.voyageone.web2.cms.CmsController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

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
    private CmsBtProductGroupDao productGroupDao;

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
    @Autowired
    private ProductSkuService productSkuSerivice;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    private CmsBtJmPromotionDaoExt cmsBtJmPromotionDaoExt;


    /**
     * 临时使用,用于清除聚美下的多个产品在一个group的数据(/cms/backdoor/splitGroup/015)
     *
     * @param channelId 渠道Id
     * @return
     */
    @RequestMapping(value = "splitGroup", method = RequestMethod.GET)
    public Object splitJmProductGroup(@RequestParam("channelId") String channelId) {
        return productGroupService.splitJmProductGroup(channelId);
    }

    /**
     * 将老的聚美数据导入都cms(/cms/backdoor/importJM/015)
     *
     * @param channelId 渠道Id
     * @return
     */
    @RequestMapping(value = "importJM", method = RequestMethod.GET)
    public Object importChannel(@RequestParam("channelId") String channelId) {
        return serviceJmBtDealImport.importJM(channelId);
    }

    /**
     * 将老的聚美数据导入都cms(/cms/backdoor/importJM/015)
     *
     * @param channelId 渠道Id
     * @return
     */
    @RequestMapping(value = "importJMOne", method = RequestMethod.GET)
    public Object importJmOne(@RequestParam("channelId") String channelId, @RequestParam("code") String code) {
        return serviceJmBtDealImport.importJMOne(channelId,code);
    }

    /**
     * 创建996的测试数据(/cms/backdoor/createTestData)
     *
     * @return
     */
    @RequestMapping(value = "createTestData", method = RequestMethod.GET)
    public Object createTestData() {

        List<CmsBtFeedInfoModel> allFeedInfoList = new ArrayList<CmsBtFeedInfoModel>();

        JongoQuery selectQuery = new JongoQuery();
//        selectQuery.setSkip(1);
//        selectQuery.setLimit(000);
        allFeedInfoList.addAll(cmsBtFeedInfoDao.selectAll("015"));
        allFeedInfoList.addAll(cmsBtFeedInfoDao.selectAll("017"));

        $info("测试店铺996的feed数据做成开始,总件数" + allFeedInfoList.size());

        // 插入feed数据
        allFeedInfoList.forEach(cmsBtFeedInfoModel -> {
            CmsBtFeedInfoModel newFeedInfo = new CmsBtFeedInfoModel();
            BeanUtils.copyProperties(cmsBtFeedInfoModel, newFeedInfo);
            newFeedInfo.set_id(null);
            newFeedInfo.setUpdFlg(9);
            newFeedInfo.setChannelId("996");
            cmsBtFeedInfoDao.insert(newFeedInfo);
            $info("插入feed的产品Code:" + newFeedInfo.getCode() + "-成功");

        });

        // 插入product数据

        // 插入group表

        // 插入feed类目树表

        // 插入feed属性表

        // 插入平台类目树表

        // 插入平台schema表

        // mysql的基础数据表

        // mysql的配置表

        // mysql的item detail表


        // 插入

        return "本次处理的feed数据总数:" + allFeedInfoList.size();
    }

    /**
     * 用于20160708的数据移植
     *
     * @param channelId  店铺Id
     * @param platformId 平台种类Id
     * @param code       产品code
     * @return
     */
    @RequestMapping(value = "changeDataByNewModel", method = RequestMethod.GET)
    public Object changeDataBy20160708(@RequestParam("channelId") String channelId, @RequestParam("platformId") String platformId, @RequestParam("productCode") String code) {

        final String productNewStatusName = CmsConstants.ProductStatus.New.name();

        List<String> codes = new ArrayList<>();
        if (!StringUtils.isEmpty(code)) {
            codes = java.util.Arrays.asList(code.split(";"));
        }
        System.out.print(codes.toArray() + ":" + codes.size());

        List<OldCmsBtProductModel> oldProductInfo = cmsBtProductDao.selectOldProduct(channelId, codes);

        List<String> errorCode = new ArrayList<>();

        System.out.println("开始处理:" + oldProductInfo.size());

        Map<String, CmsMtPlatformCategoryTreeModel> platformCategoryMap = getPlatformMap(channelId, 23);

        oldProductInfo.parallelStream().forEach(oldCmsBtProductModel -> {
            System.out.println("开始处理:" + oldCmsBtProductModel.getFields().getCode());

            System.out.println("status:" + oldCmsBtProductModel.getFields().getStatus());
            String oldStatus = oldCmsBtProductModel.getFields().getStatus();

            if (oldStatus.equals(productNewStatusName)) {
                cmsBtProductDao.deleteById(oldCmsBtProductModel.get_id(), channelId);
                return;
            }

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

                // 设置feed数据
                CmsBtFeedInfoModel feedInfo = feedInfoService.getProductByCode(channelId, oldCmsBtProductModel.getFields().getOriginalCode());

                // 设置Fields属性
                newField.setAppSwitch(0);
                newField.setCode(oldCmsBtProductModel.getFields().getCode());
                newField.setOriginalCode(StringUtils.isEmpty(oldCmsBtProductModel.getFields().getOriginalCode()) ? oldCmsBtProductModel.getFields().getCode() : oldCmsBtProductModel.getFields().getOriginalCode());
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
                if (feedInfo != null) {
                    newField.setCodeDiff(feedInfo.getColor());
                    newField.setMaterialEn(feedInfo.getMaterial());
                } else {
                    newField.setCodeDiff("");
                    newField.setMaterialEn("");
                }
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
//                    if (platformGroupInfo == null)
//                        platformGroupInfo = productGroupService.selectProductGroupByCode(channelId, oldCmsBtProductModel.getFields().getCode(), 0);

                    OldCmsBtProductModel_Carts cartInfo = oldCmsBtProductModel.getCartById(Integer.valueOf(cartId));

                    final Double[] priceMsrpSt = {0.00};
                    final Double[] priceMsrpEd = {0.00};
                    final Double[] priceRetailSt = {0.00};
                    final Double[] priceRetailEd = {0.00};
                    final Double[] priceSaleSt = {0.00};
                    final Double[] priceSaleEd = {0.00};

                    if (platformInfo == null || platformInfo.getFields() == null) {

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

                            if (priceMsrpSt[0].compareTo(0D) == 0 || priceMsrpSt[0].compareTo(sku.getPriceMsrp()) >= 0)
                                priceMsrpSt[0] = sku.getPriceMsrp();
                            if (priceMsrpEd[0].compareTo(sku.getPriceMsrp()) <= 0)
                                priceMsrpEd[0] = sku.getPriceMsrp();
                            if (priceRetailSt[0].compareTo(0D) == 0 || priceRetailSt[0].compareTo(sku.getPriceRetail()) >= 0)
                                priceRetailSt[0] = sku.getPriceRetail();
                            if (priceRetailEd[0].compareTo(sku.getPriceRetail()) <= 0)
                                priceRetailEd[0] = sku.getPriceRetail();
                            if (priceSaleSt[0].compareTo(0D) == 0 || priceSaleSt[0].compareTo(sku.getPriceSale()) >= 0)
                                priceSaleSt[0] = sku.getPriceSale();
                            if (priceSaleEd[0].compareTo(sku.getPriceSale()) <= 0)
                                priceSaleEd[0] = sku.getPriceSale();
//                            priceSaleSt[0] = priceRetailSt[0];
//                            priceSaleEd[0] = priceRetailEd[0];


                            newSku.put("skuCode", sku.getSkuCode());
                            newSku.put("priceMsrp", sku.getPriceMsrp());
                            newSku.put("priceRetail", sku.getPriceRetail());
                            newSku.put("priceSale", sku.getPriceSale());
                            newSku.put("isSale", true);
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

                        // 如果该渠道以前设置的主类目是京东类目,而该渠道的京东平台数据又没有设置的时候
                        if (channelBean.getValue().equals(CartEnums.Cart.JG.getId())
                                && platformId.equals("2")) {

                            if (!StringUtils.isEmpty(oldCmsBtProductModel.getCatPath())) {
                                platformInfo.setpCatPath(oldCmsBtProductModel.getCatPath());
                                CmsMtPlatformCategoryTreeModel platformCategory = platformCategoryService.getCategoryByCatPath(oldCmsBtProductModel.getCatPath(), Integer.valueOf(cartId));
                                if (platformCategory == null) {
                                    platformInfo.setpCatStatus("0");
                                } else {
                                    platformInfo.setpCatId(platformCategory.getCatId());
                                    platformInfo.setpCatStatus("1");
                                }
                            } else {
                                platformInfo.setpCatStatus("0");
                            }

                            //// TODO: 16/7/4 和tom确认京东的数据如何回写
                            // 插入Fields数据
//                          BaseMongoMap<String, Object> p23Fields = oldCmsBtProductModel.getFields();
                            platformInfo.setFields(oldCmsBtProductModel.getFields());
                        }
                    }

                    //// TODO: 16/7/5 暂时默认导入的时候不设置平台类目
                    // 根据主类目找到对应的平台类目
//                    if (masterCategoryTree != null) {
//
//                        // 设置平台的类目
//                        for (CmsMtCategoryTreeAllModel_Platform platformCategory : masterCategoryTree.getPlatformCategory()) {
//                            CartBean cartBean = Carts.getCart(channelBean.getValue());
//                            if (cartBean != null && platformCategory.getPlatformId().equals(cartBean.getPlatform_id())) {
//                                platformInfo.setpCatId(platformCategory.getCatId());
//                                platformInfo.setpCatPath(platformCategory.getCatPath());
//                                break;
//                            }
//                        }
//                    }

                    OldCmsBtProductModel_Field oldField = oldCmsBtProductModel.getFields();
                    if (oldField.getStatus().equals(CmsConstants.ProductStatus.New.name())
                            || oldField.getStatus().equals(CmsConstants.ProductStatus.Pending.name())) {
                        platformInfo.setStatus(CmsConstants.ProductStatus.Pending);
                        platformInfo.setpAttributeStatus("0");
                    } else {
                        platformInfo.setStatus(oldField.getStatus());
                        platformInfo.setpAttributeStatus("1");
                        platformInfo.setpAttributeSetter("数据移行设置");
                        platformInfo.setpAttributeSetTime(DateTimeUtil.getNowTimeStamp());
                    }

                    if (cartInfo != null) {
                        // 如果group数据能渠道
                        if (platformGroupInfo != null) {
                            platformInfo.setpProductId(platformGroupInfo.getPlatformPid());
                            platformInfo.setpNumIId(StringUtils.isEmpty(cartInfo.getNumIid()) ? platformGroupInfo.getNumIId() : cartInfo.getNumIid());
                            platformInfo.setpPublishTime(cartInfo.getPublishTime());
                            platformInfo.setpStatus(cartInfo.getPlatformStatus() == null ? platformGroupInfo.getPlatformStatus() : cartInfo.getPlatformStatus());
                        }
                        // 如果group数据取不到
                        else {
                            platformInfo.setpNumIId(StringUtils.isEmpty(cartInfo.getNumIid()) ? "" : cartInfo.getNumIid());
                            platformInfo.setpPublishTime(cartInfo.getPublishTime());
                            platformInfo.setpStatus(cartInfo.getPlatformStatus() == null ? CmsConstants.PlatformStatus.InStock : cartInfo.getPlatformStatus());
                        }
                    }
                    platformInfo.setModified(oldCmsBtProductModel.getModified());

                    // 该店铺的Fields数据保存的天猫国际的数据
                    if (channelBean.getValue().equals(CartEnums.Cart.TG.getId())
                            && platformId.equals("1")) {

                        if (!StringUtils.isEmpty(oldCmsBtProductModel.getCatPath())) {
                            platformInfo.setpCatPath(oldCmsBtProductModel.getCatPath());
//                            CmsMtPlatformCategoryTreeModel platformCategory = platformCategoryService.getCategoryByCatPath(oldCmsBtProductModel.getCatPath(), Integer.valueOf(cartId));
                            CmsMtPlatformCategoryTreeModel platformCategory = platformCategoryMap.get(oldCmsBtProductModel.getCatPath());
                            if (platformCategory == null) {
                                platformInfo.setpCatStatus("0");
                            } else {
                                platformInfo.setpCatId(platformCategory.getCatId());
                                platformInfo.setpCatStatus("1");
                            }
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

                            if (priceMsrpSt[0].compareTo(0D) == 0 || priceMsrpSt[0].compareTo(sku.getPriceMsrp()) >= 0)
                                priceMsrpSt[0] = sku.getPriceMsrp();
                            if (priceMsrpEd[0].compareTo(sku.getPriceMsrp()) <= 0)
                                priceMsrpEd[0] = sku.getPriceMsrp();
                            if (priceRetailSt[0].compareTo(0D) == 0 || priceRetailSt[0].compareTo(sku.getPriceRetail()) >= 0)
                                priceRetailSt[0] = sku.getPriceRetail();
                            if (priceRetailEd[0].compareTo(sku.getPriceRetail()) <= 0)
                                priceRetailEd[0] = sku.getPriceRetail();
//                            priceSaleSt[0] = priceRetailSt[0];
//                            priceSaleEd[0] = priceRetailEd[0];

                            newSku.remove("clientSkuCode");
                            newSku.remove("barcode");
                            newSku.remove("size");
                            newSku.remove("clientSize");
                            newSku.remove("skuCarts");
                            newSku.remove("qty");
                            newSku.remove("client_msrp_price");
                            newSku.remove("client_retail_price");
                            newSku.remove("client_net_price");

                            newSku.put("isSale", sku.getSkuCarts().contains(23));
                            newSku.put("priceChgFlg", "");
//                            String diffFlg = "1";
//                            if (sku.getPriceSale() < sku.getPriceRetail()) {
//                                diffFlg = "2";
//                            } else if (sku.getPriceSale() < sku.getPriceRetail()) {
//                                diffFlg = "3";
//                            }

                            // 阀值
                            CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId
                                    , CmsConstants.ChannelConfig.MANDATORY_BREAK_THRESHOLD);

                            Double breakThreshold = 0.00;
                            if (cmsChannelConfigBean != null) {
                                breakThreshold = Double.parseDouble(cmsChannelConfigBean.getConfigValue1()) / 100D;
                            }

                            String diffFlg = productSkuSerivice.getPriceDiffFlg(breakThreshold, sku.getPriceSale(), sku.getPriceRetail());
                            newSku.put("priceDiffFlg", diffFlg);

                            if (!skuInfo.contains(newSku))
                                skuInfo.add(newSku);
                        });
                        platformInfo.setSkus(skuInfo);
                        platformInfo.setpPriceMsrpSt(priceMsrpSt[0]);
                        platformInfo.setpPriceMsrpEd(priceMsrpEd[0]);
                        platformInfo.setpPriceRetailSt(priceRetailSt[0]);
                        platformInfo.setpPriceRetailEd(priceRetailEd[0]);
                        platformInfo.setpPriceSaleSt(priceSaleSt[0]);
                        platformInfo.setpPriceSaleEd(priceSaleEd[0]);
                    }

                    // 如果平台未设置对应的平台品牌
                    if (StringUtils.isEmpty(platformInfo.getpBrandId())) {
                        CmsMtBrandsMappingModel brandInfo = cmsMtBrandService.getModelByName(oldCmsBtProductModel.getFields().getBrand(), cartId, channelId);
                        if (brandInfo != null) {
                            platformInfo.setpBrandId(brandInfo.getBrandId());
                            //// TODO: 16/7/5 暂时设置成主数据的品牌类目
                            platformInfo.setpBrandName(brandInfo.getCmsBrand());
                        } else {
                            platformInfo.setpBrandId("0");
                        }
                    }

                    platformInfo.setCartId(Integer.valueOf(cartId));

                    // 设置是否该平台的主商品

                    if (platformGroupInfo != null) {
                        Integer isMain = platformGroupInfo.getMainProductCode().equals(oldCmsBtProductModel.getFields().getCode()) ? 1 : 0;
                        platformInfo.setpIsMain(isMain);
                        platformInfo.setMainProductCode(platformGroupInfo.getMainProductCode());
                    } else {
                        Integer isMain = groupModel.getMainProductCode().equals(oldCmsBtProductModel.getFields().getCode()) ? 1 : 0;
                        platformInfo.setpIsMain(isMain);
                        platformInfo.setMainProductCode(groupModel.getMainProductCode());
                    }

                    cmsBtProductModel.getPlatforms().put("P" + cartId, platformInfo);
                });

                cmsBtProductModel.getFeed().setOrgAtts(oldCmsBtProductModel.getFeed().getOrgAtts());
                cmsBtProductModel.getFeed().setCnAtts(oldCmsBtProductModel.getFeed().getCnAtts());
                cmsBtProductModel.getFeed().setCustomIds(oldCmsBtProductModel.getFeed().getCustomIds());
                cmsBtProductModel.getFeed().setCustomIdsCn(oldCmsBtProductModel.getFeed().getCustomIdsCn());
                if (feedInfo != null) {
                    cmsBtProductModel.getFeed().setCatId(feedInfo.getCatId());
                    cmsBtProductModel.getFeed().setCatPath(feedInfo.getCategory());
                } else {
                    cmsBtProductModel.getFeed().setCatId("");
                    cmsBtProductModel.getFeed().setCatPath("");
                }


                // 设置销售数据,让job执行一下就有了

                // 设置tags
                cmsBtProductModel.setTags(oldCmsBtProductModel.getTags());

                // 设置定义标签
                cmsBtProductModel.setFreeTags(oldCmsBtProductModel.getFreeTags());

                cmsBtProductDao.deleteById(oldCmsBtProductModel.get_id(), channelId);
                cmsBtProductModel.set_id(null);
                productService.insert(cmsBtProductModel);
                System.out.println("处理结束:" + cmsBtProductModel.getProdId());
            } catch (Exception ex) {

                $error(ex);
                errorCode.add(oldCmsBtProductModel.getFields().getCode());
            }
        });

//        List<String> groupCheckMessageList = checkGroupTransformOn20160708(channelId, code);

        StringBuilder builder = new StringBuilder("<body>");

        builder.append("<h2>成功处理数据件数: ").append(oldProductInfo.size()).append("</h2>");
        builder.append("<hr>");
        builder.append("<h2>错误列表</h2>");
        builder.append("<ul>");
        errorCode.forEach(_errorCode -> builder.append("<li>").append(_errorCode).append("</li>"));
        builder.append("</ul>");
        builder.append("<hr>");
        builder.append("<h2>Group 信息列表</h2>");
        builder.append("<ul>");
//        groupCheckMessageList.forEach(groupCheckMessage -> builder.append("<li>").append(groupCheckMessage).append("</li>"));
        builder.append("</ul>");
        builder.append("</body>");

        return builder.toString();
    }


    @RequestMapping(value = "changeDataBySetPrice", method = RequestMethod.GET)
    public Object changeDataBySetPrice(@RequestParam("channelId") String channelId) {


        List<CmsBtProductModel> productInfo = cmsBtProductDao.selectAll(channelId);
        productInfo.parallelStream().forEach(product -> {

            List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");
            cartList.forEach(channelBean -> {
                String cartId = channelBean.getValue();

                CmsBtProductModel_Platform_Cart platformInfo = product.getPlatform(Integer.valueOf(cartId));

                if (StringUtils.isEmpty(platformInfo.getpNumIId())) {
                    platformInfo.setStatus(CmsConstants.ProductStatus.Pending);
                    platformInfo.setAttribute("pStatus", "");
                    platformInfo.setpAttributeStatus("0");
                    platformInfo.setpAttributeSetter("");
                } else {

                    platformInfo.setStatus(CmsConstants.ProductStatus.Approved);
//                    platformInfo.setAttribute("pStatus", "");
                    if (!StringUtils.isEmpty(platformInfo.getpCatPath())) {
                        platformInfo.setpCatStatus("1");
                    } else {
                        platformInfo.setpCatStatus("0");
                    }
                    platformInfo.setpAttributeStatus("1");
                    platformInfo.setpAttributeSetter("数据移行设置");
                    if (CmsConstants.PlatformStatus.WaitingPublish.name().equals(platformInfo.getpStatus().name()))
                        platformInfo.setpStatus(CmsConstants.PlatformStatus.InStock);
                }

//                final Double[] priceMsrpSt = {0.00};
//                final Double[] priceMsrpEd = {0.00};
//                final Double[] priceRetailSt = {0.00};
//                final Double[] priceRetailEd = {0.00};
//                final Double[] priceSaleSt = {0.00};
//                final Double[] priceSaleEd = {0.00};
//
//                platformInfo.getSkus().forEach(sku -> {
//
//                    double priceMsrp = sku.getDoubleAttribute("priceMsrp");
//                    double priceRetail = sku.getDoubleAttribute("priceRetail");
//                    double priceSale = sku.getDoubleAttribute("priceSale");
//
//                    if (priceMsrpSt[0].compareTo(0D) == 0 || priceMsrpSt[0].compareTo(priceMsrp) >= 0)
//                        priceMsrpSt[0] = priceMsrp;
//                    if (priceMsrpEd[0].compareTo(priceMsrp) <= 0)
//                        priceMsrpEd[0] = priceMsrp;
//                    if (priceRetailSt[0].compareTo(0D) == 0 || priceRetailSt[0].compareTo(priceRetail) >= 0)
//                        priceRetailSt[0] = priceRetail;
//                    if (priceRetailEd[0].compareTo(priceRetail) <= 0)
//                        priceRetailEd[0] = priceRetail;
//                    if (priceSaleSt[0].compareTo(0D) == 0 || priceSaleSt[0].compareTo(priceSale) >= 0)
//                        priceSaleSt[0] = priceSale;
//                    if (priceSaleEd[0].compareTo(priceSale) <= 0)
//                        priceSaleEd[0] = priceSale;
//                });
//
//                platformInfo.setpPriceMsrpSt(priceMsrpSt[0]);
//                platformInfo.setpPriceMsrpEd(priceMsrpEd[0]);
//                platformInfo.setpPriceRetailSt(priceRetailSt[0]);
//                platformInfo.setpPriceRetailEd(priceRetailEd[0]);
//                platformInfo.setpPriceSaleSt(priceSaleSt[0]);
//                platformInfo.setpPriceSaleEd(priceSaleEd[0]);
//
//                platformInfo.getSkus().forEach(sku -> {
//                    if (sku.containsKey("PriceSale")) {
//                        sku.put("priceSale",sku.getDoubleAttribute("PriceSale"));
//                        sku.remove("PriceSale");
//                    }
//                });


                HashMap<String, Object> queryMap = new HashMap<>();
                queryMap.put("prodId", product.getProdId());
                List<BulkUpdateModel> bulkList = new ArrayList<>();
                HashMap<String, Object> updateMap = new HashMap<>();
//                platformModel.setModified(DateTimeUtil.getNowTimeStamp());
                updateMap.put("platforms.P" + platformInfo.getCartId(), platformInfo);
                BulkUpdateModel model = new BulkUpdateModel();
                model.setUpdateMap(updateMap);
                model.setQueryMap(queryMap);
                bulkList.add(model);
                cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, "changeDataBySetPrice", "$set");
            });
        });
        return null;
    }

    @RequestMapping(value = "changeDataByNewGroup", method = RequestMethod.GET)
    public Object changeDataBy20160708Group(@RequestParam("channelId") String channelId) {

        List<String> groupCheckMessageList = checkGroupTransformOn20160708(channelId, null);

        StringBuilder builder = new StringBuilder("<body>");
        builder.append("<h2>Group 信息列表</h2>");
        builder.append("<ul>");
        groupCheckMessageList.forEach(groupCheckMessage -> builder.append("<li>").append(groupCheckMessage).append("</li>"));
        builder.append("</ul>");
        builder.append("</body>");

        return builder.toString();
    }

    @RequestMapping(value = "updateFeedFlagTo9", method = RequestMethod.GET)
    public Object updateFeedFlagTo9(@RequestParam("channelId") String channelId) {

        List<String> messageList = new ArrayList<>();
        List<String> successList = new ArrayList<>();

        JongoQuery queryObject = new JongoQuery();
        queryObject.setQuery("{\"updFlg\": {$ne: 9}}");
        queryObject.setProjection("{code: 1}");
        List<CmsBtFeedInfoModel> feeds = feedInfoService.getList(channelId, queryObject);
        feeds.parallelStream().forEach(feed -> {
            String code = feed.getCode();
            try {
                List<CmsBtProductModel> productInfo = productService.getProductByOriginalCode(channelId, code);
                CmsBtProductModel productInfo1 = productService.getProductByCode(channelId, code);
                if (productInfo.size() == 0 && productInfo1 == null) {
                    // 更新 feedinfo表中的updFlg 重新出发 feed->mast
                    HashMap<String, Object> paraMap = new HashMap<>(1);
                    paraMap.put("code", code);
                    HashMap<String, Object> valueMap = new HashMap<>(1);
                    valueMap.put("updFlg", 9);
                    System.out.println("更新成功:" + code);
                    successList.add(code);
                    feedInfoService.updateFeedInfo(channelId, paraMap, valueMap);
                }
            } catch (Exception ex) {
                messageList.add(code);
            }
        });

        StringBuilder builder = new StringBuilder("<body>");
        builder.append("<h2>feed 信息列表</h2>");
        builder.append("<ul>");
        messageList.forEach(groupCheckMessage -> builder.append("<li>").append(groupCheckMessage).append("</li>"));
        builder.append("</ul>");
        builder.append("<ul>");
        successList.forEach(groupCheckMessage -> builder.append(",").append(groupCheckMessage));
        builder.append("</ul>");
        builder.append("</body>");

        return builder.toString();
    }

    @RequestMapping(value = "updateGroupNumberIid", method = RequestMethod.GET)
    public Object updateGroupNumberIid(@RequestParam("channelId") String channelId) {
        List<String> code = new ArrayList<>();

        List<CmsBtProductModel> products = cmsBtProductDao.selectAll(channelId);

        products.parallelStream().forEach(productInfo -> {
            List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");

            cartList.forEach(channelBean -> {
                String cartId = channelBean.getValue();

                CmsBtProductModel_Platform_Cart platformInfo = productInfo.getPlatformNotNull(Integer.valueOf(cartId));

                JongoQuery query = new JongoQuery();
                query.setQuery("{\"mainProductCode\": #, \"cartId\": #}");
                query.setParameters(platformInfo.getMainProductCode(), platformInfo.getCartId());
                CmsBtProductGroupModel groupModel = productGroupService.getProductGroupByQuery(channelId, query);
                if (groupModel == null) {
                    code.add("group不存在:" + platformInfo.getMainProductCode() + "--" + platformInfo.getCartId());

                }

                if (!StringUtils.isEmpty(platformInfo.getpNumIId()) && groupModel != null && StringUtils.isEmpty(groupModel.getNumIId())) {

                    groupModel.setNumIId(platformInfo.getpNumIId());
                    groupModel.setPlatformPid(platformInfo.getpProductId());
                    groupModel.setPlatformStatus(platformInfo.getpStatus());
                    groupModel.setPublishTime(platformInfo.getpPublishTime());

                    productGroupService.update(groupModel);
                    code.add(groupModel.getGroupId().toString() + ":" + platformInfo.getpNumIId());
                }

            });
        });

        StringBuilder builder = new StringBuilder("<body>");
        builder.append("<h2>feed 信息列表</h2>");
        builder.append("<ul>");
        code.forEach(groupCheckMessage -> builder.append("<li>").append(groupCheckMessage).append("</li>"));
        builder.append("</ul>");
        builder.append("<ul>");
        code.forEach(groupCheckMessage -> builder.append(",").append(groupCheckMessage));
        builder.append("</ul>");
        builder.append("</body>");

        return builder.toString();

    }


    private List<String> checkGroupTransformOn20160708(String channelId, String code) {

        List<String> messageList = new ArrayList<>();

        // 在 product 全部更新完成后
        // 获取所有的渠道下的 group
        // 对每个 group 做 product 的检查
        List<CmsBtProductGroupModel> groupModelList = productGroupDao.selectAll(channelId);

        groupModelList.parallelStream().forEach(groupModel -> {

            Integer groupCartId = groupModel.getCartId();

            if (groupCartId == null) {
                messageList.add(String.format("groupModel.getCartId() 返回 null: %s", groupModel.get_id()));
                return;
            }

            // 先判断 group 所属平台
            // 如果是 1, 那么该数据将不再使用
            // 直接删除
            if (groupCartId.equals(1)) {
                productGroupDao.delete(groupModel);
                return;
            }

            // 如果 product 的 status 是 new, 会在上一步的 product 处理时删除掉
            // 那么此时, group 的 code list 中也许就包含已经被删除, 即数据库现在不存在的 code
            // 先过滤掉这些不存在的 code
            List<String> existedCodeList = groupModel.getProductCodes().stream().filter(productCode -> {
                long productCount = cmsBtProductDao.countByQuery("{ 'common.fields.code': # }", new Object[]{productCode}, channelId);
                return productCount > 0;
            }).collect(toList());

            // 如果 该group下面没有满足条件的code, 该group就删除
            if (existedCodeList.isEmpty()) {
                productGroupDao.delete(groupModel);
                return;
            }

            // 经过过滤的数据如果和之前的长度相同
            // 说明这个 group 下的 product 数据都是正常转换完成了的
            // 对于 group 来说就不需要再进一步处理了
            if (groupModel.getProductCodes().size() == existedCodeList.size())
                return;

            System.out.println(existedCodeList.toArray());
            groupModel.setProductCodes(existedCodeList);

//            String oldMainProductCode = groupModel.getMainProductCode();
//
//            if (existedCodeList.contains(oldMainProductCode)) {
//                // 如果存在, 说明还没有删除
//                // 那么维持老的数据即可
//                // 只需要扔掉那些删除掉得 code
//                groupModel.setProductCodes(existedCodeList);
//            } else {
//                // 如果 group 现在使用的 main product 不在过滤后的集合里
//                // 说明这个 code 已经被上一步删除了
//                // 那么
//                // 除了切换 group 的 main product code 外
//                // 还需要对每一个 group 下现存的 code 做 main 相关的属性切换
//                String newMainProductCode = existedCodeList.get(0);
//                groupModel.setMainProductCode(newMainProductCode);
//                existedCodeList.forEach(existedCode -> {
//                    // 更新每一个 code
//                    // 更新他们平台属性下, 与当前 group 平台对应的 pIsMain 和 mainProductCode
//                    // 不过 P0 只有 mainProductCode
//                    // 同时还要更新 common fields 里的 isMasterMain 属性
//                    // 当然 pIsMain 和 isMasterMain 只有在当前更新的 code 是 newMainProductCode 的时候才修改
//                    CmsBtProductModel productModel = productService.getProductByCode(channelId, existedCode);
//                    boolean isMain = existedCode.equals(newMainProductCode);
//                    Map<String, Object> productSetParams = new HashMap<>(3);
//
//                    // 因为有可能并行 (parallelStream) 执行
//                    // 也就是有可能并发更新同一个商品
//                    // 所以更新平台信息时, 只更新当前 group 对应的平台属性
//                    // common 下的属性交给 P0 平台更新
//
//                    CmsBtProductModel_Platform_Cart currentGroupProductPlatform = productModel.getPlatform(groupCartId);
//
//                    if (currentGroupProductPlatform == null) {
//                        messageList.add(String.format("productModel.getPlatform(groupCartId) 返回 null: Product: %s, Group: %s", existedCode, groupModel.get_id()));
//                        return;
//                    }
//
//                    if (isMain) {
//                        if (groupCartId.equals(0))
//                            productSetParams.put("common.fields.isMasterMain", 1);
//                        else
//                            productSetParams.put("platforms.P" + groupCartId + ".pIsMain", 1);
//                    }
//
//                    productSetParams.put("platforms.P" + groupCartId + ".mainProductCode", newMainProductCode);
//
//                    HashMap<String, Object> updateMap = new HashMap<>();
//                    updateMap.put("$set", productSetParams);
//
//                    Map<String, Object> productQueryParams = new HashMap<>();
//                    productQueryParams.put("_id", productModel.get_id());
//                    productService.updateProduct(channelId, productQueryParams, updateMap);
//                });
//            }

            productGroupService.update(groupModel);
        });

        return messageList;
    }

    /**
     * 获取所有叶子类目的路径
     *
     * @param channelId 渠道
     * @param cartId    平台 ID
     * @return Map 键 -> CategoryId, 值 -> CategoryPath
     */
    private Map<String, CmsMtPlatformCategoryTreeModel> getPlatformMap(String channelId, Integer cartId) {

        // --> 取平台所有类目
        List<CmsMtPlatformCategoryTreeModel> platformCategoryTreeModels = platformCategoryService.getPlatformCategories(channelId, cartId);

        // --> 所有平台类目 --> 取所有叶子 --> 拍平
        Stream<CmsMtPlatformCategoryTreeModel> platformCategoryTreeModelStream =
                platformCategoryTreeModels.stream().flatMap(this::flattenFinal);

        // --> 所有平台类目 --> 取所有叶子 --> 拍平 --> 转 Map, id 为键, path 为值
        return platformCategoryTreeModelStream
                .collect(toMap(
                        CmsMtPlatformCategoryTreeModel::getCatPath,
                        model -> model));
    }

    /**
     * 拍平叶子类目,不包含父级
     *
     * @param platformCategoryTreeModel 平台类目模型
     * @return 叶子类目数据流
     */
    private Stream<CmsMtPlatformCategoryTreeModel> flattenFinal(CmsMtPlatformCategoryTreeModel platformCategoryTreeModel) {

        if (platformCategoryTreeModel.getIsParent() == 0)
            return Stream.of(platformCategoryTreeModel);

        List<CmsMtPlatformCategoryTreeModel> children = platformCategoryTreeModel.getChildren();

        if (children == null) children = new ArrayList<>(0);

        return children.stream().flatMap(this::flattenFinal);
    }

    /**
     * 测试getWmsProductsInfo方法
     *
     * @param channelId  店铺Id
     * @param productSku sku
     * @return ProductForWmsBean对象
     */
    @RequestMapping(value = "testGetWmsProductInfo", method = RequestMethod.GET)
    public Object testGetWmsProductInfo(@RequestParam("channelId") String channelId, @RequestParam("productSku") String productSku) {
        return JacksonUtil.bean2Json(productService.getWmsProductsInfo(channelId, productSku, null));
    }

    /**
     * 测试getOmsProductsInfo方法
     *
     * @param channelId           店铺Id
     * @param skuIncludes         检索sku
     * @param skuFlg              1:单个sku,2:多个sku
     * @param nameIncludes        名称
     * @param descriptionIncludes 描述
     * @param cartId              平台Id
     * @return List<ProductForOmsBean>对象
     */
    @RequestMapping(value = "testGetOmsProductInfo", method = RequestMethod.GET)
    public Object testGetWmsProductInfo(@RequestParam("channelId") String channelId, @RequestParam("skuIncludes") String skuIncludes
            , @RequestParam("skuFlg") String skuFlg
            , @RequestParam("nameIncludes") String nameIncludes
            , @RequestParam("descriptionIncludes") String descriptionIncludes
            , @RequestParam("cartId") String cartId) {
        if ("1".equals(skuFlg)) {
            return JacksonUtil.bean2Json(productService.getOmsProductsInfo(channelId, skuIncludes, null, nameIncludes, descriptionIncludes, cartId, null));
        } else {
            List<String> skus = new ArrayList<>();
            Collections.addAll(skus, skuIncludes.split(","));
            return JacksonUtil.bean2Json(productService.getOmsProductsInfo(channelId, null, skus, nameIncludes, descriptionIncludes, cartId, null));
        }
    }

    /**
     * 测试getOmsProductsInfo方法
     *
     * @param channelId 店铺Id
     * @param cartId    平台Id
     * @return List<ProductForOmsBean>对象
     */
    @RequestMapping(value = "updateProductPlatformIsSale", method = RequestMethod.GET)
    public Object updateProductPlatformIsSale(@RequestParam("channelId") String channelId
            , @RequestParam("code") String code
            , @RequestParam("cartId") String cartId) {
        {

            List<CmsBtProductModel> productInfo = new ArrayList<>();
            if (StringUtils.isEmpty(code))
                productInfo = cmsBtProductDao.select("{\"platforms.P27.skus.isSale\": {$in: [\"true\", \"false\"]}}", channelId);
            else
                productInfo = cmsBtProductDao.select("{\"common.fields.code\": \"" + code + "\"}", channelId);


            productInfo.parallelStream().forEach(product -> {

                product.getPlatform(Integer.valueOf(cartId)).getSkus().forEach(sku ->
                        sku.setAttribute("isSale", "true".equals(sku.getStringAttribute("isSale")))
                );

                HashMap<String, Object> queryMap = new HashMap<>();
                queryMap.put("prodId", product.getProdId());
                List<BulkUpdateModel> bulkList = new ArrayList<>();
                HashMap<String, Object> updateMap = new HashMap<>();
//                platformModel.setModified(DateTimeUtil.getNowTimeStamp());
                updateMap.put("platforms.P" + cartId, product.getPlatform(Integer.valueOf(cartId)));
                BulkUpdateModel model = new BulkUpdateModel();
                model.setUpdateMap(updateMap);
                model.setQueryMap(queryMap);
                bulkList.add(model);
                cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, "updateProductPlatformIsSale", "$set");
            });
            return code;
        }


    }


    /**
     * 根据channelId和cartId,设置已经Approved商品所有的cart
     *
     * @param channelId
     * @return
     */
    @RequestMapping(value = "updateProductJMHashId", method = RequestMethod.GET)
    public Object updateProductJMHashId(@RequestParam("channelId") String channelId) {

        List<String> codes = new ArrayList<>();

        List<MapModel> promotionCodes = cmsBtJmPromotionDaoExt.selectMaxJmHashId(channelId);

        promotionCodes.forEach(promtionCode -> {
            String code = promtionCode.get("productCode").toString();
            String jmHashId = promtionCode.get("jmHashId").toString();

            JongoUpdate updateQuery = new JongoUpdate();
            updateQuery.setQuery("{\"common.fields.code\": #}");
            updateQuery.setQueryParameters(code);

            updateQuery.setUpdate("{$set:{\"platforms.P27.pNumIId\": #}}");
            updateQuery.setUpdateParameters(jmHashId);

            cmsBtProductDao.updateFirst(updateQuery, channelId);


            JongoUpdate updateGroupQuery = new JongoUpdate();
            updateGroupQuery.setQuery("{\"cartId\": 27, \"productCodes\": #}");
            updateGroupQuery.setQueryParameters(code);

            updateGroupQuery.setUpdate("{$set:{\"numIId\": #}}");
            updateGroupQuery.setUpdateParameters(jmHashId);

            cmsBtProductGroupDao.updateFirst(updateGroupQuery, channelId);
            codes.add(code + "=======" + jmHashId);
        });

        StringBuilder builder = new StringBuilder("<body>");
        builder.append("<h2>feed 信息列表</h2>");
        builder.append("<ul>");
        codes.forEach(groupCheckMessage -> builder.append("<li>").append(groupCheckMessage).append("</li>"));
        builder.append("</ul>");
        builder.append("</body>");

        return builder.toString();
    }

    @RequestMapping(value = "changeProductTypeToLow", method = RequestMethod.GET)
    public Object changeProductTypeToLow(@RequestParam("channelId") String channelId) {
        List<String> code = new ArrayList<>();

        List<CmsBtProductModel> products = cmsBtProductDao.selectAll(channelId);

        products.parallelStream().forEach(productInfo -> {
            List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");

            cartList.forEach(channelBean -> {
                String cartId = channelBean.getValue();

                CmsBtProductModel_Platform_Cart platformInfo = productInfo.getPlatformNotNull(Integer.valueOf(cartId));

                JongoQuery query = new JongoQuery();
                query.setQuery("{\"mainProductCode\": #, \"cartId\": #}");
                query.setParameters(platformInfo.getMainProductCode(), platformInfo.getCartId());
                CmsBtProductGroupModel groupModel = productGroupService.getProductGroupByQuery(channelId, query);
                if (groupModel == null) {
                    code.add("group不存在:" + platformInfo.getMainProductCode() + "--" + platformInfo.getCartId());

                }

                if (!StringUtils.isEmpty(platformInfo.getpNumIId()) && groupModel != null && StringUtils.isEmpty(groupModel.getNumIId())) {

                    groupModel.setNumIId(platformInfo.getpNumIId());
                    groupModel.setPlatformPid(platformInfo.getpProductId());
                    groupModel.setPlatformStatus(platformInfo.getpStatus());
                    groupModel.setPublishTime(platformInfo.getpPublishTime());

                    productGroupService.update(groupModel);
                    code.add(groupModel.getGroupId().toString() + ":" + platformInfo.getpNumIId());
                }

            });
        });

        StringBuilder builder = new StringBuilder("<body>");
        builder.append("<h2>feed 信息列表</h2>");
        builder.append("<ul>");
        code.forEach(groupCheckMessage -> builder.append("<li>").append(groupCheckMessage).append("</li>"));
        builder.append("</ul>");
        builder.append("<ul>");
        code.forEach(groupCheckMessage -> builder.append(",").append(groupCheckMessage));
        builder.append("</ul>");
        builder.append("</body>");

        return builder.toString();

    }

    /**
     * 根据channelId和cartId,设置已经Approved商品所有的cart
     * @param channelId
     * @param cartId
     * @return
     */
    @RequestMapping(value = "changeProductIsSale", method = RequestMethod.GET)
    public Object changeProductIsSale(@RequestParam("channelId") String channelId, @RequestParam("cartId") String cartId) {

        List<String> code = new ArrayList<>();

        JongoQuery query = new JongoQuery();
        query.setQuery("{\"platforms.P27.status\": \"Approved\", \"platforms.P27.skus.isSale\": {$exists: false}}");
        List<CmsBtProductModel> products = cmsBtProductDao.select(query, channelId);

        products.parallelStream().forEach(product -> {

            product.getPlatform(Integer.valueOf(cartId)).getSkus().forEach(sku ->
                    sku.setAttribute("isSale", true)
            );

            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("prodId", product.getProdId());
            List<BulkUpdateModel> bulkList = new ArrayList<>();
            HashMap<String, Object> updateMap = new HashMap<>();
//                platformModel.setModified(DateTimeUtil.getNowTimeStamp());
            updateMap.put("platforms.P" + cartId, product.getPlatform(Integer.valueOf(cartId)));
            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, "updateProductPlatformIsSale", "$set");

            code.add(product.getCommon().getFields().getCode());
        });

        StringBuilder builder = new StringBuilder("<body>");
        builder.append("<h2>feed 信息列表</h2>");
        builder.append("<ul>");
        code.forEach(groupCheckMessage -> builder.append("<li>").append(groupCheckMessage).append("</li>"));
        builder.append("</ul>");
        builder.append("<ul>");
        code.forEach(groupCheckMessage -> builder.append(",").append(groupCheckMessage));
        builder.append("</ul>");
        builder.append("</body>");

        return builder.toString();
    }

}
