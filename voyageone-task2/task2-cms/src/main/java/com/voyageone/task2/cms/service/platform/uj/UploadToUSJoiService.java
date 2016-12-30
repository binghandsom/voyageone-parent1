package com.voyageone.task2.cms.service.platform.uj;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.base.exception.CommonConfigNotFoundException;
import com.voyageone.category.match.*;
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
import com.voyageone.common.util.*;
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

    @Autowired
    private Searcher searcher;

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
            // 清除缓存（这样就能马上在画面上展示出最新追加的brand等初始化mapping信息）
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

        // 由于主店产品分类和适用人群不用再往Synship.com_mt_value_channel表里面插了，因为以前想要用别人的，但现在有自己的，不用保存别人的了
        // 品牌mapping表
        Map<String, String> mapBrandMapping = new HashMap<>();

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

        // 取得feed->master导入之前的品牌等mapping件数，用于判断是否新增之后需要清空缓存
        int oldBrandCnt = mapBrandMapping.size();
        int newBrandCnt;

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
                upload(sxWorkloadModel, mapBrandMapping, usjoiTypeChannelBeanList,
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

        // 如果品牌等有新增过，则重新导入完成之后重新刷新一次
        if (oldBrandCnt != newBrandCnt) {
            needReloadMap.put(CacheKeyEnums.KeyEnum.ConfigData_TypeChannel.toString(), "1");
        }
    }

    public void upload(CmsBtSxWorkloadModel sxWorkLoadBean,
                       Map<String, String> mapBrandMapping,
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
                // 子店的产品code(到主店后产品code也一样)
                String productCode = productModel.getCommonNotNull().getFieldsNotNull().getCode();
                // 子店的原始产品Code(由于子店也可能在feed->master导入或者运营手动拆分商品，但运营手动拆分信息不带到主店去，所以根据skuCode去查product，把价格更新到该产品中)
                String originalProductCode = productModel.getCommonNotNull().getFieldsNotNull().getOriginalCode();
                // productModel是子店的产品model,pr是主店查出来的产品model
                List<CmsBtProductModel> prList = new ArrayList<>();
                // 判断是否是在子店拆分过的产品，如果拆分过的产品，直接用skuCode去查询产品
                if (!StringUtils.isEmpty(originalProductCode) && !productCode.equals(originalProductCode)) {
                    if (ListUtils.notNull(productModel.getCommonNotNull().getSkus())) {
                        productModel.getCommonNotNull().getSkus().forEach(s -> {
                            // 子店产品本身就是拆分出来的产品时，根据skuCode去主店里面查询产品
                            List<CmsBtProductModel> movedPrList = productService.getProductBySkuCode(usJoiChannelId, s.getSkuCode());
                            if (ListUtils.notNull(movedPrList)) {
                                if (movedPrList.size() > 1) {
                                    String errMsg = String.format("子店产品(%s)中skuCode(%s)在LIKING主店产品里面存在多个产品Code(%s)中，" +
                                                    "正常情况下一个skuCode只能存在于一个产品中，请确认主店产品数据。", productCode, originalProductCode,
                                            movedPrList.stream().map(p -> p.getCommonNotNull().getFieldsNotNull().getCode()).collect(Collectors.joining(",")));
                                    $error(errMsg);
                                    throw new BusinessException(errMsg);
                                } else if (movedPrList.size() == 1){
                                    // 把子店产品中每个skuCode对应的主店产品都加入到更新列表中去
                                    prList.add(movedPrList.get(0));
                                }
                            }
                        });
                    }
                } else {
                    // 子店不是拆分出来的产品，用code去查(其实也可以用skuCode去查，但效率可能会比较低)
                    CmsBtProductModel pr = productService.getProductByCode(usJoiChannelId, productCode);
                    if (pr != null) prList.add(pr);
                }

                // 根据code去查主店产品
//                pr = productService.getProductByCode(usJoiChannelId, productCode);
                // 再根据originalCode再去查一下，如果code没查到而originalCode查到的话，就是数据不整合，报错误
                List<CmsBtProductModel> originalCodeProductList = productService.getProductByOriginalCodeWithoutItself(usJoiChannelId, productCode);
                // -------------------------------------------------------------------------------------------------
                // 根据CMSDOC-281 总店的产品有可能会被别的业务拆分成成别的产品 P1(sku1,sku2,sku3)->P1(sku1),P2(sku2),P3(sku3)
                // 说明：1. P2和P3是新规生成的，它们的originalCode=P1.code，拆分sku时不能把sku追加到一个已经存在的Product中去(因为一个产品只能有一个originalCode);
                //      2. P1(sku1)里面的sku一定会至少保留一个的，不会出现P1下面的sku被拆完了把P1删掉的情况(有这种情况就是数据不整合);
                //      3. P1的sku2,sku3的更新应该更新到P2(sku2), P3(sku3)里面;
                //      4. 如果P1里面追加了一个新的sku(sku4),则应追加到P1里面 P1(sku1) -> P1(sku1,sku4) P2(sku2) P3(sku3);
                //      5. 主店产品的sku拆分是由别的程序(如:CmsProductMoveService)来做的，本程序新增产品的时候不做拆分，只需要确保更新的时候正确更新到拆分后的产品即可;
                // -------------------------------------------------------------------------------------------------
                if (ListUtils.isNull(prList)) {
                    // 如果code没查到而originalCode查到的话，就是数据不整合，报出错误消息
                    if (ListUtils.notNull(originalCodeProductList)) {
                        String errMsg = String.format("子店->USJOI主店产品导入:根据code(%s)没查到产品但根据originalCode(%s)查" +
                                        "到了产品，即使产品的sku拆分过也不能把拆分前的产品拆到一个sku都不剩的，可能是Product数据不整合，请确认" +
                                        "数据! [orgChannelId=%s] [usjoiChannelId=%s] [code=%s]",
                                productCode, productCode, productModel.getChannelId(), usJoiChannelId, productCode);
                        $error(errMsg);
                        throw new BusinessException(errMsg);
                    }

                    // 产品不存在，新增
                    productModel.setChannelId(usJoiChannelId);
                    productModel.setOrgChannelId(sxWorkLoadBean.getChannelId());
                    productModel.setSales(new CmsBtProductModel_Sales());
                    productModel.setTags(new ArrayList<>());
                    // 插入或者更新cms_bt_product_group_c928中的productGroup信息
                    doSetGroup(productModel, usJoiChannelId, usjoiTypeChannelBeanList);

                    productModel.setProdId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_PROD_ID));

                    // 更新product.common.fields里面的信息
                    // CMSDOC-365,CMCDOC-414 如果common.catConf="0"(非人工匹配主类目)时，自动匹配商品主类目
                    // TODO 2016/12/30暂时这样更新，以后要改
