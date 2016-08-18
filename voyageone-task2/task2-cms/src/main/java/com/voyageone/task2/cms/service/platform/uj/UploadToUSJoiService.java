package com.voyageone.task2.cms.service.platform.uj;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.impl.cms.product.CmsBtPriceLogService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductSkuService;
import com.voyageone.service.impl.com.ComMtValueChannelService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

//import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;

/**
 * @author james.li on 2016/4/6.
 * @version 2.0.0
 */
@Service
public class UploadToUSJoiService extends BaseTaskService {

    @Autowired
    ProductGroupService productGroupService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;

    @Autowired
    private ProductSkuService productSkuService;

    @Autowired
    private MongoSequenceService commSequenceMongoService;

    @Autowired
    private CmsBtSxWorkloadDaoExt cmsBtSxWorkloadDaoExt;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    private ComMtValueChannelService comMtValueChannelService;    // 更新Synship.com_mt_value_channel表

    @Autowired
    private CmsBtPriceLogService cmsBtPriceLogService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsUploadProductToUSJoiJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 清除缓存（这样在synship.com_mt_value_channel表中刚追加的brand，productType，sizeType等初始化mapping信息就能立刻取得了）
        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_TypeChannel.toString());

        for (OrderChannelBean channelBean : Channels.getUsJoiChannelList()) {
            List<CmsBtSxWorkloadModel> cmsBtSxWorkloadModels = cmsBtSxWorkloadDaoExt.selectSxWorkloadModelWithCartId(100, Integer.parseInt(channelBean.getOrder_channel_id()));
            cmsBtSxWorkloadModels.forEach(this::upload);
        }

        // 清除缓存（这样在synship.com_mt_value_channel表中刚追加的brand，productType，sizeType等初始化mapping信息就能立刻生效了）
        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_TypeChannel.toString());
    }

    public void upload(CmsBtSxWorkloadModel sxWorkLoadBean) {
        // --------------------------------------------------------------------------------------------
        // 品牌mapping表
        Map<String, String> mapBrandMapping = new HashMap<>();
        // 产品分类mapping表
        Map<String, String> mapProductTypeMapping = new HashMap<>();
        // 适用人群mapping表
        Map<String, String> mapSizeTypeMapping = new HashMap<>();
        // --------------------------------------------------------------------------------------------

        // workload表中的cartId是usjoi的channelId(928,929),同时也是子店product.platform.PXXX的cartId(928,929)
        String usJoiChannelId = sxWorkLoadBean.getCartId().toString();

        // --------------------------------------------------------------------------------------------
        // 品牌mapping作成
        List<TypeChannelBean> brandTypeChannelBeanList = TypeChannels.getTypeList(Constants.comMtTypeChannel.BRAND_41, usJoiChannelId);

        if (ListUtils.notNull(brandTypeChannelBeanList)) {
            for (TypeChannelBean typeChannelBean : brandTypeChannelBeanList) {
                if (
                        !StringUtils.isEmpty(typeChannelBean.getAdd_name1())
                                && !StringUtils.isEmpty(typeChannelBean.getName())
                                && Constants.LANGUAGE.EN.equals(typeChannelBean.getLang_id())
                        ) {
                    // 品牌mapping表中key,value都设为小写(feed进来的brand不区分大小写)
                    mapBrandMapping.put(typeChannelBean.getAdd_name1().toLowerCase().trim(), typeChannelBean.getName().toLowerCase().trim());
                }
            }
        }

        // 产品分类mapping作成
        List<TypeChannelBean> productTypeChannelBeanList = TypeChannels.getTypeList(Constants.comMtTypeChannel.PROUDCT_TYPE_57, usJoiChannelId);

        if (ListUtils.notNull(productTypeChannelBeanList)) {
            for (TypeChannelBean typeChannelBean : productTypeChannelBeanList) {
                if (
                        !StringUtils.isEmpty(typeChannelBean.getValue())
                                && !StringUtils.isEmpty(typeChannelBean.getName())
                                && Constants.LANGUAGE.EN.equals(typeChannelBean.getLang_id())
                        ) {
                    // 产品分类mapping表(value是key,name和add_name1是值)
                    mapProductTypeMapping.put(typeChannelBean.getValue().trim(), typeChannelBean.getName().trim());
                }
            }
        }

        // 适用人群mapping作成
        List<TypeChannelBean> sizeTypeChannelBeanList = TypeChannels.getTypeList(Constants.comMtTypeChannel.PROUDCT_TYPE_58, usJoiChannelId);

        if (ListUtils.notNull(sizeTypeChannelBeanList)) {
            for (TypeChannelBean typeChannelBean : sizeTypeChannelBeanList) {
                if (
                        !StringUtils.isEmpty(typeChannelBean.getValue())
                                && !StringUtils.isEmpty(typeChannelBean.getName())
                                && Constants.LANGUAGE.EN.equals(typeChannelBean.getLang_id())
                        ) {
                    // 适用人群mapping作成(value是key,name和add_name1是值)
                    mapSizeTypeMapping.put(typeChannelBean.getValue().trim(), typeChannelBean.getName().trim());
                }
            }
        }
        // --------------------------------------------------------------------------------------------

        try {
            $info(String.format("channelId:%s  groupId:%d  复制到%s 开始", sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getGroupId(), usJoiChannelId));
            List<CmsBtProductBean> productModels = productService.getProductByGroupId(sxWorkLoadBean.getChannelId(), new Long(sxWorkLoadBean.getGroupId()), false);

            if (ListUtils.isNull(productModels)) {
                String errMsg = "没有找到对应的group信息 (ChannelId:" + sxWorkLoadBean.getChannelId() + " GroupId:" +
                        sxWorkLoadBean.getGroupId() + ")";
                $info(errMsg);
                throw new BusinessException(errMsg);
            }

            $info("productModels" + productModels.size());
            //从group中过滤出需要上的usjoi的产品(P928,P929平台下包含已approved 并且 isSale为true 的sku的产品)
            productModels = getUSjoiProductModel(productModels, sxWorkLoadBean.getCartId());
            if (productModels.size() == 0) {
                throw new BusinessException("没有找到需要上新的SKU");
            } else {
                $info("有" + productModels.size() + "个产品要复制");
            }

            // 新增或更新产品列表，用于最后插入品牌，产品类型和使用人群用
            List<CmsBtProductModel> targetProductList = new ArrayList<>();
            // 取得USJOI店铺共通设置(是否自动同步人民币专柜价格)
            boolean usjoiIsAutoSyncPriceMsrp = isAutoSyncPriceMsrp(usJoiChannelId);
            // USJOI店铺共通设置(取得该渠道的PlatformActive初始值)
            CmsConstants.PlatformActive usjoiPlatformActive = CmsConstants.PlatformActive.ToInStock;

            for (CmsBtProductModel productModel : productModels) {
                productModel = JacksonUtil.json2Bean(JacksonUtil.bean2Json(productModel),CmsBtProductModel.class);
                productModel.set_id(null);

                final List<Integer> cartIds;
                OrderChannelBean usJoiBean = Channels.getChannel(usJoiChannelId);
                if (usJoiBean != null && !StringUtil.isEmpty(usJoiBean.getCart_ids())) {
                    cartIds = Arrays.asList(usJoiBean.getCart_ids().split(",")).stream().map(Integer::parseInt).collect(toList());
                } else {
                    cartIds = new ArrayList<>();
                }

                CmsBtProductModel pr = productService.getProductByCode(usJoiChannelId, productModel.getCommon().getFields().getCode());
                if (pr == null) {
                    // 产品不存在，新增
                    productModel.setChannelId(usJoiChannelId);
                    productModel.setOrgChannelId(sxWorkLoadBean.getChannelId());
                    productModel.setSales(new CmsBtProductModel_Sales());
                    productModel.setTags(new ArrayList<>());
                    // 插入或者更新cms_bt_product_group_c928中的productGroup信息
                    creatGroup(productModel, usJoiChannelId);

                    productModel.setProdId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_PROD_ID));

                    // platform对应 从子店的platform.p928 929 中的数据生成usjoi的platform
                    CmsBtProductModel_Platform_Cart platform = productModel.getPlatform(sxWorkLoadBean.getCartId());
                    platform.setStatus(CmsConstants.ProductStatus.Pending.toString());
                    platform.setpCatId(null);
                    platform.setpCatPath(null);
                    platform.setpBrandId(null);
                    platform.setpBrandName(null);
                    productModel.platformsClear();
                    // 下面几个cartId都设成同一个platform
                    if (platform != null) {
                        final CmsBtProductModel finalProductModel = productModel;
                        for (Integer cartId : cartIds) {
                            // 重新设置P28平台的mainProductCode和pIsMain
                            CmsBtProductGroupModel cartGroupModel = productGroupService.selectProductGroupByCode(usJoiChannelId, productModel.getCommon().getFields().getCode(), cartId);
                            if (cartGroupModel != null && !StringUtils.isEmpty(cartGroupModel.getMainProductCode())) {
                                // 如果存在group信息，则将group的mainProductCode设为mainProductCode
                                platform.setMainProductCode(cartGroupModel.getMainProductCode());
                                if (cartGroupModel.getMainProductCode().equals(productModel.getCommon().getFields().getCode())) {
                                    platform.setpIsMain(1);
                                } else {
                                    platform.setpIsMain(0);
                                }
                            } else {
                                // 如果不存在group信息，则将自身设为mainProductCode
                                platform.setMainProductCode(productModel.getCommon().getFields().getCode());
                                platform.setpIsMain(1);
                            }

                            // 下面几个cartId都设成同一个platform
                            finalProductModel.setPlatform(cartId, platform);
                        }

                    }

                    // 设置P0平台信息
                    CmsBtProductGroupModel groupModel = productGroupService.selectProductGroupByCode(usJoiChannelId, productModel.getCommon().getFields().getCode(), 0);
                    CmsBtProductModel_Platform_Cart p0 = new CmsBtProductModel_Platform_Cart();
                    p0.put("cartId", 0);
//                    p0.put("mainProductCode", groupModel.getMainProductCode());
                    if (groupModel != null && !StringUtils.isEmpty(groupModel.getMainProductCode())) {
                        // 如果存在group信息，则将group的mainProductCode设为mainProductCode
                        p0.setMainProductCode(groupModel.getMainProductCode());
                        if (groupModel.getMainProductCode().equals(productModel.getCommon().getFields().getCode())) {
                            productModel.getCommon().getFields().setIsMasterMain(1);
                        } else {
                            productModel.getCommon().getFields().setIsMasterMain(0);
                        }
                    } else {
                        // 如果不存在group信息，则将自身设为mainProductCode
                        p0.setMainProductCode(productModel.getCommon().getFields().getCode());
                        productModel.getCommon().getFields().setIsMasterMain(1);
                    }
                    productModel.getPlatforms().put("P0", p0);

                    productService.createProduct(usJoiChannelId, productModel, sxWorkLoadBean.getModifier());
                    // 将子店的产品加入更新对象产品列表中
                    targetProductList.add(productModel);
                } else {
                    // 如果已经存在（如老的数据已经有了），更新
                    // 更新common.fields.image1(品牌方商品图)
                    CmsBtProductModel_Field prCommonFields = pr.getCommon().getFields();
                    if (prCommonFields != null && productModel.getCommon().getFields() != null)
                        prCommonFields.setImages1(productModel.getCommon().getFields().getImages1());

                    for (CmsBtProductModel_Sku sku : productModel.getCommon().getSkus()) {
                        CmsBtProductModel_Sku oldSku = pr.getCommon().getSku(sku.getSkuCode());
                        if (oldSku == null) {
                            // 如果没有在usjoi产品的commom.skus找到子店对应的skuCode，则新增该sku
                            pr.getCommon().getSkus().add(sku);
                        } else {
                            // 如果在usjoi产品的commom.skus找到子店对应的skuCode时
                            // 跟feed->master统一，无条件更新尺码等共通sku属性
                            oldSku.setBarcode(sku.getBarcode());
                            oldSku.setClientSkuCode(sku.getClientSkuCode());
                            oldSku.setClientSize(sku.getClientSize());
                            oldSku.setSize(sku.getSize());
                            oldSku.setWeight((sku.getWeight()));  // 重量(单位：磅)

                            // 价格发生变化的时候更新该sku价格
                            if (oldSku.getPriceMsrp().compareTo(sku.getPriceMsrp()) != 0
                                    || oldSku.getPriceRetail().compareTo(sku.getPriceRetail()) != 0) {
                                // 美金专柜价
                                oldSku.setClientMsrpPrice(sku.getClientMsrpPrice());
                                // 美金指导价
                                oldSku.setClientRetailPrice(sku.getClientRetailPrice());
                                // 美金成本价(=priceClientCost)
                                oldSku.setClientNetPrice(sku.getClientNetPrice());
                                // 人民币专柜价(后面价格计算要用到，因为010,018等店铺不用新价格体系，还是用老的价格公式)
                                oldSku.setPriceMsrp(sku.getPriceMsrp());
                                // 人民币指导价(后面价格计算要用到，因为010,018等店铺不用新价格体系，还是用老的价格公式)
                                oldSku.setPriceRetail(sku.getPriceRetail());
                            }
                        }
                    }

                    // 由于需要无条件更新common.image1等属性，所以无条件更新common属性
                    // 共通方法里面有Approved的时候，自动插入USJOI(928,929)->平台(京东国际匠心界，悦境)上新workload记录
                    productService.updateProductCommon(usJoiChannelId, pr.getProdId(), pr.getCommon(), getTaskName(), false);

                    final CmsBtProductModel finalProductModel1 = productModel;
                    for (Integer cartId : cartIds) {
                        // 取得USJOI店铺共通设置(取得该渠道的PlatformActive初始值)
                        if (CmsConstants.PlatformActive.ToOnSale.name().equals(getPlatformActive(usJoiChannelId, cartId))) {
                            usjoiPlatformActive = CmsConstants.PlatformActive.ToOnSale;
                        }

                        CmsBtProductModel_Platform_Cart platformCart = pr.getPlatform(cartId);
                        CmsBtProductModel_Platform_Cart newPlatform = finalProductModel1.getPlatform(sxWorkLoadBean.getCartId());
                        if (platformCart == null) {
                            newPlatform.setStatus(CmsConstants.ProductStatus.Pending.toString());
                            newPlatform.setpCatId(null);
                            newPlatform.setpCatPath(null);
                            newPlatform.setpBrandId(null);
                            newPlatform.setpBrandName(null);
                            newPlatform.setCartId(cartId);
                            productService.updateProductPlatform(usJoiChannelId, pr.getProdId(), newPlatform,getTaskName());
                        } else {
                            if(platformCart.getSkus() == null){
                                platformCart.setSkus(newPlatform.getSkus());
                            }else{
                                for (BaseMongoMap<String, Object> newSku : newPlatform.getSkus()) {
                                    boolean updateFlg = false;
                                    if(platformCart.getSkus() != null) {
                                        for (BaseMongoMap<String, Object> oldSku : platformCart.getSkus()) {
                                            if (oldSku.get("skuCode").toString().equalsIgnoreCase(newSku.get("skuCode").toString())) {
                                                // 在更新前的PXX.skus找到对应的新skuCode的时候,更新价格等平台sku属性(不更新priceSale)
                                                oldSku.put("originalPriceMsrp", newSku.get("originalPriceMsrp"));
                                                if (usjoiIsAutoSyncPriceMsrp) {
                                                    // 如果USJOI店铺(928,929)配置了自动同步人民币专柜价格时，才同步priceMsrp
                                                    oldSku.put("priceMsrp", newSku.get("priceMsrp"));
                                                }
                                                oldSku.put("priceRetail", newSku.get("priceRetail"));
                                                updateFlg = true;
                                                break;
                                            }
                                        }
                                    }
                                    if(!updateFlg){
                                        platformCart.getSkus().add(newSku);
                                    }
                                    platformCart.setpPriceRetailSt(newPlatform.getpPriceRetailSt());
                                    platformCart.setpPriceRetailEd(newPlatform.getpPriceRetailEd());
                                    platformCart.setpPriceSaleSt(newPlatform.getpPriceSaleSt());
                                    platformCart.setpPriceSaleEd(newPlatform.getpPriceSaleEd());
                                }
                            }
                            productService.updateProductPlatform(usJoiChannelId, pr.getProdId(), platformCart,getTaskName());
                        }
                    }

                    if (pr.getCommon() == null || pr.getCommon().size() == 0) {
                        // 共通方法里面有Approved的时候，自动插入USJOI(928,929)->平台(京东国际匠心界，悦境)上新workload记录
                        productService.updateProductCommon(usJoiChannelId, pr.getProdId(), productModel.getCommon(),getTaskName(),false);
                    }
                    if(pr.getPlatform(0) == null){
                        CmsBtProductGroupModel groupModel = productGroupService.selectMainProductGroupByCode(usJoiChannelId, productModel.getCommon().getFields().getCode(), 0);
                        CmsBtProductModel_Platform_Cart p0 = new CmsBtProductModel_Platform_Cart();
                        p0.put("cartId",0);
//                        p0.put("mainProductCode",groupModel.getMainProductCode());
                        if (groupModel != null && !StringUtils.isEmpty(groupModel.getMainProductCode())) {
                            // 如果存在group信息，则将group的mainProductCode设为mainProductCode
                            p0.setMainProductCode(groupModel.getMainProductCode());
                            if (groupModel.getMainProductCode().equals(productModel.getCommon().getFields().getCode())) {
                                productModel.getCommon().getFields().setIsMasterMain(1);
                            } else {
                                productModel.getCommon().getFields().setIsMasterMain(0);
                            }
                        } else {
                            // 如果不存在group信息，则将自身设为mainProductCode
                            p0.setMainProductCode(productModel.getCommon().getFields().getCode());
                            productModel.getCommon().getFields().setIsMasterMain(1);
                        }
                        HashMap<String, Object> queryMap = new HashMap<>();
                        queryMap.put("prodId", pr.getProdId());

                        List<BulkUpdateModel> bulkList = new ArrayList<>();
                        HashMap<String, Object> updateMap = new HashMap<>();
                        updateMap.put("platforms.P0", p0);
                        BulkUpdateModel model = new BulkUpdateModel();
                        model.setUpdateMap(updateMap);
                        model.setQueryMap(queryMap);
                        bulkList.add(model);
                        cmsBtProductDao.bulkUpdateWithMap(usJoiChannelId, bulkList, null, "$set");
                    }
                    // 将USJOI店的产品加入更新对象产品列表中（取得USJOI店的品牌，产品分类和适用人群）
                    targetProductList.add(pr);
                }
            }

            // 记录商品价格变动履历,并向Mq发送消息同步sku,code,group价格范围
            addPriceHistorySyncScope(usJoiChannelId, targetProductList);

            // 如果Synship.com_mt_value_channel表中没有usjoi channel(928,929)对应的品牌，产品类型或适用人群信息，则插入该信息
            insertMtValueChannelInfo(usJoiChannelId, mapBrandMapping, mapProductTypeMapping, mapSizeTypeMapping, targetProductList);

            // 子店->USJOI店的虚拟上新成功之后回写workload表中的状态(1:USJOI上新成功)
            sxWorkLoadBean.setPublishStatus(1);
            cmsBtSxWorkloadDaoExt.updateSxWorkloadModel(sxWorkLoadBean);

            // 子店->USJOI店的虚拟上新成功之后,取得子店的productGroup信息，设置状态，调用共通的上新成功回写方法，回写子店状态
            CmsBtProductGroupModel cmsBtProductGroupModel = productGroupService.getProductGroupByGroupId(sxWorkLoadBean.getChannelId(),sxWorkLoadBean.getGroupId());
            // 默认设为ToInStock
            cmsBtProductGroupModel.setPlatformActive(CmsConstants.PlatformActive.ToInStock);
            if (CmsConstants.PlatformActive.ToOnSale.equals(usjoiPlatformActive)) {
                // 如果USJOI店将PlatformActive初始值设置成ToOnSale的时候
                cmsBtProductGroupModel.setPlatformActive(CmsConstants.PlatformActive.ToOnSale);
            }
            // 下面2个项目是真正向京东国际上新之后才回写的，现在设置了也没有意义
