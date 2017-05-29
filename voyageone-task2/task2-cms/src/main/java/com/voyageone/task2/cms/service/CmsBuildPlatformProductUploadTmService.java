package com.voyageone.task2.cms.service;

import com.taobao.api.ApiException;
import com.taobao.api.domain.ScItem;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.components.tmall.service.TbSaleService;
import com.voyageone.components.tmall.service.TbScItemService;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.bean.cms.CmsBtPromotionSkuBean;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.CmsBtSxCspuDao;
import com.voyageone.service.dao.cms.CmsBtSxProductDao;
import com.voyageone.service.dao.cms.CmsBtTmScItemDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.PlatformMappingDeprecatedService;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.TaobaoScItemService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.promotion.PromotionDetailService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.model.cms.CmsBtSxCspuModel;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.CmsBtTmScItemModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingDeprecatedModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 天猫平台产品上新服务
 * Product表中产品不存在就向天猫平台新增商品，否则就更新商品
 *
 * @author desmond on 2016/5/11.
 * @version 2.0.0
 */
@Service
public class CmsBuildPlatformProductUploadTmService extends BaseCronTaskService {

    @Autowired
    private PlatformProductUploadService platformProductUploadService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private TbProductService tbProductService;
    @Autowired
    private PlatformMappingDeprecatedService platformMappingDeprecatedService;
    @Autowired
    private PlatformCategoryService platformCategoryService;
    @Autowired
    private CmsBuildPlatformProductUploadTmProductService uploadTmProductService;
    @Autowired
    private CmsBuildPlatformProductUploadTmItemService uploadTmItemService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private PromotionDetailService promotionDetailService;

    @Autowired
    private CmsBtSxProductDao cmsBtSxProductDao;
    @Autowired
    private CmsBtSxCspuDao cmsBtSxCspuDao;
    @Autowired
    private CmsBtTmScItemDao cmsBtTmScItemDao;

    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;

    @Autowired
    private TaobaoScItemService taobaoScItemService;
    @Autowired
    private TbSaleService tbSaleService;
    @Autowired
    private TbScItemService tbScItemService;
    @Autowired
    private MqSender sender;
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformProductUploadTmJob";
    }

    /**
     * 天猫平台上新处理
     *
     * @param taskControlList taskcontrol信息
     */
    @Override
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

