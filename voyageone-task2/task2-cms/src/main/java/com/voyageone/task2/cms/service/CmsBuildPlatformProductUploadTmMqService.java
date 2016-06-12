package com.voyageone.task2.cms.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.PlatformMappingService;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.promotion.PromotionDetailService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.base.BaseMQCmsService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.service.putaway.ConditionPropValueRepo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 天猫平台产品上新服务
 * Product表中产品不存在就向天猫平台新增商品，否则就更新商品
 *
 * @author desmond on 2016/5/11.
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_PlatformProductUploadTmJob)
public class CmsBuildPlatformProductUploadTmMqService extends BaseMQCmsService {

    @Autowired
    private ConditionPropValueRepo conditionPropValueRepo;
    @Autowired
    private PlatformProductUploadService platformProductUploadService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private TbProductService tbProductService;
    @Autowired
    private PlatformMappingService platformMappingService;
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

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        doMain(taskControlList);
    }

    /**
     * 天猫平台上新处理
     *
     * @param taskControlList taskcontrol信息
     */
    public void doMain(List<TaskControlBean> taskControlList) throws Exception {

        // 获取该任务可以运行的销售渠道
        List<String> channelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 初始化cms_mt_channel_condition_config表的条件表达式(避免多线程时2次初始化)
        conditionPropValueRepo.init();

        // 循环所有销售渠道
        if (channelIdList != null && channelIdList.size() > 0) {
            for (String channelId : channelIdList) {
                // TODO 虽然workload表里不想上新的渠道，不会有数据，这里的循环稍微有点效率问题，后面再改
                // 天猫平台商品信息新增或更新(天猫)
                doProductUpload(channelId, Integer.parseInt(CartEnums.Cart.TM.getId()));
                // 天猫国际商品信息新增或更新(天猫国际)
                doProductUpload(channelId, Integer.parseInt(CartEnums.Cart.TG.getId()));
                // 淘宝商品信息新增或更新(淘宝)
//                doProductUpload(channelId, Integer.parseInt(CartEnums.Cart.TB.getId()));
                // 天猫MiniMall商品信息新增或更新(天猫MiniMall)
//                doProductUpload(channelId, Integer.parseInt(CartEnums.Cart.TMM.getId()));
            }
        }

        // 正常结束
        $info("正常结束");
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
    }

    /**
     * 平台产品上新处理
     *
     * @param cmsBtSxWorkloadModel CmsBtSxWorkloadModel WorkLoad信息
     * @param shopProp             ShopBean 店铺信息
     */
    private void uploadProduct(CmsBtSxWorkloadModel cmsBtSxWorkloadModel, ShopBean shopProp) {
        // 当前groupid(用于取得产品信息)
        long groupId = cmsBtSxWorkloadModel.getGroupId();
        // 渠道id
        String channelId = shopProp.getOrder_channel_id();
        // 平台id
        int cartId = Integer.parseInt(shopProp.getCart_id());
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
        CmsMtPlatformMappingModel cmsMtPlatformMappingModel;
        // 平台类目id
        String platformCategoryId = "";

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
                sxData.setErrorMessage(errMsg);
                throw new BusinessException(errMsg);
            }

            // 属性值准备
            // 取得主产品类目对应的platform mapping数据
            cmsMtPlatformMappingModel = platformMappingService.getMappingByMainCatId(channelId, cartId, mainProduct.getCatId());
            if (cmsMtPlatformMappingModel == null) {
                String errMsg = String.format("共通PlatformMapping表中对应的平台Mapping信息不存在！[ChannelId:%s] [CartId:%s] [主产品类目:%s]",
                        channelId, cartId, mainProduct.getCatId());
                $error(errMsg);
                sxData.setErrorMessage(errMsg);
                throw new BusinessException(errMsg);
            }

            // 取得主产品类目对应的平台类目
            platformCategoryId = cmsMtPlatformMappingModel.getPlatformCategoryId();
            // 取得平台类目schema信息
            cmsMtPlatformCategorySchemaModel = platformCategoryService.getPlatformCatSchema(platformCategoryId, cartId);
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

            // 表达式解析子
            expressionParser = new ExpressionParser(sxProductService, sxData);
            // 平台产品id(MongoDB的)
            platformProductId = sxData.getPlatform().getPlatformPid();

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
                        uploadTmProductService.updateTmallProduct(expressionParser, platformProductId, cmsMtPlatformMappingModel, shopProp, getTaskName());
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
                uploadTmProductService.updateTmallProduct(expressionParser, platformProductId, cmsMtPlatformMappingModel, shopProp, getTaskName());
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
            String errMsg = String.format("天猫平台产品匹配或上传产品时异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s]",
                    channelId, cartId, groupId);
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
            // modified by morse.lu 2016/06/06 start
