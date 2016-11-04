package com.voyageone.task2.cms.service;

import com.google.common.base.Joiner;
import com.taobao.api.ApiException;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaWriter;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.InputField;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.exceptions.GetUpdateSchemaFailException;
import com.voyageone.components.tmall.service.TbItemSchema;
import com.voyageone.components.tmall.service.TbSimpleItemService;
import com.voyageone.ims.rule_expression.DictWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.bean.cms.CmsBtPromotionSkuBean;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.CmsBtTmTonggouFeedAttrDao;
import com.voyageone.service.dao.cms.CmsMtChannelConditionMappingConfigDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaTmDao;
import com.voyageone.service.impl.cms.DictService;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.promotion.PromotionDetailService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.CmsBtTmTonggouFeedAttrModel;
import com.voyageone.service.model.cms.CmsMtChannelConditionMappingConfigModel;
import com.voyageone.service.model.cms.CmsMtPlatformDictModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaTmModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_SellerCat;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
import com.voyageone.task2.cms.service.putaway.ConditionPropValueRepo;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 天猫国际官网同购（官网直供）产品上新服务
 * 按照天猫国际要求的数据格式，将官网商品发布到天猫国际平台
 *
 * @author desmond on 2016/8/23.
 * @version 2.5.0
 * @since 2.5.0
 */
@Service
public class CmsBuildPlatformProductUploadTmTongGouService extends BaseCronTaskService {

    // 分隔符(,)
    private final static String Separtor_Coma = ",";

    @Autowired
    private DictService dictService;
    @Autowired
    private PlatformProductUploadService platformProductUploadService;
    @Autowired
    private ConditionPropValueRepo conditionPropValueRepo;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private ProductService productService;
    @Autowired
    private TbSimpleItemService tbSimpleItemService;
    @Autowired
    private CmsBtTmTonggouFeedAttrDao cmsBtTmTonggouFeedAttrDao;
    @Autowired
    private CmsMtPlatformCategorySchemaTmDao platformCategorySchemaDao;
    @Autowired
    private CmsMtChannelConditionMappingConfigDao cmsMtChannelConditionMappingConfigDao;
    @Autowired
    private PromotionDetailService promotionDetailService;

	@Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformProductUploadTmTongGouJob";
    }

    /**
     * 天猫国际官网同购上新处理
     *
     * @param taskControlList taskcontrol信息
     */
    @Override
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