//        // 获取该任务可以运行的销售渠道
//        List<String> channelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);
//
//        // 循环所有销售渠道
//        if (channelIdList != null && channelIdList.size() > 0) {
//            for (String channelId : channelIdList) {
//                // TODO 虽然workload表里不想上新的渠道，不会有数据，这里的循环稍微有点效率问题，后面再改
//                // 天猫平台商品信息新增或更新(天猫)
//                doProductUpload(channelId, Integer.parseInt(CartEnums.Cart.TM.getId()));
//                // 天猫国际商品信息新增或更新(天猫国际)
//                doProductUpload(channelId, Integer.parseInt(CartEnums.Cart.TG.getId()));
//                // 淘宝商品信息新增或更新(淘宝)
////                doProductUpload(channelId, Integer.parseInt(CartEnums.Cart.TB.getId()));
//                // 天猫MiniMall商品信息新增或更新(天猫MiniMall)
////                doProductUpload(channelId, Integer.parseInt(CartEnums.Cart.TMM.getId()));
//            }
//        }

		doUploadMain(taskControlList);

        // 正常结束
        $info("正常结束");
    }

	public void doUploadMain(List<TaskControlBean> taskControlList) {
		// 由于这个方法可能会自己调用自己循环很多很多次， 不一定会跳出循环， 但又希望能获取到最新的TaskControl的信息， 所以不使用基类里的这个方法了
		// 为了调试方便， 允许作为参数传入， 但是理想中实际运行中， 基本上还是自主获取的场合比较多
		if (taskControlList == null) {
			taskControlList = taskDao.getTaskControlList(getTaskName());

			if (taskControlList.isEmpty()) {
//                $info("没有找到任何配置。");
				logIssue("没有找到任何配置！！！", getTaskName());
				return;
			}

			// 是否可以运行的判断
			if (!TaskControlUtils.isRunnable(taskControlList)) {
				$info("Runnable is false");
				return;
			}

		}

		// 获取该任务可以运行的销售渠道
		List<TaskControlBean> taskControlBeanList = TaskControlUtils.getVal1s(taskControlList, TaskControlEnums.Name.order_channel_id);

		// 准备按组分配线程（相同的组， 会共用相同的一组线程通道， 不同的组， 线程通道互不干涉）
		Map<String, List<String>> mapTaskControl = new HashMap<>();
		taskControlBeanList.forEach((l)->{
			String key = l.getCfg_val2();
			if (StringUtils.isEmpty(key)) {
				key = "0";
			}
			if (mapTaskControl.containsKey(key)) {
				mapTaskControl.get(key).add(l.getCfg_val1());
			} else {
				List<String> channelList = new ArrayList<>();
				channelList.add(l.getCfg_val1());
				mapTaskControl.put(key, channelList);
			}
		});

		Map<String, ExecutorService> mapThread = new HashMap<>();

		while (true) {

			mapTaskControl.forEach((k, v)->{
				boolean blnCreateThread = false;

				if (mapThread.containsKey(k)) {
					ExecutorService t = mapThread.get(k);
					if (t.isTerminated()) {
						// 可以新做一个线程
						blnCreateThread = true;
					}
				} else {
					// 可以新做一个线程
					blnCreateThread = true;
				}

				if (blnCreateThread) {
					ExecutorService t = Executors.newSingleThreadExecutor();

					List<String> channelIdList = v;
					if (channelIdList != null) {
						for (String channelId : channelIdList) {
							t.execute(() -> {
								try {
									doProductUpload(channelId, CartEnums.Cart.TM.getValue());
									doProductUpload(channelId, CartEnums.Cart.TG.getValue());
								} catch (Exception e) {
									e.printStackTrace();
								}
							});

						}
					}
					t.shutdown();

					mapThread.put(k, t);

				}
			});

			boolean blnAllOver = true;
			for (Map.Entry<String, ExecutorService> entry : mapThread.entrySet()) {
				if (!entry.getValue().isTerminated()) {
					blnAllOver = false;
					break;
				}
			}
			if (blnAllOver) {
				break;
			}
			try {
				Thread.sleep(1000 * 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		// TODO: 所有渠道处理总件数为0的场合， 就跳出不继续做了。 以外的场合， 说明可能还有别的未完成的数据， 继续自己调用自己一下
		try {
			Thread.sleep(1000 * 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		doUploadMain(null);

	}

	/**
     * 平台产品上新主处理
     *
     * @param channelId String 渠道ID
     * @param cartId    String 平台ID
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
        for (CmsBtSxWorkloadModel cmsBtSxWorkloadModel : sxWorkloadModels) {
            // 启动多线程
            executor.execute(() -> uploadProduct(cmsBtSxWorkloadModel, shopProp));
        }
        // ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
        // 当所有已经提交的任务执行完毕后将会关闭ExecutorService。
        executor.shutdown(); //并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
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
     * @param shopProp             ShopBean 店铺信息
     */
    public void uploadProduct(CmsBtSxWorkloadModel cmsBtSxWorkloadModel, ShopBean shopProp) {
        // 当前groupid(用于取得产品信息)
        long groupId = cmsBtSxWorkloadModel.getGroupId();
        // 渠道id
        String channelId = cmsBtSxWorkloadModel.getChannelId();
        // 平台id
        int cartId = cmsBtSxWorkloadModel.getCartId();
        // 平台产品id
        String platformProductId = "";
        // 商品id
        String numIId = "";
        // 表达式解析子
        ExpressionParser expressionParser = null;
        // 上新数据
        SxData sxData = null;
        // 平台类目schema信息
        CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel;
        // 平台Mapping信息
        CmsMtPlatformMappingDeprecatedModel cmsMtPlatformMappingModel;
        // 平台类目id
        String platformCategoryId = "";
        // 达尔文是否能上新商品
        boolean canSxDarwinItem = false;
        // 上新对象产品code列表，回写状态时使用
        List<String> listSxCode = null;
        // 开始时间
        long prodStartTime = System.currentTimeMillis();

        List<String> strSkuCodeList = new ArrayList<>();
        // 天猫产品上新处理
        try {
            // 上新用的商品数据信息取得
            sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);
            if (sxData == null) {
                // modified by morse.lu 2016/06/12 start
                // 异常的时候去做这段逻辑
//                String errMsg = String.format("取得上新用的商品数据信息失败！[ChannelId:%s] [GroupId:%s]", channelId, groupId);
//                $error(errMsg);
//                // 回写详细错误信息表(cms_bt_business_log)用
//                sxData = new SxData();
//                sxData.setChannelId(channelId);
//                sxData.setCartId(cartId);
//                sxData.setGroupId(groupId);
//                sxData.setErrorMessage(errMsg);
//                throw new BusinessException(errMsg);
                throw new BusinessException("SxData取得失败!");
                // modified by morse.lu 2016/06/12 end
            }
            // 上新对象code(后面回写状态时要用到)
            if (ListUtils.notNull(sxData.getProductList())) {
                listSxCode = sxData.getProductList().stream().map(p -> p.getCommonNotNull().getFieldsNotNull().getCode()).collect(Collectors.toList());
            }
            if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
                // 取得上新数据出错时，cartId有可能没有设置
                sxData.setCartId(cartId);
                // 有错误的时候，直接报错
                throw new BusinessException(sxData.getErrorMessage());
            }
            // deleted by morse.lu 2016/07/08 start
            // getSxProductDataByGroupId里去做排序了，外部不需要啦
//            // 单个product内部的sku列表分别进行排序
//            for (CmsBtProductModel cmsBtProductModel : sxData.getProductList()) {
//                // modified by morse.lu 2016/06/28 start
//                // product表结构变化
////                sxProductService.sortSkuInfo(cmsBtProductModel.getSkus());
//                sxProductService.sortSkuInfo(cmsBtProductModel.getCommon().getSkus());
//                sxProductService.sortListBySkuCode(cmsBtProductModel.getPlatform(sxData.getCartId()).getSkus(),
//                                                        cmsBtProductModel.getCommon().getSkus().stream().map(CmsBtProductModel_Sku::getSkuCode).collect(Collectors.toList()));
//                // modified by morse.lu 2016/06/28 end
//            }
//            // added by morse.lu 2016/06/28 start
//            // skuList也排序一下
//            sxProductService.sortSkuInfo(sxData.getSkuList());
//            // added by morse.lu 2016/06/28 end
            // deleted by morse.lu 2016/07/08 end
            // 主产品等列表取得
            CmsBtProductModel mainProduct = sxData.getMainProduct();
            List<CmsBtProductModel> productList = sxData.getProductList();
//            List<CmsBtProductModel_Sku> skuList = sxData.getSkuList();
            // 主产品取得结果判断
            if (mainProduct == null) {
                String errMsg = String.format("取得主商品信息失败！[ChannelId:%s] [GroupId:%s]", channelId, groupId);
                $error(errMsg);
                sxData.setErrorMessage(errMsg);
                throw new BusinessException(errMsg);
            }

            // 表达式解析子
            expressionParser = new ExpressionParser(sxProductService, sxData);

            // 20170417 全链路库存改造 charis STA

            sxData.getSkuList().forEach(sku -> strSkuCodeList.add(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name())));

            if (ListUtils.isNull(strSkuCodeList)) {
                String errMsg = "已完成审批的产品sku列表为空";
                $error(errMsg);
                throw new BusinessException(errMsg);
            }
            // 判断是否需要做货品绑定
            String storeCode = taobaoScItemService.doCheckNeedSetScItem(shopProp, mainProduct);
            if (!StringUtils.isEmpty(storeCode)) {
                String title = sxProductService.getProductValueByMasterMapping("title", shopProp, expressionParser, getTaskName());
                Map<String, ScItem> scItemMap = new HashMap<>();
                for (String sku_outerId : strSkuCodeList) {
                    // 检查是否发布过仓储商品
                    ScItem scItem;
                    try {
                        scItem = tbScItemService.getScItemByOuterCode(shopProp, sku_outerId);
                    } catch (ApiException e) {
                        String errMsg = String.format("自动设置天猫商品全链路库存管理:检查是否发布过仓储商品:{outerId: %s, err_msg: %s}", sku_outerId, e.toString());
                        throw new BusinessException(errMsg);
                    }

                    if (scItem == null) {
                        // 没有发布过仓储商品的场合， 发布仓储商品
                        try {
                            scItem = tbScItemService.addScItemSimple(shopProp, title, sku_outerId);
                        } catch (ApiException e) {
                            String errMsg = String.format("自动设置天猫商品全链路库存管理:发布仓储商品:{outerId: %s, err_msg: %s}", sku_outerId, e.toString());
                            throw new BusinessException(errMsg);
                        }
                    }
                    scItemMap.put(sku_outerId, scItem);
                }
                sxData.setScItemMap(scItemMap);
            }
            // 20170417 全链路库存改造 charis END


            // 属性值准备
            // 取得主产品类目对应的platform mapping数据
            cmsMtPlatformMappingModel = platformMappingDeprecatedService.getMappingByMainCatId(channelId, cartId, mainProduct.getCommon().getCatId());
//            if (cmsMtPlatformMappingModel == null) {
//                String errMsg = String.format("共通PlatformMapping表中对应的平台Mapping信息不存在！[ChannelId:%s] [CartId:%s] [主产品类目:%s]",
//                        channelId, cartId, mainProduct.getCommon().getCatId());
//                $error(errMsg);
//                sxData.setErrorMessage(errMsg);
//                throw new BusinessException(errMsg);
//            }

            // 取得主产品类目对应的平台类目
//            platformCategoryId = cmsMtPlatformMappingModel.getPlatformCategoryId();
            platformCategoryId = mainProduct.getPlatform(cartId).getpCatId();
            // 取得平台类目schema信息
            cmsMtPlatformCategorySchemaModel = platformCategoryService.getPlatformCatSchemaTm(platformCategoryId, channelId, cartId);
            if (cmsMtPlatformCategorySchemaModel == null) {
                String errMsg = String.format("获取平台类目schema信息失败！[PlatformCategoryId:%s] [CartId:%s]", platformCategoryId, cartId);
                $error(errMsg);
                sxData.setErrorMessage(errMsg);
                throw new BusinessException(errMsg);
            }



            // 判断商品是否是达尔文
            boolean isDarwin = false;
            try {
                isDarwin = uploadTmProductService.getIsDarwin(sxData, shopProp, platformCategoryId, sxData.getBrandCode());
            } catch (BusinessException be) {
                // 判断商品是否是达尔文异常的时候默认为"非达尔文"
                String errMsg = String.format("判断商品是否是达尔文异常结束，默认为非达尔文！[PlatformCategoryId:%s] [CartId:%s] [BrandCode:%s]",
                        platformCategoryId, cartId, sxData.getBrandCode());
                $error(errMsg);
            }
            // 设置是否是达尔文体系标志位
            sxData.setDarwin(isDarwin);

            // 平台产品id(MongoDB的)
            platformProductId = sxData.getPlatform().getPlatformPid();

            // added by morse.lu 2016/08/08 start
            {
                // 能否更新商品
                Map<String, Object> searchParam = new HashMap<>();
                searchParam.put("channel_id", sxData.getChannelId());
                searchParam.put("cart_id", sxData.getCartId());
                searchParam.put("code", mainProduct.getCommon().getFields().getCode());
                int cnt = cmsBtSxProductDao.selectCount(searchParam);
                if (cnt == 0) {
                    // 没找到的话，看看是不是全店都允许
                    searchParam.put("code", "0");
                    cnt = cmsBtSxProductDao.selectCount(searchParam);
                }
                if (cnt > 0) {
                    // 找到了，允许更新产品
                    sxData.setUpdateProductFlg(true);
                }
            }
            if (sxData.isDarwin()) {
                // 达尔文
                Map<String, Object> searchParam = new HashMap<>();
                searchParam.put("channel_id", sxData.getChannelId());
                searchParam.put("cart_id", sxData.getCartId());
                List<CmsBtSxCspuModel> cmsBtSxCspuModels = cmsBtSxCspuDao.selectList(searchParam);
                List<String> allowUpdateBarcode = cmsBtSxCspuModels.stream().map(CmsBtSxCspuModel::getBarcode).collect(Collectors.toList());

                for (BaseMongoMap<String, Object> sku : sxData.getSkuList()) {
                    String skuCode = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
                    SxData.SxDarwinSkuProps sxDarwinSkuProps = sxData.getDarwinSkuProps(skuCode, true);
                    String barcode = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.barcode.name());
                    sxDarwinSkuProps.setBarcode(barcode);
                    if (allowUpdateBarcode.contains(barcode)) {
                        // 允许更新规格
                        sxDarwinSkuProps.setAllowUpdate(true);
                    }
                }
                canSxDarwinItem = uploadTmProductService.sxDarwinProduct(expressionParser, cmsMtPlatformCategorySchemaModel, platformProductId, shopProp, getTaskName());
            } else
                // added by morse.lu 2016/08/08 end
            // 天猫产品上新处理
            // 先看一下productGroup表和调用天猫API去平台上取看是否有platformPid,两个地方都没有才需要上传产品，
            // 只要有一个地方有就认为产品已存在，不用新增和更新产品，直接做后面的商品上新处理
            // 另外，天猫平台产品只有新增，不能更新（如果需要更新具体商品的产品信息，可以在商品页面手动更新）
            if (StringUtils.isEmpty(platformProductId)) {
                // productGroup表中platformPid不存在的时候

                // 匹配平台产品Id列表
                List<String> platformProductIdList = new ArrayList<>();
                // productGroup表中platformPid为空的时候，调用天猫API查找产品platformPid
                platformProductIdList = uploadTmProductService.getProductIdFromTmall(expressionParser, cmsMtPlatformCategorySchemaModel,
                        cmsMtPlatformMappingModel, shopProp, getTaskName());

                // added by morse.lu 2016/06/06 start
                if (platformProductIdList != null) {
                    // null的话，表示该类目没有产品，直接进入商品上新
                    // added by morse.lu 2016/06/06 end
                    // 取得可以上传商品的平台产品id
                    // 如果发现已有产品符合我们要上传的商品，但需要等待天猫审核该产品,则抛出异常，不做后续上传产品/商品处理)
                    platformProductId = uploadTmProductService.getUsefulProductId(sxData, platformProductIdList, shopProp);

                    // productGroup表和天猫平台上都不存在这个产品时，新增产品
                    if (StringUtils.isEmpty(platformProductId)) {
                        // 新增产品到平台
                        platformProductId = uploadTmProductService.addTmallProduct(expressionParser, cmsMtPlatformCategorySchemaModel,
                                cmsMtPlatformMappingModel, shopProp, getTaskName());
                        // added by morse.lu 2016/06/08 start
                    } else {
                        // 更新产品
                        // added by morse.lu 2016/09/09 start
                        // 天猫现在一家店一个产品只能发布一款商品(addItem返回同一个numIId)
                        // 所以如果匹配到了pid，先在数据库里找一下，看看是不是别的group也是这个产品，有的话抛错，需要改一下匹配产品的key(例如系列，型号，容量等等)
                        JongoQuery query = new JongoQuery();
                        query.setQuery("{\"platformPid\":#}");
                        query.setParameters(platformProductId);
                        CmsBtProductGroupModel groupModel = cmsBtProductGroupDao.selectOneWithQuery(query, channelId);
                        if (groupModel != null) {
                            throw new BusinessException(String.format("天猫一个店铺一个产品只允许发布一款商品,已经有同一款商品上新过了!主商品code是%s", groupModel.getMainProductCode()));
                        }
                        // added by morse.lu 2016/09/09 end
                        // modified by morse.lu 2016/08/08 start
                        // 如果表里设定允许更新产品，才会去做产品更新
                            if (sxData.isUpdateProductFlg()) {
                        // modified by morse.lu 2016/08/08 end
                                uploadTmProductService.updateTmallProduct(expressionParser, platformProductId, cmsMtPlatformMappingModel, shopProp, getTaskName());
                            }
                        // added by morse.lu 2016/06/08 end
                    }

                    // 以前productGroup表中没有，从天猫平台上找到匹配的productId 或者 向平台新增成功之后，回写SxData和ProductGroup表platformPid
                    if (!StringUtils.isEmpty(platformProductId)) {
                        // 上传产品成功的时候, 回写SxData和ProductGroup表中的platformPid
                        updateProductGroupProductPId(sxData, platformProductId);
                        // delete by morse.lu 2016/06/06 start
                        // 允许无产品，只有商品
//                    } else {
//                        // 上传产品失败的时候
//                        String errMsg = String.format("天猫平台产品匹配或上传产品失败！[ChannelId:%s] [CartId:%s] [GroupId:%s]",
//                                channelId, cartId, groupId);
//                        $error(errMsg);
//                        // 如果上新数据中的errorMessage为空
//                        if (StringUtils.isEmpty(sxData.getErrorMessage())) {
//                            sxData.setErrorMessage(errMsg);
//                        }
//                        // 回写workload表   (失败2)
//                        sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, CmsConstants.SxWorkloadPublishStatusNum.errorNum, getTaskName());
//                        // 回写详细错误信息表(cms_bt_business_log)
//                        sxProductService.insertBusinessLog(sxData, getTaskName());
                        // delete by morse.lu 2016/06/06 end
                    }
                }
                // added by morse.lu 2016/06/08 start
            } else {
                // 更新产品
                // modified by morse.lu 2016/08/08 start
                // 表里设定允许更新产品，才会去做产品更新
                if (sxData.isUpdateProductFlg()) {
                    // modified by morse.lu 2016/08/08 end
                    uploadTmProductService.updateTmallProduct(expressionParser, platformProductId, cmsMtPlatformMappingModel, shopProp, getTaskName());
                }
                // added by morse.lu 2016/06/08 end
            }

        } catch (Exception ex) {
            // add by morse.lu 2016/06/07 start
            // 取得sxData为空
            if (sxData == null) {
                sxData = new SxData();
                sxData.setChannelId(channelId);
                sxData.setCartId(cartId);
                sxData.setGroupId(groupId);
                sxData.setErrorMessage(String.format("取得上新用的商品数据信息失败！[ChannelId:%s] [GroupId:%s]", channelId, groupId));
            }
            // add by morse.lu 2016/06/07 end
            // 上传产品失败，后面商品也不用上传，直接回写workload表   (失败2)
            String errMsg = String.format("天猫平台产品匹配或上传产品时异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [%s]",
                    channelId, cartId, groupId, ex.getMessage());
            $error(errMsg);
            ex.printStackTrace();
            // 如果上新数据中的errorMessage为空
            if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                sxData.setErrorMessage(errMsg);
            }
            // 回写workload表   (失败2)
            sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, CmsConstants.SxWorkloadPublishStatusNum.errorNum, getTaskName());
            // 回写详细错误信息表(cms_bt_business_log)
            sxProductService.insertBusinessLog(sxData, getTaskName());
            // 上新失败后回写product表pPublishError的值("Error")和pPublishMessage(上新错误信息)
            productGroupService.updateUploadErrorStatus(sxData.getPlatform(), listSxCode, sxData.getErrorMessage());
            $error(String.format("天猫平台单个产品和商品新增或更新信息异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [耗时:%s]",
                    channelId, cartId, groupId, (System.currentTimeMillis() - prodStartTime)));
            return;
        }

        // added by morse.lu 2016/08/10 start
        if (sxData.isDarwin() && !canSxDarwinItem) {
            // 达尔文，但更新了产品，不能马上上新商品，要等待审核
            sxData.setErrorMessage("达尔文产品更新成功,请等待审核通过,再重新Approve上新商品.");
            // 回写workload表  (审核中4)
            sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, CmsConstants.SxWorkloadPublishStatusNum.review, getTaskName());
            // 回写详细错误信息表(cms_bt_business_log)
            sxProductService.insertBusinessLog(sxData, getTaskName());
            // 上新失败后回写product表pPublishError的值("Error")和pPublishMessage(上新错误信息)
            productGroupService.updateUploadErrorStatus(sxData.getPlatform(), listSxCode, sxData.getErrorMessage());
            $info(String.format("天猫平台单个商品上新结束,达尔文产品更新成功,请等待审核通过,再重新Approve上新商品！[ChannelId:%s] [CartId:%s] [GroupId:%s] [耗时:%s]",
                    channelId, cartId, groupId, (System.currentTimeMillis() - prodStartTime)));
            return;
        }
        // added by morse.lu 2016/08/10 end

        // 天猫商品上新(新增或更新)处理
        // 如果平台产品id不为空的话，上传商品到天猫平台

        // delete by morse.lu 2016/06/06 start