//            throw new BusinessException(ex.getMessage());
            return;
            // modified by morse.lu 2016/06/06 end
        }

        // 达尔文体系相关共通处理
        if (sxData.isDarwin()) {
            // TODO 达尔文相关共通处理暂时不做
        }

        // 天猫商品上新(新增或更新)处理
        // 如果平台产品id不为空的话，上传商品到天猫平台

        // delete by morse.lu 2016/06/06 start
//        if (!StringUtils.isEmpty(platformProductId)) {
            // 允许无产品，只有商品
            // delete by morse.lu 2016/06/06 end
            // 天猫商品上新处理
            try {
                // 新增或更新商品信息到天猫平台
                numIId = uploadTmItemService.uploadItem(expressionParser, platformProductId, cmsMtPlatformCategorySchemaModel, cmsMtPlatformMappingModel, shopProp, getTaskName());
                // 新增或更新商品结果判断
                if (!StringUtils.isEmpty(numIId)) {
                    // 上传商品成功的时候
                    // 上新或更新成功后回写product group表中的numIId和platformStatus(Onsale/InStock)
                    sxProductService.updateProductGroupNumIIdStatus(sxData, numIId, getTaskName());

                    // 回写ims_bt_product表(numIId)
                    sxProductService.updateImsBtProduct(sxData, getTaskName());

                    // 更新特价宝
                    updateTeJiaBaoPromotion(sxData);

                    // 回写workload表   (成功1)
                    sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, CmsConstants.SxWorkloadPublishStatusNum.okNum, getTaskName());
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
                // modified by morse.lu 2016/06/06 start
//                throw new BusinessException(ex.getMessage());
                return;
                // modified by morse.lu 2016/06/06 end
            }
//        }

        // 正常结束
        $info(String.format("天猫平台单个产品和商品新增或更新信息成功！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformProductId:%s] [itemId:%s]",
                channelId, cartId, groupId, platformProductId, numIId));
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
                if (sxProductModel.getSkus() == null || sxProductModel.getSkus().size() == 0) {
                    // 没有sku的code, 跳过
                    continue;
                }
                Double dblPrice = Double.parseDouble(sxProductModel.getSkus().get(0).getAttribute(tejiabaoPricePropName).toString());

                // 设置特价宝
                CmsBtPromotionCodesBean cmsBtPromotionCodesBean = new CmsBtPromotionCodesBean();
                cmsBtPromotionCodesBean.setPromotionId(0); // 设置为0的场合,李俊代码里会去处理
                cmsBtPromotionCodesBean.setChannelId(sxData.getChannelId());
                cmsBtPromotionCodesBean.setCartId(sxData.getCartId());
                cmsBtPromotionCodesBean.setProductCode(sxProductModel.getFields().getCode());
                cmsBtPromotionCodesBean.setProductId(sxProductModel.getProdId());
                cmsBtPromotionCodesBean.setPromotionPrice(dblPrice); // 真实售价
                cmsBtPromotionCodesBean.setNumIid(sxData.getPlatform().getNumIId());
                cmsBtPromotionCodesBean.setModifier(getTaskName());
                // 这里只需要调用更新接口就可以了, 里面会有判断如果没有的话就插入
                promotionDetailService.teJiaBaoPromotionUpdate(cmsBtPromotionCodesBean);
            }
        }
    }

}
