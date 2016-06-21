package com.voyageone.task2.cms.service;

import com.jd.open.api.sdk.domain.ware.ImageReadService.Image;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.field.MultiCheckField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.bean.JdProductBean;
import com.voyageone.components.jd.service.JdShopService;
import com.voyageone.components.jd.service.JdWareService;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.CmsMtPlatformSkusService;
import com.voyageone.service.impl.cms.DictService;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.CmsMtPlatformDictModel;
import com.voyageone.service.model.cms.CmsMtPlatformSkusModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
import com.voyageone.task2.cms.service.putaway.ConditionPropValueRepo;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 京东平台产品上新服务
 * Product表中产品不存在就向京东平台新增商品，否则就更新商品
 *
 * @author desmond on 2016/4/12.
 * @version 2.1.0
 * @since 2.0.0
 */
@Service
public class CmsBuildPlatformProductUploadJdService extends BaseTaskService {

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
    // 前台展示的商家自定义店内分类
    private final static String Prop_ShopCategory = "seller_cids_";
    // 京东运费模板
    private final static String Prop_TransportId = "transportid_";
    // 京东关联板式
    private final static String Prop_CommonHtmlId = "commonhtml_id_";
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
    // 商品主图颜色值Id(0000000000)
    private final static String ColorId_MinPic = "0000000000";

