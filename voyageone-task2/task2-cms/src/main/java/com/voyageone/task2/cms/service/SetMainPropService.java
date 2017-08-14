package com.voyageone.task2.cms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.base.exception.CommonConfigNotFoundException;
import com.voyageone.category.match.FeedQuery;
import com.voyageone.category.match.MatchResult;
import com.voyageone.category.match.Searcher;
import com.voyageone.category.match.Tokenizer;
import com.voyageone.category.match.data.MtCategoryKeysDao;
import com.voyageone.category.match.data.MtCategoryKeysModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.ImageServer;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.logger.VOAbsIssueLoggable;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.*;
import com.voyageone.ims.rule_expression.DictWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
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
import com.voyageone.service.impl.cms.prices.PlatformPriceService;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductTopService;
import com.voyageone.service.impl.cms.promotion.PromotionCodeService;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.impl.cms.tools.common.CmsMasterBrandMappingService;
import com.voyageone.service.impl.cms.usa.UsaNewArrivalService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.WmsCreateOrUpdateProductMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsCategoryReceiveMQMessageBody;
import com.voyageone.service.impl.com.ComMtValueChannelService;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.model.cms.CmsBtBusinessLogModel;
import com.voyageone.service.model.cms.CmsBtFeedImportSizeModel;
import com.voyageone.service.model.cms.CmsBtImagesModel;
import com.voyageone.service.model.cms.mongo.CmsBtCustomPropModel;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.cms.mongo.feed.mapping2.CmsBtFeedMapping2Model;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.task2.cms.dao.TmpOldCmsDataDao;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
import com.voyageone.task2.cms.service.putaway.ConditionPropValueRepo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
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
public class SetMainPropService extends VOAbsIssueLoggable {

    // 每个channel的feed->master导入默认最大件数
    private final static int FEED_IMPORT_MAX_500 = 500;
    @Autowired
    CategoryTreeService categoryTreeService;
    @Autowired
    CmsMasterBrandMappingService cmsMasterBrandMappingService;
    @Autowired
    PromotionService promotionService;
    @Autowired
    PromotionCodeService promotionCodeService;
    @Autowired
    private CmsBtFeedMapping2Dao cmsBtFeedMapping2Dao; // DAO: 新的feed->主数据的mapping关系
    @Autowired
    private CmsBtProductDao cmsBtProductDao; // DAO: 商品的值
    @Autowired
    private MongoSequenceService commSequenceMongoService; // DAO: Sequence
    @Autowired
    private ComMtValueChannelService comMtValueChannelService;    // 更新Synship.com_mt_value_channel表
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
    private
    CategoryTreeAllService categoryTreeAllService;
    @Autowired
    private BusinessLogService businessLogService;
    @Autowired
    private ImagesService imagesService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private ConditionPropValueRepo conditionPropValueRepo;
    @Autowired
    private CmsBtFeedImportSizeService cmsBtFeedImportSizeService;
    @Autowired
    private
    CmsBtBrandBlockService cmsBtBrandBlockService;
    @Autowired
    private PlatformPriceService platformPriceService;
    @Autowired
    private CmsBtTranslateService cmsBtTranslateService;
    @Autowired
    private Searcher searcher;
    @Autowired
    private ImageTemplateService imageTemplateService;
    @Autowired
    private CmsMqSenderService cmsMqSenderService;
    @Autowired
    private MqSender sender;

    @Autowired
    MtCategoryKeysDao mtCategoryKeysDao;

    @Autowired
    SellerCatService sellerCatService;

    @Autowired
    UsaNewArrivalService usaNewArrivalService;

    @Autowired
    PlatformProductUploadService platformProductUploadService;

    @Autowired
    ProductTopService productTopService;


    /**
     * 按渠道进行设置
     */
    public class setMainProp {
        Boolean usjoi;
        String taskName;
        int insertCnt = 0;
        int updateCnt = 0;
        int errCnt = 0;
        int currentIndex = 0;
        int feedListCnt = 0;
        long startTime;
        // --------------------------------------------------------------------------------------------
        // synship.com_mt_value_channel表共通信息
        // 品牌mapping表
        Map<String, String> mapBrandMapping = new HashMap<>();
        // 产品分类mapping表
        Map<String, String> mapProductTypeMapping = new HashMap<>();
        // 适用人群mapping表
        Map<String, String> mapSizeTypeMapping = new HashMap<>();
        // --------------------------------------------------------------------------------------------
        // voyageone_cms2.cms_mt_channel_config表中的共通配置信息
        // 自动同步对象平台列表(ALL:所有平台，也可具体指定需要同步的平台id,用逗号分隔(如:"28,29"))
        String ccAutoSyncCarts = "";
        List<String> ccAutoSyncCartList = null;

        List<String> ccUsAutoSyncCartList = null;
        // 是否从feed导入产品分类
        String productTypeFromFeedFlg = "0";    // 0：不从feed导入运营手动添加产品分类
        // 是否从feed导入适用人群
        String sizeTypeFromFeedFlg = "0";       // 0：不从feed导入运营手动添加适用人群
        // 是否设置PC端自拍商品图images6
        String autoSetImages6Flg = "0";         // 0: 不设置PC端自拍商品图images6
        // APP端启用开关
        String appSwitchFlg = "0";              // 0: 不启动APP端
        // Feed导入Master时，在Product更新的情况下，是否更新Feed节点下面的数据
        String feedUpdateFlg = "0";             // 0: 不更新Feed节点下面的数据
        // 该channel每次feed->master导入的最大件数(最大2000件,默认为500件)
        int feedImportMax = FEED_IMPORT_MAX_500;
        // 是否只修改价格(1:只修改价格 空,0:修改全部属性)
        String onlyUpdatePriceFlg = "0";           // 0: 更新全部属性
        // 处理名("feed->master导入"或者"feed->master导入:只修改价格")
        String strProcName = "feed->master导入";
        // --------------------------------------------------------------------------------------------
        // 允许approve这个sku到平台上去售卖渠道cart列表
        List<TypeChannelBean> typeChannelBeanListApprove = null;

        List<TypeChannelBean> typeUsChannelBeanListApprove = null;

        // 允许展示的cart列表
        List<TypeChannelBean> typeChannelBeanListDisplay = null;
        // 主类目黑名单
        List<String> categoryWhite = new ArrayList<>();
        // 价格阈值 超过该值的商品不能导入主数据
        Double priceThreshold = null;
        // 重量阈值 超过该值的商品不能导入主数据
        Double weightThreshold = null;
        // 拆分规则 0：不拆分 1：按主类目拆分 2：无条件拆分
        String singleFlg = "0";
        // group设置规则 0：按model设置group 1：按code设置group
        String singleGroupFlg = "0";
        // feed-》mast 主类目是否同步  0：不同步 1：无条件同步
        String categoryFlg = "0";
        //
        String copyMainProductFreeTagsFlg = "0";
        // 需要拆分的主类目
        List<String> categorySingle = new ArrayList<>();
        Map<String, String> mastBrand = null;
        private OrderChannelBean channel;
        private boolean skip_mapping_check;
        private Map<String, List<ConditionPropValueModel>> channelConditionConfig;
        // jeff 2016/04 change start
//        // 前一次的价格强制击穿时间
//        private String priceBreakTime;
        private int m_mulitComplex_index = 0; // 暂时只支持一层multiComplex, 如果需要多层, 就需要改成list, 先进后出
        // jeff 2016/04 change end
        private boolean m_mulitComplex_run = false; // 暂时只支持一层multiComplex, 如果需要多层, 就需要改成list, 先进后出

        public setMainProp(String channelId, boolean usjoi, boolean skip_mapping_check) {
            this.channel = Channels.getChannel(channelId);
            this.skip_mapping_check = skip_mapping_check;
            this.mastBrand = new HashMap<>();
            this.usjoi = usjoi;
//            this.priceBreakTime = priceBreakTime;
        }


        // --------------------------------------------------------------------------------------------

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        /**
         * 初始化该店铺一些共通配置的属性
         */
        public void doInit(String channelId) {


            // -----------------------------------------------------------------------------
            // 从synship.com_mt_value_channel表中取得品牌，产品分类，使用人群等mapping信息
            // 品牌mapping作成
            List<TypeChannelBean> brandTypeChannelBeanList = TypeChannels.getTypeList(Constants.comMtTypeChannel.BRAND_41,
                    usjoi ? "928" : channelId);
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
            if (!usjoi) {
                // 产品分类mapping作成

                List<TypeChannelBean> productTypeChannelBeanList = TypeChannels.getTypeList(Constants.comMtTypeChannel.PROUDCT_TYPE_57,
                        channelId);
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
                        channelId);
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

            // 从synship.com_mt_value_channel表中获取当前channel, 有多少个允许approve这个sku到平台上去售卖渠道cartId
            typeChannelBeanListApprove = TypeChannels.getTypeListSkuCarts(usjoi ? "928" : channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en"); // 取得允许Approve的数据
            if (ListUtils.isNull(typeChannelBeanListApprove)) {
                String errMsg = String.format("feed->master导入:共通配置异常终止:在com_mt_value_channel表中没有找到当前Channel允许售卖的Cart信息(用于生成product分平台信息) [ChannelId=%s A en]", channelId);
                $error(errMsg);
                throw new CommonConfigNotFoundException(errMsg);
            }
            typeUsChannelBeanListApprove = TypeChannels.getTypeListSkuCarts(usjoi ? "928" : channelId, Constants.comMtTypeChannel.SKU_CARTS_53_O, "en"); // 取得允许Approve的数据
            if (ListUtils.isNull(typeChannelBeanListApprove)) {
                String errMsg = String.format("feed->master导入:共通配置异常终止:在com_mt_value_channel表中没有找到当前Channel允许售卖的Cart信息(用于生成product分平台信息) [ChannelId=%s A en]", channelId);
                $error(errMsg);
                throw new CommonConfigNotFoundException(errMsg);
            }

            // 从synship.com_mt_value_channel表中获取当前channel, 有多少个需要展示的cart
            typeChannelBeanListDisplay = TypeChannels.getTypeListSkuCarts(usjoi ? "928" : channelId, Constants.comMtTypeChannel.SKU_CARTS_53_D, "en"); // 取得展示用数据
            if (ListUtils.isNull(typeChannelBeanListDisplay)) {
                String errMsg = String.format("feed->master导入:共通配置异常终止:在com_mt_value_channel表中没有找到当前Channel需要展示的Cart信息(用于生成productGroup信息) [ChannelId=%s D en]", channelId);
                $error(errMsg);
                throw new CommonConfigNotFoundException(errMsg);
            }
            // -----------------------------------------------------------------------------

            // -----------------------------------------------------------------------------
            // 从voyageone_cms2.cms_mt_channel_config表中取得该店铺的共通配置信息
            // 自动上新插入workload表用
            // 自动同步对象平台列表(ALL:所有平台，也可具体指定需要同步的平台id,用逗号分隔(如:"28,29"))
            CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(usjoi ? "928" : channelId,
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

            CmsChannelConfigBean cmsUsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(usjoi ? "928" : channelId,
                    CmsConstants.ChannelConfig.AUTO_SYNC_US_CARTS);
            if (cmsUsChannelConfigBean != null && !StringUtils.isEmpty(cmsUsChannelConfigBean.getConfigValue1())) {
                String strAutoSyncCarts = cmsUsChannelConfigBean.getConfigValue1().trim();
                // 取得自动同步指定平台列表
                ccUsAutoSyncCartList = Arrays.asList(strAutoSyncCarts.split(","));
            }
            // 从cms_mt_channel_config表从取得产品分类是否从feed导入flg,默认为0：不从feed导入运营手动添加
            CmsChannelConfigBean productTypeChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(usjoi ? "928" : channelId,
                    CmsConstants.ChannelConfig.PRODUCT_TYPE_FROM_FEED_FLG);
            if (productTypeChannelConfigBean != null && "1".equals(productTypeChannelConfigBean.getConfigValue1())) {
                productTypeFromFeedFlg = "1";       // 1:从feed导入产品分类
            }

            // 从cms_mt_channel_config表从取得适用人群是否从feed导入flg,默认为0：不从feed导入运营手动添加
            CmsChannelConfigBean sizeTypeChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(usjoi ? "928" : channelId,
                    CmsConstants.ChannelConfig.SIZE_TYPE_FROM_FEED_FLG);
            if (sizeTypeChannelConfigBean != null && "1".equals(sizeTypeChannelConfigBean.getConfigValue1())) {
                sizeTypeFromFeedFlg = "1";          // 1:从feed导入适用人群
            }

            // 从cms_mt_channel_config表从取得新建product时是否自动设置PC端自拍商品图images6(1:自动设置  空，0:不设置)
            CmsChannelConfigBean image6ChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId,
                    CmsConstants.ChannelConfig.AUTO_SET_IMAGES6_FLG);
            if (image6ChannelConfigBean != null && "1".equals(image6ChannelConfigBean.getConfigValue1())) {
                autoSetImages6Flg = "1";            // 1:自动设置PC端自拍商品图images6
            }

            // 从cms_mt_channel_config取得APP端启用开关的值（0或者1）
            CmsChannelConfigBean appSwitchChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId,
                    CmsConstants.ChannelConfig.APP_SWITCH);
            if (appSwitchChannelConfigBean != null && "1".equals(appSwitchChannelConfigBean.getConfigValue1())) {
                // APP端启用开关的值配置成1的场合，设为APP端启用开1
                appSwitchFlg = "1";
            }

            // Feed导入Master时，在Product更新的情况下，是否更新Feed节点下面的数据
            CmsChannelConfigBean feedUpdateChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId,
                    CmsConstants.ChannelConfig.FEED_UPDATE_FLG);
            if (feedUpdateChannelConfigBean != null && "1".equals(feedUpdateChannelConfigBean.getConfigValue1())) {
                feedUpdateFlg = "1";
            }

            // 该店铺每次feed-master导入最大件数(FEED_IMPORT_MAX)(最大2000件)
            CmsChannelConfigBean feedImportMaxChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId,
                    CmsConstants.ChannelConfig.FEED_IMPORT_MAX);
            if (feedImportMaxChannelConfigBean != null && !StringUtils.isEmpty(feedImportMaxChannelConfigBean.getConfigValue1())) {
                if (NumberUtils.toInt(feedImportMaxChannelConfigBean.getConfigValue1()) >= 2000) {
                    feedImportMax = 2000;
                } else if (NumberUtils.toInt(feedImportMaxChannelConfigBean.getConfigValue1()) > 0) {
                    feedImportMax = NumberUtils.toInt(feedImportMaxChannelConfigBean.getConfigValue1());
                }
            }