//        if (!StringUtils.isEmpty(platformProductId)) {
            // 允许无产品，只有商品
            // delete by morse.lu 2016/06/06 end
            // 天猫商品上新处理
            try {
                // 新增更新标记
                boolean isNewFlag = false;
                if (StringUtils.isEmpty(numIId)) {
                    isNewFlag = true;
                }

                // 新增或更新商品信息到天猫平台
                numIId = uploadTmItemService.uploadItem(expressionParser, platformProductId, cmsMtPlatformCategorySchemaModel, cmsMtPlatformMappingModel, shopProp, getTaskName());
                // 新增或更新商品结果判断
                if (!StringUtils.isEmpty(numIId)) {

                    // 20161202 达尔文货品关联问题的对应 START
                    // 达尔文的场合， 货品关联可能会丢失， 需要重新绑定货品
                    if (sxData.isDarwin() && canSxDarwinItem) {
                        // 自动设置天猫商品全链路库存管理（函数里会自动判断当前店铺是否需要处理全链路）
                        taobaoScItemService.doSetScItem(shopProp, sxData, Long.parseLong(numIId));

                        // 20170413 tom 如果是新建的场合， 需要根据配置来设置上下架状态 START
                        CmsConstants.PlatformActive platformActive = sxData.getPlatform().getPlatformActive();
                        if (isNewFlag) {
                            platformActive = sxProductService.getDefaultPlatformActiveConfigByChannelCart(channelId, String.valueOf(cartId));
                        }
                        // 20170413 tom 如果是新建的场合， 需要根据配置来设置上下架状态 END

                        // 如果action是onsale的场合， 再做一次上架
                        if (CmsConstants.PlatformActive.ToOnSale.equals(platformActive)) {
                            tbSaleService.doWareUpdateListing(shopProp, numIId);
                        }
                    }
                    // 20161202 达尔文货品关联问题的对应 END
                    Date nowTime  = new Date();
                    Date changeTime = null;
                    try {
                        changeTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-05-28 00:00:00");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (changeTime.before(nowTime)) {
                        // 20170526 调用新的更新库存接口同步库存 STA
                        for (String sku : strSkuCodeList) {
                            Map<String, Object> messageMap = new HashMap<>();
                            messageMap.put("channelId", channelId);
                            messageMap.put("cartId", cartId);
                            messageMap.put("sku", sku);
                            sender.sendMessage("ewms_mq_stock_sync_platform_stock" + "_" + channelId, messageMap);
                        }
                        // 20170526 调用新的更新库存接口同步库存 END
                    } else {
                        // 20170417 调用更新库存接口同步库存 STA
                        sxProductService.synInventoryToPlatform(channelId, String.valueOf(cartId), null, strSkuCodeList);
                        // 20170417 调用更新库存接口同步库存 END
                    }

                    // added by morse.lu 2017/01/05 start
                    // 更新cms_bt_tm_sc_item表，把货品id记下来，同步库存用
                    saveCmsBtTmScItem(channelId, cartId, sxData);
                    // added by morse.lu 2017/01/05 end

                    // 上传商品成功的时候
                    // 上新或更新成功后回写product group表中的numIId和platformStatus(Onsale/InStock)
                    sxProductService.updateProductGroupNumIIdStatus(sxData, numIId, getTaskName());

                    // 回写ims_bt_product表(numIId)
                    sxProductService.updateImsBtProduct(sxData, getTaskName());

                    // 更新特价宝
                    updateTeJiaBaoPromotion(sxData);

                    // added by morse.lu 2016/12/08 start
                    if (ChannelConfigEnums.Channel.SN.getId().equals(channelId)) {
                        // Sneakerhead
                        try {
                            sxProductService.uploadCnInfo(sxData);
                        } catch (IOException io) {
                            throw new BusinessException("上新成功!但在推送给美国数据库时发生异常!"+ io.getMessage());
                        }
                    }
                    // added by morse.lu 2016/12/08 end

                    // 回写workload表   (成功1)
                    sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, CmsConstants.SxWorkloadPublishStatusNum.okNum, getTaskName());

                    // 上新成功时更新该model对应的所有和上新有关的状态信息,同时清空product表pPublishError的值("Error")和pPublishMessage(上新错误信息)
                    productGroupService.updateGroupsPlatformStatus(sxData.getPlatform(), listSxCode);

                    // delete by morse.lu 2016/06/06 start
                    // 不会为空的吧，即使为空，下面的逻辑不抛错，直接return，真的好吗？
//                } else {
//                    // 新增或更新商品失败的时候
//                    // 新增或更新商品失败
//                    String errMsg = String.format("天猫新增或更新商品信息失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformProductId:%s] [NumIId:%s]",
//                                    channelId, cartId, groupId, platformProductId, numIId);
//                    $error(errMsg);
//                    // 如果上新数据中的errorMessage为空
//                    if (StringUtils.isEmpty(sxData.getErrorMessage())) {
//                        sxData.setErrorMessage(errMsg);
//                    }
//                    // 回写workload表   (失败2)
//                    sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, CmsConstants.SxWorkloadPublishStatusNum.errorNum, getTaskName());
//                    // 回写详细错误信息表(cms_bt_business_log)
//                    sxProductService.insertBusinessLog(sxData, getTaskName());
//                    return;
                    // delete by morse.lu 2016/06/06 end
                }
            } catch (Exception ex) {
                // 上传商品失败，回写workload表   (失败2)
                String errMsg = String.format("天猫平台新增或更新商品时异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformProductId:%s]",
                        channelId, cartId, groupId, platformProductId);
                if (!StringUtils.isEmpty(ex.getMessage())) {
                    errMsg = errMsg + ex.getMessage();
                }
                $error(errMsg);
                ex.printStackTrace();
                // 如果上新数据中的errorMessage为空
                if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                    sxData.setErrorMessage(errMsg);
                }
                // 回写workload表   (失败2)
                sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, CmsConstants.SxWorkloadPublishStatusNum.errorNum, getTaskName());
                // 回写详细错误信息表(cms_bt_business_log)
                sxProductService.insertBusinessLog(sxData, getTaskName());
                // 上新失败后回写product表pPublishError的值("Error")和pPublishMessage(上新错误信息)
                productGroupService.updateUploadErrorStatus(sxData.getPlatform(), listSxCode, sxData.getErrorMessage());
                $error(String.format("天猫平台新增或更新商品时异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [numIId:%s] [耗时:%s]",
                        channelId, cartId, groupId, numIId, (System.currentTimeMillis() - prodStartTime)));
                return;
            }
