package com.voyageone.task2.cms.service;

import com.google.common.base.Joiner;
import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.ComplexField;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MD5;
import com.voyageone.common.util.StringUtils;
import com.voyageone.common.util.baidu.translate.BaiduTranslateUtil;
import com.voyageone.common.util.inch2cm.InchStrConvert;
import com.voyageone.service.bean.cms.Condition;
import com.voyageone.service.bean.cms.feed.FeedCustomPropWithValueBean;
import com.voyageone.service.bean.cms.product.ProductPriceBean;
import com.voyageone.service.bean.cms.product.ProductSkuPriceBean;
import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.dao.cms.CmsBtDataAmountDao;
import com.voyageone.service.dao.cms.mongo.*;
import com.voyageone.service.daoext.cms.CmsBtImagesDaoExt;
import com.voyageone.service.impl.cms.DataAmountService;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.impl.cms.feed.FeedCustomPropService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductPriceLogService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductSkuService;
import com.voyageone.service.model.cms.CmsBtDataAmountModel;
import com.voyageone.service.model.cms.CmsBtImagesModel;
import com.voyageone.service.model.cms.enums.MappingPropType;
import com.voyageone.service.model.cms.enums.Operation;
import com.voyageone.service.model.cms.enums.SrcType;
import com.voyageone.service.model.cms.mongo.CmsMtCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedMappingModel;
import com.voyageone.service.model.cms.mongo.feed.mapping.Mapping;
import com.voyageone.service.model.cms.mongo.feed.mapping.Prop;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.bean.ItemDetailsBean;
import com.voyageone.task2.cms.dao.ItemDetailsDao;
import com.voyageone.task2.cms.dao.MainPropDao;
import com.voyageone.task2.cms.dao.SuperFeedDao;
import com.voyageone.task2.cms.dao.TmpOldCmsDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class CmsSetMainPropMongoService extends BaseTaskService {

    @Autowired
    SuperFeedDao superfeeddao;

    @Autowired
    MainPropDao mainPropDao;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    CmsBtFeedInfoDao cmsBtFeedInfoDao; // DAO: feed数据
    @Autowired
    CmsBtFeedMappingDao cmsBtFeedMappingDao; // DAO: feed->主数据的mapping关系
    @Autowired
    CmsBtProductDao cmsBtProductDao; // DAO: 商品的值
    @Autowired
    MongoSequenceService commSequenceMongoService; // DAO: Sequence
    @Autowired
    CmsMtCategorySchemaDao cmsMtCategorySchemaDao; // DAO: 主类目属性结构
    @Autowired
    ItemDetailsDao itemDetailsDao; // DAO: ItemDetailsDao
    @Autowired
    TmpOldCmsDataDao tmpOldCmsDataDao; // DAO: 旧数据
    @Autowired
    FeedCustomPropService customPropService;
    @Autowired
    ProductSkuService productSkuService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductGroupService productGroupService;
    // jeff 2016/04 add start
    @Autowired
    ProductPriceLogService productPriceLogService;
    @Autowired
    private CmsBtImagesDaoExt cmsBtImageDaoExt;
    @Autowired
    private FeedInfoService feedInfoService;
    @Autowired
    private CmsBtDataAmountDao cmsBtDataAmountDao;
    // jeff 2016/04 add end
//    @Autowired
//    private ImagesService imagesService;

    @Autowired
    private DataAmountService dataAmountService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsSetMainPropMongoJob";
    }

    /**
     * feed数据 -> 主数据
     * 关联代码1 (从天猫获取Fields):
     * 当需要从天猫上,拉下数据填充到product表里的场合, skip_mapping_check会是1
     * 并且生成product之后, 会有一个程序来填满fields, 测试程序是:CmsPlatformProductImportServiceTest
     * 关联代码2 (切换主类目的时候):
     * 切换主类目的时候, 切换完毕后, 需要删除batchField里的switchFlg
     *
     * @param taskControlList job 配置
     * @throws Exception
     */
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据渠道运行
        for (final String orderChannelID : orderChannelIdList) {

            threads.add(new Runnable() {
                @Override
                public void run() {
                    // 获取是否跳过mapping check
                    String skip_mapping_check = TaskControlUtils.getVal2(taskControlList, TaskControlEnums.Name.order_channel_id, orderChannelID);
                    boolean bln_skip_mapping_check = true;
                    if (StringUtils.isEmpty(skip_mapping_check) || "0".equals(skip_mapping_check)) {
                        bln_skip_mapping_check = false;
                    }
                    // jeff 2016/04 change start
                    // 获取前一次的价格强制击穿时间
                    String priceBreakTime = TaskControlUtils.getEndTime(taskControlList, TaskControlEnums.Name.order_channel_id, orderChannelID);
                    // 主逻辑
                    // new setMainProp(orderChannelID, bln_skip_mapping_check).doRun();
                    new setMainProp(orderChannelID, bln_skip_mapping_check, priceBreakTime).doRun();
                    // jeff 2016/04 change end
                }
            });
        }

        runWithThreadPool(threads, taskControlList);
    }

    /**
     * 按渠道进行设置
     */
    public class setMainProp {
        private OrderChannelBean channel;
        private boolean skip_mapping_check;

        // jeff 2016/04 change start
        // 前一次的价格强制击穿时间
        private String priceBreakTime;

        // public setMainProp(String orderChannelId, boolean skip_mapping_check) {
        public setMainProp(String orderChannelId, boolean skip_mapping_check, String priceBreakTime) {
            this.channel = Channels.getChannel(orderChannelId);
            this.skip_mapping_check = skip_mapping_check;
            this.priceBreakTime = priceBreakTime;
        }

        int insertCnt = 0;
        int updateCnt = 0;
        // jeff 2016/04 change end

        public void doRun() {
            $info(channel.getFull_name() + "产品导入主数据开始");

            String channelId = this.channel.getOrder_channel_id();

            // 查找当前渠道,所有等待反映到主数据的商品
            // List<CmsBtFeedInfoModel> feedList = cmsBtFeedInfoDao.selectProductByUpdFlg(channelId, 0);
            String query = String.format("{ channelId: '%s', updFlg: %s}", channelId, 0);
            JomgoQuery queryObject = new JomgoQuery();
            queryObject.setQuery(query);
            queryObject.setLimit(500);
            List<CmsBtFeedInfoModel> feedList = feedInfoService.getList(channelId, queryObject);

            // --------------------------------------------------------------------------------------------
            // 品牌mapping表
            Map<String, String> mapBrandMapping = new HashMap<>();
            // --------------------------------------------------------------------------------------------

            if (feedList.size() > 0) {
                // --------------------------------------------------------------------------------------------
                // 品牌mapping做成
                List<TypeChannelBean> typeChannelBeanList;
                typeChannelBeanList = TypeChannels.getTypeList("brand", channelId);

                if (typeChannelBeanList != null) {
                    for (TypeChannelBean typeChannelBean : typeChannelBeanList) {
                        if (
                                !StringUtils.isEmpty(typeChannelBean.getAdd_name1())
                                        && !StringUtils.isEmpty(typeChannelBean.getName())
                                        && Constants.LANGUAGE.EN.equals(typeChannelBean.getLang_id())
                                ) {
                            mapBrandMapping.put(typeChannelBean.getAdd_name1(), typeChannelBean.getName());
                        }
                    }
                }
                // --------------------------------------------------------------------------------------------
                // 自定义属性 - 初始化
                customPropService.doInit(channelId);
                // --------------------------------------------------------------------------------------------
            }

            // 遍历所有数据
            for (CmsBtFeedInfoModel feed : feedList) {
                // 将商品从feed导入主数据
                // 注意: 保存单条数据到主数据的时候, 由于要生成group数据, group数据的生成需要检索数据库进行一系列判断
                //       所以单个渠道的数据, 最好不要使用多线程, 如果以后一定要加多线程的话, 注意要自己写带锁的代码.
                feed.setFullAttribute();
                doSaveProductMainProp(feed, channelId, mapBrandMapping);
            }

            // jeff 2016/04 add start
            // 将新建的件数，更新的件数插到cms_bt_data_amount表
            insertDataAmount();
            // jeff 2016/04 add end

            $info(channel.getFull_name() + "产品导入主数据结束");

        }

        /**
         * 将商品从feed导入主数据
         *
         * @param feed            商品信息
         * @param channelId       channel id
         * @param mapBrandMapping 品牌mapping一览
         */
        private void doSaveProductMainProp(
                CmsBtFeedInfoModel feed
                , String channelId
                , Map<String, String> mapBrandMapping
        ) {
            // feed类目名称
            String feedCategory = feed.getCategory();

            // [ feed -> main ] 类目属性的匹配关系
            CmsBtFeedMappingModel mapping;

            // 查看当前商品, 在主数据中, 是否已经存在
            boolean blnProductExist;
            CmsBtProductModel cmsProduct;
            cmsProduct = productService.getProductByCode(channelId, feed.getCode());
            if (cmsProduct == null) {
                // 不存在
                blnProductExist = false;

                // 获取类目属性匹配关系(默认的)
                mapping = cmsBtFeedMappingDao.findDefault(channelId, feedCategory, true);
            } else {
                // 已经存在
                blnProductExist = true;

                // 获取类目属性匹配关系(指定的主类目)
                mapping = cmsBtFeedMappingDao.selectByKey(channelId, feedCategory, cmsProduct.getCatPath());
            }

            // 默认不忽略mapping check的场合, 需要做mapping check
            if (!this.skip_mapping_check) {
                // 查看类目是否匹配完成
                if (mapping == null) {
                    // 记下log, 跳过当前记录
                    if (cmsProduct == null) {
//                    logIssue(getTaskName(), String.format("[CMS2.0][测试]该feed类目, 没有匹配到主数据的类目 ( channel: [%s], feed: [%s] )", channelId, feed.getCategory()));
                        $warn(String.format("[CMS2.0][测试]该feed类目, 没有匹配到主数据的类目 ( channel: [%s], feed: [%s] )", channelId, feed.getCategory()));
                    } else {
//                    logIssue(getTaskName(), String.format("[CMS2.0][测试]该feed类目, 没有匹配到主数据的类目 ( channel: [%s], feed: [%s], master: [%s] )", channelId, feed.getCategory(), cmsProduct.getCatPath()));
                        $warn(String.format("[CMS2.0][测试]该feed类目, 没有匹配到主数据的类目 ( channel: [%s], feed: [%s], master: [%s] )", channelId, feed.getCategory(), cmsProduct.getCatPath()));
                    }

                    return;
                }
                // 查看属性是否匹配完成
                if (mapping.getMatchOver() == 0) {
                    // 如果没有匹配完成的话, 那就看看是否有共通
                    mapping = cmsBtFeedMappingDao.findDefaultMainMapping(channelId, mapping.getMainCategoryPath());
                    if (mapping == null || mapping.getMatchOver() == 0) {
                        // 没有共通mapping, 或者没有匹配完成
                        // 记下log, 跳过当前记录
//                        logIssue(getTaskName(), String.format("[CMS2.0][测试]该主类目的属性匹配尚未完成 ( channel: [%s], feed: [%s], main: [%s] )", channelId, feed.getCategory(), mapping.getScope().getMainCategoryPath()));
                        $warn(String.format("[CMS2.0][测试]该主类目的属性匹配尚未完成 ( channel: [%s], feed: [%s], main: [%s] )", channelId, feed.getCategory(), mapping.getMainCategoryPath()));

                        return;
                    }

                }
            }

            if (blnProductExist) {
                // 修改商品数据
                // 一般只改改价格神马的
                cmsProduct = doUpdateCmsBtProductModel(feed, cmsProduct, mapping, mapBrandMapping);
                // TODO: 没有设置的fields里的内容, 不会被清除? 这个应该是在共通里做掉的吧, 要是共通里不做的话就要自己写了

                // 清除一些batch的标记 // TODO: 梁兄啊, batchField的更新没有放到product更新里, 暂时自己写一个用, 这里暂时注释掉
//                CmsBtProductModel_BatchField batchField = cmsProduct.getBatchField();
//                batchField.setAttribute("switchCategory", "0"); // 切换主类目->完成
//                cmsProduct.setBatchField(batchField);

                ProductUpdateBean requestModel = new ProductUpdateBean();
                requestModel.setProductModel(cmsProduct);
                requestModel.setModifier(getTaskName());
                requestModel.setIsCheckModifed(false); // 不做最新修改时间ｃｈｅｃｋ

                productService.updateProduct(channelId, requestModel);

                // TODO: 梁兄啊, batchField的更新没有放到product更新里, 暂时自己写一个用
                // TODO: 等改好后下面这段内容就可以删掉了
                {
                    List<BulkUpdateModel> bulkList = new ArrayList<>();

                    HashMap<String, Object> updateMap = new HashMap<>();
                    updateMap.put("batchField.switchCategory", 0);

                    HashMap<String, Object> queryMap = new HashMap<>();
                    queryMap.put("prodId", cmsProduct.getProdId());

                    BulkUpdateModel model = new BulkUpdateModel();
                    model.setUpdateMap(updateMap);
                    model.setQueryMap(queryMap);
                    bulkList.add(model);

                    BulkWriteResult result = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, getTaskName(), "$set");

                }

                $info(getTaskName() + ":更新:" + cmsProduct.getChannelId() + ":" + cmsProduct.getFields().getCode());
                // jeff 2016/04 add start
                updateCnt++;
                // jeff 2016/04 add end

            } else {
                // 不存在的场合, 新建一个product
                cmsProduct = doCreateCmsBtProductModel(feed, mapping, mapBrandMapping);
                if (cmsProduct == null) {
                    // 有出错, 跳过
                    $error(getTaskName() + ":新增:编辑商品的时候出错:" + feed.getChannelId() + ":" + feed.getCode());

                    return;
                }

                productService.createProduct(channelId, cmsProduct, getTaskName());

                $info(getTaskName() + ":新增:" + cmsProduct.getChannelId() + ":" + cmsProduct.getFields().getCode());
                // jeff 2016/04 add start
                insertCnt++;
                // jeff 2016/04 add end
            }

            // jeff 2016/04 change start
            // 生成更新前的价格履历Bean
            ProductPriceBean productPriceBeanBefore = getProductPriceBeanBefore(cmsProduct, blnProductExist);

            // 调用共通方法来设置价格
            // doSetPrice(channelId, feed, cmsProduct);
            ProductPriceBean productPriceBean = doSetPrice(channelId, feed, cmsProduct, mapping);

            // 更新价格履历
            productPriceLogService.insertPriceLog(channelId, productPriceBean, productPriceBeanBefore, "Feed导入Master价格更新", getTaskName());

            // 自动上新
            // 是否自动上新标志
            String sxFlg = "0";
            CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.AUTO_APPROVE_PRODUCT_CHANGE);
            if (cmsChannelConfigBean != null && !StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
                // 如果没有设定则不自动上新
                sxFlg = cmsChannelConfigBean.getConfigValue1();
            }

            // 自动上新标志位 = 1:(自动上新) 并且 商品的状态是Approved的场合
            if ("1".equals(sxFlg) && CmsConstants.ProductStatus.Approved == CmsConstants.ProductStatus.valueOf(cmsProduct.getFields().getStatus())) {
                productService.insertSxWorkLoad(channelId, cmsProduct, getTaskName());
            }
            // jeff 2016/04 change end

            // 更新wms_bt_item_details表的数据
            doSaveItemDetails(channelId, cmsProduct.getProdId(), feed);

            // 更新price_log信息
            // 更新price_log信息 -> 共通代码里会处理的,我这边就不需要写了
            // 更新product_log信息
            // 更新product_log信息 -> 还要不要写呢? 状态变化的话,共通代码里已经有了,其他的变化,这里是否要更新进去? 应该不用了吧.

            // 设置商品更新完成
            feed.setUpdFlg(1);
            cmsBtFeedInfoDao.update(feed);

            // ------------- 函数结束

        }

        /**
         * 生成Fields的内容
         *
         * @param feed            feed的商品信息
         * @param mapping         feed与main的匹配关系
         * @param mapBrandMapping 品牌mapping一览
         * @param schemaModel     主类目的属性的schema
         * @param newFlg          新建flg (true:新建商品 false:更新的商品)
         * @param productField    商品属性
         * @return 返回整个儿的Fields的内容
         */
        // jeff 2016/04 change start
        // private CmsBtProductModel_Field doCreateCmsBtProductModelField(CmsBtFeedInfoModel feed, CmsBtFeedMappingModel mapping, Map<String, String> mapBrandMapping, CmsMtCategorySchemaModel schemaModel, boolean newFlg) {
        private CmsBtProductModel_Field doCreateCmsBtProductModelField(CmsBtFeedInfoModel feed, CmsBtFeedMappingModel mapping, Map<String, String> mapBrandMapping, CmsMtCategorySchemaModel schemaModel, boolean newFlg, CmsBtProductModel_Field productField) {
            // --------- 商品属性信息设定 ------------------------------------------------------
            CmsBtProductModel_Field field = new CmsBtProductModel_Field();

            if (!skip_mapping_check) {
                // 遍历mapping,设置主数据的属性
                if (mapping.getProps() != null) {
                    for (Prop prop : mapping.getProps()) {
                        if (!MappingPropType.FIELD.equals(prop.getType())) {
                            // 这段逻辑只处理类目属性(FIELD类型)的,如果是SKU属性或共通属性,则跳过
                            continue;
                        }

                        // 递归设置属性
                        // jeff 2016/04 change start
                        // field.put(prop.getProp(), getPropValueByMapping(prop.getProp(), prop, feed, field, schemaModel));
                        Object propValue = getPropValueByMapping(prop.getProp(), prop, feed, field, schemaModel);
                        if (propValue != null) {
                            field.put(prop.getProp(), propValue);
                        }
                        // jeff 2016/04 change end
                    }
                }
            }

            // 新建的场合才设置的属性
//            if (newFlg) {
            // TODO: 现在mapping画面功能还不够强大, 共通属性和SKU属性先暂时写死在代码里, 等我写完上新代码回过头来再想办法改 (tom.zhu)
            // 主数据的field里, 暂时强制写死的字段(共通属性)
            // 产品code
            if (newFlg || (!newFlg && StringUtils.isEmpty(productField.getCode()))) {
                field.setCode(feed.getCode());
            }

            // 品牌
            if (newFlg || (!newFlg && StringUtils.isEmpty(productField.getBrand()))) {
                if (mapBrandMapping.containsKey(feed.getBrand())) {
                    field.setBrand(mapBrandMapping.get(feed.getBrand()));
                } else {
                    $error(getTaskName() + ":" + String.format("[CMS2.0][测试]feed->main的品牌mapping没做 ( channel id: [%s], feed brand: [%s] )", feed.getChannelId(), feed.getBrand()));

                    // 记下log, 跳过当前记录
                    //                logIssue(getTaskName(), String.format("[CMS2.0][测试]feed->main的品牌mapping没做 ( channel id: [%s], feed brand: [%s] )", feed.getChannelId(), feed.getBrand()));
                    $warn(String.format("[CMS2.0][测试]feed->main的品牌mapping没做 ( channel id: [%s], feed brand: [%s] )", feed.getChannelId(), feed.getBrand()));

                    return null;
                }
            }
            // 产品名称（英文）
            if (newFlg || (!newFlg && StringUtils.isEmpty(productField.getProductNameEn()))) {
                field.setProductNameEn(feed.getName());
            }
            // 长标题, 中标题, 短标题: 都是中文, 需要自己翻译的
            // 款号model
            if (newFlg || (!newFlg && StringUtils.isEmpty(productField.getModel()))) {
                field.setModel(feed.getModel());
            }
            // 颜色
            if (newFlg || (!newFlg && StringUtils.isEmpty(productField.getColor()))) {
                field.setColor(feed.getColor());
            }
            // 产地
            if (newFlg || (!newFlg && StringUtils.isEmpty(productField.getOrigin()))) {
                field.setOrigin(feed.getOrigin());
            }
            // 简短描述英文
            if (newFlg || (!newFlg && StringUtils.isEmpty(productField.getShortDesEn()))) {
                field.setShortDesEn(feed.getShortDescription());
            }
            // 详情描述英文
            if (newFlg || (!newFlg && StringUtils.isEmpty(productField.getLongDesEn()))) {
                field.setLongDesEn(feed.getLongDescription());
            }
            // 税号集货: 不要设置\
            // 税号个人
            if (newFlg || (!newFlg && StringUtils.isEmpty(productField.getHsCodePrivate()))) {
                field.setHsCodePrivate(getPropSimpleValueByMapping(MappingPropType.COMMON, Constants.productForOtherSystemInfo.HS_CODE_PRIVATE, mapping));
            }
            // 产品状态
            if (newFlg || !newFlg && StringUtils.isEmpty(productField.getStatus())) {
                field.setStatus(CmsConstants.ProductStatus.New); // 产品状态: 初始时期为(新建) Synship.com_mt_type : id = 44 : productStatus
            }

            {
                // 所有的翻译内容
                String transFlg = "";
                Map<String, String> mapTrans = customPropService.getTransList(feed.getChannelId(), feed.getCategory());
                // 翻译(标题 和 长描述)
                String strProductNameEn = field.getProductNameEn();
                String strLongDesEn = field.getLongDesEn();
                for (Map.Entry<String, String> entry : mapTrans.entrySet()) {
                    strProductNameEn = strProductNameEn.replace(entry.getKey(), entry.getValue());
                    strLongDesEn = strLongDesEn.replace(entry.getKey(), entry.getValue());
                }
                // 调用百度翻译
                List<String> transBaiduOrg = new ArrayList<>(); // 百度翻译 - 输入参数
                if (newFlg || !newFlg && StringUtils.isEmpty(productField.getOriginalTitleCn())) {
                    transBaiduOrg.add(strProductNameEn); // 标题
                    transFlg = "标题";
                }
                if (newFlg || !newFlg && StringUtils.isEmpty(productField.getOriginalDesCn())) {
                    if (!StringUtils.isEmpty(strLongDesEn)) {
                        // TODO: 临时关掉017
                        if ("010".equals(feed.getChannelId())) {
                            $info("英寸转厘米:原始:" + strLongDesEn);
                            transBaiduOrg.add(new InchStrConvert().inchToCM(strLongDesEn)); // 长描述
                        } else {
                            transBaiduOrg.add(strLongDesEn); // 长描述
                        }
                        transFlg = "长描述";
                    }
                }
                List<String> transBaiduCn; // 百度翻译 - 输出参数
                try {
                    if ("017".equals(feed.getChannelId())) {
                        // lucky vitamin 不做翻译
                        if (newFlg || !newFlg && StringUtils.isEmpty(productField.getOriginalTitleCn())) {
                            field.setOriginalTitleCn(""); // 标题
                        }
                        if (newFlg || !newFlg && StringUtils.isEmpty(productField.getOriginalDesCn())) {
                            field.setOriginalDesCn(""); // 长描述
                        }
                    } else {
                        if (transBaiduOrg.size() > 0) {
                            transBaiduCn = BaiduTranslateUtil.translate(transBaiduOrg);
                            if (transBaiduOrg.size() == 2) {
                                field.setOriginalTitleCn(transBaiduCn.get(0)); // 标题
                                field.setOriginalDesCn(transBaiduCn.get(1)); // 长描述
                            } else {
                                if ("标题".equals(transFlg)) {
                                    field.setOriginalTitleCn(transBaiduCn.get(0)); // 标题
                                } else {
                                    field.setOriginalDesCn(transBaiduCn.get(0)); // 长描述
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    // 翻译失败的场合,全部设置为空, 运营自己翻译吧
                    if (newFlg || !newFlg && StringUtils.isEmpty(productField.getOriginalTitleCn())) {
                        field.setOriginalTitleCn(""); // 标题
                    }
                    if (newFlg || !newFlg && StringUtils.isEmpty(productField.getOriginalDesCn())) {
                        field.setOriginalDesCn(""); // 长描述
                    }
                }
            }
//            }

            // 官方网站链接，商品图片1，产品分类，适用人群的Feed数据可能会变化，所以不管新建还是更新操作都会去重新设定
            // 官方网站链接
            if (feed.getClientProductURL() == null) {
                field.setClientProductUrl("");
            } else {
                field.setClientProductUrl(feed.getClientProductURL());
            }

            // 商品图片1, 包装图片2, 带角度图片3, 自定义图片4 : 暂时只设置商品图片1
            {
                List<Map<String, Object>> multiComplex = new LinkedList<>();

                List<String> lstImageOrg = feed.getImage();
                if (lstImageOrg != null && lstImageOrg.size() > 0) {
                    for (String imgOrg : lstImageOrg) {
                        Map<String, Object> multiComplexChildren = new HashMap<>();
                        // jeff 2016/04 change start
                        // multiComplexChildren.put("image1", imgOrg);
                        multiComplexChildren.put("image1", doUpdateImage(feed.getChannelId(), feed.getCode(), imgOrg));
                        // jeff 2016/04 add end
                        multiComplex.add(multiComplexChildren);
                    }
                }

                field.put("images1", multiComplex);
            }

            // 商品翻译状态, 翻译者, 翻译时间, 商品编辑状态, 价格审批flg, lock商品: 暂时都不用设置

//            SELECT * from Synship.com_mt_value_channel where type_id in (57,58);
            switch (feed.getChannelId()) {
                case "010":
                    // 产品分类
                    field.setProductType(feed.getAttribute().get("ItemClassification").get(0));
                    // 适用人群
                    switch (feed.getSizeType()) {
                        case "Women's":
                            field.setSizeType("women");
                            break;
                        case "Men's":
                            field.setSizeType("men");
                            break;
                    }

                    break;
                case "012":
                    break;
            }
            // jeff 2016/04 change end

            // jeff 2016/04 add start
            if (newFlg || !newFlg && productField.getAttribute("isMasterMain") == null) {
                // isMain
                field.setIsMasterMain(getIsMasterMain(feed));
            }
            // jeff 2016/04 add end

            return field;
        }

        /**
         * 生成一个新的product
         *
         * @param feed            feed的商品信息
         * @param mapping         feed与main的匹配关系
         * @param mapBrandMapping 品牌mapping一览
         * @return 一个新的product的内容
         */
        private CmsBtProductModel doCreateCmsBtProductModel(
                CmsBtFeedInfoModel feed
                , CmsBtFeedMappingModel mapping
                , Map<String, String> mapBrandMapping
        ) {
            if (skip_mapping_check) {
                // 如果允许条股检查的场合, 说明是正在执行旧系统迁移到新系统
                // 那么就需要到旧数据库里看一下这个数据在旧系统里是否存在, 如果不存在, 那么这条数据有问题, 不能直接迁移
                int cnt = tmpOldCmsDataDao.checkExist(feed.getChannelId(), feed.getCode());
                if (cnt == 0) {
                    // 不存在, 直接跳出
                    $warn(String.format("[CMS2.0][测试]feed->mapping, 未上新过, 不能直接导入.channel id: [%s], code:[%s]", feed.getChannelId(), feed.getCode()));
                    return null;
                }
            }

            // 新创建的product
            CmsBtProductModel product = new CmsBtProductModel();

            // --------- 基本信息设定 ------------------------------------------------------
            product.setChannelId(feed.getChannelId());

            if (!skip_mapping_check) {
                String catPath = mapping.getMainCategoryPath();
                product.setCatId(MD5.getMD5(catPath)); // 主类目id
                product.setCatPath(catPath); // 主类目path
            }
            product.setProdId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_PROD_ID)); // 商品的id

            // --------- 获取主类目的schema信息 ------------------------------------------------------
            CmsMtCategorySchemaModel schemaModel = cmsMtCategorySchemaDao.getMasterSchemaModelByCatId(product.getCatId());
            // jeff 2016/04 change start
            // CmsBtProductModel_Field field = doCreateCmsBtProductModelField(feed, mapping, mapBrandMapping, schemaModel, true);
            CmsBtProductModel_Field field = doCreateCmsBtProductModelField(feed, mapping, mapBrandMapping, schemaModel, true, new CmsBtProductModel_Field());
            // jeff 2016/04 change end
            if (field == null) {
                return null;
            }
            // ProductCarts
            product.setCarts(getProductCarts(feed));
            product.setFields(field);

            // 获取当前channel, 有多少个platform
            List<TypeChannelBean> typeChannelBeanListApprove = TypeChannels.getTypeListSkuCarts(feed.getChannelId(), "A", "en"); // 取得允许Approve的数据
            if (typeChannelBeanListApprove == null) {
                return null;
            }
            List<Integer> skuCarts = new ArrayList<>();
            for (TypeChannelBean typeChannelBean : typeChannelBeanListApprove) {
                skuCarts.add(Integer.parseInt(typeChannelBean.getValue()));
            }

            // SKU级属性列表
            List<String> skuFieldSchemaList = new ArrayList<>();
            if (!skip_mapping_check) {
                MultiComplexField skuFieldSchema = (MultiComplexField) schemaModel.getSku();
                List<Field> fieldList = skuFieldSchema.getFields();
                for (Field f : fieldList) {
                    skuFieldSchemaList.add(f.getId());
                }
            }

            // --------- 商品Sku信息设定 ------------------------------------------------------
            List<CmsBtProductModel_Sku> mainSkuList = new ArrayList<>();
            for (CmsBtFeedInfoModel_Sku sku : feed.getSkus()) {
                // 设置单个sku的信息
                CmsBtProductModel_Sku mainSku = new CmsBtProductModel_Sku();

                mainSku.setSkuCode(sku.getSku()); // sku
                mainSku.setBarcode(sku.getBarcode()); // barcode
                mainSku.setSize(sku.getSize()); // 尺码

//                mainSku.setPriceMsrp(sku.getPrice_msrp());// msrp -> 共通API进行设置
//                mainSku.setPriceRetail(sku.getPrice_current()); // 零售价: 未审批 -> 共通API进行设置
//                mainSku.setPriceSale(sku); // 销售价: 已审批 (不用自动设置)

                if (!skip_mapping_check) {
                    // 设置一些每个类目不一样的, sku级别的属性
                    mainSku.putAll(doSetCustomSkuInfo(feed, skuFieldSchemaList));
                }

                // 增加默认渠道
                mainSku.setSkuCarts(skuCarts);

                mainSkuList.add(mainSku);
            }
            product.setSkus(mainSkuList);

            // --------- batchFields ------------------------------------------------------
            CmsBtProductModel_BatchField batchField = new CmsBtProductModel_BatchField();
            batchField.setAttribute("switchCategory", "0"); // 初始化: 切换主类目->完成
            product.setBatchField(batchField);

            // --------- 商品Feed信息设定 ------------------------------------------------------
            BaseMongoMap mainFeedOrgAtts = new BaseMongoMap();
            List<String> mainFeedOrgAttsKeyList = new ArrayList<>(); // 等待翻译的key
            List<String> mainFeedOrgAttsKeyCnList = new ArrayList<>(); // 等待翻译的key的中文
            List<String> mainFeedOrgAttsValueList = new ArrayList<>(); // 等待翻译的value
            // 遍历所有的feed属性
            List<FeedCustomPropWithValueBean> feedCustomPropList = customPropService.getPropList(feed.getChannelId(), feed.getCategory());
            Map<String, String> feedCustomProp = new HashMap<>();
            for (FeedCustomPropWithValueBean propModel : feedCustomPropList) {
                feedCustomProp.put(propModel.getFeed_prop_original(), propModel.getFeed_prop_translation());
            }
            for (Map.Entry<String, Object> attr : feed.getFullAttribute().entrySet()) {
                String valString = String.valueOf(attr.getValue());

                // 看看这个字段是否需要翻译
                if (feedCustomProp.containsKey(attr.getKey())) {
                    // 原始语言
                    mainFeedOrgAtts.setAttribute(attr.getKey(), valString);

                    // 放到list里, 用来后面的程序翻译用
                    mainFeedOrgAttsKeyList.add(attr.getKey());
                    mainFeedOrgAttsKeyCnList.add(feedCustomProp.get(attr.getKey()));
                    mainFeedOrgAttsValueList.add(valString);
                }
            }
            for (Map.Entry<String, List<String>> attr : feed.getAttribute().entrySet()) {
                String valString = Joiner.on(", ").skipNulls().join(attr.getValue());

                // 看看这个字段是否需要翻译
                if (feedCustomProp.containsKey(attr.getKey())) {
                    // 原始语言
                    mainFeedOrgAtts.setAttribute(attr.getKey(), valString);

                    // 放到list里, 用来后面的程序翻译用
                    mainFeedOrgAttsKeyList.add(attr.getKey());
                    mainFeedOrgAttsKeyCnList.add(feedCustomProp.get(attr.getKey()));
                    mainFeedOrgAttsValueList.add(valString);
                }
            }
            // 增加一个modelCode(来源是feed的field的model, 无需翻译)
            mainFeedOrgAtts.setAttribute("modelCode", feed.getModel());
            mainFeedOrgAtts.setAttribute("categoryCode", feed.getCategory());

            product.getFeed().setOrgAtts(mainFeedOrgAtts);

            // 翻译成中文
            BaseMongoMap mainFeedCnAtts = new BaseMongoMap();

            // 不是很想用百度翻译 ---------------------------------------------------------------- START
            // 注意: 用了百度翻译之后, 程序跑得好慢啊, 不过这也是没办法的事情, 能不用就暂时不用吧, 反正这里属性值的翻译已经都匹配过了
//            // 调用百度翻译
//            List<String> mainFeedCnAttsBaidu; // 给百度翻译用来存放翻译结果用的
//            try {
//                mainFeedCnAttsBaidu = BaiduTranslateUtil.translate(mainFeedOrgAttsValueList);
//
//                for (int i = 0; i < mainFeedOrgAttsKeyList.size(); i++) {
//                    mainFeedCnAtts.setAttribute(mainFeedOrgAttsKeyList.get(i), mainFeedCnAttsBaidu.get(i));
//                }
//            } catch (Exception e) {
//                // 翻译失败的场合,全部设置为空, 运营自己翻译吧
//                for (String aMainFeedOrgAttsKeyList : mainFeedOrgAttsKeyList) {
//                    mainFeedCnAtts.setAttribute(aMainFeedOrgAttsKeyList, "");
//                }
//            }
            // 不是很想用百度翻译 ---------------------------------------------------------------- END

            // 等百度翻译完成之后, 再自己根据customPropValue表里的翻译再翻译一次, 因为百度翻译的内容可能不是很适合某些专业术语
            {
                // 最终存放的地方(中文): mainFeedCnAtts
                // 属性名列表(英文): mainFeedOrgAttsKeyList
                // 属性值列表(英文): mainFeedOrgAttsValueList
                for (int i = 0; i < mainFeedOrgAttsKeyList.size(); i++) {
                    String strTrans = customPropService.getPropTrans(
                            feed.getChannelId(),
                            feed.getCategory(),
                            mainFeedOrgAttsKeyList.get(i),
                            mainFeedOrgAttsValueList.get(i));
                    // 如果有定义过的话, 那么就用翻译过的
                    if (!StringUtils.isEmpty(strTrans)) {
                        mainFeedCnAtts.setAttribute(mainFeedOrgAttsKeyList.get(i), strTrans);
                        continue;
                    }

                    // ------------------------------------------------------------------------
                    // 看看是否属于不想翻译的, 如果是不想翻译的内容, 那就直接用英文还原
                    //   TODO: 目前能想到的就是(品牌)(全数字)(含有英寸的), 如果以后店铺开得多了知道得比较全面了之后, 这段要共通出来的
                    // ------------------------------------------------------------------------START
                    // 看看是否是品牌
                    if ("brand".equals(mainFeedOrgAttsKeyList.get(i))) {
                        mainFeedCnAtts.setAttribute(mainFeedOrgAttsKeyList.get(i), mainFeedOrgAttsValueList.get(i));
                        continue;
                    }
                    // 看看是否是数字
                    if (StringUtils.isNumeric(mainFeedOrgAttsValueList.get(i))) {
                        mainFeedCnAtts.setAttribute(mainFeedOrgAttsKeyList.get(i), mainFeedOrgAttsValueList.get(i));
                        continue;
                    }
                    // 看看字符串里是否包含英寸
                    if (mainFeedOrgAttsValueList.get(i).contains("inches")) {
                        mainFeedCnAtts.setAttribute(mainFeedOrgAttsKeyList.get(i), mainFeedOrgAttsValueList.get(i));
                        continue;
                    }
                    // ------------------------------------------------------------------------END

                    // 翻不出来的, 又不想用百度翻译的时候, 就设置个空吧
                    mainFeedCnAtts.setAttribute(mainFeedOrgAttsKeyList.get(i), "");

                }
            }

            // 增加一个modelCode(来源是feed的field的model, 无需翻译)
            mainFeedCnAtts.setAttribute("modelCode", feed.getModel());
            mainFeedCnAtts.setAttribute("categoryCode", feed.getCategory());
            product.getFeed().setCnAtts(mainFeedCnAtts);

            product.getFeed().setCustomIds(mainFeedOrgAttsKeyList); // 自定义字段列表
            product.getFeed().setCustomIdsCn(mainFeedOrgAttsKeyCnList); // 自定义字段列表(中文)

            // --------- 商品Group信息设定 ------------------------------------------------------
            // 创建新的group
            // jeff 2016/04 change start
//            CmsBtProductGroupModel group = doSetGroup(feed, product);
//            product.setGroups(group);
            doSetGroup(feed);
            // jeff 2016/04 change end

            return product;
        }

        private Map<String, String> doSetCustomSkuInfo(CmsBtFeedInfoModel feed, List<String> skuFieldSchemaList) {
            Map<String, String> mainSku = new HashMap<>();

            // TODO: 现在mapping画面功能还不够强大, 共通属性和SKU属性先暂时写死在代码里, 等我写完上新代码回过头来再想办法改 (tom.zhu)
            // -------------------- START
            switch (feed.getChannelId()) {
                case "010":
                    // ==============================================
                    // jewelry要设置三个属性: 戒指手寸, 项链长度, 手链长度
                    // ==============================================

//                    String 戒指手寸_MASTER = "prop_9066257";
                    String 戒指手寸_MASTER = "alias_name";
                    String 戒指手寸_FEED = "Ringsize";
                    String 项链长度_MASTER = "in_prop_150988152";
                    String 项链长度_FEED = "ChainLength";
                    String 手链长度_MASTER = "in_prop_151018199";
                    String 手链长度_FEED = "Bracelet Length";

                    // 戒指手寸
                    if (skuFieldSchemaList.contains(戒指手寸_MASTER)) {
                        if (feed.getAttribute().get(戒指手寸_FEED) != null && feed.getAttribute().get(戒指手寸_FEED).size() > 0) {
                            String val = feed.getAttribute().get(戒指手寸_FEED).get(0);

                            mainSku.put(戒指手寸_MASTER, val);
                        }
                    }
                    // 项链长度
                    if (skuFieldSchemaList.contains(项链长度_MASTER)) {
                        if (feed.getAttribute().get(项链长度_FEED) != null && feed.getAttribute().get(项链长度_FEED).size() > 0) {
                            String val = feed.getAttribute().get(项链长度_FEED).get(0);

                            val = doEditSkuTemplate(val, "in2cm", "", "cm");
                            mainSku.put(项链长度_MASTER, val);
                        }
                    }
                    // 手链长度
                    if (skuFieldSchemaList.contains(手链长度_MASTER)) {
                        if (feed.getAttribute().get(手链长度_FEED) != null && feed.getAttribute().get(手链长度_FEED).size() > 0) {
                            String val = feed.getAttribute().get(手链长度_FEED).get(0);

                            val = doEditSkuTemplate(val, "in2cm", "", "cm");
                            mainSku.put(手链长度_MASTER, val);
                        }
                    }

                    break;
                case "012":
                    // BCBG暂时没有这方面需求
                    break;
            }
            // -------------------- END

            return mainSku;
        }

        /**
         * 更新product
         *
         * @param feed            feed的商品信息
         * @param product         主数据的product
         * @param mapping         feed与main的匹配关系
         * @param mapBrandMapping 品牌mapping一览
         * @return 修改过的product的内容
         */
        private CmsBtProductModel doUpdateCmsBtProductModel(CmsBtFeedInfoModel feed, CmsBtProductModel product, CmsBtFeedMappingModel mapping, Map<String, String> mapBrandMapping) {

            // 注意: 价格是在外面共通方法更新的, 这里不需要更新

            // --------- 获取主类目的schema信息 ------------------------------------------------------
            CmsMtCategorySchemaModel schemaModel = cmsMtCategorySchemaDao.getMasterSchemaModelByCatId(product.getCatId());
            // 更新Fields字段
            // jeff 2016/04 change start
            // CmsBtProductModel_Field field = doCreateCmsBtProductModelField(feed, mapping, mapBrandMapping, schemaModel, false);
            CmsBtProductModel_Field field = doCreateCmsBtProductModelField(feed, mapping, mapBrandMapping, schemaModel, false, product.getFields());
            // jeff 2016/04 change end
            if (field == null) {
                return null;
            }

            // 商品的状态
            String productStatus = product.getFields().getStatus();
//            // TODO: 暂时这个功能封印, 有些店铺的运营其实并不是很希望重新确认一下, 而不确认的话, 其实问题也不是很大 START
//            if (CmsConstants.ProductStatus.Approved.toString().equals(productStatus)) {
//                // 如果曾经的状态是已经approved的话, 需要把状态改回ready, 让运营重新确认一下商品
//                productStatus = CmsConstants.ProductStatus.Ready.toString();
//            }
//            // TODO: 暂时这个功能封印, 有些店铺的运营其实并不是很希望重新确认一下, 而不确认的话, 其实问题也不是很大 END
            field.setStatus(productStatus);
            // jeff 2016/04 add start
            // 更新的场合，虽然不更新下面2个字段，但是之后要用到这2个字段，所以原样取出
            if (StringUtils.isEmpty(field.getCode())) {
                field.setCode(product.getFields().getCode());
            }
            if (StringUtils.isEmpty(field.getHsCodePrivate())) {
                field.setHsCodePrivate(product.getFields().getHsCodePrivate());
            }
            // jeff 2016/04 add end
            product.setFields(field);

            // SKU级属性列表
            List<String> skuFieldSchemaList = new ArrayList<>();
            if (!skip_mapping_check) {
                MultiComplexField skuFieldSchema = (MultiComplexField) schemaModel.getSku();
                List<Field> fieldList = skuFieldSchema.getFields();
                for (Field f : fieldList) {
                    skuFieldSchemaList.add(f.getId());
                }
            }

            // 遍历feed的skus
            for (CmsBtFeedInfoModel_Sku feedSku : feed.getSkus()) {
                // 遍历主数据product里的sku,看看有没有
                boolean blnFound = false;
                for (CmsBtProductModel_Sku sku : product.getSkus()) {
                    if (feedSku.getSku().equals(sku.getSkuCode())) {
                        blnFound = true;
                        break;
                    }
                }

                // 如果找到了,那就什么都不做,如果没有找到,那么就需要添加
                if (!blnFound) {
                    CmsBtProductModel_Sku sku = new CmsBtProductModel_Sku();
                    sku.setSkuCode(feedSku.getSku());
                    sku.setBarcode(feedSku.getBarcode()); // barcode
                    sku.setSize(feedSku.getSize()); // 尺码

//                    sku.setPriceMsrp(feedSku.getPrice_msrp()); // msrp -> 共通API进行设置
//                    sku.setPriceRetail(feedSku.getPrice_current()); // 零售价: 未审批 -> 共通API进行设置

                    if (!skip_mapping_check) {
                        // 设置一些每个类目不一样的, sku级别的属性
                        sku.putAll(doSetCustomSkuInfo(feed, skuFieldSchemaList));
                    }

                    product.getSkus().add(sku);
                }

            }

            // --------- 商品Group信息设定 ------------------------------------------------------
            // 设置group(现有的不变, 有增加的就增加)
            // jeff 2016/04 change start
//            CmsBtProductGroupModel group = doSetGroup(feed, product);
//            product.setGroups(group);
            doSetGroup(feed);
            // jeff 2016/04 change end

            // TOM 20160413 这是一个错误, 这段话不应该要的 START
//            // 更新状态, 准备重新上传到各个平台
//            for (CmsBtProductModel_Group_Platform platform : product.getGroups().getPlatforms()) {
//                platform.setPlatformStatus(CmsConstants.PlatformStatus.WaitingPublish);
//            }
            // TOM 20160413 这是一个错误, 这段话不应该要的 END


            return product;
        }

        /**
         * 设置group
         *
         * @param feed 品牌方提供的数据
         * @return 设置好了的group
         */
        // jeff 2016/04 change start
        // private CmsBtProductGroupModel doSetGroup(CmsBtFeedInfoModel feed, CmsBtProductModel product) {
        private void doSetGroup(CmsBtFeedInfoModel feed) {
//            CmsBtProductGroupModel group = product.getGroups();
//            if (group == null) {
//                group = new CmsBtProductGroupModel();
//            }

            // 获取当前channel, 有多少个platform
            List<TypeChannelBean> typeChannelBeanList = TypeChannels.getTypeListSkuCarts(feed.getChannelId(), "D", "en"); // 取得展示用数据
            if (typeChannelBeanList == null) {
                return;
            }

            // 根据code, 到group表中去查找所有的group信息
            List<CmsBtProductGroupModel> existGroups = getGroupsByCode(feed.getChannelId(), feed.getCode());

            // 循环一下
            for (TypeChannelBean shop : typeChannelBeanList) {
                // 检查一下这个platform是否已经存在, 如果已经存在, 那么就不需要增加了
                boolean blnFound = false;
                for (CmsBtProductGroupModel group : existGroups) {
                    if (group.getCartId() == Integer.parseInt(shop.getValue())) {
                        blnFound = true;
                    }
                }
                if (blnFound) {
                    continue;
                }

                // group对象
                CmsBtProductGroupModel group = getGroupIdByFeedModel(feed.getChannelId(), feed.getModel(), shop.getValue());

                // 看看同一个model里是否已经有数据在cms里存在的
                //   如果已经有存在的话: 直接用那个group
                //   如果没有的话: 新建一个
                if (group == null) {

                    group = new CmsBtProductGroupModel();

                    // 渠道id
                    group.setChannelId(feed.getChannelId());

                    // cart id
                    group.setCartId(Integer.parseInt(shop.getValue()));

                    // 获取唯一编号
                    group.setGroupId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_GROUP_ID));

                    // 主商品Code
                    group.setMainProductCode(feed.getCode());

                    // display order
                    group.setDisplayOrder(0); // TODO: 不重要且有影响效率的可能, 有空再设置

                    // platform status:发布状态: 未上新 // Synship.com_mt_type : id = 45
                    group.setPlatformStatus(CmsConstants.PlatformStatus.WaitingPublish);

                    CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(feed.getChannelId()
                            , CmsConstants.ChannelConfig.PLATFORM_ACTIVE
                            , String.valueOf(group.getCartId()));
                    if (cmsChannelConfigBean != null && !StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
                        if (CmsConstants.PlatformActive.ToOnSale.name().equals(cmsChannelConfigBean.getConfigValue1())) {
                            group.setPlatformActive(CmsConstants.PlatformActive.ToOnSale);
                        } else {
                            // platform active:上新的动作: 暂时默认是放到:仓库中
                            group.setPlatformActive(CmsConstants.PlatformActive.ToInStock);
                        }
                    } else {
                        // platform active:上新的动作: 暂时默认是放到:仓库中
                        group.setPlatformActive(CmsConstants.PlatformActive.ToInStock);
                    }

                    // ProductCodes
                    List<String> codes = new ArrayList<>();
                    codes.add(feed.getCode());
                    group.setProductCodes(codes);
                    group.setCreater(getTaskName());
                    group.setModifier(getTaskName());
                    cmsBtProductGroupDao.insert(group);
                } else {
                    // ProductCodes
                    List<String> oldCodes = group.getProductCodes();
                    if (oldCodes == null || oldCodes.isEmpty()) {
                        List<String> codes = new ArrayList<>();
                        codes.add(feed.getCode());
                        group.setProductCodes(codes);
                    } else {
                        oldCodes.add(feed.getCode());
                        group.setProductCodes(oldCodes);
                    }
                    group.setModifier(getTaskName());

                    cmsBtProductGroupDao.update(group);
                }


            }
        }

        /**
         * 根据model, 到product表中去查找, 看看这家店里, 是否有相同的model已经存在
         * 如果已经存在, 返回 找到了的那个group id
         * 如果不存在, 返回 -1
         *
         * @param channelId channel id
         * @param modelCode 品牌方给的model
         * @param cartId    cart id
         * @return group对象
         */
        // private long getGroupIdByFeedModel(String channelId, String modelCode, String cartId) {
        private CmsBtProductGroupModel getGroupIdByFeedModel(String channelId, String modelCode, String cartId) {
            // 先去看看是否有存在的了
//            CmsBtProductGroupModel groupObj = productGroupService.selectProductGroupByModelCodeAndCartId(channelId, modelCode, cartId);
//            if (groupObj == null) {
//                return -1;
//            }
//            return groupObj.getGroupId();

            return productGroupService.selectProductGroupByModelCodeAndCartId(channelId, modelCode, cartId);
        }

        /**
         * 根据code, 到group表中去查找所有的group信息
         *
         * @param channelId 渠道id
         * @param code      品牌方给的Code
         * @return group列表
         */
        private List<CmsBtProductGroupModel> getGroupsByCode(String channelId, String code) {
            // 先去看看是否有存在的了
            JomgoQuery queryObject = new JomgoQuery();
            queryObject.setQuery("{\"productCodes\":\"" + code + "\"}");
            return productGroupService.getList(channelId, queryObject);
        }

        // jeff 2016/04 change end

        // jeff 2016/04 add start

        /**
         * getPropSimpleValueByMapping 简单属性值的取得
         *
         * @param propType 属性的类型
         * @param propName 属性名称
         * @param mapping  数据到主数据映射关系定义
         * @return 属性值
         */
        private String getPropSimpleValueByMapping(MappingPropType propType, String propName, CmsBtFeedMappingModel mapping) {

            String returnValue = "";
            if (mapping.getProps() != null) {
                for (Prop prop : mapping.getProps()) {
                    if (propType.equals(prop.getType()) && propName.equals(prop.getProp())) {
                        List<Mapping> mappingList = prop.getMappings();
                        if (mappingList != null && mappingList.size() > 0) {
                            returnValue = mappingList.get(0).getVal();
                        }
                    }
                }
            }
            return returnValue;
        }

        /**
         * calculatePriceByFormula 根据公式计算价格
         *
         * @param feedSkuInfo Feed的SKU信息
         * @param taxRate     税率
         * @param formula     计算公式
         * @return 计算后价格
         */
        private Double calculatePriceByFormula(CmsBtFeedInfoModel_Sku feedSkuInfo, Double taxRate, String formula) {

            Double priceClientMsrp = feedSkuInfo.getPriceClientMsrp();
            Double priceClientRetail = feedSkuInfo.getPriceClientRetail();
            Double priceNet = feedSkuInfo.getPriceNet();
            Double priceMsrp = feedSkuInfo.getPriceMsrp();
            Double priceCurrent = feedSkuInfo.getPriceCurrent();

            if (StringUtils.isEmpty(formula)) {
                return 0.0;
            }

            if (formula.indexOf("[taxRate]") != -1 && taxRate == null) {
                return 0.0;
            }
            // 根据公式计算价格
            try {
                ExpressionParser parser = new SpelExpressionParser();
                formula = formula.replaceAll("\\[priceClientMsrp\\]", String.valueOf(priceClientMsrp))
                        .replaceAll("\\[priceClientRetail\\]", String.valueOf(priceClientRetail))
                        .replaceAll("\\[priceNet\\]", String.valueOf(priceNet))
                        .replaceAll("\\[priceMsrp\\]", String.valueOf(priceMsrp))
                        .replaceAll("\\[priceCurrent\\]", String.valueOf(priceCurrent))
                        .replaceAll("\\[taxRate\\]", String.valueOf(taxRate));
                double valueDouble = parser.parseExpression(formula).getValue(Double.class);
                // 四舍五入取整
                BigDecimal valueBigDecimal = new BigDecimal(String.valueOf(valueDouble)).setScale(0, BigDecimal.ROUND_HALF_UP);
                return valueBigDecimal.doubleValue();

            } catch (Exception ex) {
                $error(ex);
                throw new RuntimeException("Formula Calculate Fail!", ex);
            }
        }

        /**
         * doUpdateImage 更新图片
         *
         * @param channelId   渠道id
         * @param code        品牌方给的Code
         * @param originalUrl 原始URL
         * @return 图片名
         */
        private String doUpdateImage(String channelId, String code, String originalUrl) {

            // 检查是否存在该Image
            CmsBtImagesModel param = new CmsBtImagesModel();
            param.setChannelId(channelId);
            param.setCode(code);
            param.setOriginalUrl(originalUrl);
            List<CmsBtImagesModel> findImage = cmsBtImageDaoExt.selectImages(param);

            // 不存在则插入
            if (findImage.size() == 0) {
                // 图片名最后一部分的值（索引）
                int index = 1;

                // 检查该code是否存在该Image（为了取得图片名最后一部分中的索引的最大值）
                param = new CmsBtImagesModel();
                param.setChannelId(channelId);
                param.setCode(code);
                List<CmsBtImagesModel> oldImages = cmsBtImageDaoExt.selectImages(param);
                if (oldImages.size() > 0) {
                    // 取得图片名最后一部分中的索引的最大值 + 1
                    try {
                        index = oldImages.stream().map((imagesModel) -> Integer.parseInt(imagesModel.getImgName().substring(imagesModel.getImgName().lastIndexOf("-") + 1, imagesModel.getImgName().length()))).max(Integer::compare).get() + 1;
                    } catch (Exception ex) {
                        $error(ex);
                        throw new RuntimeException("ImageName Parse Fail!", ex);
                    }
                }

                CmsBtImagesModel newModel = new CmsBtImagesModel(channelId, code, originalUrl, index++, getTaskName());
                cmsBtImageDaoExt.insertImages(newModel);

                return newModel.getImgName();
            } else {
                return findImage.get(0).getImgName();
            }
        }

        /**
         * getProductPriceBeanBefore 生成更新前的价格履历Bean
         *
         * @param cmsProduct      商品Model
         * @param blnProductExist true：更新；false：新建
         * @return 更新前的价格履历Bean
         */
        private ProductPriceBean getProductPriceBeanBefore(CmsBtProductModel cmsProduct, boolean blnProductExist) {

            ProductPriceBean productPriceBeanBefore = new ProductPriceBean();
            for (CmsBtProductModel_Sku sku : cmsProduct.getSkus()) {
                ProductSkuPriceBean productSkuPriceBean = new ProductSkuPriceBean();
                if (!blnProductExist) {
                    productSkuPriceBean.setClientMsrpPrice(null);
                    productSkuPriceBean.setClientRetailPrice(null);
                    productSkuPriceBean.setClientNetPrice(null);
                    productSkuPriceBean.setPriceMsrp(null);
                    productSkuPriceBean.setPriceRetail(null);
                    productSkuPriceBean.setPriceSale(null);
                } else {
                    productSkuPriceBean.setClientMsrpPrice(sku.getClientMsrpPrice());
                    productSkuPriceBean.setClientRetailPrice(sku.getClientRetailPrice());
                    productSkuPriceBean.setClientNetPrice(sku.getClientNetPrice());
                    productSkuPriceBean.setPriceMsrp(sku.getPriceMsrp());
                    productSkuPriceBean.setPriceRetail(sku.getPriceRetail());
                    productSkuPriceBean.setPriceSale(sku.getPriceSale());
                }
                productSkuPriceBean.setSkuCode(sku.getSkuCode());
                productPriceBeanBefore.addSkuPrice(productSkuPriceBean);

            }

            productPriceBeanBefore.setProductCode(cmsProduct.getFields().getCode());
            productPriceBeanBefore.setProductId(cmsProduct.getProdId());

            return productPriceBeanBefore;
        }

        /**
         * getProductCarts 生成ProductCarts信息
         *
         * @param feed 品牌方提供的数据
         * @return ProductCarts信息
         */
        private List<CmsBtProductModel_Carts> getProductCarts(CmsBtFeedInfoModel feed) {
            // 获取当前channel, 有多少个platform
            List<TypeChannelBean> typeChannelBeanList = TypeChannels.getTypeListSkuCarts(feed.getChannelId(), "A", "en"); // 取得展示用数据
            if (typeChannelBeanList == null) {
                return null;
            }

            List<CmsBtProductModel_Carts> carts = new ArrayList<>();

            // 循环一下
            for (TypeChannelBean shop : typeChannelBeanList) {
                CmsBtProductModel_Carts cart = new CmsBtProductModel_Carts();
                cart.setCartId(Integer.parseInt(shop.getValue()));
                cart.setPlatformStatus(CmsConstants.PlatformStatus.WaitingPublish);
                carts.add(cart);
            }

            return carts;
        }

        /**
         * getIsMasterMain 是否是Main商品
         *
         * @param feed 品牌方提供的数据
         * @return 1:isMasterMain;0:isNotMasterMain
         */
        private int getIsMasterMain(CmsBtFeedInfoModel feed) {
            long cnt = productService.getCnt(feed.getChannelId(),
                    String.format("{'feed.orgAtts.modelCode':'%s'}", feed.getModel()));
            if (cnt < 1) {
                return 1;
            }
            return 0;
        }

        /**
         * 将新建的件数，更新的件数插到cms_bt_data_amount表
         */
        private void insertDataAmount() {

            // 新建的件数
            if (insertCnt > 0) {
                CmsBtDataAmountModel insertModel = new CmsBtDataAmountModel();
                insertModel.setChannelId(channel.getOrder_channel_id());
                insertModel.setAmountName("FEED_TO_MASTER_INSERT");
                insertModel.setAmountVal(String.valueOf(insertCnt));
                insertModel.setComment("Feed导入Master新建");
                insertModel.setCreater(getTaskName());
                cmsBtDataAmountDao.insert(insertModel);
            }

            // 新建的件数
            if (updateCnt > 0) {
                CmsBtDataAmountModel updateModel = new CmsBtDataAmountModel();
                updateModel.setChannelId(channel.getOrder_channel_id());
                updateModel.setAmountName("FEED_TO_MASTER_UPDATE");
                updateModel.setAmountVal(String.valueOf(updateCnt));
                updateModel.setComment("Feed导入Master更新");
                updateModel.setCreater(getTaskName());
                cmsBtDataAmountDao.insert(updateModel);
            }
        }
        // jeff 2016/04 add end

        private int m_mulitComplex_index = 0; // 暂时只支持一层multiComplex, 如果需要多层, 就需要改成list, 先进后出
        private boolean m_mulitComplex_run = false; // 暂时只支持一层multiComplex, 如果需要多层, 就需要改成list, 先进后出

        /**
         * getPropValueByMapping 属性匹配(递归)
         *
         * @param prop        mapping表里的一个属性
         * @param feed        feed表的信息
         * @param field       当前匹配好的属性(需要返回)
         * @param schemaModel 主类目的schema信息
         * @return 匹配好的属性
         */
        private Object getPropValueByMapping(
                String propPath,
                Prop prop,
                CmsBtFeedInfoModel feed,
                CmsBtProductModel_Field field,
                CmsMtCategorySchemaModel schemaModel) {

            String strPathSplit = ">";

            Map<String, Object> complexChildren = new HashMap<>();
            List<Map<String, Object>> multiComplexChildren = new LinkedList<>();

            // 分割propPath
            String[] propPathSplit = propPath.split(strPathSplit);
            // 获取当前需要处理的这个属性,在主类目属性中的信息
            Field fieldCurrent = null;
            List<Field> schemaFieldList = schemaModel.getFields();
            for (String value : propPathSplit) {

                for (Field fieldOne : schemaFieldList) {
                    if (value.equals(fieldOne.getId())) {

                        // 看看类型
                        if (FieldTypeEnum.COMPLEX.equals(fieldOne.getType())) {
                            // 切换循环属性, 继续循环
                            schemaFieldList = ((ComplexField) fieldOne).getFields();
                        } else if (FieldTypeEnum.MULTICOMPLEX.equals(fieldOne.getType())) {
                            // 切换循环属性, 继续循环
                            schemaFieldList = ((MultiComplexField) fieldOne).getFields();
                        }

                        fieldCurrent = fieldOne;

                        break;
                    }

                }
            }

            // 看看是否有子属性
            if (prop.getChildren() != null && prop.getChildren().size() > 0) {

                // 处理子属性
                if (FieldTypeEnum.COMPLEX.equals(fieldCurrent.getType())) {
                    for (Prop p : prop.getChildren()) {
                        // jeff 2016/04 change start
                        // complexChildren.put(p.getProp(), getPropValueByMapping(propPath + strPathSplit + p.getProp(), p, feed, field, schemaModel));
                        Object propValue = getPropValueByMapping(propPath + strPathSplit + p.getProp(), p, feed, field, schemaModel);
                        if (propValue != null) {
                            complexChildren.put(p.getProp(), propValue);
                        }
                        // jeff 2016/04 change end
                    }

                    // 处理完所有的子属性之后就可以返回了
                    return complexChildren;
                } else if (FieldTypeEnum.MULTICOMPLEX.equals(fieldCurrent.getType())) {

                    m_mulitComplex_run = true;
                    m_mulitComplex_index = 0;

                    while (m_mulitComplex_run) {
                        complexChildren = new HashMap<>();
                        for (Prop p : prop.getChildren()) {
                            // jeff 2016/04 change start
                            // complexChildren.put(p.getProp(), getPropValueByMapping(propPath + strPathSplit + p.getProp(), p, feed, field, schemaModel));
                            Object propValue = getPropValueByMapping(propPath + strPathSplit + p.getProp(), p, feed, field, schemaModel);
                            if (propValue != null) {
                                complexChildren.put(p.getProp(), propValue);
                            }
                            // jeff 2016/04 change end
                        }
                        multiComplexChildren.add(complexChildren);
                        m_mulitComplex_index++;
                    }

                    // 处理完所有的子属性之后就可以返回了
                    return multiComplexChildren;
                }

            }

            // 检查条件是否满足
            for (Mapping mappingCondition : prop.getMappings()) {
                boolean blnMeetRequirements = true;

                // 遍历条件(需要符合所有条件才算通过)
                if (mappingCondition != null && mappingCondition.getCondition() != null) {
                    for (Condition c : mappingCondition.getCondition()) {
                        if (Operation.IS_NULL.equals(c.getOperation())) {
                            if (feed.getAttribute().get(c.getProperty()) != null
                                    && feed.getAttribute().get(c.getProperty()).size() > 0
                                    ) {
                                boolean blnError = false;
                                for (String s : feed.getAttribute().get(c.getProperty())) {
                                    if (!StringUtils.isEmpty(s)) {
                                        blnError = true;
                                    }
                                }
                                if (blnError) {
                                    // 不符合条件
                                    blnMeetRequirements = false;
                                    break;
                                }
                            }
                        } else if (Operation.IS_NOT_NULL.equals(c.getOperation())) {
                            if (feed.getAttribute().get(c.getProperty()) == null
                                    || feed.getAttribute().get(c.getProperty()).size() == 0
                                    ) {
                                // 不符合条件
                                blnMeetRequirements = false;
                                break;
                            } else {

                                boolean blnError = true;
                                for (String s : feed.getAttribute().get(c.getProperty())) {
                                    if (!StringUtils.isEmpty(s)) {
                                        blnError = false;
                                    }
                                }
                                if (blnError) {
                                    // 不符合条件
                                    blnMeetRequirements = false;
                                    break;
                                }
                            }
                        } else if (Operation.EQUALS.equals(c.getOperation())) {
                            List<String> feedProp = feed.getAttribute().get(c.getProperty());
                            if (feedProp == null || feedProp.size() == 0) {
                                // 不符合条件
                                blnMeetRequirements = false;
                                break;
                            }

                            if (!c.getValue().equals(feedProp.get(0))) {
                                // 不符合条件
                                blnMeetRequirements = false;
                                break;
                            }

                        } else if (Operation.NOT_EQUALS.equals(c.getOperation())) {
                            List<String> feedProp = feed.getAttribute().get(c.getProperty());
                            if (feedProp == null || feedProp.size() == 0) {
                                // 不符合条件
                                blnMeetRequirements = false;
                                break;
                            }

                            if (c.getValue().equals(feedProp.get(0))) {
                                // 不符合条件
                                blnMeetRequirements = false;
                                break;
                            }

                        }
                    }
                }

                // 符合条件的场合
                if (blnMeetRequirements) {
                    // 设置值
                    Object attributeValue = null;

                    if (SrcType.text.equals(mappingCondition.getType())) {
                        // 如果是多选框的话,那么就要生成一个数组
                        if (FieldTypeEnum.MULTICHECK.equals(fieldCurrent.getType())) {
                            List<String> lst = new ArrayList<>();
                            lst.add(mappingCondition.getVal());
                            attributeValue = lst;
                        } else {
                            attributeValue = mappingCondition.getVal();
                        }
                    } else if (SrcType.propFeed.equals(mappingCondition.getType())) {

                        if (feed.getAttribute().containsKey(mappingCondition.getVal())) {
                            // 先看看attribute里有没有
                            attributeValue = feed.getAttribute().get(mappingCondition.getVal());
                        } else if (feed.getFullAttribute().containsKey(mappingCondition.getVal())) {
                            // 看看外面有没有
                            attributeValue = feed.getFullAttribute().get(mappingCondition.getVal());
                        } else {
                            // 记下log, 无视当前属性
//                            logIssue(getTaskName(), String.format("[CMS2.0][测试] 找不到feed的这个属性 ( channel: [%s], code: [%s], attr: [%s] )", feed.getChannelId(), feed.getCode(), mappingCondition.getVal()));
                            $info(String.format("[CMS2.0][测试] 找不到feed的这个属性 ( channel: [%s], code: [%s], attr: [%s] )", feed.getChannelId(), feed.getCode(), mappingCondition.getVal()));
                        }

                        if (m_mulitComplex_run) {
                            if (attributeValue.getClass().equals(ArrayList.class)) {
                                Object attTemp = ((List) attributeValue).get(m_mulitComplex_index);

                                if (m_mulitComplex_index == (((List) attributeValue).size() - 1)) {
                                    m_mulitComplex_run = false;
                                }
                                attributeValue = attTemp;
                            }
                        }

                    }

                    return attributeValue;

                }
            }

            return null;

        }

        /**
         * doSetPrice 设置product的价格
         *
         * @param channelId  channel id
         * @param feed       feed信息
         * @param cmsProduct cms product信息
         * @param mapping    数据到主数据映射关系定义
         * @return Product的价格Bean
         */
        // jeff 2016/04 change start
        // private void doSetPrice(String channelId, CmsBtFeedInfoModel feed, CmsBtProductModel cmsProduct) {
        private ProductPriceBean doSetPrice(String channelId, CmsBtFeedInfoModel feed, CmsBtProductModel cmsProduct, CmsBtFeedMappingModel mapping) {
            // jeff 2016/04 change end
            // 查看配置表, 看看是否要自动审批价格
            CmsChannelConfigBean autoApprovePrice = CmsChannelConfigs.getConfigBeanNoCode(channelId
                    , CmsConstants.ChannelConfig.AUTO_APPROVE_PRICE);
            boolean blnAutoApproveFlg;
            if (autoApprovePrice == null || autoApprovePrice.getConfigValue1() == null || "0".equals(autoApprovePrice.getConfigValue1())) {
                // 没有配置过, 或者 配置为空, 或者 配置了0 的场合
                // 认为不自动审批价格 (注意: 即使不自动审批, 但如果价格击穿了, 仍然自动更新salePrice)
                blnAutoApproveFlg = false;
            } else {
                // 其他的场合, 自动审批价格
                blnAutoApproveFlg = true;
            }

            ProductPriceBean model = new ProductPriceBean();
            ProductSkuPriceBean skuPriceModel;

            model.setProductId(cmsProduct.getProdId());

            // jeff 2016/04 add start
            model.setProductCode(cmsProduct.getFields().getCode());

            // 税号个人
            String hsCodePrivate = cmsProduct.getFields().getHsCodePrivate();
            // 税号个人TypeChannelBean
            TypeChannelBean typeChannelBean = null;
            if (!StringUtils.isEmpty(hsCodePrivate)) {
                typeChannelBean = TypeChannels.getTypeChannelByCode(Constants.productForOtherSystemInfo.HS_CODE_PRIVATE, channelId, hsCodePrivate, "en");
            }
            // 税率
            Double taxRate = null;
            if (typeChannelBean != null && !StringUtils.isEmpty(typeChannelBean.getAdd_name1())) {
                taxRate = Double.parseDouble(typeChannelBean.getAdd_name1());
            }

            // 店铺级别MSRP价格计算公式
            String priceMsrpCalcFormula = "";
            CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.PRICE_MSRP_CALC_FORMULA);
            if (cmsChannelConfigBean != null && !StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
                priceMsrpCalcFormula = cmsChannelConfigBean.getConfigValue1();
            }

            // 店铺级别指导价格计算公式
            String priceRetailCalcFormula = "";
            cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.PRICE_RETAIL_CALC_FORMULA);
            if (cmsChannelConfigBean != null && !StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
                priceRetailCalcFormula = cmsChannelConfigBean.getConfigValue1();
            }

            // MSRP计算公式
            String priceMsrpFormula = getPropSimpleValueByMapping(MappingPropType.SKU, "priceMsrp", mapping);
            // 指导价计算公式
            String priceRetailFormula = getPropSimpleValueByMapping(MappingPropType.SKU, "priceRetail", mapping);
            // 公式未配置的场合，使用店铺级别的配置公式
            if (StringUtils.isEmpty(priceMsrpFormula)) {
                priceMsrpFormula = priceMsrpCalcFormula;
            }
            if (StringUtils.isEmpty(priceRetailFormula)) {
                priceRetailFormula = priceRetailCalcFormula;
            }

            // 价格自动同步间隔天数
            String day = "0";
            cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId
                    , CmsConstants.ChannelConfig.AUTO_SYN_DAY);
            if (cmsChannelConfigBean != null && !StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
                // 如果没有设定则相当于间隔天数为0
                day = cmsChannelConfigBean.getConfigValue1();
            }

            // 强制击穿阈值
            String threshold = "";
            cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId
                    , CmsConstants.ChannelConfig.MANDATORY_BREAK_THRESHOLD);
            if (cmsChannelConfigBean != null && !StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
                threshold = cmsChannelConfigBean.getConfigValue1();
            }
            // 如果强制击穿阈值没有设定的话，那么只要指导价高于原来最终售价就击穿
            if (StringUtils.isEmpty(threshold)) {
                threshold = "0";
            }
            // 是否同步
            boolean synFlg = true;
            try {
                synFlg = DateTimeUtil.addDays(DateTimeUtil.parse(this.priceBreakTime), Integer.parseInt(day)).before(DateTimeUtil.getDate());
            } catch (Exception ex) {
            }
            // jeff 2016/04 add end

            for (CmsBtFeedInfoModel_Sku sku : feed.getSkus()) {
                skuPriceModel = new ProductSkuPriceBean();

                skuPriceModel.setSkuCode(sku.getSku());
                // jeff 2016/04 change start
                // skuPriceModel.setPriceMsrp(sku.getPrice_msrp());
                // skuPriceModel.setPriceRetail(sku.getPrice_current());
                skuPriceModel.setPriceMsrp(calculatePriceByFormula(sku, taxRate, priceMsrpFormula));
                skuPriceModel.setPriceRetail(calculatePriceByFormula(sku, taxRate, priceRetailFormula));
                // jeff 2016/04 change end

                skuPriceModel.setClientMsrpPrice(sku.getPriceClientMsrp());
                skuPriceModel.setClientRetailPrice(sku.getPriceClientRetail());
                skuPriceModel.setClientNetPrice(sku.getPriceNet());

                // jeff 2016/04 change start
                // 如果是新的SKU, 那么: PriceRetail -> priceSale
                if (cmsProduct.getSku(sku.getSku()) == null) {
                    skuPriceModel.setPriceSale(skuPriceModel.getPriceRetail());
                    skuPriceModel.setPriceChgFlg("");
                } else if (cmsProduct.getSku(sku.getSku()).getPriceSale() == null || cmsProduct.getSku(sku.getSku()).getPriceSale() == 0d) {
                    skuPriceModel.setPriceSale(skuPriceModel.getPriceRetail());
                    skuPriceModel.setPriceChgFlg("");
                } else {
                    // 之前有价格的场合, 判断是否需要把价格更新掉

                    // 旧sku
                    CmsBtProductModel_Sku oldSku = cmsProduct.getSku(sku.getSku());

                    // 旧价格取得
                    Double oldPriceSale = oldSku.getPriceSale();

                    // 新的的指导价
                    Double newPriceSale = skuPriceModel.getPriceRetail();

                    // 是否自动同步最终售价
                    if (blnAutoApproveFlg) {
                        if (newPriceSale > oldPriceSale) {
                            skuPriceModel.setPriceChgFlg("U" + (newPriceSale - oldPriceSale));
                            skuPriceModel.setPriceSale(newPriceSale);
                        } else if (newPriceSale < oldPriceSale) {
                            skuPriceModel.setPriceChgFlg("D" + Math.abs(newPriceSale - oldPriceSale));
                            skuPriceModel.setPriceSale(newPriceSale);
                        }
                    } else {

                        // 同步的场合
                        if (synFlg && StringUtils.isDigit(threshold)) {
                            // 指导价高于原来最终售价的阈值(例：10%)时，强制击穿
                            if (newPriceSale > oldPriceSale * (1.0 + Double.parseDouble(threshold) / 100.0)) {
                                skuPriceModel.setPriceChgFlg("X" + (newPriceSale - oldPriceSale));
                                skuPriceModel.setPriceSale(newPriceSale);
                            } else {
                                // 为了之后计算PriceSaleSt和PriceSaleEd，也需要赋上旧值
                                skuPriceModel.setPriceSale(oldSku.getPriceSale());
                            }
                        } else {
                            // 为了之后计算PriceSaleSt和PriceSaleEd，也需要赋上旧值
                            skuPriceModel.setPriceSale(oldSku.getPriceSale());
                        }
                    }

//                    if (sku.getPrice_current().compareTo(oldPriceSale) > 0) {
//                        // 新current price > 旧sale price的场合, 击穿 ("X" + 新current price - 旧sale price)
//                        // 更新标志位
//                        skuPriceModel.setPriceChgFlg("X" + (sku.getPrice_current() - oldPriceSale));
//
//                        // 更新price sale
//                        // TODO: tom: 据说之后会有新方案, 暂时先按照这个flg来判断
//                        if (blnAutoApproveFlg) {
//                            skuPriceModel.setPriceSale(sku.getPrice_current());
//                        }
//
//                    } else if (sku.getPrice_current().compareTo(oldPriceSale) < 0) {
//                        // 新current price < 现sale price的场合, ("U"或"D" + ABS[ 新current - 旧sale price ])
//                        // 更新标志位
//                        skuPriceModel.setPriceChgFlg("D" + Math.abs(sku.getPrice_current() - oldPriceSale));
//
//                        // 更新price sale (设置了自动approve价格的场合)
//                        if (blnAutoApproveFlg) {
//                            skuPriceModel.setPriceSale(sku.getPrice_current());
//                        }
//                    }

                }
                // jeff 2016/04 change end

                model.addSkuPrice(skuPriceModel);
            }

            List<ProductPriceBean> productPrices = new ArrayList<>();
            productPrices.add(model);

            productSkuService.updatePrices(channelId, productPrices, getTaskName());
            // jeff 2016/04 add start
            // 如果发生价格强制击穿的话，更新价格强制击穿时间
            if (synFlg) {
                TaskControlBean param = new TaskControlBean();
                param.setTask_id(getTaskName());
                param.setCfg_name(TaskControlEnums.Name.order_channel_id.toString());
                param.setCfg_val1(channelId);

                // 价格强制击穿时间
                if (StringUtils.isEmpty(this.priceBreakTime)) {
                    param.setEnd_time(DateTimeUtil.getNow());
                } else {
                    param.setEnd_time(DateTimeUtil.format(DateTimeUtil.addDays(DateTimeUtil.parse(this.priceBreakTime), Integer.parseInt(day)), null));
                }
                taskDao.updateTaskControl(param);
            }

            return model;
            // jeff 2016/04 add end
        }

        /**
         * doSaveItemDetails 保存item details的数据
         *
         * @param channelId channel id
         * @param productId product id
         * @param feed      feed信息
         */
        private void doSaveItemDetails(String channelId, Long productId, CmsBtFeedInfoModel feed) {

            // 如果feed里,没有sku的数据的话,那么就不需要做下去了
            if (feed.getSkus() == null || feed.getSkus().size() == 0) {
                return;
            }

            // 根据product id, 获取现有的item details表的数据
            List<String> skuList = new ArrayList<>();
            List<ItemDetailsBean> itemDetailsBeanList = itemDetailsDao.selectByCode(channelId, feed.getCode());
            for (ItemDetailsBean itemDetailsBean : itemDetailsBeanList) {
                skuList.add(itemDetailsBean.getSku());
            }

            // 遍历feed的sku信息
            for (CmsBtFeedInfoModel_Sku feedSku : feed.getSkus()) {
                // 数据准备
                ItemDetailsBean itemDetailsBean = new ItemDetailsBean();
                itemDetailsBean.setOrder_channel_id(channelId);
                itemDetailsBean.setSku(feedSku.getSku());
                itemDetailsBean.setProduct_id(productId);
                itemDetailsBean.setItemcode(feed.getCode());
                itemDetailsBean.setSize(feedSku.getSize());
                itemDetailsBean.setBarcode(feedSku.getBarcode());
                itemDetailsBean.setIs_sale("1");
                itemDetailsBean.setClient_sku(feedSku.getClientSku());
                itemDetailsBean.setActive(1);

                // 判断这个sku是否已经存在
                if (skuList.contains(feedSku.getSku())) {
                    // 已经存在的场合: 更新数据库
                    itemDetailsDao.updateItemDetails(itemDetailsBean, getTaskName());
                } else {
                    // 不存在的场合: 插入数据库
                    itemDetailsDao.insertItemDetails(itemDetailsBean, getTaskName());

                    // 添加到判断列表中
                    skuList.add(feedSku.getSku());
                }

            }


        }

        /**
         * 进行一些字符串或数字的特殊编辑
         *
         * @param inputValue 输入的字符串
         * @param edit       目前支持的是 "in2cm" 英寸转厘米
         * @param prefix     前缀
         * @param suffix     后缀
         * @return
         */
        private String doEditSkuTemplate(String inputValue, String edit, String prefix, String suffix) {
            String value = inputValue;

            // 根据edit进行变换
            if (!StringUtils.isEmpty(edit)) {
                if ("in2cm".equals(edit)) {
                    // 奇怪的数据转换
                    // 有时候别人提供的数字中会有类似于这样的数据：
                    // 33 3/4 意思是 33又四分之三 -> 33.75
                    // 33 1/2 意思是 33又二分之一 -> 33.5
                    // 33 1/4 意思是 33又四分之一 -> 33.25
                    // 33 5/8 意思是 33又八分之五 -> 33.625
                    // 直接这边代码处理掉避免人工干预
                    if (value.contains(" ")) {
                        value = value.replaceAll(" +", " ");
                        String[] strSplit = value.split(" ");

                        String[] strSplitSub = strSplit[1].split("/");
                        value = String.valueOf(
                                Float.valueOf(strSplit[0]) +
                                        (Float.valueOf(strSplitSub[0]) / Float.valueOf(strSplitSub[1]))
                        );

                    }

                    // 英寸转厘米
                    value = String.valueOf(Float.valueOf(value) * 2.54);

                    DecimalFormat df = new DecimalFormat("0.00");
                    value = df.format(Float.valueOf(value));

                }
            }

            // 设置前缀
            if (!StringUtils.isEmpty(prefix)) {
                value = prefix + value;
            }

            // 设置后缀
            if (!StringUtils.isEmpty(suffix)) {
                value = value + suffix;
            }

            return value;

        }


    }

}