            // 是否只修改价格(1:只修改价格 空,0:修改全部属性)
            CmsChannelConfigBean onlyUpdatePriceChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId,
                    CmsConstants.ChannelConfig.ONLY_UPDATE_PRICE_FLG);
            if (onlyUpdatePriceChannelConfigBean != null && "1".equals(onlyUpdatePriceChannelConfigBean.getConfigValue1())) {
                onlyUpdatePriceFlg = "1";
                strProcName = "feed->master导入:只修改价格";
            }
            // -----------------------------------------------------------------------------

            //
            if (usjoi) {
                // 主类目不导入的黑名单
                List<CmsChannelConfigBean> categoryWhitelist = CmsChannelConfigs.getConfigBeans("000",
                        CmsConstants.ChannelConfig.CATEGORY_WHITE, "0");

                if (!ListUtils.isNull(categoryWhitelist) && !StringUtil.isEmpty(categoryWhitelist.get(0).getConfigValue1())) {
                    categoryWhite = Arrays.asList(categoryWhitelist.get(0).getConfigValue1().split(";"));
                }
            }

            CmsChannelConfigBean feedMastThreshold = CmsChannelConfigs.getConfigBeanNoCode(usjoi ? "928" : channelId,
                    CmsConstants.ChannelConfig.FEED_MAST_THRESHOLD);

            if (feedMastThreshold != null) {
                if (!StringUtil.isEmpty(feedMastThreshold.getConfigValue1())) {
                    priceThreshold = Double.parseDouble(feedMastThreshold.getConfigValue1());
                }
                if (!StringUtil.isEmpty(feedMastThreshold.getConfigValue2())) {
                    weightThreshold = Double.parseDouble(feedMastThreshold.getConfigValue2());
                }
            }

            CmsChannelConfigBean feedMastConfig = CmsChannelConfigs.getConfigBeanNoCode(usjoi ? "928" : channelId,
                    CmsConstants.ChannelConfig.FEED_MAST_CONFIG);
            if (feedMastConfig != null) {
                singleFlg = StringUtil.isEmpty(feedMastConfig.getConfigValue1()) ? "0" : feedMastConfig.getConfigValue1();
                singleGroupFlg = StringUtil.isEmpty(feedMastConfig.getConfigValue2()) ? "0" : feedMastConfig.getConfigValue2();
                categoryFlg = StringUtil.isEmpty(feedMastConfig.getConfigValue3()) ? "0" : feedMastConfig.getConfigValue3();
            }
            List<CmsChannelConfigBean> categorySingleConfig = CmsChannelConfigs.getConfigBeans("000",
                    CmsConstants.ChannelConfig.CATEGORY_SINGLE, "0");
            if (!ListUtils.isNull(categorySingleConfig) && !StringUtil.isEmpty(categorySingleConfig.get(0).getConfigValue1())) {
                categorySingle = Arrays.asList(categorySingleConfig.get(0).getConfigValue1().split(";"));
            }

            channelConditionConfig = conditionPropValueRepo.getAllByChannelId(channelId);

            // 从cms_mt_channel_config查询COPY_MAIN_PRODUCT_FREE_TAGS设置，如果config_value1为1则复制主商品FreeTags
            CmsChannelConfigBean channelConfig = CmsChannelConfigs.getConfigBeanNoCode(usjoi ? "928" : channelId, CmsConstants.ChannelConfig.COPY_MAIN_PRODUCT_FREE_TAGS);
            if (channelConfig != null && "1".equals(channelConfig.getConfigValue1())) {
                copyMainProductFreeTagsFlg = "1";
            }

        }

        private void showChannelErrorResult(String channelId, Map<String, String> resultMap) {
            // 如果是共通配置没有或者价格计算时抛出整个Channel的配置没有的错误时，后面的feed导入就不用做了，免得报出几百条同样的错误
            String resultInfo = channelId + " " + String.format("%1$-15s", channel.getFull_name()) + " 产品导入结果 [渠道级别共通配置属性错误,该渠道下" +
                    "所有的feed都不做导入了，请修改好后重新导入]";
            // 将该channel的feed->master导入信息加入map，供channel导入线程全部完成一起显示
            resultMap.put(channelId, resultInfo);
            $info(channel.getOrder_channel_id() + " " + channel.getFull_name() + " 产品导入主数据异常结束");
        }

        /**
         * feed->master导入主处理
         *
         * @param resultMap 用于保存每个channel最终导入结果信息
         */
        public void doRun(Map<String, String> resultMap, Map<String, String> needReloadMap) {
            $info(channel.getOrder_channel_id() + " " + channel.getFull_name() + " 产品导入主数据开始");
            String channelId = this.channel.getOrder_channel_id();

            try {
                // 渠道级别共通属性初始化,如果出现异常则终止全部的feed->master导入
                doInit(channelId);
            } catch (CommonConfigNotFoundException ce) {
                // 如果是共通配置没有或者价格计算时抛出整个Channel的配置没有的错误时，后面的feed导入就不用做了，免得报出几百条同样的错误
                showChannelErrorResult(channelId, resultMap);
                return;
            } catch (Exception e) {
                String errMsg = "feed->master导入:取得共通配置信息时异常终止:";
                if (StringUtils.isNullOrBlank2(e.getMessage())) {
                    errMsg += "导入时出现不可预知的错误，请跟管理员联系 [ErrMsg=" + e.getStackTrace()[0].toString() + "]";
                    $error(errMsg);
                    e.printStackTrace();
                } else {
                    errMsg = e.getMessage();
                }
                $error(errMsg);
                showChannelErrorResult(channelId, resultMap);
                return;
            }

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
            queryObject.setLimit(feedImportMax);   // 默认为每次最大500件
            List<CmsBtFeedInfoModel> feedList = feedInfoService.getList(channelId, queryObject);

            // 共通配置信息存在的时候才进行feed->master导入
            if (ListUtils.notNull(feedList)) {

                // 取得feed->master导入之前的品牌，产品分类，使用人群等mapping件数，用于判断是否新增之后需要清空缓存
                int oldBrandCnt = mapBrandMapping.size();
                int oldProductTypeCnt = mapProductTypeMapping.size();
                int oldSizeTypeCnt = mapSizeTypeMapping.size();
                int newBrandCnt, newProductTypeCnt, newSizeTypeCnt;

                // jeff 2016/05 add start
                // 取得所有主类目
                // update desmond 2016/07/04 start
                //            List<CmsMtCategoryTreeModel> categoryTreeList = categoryTreeService.getMasterCategory();
//                List<CmsMtCategoryTreeAllModel> categoryTreeAllList = categoryTreeAllService.getMasterCategory();
                // update desmond 2016/07/04 end
                // jeff 2016/05 add end

                // 当前channel需要导入的feed总件数
                feedListCnt = feedList.size();
                HashMap<String, Boolean> mapFeedBrand = new HashMap<>();
                // 遍历所有数据
                for (CmsBtFeedInfoModel feed : feedList) {
                    startTime = System.currentTimeMillis();
                    currentIndex++;
                    // 将商品从feed导入主数据
                    // 注意: 保存单条数据到主数据的时候, 由于要生成group数据, group数据的生成需要检索数据库进行一系列判断
                    //       所以单个渠道的数据, 最好不要使用多线程, 如果以后一定要加多线程的话, 注意要自己写带锁的代码.
                    // update by desmond 2016/07/05 start
                    // 增加try catch捕捉feed导入时出现的异常并新增失败时回写处理等,feed导入共通处理里面出错时改为抛出异常
                    try {
                        feed.setFullAttribute();
                        if (isBlocked(feed.getChannelId(), feed.getBrand(), mapFeedBrand)) {
                            // feed里面的品牌已被加入黑名单,导入失败
                            String errMsg = String.format(strProcName + ":feed里面的品牌已被加入黑名单,不能导入:" +
                                    "[ChannelId:%s] [FeedCode:%s]", feed.getChannelId(), feed.getCode());
                            $error(errMsg);
                            throw new BusinessException(errMsg);
                        }

                        List<CmsBtFeedInfoModel_Sku> skus = feed.getSkus().stream()
                                .filter(sku -> sku.getPriceNet() != null && sku.getPriceNet().compareTo(0D) > 0 && !StringUtil.isEmpty(sku.getBarcode()))
                                .collect(Collectors.toList());
                        if (ListUtils.isNull(skus)) {
                            // feed里面的品牌已被加入黑名单,导入失败
                            String errMsg = String.format(strProcName + ":feed里面的sku的价格成本价为0或UPC为空" +
                                    "[ChannelId:%s] [FeedCode:%s]", feed.getChannelId(), feed.getCode());
                            $error(errMsg);
                            throw new BusinessException(errMsg);
                        }
                        feed.setSkus(skus);
                        // feed->master导入主处理
                        doSaveProductMainProp(feed, channelId);
                        for (CmsBtFeedInfoModel_Sku cmsBtFeedInfoModel_Sku : feed.getSkus()) {
                            if (!StringUtils.isEmpty(cmsBtFeedInfoModel_Sku.getMainVid())) {
                                HttpHeaders httpHeaders = new HttpHeaders();
                                httpHeaders.setContentType(MediaType.parseMediaType("application/json;charset=UTF-8"));
                                ObjectMapper objectMapper = new ObjectMapper();
                                HashMap<String, Object> feedInfo = new HashMap<>();
                                feedInfo.put("orderChannelId", channelId);
                                feedInfo.put("clientSku", cmsBtFeedInfoModel_Sku.getClientSku());
                                feedInfo.put("mainClientSku", cmsBtFeedInfoModel_Sku.getMainVid());
                                List<HashMap<String, Object>> requestList = Arrays.asList(feedInfo);
                                String json = objectMapper.writeValueAsString(requestList);
                                httpHeaders.set("Authorization", "Basic " + MD5.getMD5(json + System.currentTimeMillis() / TimeUnit.MINUTES.toMillis(30)));
                                HttpEntity<String> httpEntity = new HttpEntity<>(json, httpHeaders);
                                SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
                                simpleClientHttpRequestFactory.setConnectTimeout(6000);
                                simpleClientHttpRequestFactory.setReadTimeout(6000);
                                RestTemplate restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
                                ResponseEntity<String> exchange = restTemplate.exchange("http://open.synship.net/wms/prdouctMapping/import", HttpMethod.POST, httpEntity, String.class);
                            }
                        }
                    } catch (CommonConfigNotFoundException ce) {
                        errCnt++;
                        // 如果是共通配置没有或者价格计算时抛出整个Channel的配置没有的错误时，后面的feed导入就不用做了，免得报出几百条同样的错误
                        String errMsg = strProcName + ":共通配置异常终止:";
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

                        $info("feed->master导入:共通配置异常终止:[ChannelId:%s] [%d/%d] [FeedCode:%s] [耗时:%s]",
                                channelId, currentIndex, feedListCnt, feed.getCode(), (System.currentTimeMillis() - startTime));

                        // 跳出循环,后面的feed->master导入不做了，等这个channel共通错误改好了之后再做导入
                        break;
                    } catch (Exception e) {
                        errCnt++;
                        String errMsg = strProcName + ":异常终止:";
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

                        $info("feed->master导入:异常终止:[ChannelId:%s] [%d/%d] [FeedCode:%s] [耗时:%s]",
                                channelId, currentIndex, feedListCnt, feed.getCode(), (System.currentTimeMillis() - startTime));
                    }
                    // update by desmond 2016/07/05 end

                    $debug("feed->master导入计时结束 [ChannelId=%s] [FeedCode=%s] [耗时:%s]", channelId, feed.getCode(),
                            (System.currentTimeMillis() - startTime));
                }

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

            // jeff 2016/04 add start
            // 将新建的件数，更新的件数插到cms_bt_data_amount表
//            insertDataAmount();         // delete desmond 2016/07/04 以后不用更新这个表了
            // jeff 2016/04 add end
            // add by desmond 2016/07/05 start
            //            $info(channel.getFull_name() + "产品导入结果 [总件数:" + feedList.size()
//                    + " 新增成功:" + insertCnt + " 更新成功:" + updateCnt + " 失败:" + errCnt + "]");

            String resultInfo = channelId + " " + String.format("%1$-15s", channel.getFull_name()) + " " + strProcName + ":结果 [总件数:" + feedList.size()
                    + " 新增成功:" + insertCnt + " 更新成功:" + updateCnt + " 失败:" + errCnt + "]";
//            $info(resultInfo);
            // 将该channel的feed->master导入信息加入map，供channel导入线程全部完成一起显示
            resultMap.put(channelId, resultInfo);
            // add by desmond 2016/07/05 end
            $info(channel.getOrder_channel_id() + " " + channel.getFull_name() + " 产品导入主数据结束 ");

        }

        /**
         * 黑名单check
         *
         * @param channelId      channel id
         * @param feedBrand      feed品牌
         * @param mapMasterBrand 品牌是否在黑名单状态列表
         * @return boolean        品牌是否在黑名单状态
         */
        public boolean isBlocked(String channelId, String feedBrand, HashMap<String, Boolean> mapMasterBrand) {
            if (!mapMasterBrand.containsKey(feedBrand)) {
                if (cmsBtBrandBlockService.isBlocked(channelId, 1, feedBrand, "", "")) {
                    mapMasterBrand.put(feedBrand, true);
                } else {
                    mapMasterBrand.put(feedBrand, false);
                }
            }
            return mapMasterBrand.get(feedBrand);
        }

        /**
         * 判断是否是需要拆分code
         *
         * @param cmsBtProductModel
         * @param originalFeed
         * @return
         */
        public boolean singleSku(CmsBtProductModel cmsBtProductModel, CmsBtFeedInfoModel originalFeed) {
            if ("0".equals(singleFlg)) {
                return false;
            } else if ("1".equals(singleFlg)) {
                String catPath = "";
                switch (categoryFlg) {
                    case "0":
                        if (cmsBtProductModel != null) {
                            catPath = cmsBtProductModel.getCommonNotNull().getCatPath();
                        } else {
                            catPath = originalFeed.getMainCategoryCn();
                        }
                        break;
                    case "1":
                        catPath = originalFeed.getMainCategoryCn();
                        break;
                    case "2":
                        if (cmsBtProductModel != null) {
                            catPath = cmsBtProductModel.getCommonNotNull().getCatPath();
                            if (StringUtil.isEmpty(catPath)) {
                                catPath = originalFeed.getMainCategoryCn();
                            }
                        } else {
                            catPath = originalFeed.getMainCategoryCn();
                        }
                        break;
                }
                if (StringUtil.isEmpty(catPath)) {
                    MatchResult searchResult = getMainCatInfo(originalFeed.getCategory(),
                            !StringUtils.isEmpty(originalFeed.getProductType()) ? originalFeed.getProductType() : "",
                            !StringUtils.isEmpty(originalFeed.getSizeType()) ? originalFeed.getSizeType() : "",
                            originalFeed.getName(),
                            originalFeed.getBrand());
                    if (searchResult != null)
                        catPath = searchResult.getCnName();

                }
                if (StringUtil.isEmpty(catPath)) return false;
                String finalCatPath = catPath;
                return categorySingle.stream().noneMatch(cat -> finalCatPath.indexOf(cat) == 0);
            } else if ("2".equals(singleFlg)) {
                return true;
            }
            return false;
        }

        /**
         * 将商品从feed导入主数据
         *
         * @param originalFeed Feed信息
         * @param channelId    channel id
         *                     //         * @param mapBrandMapping 品牌mapping一览
         */
        public void doSaveProductMainProp(
                CmsBtFeedInfoModel originalFeed
                , String channelId
        ) {
            // feed类目名称
            String feedCategory = originalFeed.getCategory();

            // [ feed -> main ] 类目属性的匹配关系
            CmsBtFeedMapping2Model newMapping = null;

            // 通过Code查找到的产品
            CmsBtProductModel oldCmsProduct = null;

            // jeff 2016/05 change start
            List<CmsBtFeedInfoModel> feedList = new ArrayList<>();
            List<CmsBtProductModel> cmsProductList = usjoi ? productService.getProductByOriginalCode("928", channelId, originalFeed.getCode()) : productService.getProductByOriginalCode(channelId, originalFeed.getCode());
            // 取得匹配到的类目是否只允许一个Sku
            boolean isSingleSku = false;
            // 如果找不到，也可能是原来的数据没有改。按原来的Code查找方式，看看有没有
            if (cmsProductList.size() == 0) {
                oldCmsProduct = usjoi ? productService.getProductByCode("928", channelId, originalFeed.getCode()) : productService.getProductByCode(channelId, originalFeed.getCode());
            }
            CmsBtProductModel cmsProductParam = null;   // add desmond 2016/07/04
            // 找不到Code
            if (oldCmsProduct == null && cmsProductList.size() == 0) {

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
                }
            }

            if (StringUtil.isEmpty(originalFeed.getMainCategoryCn())) {
                MatchResult matchResult = getMainCatInfo(originalFeed.getCategory(), originalFeed.getProductType(), originalFeed.getSizeType(), originalFeed.getName(), originalFeed.getBrand());
                if (matchResult != null) {
                    originalFeed.setMainCategoryEn(matchResult.getEnName());
                    originalFeed.setMainCategoryCn(matchResult.getCnName());
                }
            }
            isSingleSku = singleSku(cmsProductParam, originalFeed);

            boolean delOriginalFlg = false;
            // 需要拆分
            // 条件1：根据OriginalCode找到多条
            // 条件2：根据OriginalCode找到1条 并且 产品对应的主类目.singleSku == 1：需要拆分 并且 之前的sku = 1 并且 Feed.sku多条)
            // 条件3：根据OriginalCode找到0条 并且 对应的默认主类目.singleSku == 1：需要拆分 并且 Feed.sku多条)
            if (cmsProductList.size() > 1
                    || (cmsProductList.size() == 1 && cmsProductList.get(0).getCommon().getSkus().size() == 1 && isSingleSku && originalFeed.getSkus() != null && originalFeed.getSkus().size() > 1)
                    || (cmsProductList.size() == 0 && isSingleSku && originalFeed.getSkus() != null && originalFeed.getSkus().size() > 1 && oldCmsProduct == null)) {

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
                        } else if (cmsProductList.size() == 0) {
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
                    cmsProduct = usjoi ? productService.getProductSingleSku("928", channelId, feed.getCode(), originalFeed.getCode()) : productService.getProductSingleSku(channelId, feed.getCode(), originalFeed.getCode());
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
                        blnProductExist = cmsProduct != null;
                    }
                }

                // add by desmond 2016/09/07 start
                // 因为价格公式变更了，只想批量更新既存产品的价格，其他的项目都不想更新的时候
                // 如果该feedCode是新的产品，在既存的产品表中不存在，也不追加这个产品
                if ("1".equals(onlyUpdatePriceFlg)) {
                    // 产品存在时，只更新价格相关项目；产品不存在时，什么都不做
                    if (blnProductExist) {
                        if (cmsProduct.getCommon() == null || cmsProduct.getCommon().getFields() == null
                                || ListUtils.isNull(cmsProduct.getCommon().getSkus())
                                || cmsProduct.getPlatforms() == null || cmsProduct.getPlatforms().size() == 0) {
                            // 如果产品表中取得的product一些关键数据为空，则不做价格同步，直接跳出去
                            String errMsg = String.format(strProcName + ":取得产品信息不正确:[ChannelId:%s] " +
                                    "[FeedCode:%s]", originalFeed.getChannelId(), originalFeed.getCode());
                            $error(errMsg);
                            throw new BusinessException(errMsg);
                        }

                        // 更新价格相关项目
                        doSetPrice(feed, cmsProduct);

                        // 更新产品并记录商品价格表动履历，并向Mq发送消息同步sku,code,group价格范围
                        int updCnt = productService.updateProductFeedToMaster(channelId, cmsProduct, getTaskName(), strProcName);
                        if (updCnt == 0) {
                            // 有出错, 跳过
                            String errMsg = strProcName + ":更新:编辑商品的时候排他错误:" + originalFeed.getChannelId() + ":" + originalFeed.getCode();
                            $error(errMsg);
                            throw new BusinessException(errMsg);
                        }

                        // 插入上新表，可用于向USJOI主店同步价格变更
                        // 当该产品未被锁定且已批准的时候，往workload表里面插入一条上新数据，并逻辑清空相应的business_log
                        insertWorkload(cmsProduct, feed);

                        // 更新成功数
                        updateCnt++;

                        // 回写feed表updFlg状态为1:feed->master导入成功
                        updateFeedInfo(originalFeed, 1, "", "0");

                        $info(strProcName + " 更新成功 [ChannelId:%s] [%d/%d] [ProductCode:%s] [耗时:%s]",
                                cmsProduct.getChannelId(), currentIndex, feedListCnt, cmsProduct.getCommon().getFields().getCode(),
                                (System.currentTimeMillis() - startTime));
                    }

                    // 后面的更新不做，直接返回
                    return;
                }
                // add by desmond 2016/09/07 end

                if (blnProductExist) {
                    // 修改商品数据
                    // 一般只改改价格神马的
                    cmsProduct = doUpdateCmsBtProductModel(feed, cmsProduct, newMapping, feedList.size() > 1, originalFeed.getCode());
                    cmsProduct.getFeed().setCatPath(feed.getCategory());
                    if (feed.getChannelId().equalsIgnoreCase(ChannelConfigEnums.Channel.CHAMPION.getId())) {
                        cmsProduct.getCommon().getFields().setProductNameEn(feed.getName());
                        cmsProduct.getCommon().getFields().setOriginalTitleCn(feed.getName());
                        cmsProduct.getCommon().getFields().setLongDesEn(feed.getLongDescription());
                        cmsProduct.getCommon().getFields().setShortDesEn(feed.getShortDescription());
                        cmsProduct.getCommon().getFields().setLongDesCn(feed.getLongDescription());
                        cmsProduct.getCommon().getFields().setShortDesCn(feed.getShortDescription());
                        cmsProduct.getCommon().getFields().setMaterialCn(feed.getMaterial());
                        cmsProduct.getCommon().getFields().setMaterialEn(feed.getMaterial());
                        cmsProduct.getCommon().getFields().setOrigin(feed.getOrigin());
                        cmsProduct.getCommon().getFields().setCodeDiff(feed.getColor());
                        cmsProduct.getCommon().getFields().setBrand(feed.getBrand());
                        if (!StringUtil.isEmpty(feed.getColor())) {
                            cmsProduct.getCommon().getFields().setColor(feed.getColor().split("-")[0]);
                        }
                    }
                    if (feed.getChannelId().equalsIgnoreCase(ChannelConfigEnums.Channel.KitBag.getId())) {
                        cmsProduct.getCommon().getFields().setShortDesEn(feed.getShortDescription());
                        if (StringUtil.isEmpty(cmsProduct.getCommonNotNull().getFieldsNotNull().getShortDesCn())) {
                            cmsProduct.getCommon().getFields().setShortDesCn(feed.getShortDescription());
                        }
                        cmsProduct.getCommon().getFields().setMaterialEn(feed.getMaterial());
                        if (StringUtil.isEmpty(cmsProduct.getCommonNotNull().getFieldsNotNull().getMaterialCn())) {
                            cmsProduct.getCommon().getFields().setMaterialCn(feed.getMaterial());
                        }
                    }
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

                    //james g kg 计算
                    weightCalculate(cmsProduct);

                    // tom 20160510 追加 END

                    // TODO: 没有设置的fields里的内容, 不会被清除? 这个应该是在共通里做掉的吧, 要是共通里不做的话就要自己写了

                    // 清除一些batch的标记 // TODO: 梁兄啊, batchField的更新没有放到product更新里, 暂时自己写一个用, 这里暂时注释掉

                    // 计算主类目
                    if (usjoi || cmsProduct.getChannelId().equals("024")) {
                        doSetMainCategory(cmsProduct.getCommon(), feed);

                    }

                    // 往wms推送数据
                    doSaveItemDetails(cmsProduct);


                    // 更新价格相关项目
                    Integer chg = doSetPrice(feed, cmsProduct);

                    // 设置店铺共通的店铺内分类信息
                    setSellerCats(feed, cmsProduct);

                    // 设置sku数
                    cmsProduct.getCommon().getFields().setSkuCnt(cmsProduct.getCommon().getSkus().size());

//                    doCreateUsaPlatform(cmsProduct, feed);

                    // productService.updateProduct(channelId, requestModel);
                    // 更新产品并记录商品价格表动履历，并向Mq发送消息同步sku,code,group价格范围
                    int updCnt = productService.updateProductFeedToMaster(usjoi ? "928" : channelId, cmsProduct, getTaskName(), "feed->master导入");
                    if (updCnt == 0) {
                        // 有出错, 跳过
                        String errMsg = "feed->master导入:更新:编辑商品的时候排他错误:" + originalFeed.getChannelId() + ":" + originalFeed.getCode() + " id:" + cmsProduct.getProdId() + "  " + cmsProduct.getModified();
                        $error(errMsg);
                        throw new BusinessException(errMsg);
                    }


                    // 判断是否更新平台价格 如果要更新直接更新
                    platformPriceService.publishPlatFormPrice(usjoi ? "928" : channelId, chg, cmsProduct, getTaskName(), true);
                    // 插入尺码表
                    insertCmsBtFeedImportSize(usjoi ? "928" : channelId, cmsProduct);

                } else {

                    // 不存在的场合, 新建一个product
                    cmsProduct = doCreateCmsBtProductModel(feed, newMapping, feedList.size() > 1, originalFeed.getCode());
                    if (feed.getChannelId().equals(ChannelConfigEnums.Channel.SN.getId()) && CmsConstants.UsaFeedStatus.Approved.name().equals(feed.getStatus())) {
                        doCreateUsaPlatform(cmsProduct, feed);
                    }
                    // Champion特殊处理
                    if (feed.getChannelId().equalsIgnoreCase(ChannelConfigEnums.Channel.CHAMPION.getId())) {
                        cmsProduct.getCommon().getFields().setOriginalTitleCn(feed.getName());
                        cmsProduct.getCommon().getFields().setShortDesCn(feed.getShortDescription());
                        cmsProduct.getCommon().getFields().setLongDesCn(feed.getLongDescription());
                        cmsProduct.getCommon().getFields().setMaterialCn(feed.getMaterial());
                        if (!StringUtil.isEmpty(feed.getColor())) {
                            cmsProduct.getCommon().getFields().setColor(feed.getColor().split("-")[0]);
                        }
                        cmsProduct.getCommon().getFields().setTranslateStatus("1");
                        cmsProduct.getCommon().getFields().setTranslator(getTaskName());
                        cmsProduct.getCommon().getFields().setTranslateTime(DateTimeUtil.getGMTTime());
                    }
                    if (feed.getChannelId().equalsIgnoreCase(ChannelConfigEnums.Channel.KitBag.getId())) {
                        cmsProduct.getCommon().getFields().setShortDesCn(feed.getShortDescription());
                        cmsProduct.getCommon().getFields().setMaterialCn(feed.getMaterial());
                    }

                    $debug("doCreateCmsBtProductModel:" + (System.currentTimeMillis() - startTime));
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

                    platFromAttributeCopyFromMainProduct(cmsProduct);

                    //james g kg 计算
                    weightCalculate(cmsProduct);

                    // 计算主类目
                    if (usjoi || cmsProduct.getChannelId().equals("024")) {
                        doSetMainCategory(cmsProduct.getCommon(), feed);
                    }

                    // 更新价格相关项目
                    doSetPrice(feed, cmsProduct);
                    $debug("doSetPrice:" + (System.currentTimeMillis() - startTime));
                    // 设置店铺共通的店铺内分类信息
                    setSellerCats(feed, cmsProduct);
                    $debug("setSellerCats:" + (System.currentTimeMillis() - startTime));

                    // 设置sku数
                    cmsProduct.getCommon().getFields().setSkuCnt(cmsProduct.getCommon().getSkus().size());
                    // 检查类目 重量 和 价格是否超过阈值
                    checkProduct(cmsProduct);

                    // 往wms推送数据
                    doSaveItemDetails(cmsProduct);
                    $debug("doSaveItemDetails:" + (System.currentTimeMillis() - startTime));

                    // 生成productGroup数据
                    doSetGroup(feed);
                    productService.createProduct(cmsProduct.getChannelId(), cmsProduct, getTaskName());


                    // sneakerHead 新商品加入top50
                    if (feed.getChannelId().equals(ChannelConfigEnums.Channel.SN.getId()) && CmsConstants.UsaFeedStatus.Approved.name().equals(feed.getStatus())) {
                        doInsertTop50(cmsProduct);
                    }

                    $debug("createProduct:" + (System.currentTimeMillis() - startTime));

                }

                // 插入尺码表
                insertCmsBtFeedImportSize(usjoi ? "928" : channelId, cmsProduct);

                insertWorkload(cmsProduct, feed);

                // add desmond 2016/07/07 start
                if (blnProductExist) {
                    updateCnt++;
                    $info("feed->master导入:更新成功 [ChannelId:%s] [%d/%d] [ProductCode:%s] [耗时:%s]",
                            usjoi ? cmsProduct.getOrgChannelId() : cmsProduct.getChannelId(), currentIndex, feedListCnt, cmsProduct.getCommon().getFields().getCode(),
                            (System.currentTimeMillis() - startTime));
                } else {
                    insertCnt++;
                    $info("feed->master导入:新增成功 [ChannelId:%s] [%d/%d] [ProductCode:%s] [耗时:%s]",
                            usjoi ? cmsProduct.getOrgChannelId() : cmsProduct.getChannelId(), currentIndex, feedListCnt, cmsProduct.getCommon().getFields().getCode(),
                            (System.currentTimeMillis() - startTime));
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

        // 新code插入美国sneakhead官网相应类目的第一个
        public void doInsertTop50(CmsBtProductModel cmsProduct) {
            CmsBtProductModel_Platform_Cart platform = cmsProduct.getUsPlatform(CartEnums.Cart.SNKRHDp.getValue());
            if (platform != null && ListUtils.notNull(platform.getSellerCats())) {
                List<String> sellerCatIds = new ArrayList<>();
                List<String> fullCatIds = platform.getSellerCats()
                        .stream()
                        .filter(item -> ListUtils.notNull(item.getcIds()))
                        .map(item -> "-" + item.getcIds().stream().collect(Collectors.joining("-")) + "-")
                        .collect(Collectors.toList());
                platform.getSellerCats().stream().map(CmsBtProductModel_SellerCat::getcIds).collect(Collectors.toList()).forEach(sellerCatIds::addAll);
                sellerCatIds = sellerCatIds.stream().distinct().collect(Collectors.toList());
                sellerCatIds.forEach(sellerCatId -> productTopService.insertTop50(cmsProduct.getChannelId(), sellerCatId, cmsProduct.getCommon().getFields().getCode(), CartEnums.Cart.SNKRHDp.getValue()));
                CmsCategoryReceiveMQMessageBody cmsCategoryReceiveMQMessageBody = new CmsCategoryReceiveMQMessageBody();
                cmsCategoryReceiveMQMessageBody.setChannelId(cmsProduct.getChannelId());
                cmsCategoryReceiveMQMessageBody.setCartId(platform.getCartId() + "");
                cmsCategoryReceiveMQMessageBody.setFullCatIds(fullCatIds);
                cmsCategoryReceiveMQMessageBody.setSender(getTaskName());
                cmsMqSenderService.sendMessage(cmsCategoryReceiveMQMessageBody);
            }
        }

        private void doCreateUsaPlatform(CmsBtProductModel cmsProduct, CmsBtFeedInfoModel feed) {
            Map<String, CmsBtProductModel_Platform_Cart> usPlatforms = new HashMap<>();

            cmsProduct.getCommonNotNull().getFieldsNotNull().setFeedAttribute("colorMap", feed.getAttribute().get("colorMap"));
            cmsProduct.getCommonNotNull().getFieldsNotNull().setFeedAttribute("urlkey", feed.getAttribute().get("urlkey"));
            cmsProduct.getCommonNotNull().getFieldsNotNull().setFeedAttribute("abstract", feed.getAttribute().get("abstract"));
            cmsProduct.getCommonNotNull().getFieldsNotNull().setFeedAttribute("accessory", feed.getAttribute().get("accessory"));
            cmsProduct.getCommonNotNull().getFieldsNotNull().setFeedAttribute("googleCategory", feed.getAttribute().get("googleCategory"));
            cmsProduct.getCommonNotNull().getFieldsNotNull().setFeedAttribute("googleDepartment", feed.getAttribute().get("googleDepartment"));
            cmsProduct.getCommonNotNull().getFieldsNotNull().setFeedAttribute("priceGrabberCategory", feed.getAttribute().get("priceGrabberCategory"));
            cmsProduct.getCommonNotNull().getFieldsNotNull().setFeedAttribute("taxable", feed.getAttribute().get("taxable"));
            cmsProduct.getCommonNotNull().getFieldsNotNull().setUnisex(StringUtil.isEmpty(feed.getUnisex()) ? "0" : feed.getUnisex());

//            feed.setCategory(feed.getCategory().replaceAll("-",">").replaceAll("－","-"));
            if (!StringUtil.isEmpty(cmsProduct.getFeed().getCatPath())) {
                cmsProduct.getFeed().setCatPath(cmsProduct.getFeed().getCatPath().replaceAll(">", "-"));
                cmsProduct.getFeed().setCatId(feed.getCategoryCatId());
            }
            for (TypeChannelBean typeChannelBean : typeUsChannelBeanListApprove) {
                // add desmond 2016/07/05 start
                // P0（主数据）等平台不用设置分平台共通属性(typeChannel表里面保存的是0)
                int iCartId = Integer.parseInt(typeChannelBean.getValue());
                if (iCartId >= CmsConstants.ACTIVE_CARTID_MIN && iCartId > 0) {
                    continue;
                }
                // add desmond 2016/07/05 end
                CmsBtProductModel_Platform_Cart platform = new CmsBtProductModel_Platform_Cart();
                // cartId
                platform.setCartId(Integer.parseInt(typeChannelBean.getValue()));

                // 平台类目状态(新增时)
                platform.setpCatStatus("0");  // add desmond 2016/07/05


                // 平台sku
                List<BaseMongoMap<String, Object>> skuList = new ArrayList<>();
                Double msrpMin = Double.MAX_VALUE;
                Double msrpMax = Double.MIN_VALUE;
                Double retailMin = Double.MAX_VALUE;
                Double retailMax = Double.MIN_VALUE;
                CmsBtFeedInfoModel_Platform_Cart feedPlatformCart = feed.getUsPlatform(iCartId);
                for (CmsBtFeedInfoModel_Sku sku : feed.getSkus()) {
                    BaseMongoMap<String, Object> skuInfo = new BaseMongoMap();
                    skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.skuCode.name(), sku.getSku());
                    if (feedPlatformCart != null) {
                        skuInfo.put("clientMsrpPrice", feedPlatformCart.getPriceClientMsrp());
                        skuInfo.put("clientRetailPrice", feedPlatformCart.getPriceClientRetail());
                        skuInfo.put("clientNetPrice", feedPlatformCart.getPriceClientRetail());

                        Boolean isSale = true;
                        if (feedPlatformCart.getIsSale() != null && feedPlatformCart.getIsSale() == 0) isSale = false;
                        skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.isSale.name(), isSale);
                        skuList.add(skuInfo);
                        msrpMin = Double.min(msrpMin, feedPlatformCart.getPriceClientMsrp());
                        msrpMax = Double.max(msrpMax, feedPlatformCart.getPriceClientMsrp());
                        retailMin = Double.min(retailMin, feedPlatformCart.getPriceClientRetail());
                        retailMax = Double.max(retailMax, feedPlatformCart.getPriceClientRetail());
                    }
                }
                platform.setpPriceMsrpSt(msrpMin);
                platform.setpPriceMsrpEd(msrpMax);
                platform.setpPriceSaleSt(retailMin);
                platform.setpPriceSaleEd(retailMax);
                BaseMongoMap<String, Object> fields = new BaseMongoMap<>();
                platform.setFields(fields);
                switch (CartEnums.Cart.getValueByID(iCartId + "")) {
                    case SNKRHDp:
                    case SNKRHDm:
                        CmsBtProductModel_SellerCat seller = sellerCatService.getSellerCat(feed.getChannelId(), CartEnums.Cart.SNKRHDp.getValue(), feed.getCategory());
                        if (seller != null) {
                            platform.setpCatId(seller.getcId());
                        }
                        platform.setpCatPath(feed.getCategory());
                        platform.getFields().setFeedAttribute("orderlimitcount", feed.getAttribute().get("orderlimitcount"));
                        platform.getFields().setFeedAttribute("phoneOrderOnly", feed.getAttribute().get("phoneOrderOnly"));
                        platform.getFields().setFeedAttribute("seoTitle", feed.getAttribute().get("seoTitle"));
                        platform.getFields().setFeedAttribute("seoDescription", feed.getAttribute().get("seoDescription"));
                        platform.getFields().setFeedAttribute("seoKeywords", feed.getAttribute().get("seoKeywords"));
                        platform.getFields().setFeedAttribute("freeShipping", feed.getAttribute().get("freeShipping"));
                        platform.getFields().setFeedAttribute("rewardEligible", feed.getAttribute().get("rewardEligible"));
                        platform.getFields().setFeedAttribute("discountEligible", feed.getAttribute().get("discountEligible"));
                        platform.getFields().setFeedAttribute("sneakerheadPlus", feed.getAttribute().get("sneakerheadPlus"));
                        platform.getFields().setFeedAttribute("newArrival", feed.getAttribute().get("newArrival"));
                        platform.getFields().setAttribute("sneakerfoiko", false);

                        List<String> catPath = new ArrayList<>();
                        if (!StringUtil.isEmpty(feed.getCategory())) {
                            catPath.add(feed.getCategory());
                        }
                        if (ListUtils.notNull(feed.getAttribute().get("categoriesTree"))) {
                            try {
                                List<Map<String, Object>> cats = JacksonUtil.jsonToMapList(feed.getAttribute().get("categoriesTree").get(0));
                                for (Map<String, Object> item : cats) {
                                    catPath.add((String) item.get("catPath"));
                                }
                            } catch (Exception ignored) {
                            }
                        }
                        // NewArrival的自动匹配店铺分类
                        if (platform.getFields().getAttribute("newArrival") != null && "1".equals(platform.getFields().getAttribute("newArrival"))) {
                            List<String> newArrivalCategorys = usaNewArrivalService.getNewArrivalCategory(feed.getProductType(), feed.getSizeType());
                            if (ListUtils.notNull(newArrivalCategorys)) {
                                catPath.addAll(newArrivalCategorys);
                            }
                        }

                        // 店铺内分类去重
                        if (ListUtils.notNull(catPath)) {
                            catPath = catPath.stream().distinct().collect(Collectors.toList());
                        }

                        // 店铺内分类设置
                        List<CmsBtProductModel_SellerCat> sellerCats = new ArrayList<>();
                        catPath.forEach(cat -> {
                            CmsBtProductModel_SellerCat sellerCat = sellerCatService.getSellerCat(feed.getChannelId(), CartEnums.Cart.SNKRHDp.getValue(), cat);
                            if (sellerCat != null) {
                                sellerCats.add(sellerCat);
                            }
                        });
                        platform.setSellerCats(sellerCats);
                        break;
                    case xSNKR:
                        platform.setpCatId(feed.getCategoryCatId());
                        platform.setpCatPath(feed.getCategory());
                        platform.getFields().setFeedAttribute("orderlimitcount", feed.getAttribute().get("orderlimitcount"));
                        platform.getFields().setFeedAttribute("phoneOrderOnly", feed.getAttribute().get("phoneOrderOnly"));
                        platform.getFields().setFeedAttribute("seoTitle", feed.getAttribute().get("seoTitle"));
                        platform.getFields().setFeedAttribute("seoDescription", feed.getAttribute().get("seoDescription"));
                        platform.getFields().setFeedAttribute("seoKeywords", feed.getAttribute().get("seoKeywords"));
                        platform.getFields().setFeedAttribute("newArrival", feed.getAttribute().get("newArrival"));
                        break;
                    case SNKRx:
                    case SNKRxM:
                        platform.setpCatId(feed.getCategoryCatId());
                        platform.setpCatPath(feed.getCategory());
                        platform.getFields().setFeedAttribute("orderlimitcount", feed.getAttribute().get("orderlimitcount"));
                        platform.getFields().setFeedAttribute("phoneOrderOnly", feed.getAttribute().get("phoneOrderOnly"));
                        platform.getFields().setFeedAttribute("seoTitle", feed.getAttribute().get("seoTitle"));
                        platform.getFields().setFeedAttribute("seoDescription", feed.getAttribute().get("seoDescription"));
                        platform.getFields().setFeedAttribute("seoKeywords", feed.getAttribute().get("seoKeywords"));
                        platform.getFields().setFeedAttribute("newArrival", feed.getAttribute().get("newArrival"));
                        break;
                    case DJKix:
                        platform.setpCatId(feed.getCategoryCatId());
                        platform.setpCatPath(feed.getCategory());
                        platform.getFields().setFeedAttribute("orderlimitcount", feed.getAttribute().get("orderlimitcount"));
                        platform.getFields().setFeedAttribute("phoneOrderOnly", feed.getAttribute().get("phoneOrderOnly"));
                        platform.getFields().setFeedAttribute("seoTitle", feed.getAttribute().get("seoTitle"));
                        platform.getFields().setFeedAttribute("seoDescription", feed.getAttribute().get("seoDescription"));
                        platform.getFields().setFeedAttribute("seoKeywords", feed.getAttribute().get("seoKeywords"));
                        platform.getFields().setFeedAttribute("newArrival", feed.getAttribute().get("newArrival"));
                        break;
                    case SNKRHDa:
                        platform.setpCatPath(feed.getAttribute().get("amazonBrowseTree") == null ? "" : feed.getAttribute().get("amazonBrowseTree").get(0));
                        platform.getFields().setAttribute("sellerFulfilledPrime", "1");
                        break;
                }

                if (!StringUtil.isEmpty(platform.getpCatPath())) {
                    platform.setpCatStatus("1");
                } else {
                    platform.setpCatStatus("0");
                }
                platform.setSkus(skuList);

                if (feedPlatformCart != null && feedPlatformCart.getIsSale() == 0) {
                    platform.setLock("1");
                    platform.setIsSale("0");
                    platform.setStatus(CmsConstants.ProductStatus.Pending.toString());
                } else {
                    platform.setLock("0");
                    platform.setIsSale("1");
                    platform.setStatus(CmsConstants.ProductStatus.Approved.toString());
                }
                // 商品状态
                if (ccUsAutoSyncCartList != null && ccUsAutoSyncCartList.contains(iCartId + "") && "1".equals(platform.getIsSale())) {
                    platform.setpStatus(CmsConstants.PlatformStatus.OnSale.toString());
                } else {
                    platform.setpStatus(CmsConstants.ProductStatus.Pending.toString());
                }

                usPlatforms.put("P" + typeChannelBean.getValue(), platform);
            }
            cmsProduct.setUsPlatforms(usPlatforms);
        }

        private void checkProduct(CmsBtProductModel cmsProduct) {
            if (usjoi) {
                if (StringUtil.isEmpty(cmsProduct.getCommonNotNull().getCatPath())) {
                    throw new BusinessException("主类目没有计算成功");
                }
                if (!ListUtils.isNull(categoryWhite)) {
                    if (categoryWhite.stream().noneMatch(cat -> cmsProduct.getCommonNotNull().getCatPath().indexOf(cat) == 0)) {
                        throw new BusinessException("主类目属于黑名单不能导入CMS：" + cmsProduct.getCommonNotNull().getCatPath());
                    }
                }
            }

            if (priceThreshold != null) {
                if (cmsProduct.getCommon().getSkus().stream().anyMatch(sku -> sku.getPriceRetail().compareTo(priceThreshold) > 0)) {
                    throw new BusinessException("该商品的价格超出了cms的价格阈值不能导入");
                }
            }
            if (weightThreshold != null) {
                if (cmsProduct.getCommonNotNull().getFields().getWeightKG().compareTo(weightThreshold) > 0) {
                    throw new BusinessException("该商品的重量超出了cms的重量阈值不能导入");
                }
            }
        }


        /**
         * 平台属性冲主商品复制
         *
         * @param cmsBtProductModel
         */
        private void platFromAttributeCopyFromMainProduct(CmsBtProductModel cmsBtProductModel) {

            cmsBtProductModel.getPlatforms().forEach((s, cmsBtProductModel_platform_cart) -> {
                if (cmsBtProductModel_platform_cart.getCartId() > 20 && cmsBtProductModel_platform_cart.getCartId() < 900) {
                    if (cmsBtProductModel_platform_cart.getpIsMain() == 0 && !StringUtil.isEmpty(cmsBtProductModel_platform_cart.getMainProductCode())) {
                        CmsBtProductModel mainProduct = productService.getProductByCode(cmsBtProductModel.getChannelId(), cmsBtProductModel_platform_cart.getMainProductCode());
                        if (mainProduct != null && mainProduct.getPlatform(cmsBtProductModel_platform_cart.getCartId()) != null) {
                            CmsBtProductModel_Platform_Cart mainPlatform = mainProduct.getPlatform(cmsBtProductModel_platform_cart.getCartId());
                            cmsBtProductModel_platform_cart.setpCatId(mainPlatform.getpCatId());
                            cmsBtProductModel_platform_cart.setpCatPath(mainPlatform.getpCatPath());
                            cmsBtProductModel_platform_cart.setpCatStatus(mainPlatform.getpCatStatus());
                            cmsBtProductModel_platform_cart.setpAttributeSetter(mainPlatform.getpAttributeSetter());
                            cmsBtProductModel_platform_cart.setpAttributeSetTime(mainPlatform.getpAttributeSetTime());
                            cmsBtProductModel_platform_cart.setpAttributeStatus(mainPlatform.getpAttributeStatus());
                            cmsBtProductModel_platform_cart.setFields(mainPlatform.getFields());
                            cmsBtProductModel_platform_cart.setpBrandId(mainPlatform.getpBrandId());
                            cmsBtProductModel_platform_cart.setpBrandName(mainPlatform.getpBrandName());
                            cmsBtProductModel_platform_cart.setSellerCats(mainPlatform.getSellerCats());
                            if ("Approved".equalsIgnoreCase(mainPlatform.getStatus()) || "Ready".equalsIgnoreCase(mainPlatform.getStatus())) {
                                cmsBtProductModel_platform_cart.setStatus(CmsConstants.ProductStatus.Ready);
                            }
                        }
                    }
                }
            });

        }


        private CmsBtProductModel_Field doCreateCmsBtProductModelField(CmsBtFeedInfoModel feed, boolean newFlg,
                                                                       CmsBtProductModel_Field productCommonField,
                                                                       boolean isSplit, String originalCode) {
            // --------- 商品属性信息设定 ------------------------------------------------------
            if (newFlg || StringUtils.isEmpty(productCommonField.getCode())) {
                productCommonField.setCode(feed.getCode());
            }

            // jeff 2016/05 add start
            // 产品原始code
            if (newFlg || StringUtils.isEmpty(productCommonField.getOriginalCode())) {
                productCommonField.setOriginalCode(originalCode);
            }
            // jeff 2016/05 add end

            // edward 2017/04/24 brand无条件同步
            if (((newFlg || StringUtils.isEmpty(productCommonField.getBrand()) || "1".equals(feed.getIsFeedReImport()))
                    && !"001".equals(this.channel.getOrder_channel_id()))
                    || "001".equals(this.channel.getOrder_channel_id())) {
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
                        comMtValueChannelService.insertComMtValueChannelMapping(41, usjoi ? "928" : this.channel.getOrder_channel_id(),
                                feedBrandLowerCase, feedBrandLowerCase, getTaskName());
                        // 将更新完整之后的mapping信息添加到前面取出来的品牌mapping表中
                        mapBrandMapping.put(feedBrandLowerCase, feedBrandLowerCase);
                    }
                    // add by desmond 2016/07/18 end
                }
            }

            // 产品名称（英文）
