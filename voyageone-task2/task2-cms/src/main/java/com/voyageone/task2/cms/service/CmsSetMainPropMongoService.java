package com.voyageone.task2.cms.service;

import com.google.common.base.Joiner;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.base.exception.CommonConfigNotFoundException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.ComplexField;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.MD5;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.DictWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.service.bean.cms.Condition;
import com.voyageone.service.bean.cms.feed.FeedCustomPropWithValueBean;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedMapping2Dao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.daoext.cms.CmsBtImagesDaoExt;
import com.voyageone.service.impl.cms.*;
import com.voyageone.service.impl.cms.feed.CmsBtFeedImportSizeService;
import com.voyageone.service.impl.cms.feed.FeedCustomPropService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.prices.IllegalPriceConfigException;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.CmsBtPriceLogService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.impl.com.ComMtValueChannelService;
import com.voyageone.service.model.cms.CmsBtBusinessLogModel;
import com.voyageone.service.model.cms.CmsBtFeedImportSizeModel;
import com.voyageone.service.model.cms.CmsBtImagesModel;
import com.voyageone.service.model.cms.enums.MappingPropType;
import com.voyageone.service.model.cms.enums.Operation;
import com.voyageone.service.model.cms.enums.SrcType;
import com.voyageone.service.model.cms.mongo.CmsMtCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeAllModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeAllModel_Platform;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedMappingModel;
import com.voyageone.service.model.cms.mongo.feed.mapping.Mapping;
import com.voyageone.service.model.cms.mongo.feed.mapping.Prop;
import com.voyageone.service.model.cms.mongo.feed.mapping2.CmsBtFeedMapping2Model;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.bean.ItemDetailsBean;
import com.voyageone.task2.cms.dao.ItemDetailsDao;
import com.voyageone.task2.cms.dao.TmpOldCmsDataDao;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
import com.voyageone.task2.cms.service.putaway.ConditionPropValueRepo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * feed->master导入服务
 *
 * @author tom on 2016/2/18.
 * @version 2.4.0
 * @since 2.0.0
 */
@Service
public class CmsSetMainPropMongoService extends BaseTaskService {

    @Autowired
    private CmsBtFeedMapping2Dao cmsBtFeedMapping2Dao; // DAO: 新的feed->主数据的mapping关系
    @Autowired
    private CmsBtProductDao cmsBtProductDao; // DAO: 商品的值
    @Autowired
    private MongoSequenceService commSequenceMongoService; // DAO: Sequence
    @Autowired
    private ComMtValueChannelService comMtValueChannelService;    // 更新Synship.com_mt_value_channel表
    @Autowired
    private ItemDetailsDao itemDetailsDao; // DAO: ItemDetailsDao
    @Autowired
    private TmpOldCmsDataDao tmpOldCmsDataDao; // DAO: 旧数据
    @Autowired
    private FeedCustomPropService customPropService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    private CmsBtImagesDaoExt cmsBtImageDaoExt;
    @Autowired
    private FeedInfoService feedInfoService;
    @Autowired
    CategoryTreeService categoryTreeService;
    @Autowired
    CategoryTreeAllService categoryTreeAllService;
    @Autowired
    private BusinessLogService businessLogService;
    @Autowired
    private ImagesService imagesService;
    @Autowired
    private CmsBtPriceLogService cmsBtPriceLogService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private ConditionPropValueRepo conditionPropValueRepo;
    @Autowired
    private CmsBtFeedImportSizeService cmsBtFeedImportSizeService;

    // 每个channel的feed->master导入最大件数
    private final static int FEED_IMPORT_MAX_500 = 500;

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
     *
     * @param taskControlList job 配置
     * @throws Exception
     */
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 初始化cms_mt_channel_condition_config表的条件表达式(避免多线程时2次初始化)
        conditionPropValueRepo.init();

        // 默认线程池最大线程数
        int threadPoolCnt = 5;
        // 保存每个channel最终导入结果(成功失败件数信息)
        Map<String, String> resultMap = new HashMap<>();
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolCnt);
        // 根据渠道运行
        for (final String orderChannelID : orderChannelIdList) {
            // 获取是否跳过mapping check
            String skip_mapping_check = TaskControlUtils.getVal2(taskControlList, TaskControlEnums.Name.order_channel_id, orderChannelID);
            boolean bln_skip_mapping_check = true;
            if (StringUtils.isEmpty(skip_mapping_check) || "0".equals(skip_mapping_check)) {
                bln_skip_mapping_check = false;
            }
            // 获取前一次的价格强制击穿时间
            String priceBreakTime = TaskControlUtils.getEndTime(taskControlList, TaskControlEnums.Name.order_channel_id, orderChannelID);
            // 主逻辑
            setMainProp mainProp = new setMainProp(orderChannelID, bln_skip_mapping_check, priceBreakTime);
            // 启动多线程
            executor.execute(() -> mainProp.doRun(resultMap));
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

        $info("=================feed->master导入  最终结果=====================");
        resultMap.entrySet().stream()
                            .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                            .forEach(p ->  $info(p.getValue()));
        $info("=================feed->master导入  主线程结束====================");

        // 线程
//        List<Runnable> threads = new ArrayList<>();

//        // 根据渠道运行
//        for (final String orderChannelID : orderChannelIdList) {
//
//            threads.add(new Runnable() {
//                @Override
//                public void run() {
//                    // 获取是否跳过mapping check
//                    String skip_mapping_check = TaskControlUtils.getVal2(taskControlList, TaskControlEnums.Name.order_channel_id, orderChannelID);
//                    boolean bln_skip_mapping_check = true;
//                    if (StringUtils.isEmpty(skip_mapping_check) || "0".equals(skip_mapping_check)) {
//                        bln_skip_mapping_check = false;
//                    }
//                    // jeff 2016/04 change start
//                    // 获取前一次的价格强制击穿时间
//                    String priceBreakTime = TaskControlUtils.getEndTime(taskControlList, TaskControlEnums.Name.order_channel_id, orderChannelID);
//                    // 主逻辑
//                    // new setMainProp(orderChannelID, bln_skip_mapping_check).doRun();
//                    new setMainProp(orderChannelID, bln_skip_mapping_check, priceBreakTime).doRun();
//                    // jeff 2016/04 change end
//                }
//            });
//        }
//
//        runWithThreadPool(threads, taskControlList);
    }

    /**
     * 按渠道进行设置
     */
    public class setMainProp {
        int insertCnt = 0;
        int updateCnt = 0;
        int errCnt = 0;
        private OrderChannelBean channel;
        private boolean skip_mapping_check;
        // jeff 2016/04 change start
        // 前一次的价格强制击穿时间
        private String priceBreakTime;
        private int m_mulitComplex_index = 0; // 暂时只支持一层multiComplex, 如果需要多层, 就需要改成list, 先进后出
        // jeff 2016/04 change end
        private boolean m_mulitComplex_run = false; // 暂时只支持一层multiComplex, 如果需要多层, 就需要改成list, 先进后出

        // --------------------------------------------------------------------------------------------
        // 品牌mapping表
        Map<String, String> mapBrandMapping = new HashMap<>();
        // 产品分类mapping表
        Map<String, String> mapProductTypeMapping = new HashMap<>();
        // 适用人群mapping表
        Map<String, String> mapSizeTypeMapping = new HashMap<>();
        // --------------------------------------------------------------------------------------------
        // 允许approve这个sku到平台上去售卖渠道cart列表
        List<TypeChannelBean> typeChannelBeanListApprove = null;
        // 允许展示的cart列表
        List<TypeChannelBean> typeChannelBeanListDisplay = null;


        // public setMainProp(String orderChannelId, boolean skip_mapping_check) {
        public setMainProp(String orderChannelId, boolean skip_mapping_check, String priceBreakTime) {
            this.channel = Channels.getChannel(orderChannelId);
            this.skip_mapping_check = skip_mapping_check;
            this.priceBreakTime = priceBreakTime;
        }

        /**
         * 从synship.com_mt_value_channel表中取得品牌，产品分类，使用人群等mapping信息
         */
        public void getTypeChannelMappingInfo() {

            // 品牌mapping作成
            List<TypeChannelBean> brandTypeChannelBeanList = TypeChannels.getTypeList(Constants.comMtTypeChannel.BRAND_41,
                    this.channel.getOrder_channel_id());
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
            List<TypeChannelBean> productTypeChannelBeanList = TypeChannels.getTypeList(Constants.comMtTypeChannel.PROUDCT_TYPE_57,
                    this.channel.getOrder_channel_id());
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
            List<TypeChannelBean> sizeTypeChannelBeanList = TypeChannels.getTypeList(Constants.comMtTypeChannel.PROUDCT_TYPE_58,
                    this.channel.getOrder_channel_id());
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
        }

        /**
         * feed->master导入主处理
         *
         * @param resultMap 用于保存每个channel最终导入结果信息
         */
        public void doRun(Map<String, String> resultMap) {
            $info(channel.getOrder_channel_id() + " " + channel.getFull_name() + " 产品导入主数据开始");

            String channelId = this.channel.getOrder_channel_id();

            // 查找当前渠道,所有等待反映到主数据的商品
//            CmsBtFeedInfoModel feedInfo = feedInfoService.getProductByCode(channelId, "36/G05");
//            List<CmsBtFeedInfoModel> feedList = new ArrayList<>();
//            feedList.add(feedInfo);
            String query = String.format("{ channelId: '%s', updFlg: %s}", channelId, 0);
            // jeff 2016/05 add start
            String sort = "{modified:1}";
            // jeff 2016/05 add end
            JongoQuery queryObject = new JongoQuery();
            queryObject.setQuery(query);
            queryObject.setSort(sort);
            queryObject.setLimit(FEED_IMPORT_MAX_500);   // 默认为每次最大500件
            List<CmsBtFeedInfoModel> feedList = feedInfoService.getList(channelId, queryObject);

            // 共通配置信息存在的时候才进行feed->master导入
            if (ListUtils.notNull(feedList)) {
                // 清除缓存（这样在cms_mt_channel_config表中刚追加的价格计算公式等配置就能立刻生效了）
                CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_CmsChannelConfigs.toString());
                // 清除缓存（这样在synship.com_mt_value_channel表中刚追加的brand，productType，sizeType等初始化mapping信息就能立刻生效了）
                CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_TypeChannel.toString());
                // 从synship.com_mt_value_channel表中取得品牌，产品分类，使用人群等mapping信息
                getTypeChannelMappingInfo();
                // --------------------------------------------------------------------------------------------
                //                // 自定义属性 - 初始化
                //                customPropService.doInit(channelId);
                // --------------------------------------------------------------------------------------------
                //            }

                // jeff 2016/05 add start
                // 取得所有主类目
                // update desmond 2016/07/04 start
                //            List<CmsMtCategoryTreeModel> categoryTreeList = categoryTreeService.getMasterCategory();
                List<CmsMtCategoryTreeAllModel> categoryTreeAllList = categoryTreeAllService.getMasterCategory();
                // update desmond 2016/07/04 end
                // jeff 2016/05 add end

                // 遍历所有数据
                for (CmsBtFeedInfoModel feed : feedList) {
                    // 将商品从feed导入主数据
                    // 注意: 保存单条数据到主数据的时候, 由于要生成group数据, group数据的生成需要检索数据库进行一系列判断
                    //       所以单个渠道的数据, 最好不要使用多线程, 如果以后一定要加多线程的话, 注意要自己写带锁的代码.
                    // update by desmond 2016/07/05 start
                    // 增加try catch捕捉feed导入时出现的异常并新增失败时回写处理等,feed导入共通处理里面出错时改为抛出异常
                    try {
                        feed.setFullAttribute();
                        doSaveProductMainProp(feed, channelId, categoryTreeAllList);
                    } catch (CommonConfigNotFoundException ce) {
                        errCnt++;
                        // 如果是共通配置没有或者价格计算时抛出整个Channel的配置没有的错误时，后面的feed导入就不用做了，免得报出几百条同样的错误
                        String errMsg = "feed->master导入:共通配置异常终止:";
                        if (StringUtils.isNullOrBlank2(ce.getMessage())) {
                            errMsg += "出现不可预知的错误，请跟管理员联系  [ErrMsg=" + ce.getStackTrace()[0].toString() + "]";
                            ce.printStackTrace();
                        } else {
                            errMsg = ce.getMessage();
                        }
                        // 回写详细错误信息表(cms_bt_business_log)
                        insertBusinessLog(feed.getChannelId(), "", feed.getModel(), feed.getCode(), "", errMsg, getTaskName());

                        // 回写feedInfo表(本来准备不回写，但一个feed都不回写的话画面上看不见错误信息)
                        updateFeedInfo(feed, 2, errMsg, "");  // 2:feed->master导入失败
                        // 跳出循环,后面的feed->master导入不做了，等这个channel共通错误改好了之后再做导入
                        break;
                    } catch (Exception e) {
                        errCnt++;
                        String errMsg = "feed->master导入:异常终止:";
                        if (StringUtils.isNullOrBlank2(e.getMessage())) {
                            errMsg += "出现不可预知的错误，请跟管理员联系 [ErrMsg=" + e.getStackTrace()[0].toString() + "]";
                            $error(errMsg);
                            e.printStackTrace();
                        } else {
                            errMsg = e.getMessage();
                        }
                        // 回写详细错误信息表(cms_bt_business_log)
                        insertBusinessLog(feed.getChannelId(), "", feed.getModel(), feed.getCode(), "", errMsg, getTaskName());

                        // 回写feedInfo表
                        updateFeedInfo(feed, 2, errMsg, "");  // 2:feed->master导入失败
                        // 继续循环做下一条feed->master导入
                    }
                    // update by desmond 2016/07/05 end

                }

                // 清除缓存（这样就能再画面上展示出最新的brand，productType，sizeType等初始化mapping信息）
                CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_TypeChannel.toString());
            }

            // jeff 2016/04 add start
            // 将新建的件数，更新的件数插到cms_bt_data_amount表
//            insertDataAmount();         // delete desmond 2016/07/04 以后不用更新这个表了
            // jeff 2016/04 add end
            // add by desmond 2016/07/05 start
            //            $info(channel.getFull_name() + "产品导入结果 [总件数:" + feedList.size()
//                    + " 新增成功:" + insertCnt + " 更新成功:" + updateCnt + " 失败:" + errCnt + "]");
            String resultInfo = channelId + " " + channel.getFull_name() + " 产品导入结果 [总件数:" + feedList.size()
                    + " 新增成功:" + insertCnt + " 更新成功:" + updateCnt + " 失败:" + errCnt + "]";