//        }

        // 正常结束
        $info(String.format("天猫平台单个产品和商品新增或更新信息成功！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformProductId:%s] [numIId:%s] [耗时:%s]",
                channelId, cartId, groupId, platformProductId, numIId, (System.currentTimeMillis() - prodStartTime)));
    }

    private void saveCmsBtTmScItem(String channelId, int cartId, SxData sxData) {
        for (CmsBtProductModel productModel : sxData.getProductList()) {
            for(CmsBtProductModel_Sku commonSku : productModel.getCommon().getSkus()) {
                String code = productModel.getCommon().getFields().getCode();
                String skuCode = commonSku.getSkuCode();
                Map<String, Object> searchParam = new HashMap<>();
                searchParam.put("channelId", channelId);
                searchParam.put("cartId", cartId);
                searchParam.put("code", code);
                searchParam.put("sku", skuCode);
                searchParam.put("orgChannelId", sxData.getMainProduct().getOrgChannelId());
                CmsBtTmScItemModel scItemModel = cmsBtTmScItemDao.selectOne(searchParam);
                SxData.SxSkuExInfo sxSkuExInfo = sxData.getSxSkuExInfo(skuCode, false);
                String scProductId = sxSkuExInfo != null? sxSkuExInfo.getScProductId() : null;
                if (StringUtils.isEmpty(scProductId)) {
                    // delete
                    if (scItemModel != null) {
                        cmsBtTmScItemDao.delete(scItemModel.getId());
                    }
                } else {
                    if (scItemModel == null) {
                        // add
                        scItemModel = new CmsBtTmScItemModel();
                        scItemModel.setChannelId(channelId);
                        scItemModel.setOrgChannelId(sxData.getMainProduct().getOrgChannelId());
                        scItemModel.setCartId(cartId);
                        scItemModel.setCode(code);
                        scItemModel.setSku(skuCode);
                        scItemModel.setScProductId(scProductId);
                        scItemModel.setCreater(getTaskName());
                        cmsBtTmScItemDao.insert(scItemModel);
                    } else {
                        // update
                        if (!scProductId.equals(scItemModel.getScProductId())) {
                            scItemModel.setScProductId(scProductId);
                            scItemModel.setModifier(getTaskName());
                            scItemModel.setModified(DateTimeUtil.getDate());
                            cmsBtTmScItemDao.update(scItemModel);
                        }
                    }
                }
            }
        }
    }

    /**
     * 回写产品Group表里的平台产品id
     *
     * @param sxData            SxData 上新数据
     * @param platformProductId String 平台产品id
     */
    private void updateProductGroupProductPId(SxData sxData, String platformProductId) {

        // 回写平台产品id(platformProductId->platformPid)
        sxData.getPlatform().setPlatformPid(platformProductId);
        // 更新者
        sxData.getPlatform().setModifier(getTaskName());
        // 更新ProductGroup表
        productGroupService.update(sxData.getPlatform());
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
                , CmsConstants.ChannelConfig.PRICE_TEJIABAO_IS_OPEN_KEY
                , String.valueOf(sxData.getCartId()) + CmsConstants.ChannelConfig.PRICE_TEJIABAO_IS_OPEN_CODE);
        CmsChannelConfigBean tejiabaoPriceConfig = CmsChannelConfigs.getConfigBean(sxData.getChannelId()
                , CmsConstants.ChannelConfig.PRICE_TEJIABAO_KEY
                , String.valueOf(sxData.getCartId()) + CmsConstants.ChannelConfig.PRICE_TEJIABAO_PRICE_CODE);

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