//            if (newFlg || StringUtils.isEmpty(productCommonField.getProductNameEn()) || "1".equals(feed.getIsFeedReImport())) {
            productCommonField.setProductNameEn(feed.getName());
//            }

            // 长标题, 中标题, 短标题: 都是中文, 需要自己翻译的
            // 款号model
            if (newFlg || StringUtils.isEmpty(productCommonField.getModel())) {
                productCommonField.setModel(feed.getModel());
            }
            // 颜色(中文颜色，feed->master不用设置了)
            // update desmond 2016/07/05 start
            // 小林说common.fields.color是中文颜色，不用在这里设置了，英文颜色值设到新加的字段codeDiff（商品特质英文）里面
            if (newFlg || "1".equals(feed.getIsFeedReImport())) {
                productCommonField.setColor("");   // 初期值

                // 20161227 tom champion特殊处理， 目前没有设置common的配置画面， 将来会增加 START
                if ("007".equals(feed.getChannelId())) {
                    String colorName = "";
                    if (feed.getAttribute() != null && feed.getAttribute().containsKey("ColorName") && feed.getAttribute().get("ColorName").size() > 0) {
                        colorName = feed.getAttribute().get("ColorName").get(0);
                    }
                    String colorId = "";
                    if (feed.getAttribute() != null && feed.getAttribute().containsKey("ColorId") && feed.getAttribute().get("ColorId").size() > 0) {
                        colorId = feed.getAttribute().get("ColorId").get(0);
                    }
                    productCommonField.setColor(colorName + colorId);
                } else if ("044".equals(feed.getChannelId())) {
                    productCommonField.setColor(feed.getColor());
                }
                // 20161227 tom champion特殊处理， 目前没有设置common的配置画面， 将来会增加 END
            }
            // 商品特质英文(颜色/口味/香型等)
