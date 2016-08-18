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
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.impl.cms.BusinessLogService;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.impl.cms.product.CmsBtPriceLogService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductSkuService;
import com.voyageone.service.impl.com.ComMtValueChannelService;
import com.voyageone.service.model.cms.CmsBtBusinessLogModel;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 子店->USJOI主店产品导入服务
 *
 * 可以把USJOI主店(京东国际匠心界(928,28)，京东国际悦境(929,29))理解成为一个虚拟的销售平台，
 * 本服务是把子店(如：017 LuckyVitamin)的产品导入（复制）到USJOI主店里面，变成主店的产品（例：928,28的产品）。
 * 如果主店里面没有该商品，新增的时候，所有的产品属性都会设置；
 * 如果主店里面有对应的产品，更新的时候，只更新一部分属性（如：common.fields.images1,common.skus里面的共通sku属性，
 * PXX.skus里面的价格(不更新priceSale)等）
 *
 * @author desmond
 * @author james.li on 2016/4/6.
 * @version 2.4.0
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

    @Autowired
    private BusinessLogService businessLogService;

    // 每个channel的子店->USJOI主店导入最大件数
    private final static int UPLOAD_TO_USJOI_MAX_100 = 100;

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

        // 默认线程池最大线程数(目前最后只有2个USJOI的channelId 928, 929)
        int threadPoolCnt = 2;
        // 保存每个channel最终导入结果(成功失败件数信息)
        Map<String, String> resultMap = new HashMap<>();
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolCnt);
        for (OrderChannelBean channelBean : Channels.getUsJoiChannelList()) {
            // 启动多线程(每个USJOI channel一个线程)
            executor.execute(() -> uploadByChannel(channelBean, resultMap));
        }
        // ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
        // 当所有已经提交的任务执行完毕后将会关闭ExecutorService。
        executor.shutdown(); // 并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        try {
            // 阻塞，直到线程池里所有任务结束
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        $info("=================子店->USJOI主店导入  最终结果=====================");
        resultMap.entrySet().stream()
                .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                .forEach(p ->  $info(p.getValue()));
        $info("=================子店->USJOI主店导入  主线程结束====================");

    }

    public void uploadByChannel(OrderChannelBean channelBean, Map<String, String> resultMap) {
        int successCnt = 0;
        int errCnt = 0;

        // usjoi的channelId(928,929),同时也是子店product.platform.PXXX的cartId(928,929)
        String usjoiChannelId = channelBean.getOrder_channel_id();

        // 清除缓存（这样在synship.com_mt_value_channel表中刚追加的brand，productType，sizeType等初始化mapping信息就能立刻取得了）
        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_TypeChannel.toString());

        // --------------------------------------------------------------------------------------------
        // 品牌mapping表
        Map<String, String> mapBrandMapping = new HashMap<>();
        // 产品分类mapping表
        Map<String, String> mapProductTypeMapping = new HashMap<>();
        // 适用人群mapping表
        Map<String, String> mapSizeTypeMapping = new HashMap<>();

        // 品牌mapping作成
        List<TypeChannelBean> brandTypeChannelBeanList = TypeChannels.getTypeList(Constants.comMtTypeChannel.BRAND_41, usjoiChannelId);
        if (ListUtils.notNull(brandTypeChannelBeanList)) {
            for (TypeChannelBean typeChannelBean : brandTypeChannelBeanList) {
                if (!StringUtils.isEmpty(typeChannelBean.getAdd_name1())
                        && !StringUtils.isEmpty(typeChannelBean.getName())
                        && Constants.LANGUAGE.EN.equals(typeChannelBean.getLang_id())
                        ) {
                    // 品牌mapping表中key,value都设为小写(feed进来的brand不区分大小写)
                    mapBrandMapping.put(typeChannelBean.getAdd_name1().toLowerCase().trim(), typeChannelBean.getName().toLowerCase().trim());
                }
            }
        }

        // 产品分类mapping作成
        List<TypeChannelBean> productTypeChannelBeanList = TypeChannels.getTypeList(Constants.comMtTypeChannel.PROUDCT_TYPE_57, usjoiChannelId);
        if (ListUtils.notNull(productTypeChannelBeanList)) {
            for (TypeChannelBean typeChannelBean : productTypeChannelBeanList) {
                if (!StringUtils.isEmpty(typeChannelBean.getValue())
                        && !StringUtils.isEmpty(typeChannelBean.getName())
                        && Constants.LANGUAGE.EN.equals(typeChannelBean.getLang_id())
                        ) {
                    // 产品分类mapping表(value是key,name和add_name1是值)
                    mapProductTypeMapping.put(typeChannelBean.getValue().trim(), typeChannelBean.getName().trim());
                }
            }
        }

        // 适用人群mapping作成
        List<TypeChannelBean> sizeTypeChannelBeanList = TypeChannels.getTypeList(Constants.comMtTypeChannel.PROUDCT_TYPE_58, usjoiChannelId);
        if (ListUtils.notNull(sizeTypeChannelBeanList)) {
            for (TypeChannelBean typeChannelBean : sizeTypeChannelBeanList) {
                if (!StringUtils.isEmpty(typeChannelBean.getValue())
                        && !StringUtils.isEmpty(typeChannelBean.getName())
                        && Constants.LANGUAGE.EN.equals(typeChannelBean.getLang_id())
                        ) {
                    // 适用人群mapping作成(value是key,name和add_name1是值)
                    mapSizeTypeMapping.put(typeChannelBean.getValue().trim(), typeChannelBean.getName().trim());
                }
            }
        }
        // --------------------------------------------------------------------------------------------

        // 每个channel读入子店数据上新到USJOI主店
        List<CmsBtSxWorkloadModel> cmsBtSxWorkloadModels = cmsBtSxWorkloadDaoExt.selectSxWorkloadModelWithCartId(
                UPLOAD_TO_USJOI_MAX_100, Integer.parseInt(channelBean.getOrder_channel_id()));
        for (CmsBtSxWorkloadModel sxWorkloadModel : cmsBtSxWorkloadModels) {
            try {
                // 循环上传单个产品到USJOI主店
                upload(sxWorkloadModel, mapBrandMapping, mapProductTypeMapping, mapSizeTypeMapping);
                successCnt++;
            } catch (Exception e) {
                errCnt++;
                // 继续循环做下一条子店->USJOI导入
            }
        }

        String resultInfo = usjoiChannelId + " " + channelBean.getFull_name() +
                "USJOI主店从子店（可能为多个子店）中导入产品结果 [总件数:" + cmsBtSxWorkloadModels.size()
                + " 成功:" + successCnt + " 失败:" + errCnt + "]";