//            $info(resultInfo);
            // 将该channel的feed->master导入信息加入map，供channel导入线程全部完成一起显示
            resultMap.put(channelId, resultInfo);
            // add by desmond 2016/07/05 end
            $info(channel.getOrder_channel_id() + " " + channel.getFull_name() + " 产品导入主数据结束");

        }

        /**
         * 将商品从feed导入主数据
         *
         * @param originalFeed     Feed信息
         * @param channelId       channel id
//         * @param mapBrandMapping 品牌mapping一览
         * @param categoryTreeAllList 所有主类目
         */
        public void doSaveProductMainProp(
                CmsBtFeedInfoModel originalFeed
                , String channelId
//                , Map<String, String> mapBrandMapping
//                , Map<String, String> mapProductTypeMapping
//                , Map<String, String> mapSizeTypeMapping
//                ,  List<CmsMtCategoryTreeModel> categoryTreeList    // delete desmond 2016/07/04
                ,  List<CmsMtCategoryTreeAllModel> categoryTreeAllList
        ) {
            // feed类目名称
            String feedCategory = originalFeed.getCategory();

            // [ feed -> main ] 类目属性的匹配关系
//            CmsBtFeedMappingModel mapping = null;    // delete desmond 2016/07/04
            CmsBtFeedMapping2Model newMapping = null;

            // 通过Code查找到的产品
            CmsBtProductModel oldCmsProduct = null;

            // 从synship.com_mt_value_channel表中获取当前channel, 有多少个允许approve这个sku到平台上去售卖渠道cartId
            typeChannelBeanListApprove = TypeChannels.getTypeListSkuCarts(channelId, "A", "en"); // 取得允许Approve的数据
            if (ListUtils.isNull(typeChannelBeanListApprove)) {
                String errMsg = String.format("feed->master导入:共通配置异常终止:在com_mt_value_channel表中没有找到当前Channel允许售卖的Cart信息(用于生成product分平台信息) [ChannelId=%s A en]", channelId);
                $error(errMsg);
                throw new CommonConfigNotFoundException(errMsg);
            }

            // 从synship.com_mt_value_channel表中获取当前channel, 有多少个需要展示的cart
            typeChannelBeanListDisplay = TypeChannels.getTypeListSkuCarts(channelId, "D", "en"); // 取得展示用数据
            if (ListUtils.isNull(typeChannelBeanListDisplay)) {
                String errMsg = String.format("feed->master导入:共通配置异常终止:在com_mt_value_channel表中没有找到当前Channel需要展示的Cart信息(用于生成productGroup信息) [ChannelId=%s D en]", channelId);
                $error(errMsg);
                throw new CommonConfigNotFoundException(errMsg);
            }

            // jeff 2016/05 change start
            List <CmsBtFeedInfoModel> feedList = new ArrayList<>();
            List<CmsBtProductModel> cmsProductList = productService.getProductByOriginalCode(channelId, originalFeed.getCode());
            // 取得匹配到的类目是否只允许一个Sku
            boolean isSingleSku = false;
            // 如果找不到，也可能是原来的数据没有改。按原来的Code查找方式，看看有没有
            if (cmsProductList.size() == 0) {
                oldCmsProduct = productService.getProductByCode(channelId, originalFeed.getCode());
            }
            CmsBtProductModel cmsProductParam = null;   // add desmond 2016/07/04
            // 找不到Code
            if (oldCmsProduct == null && cmsProductList.size() == 0) {
                // 不存在
                // 获取类目属性匹配关系(默认的)
//                mapping = cmsBtFeedMappingDao.findDefault(channelId, feedCategory, true);  // delete desmond 2016/07/04
                newMapping = cmsBtFeedMapping2Dao.findDefault(channelId, feedCategory);
                // mapping check
//                if (!checkMapping(mapping, null,channelId, originalFeed)) return;          // delete desmond 2016/07/04

            } else {
                // 存在
                 // 根据OriginalCode找到一条（非拆分的情况）
                 if (cmsProductList.size() == 1 || oldCmsProduct != null) {
//                     CmsBtProductModel cmsProductParam = null;    // delete desmond 2016/07/04
                     if (cmsProductList.size() == 1) {
                         cmsProductParam = cmsProductList.get(0);
                     } else {
                         cmsProductParam = oldCmsProduct;
                     }

                     // 获取类目属性匹配关系(指定的主类目)
//                     mapping = cmsBtFeedMappingDao.selectByKey(channelId, feedCategory, cmsProductParam.getCommon().getCatPath());  // delete desmond 2016/07/04
                     if (cmsProductParam.getCommon() != null && cmsProductParam.getCommon().getCatPath() != null) {
                         newMapping = cmsBtFeedMapping2Dao.selectByKey(channelId, feedCategory, cmsProductParam.getCommon().getCatPath());
                     }

                     // mapping check
//                     if (!checkMapping(mapping, cmsProductParam,channelId, originalFeed)) return;  // delete desmond 2016/07/04
                 }
            }

            if (!this.skip_mapping_check) {
                // update desmon 2016/07/04 start   CmsMtCategoryTreeModel -> CmsMtCategoryTreeAllModel
//                if (mapping != null) {
//                    String mainCategoryPath = mapping.getMainCategoryPath();
//                    for (CmsMtCategoryTreeModel categoryTree : categoryTreeList) {
//                        if (mainCategoryPath.equals(categoryTree.getCatPath())) {
//                            if ("1".equals(categoryTree.getSingleSku())) {
//                                isSingleSku = true;
//                            }
//                            break;
//                        } else {
//                            List result = new ArrayList<>();
//                            CmsMtCategoryTreeModel categoryTreeFind = categoryTreeService.findCategorySingleSku(categoryTree, mainCategoryPath, result);
//                            if (categoryTreeFind != null) {
//                                if (result.size() > 0 || "1".equals(categoryTree.getSingleSku())) {
//                                    isSingleSku = true;
//                                }
//                                break;
//                            }
//                        }
//                    }
//                }

                // 主类目Path(更新的时候，优先用product的主类目路径，没有的话再使用newMapping匹配的主类目路径)
                String mainCategoryPath = null;
                if (cmsProductParam != null
                        && cmsProductParam.getCommon() != null
                        && !StringUtil.isEmpty(cmsProductParam.getCommon().getCatPath())) {
                    mainCategoryPath = cmsProductParam.getCommon().getCatPath();
                } else if (newMapping != null) {
                    mainCategoryPath = newMapping.getMainCategoryPath();
                }
                // 判断主类目路径是否只能为SingleSku
                if (!StringUtils.isEmpty(mainCategoryPath)) {
                    for (CmsMtCategoryTreeAllModel categoryTreeAll : categoryTreeAllList) {
                        if (mainCategoryPath.equals(categoryTreeAll.getCatPath())) {
                            if ("1".equals(categoryTreeAll.getSingleSku())) {
                                isSingleSku = true;
                            }
                            break;
                        } else {
                            List<String> result = new ArrayList<>();
                            CmsMtCategoryTreeAllModel categoryTreeFind = categoryTreeAllService.findCategorySingleSku(categoryTreeAll, mainCategoryPath, result);
                            if (categoryTreeFind != null) {
                                if (result.size() > 0 || "1".equals(categoryTreeAll.getSingleSku())) {
                                    isSingleSku = true;
                                }
                                break;
                            }
                        }
                    }
                }
                // update desmond 2016/07/04 end
            }

            boolean delOriginalFlg = false;
            // 需要拆分
            // 条件1：根据OriginalCode找到多条
            // 条件2：根据OriginalCode找到1条 并且 产品对应的主类目.singleSku == 1：需要拆分 并且 Feed.sku多条)
            // 条件3：根据OriginalCode找到0条 并且 对应的默认主类目.singleSku == 1：需要拆分 并且 Feed.sku多条)
            if (cmsProductList.size() > 1
                    || (cmsProductList.size() == 1 && isSingleSku && originalFeed.getSkus() != null && originalFeed.getSkus().size() > 1)
                    || (cmsProductList.size() == 0  && isSingleSku && originalFeed.getSkus() != null && originalFeed.getSkus().size() > 1 && oldCmsProduct == null)) {

                // 根据OriginalCode找到多条的情况下，先把多个Product信息集记一下Map<sku，ProductInfo>（这种情况下，Product下面只会有一个sku）
                Map<String, CmsBtProductModel> existProduct = new HashMap<>();
                if (cmsProductList.size() > 1) {
                    for (CmsBtProductModel cmsProduct : cmsProductList) {
                        String sku = "";
                        if (cmsProduct.getCommon().getSkus().size() == 1) {
                            sku = cmsProduct.getCommon().getSkus().get(0).getSkuCode();
                        }
                        if (!StringUtils.isEmpty(sku)) {
                            existProduct.put(sku, cmsProduct);
                        }
                    }
                }
                // 拆分
                for (CmsBtFeedInfoModel_Sku feedSku : originalFeed.getSkus()) {
                    CmsBtFeedInfoModel splitFeed = new CmsBtFeedInfoModel();
                    List<CmsBtFeedInfoModel_Sku> splitSkus = new ArrayList<>();
                    CmsBtFeedInfoModel_Sku splitSku = new CmsBtFeedInfoModel_Sku();
                    try {
                        // 根据OriginalCode找到1条的那种情况
                        if (cmsProductList.size() == 1) {
                            // 如果原来product中只包含一个sku，那么原来的那条还延用原来的Code
                            CmsBtProductModel cmsProduct = cmsProductList.get(0);
                            if (cmsProduct.getCommon().getSkus().size() == 1) {
                                // 如果原来product中只包含一个sku （Feed的sku从1个变化为1个以上的情况）
                                CmsBtProductModel_Sku productSku = cmsProduct.getCommon().getSkus().get(0);
                                if (feedSku.getSku().equals(productSku.getSkuCode())) {
                                    // product中包含的那个sku 还延用原来的Code，Mode
                                    BeanUtils.copyProperties(splitFeed, originalFeed);
                                    BeanUtils.copyProperties(splitSku, feedSku);
                                    splitFeed.setCode(cmsProduct.getCommon().getFields().getCode());
                                    splitFeed.setModel(cmsProduct.getCommon().getFields().getModel());
                                    splitSkus.add(splitSku);
                                    splitFeed.setSkus(splitSkus);
                                    feedList.add(splitFeed);
                                } else {
                                    // product中不包含的其他sku： model,code,sku重新设置为单个sku
                                    BeanUtils.copyProperties(splitFeed, originalFeed);
                                    BeanUtils.copyProperties(splitSku, feedSku);
                                    splitFeed.setCode(feedSku.getSku());
                                    splitFeed.setModel(feedSku.getSku());
                                    splitSkus.add(splitSku);
                                    splitFeed.setSkus(splitSkus);
                                    feedList.add(splitFeed);
                                }
                            } else {
                                // 如果原来product中包含一个以上sku， model,code,sku重新设置为单个sku
                                // （从非拆分的类目变化为拆分类目的情况）
                                BeanUtils.copyProperties(splitFeed, originalFeed);
                                BeanUtils.copyProperties(splitSku, feedSku);
                                splitFeed.setCode(feedSku.getSku());
                                splitFeed.setModel(feedSku.getSku());
                                splitSkus.add(splitSku);
                                splitFeed.setSkus(splitSkus);
                                feedList.add(splitFeed);
                                delOriginalFlg = true;
                            }
                        } else if (cmsProductList.size() == 0){
                            // 根据OriginalCode找到0条, model,code,sku重新设置为单个sku
                            BeanUtils.copyProperties(splitFeed, originalFeed);
                            BeanUtils.copyProperties(splitSku, feedSku);
                            splitFeed.setCode(feedSku.getSku());
                            splitFeed.setModel(feedSku.getSku());
                            splitSkus.add(splitSku);
                            splitFeed.setSkus(splitSkus);
                            feedList.add(splitFeed);
                        } else {
                            if (existProduct.containsKey(feedSku.getSku())) {
                                // product中包含的那个sku 还延用原来的Code，Mode
                                BeanUtils.copyProperties(splitFeed, originalFeed);
                                BeanUtils.copyProperties(splitSku, feedSku);
                                splitFeed.setCode(existProduct.get(feedSku.getSku()).getCommon().getFields().getCode());
                                splitFeed.setModel(existProduct.get(feedSku.getSku()).getCommon().getFields().getModel());
                                splitSkus.add(splitSku);
                                splitFeed.setSkus(splitSkus);
                                feedList.add(splitFeed);
                            } else {
                                // product中不包含的其他sku： model,code,sku重新设置为单个sku
                                BeanUtils.copyProperties(splitFeed, originalFeed);
                                BeanUtils.copyProperties(splitSku, feedSku);
                                splitFeed.setCode(feedSku.getSku());
                                splitFeed.setModel(feedSku.getSku());
                                splitSkus.add(splitSku);
                                splitFeed.setSkus(splitSkus);
                                feedList.add(splitFeed);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                // 不需要拆分
                feedList.add(originalFeed);
            }

            for (CmsBtFeedInfoModel feed : feedList) {

                CmsBtProductModel cmsProduct = null;
                // 查看当前商品, 在主数据中, 是否已经存在
                boolean blnProductExist;

                // 需要拆分的情况下，去确认每个Code/Sku是否存在
                if (feedList.size() > 1) {
                    cmsProduct = productService.getProductSingleSku(channelId, feed.getCode(), originalFeed.getCode());
                    if (cmsProduct == null) {
                        // 不存在
                        blnProductExist = false;
                        // 获取类目属性匹配关系(默认的)
//                        mapping = cmsBtFeedMappingDao.findDefault(channelId, feedCategory, true); // delete desmon 2016/07/04
                        newMapping = cmsBtFeedMapping2Dao.findDefault(channelId, feedCategory);
                    } else {
                        // 存在
                        blnProductExist = true;
                        // 获取类目属性匹配关系(指定的主类目)
//                        mapping = cmsBtFeedMappingDao.selectByKey(channelId, feedCategory, cmsProduct.getCommon().getCatPath());  // delete desmon 2016/07/04
                        if (cmsProduct.getCommon().getFields() != null && cmsProduct.getCommon().getCatPath() != null) {
                            newMapping = cmsBtFeedMapping2Dao.selectByKey(channelId, feedCategory, cmsProduct.getCommon().getCatPath());
                        }
                    }

                    // mapping check
//                    if (!checkMapping(mapping, cmsProduct,channelId, originalFeed)) return;   // delete desmon 2016/07/04
                } else {
                    // 不需要拆分的情况
                    if (cmsProductList.size() == 1) {
                        cmsProduct = cmsProductList.get(0);
                        blnProductExist = true;
                    } else {
                        cmsProduct = oldCmsProduct;
                        if (cmsProduct == null) {
                            blnProductExist = false;
                        } else {
                            blnProductExist = true;
                        }
                    }
                }

//                // jeff 2016/05 change end
//                if (cmsProduct == null) {
//                    // 不存在
//                    blnProductExist = false;
//
//                    // 获取类目属性匹配关系(默认的)
//                    mapping = cmsBtFeedMappingDao.findDefault(channelId, feedCategory, true);
//                } else {
//                    // 已经存在
//                    blnProductExist = true;
//
//                    // 获取类目属性匹配关系(指定的主类目)
//                    mapping = cmsBtFeedMappingDao.selectByKey(channelId, feedCategory, cmsProduct.getCatPath());
//                }

//                // 默认不忽略mapping check的场合, 需要做mapping check
//                if (!this.skip_mapping_check) {
//                    // 查看类目是否匹配完成
//                    if (mapping == null) {
//                        // 记下log, 跳过当前记录
//                        if (cmsProduct == null) {
//                            //                    logIssue(getTaskName(), String.format("[CMS2.0][测试]该feed类目, 没有匹配到主数据的类目 ( channel: [%s], feed: [%s] )", channelId, originalFeed.getCategory()));
//                            $warn(String.format("[CMS2.0][测试]该feed类目, 没有匹配到主数据的类目 ( channel: [%s], feed: [%s] )", channelId, originalFeed.getCategory()));
//                        } else {
//                            //                    logIssue(getTaskName(), String.format("[CMS2.0][测试]该feed类目, 没有匹配到主数据的类目 ( channel: [%s], feed: [%s], master: [%s] )", channelId, originalFeed.getCategory(), cmsProduct.getCatPath()));
//                            $warn(String.format("[CMS2.0][测试]该feed类目, 没有匹配到主数据的类目 ( channel: [%s], feed: [%s], master: [%s] )", channelId, originalFeed.getCategory(), cmsProduct.getCatPath()));
//                        }
//                        // 设置更新时间,更新者
//                        originalFeed.setModifier(getTaskName());
//                        cmsBtFeedInfoDao.update(originalFeed);
//                        return;
//                    }
//                    // 查看属性是否匹配完成
//                    if (mapping.getMatchOver() == 0) {
//                        // 如果没有匹配完成的话, 那就看看是否有共通
//                        mapping = cmsBtFeedMappingDao.findDefaultMainMapping(channelId, mapping.getMainCategoryPath());
//                        if (mapping == null || mapping.getMatchOver() == 0) {
//                            // 没有共通mapping, 或者没有匹配完成
//                            // 记下log, 跳过当前记录
//                            //                        logIssue(getTaskName(), String.format("[CMS2.0][测试]该主类目的属性匹配尚未完成 ( channel: [%s], feed: [%s], main: [%s] )", channelId, originalFeed.getCategory(), mapping.getScope().getMainCategoryPath()));
//                            $warn(String.format("[CMS2.0][测试]该主类目的属性匹配尚未完成 ( channel: [%s], feed: [%s], main: [%s] )", channelId, originalFeed.getCategory(), mapping == null ? "" : mapping.getMainCategoryPath()));
//                            // 设置更新时间,更新者
//                            originalFeed.setModifier(getTaskName());
//                            cmsBtFeedInfoDao.update(originalFeed);
//                            return;
//                        }
//
//                    }
//                }

                if (blnProductExist) {
                    // 修改商品数据
                    // 一般只改改价格神马的
//                    cmsProduct = doUpdateCmsBtProductModel(feed, cmsProduct, mapping, newMapping, mapBrandMapping, feedList.size() > 1 ? true : false, originalFeed.getCode());
                    cmsProduct = doUpdateCmsBtProductModel(feed, cmsProduct, newMapping, feedList.size() > 1 ? true : false, originalFeed.getCode());
                    if (cmsProduct == null) {
                        // 有出错, 跳过
                        String errMsg = "feed->master导入:更新:编辑商品的时候出错(cmsProduct = null):" + originalFeed.getChannelId() + ":" + originalFeed.getCode();
                        $error(errMsg);
                        throw new BusinessException(errMsg);
//                        $error(getTaskName() + ":更新:编辑商品的时候出错:" + originalFeed.getChannelId() + ":" + originalFeed.getCode());
//                        // 设置更新时间,更新者
//                        originalFeed.setModifier(getTaskName());
//                        cmsBtFeedInfoDao.update(originalFeed);
//                        return;
                    }

                    // tom 20160510 追加 START
                    // 更新wms_bt_item_details表的数据
                    if (!doSaveItemDetails(channelId, cmsProduct.getProdId(), feed)) {
                        // 如果出错了的话, 就跳出去
                        String errMsg = "feed->master导入:更新:更新wms_bt_item_details表数据的时候出错:" + originalFeed.getChannelId() + ":" + originalFeed.getCode();
                        $error(errMsg);
                        throw new BusinessException(errMsg);
//                        // 设置更新时间,更新者
//                        originalFeed.setModifier(getTaskName());
//                        cmsBtFeedInfoDao.update(originalFeed);
//                        return;
                    }
                    // tom 20160510 追加 END

                    // TODO: 没有设置的fields里的内容, 不会被清除? 这个应该是在共通里做掉的吧, 要是共通里不做的话就要自己写了

                    // 清除一些batch的标记 // TODO: 梁兄啊, batchField的更新没有放到product更新里, 暂时自己写一个用, 这里暂时注释掉
                    //                CmsBtProductModel_BatchField batchField = cmsProduct.getBatchField();
                    //                batchField.setAttribute("switchCategory", "0"); // 切换主类目->完成
                    //                cmsProduct.setBatchField(batchField);

//                    ProductUpdateBean requestModel = new ProductUpdateBean();
//                    requestModel.setProductModel(cmsProduct);
//                    requestModel.setModifier(getTaskName());
//                    requestModel.setIsCheckModifed(false); // 不做最新修改时间ｃｈｅｃｋ

                    // 更新价格相关项目
                    cmsProduct = doSetPrice(channelId, feed, cmsProduct);

                    // 设置店铺共通的店铺内分类信息
                    setSellerCats(feed, cmsProduct);

                    // productService.updateProduct(channelId, requestModel);
                    int updCnt = productService.updateProductFeedToMaster(channelId, cmsProduct, getTaskName());
                    if (updCnt == 0) {
                        // 有出错, 跳过
                        String errMsg = "feed->master导入:更新:编辑商品的时候排他错误:" + originalFeed.getChannelId() + ":" + originalFeed.getCode();
                        $error(errMsg);
                        throw new BusinessException(errMsg);
//                        $error(getTaskName() + "更新::编辑商品的时候排他错误:" + originalFeed.getChannelId() + ":" + originalFeed.getCode());
//                        return;
                    }
                    // productService.updateProductCommon(channelId, cmsProduct.getProdId(), cmsProduct.getCommon());

                    // TODO: 梁兄啊, batchField的更新没有放到product更新里, 暂时自己写一个用
                    // TODO: 等改好后下面这段内容就可以删掉了
                    // delete desmon 2016/07/01 start
                    // 已经没有batchField这个项目了，所以不用回写这个字段了
//                    {
//                        List<BulkUpdateModel> bulkList = new ArrayList<>();
//
//                        HashMap<String, Object> updateMap = new HashMap<>();
//                        updateMap.put("batchField.switchCategory", 0);
//
//                        HashMap<String, Object> queryMap = new HashMap<>();
//                        queryMap.put("prodId", cmsProduct.getProdId());
//
//                        BulkUpdateModel model = new BulkUpdateModel();
//                        model.setUpdateMap(updateMap);
//                        model.setQueryMap(queryMap);
//                        bulkList.add(model);
//
//                        BulkWriteResult result = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, getTaskName(), "$set");
//
//                    }
                    // delete desmon 2016/07/01 end
                    insertCmsBtFeedImportSize(channelId, cmsProduct);

                    $info("feed->master导入:更新成功:" + cmsProduct.getChannelId() + ":" + cmsProduct.getCommon().getFields().getCode());
//                    $info(getTaskName() + ":更新:" + cmsProduct.getChannelId() + ":" + cmsProduct.getCommon().getFields().getCode());
                    // jeff 2016/04 add start
//                    updateCnt++;
                    // jeff 2016/04 add end

                } else {
                    // 不存在的场合, 新建一个product
//                    cmsProduct = doCreateCmsBtProductModel(feed, mapping, newMapping, mapBrandMapping, feedList.size() > 1 ? true : false, originalFeed.getCode());
                    cmsProduct = doCreateCmsBtProductModel(feed, newMapping, feedList.size() > 1 ? true : false, originalFeed.getCode());
                    if (cmsProduct == null) {
                        // 有出错, 跳过
                        String errMsg = "feed->master导入:新增:编辑商品的时候出错(cmsProduct = null):" + originalFeed.getChannelId() + ":" + originalFeed.getCode();
                        $error(errMsg);
                        throw new BusinessException(errMsg);
//                        $error(getTaskName() + ":新增:编辑商品的时候出错:" + originalFeed.getChannelId() + ":" + originalFeed.getCode());
//                        // 设置更新时间,更新者
//                        originalFeed.setModifier(getTaskName());
//                        cmsBtFeedInfoDao.update(originalFeed);
//                        return;
                    }

                    // tom 20160510 追加 START
                    // 更新wms_bt_item_details表的数据
                    if (!doSaveItemDetails(channelId, cmsProduct.getProdId(), feed)) {
                        // 如果出错了的话, 就跳出去
                        String errMsg = "feed->master导入:新增:更新wms_bt_item_details表数据的时候出错:" + originalFeed.getChannelId() + ":" + originalFeed.getCode();
                        $error(errMsg);
                        throw new BusinessException(errMsg);
//                        // 设置更新时间,更新者
//                        originalFeed.setModifier(getTaskName());
//                        cmsBtFeedInfoDao.update(originalFeed);
//                        return;
                    }
                    // tom 20160510 追加 END

                    // 更新价格相关项目
                    cmsProduct = doSetPrice(channelId, feed, cmsProduct);

                    // 设置店铺共通的店铺内分类信息
                    setSellerCats(feed, cmsProduct);

                    productService.createProduct(channelId, cmsProduct, getTaskName());

                    insertCmsBtFeedImportSize(channelId, cmsProduct);

                    $info("feed->master导入:新增成功:" + cmsProduct.getChannelId() + ":" + cmsProduct.getCommon().getFields().getCode());
//                    $info(getTaskName() + ":新增:" + cmsProduct.getChannelId() + ":" + cmsProduct.getCommon().getFields().getCode());
                    // jeff 2016/04 add start
//                    insertCnt++;
                    // jeff 2016/04 add end
                }

                // jeff 2016/04 change start
                // 生成更新前的价格履历Bean
                // ProductPriceBean productPriceBeanBefore = getProductPriceBeanBefore(cmsProduct, blnProductExist);

                // 调用共通方法来设置价格
                // doSetPrice(channelId, feed, cmsProduct);
//                CmsBtProductModel cmsProductBean = doSetPrice(channelId, feed, cmsProduct);

                // 生成productGroup数据
                doSetGroup(feed);

                // 更新价格履历
                // productPriceLogService.insertPriceLog(channelId, productPriceBean, productPriceBeanBefore, "Feed导入Master价格更新", getTaskName());

                // add by desmond 2016/07/05 start
                // 记录商品价格表动履历，并向Mq发送消息同步sku,code,group价格范围
                if (cmsProduct != null && ListUtils.notNull(cmsProduct.getCommon().getSkus())) {
                    List<String> skuCodeList = cmsProduct.getCommon().getSkus()
                                  .stream()
                                  .map(CmsBtProductModel_Sku::getSkuCode)
                                  .collect(Collectors.toList());
                    // 记录商品价格变动履历
                    cmsBtPriceLogService.addLogForSkuListAndCallSyncPriceJob(skuCodeList, cmsProduct.getChannelId(), null, getTaskName(), "feed->master导入");
                }
                // add by desmond 2016/07/05 end


                // 自动上新
                // 是否自动上新标志
                String sxFlg = "0";
                CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.AUTO_APPROVE_PRODUCT_CHANGE);
                if (cmsChannelConfigBean != null && !StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
                    // 如果没有设定则不自动上新
                    sxFlg = cmsChannelConfigBean.getConfigValue1();
                }

                // 自动上新标志位 = 1:(自动上新) 并且 商品的状态是非lock，已Approved的场合
                // Add desmond 2016/07/01 start
                if ("1".equals(sxFlg)) {
                    // 当该产品未被锁定且已批准的时候，往workload表里面插入一条上新数据，并逻辑清空相应的business_log
                    sxProductService.insertSxWorkLoad(cmsProduct, getTaskName());
                }
//                if ("1".equals(sxFlg) && !"1".equals(cmsProduct.getLock())) {
//                    // 遍历主数据product里的sku,看看有没有
//                    for (Map.Entry<String, CmsBtProductModel_Platform_Cart> entry : cmsProduct.getPlatforms().entrySet()) {
//                        // P0（主数据）平台跳过
//                        if (entry.getValue().getCartId() < CmsConstants.ACTIVE_CARTID_MIN) {
//                            continue;
//                        }
//
//                        // 该平台已经Approved过的才插入workload表
//                        if (CmsConstants.ProductStatus.Approved.name().equalsIgnoreCase(entry.getValue().getStatus())) {
//                            sxProductService.insertSxWorkLoad(channelId, cmsProduct.getCommon().getFields().getCode(), entry.getValue().getCartId(), getTaskName());
//                        }
//                    }
//                }

                // Add desmond 2016/07/01 end
                // jeff 2016/04 change end

                // tom 20160510 删除 START
                // 这里不要了, 放到最前面去做, 如果出错了, 那么就跳过当前记录
                //            // 更新wms_bt_item_details表的数据
                //            doSaveItemDetails(channelId, cmsProduct.getProdId(), feed);
                // tom 20160510 删除 END

                // 更新price_log信息
                // 更新price_log信息 -> 共通代码里会处理的,我这边就不需要写了
                // 更新product_log信息
                // 更新product_log信息 -> 还要不要写呢? 状态变化的话,共通代码里已经有了,其他的变化,这里是否要更新进去? 应该不用了吧.

                // add desmond 2016/07/07 start
                if (blnProductExist) {
                    updateCnt++;
                } else {
                    insertCnt++;
                }
                // add desmond 2016/07/07 end
            }

            // jeff 2016/05 add start
            if (delOriginalFlg) {
                cmsBtProductDao.deleteWithQuery("{\"common.fields.code\":\"" + originalFeed.getCode() + "\"}", channelId);
                cmsBtProductGroupDao.deleteWithQuery("{\"productCodes\":\"" + originalFeed.getCode() + "\"}", channelId);
            }
             // jeff 2016/05 add end
            // 设置商品更新完成
//            originalFeed.setUpdFlg(1);           // 1:feed->master导入成功
//            originalFeed.setIsFeedReImport("0");
//            originalFeed.setUpdMessage("");      // add desmond 2016/07/05
//            originalFeed.setModifier(getTaskName());
//            feedInfoService.updateFeedInfo(originalFeed);
            updateFeedInfo(originalFeed, 1, "", "0");
            // ------------- 函数结束

        }


        /**
         * 默认不忽略mapping check的场合, 需要做mapping check
         *
         * @param mapping         feed与main的匹配关系
         * @param cmsProduct      产品对象
         * @param channelId       渠道id
         * @param feed           原始Feed
         * @return check结果
         */
            // delete desmond 2016/07/04 start
//        private boolean checkMapping(CmsBtFeedMappingModel mapping, CmsBtProductModel cmsProduct, String channelId, CmsBtFeedInfoModel feed) {
//            // 默认不忽略mapping check的场合, 需要做mapping check
//            if (!this.skip_mapping_check) {
//                // 查看类目是否匹配完成
//                if (mapping == null) {
//                    // 记下log, 跳过当前记录
//                    if (cmsProduct == null) {
//                        //                    logIssue(getTaskName(), String.format("[CMS2.0][测试]该feed类目, 没有匹配到主数据的类目 ( channel: [%s], feed: [%s] )", channelId, feed.getCategory()));
//                        $warn(String.format("[CMS2.0][测试]该feed类目, 没有匹配到主数据的类目 ( channel: [%s], feed: [%s] )", channelId, feed.getCategory()));
//                    } else {
//                        //                    logIssue(getTaskName(), String.format("[CMS2.0][测试]该feed类目, 没有匹配到主数据的类目 ( channel: [%s], feed: [%s], master: [%s] )", channelId, feed.getCategory(), cmsProduct.getCatPath()));
//                        $warn(String.format("[CMS2.0][测试]该feed类目, 没有匹配到主数据的类目 ( channel: [%s], feed: [%s], master: [%s] )", channelId, feed.getCategory(), cmsProduct.getCommon().getCatPath()));
//                    }
//                    // 设置更新时间,更新者
//                    feed.setModifier(getTaskName());
//                    cmsBtFeedInfoDao.update(feed);
//                    return false;
//                }
//                // 查看属性是否匹配完成
//                if (mapping.getMatchOver() == 0) {
//                    // 如果没有匹配完成的话, 那就看看是否有共通
//                    mapping = cmsBtFeedMappingDao.findDefaultMainMapping(channelId, mapping.getMainCategoryPath());
//                    if (mapping == null || mapping.getMatchOver() == 0) {
//                        // 没有共通mapping, 或者没有匹配完成
//                        // 记下log, 跳过当前记录
//                        //                        logIssue(getTaskName(), String.format("[CMS2.0][测试]该主类目的属性匹配尚未完成 ( channel: [%s], feed: [%s], main: [%s] )", channelId, feed.getCategory(), mapping.getScope().getMainCategoryPath()));
//                        $warn(String.format("[CMS2.0][测试]该主类目的属性匹配尚未完成 ( channel: [%s], feed: [%s], main: [%s] )", channelId, feed.getCategory(), mapping == null ? "" : mapping.getMainCategoryPath()));
//                        // 设置更新时间,更新者
//                        feed.setModifier(getTaskName());
//                        cmsBtFeedInfoDao.update(feed);
//                        return false;
//                    }
//
//                }
//            }
//            return true;
//        }
            // delete desmond 2016/07/04 end

        /**
         * 生成Fields的内容
         *
         * @param feed            feed的商品信息
//         * @param mapping         feed与main的匹配关系
//         * @param mapBrandMapping 品牌mapping一览
//         * @param schemaModel     主类目的属性的schema
         * @param newFlg          新建flg (true:新建商品 false:更新的商品)
//         * @param productField    商品属性
         * @param productCommonField    共通商品属性
         * @param isSplit          是否拆分对象
         * @param originalCode 原始Code
         * @return 返回整个儿的Fields的内容
         */
        // update desmond 2016/07/01 start
        // 删除product外面的Fields
        // jeff 2016/04 change start
        // private CmsBtProductModel_Field doCreateCmsBtProductModelField(CmsBtFeedInfoModel feed, CmsBtFeedMappingModel mapping, Map<String, String> mapBrandMapping, CmsMtCategorySchemaModel schemaModel, boolean newFlg) {
//        private CmsBtProductModel_Field doCreateCmsBtProductModelField(CmsBtFeedInfoModel feed, CmsBtFeedMappingModel mapping, Map<String, String> mapBrandMapping, CmsMtCategorySchemaModel schemaModel,
//                                                                       boolean newFlg, CmsBtProductModel_Field productField, CmsBtProductModel_Field ,boolean isSplit, String originalCode) {
        private CmsBtProductModel_Field doCreateCmsBtProductModelField(CmsBtFeedInfoModel feed, boolean newFlg,
                                                                       CmsBtProductModel_Field productCommonField,
                                                                       boolean isSplit, String originalCode) {
            // --------- 商品属性信息设定 ------------------------------------------------------
            // CmsBtProductModel_Field field = new CmsBtProductModel_Field();

//            if (!skip_mapping_check) {
//                // 遍历mapping,设置主数据的属性
//                if (mapping.getProps() != null) {
//                    for (Prop prop : mapping.getProps()) {
//                        if (!MappingPropType.FIELD.equals(prop.getType())) {
//                            // 这段逻辑只处理类目属性(FIELD类型)的,如果是SKU属性或共通属性,则跳过
//                            continue;
//                        }
//
//                        // 递归设置属性
//                        // jeff 2016/04 change start
//                        // field.put(prop.getProp(), getPropValueByMapping(prop.getProp(), prop, feed, field, schemaModel));
//                        if (newFlg || productField.get(prop.getProp()) == null) {
//                            Object propValue = getPropValueByMapping(prop.getProp(), prop, feed, productField, schemaModel);
//                            if (propValue != null) {
//                                productField.put(prop.getProp(), propValue);
//                            }
//                        }
//                        // jeff 2016/04 change end
//                    }
//                }
//            }

            // 新建的场合才设置的属性
//            if (newFlg) {
            // TODO: 现在mapping画面功能还不够强大, 共通属性和SKU属性先暂时写死在代码里, 等我写完上新代码回过头来再想办法改 (tom.zhu)
            // 主数据的field里, 暂时强制写死的字段(共通属性)
            // 产品code
//            if (newFlg || StringUtils.isEmpty(productField.getCode())) {
//                productField.setCode(feed.getCode());
//            }
            if (newFlg || StringUtils.isEmpty(productCommonField.getCode())) {
                productCommonField.setCode(feed.getCode());
            }

            // jeff 2016/05 add start
            // 产品原始code
//            if (newFlg || StringUtils.isEmpty(productField.getOriginalCode())) {
//                productField.setOriginalCode(originalCode);
//            }
            if (newFlg || StringUtils.isEmpty(productCommonField.getOriginalCode())) {
                productCommonField.setOriginalCode(originalCode);
            }
            // jeff 2016/05 add end

            // 品牌
//            if (newFlg || StringUtils.isEmpty(productField.getBrand()) || "1".equals(feed.getIsFeedReImport())) {
//                if (mapBrandMapping.containsKey(feed.getBrand().toLowerCase())) {
//                    productField.setBrand(mapBrandMapping.get(feed.getBrand().toLowerCase()));
//                } else {
//                    $error(getTaskName() + ":" + String.format("[CMS2.0][测试]feed->main的品牌mapping没做 ( channel id: [%s], feed brand: [%s] )", feed.getChannelId(), feed.getBrand()));
//
//                    // 记下log, 跳过当前记录
//                    //                logIssue(getTaskName(), String.format("[CMS2.0][测试]feed->main的品牌mapping没做 ( channel id: [%s], feed brand: [%s] )", feed.getChannelId(), feed.getBrand()));
//                    $warn(String.format("[CMS2.0][测试]feed->main的品牌mapping没做 ( channel id: [%s], feed brand: [%s] )", feed.getChannelId(), feed.getBrand()));
//
//                    return null;
//                }
//            }
            if (newFlg || StringUtils.isEmpty(productCommonField.getBrand()) || "1".equals(feed.getIsFeedReImport())) {
                // 插入的品牌名称为feed中的品牌名称的小写值
                String feedBrandLowerCase = feed.getBrand().toLowerCase().trim();
                if (mapBrandMapping.containsKey(feedBrandLowerCase)) {
                    productCommonField.setBrand(mapBrandMapping.get(feedBrandLowerCase));
                } else {
                    // add by desmond 2016/07/18 start
                    // 碰到没有品牌Mapping的商品时，自动向Synship.com_mt_value_channel表中增加品牌Mapping数据
                    // 设为feed品牌名的小写值(统一都用小写品牌名，画面展示也用小写品牌)
                    productCommonField.setBrand(feedBrandLowerCase);
                    // 将该feed品牌小写值mapping信息插入或更新到Synship.com_mt_value_channel表中(41:品牌mapping信息)
//                    insertBrandMappingInfo(this.channel.getOrder_channel_id(), feed, feedBrandLowerCase);
                    if (!StringUtils.isEmpty(feedBrandLowerCase)) {
                        comMtValueChannelService.insertComMtValueChannelMapping(41, this.channel.getOrder_channel_id(),
                                feedBrandLowerCase, feedBrandLowerCase, getTaskName());
                        // 将更新完整之后的mapping信息添加到前面取出来的品牌mapping表中
                        mapBrandMapping.put(feedBrandLowerCase, feedBrandLowerCase);
                    }
                    // add by desmond 2016/07/18 end

                    // update desmond 2016/07/04 start
//                    String strAddUpdate = newFlg ? "新增" : "更新";
//                    String errMsg = "feed->master导入:" + strAddUpdate +"出错:" + feed.getChannelId() + ":" + feed.getCode() + ":feed->main的品牌mapping没做 (feed brand:" + feed.getBrand() + ")";
//                    $error(errMsg);
//                    throw new BusinessException(errMsg);

//                    $error(getTaskName() + ":" + String.format("[CMS2.0][测试]feed->main的品牌mapping没做 ( channel id: [%s], feed brand: [%s] )", feed.getChannelId(), feed.getBrand()));

                    // 记下log, 跳过当前记录
                    //                logIssue(getTaskName(), String.format("[CMS2.0][测试]feed->main的品牌mapping没做 ( channel id: [%s], feed brand: [%s] )", feed.getChannelId(), feed.getBrand()));
//                    $warn(String.format("[CMS2.0][测试]feed->main的品牌mapping没做 ( channel id: [%s], feed brand: [%s] )", feed.getChannelId(), feed.getBrand()));

//                    return null;
                    // update desmond 2016/07/04 end
                }
            }


            // 产品名称（英文）
//            if (newFlg || StringUtils.isEmpty(productField.getProductNameEn()) || "1".equals(feed.getIsFeedReImport())) {
//                productField.setProductNameEn(feed.getName());
//            }
            if (newFlg || StringUtils.isEmpty(productCommonField.getProductNameEn()) || "1".equals(feed.getIsFeedReImport())) {
                productCommonField.setProductNameEn(feed.getName());
            }
            // 长标题, 中标题, 短标题: 都是中文, 需要自己翻译的
            // 款号model
//            if (newFlg || StringUtils.isEmpty(productField.getModel()))  {
//                productField.setModel(feed.getModel());
//            }
            if (newFlg || StringUtils.isEmpty(productCommonField.getModel())) {
                productCommonField.setModel(feed.getModel());
            }
            // 颜色(中文颜色，feed->master不用设置了)
//            if (newFlg || StringUtils.isEmpty(productField.getColor()) || "1".equals(feed.getIsFeedReImport())) {
//                productField.setColor(feed.getColor());
//            }
            // update desmond 2016/07/05 start
            // 小林说common.fields.color是中文颜色，不用在这里设置了，英文颜色值设到新加的字段codeDiff（商品特质英文）里面
            if (newFlg || "1".equals(feed.getIsFeedReImport())) {
                productCommonField.setColor("");   // 初期值
            }
            // 商品特质英文(颜色/口味/香型等)
            if (newFlg || StringUtils.isEmpty(productCommonField.getCodeDiff()) || "1".equals(feed.getIsFeedReImport())) {
                productCommonField.setCodeDiff(feed.getColor());
            }
            // update desmond 2016/07/05 end
            // 产地
//            if (newFlg || StringUtils.isEmpty(productField.getOrigin()) || "1".equals(feed.getIsFeedReImport())) {
//                productField.setOrigin(feed.getOrigin());
//            }
            if (newFlg || StringUtils.isEmpty(productCommonField.getOrigin()) || "1".equals(feed.getIsFeedReImport())) {
                productCommonField.setOrigin(feed.getOrigin());
            }
            // 简短描述英文
//            if (newFlg || StringUtils.isEmpty(productField.getShortDesEn()) || "1".equals(feed.getIsFeedReImport())) {
//                productField.setShortDesEn(feed.getShortDescription());
//            }
            if (newFlg || StringUtils.isEmpty(productCommonField.getShortDesEn()) || "1".equals(feed.getIsFeedReImport())) {
                productCommonField.setShortDesEn(feed.getShortDescription());
            }
            // 详情描述英文
//            if (newFlg || StringUtils.isEmpty(productField.getLongDesEn()) || "1".equals(feed.getIsFeedReImport())) {
//                productField.setLongDesEn(feed.getLongDescription());
//            }
            if (newFlg || StringUtils.isEmpty(productCommonField.getLongDesEn()) || "1".equals(feed.getIsFeedReImport())) {
                productCommonField.setLongDesEn(feed.getLongDescription());
            }
            // 税号集货: 不要设置
            // 税号个人: 不要设置
//            if (newFlg || (StringUtils.isEmpty(productField.getHsCodePrivate()))) {
//                field.setHsCodePrivate(getPropSimpleValueByMapping(MappingPropType.COMMON, Constants.productForOtherSystemInfo.HS_CODE_PRIVATE, mapping));
//            }
            // quantity
            if (newFlg || productCommonField.getQuantity() == null || "1".equals(feed.getIsFeedReImport())) {
                productCommonField.setQuantity(feed.getQty());
            }
            // 材质
            if (newFlg || StringUtils.isEmpty(productCommonField.getMaterialEn()) || "1".equals(feed.getIsFeedReImport())) {
                productCommonField.setMaterialEn(feed.getMaterial());
            }

            // delete desmond 2016/07/01 start
            // product.field.status -> product.platforms.P23.status之后，这里不要设置，到分平台下面去设置了
//            // 产品状态
//            if (newFlg || StringUtils.isEmpty(productField.getStatus()) || "1".equals(feed.getIsFeedReImport())) {
//                productField.setStatus(CmsConstants.ProductStatus.New); // 产品状态: 初始时期为(新建) Synship.com_mt_type : id = 44 : productStatus
//            }
            // delete desmond 2016/07/01 end

//            {
//                // 所有的翻译内容
//                String transFlg = "";
//                Map<String, String> mapTrans = customPropService.getTransList(feed.getChannelId(), feed.getCategory());
//                // 翻译(标题 和 长描述)
//                String strProductNameEn = productField.getProductNameEn();
//                String strLongDesEn = productField.getLongDesEn();
//                for (Map.Entry<String, String> entry : mapTrans.entrySet()) {
//                    strProductNameEn = strProductNameEn.replace(entry.getKey(), entry.getValue());
//                    strLongDesEn = strLongDesEn.replace(entry.getKey(), entry.getValue());
//                }
//                // 调用百度翻译
//                // List<String> transBaiduOrg = new ArrayList<>(); // 百度翻译 - 输入参数
//                if (newFlg || StringUtils.isEmpty(productField.getOriginalTitleCn()) || "1".equals(feed.getIsFeedReImport())) {
//                    // transBaiduOrg.add(strProductNameEn); // 标题
//                    transFlg = "标题";
//                }
//                if (newFlg || StringUtils.isEmpty(productField.getOriginalDesCn()) || "1".equals(feed.getIsFeedReImport())) {
//                    if (!StringUtils.isEmpty(strLongDesEn)) {
//                        // TODO: 临时关掉017
//                        if ("010".equals(feed.getChannelId())) {
//                            $info("英寸转厘米:原始:" + strLongDesEn);
//                            // transBaiduOrg.add(new InchStrConvert().inchToCM(strLongDesEn)); // 长描述
//                        } else {
//                            // transBaiduOrg.add(strLongDesEn); // 长描述
//                        }
//                        transFlg = "长描述";
//                    }
//                }
//                List<String> transBaiduCn; // 百度翻译 - 输出参数
//                try {
//                    if ("017".equals(feed.getChannelId()) || "021".equals(feed.getChannelId())) {
//                        // lucky vitamin 和 BHFO不做翻译
//                        if (newFlg || StringUtils.isEmpty(productField.getOriginalTitleCn()) || "1".equals(feed.getIsFeedReImport())) {
//                            // productField.setOriginalTitleCn(""); // 标题
//                        }
//                        if (newFlg || StringUtils.isEmpty(productField.getOriginalDesCn()) || "1".equals(feed.getIsFeedReImport())) {
//                            // productField.setOriginalDesCn(""); // 长描述
//                        }
//                    } else {
//                        if (transBaiduOrg.size() > 0) {
//                            transBaiduCn = BaiduTranslateUtil.translate(transBaiduOrg);
//                            if (transBaiduOrg.size() == 2) {
//                                field.setOriginalTitleCn(transBaiduCn.get(0)); // 标题
//                                field.setOriginalDesCn(transBaiduCn.get(1)); // 长描述
//                            } else {
//                                if ("标题".equals(transFlg)) {
//                                    field.setOriginalTitleCn(transBaiduCn.get(0)); // 标题
//                                } else {
//                                    field.setOriginalDesCn(transBaiduCn.get(0)); // 长描述
//                                }
//                            }
//                        }
//                        if (newFlg || !newFlg && StringUtils.isEmpty(productField.getOriginalDesCn())) {
//                            field.setOriginalDesCn(""); // 长描述
//                        }
////                    } else {
////                        if (transBaiduOrg.size() > 0) {
////                            transBaiduCn = BaiduTranslateUtil.translate(transBaiduOrg);
////                            if (transBaiduOrg.size() == 2) {
////                                field.setOriginalTitleCn(transBaiduCn.get(0)); // 标题
////                                field.setOriginalDesCn(transBaiduCn.get(1)); // 长描述
////                            } else {
////                                if ("标题".equals(transFlg)) {
////                                    field.setOriginalTitleCn(transBaiduCn.get(0)); // 标题
////                                } else {
////                                    field.setOriginalDesCn(transBaiduCn.get(0)); // 长描述
////                                }
////                            }
////                        }
////                    }
//
//                } catch (Exception e) {
//                    // 翻译失败的场合,全部设置为空, 运营自己翻译吧
//                    if (newFlg || !newFlg && StringUtils.isEmpty(productField.getOriginalTitleCn())) {
//                        field.setOriginalTitleCn(""); // 标题
//                    }

//                } catch (Exception e) {
//                    // 翻译失败的场合,全部设置为空, 运营自己翻译吧
//                    if (newFlg || StringUtils.isEmpty(productField.getOriginalTitleCn()) || "1".equals(feed.getIsFeedReImport())) {
//                        // productField.setOriginalTitleCn(""); // 标题
//                    }
//                    if (newFlg || StringUtils.isEmpty(productField.getOriginalDesCn()) || "1".equals(feed.getIsFeedReImport())) {
//                        // productField.setOriginalDesCn(""); // 长描述
//                    }
//                }
//            }
//            }

            // 官方网站链接，商品图片1，产品分类，适用人群的Feed数据可能会变化，所以不管新建还是更新操作都会去重新设定
            // 官方网站链接
            if (feed.getClientProductURL() == null) {
//                productField.setClientProductUrl("");
                productCommonField.setClientProductUrl("");
            } else {
//                productField.setClientProductUrl(feed.getClientProductURL());
                productCommonField.setClientProductUrl(feed.getClientProductURL());
            }

            // 1品牌方商品图, 2包装图, 3角度图, 4PC端自定义图, 5APP端自定义图, 6PC端自拍商品图, 7App端自拍商品图, 8吊牌图
            // 暂时只设置1商品图片,有的店铺设置设置6PC端自拍商品图
//            {
//                if (newFlg) {
            List<Map<String, Object>> multiComplex = new LinkedList<>();
            List<Map<String, Object>> multiComplex6 = new LinkedList<>();

            // jeff 2016/05 change start
            //  List<String> lstImageOrg = feed.getImage();
            List<String> lstImageOrg = null;
            if (isSplit) {
                if (feed.getSkus() != null && feed.getSkus().size() > 0) {
                    lstImageOrg = feed.getSkus().get(0).getImage();
                }
                if (lstImageOrg == null) {
                    lstImageOrg = feed.getImage();
                }
            } else {
                lstImageOrg = feed.getImage();
            }
            // jeff 2016/05 change end
            if (lstImageOrg != null && lstImageOrg.size() > 0) {
                for (String imgOrg : lstImageOrg) {
                    Map<String, Object> multiComplexChildren = new HashMap<>();
                    Map<String, Object> multiComplexChildren6 = new HashMap<>();
                    // jeff 2016/04 change start
                    // multiComplexChildren.put("image1", imgOrg);
                    String picName = doUpdateImage(feed.getChannelId(), feed.getCode(), imgOrg);
                    multiComplexChildren.put("image1", picName);
                    multiComplexChildren6.put("image6", picName);
                    // jeff 2016/04 add end
                    multiComplex.add(multiComplexChildren);
                    multiComplex6.add(multiComplexChildren6);
                }
            }

//            productField.put("images1", multiComplex);
            productCommonField.put("images1", multiComplex);
            // 新增商品时，根据设置决定是否同时设置PC端自拍商品图images6,更新商品时不更新images6(老的数据里面本来就没有images6的时候更新)
            if (newFlg
                    || (ListUtils.isNull(productCommonField.getImages6()) || StringUtils.isEmpty(productCommonField.getImages6().get(0).getName()))
                    || "1".equals(feed.getIsFeedReImport())) {
                // 从cms_mt_channel_config表从取得新建product时是否自动设置PC端自拍商品图images6(1:自动设置  空，0:不设置)
                String autoSetImages6Flg = "0";    // 0:不设置PC端自拍商品图images6
                CmsChannelConfigBean productTypeChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(this.channel.getOrder_channel_id(),
                        CmsConstants.ChannelConfig.AUTO_SET_IMAGES6_FLG);
                if (productTypeChannelConfigBean != null && "1".equals(productTypeChannelConfigBean.getConfigValue1())) {
                    autoSetImages6Flg = "1";       // 1:自动设置PC端自拍商品图images6
                }

                if ("1".equals(autoSetImages6Flg)) {
                    // 设置PC端自拍商品图images6
                    productCommonField.put("images6", multiComplex6);
                }
            }

            // 商品翻译状态, 翻译者, 翻译时间, 商品编辑状态, 价格审批flg, lock商品: 暂时都不用设置

//            SELECT * from Synship.com_mt_value_channel where type_id in (57,58);
            // 从cms_mt_channel_config表从取得产品分类是否从feed导入flg,默认为0：不从feed导入运营手动添加
            String productTypeFromFeedFlg = "0";    // 0：不从feed导入运营手动添加产品分类
            CmsChannelConfigBean productTypeChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(this.channel.getOrder_channel_id(),
                    CmsConstants.ChannelConfig.PRODUCT_TYPE_FROM_FEED_FLG);
            if (productTypeChannelConfigBean != null && "1".equals(productTypeChannelConfigBean.getConfigValue1())) {
                productTypeFromFeedFlg = "1";       // 1:从feed导入产品分类
            }

            // 从cms_mt_channel_config表从取得适用人群是否从feed导入flg,默认为0：不从feed导入运营手动添加
            String sizeTypeFromFeedFlg = "0";       // 0：不从feed导入运营手动添加适用人群
            CmsChannelConfigBean sizeTypeChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(this.channel.getOrder_channel_id(),
                    CmsConstants.ChannelConfig.SIZE_TYPE_FROM_FEED_FLG);
            if (sizeTypeChannelConfigBean != null && "1".equals(sizeTypeChannelConfigBean.getConfigValue1())) {
                sizeTypeFromFeedFlg = "1";          // 1:从feed导入适用人群
            }

            String feedProductType = "";
            String feedSizeType = "";
            switch (feed.getChannelId()) {
                case "010":
                    // 产品分类
                    if (("1".equals(productTypeFromFeedFlg)) &&
                            (newFlg || StringUtils.isEmpty(productCommonField.getProductType()) || "1".equals(feed.getIsFeedReImport()))) {
                        feedProductType = feed.getAttribute().get("ItemClassification").get(0);
                        productCommonField.setProductType(feedProductType);
                    }
                    // 适用人群
                    if (("1".equals(sizeTypeFromFeedFlg)) &&
                            (newFlg || StringUtils.isEmpty(productCommonField.getSizeType()) || "1".equals(feed.getIsFeedReImport()))) {
                        switch (feed.getSizeType()) {
                            case "Women's":
                                feedSizeType = "women";
                                productCommonField.setSizeType(feedSizeType);
                                break;
                            case "Men's":
                                feedSizeType = "men";
                                productCommonField.setSizeType(feedSizeType);
                                break;
                        }
                    }

                    break;
                default:
                    // 产品分类
                    if (("1".equals(productTypeFromFeedFlg)) &&
                            (newFlg || StringUtils.isEmpty(productCommonField.getProductType()) || "1".equals(feed.getIsFeedReImport()))) {
                        feedProductType = feed.getProductType();
                        productCommonField.setProductType(feedProductType);
                    }
                    // 适用人群
                    if (("1".equals(sizeTypeFromFeedFlg)) &&
                            (newFlg || StringUtils.isEmpty(productCommonField.getSizeType()) || "1".equals(feed.getIsFeedReImport()))) {
                        feedSizeType = feed.getSizeType();
                        productCommonField.setSizeType(feedSizeType);
                    }
            }
            // jeff 2016/04 change end

            // add by desmond 2016/07/22 start
            // feed->master导入时，将一些项目(如：sizeType,productType)的初始化中英文mapping信息插入到Synship.com_mt_value_channel表中
            // 从cms_mt_channel_config表从取得的产品分类是否从feed导入flg=1的时候，才插入产品分类mapping信息
            if ("1".equals(productTypeFromFeedFlg)
                    && !StringUtils.isEmpty(feedProductType)
                    && !mapProductTypeMapping.containsKey(feedProductType)) {
                // 插入产品分类初始中英文mapping信息到Synship.com_mt_value_channel表中
                comMtValueChannelService.insertComMtValueChannelMapping(57, feed.getChannelId(), feedProductType,
                        feedProductType, getTaskName());
                // 将更新完整之后的mapping信息添加到前面取出来的产品分类mapping表中
                mapProductTypeMapping.put(feedProductType, feedProductType);
            }

            // 从cms_mt_channel_config表从取得的适用人群是否从feed导入flg=1的时候，才插入适用人群mapping信息
            if ("1".equals(sizeTypeFromFeedFlg)
                    && !StringUtils.isEmpty(feedSizeType)
                    && !mapSizeTypeMapping.containsKey(feedSizeType)) {
                // 插入适用人群初始中英文mapping信息到Synship.com_mt_value_channel表中
                comMtValueChannelService.insertComMtValueChannelMapping(58, feed.getChannelId(), feedSizeType,
                        feedSizeType, getTaskName());
                // 将更新完整之后的mapping信息添加到前面取出来的适用人群mapping表中
                mapSizeTypeMapping.put(feedSizeType, feedSizeType);
            }
            // add by desmond 2016/07/22 end

            // jeff 2016/04 add start
//            if (newFlg || productField.getAttribute("isMasterMain") == null || "1".equals(feed.getIsFeedReImport())) {
//                // isMain
//                productField.setIsMasterMain(getIsMasterMain(feed));
//            }
            // jeff 2016/04 add end

            if (newFlg || productCommonField.getAttribute("isMasterMain") == null || "1".equals(feed.getIsFeedReImport())) {
                // isMain
                // TODO 这个版本先这样
//                productCommonField.setIsMasterMain(productField.getIsMasterMain());
                productCommonField.setIsMasterMain(getIsMasterMain(feed));
            }

            // 商品翻译状态
            if (newFlg || StringUtils.isEmpty(productCommonField.getTranslateStatus()) || "1".equals(feed.getIsFeedReImport())) {
                productCommonField.setTranslateStatus("0");  // 初期值为0
            }

            // 税号设置状态
            if (newFlg || StringUtils.isEmpty(productCommonField.getHsCodeStatus()) || "1".equals(feed.getIsFeedReImport())) {
                productCommonField.setHsCodeStatus("0");     // 初期值为0
            }

            // 使用说明英文
            if (newFlg || StringUtils.isEmpty(productCommonField.getUsageEn()) || "1".equals(feed.getIsFeedReImport())) {
                productCommonField.setUsageEn(feed.getUsageEn());
            }

            // APP端启用开关
            if (newFlg || productCommonField.getAppSwitch() == null || "1".equals(feed.getIsFeedReImport())) {
                // 从cms_mt_channel_config取得APP端启用开关的值（0或者1）
                CmsChannelConfigBean sxPriceConfig = CmsChannelConfigs.getConfigBeanNoCode(feed.getChannelId(), CmsConstants.ChannelConfig.APP_SWITCH);
                if (sxPriceConfig != null && !StringUtils.isEmpty(sxPriceConfig.getConfigValue1()) && "1".equals(sxPriceConfig.getConfigValue1())) {
                    // APP端启用开关的值配置成1的场合，设为APP端启用开1
                    productCommonField.setAppSwitch(1);
                } else {
                    productCommonField.setAppSwitch(0);
                }
            }
            // add desmond 2016/07/05 end

//            return productField;
            return productCommonField;
        }
        // update desmond 2016/07/01 end

        /**
         * 生成一个新的product
         *
         * @param feed            feed的商品信息
//         * @param mapping         feed与main的匹配关系
         * @param newMapping      新的feed与main的匹配关系
         * @param isSplit         是否拆分对象
         * @param originalCode 原始Code
         * @return 一个新的product的内容
         */
        private CmsBtProductModel doCreateCmsBtProductModel(
                CmsBtFeedInfoModel feed
//                , CmsBtFeedMappingModel mapping
                , CmsBtFeedMapping2Model newMapping
                , boolean isSplit
                , String originalCode
        ) {
            if (skip_mapping_check) {
                // 如果允许条件检查的场合, 说明是正在执行旧系统迁移到新系统
                // 那么就需要到旧数据库里看一下这个数据在旧系统里是否存在, 如果不存在, 那么这条数据有问题, 不能直接迁移
                int cnt = tmpOldCmsDataDao.checkExist(feed.getChannelId(), feed.getCode());
                if (cnt == 0) {
                    // 不存在, 直接跳出
                    String errMsg = String.format("feed->master导入:新增:编辑商品的时候警告:feed->mapping, 未上新过, 不能直接导入.channel id: [%s], code:[%s]", feed.getChannelId(), feed.getCode());
                    $warn(errMsg);
                    throw new BusinessException(errMsg);
//                    $warn(String.format("[CMS2.0][测试]feed->mapping, 未上新过, 不能直接导入.channel id: [%s], code:[%s]", feed.getChannelId(), feed.getCode()));
//                    return null;
                }
            }

            // 新创建的product
            CmsBtProductModel product = new CmsBtProductModel();

            // --------- 基本信息设定 ------------------------------------------------------
            product.setChannelId(feed.getChannelId());

            // delete desmond 2016/07/04 start
//            if (!skip_mapping_check) {
//                String catPath = mapping.getMainCategoryPath();
//                product.setCatId(MD5.getMD5(catPath)); // 主类目id
//                product.setCatPath(catPath); // 主类目path
//            }
            // delete desmond 2016/07/04 end
            product.setProdId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_PROD_ID)); // 商品的id

            // jeff 2016/06 add start
            // 默认不lock
            product.setLock("0");
            // jeff 2016/06 add end

            // delete desmond 2016/07/04 start
            // --------- 获取主类目的schema信息 ------------------------------------------------------
//            CmsMtCategorySchemaModel schemaModel =  cmsMtCategorySchemaDao.getMasterSchemaModelByCatId(product.getCommon().getCatId());
            // delete desmond 2016/07/04 end

            CmsBtProductModel_Common common = new CmsBtProductModel_Common();
            common.setFields(new CmsBtProductModel_Field());
            product.setCommon(common);

            // jeff 2016/04 change start
            // CmsBtProductModel_Field field = doCreateCmsBtProductModelField(feed, mapping, mapBrandMapping, schemaModel, true);
            // CmsBtProductModel_Field field = doCreateCmsBtProductModelField(feed, mapping, mapBrandMapping, schemaModel, true, new CmsBtProductModel_Field(), common.getFields(), isSplit, originalCode);
            CmsBtProductModel_Field field = doCreateCmsBtProductModelField(feed, true, common.getFields(), isSplit, originalCode);
            // jeff 2016/04 change end
            if (field == null) {
                return null;
            }
            // ProductCarts
            // product.setCarts(getProductCarts(feed));
            // delete desmond 2016/07/01 start
            // 外面的fields被删掉了，common.fields通过doCreateCmsBtProductModelField方法的参数已经设好值了
//            product.setFields(field);
            // delete desmond 2016/07/01 end

            if (newMapping != null) {
                String catPath = newMapping.getMainCategoryPath();
                common.setCatId(MD5.getMD5(catPath)); // 主类目id
                common.setCatPath(catPath); // 主类目path
            } else {   // add desmond 2016/07/04 start
                // 设置初期值
                common.setCatId(""); // 主类目id
                common.setCatPath(""); // 主类目path
            }

            // add desmond 2016/07/05 start
            // 主类目设置状态
            if (!StringUtils.isEmpty(common.getCatPath())) {
                common.getFields().setCategoryStatus("1");
                common.getFields().setCategorySetter(getTaskName());
                common.getFields().setCategorySetTime(DateTimeUtil.getNow());
            } else {
                common.getFields().setCategoryStatus("0");
                common.getFields().setCategorySetter("");
                common.getFields().setCategorySetTime("");
            }
            // add desmond 2016/07/05 end

            // 分平台属性
            Map<String, CmsBtProductModel_Platform_Cart> platforms = new HashMap<>();
            // 追加P0(主数据)平台属性
            CmsBtProductModel_Platform_Cart platformP0 = new CmsBtProductModel_Platform_Cart();
            platformP0.setCartId(0);
            CmsBtProductGroupModel groupP0 = getGroupIdByFeedModel(feed.getChannelId(), feed.getModel(), "0");
            if (groupP0 == null) {
                platformP0.setMainProductCode(common.getFields().getCode());
            } else {
                platformP0.setMainProductCode(groupP0.getMainProductCode());

                // 把主商品的几个状态复制过来 james 2016/08/29
                copyAttributeFromMainProduct(feed.getChannelId(), common,groupP0.getMainProductCode());
            }
            platforms.put("P0", platformP0);
            // add desmond 2016/07/04 end

//            // 从synship.com_mt_value_channel表中获取当前channel, 有多少个允许approve这个sku到平台上去售卖渠道cartId
//            List<TypeChannelBean> typeChannelBeanListApprove = TypeChannels.getTypeListSkuCarts(feed.getChannelId(), "A", "en"); // 取得允许Approve的数据
//            if (ListUtils.isNull(typeChannelBeanListApprove)) {
//                String errMsg = String.format("feed->master导入:新增:在com_mt_value_channel表中没有找到当前Channel允许售卖的Cart信息 [ChannelId=%s A en]", feed.getChannelId());
//                $error(errMsg);
//                throw new BusinessException(errMsg);
////                return null;
//            }
            // delete desmond 2016/07/01 start
//            List<Integer> skuCarts = new ArrayList<>();
//            for (TypeChannelBean typeChannelBean : typeChannelBeanListApprove) {
//                skuCarts.add(Integer.parseInt(typeChannelBean.getValue()));
//            }

            // SKU级属性列表
//            List<String> skuFieldSchemaList = new ArrayList<>();
//            if (!skip_mapping_check && schemaModel != null) {
//                MultiComplexField skuFieldSchema = (MultiComplexField) schemaModel.getSku();
//                List<Field> schemaFieldList = skuFieldSchema.getFields();
//                for (Field f : schemaFieldList) {
//                    skuFieldSchemaList.add(f.getId());
//                }
//            }
            // delete desmond 2016/07/01 end

            // --------- 商品Sku信息设定 ------------------------------------------------------
            // 对应product.skus -> product.common.skus变更
//            List<CmsBtProductModel_Sku> mainSkuList = new ArrayList<>();
            List<CmsBtProductModel_Sku> commonSkuList = new ArrayList<>();
            for (CmsBtFeedInfoModel_Sku sku : feed.getSkus()) {
                // 设置单个sku的信息
//                CmsBtProductModel_Sku mainSku = new CmsBtProductModel_Sku();
                CmsBtProductModel_Sku commonSku = new CmsBtProductModel_Sku();

//                mainSku.setSkuCode(sku.getSku()); // sku
//                mainSku.setBarcode(sku.getBarcode()); // barcode
//                mainSku.setSize(sku.getSize()); // 尺码

//                mainSku.setPriceMsrp(sku.getPrice_msrp());// msrp -> 共通API进行设置
//                mainSku.setPriceRetail(sku.getPrice_current()); // 零售价: 未审批 -> 共通API进行设置
//                mainSku.setPriceSale(sku); // 销售价: 已审批 (不用自动设置)

//                if (!skip_mapping_check) {  // delete sku级别的属性在画面上设置，这里不用做了
//                    // 设置一些每个类目不一样的, sku级别的属性
//                    mainSku.putAll(doSetCustomSkuInfo(feed, skuFieldSchemaList));
//                }

                commonSku.setSkuCode(sku.getSku()); // sku
                commonSku.setBarcode(sku.getBarcode()); // barcode
                commonSku.setClientSkuCode(sku.getClientSku()); // ClientSku
                commonSku.setClientSize(sku.getSize()); // ClientSize
                commonSku.setSize(sku.getSize()); // 尺码
                // 重量(单位：磅) 如果原始重量不是lb的,feed里已根据公式转成lb
                if (!StringUtils.isEmpty(sku.getWeightCalc()))
                    commonSku.setWeight(NumberUtils.toDouble(sku.getWeightCalc()));

                // 增加默认渠道
//                mainSku.setSkuCarts(skuCarts); // 删除现在没有skuCarts这个项目了

//                mainSkuList.add(mainSku);
                commonSkuList.add(commonSku);
            }
//            product.setSkus(mainSkuList);
//            common.setSkus(mainSkuList);
            common.setSkus(commonSkuList);
            // update desmond 2016/07/01 end

            // --------- platform ------------------------------------------------------
//            Map<String, CmsBtProductModel_Platform_Cart> platforms = new HashMap<>();  // delete desmond 2016/07/04
            List<CmsMtCategoryTreeAllModel_Platform> platformCategoryList = null;
            // 取得新的主类目对应的平台类目
            if (newMapping != null) {
                CmsMtCategoryTreeAllModel categoryTreeAllModel = categoryTreeAllService.getCategoryByCatPath(newMapping.getMainCategoryPath());
                if (categoryTreeAllModel != null) {
                    platformCategoryList = categoryTreeAllModel.getPlatformCategory();
                }
            }

            // add desmond 2016/07/07 start
            // 根据渠道和平台取得已经申请的平台类目
            Map<String, Map<String, CmsMtPlatformCategoryTreeModel>> applyPlatformCategoryMap =
                    categoryTreeAllService.getApplyPlatformCategory(feed.getChannelId(), typeChannelBeanListApprove);
            // add desmond 2016/07/07 end

            for (TypeChannelBean typeChannelBean : typeChannelBeanListApprove) {
                // add desmond 2016/07/05 start
                // P0（主数据）等平台不用设置分平台共通属性(typeChannel表里面保存的是0)
                int iCartId = Integer.parseInt(typeChannelBean.getValue());
                if (iCartId < CmsConstants.ACTIVE_CARTID_MIN) {
                    continue;
                }
                // add desmond 2016/07/05 end
                CmsBtProductModel_Platform_Cart platform = new CmsBtProductModel_Platform_Cart();
                // cartId
                platform.setCartId(Integer.parseInt(typeChannelBean.getValue()));
                // 设定是否主商品
                // 如果是聚美或者独立官网的话，那么就是一个Code对应一个Group
                CmsBtProductGroupModel group = null;
                if (!CartEnums.Cart.JM.getId().equals(typeChannelBean.getValue())
                            && !CartEnums.Cart.CN.getId().equals(typeChannelBean.getValue())) {
                    group = getGroupIdByFeedModel(feed.getChannelId(), feed.getModel(), typeChannelBean.getValue());
                }
                if (group == null) {
                    platform.setpIsMain(1);
                    platform.setMainProductCode(common.getFields().getCode());  // add desmond 2016/07/04
                } else {
                    platform.setpIsMain(0);
                    platform.setMainProductCode(group.getMainProductCode());    // add desmond 2016/07/04
                }

                // 平台类目状态(新增时)
                platform.setpCatStatus("0");  // add desmond 2016/07/05
                // 如果新的主类目对应的平台类目存在，那么设定
                if (platformCategoryList != null) {
                    for (CmsMtCategoryTreeAllModel_Platform platformCategory : platformCategoryList) {
                        CartBean cartBean = Carts.getCart(typeChannelBean.getValue());
                        if (cartBean != null && platformCategory.getPlatformId().equals(cartBean.getPlatform_id())) {
                            // update desmond 2016/07/07 start
                            // 新增时，如果该catId已经申请了才设置平台catId属性，没申请不设置
                            if (applyPlatformCategoryMap.get(typeChannelBean.getValue()) != null
                                    && applyPlatformCategoryMap.get(typeChannelBean.getValue()).get(platformCategory.getCatId()) != null) {
                                platform.setpCatId(platformCategory.getCatId());
                                platform.setpCatPath(platformCategory.getCatPath());
                                platform.setpCatStatus("1");
                            }
                            break;
                            // update desmond 2016/07/07 end
                        }
                    }
                }
                // 商品状态
                platform.setStatus(CmsConstants.ProductStatus.Pending.toString());
                // 平台属性状态(新增时)
                platform.setpAttributeStatus("0");    // add desmond 2016/07/05

                // 平台sku
                List<BaseMongoMap<String, Object>> skuList = new ArrayList<>();
                for (CmsBtFeedInfoModel_Sku sku : feed.getSkus()) {
                    BaseMongoMap<String, Object> skuInfo = new BaseMongoMap();
                    skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.skuCode.name(), sku.getSku());
                    skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.isSale.name(), true);
                    skuList.add(skuInfo);
                }

                platform.setSkus(skuList);
                platforms.put("P" + typeChannelBean.getValue(), platform);
            }
            product.setPlatforms(platforms);

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
            product.getFeed().setCatId(feed.getCatId());
            product.getFeed().setCatPath(feed.getCategory());

            // --------- 商品Group信息设定 ------------------------------------------------------
            // 创建新的group
            // jeff 2016/04 change start