//            if (newFlg || StringUtils.isEmpty(productCommonField.getCodeDiff()) || "1".equals(feed.getIsFeedReImport())) {
            productCommonField.setCodeDiff(feed.getColor());
//            }

//            if (!"007".equals(feed.getChannelId()) && !StringUtil.isEmpty(productCommonField.getCodeDiff()) && StringUtil.isEmpty(productCommonField.getColor())) {
//                String color = cmsBtTranslateService.translate(usjoi ? "928" : feed.getChannelId(), CmsBtCustomPropModel.CustomPropType.Common.getValue(), "com_color", productCommonField.getCodeDiff());
//                if (!StringUtil.isEmpty(color)) productCommonField.setColor(color);
//            }
//            1.【原始 颜色/口味/香型等】为空的场合，【颜色/口味/香型等】为“暂无”。
//            2.【原始 颜色/口味/香型等】不为空的场合，【颜色/口味/香型等】调用字典进行中文自动翻译。
            if (!"007".equals(feed.getChannelId()) && !"008".equals(feed.getChannelId())) {
                if (!StringUtil.isEmpty(productCommonField.getCodeDiff()) && (StringUtil.isEmpty(productCommonField.getColor()) || "暂无".equals(productCommonField.getColor()))) {
                    String color = cmsBtTranslateService.translate(usjoi ? "928" : feed.getChannelId(), CmsBtCustomPropModel.CustomPropType.Common.getValue(), "com_color", productCommonField.getCodeDiff());
                    if (!StringUtil.isEmpty(color)) productCommonField.setColor(color);
                } else if (StringUtil.isEmpty(productCommonField.getCodeDiff()) && StringUtil.isEmpty(productCommonField.getColor())) {
                    productCommonField.setColor("暂无");
                }
            }

            // update desmond 2016/07/05 end
            // 产地
            if (newFlg || StringUtils.isEmpty(productCommonField.getOrigin()) || "1".equals(feed.getIsFeedReImport())) {
                productCommonField.setOrigin(feed.getOrigin());
            }
            //货号
            if (!StringUtils.isEmpty(feed.getMpn())) {
                productCommonField.setMpn(feed.getMpn());
            }
            // 简短描述英文
