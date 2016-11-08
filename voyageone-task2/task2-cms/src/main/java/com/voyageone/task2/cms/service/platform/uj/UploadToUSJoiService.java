package com.voyageone.task2.cms.service.platform.uj;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.base.exception.CommonConfigNotFoundException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
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
import com.voyageone.service.impl.cms.prices.IllegalPriceConfigException;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.com.ComMtValueChannelService;
import com.voyageone.service.model.cms.CmsBtBusinessLogModel;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
public class UploadToUSJoiService extends BaseCronTaskService {

    @Autowired
    ProductGroupService productGroupService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;

    @Autowired
    private PriceService priceService;

    @Autowired
    private MongoSequenceService commSequenceMongoService;

    @Autowired
    private CmsBtSxWorkloadDaoExt cmsBtSxWorkloadDaoExt;

    @Autowired
    private ComMtValueChannelService comMtValueChannelService;    // 更新synship.com_mt_value_channel表

    @Autowired
    private BusinessLogService businessLogService;

    @Autowired
    private SxProductService sxProductService;

    // 每个channel的子店->USJOI主店导入最大件数
    private final static int UPLOAD_TO_USJOI_MAX_500 = 500;

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
        Map<String, String> resultMap = new ConcurrentHashMap<>();
        // 保存是否需要清空缓存(添加过品牌等信息时，需要清空缓存)
        Map<String, String> needReloadMap = new ConcurrentHashMap<>();
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolCnt);
        for (OrderChannelBean channelBean : Channels.getUsJoiChannelList()) {
            // 启动多线程(每个USJOI channel一个线程)
            executor.execute(() -> uploadByChannel(channelBean, resultMap, needReloadMap));
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

        // 判断是否需要清空缓存
        if ("1".equals(needReloadMap.get(CacheKeyEnums.KeyEnum.ConfigData_TypeChannel.toString()))) {
            // 清除缓存（这样就能马上在画面上展示出最新追加的brand，productType，sizeType等初始化mapping信息）
            TypeChannels.reload();
        }

        $info("=================子店->USJOI主店导入  最终结果=====================");
        resultMap.entrySet().stream()
                .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                .forEach(p ->  $info(p.getValue()));
        $info("=================子店->USJOI主店导入  主线程结束====================");

    }

    public void uploadByChannel(OrderChannelBean channelBean, Map<String, String> resultMap, Map<String, String> needReloadMap) {
        int successCnt = 0;
        int errCnt = 0;

        // usjoi的channelId(928,929),同时也是子店product.platform.PXXX的cartId(928,929)
        String usjoiChannelId = channelBean.getOrder_channel_id();

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

        // 获取当前usjoi channel, 有多少个platform
        List<TypeChannelBean> usjoiTypeChannelBeanList = TypeChannels.getTypeListSkuCarts(usjoiChannelId, "D", "en"); // 取得展示用数据
        if (ListUtils.isNull(usjoiTypeChannelBeanList)) {
            String errMsg = usjoiChannelId + " " + String.format("%1$-15s", channelBean.getFull_name()) + " com_mt_value_channel表中没有usJoiChannel(" + usjoiChannelId + ")对应的展示用(53 D en)mapping" +
                    "信息,不能插入usJoiGroup信息，终止UploadToUSJoiServie处理，请修改好共通数据后再导入";
            $info(errMsg);
            // channel级的共通配置异常，本USJOI channel后面的产品都不导入了
            resultMap.put(usjoiChannelId, errMsg);
            return;
        }
        // --------------------------------------------------------------------------------------------

        // 从synship.tm_order_channel表中取得USJOI店铺channel对应的cartId列表（一般只有一条cartId.如928对应28, 929对应29）
        // 用于product.PXX追加平台信息(group表里面用到的用于展示的cartId不是从这里取得的)
        final List<Integer> cartIds = new ArrayList<>();
//        final List<Integer> cartIds;
//        OrderChannelBean usJoiBean = Channels.getChannel(usjoiChannelId);
//        if (usJoiBean != null && !StringUtil.isEmpty(usJoiBean.getCart_ids())) {
//            cartIds = Arrays.asList(usJoiBean.getCart_ids().split(",")).stream().map(Integer::parseInt).collect(toList());
//        } else {
//            cartIds = new ArrayList<>();
//        }
        // 从synship.com_mt_value_channel表中取得USJOI店铺channel对应的可售卖的cartId列表（如928对应28,29,27等）
        List<TypeChannelBean> approveCartList = TypeChannels.getTypeListSkuCarts(usjoiChannelId, "A", "en"); // 取得可售卖平台数据
        if (ListUtils.notNull(approveCartList)) {
            // 取得配置表中可售卖的非空cartId列表
            approveCartList.forEach(p -> {
                if(!StringUtils.isEmpty(p.getValue()))
                    cartIds.add(NumberUtils.toInt(p.getValue()));
            });
        }
        if (ListUtils.isNull(approveCartList) || ListUtils.isNull(cartIds)) {
            String errMsg = usjoiChannelId + " " + String.format("%1$-15s", channelBean.getFull_name()) + " com_mt_value_channel表中没有usJoiChannel(" +
                    usjoiChannelId + ")对应的可售卖平台(53 A en)mapping信息,不能生成Product.PXX分平台信息，终止UploadToUSJoiServie处理，请修改好共通数据后再导入";
            $info(errMsg);
            // channel级的共通配置异常，本USJOI channel后面的产品都不导入了
            resultMap.put(usjoiChannelId, errMsg);
            return;
        }

        // 从cms_mt_channel_config配置表中取得渠道级别配置项的值集合,2016/10/24以后在这个表里面新加的配置项都可以在这个里面取得
        Map<String, String> channelConfigValueMap = new ConcurrentHashMap<>();
        // 取得cms_mt_channel_config表中配置的渠道级别的配置项目值(如：颜色别名等)
        doChannelConfigInit(usjoiChannelId, channelConfigValueMap);

        // 该usjoichannel每次子店->USJOI主店导入最大件数(最大2000件,默认为500件)
        int uploadToUsjoiMax = UPLOAD_TO_USJOI_MAX_500;
        // 该店铺每次子店->USJOI主店导入最大件数(FEED_IMPORT_MAX)
        CmsChannelConfigBean uploadToUsjoiMaxChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(usjoiChannelId,
                CmsConstants.ChannelConfig.FEED_IMPORT_MAX);
        if (uploadToUsjoiMaxChannelConfigBean != null && !StringUtils.isEmpty(uploadToUsjoiMaxChannelConfigBean.getConfigValue1())) {
            if (NumberUtils.toInt(uploadToUsjoiMaxChannelConfigBean.getConfigValue1()) >= 2000) {
                uploadToUsjoiMax = 2000;
            } else if (NumberUtils.toInt(uploadToUsjoiMaxChannelConfigBean.getConfigValue1()) > 0) {
                uploadToUsjoiMax = NumberUtils.toInt(uploadToUsjoiMaxChannelConfigBean.getConfigValue1());
            }
        }

        // 自动同步对象平台列表(ALL:所有平台，也可具体指定需要同步的平台id,用逗号分隔(如:"28,29"))
        // 自动上新插入workload表用(在feed->master时用于自动向liking传递变更信息，在liking主店时用于自动上新到集合店平台)
        String ccAutoSyncCarts = "";
        List<String> ccAutoSyncCartList = null;
        // 自动同步对象平台列表(ALL:所有平台，也可具体指定需要同步的平台id,用逗号分隔(如:"28,29"))
        CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(usjoiChannelId,
                CmsConstants.ChannelConfig.AUTO_SYNC_CARTS);
        if (cmsChannelConfigBean != null && !StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
            String strAutoSyncCarts = cmsChannelConfigBean.getConfigValue1().trim();
            // 如果配置的值为ALL,则同步所有平台
            if ("ALL".equalsIgnoreCase(strAutoSyncCarts)) {
                ccAutoSyncCarts = "ALL";
            } else {
                // 取得自动同步指定平台列表
                ccAutoSyncCartList = Arrays.asList(strAutoSyncCarts.split(","));
            }
        }

        // 取得feed->master导入之前的品牌，产品分类，使用人群等mapping件数，用于判断是否新增之后需要清空缓存
        int oldBrandCnt = mapBrandMapping.size();
        int oldProductTypeCnt = mapProductTypeMapping.size();
        int oldSizeTypeCnt = mapSizeTypeMapping.size();
        int newBrandCnt, newProductTypeCnt, newSizeTypeCnt;

        int totalCnt = 0;
        int currentIndex = 0;
        long startTime;
        // 每个channel读入子店数据上新到USJOI主店
        List<CmsBtSxWorkloadModel> cmsBtSxWorkloadModels = cmsBtSxWorkloadDaoExt.selectSxWorkloadModelWithCartId(
                uploadToUsjoiMax, Integer.parseInt(channelBean.getOrder_channel_id()));
        totalCnt = cmsBtSxWorkloadModels.size();
        for (CmsBtSxWorkloadModel sxWorkloadModel : cmsBtSxWorkloadModels) {
            startTime = System.currentTimeMillis();
            currentIndex++;
            try {
                // 循环上传单个产品到USJOI主店
                upload(sxWorkloadModel, mapBrandMapping, mapProductTypeMapping, mapSizeTypeMapping, usjoiTypeChannelBeanList,
                        cartIds, ccAutoSyncCarts, ccAutoSyncCartList, currentIndex, totalCnt, startTime, channelConfigValueMap);
                successCnt++;
            } catch (CommonConfigNotFoundException ce) {
                errCnt++;
                // channel级别的共通配置异常的时候，直接跳出循环,后面的子店->USJOI主店导入不做了，等这个channel共通错误改好了之后再做导入
                break;
            } catch (Exception e) {
                errCnt++;
                // 继续循环做下一条子店->USJOI导入
            }
        }

        String resultInfo = usjoiChannelId + " " + String.format("%1$-15s", channelBean.getFull_name()) +
                " USJOI主店从子店（可能为多个子店）中导入产品结果 [总件数:" + cmsBtSxWorkloadModels.size()
                + " 成功:" + successCnt + " 失败:" + errCnt + "]";
        // 将该channel的子店->主店导入信息加入map，供channel导入线程全部完成一起显示
        resultMap.put(usjoiChannelId, resultInfo);

        // 取得feed->master导入之后的品牌，产品分类，使用人群等mapping件数
        newBrandCnt = mapBrandMapping.size();
        newProductTypeCnt = mapProductTypeMapping.size();
        newSizeTypeCnt = mapSizeTypeMapping.size();

        // 如果品牌，产品分类或者使用人群等有新增过，则重新导入完成之后重新刷新一次
        if (oldBrandCnt != newBrandCnt
                || oldProductTypeCnt != newProductTypeCnt
                || oldSizeTypeCnt != newSizeTypeCnt) {
            needReloadMap.put(CacheKeyEnums.KeyEnum.ConfigData_TypeChannel.toString(), "1");
        }
    }

    public void upload(CmsBtSxWorkloadModel sxWorkLoadBean,
                       Map<String, String> mapBrandMapping,
                       Map<String, String> mapProductTypeMapping,
                       Map<String, String> mapSizeTypeMapping,
                       List<TypeChannelBean> usjoiTypeChannelBeanList,
                       List<Integer> cartIds,
                       String ccAutoSyncCarts,
                       List<String> ccAutoSyncCartList,
                       int currentIndex,
                       int totalCnt,
                       long startTime,
                       Map<String, String> channelConfigValueMap) {
        // workload表中的cartId是usjoi的channelId(928,929),同时也是子店product.platform.PXXX的cartId(928,929)
        String usJoiChannelId = sxWorkLoadBean.getCartId().toString();

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
            productModels = getUsjoiProductModel(productModels, sxWorkLoadBean.getCartId());
            if (productModels.size() == 0) {
                throw new BusinessException("没有找到需要上新的SKU");
            } else {
                $info("有" + productModels.size() + "个产品要复制");
            }

            // 新增或更新产品列表，用于最后插入品牌，产品类型和使用人群用
            List<CmsBtProductModel> targetProductList = new ArrayList<>();
//            // 取得USJOI店铺共通设置(是否自动同步人民币专柜价格)
//            boolean usjoiIsAutoSyncPriceMsrp = isAutoSyncPriceMsrp(usJoiChannelId);

            for (CmsBtProductModel productModel : productModels) {
                productModel = JacksonUtil.json2Bean(JacksonUtil.bean2Json(productModel), CmsBtProductModel.class);
                productModel.set_id(null);

                // productModel是子店的产品model,pr是主店查出来的产品model
                CmsBtProductModel pr = productService.getProductByCode(usJoiChannelId, productModel.getCommon().getFields().getCode());
                if (pr == null) {
                    // 产品不存在，新增
                    productModel.setChannelId(usJoiChannelId);
                    productModel.setOrgChannelId(sxWorkLoadBean.getChannelId());
                    productModel.setSales(new CmsBtProductModel_Sales());
                    productModel.setTags(new ArrayList<>());
                    // 插入或者更新cms_bt_product_group_c928中的productGroup信息
                    doSetGroup(productModel, usJoiChannelId, usjoiTypeChannelBeanList);

                    productModel.setProdId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_PROD_ID));

                    // platform对应 从子店的platform.p928 929 中的数据生成usjoi的platform
                    CmsBtProductModel_Platform_Cart fromPlatform = productModel.getPlatform(sxWorkLoadBean.getCartId());
                    productModel.platformsClear();
                    // 下面几个cartId都设成同一个platform
                    if (fromPlatform != null) {
                        final CmsBtProductModel finalProductModel = productModel;
                        for (Integer cartId : cartIds) {
                            CmsBtProductModel_Platform_Cart platform = new CmsBtProductModel_Platform_Cart();
                            platform.putAll(fromPlatform);
                            platform.setStatus(CmsConstants.ProductStatus.Pending.toString());
                            platform.setpCatId(null);
                            platform.setpCatPath(null);
                            platform.setpBrandId(null);
                            platform.setpBrandName(null);
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

                    // 更新价格相关项目(根据主店配置的税号，公式等计算主店产品的SKU人民币价格)
                    productModel = doSetPrice(productModel);

                    productService.createProduct(usJoiChannelId, productModel, sxWorkLoadBean.getModifier());

                    // 插入主店上新workload表
                    insertWorkload(productModel, ccAutoSyncCarts, ccAutoSyncCartList);

                    // 将子店的产品加入更新对象产品列表中
                    targetProductList.add(productModel);
                } else {
                    // 如果已经存在（如老的数据已经有了），更新
                    // 插入或者更新cms_bt_product_group_c928中的productGroup信息
                    doSetGroup(pr, usJoiChannelId, usjoiTypeChannelBeanList);
                    // 更新common的一部分属性
                    // productModel是子店的产品model,pr是主店查出来的产品model
                    CmsBtProductModel_Field prCommonFields = pr.getCommon().getFields();
                    if (prCommonFields != null && productModel.getCommon().getFields() != null) {
                        // 更新common.fields.images1(品牌方商品图)
                        prCommonFields.setImages1(productModel.getCommon().getFields().getImages1());

                        // add by desmond 2016/10/27 start
                        // 从子店产品中复制图片到总店产品的common.fields.images2~images9的图片
                        // -------------------------------------------
                        // 子店到主店的图片(images2~images9)复制方式:
                        //    空或0:不复制品牌方商品图以外的图片
                        //    1:以UNION方式复制图片
                        //    2:以总店的数据为准。只要总店有数据，那么总店为准。如果总店没有，子店有，那么子店的数据复制到总店
                        // -------------------------------------------
                        // 取得cms_mt_channel_config配置表中配置的子店->USJOI总店的品牌方商品图(images1)以外的图片复制方式(LIKING_IMAGE_COPY_FLG)
                        // LIKING_IMAGE_COPY_FLG  LIKING子店到总店更新时图片复制方式(不包含品牌方商品图images1)(空或0：不复制，1:以UNION方式复制，2：以主店优先方式复制)
                        String likingImageCopyFlg = "";   // 没有配置时，默认为空(""或者"0":不复制品牌方商品图以外的图)
                        // 子店到主店的品牌方商品图(images1)以外图片拷贝方式取得key
                        String likingImageCopyFlgKey = CmsConstants.ChannelConfig.LIKING_IMAGE_COPY_FLG + "_0";
                        if (channelConfigValueMap != null && channelConfigValueMap.containsKey(likingImageCopyFlgKey)) {
                            likingImageCopyFlg = channelConfigValueMap.get(likingImageCopyFlgKey);
                        }
                        // 从子店产品中复制图片到总店产品
                        // 子店到总店更新时图片复制方式(空或0：不复制，1:以UNION方式复制，2：以主店优先方式复制)
                        // 包装图(images2) 角度图(images3) PC端自定义图(images4) APP端自定义图(images5) PC端自拍商品图(images6)
                        // App端自拍商品图(images7) App端自拍商品图(images7) 吊牌图(images8) (images9)
                        for (int imagesNo = 2; imagesNo <= 9; imagesNo++) {
                            // 从子店产品复制图片到主店产品中(images2~images9)
                            copyImageToUsjoi(likingImageCopyFlg, imagesNo, productModel, pr);
                        }
                        // add by desmond 2016/10/27 end

                        // 主店的税号没设置，子店的税号设置了的话，将主店商品更新成子店的税号code
                        // 税号集货
                        if (StringUtil.isEmpty(prCommonFields.getHsCodeCrop())
                                && !StringUtil.isEmpty(productModel.getCommon().getFields().getHsCodeCrop())) {
                            prCommonFields.setHsCodeCrop(productModel.getCommon().getFields().getHsCodeCrop());
                        }

                        // 税号跨境申报（10位）
                        if (StringUtil.isEmpty(prCommonFields.getHsCodeCross())
                                && !StringUtil.isEmpty(productModel.getCommon().getFields().getHsCodeCross())) {
                            prCommonFields.setHsCodeCross(productModel.getCommon().getFields().getHsCodeCross());
                        }

                        // 税号个人
                        if (StringUtil.isEmpty(prCommonFields.getHsCodePrivate())
                                && !StringUtil.isEmpty(productModel.getCommon().getFields().getHsCodePrivate())) {
                            prCommonFields.setHsCodePrivate(productModel.getCommon().getFields().getHsCodePrivate());
                        }

                        // 更新税号设置状态
                        String oldHsCodeStatus = prCommonFields.getHsCodeStatus();
                        prCommonFields.setHsCodeStatus(StringUtil.isEmpty(prCommonFields.getHsCodePrivate()) ? "0" : "1");
                        if(!prCommonFields.getHsCodeStatus().equalsIgnoreCase(oldHsCodeStatus)){
                            // 如果状态有变更且变成1时，记录更新时间
                            if(prCommonFields.getHsCodeStatus().equalsIgnoreCase("1")){
                                prCommonFields.setHsCodeSetTime(DateTimeUtil.getNowTimeStamp());
                                prCommonFields.setHsCodeSetter(getTaskName());
                            }
                        }

                        // 克(common.fields.weightG)
                        if (productModel.getCommon().getFields().getWeightG() != 0)
                            prCommonFields.setWeightG(productModel.getCommon().getFields().getWeightG());
                        // 千克(common.fields.WeighKG)
                        if (productModel.getCommon().getFields().getWeightKG().compareTo(0.0d) != 0)
                            prCommonFields.setWeightKG(productModel.getCommon().getFields().getWeightKG());
                    }

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
//                            if (oldSku.getPriceMsrp().compareTo(sku.getPriceMsrp()) != 0
//                                    || oldSku.getPriceRetail().compareTo(sku.getPriceRetail()) != 0) {
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
//                            }
                        }
                    }

//                    // 由于需要无条件更新common.image1等属性，所以无条件更新common属性
//                    // 共通方法里面有Approved的时候，自动插入USJOI(928,929)->平台(京东国际匠心界，悦境)上新workload记录
//                    productService.updateProductCommon(usJoiChannelId, pr.getProdId(), pr.getCommon(), getTaskName(), false);

                    final CmsBtProductModel finalProductModel1 = productModel;
                    for (Integer cartId : cartIds) {
                        CmsBtProductModel_Platform_Cart platformCart = pr.getPlatform(cartId);
                        CmsBtProductModel_Platform_Cart fromPlatform = finalProductModel1.getPlatform(sxWorkLoadBean.getCartId());
                        if (platformCart == null) {
                            CmsBtProductModel_Platform_Cart newPlatform = new CmsBtProductModel_Platform_Cart();
                            newPlatform.putAll(fromPlatform);
                            // 如果主店商品里面没有这个cartId的platform,则新加
                            newPlatform.setStatus(CmsConstants.ProductStatus.Pending.toString());
                            newPlatform.setpCatId(null);
                            newPlatform.setpCatPath(null);
                            newPlatform.setpBrandId(null);
                            newPlatform.setpBrandName(null);
                            newPlatform.setCartId(cartId);

                            // 设定是否主商品
                            CmsBtProductGroupModel group = productGroupService.selectMainProductGroupByCode(usJoiChannelId,
                                    finalProductModel1.getCommon().getFields().getCode(), cartId);
                            if (group == null) {
                                newPlatform.setpIsMain(0);
                            } else {
                                newPlatform.setpIsMain(1);
                            }

                            pr.getPlatforms().put("P" + StringUtils.toString(cartId), newPlatform);
//                            productService.updateProductPlatform(usJoiChannelId, pr.getProdId(), newPlatform, getTaskName());
                        } else {
                            // 设定是否主商品
                            CmsBtProductGroupModel group = productGroupService.selectMainProductGroupByCode(usJoiChannelId,
                                    finalProductModel1.getCommon().getFields().getCode(), cartId);
                            if (group == null) {
                                platformCart.setpIsMain(0);
                            } else {
                                platformCart.setpIsMain(1);
                            }

                            if (platformCart.getSkus() == null) {
                                platformCart.setSkus(fromPlatform.getSkus());
                            } else {
                                for (BaseMongoMap<String, Object> newSku : fromPlatform.getSkus()) {
                                    boolean updateFlg = false;
                                    if (platformCart.getSkus() != null) {
                                        for (BaseMongoMap<String, Object> oldSku : platformCart.getSkus()) {
                                            if (oldSku.get("skuCode").toString().equalsIgnoreCase(newSku.get("skuCode").toString())) {
                                                // delete by desmond 2016/09/08 start DOC-128 主店的SKU价格变为调用共通价格计算重新计算
//                                                // 在更新前的PXX.skus找到对应的新skuCode的时候,更新价格等平台sku属性(不更新priceSale)
//                                                oldSku.put("originalPriceMsrp", newSku.get("originalPriceMsrp"));
//                                                if (usjoiIsAutoSyncPriceMsrp) {
//                                                    // 如果USJOI店铺(928,929)配置了自动同步人民币专柜价格时，才同步priceMsrp
//                                                    oldSku.put("priceMsrp", newSku.get("priceMsrp"));
//                                                }
//
//                                                // 获取上一次指导价
//                                                Double lastRetailPrice = oldSku.getDoubleAttribute("priceRetail");
//                                                // 保存最新中国指导价格
//                                                oldSku.put("priceRetail", newSku.get("priceRetail"));
//
//                                                // 获取最新指导价
//                                                Double retailPrice = oldSku.getDoubleAttribute("priceRetail");
//                                                // 获取价格波动字符串
//                                                String priceFluctuation = priceService.getPriceFluctuation(retailPrice, lastRetailPrice);
//                                                // 保存价格波动(U50% D30%)
//                                                oldSku.put(priceChgFlg.name(), priceFluctuation);
//
//                                                // 保存击穿标识
//                                                String priceDiffFlgValue = productSkuService.getPriceDiffFlg(usJoiChannelId, oldSku);
//                                                // 最终售价变化状态（价格为-1:空，等于指导价:1，比指导价低:2，比指导价高:3，向上击穿警告:4，向下击穿警告:5）
//                                                oldSku.put(priceDiffFlg.name(), priceDiffFlgValue);
                                                // delete by desmond 2016/09/08 end

                                                updateFlg = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (!updateFlg) {
                                        platformCart.getSkus().add(newSku);
                                    }
//                                    platformCart.setpPriceRetailSt(newPlatform.getpPriceRetailSt());
//                                    platformCart.setpPriceRetailEd(newPlatform.getpPriceRetailEd());
//                                    platformCart.setpPriceSaleSt(newPlatform.getpPriceSaleSt());
//                                    platformCart.setpPriceSaleEd(newPlatform.getpPriceSaleEd());
                                }
                            }
//                            productService.updateProductPlatform(usJoiChannelId, pr.getProdId(), platformCart, getTaskName());
                        }
                    }

//                    if (pr.getCommon() == null || pr.getCommon().size() == 0) {
//                        // 共通方法里面有Approved的时候，自动插入USJOI(928,929)->平台(京东国际匠心界，悦境)上新workload记录
//                        productService.updateProductCommon(usJoiChannelId, pr.getProdId(), productModel.getCommon(), getTaskName(), false);
//                    }
                    if (pr.getPlatform(0) == null) {
                        CmsBtProductGroupModel groupModel = productGroupService.selectMainProductGroupByCode(usJoiChannelId, productModel.getCommon().getFields().getCode(), 0);
                        // 新规作成P0平台信息
                        CmsBtProductModel_Platform_Cart p0 = getPlatformP0(groupModel, productModel);
                        pr.getPlatforms().put("P0", p0);

//                        HashMap<String, Object> queryMap = new HashMap<>();
//                        queryMap.put("prodId", pr.getProdId());

//                        List<BulkUpdateModel> bulkList = new ArrayList<>();
//                        HashMap<String, Object> updateMap = new HashMap<>();
//                        updateMap.put("platforms.P0", p0);
//                        BulkUpdateModel model = new BulkUpdateModel();
//                        model.setUpdateMap(updateMap);
//                        model.setQueryMap(queryMap);
//                        bulkList.add(model);
//                        cmsBtProductDao.bulkUpdateWithMap(usJoiChannelId, bulkList, null, "$set");
                    }

                    // 更新价格相关项目(根据主店配置的税号，公式等计算主店产品的SKU人民币价格)
                    pr = doSetPrice(pr);

                    // 更新产品并记录商品价格表动履历，并向Mq发送消息同步sku,code,group价格范围
                    // productService.updateProduct(channelId, requestModel);
                    int updCnt = productService.updateProductFeedToMaster(usJoiChannelId, pr, getTaskName(), "子店->USJOI主店导入");
                    if (updCnt == 0) {
                        // 有出错, 跳过
                        String errMsg = String.format("子店->USJOI主店产品导入:更新:编辑商品的时候排他错误:[orgChannelId=%s]" +
                                " [usjoiChannelId=%s] [code=%s]", pr.getOrgChannelId(), usJoiChannelId, pr.getCommon().getFields().getCode());
                        $error(errMsg);
                        throw new BusinessException(errMsg);
                    }

                    // 插入主店上新workload表
                    insertWorkload(productModel, ccAutoSyncCarts, ccAutoSyncCartList);

                    // 将USJOI店的产品加入更新对象产品列表中（取得USJOI店的品牌，产品分类和适用人群）
                    targetProductList.add(pr);
                }
            }

            // delete by desmond 2016/09/06 start
            // 因为productService.updateProductPlatform()里面已经调用了记录价格变动履历的方法，所以这里不要再重新记录价格变动履历了
            // 记录商品价格变动履历,并向Mq发送消息同步sku,code,group价格范围
//          addPriceHistoryAndSyncPriceScope(usJoiChannelId, targetProductList);
            // delete by desmond 2016/09/06 end

            // 新增更新USJOI主店产品结束之后，重新计算一下每个产品(包含code,originalCode)的skuCnt
            List<String> productCodes = productModels.stream().map(p -> p.getCommonNotNull().getFieldsNotNull().getCode()).collect(Collectors.toList());
            if (ListUtils.notNull(productCodes)) {
                doSetSkuCnt(usJoiChannelId, productCodes);
            }

            // 如果Synship.com_mt_value_channel表中没有usjoi channel(928,929)对应的品牌，产品类型或适用人群信息，则插入该信息
            insertMtValueChannelInfo(usJoiChannelId, mapBrandMapping, mapProductTypeMapping, mapSizeTypeMapping, targetProductList);

            // 子店->USJOI主店产品导入的虚拟上新成功之后回写workload表中的状态(1:USJOI上新成功)
            sxWorkLoadBean.setPublishStatus(1);
            cmsBtSxWorkloadDaoExt.updateSxWorkloadModel(sxWorkLoadBean);

            // 子店->USJOI主店产品导入的虚拟上新成功之后,取得子店的productGroup信息，设置状态，调用共通的上新成功回写方法，回写子店状态
            // 子店的group表里面getPlatformActive是由feed->master导入根据配置设置的，上新成功之后，根据这个值回写状态（因为是虚拟上新，所以回写的这个状态没啥太大意义）
            CmsBtProductGroupModel cmsBtProductGroupModel = productGroupService.getProductGroupByGroupId(sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getGroupId());
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
            $info(String.format("channelId:%s [%d/%d] groupId:%d 复制到%s USJOI成功结束 [耗时:%s]", sxWorkLoadBean.getChannelId(),
                    currentIndex, totalCnt, sxWorkLoadBean.getGroupId(), usJoiChannelId, (System.currentTimeMillis() - startTime)));
        } catch (CommonConfigNotFoundException ce) {
            String errMsg = "子店->USJOI主店产品导入:异常终止:";
            if (StringUtils.isNullOrBlank2(ce.getMessage())) {
                errMsg += "出现不可预知的错误，请跟管理员联系 [ErrMsg=" + ce.getStackTrace()[0].toString() + "]";
            } else {
                errMsg += ce.getMessage();
            }
            // 回写详细错误信息表(cms_bt_business_log)
            insertBusinessLog(sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getCartId(),
                    StringUtils.toString(sxWorkLoadBean.getGroupId()), "", "", errMsg, getTaskName());

            $info(String.format("channelId:%s [%d/%d] groupId:%d  复制到%s USJOI 共通配置异常", sxWorkLoadBean.getChannelId(),
                    currentIndex, totalCnt, sxWorkLoadBean.getGroupId(), usJoiChannelId));

            // 抛出让外面的循环做处理
            throw ce;
        } catch (Exception e) {
            String errMsg = "子店->USJOI主店产品导入:异常终止:";
            if (StringUtils.isNullOrBlank2(e.getMessage())) {
                errMsg += "出现不可预知的错误，请跟管理员联系 [ErrMsg=" + e.getStackTrace()[0].toString() + "]";
                $error(errMsg);
            } else {
                errMsg += e.getMessage();
            }
            // 将子店->主店的上新workload的状态更新为2(导入上新失败)
            sxWorkLoadBean.setPublishStatus(2);
            cmsBtSxWorkloadDaoExt.updateSxWorkloadModel(sxWorkLoadBean);
            $info(String.format("channelId:%s [%d/%d] groupId:%d  复制到%s USJOI 异常", sxWorkLoadBean.getChannelId(),
                    currentIndex, totalCnt, sxWorkLoadBean.getGroupId(), usJoiChannelId));
            e.printStackTrace();

            // 上新失败时回写错误状态到子店的productGroup和product
            CmsBtProductGroupModel groupModel = productGroupService.getProductGroupByGroupId(sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getGroupId());
            productGroupService.updateUploadErrorStatus(groupModel, errMsg);
            // 出错的时候将错误信息回写到cms_bt_business_log表
            insertBusinessLog(sxWorkLoadBean.getChannelId(), sxWorkLoadBean.getCartId(),
                    StringUtils.toString(sxWorkLoadBean.getGroupId()), "", "", errMsg, getTaskName());
//            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
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
     * @param orgChannelId 子店channelId
     * @return group id
     */
    private CmsBtProductGroupModel getGroupIdByFeedModel(String channelId, String modelCode, String cartId, String orgChannelId) {
        // 先去看看是否有存在的了
        CmsBtProductGroupModel groupObj = productGroupService.selectProductGroupByModelCodeAndCartId(channelId, modelCode, cartId, orgChannelId);
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
    private boolean doSetGroup(CmsBtProductModel cmsBtProductModel, String usJoiChannel, List<TypeChannelBean> usjoiTypeChannelBeanList) {
//            // 价格区间设置 ( -> 调用顾步春的api自动会去设置,这里不需要设置了)

        boolean result = true;

        // 根据procutCode, 到group表中去查找所有的group信息，取得当前产品已经加入的所有group列表
        List<CmsBtProductGroupModel> existGroups = productGroupService.selectProductGroupListByCode(usJoiChannel,
                cmsBtProductModel.getCommon().getFields().getCode());

        // 循环一下
        for (TypeChannelBean displayPlatform : usjoiTypeChannelBeanList) {
            // 当前显示用cartId
            String currentCartId = displayPlatform.getValue();
            // 检查一下该产品code是否已经在这个platform存在, 如果已经存在, 那么就不需要增加code到group了
            boolean blnFound = false;
            for (CmsBtProductGroupModel group : existGroups) {
                // 如果当前平台(如：27聚美)已经追加过group的时候，blnFound=true，这个code在该平台(如:27聚美)就不用新增/更新group信息了
                if (group.getCartId() == Integer.parseInt(currentCartId)) {
                    blnFound = true;
                    // NumId有值的时候，返回false
                    if (!StringUtils.isEmpty(group.getNumIId())) {
                        result = false;
                    }
                    break;
                }
            }
            if (blnFound) {
                // 该产品code已经加到该平台(如28)对应的group中了，直接跳过该平台group的新增/更新
                continue;
            }

            // 当前code对应的该cartId平台的group不存在时，新增新的group或者追加code到group的ProductCodes中
            // 聚美和独立官网的时候，是一个Code对应一个Group，所以直接新增group, 其他的平台都是几个Code对应一个Group
            // group对象
            CmsBtProductGroupModel group = null;
            // 如果是聚美或者独立官网的时候，是一个Code对应一个Group,其他的平台都是几个Code对应一个Group
            // 目前的USJOI有京东国际平台, 也有聚美平台(27)
            if (!CartEnums.Cart.JM.getId().equals(currentCartId)
                    && !CartEnums.Cart.CN.getId().equals(currentCartId)
                    && !CartEnums.Cart.LIKING.getId().equals(currentCartId)) {
                // 聚美和官网以外的平台，先取得product.model对应的group信息(根据model取得Product(没找到直接返回null),再根据productCode查找group信息)
                // 由于可能存在2个子店的Product.model相同的情况，如果不加orgChannelId只用model去查product的话，会导致查出来别的店铺的product对应的group
                // 例如:A店铺的Product1(model="model0001")已经生成了group信息，然后B店铺的Product2(model="model0001")就会查出来Product1对应的group,会把B店铺的Product2加到A店铺的Product1对应的group中去
                group = getGroupIdByFeedModel(cmsBtProductModel.getChannelId(), cmsBtProductModel.getCommon().getFields().getModel(),
                        currentCartId, cmsBtProductModel.getOrgChannelId());
            }

            // group id
            // 看看同一个model里是否已经有数据在cms里存在的
            //   如果已经有存在的话: 直接用哪个group id
            //   如果没有的话: 取一个最大的 + 1
            if (group == null) {
                // 创建一个platform
                group = new CmsBtProductGroupModel();
                // cart id
                group.setCartId(Integer.parseInt(currentCartId));
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
                if (CmsConstants.PlatformActive.ToOnSale.name().equalsIgnoreCase(getPlatformActive(usJoiChannel, NumberUtils.toInt(currentCartId)).name())) {
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

//    /**
//     * 查看该渠道是否自动同步人民币专柜价格MSRP
//     *
//     * @param channelId String channel id
//     */
//    private boolean isAutoSyncPriceMsrp(String channelId) {
//        boolean isAutoSyncPriceMsrp = false;
//
//        CmsChannelConfigBean autoSyncPriceMsrp = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_MSRP);
//
//        if (autoSyncPriceMsrp != null && "1".equals(autoSyncPriceMsrp.getConfigValue1()))
//            isAutoSyncPriceMsrp = true;
//
//        return isAutoSyncPriceMsrp;
//    }

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

//    /**
//     * 记录商品价格变动履历,并向Mq发送消息同步sku,code,group价格范围
//     *
//     * @param usjoiChannelId String usjoi渠道id
//     * @param usjoiProductModels List<CmsBtProductModel> usjoi产品列表
//     */
//    private void addPriceHistoryAndSyncPriceScope(String usjoiChannelId, List<CmsBtProductModel> usjoiProductModels) {
//
//        for (CmsBtProductModel usjoiProduct : usjoiProductModels) {
//            // 记录商品价格表动履历，并向Mq发送消息同步sku,code,group价格范围
//            if (usjoiProduct != null && ListUtils.notNull(usjoiProduct.getCommon().getSkus())) {
//                List<String> skuCodeList = usjoiProduct.getCommon().getSkus()
//                        .stream()
//                        .map(CmsBtProductModel_Sku::getSkuCode)
//                        .collect(Collectors.toList());
//                // 记录商品价格变动履历,并向Mq发送消息同步sku,code,group价格范围
//                cmsBtPriceLogService.addLogForSkuListAndCallSyncPriceJob(skuCodeList, usjoiChannelId, null, getTaskName(), "子店->USJOI主店导入");
//            }
//        }
//
//    }

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

    /**
     * doSetPrice 调用共通函数设置product各平台的sku的价格
     * 把商品推到USJOI主店的都不是旗舰店(用公式计算价格且货款直接打给客户)，所以都是利用新价格体系来计算价格的
     *
     * @param usjoiCmsProduct usjoi cms product信息
     * @return CmsBtProductModel 以后计算价格直接用ProductModel
     */
    private CmsBtProductModel doSetPrice(CmsBtProductModel usjoiCmsProduct) {

        // 设置platform.PXX.skus里面的价格
        try {
            priceService.setPrice(usjoiCmsProduct, false);
        } catch (IllegalPriceConfigException ie) {
            // 渠道级别价格计算配置错误, 停止后面的子店->USJOI主店产品导入，避免报几百条一样的错误信息
            String errMsg = String.format("子店->USJOI主店产品导入:共通配置异常终止:发现渠道级别的价格计算配置错误，后面的feed导入不做了，" +
                    "请修改好相应配置项目后重新导入 [ErrMsg=%s]", ie.getMessage());
            $error(errMsg);
            throw new CommonConfigNotFoundException(errMsg);
        } catch (Exception ex) {
            String errMsg = "子店->USJOI主店产品导入:异常终止:调用共通函数计算产品价格时出错 [ErrMsg= ";
            if (StringUtils.isNullOrBlank2(ex.getMessage())) {
                errMsg += ex.getStackTrace()[0].toString() + "]";
            } else {
                errMsg += ex.getMessage() + "]";
            }
            $error(errMsg);
            throw new BusinessException(errMsg);
        }

        return usjoiCmsProduct;
    }

    private void insertWorkload(CmsBtProductModel cmsProduct, String ccAutoSyncCarts, List<String> ccAutoSyncCartList) {
        // 变更自动同步到全部平台("ALL")或者自动同步到指定平台(用逗号分隔 如:"28,29"),没有配置时不插入workload表
        // 当该产品未被锁定且已批准的时候，往workload表里面插入一条上新数据，并逻辑清空相应的business_log
        if ("ALL".equalsIgnoreCase(ccAutoSyncCarts)) {
            // 变更自动同步到全部平台("ALL")时
            sxProductService.insertSxWorkLoad(cmsProduct, getTaskName());
        } else if (ListUtils.notNull(ccAutoSyncCartList)) {
            // 变更自动同步到指定平台(用逗号分隔 如:"28,29")时
            sxProductService.insertSxWorkLoad(cmsProduct, ccAutoSyncCartList, getTaskName());
        }
    }

    /**
     * 取得cms_mt_channel_config配置表中配置的值集合
     *
     * @param channelId String LIKING渠道id
     * @param channelConfigValueMap 返回cms_mt_channel_config配置表中配置的值集合用
     */
    public void doChannelConfigInit(String channelId,  Map<String, String> channelConfigValueMap) {

        // 从配置表(cms_mt_channel_config)表中取得子店->USJOI总店的品牌方商品图(images1)以外的图片复制方式(LIKING__IMAGE_COPY_FLG_0)
        String likingImageCopyFlgKey = CmsConstants.ChannelConfig.LIKING_IMAGE_COPY_FLG + "_0";
        String likingImageCopyFlgValue1 = getChannelConfigValue(channelId, CmsConstants.ChannelConfig.LIKING_IMAGE_COPY_FLG, null);
        channelConfigValueMap.put(likingImageCopyFlgKey, likingImageCopyFlgValue1);
    }

    /**
     * 取得cms_mt_channel_config配置表中配置的值
     *
     * @param channelId String 渠道id
     * @param configKey CmsConstants.ChannelConfig ConfigKey
     * @param configCode String ConfigCode
     * @return String cms_mt_channel_config配置表中配置的值
     */
    public String getChannelConfigValue(String channelId, String configKey, String configCode) {
        if (StringUtils.isEmpty(channelId) || StringUtils.isEmpty(configKey)) return "";

        // 配置表(cms_mt_channel_config)表中ConfigCode的默认值为0
        String strConfigCode = "0";
        if (!StringUtils.isEmpty(configCode)) {
            strConfigCode = configCode;
        }

        String strConfigValue = "";
        // 通过配置表(cms_mt_channel_config)取得Configykey和ConfigCode对应的配置值(config_value1)
        CmsChannelConfigBean channelConfig = CmsChannelConfigs.getConfigBean(channelId, configKey, strConfigCode);
        if (channelConfig != null) {
            strConfigValue = channelConfig.getConfigValue1();
        }

        return strConfigValue;
    }

    /**
     * 从子店产品中复制图片到总店产品
     *
     * @param likingImageCopyFlg String 子店到总店时图片复制方式(空或0：不复制，1:以UNION方式复制，2：以主店优先方式复制)
     * @param imagesNo String 图片No
     * @param productModel CmsBtProductModel子店产品
     * @param pr CmsBtProductModel 主店产品
     */
    public void copyImageToUsjoi(String likingImageCopyFlg, int imagesNo, CmsBtProductModel productModel, CmsBtProductModel pr) {
        if (!"1".equals(likingImageCopyFlg) && !"2".equals(likingImageCopyFlg)) return;
        if (productModel == null || pr == null) return;

        String imageKey = "image" + imagesNo;
        CmsBtProductConstants.FieldImageType imageTypeNo = getImageTypeByNo(imagesNo);
        if (imageTypeNo == null) return;

        // 子店产品的images(如果不存在会new一个空的List，不会出现null的情况)
        List<CmsBtProductModel_Field_Image> productImagesNo = productModel.getCommon().getFields().getImages(imageTypeNo);
        // 主店产品images(如果不存在会new一个空的List，不会出现null的情况)
        List<CmsBtProductModel_Field_Image> prImagesNo = pr.getCommon().getFields().getImages(imageTypeNo);

        // 子店到总店时图片复制方式为"1:以UNION方式复制"时
        if ("1".equals(likingImageCopyFlg)) {
            // 循环子店产品的是images列表，看看在主店商品里有没有这张图片，如果没有就追加到主店images列表的末尾
            productImagesNo.forEach(s -> {
                // 取得子店图片名
                String productImageName = s.getStringAttribute(imageKey);  // s.getName()也可以取得
                // 如果子店产品的image的图片名不为空，并且，在主店产品里面没有找到对应的图片名时，将这张子店产品图片追加到住店产品图片的最后
                if (!StringUtils.isEmpty(productImageName)
                        && (prImagesNo.stream().filter(p -> productImageName.equals(p.getStringAttribute(imageKey))).count() == 0)) {
                    // 将在主店中不存在的这张子店图片加入主店产品中图片列表末尾
                    prImagesNo.add(s);
                }
            });
        } else if ("2".equals(likingImageCopyFlg)) {
            // 子店到总店时图片复制方式为"2:以主店优先方式复制"时(以总店的数据为准。只要总店有数据，那么总店为准。如果总店没有，子店有，那么子店的数据复制到总店)时
            // 子店产品有图片且总店产品一张图片都没有时，将子店产品图片复制到总店产品中
            if ((ListUtils.notNull(productImagesNo) && !StringUtils.isEmpty(productImagesNo.get(0).getStringAttribute(imageKey)))
                    && (ListUtils.isNull(prImagesNo) || StringUtils.isEmpty(prImagesNo.get(0).getStringAttribute(imageKey)))) {
                pr.getCommon().getFields().setImages(imageTypeNo, productImagesNo);
            }
        }

        // 如果更新前主店为空(images数组size=1，但里面其实没有值)，要删除空的image数据元素;万一有2个以上的元素为空，全部删除
        for (int i = 0; i < prImagesNo.size(); i++) {
            // 删除空的images数组元素(数组只有一个元素，空的就不删除了;万一有2个以上的元素为空，全部删除)
            if (prImagesNo.size() > 1 && StringUtils.isEmpty(prImagesNo.get(i).getStringAttribute(imageKey))) {
                prImagesNo.remove(i);
                // 正向删除集合中元素的时候要减1(因为删除元素后面的元素index会减1)，如果是反向删除集合不需要减1
                i--;
            }
        }
    }

    public CmsBtProductConstants.FieldImageType getImageTypeByNo(int imagesNo) {
        CmsBtProductConstants.FieldImageType imageType = null;

        switch (imagesNo) {
            case 1:
                imageType = CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE;
                break;
            case 2:
                imageType = CmsBtProductConstants.FieldImageType.PACKAGE_IMAGE;
                break;
            case 3:
                imageType = CmsBtProductConstants.FieldImageType.ANGLE_IMAGE;
                break;
            case 4:
                imageType = CmsBtProductConstants.FieldImageType.CUSTOM_IMAGE;
                break;
            case 5:
                imageType = CmsBtProductConstants.FieldImageType.MOBILE_CUSTOM_IMAGE;
                break;
            case 6:
                imageType = CmsBtProductConstants.FieldImageType.CUSTOM_PRODUCT_IMAGE;
                break;
            case 7:
                imageType = CmsBtProductConstants.FieldImageType.M_CUSTOM_PRODUCT_IMAGE;
                break;
            case 8:
                imageType = CmsBtProductConstants.FieldImageType.HANG_TAG_IMAGE;
                break;
            case 9:
                imageType = CmsBtProductConstants.FieldImageType.DURABILITY_TAG_IMAGE;
                break;
            default:
                break;
        }

        return imageType;
    }

    /**
     * 重新计算product的common.skus的sku个数设置product.common.fields.skuCnt
     * 除了计算本次子店->USJOI主店导入的对象Product,还包含拆分后的Product,里面会自动去查找拆分后的Product
     *
     * @param usjoiChannelId  usjoi主店渠道id
     * @param productCodeList 子店->主店导入对象Product列表(不包含拆分后的Product,里面会自动去查找拆分后的Product)
     */
    protected void doSetSkuCnt(String usjoiChannelId, List<String> productCodeList) {
        if (ListUtils.isNull(productCodeList)) return;

        // 对象产品code列表(大于等于productModels列表值,包含code或originalCode一致的产品code)
        List<String> codeList = new ArrayList<>();
        productCodeList.forEach(code -> {
            // 重复的code不用添加
            if (!codeList.contains(code)) codeList.add(code);
        });

        // 主店的产品运营可能会把一个产品的sku拆分到另一个产品中去(根据originalCode建立关联关系)
        if (ListUtils.notNull(codeList)) {
            // 查询所有originalCode在对象productCode列表中的产品信息列表
            JongoQuery queryObj = new JongoQuery();
            queryObj.setQuery("{'common.fields.originalCode':{$in:#}}");
            queryObj.setParameters(codeList);
            queryObj.setProjectionExt("common.fields.code");
            List<CmsBtProductModel> originalCodeProductList = productService.getList(usjoiChannelId, queryObj);
            if (ListUtils.notNull(originalCodeProductList)) {
                // 取得originalCode=对象code的产品的common.fields.code并加入待重新计算skuCnt的code列表
                List<String> originalCodeList = originalCodeProductList.stream().map(p -> p.getCommon().getFields().getCode()).collect(Collectors.toList());
                if (ListUtils.notNull(originalCodeList)) {
                    originalCodeList.forEach(code -> {
                        // 重复的code不用添加
                        if (!codeList.contains(code)) codeList.add(code);
                    });
                }
            }
        }

        // 重新计算每个产品的common.skus的个数，并更新到common.fields.skuCnt中
        if (ListUtils.notNull(codeList)) {
            // 批量更新USJOI主店product表的common.skuCnt的值
            BulkJongoUpdateList bulkList = new BulkJongoUpdateList(1000, cmsBtProductDao, usjoiChannelId);

            BulkWriteResult rs;
            JongoQuery queryObj = new JongoQuery();

            for (String productCode : codeList) {
                // 取得商品信息
                queryObj.setQuery("{'common.fields.code':#}");
                queryObj.setParameters(productCode);
                queryObj.setProjectionExt("common.fields.skuCnt", "common.skus");
                CmsBtProductModel prodObj = cmsBtProductDao.selectOneWithQuery(queryObj, usjoiChannelId);
                if (prodObj == null) {
                    $warn("UploadToUSJoiService 找不到商品code [ChannelId=%s] [ProductCode=%d]", usjoiChannelId, productCode);
                } else {
                    int skuCnt = prodObj.getCommonNotNull().getFieldsNotNull().getSkuCnt();
                    int reallySkuCnt = prodObj.getCommonNotNull().getSkus().size();
                    // 目前的skuCnt和真实的reallySkuCnt不一致时，才做更新(如果skuCnt数没有变化，就不用更新了)
                    if (skuCnt != reallySkuCnt) {
                        JongoUpdate updObj = new JongoUpdate();
                        updObj.setQuery("{'common.fields.code':#}");
                        updObj.setQueryParameters(productCode);
                        updObj.setUpdate("{$set:{'common.fields.skuCnt':#,'modified':#,'modifier':#}}");
                        updObj.setUpdateParameters(reallySkuCnt, DateTimeUtil.getNowTimeStamp(), getTaskName());
                        rs = bulkList.addBulkJongo(updObj);
                        if (rs != null) {
                            $debug("UploadToUSJoiService [ChannelId=%s] [ProductCode=%s] [skuCnt更新结果=%s]", usjoiChannelId, productCode, rs.toString());
                        }
                    }
                }
            }
            rs = bulkList.execute();
            if (rs != null) {
                $debug("UploadToUSJoiService [ChannelId=%s] [skuCnt更新结果=%s]", usjoiChannelId, rs.toString());
            }
        }

    }

}