//            CmsBtProductGroupModel group = doSetGroup(feed, product);
//            product.setGroups(group);
//            doSetGroup(feed);
            // jeff 2016/04 change end

            return product;
        }

        // delete desmond 2016/07/01 start
//        // 现在sku的属性都是通过画面设置了，这里不用做了
//        private Map<String, String> doSetCustomSkuInfo(CmsBtFeedInfoModel feed, List<String> skuFieldSchemaList) {
//            Map<String, String> mainSku = new HashMap<>();
//
//            // TODO: 现在mapping画面功能还不够强大, 共通属性和SKU属性先暂时写死在代码里, 等我写完上新代码回过头来再想办法改 (tom.zhu)
//            // -------------------- START
//            switch (feed.getChannelId()) {
//                case "010":
//                    // ==============================================
//                    // jewelry要设置三个属性: 戒指手寸, 项链长度, 手链长度
//                    // ==============================================
//
////                    String 戒指手寸_MASTER = "prop_9066257";
//                    String 戒指手寸_MASTER = "alias_name";
//                    String 戒指手寸_FEED = "Ringsize";
//                    String 项链长度_MASTER = "in_prop_150988152";
//                    String 项链长度_FEED = "ChainLength";
//                    String 手链长度_MASTER = "in_prop_151018199";
//                    String 手链长度_FEED = "Bracelet Length";
//
//                    // 戒指手寸
//                    if (skuFieldSchemaList.contains(戒指手寸_MASTER)) {
//                        if (feed.getAttribute().get(戒指手寸_FEED) != null && feed.getAttribute().get(戒指手寸_FEED).size() > 0) {
//                            String val = feed.getAttribute().get(戒指手寸_FEED).get(0);
//
//                            mainSku.put(戒指手寸_MASTER, val);
//                        }
//                    }
//                    // 项链长度
//                    if (skuFieldSchemaList.contains(项链长度_MASTER)) {
//                        if (feed.getAttribute().get(项链长度_FEED) != null && feed.getAttribute().get(项链长度_FEED).size() > 0) {
//                            String val = feed.getAttribute().get(项链长度_FEED).get(0);
//
//                            val = doEditSkuTemplate(val, "in2cm", "", "cm");
//                            mainSku.put(项链长度_MASTER, val);
//                        }
//                    }
//                    // 手链长度
//                    if (skuFieldSchemaList.contains(手链长度_MASTER)) {
//                        if (feed.getAttribute().get(手链长度_FEED) != null && feed.getAttribute().get(手链长度_FEED).size() > 0) {
//                            String val = feed.getAttribute().get(手链长度_FEED).get(0);
//
//                            val = doEditSkuTemplate(val, "in2cm", "", "cm");
//                            mainSku.put(手链长度_MASTER, val);
//                        }
//                    }
//
//                    break;
//                case "012":
//                    // BCBG暂时没有这方面需求
//                    break;
//            }
//            // -------------------- END
//
//            return mainSku;
//        }
        // delete desmond 2016/07/01 end

        /**
         * 更新product
         *
         * @param feed            feed的商品信息
         * @param product         主数据的product
//         * @param mapping         feed与main的匹配关系
         * @param newMapping      新的feed与main的匹配关系
         * @param isSplit          是否拆分对象
         * @param originalCode 原始Code
         * @return 修改过的product的内容
         */