//            $info(resultInfo);
        // 将该channel的子店->主店导入信息加入map，供channel导入线程全部完成一起显示
        resultMap.put(usjoiChannelId, resultInfo);

        // 清除缓存（这样在synship.com_mt_value_channel表中刚追加的brand，productType，sizeType等初始化mapping信息就能立刻生效了）
        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_TypeChannel.toString());
    }


    public void upload(CmsBtSxWorkloadModel sxWorkLoadBean,
                       Map<String, String> mapBrandMapping,
                       Map<String, String> mapProductTypeMapping,
                       Map<String, String> mapSizeTypeMapping) {

        // workload表中的cartId是usjoi的channelId(928,929),同时也是子店product.platform.PXXX的cartId(928,929)
        String usJoiChannelId = sxWorkLoadBean.getCartId().toString();

        try {
            $info(String.format("channelId:%s  groupId:%d  复制到%s 开始", sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getGroupId(), usJoiChannelId));

            // 获取当前usjoi channel, 有多少个platform
            List<TypeChannelBean> usjoiTypeChannelBeanList = TypeChannels.getTypeListSkuCarts(usJoiChannelId, "D", "en"); // 取得展示用数据
            if (ListUtils.isNull(usjoiTypeChannelBeanList)) {
                String errMsg = "com_mt_value_channel表中没有usJoiChannel("+usJoiChannelId+")对应的展示用(53 D en)mapping" +
                        "信息,不能插入usJoiGroup信息，终止UploadToUSJoiServie处理";
                $info(errMsg);
                throw new BusinessException(errMsg);
            }

            List<CmsBtProductBean> productModels = productService.getProductByGroupId(sxWorkLoadBean.getChannelId(), new Long(sxWorkLoadBean.getGroupId()), false);

            if (ListUtils.isNull(productModels)) {
                String errMsg = "没有找到对应的group信息 (ChannelId:" + sxWorkLoadBean.getChannelId() + " GroupId:" +
                        sxWorkLoadBean.getGroupId() + ")";
                $info(errMsg);
                throw new BusinessException(errMsg);
            }

            $info("productModels" + productModels.size());
            //从group中过滤出需要上的usjoi的产品(P928,P929平台下包含已approved 并且 isSale为true 的sku的产品)
            productModels = getUsjoiProductModel(productModels, sxWorkLoadBean.getCartId());
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

            // 取得USJOI店铺channel对应的cartId列表（一般只有一条cartId.如928对应28, 929对应29）
            final List<Integer> cartIds;
            OrderChannelBean usJoiBean = Channels.getChannel(usJoiChannelId);
            if (usJoiBean != null && !StringUtil.isEmpty(usJoiBean.getCart_ids())) {
                cartIds = Arrays.asList(usJoiBean.getCart_ids().split(",")).stream().map(Integer::parseInt).collect(toList());
            } else {
                cartIds = new ArrayList<>();
            }

            for (CmsBtProductModel productModel : productModels) {
                productModel = JacksonUtil.json2Bean(JacksonUtil.bean2Json(productModel),CmsBtProductModel.class);
                productModel.set_id(null);

                CmsBtProductModel pr = productService.getProductByCode(usJoiChannelId, productModel.getCommon().getFields().getCode());
                if (pr == null) {
                    // 产品不存在，新增
                    productModel.setChannelId(usJoiChannelId);
                    productModel.setOrgChannelId(sxWorkLoadBean.getChannelId());
                    productModel.setSales(new CmsBtProductModel_Sales());
                    productModel.setTags(new ArrayList<>());
                    // 插入或者更新cms_bt_product_group_c928中的productGroup信息
                    creatGroup(productModel, usJoiChannelId, usjoiTypeChannelBeanList);

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
                    // 新规作成P0平台信息
                    CmsBtProductModel_Platform_Cart p0 = getPlatformP0(groupModel, productModel);
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
                        // 新规作成P0平台信息
                        CmsBtProductModel_Platform_Cart p0 = getPlatformP0(groupModel, productModel);
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
                    // 插入或者更新cms_bt_product_group_c928中的productGroup信息
                    creatGroup(pr, usJoiChannelId, usjoiTypeChannelBeanList);
                    // 将USJOI店的产品加入更新对象产品列表中（取得USJOI店的品牌，产品分类和适用人群）
                    targetProductList.add(pr);
                }
            }

            // 记录商品价格变动履历,并向Mq发送消息同步sku,code,group价格范围
            addPriceHistoryAndSyncPriceScope(usJoiChannelId, targetProductList);

            // 如果Synship.com_mt_value_channel表中没有usjoi channel(928,929)对应的品牌，产品类型或适用人群信息，则插入该信息
            insertMtValueChannelInfo(usJoiChannelId, mapBrandMapping, mapProductTypeMapping, mapSizeTypeMapping, targetProductList);

            // 子店->USJOI主店产品导入的虚拟上新成功之后回写workload表中的状态(1:USJOI上新成功)
            sxWorkLoadBean.setPublishStatus(1);
            cmsBtSxWorkloadDaoExt.updateSxWorkloadModel(sxWorkLoadBean);

            // 子店->USJOI主店产品导入的虚拟上新成功之后,取得子店的productGroup信息，设置状态，调用共通的上新成功回写方法，回写子店状态
            // 子店的group表里面getPlatformActive是由feed->master导入根据配置设置的，上新成功之后，根据这个值回写状态（因为是虚拟上新，所以回写的这个状态没啥太大意义）
            CmsBtProductGroupModel cmsBtProductGroupModel = productGroupService.getProductGroupByGroupId(sxWorkLoadBean.getChannelId(),sxWorkLoadBean.getGroupId());
            if (CmsConstants.PlatformActive.ToOnSale.equals(cmsBtProductGroupModel.getPlatformActive())) {
                // 如果子店group将PlatformActive初始值设置成ToOnSale的时候,回写成OnSale状态
                cmsBtProductGroupModel.setOnSaleTime(DateTimeUtil.getNowTimeStamp());
                cmsBtProductGroupModel.setPlatformStatus(CmsConstants.PlatformStatus.OnSale);
            } else {
                // 默认设为ToInStock,回写InStock状态
                cmsBtProductGroupModel.setInStockTime(DateTimeUtil.getNowTimeStamp());
                cmsBtProductGroupModel.setPlatformStatus(CmsConstants.PlatformStatus.InStock);
            }

            // 上新对象产品Code列表
            List<String> listSxCode = productModels.stream().map(p -> p.getCommon().getFields().getCode()).collect(Collectors.toList());
            // 回写USJOI导入成功状态到子店productGroup和product
            productGroupService.updateGroupsPlatformStatus(cmsBtProductGroupModel, listSxCode);
            $info(String.format("channelId:%s  groupId:%d  复制到%s JOI 结束", sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getGroupId(), usJoiChannelId));
        } catch (Exception e) {
            String errMsg = "子店->USJOI主店产品导入:异常终止:[ErrMsg=";
            if (StringUtils.isNullOrBlank2(e.getMessage())) {
                errMsg += e.getStackTrace()[0].toString() + "]";
                $error(errMsg);
            } else {
                errMsg += e.getMessage() + "]";
            }
            // 将子店->主店的上新workload的状态更新为2(导入上新失败)
            sxWorkLoadBean.setPublishStatus(2);
            cmsBtSxWorkloadDaoExt.updateSxWorkloadModel(sxWorkLoadBean);
            $info(String.format("channelId:%s  groupId:%d  复制到%s JOI 异常", sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getGroupId(), usJoiChannelId));
            e.printStackTrace();

            // 上新失败时回写错误状态到子店的productGroup和product
            CmsBtProductGroupModel groupModel = productGroupService.getProductGroupByGroupId(sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getGroupId());
            productGroupService.updateUploadErrorStatus(groupModel, errMsg);
            // 出错的时候将错误信息回写到cms_bt_business_log表
            insertBusinessLog(sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getCartId(),
                    StringUtils.toString(sxWorkLoadBean.getGroupId()), "", "", errMsg, getTaskName());
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            // 抛出错误，让外面统计整个usjoi channel的产品导入错误总数
            throw e;
        }
    }

    /**
     * 找出需要上到minmall的产品和sku(已经Approved 且 isSale=true)
     *
     * @param productModels 产品列表
     * @return 产品列表
     */
    private List<CmsBtProductBean> getUsjoiProductModel(List<CmsBtProductBean> productModels, Integer cartId) {

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

    /**
     * 设置group(新规和更新)
     *
     * @param cmsBtProductModel CmsBtProductModel 产品信息
     * @param usJoiChannel String USJOI店channel(929,928)
     * @param usjoiTypeChannelBeanList USJOI店展示用平台cartId（如:0,28或0,29等）
     * @return NumID是否都是空 1：是 2：否
     */
    private boolean creatGroup(CmsBtProductModel cmsBtProductModel, String usJoiChannel, List<TypeChannelBean> usjoiTypeChannelBeanList) {
//            // 价格区间设置 ( -> 调用顾步春的api自动会去设置,这里不需要设置了)

        boolean result = true;

        // 根据procutCode, 到group表中去查找所有的group信息，取得当前产品已经加入的所有group列表
        List<CmsBtProductGroupModel> existGroups = productGroupService.selectProductGroupListByCode(usJoiChannel,
                cmsBtProductModel.getCommon().getFields().getCode());

        // 循环一下
        for (TypeChannelBean shop : usjoiTypeChannelBeanList) {
            // 检查一下该产品code是否已经在这个platform存在, 如果已经存在, 那么就不需要增加code到group了
            boolean blnFound = false;
            for (CmsBtProductGroupModel group : existGroups) {
                if (group.getCartId() == Integer.parseInt(shop.getValue())) {
                    blnFound = true;
                    // NumId有值
                    if (!StringUtils.isEmpty(group.getNumIId())) {
                        result = false;
                    }
                }
            }
            if (blnFound) {
                // 该产品code已经加到该平台(如28)对应的group中了，直接跳过该平台group的新增更新
                continue;
            }

            // group对象
            CmsBtProductGroupModel group = null;
            // 聚美意外的平台取得product.model对应的group信息（如果是聚美的话，那么就是一个Code对应一个Group）
            // 目前的USJOI没有聚美平台，只有京东国际平台
            if (!CartEnums.Cart.JM.getId().equalsIgnoreCase(shop.getValue())) {
                // 获取group id
                group = getGroupIdByFeedModel(cmsBtProductModel.getChannelId(), cmsBtProductModel.getCommon().getFields().getModel(), shop.getValue());
            }

            // group id
            // 看看同一个model里是否已经有数据在cms里存在的
            //   如果已经有存在的话: 直接用哪个group id
            //   如果没有的话: 取一个最大的 + 1
            if (group == null) {
                // 创建一个platform
                group = new CmsBtProductGroupModel();
                // cart id
                group.setCartId(Integer.parseInt(shop.getValue()));
                // 获取唯一编号
                group.setGroupId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_GROUP_ID));

                group.setChannelId(cmsBtProductModel.getChannelId());
                group.setMainProductCode(cmsBtProductModel.getCommon().getFields().getCode());
                group.setProductCodes(Arrays.asList(cmsBtProductModel.getCommon().getFields().getCode()));
                group.setCreater(getTaskName());
                group.setModifier(getTaskName());

                group.setPriceMsrpSt(cmsBtProductModel.getCommon().getFields().getPriceMsrpSt());
                group.setPriceMsrpEd(cmsBtProductModel.getCommon().getFields().getPriceMsrpEd());
                group.setPriceRetailSt(cmsBtProductModel.getCommon().getFields().getPriceRetailSt());
                group.setPriceRetailEd(cmsBtProductModel.getCommon().getFields().getPriceRetailEd());
//                group.setPriceSaleSt(cmsBtProductModel.getCommon().getFields().getPriceSaleSt());
//                group.setPriceSaleEd(cmsBtProductModel.getCommon().getFields().getPriceSaleEd());
                // num iid
                group.setNumIId(""); // 因为没有上新, 所以不会有值

                // display order
//                group.setDisplayOrder(0); // TODO: 不重要且有影响效率的可能, 有空再设置

                // platform status:发布状态: 未上新 // Synship.com_mt_type : id = 45
                group.setPlatformStatus(CmsConstants.PlatformStatus.WaitingPublish);
                // platform active:上新的动作: 默认所有店铺是放到:仓库中
                group.setPlatformActive(CmsConstants.PlatformActive.ToInStock);
                // 取得USJOI店铺共通设置(取得该渠道的PlatformActive初始值)(928,28)(929,29)
                if (CmsConstants.PlatformActive.ToOnSale.name().equalsIgnoreCase(getPlatformActive(usJoiChannel, NumberUtils.toInt(shop.getValue())).name())) {
                    // 设置USJOI店铺共通设置中platformActive初始值为ToOnSale,则设为ToOnSale
                    group.setPlatformActive(CmsConstants.PlatformActive.ToOnSale);
                }

                // qty
                group.setQty(0); // 初始为0, 之后会有库存同步程序把这个地方的值设为正确的值的

                cmsBtProductGroupDao.insert(group);
            } else {
                // ProductCodes
                List<String> oldCodes = group.getProductCodes();
                if (oldCodes == null || oldCodes.isEmpty()) {
                    List<String> codes = new ArrayList<>();
                    codes.add(cmsBtProductModel.getCommon().getFields().getCode());
                    group.setProductCodes(codes);
                } else {
                    oldCodes.add(cmsBtProductModel.getCommon().getFields().getCode());
                    group.setProductCodes(oldCodes);
                }
                group.setModifier(getTaskName());

                if (group.getPriceMsrpSt() == null || group.getPriceMsrpSt().compareTo(cmsBtProductModel.getCommon().getFields().getPriceMsrpSt()) > 0) {
                    group.setPriceMsrpSt(cmsBtProductModel.getCommon().getFields().getPriceMsrpSt());
                }
                if (group.getPriceMsrpEd() == null || group.getPriceMsrpEd().compareTo(cmsBtProductModel.getCommon().getFields().getPriceMsrpEd()) < 0) {
                    group.setPriceMsrpEd(cmsBtProductModel.getCommon().getFields().getPriceMsrpEd());
                }

                if (group.getPriceRetailSt() == null || group.getPriceRetailSt().compareTo(cmsBtProductModel.getCommon().getFields().getPriceRetailSt()) > 0) {
                    group.setPriceRetailSt(cmsBtProductModel.getCommon().getFields().getPriceRetailSt());
                }
                if (group.getPriceRetailEd() == null || group.getPriceRetailEd().compareTo(cmsBtProductModel.getCommon().getFields().getPriceRetailEd()) < 0) {
                    group.setPriceRetailEd(cmsBtProductModel.getCommon().getFields().getPriceRetailEd());
                }

//                if (group.getPriceSaleSt() == null || group.getPriceSaleSt().compareTo(cmsBtProductModel.getCommon().getFields().getPriceSaleSt()) > 0) {
//                    group.setPriceSaleSt(cmsBtProductModel.getCommon().getFields().getPriceSaleSt());
//                }
//                if (group.getPriceSaleEd() == null || group.getPriceSaleEd().compareTo(cmsBtProductModel.getCommon().getFields().getPriceSaleEd()) < 0) {
//                    group.setPriceSaleEd(cmsBtProductModel.getCommon().getFields().getPriceSaleEd());
//                }

                cmsBtProductGroupDao.update(group);
                // is Main
                // TODO 修改设置isMain属性
//                platform.setIsMain(false);
            }
        }

        return result;
    }

    /**
     * 新规作成P0平台信息
     *
     * @param groupModel CmsBtProductGroupModel group信息
     * @param productModel CmsBtProductModel product信息
     * @return P0 CmsBtProductModel_Platform_Cart P0平台信息
     */
    private CmsBtProductModel_Platform_Cart getPlatformP0(CmsBtProductGroupModel groupModel, CmsBtProductModel productModel) {

        CmsBtProductModel_Platform_Cart P0 = new CmsBtProductModel_Platform_Cart();
        P0.put("cartId", 0);
        if (groupModel != null && !StringUtils.isEmpty(groupModel.getMainProductCode())) {
            // 如果存在group信息，则将group的mainProductCode设为mainProductCode
            P0.setMainProductCode(groupModel.getMainProductCode());
            if (groupModel.getMainProductCode().equals(productModel.getCommon().getFields().getCode())) {
                productModel.getCommon().getFields().setIsMasterMain(1);
            } else {
                productModel.getCommon().getFields().setIsMasterMain(0);
            }
        } else {
            // 如果不存在group信息，则将自身设为mainProductCode
            P0.setMainProductCode(productModel.getCommon().getFields().getCode());
            productModel.getCommon().getFields().setIsMasterMain(1);
        }

        return P0;
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
    private void addPriceHistoryAndSyncPriceScope(String usjoiChannelId, List<CmsBtProductModel> usjoiProductModels) {

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

    /**
     * 出错的时候将错误信息回写到cms_bt_business_log表
     *
     * @param channelId String 子店渠道id
     * @param cartId String 平台id
     * @param groupId String GroupId
     * @param productCode String 产品code
     * @param errCode String 错误code
     * @param errMsg String 错误消息
     * @param modifier String 更新者
     */
    private void insertBusinessLog(String channelId, Integer cartId, String groupId, String productCode, String errCode, String errMsg, String modifier) {
        CmsBtBusinessLogModel businessLogModel = new CmsBtBusinessLogModel();
        // 子店渠道id
        businessLogModel.setChannelId(channelId);
        // USJOI平台id(928,929)
        businessLogModel.setCartId(cartId);
        // GroupId
        businessLogModel.setGroupId(groupId);
        // ProduCode
        businessLogModel.setCode(productCode);
        // 错误类型(2:非上新错误)
        businessLogModel.setErrorTypeId(2);
        // 错误code
        businessLogModel.setErrorCode(errCode);
        // 详细错误信息
        businessLogModel.setErrorMsg(errMsg);
        // 状态(0:未处理 1:已处理)
        businessLogModel.setStatus(0);
        // 创建者
        businessLogModel.setCreater(modifier);
        // 更新者
        businessLogModel.setModifier(modifier);

        businessLogService.insertBusinessLog(businessLogModel);
    }


}