//            cmsBtProductGroupModel.setOnSaleTime(DateTimeUtil.getNowTimeStamp());
//            cmsBtProductGroupModel.setPlatformStatus(CmsConstants.PlatformStatus.InStock);

            // 上新对象产品Code列表
            List<String> listSxCode = productModels.stream().map(p -> p.getCommon().getFields().getCode()).collect(Collectors.toList());
            productGroupService.updateGroupsPlatformStatus(cmsBtProductGroupModel, listSxCode);
            $info(String.format("channelId:%s  groupId:%d  复制到%s JOI 结束", sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getGroupId(), usJoiChannelId));
        } catch (Exception e) {
            sxWorkLoadBean.setPublishStatus(2);
            cmsBtSxWorkloadDaoExt.updateSxWorkloadModel(sxWorkLoadBean);
            $info(String.format("channelId:%s  groupId:%d  复制到%s JOI 异常", sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getGroupId(), usJoiChannelId));
            e.printStackTrace();
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
        }
    }

    /**
     * 找出需要上到minmall的产品和sku(已经Approved 且 isSale=true)
     *
     * @param productModels 产品列表
     * @return 产品列表
     */
    private List<CmsBtProductBean> getUSjoiProductModel(List<CmsBtProductBean> productModels, Integer cartId) {

        List<CmsBtProductBean> usJoiProductModes = new ArrayList<>();

        // 找出928,929（usjoi的cartid）平台下包含已approved 并且 isSale为true 的sku的产品
        productModels.stream().filter(productModel -> CmsConstants.ProductStatus.Approved.name().equalsIgnoreCase(productModel.getPlatform(cartId).getStatus())).forEach(productModel -> {
            List<BaseMongoMap<String, Object>> skus = productModel.getPlatform(cartId).getSkus().stream()
                    .filter(platFormInfo -> {
                        return Boolean.valueOf(String.valueOf(platFormInfo.get("isSale")));
                    })
                    .collect(toList());
            if (skus.size() > 0) {
                productModel.getPlatform(cartId).setSkus(skus);
                usJoiProductModes.add(productModel);
            }
        });
        return usJoiProductModes;

    }

    /**
     * 根据model, 到product表中去查找, 看看这家店里, 是否有相同的model已经存在
     * 如果已经存在, 返回 找到了的那个group id
     * 如果不存在, 返回 -1
     *
     * @param channelId channel id
     * @param modelCode 品牌方给的model
     * @param cartId    cart id
     * @return group id
     */
    private CmsBtProductGroupModel getGroupIdByFeedModel(String channelId, String modelCode, String cartId) {
        // 先去看看是否有存在的了
        CmsBtProductGroupModel groupObj = productGroupService.selectProductGroupByModelCodeAndCartId(channelId, modelCode, cartId);
        return groupObj;
    }

    private void creatGroup(CmsBtProductModel cmsBtProductModel, String usJoiChannel) {
//            // 价格区间设置 ( -> 调用顾步春的api自动会去设置,这里不需要设置了)

        // 获取当前channel, 有多少个platform
        List<TypeChannelBean> typeChannelBeanList = TypeChannels.getTypeListSkuCarts(usJoiChannel, "D", "en"); // 取得展示用数据
        if (ListUtils.isNull(typeChannelBeanList)) {
            String errMsg = "com_mt_value_channel表中没有usJoiChannel("+usJoiChannel+")对应的展示用(53 D en)mapping" +
                    "信息,不能插入usJoiGroup信息，终止UploadToUSJoiServie处理";
            $info(errMsg);
            throw new BusinessException(errMsg);
        }


        // 循环一下
        for (TypeChannelBean shop : typeChannelBeanList) {

            // 获取group id
            CmsBtProductGroupModel platform = getGroupIdByFeedModel(cmsBtProductModel.getChannelId(), cmsBtProductModel.getCommon().getFields().getModel(), shop.getValue());

            // group id
            // 看看同一个model里是否已经有数据在cms里存在的
            //   如果已经有存在的话: 直接用哪个group id
            //   如果没有的话: 取一个最大的 + 1
            if (platform == null) {
                // 创建一个platform
                platform = new CmsBtProductGroupModel();
                // cart id
                platform.setCartId(Integer.parseInt(shop.getValue()));
                // 获取唯一编号
                platform.setGroupId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_GROUP_ID));

                platform.setChannelId(cmsBtProductModel.getChannelId());
                platform.setMainProductCode(cmsBtProductModel.getCommon().getFields().getCode());
                platform.setProductCodes(Arrays.asList(cmsBtProductModel.getCommon().getFields().getCode()));

                platform.setPriceMsrpSt(cmsBtProductModel.getCommon().getFields().getPriceMsrpSt());
                platform.setPriceMsrpEd(cmsBtProductModel.getCommon().getFields().getPriceMsrpEd());
                platform.setPriceRetailSt(cmsBtProductModel.getCommon().getFields().getPriceRetailSt());
                platform.setPriceRetailEd(cmsBtProductModel.getCommon().getFields().getPriceRetailEd());
//                platform.setPriceSaleSt(cmsBtProductModel.getCommon().getFields().getPriceSaleSt());
//                platform.setPriceSaleEd(cmsBtProductModel.getCommon().getFields().getPriceSaleEd());
                // num iid
                platform.setNumIId(""); // 因为没有上新, 所以不会有值

                // display order
//                platform.setDisplayOrder(0); // TODO: 不重要且有影响效率的可能, 有空再设置

                // platform status:发布状态: 未上新 // Synship.com_mt_type : id = 45
                platform.setPlatformStatus(CmsConstants.PlatformStatus.WaitingPublish);
                // platform active:上新的动作: 暂时默认所有店铺是放到:仓库中
                platform.setPlatformActive(CmsConstants.PlatformActive.ToInStock);

                // qty
                platform.setQty(0); // 初始为0, 之后会有库存同步程序把这个地方的值设为正确的值的

                cmsBtProductGroupDao.insert(platform);
            } else {
                platform.getProductCodes().add(cmsBtProductModel.getCommon().getFields().getCode());

                if (platform.getPriceMsrpSt() == null || platform.getPriceMsrpSt().compareTo(cmsBtProductModel.getCommon().getFields().getPriceMsrpSt()) > 0) {
                    platform.setPriceMsrpSt(cmsBtProductModel.getCommon().getFields().getPriceMsrpSt());
                }
                if (platform.getPriceMsrpEd() == null || platform.getPriceMsrpEd().compareTo(cmsBtProductModel.getCommon().getFields().getPriceMsrpEd()) < 0) {
                    platform.setPriceMsrpEd(cmsBtProductModel.getCommon().getFields().getPriceMsrpEd());
                }

                if (platform.getPriceRetailSt() == null || platform.getPriceRetailSt().compareTo(cmsBtProductModel.getCommon().getFields().getPriceRetailSt()) > 0) {
                    platform.setPriceRetailSt(cmsBtProductModel.getCommon().getFields().getPriceRetailSt());
                }
                if (platform.getPriceRetailEd() == null || platform.getPriceRetailEd().compareTo(cmsBtProductModel.getCommon().getFields().getPriceRetailEd()) < 0) {
                    platform.setPriceRetailEd(cmsBtProductModel.getCommon().getFields().getPriceRetailEd());
                }

//                if (platform.getPriceSaleSt() == null || platform.getPriceSaleSt().compareTo(cmsBtProductModel.getCommon().getFields().getPriceSaleSt()) > 0) {
//                    platform.setPriceSaleSt(cmsBtProductModel.getCommon().getFields().getPriceSaleSt());
//                }
//                if (platform.getPriceSaleEd() == null || platform.getPriceSaleEd().compareTo(cmsBtProductModel.getCommon().getFields().getPriceSaleEd()) < 0) {
//                    platform.setPriceSaleEd(cmsBtProductModel.getCommon().getFields().getPriceSaleEd());
//                }

                cmsBtProductGroupDao.update(platform);
                // is Main
                // TODO 修改设置isMain属性
//                platform.setIsMain(false);
            }
        }

    }

    /**
     * 如果Synship.com_mt_value_channel表中没有usjoi channel(928,929)对应的品牌，产品类型或适用人群信息，则插入该信息
     *
     * @param usjoiChannelId String usjoi channel id
     * @param mapBrandMapping Map<String, String> 品牌mapping一览
     * @param mapProductTypeMapping Map<String, String> 产品类型mapping一览
     * @param mapSizeTypeMapping Map<String, String> 适用人群mapping一览
     * @param usjoiProductModels List<CmsBtProductBean> 产品列表
     */
    private void insertMtValueChannelInfo(String usjoiChannelId, Map<String, String> mapBrandMapping, Map<String, String> mapProductTypeMapping,
                                          Map<String, String> mapSizeTypeMapping, List<CmsBtProductModel> usjoiProductModels) {

        // 循环产品列表，如果品牌，产品类型或适用人群信息，则插入该信息到Synship.com_mt_value_channel表中
        for (CmsBtProductModel usjoiProductModel : usjoiProductModels) {
            if (usjoiProductModel.getCommon() == null || usjoiProductModel.getCommon().getFields() == null) {
                continue;
            }
            // 品牌
            String usjoiBrand = usjoiProductModel.getCommon().getFields().getBrand();
            // 产品类型
            String usjoiProductType = usjoiProductModel.getCommon().getFields().getProductType();
            // 适用人群
            String usjoiSizeType = usjoiProductModel.getCommon().getFields().getSizeType();

            // 品牌(不区分大小写，全部小写)
            if (!StringUtils.isEmpty(usjoiBrand)
                    && !mapBrandMapping.containsKey(usjoiBrand.toLowerCase().trim())) {
                // 插入品牌初始中英文mapping信息到Synship.com_mt_value_channel表中
                comMtValueChannelService.insertComMtValueChannelMapping(41, usjoiChannelId,
                        usjoiBrand.toLowerCase().trim(), usjoiBrand.toLowerCase().trim(), getTaskName());
                // 将更新完整之后的mapping信息添加到前面取出来的品牌mapping表中
                mapBrandMapping.put(usjoiBrand.toLowerCase().trim(), usjoiBrand.toLowerCase().trim());
            }

            // 产品分类
            if (!StringUtils.isEmpty(usjoiProductType)
                    && !mapProductTypeMapping.containsKey(usjoiProductType)) {
                // 插入产品分类初始中英文mapping信息到Synship.com_mt_value_channel表中
                comMtValueChannelService.insertComMtValueChannelMapping(57, usjoiChannelId, usjoiProductType,
                        usjoiProductType, getTaskName());
                // 将更新完整之后的mapping信息添加到前面取出来的产品分类mapping表中
                mapProductTypeMapping.put(usjoiProductType, usjoiProductType);
            }

            // 适用人群
            if (!StringUtils.isEmpty(usjoiSizeType)
                    && !mapSizeTypeMapping.containsKey(usjoiSizeType)) {
                // 插入适用人群初始中英文mapping信息到Synship.com_mt_value_channel表中
                comMtValueChannelService.insertComMtValueChannelMapping(58, usjoiChannelId, usjoiSizeType,
                        usjoiSizeType, getTaskName());
                // 将更新完整之后的mapping信息添加到前面取出来的适用人群mapping表中
                mapSizeTypeMapping.put(usjoiSizeType, usjoiSizeType);
            }
        }
    }

    /**
     * 查看该渠道是否自动同步人民币专柜价格MSRP
     *
     * @param channelId String channel id
     */
    private boolean isAutoSyncPriceMsrp(String channelId) {
        boolean isAutoSyncPriceMsrp = false;

        CmsChannelConfigBean autoSyncPriceMsrp = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_MSRP);

        if (autoSyncPriceMsrp != null && "1".equals(autoSyncPriceMsrp.getConfigValue1()))
            isAutoSyncPriceMsrp = true;

        return isAutoSyncPriceMsrp;
    }

    /**
     * 取得该渠道的PlatformActive初始值
     *
     * @param channelId String channel id
     * @param cartId int 平台id
     * @return platformActive CmsConstants.PlatformActive 上新的动作初始值
     */
    private CmsConstants.PlatformActive getPlatformActive(String channelId, Integer cartId) {
        // platform active:上新的动作: 默认是放到:仓库中
        CmsConstants.PlatformActive platformActive = CmsConstants.PlatformActive.ToInStock;

        CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(channelId
                , CmsConstants.ChannelConfig.PLATFORM_ACTIVE
                , String.valueOf(cartId));

        if (cmsChannelConfigBean != null && CmsConstants.PlatformActive.ToOnSale.name().equals(cmsChannelConfigBean.getConfigValue1())) {
            platformActive = CmsConstants.PlatformActive.ToOnSale;
        }

        return platformActive;
    }

    /**
     * 记录商品价格变动履历,并向Mq发送消息同步sku,code,group价格范围
     *
     * @param usjoiChannelId String usjoi渠道id
     * @param usjoiProductModels List<CmsBtProductModel> usjoi产品列表
     */
    private void addPriceHistorySyncScope(String usjoiChannelId, List<CmsBtProductModel> usjoiProductModels) {

        for (CmsBtProductModel usjoiProduct : usjoiProductModels) {
            // 记录商品价格表动履历，并向Mq发送消息同步sku,code,group价格范围
            if (usjoiProduct != null && ListUtils.notNull(usjoiProduct.getCommon().getSkus())) {
                List<String> skuCodeList = usjoiProduct.getCommon().getSkus()
                        .stream()
                        .map(CmsBtProductModel_Sku::getSkuCode)
                        .collect(Collectors.toList());
                // 记录商品价格变动履历,并向Mq发送消息同步sku,code,group价格范围
                cmsBtPriceLogService.addLogForSkuListAndCallSyncPriceJob(skuCodeList, usjoiChannelId, null, getTaskName(), "子店->USJOI主店导入");
            }
        }

    }


}