//        private CmsBtProductModel doUpdateCmsBtProductModel(CmsBtFeedInfoModel feed, CmsBtProductModel product, CmsBtFeedMappingModel mapping, CmsBtFeedMapping2Model newMapping, Map<String, String> mapBrandMapping, boolean isSplit, String originalCode) {
        private CmsBtProductModel doUpdateCmsBtProductModel(CmsBtFeedInfoModel feed, CmsBtProductModel product,
                                                            CmsBtFeedMapping2Model newMapping,
                                                            boolean isSplit, String originalCode) {

            // jeff 2016/05 add start
            boolean numIdNoSet = doSetGroup(feed);
//            if (numIdNoSet && !skip_mapping_check) {
//                String catPath = mapping.getMainCategoryPath();
//                product.setCatId(MD5.getMD5(catPath)); // 主类目id
//                product.setCatPath(catPath); // 主类目path
//            }
            // jeff 2016/05 add end

            // 注意: 价格是在外面共通方法更新的, 这里不需要更新

            // --------- 获取主类目的schema信息 ------------------------------------------------------
            // delete desmond 2016/07/04 start 现在不用通过shema匹配sku信息了
//            CmsMtCategorySchemaModel schemaModel = cmsMtCategorySchemaDao.getMasterSchemaModelByCatId(product.getCommon().getCatId());
            // delete desmond 2016/07/04 end
            // 更新Fields字段
            // jeff 2016/04 change start
            // CmsBtProductModel_Field field = doCreateCmsBtProductModelField(feed, mapping, mapBrandMapping, schemaModel, false);
            // CmsBtProductModel_Field field = doCreateCmsBtProductModelField(feed, mapping, mapBrandMapping, schemaModel, false, product.getFields(), isSplit, originalCode);
            if (product.getCommon() == null) {
                CmsBtProductModel_Common common = new CmsBtProductModel_Common();
                common.setFields(new CmsBtProductModel_Field());
                product.setCommon(common);
            } else if (product.getCommon().getFields() == null) {
                product.getCommon().setFields(new CmsBtProductModel_Field());
            }
            // delete desmond 2016/07/01 start 跟上面重复了
//            if (product.getFields() == null) {
//                product.setFields(new CmsBtProductModel_Field());
//            }
//            CmsBtProductModel_Field field = doCreateCmsBtProductModelField(feed, mapping, mapBrandMapping, schemaModel, false, product.getFields(), product.getCommon().getFields(), isSplit, originalCode);
            CmsBtProductModel_Field field = doCreateCmsBtProductModelField(feed, false, product.getCommon().getFields(), isSplit, originalCode);
            // delete desmond 2016/07/01 end
            // jeff 2016/04 change end
            if (field == null) {
                return null;
            }

            // 商品的状态
//            String productStatus = product.getFields().getStatus();
//            // TODO: 暂时这个功能封印, 有些店铺的运营其实并不是很希望重新确认一下, 而不确认的话, 其实问题也不是很大 START
//            if (CmsConstants.ProductStatus.Approved.toString().equals(productStatus)) {
//                // 如果曾经的状态是已经approved的话, 需要把状态改回ready, 让运营重新确认一下商品
//                productStatus = CmsConstants.ProductStatus.Ready.toString();
//            }
//            // TODO: 暂时这个功能封印, 有些店铺的运营其实并不是很希望重新确认一下, 而不确认的话, 其实问题也不是很大 END
//            field.setStatus(productStatus);
            // jeff 2016/04 add start
            // 更新的场合，虽然不更新下面2个字段，但是之后要用到这2个字段，所以原样取出
//            if (StringUtils.isEmpty(field.getCode())) {
//                field.setCode(product.getFields().getCode());
//            }

//            if (StringUtils.isEmpty(field.getHsCodePrivate())) {
//                field.setHsCodePrivate(product.getFields().getHsCodePrivate());
//            }
            // jeff 2016/04 add end
            // 没有上新并且Feed到主数据Mapping存在并且设置Feed重新导入的情况下重新设置主类目
            if (numIdNoSet && newMapping!= null && "1".equals(feed.getIsFeedReImport())) {
                String catPath = newMapping.getMainCategoryPath();
                product.getCommon().setCatId(MD5.getMD5(catPath)); // 主类目id
                product.getCommon().setCatPath(catPath); // 主类目path
                // add desmond 2016/07/05 start
                if (!StringUtils.isEmpty(product.getCommon().getCatPath())) {
                    product.getCommon().getFields().setCategoryStatus("1"); // 主类目设置状态
                    product.getCommon().getFields().setCategorySetter(getTaskName());
                    product.getCommon().getFields().setCategorySetTime(DateTimeUtil.getNow());
                } else {
                    product.getCommon().getFields().setCategoryStatus("0"); // 主类目设置状态
                    product.getCommon().getFields().setCategorySetter("");
                    product.getCommon().getFields().setCategorySetTime("");
                }
                // add desmond 2016/07/05 end
            }
            // product.setFields(field);

            // delete desmond 2016/07/01 start
            // 不用设置sku级属性(现在通过画面做），也不用再设置product.skus,删除下面这段代码
            // SKU级属性列表
//            List<String> skuFieldSchemaList = new ArrayList<>();
//            if (!skip_mapping_check && schemaModel!= null) {
//                MultiComplexField skuFieldSchema = (MultiComplexField) schemaModel.getSku();
//                List<Field> schemaFieldList = skuFieldSchema.getFields();
//                for (Field f : schemaFieldList) {
//                    skuFieldSchemaList.add(f.getId());
//                }
//            }

//            // 遍历feed的skus
//            for (CmsBtFeedInfoModel_Sku feedSku : feed.getSkus()) {
//                // 遍历主数据product里的sku,看看有没有
//                boolean blnFound = false;
//                for (CmsBtProductModel_Sku sku : product.getCommon().getSkus()) {
//                    if (feedSku.getSku().equals(sku.getSkuCode())) {
//                        blnFound = true;
//                        break;
//                    }
//                }
//
//                // 如果找到了,那就什么都不做,如果没有找到,那么就需要添加
//                if (!blnFound) {
//                    // 获取当前channel, 有多少个platform
//                    List<TypeChannelBean> typeChannelBeanListApprove = TypeChannels.getTypeListSkuCarts(feed.getChannelId(), "A", "en"); // 取得允许Approve的数据
//                    List<Integer> skuCarts = new ArrayList<>();
//                    if (typeChannelBeanListApprove != null) {
//                        for (TypeChannelBean typeChannelBean : typeChannelBeanListApprove) {
//                            skuCarts.add(Integer.parseInt(typeChannelBean.getValue()));
//                        }
//                    }
//
//                    CmsBtProductModel_Sku sku = new CmsBtProductModel_Sku();
//                    sku.setSkuCode(feedSku.getSku());
//                    sku.setBarcode(feedSku.getBarcode()); // barcode
//                    sku.setSize(feedSku.getSize()); // 尺码
//
////                    sku.setPriceMsrp(feedSku.getPrice_msrp()); // msrp -> 共通API进行设置
////                    sku.setPriceRetail(feedSku.getPrice_current()); // 零售价: 未审批 -> 共通API进行设置
//
//                    if (!skip_mapping_check) {   // delete sku级别的属性在画面上设置，这里不用做了
//                        // 设置一些每个类目不一样的, sku级别的属性
//                        sku.putAll(doSetCustomSkuInfo(feed, skuFieldSchemaList));
//                    }
//
//                    product.getSkus().add(sku);
//                }
//
//            }
            // delete desmond 2016/07/01 end

            // 生成common.skus
            for (CmsBtFeedInfoModel_Sku feedSku : feed.getSkus()) {
                // 遍历主数据product里的sku,看看有没有
                boolean blnFound = false;
                List<CmsBtProductModel_Sku> skuList = product.getCommon().getSkus();
                if (skuList == null) {
                    skuList = new ArrayList<>();
                    product.getCommon().setSkus(skuList);
                }
                for (CmsBtProductModel_Sku sku : skuList) {
                    if (feedSku.getSku().equals(sku.getSkuCode())) {
                        blnFound = true;
                        // 找到这个skuCode的时候,更新从feed导入的相关项目
                        sku.setBarcode(feedSku.getBarcode());
                        sku.setClientSkuCode(feedSku.getClientSku());
                        sku.setClientSize(feedSku.getSize());
                        sku.setSize(feedSku.getSize());
                        if (!StringUtils.isEmpty(feedSku.getWeightCalc()))
                            sku.setWeight(NumberUtils.toDouble(feedSku.getWeightCalc()));  // 重量(单位：磅)

                        break;
                    }
                }

                // 如果找到了,那就什么都不做,如果没有找到,那么就需要添加
                if (!blnFound) {
                    // 没找到这个skuCode,则新加这个sku
                    CmsBtProductModel_Sku sku = new CmsBtProductModel_Sku();
                    sku.setSkuCode(feedSku.getSku());
                    sku.setBarcode(feedSku.getBarcode());
                    sku.setClientSkuCode(feedSku.getClientSku());
                    sku.setClientSize(feedSku.getSize());
                    sku.setSize(feedSku.getSize());        // Add by desmond 2016/07/04 因为上新用的是这个字段
                    if (!StringUtils.isEmpty(feedSku.getWeightCalc()))
                        sku.setWeight(NumberUtils.toDouble(feedSku.getWeightCalc()));  // 重量(单位：磅)

                    skuList.add(sku);
                }
            }

            // 如果platform不存在，先把platform建出来
            Map<String, CmsBtProductModel_Platform_Cart> platforms = null;
            if (product.getPlatforms() == null || product.getPlatforms().size() == 0) {
                platforms = new HashMap<>();
                product.setPlatforms(platforms);
            } else {
                platforms = product.getPlatforms();
            }

            // add desmond 2016/07/04 start
            // 追加P0(主数据)平台属性
            if (platforms.get("P0") == null) {
                CmsBtProductModel_Platform_Cart platformP0 = new CmsBtProductModel_Platform_Cart();
                platformP0.setCartId(0);
                CmsBtProductGroupModel groupP0 = productGroupService.selectMainProductGroupByCode(feed.getChannelId(), product.getCommon().getFields().getCode(), 0);
                if (groupP0 == null) {
                    platformP0.setMainProductCode(product.getCommon().getFields().getCode());
                } else {
                    platformP0.setMainProductCode(groupP0.getMainProductCode());
                }
                platforms.put("P0", platformP0);
            }
            // add desmond 2016/07/04 end

//            // 从synship.com_mt_value_channel表中获取当前channel, 有多少个允许Approve的cartId
//            List<TypeChannelBean> typeChannelBeanListApprove = TypeChannels.getTypeListSkuCarts(feed.getChannelId(), "A", "en"); // 取得允许Approve的数据
//            if (ListUtils.isNull(typeChannelBeanListApprove)) {
//                String errMsg = String.format("feed->master导入:更新:在com_mt_value_channel表中没有找到当前Channel允许售卖的Cart信息 [ChannelId=%s A en]", feed.getChannelId());
//                $error(errMsg);
//                throw new BusinessException(errMsg);
////                return null;
//            }

            // add desmond 2016/07/07 start
            // 根据渠道和平台取得已经申请的平台类目
            Map<String, Map<String, CmsMtPlatformCategoryTreeModel>> applyPlatformCategoryMap =
                    categoryTreeAllService.getApplyPlatformCategory(feed.getChannelId(), typeChannelBeanListApprove);
            // add desmond 2016/07/07 end

            List<CmsMtCategoryTreeAllModel_Platform> platformCategoryList = null;
            for (TypeChannelBean typeChannelBean : typeChannelBeanListApprove) {
                // add desmond 2016/07/05 start
                // P0（主数据）等平台不用设置分平台共通属性(typeChannel表里面保存的是0)
                int iCartId = Integer.parseInt(typeChannelBean.getValue());
                if (iCartId < CmsConstants.ACTIVE_CARTID_MIN) {
                    continue;
                }
                // add desmond 2016/07/05 end
                boolean blnFound = false;
                // 查看platforms下是否包含某个cartId的内容
                for (Map.Entry<String, CmsBtProductModel_Platform_Cart> entry : platforms.entrySet()) {
                    if (typeChannelBean.getValue().equals(String.valueOf(entry.getValue().getCartId()))) {
                        blnFound = true;
                        break;
                    }
                }
                if (!blnFound) {
                    // 更新时，没找到该cartId对应的platform，则新建这个cartId对应的platform  PXX
                    if (platformCategoryList == null && newMapping != null) {
                        CmsMtCategoryTreeAllModel categoryTreeAllModel = categoryTreeAllService.getCategoryByCatPath(newMapping.getMainCategoryPath());
                        if (categoryTreeAllModel != null) {
                            platformCategoryList = categoryTreeAllModel.getPlatformCategory();
                        }
                    }
                    CmsBtProductModel_Platform_Cart platform = new CmsBtProductModel_Platform_Cart();
                    // cartId
                    platform.setCartId(Integer.parseInt(typeChannelBean.getValue()));
                    // 设定是否主商品
                    CmsBtProductGroupModel group = productGroupService.selectMainProductGroupByCode(feed.getChannelId(), product.getCommon().getFields().getCode(), Integer.parseInt(typeChannelBean.getValue()));
                    if (group == null) {
                        platform.setpIsMain(0);
                    } else {
                        platform.setpIsMain(1);
                    }

                    // 平台类目状态(更新时，新增PXX平台属性时)
                    platform.setpCatStatus("0");
                    // 如果新的主类目对应的平台类目存在，那么设定
                    if (platformCategoryList != null) {
                        for (CmsMtCategoryTreeAllModel_Platform platformCategory : platformCategoryList) {
                            CartBean cartBean = Carts.getCart(typeChannelBean.getValue());
                            if (cartBean != null && platformCategory.getPlatformId().equals(cartBean.getPlatform_id())) {
                                // update desmond 2016/07/07 start
                                // 新增PXX平台属性时，如果该catId已经申请了就设置平台catId属性，没申请就不设置
                                if (applyPlatformCategoryMap.get(typeChannelBean.getValue()) != null
                                        && applyPlatformCategoryMap.get(typeChannelBean.getValue()).get(platformCategory.getCatId()) != null) {
                                    platform.setpCatId(platformCategory.getCatId());
                                    platform.setpCatPath(platformCategory.getCatPath());
                                    platform.setpCatStatus("1");
                                }
                                break;
                                // update desmond 2016/07/07 end
                            }
                        }
                    }
                    // 商品状态
                    platform.setStatus(CmsConstants.ProductStatus.Pending.toString());
                    // 平台属性状态(更新时，新增PXX平台属性时)
                    platform.setpAttributeStatus("0");   // add desmond 2016/07/05
                    platforms.put("P" + typeChannelBean.getValue(), platform);
                } else {
                    // add desmond 2016/07/07 start
                    // 更新时，找到该cartId对应的platform，只更新pCatId相关属性，不更新其他属性
                    if (platformCategoryList != null) {
                        for (CmsMtCategoryTreeAllModel_Platform platformCategory : platformCategoryList) {
                            CartBean cartBean = Carts.getCart(typeChannelBean.getValue());
                            if (cartBean != null && platformCategory.getPlatformId().equals(cartBean.getPlatform_id())) {
                                CmsBtProductModel_Platform_Cart platform = platforms.get("P" + typeChannelBean.getValue());
                                // 如果pCatId已经手动设过了，不更新；如果没有设置过，并且该catId已经申请了才更新,没申请不更新
                                if (platform != null
                                        && StringUtils.isEmpty(platform.getpCatId())
                                        && applyPlatformCategoryMap.get(typeChannelBean.getValue()) != null
                                        && applyPlatformCategoryMap.get(typeChannelBean.getValue()).get(platformCategory.getCatId()) != null) {
                                    platform.setpCatId(platformCategory.getCatId());
                                    platform.setpCatPath(platformCategory.getCatPath());
                                    platform.setpCatStatus("1");
                                }
                                break;
                            }
                        }
                    }
                    // add desmond 2016/07/07 end
                }
            }


            // 生成platform.[cartId].skus
            for (CmsBtFeedInfoModel_Sku feedSku : feed.getSkus()) {
                // 遍历主数据product里的sku,看看有没有
                for (Map.Entry<String, CmsBtProductModel_Platform_Cart> entry : product.getPlatforms().entrySet()) {
                    // add desmond 2016/07/05 start
                    // P0（主数据）平台不用设置sku
                    if (entry.getValue().getCartId() < CmsConstants.ACTIVE_CARTID_MIN) {
                        continue;
                    }
                    // add desmond 2016/07/05 end
                    boolean blnFound = false;
                    if (entry.getValue().getSkus() != null) {
                        for (BaseMongoMap<String, Object> platFormSku : entry.getValue().getSkus()) {
                            if (feedSku.getSku().equals(platFormSku.get(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()))) {
                                blnFound = true;
                                break;
                            }
                        }
                    }
                    // 如果找到了,那就什么都不做,如果没有找到,那么就需要添加
                    if (!blnFound) {
                        BaseMongoMap<String, Object> skuInfo = new BaseMongoMap();
                        skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.skuCode.name(), feedSku.getSku());
                        skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.isSale.name(), true);
                        if (entry.getValue().getSkus() == null) {
                            entry.getValue().setSkus(new ArrayList<BaseMongoMap<String, Object>>());
                        }
                        entry.getValue().getSkus().add(skuInfo);
                    }
                }
            }

            // --------- 商品Group信息设定 ------------------------------------------------------
            // 设置group(现有的不变, 有增加的就增加)
            // jeff 2016/04 change start