//                    if ("0".equals(productModel.getCommonNotNull().getCatConf())) {
                        doSetMainCategory(productModel.getCommon(), productModel.getFeed().getCatPath());
//                    }

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
                    for (CmsBtProductModel pr : prList) {
                        // 如果已经存在（如老的数据已经有了），更新
                        // 插入或者更新cms_bt_product_group_c928中的productGroup信息
                        doSetGroup(pr, usJoiChannelId, usjoiTypeChannelBeanList);
                        // 更新common的一部分属性
                        // 如果主店产品没有设置主类目，子店设置了，就把子店设置的主类目COPY到主店产品中
                        // 主类目id
                        if (StringUtil.isEmpty(pr.getCommonNotNull().getCatId())
                                && !StringUtil.isEmpty(productModel.getCommonNotNull().getCatId())) {
                            pr.getCommonNotNull().setCatId(productModel.getCommonNotNull().getCatId());
                        }

                        // 主类目Path(中文)
                        if (StringUtil.isEmpty(pr.getCommonNotNull().getCatPath())
                                && !StringUtil.isEmpty(productModel.getCommonNotNull().getCatPath())) {
                            pr.getCommonNotNull().setCatPath(productModel.getCommonNotNull().getCatPath());
                        }

                        // 主类目Path(英文)
                        if (StringUtil.isEmpty(pr.getCommonNotNull().getCatPathEn())
                                && !StringUtil.isEmpty(productModel.getCommonNotNull().getCatPathEn())) {
                            pr.getCommonNotNull().setCatPathEn(productModel.getCommonNotNull().getCatPathEn());
                        }

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
//                            // 税号集货
//                            if (StringUtil.isEmpty(prCommonFields.getHsCodeCrop())
//                                    && !StringUtil.isEmpty(productModel.getCommon().getFields().getHsCodeCrop())) {
//                                prCommonFields.setHsCodeCrop(productModel.getCommon().getFields().getHsCodeCrop());
//                            }

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

                            // CMCDOC-354 子店->总店更新产品common.fields时，总的原则是总店没有设置时，就用子店的设置（子店也有可能追加字段）
                            // 款号(common.fields.model)
                            if (StringUtil.isEmpty(prCommonFields.getModel())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getModel())) {
                                prCommonFields.setModel(productModel.getCommonNotNull().getFieldsNotNull().getModel());
                            }

                            // 商品编码(common.fields.code)
                            if (StringUtil.isEmpty(prCommonFields.getCode())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getCode())) {
                                prCommonFields.setCode(productModel.getCommonNotNull().getFieldsNotNull().getCode());
                            }

                            // 原始code 原商品编码(common.fields.originalCode)
                            if (StringUtil.isEmpty(prCommonFields.getOriginalCode())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getOriginalCode())) {
                                prCommonFields.setOriginalCode(productModel.getCommonNotNull().getFieldsNotNull().getOriginalCode());
                            }

                            // 品牌(common.fields.brand)
                            if (StringUtil.isEmpty(prCommonFields.getBrand())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getBrand())) {
                                prCommonFields.setBrand(productModel.getCommonNotNull().getFieldsNotNull().getBrand());
                            }

                            // 选择的尺码对照(common.fields.sizeChart)