    @Autowired
    private DictService dictService;
    @Autowired
    private PlatformProductUploadService platformProductUploadService;
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

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformProductUploadJdJob";
    }

    /**
     * 京东平台上新处理
     *
     * @param taskControlList taskcontrol信息
     */
    @Override
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 获取该任务可以运行的销售渠道
        List<String> channelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 初始化cms_mt_channel_condition_config表的条件表达式(避免多线程时2次初始化)
        conditionPropValueRepo.init();

        // 循环所有销售渠道
        if (ListUtils.notNull(channelIdList)) {
            for (String channelId : channelIdList) {
                // TODO 虽然workload表里不想上新的渠道，不会有数据，这里的循环稍微有点效率问题，后面再改
                // 京东平台商品信息新增或更新(京东)
//                doProductUpload(channelId, CartEnums.Cart.JD.getValue());
                // 京东国际商品信息新增或更新(京东国际)
                doProductUpload(channelId, CartEnums.Cart.JG.getValue());
                // 京东平台商品信息新增或更新(京东国际 匠心界)
                doProductUpload(channelId, CartEnums.Cart.JGJ.getValue());
                // 京东国际商品信息新增或更新(京东国际 悦境)
                doProductUpload(channelId, CartEnums.Cart.JGY.getValue());
            }
        }

        // 正常结束
        $info("主线程正常结束");
    }

    /**
     * 平台产品上新主处理
     *
     * @param channelId String 渠道ID
     * @param cartId String 平台ID
     */
    private void doProductUpload(String channelId, int cartId) throws Exception {

        // 默认线程池最大线程数
        int threadPoolCnt = 5;

        // 获取店铺信息
        ShopBean shopProp = Shops.getShop(channelId, cartId);
        if (shopProp == null) {
            $error("获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }
        $info("获取店铺信息成功![ChannelId:%s] [CartId:%s]", channelId, cartId);

        // 从上新的任务表中获取该平台及渠道需要上新的任务列表(group by channel_id, cart_id, group_id)
        List<CmsBtSxWorkloadModel> sxWorkloadModels = platformProductUploadService.getSxWorkloadWithChannelIdCartId(
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
            executor.execute(() -> uploadProduct(cmsBtSxWorkloadModel, shopProp));
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
    }

    /**
     * 平台产品上新处理
     *
     * @param cmsBtSxWorkloadModel CmsBtSxWorkloadModel WorkLoad信息
     * @param shopProp ShopBean 店铺信息
     */
    private void uploadProduct(CmsBtSxWorkloadModel cmsBtSxWorkloadModel, ShopBean shopProp) {

        // 当前groupid(用于取得产品信息)
        long groupId = cmsBtSxWorkloadModel.getGroupId();
        // 渠道id
        String channelId = shopProp.getOrder_channel_id();
        // 平台id
        int cartId = Integer.parseInt(shopProp.getCart_id());
        // 商品id
        long jdWareId = 0;
        // 上新数据
        SxData sxData = null;
        // 新增或更新商品标志
        boolean updateWare = false;

        try {
            // 上新用的商品数据信息取得
            sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);
            if (sxData == null) {
                String errMsg = String.format("取得上新用的商品数据信息失败！[ChannelId:%s] [GroupId:%s] [sxData:null]", channelId, groupId);
                $error(errMsg);
                // 回写详细错误信息表(cms_bt_business_log)用
                sxData = new SxData();
                sxData.setChannelId(channelId);
                sxData.setCartId(cartId);
                sxData.setGroupId(groupId);
                sxData.setErrorMessage(errMsg);
                throw new BusinessException(shopProp.getShop_name() + "取得上新用的商品数据信息失败！请向管理员确认 [sxData=null]");
            }
            // 如果取得上新对象商品信息出错时，报错
            if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
                // 有错误的时候，直接报错
                throw new BusinessException(sxData.getErrorMessage());
            }
            // 单个product内部的sku列表分别进行排序
            for (CmsBtProductModel cmsBtProductModel : sxData.getProductList()) {
                sxProductService.sortSkuInfo(cmsBtProductModel.getSkus());
            }
            // 主产品等列表取得
            CmsBtProductModel mainProduct = sxData.getMainProduct();
            List<CmsBtProductModel> cmsBtProductList = sxData.getProductList();
            // modified by morse.lu 2016/06/13 start
//            List<CmsBtProductModel_Sku> skuList = sxData.getSkuList();
            List<BaseMongoMap<String, Object>> skuList = sxData.getSkuList();
            // modified by morse.lu 2016/06/13 end

            // 没有lock并且已Approved的产品列表为空的时候,中止该产品的上新流程
            if (ListUtils.isNull(cmsBtProductList)) {
                String errMsg = String.format("未lock并且已Approved产品列表为空");
                $error(errMsg);
                sxData.setErrorMessage(errMsg);
                throw new BusinessException(errMsg);
            }

            // 主产品取得结果判断
            if (mainProduct == null) {
                String errMsg = String.format("取得主商品信息失败");
                $error(errMsg);
                sxData.setErrorMessage(errMsg);
                throw new BusinessException(errMsg);
            }

            // 构造该产品所有SKUCODE的字符串列表
            List<String> strSkuCodeList = new ArrayList<>();
            // modified by morse.lu 2016/06/13 start
//            skuList.forEach(sku -> strSkuCodeList.add(sku.getSkuCode()));
            skuList.forEach(sku -> strSkuCodeList.add(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name())));
            // modified by morse.lu 2016/06/13 end
            // 如果已Approved产品skuList为空，则把库存表里面所有的数据（几万条）数据全部查出来了，很花时间
            // 如果已Approved产品skuList为空，则中止该产品的上新流程
            if (strSkuCodeList.isEmpty()) {
                String errMsg = String.format("已Approved产品sku列表为空");
                $error(errMsg);
                sxData.setErrorMessage(errMsg);
                throw new BusinessException(errMsg);
            }
            // 如果skuList不为空，取得所有sku的库存信息
            // 为了对应MiniMall的场合， 获取库存的时候要求用getOrgChannelId()（其他的场合仍然是用channelId即可）
            Map<String, Integer> skuLogicQtyMap = productService.getLogicQty(mainProduct.getOrgChannelId(), strSkuCodeList);

            // 属性值准备
            // 2016/06/01 Delete by desmond start   京东不需要mapping表了
            // 取得主产品类目对应的platform mapping数据
//            CmsMtPlatformMappingModel cmsMtPlatformMappingModel = platformMappingService.getMappingByMainCatId(shopProp.getOrder_channel_id(),
//                    Integer.parseInt(shopProp.getCart_id()), mainProduct.getCatId());
//            if (cmsMtPlatformMappingModel == null) {
//                String errMsg = String.format("共通PlatformMapping表中对应的平台Mapping信息不存在！[ChannelId:%s] [CartId:%s] [主产品类目:%s]", channelId, cartId, mainProduct.getCatId());
//                $error(errMsg);
//                sxData.setErrorMessage(errMsg);
//                throw new BusinessException(errMsg);
//            }
            // 2016/06/01 Delete by desmond end

            // 取得主产品京东平台设置信息(包含SKU等信息)
            CmsBtProductModel_Platform_Cart mainProductPlatformCart = mainProduct.getPlatform(sxData.getCartId());
            if (mainProductPlatformCart == null) {
                String errMsg = String.format("获取主产品京东平台(Platform_Cart)设置信息失败！[ProductCode:%s][CartId:%s]",
                        mainProduct.getFields().getCode(), sxData.getCartId());
                $error(errMsg);
                sxData.setErrorMessage(errMsg);
                throw new BusinessException(errMsg);
            }

            // 取得主产品的京东平台类目(用于取得京东平台该类目下的Schema信息)
            String platformCategoryId = mainProductPlatformCart.getpCatId();
            // 取得平台类目schema信息
            CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = platformCategoryService.getPlatformCatSchema(platformCategoryId, cartId);
            if (cmsMtPlatformCategorySchemaModel == null) {
                String errMsg = String.format("获取平台类目schema信息失败！[PlatformCategoryId:%s] [CartId:%s]", platformCategoryId, cartId);
                $error(errMsg);
                sxData.setErrorMessage(errMsg);
                throw new BusinessException(errMsg);
            }

            // 获取cms_mt_platform_skus表里渠道指定类目对应的所有颜色和尺寸信息列表
            List<CmsMtPlatformSkusModel> cmsMtPlatformSkusList = cmcMtPlatformSkusService.getModesByAttrType(channelId, cartId, platformCategoryId, AttrType_Active_1);
            if (ListUtils.isNull(cmsMtPlatformSkusList)) {
                String errMsg = String.format("平台skus表中该商品类目对应的颜色和尺寸信息不存在！[ChannelId:%s] [CartId:%s] [PlatformCategoryId:%s] [PlatformCategoryPath:%s]",
                        channelId, cartId, platformCategoryId, mainProductPlatformCart.getpCatPath());
                $error(errMsg);
                sxData.setErrorMessage("平台skus表中该商品类目对应的颜色和尺寸信息不存在 [平台类目(" + platformCategoryId +
                        ":" + mainProductPlatformCart.getpCatPath() + ")]");
                throw new BusinessException(errMsg);
            }

            // 获取尺码表
            // 7. product.fields（mongodb， 共通字段略）增加三个字段
            //       * sizeGroupId        <-设置SKU信息     //不是新任务列表中的groupid
            //       * sizeChartIdPc      <-上传图片用
            //       * sizeChartIdMobile  <-上传图片用
            //String sizeMapGroupId = ""; // TODO No.1 这个字段还没加 sizeMapGroupId =  mainProduct.getFields().getSizeMapGroupId();

            // 取得尺码转换信息
            Map<String, String> jdSizeMap = sxProductService.getSizeMap(channelId, mainProduct.getFields().getBrand(),
                    mainProduct.getFields().getProductType(), mainProduct.getFields().getSizeType());

            // 获取字典表(根据channel_id)上传图片的规格等信息
            List<CmsMtPlatformDictModel> cmsMtPlatformDictModelList = dictService.getModesByChannelCartId(channelId, cartId);
            if (cmsMtPlatformDictModelList == null || cmsMtPlatformDictModelList.size() == 0) {
                String errMsg = String.format("获取字典表数据失败！[ChannelId:%s] [CartId:%s]", channelId, cartId);
                $error(errMsg);
                sxData.setErrorMessage(errMsg);
                throw new BusinessException(errMsg);
            }

            // 编辑京东共通属性
            JdProductBean jdProductBean = setJdProductCommonInfo(sxData, platformCategoryId, groupId, shopProp,
                    cmsMtPlatformCategorySchemaModel, skuLogicQtyMap);

            // 产品和颜色值的Mapping关系表(设置SKU属性时填入值，上传SKU图片时也会用到)
            Map<String, Object> productColorMap = new HashMap<>();

            // 返回结果是否成功状态
            boolean retStatus = false;

            // 判断新增商品还是更新商品
            // 只要numIId不为空，则为更新商品
            if (!StringUtils.isEmpty(sxData.getPlatform().getNumIId())) {
                // 更新商品
                updateWare = true;
                // 取得更新对象商品id
                jdWareId = Long.parseLong(sxData.getPlatform().getNumIId());
                jdProductBean.setWareId(sxData.getPlatform().getNumIId());
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
                    sxData.setErrorMessage(errMsg);
                    throw new BusinessException(errMsg);
                }

                // 回写商品id(wareId->numIId)
                // 为了避免商品上传成功，但后续处理出现异常时，表中没有numIId信息，所以要先回写numIId，后面上新失败再改为空
                updateProductGroupNumIId(sxData, String.valueOf(jdWareId));

                // 上传商品主图
                List<String> mainPicNameList = new ArrayList<>();
                mainPicNameList.add("京东产品图片-2");  // TODO 暂时用固定值，以后改为从表里取
                mainPicNameList.add("京东产品图片-3");
                mainPicNameList.add("京东产品图片-4");
                mainPicNameList.add("京东产品图片-5");

                // 取得图片URL参数
                ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
                // 京东要求图片必须是5张，商品主图的第一张已经在前面的共通属性里面设置了，这里最多只需要设置4张非主图
                for (String picName : mainPicNameList) {
                    String picUrl = "";
                    try {
                        // 取得图片URL
                        picUrl = sxProductService.resolveDict(picName, expressionParser, shopProp, getTaskName(), null);
                        if (StringUtils.isEmpty(picUrl)) continue;
                        // 上传主产品的其余4张非主图片
                        jdWareService.addWarePropimg(shopProp, String.valueOf(jdWareId), ColorId_MinPic, picUrl, picName, false);
                    } catch (Exception ex) {
                        // 有可能是因为新增商品时，如果该类目最少5张图片，京东自动的把上传的第一张图片复制成5张图，
                        // 则传后续4张图时只有第2张能正常上传，后面都会报超过6张图片的错误
                        $error("京东上传主商品非主图失败！[WareId:%s] [ColorId:%s] [PicName:%s] [PicUrl:%s]", jdWareId, ColorId_MinPic, picName, picUrl);
                        $error(ex);
                        // 继续上传下一张图片
                    }
                }

                // 新增商品成功后，设置SKU属性 调用京东商品修改API
                JdProductBean updateProductBean = new JdProductBean();
                // 新增商品id
                updateProductBean.setWareId(String.valueOf(jdWareId));
                // 构造更新用商品bean，主要设置SKU相关属性
                updateProductBean = setJdProductSkuInfo(updateProductBean, sxData, cmsMtPlatformSkusList,
                        shopProp, productColorMap, skuLogicQtyMap, jdSizeMap);

                // 新增之后调用京东商品更新API
                // 调用京东商品更新API设置SKU信息的好处是可以一次更新SKU信息，不用再一个一个SKU去设置
                String modified = "";
                try {
                    modified = jdWareService.updateProduct(shopProp, updateProductBean);
                } catch (Exception ex) {
                    String errMsg = String.format("新增商品之后调用京东商品更新API批量设置SKU信息失败! [WareId:%s]", jdWareId);
                    $error(errMsg);
                    sxData.setErrorMessage(ex.getMessage());
                }

                // 设置SKU信息是否成功判断
                if (!StringUtils.isEmpty(modified)) {
                    // 新增之后更新商品SKU信息成功
                    // 上传该商品下所有产品的图片
                    retStatus = uploadJdProductAddPics(shopProp, jdWareId, sxData, productColorMap);
                    if (!retStatus) {
                        String errMsg = String.format("新增商品的产品5张图片上传均失败! [WareId:%s]", jdWareId);
                        $error(errMsg);
                        sxData.setErrorMessage(errMsg);
                    }
                }

                // 新增商品成功之后更新SKU信息失败时，删除该商品
                if (StringUtils.isEmpty(modified) || !retStatus) {
                    // 新增之后更新商品SKU信息失败
                    // 删除商品
                    try {
                        // 参数：1.ware_id 2.trade_no(流水号：现在时刻)
                        jdWareService.deleteWare(shopProp, String.valueOf(jdWareId), DateTimeUtil.getNowTimeStamp());
                    } catch (Exception ex) {
                        String errMsg = String.format("新增商品后设置SKU信息失败之后，删除该新增商品失败! [WareId:%s]", jdWareId);
                        $error(errMsg);
                        sxData.setErrorMessage(ex.getMessage());
                        throw ex;
                    }

                    // 删除group表中的商品id(numIId)
                    updateProductGroupNumIId(sxData, "");

                    // 失败状态设定
                    retStatus = false;
                }

            } else {
                // 更新商品的时候
                // 设置更新用商品beanSKU属性 (更新用商品bean，共通属性前面已经设置)
                jdProductBean = setJdProductSkuInfo(jdProductBean, sxData, cmsMtPlatformSkusList,
                        shopProp, productColorMap, skuLogicQtyMap, jdSizeMap);

                // 京东商品更新API返回的更新时间
                // 调用京东商品更新API
                String retModified = jdWareService.updateProduct(shopProp, jdProductBean);

                // 更新商品是否成功
                if (!StringUtils.isEmpty(retModified)) {
                     // 更新该商品下所有产品的图片
                    retStatus = uploadJdProductUpdatePics(shopProp, jdWareId, sxData, productColorMap);
                    if (!retStatus) {
                        String errMsg = String.format("更新商品的产品图片失败! [WareId:%s]", jdWareId);
                        $error(errMsg);
                        // 如果上新数据中的errorMessage为空
                        if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                            sxData.setErrorMessage(errMsg);
                        }
                    }
                }
            }

            // 新增或者更新商品结束时，根据状态回写product表（成功1 失败2）
            if (retStatus) {
                // 新增或更新商品成功时
                // 回写ims_bt_product表(numIId)
                sxProductService.updateImsBtProduct(sxData, getTaskName());

                // 设置京东运费模板和关联板式
                // 设置京东运费模板
                updateJdWareTransportId(shopProp, sxData, jdWareId);
                // 设置京东关联板式
                updateJdWareLayoutId(shopProp, sxData, jdWareId);

                // 执行商品上架/下架操作
                boolean updateJdWareListing = false;
                updateJdWareListing = updateJdWareListing(shopProp, sxData, jdWareId, updateWare);

                // 上新或更新成功后回写product group表中的platformStatus(Onsale/InStock)
                updateProductGroupStatus(sxData, updateWare, updateJdWareListing);

                // 回写workload表   (成功1)
                sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, CmsConstants.SxWorkloadPublishStatusNum.okNum, getTaskName());
            } else {
                // 新增或更新商品失败
                String errMsg = String.format("京东单个商品新增或更新信息失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s]",
                        channelId, cartId, groupId, jdWareId);
                $error(errMsg);
                // 如果上新数据中的errorMessage为空
                if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                    sxData.setErrorMessage("京东单个商品新增或更新信息失败！请向管理员确认 [WareId:" + jdWareId + "]" );
                }
                // 回写详细错误信息表(cms_bt_business_log)
                sxProductService.insertBusinessLog(sxData, getTaskName());

                // 更新商品出错时，也要设置运费模板和关联板式
                if (updateWare) {
                    // 设置京东运费模板
                    updateJdWareTransportId(shopProp, sxData, jdWareId);
                    // 设置京东关联板式
                    updateJdWareLayoutId(shopProp, sxData, jdWareId);
                }

                // 回写workload表   (失败2)
                sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, CmsConstants.SxWorkloadPublishStatusNum.errorNum, getTaskName());
                return;
            }
        } catch (Exception ex) {
            // 异常结束时
            String errMsg = String.format("京东单个商品新增或更新信息异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s]",
                    channelId, cartId, groupId, jdWareId);
            $error(errMsg);
            ex.printStackTrace();
            if (sxData == null) {
                // 回写详细错误信息表(cms_bt_business_log)用
                sxData = new SxData();
                sxData.setChannelId(channelId);
                sxData.setCartId(cartId);
                sxData.setGroupId(groupId);
                sxData.setErrorMessage(shopProp.getShop_name() + "取得上新用的商品数据信息异常！请向管理员确认 [上新数据为null]");
            }
            // 如果上新数据中的errorMessage为空
            if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                sxData.setErrorMessage(ex.getMessage());
            }
            // 回写详细错误信息表(cms_bt_business_log)
            sxProductService.insertBusinessLog(sxData, getTaskName());
            // 回写workload表   (失败2)
            sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, CmsConstants.SxWorkloadPublishStatusNum.errorNum, getTaskName());
            return;
        }

        // 正常结束
        if (!updateWare) {
            $info(String.format("京东单个商品新增信息成功！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s]",
                    channelId, cartId, groupId, jdWareId));
        } else {
            $info(String.format("京东单个商品更新信息成功！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s]",
                    channelId, cartId, groupId, jdWareId));
        }

    }

    /**
     * 设置京东上新产品用共通属性
     * 不包含"sku属性","sku价格","sku库存","自定义属性值别名","SKU外部ID"的值
     *
     * @param sxData sxData 上新产品对象
     * @param platformCategoryId String     平台类目id
     * @param groupId long                  groupid
     * @param shopProp ShopBean             店铺信息
     * @param platformSchemaData CmsMtPlatformCategorySchemaModel  主产品类目对应的平台schema数据
     * @param skuLogicQtyMap Map<String, Integer>  SKU逻辑库存
     * @return JdProductBean 京东上新用bean
     * @throws BusinessException
     */
    private JdProductBean setJdProductCommonInfo(SxData sxData, String platformCategoryId,
                                                 long groupId, ShopBean shopProp,
                                                 CmsMtPlatformCategorySchemaModel platformSchemaData,
                                                 Map<String, Integer> skuLogicQtyMap) throws BusinessException {
        CmsBtProductModel mainProduct = sxData.getMainProduct();
        List<CmsBtProductModel> productList = sxData.getProductList();
        //List<CmsBtProductModel_Sku> skuList = sxData.getSkuList();

        // 渠道id
        String channelId = shopProp.getOrder_channel_id();
        // 平台id
        String cartId = shopProp.getCart_id();

        // 京东上新产品共通属性设定
        JdProductBean jdProductBean = new JdProductBean();

        // 流水号(非必须)
//        jdProductBean.setTradeNo(mainProduct.getXXX());                  // 不使用
        // 产地(非必须)
//        jdProductBean.setWareLocation(mainProduct.getFields().getOrigin());
        // 类目id(必须)
        jdProductBean.setCid(platformCategoryId);
        // 自定义店内分类(非必须)(123-111;122-222)
        try {
            String shopCagetory = this.getShopCategory(shopProp, sxData);
            jdProductBean.setShopCategory(shopCagetory);
        } catch (Exception ex) {
            String errMsg = String.format("京东取得自定义店内分类失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                    channelId, cartId, groupId, platformCategoryId);
            $error(errMsg, ex);
        }
        // 商品标题(必须)
        jdProductBean.setTitle(mainProduct.getFields().getLongTitle());
        // UPC编码(非必须)
//        jdProductBean.setUpcCode(mainProduct.getXXX());                 // 不使用
        // 操作类型 现只支持：offsale 或onsale,默认为下架状态 (非必须)
        jdProductBean.setOptionType(this.getOptionType(sxData.getPlatform(), groupId));
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
        Object objfieldItemValue = null;
        String strWeight = "1";  // 默认为1kg
        objfieldItemValue = sxProductService.getPropValue(mainProduct.getPlatform(sxData.getCartId()).getFields(), "productWeightKg");
        // 取得值为null不设置，空字符串的时候还是要设置（可能是更新时特意把某个属性的值改为空）
        if (objfieldItemValue != null && objfieldItemValue instanceof String) {
            strWeight = String.valueOf(objfieldItemValue);
        }
        jdProductBean.setWeight(strWeight);
        // 进货价,精确到2位小数，单位:元(非必须)
//        jdProductBean.setCostPrice(String.valueOf(jdPrice));     // 不使用
        // 市场价, 精确到2位小数，单位:元(必须)
        Double marketPrice = getItemPrice(productList, channelId, cartId, PriceType_marketprice);
        jdProductBean.setMarketPrice(String.valueOf(marketPrice));
        // 京东价,精确到2位小数，单位:元(必须)
        Double jdPrice = getItemPrice(productList, channelId, cartId, PriceType_jdprice);
        sxData.setMaxPrice(jdPrice);
        jdProductBean.setJdPrice(String.valueOf(jdPrice));

        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
        // 描述（最多支持3万个英文字符(必须)
        String strNotes = "";
        try {
            // 取得描述
            strNotes = sxProductService.resolveDict("京东详情页描述", expressionParser, shopProp, getTaskName(), null);
        } catch (Exception ex) {
            String errMsg = String.format("京东取得详情页描述信息失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                    channelId, cartId, groupId, platformCategoryId);
            $error(errMsg, ex);
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
                picUrl = sxProductService.resolveDict(picName, expressionParser, shopProp, getTaskName(), null);
                if (StringUtils.isEmpty(picUrl)) continue;
                // 读取图片
                InputStream inputStream = jdWareService.getImgInputStream(picUrl, 3);
                bytes = IOUtils.toByteArray(inputStream);
                // 取得图片就退出循环
                break;
            } catch (Exception ex) {
                String errMsg = String.format("京东取得商品主图信息失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s] [PicName:%s]",
                        channelId, cartId, groupId, platformCategoryId, picName);
                $error(errMsg, ex);
                sxData.setErrorMessage("京东取得商品主图信息失败 [图片URL:" + picUrl + "]");
                // 继续取下一张图片
            }
        }
        // 图片信息（图片尺寸为800*800，单张大小不超过 1024K）(必须)
        if (bytes == null) {
            throw new BusinessException(sxData.getErrorMessage());
        }
        jdProductBean.setWareImage(bytes);

        // 包装清单(非必须)
//        jdProductBean.setPackListing(mainProduct.getXXX());               // 不使用
        // 售后服务(非必须)
//        jdProductBean.setService(mainProduct.getXXX());                   // 不使用

        // 调用共通函数取得商品属性列表，用户自行输入的类目属性ID和用户自行输入的属性值Map
//        Map<String, String> jdProductAttrMap = getJdProductAttributes(platformMappingData, platformSchemaData, shopProp, expressionParser, getTaskName());
        Map<String, String> jdProductAttrMap = getJdProductAttributes(platformSchemaData, shopProp, expressionParser);
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
        // 品牌id
        jdProductBean.setBrandId(sxData.getBrandCode());

        return jdProductBean;
    }

    /**
     * 取得京东商品属性值
     *
     * @param platformSchemaData CmsMtPlatformCategorySchemaModel  主产品类目对应的平台schema数据
     * @return Map(包含商品属性列表 用户自行输入的类目属性ID 用户自行输入的类目属性值)
     */
    private Map<String, String> getJdProductAttributes(CmsMtPlatformCategorySchemaModel platformSchemaData,
                                           ShopBean shopBean, ExpressionParser expressionParser) {
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
            // 取得平台Schema所有field对应的属性值（不使用platform_mapping，直接从mainProduct中取得fieldId对应的值）
            attrMap = sxProductService.constructPlatformProps(itemFieldList, shopBean, expressionParser);
        } catch (Exception ex) {
            String errMsg = String.format("取得京东平台Schema所有Field对应的属性值失败！[ChannelId:%s] [CartId:%s] [PlatformCategoryId:%s]",
                    shopBean.getOrder_channel_id(), shopBean.getCart_id(), platformSchemaData.getCatId());
            $error(errMsg, ex);
        }

        // 商品属性列表,多组之间用|分隔，格式:aid:vid 或 aid:vid|aid1:vid1 或 aid1:vid1（需要从类目服务接口获取）
        // 如输入类型input_type为1或2，则attributes为必填属性；如输入类型input_type为3，则用字段input_str填入属性的值 
        StringBuilder sbAttributes = new StringBuilder();
        // 用户自行输入的类目属性ID串结构：‘pid1|pid2|pid3’,属性的pid调用360buy.ware.get.attribute取得, 输入类型input_type=3即输入 
        StringBuilder sbInputPids = new StringBuilder();
        // 用户自行输入的属性值,结构:‘输入值|输入值2|输入值3’
        // 图书品类输入值规则：ISBN：数字、字母格式 出版时间：日期格式“yyyy-mm-dd” 版次：数字格式 印刷时间：日期格式“yyyy-mm-dd” 印次：数字格式 页数：数字格式 字数：数字格式 套装数量：数字格式 附件数量：数字格式 
        StringBuilder sbInputStrs = new StringBuilder();

        // 如果list为空说明没有mappingg过，不用设置
        if (attrMap != null && attrMap.size() > 0 && itemFieldList != null) {
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
                        sbAttributes.append(((SingleCheckField)fieldValue).getValue().getValue()); // 属性值id
                        sbAttributes.append(Separtor_Vertical);                // "|"
                        break;
                    }
                    case MULTICHECK: {
                        // 多选的时候，属性值多个，则用逗号分隔 "属性值1，属性值2，属性值3")
                        StringBuilder sbMultiAttrValue = new StringBuilder();
                        List<Value> valueList = ((MultiCheckField) fieldValue).getValues();
                        if (valueList != null && valueList.size() > 0) {
                            for (int i = 0; i < valueList.size(); i++) {
                                // 属性值id(fieldId对应的属性值id)
                                sbMultiAttrValue.append(valueList.get(i).getValue());
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
                        sbInputStrs.append(((InputField) fieldValue).getValue()); // 属性值id
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
     * @param shop ShopBean 店铺信息
     * @param productColorMap Map<String, Object> 产品和颜色值Mapping关系表
     * @param skuLogicQtyMap Map<String, Integer> 所有SKU的逻辑库存列表
     * @param jdSizeMap  Map<String, String> 尺码对照表
     * @return JdProductBean 京东上新用bean
     * @throws BusinessException
     */
    private JdProductBean setJdProductSkuInfo(JdProductBean targetProductBean, SxData sxData, List<CmsMtPlatformSkusModel> cmsMtPlatformSkusList,
                                              ShopBean shop, Map<String, Object> productColorMap,
                                              Map<String, Integer> skuLogicQtyMap, Map<String, String> jdSizeMap) throws BusinessException {
        List<CmsBtProductModel> productList = sxData.getProductList();

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
        Map<String, Object> skuSizeMap = new HashMap<>();

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
            for (CmsBtProductModel_Sku sku : productSkuList) {
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
        StringBuilder sbSkuProperties = new StringBuilder();
        // sku价格,多组之间用‘|’分隔，格式:p1|p2 (非必须)
        StringBuilder sbSkuPrice = new StringBuilder();
        // sku 库存,多组之间用‘|’分隔， 格式:s1|s2(非必须)
        StringBuilder sbSkuStocks = new StringBuilder();
        // 自定义属性值别名：属性ID:属性值ID:别名(非必须)
        StringBuilder sbPropertyAlias = new StringBuilder();
        // SKU外部ID,多组之间用‘|’分隔， 格式:s1|s2(非必须)
        StringBuilder sbSkuOuterId = new StringBuilder();

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
            String size = entry.getKey();
            sbPropertyAlias.append((StringUtils.isEmpty(jdSizeMap.get(size))) ? size : jdSizeMap.get(size)); // 尺寸值别名(转换后尺码)
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
     * 新增商品时设置该商品每个产品的图片
     * 遍历当前group里的所有的product列表，每个product上传5张图片
     * 第一张图设为主图true，其他的都设置为false
     *
     * @param wareId long 商品id
     * @param sxData SxData 产品对象
     * @param productColorMap Map<String Object> 产品和颜色值Mapping关系表
     * @return boolean 新增商品上传SKU图片是否成功
     */
    private boolean uploadJdProductAddPics(ShopBean shopProp, long wareId, SxData sxData,
                                        Map<String, Object> productColorMap) {
        boolean retUploadAddPics = true;
        List<CmsBtProductModel> productList = sxData.getProductList();
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 产品5张图片名称列表(TODO 暂时为固定值，以后会改成从表中读取)
        List<String> productPicNameList = new ArrayList<>();
        productPicNameList.add("京东产品图片-1");
        productPicNameList.add("京东产品图片-2");
        productPicNameList.add("京东产品图片-3");
        productPicNameList.add("京东产品图片-4");
        productPicNameList.add("京东产品图片-5");

        // 增加图片  调用API【根据商品Id，销售属性值Id增加图片】
        // 每个颜色要传五张图，第一张图设为主图，其他的都设置为非主图
        for (CmsBtProductModel product:productList) {
            // 颜色值id取得
            // 新增或更新商品的时候，从产品和颜色值Mapping关系表中取得该产品对应的颜色值
            String colorId = "";
            if (!StringUtil.isEmpty(productColorMap.get(product.getFields().getCode()).toString())) {
                String[] colorIdArray = productColorMap.get(product.getFields().getCode()).toString().split(Separtor_Colon);
                if (colorIdArray.length >= 2) {
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
                    // 取得图片URL
                    picUrl = sxProductService.resolveDict(picName, expressionParser, shopProp, getTaskName(), extParameter);
                    if (StringUtils.isEmpty(picUrl)) continue;
                    // 如果之前没有一张图片上传成功则本次上传对象图片设置为主图，如果之前已经有图片上传成功，则本次设为非主图
                    boolean skuPicResult = jdWareService.addWarePropimg(shopProp, String.valueOf(wareId), colorId, picUrl, picName, !uploadProductPicResult);

                    // 5张图片只有曾经有一张上传成功就认为SKU图片上传成功
                    uploadProductPicResult = uploadProductPicResult || skuPicResult;
                } catch (Exception ex) {
                    // 如果5张图片里面有一张上传成功的时候
                    if (uploadProductPicResult) {
                        $info("京东根据商品Id销售属性值Id上传产品主图成功，上传非主图图片失败！[WareId:%s] [ColorId:%s] [PicName:%s] [PicUrl:%s]", wareId, colorId, picName, picUrl);
                    } else {
                        $info("京东根据商品Id销售属性值Id上传产品主图失败！[WareId:%s] [ColorId:%s] [PicName:%s] [PicUrl:%s]", wareId, colorId, picName, picUrl);
                    }
                    $error(ex);
                    // 即使5张图片中的某张上传出错，也继续循环上传后面的图片
                }
            }

            // 该产品5张图片全部上传失败的时候
            if (!uploadProductPicResult) {
                $error("新增商品时该颜色图片全部上传失败！[WareId:%s] [ProductCode:%s]", wareId, product.getFields().getCode());
            } else {
                $info("新增商品时该颜色图片至少有1张上传成功！[WareId:%s] [ProductCode:%s]", wareId, product.getFields().getCode());
            }

            // 图片上传返回状态判断(该商品下所有产品的图片均上传成功时，才返回成功，否则返回失败)
            retUploadAddPics = retUploadAddPics && uploadProductPicResult;

            // 只要有一个产品（颜色）的5张图片都上传失败，则认为失败，退出循环，后面会删除该商品
            if (!retUploadAddPics) {
                break;
            }
        }

        return retUploadAddPics;
    }

    /**
     * 更新商品时更新该商品每个产品的图片（不包括主图）
     * 遍历当前group里的所有的product，更新每个product的5张图片
     * 每个颜色先上传一张，再删除一张，最后删除多余的更新前图片
     *
     * @param wareId long 商品id
     * @param sxData SxData 产品对象
     * @param productColorMap Map<String Object> 产品和颜色值Mapping关系表
     * @return boolean 上传指定商品SKU图片是否成功
     */
    private boolean uploadJdProductUpdatePics(ShopBean shopProp, long wareId, SxData sxData,
                                           Map<String, Object> productColorMap) {
        boolean retUploadUpdatePics = true;
        List<CmsBtProductModel> productList = sxData.getProductList();

        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 产品5张图片名称列表(TODO 暂时为固定值，以后会改成从表中读取)
        List<String> productPicNameList = new ArrayList<>();
        productPicNameList.add("京东产品图片-1");
        productPicNameList.add("京东产品图片-2");
        productPicNameList.add("京东产品图片-3");
        productPicNameList.add("京东产品图片-4");
        productPicNameList.add("京东产品图片-5");

        // ---------------------------------------------
        // 京东平台如果想替换产品图片需要将图片先上传到京东平台，并且每种颜色不能删除全部的图片
        // 有的类目需要最少留1张图片，有的类目需要最少留3张图片，不能全部删除。
        // （京东平台上每种颜色最多可以上传6张图，我们最多只会上5张图）
        // 基于以上原因，商品更新时颜色图片的更新采用以下方法：
        //
        // 1.检索 取得图片更新前京东平台上每种颜色的图片张数（可能范围：0~5张）
        // 2.循环颜色（产品）列表更新图片
        //     再循环产品中的要更新的图片列表（可能范围：0~5张），执行下面操作
        //         上传一张到京东平台（会自动添加到京东平台颜色图片末尾），再删除京东平台产品图片的第一张（删除之后图片会自动往前移）
        //     如果更新后的图片件数比平台上更新前的图片件数少，则循环删除京东平台上剩余未删除的图片
        //   结束颜色列表循环
        //
        // 备注：每种颜色的5张图片如果有一张上传成功则认为成功，
        //      但是如果有一种颜色的5张图片都上传失败，则认为更新失败并退出循环，并将颜色及错误信息写入log表
        // ---------------------------------------------

        // 1.检索 取得图片更新前京东平台上每种颜色的图片张数（可能范围：0~5张）
        // 更新的时候，保存更新之前的颜色和图片INDEX列表用（不包含颜色值Id为0000000000主图）
        Map<String,List<String>> jdColorIndexesMap = new HashMap<>();
        // 调用京东API根据商品Id检索指定商品Id对应的商品图片列表（包含颜色值Id为0000000000）
        List<Image> wareIdPics = jdWareService.getImagesByWareId(shopProp, wareId);
        // 取得图片更新前京东平台上每种颜色的图片张数
        for (Image img : wareIdPics) {
            // 过滤掉主图的颜色值Id0000000000
            if (ColorId_MinPic.equals(img.getColorId())) {
                continue;
            }

            // 如果对象MAP中没有这个颜色
            if (!jdColorIndexesMap.containsKey(img.getColorId())) {
                jdColorIndexesMap.put(img.getColorId(), new ArrayList<>());
            }

            // 将图片index追加到列表中
            if (jdColorIndexesMap.containsKey(img.getColorId())){
                jdColorIndexesMap.get(img.getColorId()).add(img.getImgIndex().toString());
            }
        }

        // 2.循环颜色（产品）列表更新图片
        // 每个颜色先上传一张，再删除一张，最后删除多余的更新前图片
        for (CmsBtProductModel product:productList) {
            // 颜色值id取得
            // 新增或更新商品的时候，从产品和颜色值Mapping关系表中取得该产品对应的颜色值
            String colorId = "";
            if (!StringUtil.isEmpty(productColorMap.get(product.getFields().getCode()).toString())) {
                String[] colorIdArray = productColorMap.get(product.getFields().getCode()).toString().split(Separtor_Colon);
                if (colorIdArray.length >= 2) {
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
            // 京东平台上该颜色对应的更新前图片张数
            int intOldImageCnt = 0;
            if (!ListUtils.isNull(jdColorIndexesMap.get(colorId))) {
                intOldImageCnt = jdColorIndexesMap.get(colorId).size();
            }
            // 要更新的产品图片张数
//            int intNewImageCnt = product.getFields().getImages1().size();
            // 京东平台上该颜色已经删除的图片件数
            int delImageCnt = 0;

            // 循环取得5张图片的url并分别上传到京东
            for (String picName : productPicNameList) {
                String picUrl = "";
                try {
                    // 取得图片URL(TODO 这里的解析字典不能知道现在到底是哪个颜色产品的图片，目前每个商品都只有一个product，暂时没问题)
                    picUrl = sxProductService.resolveDict(picName, expressionParser, shopProp, getTaskName(), extParameter);
                    if (StringUtils.isEmpty(picUrl)) continue;
                    // 如果之前没有一张图片上传成功则本次上传对象图片设置为主图，如果之前已经有图片上传成功，则本次设为非主图
                    boolean skuPicResult = jdWareService.addWarePropimg(shopProp, String.valueOf(wareId), colorId, picUrl, picName, !uploadProductPicResult);

                    // 5张图片只有曾经有一张上传成功就认为SKU图片上传成功
                    uploadProductPicResult = uploadProductPicResult || skuPicResult;
                } catch (Exception ex) {
                    // 如果5张图片里面有一张上传成功的时候
                    if (uploadProductPicResult) {
                        $info("京东根据商品Id销售属性值Id上传产品主图成功，上传非主图图片失败！[WareId:%s] [ColorId:%s] [PicName:%s] [PicUrl:%s]", wareId, colorId, picName, picUrl);
                    } else {
                        $info("京东根据商品Id销售属性值Id上传产品主图失败！[WareId:%s] [ColorId:%s] [PicName:%s] [PicUrl:%s]", wareId, colorId, picName, picUrl);
                    }
                    $error(ex);
                    sxData.setErrorMessage(ex.getMessage());
                    // 即使5张图片中的某张上传出错，也继续循环上传后面的图片
                }

                // 删除第一张图片(及时上传失败，也要删除老的图片，如果报必须大于3张/1张的错误，只能让运营把图片改好)
                if (delImageCnt < intOldImageCnt) {
                    try {
                        // 调用API【删除商品图片】批量删除该商品该颜色的第一张图片
                        boolean delPicResult = jdWareService.deleteImagesByWareId(shopProp, wareId, colorId, "1");
                        // 京东平台上该颜色已经删除的图片件数
                        if (delPicResult) delImageCnt++;
                    } catch (Exception ex) {
                        // 如果报"图片张数必须多余N张"的异常，则继续上传下一张图片，否则抛出异常
                        if (ex.getMessage().contains("图片张数必须多于")) {
                            continue;
                        } else {
                            throw new BusinessException(ex.getMessage());
                        }
                    }
                }
            }

            // 删除剩余的图片
            for (int i = delImageCnt; delImageCnt < intOldImageCnt; i++) {
                // 调用删除图片API（指定删除第一张）
                // 如果报"图片张数必须多余N张"的异常，则写到log表里，让运营在CMS里面添加图片
                jdWareService.deleteImagesByWareId(shopProp, wareId, colorId, "1");
            }

            // 该产品5张图片全部上传失败的时候
            if (!uploadProductPicResult) {
                $error("更新商品时该颜色图片全部上传失败！[WareId:%s] [ProductCode:%s]", wareId, product.getFields().getCode());
            } else {
                $info("更新商品时该颜色图片至少有1张上传成功！[WareId:%s] [ProductCode:%s]", wareId, product.getFields().getCode());
            }

            // 图片上传返回状态判断(该商品下所有产品的图片均上传成功时，才返回成功，否则返回失败)
            retUploadUpdatePics = retUploadUpdatePics && uploadProductPicResult;

            // 只要有一个产品（颜色）的5张图片都上传失败，则认为失败，退出循环，后面会删除该商品
            if (!retUploadUpdatePics) {
                break;
            }
        }

        return retUploadUpdatePics;
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
        StringBuilder sbbuilder = new StringBuilder();
        // 条件表达式表platform_prop_id字段的检索条件为"seller_cids_"加cartId
        String platformPropId = Prop_ShopCategory + shop.getCart_id();

        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 根据channelid和platformPropId取得cms_bt_condition_prop_value表的条件表达式
        List<ConditionPropValueModel> conditionPropValueModels = conditionPropValueRepo.get(shop.getOrder_channel_id(), platformPropId);

        // 优先使用条件表达式
        if (conditionPropValueModels != null && !conditionPropValueModels.isEmpty()) {
            RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
            for (ConditionPropValueModel conditionPropValueModel : conditionPropValueModels) {
                String conditionExpressionStr = conditionPropValueModel.getCondition_expression();
                RuleExpression conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
                String propValue = expressionParser.parse(conditionExpression, shop, getTaskName(), null);

                // 多个表达式(2392231-4345291格式)用分号分隔
                if (!StringUtils.isEmpty(propValue)) {
                    sbbuilder.append(propValue);
                    sbbuilder.append(Separtor_Semicolon);   // 用分号(";")分隔
                }
            }
        }

        if (StringUtils.isEmpty(sbbuilder.toString())) {
            // 获取京东平台前台展示的商家自定义店内分类(从分平台信息里面取得sellerCats)
            CmsBtProductModel_Platform_Cart productPlatformCart = sxData.getMainProduct().getPlatform(sxData.getCartId());
            if (productPlatformCart != null && ListUtils.notNull(productPlatformCart.getSellerCats())) {
                // 取得
                List<CmsBtProductModel_SellerCat> sellerCatList = productPlatformCart.getSellerCats();
                // 里面的数据是“1233797770-1233809821;1233797771-1233809822”样式的
                for (CmsBtProductModel_SellerCat sellerCat : sellerCatList ) {
                    if (sellerCat == null || ListUtils.isNull(sellerCat.getcIds())) {
                        continue;
                    }
                    // 用连字符("-")连接 (1233797770-1233809821)
                    sbbuilder.append(sellerCat.getcIds().stream().collect(Collectors.joining(Separtor_Hyphen)));
                    sbbuilder.append(Separtor_Semicolon);    // 用分号(";")分隔
                }
            }
            // 直接从Product中取得店铺内分类，不用从京东去取了
//            List<ShopCategory> shopCategoryList = jdShopService.getShopCategoryList(shop);
//            if (shopCategoryList != null && !shopCategoryList.isEmpty()) {
//                for (ShopCategory shopCategory : shopCategoryList) {
//                    // 如果不是父类目的话，加到店内分类里，用分号分隔
//                    if (!shopCategory.getParent()) {
//                        // 转换成“parent_id-cid"格式，同时设置多个以分号（;）分隔
//                        sbbuilder.append(String.valueOf(shopCategory.getParentId()));
//                        sbbuilder.append(Separtor_Hyphen);       // 用连字符("-")连接
//                        sbbuilder.append(String.valueOf(shopCategory.getCid()));
//                        sbbuilder.append(Separtor_Semicolon);    // 用分号(";")分隔
//                    }
//                }
//            }
        }
        // 移除最后的分号
        if (sbbuilder.length() > 0) {
            sbbuilder.deleteCharAt(sbbuilder.length() - 1);
        }

        // 店铺种类
        return sbbuilder.toString();
    }

    /**
     * 取得运费模板id或关联版式id
     *
     * @param shop ShopBean 店铺对象
     * @param sxData SxData 产品对象
     * @param prePropId String 条件表达式前缀(运费模板:transportid_ 关联版式:commonhtml_id_)
     * @return String 运费模板id或关联版式id
     */
    private String getConditionPropValue(ShopBean shop, SxData sxData, String prePropId) throws Exception {

        // 运费模板id或关联版式id返回用
        String  retStr = "";
        // 条件表达式前缀(运费模板:transportid_ 关联版式:commonhtml_id_)
        // 条件表达式表platform_prop_id字段的检索条件为条件表达式前缀加cartId
        String platformPropId = prePropId + shop.getCart_id();

        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 根据channelid和platformPropId取得cms_mt_channel_condition_config表的条件表达式
        List<ConditionPropValueModel> conditionPropValueModels = conditionPropValueRepo.get(shop.getOrder_channel_id(), platformPropId);

        // 使用运费模板或关联版式条件表达式
        if (conditionPropValueModels != null && !conditionPropValueModels.isEmpty()) {
            RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
            for (ConditionPropValueModel conditionPropValueModel : conditionPropValueModels) {
                String conditionExpressionStr = conditionPropValueModel.getCondition_expression();
                RuleExpression conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
                String propValue = expressionParser.parse(conditionExpression, shop, getTaskName(), null);

                // 找到运费模板或关联版式表达式则跳出循环
                if (!StringUtils.isEmpty(propValue)) {
                    retStr = propValue;
                    break;
                }
            }
        }

        // 运费模板id或关联版式id
        return retStr;
    }

    /**
     * 取得操作类型
     * 现只支持：offsale 或onsale,默认为下架状态
     *
     * @param productGroup CmsBtProductGroupModel 商品Model Group Channel
     * @param groupId long groupid
     * @return String 前台操作类型（offsale或onsale）
     */
    private String getOptionType(CmsBtProductGroupModel productGroup, long groupId) {
        String retOptionType;
        CmsConstants.PlatformActive platformActive = productGroup.getPlatformActive();

        if (platformActive == CmsConstants.PlatformActive.ToOnSale) {
            // 如果是ToOnSale， 那么onsale
            retOptionType = OptioinType_onsale;
        } else {
            // 如果是ToInStock， 那么offsale（默认状态为offsale）
            retOptionType = OptioinType_offsale;
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
        CmsChannelConfigBean sxPriceConfig = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.PRICE, cartId + "." + priceType);

        // 检查一下
        String sxPricePropName;
        if (sxPriceConfig == null) {
            return 0.0d;
        } else {
            // 取得价格属性名
            sxPricePropName = sxPriceConfig.getConfigValue1();
            if (StringUtils.isEmpty(sxPricePropName)) {
                return 0.0d;
            }
        }

        Double resultPrice = 0.0d;    //, onePrice = 0d;
        List<Double> skuPriceList = new ArrayList<>();
        for (CmsBtProductModel cmsProduct : sxProducts) {
            for (CmsBtProductModel_Sku cmsBtProductModelSku : cmsProduct.getSkus()) {
                double skuPrice = 0.00;
                // 如果是平台售价，则取个平台相应的售价(platform.P29.sku.priceSale)
//                if (PriceType_jdprice.equals(priceType)) {  // TODO 2016/06/17版本暂时还是从以前外面的sku里面取得京东价格
//                    CmsBtProductModel_Platform_Cart platformCart = cmsProduct.getPlatform(Integer.parseInt(cartId));
//                    List<BaseMongoMap<String, Object>> platformCartSkuList = platformCart.getSkus();
//                    // 循环取得找到本skucode对应的平台售价
//                    for(BaseMongoMap<String, Object> platformSkuMap : platformCartSkuList) {
//                        // 找到skucode对应的平台售价，然后跳出循环
//                        if (cmsBtProductModelSku.getSkuCode().equals(platformSkuMap.get("skuCode"))) {
//                            if(!StringUtil.isEmpty(platformSkuMap.get("priceSale").toString())) {
//                                skuPrice = Double.parseDouble(platformSkuMap.get("priceSale").toString());
//                                break;
//                            }
//                        }
//                    }
//                } else {
                    skuPrice = cmsBtProductModelSku.getDoubleAttribute(sxPricePropName);
//                }
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
        CmsChannelConfigBean sxPriceConfig = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.PRICE, cartId + "." + priceType);

        // 检查一下
        String sxPricePropName;
        if (sxPriceConfig == null) {
            return 0.0d;
        } else {
            // 取得价格属性名
            sxPricePropName = sxPriceConfig.getConfigValue1();
            if (StringUtils.isEmpty(sxPricePropName)) {
                return 0.0d;
            }
        }

        // 取得该SKU的价格
        return cmsBtProductModelSku.getDoubleAttribute(sxPricePropName);
    }

    /**
     * 回写产品Group表里的商品id
     *
     * @param sxData SxData 上新数据
     * @param numIId String 商品id
     */
    private void updateProductGroupNumIId(SxData sxData, String numIId) {

        // 回写商品id(wareId->numIId) 删除的时候设为空("")即可
        sxData.getPlatform().setNumIId(numIId);
        // 更新者
        sxData.getPlatform().setModifier(getTaskName());
        // 更新ProductGroup表
        productGroupService.update(sxData.getPlatform());
    }

    /**
     * 设置京东运费模板
     *
     * @param shop ShopBean 店铺对象
     * @param sxData SxData 上新数据
     * @param wareId long 商品id
     */
    private void updateJdWareTransportId(ShopBean shop, SxData sxData, long wareId) {
        // 京东运费模板id
        String wareTransportId = "";

        try {
            // 取得京东运费模板id
            wareTransportId = getConditionPropValue(shop, sxData, Prop_TransportId);
        } catch (Exception ex) {
            String errMsg = String.format("取得京东运费模板id失败！[ChannelId:%s] [CartId:%s] [Prop_TransportId:%s]",
                    shop.getOrder_channel_id(), shop.getCart_id(), Prop_TransportId);
            $info(errMsg, ex);
        }

        // 调用京东API设置运费模板
        if (!StringUtils.isEmpty(wareTransportId)) {
            jdWareService.updateWareTransportId(shop, wareId, Long.parseLong(wareTransportId));
        }
    }

    /**
     * 设置京东关联板式
     *
     * @param shop ShopBean 店铺对象
     * @param sxData SxData 上新数据
     * @param wareId long 商品id
     */
    private void updateJdWareLayoutId(ShopBean shop, SxData sxData, long wareId) {
        // 京东关联板式id
        String commonHtml_Id = "";

        try {
            // 取得京东关联板式id
            commonHtml_Id = getConditionPropValue(shop, sxData, Prop_CommonHtmlId);
        } catch (Exception ex) {
            String errMsg = String.format("取得京东关联板式id失败！[ChannelId:%s] [CartId:%s] [Prop_CommonHtmlId:%s]",
                    shop.getOrder_channel_id(), shop.getCart_id(), Prop_CommonHtmlId);
            $info(errMsg, ex);
        }

        // 调用京东API设置关联板式（取消商品关联版式时，请将commonHtml_Id值设置为空）
        jdWareService.updateWareLayoutId(shop, String.valueOf(wareId), commonHtml_Id);
    }

    /**
     * 回写product group表中的platformStatus(Onsale/InStock)
     *
     * @param sxData SxData 上新数据
     * @param updateFlg boolean 新增/更新商品flg
     * @param updateListingResult boolean 商品上架/下架操作结果状态
     */
    private void updateProductGroupStatus(SxData sxData, boolean updateFlg, boolean updateListingResult) {
        // 上新成功后回写product group表中的platformStatus
        // 设置PublishTime
        sxData.getPlatform().setPublishTime(DateTimeUtil.getNowTimeStamp());

        // 新增或更新商品，只有在商品上架/下架操作成功之后才回写platformStatus，失败不回写状态(新增商品时除外)
        if (updateListingResult) {
            // platformActive平台上新状态类型(ToOnSale/ToInStock)
            if (CmsConstants.PlatformActive.ToOnSale.equals(sxData.getPlatform().getPlatformActive())) {
                // platformActive是(ToOnSale)时，把platformStatus更新成"OnSale"
                sxData.getPlatform().setPlatformStatus(CmsConstants.PlatformStatus.OnSale);
            } else {
                // platformActive是(ToInStock)时，把platformStatus更新成"InStock"(默认)
                sxData.getPlatform().setPlatformStatus(CmsConstants.PlatformStatus.InStock);
            }
        } else {
            // 商品上架/下架失败的时候，更新商品时不回写状态，新增商品时一律回写成"InStock"(默认)
            if (!updateFlg) {
                // 新增商品之后商品上架/下架失败时，把platformStatus更新成"InStock"(默认)
                sxData.getPlatform().setPlatformStatus(CmsConstants.PlatformStatus.InStock);
            }
        }
        // 更新者
        sxData.getPlatform().setModifier(getTaskName());
        // 更新ProductGroup表(更新该model对应的所有(包括product表)和上新有关的状态信息)
        productGroupService.updateGroupsPlatformStatus(sxData.getPlatform());
    }

    /**
     * 商品上架/下架处理
     *
     * @param shop ShopBean 店铺对象
     * @param sxData SxData 上新数据
     * @param wareId long 商品id
     * @param updateFlg boolean 新增/更新商品flg
     */
    private boolean updateJdWareListing(ShopBean shop, SxData sxData, long wareId, boolean updateFlg) {
        // 商品上架/下架结果
        boolean updateListingResult = false;

        // platformActive平台上新状态类型(ToOnSale/ToInStock)
        if (CmsConstants.PlatformActive.ToOnSale.equals(sxData.getPlatform().getPlatformActive())) {
            // platformActive是(ToOnSale)时，执行商品上架操作
            updateListingResult = jdWareService.doWareUpdateListing(shop, wareId, updateFlg);
        } else {
            // platformActive是(ToInStock)时，执行商品下架操作
            updateListingResult = jdWareService.doWareUpdateDelisting(shop, wareId, updateFlg);
        }

        return updateListingResult;
    }

}