//            CmsBtProductGroupModel group = doSetGroup(feed, product);
//            product.setGroups(group);
//            doSetGroup(feed);
            // jeff 2016/04 change end

            // TOM 20160413 这是一个错误, 这段话不应该要的 START
//            // 更新状态, 准备重新上传到各个平台
//            for (CmsBtProductModel_Group_Platform platform : product.getGroups().getPlatforms()) {
//                platform.setPlatformStatus(CmsConstants.PlatformStatus.WaitingPublish);
//            }
            // TOM 20160413 这是一个错误, 这段话不应该要的 END

            // Feed导入Master时，在Product更新的情况下，是否更新Feed节点下面的数据
            String feedUpdateFlg = "0";
            CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(this.channel.getOrder_channel_id(), CmsConstants.ChannelConfig.FEED_UPDATE_FLG);
            if (cmsChannelConfigBean != null && "1".equals(cmsChannelConfigBean.getConfigValue1())) {
                feedUpdateFlg = "1";
            }

            if ("1".equals(feedUpdateFlg)) {
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
            }
            // TODO 因为这2个字段是新加的，原来存在的数据的这2个字段需要导入，等全部导过一遍之后，这2句话再移到条件内
            product.getFeed().setCatId(feed.getCatId());
            product.getFeed().setCatPath(feed.getCategory());


            return product;
        }

        /**
         * 设置group
         *
         * @param feed 品牌方提供的数据
         * @return NumID是否都是空 1：是 2：否
         */
        // jeff 2016/04 change start
        // private CmsBtProductGroupModel doSetGroup(CmsBtFeedInfoModel feed, CmsBtProductModel product) {