//                            if (StringUtil.isEmpty(prCommonFields.getSizeChart())
//                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getSizeChart())) {
//                                prCommonFields.setSizeChart(productModel.getCommonNotNull().getFieldsNotNull().getSizeChart());
//                            }
                            if (StringUtil.isEmpty(pr.getCommon().getStringAttribute("sizeChart"))
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getStringAttribute("sizeChart"))) {
                                pr.getCommon().setAttribute("sizeChart", productModel.getCommonNotNull().getAttribute("sizeChart"));
                            }

                            // 产品名称英文(common.fields.productNameEn)
                            if (StringUtil.isEmpty(prCommonFields.getProductNameEn())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getProductNameEn())) {
                                prCommonFields.setProductNameEn(productModel.getCommonNotNull().getFieldsNotNull().getProductNameEn());
                            }

                            // 产品名称中文(common.fields.originalTitleCn)
                            if (StringUtil.isEmpty(prCommonFields.getOriginalTitleCn())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getOriginalTitleCn())) {
                                prCommonFields.setOriginalTitleCn(productModel.getCommonNotNull().getFieldsNotNull().getOriginalTitleCn());
                            }

                            // 简短描述英语(common.fields.shortDesEn)
                            if (StringUtil.isEmpty(prCommonFields.getShortDesEn())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getShortDesEn())) {
                                prCommonFields.setShortDesEn(productModel.getCommonNotNull().getFieldsNotNull().getShortDesEn());
                            }

                            // 简短描述中文(common.fields.shortDesCn)
                            if (StringUtil.isEmpty(prCommonFields.getShortDesCn())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getShortDesCn())) {
                                prCommonFields.setShortDesCn(productModel.getCommonNotNull().getFieldsNotNull().getShortDesCn());
                            }

                            // 详情描述英语(common.fields.longDesEn)
                            if (StringUtil.isEmpty(prCommonFields.getLongDesEn())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getLongDesEn())) {
                                prCommonFields.setLongDesEn(productModel.getCommonNotNull().getFieldsNotNull().getLongDesEn());
                            }

                            // 详情描述中文(common.fields.longDesCn)
                            if (StringUtil.isEmpty(prCommonFields.getLongDesCn())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getLongDesCn())) {
                                prCommonFields.setLongDesCn(productModel.getCommonNotNull().getFieldsNotNull().getLongDesCn());
                            }

                            // 材质英文(common.fields.materialEn)
                            if (StringUtil.isEmpty(prCommonFields.getMaterialEn())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getMaterialEn())) {
                                prCommonFields.setMaterialEn(productModel.getCommonNotNull().getFieldsNotNull().getMaterialEn());
                            }

                            // 材质中文(common.fields.materialCn)
                            if (StringUtil.isEmpty(prCommonFields.getMaterialCn())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getMaterialCn())) {
                                prCommonFields.setMaterialCn(productModel.getCommonNotNull().getFieldsNotNull().getMaterialCn());
                            }

                            // 颜色/口味/香型等(common.fields.color)
                            if (StringUtil.isEmpty(prCommonFields.getColor())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getColor())) {
                                prCommonFields.setColor(productModel.getCommonNotNull().getFieldsNotNull().getColor());
                            }

                            // 产地(common.fields.origin)
                            if (StringUtil.isEmpty(prCommonFields.getOrigin())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getOrigin())) {
                                prCommonFields.setOrigin(productModel.getCommonNotNull().getFieldsNotNull().getOrigin());
                            }

                            // 产品分类(common.fields.productType)
                            if (StringUtil.isEmpty(prCommonFields.getProductType())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getProductType())) {
                                prCommonFields.setProductType(productModel.getCommonNotNull().getFieldsNotNull().getProductType());
                            }

                            // 适用人群(common.fields.sizeType)
                            if (StringUtil.isEmpty(prCommonFields.getSizeType())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getSizeType())) {
                                prCommonFields.setSizeType(productModel.getCommonNotNull().getFieldsNotNull().getSizeType());
                            }

                            // 库存(common.fields.quantity)
                            if ((prCommonFields.getQuantity() == null || prCommonFields.getQuantity() == 0)
                                    && (productModel.getCommonNotNull().getFieldsNotNull().getQuantity() != null && productModel.getCommonNotNull().getFieldsNotNull().getQuantity() != 0)) {
                                prCommonFields.setQuantity(productModel.getCommonNotNull().getFieldsNotNull().getQuantity());
                            }

                            // 品牌方商品地址(common.fields.clientProductUrl)
                            if (StringUtil.isEmpty(prCommonFields.getClientProductUrl())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getClientProductUrl())) {
                                prCommonFields.setClientProductUrl(productModel.getCommonNotNull().getFieldsNotNull().getClientProductUrl());
                            }

                            // 类目设置状态(common.fields.categoryStatus)
                            if ((StringUtil.isEmpty(prCommonFields.getCategoryStatus()) || "0".equals(prCommonFields.getCategoryStatus()))
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getCategoryStatus())) {
                                prCommonFields.setCategoryStatus(productModel.getCommonNotNull().getFieldsNotNull().getCategoryStatus());
                            }

                            // 类目设置者(common.fields.categorySetter)
                            if (StringUtil.isEmpty(prCommonFields.getCategorySetter())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getCategorySetter())) {
                                prCommonFields.setCategorySetter(productModel.getCommonNotNull().getFieldsNotNull().getCategorySetter());
                            }

                            // 类目设置时间(common.fields.categorySetTime)
                            if (StringUtil.isEmpty(prCommonFields.getCategorySetTime())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getCategorySetTime())) {
                                prCommonFields.setCategorySetTime(productModel.getCommonNotNull().getFieldsNotNull().getCategorySetTime());
                            }

                            // 商品翻译状态(common.fields.translateStatus)
                            if ((StringUtil.isEmpty(prCommonFields.getTranslateStatus()) || "0".equals(prCommonFields.getTranslateStatus()))
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getTranslateStatus())) {
                                prCommonFields.setTranslateStatus(productModel.getCommonNotNull().getFieldsNotNull().getTranslateStatus());
                            }

                            // 翻译者(common.fields.translator)
                            if (StringUtil.isEmpty(prCommonFields.getTranslator())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getTranslator())) {
                                prCommonFields.setTranslator(productModel.getCommonNotNull().getFieldsNotNull().getTranslator());
                            }

                            // 翻译时间(common.fields.translateTime)
                            if (StringUtil.isEmpty(prCommonFields.getTranslateTime())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getTranslateTime())) {
                                prCommonFields.setTranslateTime(productModel.getCommonNotNull().getFieldsNotNull().getTranslateTime());
                            }

                            // 商品特质英文（颜色/口味/香型等）(common.fields.codeDiff)
                            if (StringUtil.isEmpty(prCommonFields.getCodeDiff())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getCodeDiff())) {
                                prCommonFields.setCodeDiff(productModel.getCommonNotNull().getFieldsNotNull().getCodeDiff());
                            }

                            // 使用说明英语(common.fields.usageEn)
                            if (StringUtil.isEmpty(prCommonFields.getUsageEn())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getUsageEn())) {
                                prCommonFields.setUsageEn(productModel.getCommonNotNull().getFieldsNotNull().getUsageEn());
                            }

                            // 使用说明中文(common.fields.usageCn)
                            if (StringUtil.isEmpty(prCommonFields.getUsageCn())
                                    && !StringUtil.isEmpty(productModel.getCommonNotNull().getFieldsNotNull().getUsageCn())) {
                                prCommonFields.setUsageCn(productModel.getCommonNotNull().getFieldsNotNull().getUsageCn());
                            }

                            // App端启用开关(用于控制所有平台的)(common.fields.appSwitch)只在主店未设置的时候COPY子店的，主店子店可以单独来设置
                            if (prCommonFields.getAppSwitch() == null
                                    && productModel.getCommonNotNull().getFieldsNotNull().getAppSwitch() != null) {
                                prCommonFields.setAppSwitch(productModel.getCommonNotNull().getFieldsNotNull().getAppSwitch());
                            }

                            // VO佣金费率,商品级(common.fields.commissionRate)
                            if ((prCommonFields.getCommissionRate() == null || prCommonFields.getCommissionRate().compareTo(0.0d) == 0)
                                    && (productModel.getCommonNotNull().getFieldsNotNull().getCommissionRate() != null && productModel.getCommonNotNull().getFieldsNotNull().getCommissionRate().compareTo(0.0d) != 0)) {
                                prCommonFields.setCommissionRate(productModel.getCommonNotNull().getFieldsNotNull().getCommissionRate());
                            }
                        }

                        // CMSDOC-365,CMCDOC-414 如果common.catConf="0"(非人工匹配主类目)时，自动匹配商品主类目
                        // TODO 2016/12/30暂时这样更新，以后要改