//        if (tejiabaoOpenFlag != null && "1".equals(tejiabaoOpenFlag)) {
//            for (CmsBtProductModel sxProductModel : sxData.getProductList()) {
//                // 获取价格
//                // modified by morse.lu 2016/06/28 start
//                // product表结构变化
////                if (sxProductModel.getSkus() == null || sxProductModel.getSkus().size() == 0) {
//                if (sxProductModel.getCommon().getSkus() == null || sxProductModel.getCommon().getSkus().size() == 0) {
//                    // modified by morse.lu 2016/06/28 end
//                    // 没有sku的code, 跳过
//                    continue;
//                }
//                // modified by morse.lu 2016/06/28 start
//                // product表结构变化
////                Double dblPrice = Double.parseDouble(sxProductModel.getSkus().get(0).getAttribute(tejiabaoPricePropName).toString());
//                Double dblPrice = Double.parseDouble(sxProductModel.getPlatform(sxData.getCartId()).getSkus().get(0).getAttribute(tejiabaoPricePropName).toString());
//                // modified by morse.lu 2016/06/28 end
//
//                // 设置特价宝
//                CmsBtPromotionCodesBean cmsBtPromotionCodesBean = new CmsBtPromotionCodesBean();
//                cmsBtPromotionCodesBean.setPromotionId(0); // 设置为0的场合,李俊代码里会去处理
//                cmsBtPromotionCodesBean.setChannelId(sxData.getChannelId());
//                cmsBtPromotionCodesBean.setCartId(sxData.getCartId());
//                // modified by morse.lu 2016/06/28 start
//                // product表结构变化
////                cmsBtPromotionCodesBean.setProductCode(sxProductModel.getFields().getCode());
//                cmsBtPromotionCodesBean.setProductCode(sxProductModel.getCommon().getFields().getCode());
//                // modified by morse.lu 2016/06/28 end
//                cmsBtPromotionCodesBean.setProductId(sxProductModel.getProdId());
//                cmsBtPromotionCodesBean.setPromotionPrice(dblPrice); // 真实售价
//                cmsBtPromotionCodesBean.setNumIid(sxData.getPlatform().getNumIId());
//                cmsBtPromotionCodesBean.setModifier(getTaskName());
//                // 这里只需要调用更新接口就可以了, 里面会有判断如果没有的话就插入
//                promotionDetailService.teJiaBaoPromotionUpdate(cmsBtPromotionCodesBean);
//            }
//        }

        if (tejiabaoOpenFlag != null && "1".equals(tejiabaoOpenFlag)) {
            for (CmsBtProductModel sxProductModel : sxData.getProductList()) {
                // 获取价格
                // modified by morse.lu 2016/06/28 start
                // product表结构变化
//                if (sxProductModel.getSkus() == null || sxProductModel.getSkus().size() == 0) {
                if (sxProductModel.getCommon().getSkus() == null || sxProductModel.getCommon().getSkus().size() == 0) {
                    // modified by morse.lu 2016/06/28 end
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
                // modified by morse.lu 2016/06/28 end

                // 设置特价宝
                CmsBtPromotionCodesBean cmsBtPromotionCodesBean = new CmsBtPromotionCodesBean();
                cmsBtPromotionCodesBean.setPromotionId(0); // 设置为0的场合,李俊代码里会去处理
                cmsBtPromotionCodesBean.setChannelId(sxData.getChannelId());
                cmsBtPromotionCodesBean.setCartId(sxData.getCartId());
                // modified by morse.lu 2016/06/28 start
                // product表结构变化
//                cmsBtPromotionCodesBean.setProductCode(sxProductModel.getFields().getCode());
                cmsBtPromotionCodesBean.setProductCode(sxProductModel.getCommon().getFields().getCode());
                // modified by morse.lu 2016/06/28 end
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