//        private void doSetGroup(CmsBtFeedInfoModel feed) {
        private boolean doSetGroup(CmsBtFeedInfoModel feed) {
//            CmsBtProductGroupModel group = product.getGroups();
//            if (group == null) {
//                group = new CmsBtProductGroupModel();
//            }

            boolean result = true;

//            // 从synship.com_mt_value_channel表中获取当前channel, 有多少个需要展示的cart
//            List<TypeChannelBean> typeChannelBeanListDisplay = TypeChannels.getTypeListSkuCarts(feed.getChannelId(), "D", "en"); // 取得展示用数据
//            if (ListUtils.isNull(typeChannelBeanListDisplay)) {
//                String errMsg = String.format("feed->master导入:生成productGroup信息失败:在com_mt_value_channel表中没有找到当前Channel需要展示的Cart信息 [ChannelId=%s D en]", feed.getChannelId());
//                $error(errMsg);
//                throw new BusinessException(errMsg);
////                return result;
//            }

            // 根据code, 到group表中去查找所有的group信息
            List<CmsBtProductGroupModel> existGroups = productGroupService.selectProductGroupListByCode(feed.getChannelId(), feed.getCode());

            // 循环一下
            for (TypeChannelBean shop : typeChannelBeanListDisplay) {
                // 检查一下这个platform是否已经存在, 如果已经存在, 那么就不需要增加了
                boolean blnFound = false;
                for (CmsBtProductGroupModel group : existGroups) {
                    if (group.getCartId() == Integer.parseInt(shop.getValue())) {
                        blnFound = true;
                        // NumId有值
                        if (!StringUtils.isEmpty(group.getNumIId())) {
                            result = false;
                        }
                        break;
                    }
                }
                if (blnFound) {
                    continue;
                }

                // group对象
                CmsBtProductGroupModel group = null;
                // 如果是聚美或者独立官网的时候，是一个Code对应一个Group,其他的平台都是几个Code对应一个Group
                if (!CartEnums.Cart.JM.getId().equals(shop.getValue())
                        && !CartEnums.Cart.CN.getId().equals(shop.getValue())) {
                    // 取得product.model对应的group信息
                    group = getGroupIdByFeedModel(feed.getChannelId(), feed.getModel(), shop.getValue());
                }

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

            return result;
        }

        // jeff 2016/04 change end

        // jeff 2016/04 add start

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
//        private List<CmsBtProductGroupModel> getGroupsByCode(String channelId, String code) {
//            // 先去看看是否有存在的了
//            JongoQuery queryObject = new JongoQuery();
//            queryObject.setQuery("{\"productCodes\":\"" + code + "\"}");
//            return productGroupService.getList(channelId, queryObject);
//        }

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

//        /**
//         * calculatePriceByFormula 根据公式计算价格
//         *
//         * @param feedSkuInfo CmsBtFeedInfoModel_Sku Feed的SKU信息
//         * @param formula     String   计算公式
//         * @param channelId   String   渠道id
//         * @param feedCategory String  feed类目(有些店铺的价格计算公式时基于类目的)
//         * @param priceRoundUpFlg String 价格是否向上取整flg(1:向上取整 0:不向上取整)
//         * @return 计算后价格
//         */
//        private Double calculatePriceByFormula(CmsBtFeedInfoModel_Sku feedSkuInfo, String formula, String channelId,
//                                               String feedCategory, String priceRoundUpFlg) {
//
//            String originalFomula = formula;
//            Double priceClientMsrp = feedSkuInfo.getPriceClientMsrp();
//            Double priceClientRetail = feedSkuInfo.getPriceClientRetail();
//            Double priceNet = feedSkuInfo.getPriceNet();
//            Double priceMsrp = feedSkuInfo.getPriceMsrp();
//            Double priceCurrent = feedSkuInfo.getPriceCurrent();
//
//            if (StringUtils.isEmpty(formula)) {
//                // update by desmond 2016/07/19 start
////                return 0.00;
//                // 如果传入的价格计算公式为空，则中止feed导入，抛出异常
//                String errMsg = "";
//                if (isCategoryFormula(channelId)) {
//                    // 基于类目的计算公式为空时，抛出异常之后继续后面的feed导入
//                    errMsg = String.format("feed->master导入:异常终止:在cms_mt_channel_config表中该feed类目配置的价格计算公式为空 " +
//                            "( channel:[%s] feedCategory:[%s] formula:[null] )", channelId, feedCategory);
//                } else {
//                    // 不基于类目的计算公式为空时，该店铺所有商品都不能继续做了，中止后面的feed导入
//                    errMsg = String.format("feed->master导入:异常终止:在cms_mt_channel_config表配置的价格计算公式错误 " +
//                            "( channel:[%s] formula:[null] )", channelId);
//                }
//                $error(errMsg);
//                throw new BusinessException(errMsg);
//                // update by desmond 2016/07/19 end
//            }
//
//            // 根据公式计算价格
//            try {
//                // 价格说明：
//                // priceClientMsrp:美金专柜价
//                // priceClientRetail:美金指导价
//                // priceNet:美金成本价
//                // priceMsrp:人民币专柜价
//                // priceCurrent:人民币指导价
//                ExpressionParser parser = new SpelExpressionParser();
//                formula = formula.replaceAll("\\[priceClientMsrp\\]", String.valueOf(priceClientMsrp))
//                        .replaceAll("\\[priceClientRetail\\]", String.valueOf(priceClientRetail))
//                        .replaceAll("\\[priceNet\\]", String.valueOf(priceNet))
//                        .replaceAll("\\[priceMsrp\\]", String.valueOf(priceMsrp))
//                        .replaceAll("\\[priceCurrent\\]", String.valueOf(priceCurrent));
//                double valueDouble = parser.parseExpression(formula).getValue(Double.class);
//                // update by desmond 2016/07/12 start
////                // 四舍五入取整
////                BigDecimal valueBigDecimal = new BigDecimal(String.valueOf(valueDouble)).setScale(0, BigDecimal.ROUND_HALF_UP);
//
//                double value = valueDouble;
//                if ("1".equals(priceRoundUpFlg)) {
//                    // 向上取整(3.01->4.00)
//                    value = new BigDecimal(String.valueOf(valueDouble)).setScale(0, BigDecimal.ROUND_UP).doubleValue();
//                }
//                // update by desmond 2016/07/12 end
//                return value;
//
//            } catch (Exception ex) {
//                // 价格计算公式出错时抛出异常，中止feed导入
////                $error(ex);
////                throw new RuntimeException("Formula Calculate Fail!", ex);
//                String errMsg = "";
//                if (isCategoryFormula(channelId)) {
//                    // 基于类目的计算公式为空时，抛出异常之后继续后面的feed导入
//                    errMsg = String.format("feed->master导入:异常终止:在cms_mt_channel_config表中该feed类目配置的价格计算公式不正确 " +
//                            "( channel:[%s] feedCategory:[%s] formula:[%s] )", channelId, feedCategory, originalFomula);
//                } else {
//                    // 不基于类目的计算公式为空时，该店铺所有商品都不能继续做了，中止后面的feed导入
//                    errMsg = String.format("feed->master导入:异常终止:在cms_mt_channel_config表配置的价格计算公式错误 " +
//                            "( channel:[%s] formula: [%s] )", channelId, originalFomula);
//                }
//                $error(errMsg);
//                throw new BusinessException(errMsg);
//            }
//        }

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
//            CmsBtImagesModel param = new CmsBtImagesModel();
//            param.setChannelId(channelId);
//            param.setCode(code);
//            param.setOriginalUrl(originalUrl);
//            List<CmsBtImagesModel> findImage = cmsBtImageDaoExt.selectImages(param);
            CmsBtImagesModel findImage = imagesService.getImageIsExists(channelId, code, originalUrl);

            // 不存在则插入
            if (findImage == null) {
                // 图片名最后一部分的值（索引）
                int index = 1;

                // 检查该code是否存在该Image（为了取得图片名最后一部分中的索引的最大值）
                CmsBtImagesModel param = new CmsBtImagesModel();
                param.setChannelId(channelId);
                param.setCode(code);
                List<CmsBtImagesModel> oldImages = cmsBtImageDaoExt.selectImages(param);
                if (oldImages.size() > 0) {
                    // 取得图片名最后一部分中的索引的最大值 + 1
                    try {
                        index = oldImages.stream().map((imagesModel) -> imagesModel.getImgName().lastIndexOf("-") > 0 && StringUtils.isDigit(imagesModel.getImgName().substring(imagesModel.getImgName().lastIndexOf("-") + 1, imagesModel.getImgName().length()))
                                ? Integer.parseInt(imagesModel.getImgName().substring(imagesModel.getImgName().lastIndexOf("-") + 1, imagesModel.getImgName().length())) : 0).max(Integer::compare).get() + 1;
                    } catch (Exception ex) {
                        $error(ex);
                        throw new RuntimeException("ImageName Parse Fail!", ex);
                    }
                }

//                CmsBtImagesModel newModel = new CmsBtImagesModel(channelId, code, originalUrl, index++, getTaskName());
                CmsBtImagesModel newModel = new CmsBtImagesModel();
                newModel.setChannelId(channelId);
                newModel.setOriginalUrl(originalUrl);
                newModel.setCode(code);
                newModel.setUpdFlg(0);
                newModel.setCreater(getTaskName());
                newModel.setModifier(getTaskName());
                String URL_FORMAT = "[~@.' '#+$%&*_'':/‘’^\\()]";
                Pattern special_symbol = Pattern.compile(URL_FORMAT);
                newModel.setImgName(channelId + "-" + special_symbol.matcher(code).replaceAll(Constants.EmptyString) + "-" + index);
                imagesService.insert(newModel);

                return newModel.getImgName();
            } else {

                // 如果原始图片的地址发生变更则做更新操作
                if (!originalUrl.equals(findImage.getOriginalUrl())) {
                    findImage.setOriginalUrl(originalUrl);
                    findImage.setUpdFlg(0);
                    findImage.setModifier(getTaskName());
                    imagesService.update(findImage);
                }

                return findImage.getImgName();
            }
        }

        /**
         * getProductPriceBeanBefore 生成更新前的价格履历Bean
         *
         * @param cmsProduct      商品Model
         * @param blnProductExist true：更新；false：新建
         * @return 更新前的价格履历Bean
         */
        // delete desmond 2016/07/01 start
        // TODO 价格履历功能没有做，这个方法没人调用，以后改成sku.getPriceSale()要改成分平台下读取
//        private ProductPriceBean getProductPriceBeanBefore(CmsBtProductModel cmsProduct, boolean blnProductExist) {
//
//            ProductPriceBean productPriceBeanBefore = new ProductPriceBean();
//            for (CmsBtProductModel_Sku sku : cmsProduct.getCommon().getSkus()) {
//                ProductSkuPriceBean productSkuPriceBean = new ProductSkuPriceBean();
//                if (!blnProductExist) {
//                    productSkuPriceBean.setClientMsrpPrice(null);
//                    productSkuPriceBean.setClientRetailPrice(null);
//                    productSkuPriceBean.setClientNetPrice(null);
//                    productSkuPriceBean.setPriceMsrp(null);
//                    productSkuPriceBean.setPriceRetail(null);
//                    productSkuPriceBean.setPriceSale(null);
//                } else {
//                    productSkuPriceBean.setClientMsrpPrice(sku.getClientMsrpPrice());
//                    productSkuPriceBean.setClientRetailPrice(sku.getClientRetailPrice());
//                    productSkuPriceBean.setClientNetPrice(sku.getClientNetPrice());
//                    productSkuPriceBean.setPriceMsrp(sku.getPriceMsrp());
//                    productSkuPriceBean.setPriceRetail(sku.getPriceRetail());
//                    productSkuPriceBean.setPriceSale(sku.getPriceSale());
//                }
//                productSkuPriceBean.setSkuCode(sku.getSkuCode());
//                productPriceBeanBefore.addSkuPrice(productSkuPriceBean);
//
//            }
//
//            productPriceBeanBefore.setProductCode(cmsProduct.getCommon().getFields().getCode());
//            productPriceBeanBefore.setProductId(cmsProduct.getProdId());
//
//            return productPriceBeanBefore;
//        }
        // delete desmond 2016/07/01 end

        /**
         * getIsMasterMain 是否是Main商品
         *
         * @param feed 品牌方提供的数据
         * @return 1:isMasterMain;0:isNotMasterMain
         */
        private int getIsMasterMain(CmsBtFeedInfoModel feed) {
            long cnt = productService.getCnt(feed.getChannelId(),
                    String.format("{\"common.fields.model\":\"%s\", \"common.fields.isMasterMain\":1}", feed.getModel()));
            if (cnt < 1) {
                return 1;
            }
            return 0;
        }

        // delete by desmond 2016/07/06 start
        // 跟小林确认过了，这个表以后不用更新了