//                        if ("0".equals(pr.getCommonNotNull().getCatConf())) {
                            // 自动匹配商品主类目
                            doSetMainCategory(pr.getCommon(), pr.getFeed().getCatPath());
//                        }

                        // ****************common.skus的更新(有的sku可能在拆分后的product中)****************
                        for (CmsBtProductModel_Sku sku : productModel.getCommon().getSkus()) {
                            CmsBtProductModel_Sku oldSku = pr.getCommon().getSku(sku.getSkuCode());
                            if (oldSku == null) {
                                boolean updateFlg = false;
                                // 如果没有在usjoi产品的commom.skus找到子店对应的skuCode，再用originalCode产品里面查找该skucode,找到就更新到originalCode的产品中的sku
                                if (ListUtils.notNull(originalCodeProductList)) {
                                    for (CmsBtProductModel prodObj : originalCodeProductList) {
                                        List<CmsBtProductModel_Sku> prodCommonSkusObj = prodObj.getCommonNotNull().getSkus();
                                        if (ListUtils.notNull(prodCommonSkusObj) && prodCommonSkusObj.stream().filter(p -> sku.getSkuCode().equals(p.getSkuCode())).count() > 0) {
                                            CmsBtProductModel_Sku prodCommonSku = prodCommonSkusObj.stream().filter(p -> sku.getSkuCode().equals(p.getSkuCode())).findFirst().get();
                                            if (prodCommonSku != null) {
                                                // 把子店产品中common.skus.sku更新到总店拆分后的产品(例：P2, P3)
                                                updateCommonSku(sku, prodCommonSku);
                                                updateFlg = true;
                                                // 正常情况下，一个skuCode应该只存在一个产品中,所以找到就退出
                                                break;
                                            }
                                        }
                                    }
                                }
                                // 如果在code和拆分后的originalCode里面都没有找到该skuCode,则将该sku追加到code(例：P1)中
                                if (!updateFlg) pr.getCommon().getSkus().add(sku);
                            } else {
                                // 把子店产品中common.skus.sku更新到总店产品
                                updateCommonSku(sku, oldSku);
//                            // 如果在usjoi产品的commom.skus找到子店对应的skuCode时
//                            // 跟feed->master统一，无条件更新尺码等共通sku属性
//                            oldSku.setBarcode(sku.getBarcode());
//                            oldSku.setClientSkuCode(sku.getClientSkuCode());
//                            oldSku.setClientSize(sku.getClientSize());
//                            oldSku.setSize(sku.getSize());
//                            oldSku.setWeight((sku.getWeight()));  // 重量(单位：磅)
//
//                            // 价格发生变化的时候更新该sku价格
////                            if (oldSku.getPriceMsrp().compareTo(sku.getPriceMsrp()) != 0
////                                    || oldSku.getPriceRetail().compareTo(sku.getPriceRetail()) != 0) {
//                            // 美金专柜价
//                            oldSku.setClientMsrpPrice(sku.getClientMsrpPrice());
//                            // 美金指导价
//                            oldSku.setClientRetailPrice(sku.getClientRetailPrice());
//                            // 美金成本价(=priceClientCost)
//                            oldSku.setClientNetPrice(sku.getClientNetPrice());
//                            // 人民币专柜价(后面价格计算要用到，因为010,018等店铺不用新价格体系，还是用老的价格公式)
//                            oldSku.setPriceMsrp(sku.getPriceMsrp());
//                            // 人民币指导价(后面价格计算要用到，因为010,018等店铺不用新价格体系，还是用老的价格公式)
//                            oldSku.setPriceRetail(sku.getPriceRetail());
////                            }
                            }
                        }

