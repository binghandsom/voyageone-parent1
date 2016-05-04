package com.voyageone.task2.cms.service;

import com.jd.open.api.sdk.domain.sellercat.ShopCategory;
import com.jd.open.api.sdk.domain.ware.ImageReadService.Image;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.MultiCheckField;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.bean.JdProductBean;
import com.voyageone.components.jd.service.JdShopService;
import com.voyageone.components.jd.service.JdWareService;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.*;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtSizeMapModel;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.CmsMtPlatFormDictModel;
import com.voyageone.service.model.cms.CmsMtPlatformSkusModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.base.BaseMQCmsService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.CmsConstants;
import com.voyageone.task2.cms.bean.WorkLoadBean;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
import com.voyageone.task2.cms.service.putaway.ConditionPropValueRepo;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 京东平台产品上新服务
 * Product表中产品不存在就向京东平台新增商品，否则就更新商品
 *
 * @author desmond on 2016/4/12.
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_PlatformProductUploadJdJob)
public class CmsBuildPlatformProductUploadJdMqService extends BaseMQCmsService {

    // 京东平台的操作类型(在售)
    private final static String OptioinType_onsale = "onsale";
    // 京东平台的操作类型(在库)
    private final static String OptioinType_offsale = "offsale";
    // 价格类型(市场价格)
    private final static String PriceType_marketprice = "retail_price";
    // 价格类型(京东价格)
    private final static String PriceType_jdprice = "sale_price";
    // 商品属性列表
    private final static String Attrivutes = "attributes";
    // 用户自行输入的类目属性ID串
    private final static String Input_Pids = "input_pids";
    // 用户自行输入的属性值串
    private final static String Input_Strs = "input_strs";
    // SKU属性类型(颜色)
    private final static String AttrType_Color = "c";
    // SKU属性类型(尺寸)
    private final static String AttrType_Size = "s";
    // SKU属性Active
    private final static int AttrType_Active_1 = 1;
    // 分隔符(tab)
    private final static String Separtor_Xor = "^";
    // 分隔符(|)
    private final static String Separtor_Vertical = "|";
    // 分隔符(-)
    private final static String Separtor_Hyphen = "-";
    // 分隔符(:)
    private final static String Separtor_Colon = ":";
    // 分隔符(;)
    private final static String Separtor_Semicolon = ";";
    // 分隔符(,)
    private final static String Separtor_Coma = ",";
    // work_load表publish_status会(1:成功)
    private final static int WorkLoad_status_1 = 1;
    // work_load表publish_status会(2:失败)
    private final static int WorkLoad_status_2 = 2;
    // 商品主图颜色值Id(0000000000)
    private final static String ColorId_MinPic = "0000000000";
    // 用户名（当前类名）
    private final String UserId_ClassName = this.getClass().getSimpleName();
    @Autowired
    PlatformMappingService platformMappingService;
    @Autowired
    DictService dictService;
    @Autowired
    private PlatformProductUploadJdService jdProductUploadService;
    @Autowired
    private PlatformCategoryService platformCategoryService;
    @Autowired
    private ConditionPropValueRepo conditionPropValueRepo;
    @Autowired
    private JdShopService jdShopService;
    @Autowired
    private JdWareService jdWareService;
    @Autowired
    private CmsMtPlatformSkusService cmcMtPlatformSkusService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupService productGroupService;
    // workload对象列表
    private Set<WorkLoadBean> workLoadBeans;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        doMain(taskControlList);
    }

    /**
     * 京东平台上新处理
     *
     * @param taskControlList taskcontrol信息
     */
    public void doMain(List<TaskControlBean> taskControlList) throws Exception {

        // 获取该任务可以运行的销售渠道
        List<String> channelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 循环所有销售渠道
        if (channelIdList != null && channelIdList.size() > 0) {
            for (String channelId : channelIdList) {
                // TODO 虽然workload表里不想上新的渠道，不会有数据，这里的循环稍微有点效率问题，后面再改
                // 京东平台商品信息新增或更新(京东)
                doProductUploadJd(channelId, Integer.parseInt(CartEnums.Cart.JD.getId()));
                // 京东国际商品信息新增或更新(京东国际)
                doProductUploadJd(channelId, Integer.parseInt(CartEnums.Cart.JG.getId()));
                // 京东平台商品信息新增或更新(京东国际 匠心界)
                doProductUploadJd(channelId, Integer.parseInt(CartEnums.Cart.JGJ.getId()));
                // 京东国际商品信息新增或更新(京东国际 悦境)
                doProductUploadJd(channelId, Integer.parseInt(CartEnums.Cart.JGY.getId()));
            }
        }

        // 正常结束
        $info("正常结束");
    }

    /**
     * 京东平台产品每个平台的上新主处理
     *
     * @param channelId String 渠道ID
     */
    private void doProductUploadJd(String channelId, int cartId) throws Exception {

        // 默认线程池最大线程数
        int threadPoolCnt = 5;

        // 获取店铺信息
        ShopBean shopProp = Shops.getShop(channelId, cartId);
        if (shopProp == null) {
            $error("获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }

        // 从上新的任务表中获取该平台及渠道需要上新的任务列表(group by channel_id, cart_id, group_id)
        List<CmsBtSxWorkloadModel> sxWorkloadModels = jdProductUploadService.getSxWorkloadWithChannelIdCartId(
                CmsConstants.PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE, channelId, cartId);
        if (sxWorkloadModels == null || sxWorkloadModels.size() == 0) {
            $error("上新任务表中没有该渠道和平台对应的任务列表信息！[ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolCnt);
        // 根据上新任务列表中的groupid循环上新处理
        for(CmsBtSxWorkloadModel cmsBtSxWorkloadModel:sxWorkloadModels) {
            // 启动多线程
            executor.execute(() -> uploadProductJd(cmsBtSxWorkloadModel, shopProp));
        }
        // ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
        // 当所有已经提交的任务执行完毕后将会关闭ExecutorService。
        executor.shutdown(); //并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
//        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

        // 正常结束
//        $info(String.format("京东指定渠道和平台新增或更新商品信息成功！[ChannelId:%s] [CartId:%s]", channelId, cartId));

    }

    /**
     * 每个平台的京东产品上新处理
     *
     * @param cmsBtSxWorkloadModel CmsBtSxWorkloadModel WorkLoad信息
     * @param shopProp ShopBean 店铺信息
     */
    private void uploadProductJd(CmsBtSxWorkloadModel cmsBtSxWorkloadModel, ShopBean shopProp) {

        // 当前groupid(用于取得产品信息)
        long groupId = cmsBtSxWorkloadModel.getGroupId();
        // 渠道id
        String channelId = shopProp.getOrder_channel_id();
        // 平台id
        int cartId = Integer.parseInt(shopProp.getCart_id());
        // 商品id
        long jdWareId = 0;

        try {
            // 上新用的商品数据信息取得
            SxData sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);
            if (sxData == null) {
                String errMsg = String.format("取得上新用的商品数据信息失败！[ChannelId:%s] [GroupId:%s]", channelId, groupId);
                $error(errMsg);
                throw new BusinessException(errMsg);
            }
            // 单个product内部的sku列表分别进行排序
            for (CmsBtProductModel cmsBtProductModel : sxData.getProductList()) {
                sxProductService.sortSkuInfo(cmsBtProductModel.getSkus());
            }
            // 主产品等列表取得
            CmsBtProductModel mainProduct = sxData.getMainProduct();
            List<CmsBtProductModel> productList = sxData.getProductList();
            List<CmsBtProductModel_Sku> skuList = sxData.getSkuList();
            // 主产品取得结果判断
            if (mainProduct == null) {
                String errMsg = String.format("取得主商品信息失败！[ChannelId:%s] [GroupId:%s]", channelId, groupId);
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

            // 构造该产品所有SKUCODE的字符串列表
            List<String> strSkuCodeList = new ArrayList<>();
            skuList.forEach(sku -> strSkuCodeList.add(sku.getSkuCode()));
            // 取得每个SKU的逻辑库存信息
            Map<String, Integer> skuLogicQtyMap = new HashMap<>();
            skuLogicQtyMap = productService.getLogicQty(channelId, strSkuCodeList);

            String sizeMapGroupId = ""; // TODO No.1 这个字段还没加 sizeMapGroupId =  mainProduct.getFields().getSizeMapGroupId();

            // 属性值准备
            // 取得主产品类目对应的platform mapping数据
            CmsMtPlatformMappingModel cmsMtPlatformMappingModel = platformMappingService.getMappingByMainCatId(shopProp.getOrder_channel_id(),
                    Integer.parseInt(shopProp.getCart_id()), mainProduct.getCatId());
            if (cmsMtPlatformMappingModel == null) {
                String errMsg = String.format("共通PlatformMapping表中对应的平台Mapping信息不存在！[ChannelId:%s] [CartId:%s] [主产品类目:%s]", channelId, cartId, mainProduct.getCatId());
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

            // 取得主产品类目对应的平台类目
            String platformCategoryId = cmsMtPlatformMappingModel.getPlatformCategoryId();
            // 取得平台类目schema信息
            CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = platformCategoryService.getPlatformCatSchema(platformCategoryId, cartId);
            if (cmsMtPlatformCategorySchemaModel == null) {
                String errMsg = String.format("获取平台类目schema信息失败！[PlatformCategoryId:%s] [CartId:%s]", platformCategoryId, cartId);
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

            // 获取cms_mt_platform_skus表里渠道指定类目对应的所有颜色和尺寸信息列表
            List<CmsMtPlatformSkusModel> cmsMtPlatformSkusList = new ArrayList<>();
            cmsMtPlatformSkusList = cmcMtPlatformSkusService.getModesByAttrType(channelId, cartId, platformCategoryId, AttrType_Active_1);

            // 获取尺码表
            // 7. product.fields（mongodb， 共通字段略）增加三个字段
            //       * sizeGroupId        <-设置SKU信息     //不是新任务列表中的groupid
            //       * sizeChartIdPc      <-上传图片用
            //       * sizeChartIdMobile  <-上传图片用
            // 调用共通函数
            // TODO No.1 SizeMapGroupId现在没有后面会加到field里面去,暂时不做，预计五月中旬再做
            List<CmsBtSizeMapModel> cmsBtSizeMapModelList = new ArrayList<>();
//                cmsBtSizeMapModelList = sxProductService.selectSizeMapList();

            // 获取字典表(根据channel_id)上传图片的规格等信息
            List<CmsMtPlatFormDictModel> cmsMtPlatFormDictModelList = dictService.getModesByChannelCartId(channelId, cartId);
            if (cmsMtPlatFormDictModelList == null) {
                String errMsg = String.format("获取字典表数据失败！[ChannelId:%s] [CartId:%s]", channelId, cartId);
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

            // 编辑京东共通属性
            JdProductBean jdProductBean = new JdProductBean();
            jdProductBean = setJdProductCommonInfo(sxData, platformCategoryId, groupId, shopProp,
                    cmsMtPlatformMappingModel, cmsMtPlatformCategorySchemaModel, skuLogicQtyMap);

            // 产品和颜色值的Mapping关系表(设置SKU属性时填入值，上传SKU图片时也会用到)
            Map<String, Object> productColorMap = new HashMap<String, Object>();

            // 返回结果是否成功状态
            boolean retStatus = false;

            // 判断新增商品还是更新商品
            // 遍历product列表中所有product的platform的numIID,只要有一个非空则为更新商品，全部为空则是新增商品
            // 新增或更新商品标志
            boolean updateWare = false;
            for (CmsBtProductModel product : productList) {
                // 只要有一个有numIId就认为是已经存在的商品， 需要更新
                if (!StringUtils.isEmpty(product.getGroups().getNumIId())) {
                    // 更新商品
                    updateWare = true;
                    // 取得更新对象商品id
                    jdWareId = Long.parseLong(product.getGroups().getNumIId());
                    break;
                }
            }

            // 新增或更新商品主处理
            if (!updateWare) {
                // 新增商品的时候

                // 调用京东新增商品API(360buy.ware.add)
                jdWareId = jdWareService.addProduct(shopProp, jdProductBean);
                // 返回的商品id是0的时候，新增商品失败
                if (0 == jdWareId) {
                    String errMsg = String.format("京东新增商品失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                            channelId, cartId, groupId, platformCategoryId);
                    $error(errMsg);
                    throw new BusinessException(errMsg);
                }

                // 回写商品id(wareId->numIId)
                mainProduct.getGroups().setNumIId(String.valueOf(jdWareId));
                // 更新者
                mainProduct.setModifier(UserId_ClassName);
                productGroupService.updateGroupsPlatformStatus(mainProduct.getGroups());

                // 上传商品主图
                List<String> mainPicNameList = new ArrayList<>();
                mainPicNameList.add("京东产品图片-2");  // TODO 暂时用固定值，以后改为从表里取
                mainPicNameList.add("京东产品图片-3");
                mainPicNameList.add("京东产品图片-4");
                mainPicNameList.add("京东产品图片-5");

                // 取得图片URL参数
//                String[] extParameter = {mainProduct.getFields().getCode()};   // 主产品code
                ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

                // 京东要求图片必须是5张，商品主图的第一张已经在前面的共通属性里面设置了，这里最多只需要设置4张非主图
                for (String picName : mainPicNameList) {
                    String picUrl = "";
                    try {
                        // 取得图片URL
                        picUrl = sxProductService.resolveDict(picName, expressionParser, shopProp, UserId_ClassName, null);
                        // 上传主产品的其余4张非主图片
                        jdWareService.addWarePropimg(shopProp, String.valueOf(jdWareId), ColorId_MinPic, picUrl, false);
                    } catch (Exception ex) {
                        $error("京东上传主商品非主图失败！[WareId:%s] [ColorId:%s] [PicName:%s]", jdWareId, ColorId_MinPic, picName);
                        // 继续上传下一张图片
                    }
                }

                // 新增商品成功后，设置SKU属性 调用京东商品修改API
                JdProductBean updateProductBean = new JdProductBean();
                // 新增商品id
                updateProductBean.setWareId(String.valueOf(jdWareId));
                // 构造更新用商品bean，主要设置SKU相关属性
                updateProductBean = setJdProductSkuInfo(updateProductBean, sxData, cmsMtPlatformSkusList,
                        cmsBtSizeMapModelList, shopProp, productColorMap, skuLogicQtyMap);

                // 新增之后调用京东商品更新API
                // 调用京东商品更新API设置SKU信息的好处是可以一次更新SKU信息，不用再一个一个SKU去设置
                String modified = "";
                try {
                    modified = jdWareService.updateProduct(shopProp, updateProductBean);
                } catch (Exception ex) {
                    $error("新增商品之后调用京东商品更新API批量设置SKU信息失败! [WareId:%s]", jdWareId);
                    throw ex;
                }

                // 设置SKU信息是否成功判断
                if (!StringUtils.isEmpty(modified)) {
                    // 新增之后更新商品SKU信息成功
                    // 上传该商品下所有产品的图片
                    retStatus = uploadJdProductPics(shopProp, jdWareId, sxData, productColorMap, false);
                    if (!retStatus) {
                        $error("新增商品的产品图片设置失败! [WareId:%s]", jdWareId);
                    }
                } else {
                    // 新增之后更新商品SKU信息失败
                    // 删除商品
                    try {
                        // 参数：1.ware_id 2.trade_no(流水号：现在时刻)
                        jdWareService.deleteWare(shopProp, String.valueOf(jdWareId), Long.toString(new Date().getTime()));
                    } catch (Exception ex) {
                        $error("新增商品SKU信息设置失败之后，删除该新增商品失败! [WareId:%s]", jdWareId);
                        throw ex;
                    }

                    // 删除group表中的商品id(numIId)
                    mainProduct.getGroups().setNumIId("");
                    // 更新者
                    mainProduct.setModifier(UserId_ClassName);
                    productGroupService.updateGroupsPlatformStatus(mainProduct.getGroups());

                    // 失败状态设定
                    retStatus = false;
                }

            } else {
                // 更新商品的时候
                // 设置更新用商品beanSKU属性 (更新用商品bean，共通属性前面已经设置)
                jdProductBean = setJdProductSkuInfo(jdProductBean, sxData, cmsMtPlatformSkusList,
                        cmsBtSizeMapModelList, shopProp, productColorMap, skuLogicQtyMap);

                // 京东商品更新API返回的更新时间
                String retModified = "";
                // 调用京东商品更新API
                retModified = jdWareService.updateProduct(shopProp, jdProductBean);

                // 更新商品是否成功
                if (!StringUtils.isEmpty(retModified)) {
                     // 上传该商品下所有产品的图片
                    retStatus = uploadJdProductPics(shopProp, jdWareId, sxData, productColorMap, true);
                }
            }

            // 新增或者更新商品结束时，根据状态回写product表（成功1 失败2）
            if (retStatus) {
                // 回写workload表   (成功1)
                sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, WorkLoad_status_1, UserId_ClassName);
            } else {
                // 正常结束
                $error(String.format("京东单个商品新增或更新信息失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s]",
                        channelId, cartId, groupId, jdWareId));
                // 回写workload表   (失败2)
                sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, WorkLoad_status_2, UserId_ClassName);
            }
        } catch (Exception ex) {
            // 回写workload表   (失败2)
            // 正常结束
            $error(String.format("京东单个商品新增或更新信息失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s]",
                    channelId, cartId, groupId, jdWareId));
            sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, WorkLoad_status_2, UserId_ClassName);
            throw ex;
        }

        // 正常结束
        $info(String.format("京东单个商品新增或更新信息成功！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s]",
                channelId, cartId, groupId, jdWareId));
    }

    /**
     * 设置京东上新产品用共通属性
     * 不包含"sku属性","sku价格","sku库存","自定义属性值别名","SKU外部ID"的值
     *
     * @param sxData sxData 上新产品对象
     * @param platformCategoryId String     平台类目id
     * @param groupId long                  groupid
     * @param shopProp ShopBean             店铺信息
     * @param platformMappingData CmsMtPlatformMappingModel  主产品对应的平台Mapping数据
     * @param platformSchemaData CmsMtPlatformCategorySchemaModel  主产品类目对应的平台schema数据
     * @param skuLogicQtyMap Map<String, Integer>  SKU逻辑库存
     * @return JdProductBean 京东上新用bean
     * @throws BusinessException
     */
    private JdProductBean setJdProductCommonInfo(SxData sxData, String platformCategoryId,
                                                 long groupId, ShopBean shopProp,
                                                 CmsMtPlatformMappingModel platformMappingData,
                                                 CmsMtPlatformCategorySchemaModel platformSchemaData,
                                                 Map<String, Integer> skuLogicQtyMap) throws BusinessException {
        CmsBtProductModel mainProduct = sxData.getMainProduct();
        List<CmsBtProductModel> productList = sxData.getProductList();
        List<CmsBtProductModel_Sku> skuList = sxData.getSkuList();

        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 渠道id
        String channelId = shopProp.getOrder_channel_id();
        // 平台id
        String cartId = shopProp.getCart_id();

        // 京东上新产品共通属性设定
        JdProductBean jdProductBean = new JdProductBean();

        // 调用共通函数取得商品属性列表，用户自行输入的类目属性ID和用户自行输入的属性值Map
        Map<String, String> jdProductAttrMap = getJdProductAttributes(platformMappingData, platformSchemaData, shopProp, expressionParser, UserId_ClassName);

        // 流水号(非必须)
//        jdProductBean.setTradeNo(mainProduct.getXXX());                  // 不使用
        // 产地(非必须)
        jdProductBean.setWareLocation(mainProduct.getFields().getOrigin());
        // 类目id(必须)
        jdProductBean.setCid(platformCategoryId);
        // 自定义店内分类(非必须)(123-111;122-222)
        try {
            String shopCagetory = this.getShopCategory(shopProp, sxData);
            jdProductBean.setShopCategory(shopCagetory);
        } catch (Exception ex) {
            String errMsg = String.format("京东取得自定义店内分类失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                    channelId, cartId, groupId, platformCategoryId);
            $error(errMsg);
        }
        // 商品标题(必须)
        jdProductBean.setTitle(mainProduct.getFields().getLongTitle());
        // UPC编码(非必须)
//        jdProductBean.setUpcCode(mainProduct.getXXX());                 // 不使用
        // 操作类型 现只支持：offsale 或onsale,默认为下架状态 (非必须)
        jdProductBean.setOptionType(this.getOptionType(mainProduct, groupId));
        // 外部商品编号，对应商家后台货号(非必须)
//        jdProductBean.setItemNum(mainProduct.getFields().getCode());    // 不使用
        // 库存(必须)
        // 计算该产品所有SKU的逻辑库存之和
        int skuTotalLogicQty = 0;
        for(String skuCode : skuLogicQtyMap.keySet()) {
            skuTotalLogicQty += skuLogicQtyMap.get(skuCode);
        }
        jdProductBean.setStockNum(String.valueOf(skuTotalLogicQty));
        // 生产厂商(非必须)
//        jdProductBean.setProducter(mainProduct.getXXX());                // 不使用
        // 包装规格 (非必须)
//        jdProductBean.setWrap(mainProduct.getXXX());                     // 不使用
        // 长(单位:mm)(必须)
        jdProductBean.setLength("50");
        // 宽(单位:mm)(必须)
        jdProductBean.setWide("50");
        // 高(单位:mm)(必须)
        jdProductBean.setHigh("50");
        // 重量(单位:kg)(必须)
        jdProductBean.setWeight("1");
//        // 进货价,精确到2位小数，单位:元(非必须)
//        jdProductBean.setCostPrice(mainProduct.getXXX());                 // 不使用
//        // 市场价, 精确到2位小数，单位:元(必须)
        Double marketPrice = getItemPrice(productList, channelId, cartId, PriceType_marketprice);
        jdProductBean.setMarketPrice(String.valueOf(marketPrice));
//        // 京东价,精确到2位小数，单位:元(必须)
        Double jdPrice = getItemPrice(productList, channelId, cartId, PriceType_jdprice);
        jdProductBean.setJdPrice(String.valueOf(jdPrice));

        // 描述（最多支持3万个英文字符(必须)
        String strNotes = "详情页描述";
        try {
            // 取得描述
            strNotes = sxProductService.resolveDict("京东详情页描述", expressionParser, shopProp, UserId_ClassName, null);
            jdProductBean.setNotes(strNotes);
        } catch (Exception ex) {
            String errMsg = String.format("京东取得详情页描述信息失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                    channelId, cartId, groupId, platformCategoryId);
            $error(errMsg);
        }
        jdProductBean.setNotes(strNotes);

        // 图片信息（图片尺寸为800*800，单张大小不超过 1024K）(必须)
        // 产品5张图片名称列表(TODO 暂时为固定值，以后会改成从表中读取)
        List<String> mainPicNameList = new ArrayList<>();
        mainPicNameList.add("京东产品图片-1");
        mainPicNameList.add("京东产品图片-2");
        mainPicNameList.add("京东产品图片-3");
        mainPicNameList.add("京东产品图片-4");
        mainPicNameList.add("京东产品图片-5");
        byte[] bytes = null;

        // 循环取得5张图片的url并分别上传到京东
        for (String picName : mainPicNameList) {
            String picUrl = "";
            try {
                // 取得图片url
                picUrl = sxProductService.resolveDict(picName, expressionParser, shopProp, UserId_ClassName, null);
                // 读取图片
                InputStream inputStream = jdWareService.getImgInputStream(picUrl, 3);
                bytes = IOUtils.toByteArray(inputStream);
                // 取得图片就推出循环
                break;
            } catch (Exception ex) {
                String errMsg = String.format("京东取得商品主图信息失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                        channelId, cartId, groupId, platformCategoryId);
                $error(errMsg);
                // 继续取下一张图片
            }
        }
        // 图片信息（图片尺寸为800*800，单张大小不超过 1024K）(必须)
        jdProductBean.setWareImage(bytes);

        // 包装清单(非必须)
//        jdProductBean.setPackListing(mainProduct.getXXX());               // 不使用
        // 售后服务(非必须)
//        jdProductBean.setService(mainProduct.getXXX());                   // 不使用
        // 商品属性列表,多组之间用|分隔，格式:aid:vid 或 aid:vid|aid1:vid1 或 aid1:vid1(必须)
        // 如输入类型input_type为1或2，则attributes为必填属性；如输入类型input_type为3，则用字段input_str填入属性的值
        jdProductBean.setAttributes(jdProductAttrMap.get(Attrivutes));
        // 是否先款后货,false为否，true为是 (非必须)
        jdProductBean.setPayFirst("true");
        // 发票限制：非必须输入，true为限制，false为不限制开增值税发票，FBP、LBP、SOPL、SOP类型商品均可输入(非必须)
        jdProductBean.setCanVAT("true");
        // 是否进口商品：非必须输入，false为否，true为是，FBP类型商品可输入(非必须)
//        jdProductBean.setImported(mainProduct.getXXX());                   // 不使用
        // 是否保健品：非必须输入，false为否，true为是，FBP类型商品可输入  (非必须)
//        jdProductBean.setHealthProduct(mainProduct.getXXX());              // 不使用
        // 是否保质期管理商品, false为否，true为是(非必须)
//        jdProductBean.setShelfLife(mainProduct.getXXX());                  // 不使用
        // 保质期：非必须输入，0-99999范围区间，FBP类型商品可输入(非必须)
//        jdProductBean.setShelfLifeDays(mainProduct.getXXX());              // 不使用
        // 是否序列号管理：非必须输入，false为否，true为是，FBP类型商品可输入(非必须)
//        jdProductBean.setSerialNo(mainProduct.getXXX());                   // 不使用
        // 大家电购物卡：非必须输入，false为否，true为是，FBP类型商品可输入  (非必须)
//        jdProductBean.setAppliancesCard(mainProduct.getXXX());             // 不使用
        // 是否特殊液体：非必须输入，false为否，true为是，FBP、LBP、SOPL类型商品可输入(非必须)
//        jdProductBean.setSpecialWet(mainProduct.getXXX());                // 不使用
        // 商品件型：FBP类型商品必须输入，非FBP类型商品可输入非必填，0免费、1超大件、2超大件半件、3大件、4大件半件、5中件、6中件半件、7小件、8超小件(必须)
//        jdProductBean.setWareBigSmallModel(mainProduct.getXXX());         // 不使用
        // 商品包装：FBP类型商品必须输入，非FBP类型商品可输入非必填，1普通商品、2易碎品、3裸瓶液体、4带包装液体、5按原包装出库(必须)
//        jdProductBean.setWarePackType(mainProduct.getXXX());              // 不使用
        // 用户自行输入的类目属性ID串结构：‘pid1|pid2|pid3’,属性的pid调用360buy.ware.get.attribute取得, 输入类型input_type=3即输入(非必须)
        jdProductBean.setInputPids(jdProductAttrMap.get(Input_Pids));
        // 用户自行输入的属性值,结构:‘输入值|输入值2|输入值3’图书品类输入值规则： ISBN：数字、字母格式 出版时间：日期格式“yyyy-mm-dd”
        // 版次：数字格式 印刷时间：日期格式“yyyy-mm-dd” 印次：数字格式 页数：数字格式 字数：数字格式 套装数量：数字格式 附件数量：数字格式(非必须)
        jdProductBean.setInputStrs(jdProductAttrMap.get(Input_Strs));
        // 是否输入验证码 true:是;false:否  (非必须)
        jdProductBean.setHasCheckCode("false");
        // 广告词内容最大支持45个字符(非必须)
//        jdProductBean.setAdContent(mainProduct.getXXX());                  // 不使用
        // 定时上架时间 时间格式：yyyy-MM-dd HH:mm:ss;规则是大于当前时间，10天内。(非必须)
//        jdProductBean.setListTime(mainProduct.getXXX());                   // 不使用

        return jdProductBean;
    }

    /**
     * 取得京东商品属性值
     *
     * @param platformMappingData CmsMtPlatformMappingModel  主产品对应的平台Mapping数据
     * @param platformSchemaData CmsMtPlatformCategorySchemaModel  主产品类目对应的平台schema数据
     * @return Map<String, String   Map(包含商品属性列表，用户自行输入的类目属性ID，用户自行输入的类目属性值)
     */
    private Map<String, String> getJdProductAttributes(CmsMtPlatformMappingModel platformMappingData, CmsMtPlatformCategorySchemaModel platformSchemaData,
                                           ShopBean shopBean, ExpressionParser expressionParser, String user) {
        Map<String, String> retAttrMap = new HashMap<>();

        // 取得schema数据中的propsItem(XML字符串)
        String propsItem = platformSchemaData.getPropsItem();
        List<Field> itemFieldList =null;
        if (!StringUtils.isEmpty(propsItem)) {
            // 将取出的propsItem转换为字段列表
            itemFieldList = SchemaReader.readXmlForList(propsItem);
        }

        // 根据field列表取得属性值mapping数据
        Map<String, Field> attrMap = null;

        try {
            // 取得所有field对应的属性值
            attrMap = sxProductService.constructMappingPlatformProps(itemFieldList, platformMappingData, shopBean, expressionParser, user);
        } catch (Exception e) {
            String errMsg = String.format("取得京东商品属性值失败！[ChannelId:%s] [CartId:%s] [PlatformCategoryId:%s]",
                    shopBean.getOrder_channel_id(), shopBean.getCart_id(), platformMappingData.getPlatformCartId());
            $error(errMsg);
        }

        // 商品属性列表,多组之间用|分隔，格式:aid:vid 或 aid:vid|aid1:vid1 或 aid1:vid1（需要从类目服务接口获取）
        // 如输入类型input_type为1或2，则attributes为必填属性；如输入类型input_type为3，则用字段input_str填入属性的值 
        StringBuffer sbAttributes = new StringBuffer();
        // 用户自行输入的类目属性ID串结构：‘pid1|pid2|pid3’,属性的pid调用360buy.ware.get.attribute取得, 输入类型input_type=3即输入 
        StringBuffer sbInputPids = new StringBuffer();
        // 用户自行输入的属性值,结构:‘输入值|输入值2|输入值3’
        // 图书品类输入值规则：ISBN：数字、字母格式 出版时间：日期格式“yyyy-mm-dd” 版次：数字格式 印刷时间：日期格式“yyyy-mm-dd” 印次：数字格式 页数：数字格式 字数：数字格式 套装数量：数字格式 附件数量：数字格式 
        StringBuffer sbInputStrs = new StringBuffer();

        // 如果list为空说明没有mappingg过，不用设置
        if (attrMap != null && attrMap.size() > 0) {

            // 遍历fieldList
            for (Field field : itemFieldList) {
                // 属性值id(field_id)
                String fieldId = field.getId();

                // 从属性值map里面取得当前fieldId对应的Field
                Field fieldValue = attrMap.get(fieldId);
                if (fieldValue == null) {
                    $info("没找到该fieldId对应的属性值！ [FieldId:%s]", fieldId);
                    continue;
                }

                // 根据输入类型分别设置3个属性值
                switch (fieldValue.getType()) {
                    case SINGLECHECK: {
                        // 输入类型input_type为1(单选)的时候,设置【商品属性列表】
                        // 设置商品属性列表,多组之间用|分隔，格式:aid:vid 或 aid:vid|aid1:vid1
                        sbAttributes.append(fieldId);                          // 属性id(fieldId)
                        sbAttributes.append(Separtor_Colon);                   // ":"
                        // 属性值设置(如果有多个，则用逗号分隔 "属性值1，属性值2，属性值3")
                        sbAttributes.append(fieldValue.getValue().toString()); // 属性值id
                        sbAttributes.append(Separtor_Vertical);                // "|"
                        break;
                    }
                    case MULTICHECK: {
                        // 多选的时候，属性值多个，则用逗号分隔 "属性值1，属性值2，属性值3")
                        StringBuffer sbMultiAttrValue = new StringBuffer();
                        List<Value> valueList = ((MultiCheckField) fieldValue).getValues();
                        if (valueList != null && valueList.size() > 0) {
                            for (int i = 0; i < valueList.size(); i++) {
                                // 属性值id(fieldId对应的属性值id)
                                sbMultiAttrValue.append(valueList.get(i));
                                // 如果不是属性值列表中的最后一个则加上逗号(末尾不加逗号)
                                if (i < valueList.size() - 1) {
                                    sbMultiAttrValue.append(Separtor_Coma);   // ","
                                }
                            }
                        }

                        // 输入类型input_type为2(多选)的时候,设置【商品属性列表】
                        // 设置商品属性列表,多组之间用|分隔，格式:aid:vid 或 aid:vid|aid1:vid1
                        sbAttributes.append(fieldId);                          // 属性id(fieldId)
                        sbAttributes.append(Separtor_Colon);                   // ":"
                        // 属性值设置(如果有多个，则用逗号分隔 "属性值1，属性值2，属性值3")
                        sbAttributes.append(sbMultiAttrValue.toString());      // 属性值1,属性值2，属性值3
                        sbAttributes.append(Separtor_Vertical);                // "|"
                        break;
                    }
                    case INPUT: {
                        // 输入类型input_type为3(可输入)的时候,设置【用户自行输入的类目属性ID串】和【用户自行输入的属性值】
                        // 设置用户自行输入的类目属性ID串结构：‘pid1|pid2|pid3’
                        sbInputPids.append(fieldId);                         // 属性id(fieldId)
                        sbInputPids.append(Separtor_Vertical);               // "|"

                        // 设置用户自行输入的属性值,结构:‘输入值|输入值2|输入值3’
                        sbInputStrs.append(fieldValue.getValue().toString()); // 属性值id
                        sbInputStrs.append(Separtor_Vertical);                // "|"
                        break;
                    }
                    default:
                        $error("复杂类型[" + field.getType() + "]不能作为属性值来使用！");
                }
            }
        }

        // 移除用户自行输入的类目属性ID最后的"|"
        if (sbAttributes.length() > 0) {
            sbAttributes.deleteCharAt(sbAttributes.length() - 1);
        }

        // 移除用户自行输入的类目属性ID最后的"|"
        if (sbInputPids.length() > 0) {
            sbInputPids.deleteCharAt(sbInputPids.length() - 1);
        }

        // 移除用户自行输入的属性值最后的"|"
        if (sbInputStrs.length() > 0) {
            sbInputStrs.deleteCharAt(sbInputStrs.length() - 1);
        }

        // 把取得的字符串值放进返回map中
        retAttrMap.put(Attrivutes, sbAttributes.toString());
        retAttrMap.put(Input_Pids, sbInputPids.toString());
        retAttrMap.put(Input_Strs, sbInputStrs.toString());

        return retAttrMap;
    }

    /**
     * 设置京东上新产品SKU属性
     * 更新产品时用，传入设置好共通属性的JdProductBean
     * 新增商品之后设置SKU属性时，传入一个全新的更新用JdProductBean(new一个全新的更新用bean)
     *
     * @param targetProductBean JdProductBean   产品对象
     * @param sxData SxData     产品对象
     * @param cmsMtPlatformSkusList List<CmsMtPlatformSkusModel>  SKU颜色和尺寸列表
     * @param cmsBtSizeMapModelList List<CmsBtSizeMapModel> 尺码对照表
     * @param shop ShopBean 店铺信息
     * @param productColorMap Map<String, Object> 产品和颜色值Mapping关系表
     * @param skuLogicQtyMap Map<String, Integer> 所有SKU的逻辑库存列表
     * @return JdProductBean 京东上新用bean
     * @throws BusinessException
     */
    private JdProductBean setJdProductSkuInfo(JdProductBean targetProductBean, SxData sxData, List<CmsMtPlatformSkusModel> cmsMtPlatformSkusList,
                                              List<CmsBtSizeMapModel> cmsBtSizeMapModelList, ShopBean shop, Map<String, Object> productColorMap,
                                              Map<String, Integer> skuLogicQtyMap) throws BusinessException {
        CmsBtProductModel mainProduct = sxData.getMainProduct();
        List<CmsBtProductModel> productList = sxData.getProductList();
        List<CmsBtProductModel_Sku> skuList = sxData.getSkuList();

        // 取得cms_mt_platform_skus表里平台类目id对应的颜色信息列表
        List<CmsMtPlatformSkusModel> cmsColorList = new ArrayList<>();
        for (CmsMtPlatformSkusModel skuModel : cmsMtPlatformSkusList) {
            // 颜色
            if (AttrType_Color.equals(skuModel.getAttrType())) {
                cmsColorList.add(skuModel);
            }
        }
        // 取得cms_mt_platform_skus表里平台类目id对应的尺寸信息列表
        List<CmsMtPlatformSkusModel> cmsSizeList = new ArrayList<>();
        for (CmsMtPlatformSkusModel skuModel : cmsMtPlatformSkusList) {
            // 尺寸
            if (AttrType_Size.equals(skuModel.getAttrType())) {
                cmsSizeList.add(skuModel);
            }
        }

        // 产品和颜色的Mapping表(因为后面上传SKU图片的时候也要用到，所以从外面传进来)
        // SKU尺寸和尺寸值的Mapping表
        Map<String, Object> skuSizeMap = new HashMap<String, Object>();

        // 根据product列表取得要上新的产品颜色和尺寸Mapping关系
        for (CmsBtProductModel product : productList) {
            // 取得颜色值列表中的第一个颜色值
            if (cmsColorList.size() > 0) {
                // "产品code":"颜色值Id" Mapping追加
                productColorMap.put(product.getFields().getCode(), cmsColorList.get(0).getAttrValue());
                // 已经Mapping过的颜色值从颜色列表中删除
                cmsColorList.remove(0);
            } else {
                $warn("商品件数比cms_mt_platform_skus表中颜色值件数多，该商品未找到对应的颜色值！[ProductCode:%s]", product.getFields().getCode());
            }

            // 取得当前商品中每个SKU的size对应的sizeValue
            List<CmsBtProductModel_Sku> productSkuList = product.getSkus();
            for (CmsBtProductModel_Sku sku:productSkuList) {
                // SKU和尺寸的Mapping表中不存在的话，追加进去(已存在不要再追加)
                if (!skuSizeMap.containsKey(sku.getSize())) {
                    // 取得尺寸列表中的第一个尺寸值
                    if (cmsSizeList.size() > 0) {
                        // "SKU尺寸(3,3.5等)":"尺寸值Id" Mapping追加
                        skuSizeMap.put(sku.getSize(), cmsSizeList.get(0).getAttrValue());
                        // 已经Mapping过的尺寸值从尺寸列表中删除
                        cmsSizeList.remove(0);
                    } else {
                        $warn("SKU尺寸件数比cms_mt_platform_skus表中尺寸值件数多，该尺寸未找到对应的尺寸值！[Size:%s]", sku.getSize());
                    }
                }
            }
        }

        // 调用共通函数从cms_mt_platform_skus表中取得每个类目对应的颜色和尺寸信息
        // 并与产品SKU价格，库存，外部id等mapping起来
        // sku属性,一组sku 属性之间用^分隔，多组用|分隔格式(非必须)
        StringBuffer sbSkuProperties = new StringBuffer();
        // sku价格,多组之间用‘|’分隔，格式:p1|p2 (非必须)
        StringBuffer sbSkuPrice = new StringBuffer();
        // sku 库存,多组之间用‘|’分隔， 格式:s1|s2(非必须)
        StringBuffer sbSkuStocks = new StringBuffer();
        // 自定义属性值别名：属性ID:属性值ID:别名(非必须)
        StringBuffer sbPropertyAlias = new StringBuffer();
        // SKU外部ID,多组之间用‘|’分隔， 格式:s1|s2(非必须)
        StringBuffer sbSkuOuterId = new StringBuffer();

        // 根据product列表循环设置该商品的SKU属性
        for (CmsBtProductModel objProduct : productList) {
            // 设置该商品的自定义属性值别名(颜色1:颜色1的别名^颜色2:颜色2的别名)
            sbPropertyAlias.append(productColorMap.get(objProduct.getFields().getCode())); // 产品CODE对应的颜色值ID
            sbPropertyAlias.append(Separtor_Colon);         // ":"
            sbPropertyAlias.append(objProduct.getFields().getCode());
            sbPropertyAlias.append(Separtor_Xor);           // "^"

            List<CmsBtProductModel_Sku> objProductSkuList = objProduct.getSkus();
            for (CmsBtProductModel_Sku objSku:objProductSkuList) {
                // sku属性(1000021641:1523005913^1000021641:1523005771|1000021641:1523005913^1000021641:1523005772)
                // 颜色1^尺码1|颜色1^尺码2|颜色2^尺码1|颜色2^尺码2
                sbSkuProperties.append(productColorMap.get(objProduct.getFields().getCode()));
                sbSkuProperties.append(Separtor_Xor);        // "^"
                sbSkuProperties.append(skuSizeMap.get(objSku.getSize()));
                sbSkuProperties.append(Separtor_Vertical);   // "|"

                // sku价格(100.0|150.0|100.0|100.0)
                Double skuPrice = getSkuPrice(objSku, shop.getOrder_channel_id(), shop.getCart_id(), PriceType_jdprice);
                sbSkuPrice.append(String.valueOf(skuPrice));
                sbSkuPrice.append(Separtor_Vertical);        // "|"

                // sku 库存(100.0|150.0|100.0|100.0)
                sbSkuStocks.append(skuLogicQtyMap.get(objSku.getSkuCode()));
                sbSkuStocks.append(Separtor_Vertical);   // "|"

                // SKU外部ID(200001-001-41|200001-001-42)
                sbSkuOuterId.append(objSku.getSkuCode());
                sbSkuOuterId.append(Separtor_Vertical);   // "|"
            }
        }

        // 根据SKU尺寸和尺寸值的Mapping表循环设置该商品的自定义属性值别名
        for (Map.Entry<String, Object> entry : skuSizeMap.entrySet()) {
            // 设置该商品的自定义属性值别名(尺码1:尺码1的别名^尺码2:尺码2的别名)
            sbPropertyAlias.append(entry.getValue());         // 尺寸值
            sbPropertyAlias.append(Separtor_Colon);           // ":"
            // 尺码别名到尺码表转换一下(参数为尺码对照表id,转换前size)
            // TODO: 暂时不做尺码转换， 直接使用原始的size，预计五月中旬再改
//            String size = sxProductService.changeSize(cmsBtSizeMapModelList, entry.getKey());
            String size = entry.getKey();
            sbPropertyAlias.append(size == null ? "" : size); // 尺寸值别名(转换后尺码)
            sbPropertyAlias.append(Separtor_Xor);             // "^"
        }

        // 移除sku属性最后的"|"
        if (sbSkuProperties.length() > 0) {
            sbSkuProperties.deleteCharAt(sbSkuProperties.length() - 1);
        }

        // 移除sku价格最后的"|"
        if (sbSkuPrice.length() > 0) {
            sbSkuPrice.deleteCharAt(sbSkuPrice.length() - 1);
        }

        // 移除sku库存最后的"|"
        if (sbSkuStocks.length() > 0) {
            sbSkuStocks.deleteCharAt(sbSkuStocks.length() - 1);
        }

        // 移除自定义属性值别名最后的"^"
        if (sbPropertyAlias.length() > 0) {
            sbPropertyAlias.deleteCharAt(sbPropertyAlias.length() - 1);
        }

        // 移除SKU外部ID最后的"|"
        if (sbSkuOuterId.length() > 0) {
            sbSkuOuterId.deleteCharAt(sbSkuOuterId.length() - 1);
        }

        // sku属性,一组sku 属性之间用^分隔，多组用|分隔格式(非必须)
        targetProductBean.setSkuProperties(sbSkuProperties.toString());
        // sku价格,多组之间用‘|’分隔，格式:p1|p2 (非必须)
        targetProductBean.setSkuPrices(sbSkuPrice.toString());
        // sku 库存,多组之间用‘|’分隔， 格式:s1|s2(非必须)
        targetProductBean.setSkuStocks(sbSkuStocks.toString());
        // 自定义属性值别名：属性ID:属性值ID:别名(非必须)
        targetProductBean.setPropertyAlias(sbPropertyAlias.toString());
        // SKU外部ID，对个之间用‘|’分隔格(非必须)
        targetProductBean.setOuterId(sbSkuOuterId.toString());

        return targetProductBean;
    }

    /**
     * 设置该商品每个产品的图片
     * 遍历当前group里的所有的product，每个product上传5张图片
     * 检索到当前商品ware里所有图片，先全部删掉，再一张一张增加
     * 第一张图设true， 其他的都设置为false（如果不满5张，必须使用一张图片作为补充）
     *
     * @param wareId long 商品id
     * @param sxData SxData 产品对象
     * @param productColorMap Map<String, Object> 产品和颜色值Mapping关系表
     * @param updateFlg boolean 是否更新商品标志
     * @return boolean 上传指定商品SKU图片是否成功
     */
    private boolean uploadJdProductPics(ShopBean shopProp, long wareId, SxData sxData,
                                           Map<String, Object> productColorMap, boolean updateFlg) {
        boolean retUploadPics = true;

        CmsBtProductModel mainProduct = sxData.getMainProduct();
        List<CmsBtProductModel> productList = sxData.getProductList();
        List<CmsBtProductModel_Sku> skuList = sxData.getSkuList();

        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 产品5张图片名称列表(TODO 暂时为固定值，以后会改成从表中读取)
        List<String> productPicNameList = new ArrayList<>();
        productPicNameList.add("京东产品图片-1");
        productPicNameList.add("京东产品图片-2");
        productPicNameList.add("京东产品图片-3");
        productPicNameList.add("京东产品图片-4");
        productPicNameList.add("京东产品图片-5");

        // 检索     调用API【根据商品Id，检索商品图片】(更新时)
        // 删除图片  调用API【删除商品图片】(更新时)
        // 增加图片  调用API【根据商品Id，销售属性值Id增加图片】(新增或更新时)

        // 更新商品的时候需要事先删除京东平台上已有的SKU图片，新增的时候不需要
        if (updateFlg) {
            // 根据商品Id，检索商品图片列表
            // 检索 调用API【根据商品Id，检索商品图片】
            // 检索指定商品Id对应的商品图片列表（包含颜色值Id为0000000000）
            List<Image> wareIdPics = new ArrayList<>();
            try {
                // 调用京东API根据商品Id，检索商品图片
                wareIdPics = jdWareService.getImagesByWareId(shopProp, wareId);
            } catch (Exception ex) {
                $error("京东根据商品Id检索商品图片列表失败！ [WareId:%s]", wareId);
                return false;
            }

            // 删除图片 调用API【删除商品图片】批量删除该商品全部SKU图片，不删主图（颜色值Id0000000000）
            // 删除商品图片的接口，支持批量 颜色和排序的数组长度要一致 删除时按数组对应的坐标删除

            // 取出商品图片列表中的要删除颜色值Id列表(去掉重复，去掉主图颜色值Id(ID0000000000))
            List<String> picColorIds = new ArrayList<>();
            for (Image img : wareIdPics) {
                // 过滤掉主图的颜色值Id0000000000
                if (ColorId_MinPic.equals(img.getColorId())) {
                    continue;
                }

                // 如果要删除颜色值Id列表中不存在的时候，追加
                if (!picColorIds.contains(img.getColorId())) {
                    picColorIds.add(img.getColorId());
                }
            }

            // 根据要删除的列表及图片列表作成删除用的颜色id数组和图片位置数组("123,234,345")
            StringBuffer sbDelColorIds = new StringBuffer();
            StringBuffer sbDelImgIndexes = new StringBuffer();
            for (String strColorId : picColorIds) {
                // 颜色id数组("jingdong,yanfa,pop")
                sbDelColorIds.append(strColorId);
                sbDelColorIds.append(Separtor_Coma);   // ","

                // 图片位置数组("123,234,345")
                for (Image pic : wareIdPics) {
                    // 删除对象图片id与图片id一致的时候
                    if (strColorId.equals(pic.getColorId())) {
                        // 该颜色对应图片位置拼起来("123")
                        sbDelImgIndexes.append(pic.getImgIndex());
                    }
                }
                // 一种颜色对应图片位置最后的逗号("123,")
                sbDelImgIndexes.append(Separtor_Coma);   // ","
            }

            // 移除颜色id值数组最后的"，"
            if (sbDelColorIds.length() > 0) {
                sbDelColorIds.deleteCharAt(sbDelColorIds.length() - 1);
            }

            // 移除图片位置最后的"，"
            if (sbDelImgIndexes.length() > 0) {
                sbDelImgIndexes.deleteCharAt(sbDelImgIndexes.length() - 1);
            }

            // 调用API【删除商品图片】批量删除该商品全部SKU图片，不删主图（颜色值Id0000000000）
            retUploadPics = jdWareService.deleteImagesByWareId(shopProp, wareId, sbDelColorIds.toString(), sbDelImgIndexes.toString());

            // 删除商品图片失败
            if (!retUploadPics) {
                return false;
            }
        }

        // 增加图片  调用API【根据商品Id，销售属性值Id增加图片】
        // 每个颜色要传五张图，第一张图设为主图，其他的都设置为非主图
        for (CmsBtProductModel product:productList) {
            // 颜色值id取得
            // 新增或更新商品的时候，从产品和颜色值Mapping关系表中取得该产品对应的颜色值
            String colorId = "";
            if (!StringUtil.isEmpty(productColorMap.get(product.getFields().getCode()).toString())) {
                String[] colorIdArray = productColorMap.get(product.getFields().getCode()).toString().split(Separtor_Colon);
                if (colorIdArray != null && colorIdArray.length >= 2) {
                    // 产品颜色值Mapping关系表里面取得的颜色值为"1000021641:1523005913",取得后面的颜色值"1523005913"
                    colorId = colorIdArray[1];
                }
            }

            // 主图不上传
            if (ColorId_MinPic.equals(colorId)) {
                continue;
            }

            // 根据商品Id，销售属性值Id增加5张产品图片
            // SKU图片上传是否成功(只要5张图片有一张上传成功则认为成功)
            boolean uploadProductPicResult = false;
            // 取得图片URL参数
            String[] extParameter = {product.getFields().getCode()};   // 产品code

            // 循环取得5张图片的url并分别上传到京东
            for (String picName : productPicNameList) {
                String picUrl = "";
                try {
                    boolean skuPicResult = false;
                    // 取得图片URL
                    picUrl = sxProductService.resolveDict(picName, expressionParser, shopProp, UserId_ClassName, extParameter);

                    // 如果之前没有一张图片上传成功则本次上传对象图片设置为主图，如果之前已经有图片上传成功，则本次设为非主图
                    skuPicResult = jdWareService.addWarePropimg(shopProp, String.valueOf(wareId), colorId, picUrl, !uploadProductPicResult);

                    // 5张图片只有曾经有一张上传成功就认为SKU图片上传成功
                    uploadProductPicResult = uploadProductPicResult || skuPicResult;
                } catch (Exception ex) {
                    // 如果5张图片里面有一张上传成功的时候
                    if (uploadProductPicResult) {
                        $info("京东根据商品Id销售属性值Id上传产品主图成功，上传非主图图片失败！[WareId:%s] [ColorId:%s] [PicName:%s]", wareId, colorId, picName);
                    } else {
                        $info("京东根据商品Id销售属性值Id上传产品主图失败！[WareId:%s] [ColorId:%s] [PicName:%s]", wareId, colorId, picName);
                    }
                    // 即使5张图片中的某张上传出错，也继续循环上传后面的图片
                }
            }

            // 该产品5张图片全部上传失败的时候
            if (!uploadProductPicResult) {
                $error("京东根据商品Id销售属性值Id增加该产品图片失败！[WareId:%s] [ProductCode:%s]", wareId, product.getFields().getCode());
            } else {
                $info("京东根据商品Id销售属性值Id增加该产品图片成功！[WareId:%s] [ProductCode:%s]", wareId, product.getFields().getCode());
            }

            // 图片上传返回状态判断(该商品下所有产品的图片均上传成功时，才返回成功，否则返回失败)
            retUploadPics = retUploadPics && uploadProductPicResult;
        }

        return retUploadPics;
    }

    /**
     * 取得前台展示的商家自定义店内分类
     * 如果cms_bt_condition_prop_value表存在条件表达式，优先使用这个条件表达式；
     * 不存在的话，通过360buy.sellercats.get获取店铺分类的parent_id及cid，
     * 按“parent_id-cid"格式传入，同时设置多个以分号（;）分隔即可。
     * 店内分类，格式:206-208;207-208 206(一级)-208(二级);207(一级)-207(一级)
     *
     * @param shop ShopBean 店铺对象
     * @param sxData SxData 产品对象
     * @return String 前台展示的商家自定义店内分类
     */
    private String getShopCategory(ShopBean shop, SxData sxData) throws Exception {

        // 多个条件表达式用分号分隔用
        StringBuilder builder = new StringBuilder();
        // 条件表达式表platform_prop_id字段的检索条件为"seller_cids"加cartId
        String platformPropId = "seller_cids_" + shop.getCart_id();

        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 根据channelid和platformPropId取得cms_bt_condition_prop_value表的条件表达式
        List<ConditionPropValueModel> conditionPropValueModels = conditionPropValueRepo.get(shop.getOrder_channel_id(), platformPropId);

        // 优先使用条件表达式
        if (conditionPropValueModels != null && !conditionPropValueModels.isEmpty()) {
            RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
            for (ConditionPropValueModel conditionPropValueModel : conditionPropValueModels) {
                String conditionExpressionStr = conditionPropValueModel.getCondition_expression();
                RuleExpression conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
                String propValue = expressionParser.parse(conditionExpression, shop, UserId_ClassName, null);

                // 多个表达式(2392231-4345291格式)用分号分隔
                if (propValue != null) {
                    builder.append(propValue);
                    builder.append(Separtor_Semicolon);   // 用分号(";")分隔
                }
            }
        } else {
            // 获取京东平台前台展示的商家自定义店内分类
            List<ShopCategory> shopCategoryList = jdShopService.getShopCategoryList(shop);
            if (shopCategoryList != null && shopCategoryList.size() > 0) {
                for (ShopCategory shopCategory:shopCategoryList) {
                    // 如果不是父类目的话，加到店内分类里，用分号分隔
                    if (!shopCategory.getParent()) {
                        // 转换成“parent_id-cid"格式，同时设置多个以分号（;）分隔
                        builder.append(String.valueOf(shopCategory.getParentId()));
                        builder.append(Separtor_Hyphen);       // 用连字符("-")连接
                        builder.append(String.valueOf(shopCategory.getCid()));
                        builder.append(Separtor_Semicolon);    // 用分号(";")分隔
                    }
                }
            }
        }
        // 移除最后的分号
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }

        // 店铺种类
        return builder.toString();
    }

    /**
     * 取得操作类型
     * 现只支持：offsale 或onsale,默认为下架状态
     *
     * @param mainProduct CmsBtProductModel 主产品对象
     * @param groupId long groupid
     * @return String 前台操作类型（offsale或onsale）
     */
    private String getOptionType(CmsBtProductModel mainProduct, long groupId) {
        String retOptionType = "";
        CmsBtProductGroupModel mainProductPlatform = mainProduct.getGroups();
        com.voyageone.common.CmsConstants.PlatformActive platformActive = mainProductPlatform.getPlatformActive();

        if (platformActive == com.voyageone.common.CmsConstants.PlatformActive.ToInStock) {
            // 如果是Instock， 那么offsale
            retOptionType = OptioinType_offsale;
        } else if (platformActive == com.voyageone.common.CmsConstants.PlatformActive.ToOnSale) {
            // 如果是Onsale， 那么onsale
            retOptionType = OptioinType_onsale;
        }

        return retOptionType;
    }

    /**
     * 取得所有SKU价格的最高价格
     *
     * @param sxProducts List<CmsBtProductModel> 所有产品的状态对象
     * @param channelId String 渠道id
     * @param cartId String 平台id
     * @param priceType String 价格类型
     * @return double 所有产品全部SKU的最高价格
     */
    private double getItemPrice(List<CmsBtProductModel> sxProducts, String channelId, String cartId, String priceType) {
        // 价格有可能是用priceSale, 也有可能用priceMsrp, 所以需要判断一下
        // priceType:"retail_price"(市场价)  "sale_price"(京东价)
        CmsChannelConfigBean sxPriceConfig = CmsChannelConfigs.getConfigBean(channelId, com.voyageone.common.CmsConstants.ChannelConfig.PRICE, cartId + "." + priceType);

        // 检查一下
        String sxPricePropName;
        if (sxPriceConfig == null) {
            return 0d;
        } else {
            // 取得价格属性名
            sxPricePropName = sxPriceConfig.getConfigValue1();
            if (StringUtils.isEmpty(sxPricePropName)) {
                return 0d;
            }
        }

        Double resultPrice = 0d;    //, onePrice = 0d;
        List<Double> skuPriceList = new ArrayList<>();
        for (CmsBtProductModel cmsProduct : sxProducts) {
            for (CmsBtProductModel_Sku cmsBtProductModelSku : cmsProduct.getSkus()) {
                double skuPrice = 0;
                skuPrice = cmsBtProductModelSku.getDoubleAttribute(sxPricePropName);
                skuPriceList.add(skuPrice);
            }
        }

        // 取得所以skuPrice的最大值
        for (double skuPrice : skuPriceList) {
            resultPrice = Double.max(resultPrice, skuPrice);
        }

        return resultPrice;
    }

    /**
     * 取得指定SKU的价格
     *
     * @param cmsBtProductModelSku CmsBtProductModel_Sku SKU对象
     * @param channelId String 渠道id
     * @param cartId String 平台id
     * @param priceType String 价格类型 ("sale_price"(京东价))
     * @return double SKU价格
     */
    private double getSkuPrice(CmsBtProductModel_Sku cmsBtProductModelSku, String channelId, String cartId, String priceType) {
        // 价格有可能是用priceSale, 也有可能用priceMsrp, 所以需要判断一下
        // SKU价格类型应该用"sale_price"(京东价)
        CmsChannelConfigBean sxPriceConfig = CmsChannelConfigs.getConfigBean(channelId, com.voyageone.common.CmsConstants.ChannelConfig.PRICE, cartId + "." + priceType);

        // 检查一下
        String sxPricePropName;
        if (sxPriceConfig == null) {
            return 0d;
        } else {
            // 取得价格属性名
            sxPricePropName = sxPriceConfig.getConfigValue1();
            if (StringUtils.isEmpty(sxPricePropName)) {
                return 0d;
            }
        }

        Double resultPrice = 0d;
        // 取得该SKU的价格
        resultPrice = cmsBtProductModelSku.getDoubleAttribute(sxPricePropName);

        return resultPrice;
    }

}