//        /**
//         * 将新建的件数，更新的件数插到cms_bt_data_amount表
//         */
//        private void insertDataAmount() {
//
//            // 新建的件数
//            if (insertCnt > 0) {
//                dataAmountService.updateWithInsert(channel.getOrder_channel_id(), CmsConstants.DataAmount.FEED_TO_MASTER_INSERT, String.valueOf(insertCnt), "Feed导入Master新建", getTaskName());
//            }
//
//            // 新建的件数
//            if (updateCnt > 0) {
//                dataAmountService.updateWithInsert(channel.getOrder_channel_id(), CmsConstants.DataAmount.FEED_TO_MASTER_UPDATE, String.valueOf(updateCnt), "Feed导入Master更新", getTaskName());
//            }
//        }
        // delete by desmond 2016/07/06 end

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
                            $info(String.format("feed->master导入:找不到feed的这个属性 ( channel: [%s], code: [%s], attr: [%s] )", feed.getChannelId(), feed.getCode(), mappingCondition.getVal()));
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
         * doSetPrice 调用共通函数设置product各平台的sku的价格
         *
         * @param channelId  channel id
         * @param feed       feed信息
         * @param cmsProduct cms product信息
         * @return CmsBtProductModel 以后计算价格直接用ProductModel
         */
        // jeff 2016/04 change start
        // private void doSetPrice(String channelId, CmsBtFeedInfoModel feed, CmsBtProductModel cmsProduct) {
//        private CmsBtProductModel doSetPrice(String channelId, CmsBtFeedInfoModel feed, CmsBtProductModel cmsProduct) {
        private CmsBtProductModel doSetPrice(String channelId, CmsBtFeedInfoModel feed, CmsBtProductModel cmsProduct) {

            List<CmsBtProductModel_Sku> commonSkuList = cmsProduct.getCommon().getSkus();

            // 设置common.skus里面的价格
            for (CmsBtFeedInfoModel_Sku sku : feed.getSkus()) {
                CmsBtProductModel_Sku commonSku = null;
                if (ListUtils.notNull(commonSkuList)) {
                    for (CmsBtProductModel_Sku commonSkuTemp : commonSkuList) {
                        if (sku.getSku().equals(commonSkuTemp.getSkuCode())) {
                            commonSku = commonSkuTemp;
                            break;
                        }
                    }
                }

                if (commonSku != null) {
                    // 美金专柜价
                    commonSku.setClientMsrpPrice(sku.getPriceClientMsrp());
                    // 美金指导价
                    commonSku.setClientRetailPrice(sku.getPriceClientRetail());
                    // 美金成本价(=priceClientCost)
                    commonSku.setClientNetPrice(sku.getPriceNet());
                    // 人民币专柜价(后面价格计算要用到，因为010,018等店铺不用新价格体系，还是用老的价格公式)
                    commonSku.setPriceMsrp(sku.getPriceMsrp());
                    // 人民币指导价(后面价格计算要用到，因为010,018等店铺不用新价格体系，还是用老的价格公式)
                    commonSku.setPriceRetail(sku.getPriceCurrent());
                }

            }

            // 设置platform.PXX.skus里面的价格
            try {
                priceService.setPrice(cmsProduct, false);
            } catch (IllegalPriceConfigException ie) {
                // 渠道级别价格计算配置错误, 停止后面的feed->master导入，避免报几百条一样的错误信息
                String errMsg = String.format("feed->master导入:共通配置异常终止:发现渠道级别的价格计算配置错误，后面的feed导入不做了，" +
                        "请修改好相应配置项目后重新导入 [ErrMsg=%s]", ie.getMessage());
                $error(errMsg);
                throw new CommonConfigNotFoundException(errMsg);
            } catch (Exception ex) {
                String errMsg = String.format("feed->master导入:异常终止:调用共通函数计算产品价格时出错 [ErrMsg= ", channelId, feed.getCode());
                if(StringUtils.isNullOrBlank2(ex.getMessage())) {
                    errMsg += ex.getStackTrace()[0].toString() + "]";
                } else {
                    errMsg += ex.getMessage() + "]";
                }
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

//            Map<String, CmsBtProductModel_Platform_Cart> platforms = cmsProduct.getPlatforms();
//            List<CmsBtProductModel_Sku> commonSkus = cmsProduct.getCommon().getSkus();
//
//            // jeff 2016/04 change end
//            // 查看配置表, 看看是否要自动审批价格
//            CmsChannelConfigBean autoApprovePrice = CmsChannelConfigs.getConfigBeanNoCode(channelId
//                    , CmsConstants.ChannelConfig.AUTO_APPROVE_PRICE);
//            boolean blnAutoApproveFlg = false;
//            if (autoApprovePrice != null && autoApprovePrice.getConfigValue1() != null && "1".equals(autoApprovePrice.getConfigValue1())) {
//                // 自动审批价格配置成1的场合，自动审批价格
//                blnAutoApproveFlg = true;
//            }
////            if (autoApprovePrice == null || autoApprovePrice.getConfigValue1() == null || "0".equals(autoApprovePrice.getConfigValue1())) {
////                // 没有配置过, 或者 配置为空, 或者 配置了0 的场合
////                // 认为不自动审批价格 (注意: 即使不自动审批, 但如果价格击穿了, 仍然自动更新salePrice)
////                blnAutoApproveFlg = false;
////            } else {
////                // 其他的场合, 自动审批价格
////                blnAutoApproveFlg = true;
////            }
//
//            // delete desmond 2016/07/01 start
////            ProductPriceBean model = new ProductPriceBean();
////            ProductSkuPriceBean skuPriceModel;
////
////            model.setProductId(cmsProduct.getProdId());
////
////            // jeff 2016/04 add start
////            model.setProductCode(cmsProduct.getCommon().getFields().getCode());
//            // delete desmond 2016/07/01 end
//
//            // 店铺级别MSRP价格计算公式
//            String priceMsrpCalcFormula = "";
//            // update by desmond 2016/07/19 start
////            CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.PRICE_MSRP_CALC_FORMULA);
//            CmsChannelConfigBean cmsChannelConfigBean = null;
//            // 有3个渠道('020','025','026')要根据类目税率不一样，要根据类目(config_code字段)取得价格计算公式
//            if (isCategoryFormula(channelId)) {
//                cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.PRICE_MSRP_CALC_FORMULA, feed.getCategory());
//
//                // 如果没有取到feed类目对应的价格计算公式，则中止feed导入，抛出异常
//                if (cmsChannelConfigBean == null || StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
//                    throwNotFoundPriceFormulaException(channelId, feed.getCategory(), CmsConstants.ChannelConfig.PRICE_MSRP_CALC_FORMULA, feed.getModel());
//                }
//            } else {
//                cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.PRICE_MSRP_CALC_FORMULA);
//
//                // 如果没有取到channel对应的价格计算公式，则中止feed导入，抛出异常
//                if (cmsChannelConfigBean == null || StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
//                    throwNotFoundPriceFormulaException(channelId, null, CmsConstants.ChannelConfig.PRICE_MSRP_CALC_FORMULA, feed.getModel());
//                }
//            }
//            // update by desmond 2016/07/19 end
//            if (cmsChannelConfigBean != null && !StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
//                priceMsrpCalcFormula = cmsChannelConfigBean.getConfigValue1();
//            }
//
//            // 店铺级别指导价格计算公式
//            String priceRetailCalcFormula = "";
//            // update by desmond 2016/07/19 start
////            cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.PRICE_RETAIL_CALC_FORMULA);
//            cmsChannelConfigBean = null;
//            // 有3个渠道('020','025','026')要根据类目税率不一样，要根据类目(config_code字段)取得价格计算公式
//            if (isCategoryFormula(channelId)) {
//                cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.PRICE_RETAIL_CALC_FORMULA, feed.getCategory());
//
//                // 如果没有取到feed类目对应的价格计算公式，则中止feed导入，抛出异常
//                if (cmsChannelConfigBean == null || StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
//                    throwNotFoundPriceFormulaException(channelId, feed.getCategory(), CmsConstants.ChannelConfig.PRICE_RETAIL_CALC_FORMULA, feed.getModel());
//                }
//
//            } else {
//                cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.PRICE_RETAIL_CALC_FORMULA);
//
//                // 如果没有取到channel对应的价格计算公式，则中止feed导入，抛出异常
//                if (cmsChannelConfigBean == null || StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
//                    throwNotFoundPriceFormulaException(channelId, null, CmsConstants.ChannelConfig.PRICE_RETAIL_CALC_FORMULA, feed.getModel());
//                }
//            }
//            // update by desmond 2016/07/19 end
//            if (cmsChannelConfigBean != null && !StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
//                priceRetailCalcFormula = cmsChannelConfigBean.getConfigValue1();
//            }
//
//            // 价格自动同步间隔天数
////            String day = "0";
////            cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId
////                    , CmsConstants.ChannelConfig.AUTO_SYN_DAY);
////            if (cmsChannelConfigBean != null && !StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
////                // 如果没有设定则相当于间隔天数为0
////                day = cmsChannelConfigBean.getConfigValue1();
////            }
//
//            // delete desmond 2016/07/07 start
////            // 强制击穿阈值
////            String threshold = "";
////            cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId
////                    , CmsConstants.ChannelConfig.MANDATORY_BREAK_THRESHOLD);
////            if (cmsChannelConfigBean != null && !StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
////                threshold = cmsChannelConfigBean.getConfigValue1();
////            }
//            // delete desmond 2016/07/07 end
//            // 如果强制击穿阈值没有设定的话，那么只要指导价高于原来最终售价就击穿
////            if (StringUtils.isEmpty(threshold)) {
////                threshold = "0";
////            }
//            // 是否同步
////            boolean synFlg = true;
////            try {
////                synFlg = DateTimeUtil.addDays(DateTimeUtil.parse(this.priceBreakTime), Integer.parseInt(day)).before(DateTimeUtil.getDate());
////            } catch (Exception ex) {
////            }
//            // jeff 2016/04 add end
//
//            for (CmsBtFeedInfoModel_Sku sku : feed.getSkus()) {
//                CmsBtProductModel_Sku commonSku = null;
//                if (commonSkus != null) {
//                    for (CmsBtProductModel_Sku commonSkuTemp : commonSkus) {
//                        if (sku.getSku().equals(commonSkuTemp.getSkuCode())) {
//                            commonSku = commonSkuTemp;
//                            break;
//                        }
//                    }
//                }
//                // delete desmond 2016/07/01 start
//                // product.skus被删除了
////                skuPriceModel = new ProductSkuPriceBean();
////
////                skuPriceModel.setSkuCode(sku.getSku());
////                // jeff 2016/04 change start
////                // skuPriceModel.setPriceMsrp(sku.getPrice_msrp());
////                // skuPriceModel.setPriceRetail(sku.getPrice_current());
////                skuPriceModel.setPriceMsrp(calculatePriceByFormula(sku, priceMsrpCalcFormula));
////                skuPriceModel.setPriceRetail(calculatePriceByFormula(sku, priceRetailCalcFormula));
////                // jeff 2016/04 change end
////
////                skuPriceModel.setClientMsrpPrice(sku.getPriceClientMsrp());
////                skuPriceModel.setClientRetailPrice(sku.getPriceClientRetail());
////                skuPriceModel.setClientNetPrice(sku.getPriceNet());
//                // delete desmond 2016/07/01 end
//
//                if (commonSku != null) {
//                    // add by desmond 2016/08/04 start
//                    // 从cms_mt_channel_config表从取得该channel的价格是否要向上取整flg
//                    String priceRoundUpFlg = "1";  // 默认为向上取整
//                    CmsChannelConfigBean sizeTypeChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(this.channel.getOrder_channel_id(),
//                            CmsConstants.ChannelConfig.PRICE_ROUND_UP_FLG);
//                    if (sizeTypeChannelConfigBean != null && "0".equals(sizeTypeChannelConfigBean.getConfigValue1())) {
//                        priceRoundUpFlg = "0";          // 0:不向上取整
//                    }
//                    // add by desmond 2016/08/04 start
//                    commonSku.setPriceMsrp(calculatePriceByFormula(sku, priceMsrpCalcFormula, channelId, feed.getCategory(), priceRoundUpFlg));
//                    commonSku.setPriceRetail(calculatePriceByFormula(sku, priceRetailCalcFormula, channelId, feed.getCategory(), priceRoundUpFlg));
//                    commonSku.setClientMsrpPrice(sku.getPriceClientMsrp());
//                    commonSku.setClientRetailPrice(sku.getPriceClientRetail());
//                    commonSku.setClientNetPrice(sku.getPriceNet());
//                    for (Map.Entry<String, CmsBtProductModel_Platform_Cart> entry : platforms.entrySet()) {
//                        CmsBtProductModel_Platform_Cart platform = entry.getValue();
//                        // add desmond 2016/07/05 start
//                        // P0（主数据）平台不用设置sku
//                        if (platform == null || platform.getCartId() < CmsConstants.ACTIVE_CARTID_MIN) {
//                            continue;
//                        }
//                        // add desmond 2016/07/05 end
//                        List<BaseMongoMap<String, Object>> platformSkus = platform.getSkus();
//                        if (platformSkus != null && platformSkus.size() > 0) {
//                            for (BaseMongoMap<String, Object> platformSku : platformSkus) {
//                                // 找到platform下面的sku
//                                if (sku.getSku().equals(platformSku.get(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()))) {
//                                    // 设定平台的Msrp
//                                    platformSku.put(CmsBtProductConstants.Platform_SKU_COM.priceMsrp.name(), commonSku.getPriceMsrp());
//
//                                    // update desmond 2016/07/07 start
//                                    // 设定平台的RetailPrice(销售指导价)
//                                    if (platformSku.get(CmsBtProductConstants.Platform_SKU_COM.priceRetail.name()) != null) {
//                                        Double oldRetailPrice = Double.parseDouble(String.valueOf(platformSku.get(CmsBtProductConstants.Platform_SKU_COM.priceRetail.name())));
//                                        platformSku.put(CmsBtProductConstants.Platform_SKU_COM.priceRetail.name(), commonSku.getPriceRetail());
//                                        Double newRetailPrice = Double.parseDouble(String.valueOf(platformSku.get(CmsBtProductConstants.Platform_SKU_COM.priceRetail.name())));
//
//                                        // 设置priceChgFlg(指导售价变化状态（U/D） 这里是指导售价价格本身变化,与priceSale无关)
//                                        if (oldRetailPrice < newRetailPrice) {
//                                            // 指导售价升高的时候
//                                            if (oldRetailPrice == 0.00) {
//                                                platformSku.put(CmsBtProductConstants.Platform_SKU_COM.priceChgFlg.name(), "U100%");
//                                            } else {
//                                                platformSku.put(CmsBtProductConstants.Platform_SKU_COM.priceChgFlg.name(), "U" + Math.round(((newRetailPrice - oldRetailPrice) / oldRetailPrice) * 100) + "%");
//                                            }
//                                        } else if (oldRetailPrice > newRetailPrice) {
//                                            // 指导售价降低的时候
//                                            platformSku.put(CmsBtProductConstants.Platform_SKU_COM.priceChgFlg.name(), "D" + Math.round(((oldRetailPrice - newRetailPrice) / oldRetailPrice) * 100) + "%");
//                                        } else {
//                                            // 指导售价不变的时候
//                                            platformSku.put(CmsBtProductConstants.Platform_SKU_COM.priceChgFlg.name(), "");
//                                        }
//                                    } else {
//                                        // 平台销售指导价为null的情况下属于新建
//                                        platformSku.put(CmsBtProductConstants.Platform_SKU_COM.priceRetail.name(), commonSku.getPriceRetail());
//                                        platformSku.put(CmsBtProductConstants.Platform_SKU_COM.priceChgFlg.name(), "");
//                                    }
//
//                                    // 设定平台的最终价格(priceChgFlg与priceSale没有关系了，所以下面代码删除)
//                                    if (platformSku.get(CmsBtProductConstants.Platform_SKU_COM.priceSale.name()) != null) {
////                                        Double newPrice = Double.parseDouble(String.valueOf(platformSku.get("priceRetail")));
////                                        Double oldPrice = Double.parseDouble(String.valueOf(platformSku.get("priceSale")));
//
//                                        // 是否自动同步最终售价
//                                        if (blnAutoApproveFlg) {
//                                            platformSku.put(CmsBtProductConstants.Platform_SKU_COM.priceSale.name(), commonSku.getPriceRetail());
////                                            platformSku.put("priceChgFlg", "");
//                                        } //else {
////                                            // 不设置最终售价
////                                            // 指导价高于原来最终售价的阈值(例：10%)时，强制击穿
////                                            if (!StringUtils.isEmpty(threshold) && StringUtils.isDigit(threshold)) {
////                                                if (newPrice > oldPrice * (1.0 + Double.parseDouble(threshold) / 100.0)) {
////                                                    if (oldPrice == 0.0) {
////                                                        platformSku.put("priceChgFlg", "XU100");
////                                                    } else {
////                                                        platformSku.put("priceChgFlg", "XU" + Math.round(((newPrice / oldPrice) - 1) * 100));
////                                                    }
////                                                } else if (newPrice <= oldPrice * (1.0 + Double.parseDouble(threshold) / 100.0) && newPrice > oldPrice) {
////                                                    platformSku.put("priceChgFlg", "U" + Math.round(((newPrice / oldPrice) - 1) * 100));
////                                                } else if (oldPrice * (1.0 - Double.parseDouble(threshold) / 100.0) > newPrice) {
////                                                    if (newPrice == 0.0) {
////                                                        platformSku.put("priceChgFlg", "XD100");
////                                                    } else {
////                                                        platformSku.put("priceChgFlg", "XD" + Math.round(((oldPrice / newPrice) - 1) * 100));
////                                                    }
////                                                } else if (oldPrice * (1.0 - Double.parseDouble(threshold) / 100.0) <= newPrice && oldPrice > newPrice) {
////                                                    platformSku.put("priceChgFlg", "D" + Math.round(((oldPrice / newPrice) - 1) * 100));
////                                                }
////                                            } else {
////                                                if (newPrice > oldPrice) {
////                                                    if (oldPrice == 0.0) {
////                                                        platformSku.put("priceChgFlg", "U100");
////                                                    } else {
////                                                        platformSku.put("priceChgFlg", "U" + Math.round(((newPrice / oldPrice) - 1) * 100));
////                                                    }
////                                                } else if (newPrice < oldPrice) {
////                                                    if (newPrice == 0.0) {
////                                                        platformSku.put("priceChgFlg", "D100");
////                                                    } else {
////                                                        platformSku.put("priceChgFlg", "D" + Math.round(((oldPrice / newPrice) - 1) * 100));
////                                                    }
////                                                }
////                                            }
////                                        }
//
//                                    } else {
//                                        // 平台最终价格为null的情况下属于新建，那么设定最终价格 = RetailPrice
//                                        platformSku.put(CmsBtProductConstants.Platform_SKU_COM.priceSale.name(), commonSku.getPriceRetail());
////                                        platformSku.put("priceChgFlg", "");
//                                    }
//                                    // update desmond 2016/07/07 end
//                                }
//                                // add by desmond 2016/07/05 start
//                                // 设置最终售价变化状态,这里表示最终售价(priceSale)与销售指导价(priceRetail)的比较结果
//                                // （比指导价低:2，比指导价高:3，等于指导价:1，向上击穿警告:4，向下击穿警告:5）
//                                String priceDiffFlg = productSkuService.getPriceDiffFlg(channelId, platformSku);
//                                platformSku.put(CmsBtProductConstants.Platform_SKU_COM.priceDiffFlg.name(), priceDiffFlg);
//                                // add by desmond 2016/07/05 end
//                            }
//                        }
//                    }
//                }
//            }
                // delete by desmond 2016/07/01 start
//                // product.fields,product.skus被删除了，改成product.common.fields,product.common.skus
//                // jeff 2016/04 change start
//                // 如果是新的SKU, 那么: PriceRetail -> priceSale
//                if (cmsProduct.getCommon().getSku(sku.getSku()) == null) {
//                    skuPriceModel.setPriceSale(skuPriceModel.getPriceRetail());
//                    skuPriceModel.setPriceChgFlg("");
//                } else if (cmsProduct.getCommon().getSku(sku.getSku()).getPriceSale() == null || cmsProduct.getCommon().getSku(sku.getSku()).getPriceSale() == 0d) {
//                    skuPriceModel.setPriceSale(skuPriceModel.getPriceRetail());
//                    skuPriceModel.setPriceChgFlg("");
//                } else {
//                    // 之前有价格的场合, 判断是否需要把价格更新掉
//
//                    // 旧sku
//                    CmsBtProductModel_Sku oldSku = cmsProduct.getCommon().getSku(sku.getSku());
//
//                    // 旧价格取得
//                    Double oldPriceSale = oldSku.getPriceSale();
//
//                    // 新的的指导价
//                    Double newPriceSale = skuPriceModel.getPriceRetail();
//
//                    // 是否自动同步最终售价
//                    if (blnAutoApproveFlg) {
//                        if (newPriceSale > oldPriceSale) {
//                            // skuPriceModel.setPriceChgFlg("U" + (newPriceSale - oldPriceSale));
//                            skuPriceModel.setPriceSale(newPriceSale);
//                        } else if (newPriceSale < oldPriceSale) {
//                            // skuPriceModel.setPriceChgFlg("D" + Math.abs(newPriceSale - oldPriceSale));
//                            skuPriceModel.setPriceSale(newPriceSale);
//                        }
//                    } else {
//
//                        // 同步的场合
//                        if (!StringUtils.isEmpty(threshold) && StringUtils.isDigit(threshold)) {
//                            // 指导价高于原来最终售价的阈值(例：10%)时，强制击穿
//                            if (newPriceSale > oldPriceSale * (1.0 + Double.parseDouble(threshold) / 100.0)) {
//                                // skuPriceModel.setPriceChgFlg("X" + (newPriceSale - oldPriceSale));
//                                skuPriceModel.setPriceSale(newPriceSale);
//                            } else {
//                                // 为了之后计算PriceSaleSt和PriceSaleEd，也需要赋上旧值
//                                skuPriceModel.setPriceSale(oldSku.getPriceSale());
//                            }
//                        } else {
//                            // 为了之后计算PriceSaleSt和PriceSaleEd，也需要赋上旧值
//                            skuPriceModel.setPriceSale(oldSku.getPriceSale());
//                        }
//                    }
//                }
//                // jeff 2016/04 change end
//
//                model.addSkuPrice(skuPriceModel);
//            }
//
//            List<ProductPriceBean> productPrices = new ArrayList<>();
//            productPrices.add(model);
//
//            productSkuService.updatePrices(channelId, productPrices, getTaskName());
            // delete by desmond 2016/07/01 end
            // 不需要自己同步价格区间，后面设置价格变更履历时会自动发MQ消息同步价格区间的
//            productSkuService.updatePricesNew(channelId, cmsProduct, getTaskName());
            // jeff 2016/04 add start
            // 如果发生价格强制击穿的话，更新价格强制击穿时间
//            if (synFlg) {
//                TaskControlBean param = new TaskControlBean();
//                param.setTask_id(getTaskName());
//                param.setCfg_name(TaskControlEnums.Name.order_channel_id.toString());
//                param.setCfg_val1(channelId);
//
//                // 价格强制击穿时间
//                if (StringUtils.isEmpty(this.priceBreakTime)) {
//                    param.setEnd_time(DateTimeUtil.getNow());
//                } else {
//                    param.setEnd_time(DateTimeUtil.format(DateTimeUtil.addDays(DateTimeUtil.parse(this.priceBreakTime), Integer.parseInt(day)), null));
//                }
//                taskDao.updateTaskControl(param);
//            }

            return cmsProduct;
            // jeff 2016/04 add end
        }

        /**
         * doSaveItemDetails 保存item details的数据
         *
         * @param channelId channel id
         * @param productId product id
         * @param feed      feed信息
         */
        private boolean doSaveItemDetails(String channelId, Long productId, CmsBtFeedInfoModel feed) {

            // 如果feed里,没有sku的数据的话,那么就不需要做下去了
            if (feed.getSkus() == null || feed.getSkus().size() == 0) {
                // 也认为是正常
                return true;
            }

            // 根据product id, 获取现有的item details表的数据
//            List<String> skuList = new ArrayList<>();
//            List<ItemDetailsBean> itemDetailsBeanList = itemDetailsDao.selectByCode(channelId, feed.getCode());
//            for (ItemDetailsBean itemDetailsBean : itemDetailsBeanList) {
//                skuList.add(itemDetailsBean.getSku());
//            }

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

                try {
                    // 判断这个sku是否已经存在
                    //if (skuList.contains(feedSku.getSku())) {
                    ItemDetailsBean oldRecord = itemDetailsDao.selectBySku(channelId, feedSku.getSku());
                    if (oldRecord != null) {
                        // 如果该skuCode没变，但feed里面的code从A->B了，则报出异常, feedCode一致才更新
                        if (!oldRecord.getItemcode().equals(feed.getCode())) {
                            String errMsg = String.format("feed->master导入:异常终止:由于该sku所属的feedCode发生了变更," +
                                    "导致不能更新wms_bt_item_details表 [sku:%s] [OldFeedCode:%s] [NewFeedCode:%s]",
                                    itemDetailsBean.getSku(),
                                    oldRecord.getItemcode(),
                                    feed.getCode()
                            );
                            $error(errMsg);
                            throw new BusinessException(errMsg);
                        }
                        // 已经存在的场合: 更新数据库
                        itemDetailsDao.updateItemDetails(itemDetailsBean, getTaskName());
                    } else {
                        // 不存在的场合: 插入数据库
                        itemDetailsDao.insertItemDetails(itemDetailsBean, getTaskName());

//                        // 添加到判断列表中
//                        skuList.add(feedSku.getSku());
                    }
                } catch (Exception e) {
                    // update desmond 2016/07/06 start
                    String errMsg = String.format("feed->master导入:异常终止:无法插入或更新wms_bt_item_details表( channel: [%s], sku: [%s], itemcode: [%s], barcode: [%s], size: [%s]  )",
                            channelId,
                            itemDetailsBean.getSku(),
                            itemDetailsBean.getItemcode(),
                            itemDetailsBean.getBarcode(),
                            itemDetailsBean.getSize()
                    );
                    $error(errMsg);
                    throw new BusinessException(errMsg);
//                    logIssue(getTaskName(),
//                            String.format("[CMS2.0]无法插入或更新item detail表( channel: [%s], sku: [%s], itemcode: [%s], barcode: [%s], size: [%s]  )",
//                                    channelId,
//                                    itemDetailsBean.getSku(),
//                                    itemDetailsBean.getItemcode(),
//                                    itemDetailsBean.getBarcode(),
//                                    itemDetailsBean.getSize()
//                            ));
//                    return false;
                    // update desmond 2016/07/06 end
                }

            }

            return true;

        }

        // delete by desmond 2016/07/08 start