//                    // 由于需要无条件更新common.image1等属性，所以无条件更新common属性
//                    // 共通方法里面有Approved的时候，自动插入USJOI(928,929)->平台(京东国际匠心界，悦境)上新workload记录
//                    productService.updateProductCommon(usJoiChannelId, pr.getProdId(), pr.getCommon(), getTaskName(), false);

                        // ****************platform.PXX.skus的更新(有的sku可能在拆分后的product中)****************
                        final CmsBtProductModel finalProductModel1 = productModel;
                        // 子店产品中的一部分sku可能已经在主店中被拆分到其他产品中去了，所以不能直接用子店产品的PXX.skus设置
                        // 由于前面已经更新了pr.common.skus(该加的sku也都加了)，所以这里根据pr.common.skus里面有的skuCode过滤就行了
                        // 当前产品的正确common.skuCode列表
                        List<CmsBtProductModel_Sku> prCommonSkus = pr.getCommonNotNull().getSkus();
                        List<String> prCommonSkuCodeList = (prCommonSkus == null) ?
                                new ArrayList<>() : prCommonSkus.stream().map(p -> p.getSkuCode()).collect(Collectors.toList());
                        // 保存子店过来的P928.skus，去掉拆分到其他产品中的skuCode列表
                        List<BaseMongoMap<String, Object>> correctPlatformSkus = new ArrayList<>();
                        // 取得子店的平台(P928)数据
                        CmsBtProductModel_Platform_Cart fromPlatform = finalProductModel1.getPlatform(sxWorkLoadBean.getCartId());
                        fromPlatform.getSkus().forEach(p -> {
                            // 在common.skus里面有的sku才会加进来(已拆分出去的sku不会加进来)
                            if (prCommonSkuCodeList.contains(p.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()))) {
                                correctPlatformSkus.add(p);
                            }
                        });
                        // 设置分平台信息(P28,P29,P32等,cartId不一样，其他都一样)
                        for (Integer cartId : cartIds) {
                            CmsBtProductModel_Platform_Cart platformCart = pr.getPlatform(cartId);
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
                                // 重新设置newPlatform的skus，因为fromPlatform里面过来的是全部的sku，要去掉拆分到其他产品的sku
                                newPlatform.setSkus(correctPlatformSkus);

                                // 设定是否主商品(根据主店商品code来判断)
                                CmsBtProductGroupModel group = productGroupService.selectMainProductGroupByCode(usJoiChannelId,
                                        pr.getCommonNotNull().getFieldsNotNull().getCode(), cartId);
                                if (group == null) {
                                    newPlatform.setpIsMain(0);
                                    CmsBtProductGroupModel cmsBtProductGroupModel = productGroupService.selectProductGroupByCode(usJoiChannelId, pr.getCommonNotNull().getFieldsNotNull().getCode(), cartId);
                                    if(cmsBtProductGroupModel != null){
                                        newPlatform.setMainProductCode(cmsBtProductGroupModel.getMainProductCode());
                                    }
                                } else {
                                    newPlatform.setpIsMain(1);
                                    newPlatform.setMainProductCode(pr.getCommonNotNull().getFieldsNotNull().getCode());
                                }

                                pr.getPlatforms().put("P" + StringUtils.toString(cartId), newPlatform);
//                            productService.updateProductPlatform(usJoiChannelId, pr.getProdId(), newPlatform, getTaskName());

                                // 看看拆分后的产品中有没有当前平台的分平台信息，如果没有的话新增该分平台信息
                                // 例如：以前只有P28平台，配置表里面追加了一个29平台，要把P28复制一下生成P29平台信息)
                                // 拆分后的产品的PXX.skus不能设置为correctPlatformSkus，因为这个是给被拆分产品用的(新追加的sku会追加到被拆分产品中)
                                if (ListUtils.notNull(originalCodeProductList)) {
                                    for (CmsBtProductModel prodObj : originalCodeProductList) {
                                        CmsBtProductModel_Platform_Cart currentOriginalPlatformCart = prodObj.getPlatform(cartId);
                                        if (currentOriginalPlatformCart == null) {
                                            // 如果拆分后产品中没有当前分平台信息的话，要复制追加一下
                                            for (Integer tempCartId : cartIds) {
                                                CmsBtProductModel_Platform_Cart tempPlatformCart = prodObj.getPlatform(tempCartId);
                                                if (tempPlatformCart != null) {
                                                    // 这里一定要新建一个分平台信息，不新建的话，会跟复制元共享同一个对象，不能改cartId(一改就所有PXX都会改)
                                                    CmsBtProductModel_Platform_Cart tempNewPlatform = new CmsBtProductModel_Platform_Cart();
                                                    tempNewPlatform.putAll(tempPlatformCart);
                                                    // 新增当前cartId的分平台信息(如:P29)(里面会自动设置cartId的)
                                                    prodObj.setPlatform(cartId, tempNewPlatform);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
                                // 设定是否主商品(根据主店商品code来判断)
                                CmsBtProductGroupModel group = productGroupService.selectMainProductGroupByCode(usJoiChannelId,
                                        pr.getCommonNotNull().getFieldsNotNull().getCode(), cartId);
                                if (group == null) {
                                    platformCart.setpIsMain(0);
                                } else {
                                    platformCart.setpIsMain(1);
                                }

                                if (ListUtils.isNull(platformCart.getSkus())) {
//                              // 设置主店产品的PXX.skus信息
                                    platformCart.setSkus(correctPlatformSkus);
                                } else {
                                    for (BaseMongoMap<String, Object> newSku : fromPlatform.getSkus()) {
                                        boolean updateFlg = false;
                                        // 取得当前循环到的子店sku的skuCode
                                        String currentSkuCode = newSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
                                        if (platformCart.getSkus() != null) {
                                            // newSku是子店的sku, oldSku是从USJOI主店根据code(不是originalCode)里面查出来的sku
                                            for (BaseMongoMap<String, Object> oldSku : platformCart.getSkus()) {
                                                if (oldSku.get("skuCode").toString().equalsIgnoreCase(currentSkuCode)) {
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

                                        // 如果在当前产品里没找到，看一下拆分后的产品中有没有，有的话也是更新，不能新增sku的
                                        // 如果没有在usjoi产品的commom.skus找到子店对应的skuCode，再用originalCode产品里面查找该skucode,找到就更新到originalCode的产品中的sku
                                        if (!updateFlg && ListUtils.notNull(originalCodeProductList)) {
                                            for (CmsBtProductModel prodObj : originalCodeProductList) {
                                                // 因为分平台PXX下面的skuCode肯定会存在common.skus里面，所以直接用common.skus判断
                                                List<CmsBtProductModel_Sku> prodCommonSkusObj = prodObj.getCommonNotNull().getSkus();
                                                if (ListUtils.notNull(prodCommonSkusObj) && prodCommonSkusObj.stream().filter(p -> currentSkuCode.equals(p.getSkuCode())).count() > 0) {
                                                    // 看看当前子店的sku(newSku)在拆分后产品里是否存在，存在的话就不能做追加
                                                    CmsBtProductModel_Sku prodCommonSku = prodCommonSkusObj.stream().filter(p -> currentSkuCode.equals(p.getSkuCode())).findFirst().get();
                                                    if (prodCommonSku != null) {
                                                        // 找到了也不用做更新，因为PXX.skus下面只有一个平台相关价格属性，这个会再后面调用价格计算共通方法自动把美金价格算成人民币价格设置的
                                                        updateFlg = true;
                                                        // 看看拆分后的产品中有没有当前平台的分平台信息，如果没有的话新增该分平台信息
                                                        // 例如：以前只有P28平台，配置表里面追加了一个29平台，要把P28复制一下生成P29平台信息)
                                                        CmsBtProductModel_Platform_Cart currentOriginalPlatformCart = prodObj.getPlatform(cartId);
                                                        if (currentOriginalPlatformCart == null) {
                                                            // 如果拆分后产品中没有当前分平台信息的话，要复制追加一下
                                                            for (Integer tempCartId : cartIds) {
                                                                CmsBtProductModel_Platform_Cart tempPlatformCart = prodObj.getPlatform(tempCartId);
                                                                if (tempPlatformCart != null) {
                                                                    // 这里一定要新建一个分平台信息，不新建的话，会跟复制元共享同一个对象，不能改cartId(一改就所有PXX都会改)
                                                                    CmsBtProductModel_Platform_Cart tempNewPlatform = new CmsBtProductModel_Platform_Cart();
                                                                    tempNewPlatform.putAll(tempPlatformCart);
                                                                    // 新增当前cartId的分平台信息(如:P29)(里面会自动设置cartId的)
                                                                    prodObj.setPlatform(cartId, tempNewPlatform);
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        // 正常情况下，一个skuCode应该只存在一个产品中,所以找到就退出
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                        if (!updateFlg) {
                                            // 只有当当前code和originalCode里面都没有找到该skuCode，才会把它加到当前code中
                                            platformCart.getSkus().add(newSku);
                                            // 子店到总店结束之后，如果该code已经上新，并且该code下存在新追加sku的情况是，设置platforms.Pxx.isNewSku = "1"
                                            if (!StringUtils.isEmpty(platformCart.getpNumIId())) {
                                                platformCart.setIsNewSku("1");
                                            }
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

                        // 将USJOI店的产品加入更新对象产品列表中（取得USJOI店的品牌，产品分类和适用人群）
                        targetProductList.add(pr);
                    }

                    // 更新拆分后的产品的价格相关项目,并更新mongoDB的Product表
                    if (ListUtils.notNull(originalCodeProductList)) {
                        originalCodeProductList.forEach(p -> {
                            // 更新拆分后的产品的价格相关项目
                            p = doSetPrice(p);

                            // 更新拆分后的产品并记录商品价格表动履历，并向Mq发送消息同步sku,code,group价格范围
                            productService.updateProductFeedToMaster(usJoiChannelId, p, getTaskName(), "子店->USJOI主店导入:更新拆分后的产品:");
                        });
                    }

                    // 插入主店上新workload表
                    insertWorkload(productModel, ccAutoSyncCarts, ccAutoSyncCartList);

                    // 虽然这里拆分后的产品只是做一下更新sku信息而已，也加进去吧，万一产分后的品牌等没有的话，也可以追加进去了
                    if (ListUtils.notNull(originalCodeProductList)) {
                        originalCodeProductList.forEach(p -> {
                            targetProductList.add(p);
                        });
                    }

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

            // 如果Synship.com_mt_value_channel表中没有usjoi channel(928,929)对应的品牌，则插入该信息
            insertMtValueChannelInfo(usJoiChannelId, mapBrandMapping, targetProductList);

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
            // 上新对象code
            productGroupService.updateUploadErrorStatus(groupModel, groupModel.getProductCodes(), errMsg);
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
     * 如果Synship.com_mt_value_channel表中没有usjoi channel(928,929)对应的品牌信息，则插入该信息
     *
     * @param usjoiChannelId String usjoi channel id
     * @param mapBrandMapping Map<String, String> 品牌mapping一览
     * @param usjoiProductModels List<CmsBtProductBean> 产品列表
     */
    private void insertMtValueChannelInfo(String usjoiChannelId, Map<String, String> mapBrandMapping, List<CmsBtProductModel> usjoiProductModels) {

        // 循环产品列表，如果品牌，产品类型或适用人群信息，则插入该信息到Synship.com_mt_value_channel表中
        for (CmsBtProductModel usjoiProductModel : usjoiProductModels) {
            if (usjoiProductModel.getCommon() == null || usjoiProductModel.getCommon().getFields() == null) {
                continue;
            }
            // 品牌
            String usjoiBrand = usjoiProductModel.getCommon().getFields().getBrand();

            // 品牌(不区分大小写，全部小写)
            if (!StringUtils.isEmpty(usjoiBrand)
                    && !mapBrandMapping.containsKey(usjoiBrand.toLowerCase().trim())) {
                // 插入品牌初始中英文mapping信息到Synship.com_mt_value_channel表中
                comMtValueChannelService.insertComMtValueChannelMapping(41, usjoiChannelId,
                        usjoiBrand.toLowerCase().trim(), usjoiBrand.toLowerCase().trim(), getTaskName());
                // 将更新完整之后的mapping信息添加到前面取出来的品牌mapping表中
                mapBrandMapping.put(usjoiBrand.toLowerCase().trim(), usjoiBrand.toLowerCase().trim());
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

    /**
     * 把子店产品中common.skus.sku更新到总店产品
     *
     * @param fromSku String  子店产品的common.skus.sku
     * @param usjoiSku String 主店产品的common.skus.sku
     */
    protected void updateCommonSku(CmsBtProductModel_Sku fromSku, CmsBtProductModel_Sku usjoiSku) {
        // 如果在usjoi产品的commom.skus找到子店对应的skuCode时
        // 跟feed->master统一，无条件更新尺码等共通sku属性
        usjoiSku.setBarcode(fromSku.getBarcode());
        usjoiSku.setClientSkuCode(fromSku.getClientSkuCode());
        usjoiSku.setClientSize(fromSku.getClientSize());
        usjoiSku.setSize(fromSku.getSize());
        usjoiSku.setWeight((fromSku.getWeight()));  // 重量(单位：磅)

        // 更新该sku价格
        // 美金专柜价
        usjoiSku.setClientMsrpPrice(fromSku.getClientMsrpPrice());
        // 美金指导价
        usjoiSku.setClientRetailPrice(fromSku.getClientRetailPrice());
        // 美金成本价(=priceClientCost)
        usjoiSku.setClientNetPrice(fromSku.getClientNetPrice());
        // 人民币专柜价(后面价格计算要用到，因为010,018等店铺不用新价格体系，还是用老的价格公式)
        usjoiSku.setPriceMsrp(fromSku.getPriceMsrp());
        // 人民币指导价(后面价格计算要用到，因为010,018等店铺不用新价格体系，还是用老的价格公式)
        usjoiSku.setPriceRetail(fromSku.getPriceRetail());

    }

    /**
     * 自动匹配商品主类目
     *
     * @param prodCommon 产品共通属性
     * @param feedCategoryPath feed类目Path
     */
    protected void doSetMainCategory(CmsBtProductModel_Common prodCommon, String feedCategoryPath) {
        if (prodCommon == null || StringUtils.isEmpty(feedCategoryPath)) return;

        // 共通Field
        CmsBtProductModel_Field prodCommonField = prodCommon.getFieldsNotNull();

        // 调用Feed到主数据的匹配接口取得匹配度最高的主类目
        SearchResult searchResult = getMainCatInfo(feedCategoryPath,
                prodCommonField.getProductType(),
                prodCommonField.getSizeType(),
                prodCommonField.getProductNameEn());
        if (searchResult != null && searchResult.getMtCategoryKeysModel() != null) {
            // 先备份原来的productType和sizeType
            // feed原始产品分类
            prodCommonField.setOrigProductType(prodCommonField.getProductType());
            // feed原始适合人群
            prodCommonField.setOrigSizeType(prodCommonField.getSizeType());

            // 主类目匹配结果model
            MtCategoryKeysModel mtCategoryKeysModel = searchResult.getMtCategoryKeysModel();

            // 主类目path(中文)
            prodCommon.setCatPath(mtCategoryKeysModel.getCnName());
            // 主类目path(英文)
            prodCommon.setCatPathEn(mtCategoryKeysModel.getEnName());
            // 主类目id(就是主类目path中文的MD5码)
            prodCommon.setCatId(MD5.getMD5(mtCategoryKeysModel.getCnName()));

            // 更新主类目设置状态
            String oldCategoryStatus = prodCommonField.getCategoryStatus();
            prodCommonField.setCategoryStatus(StringUtil.isEmpty(prodCommon.getCatId()) ? "0" : "1");
            if(!prodCommonField.getCategoryStatus().equalsIgnoreCase(oldCategoryStatus)){
                // 如果状态有变更且变成1时，记录更新时间
                if("1".equals(prodCommonField.getCategoryStatus())){
                    prodCommonField.setCategorySetTime(DateTimeUtil.getNow());
                    prodCommonField.setCategorySetter(getTaskName());
                }
            }
            // 产品分类(英文)
            prodCommonField.setProductType(mtCategoryKeysModel.getProductTypeEn());
            // 产品分类(中文)
            prodCommonField.setProductTypeCn(mtCategoryKeysModel.getProductTypeCn());
            // 适合人群(英文)
            prodCommonField.setSizeType(mtCategoryKeysModel.getSizeTypeEn());
            // 适合人群(中文)
            prodCommonField.setSizeTypeCn(mtCategoryKeysModel.getSizeTypeCn());
            // TODO 2016/12/30暂时这样更新，以后要改
            if ("CmsUploadProductToUSJoiJob".equalsIgnoreCase(prodCommonField.getHsCodeSetter())) {
                // 税号个人
                prodCommonField.setHsCodePrivate(mtCategoryKeysModel.getTaxPersonal());
                // 更新税号设置状态
                String oldHsCodeStatus = prodCommonField.getHsCodeStatus();
                prodCommonField.setHsCodeStatus(StringUtil.isEmpty(prodCommonField.getHsCodePrivate()) ? "0" : "1");
                if(!prodCommonField.getHsCodeStatus().equalsIgnoreCase(oldHsCodeStatus)){
                    // 如果状态有变更且变成1时，记录更新时间
                    if("1".equals(prodCommonField.getHsCodeStatus())){
                        prodCommonField.setHsCodeSetTime(DateTimeUtil.getNow());
                        prodCommonField.setHsCodeSetter(getTaskName());
                    }
                }
            }
            // 税号跨境申报（10位）
            prodCommonField.setHsCodeCross(mtCategoryKeysModel.getTaxDeclare());

            // 商品中文名称(如果已翻译，则不设置)
            if ("0".equals(prodCommonField.getTranslateStatus())) {
                // 主类目叶子级中文名称（"服饰>服饰配件>钱包卡包钥匙包>护照夹" -> "护照夹"）
                String leafCategoryCnName = mtCategoryKeysModel.getCnName().substring(mtCategoryKeysModel.getCnName().lastIndexOf(">") + 1,
                        mtCategoryKeysModel.getCnName().length());
                // 设置商品中文名称（品牌 + 空格 + Size Type中文 + 空格 + 主类目叶子级中文名称）
                prodCommonField.setOriginalTitleCn(getOriginalTitleCnByCategory(prodCommonField.getBrand()
                        , prodCommonField.getSizeTypeCn(), leafCategoryCnName));
            }
        }
    }

    /**
     * 根据自动匹配商品主类目取得商品中文名称
     *
     * @param brand 品牌
     * @param sizeTypeCn 适合人群(中文)
     * @param leafCategoryCnName 主类目叶子级中文名称
     * @return String 商品中文名称(品牌 + 空格 + Size Type中文 + 空格 + 主类目叶子级中文名称)
     */
    public String getOriginalTitleCnByCategory(String brand, String sizeTypeCn, String leafCategoryCnName) {
        return brand + " " + sizeTypeCn + " " + leafCategoryCnName;
    }

    /**
     * 调用Feed到主数据的匹配接口匹配主类目,返回匹配度最高的第一个查询结果
     *
     * @param feedCategoryPath feed类目Path
     * @param productType 产品分类
     * @param sizeType 适合人群(英文)
     * @param productNameEn 产品名称（英文）
     * @return SearchResult 匹配度最高的第一个查询结果
     */
    public SearchResult getMainCatInfo(String feedCategoryPath, String productType, String sizeType, String productNameEn) {
        // 调用Feed到主数据的匹配程序匹配主类目
        StopWordCleaner cleaner = new StopWordCleaner();
        // 子店feed类目path分隔符(由于导入feedInfo表时全部替换成用"-"来分隔了，所以这里写固定值就可以了)
        List<String> categoryPathSplit = new ArrayList<>();
        categoryPathSplit.add("-");
        Tokenizer tokenizer = new Tokenizer(categoryPathSplit);

        FeedQuery query = new FeedQuery(feedCategoryPath, cleaner, tokenizer);
        query.setProductType(productType);
        query.setSizeType(sizeType);
        query.setProductName(productNameEn);

        // 调用主类目匹配接口，取得匹配度最高的一个主类目
        List<SearchResult> searchResults = searcher.search(query, 1);
        if (ListUtils.isNull(searchResults)) {
            String errMsg = String.format("调用Feed到主数据的匹配程序匹配主类目失败！[feedCategoryPath:%s] [productType:%s] " +
                    "[sizeType:%s] [productNameEn:%s]", feedCategoryPath, productType, sizeType, productNameEn);
            $error(errMsg);
            return null;
        }

        // 取得匹配度最高的主类目
        return searchResults.get(0);
    }
}