//            if (newFlg || StringUtils.isEmpty(productCommonField.getShortDesEn()) || "1".equals(feed.getIsFeedReImport())) {
            productCommonField.setShortDesEn(feed.getShortDescription());
//            }
            // 详情描述英文
//            if (newFlg || StringUtils.isEmpty(productField.getLongDesEn()) || "1".equals(feed.getIsFeedReImport())) {
//                productField.setLongDesEn(feed.getLongDescription());
//            }
//            if (newFlg || StringUtils.isEmpty(productCommonField.getLongDesEn()) || "1".equals(feed.getIsFeedReImport())) {
            productCommonField.setLongDesEn(feed.getLongDescription());
//            }

//            if (newFlg || StringUtils.isEmpty(productCommonField.getLastReceivedOn()) || "1".equals(feed.getIsFeedReImport())) {
            productCommonField.setLastReceivedOn(feed.getLastReceivedOn());
//            }
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
//            if (newFlg || StringUtils.isEmpty(productCommonField.getMaterialEn()) || "1".equals(feed.getIsFeedReImport())) {
            productCommonField.setMaterialEn(feed.getMaterial());
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
            List<Map<String, Object>> multiComplex2 = new LinkedList<>();
            List<Map<String, Object>> multiComplex6 = new LinkedList<>();

            if (!feed.getChannelId().equalsIgnoreCase(ChannelConfigEnums.Channel.CHAMPION.getId())) {
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
                        String picName = doUpdateImage(feed.getChannelId(), feed.getCode(), imgOrg.trim());
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

                    if ("1".equals(autoSetImages6Flg)) {
                        // 设置PC端自拍商品图images6
                        productCommonField.put("images6", multiComplex6);
                    }
                }
            }
            // sneakerHead 鞋盒图
//            CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(feed.getChannelId(), CmsConstants.ChannelConfig.SPLIT_QUARTER_BY_CODE, "0");
//            if (cmsChannelConfigBean != null && cmsChannelConfigBean.getChannelId() != null
//                    && feed.getChannelId().equals(cmsChannelConfigBean.getChannelId())) {
            if (feed.getChannelId().equalsIgnoreCase(ChannelConfigEnums.Channel.SN.getId())) {
                if (feed.getAttribute() != null && feed.getAttribute().get("boximages") != null) {
                    for (String images : feed.getAttribute().get("boximages")) {
                        Map<String, Object> multiComplexChildren = new HashMap<>();
                        String picName = doUpdateImage(feed.getChannelId(), feed.getCode(), images);
                        multiComplexChildren.put("image2", picName);
                        multiComplex2.add(multiComplexChildren);
                    }

                }
                productCommonField.put("images2", multiComplex2);
            }

            // 商品翻译状态, 翻译者, 翻译时间, 商品编辑状态, 价格审批flg, lock商品: 暂时都不用设置

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
                case "007":
                case "008":
                case "033":
                    feedProductType = feed.getProductType();
                    productCommonField.setProductType(feed.getProductType());
                    feedSizeType = feed.getSizeType();
                    productCommonField.setSizeType(feed.getSizeType());
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
            productCommonField.setOrigProductType(feedProductType);
            productCommonField.setOrigSizeType(feedSizeType);
            // jeff 2016/04 change end

            // add by desmond 2016/07/22 start
            // feed->master导入时，将一些项目(如：sizeType,productType)的初始化中英文mapping信息插入到Synship.com_mt_value_channel表中
            // 从cms_mt_channel_config表从取得的产品分类是否从feed导入flg=1的时候，才插入产品分类mapping信息
            if (!usjoi && "1".equals(productTypeFromFeedFlg)
                    && !StringUtils.isEmpty(feedProductType)
                    && !mapProductTypeMapping.containsKey(feedProductType)) {
                // 插入产品分类初始中英文mapping信息到Synship.com_mt_value_channel表中
                comMtValueChannelService.insertComMtValueChannelMapping(57, feed.getChannelId(), feedProductType,
                        feedProductType, getTaskName());
                // 将更新完整之后的mapping信息添加到前面取出来的产品分类mapping表中
                mapProductTypeMapping.put(feedProductType, feedProductType);
            }

            // 从cms_mt_channel_config表从取得的适用人群是否从feed导入flg=1的时候，才插入适用人群mapping信息
            if (!usjoi && "1".equals(sizeTypeFromFeedFlg)
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
                if ("1".equals(appSwitchFlg)) {
                    productCommonField.setAppSwitch(1);
                } else {
                    productCommonField.setAppSwitch(0);
                }
            }
            // add desmond 2016/07/05 end

            // Luckyvitamin feed中文名称和中文描述对应
            if ("017".equalsIgnoreCase(feed.getChannelId())) {
                if (StringUtil.isEmpty(productCommonField.getOriginalTitleCn())) {
                    if (feed.getAttribute() != null && feed.getAttribute().get("translationValue1") != null) {
                        List<String> v = feed.getAttribute().get("translationValue1");
                        if (!ListUtils.isNull(v)) {
                            productCommonField.setOriginalTitleCn(v.get(0));
                        }
                    }

                }
                if (StringUtil.isEmpty(productCommonField.getLongDesCn())) {
                    if (feed.getAttribute() != null && feed.getAttribute().get("translationValue2") != null) {
                        List<String> v = feed.getAttribute().get("translationValue2");
                        if (!ListUtils.isNull(v)) {
                            productCommonField.setLongDesCn(v.get(0));
                        }
                    }
                }
            }

//            return productField;
            return productCommonField;
        }
        // update desmond 2016/07/01 end

        /**
         * 生成一个新的product
         *
         * @param feed         feed的商品信息
         *                     //         * @param mapping         feed与main的匹配关系
         * @param newMapping   新的feed与main的匹配关系
         * @param isSplit      是否拆分对象
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
            product.setChannelId(usjoi ? "928" : feed.getChannelId());
            product.setOrgChannelId(feed.getChannelId());

            product.setProdId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_PROD_ID)); // 商品的id

            // 默认不lock
            product.setLock("0");

            CmsBtProductModel_Common common = new CmsBtProductModel_Common();
            common.setFields(new CmsBtProductModel_Field());
            product.setCommon(common);


            CmsBtProductModel_Field field = doCreateCmsBtProductModelField(feed, true, common.getFields(), isSplit, originalCode);
            if (field == null) {
                return null;
            }


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
            CmsBtProductGroupModel groupP0 = getGroupIdByFeedModel(usjoi ? "928" : feed.getChannelId(), feed.getModel(), "0", usjoi ? feed.getChannelId() : null);
            if (groupP0 == null) {
                platformP0.setMainProductCode(common.getFields().getCode());
            } else {
                platformP0.setMainProductCode(groupP0.getMainProductCode());

                // 把主商品的几个状态复制过来 james 2016/08/29
                // 把主商品的自由标签复制过来 rex 2017/07/04
                copyAttributeFromMainProduct(product.getChannelId(), product, groupP0.getMainProductCode());
            }
            platforms.put("P0", platformP0);
            // add desmond 2016/07/04 end

            // --------- 商品Sku信息设定 ------------------------------------------------------
            // 对应product.skus -> product.common.skus变更
            List<CmsBtProductModel_Sku> commonSkuList = new ArrayList<>();
            for (CmsBtFeedInfoModel_Sku sku : feed.getSkus()) {
                // 设置单个sku的信息
                CmsBtProductModel_Sku commonSku = new CmsBtProductModel_Sku();

                commonSku.setSkuCode(sku.getSku()); // sku
                commonSku.setBarcode(sku.getBarcode()); // barcode
                commonSku.setClientSkuCode(sku.getClientSku()); // ClientSku
                commonSku.setClientSize(sku.getSize()); // ClientSize
                commonSku.setSize(sku.getSize()); // 尺码
                commonSku.setQty(sku.getQty());
                commonSku.setIsSale(sku.getIsSale() == null ? 1 : sku.getIsSale());
                // 重量(单位：磅) 如果原始重量不是lb的,feed里已根据公式转成lb
                if (!StringUtils.isEmpty(sku.getWeightCalc())) {
                    commonSku.setWeight(NumberUtils.toDouble(sku.getWeightCalc()));
                    commonSku.setWeightUnit(sku.getWeightOrgUnit());
                }
//                else if(product.getChannelId().equals("001") && !StringUtil.isEmpty(sku.getWeightOrgUnit()) && !StringUtils.isEmpty(sku.getWeightOrg())){
//                    if("lb".equalsIgnoreCase(sku.getWeightOrgUnit())){
//                        commonSku.setWeight(NumberUtils.toDouble(sku.getWeightOrg()));
//                        commonSku.setWeightUnit(sku.getWeightOrgUnit());
//                    }else if("kg".equalsIgnoreCase(sku.getWeightOrgUnit())){
//                        Double weight = NumberUtils.toDouble(sku.getWeightOrg());
//                        BigDecimal b = new BigDecimal(weight * 2.204623);
//                        commonSku.setWeight(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//                        commonSku.setWeightUnit("lb");
//                    }
//                }


                commonSkuList.add(commonSku);
            }
            common.setSkus(commonSkuList);

            // --------- platform ------------------------------------------------------
//            Map<String, CmsBtProductModel_Platform_Cart> platforms = new HashMap<>();  // delete desmond 2016/07/04
//            List<CmsMtCategoryTreeAllModel_Platform> platformCategoryList = null;
            // 取得新的主类目对应的平台类目
//            if (newMapping != null) {
//                CmsMtCategoryTreeAllModel categoryTreeAllModel = categoryTreeAllService.getCategoryByCatPath(newMapping.getMainCategoryPath());
//                if (categoryTreeAllModel != null) {
//                    platformCategoryList = categoryTreeAllModel.getPlatformCategory();
//                }
//            }

            // add desmond 2016/07/07 start
            // 根据渠道和平台取得已经申请的平台类目
            Map<String, Map<String, CmsMtPlatformCategoryTreeModel>> applyPlatformCategoryMap =
                    categoryTreeAllService.getApplyPlatformCategory(usjoi ? "928" : feed.getChannelId(), typeChannelBeanListApprove);
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
                        && !CartEnums.Cart.CN.getId().equals(typeChannelBean.getValue())
                        && !CartEnums.Cart.KL.getId().equals(typeChannelBean.getValue())) {
                    group = getGroupIdByFeedModel(usjoi ? "928" : feed.getChannelId(), feed.getModel(), typeChannelBean.getValue(), usjoi ? feed.getChannelId() : null);
                }
                if (group == null) {
                    platform.setpIsMain(1);
                    platform.setMainProductCode(common.getFields().getCode());  // add desmond 2016/07/04
                } else {
                    platform.setpIsMain(0);
                    platform.setMainProductCode(group.getMainProductCode());    // add desmond 2016/07/04

                    // 复制主商品的店铺内分类
                    CmsBtProductModel mainProductModel = productService.getProductByCode(product.getChannelId(), group.getMainProductCode());
                    if (mainProductModel != null && mainProductModel.getPlatform(iCartId) != null) {
                        platform.setSellerCats(mainProductModel.getPlatform(iCartId).getSellerCats());
                    }

                }

                // 平台类目状态(新增时)
                platform.setpCatStatus("0");  // add desmond 2016/07/05
                // 如果新的主类目对应的平台类目存在，那么设定
//                if (platformCategoryList != null) {
//                    for (CmsMtCategoryTreeAllModel_Platform platformCategory : platformCategoryList) {
//                        CartBean cartBean = Carts.getCart(typeChannelBean.getValue());
//                        if (cartBean != null && platformCategory.getPlatformId().equals(cartBean.getPlatform_id())) {
//                            // update desmond 2016/07/07 start
//                            // 新增时，如果该catId已经申请了才设置平台catId属性，没申请不设置
//                            if (applyPlatformCategoryMap.get(typeChannelBean.getValue()) != null
//                                    && applyPlatformCategoryMap.get(typeChannelBean.getValue()).get(platformCategory.getCatId()) != null) {
//                                platform.setpCatId(platformCategory.getCatId());
//                                platform.setpCatPath(platformCategory.getCatPath());
//                                platform.setpCatStatus("1");
//                            }
//                            break;
//                            // update desmond 2016/07/07 end
//                        }
//                    }
//                }
                // 商品状态
                // cartID是928的场合 状态直接是approved james.li
                if (platform.getCartId() == CartEnums.Cart.USJGJ.getValue()) {
                    platform.setStatus(CmsConstants.ProductStatus.Approved.toString());
                } else {
                    platform.setStatus(CmsConstants.ProductStatus.Pending.toString());
                }
                // 平台属性状态(新增时)
                platform.setpAttributeStatus("0");    // add desmond 2016/07/05

                // 平台sku
                List<BaseMongoMap<String, Object>> skuList = new ArrayList<>();
                for (CmsBtFeedInfoModel_Sku sku : feed.getSkus()) {
                    BaseMongoMap<String, Object> skuInfo = new BaseMongoMap();
                    skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.skuCode.name(), sku.getSku());
                    Boolean isSale = true;
                    if (sku.getIsSale() != null && sku.getIsSale() == 0) isSale = false;
                    skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.isSale.name(), isSale);
                    skuList.add(skuInfo);
                }

                platform.setSkus(skuList);
                if (feed.getPlatform(platform.getCartId()) != null && feed.getPlatform(platform.getCartId()).getIsSale() == 0) {
                    platform.setLock("1");
                    platform.setIsSale("0");
                }
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
            product.getFeed().setBrand(feed.getBrand());

            // 特殊处理sneakerhead的subCategories初始化
            if (CollectionUtils.isEmpty(product.getFeed().getSubCategories()) && "001".equals(feed.getChannelId())) {
                // 2017/5/11 rex.wu CMS-13
                String[] subCategories = feed.getCategory().split("-");
                List<String> productSubCategories = new ArrayList<>();
                int len = subCategories.length;
                for (int i = 0; i < len; i++) {
                    String tempSubCategory = "";
                    for (int j = 0; j <= i; j++) {
                        tempSubCategory += subCategories[j] + (j == i ? "" : "-");
                    }
                    if (!StringUtils.isEmpty(tempSubCategory)) {
                        productSubCategories.add(tempSubCategory);
                    }
                }
                product.getFeed().setSubCategories(productSubCategories);

                /*List<String> subCategories = new ArrayList<>(Arrays.asList(feed.getCategory().split("-")));
                subCategories.set(subCategories.size() - 1, feed.getCategory());
                product.getFeed().setSubCategories(subCategories);*/
            }

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
         * @param feed         feed的商品信息
         * @param product      主数据的product
         *                     //         * @param mapping         feed与main的匹配关系
         * @param newMapping   新的feed与main的匹配关系
         * @param isSplit      是否拆分对象
         * @param originalCode 原始Code
         * @return 修改过的product的内容
         */