//        // 清除缓存（这样在cms_mt_channel_config表中刚追加的价格计算公式等配置就能立刻生效了）
//        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_CmsChannelConfigs.toString());

        // 获取该任务可以运行的销售渠道
        List<String> channelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 初始化cms_mt_channel_condition_config表的条件表达式(避免多线程时2次初始化)
        conditionPropValueRepo.init();

        // 循环所有销售渠道
        if (ListUtils.notNull(channelIdList)) {
            for (String channelId : channelIdList) {
                // 商品上传(天猫国际官网同购)
                doProductUpload(channelId, CartEnums.Cart.TT.getValue());
//                // 商品上传(USJOI天猫国际官网同购)
//                doProductUpload(channelId, CartEnums.Cart.USTT.getValue());
            }
        }

        // 正常结束
        $info("天猫国际官网同购主线程正常结束");
    }

    /**
     * 平台产品上新主处理
     *
     * @param channelId String 渠道ID
     * @param cartId String 平台ID
     */
    public void doProductUpload(String channelId, int cartId) throws Exception {

        // 默认线程池最大线程数
        int threadPoolCnt = 5;

        // 获取店铺信息
        ShopBean shopProp = Shops.getShop(channelId, cartId);
        if (shopProp == null) {
            $error("获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }
        $info("获取店铺信息成功![ChannelId:%s] [CartId:%s]", channelId, cartId);

        // 从cms_bt_tm_tonggou_feed_attr表中取得该渠道，平台对应的天猫官网同购允许上传的feed attribute属性，如果为空则全部上传
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("channelId", channelId);
        paramMap.put("cartId", StringUtils.toString(cartId));
        List<CmsBtTmTonggouFeedAttrModel> tmTonggouFeedAttrModelList = cmsBtTmTonggouFeedAttrDao.selectList(paramMap);
        List<String> tmTonggouFeedAttrList = new ArrayList<>();
        if (ListUtils.notNull(tmTonggouFeedAttrModelList)) {
            // 如果表中有该渠道和平台对应的feed attribute属性，则将每个attribute加到列表中
            tmTonggouFeedAttrModelList.forEach(p -> tmTonggouFeedAttrList.add(p.getFeedAttr()));
        }

        // 从上新的任务表中获取该平台及渠道需要上新的任务列表(group by channel_id, cart_id, group_id)
        List<CmsBtSxWorkloadModel> sxWorkloadModels = platformProductUploadService.getSxWorkloadWithChannelIdCartId(
                CmsConstants.PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE, channelId, cartId);
        if (ListUtils.isNull(sxWorkloadModels)) {
            $error("上新任务表中没有该渠道和平台对应的任务列表信息！[ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }

        // 从cms_mt_channel_condition_mapping_config表中取得该渠道，平台对应的客户过来的类目id和天猫平台一级类目之间的mapping关系数据
        Map<String, String> conditionMappingParamMap = new HashMap<>();
        conditionMappingParamMap.put("channelId", channelId);
        conditionMappingParamMap.put("cartId", StringUtils.toString(cartId));
        conditionMappingParamMap.put("propName", "tt_category");   // 天猫同购一级类目匹配
        List<CmsMtChannelConditionMappingConfigModel> conditionMappingConfigModels =
                cmsMtChannelConditionMappingConfigDao.selectList(conditionMappingParamMap);
        if (ListUtils.isNull(conditionMappingConfigModels)) {
            $error("cms_mt_channel_condition_mapping_config表中没有该渠道和平台对应的天猫同购一级类目匹配信息！[ChannelId:%s] " +
                    "[CartId:%s] [propName:%s]", channelId, cartId, "tt_category");
            return;
        }
        Map<String, String> conditionMappingMap = new HashMap<>();
        conditionMappingConfigModels.forEach(p -> conditionMappingMap.put(p.getMapKey(), p.getMapValue()));

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolCnt);
        // 根据上新任务列表中的groupid循环上新处理
        for (CmsBtSxWorkloadModel cmsBtSxWorkloadModel : sxWorkloadModels) {
            // 启动多线程
            executor.execute(() -> uploadProduct(cmsBtSxWorkloadModel, shopProp, tmTonggouFeedAttrList, conditionMappingMap));
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
     * @param tmTonggouFeedAttrList List<String> 当前渠道和平台设置的可以天猫官网同购上传的feed attribute列表
     * @param conditionMappingMap 当前渠道和平台设置的天猫同购一级类目匹配信息map
     */
    public void uploadProduct(CmsBtSxWorkloadModel cmsBtSxWorkloadModel, ShopBean shopProp,
                              List<String> tmTonggouFeedAttrList, Map<String, String> conditionMappingMap) {

        // 当前groupid(用于取得产品信息)
        long groupId = cmsBtSxWorkloadModel.getGroupId();
        // 渠道id
        String channelId = shopProp.getOrder_channel_id();
        // 平台id
        int cartId = Integer.parseInt(shopProp.getCart_id());
        // 上新数据
        SxData sxData = null;
        // 商品id
        String numIId = "";
        // 新增或更新商品标志
        boolean updateWare = false;

        try {
            // 上新用的商品数据信息取得
            sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);
            if (sxData == null) {
                throw new BusinessException("取得上新用的商品数据信息失败！请向管理员确认 [sxData=null]");
            }
            // 如果取得上新对象商品信息出错时，报错
            if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
                String errorMsg = sxData.getErrorMessage();
//                sxData.setErrorMessage(""); // 这里设为空之后，异常捕捉到之后msg前面会加上店铺名称
                // 取得上新数据出错时，cartId有可能没有设置
                sxData.setCartId(cartId);
                // 有错误的时候，直接报错
                throw new BusinessException(errorMsg);
            }

            // 主产品等列表取得
            CmsBtProductModel mainProduct = sxData.getMainProduct();
            List<CmsBtProductModel> cmsBtProductList = sxData.getProductList();
            List<BaseMongoMap<String, Object>> skuList = sxData.getSkuList();

            // 没有lock并且已Approved的产品列表为空的时候,中止该产品的上新流程
            if (ListUtils.isNull(cmsBtProductList)) {
                String errMsg = "未被锁定且已完成审批的产品列表为空";
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

            // 主产品取得结果判断
            if (mainProduct == null) {
                String errMsg = "取得主商品信息失败 [mainProduct=null]";
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

            // 如果产品没有common信息，数据异常不上新
            if (mainProduct.getCommon() == null || mainProduct.getCommon().getFields() == null) {
                String errMsg = "取得主商品common信息失败";
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

            // 构造该产品所有SKUCODE的字符串列表
            List<String> strSkuCodeList = new ArrayList<>();
            skuList.forEach(sku -> strSkuCodeList.add(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name())));

            // 如果已Approved产品skuList为空，则中止该产品的上新流程
            // 如果已Approved产品skuList为空，则把库存表里面所有的数据（几万条）数据全部查出来了，很花时间
            if (ListUtils.isNull(strSkuCodeList)) {
                String errMsg = "已完成审批的产品sku列表为空";
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

            // 从cms_mt_channel_config表中取得上新用价格配置项目名(例：31.sx_price对应的价格项目，有可能priceRetail, 有可能是priceMsrp)
            String priceConfigValue = getPriceConfigValue(sxData.getChannelId(), StringUtils.toString(cartId),
                    CmsConstants.ChannelConfig.PRICE_SX_PRICE);
            if (StringUtils.isEmpty(priceConfigValue)) {
                String errMsg = String.format("从cms_mt_channel_config表中未能取得该店铺设置的上新用价格配置项目！ [config_key:%s]",
                        StringUtils.toString(cartId) + CmsConstants.ChannelConfig.PRICE_SX_PRICE);
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

            // 如果skuList不为空，取得所有sku的库存信息
            // 为了对应MiniMall的场合， 获取库存的时候要求用getOrgChannelId()（其他的场合仍然是用channelId即可）
            Map<String, Integer> skuLogicQtyMap = productService.getLogicQty(mainProduct.getOrgChannelId(), strSkuCodeList);

            // 取得主产品天猫同购平台设置信息(包含SKU等信息)
            CmsBtProductModel_Platform_Cart mainProductPlatformCart = mainProduct.getPlatform(cartId);
            if (mainProductPlatformCart == null) {
                $error(String.format("获取主产品天猫同购平台设置信息(包含SKU，Schema属性值等信息)失败！[ProductCode:%s][CartId:%s]",
                        mainProduct.getCommon().getFields().getCode(), cartId));
                throw new BusinessException("获取主产品天猫同购平台设置信息(包含SKU，Schema属性值等信息)失败");
            }

            // 获取字典表cms_mt_platform_dict(根据channel_id)上传图片的规格等信息
            List<CmsMtPlatformDictModel> cmsMtPlatformDictModelList = dictService.getModesByChannelCartId(channelId, cartId);
            if (cmsMtPlatformDictModelList == null || cmsMtPlatformDictModelList.size() == 0) {
                $error(String.format("获取cms_mt_platform_dict字典表数据（属性图片模板等）失败 [ChannelId:%s] [CartId:%s]", channelId, cartId));
                throw new BusinessException("获取cms_mt_platform_dict字典表数据（属性图片模板等）失败");
            }

            // 判断新增商品还是更新商品
            // 只要numIId不为空，则为更新商品
            if (!StringUtils.isEmpty(sxData.getPlatform().getNumIId())) {
                // 更新商品
                updateWare = true;
                // 取得更新对象商品id
                numIId = sxData.getPlatform().getNumIId();
            }

            // 编辑天猫国际官网同购共通属性
            BaseMongoMap<String, String> productInfoMap = getProductInfo(sxData, shopProp, priceConfigValue,
                    skuLogicQtyMap, tmTonggouFeedAttrList, conditionMappingMap, updateWare);

            // 构造Field列表
            List<Field> itemFieldList = new ArrayList<>();
            productInfoMap.entrySet().forEach(p -> {
                InputField inputField = new InputField();
                inputField.setId(p.getKey());
                inputField.setValue(p.getValue());
//                inputField.setType(FieldTypeEnum.INPUT);
                itemFieldList.add(inputField);
            });

            // 转换成XML格式(root元素为<itemRule>)
            String productInfoXml = "";
            if (ListUtils.notNull(itemFieldList))
                productInfoXml = SchemaWriter.writeParamXmlString(itemFieldList);

            // 测试用输入XML内容
            $debug(productInfoXml);

            String result;
            // 新增或更新商品主处理
            if (!updateWare) {
                // 新增商品的时候
                result = tbSimpleItemService.addSimpleItem(shopProp, productInfoXml);
			} else {
                // 更新商品的时候
                result = tbSimpleItemService.updateSimpleItem(shopProp, NumberUtils.toLong(numIId), productInfoXml);
            }

			if ("ERROR:15:isv.invalid-parameter::该类目没有颜色销售属性,不能上传图片".equals(result)) {
				// 用simple的那个sku， 覆盖到原来的那个sku上
				int idxOrg = -1;
				String strSimpleSkuValue = null;
				for (int i = 0; i < itemFieldList.size(); i++) {
					Field field = itemFieldList.get(i);
					if (field.getId().equals("skus_simple")) {
						InputField inputField = (InputField) field;
						if (!StringUtils.isEmpty(inputField.getValue())) {
							strSimpleSkuValue = inputField.getValue();
						}
					} else if (field.getId().equals("skus")) {
						idxOrg = i;
					}
				}
				if (!StringUtils.isEmpty(strSimpleSkuValue) && idxOrg != -1) {
					// 找到如果设置过值的话， 再给一次机会尝试一下
					// 如果没设置过值的话， 就说明前面已经判断过， 这个商品有多个sku， 没办法使用这种方式
					((InputField)itemFieldList.get(idxOrg)).setValue(strSimpleSkuValue);

					productInfoXml = SchemaWriter.writeParamXmlString(itemFieldList);

					if (!updateWare) {
						// 新增商品的时候
						result = tbSimpleItemService.addSimpleItem(shopProp, productInfoXml);
					} else {
						// 更新商品的时候
						result = tbSimpleItemService.updateSimpleItem(shopProp, NumberUtils.toLong(numIId), productInfoXml);
					}
				}
			}

			if (!StringUtils.isEmpty(result) && result.startsWith("ERROR:")) {
                // 天猫官网同购新增/更新商品失败时
                String errMsg = "天猫官网同购新增商品时出现错误! ";
                if (updateWare) {
                    errMsg = "天猫官网同购更新商品时出现错误! ";
                }
                errMsg += result;
                $error(errMsg);
                throw new BusinessException(errMsg);
            } else {
                // 天猫官网同购新增/更新商品成功时
                if (!updateWare) numIId = result;
            }

            // 调用淘宝商品上下架操作(新增的时候默认为下架，只有更新的时候才根据group里面platformActive调用上下架操作)
            // 回写用商品上下架状态(OnSale/InStock)
            CmsConstants.PlatformStatus platformStatus = null;
            CmsConstants.PlatformActive platformActive = sxData.getPlatform().getPlatformActive();
            // 更新商品并且PlatformActive=ToOnSale时,执行商品上架；新增商品或PlatformActive=ToInStock时，执行下架功能
            if (updateWare && platformActive == CmsConstants.PlatformActive.ToOnSale) {
                platformStatus = CmsConstants.PlatformStatus.OnSale;   // 上架
            } else {
                platformStatus = CmsConstants.PlatformStatus.InStock;   // 在库
            }

            // 更新特价宝
            updateTeJiaBaoPromotion(sxData);

            // 回写PXX.pCatId, PXX.pCatPath等信息
            Map<String, String> pCatInfoMap = getSimpleItemCatInfo(shopProp, numIId);
            if (pCatInfoMap != null && pCatInfoMap.size() > 0) {
                // 上新成功且成功取得平台类目信息时状态回写操作(默认为在库)
                sxProductService.doUploadFinalProc(shopProp, true, sxData, cmsBtSxWorkloadModel, numIId,
                        platformStatus, "", getTaskName(), pCatInfoMap);
            } else {
                // 上新成功时但未取得平台类目信息状态回写操作(默认为在库)
                sxProductService.doUploadFinalProc(shopProp, true, sxData, cmsBtSxWorkloadModel, numIId,
                        platformStatus, "", getTaskName());
            }

            // 正常结束
            $info(String.format("天猫官网同购商品上新成功！[ChannelId:%s] [CartId:%s] [GroupId:%s] [NumIId:%s]",
                    channelId, cartId, groupId, numIId));
        } catch (Exception ex) {
            // 异常结束时
            String errMsg = String.format(" 天猫官网同购上新异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [NumIId:%s]",
                    channelId, cartId, groupId, numIId);
            $error(errMsg);

            if (sxData == null) {
                // 回写详细错误信息表(cms_bt_business_log)用
                sxData = new SxData();
                sxData.setChannelId(channelId);
                sxData.setCartId(cartId);
                sxData.setGroupId(groupId);
                sxData.setErrorMessage(shopProp.getShop_name() + " 天猫同购取得上新用的商品数据信息异常,请跟管理员联系! [上新数据为null]");
            }
            // 如果上新数据中的errorMessage为空
            if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                // nullpoint错误的处理
                if(StringUtils.isNullOrBlank2(ex.getMessage())) {
                    ex.printStackTrace();
                    sxData.setErrorMessage(shopProp.getShop_name() + " 天猫同购上新时出现不可预知的错误，请跟管理员联系! "
                            + ex.getStackTrace()[0].toString());
                } else {
                    sxData.setErrorMessage(shopProp.getShop_name() + " " +ex.getMessage());
                }
            }

            // 上新出错时状态回写操作
            sxProductService.doUploadFinalProc(shopProp, false, sxData, cmsBtSxWorkloadModel, "", null, "", getTaskName());
        }
    }

    /**
     * 设置天猫同购上新产品用共通属性
     *
     * @param sxData sxData 上新产品对象
     * @param shopProp ShopBean  店铺信息
     * @param priceConfigValue String 该店铺上新用项目名
     * @param skuLogicQtyMap Map<String, Integer>  SKU逻辑库存
     * @param tmTonggouFeedAttrList List<String> 当前渠道和平台设置的可以天猫官网同购上传的feed attribute列表
     * @param conditionMappingMap 当前渠道和平台设置的天猫同购一级类目匹配信息map
     * @param updateWare 新增/更新flg(false:新增 true:更新)
     * @return JdProductBean 京东上新用bean
     * @throws BusinessException
     */
    private BaseMongoMap<String, String> getProductInfo(SxData sxData, ShopBean shopProp, String priceConfigValue,
                                                 Map<String, Integer> skuLogicQtyMap, List<String> tmTonggouFeedAttrList,
                                                 Map<String, String> conditionMappingMap,
                                                 boolean updateWare) throws BusinessException {
        // 上新产品信息保存map
        BaseMongoMap<String, String> productInfoMap = new BaseMongoMap<>();

        CmsBtProductModel mainProduct = sxData.getMainProduct();
        CmsBtFeedInfoModel feedInfo = sxData.getCmsBtFeedInfoModel();
        List<CmsBtProductModel> productList = sxData.getProductList();
        List<BaseMongoMap<String, Object>> skuList = sxData.getSkuList();
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 天猫国际官网同购平台（或USJOI天猫国际官网同购平台）
        CmsBtProductModel_Platform_Cart mainProductPlatformCart = mainProduct.getPlatform(sxData.getCartId());

        // 标题(必填)
        // 商品标题支持英文到中文，韩文到中文的自动翻译，可以在extends字段里面进行设置是否需要翻译
        // 注意：使用测试账号的APPKEY测试时，标题应包含中文"测试请不要拍"
        String valTitle = "";
        if (mainProductPlatformCart != null && mainProductPlatformCart.getFields() != null
                && !StringUtils.isEmpty(mainProductPlatformCart.getFields().getStringAttribute("title"))) {
            // 画面上输入的platform的fields中的标题 (格式：<value>测试请不要拍 title</value>)
            valTitle = mainProductPlatformCart.getFields().getStringAttribute("title");
        } else if (!StringUtils.isEmpty(mainProduct.getCommon().getFields().getStringAttribute("originalTitleCn"))) {
            // common中文长标题
            valTitle = mainProduct.getCommon().getFields().getStringAttribute("originalTitleCn");
        } else if (!StringUtils.isEmpty(mainProduct.getCommon().getFields().getStringAttribute("productNameEn"))) {
            // common英文长标题
            valTitle = mainProduct.getCommon().getFields().getStringAttribute("productNameEn");
        }
//        productInfoMap.put("title", "测试请不要拍 " + valTitle);
        productInfoMap.put("title", valTitle);

        // 类目(必填)
        // 注意：使用天猫授权类目ID发布时，必须使用叶子类目的ID
        // 注意：使用商家自有系统类目路径发布时，不同层级的类目，使用&gt;进行分隔；使用系统匹配时，会有一定的badcase,
        //      商品上架前，建议商家做自查，查看商品是否被匹配到了正确的类目
        String valCategory = "";
        if (mainProductPlatformCart != null
                && !StringUtils.isEmpty(mainProductPlatformCart.getpCatId())) {
            // 画面上输入的platform获得授权的叶子类目ID (格式：<value>{"cat_id":"50012036"}</value>)
            Map<String, Object> paramCategory = new HashMap<>();
            paramCategory.put("cat_id", mainProductPlatformCart.getpCatId());
            valCategory = JacksonUtil.bean2Json(paramCategory);
        } else if (feedInfo != null && !StringUtils.isEmpty(feedInfo.getCategory())) {
            // 使用商家自有系统类目路径
            // feed_info表的category（将中杠【-】替换为：【&gt;】(>)） (格式：<value>man&gt;sports&gt;socks</value>)

			// TODO 测试代码, 这里需要改进
//            valCategory = feedInfo.getCategory().replaceAll("-", "&gt;");
//            valCategory = "居家布艺";

            String tmallCatKey = getValueFromPageOrCondition("tmall_category_key", "", mainProductPlatformCart, sxData, shopProp);
            if (!StringUtils.isEmpty(tmallCatKey)) {
                if (conditionMappingMap != null && conditionMappingMap.containsKey(tmallCatKey)) {
                    valCategory = conditionMappingMap.get(tmallCatKey);
                } else {
                    String errMsg = String.format("从cms_mt_channel_condition_mapping_config表中没有取到客户类目对应的天猫" +
                            "平台一级类目信息，中止上新！[Mapkey:%s]", tmallCatKey);
                    $error(errMsg);
                    throw new BusinessException(errMsg);
                }
            } else {
                String errMsg = String.format("从cms_mt_channel_condition_config表中没有取到客户类目key配置信息,导致不能取得对应的天猫" +
                        "平台一级类目信息，中止上新！");
                $error(errMsg);
                throw new BusinessException(errMsg);
            }
        }
        productInfoMap.put("category", valCategory);

        // 商品属性(非必填)
        // 商品的一些属性，可以通过property字段来进行填写，该字段为非必填字段，如果商家有对商品的更为具体的属性信息，
        // 可以都进行填写。格式为"Key":"Value"
        // 注意：所有的属性字段，天猫国际系统会进行自动匹配，如果是天猫对应的该类目的标准属性，则会显示在商品的detail页面
        String valProperty = "";
        if (feedInfo != null && feedInfo.getAttribute() != null && feedInfo.getAttribute().size() > 0) {
            // 画面上输入的platform获得授权的叶子类目ID (格式：<value>{"material":"cotton","gender":"men","color":"grey"}</value>)
            Map<String, Object> paramProperty = new HashMap<>();

            Map<String, List<String>> feedAttribute = feedInfo.getAttribute();
            // 误 -> 如果cms_bt_tm_tonggou_feed_attr表中没有配置当前渠道和平台可以上传的feed attribute属性，
            // 误 -> 则认为可以上传全部feed attribute属性
			// 没有配置就不要设置
            if (ListUtils.isNull(tmTonggouFeedAttrList)) {
//                feedAttribute.entrySet().forEach(p -> {
//                    List<String> attrValueList = p.getValue();
//                    // feed.Attrivute里面的value是一个List，有多个值，用逗号分隔
//                    String value = Joiner.on(Separtor_Coma).join(attrValueList);
//                    paramProperty.put(p.getKey(), value);
//                });
            } else {
                // 如果cms_bt_tm_tonggou_feed_attr表中配置了当前渠道和平台可以上传的feed attribute属性，
                // 则只上传表中配置过的feed attribute属性
                feedAttribute.entrySet().forEach(p -> {
                    // 只追加在表中配置过的eed attribute属性
                    if (tmTonggouFeedAttrList.contains(p.getKey())) {
                        List<String> attrValueList = p.getValue();
                        // feed.Attrivute里面的value是一个List，有多个值，用逗号分隔
                        String value = Joiner.on(Separtor_Coma).join(attrValueList);
                        paramProperty.put(p.getKey(), value);
                    } else {
                        $debug("在cms_bt_tm_tonggou_feed_attr表中没有配置了该feed attribute属性，不上传该feed属性. " +
                                "[feed attribute:" + p.getKey() + "]");
                    }
                });
            }

            valProperty = JacksonUtil.bean2Json(paramProperty);
        }
        productInfoMap.put("property", valProperty);

        // 品牌(必填)
        // 商品品牌的值只支持英文，中文
        // 注意：天猫国际系统会进行品牌的匹配，部分品牌因在天猫品牌库中不存在，因为不一定全部品牌都能成功匹配。
        //      匹配成功的品牌，会出现在商品详情页面;
        //      如果无法匹配，且商家希望显示品牌的，建议商家通过商家后台申请新品牌。
        String valBrand = "";
        if (mainProduct.getCommon() != null && mainProduct.getCommon().getFields() != null
                && !StringUtils.isEmpty(mainProduct.getCommon().getFields().getStringAttribute("brand"))) {
            // common中的品牌 (格式：<value>nike</value>)
            valBrand = mainProduct.getCommon().getFields().getStringAttribute("brand");
        }
        productInfoMap.put("brand", valBrand);

        // 为什么要这段内容呢， 因为发生了一件很奇怪的事情， 曾经上新成功的商品， 更新的时候提示说【id:xxx还没有成为品牌】
        // 所以使用之前上过的品牌
        {
            // 如果已经上新过了的话， 使用曾经上新过的品牌
            if (!StringUtils.isEmpty(sxData.getPlatform().getNumIId())) {
                // 取得更新对象商品id
                String numIId = sxData.getPlatform().getNumIId();
                try {
                    Map<String, String> pCatInfoMap = getSimpleItemCatInfo(shopProp, numIId);
                    if (pCatInfoMap.containsKey("brand")) {
                        productInfoMap.put("brand", pCatInfoMap.get("brand"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // 主图(必填)
        // 最少1张，最多5张。多张图片之间，使用英文的逗号进行分割。需要使用alicdn的图片地址。建议尺寸为800*800像素。
        // 格式：<value>http://img.alicdn.com/imgextra/i1/2640015666/TB2PTFYkXXXXXaUXpXXXXXXXXXX_!!2640015666.jpg,
        //      http://img.alicdn.com/imgextra/~~</value>
        String valMainImages = "";
        // 解析cms_mt_platform_dict表中的数据字典
        String mainPicUrls = getValueByDict("天猫同购商品主图5张", expressionParser, shopProp);
        if (!StringUtils.isNullOrBlank2(mainPicUrls)) {
            // 去掉末尾的逗号
            valMainImages = mainPicUrls.substring(0, mainPicUrls.lastIndexOf(Separtor_Coma));
        }
        productInfoMap.put("main_images", valMainImages);

        // 描述(必填)
        // 商品描述支持HTML格式，但是需要将内容变成XML格式。
        // 为了更好的用户体验，建议全部使用图片来做描述内容。描述的图片宽度不超过800像素.
        // 格式：<value>&lt;img align="middle" src="http://img.alicdn.com/imgextra/i1/2640015666/TB2islBkXXXXXXBXFXXXXXXXXXX_!!2640015666.jpg"
        //      /&gt; &lt;br&gt;&lt;img align="middle" src="http://img.alicdn.com/imgextra/i1/2640015666/~~</value>
        // 解析cms_mt_platform_dict表中的数据字典
        String valDescription = getValueByDict("天猫同购描述", expressionParser, shopProp);
        productInfoMap.put("description", valDescription);

        // 物流信息(必填)
        // 物流字段：weight值为重量，用于计算运费，单位是千克;
        //         volumn值为体积，由于运费都是基于重量，这个值可以随便填写;
        //         templete_id为物流模板;
        //         province&city值，非港澳台的地区，直接填写中文的国家即可;
        //         start_from为货源地;
        // 格式:<value>{"weight":"1.5","volume":"0.0001","template_id":"243170100","province":"美国","city":"美国"}</value>
        Map<String, Object> paramLogistics = new HashMap<>();
        // 物流重量 // TODO 这里要改进
        paramLogistics.put("weight", getValueFromPage("logistics_weight", "1", mainProductPlatformCart));
        // 物流体积 // TODO 这里是要改成从condition表获取
        paramLogistics.put("volume", getValueFromPage("logistics_volume", "1", mainProductPlatformCart));
        // 物流模板ID
        paramLogistics.put("template_id", getValueFromPageOrCondition("logistics_template_id", "", mainProductPlatformCart, sxData, shopProp));
        // 省(国家)
        paramLogistics.put("province", getValueFromPageOrCondition("logistics_province", "", mainProductPlatformCart, sxData, shopProp));
        // 城市
        paramLogistics.put("city", getValueFromPageOrCondition("logistics_city", "", mainProductPlatformCart, sxData, shopProp));
        // 货源地
        paramLogistics.put("start_from", getValueFromPageOrCondition("logistics_start_from", "", mainProductPlatformCart, sxData, shopProp));

        productInfoMap.put("logistics", JacksonUtil.bean2Json(paramLogistics));

        // skus(必填)
        // cms_mt_channel_condition_config表中入关方式(cross_border_report)
        // 设置成true跨境申报后，商品的每个SKU必须带有HSCODE才可以上架，
        // 如果设置成false邮关申报后，商品不需要设置HSCODE
        String crossBorderRreportFlg = getValueFromPageOrCondition("extends_cross_border_report", "", mainProductPlatformCart, sxData, shopProp);
        // 取得天猫同购上新用skus列表
        List<BaseMongoMap<String, Object>> targetSkuList_0 = getSkus(0, sxData.getCartId(), productList, skuList,
                priceConfigValue, skuLogicQtyMap, expressionParser, shopProp, crossBorderRreportFlg);

        productInfoMap.put("skus", JacksonUtil.bean2Json(targetSkuList_0));
		if (skuList.size() == 1) {
			// 只有一个sku的场合， 万一天猫自动匹配的类目只允许一个sku的时候， 可以用上
			List<BaseMongoMap<String, Object>> targetSkuList_1 = getSkus(1, sxData.getCartId(), productList, skuList,
					priceConfigValue, skuLogicQtyMap, expressionParser, shopProp, crossBorderRreportFlg);
			productInfoMap.put("skus_simple", JacksonUtil.bean2Json(targetSkuList_1));
		} else {
			// 多个sku的场合， 万一天猫自动匹配的类目只允许一个sku的时候， 就上新不了了
			productInfoMap.put("skus_simple", null);
		}

        // 扩展(部分必填)
        // 该字段主要控制商品的部分备注信息等，其中部分字段是必须填写的，非必填的字段部分可以完全不用填写。
        // 且其中的部分字段，可以做好统一配置，做好配置的，不需要每个商品发布时都提交.
        Map<String, Object> paramExtends = new HashMap<>();
        // 官网来源代码(必填)   商品详情中会显示来源的国家旗帜
        // 主要国家代码：美国/US 英国/UK 澳大利亚/AU 加拿大/CA 德国/DE 西班牙/ES 法国/FR 香港/HK 意大利/IT 日本/JP
        //             韩国/KR 荷兰/NL 新西兰/NZ 台湾/TW 新加坡/SG
        paramExtends.put("nationality", getValueFromPageOrCondition("extends_nationality", "", mainProductPlatformCart, sxData, shopProp));
        // 币种代码(必填)      用于区分币种，选择后会根据price值和币种，根据支付宝的汇率计算人民币价格
        // 主要币种代码：人民币/CNY 港币/HKD 新台币/TWD 美元/USD 英镑/GBP 日元/JPY 韩元/KRW 欧元/EUR 加拿大元/CAD
        //             澳元/AUD 新西兰元/NZD
        paramExtends.put("currency_type", getValueFromPageOrCondition("extends_currency_type", "", mainProductPlatformCart, sxData, shopProp));
        // 是否需要自动翻译(必填)  (如果标题是中文，那么就是false，否则就是true)
        String extends_translate = "";
        if (mainProductPlatformCart != null && mainProductPlatformCart.getFields() != null
                && !StringUtils.isEmpty(mainProductPlatformCart.getFields().getStringAttribute("title"))) {
            extends_translate = "false";
        } else if (!StringUtils.isEmpty(mainProduct.getCommon().getFields().getStringAttribute("originalTitleCn"))) {
            extends_translate = "false";
        } else if (StringUtils.isEmpty(StringUtils.toString(productInfoMap.get("title")))) {
            extends_translate = "false";
        } else {
            extends_translate = "true";
        }
        paramExtends.put("translate", extends_translate);
        // 商品原始语言(必填)
        // 主要语言代码为：中文/zh 中文繁体/zt 英文/en 韩文/ko
        paramExtends.put("source_language", getValueFromPageOrCondition("extends_source_language", "", mainProductPlatformCart, sxData, shopProp));
        // 官网名称(必填)     网站的名称，如果美国某某网站，可以做配置，配置后，可以不需要发布的时候填写
        paramExtends.put("website_name", getValueFromPageOrCondition("extends_website_name", "", mainProductPlatformCart, sxData, shopProp));
        // 官网商品地址(必填)  商品在海外网址的地址，如果无法确保一一对应，可以先填写网站url
        paramExtends.put("website_url", getValueFromPageOrCondition("extends_website_url", "", mainProductPlatformCart, sxData, shopProp));
        // 参考价格(非必填)    商品的参考价格，如果大于现在的价格，则填写
//        paramExtends.put("foreign_origin_price", getValueFromPageOrCondition("extends_foreign_origin_price", "", mainProductPlatformCart, sxData, shopProp));
        // 是否使用原标题(false自动插入商品关键词）(非必填)  填写true表示使用原始标题，false表示需要插入关键词，不填写默认为不需要插入关键词
        paramExtends.put("original_title", getValueFromPageOrCondition("extends_original_title", "", mainProductPlatformCart, sxData, shopProp));
        // 店铺内分类id(非必填)  格式："shop_cats":"111111,222222,333333"
        String extends_shop_cats = "";
        if (mainProductPlatformCart != null && mainProductPlatformCart.getFields() != null
                && ListUtils.notNull(mainProductPlatformCart.getSellerCats())) {
            List<String> sellerCatIdList = new ArrayList<>();
            for (CmsBtProductModel_SellerCat sellerCat : mainProductPlatformCart.getSellerCats()) {
                if (!StringUtils.isEmpty(sellerCat.getcId())) {
                    sellerCatIdList.add(sellerCat.getcId());
                }
            }
            if (ListUtils.notNull(sellerCatIdList)) {
                extends_shop_cats = Joiner.on(Separtor_Coma).join(sellerCatIdList);
            }
        }
        paramExtends.put("shop_cats", extends_shop_cats);
        // 是否包税(非必填)     标识是否要报税，true表示报税，false表示不报税，不填写默认为不报税
        // 注意：跨境申报的商品，是不允许报税的，即便设置了报税，商品在交易的过程中，也需要支付税费
        paramExtends.put("tax_free", getValueFromPageOrCondition("extends_tax_free", "", mainProductPlatformCart, sxData, shopProp));
        // 尺码表图片(非必填)    暂不设置
//        paramExtends.put("international_size_table", getValueFromPageOrCondition("extends_international_size_table", "", mainProductPlatformCart, sxData, shopProp));
        // 是否单独发货(非必填)   true表示单独发货，false表示需要不做单独发货，不填写默认为不单独发货
        paramExtends.put("delivery_separate", getValueFromPageOrCondition("extends_delivery_separate", "", mainProductPlatformCart, sxData, shopProp));
        // 是否支持退货
        paramExtends.put("support_refund", getValueFromPageOrCondition("extends_support_refund", "", mainProductPlatformCart, sxData, shopProp));
        // 官网是否热卖(非必填)    表示是否在官网是热卖商品，true表示热卖，false表示非热卖，不填写默认为非热卖
        paramExtends.put("hot_sale", getValueFromPageOrCondition("extends_hot_sale", "", mainProductPlatformCart, sxData, shopProp));
        // 在官网是否是新品(非必填) 表示是否在官网是新品，true表示新品，false表示非新品，不填写默认为非新品
        paramExtends.put("new_goods", getValueFromPageOrCondition("extends_new_goods", "", mainProductPlatformCart, sxData, shopProp));
        // 入关方式 跨境申报/邮关(必填)     true表示跨境申报，false表示邮关申报
        // 根据中国海关4月8日的最新规定，天猫国际的商品必须设置入关方式，入关方式有2种：
        // 1.跨境申报，即每单交易都向海关申报，单单交税；
        // 2.邮关申报，即通过万国邮联的方式快递包裹，有几率抽检；
        // 说明：设置成跨境申报后，商品的每个SKU必须带有HSCODE才可以上架，设置成邮关申报后，商品不需要设置HSCODE
        paramExtends.put("cross_border_report", getValueFromPageOrCondition("extends_cross_border_report", "", mainProductPlatformCart, sxData, shopProp));

        productInfoMap.put("extends", JacksonUtil.bean2Json(paramExtends));

        // 无线描述(选填)
        // 解析cms_mt_platform_dict表中的数据字典
		if (mainProduct.getCommon().getFields().getAppSwitch() != null &&
				mainProduct.getCommon().getFields().getAppSwitch() == 1) {
			productInfoMap.put("wireless_desc", getValueByDict("天猫同购无线描述", expressionParser, shopProp));
		}

        // 商品上下架
        CmsConstants.PlatformActive platformActive = sxData.getPlatform().getPlatformActive();
        // 更新商品并且PlatformActive=ToOnSale时,执行商品上架；新增商品或PlatformActive=ToInStock时，执行下架功能
        if (updateWare && platformActive == CmsConstants.PlatformActive.ToOnSale) {
            productInfoMap.put("status", "0");   // 商品上架
        } else {
            productInfoMap.put("status", "2");   // 商品在库
        }

        return productInfoMap;
    }

    /**
     * 取得画面上输入的项目对应的值
     *
     * @param itemName 项目名字(如："logistics_weight")
     * @param defaultValue 如果未取到对应的值时，返回的默认值
     * @param mainProductPlatformCart 产品中的分平台信息
     * @return 项目对应的值
     * @throws Exception
     */
    private String getValueFromPage(String itemName, String defaultValue, CmsBtProductModel_Platform_Cart mainProductPlatformCart)  {
        String value = "";
        if (mainProductPlatformCart != null && mainProductPlatformCart.getFields() != null
                && !StringUtils.isEmpty(mainProductPlatformCart.getFields().getStringAttribute(itemName))) {
            // 画面上输入的platform的fields中的项目值
            value = mainProductPlatformCart.getFields().getStringAttribute(itemName);
        }

        return StringUtils.isEmpty(value) ? defaultValue : value;
    }

    /**
     * 取得画面上输入的项目对应的值，如果未取到则读取cms_mt_channel_condition_config表配置的值
     *
     * @param itemName 项目名字(如："logistics_weight")
     * @param defaultValue 如果未取到对应的值时，返回的默认值
     * @param mainProductPlatformCart 产品中的分平台信息
     * @param sxData 上新数据
     * @param shopProp 店铺信息
     * @return 项目对应的值
     */
    private String getValueFromPageOrCondition(String itemName, String defaultValue,
                                               CmsBtProductModel_Platform_Cart mainProductPlatformCart,
                                               SxData sxData, ShopBean shopProp)  {
        String value = "";
        if (mainProductPlatformCart != null && mainProductPlatformCart.getFields() != null
                && !StringUtils.isEmpty(mainProductPlatformCart.getFields().getStringAttribute(itemName))) {
            // 画面上输入的platform的fields中的项目值
            value = mainProductPlatformCart.getFields().getStringAttribute(itemName);
        } else {
            // 解析cms_mt_channel_condition_config表中的数据字典取得"项目名_XX"(XX为cartId)对应的值
            value = getConditionPropValue(sxData, itemName, shopProp);
        }

        return StringUtils.isEmpty(value) ? defaultValue : value;
    }


    /**
     * 读取字典对应的值，如果返回空字符串则抛出异常
     *
     * @param dictName 字典名字(如："商品主图5张")
     * @param expressionParser 解析子
     * @param shopProp 店铺信息
     * @return 解析出来值（如果为多张图片URL，用逗号分隔)
     * @throws Exception
     */
    private String getValueByDict(String dictName, ExpressionParser expressionParser, ShopBean shopProp)  {
        String result = "";
        try {
            // 解析字典，取得对应的值
            result = sxProductService.resolveDict(dictName, expressionParser, shopProp, getTaskName(), null);
            if(StringUtils.isNullOrBlank2(result))
            {
                String errorMsg = String.format("字典解析的结果为空! (猜测有可能是字典不存在或者素材管理里的共通图片没有一张图片成功上传到平台) " +
                        "[dictName:%s]", dictName);
                $error(errorMsg);
                throw new BusinessException(errorMsg);
            }
        } catch (Exception e) {
            String errorMsg = "";
            // 如果字典解析异常的errorMessage为空
            if (StringUtils.isNullOrBlank2(e.getMessage())) {
                // nullpoint错误的处理
                errorMsg = "天猫同购上新字典解析时出现不可预知的错误，请跟管理员联系. " + e.getStackTrace()[0].toString();
                e.printStackTrace();
            } else {
                errorMsg = e.getMessage();
            }
            throw new BusinessException(errorMsg);
        }

        return result;
    }

    /**
     * 从cms_mt_channel_condition_config表中取得指定类型的值
     *
     * @param shop ShopBean 店铺对象
     * @param sxData SxData 产品对象
     * @param prePropId String 条件表达式前缀(运费模板:transportid_ 关联版式:commonhtml_id_)
     * @return String 指定类型(如：运费模板id或关联版式id等)对应的值
     */
    private String getConditionPropValue(SxData sxData, String prePropId, ShopBean shop) {

        // 运费模板id或关联版式id返回用
        String  resultStr = "";
        // 条件表达式前缀(运费模板:transportid_ 关联版式:commonhtml_id_)
        // 条件表达式表platform_prop_id字段的检索条件为条件表达式前缀加cartId
        String platformPropId = prePropId + "_" + sxData.getCartId();

        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 根据channelid和platformPropId取得cms_mt_channel_condition_config表的条件表达式
        List<ConditionPropValueModel> conditionPropValueModels = conditionPropValueRepo.get(sxData.getChannelId(), platformPropId);

        // 使用运费模板或关联版式条件表达式
        if (ListUtils.isNull(conditionPropValueModels))
            return resultStr;

        try {
            RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
            for (ConditionPropValueModel conditionPropValueModel : conditionPropValueModels) {
                String conditionExpressionStr = conditionPropValueModel.getCondition_expression().trim();
                RuleExpression conditionExpression;
                String propValue;

                // 带名字字典解析
                if (conditionExpressionStr.startsWith("{\"type\":\"DICT\"")) {
                    DictWord conditionDictWord = (DictWord) ruleJsonMapper.deserializeRuleWord(conditionExpressionStr);
                    conditionExpression = conditionDictWord.getExpression();
                } else if (conditionExpressionStr.startsWith("{\"ruleWordList\"")) {
                    // 不带名字，只有字典表达式字典解析
                    conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
                } else {
                    String errMsg = String.format("cms_mt_channel_condition_config表中数据字典的格式不对 [ChannelId:%s]" +
                            " [CartId:%s] [DictName:%s]", sxData.getChannelId(), sxData.getCartId(), prePropId);
//                        logIssue(getTaskName(), errMsg);
                    $info(errMsg);
                    continue;
                }

                // 解析出字典对应的值
                if (shop != null) {
                    propValue = expressionParser.parse(conditionExpression, shop, getTaskName(), null);
                } else {
                    propValue = expressionParser.parse(conditionExpression, null, getTaskName(), null);
                }

                // 找到字典对应的值则跳出循环
                if (!StringUtils.isEmpty(propValue)) {
                    resultStr = propValue;
                    break;
                }
            }
        } catch (Exception e) {
            String errMsg = String.format("cms_mt_channel_condition_config表中数据字典解析出错 [ChannelId:%s]" +
                            " [CartId:%s] [DictName:%s] [errMsg:%s]", sxData.getChannelId(), sxData.getCartId(), prePropId,
                    StringUtils.isEmpty(e.getMessage()) ? "出现不可预知的错误，请跟管理员联系" : e.getMessage());
            // 如果上新数据中的errorMessage为空
            if (StringUtils.isNullOrBlank2(sxData.getErrorMessage())) {
                e.printStackTrace();
            }
            $info(errMsg);
            throw new BusinessException(errMsg);
        }

        // 指定类型(如：运费模板id或关联版式id等)对应的值
        return resultStr;
    }

    /**
     * 从cms_mt_channel_config表中取得价格对应的配置项目值
     *
     * @param channelId String 渠道id
     * @param cartId String 平台id
     * @param priceType String 价格类型 (".sx_price",".tejiabao_open",".tejiabao_price")
     * @return double SKU价格
     */
    public String getPriceConfigValue(String channelId, String cartId, String priceType) {
        // 价格有可能是用priceSale, 也有可能用priceMsrp
        CmsChannelConfigBean priceConfig = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.PRICE,
                cartId + priceType);

        String priceConfigValue = "";
        if (priceConfig != null) {
            // 取得价格对应的configValue名
            priceConfigValue = priceConfig.getConfigValue1();
        }

        return priceConfigValue;
    }

    /**
     * 取得天猫同购上新用skus列表
     *
     * @param type 0: 适合颜色尺码都有的类目， 1：适合单sku的类目
     * @param cartId String 平台id
     * @param productList List<BaseMongoMap<String, Object>> 上新产品列表
     * @param skuList List<BaseMongoMap<String, Object>> 上新合并后sku列表
     * @param priceConfigValue String 价格取得项目名 ("cartId.sx_price")配置的项目名
     * @param skuLogicQtyMap Map<String, Integer> SKU逻辑库存
     * @param expressionParser 解析子
     * @param shopProp ShopBean 店铺信息
     * @param crossBorderRreportFlg String 入关方式(true表示跨境申报，false表示邮关申报)
     * @return List<BaseMongoMap<String, Object>> 天猫同购上新用skus列表
     */
    private List<BaseMongoMap<String, Object>> getSkus(int type, Integer cartId, List<CmsBtProductModel> productList,
                                                       List<BaseMongoMap<String, Object>> skuList,
                                                       String priceConfigValue, Map<String, Integer> skuLogicQtyMap,
                                                       ExpressionParser expressionParser,
                                                       ShopBean shopProp, String crossBorderRreportFlg) {

		// 官网同购， 上新时候的价格， 统一用所有sku里的最高价
		Double priceMax = 0d;
		for (CmsBtProductModel product : productList) {
			if (product.getCommon() == null
					|| product.getCommon().getFields() == null
					|| product.getPlatform(cartId) == null
					|| ListUtils.isNull(product.getPlatform(cartId).getSkus())) {
				continue;
			}
			for (BaseMongoMap<String, Object> sku : product.getPlatform(cartId).getSkus()) {
				String skuCode = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());

				// 根据skuCode从skuList中取得common.sku和PXX.sku合并之后的sku
				BaseMongoMap<String, Object> mergedSku = skuList.stream()
						.filter(s -> s.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()).equals(skuCode))
						.findFirst()
						.get();
				// 价格(根据cms_mt_channel_config表中的配置有可能是从priceRetail或者priceMsrp中取得价格)
				if (priceMax.compareTo(Double.parseDouble(mergedSku.getStringAttribute(priceConfigValue))) < 0) {
					priceMax = Double.parseDouble(mergedSku.getStringAttribute(priceConfigValue));
				}

			}

		}


		// 具体设置属性的逻辑
        List<BaseMongoMap<String, Object>> targetSkuList = new ArrayList<>();
        // 循环productList设置颜色和尺码等信息到sku列表
        for (CmsBtProductModel product : productList) {
            if (product.getCommon() == null
                    || product.getCommon().getFields() == null
                    || product.getPlatform(cartId) == null
                    || ListUtils.isNull(product.getPlatform(cartId).getSkus())) {
                continue;
            }

            // 取得海关报关税号code(10位数字)  (例："9404909000,变形枕,个")
            String hscode = "";
            // 只有当入关方式(true表示跨境申报)时，才需要设置海关报关税号hscode;false表示邮关申报时，不需要设置海关报关税号hscode
            if ("true".equalsIgnoreCase(crossBorderRreportFlg)) {
                String hsCodePrivate = product.getCommon().getFields().getHsCodePrivate();
                if (!StringUtils.isEmpty(hsCodePrivate)) {
                    if (hsCodePrivate.contains(Separtor_Coma)) {
                        hscode = hsCodePrivate.substring(0, hsCodePrivate.indexOf(Separtor_Coma));
                    } else {
                        hscode = hsCodePrivate;
                    }
                }
            }

            // 采用Ⅲ有SKU,且有不同图案，颜色的设置方式
            // 根据cms_mt_channel_condition_config表中配置的取得对象项目，取得当前product对应的商品特质英文（code 或 颜色/口味/香型等）的设置项目 (根据配置决定是用code还是codeDiff，默认为code)
            SxData sxData = expressionParser.getSxData();
            // 由于字典解析方式只能取得mainProduct里面字段的值，但color需要取得每个product里面字段的值，所以另加一个新的方法取得color值
            String color = getColorCondition(sxData.getChannelId(), sxData.getCartId(), product, "color_code_codediff", shopProp);
            // 如果根据配置(code或者codeDiff)取出来的值大于30位，则采用color字段的值
            if (!StringUtils.isEmpty(color) && color.length() > 30) {
                color = product.getCommon().getFields().getColor();
            }

            // 在根据skuCode循环
            for (BaseMongoMap<String, Object> sku : product.getPlatform(cartId).getSkus()) {
                String skuCode = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
                BaseMongoMap<String, Object> skuMap = new BaseMongoMap<>();
                // 销售属性map(颜色，尺寸)
                BaseMongoMap<String, Object> saleProp = new BaseMongoMap<>();
                // 商品特质英文（颜色/口味/香型等）(根据配置决定是用code还是codeDiff，默认为code)
                saleProp.put("color", color);
                // 根据skuCode从skuList中取得common.sku和PXX.sku合并之后的sku
                BaseMongoMap<String, Object> mergedSku = skuList.stream()
                        .filter(s -> s.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()).equals(skuCode))
                        .findFirst()
                        .get();
                // 尺寸
                saleProp.put("size", mergedSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name()));
                // 追加销售属性
				if (type == 0) { // 只有在type是0的场合（有多个颜色尺码的场合）才需要sale_prop这个属性
					skuMap.put("sale_prop", saleProp);
				}

                // 价格(根据cms_mt_channel_config表中的配置有可能是从priceRetail或者priceMsrp中取得价格)
//                skuMap.put("price", mergedSku.getStringAttribute(priceConfigValue));
				skuMap.put("price", String.valueOf(priceMax));
                // outer_id
                skuMap.put("outer_id", skuCode);
                // 库存
                skuMap.put("quantity", skuLogicQtyMap.get(skuCode));
                // 与颜色尺寸这个销售属性关联的图片
				String imageTemplate = getValueByDict("属性图片模板", expressionParser, shopProp);
				String propImage = expressionParser.getSxProductService().getProductImages(product, CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE).get(0).getName();
				String srcImage = String.format(imageTemplate, propImage);
				Set<String> url = new HashSet<>();
				url.add(srcImage);
				try {
					Map<String, String> map = sxProductService.uploadImage(shopProp.getOrder_channel_id(), cartId, expressionParser.getSxData().getGroupId().toString(), shopProp, url, getTaskName());
					skuMap.put("image", map.get(srcImage));
				} catch (Exception e) {
					logger.warn("官网同购sku颜色图片取得失败, groupId: " + expressionParser.getSxData().getGroupId());
				}
                // 只有当入关方式(true表示跨境申报)时，才需要设置海关报关税号hscode;false表示邮关申报时，不需要设置海关报关税号hscode
                if ("true".equalsIgnoreCase(crossBorderRreportFlg)) {
                    // 海关报关的税号
                    skuMap.put("hscode", hscode);
                }

                targetSkuList.add(skuMap);
            }
        }

        return targetSkuList;
    }

    /**
     * 从cms_mt_channel_condition_config表中取得商品特质英文(CodeDiff或Code)的值
     * 由于通过字典解析取值，只能取得mainProduct里面的字段的值，而商品特质英文需要取得每个product中的字段的值，所以不能用原来的字典解析
     *
     * @param channelId String 渠道id
     * @param cartId String 平台id
     * @param prePropId String 条件表达式前缀(运费模板:transportid_ 关联版式:commonhtml_id_)
     * @return String 指定类型(如：运费模板id或关联版式id等)对应的值
     */
    protected String getColorCondition(String channelId, Integer cartId, CmsBtProductModel product, String prePropId, ShopBean shop) {
        if (product == null) return "";

        // 返回值用(默认为code)
        String resultStr = product.getCommon().getFields().getCode();

        // 条件表达式前缀(商品特质英文(CodeDiff或Code):color_code_codediff)
        // 条件表达式表platform_prop_id字段的检索条件为条件表达式前缀加cartId
        String platformPropId = prePropId + "_" + StringUtils.toString(cartId);
        // 根据channelid和platformPropId取得cms_mt_channel_condition_config表的条件表达式
        List<ConditionPropValueModel> conditionPropValueModels = conditionPropValueRepo.get(channelId, platformPropId);

        // 使用运费模板或关联版式条件表达式
        if (ListUtils.notNull(conditionPropValueModels)) {
            for (ConditionPropValueModel conditionPropValueModel : conditionPropValueModels) {
                String conditionExpressionStr = conditionPropValueModel.getCondition_expression().trim();

                String propValue = null;
                // 带名字字典解析
                if (conditionExpressionStr.contains("color")) {
                    propValue = product.getCommon().getFields().getColor();
                } else if (conditionExpressionStr.contains("codeDiff")) {
                    propValue = product.getCommon().getFields().getCodeDiff();
                } else if (conditionExpressionStr.contains("code")) {
                    propValue = product.getCommon().getFields().getCode();
                }

                // 找到字典对应的值则跳出循环
                if (!StringUtils.isEmpty(propValue)) {
                    resultStr = propValue;
                    break;
                }
            }
        }

        // 如果cms_mt_channel_condition_config表里有配置项，就返回配置的字段值；
        // 如果没有配置加这个配置项目，直接反应该产品的code
        return resultStr;
    }

    /**
     * 根据天猫官网同购返回的NumIId取得平台上的类目id和类目path
     *
     * @param shopProp 店铺信息
     * @param numIId 天猫官网同购上新成功之后返回的numIId
     * @return Map<String, String> 天猫官网同购平台类目信息
     */
    protected Map<String, String> getSimpleItemCatInfo(ShopBean shopProp, String numIId) throws ApiException, GetUpdateSchemaFailException, TopSchemaException {
        // 调用官网同购编辑商品的get接口取得商品信息
        TbItemSchema tbItemSchema = tbSimpleItemService.getSimpleItem(shopProp, NumberUtils.toLong(numIId));
        if (tbItemSchema == null || ListUtils.isNull(tbItemSchema.getFields())) {
            // 天猫官网同购取得商品信息失败
            String errMsg = "天猫官网同购取得商品信息失败! ";
            $error(errMsg);
            throw new BusinessException(errMsg);
        }

        String pCatId = null;
        // 取得类目对应的Field
        InputField inputFieldCategory = (InputField)tbItemSchema.getFieldMap().get("category");
        if (!StringUtils.isEmpty(inputFieldCategory.getDefaultValue())
                && inputFieldCategory.getDefaultValue().contains(":")) {
            // 取得平台上返回的catId
            String[] strings = inputFieldCategory.getDefaultValue().split(":");
            pCatId = strings[1].replaceAll("}|\"", "");   // 删除里面多余的大括号(})和双引号(")
        }

        if (StringUtils.isEmpty(pCatId)) {
            return null;
        }

        Map<String, String> pCatInfoMap = new HashMap<>(2, 1f);
        pCatInfoMap.put("pCatId", pCatId);

        try {
            // 取得天猫官网同购pCatId对应的pCatPath
            String pCatPath = getTongGouCatFullPathByCatId(shopProp, pCatId);
            if (!StringUtils.isEmpty(pCatPath)) {
                pCatInfoMap.put("pCatPath", pCatPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pCatInfoMap;
    }

    /**
     * 根据天猫官网同购返回的NumIId取得平台上的类目id和类目path
     *
     * @param shopProp 店铺信息
     * @param pCatdId 天猫官网同购商品中取得的catId
     * @return String 天猫官网同购catId对应的平台catPath
     */
    protected String getTongGouCatFullPathByCatId(ShopBean shopProp, String pCatdId) {
        String pCatPath = null;

        // 从cms_mt_platform_category_schema_tm_cXXX(channelId)表中取得pCatId对应的pCatPath
        CmsMtPlatformCategorySchemaTmModel categorySchemaTm = platformCategorySchemaDao.selectPlatformCatSchemaTmModel(
                pCatdId, shopProp.getOrder_channel_id(), NumberUtils.toInt(shopProp.getCart_id()));
        if (categorySchemaTm != null) {
            pCatPath = categorySchemaTm.getCatFullPath();
        }

        return pCatPath;
    }

    /**
     * 特价宝的调用
     *
     * @param sxData            SxData 上新数据
     */
    private void updateTeJiaBaoPromotion(SxData sxData) {
        // 特价宝的调用
        // 价格有可能是用priceSale, 也有可能用priceMsrp, 所以需要判断一下
        CmsChannelConfigBean tejiabaoOpenConfig = CmsChannelConfigs.getConfigBean(sxData.getChannelId()
                , CmsConstants.ChannelConfig.PRICE
                , String.valueOf(sxData.getCartId()) + CmsConstants.ChannelConfig.PRICE_TEJIABAO_OPEN);
        CmsChannelConfigBean tejiabaoPriceConfig = CmsChannelConfigs.getConfigBean(sxData.getChannelId()
                , CmsConstants.ChannelConfig.PRICE
                , String.valueOf(sxData.getCartId()) + CmsConstants.ChannelConfig.PRICE_TEJIABAO_PRICE);

        // 检查一下
        String tejiabaoOpenFlag = null;
        String tejiabaoPricePropName = null;

        if (tejiabaoOpenConfig != null && !StringUtils.isEmpty(tejiabaoOpenConfig.getConfigValue1())) {
            if ("0".equals(tejiabaoOpenConfig.getConfigValue1()) || "1".equals(tejiabaoOpenConfig.getConfigValue1())) {
                tejiabaoOpenFlag = tejiabaoOpenConfig.getConfigValue1();
            }
        }
        if (tejiabaoPriceConfig != null && !StringUtils.isEmpty(tejiabaoPriceConfig.getConfigValue1())) {
            tejiabaoPricePropName = tejiabaoPriceConfig.getConfigValue1();
        }

        if (tejiabaoOpenFlag != null && "1".equals(tejiabaoOpenFlag)) {
            for (CmsBtProductModel sxProductModel : sxData.getProductList()) {
                // 获取价格
                if (sxProductModel.getCommon().getSkus() == null || sxProductModel.getCommon().getSkus().size() == 0) {
                    // 没有sku的code, 跳过
                    continue;
                }

                List<CmsBtPromotionSkuBean> skus = new ArrayList<>();
                for (BaseMongoMap<String, Object> sku : sxProductModel.getPlatform(sxData.getCartId()).getSkus()) {
                    CmsBtPromotionSkuBean skuBean = new CmsBtPromotionSkuBean();
                    skuBean.setProductSku(sku.getAttribute("skuCode"));
                    Double dblPriceSku = Double.parseDouble(sku.getAttribute(tejiabaoPricePropName).toString());
                    skuBean.setPromotionPrice(new BigDecimal(dblPriceSku));
                    skus.add(skuBean);
                }

                Double dblPrice = Double.parseDouble(sxProductModel.getPlatform(sxData.getCartId()).getSkus().get(0).getAttribute(tejiabaoPricePropName).toString());

                // 设置特价宝
                CmsBtPromotionCodesBean cmsBtPromotionCodesBean = new CmsBtPromotionCodesBean();
                cmsBtPromotionCodesBean.setPromotionId(0); // 设置为0的场合,李俊代码里会去处理
                cmsBtPromotionCodesBean.setChannelId(sxData.getChannelId());
                cmsBtPromotionCodesBean.setCartId(sxData.getCartId());
                // product表结构变化
                cmsBtPromotionCodesBean.setProductCode(sxProductModel.getCommon().getFields().getCode());
                cmsBtPromotionCodesBean.setProductId(sxProductModel.getProdId());
                cmsBtPromotionCodesBean.setPromotionPrice(dblPrice); // 真实售价
                cmsBtPromotionCodesBean.setNumIid(sxData.getPlatform().getNumIId());
                cmsBtPromotionCodesBean.setModifier(getTaskName());
                cmsBtPromotionCodesBean.setSkus(skus);
                // 这里只需要调用更新接口就可以了, 里面会有判断如果没有的话就插入
                promotionDetailService.teJiaBaoPromotionUpdate(cmsBtPromotionCodesBean);

            }
        }

    }
}