//        /**
//         * 进行一些字符串或数字的特殊编辑
//         *
//         * @param inputValue 输入的字符串
//         * @param edit       目前支持的是 "in2cm" 英寸转厘米
//         * @param prefix     前缀
//         * @param suffix     后缀
//         * @return
//         */
//        private String doEditSkuTemplate(String inputValue, String edit, String prefix, String suffix) {
//            String value = inputValue;
//
//            // 根据edit进行变换
//            if (!StringUtils.isEmpty(edit)) {
//                if ("in2cm".equals(edit)) {
//                    // 奇怪的数据转换
//                    // 有时候别人提供的数字中会有类似于这样的数据：
//                    // 33 3/4 意思是 33又四分之三 -> 33.75
//                    // 33 1/2 意思是 33又二分之一 -> 33.5
//                    // 33 1/4 意思是 33又四分之一 -> 33.25
//                    // 33 5/8 意思是 33又八分之五 -> 33.625
//                    // 直接这边代码处理掉避免人工干预
//                    if (value.contains(" ")) {
//                        value = value.replaceAll(" +", " ");
//                        String[] strSplit = value.split(" ");
//
//                        String[] strSplitSub = strSplit[1].split("/");
//                        value = String.valueOf(
//                                Float.valueOf(strSplit[0]) +
//                                        (Float.valueOf(strSplitSub[0]) / Float.valueOf(strSplitSub[1]))
//                        );
//
//                    }
//
//                    // 英寸转厘米
//                    value = String.valueOf(Float.valueOf(value) * 2.54);
//
//                    DecimalFormat df = new DecimalFormat("0.00");
//                    value = df.format(Float.valueOf(value));
//
//                }
//            }
//
//            // 设置前缀
//            if (!StringUtils.isEmpty(prefix)) {
//                value = prefix + value;
//            }
//
//            // 设置后缀
//            if (!StringUtils.isEmpty(suffix)) {
//                value = value + suffix;
//            }
//
//            return value;
//
//        }
        // delete by desmond 2016/07/08 end


    }

    private void insertCmsBtFeedImportSize(String channelId, CmsBtProductModel cmsProduct) {
        CmsBtFeedImportSizeModel cmsBtFeedImportSizeModel = new CmsBtFeedImportSizeModel();
        cmsBtFeedImportSizeModel.setChannelId(channelId);
        cmsBtFeedImportSizeModel.setBrandName(cmsProduct.getCommon().getFields().getBrand());
        cmsBtFeedImportSizeModel.setProductType(cmsProduct.getCommon().getFields().getProductType());
        cmsBtFeedImportSizeModel.setSizeType(cmsProduct.getCommon().getFields().getSizeType());
        cmsProduct.getCommon().getSkus().forEach(sku -> {
            cmsBtFeedImportSizeModel.setOriginalSize(sku.getSize());
            cmsBtFeedImportSizeService.saveCmsBtFeedImportSizeModel(cmsBtFeedImportSizeModel, getTaskName());
        });
    }

    private void copyAttributeFromMainProduct(String channelId, CmsBtProductModel_Common common, String mainProductCode) {
        CmsBtProductModel mainProduct = productService.getProductByCode(channelId, mainProductCode);
        if(mainProduct != null){
            common.setCatId(mainProduct.getCommon().getCatId());
            common.setCatPath(mainProduct.getCommon().getCatPath());

            common.getFields().setTranslateStatus(mainProduct.getCommon().getFields().getTranslateStatus());
            common.getFields().setTranslateTime(mainProduct.getCommon().getFields().getTranslateTime());
            common.getFields().setTranslator(mainProduct.getCommon().getFields().getTranslator());

            common.getFields().setHsCodePrivate(mainProduct.getCommon().getFields().getHsCodePrivate());
            common.getFields().setHsCodeSetter(mainProduct.getCommon().getFields().getHsCodeSetter());
            common.getFields().setHsCodeStatus(mainProduct.getCommon().getFields().getHsCodeStatus());
            common.getFields().setHsCodeSetTime(mainProduct.getCommon().getFields().getHsCodeSetTime());
            common.getFields().setHsCodeCrop(mainProduct.getCommon().getFields().getHsCodeCrop());
            common.getFields().setHsCodeCross(mainProduct.getCommon().getFields().getHsCodeCross());

            common.getFields().setCategorySetTime(mainProduct.getCommon().getFields().getCategorySetTime());
            common.getFields().setCategoryStatus(mainProduct.getCommon().getFields().getCategoryStatus());
            common.getFields().setCategorySetter(mainProduct.getCommon().getFields().getCategorySetter());

            common.getFields().setShortDesCn(mainProduct.getCommon().getFields().getShortDesCn());
            common.getFields().setLongDesCn(mainProduct.getCommon().getFields().getLongDesCn());
            common.getFields().setOriginalTitleCn(mainProduct.getCommon().getFields().getOriginalTitleCn());
            common.getFields().setMaterialCn(mainProduct.getCommon().getFields().getMaterialCn());
            common.getFields().setUsageCn(mainProduct.getCommon().getFields().getUsageCn());
        }
    }

    /**
     * 出错的时候将错误信息回写到cms_bt_business_log表
     *
     * @param channelId String 渠道id
     * @param cartId String 平台id
     * @param feedModel String feed model
     * @param feedProductCode String feed产品code
     * @param errCode String 错误code
     * @param errMsg String 错误消息
     * @param modifier String 更新者
     */
    private void insertBusinessLog(String channelId, String cartId, String feedModel, String feedProductCode, String errCode, String errMsg, String modifier) {
        CmsBtBusinessLogModel businessLogModel = new CmsBtBusinessLogModel();
        // 渠道id
        businessLogModel.setChannelId(channelId);
        // 平台id
        if (!StringUtils.isEmpty(cartId)) businessLogModel.setCartId(Integer.parseInt(cartId));
        // feedModel
        businessLogModel.setModel(feedModel);
        // feedProduCode
        businessLogModel.setCode(feedProductCode);
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
     * feed->master导入成功或者失败之后，回写cms_bt_feed_info_cxxx表
     *
     * @param feed CmsBtFeedInfoModel feed信息
     * @param updFlg Integer 更新flg(1:feed->master导入失败   2:feed->master导入失败)
     * @param errMsg String 失败的错误消息
     * @param isFeedReImport String 是否强制重新导入
     */
    private void updateFeedInfo(CmsBtFeedInfoModel feed, Integer updFlg, String errMsg, String isFeedReImport) {
        if (feed == null) return;

        feed.setUpdFlg(updFlg);  // 1:feed->master导入失败   2:feed->master导入失败
        feed.setUpdMessage(errMsg);
        if (!StringUtils.isEmpty(isFeedReImport))
            feed.setIsFeedReImport(isFeedReImport);
        feed.setModifier(getTaskName());
        feedInfoService.updateFeedInfo(feed);
    }

    /**
     * 设置产品各个平台的店铺内分类信息
     *
     * 新增或更新产品时，如果字典中解析出来子分类id在产品该平台的店铺内信息中不存在，则新加一条店铺类分类
     * 存在的话不更新，因为店铺内信息运营有可能已经修改过了
     *
     * 从cms_mt_channel_condition_config表中取得该店铺对应的店铺内分类数据字典，
     * 表里的platformPropId字段的值为：
     *   "seller_cids_"+cartId  例：seller_cats_26
     * 店铺内分类字典的里面的value值结构如下：
     *   结构："cId(子分类id)|cIds(父分类id,子分类id)|cName(父分类id,子分类id)|cNames(父分类id,子分类id)"
     *   例子："1124130584|1124130579,1124130584|系列>彩色宝石|系列,彩色宝石"
     *
     * @param feed CmsBtFeedInfoModel feed信息
     * @param product CmsBtProductModel 产品信息(兼结果返回用)
     */
    public void setSellerCats(CmsBtFeedInfoModel feed, CmsBtProductModel product) {

        if (feed == null || product == null) {
            $warn("feed->master导入:警告:设置店铺内分类时传入的feed或者product信息为null");
            return;
        }

        // 遍历主数据product里的分平台信息，设置店铺内分类
        for (Map.Entry<String, CmsBtProductModel_Platform_Cart> entry : product.getPlatforms().entrySet()) {
            // P0（主数据）平台跳过
            if (entry.getValue().getCartId() < CmsConstants.ACTIVE_CARTID_MIN) {
                continue;
            }

            List<CmsBtProductModel_SellerCat> sellerCatList = entry.getValue().getSellerCats();
            if (sellerCatList == null) {
                // 如果该平台上没有店铺内分类项目，则新建一个店铺内分类列表
                sellerCatList = new ArrayList<CmsBtProductModel_SellerCat>();
                entry.getValue().setSellerCats(sellerCatList);
            }

            // 条件表达式表platform_prop_id字段的检索条件为"seller_cids_"加cartId
            String platformPropId = "seller_cids_" + entry.getValue().getCartId();

            // 构造解析店铺内分类字典必须的一些对象
            SxData sxData = new SxData();
            sxData.setMainProduct(product);                  // product信息
            sxData.setCmsBtFeedInfoModel(feed);              // feed信息
            sxData.setChannelId(product.getChannelId());     // channelId
            sxData.setCartId(entry.getValue().getCartId());  // cartId
            // 构造字典解析子
            ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

            // 根据channelid和platformPropId取得cms_mt_channel_condition_config表的条件表达式
            List<ConditionPropValueModel> conditionPropValueModels = conditionPropValueRepo.get(product.getChannelId(), platformPropId);
            if (ListUtils.isNull(conditionPropValueModels))
                continue;

            // 解析cms_mt_channel_condition_config表中取得该平台对应店铺内分类数据字典
            RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
            for (ConditionPropValueModel conditionPropValueModel : conditionPropValueModels) {
                String conditionExpressionStr = conditionPropValueModel.getCondition_expression();
                RuleExpression conditionExpression;
                String propValue;

                try {
                    // 带名字字典解析
                    if (conditionExpressionStr.startsWith("{\"type\":\"DICT\"")) {
                        DictWord conditionDictWord = (DictWord)ruleJsonMapper.deserializeRuleWord(conditionExpressionStr);
                        conditionExpression = conditionDictWord.getExpression();
                    } else if (conditionExpressionStr.startsWith("{\"ruleWordList\"")) {
                        // 不带名字，只有字典表达式字典解析
                        conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
                    } else {
                        logIssue(getTaskName(), String.format("feed->master导入:警告:店铺内分类数据字典格式不对 [ChannelId:%s]" +
                                " [CartId:%s])", product.getChannelId(), entry.getValue().getCartId()));
                        continue;
                    }

                    // 店铺内分类字典的值（"cId(子分类id)|cIds(父分类id,子分类id)|cName(父分类id,子分类id)|cNames(父分类id,子分类id)"）
                    // 例："1124130584|1124130579,1124130584|系列>彩色宝石|系列,彩色宝石"
                    propValue = expressionParser.parse(conditionExpression, null, getTaskName(), null);
                } catch (Exception e) {
                    // 因为店铺内分类即使这里不设置，运营也可以手动设置的，所以这里如果解析字典异常时，不算feed->master导入失败
                    logIssue(getTaskName(), String.format("feed->master导入:警告:解析店铺内分类数据字典出错 [ChannelId:%s]" +
                            " [CartId:%s])", product.getChannelId(), entry.getValue().getCartId()));
                    continue;
                }

                // 如果从字典里面取得店铺内分类为空
                if (StringUtils.isEmpty(propValue))
                    continue;

                List<String> sellerCatsList = Arrays.asList(propValue.split("\\|"));
                // 如果取得的店铺内分类列表为空，继续循环
                if(ListUtils.isNull(sellerCatsList))
                    continue;

                // cId(子分类id)
                String strCId = sellerCatsList.get(0);
                boolean isSellerCatExists = false;
                for (CmsBtProductModel_SellerCat sellerCat : sellerCatList) {
                    if (sellerCat.getcId().equalsIgnoreCase(strCId)) {
                        // 如果在产品已经存在该子分类id的店铺内分类信息
                        isSellerCatExists = true;
                        break;
                    }
                }
                // 新增或更新产品时，如果字典中解析出来子分类id在产品该平台的店铺内信息中不存在，则新加一条店铺类分类
                // 存在的话不更新，因为店铺内信息运营有可能已经修改过了
                if (isSellerCatExists)
                    continue;

                // 不存在时新加一条店铺内分类信息
                CmsBtProductModel_SellerCat sellerCatFromDict = new CmsBtProductModel_SellerCat();
                // cId(子分类id)           例："1124130584"
                sellerCatFromDict.setcId(sellerCatsList.get(0));
                // cIds(父分类id,子分类id)  例："1124130579,1124130584"
                if (sellerCatsList.size() > 1) {
                    List<String> cidsList = Arrays.asList(sellerCatsList.get(1).split(","));
                    if (ListUtils.notNull(cidsList)) {
                        sellerCatFromDict.setcIds(cidsList);
                    }
                }
                // cName(父分类id,子分类id)  例："系列>彩色宝石"
                if (sellerCatsList.size() > 2) {
                    sellerCatFromDict.setcName(sellerCatsList.get(2));
                }
                // cNames(父分类id,子分类id) 例："系列,彩色宝石"
                if (sellerCatsList.size() > 3) {
                    List<String> cNamesList = Arrays.asList(sellerCatsList.get(3).split(","));
                    if (ListUtils.notNull(cNamesList)) {
                        sellerCatFromDict.setcNames(cNamesList);
                    }
                }

                sellerCatList.add(sellerCatFromDict);
            }
        }
    }

}