//        private CmsBtProductModel doUpdateCmsBtProductModel(CmsBtFeedInfoModel feed, CmsBtProductModel product, CmsBtFeedMappingModel mapping, CmsBtFeedMapping2Model newMapping, Map<String, String> mapBrandMapping, boolean isSplit, String originalCode) {
        private CmsBtProductModel doUpdateCmsBtProductModel(CmsBtFeedInfoModel feed, CmsBtProductModel product,
                                                            CmsBtFeedMapping2Model newMapping,
                                                            boolean isSplit, String originalCode) {

            // jeff 2016/05 add start
            boolean numIdNoSet = doSetGroup(feed);
            if (product.getCommon() == null) {
                CmsBtProductModel_Common common = new CmsBtProductModel_Common();
                common.setFields(new CmsBtProductModel_Field());
                product.setCommon(common);
            } else if (product.getCommon().getFields() == null) {
                product.getCommon().setFields(new CmsBtProductModel_Field());
            }

            if (product.getChannelId().equalsIgnoreCase("024")) {
                product.getCommon().getFields().setLongDesEn(feed.getLongDescription());
            }

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
            if (numIdNoSet && newMapping != null && "1".equals(feed.getIsFeedReImport())) {
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
                        if (feedSku.getIsSale() != null)
                            sku.setIsSale(feedSku.getIsSale());
                        if (!StringUtils.isEmpty(feedSku.getWeightCalc())) {
                            sku.setWeight(NumberUtils.toDouble(feedSku.getWeightCalc()));  // 重量(单位：磅)
                            sku.setWeightUnit(feedSku.getWeightOrgUnit());
                        }

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
                    sku.setQty(feedSku.getQty());
                    sku.setIsSale(feedSku.getIsSale() == null ? 1 : feedSku.getIsSale());
                    if (!StringUtils.isEmpty(feedSku.getWeightCalc())) {
                        sku.setWeight(NumberUtils.toDouble(feedSku.getWeightCalc()));  // 重量(单位：磅)
                        sku.setWeightUnit(feedSku.getWeightOrgUnit());
                    }
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

//            List<CmsMtCategoryTreeAllModel_Platform> platformCategoryList = null;
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
//                    if (platformCategoryList == null && newMapping != null) {
//                        CmsMtCategoryTreeAllModel categoryTreeAllModel = categoryTreeAllService.getCategoryByCatPath(newMapping.getMainCategoryPath());
//                        if (categoryTreeAllModel != null) {
//                            platformCategoryList = categoryTreeAllModel.getPlatformCategory();
//                        }
//                    }
                    CmsBtProductModel_Platform_Cart platform = new CmsBtProductModel_Platform_Cart();
                    // cartId
                    platform.setCartId(Integer.parseInt(typeChannelBean.getValue()));
                    // 设定是否主商品
                    CmsBtProductGroupModel group = productGroupService.selectMainProductGroupByCode(feed.getChannelId(), product.getCommon().getFields().getCode(), Integer.parseInt(typeChannelBean.getValue()));
                    if (group == null) {
                        CmsBtProductGroupModel cmsBtProductGroupModel = productGroupService.selectProductGroupByCode(feed.getChannelId(), product.getCommon().getFields().getCode(), Integer.parseInt(typeChannelBean.getValue()));
                        platform.setpIsMain(0);
                        if (cmsBtProductGroupModel != null) {
                            platform.setMainProductCode(cmsBtProductGroupModel.getMainProductCode());
                        }

                    } else {
                        platform.setpIsMain(1);
                        platform.setMainProductCode(product.getCommon().getFields().getCode());
                    }

                    // 平台类目状态(更新时，新增PXX平台属性时)
                    platform.setpCatStatus("0");
                    // 如果新的主类目对应的平台类目存在，那么设定
//                    if (platformCategoryList != null) {
//                        for (CmsMtCategoryTreeAllModel_Platform platformCategory : platformCategoryList) {
//                            CartBean cartBean = Carts.getCart(typeChannelBean.getValue());
//                            if (cartBean != null && platformCategory.getPlatformId().equals(cartBean.getPlatform_id())) {
//                                // update desmond 2016/07/07 start
//                                // 新增PXX平台属性时，如果该catId已经申请了就设置平台catId属性，没申请就不设置
//                                if (applyPlatformCategoryMap.get(typeChannelBean.getValue()) != null
//                                        && applyPlatformCategoryMap.get(typeChannelBean.getValue()).get(platformCategory.getCatId()) != null) {
//                                    platform.setpCatId(platformCategory.getCatId());
//                                    platform.setpCatPath(platformCategory.getCatPath());
//                                    platform.setpCatStatus("1");
//                                }
//                                break;
//                                // update desmond 2016/07/07 end
//                            }
//                        }
//                    }
                    // 商品状态
                    // cartID是928的场合 状态直接是approved james.li
                    if (platform.getCartId() == CartEnums.Cart.USJGJ.getValue()) {
                        platform.setStatus(CmsConstants.ProductStatus.Approved.toString());
                    } else {
                        platform.setStatus(CmsConstants.ProductStatus.Pending.toString());
                    }
                    // 平台属性状态(更新时，新增PXX平台属性时)
                    platform.setpAttributeStatus("0");   // add desmond 2016/07/05
                    platforms.put("P" + typeChannelBean.getValue(), platform);
                } else {
                    // add desmond 2016/07/07 start
                    // 更新时，找到该cartId对应的platform，只更新pCatId相关属性，不更新其他属性
//                    if (platformCategoryList != null) {
//                        for (CmsMtCategoryTreeAllModel_Platform platformCategory : platformCategoryList) {
//                            CartBean cartBean = Carts.getCart(typeChannelBean.getValue());
//                            if (cartBean != null && platformCategory.getPlatformId().equals(cartBean.getPlatform_id())) {
//                                CmsBtProductModel_Platform_Cart platform = platforms.get("P" + typeChannelBean.getValue());
//                                // 如果pCatId已经手动设过了，不更新；如果没有设置过，并且该catId已经申请了才更新,没申请不更新
//                                if (platform != null
//                                        && StringUtils.isEmpty(platform.getpCatId())
//                                        && applyPlatformCategoryMap.get(typeChannelBean.getValue()) != null
//                                        && applyPlatformCategoryMap.get(typeChannelBean.getValue()).get(platformCategory.getCatId()) != null) {
//                                    platform.setpCatId(platformCategory.getCatId());
//                                    platform.setpCatPath(platformCategory.getCatPath());
//                                    platform.setpCatStatus("1");
//                                }
//                                break;
//                            }
//                        }
//                    }
                    // add desmond 2016/07/07 end

                    // cartID是928的场合 状态直接是approved james.li
                    CmsBtProductModel_Platform_Cart platform = platforms.get("P" + typeChannelBean.getValue());
                    //
                    if (platform.getCartId() == CartEnums.Cart.USJGJ.getValue()) {
                        platform.setStatus(CmsConstants.ProductStatus.Approved.toString());
                    }
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
                        // 已经approved的商品如果用新的sku的场合 platforms.Pxx.isNewSku = "1" james CMSDOC-340
                        if (entry.getValue().getCartId() != 928 && CmsConstants.ProductStatus.Approved.name().equalsIgnoreCase(entry.getValue().getStatus())) {
                            entry.getValue().setIsNewSku("1");
                        }
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
            product.getFeed().setBrand(feed.getBrand());

            // 特殊处理sneakerhead的subCategories初始化
            if (CollectionUtils.isEmpty(product.getFeed().getSubCategories()) && "001".equals(feed.getChannelId())) {
                List<String> subCategories = new ArrayList<>(Arrays.asList(feed.getCategory().split("-")));
                subCategories.set(subCategories.size() - 1, feed.getCategory());
                product.getFeed().setSubCategories(subCategories);
            }

            return product;
        }

        /**
         * 设置group
         *
         * @param feed 品牌方提供的数据
         * @return NumID是否都是空 1：是 2：否
         */
        // jeff 2016/04 change start
        private boolean doSetGroup(CmsBtFeedInfoModel feed) {

            boolean result = true;

            // 根据code, 到group表中去查找所有的group信息
            List<CmsBtProductGroupModel> existGroups = productGroupService.selectProductGroupListByCode(usjoi ? "928" : feed.getChannelId(), feed.getCode());

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
                if (Integer.parseInt(shop.getValue()) > 0 && Integer.parseInt(shop.getValue()) < 20) {
                    continue;
                }

                // group对象
                CmsBtProductGroupModel group = null;
                // 如果是聚美或者独立官网的时候，是一个Code对应一个Group,其他的平台都是几个Code对应一个Group
                if (!CartEnums.Cart.JM.getId().equals(shop.getValue())
                        && !CartEnums.Cart.CN.getId().equals(shop.getValue())
                        && !CartEnums.Cart.KL.getId().equals(shop.getValue())) {
                    // 取得product.model对应的group信息

                    boolean isQuarter = false;
                    if ("0".equals(shop.getValue()) || "1".equals(shop.getValue())) {
                        isQuarter = false;
                    } else {
                        CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(feed.getChannelId(), CmsConstants.ChannelConfig.SPLIT_QUARTER_BY_CODE, shop.getValue());
                        if (cmsChannelConfigBean != null && feed.getChannelId().equals(cmsChannelConfigBean.getChannelId()) && "1".equals(cmsChannelConfigBean.getConfigValue1())) {
                            isQuarter = true;
                        }
                    }
                    if (isQuarter) {
                        //根据当前feed的code判断是否属于最新的group还是创建group
                        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
                        //取得当前code的创建的时间
                        LocalDate feedDate = formatter.parseLocalDate(feed.getCreated());
                        //取得当前group的创建的时间
                        CmsBtProductGroupModel groupCode = getGroupIdByFeedModel(usjoi ? "928" : feed.getChannelId(), feed.getModel(), shop.getValue(), usjoi ? feed.getChannelId() : null);
                        if (groupCode != null) {
                            LocalDate groupDate = formatter.parseLocalDate(groupCode.getCreated());
                            //feed和group的创建时间作比较
                            if (feedDate.getYearOfCentury() == groupDate.getYearOfCentury()
                                    && Math.ceil(feedDate.getMonthOfYear() / 4) == Math.ceil(groupDate.getMonthOfYear() / 4)) {
                                group = groupCode;
                            } else {
                                //根据当前model取得最新的group
                                group = null;
                            }
                        }
                    } else {
                        group = getGroupIdByFeedModel(usjoi ? "928" : feed.getChannelId(), feed.getModel(), shop.getValue(), usjoi ? feed.getChannelId() : null);
                    }
                }

                // 看看同一个model里是否已经有数据在cms里存在的
                //   如果已经有存在的话: 直接用那个group
                //   如果没有的话: 新建一个
                if (group == null) {
                    group = productGroupService.createNewGroup(usjoi ? "928" : feed.getChannelId(), Integer.parseInt(shop.getValue()), feed.getCode(), false);
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
        private CmsBtProductGroupModel getGroupIdByFeedModel(String channelId, String modelCode, String cartId, String orgChannelId) {
            // 先去看看是否有存在的了
            return productGroupService.selectProductGroupByModelCodeAndCartId(channelId, modelCode, cartId, orgChannelId);
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

//        /**
//         * getPropSimpleValueByMapping 简单属性值的取得
//         *
//         * @param propType 属性的类型
//         * @param propName 属性名称
//         * @param mapping  数据到主数据映射关系定义
//         * @return 属性值
//         */
//        private String getPropSimpleValueByMapping(MappingPropType propType, String propName, CmsBtFeedMappingModel mapping) {
//
//            String returnValue = "";
//            if (mapping.getProps() != null) {
//                for (Prop prop : mapping.getProps()) {
//                    if (propType.equals(prop.getType()) && propName.equals(prop.getProp())) {
//                        List<Mapping> mappingList = prop.getMappings();
//                        if (mappingList != null && mappingList.size() > 0) {
//                            returnValue = mappingList.get(0).getVal();
//                        }
//                    }
//                }
//            }
//            return returnValue;
//        }

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
//            CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.SPLIT_QUARTER_BY_CODE, "0");

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

                newModel.setCreater(getTaskName());
                newModel.setModifier(getTaskName());
                String URL_FORMAT = "[~@.' ';#+$%&*_'':/‘’^\\()]";
                Pattern special_symbol = Pattern.compile(URL_FORMAT);
//                if (cmsChannelConfigBean != null && cmsChannelConfigBean.getChannelId() != null &&
//                        channelId.equals(cmsChannelConfigBean.getChannelId()) && "1".equalsIgnoreCase(cmsChannelConfigBean.getConfigValue1())) {
                if (channelId.equalsIgnoreCase(ChannelConfigEnums.Channel.SN.getId())) {
                    String[] imgName = originalUrl.split("/");
                    newModel.setImgName(imgName[imgName.length - 1]);
                    newModel.setUpdFlg(1);
                } else {
                    newModel.setUpdFlg(0);
                    newModel.setImgName(channelId + "-" + special_symbol.matcher(code).replaceAll(Constants.EmptyString) + "-" + index);
                }

                imagesService.insert(newModel);

                return newModel.getImgName();
            } else {
                if (!StringUtil.isEmpty(findImage.getOriginalUrl())) {
                    findImage.setOriginalUrl(findImage.getOriginalUrl().trim());
                }
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
         * getIsMasterMain 是否是Main商品
         *
         * @param feed 品牌方提供的数据
         * @return 1:isMasterMain;0:isNotMasterMain
         */
        private int getIsMasterMain(CmsBtFeedInfoModel feed) {

            String query = String.format("{\"common.fields.model\":\"%s\", \"common.fields.isMasterMain\":1}", feed.getModel());

            long cnt = productService.countByQuery(query, null, feed.getChannelId());

            return cnt < 1 ? 1 : 0;
        }


        /**
         * doSetPrice 调用共通函数设置product各平台的sku的价格
         *
         * @param feed       feed信息
         * @param cmsProduct cms product信息
         * @return CmsBtProductModel 以后计算价格直接用ProductModel
         */
        // jeff 2016/04 change start
        // private void doSetPrice(String channelId, CmsBtFeedInfoModel feed, CmsBtProductModel cmsProduct) {
//        private CmsBtProductModel doSetPrice(String channelId, CmsBtFeedInfoModel feed, CmsBtProductModel cmsProduct) {
        private Integer doSetPrice(CmsBtFeedInfoModel feed, CmsBtProductModel cmsProduct) {

            List<CmsBtProductModel_Sku> commonSkuList = cmsProduct.getCommon().getSkus();

            double maxClientMsrpPrice = 0;
            double minClientMsrpPrice = 0;
            double maxClientNetPrice = 0;
            double minClientNetPrice = 0;
            boolean isFirst = true;
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
                    // 客户过来的美金价格，不一定3个价格都有，有可能没有MSRP或者没有Retail价格(from will)
                    // 美金专柜价(优先顺序: Msrp > Retail > Net)
//                    commonSku.setClientMsrpPrice(sku.getPriceClientMsrp());
                    if (sku.getPriceClientMsrp() > 0d) {
                        commonSku.setClientMsrpPrice(sku.getPriceClientMsrp());
                    } else if (sku.getPriceClientRetail() > 0d) {
                        commonSku.setClientMsrpPrice(sku.getPriceClientRetail());
                    } else {
                        commonSku.setClientMsrpPrice(sku.getPriceNet());
                    }
                    // 美金指导价(优先顺序: Retail > Net)
//                    commonSku.setClientRetailPrice(sku.getPriceClientRetail());
                    if (sku.getPriceClientRetail() > 0d) {
                        commonSku.setClientRetailPrice(sku.getPriceClientRetail());
                    } else {
                        commonSku.setClientRetailPrice(sku.getPriceNet());
                    }
                    // 美金成本价(=priceClientCost)
                    commonSku.setClientNetPrice(sku.getPriceNet());
                    // update by desmond 2016/09/13 end
                    // 人民币专柜价(后面价格计算要用到，因为010,018等店铺不用新价格体系，还是用老的价格公式)
                    commonSku.setPriceMsrp(sku.getPriceMsrp());
                    // 人民币指导价(后面价格计算要用到，因为010,018等店铺不用新价格体系，还是用老的价格公式)
                    commonSku.setPriceRetail(sku.getPriceCurrent());
                    if (isFirst) {
                        minClientNetPrice = commonSku.getClientNetPrice();
                        minClientMsrpPrice = commonSku.getClientMsrpPrice();
                        isFirst = false;
                    }
                    //clientMsrpPrice
                    if (commonSku.getClientMsrpPrice() > maxClientMsrpPrice) {
                        maxClientMsrpPrice = commonSku.getClientMsrpPrice();
                    } else if (commonSku.getClientMsrpPrice() < minClientMsrpPrice) {
                        minClientMsrpPrice = commonSku.getClientMsrpPrice();
                    }

                    //clientNetPrice
                    if (commonSku.getClientNetPrice() > maxClientNetPrice) {
                        maxClientNetPrice = commonSku.getClientNetPrice();
                    } else if (commonSku.getClientNetPrice() < minClientNetPrice) {
                        minClientNetPrice = commonSku.getClientNetPrice();
                    }

                    if (commonSku.getAttribute("confClientMsrpPrice") == null) {
                        commonSku.setConfClientMsrpPrice(commonSku.getClientMsrpPrice());
                    }

                    String clientMsrpPriceChgFlg = null;
                    if (commonSku.getConfClientMsrpPrice().compareTo(commonSku.getClientMsrpPrice()) == 0) {
                        clientMsrpPriceChgFlg = "0";
                    } else if (commonSku.getConfClientMsrpPrice() < commonSku.getClientMsrpPrice()) {
                        if (commonSku.getConfClientMsrpPrice().equals(0D))
                            clientMsrpPriceChgFlg = "U100%";
                        else {
                            Long range = Math.round(((commonSku.getClientMsrpPrice() - commonSku.getConfClientMsrpPrice()) / commonSku.getConfClientMsrpPrice()) * 100d);
                            clientMsrpPriceChgFlg = "U" + range + "%";
                        }
                    } else {
                        Long range = Math.round(((commonSku.getConfClientMsrpPrice() - commonSku.getClientMsrpPrice()) / commonSku.getConfClientMsrpPrice()) * 100d);
                        if (range.compareTo(0L) == 0) {
                            clientMsrpPriceChgFlg = "0";
                        } else {
                            clientMsrpPriceChgFlg = "D" + range + "%";
                        }
                    }
                    commonSku.setClientMsrpPriceChgFlg(clientMsrpPriceChgFlg);
                }
            }

            if (Double.compare(minClientMsrpPrice, maxClientMsrpPrice) == 0) {
                cmsProduct.getCommon().getFields().setClientMsrpPrice(String.format("%s", minClientMsrpPrice));
            } else {
                cmsProduct.getCommon().getFields().setClientMsrpPrice(String.format("%s~%s", minClientMsrpPrice, maxClientMsrpPrice));
            }

            if (Double.compare(maxClientNetPrice, minClientNetPrice) == 0) {
                cmsProduct.getCommon().getFields().setClientNetPrice(String.format("%s", minClientNetPrice));
            } else {
                cmsProduct.getCommon().getFields().setClientNetPrice(String.format("%s~%s", minClientNetPrice, maxClientNetPrice));
            }

            // 设置platform.PXX.skus里面的价格
            try {
                return priceService.setPrice(cmsProduct, false);
            } catch (IllegalPriceConfigException ie) {
                // 渠道级别价格计算配置错误, 停止后面的feed->master导入，避免报几百条一样的错误信息
                String errMsg = String.format("feed->master导入:共通配置异常终止:发现渠道级别的价格计算配置错误，后面的feed导入不做了，" +
                        "请修改好相应配置项目后重新导入 [ErrMsg=%s]", ie.getMessage());
                $error(errMsg);
                throw new CommonConfigNotFoundException(errMsg);
            } catch (Exception ex) {
                String errMsg = "feed->master导入:异常终止:调用共通函数计算产品价格时出错 [ErrMsg= ";
                if (StringUtils.isNullOrBlank2(ex.getMessage())) {
                    errMsg += ex.getStackTrace()[0].toString() + "]";
                } else {
                    errMsg += ex.getMessage() + "]";
                }
                $error(errMsg);
                throw new BusinessException(errMsg);
            }
            // jeff 2016/04 add end
        }

        /**
         * doSaveItemDetails 往wms推送sku产品信息
         *
         * @param productModel productModel
         */
        private boolean doSaveItemDetails(CmsBtProductModel productModel) {

            // 如果feed里,没有sku的数据的话,那么就不需要做下去了
            if (productModel.getCommon().getSkus() == null || productModel.getCommon().getSkus().size() == 0) {
                // 也认为是正常
                return true;
            }

            CmsBtProductModel_Field field = productModel.getCommon().getFields();

            for (CmsBtProductModel_Sku sku : productModel.getCommon().getSkus()) {
                String channelId = StringUtils.isEmpty(productModel.getOrgChannelId()) ? productModel.getChannelId() : productModel.getOrgChannelId();
                String oneSkuCode = sku.getSkuCode();
                CmsBtProductModel oldProductModel = null;
                if (usjoi) {
                    oldProductModel = productService.getProductBySku("928", channelId, oneSkuCode);
                } else {
                    oldProductModel = productService.getProductBySku(channelId, oneSkuCode);
                }
                if (oldProductModel != null && !field.getCode().equals(oldProductModel.getCommon().getFields().getCode())) {
                    String errMsg = String.format("feed->master导入:异常终止:由于该sku所属的feedCode发生了变更,[sku:%s] [OldFeedCode:%s] [NewFeedCode:%s]",
                            oneSkuCode, oldProductModel.getCommon().getFields().getCode(), field.getCode());
                    $error(errMsg);
                    throw new BusinessException(errMsg);
                }
            }

            // 按sku为单位推送产品信息到wms
            productModel.getCommon().getSkus().forEach(sku -> {
                WmsCreateOrUpdateProductMQMessageBody messageBody = new WmsCreateOrUpdateProductMQMessageBody();
                messageBody.setChannelId(StringUtils.isEmpty(productModel.getOrgChannelId()) ? productModel.getChannelId() : productModel.getOrgChannelId());
                messageBody.setSku(sku.getSkuCode());
                messageBody.setCode(field.getCode());
                // 商品名称：取英文名称，如果英文名称为空则取中文名称
                messageBody.setName(StringUtils.isEmpty(field.getProductNameEn()) ? field.getOriginalTitleCn() : field.getProductNameEn());
                messageBody.setBrand(field.getBrand());
                messageBody.setColor(field.getColor());
                messageBody.setSize(sku.getClientSize());
                messageBody.setBarcode(sku.getBarcode());
                String imagePath = "";

                final List<CmsBtProductModel_Field_Image> image1List = field.getImages1();
                if (!image1List.isEmpty()) {
                    final String image1 = image1List.get(0).getName();
                    if (!StringUtils.isEmpty(image1)) {
                        imagePath = ImageServer.imageUrl(messageBody.getChannelId(), image1);
                    }
                }

                messageBody.setImageUrl(imagePath);
                messageBody.setMsrp(BigDecimal.valueOf(sku.getClientMsrpPrice()));
                messageBody.setClientSku(sku.getClientSkuCode());
                messageBody.setSkuKind("1");
                messageBody.setNetPrice(BigDecimal.valueOf(sku.getClientNetPrice()));
                // messageBody.setProductType(field.getProductType()); // 暂时忽略，因为和WMS不一致
                messageBody.setUserName(productModel.getModifier());
                messageBody.setIsSale(sku.getIsSale());
                messageBody.setCmsSku(sku.getSkuCode());

                /*WmsCreateOrUpdateProductMQMessageBody_detail detailInfo = new WmsCreateOrUpdateProductMQMessageBody_detail();
                detailInfo.setKgWeight(field.getWeightKG());
                detailInfo.setLbWeight(field.getWeightLb());
                detailInfo.setOrigin(field.getOrigin());
                messageBody.setDetailInfo(detailInfo);*/
                cmsMqSenderService.sendMessage(messageBody);
                $debug(productModel.getModifier() + "同步最新Product信息至WMS，内容：" + JacksonUtil.bean2Json(messageBody));
            });

            return true;
        }


        private void insertWorkload(CmsBtProductModel cmsProduct, CmsBtFeedInfoModel feed) {

            // 变更自动同步到全部平台("ALL")或者自动同步到指定平台(用逗号分隔 如:"28,29"),没有配置时不插入workload表
            // 当该产品未被锁定且已批准的时候，往workload表里面插入一条上新数据，并逻辑清空相应的business_log
            if ("ALL".equalsIgnoreCase(ccAutoSyncCarts)) {
                // 变更自动同步到全部平台("ALL")时
                sxProductService.insertSxWorkLoad(cmsProduct, getTaskName());
            } else if (ListUtils.notNull(ccAutoSyncCartList)) {
                // 变更自动同步到指定平台(用逗号分隔 如:"28,29")时
                sxProductService.insertSxWorkLoad(cmsProduct, ccAutoSyncCartList, getTaskName());
            }

            if (ListUtils.notNull(ccUsAutoSyncCartList)) {
                ccUsAutoSyncCartList.forEach(cartId -> {
                    CmsBtProductModel_Platform_Cart platform = cmsProduct.getUsPlatform(Integer.parseInt(cartId));
                    if (platform != null && CmsConstants.ProductStatus.Approved.toString().equals(platform.getStatus())) {
                        Integer publishTime = 0;
                        if (feed.getUsPlatform(Integer.parseInt(cartId)) != null)
                            publishTime = feed.getUsPlatform(Integer.parseInt(cartId)).getSharingDay();
                        if (publishTime == null) publishTime = 0;
                        if (publishTime == 0) {
                            platformProductUploadService.saveCmsBtUsWorkloadModel(cmsProduct.getChannelId(), Integer.parseInt(cartId), cmsProduct.getCommon().getFields().getCode(), null, 0, getTaskName());
                        } else {
                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(new Date());
                            calendar.add(Calendar.DATE, publishTime);
                            platformProductUploadService.saveCmsBtUsWorkloadModel(cmsProduct.getChannelId(), Integer.parseInt(cartId), cmsProduct.getCommon().getFields().getCode(), calendar.getTime(), 0, getTaskName());
                        }
                    }
                });
            }
        }

        private void weightCalculate(CmsBtProductModel cmsProduct) {
            Double weight = null;
            for (CmsBtProductModel_Sku sku : cmsProduct.getCommon().getSkus()) {
                if (sku.getWeight() != null && (weight == null || weight.compareTo(sku.getWeight()) > 0)) {
                    weight = sku.getWeight();
                }
            }
            //1磅(lb)=453.59237克(g)
            if (weight != null) {
                cmsProduct.getCommon().getFields().setWeightLb(weight);
                BigDecimal b = new BigDecimal(weight * 453.59237);
                cmsProduct.getCommon().getFields().setWeightG(b.setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
                b = new BigDecimal(weight * 453.59237 / 1000.0);

                Double kg = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                if (kg.compareTo(0.0) == 0) kg = 0.1;
                cmsProduct.getCommon().getFields().setWeightKG(kg);
            }
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

        private void copyAttributeFromMainProduct(String channelId, CmsBtProductModel productModel, String mainProductCode) {
            CmsBtProductModel mainProduct = productService.getProductByCode(channelId, mainProductCode);
            if (mainProduct != null) {
                CmsBtProductModel_Common common = productModel.getCommon();
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

                // 复制主商品自由标签
                if ("1".equals(copyMainProductFreeTagsFlg)) {
                    if (ListUtils.isNull(productModel.getFreeTags())) {
                        productModel.setFreeTags(mainProduct.getFreeTags());
                    } else {
                        for (String tag : mainProduct.getFreeTags()) {
                            if (!productModel.getFreeTags().contains(tag)) {
                                productModel.getFreeTags().add(tag);
                            }
                        }
                    }
                }
            }
        }

        /**
         * 出错的时候将错误信息回写到cms_bt_business_log表
         *
         * @param channelId       String 渠道id
         * @param cartId          String 平台id
         * @param feedModel       String feed model
         * @param feedProductCode String feed产品code
         * @param errCode         String 错误code
         * @param errMsg          String 错误消息
         * @param modifier        String 更新者
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
         * @param feed           CmsBtFeedInfoModel feed信息
         * @param updFlg         Integer 更新flg(1:feed->master导入失败   2:feed->master导入失败)
         * @param errMsg         String 失败的错误消息
         * @param isFeedReImport String 是否强制重新导入
         */
        private void updateFeedInfo(CmsBtFeedInfoModel feed, Integer updFlg, String errMsg, String isFeedReImport) {
            if (feed == null) return;

            Map<String, Object> paraMap = new HashedMap();
            paraMap.put("code", feed.getCode());

            Map<String, Object> rsMap = new HashedMap();
            rsMap.put("updFlg", updFlg);
            rsMap.put("updMessage", errMsg);
            rsMap.put("modifier", getTaskName());
            rsMap.put("modified", DateTimeUtil.getNowTimeStamp());
            if (!StringUtils.isEmpty(isFeedReImport))
                rsMap.put("isFeedReImport", isFeedReImport);
            feedInfoService.updateFeedInfo(feed.getChannelId(), paraMap, rsMap);
        }

        /**
         * 设置产品各个平台的店铺内分类信息
         * <p>
         * 新增或更新产品时，如果字典中解析出来子分类id在产品该平台的店铺内信息中不存在，则新加一条店铺类分类
         * 存在的话不更新，因为店铺内信息运营有可能已经修改过了
         * <p>
         * 从cms_mt_channel_condition_config表中取得该店铺对应的店铺内分类数据字典，
         * 表里的platformPropId字段的值为：
         * "seller_cids_"+cartId  例：seller_cats_26
         * 店铺内分类字典的里面的value值结构如下：
         * 结构："cId(子分类id)|cIds(父分类id,子分类id)|cName(父分类id,子分类id)|cNames(父分类id,子分类id)"
         * 例子："1124130584|1124130579,1124130584|系列>彩色宝石|系列,彩色宝石"
         *
         * @param feed    CmsBtFeedInfoModel feed信息
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
                List<ConditionPropValueModel> conditionPropValueModels = null;

                conditionPropValueModels = channelConditionConfig.get(platformPropId);

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
                            DictWord conditionDictWord = (DictWord) ruleJsonMapper.deserializeRuleWord(conditionExpressionStr);
                            conditionExpression = conditionDictWord.getExpression();
                        } else if (conditionExpressionStr.startsWith("{\"ruleWordList\"")) {
                            // 不带名字，只有字典表达式字典解析
                            conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
                        } else {
                            continue;
                        }

                        // 店铺内分类字典的值（"cId(子分类id)|cIds(父分类id,子分类id)|cName(父分类id,子分类id)|cNames(父分类id,子分类id)"）
                        // 例："1124130584|1124130579,1124130584|系列>彩色宝石|系列,彩色宝石"
                        propValue = expressionParser.parse(conditionExpression, null, getTaskName(), null);
                    } catch (Exception e) {
                        // 因为店铺内分类即使这里不设置，运营也可以手动设置的，所以这里如果解析字典异常时，不算feed->master导入失败
                        continue;
                    }

                    // 如果从字典里面取得店铺内分类为空
                    if (StringUtils.isEmpty(propValue))
                        continue;

                    List<String> sellerCatsList = Arrays.asList(propValue.split("\\|"));
                    // 如果取得的店铺内分类列表为空，继续循环
                    if (ListUtils.isNull(sellerCatsList))
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

        protected void doSetMainCategory(CmsBtProductModel_Common prodCommon, CmsBtFeedInfoModel feed) {
            prodCommon.getFieldsNotNull().setOrigProductType(feed.getProductType());
            prodCommon.getFieldsNotNull().setOrigSizeType(feed.getSizeType());
            if (prodCommon == null || StringUtils.isEmpty(feed.getCategory())) return;

            // feed里面人为设置过主类目的场合
            MtCategoryKeysModel mtCategoryKeysModel = null;
            if (!StringUtil.isEmpty(feed.getMainCategoryEn()) && "1".equals(feed.getCatConf())) {
                mtCategoryKeysModel = mtCategoryKeysDao.selectOne(feed.getMainCategoryEn());
            }

            // 共通Field
            CmsBtProductModel_Field prodCommonField = prodCommon.getFieldsNotNull();

            // 调用Feed到主数据的匹配接口取得匹配度最高的主类目
            long beginTime = System.currentTimeMillis();
            MatchResult searchResult = getMainCatInfo(feed.getCategory(),
                    !StringUtils.isEmpty(prodCommonField.getOrigProductType()) ? prodCommonField.getOrigProductType() : "",
                    !StringUtils.isEmpty(prodCommonField.getOrigSizeType()) ? prodCommonField.getOrigSizeType() : "",
                    prodCommonField.getProductNameEn(),
                    prodCommonField.getBrand());


            // 用feed里面的主类目进行替换
            if (!"1".equals(prodCommon.getCatConf()) && mtCategoryKeysModel != null) {
                searchResult.setCnName(feed.getMainCategoryCn());
                searchResult.setEnName(feed.getMainCategoryEn());
                searchResult.setTaxDeclare(mtCategoryKeysModel.getTaxDeclare());
                searchResult.setTaxPersonal(mtCategoryKeysModel.getTaxPersonal());
                searchResult.setProductTypeCn(mtCategoryKeysModel.getProductTypeCn());
                searchResult.setProductTypeEn(mtCategoryKeysModel.getProductTypeEn());
                String weight = mtCategoryKeysModel.getWeight();
                if (!StringUtil.isEmpty(weight)) {
                    searchResult.setWeight(Double.parseDouble(weight));
                }
            }

            if (searchResult != null) {
                $info(String.format("调用主类目匹配接口取得主类目和适用人群正常结束！[耗时:%s] [feedCategoryPath:%s] [productType:%s] " +
                                "[sizeType:%s] [productNameEn:%s] [brand:%s] [mainCategory:%s]", (System.currentTimeMillis() - beginTime), feed.getCategory(),
                        prodCommonField.getProductType(), prodCommonField.getSizeType(), prodCommonField.getProductNameEn(), prodCommonField.getBrand(), searchResult.getCnName()));

                // 先备份原来的productType和sizeType
                // feed原始产品分类
                if (StringUtils.isEmpty(prodCommonField.getOrigProductType())
                        && !StringUtils.isEmpty(prodCommonField.getProductType())) {
                    prodCommonField.setOrigProductType(prodCommonField.getProductType());
                }
                // feed原始适合人群
                if (StringUtils.isEmpty(prodCommonField.getOrigSizeType())
                        && !StringUtils.isEmpty(prodCommonField.getSizeType())) {
                    prodCommonField.setOrigSizeType(prodCommonField.getSizeType());
                }

                // 主类目path(中文)
                if (!StringUtils.isEmpty(searchResult.getCnName()) && (!"1".equals(prodCommon.getCatConf()) || StringUtil.isEmpty(prodCommon.getCatPath()))) {
                    prodCommon.setCatPath(searchResult.getCnName());
                    prodCommon.setCatId(MD5.getMD5(searchResult.getCnName()));
                    prodCommonField.setCategorySetTime(DateTimeUtil.getNow());
                    prodCommonField.setCategorySetter(getTaskName());
                }
                // 主类目path(英文)
                if (!StringUtils.isEmpty(searchResult.getEnName()) && (!"1".equals(prodCommon.getCatConf()) || StringUtil.isEmpty(prodCommon.getCatPathEn()))) {
                    prodCommon.setCatPathEn(searchResult.getEnName());
                }
//                // 主类目id(就是主类目path中文的MD5码)
//                if (!StringUtils.isEmpty(searchResult.getCnName())) {
//                    prodCommon.setCatId(MD5.getMD5(searchResult.getCnName()));
//                    prodCommonField.setCategorySetTime(DateTimeUtil.getNow());
//                    prodCommonField.setCategorySetter(getTaskName());
//                }
                // 更新主类目设置状态
                if (!StringUtils.isEmpty(prodCommon.getCatId())) {
                    prodCommonField.setCategoryStatus("1");
                } else {
                    prodCommonField.setCategoryStatus("0");
                }

                // 根据主类目设置商品重量
                if (searchResult.getWeight() != null && searchResult.getWeight() != 0.0D
                        && (prodCommonField.getWeightLb() == null || prodCommonField.getWeightLb() == 0.0)) {
                    prodCommonField.setWeightLb(searchResult.getWeight());
                    BigDecimal b = new BigDecimal(searchResult.getWeight() * 453.59237);
                    prodCommonField.setWeightG(b.setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
                    b = new BigDecimal(searchResult.getWeight() * 453.59237 / 1000.0);
                    prodCommonField.setWeightKG(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }

                // 如果sku的重量不存在,则设置成默认重量
                prodCommon.getSkus().forEach(sku -> {
                    if ((sku.getWeight() == null || sku.getWeight() == 0.0D)
                            && searchResult.getWeight() != null && searchResult.getWeight() != 0.0D) {
                        sku.setWeight(searchResult.getWeight());
                        sku.setWeightUnit("lb");
                    }
                });
                // 产品分类(英文)
                if (!StringUtils.isEmpty(searchResult.getProductTypeEn()) && (!"1".equals(prodCommon.getCatConf()) || StringUtil.isEmpty(prodCommonField.getProductType())))
                    prodCommonField.setProductType(searchResult.getProductTypeEn().toLowerCase());
                // 产品分类(中文)
                if (!StringUtils.isEmpty(searchResult.getProductTypeCn()) && (!"1".equals(prodCommon.getCatConf()) || StringUtil.isEmpty(prodCommonField.getProductTypeCn())))
                    prodCommonField.setProductTypeCn(searchResult.getProductTypeCn());
                // 适合人群(英文)
                if (!StringUtils.isEmpty(searchResult.getSizeTypeEn()) && (!"1".equals(prodCommon.getCatConf()) || StringUtil.isEmpty(prodCommonField.getSizeType())))
                    prodCommonField.setSizeType(searchResult.getSizeTypeEn().toLowerCase());
                // 适合人群(中文)
                if (!StringUtils.isEmpty(searchResult.getSizeTypeCn()) && (!"1".equals(prodCommon.getCatConf()) || StringUtil.isEmpty(prodCommonField.getSizeTypeCn())))
                    prodCommonField.setSizeTypeCn(searchResult.getSizeTypeCn());
                // TODO 2016/12/30暂时这样更新，以后要改
                if (!StringUtils.isEmpty(searchResult.getTaxPersonal()) && (getTaskName().equalsIgnoreCase(prodCommonField.getHsCodeSetter()) || "CmsUploadProductToUSJoiJob".equalsIgnoreCase(prodCommonField.getHsCodeSetter()) || !"1".equals(prodCommon.getCatConf()) || StringUtil.isEmpty(prodCommonField.getHsCodePrivate()))) {
                    // 税号个人
                    if (!StringUtils.isEmpty(searchResult.getTaxPersonal())) {
                        prodCommonField.setHsCodePrivate(searchResult.getTaxPersonal());
                        prodCommonField.setHsCodeSetTime(DateTimeUtil.getNow());
                        prodCommonField.setHsCodeSetter(getTaskName());
                    }
                    // 更新税号设置状态
                    if (!StringUtils.isEmpty(prodCommonField.getHsCodePrivate())) {
                        prodCommonField.setHsCodeStatus("1");
                    } else {
                        prodCommonField.setHsCodeStatus("0");
                    }
                }
                // 税号跨境申报（10位）
                if (!StringUtils.isEmpty(searchResult.getTaxDeclare()) && (!"1".equals(prodCommon.getCatConf()) || StringUtil.isEmpty(prodCommonField.getHsCodeCross())))
                    prodCommonField.setHsCodeCross(searchResult.getTaxDeclare());

                // 商品中文名称(如果已翻译，则不设置)
                // 临时特殊处理 017的名称不根据主类目自动翻译,如果后续有这个需求再改正
                if (usjoi && "0".equals(prodCommonField.getTranslateStatus())) {
                    if (!StringUtils.isEmpty(searchResult.getCnName())) {
                        // 主类目叶子级中文名称（"服饰>服饰配件>钱包卡包钥匙包>护照夹" -> "护照夹"）
                        String leafCategoryCnName = searchResult.getCnName().substring(searchResult.getCnName().lastIndexOf(">") + 1,
                                searchResult.getCnName().length());

                        // 设置中文名称 产品名称 brand(英文) + " " + brand(中文) + " " + sizeTypeCn + " " + leafCategoryCnName 来生成
                        TypeChannelBean typeChannelBean = null;
                        if (!StringUtil.isEmpty(prodCommonField.getBrand()) && usjoi) {
                            typeChannelBean = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.BRAND_41, "928", prodCommonField.getBrand(), "cn");
                        }
                        if (typeChannelBean != null && !StringUtil.isEmpty(typeChannelBean.getName())) {
                            prodCommonField.setOriginalTitleCn(getOriginalTitleCnByCategory(typeChannelBean.getName()
                                    , prodCommonField.getSizeTypeCn(), leafCategoryCnName));
                        } else {
                            prodCommonField.setOriginalTitleCn(getOriginalTitleCnByCategory(prodCommonField.getBrand()
                                    , prodCommonField.getSizeTypeCn(), leafCategoryCnName));
                        }
                        $info(prodCommonField.getOriginalTitleCn());
                    }
                }
            }
        }

        /**
         * 调用Feed到主数据的匹配接口匹配主类目,返回匹配度最高的第一个查询结果
         *
         * @param feedCategoryPath feed类目Path
         * @param productType      产品分类
         * @param sizeType         适合人群(英文)
         * @param productNameEn    产品名称（英文）
         * @param brand            产品品牌
         * @return SearchResult 匹配度最高的第一个查询结果
         */
        public MatchResult getMainCatInfo(String feedCategoryPath, String productType, String sizeType, String productNameEn, String brand) {
            // 取得查询条件
            FeedQuery query = getFeedQuery(feedCategoryPath, productType, sizeType, productNameEn, brand);

            // 调用主类目匹配接口，取得匹配度最高的一个主类目和sizeType
            MatchResult searchResult = searcher.search(query, false);
            if (searchResult == null) {
                String errMsg = String.format("调用Feed到主数据的匹配程序匹配主类目失败！[feedCategoryPath:%s] [productType:%s] " +
                        "[sizeType:%s] [productNameEn:%s] [brand:%s]", feedCategoryPath, productType, sizeType, productNameEn, brand);
                $error(errMsg);
                return null;
            }

            // 取得匹配度最高的主类目
            return searchResult;
        }

        /**
         * 取得查询条件
         *
         * @param feedCategoryPath feed类目Path
         * @param productType      产品分类
         * @param sizeType         适合人群(英文)
         * @param productNameEn    产品名称（英文）
         * @param brand            产品品牌
         * @return FeedQuer 查询条件
         */
        private FeedQuery getFeedQuery(String feedCategoryPath, String productType, String sizeType, String productNameEn, String brand) {
            // 调用Feed到主数据的匹配程序匹配主类目
            // 子店feed类目path分隔符(由于导入feedInfo表时全部替换成用"-"来分隔了，所以这里写固定值就可以了)
            List<String> categoryPathSplit = new ArrayList<>();
            categoryPathSplit.add("-");
            Tokenizer tokenizer = new Tokenizer(categoryPathSplit);

            FeedQuery query = new FeedQuery(feedCategoryPath, null, tokenizer);
            query.setProductType(productType);
            query.setSizeType(sizeType);
            query.setProductName(productNameEn, brand);

            return query;
        }

        /**
         * 根据自动匹配商品主类目取得商品中文名称
         *
         * @param brand              品牌
         * @param sizeTypeCn         适合人群(中文)
         * @param leafCategoryCnName 主类目叶子级中文名称
         * @return String 商品中文名称(品牌 + 空格 + Size Type中文 + 空格 + 主类目叶子级中文名称)
         */
        private String getOriginalTitleCnByCategory(String brand, String sizeTypeCn, String leafCategoryCnName) {
            if (brand == null) brand = "";
            if (sizeTypeCn == null) sizeTypeCn = "";
            if (leafCategoryCnName == null) leafCategoryCnName = "";
            return brand + " " + sizeTypeCn + " " + leafCategoryCnName;
        }
    }
}
