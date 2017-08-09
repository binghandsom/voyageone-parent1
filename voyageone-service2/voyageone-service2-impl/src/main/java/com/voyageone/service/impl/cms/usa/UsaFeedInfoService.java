package com.voyageone.service.impl.cms.usa;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by james on 2017/7/5.
 * 美国用 feed info数据service
 */
@Service
public class UsaFeedInfoService extends BaseService {

    @Autowired
    FeedInfoService feedInfoService;

    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private ProductService productService;


    /**
     * feed的中国价格计算
     *
     * @param cmsBtFeedInfoModel feed对象
     * @return 价格计算后的对象
     */
    public CmsBtFeedInfoModel setPrice(CmsBtFeedInfoModel cmsBtFeedInfoModel) {

        String productType = cmsBtFeedInfoModel.getProductType();
        if (!StringUtil.isEmpty(productType)) {
            productType = productType.toLowerCase();
        }
        CmsChannelConfigBean msrpConfig = CmsChannelConfigs.getConfigBean(cmsBtFeedInfoModel.getChannelId(), CmsConstants.ChannelConfig.FEED_PRICE_MSRP, productType);
        if (msrpConfig == null) {
            msrpConfig = CmsChannelConfigs.getConfigBean(cmsBtFeedInfoModel.getChannelId(), CmsConstants.ChannelConfig.FEED_PRICE_MSRP, "0");
        }
        String formulaMsrp = msrpConfig.getConfigValue1();

        CmsChannelConfigBean retailConfig = CmsChannelConfigs.getConfigBean(cmsBtFeedInfoModel.getChannelId(), CmsConstants.ChannelConfig.FEED_PRICE_RETAIL, productType);
        if (retailConfig == null) {
            retailConfig = CmsChannelConfigs.getConfigBean(cmsBtFeedInfoModel.getChannelId(), CmsConstants.ChannelConfig.FEED_PRICE_RETAIL, "0");
        }
        String formulaRetail = retailConfig.getConfigValue1();

        // 如果usPlatform.cartId=8的Msrp($)和Price($)值发送改变, 同步计算所有平台中国价格级SKU价格
        CmsBtFeedInfoModel_Platform_Cart snPlatform = cmsBtFeedInfoModel.getUsPlatform(Integer.valueOf(8));
        if (snPlatform != null) {
            Double priceMsrp = null;
            Double priceCurrent = null;
            if (snPlatform.getPriceClientMsrp() == null || Double.compare(snPlatform.getPriceClientMsrp(), 0.0) == 0) {
                priceMsrp = 0D;
            } else {
                priceMsrp = calculatePrice(formulaMsrp, snPlatform);
            }
            if (snPlatform.getPriceClientRetail() == null || snPlatform.getPriceClientMsrp() == null
                    || Double.compare(snPlatform.getPriceClientRetail(), 0.0) == 0 || Double.compare(snPlatform.getPriceClientMsrp(), 0.0) == 0) {
                priceCurrent = 0D;
            } else {
                priceCurrent = calculatePrice(formulaRetail, snPlatform);
            }

            // 中国平台价格
            for (CmsBtFeedInfoModel_Platform_Cart platformCart : cmsBtFeedInfoModel.getPlatforms().values()) {
                platformCart.setPriceMsrp(priceMsrp);
                platformCart.setPriceCurrent(priceCurrent);
            }

            Double priceClientRetailMin = cmsBtFeedInfoModel.getSkus().get(0).getPriceClientRetail();
            Double priceClientMsrpMin = cmsBtFeedInfoModel.getSkus().get(0).getPriceClientMsrp();
            Double priceClientRetailMax = cmsBtFeedInfoModel.getSkus().get(0).getPriceClientRetail();
            Double priceClientMsrpMax = cmsBtFeedInfoModel.getSkus().get(0).getPriceClientMsrp();

            for (CmsBtFeedInfoModel_Sku sku : cmsBtFeedInfoModel.getSkus()) {
                sku.setPriceMsrp(priceMsrp);
                sku.setPriceCurrent(priceCurrent);
                sku.setPriceClientMsrp(snPlatform.getPriceClientMsrp());
                sku.setPriceClientRetail(snPlatform.getPriceClientRetail());
                sku.setPriceNet(snPlatform.getPriceClientRetail());

                priceClientRetailMin = Double.min(priceClientRetailMin, sku.getPriceClientRetail());
                priceClientRetailMax = Double.max(priceClientRetailMax, sku.getPriceClientRetail());
                priceClientMsrpMin = Double.min(priceClientMsrpMin, sku.getPriceClientMsrp());
                priceClientMsrpMax = Double.max(priceClientMsrpMax, sku.getPriceClientMsrp());
            }

            cmsBtFeedInfoModel.setPriceClientMsrpMax(priceClientMsrpMax);
            cmsBtFeedInfoModel.setPriceClientRetailMax(priceClientRetailMax);
            cmsBtFeedInfoModel.setPriceClientRetailMin(priceClientRetailMin);
            cmsBtFeedInfoModel.setPriceClientMsrpMin(priceClientMsrpMin);
        }

        /*Double priceClientRetailMin = cmsBtFeedInfoModel.getSkus().get(0).getPriceClientRetail();
        Double priceClientMsrpMin = cmsBtFeedInfoModel.getSkus().get(0).getPriceClientMsrp();
        Double priceClientRetailMax = cmsBtFeedInfoModel.getSkus().get(0).getPriceClientRetail();
        Double priceClientMsrpMax = cmsBtFeedInfoModel.getSkus().get(0).getPriceClientMsrp();

        for (CmsBtFeedInfoModel_Sku sku : cmsBtFeedInfoModel.getSkus()) {
            try {
                if(Double.compare(sku.getPriceClientMsrp(),0.0) == 0){
                    sku.setPriceMsrp(0.0);
                }else{
                    sku.setPriceMsrp(calculatePrice(formulaMsrp, sku));
                }
                if(Double.compare(sku.getPriceClientRetail(),0.0) == 0 || Double.compare(sku.getPriceClientMsrp(),0.0) == 0){
                    sku.setPriceCurrent(0.0);
                }else{
                    sku.setPriceCurrent(calculatePrice(formulaMsrp, sku));
                }
                priceClientRetailMin = Double.min(priceClientRetailMin, sku.getPriceClientRetail());
                priceClientRetailMax = Double.max(priceClientRetailMax, sku.getPriceClientRetail());
                priceClientMsrpMin = Double.min(priceClientMsrpMin, sku.getPriceClientMsrp());
                priceClientMsrpMax = Double.max(priceClientMsrpMax, sku.getPriceClientMsrp());
            }catch (Exception ignored){

            }
        }
        cmsBtFeedInfoModel.setPriceClientMsrpMax(priceClientMsrpMax);
        cmsBtFeedInfoModel.setPriceClientRetailMax(priceClientRetailMax);
        cmsBtFeedInfoModel.setPriceClientRetailMin(priceClientRetailMin);
        cmsBtFeedInfoModel.setPriceClientMsrpMin(priceClientMsrpMin);*/

        return cmsBtFeedInfoModel;
    }

    private Double calculatePrice(String formula, CmsBtFeedInfoModel_Platform_Cart platformPrice) {
        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression(formula);

        StandardEvaluationContext context = new StandardEvaluationContext(platformPrice);

        try {
            BigDecimal price = expression.getValue(context, BigDecimal.class);

            return price.setScale(0, RoundingMode.UP).doubleValue();
        } catch (SpelEvaluationException sp) {
            $error("使用固定公式计算时出现错误", sp);
            throw new BusinessException("使用固定公式计算时出现错误", sp);
        }
    }

    private Double calculatePrice(String formula, CmsBtFeedInfoModel_Sku sku) {
        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression(formula);

        StandardEvaluationContext context = new StandardEvaluationContext(sku);

        try {
            BigDecimal price = expression.getValue(context, BigDecimal.class);

            return price.setScale(0, RoundingMode.UP).doubleValue();
        } catch (SpelEvaluationException sp) {
            $error("使用固定公式计算时出现错误", sp);
            throw new BusinessException("使用固定公式计算时出现错误", sp);
        }
    }

    /**
     * 根据Feed在MongoDB中的id查询Feed数据
     *
     * @param channelId 渠道ID
     * @param id        ID
     * @return CmsBtFeedInfoModel
     */
    public CmsBtFeedInfoModel getFeedById(String channelId, String id) {
        if (StringUtils.isNotBlank(id)) {
            return cmsBtFeedInfoDao.selectById(id, channelId);
        }
        return null;
    }

    /**
     * 条件查询feed商品列表
     */
    public List<CmsBtFeedInfoModel> getFeedList(Map<String, Object> searchValue, String channel) {
        //封装查询条件
        JongoQuery queryObject = getQuery(searchValue);
        //设置排序条件,排序字段后面拼接排序方式,下划线分割(1,正序,-1倒叙),默认不排序,点击那个按照哪个排序
        String sortName = (String) searchValue.get("sortName");
        String sortType = (String) searchValue.get("sortType");
        if (StringUtils.isNotEmpty(sortName) && StringUtils.isNotEmpty(sortType)) {
            String buffer = "{'" +
                    sortName +
                    "':" +
                    sortType +
                    "}";
            queryObject.setSort(buffer);
        }
        //封装分页条件
        Integer pageNum = (Integer) searchValue.get("curr");
        Integer pageSize = (Integer) searchValue.get("size");
        if (pageNum != null && pageSize != null) {
            queryObject.setSkip((pageNum - 1) * pageSize);
            queryObject.setLimit(pageSize);
        }
        return feedInfoService.getList(channel, queryObject);
    }

    public List<String> getFeedCodeList(Map<String, Object> searchValue, String channel) {
        //封装查询条件
        JongoQuery queryObject = getQuery(searchValue);
        List<CmsBtFeedInfoModel> feedList = feedInfoService.getList(channel, queryObject);
        if (ListUtils.notNull(feedList)) {
            return feedList.stream().map(CmsBtFeedInfoModel::getCode).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 查询feed商品总数
     */
    public Long getFeedCount(Map<String, Object> searchValue, String channel) {
        //封装查询条件
        JongoQuery query = getQuery(searchValue);
        return cmsBtFeedInfoDao.countByQuery(query.getQuery(), channel);
    }

    /**
     * 根据条件跟新feed信息
     */
    public WriteResult upDateFeedInfo(String channelId, Map queryMap, Map updateMap) {
        WriteResult writeResult = feedInfoService.updateFeedInfo(channelId, queryMap, updateMap);
        return writeResult;
    }

    //组装查询条件
    public JongoQuery getQuery(Map<String, Object> searchValue) {
        //封装查询条件
        Criteria criteria = new Criteria();
        //状态
        if (ListUtils.notNull((List) searchValue.get("status"))) {
            List status = (List) searchValue.get("status");
            criteria = criteria.and("status").in(status);
        }
        //设置开始和截止的时间
        if (searchValue.get("lastReceivedOnStart") != null && searchValue.get("lastReceivedOnEnd") == null) {
            criteria = criteria.and("lastReceivedOn").gte((String) searchValue.get("lastReceivedOnStart"));
        } else if (searchValue.get("lastReceivedOnEnd") != null && searchValue.get("lastReceivedOnStart") == null) {
            criteria = criteria.and("lastReceivedOn").lte((String) searchValue.get("lastReceivedOnEnd"));
        } else if (searchValue.get("lastReceivedOnEnd") != null && searchValue.get("lastReceivedOnStart") != null) {
            criteria = criteria.and("lastReceivedOn").gte((String) searchValue.get("lastReceivedOnStart")).lte((String) searchValue.get("lastReceivedOnEnd"));
        }
        //通过创建时间查询
        if (searchValue.get("createdStart") != null && searchValue.get("createdEnd") == null) {
            criteria = criteria.and("created").gte((String) searchValue.get("createdStart"));
        } else if (searchValue.get("createdEnd") != null && searchValue.get("createdStart") == null) {
            criteria = criteria.and("created").lte((String) searchValue.get("createdEnd"));
        } else if (searchValue.get("createdEnd") != null && searchValue.get("createdStart") != null) {
            criteria = criteria.and("created").gte((String) searchValue.get("createdStart")).lte((String) searchValue.get("createdEnd"));
        }
        //name模糊查询


        if (StringUtils.isNotEmpty((String) searchValue.get("name"))) {
            String name = (String) searchValue.get("name");
            String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(name);
            while (m.find()) {
                String group = m.group();
                name = name.replace(group, "\\" + group);

            }
            criteria = criteria.and("name").regex(name);

        }
        //多条件精确查询,SKU/ Barcode/ Code / Model

        if (StringUtils.isNotEmpty((String) searchValue.get("searchContent"))) {
            String searchContent = (String) searchValue.get("searchContent");
            String[] split = searchContent.split("\n");
            List<String> searchContents = Arrays.asList(split);
            criteria = criteria.orOperator(Criteria.where("code").in(searchContents), Criteria.where("model").in(searchContents), Criteria.where("skus.sku").in(searchContents), Criteria.where("skus.barcode").in(searchContents), Criteria.where("upc").in(searchContents));
        }

        if (ListUtils.notNull((List) searchValue.get("approvePricing"))) {
            criteria = criteria.and("approvePricing").in((List) searchValue.get("approvePricing"));
        }

        if (ListUtils.notNull((List) searchValue.get("codeList"))) {
            criteria = criteria.and("code").in(((List) searchValue.get("codeList")).toArray());
        }
        return new JongoQuery(criteria);
    }


    //==============================================================================================
    //===========================================任务分割线===========================================
    //==============================================================================================

    /**
     * 根据model查询符合特定条件的特定个数(暂定5)的code
     * <p>先查询product.platforms.Pxx.status in[Approved->Ready->Pending]</p>
     * <p>  其中xx为U.S.Official对应的cartId, 平台状态有优先级</p>
     * <p>如果product查不到满足条件的model信息，则从feed中查询model且status in[Approved->Ready->Pending->New]</p>
     * <p>  feed状态有优先级</p>
     *
     * @param channelId 渠道ID
     * @param code      Code
     * @param model     Feed->model
     * @param top       查询个数
     */
    public List<CmsBtFeedInfoModel> getTopFeedByModel(String channelId, String code, String model, int top) {
        if (top <= 0) top = 5;

        // 同一Model下的Product个数不太多，先查出来再排序筛选
        String query = "{\"channelId\": #, \"common.fields.model\": #}";
        JongoQuery jongoQuery = new JongoQuery();
        jongoQuery.setQuery(query);
        jongoQuery.setParameters(channelId, model);
        jongoQuery.setLimit(top);
        List<CmsBtProductModel> productModels = cmsBtProductDao.select(jongoQuery, channelId);
        if (CollectionUtils.isNotEmpty(productModels)) {
            // 选中同Model的某个Code,则执行覆盖操作,覆盖内容: 除了code, name, color, colorMap, 图片，urlKey以外的code级别属性
            // 覆盖内容: Brand,ProductType,SizeType,Material,Made In,Amazon Category,Usage,Short Description,Long Description,Order Limit Count,Abstract,Accessory
            return this.tempConvertToFeedInfo(productModels);
        } else {

            int count = 0;
            List<CmsConstants.UsaFeedStatus> queryFeedStatus = new ArrayList<>();
            queryFeedStatus.add(CmsConstants.UsaFeedStatus.Approved);
            queryFeedStatus.add(CmsConstants.UsaFeedStatus.Ready);
            queryFeedStatus.add(CmsConstants.UsaFeedStatus.Pending);
            queryFeedStatus.add(CmsConstants.UsaFeedStatus.New);

            List<CmsBtFeedInfoModel> resultFeedList = new ArrayList<>(top);
            query = "{\"channelId\":#,\"code\":{$ne:#},\"model\":#,\"status\":{$exists:true},\"status\":#}";
            for (CmsConstants.UsaFeedStatus feedStatus : queryFeedStatus) {
                jongoQuery = new JongoQuery(null, query, null, top - resultFeedList.size(), 0);
                jongoQuery.setParameters(channelId, code, model, feedStatus.name());
                List<CmsBtFeedInfoModel> tempResultFeedList = cmsBtFeedInfoDao.select(jongoQuery, channelId);
                count = tempResultFeedList.size();
                if (count > 0) {
                    resultFeedList.addAll(tempResultFeedList);
                }
                if (resultFeedList.size() >= top) {
                    break;
                }
            }
            return resultFeedList;
        }
    }

    /**
     * 抽取Product部分属性,将其临时封装为FeedInfo
     *
     * @param productModels 待处理Product数据
     */
    private List<CmsBtFeedInfoModel> tempConvertToFeedInfo(List<CmsBtProductModel> productModels) {
        List<CmsBtFeedInfoModel> feedInfoModels = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(productModels)) {
            CmsBtFeedInfoModel feed = null;
            for (CmsBtProductModel product : productModels) {
                CmsBtProductModel_Field fields = product.getCommon().getFields();
                feed = new CmsBtFeedInfoModel();
                feed.setChannelId(product.getChannelId());
                feed.setModel(fields.getModel());
                feed.setCode(fields.getCode());
                // Brand
                feed.setBrand(fields.getBrand());
                // ProductType
                feed.setProductType(fields.getProductType());
                // SizeType
                feed.setSizeType(fields.getSizeType());
                // Material
                feed.setMaterial(fields.getMaterialEn());
                // Made In
                feed.setOrigin(fields.getOrigin());
                // Usage
                feed.setUsageEn(fields.getUsageEn());
                // Short Description
                feed.setShortDescription(fields.getShortDesEn());
                // Long Description
                feed.setLongDescription(fields.getLongDesEn());
                // Feed状态
                if (product.getPlatform(1) != null) {
                    feed.setStatus(product.getPlatform(1).getStatus());
                }
                // Feed image取自Product iamges1
                List<CmsBtProductModel_Field_Image> productImages1 = fields.getImages1();
                if (CollectionUtils.isNotEmpty(productImages1)) {
                    List<String> feedImages = new ArrayList<>();
                    for (CmsBtProductModel_Field_Image image : productImages1) {
                        feedImages.add(image.getName());
                    }
                    feed.setImage(feedImages);
                }

                // 部分Code级别属性,Product没有,再去查询同Code Feed,Copy属性
                CmsBtFeedInfoModel refFeed = cmsBtFeedInfoDao.selectProductByCode(product.getChannelId(), fields.getCode());
                if (refFeed != null && refFeed.getModel().equals(fields.getModel())) {
                    // category
                    feed.setCategory(refFeed.getCategory());
                    // attribute.amazonBrowseTree、attribute.abstract、attribute.accessory、attribute.orderlimitcount
                    feed.setAttribute(refFeed.getAttribute());
                }

                feedInfoModels.add(feed);
            }
        }
        return feedInfoModels;
    }

    /**
     * 保存或者提交Feed
     *
     * @param channelId     渠道ID
     * @param feedInfoModel Feed信息
     * @param flag          是否是Submit至下一步,默认false
     * @param username      更新人
     * @return 最新的Feed
     */
    public CmsBtFeedInfoModel saveOrSubmitFeed(String channelId, CmsBtFeedInfoModel feedInfoModel, boolean flag, String username) {
        CmsBtFeedInfoModel feed = null;
        String code = null;
        if (feedInfoModel == null
                || StringUtils.isBlank(code = feedInfoModel.getCode())
                || (feed = cmsBtFeedInfoDao.selectProductByCode(channelId, code)) == null) {
            throw new BusinessException(String.format("Feed(Code:%s) not exists.", code));
        }


        // 1、如果页面提交过来的Feed状态和DB中的Feed状态不一致则说明DB中Feed信息已经被修改过了
        // 2、如果DB中的Feed状态已经是Approved,则无论是Save还是Submit操作都禁止,因为美国CMS2看不到Approved过的Feed
        // 以上两种Feed报警,无须操作了
        if (!Objects.equals(feed.getStatus(), feedInfoModel.getStatus()) || CmsConstants.UsaFeedStatus.Approved.name().equals(feed.getStatus())) {
            throw new BusinessException(String.format("Feed(Code:%s) status is already %s in DB.", code, feed.getStatus()));
        }

        // 保存 or 提交至下一步
        boolean isSave = !flag;
        if (isSave) {
            // New状态Save时如果urlKey不空则校验其唯一性
            if (CmsConstants.UsaFeedStatus.New.name().equals(feed.getStatus())
                    && MapUtils.isNotEmpty(feedInfoModel.getAttribute())
                    && CollectionUtils.isNotEmpty(feedInfoModel.getAttribute().get("urlKey"))
                    && StringUtils.isNotBlank(feedInfoModel.getAttribute().get("urlKey").get(0))) {
                // New状态Save时校验urlKey是否唯一
                if (this.isUrlKeyDuplicated(channelId, code, feedInfoModel.getAttribute().get("urlKey").get(0))) {
                    throw new BusinessException(String.format("URL Key(%s) already exists.", feedInfoModel.getAttribute().get("urlKey").get(0)));
                }
            }
        } else {
            // 美国CMS2 Feed状态流[New->Pending->Ready->Approved],校验其是否跨节点提交了
            String nextFeedStatus = null;
            if (CmsConstants.UsaFeedStatus.New.name().equals(feed.getStatus())) {
                nextFeedStatus = CmsConstants.UsaFeedStatus.Pending.name();
            } else if (CmsConstants.UsaFeedStatus.Pending.name().equals(feed.getStatus())) {
                nextFeedStatus = CmsConstants.UsaFeedStatus.Ready.name();
            } else if (CmsConstants.UsaFeedStatus.Ready.name().equals(feed.getStatus())) {
                nextFeedStatus = CmsConstants.UsaFeedStatus.Approved.name();
            }
            feedInfoModel.setStatus(nextFeedStatus);

            if (CmsConstants.UsaFeedStatus.Approved.name().equals(nextFeedStatus)) {
                // 如果是Submit下一步是Approved,更新updFlg=0
                feedInfoModel.setUpdFlg(0);
            }
        }

        feedInfoModel.getSkus().forEach(sku -> {

            sku.setWeightOrg(feedInfoModel.getWeight());
            sku.setWeightOrgUnit(feedInfoModel.getWeightUnit());

            if (StringUtil.isEmpty(sku.getWeightOrgUnit())) sku.setWeightOrgUnit("lb");
            if (!StringUtils.isEmpty(sku.getWeightOrg())) {
                if ("lb".equalsIgnoreCase(sku.getWeightOrgUnit())) {
                    sku.setWeightCalc(sku.getWeightOrg());
                } else if ("kg".equalsIgnoreCase(sku.getWeightOrgUnit())) {
                    Double weight = NumberUtils.toDouble(sku.getWeightOrg());
                    BigDecimal b = new BigDecimal(weight * 2.204623);
                    sku.setWeightCalc(b.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                }
            }
        });
        feedInfoModel.setModifier(username);
        feedInfoModel.setModified(DateTimeUtil.getNow());
        WriteResult writeResult = cmsBtFeedInfoDao.update(feedInfoModel);
        $info(String.format("(%s)Save Feed(channelId=%s,code=%s)结果: %s", username, channelId, code, JacksonUtil.bean2Json(writeResult)));
        return cmsBtFeedInfoDao.selectById(feed.get_id(), channelId);
    }

    /**
     * 判断urlKey在Feed中是否存在
     *
     * @param channelId 渠道ID
     * @param urlKey    urlKey
     * @return urlKey是否已存在
     */
    public boolean isUrlKeyDuplicated(String channelId, String code, String urlKey) {
        String query = "{\"channelId\":#,\"code\":{$ne:#},\"attribute.urlkey\":#}";
        return cmsBtFeedInfoDao.countByQuery(query, new Object[]{channelId, code, urlKey}, channelId) > 0;
    }

    /**
     * 保存要Approve的Feed信息
     *
     * @param channelId   渠道ID
     * @param codeList    codeList
     * @param approveInfo 各平台Approve信息
     * @param username    修改人
     */
    public void approve(String channelId, List<String> codeList, List<Map<String, Object>> approveInfo, String username) {
        if (StringUtils.isBlank(channelId) || CollectionUtils.isEmpty(codeList)) {
            throw new BusinessException("Approve parameter error.");
        }

        JongoUpdate jongoUpdate = new JongoUpdate();
        jongoUpdate.setQuery("{\"channelId\":#,\"code\":{$in:#}}");
        jongoUpdate.setQueryParameters(channelId, codeList);

        StringBuffer sb = new StringBuffer();
        sb.append("{\"updFlg\":0,\"status\":#,\"modifier\":#,\"modified\":#");
        List<Object> updateParameters = new ArrayList<>();
        updateParameters.add(CmsConstants.UsaFeedStatus.Approved.name());
        updateParameters.add(username);
        updateParameters.add(DateTimeUtil.getNow());
        for (Map<String, Object> approveMap : approveInfo) {
            Integer cartId = (Integer) approveMap.get("cartId");
            Integer day = (Integer) approveMap.get("day");
            Boolean checked = (Boolean) approveMap.get("checked");
            if (cartId == null) continue;
            if (day == null || day < 0) {
                day = 0;
            }

            if (cartId < 20) {
                sb.append(",\"usPlatforms.P" + cartId + ".isSale\":#");
                sb.append(",\"usPlatforms.P" + cartId + ".sharingDay\":#");
                updateParameters.add((checked != null && checked.booleanValue()) ? 1 : 0);
                updateParameters.add(day);
            } else {
                sb.append(",\"platforms.P" + cartId + ".isSale\":#");
                updateParameters.add((checked != null && checked.booleanValue()) ? 1 : 0);
            }
        }
        sb.append("}");

        jongoUpdate.setUpdate("{$set:" + sb.toString() + "}");
        jongoUpdate.setUpdateParameters(updateParameters.toArray());

        WriteResult writeResult = cmsBtFeedInfoDao.updateMulti(jongoUpdate, channelId);
        $info(String.format("(%s)批量Approve Feed, 结果:%s", username, JacksonUtil.bean2Json(writeResult)));
    }

    /**
     * 批量修改ApprovePricing
     *
     * @param channelId 渠道ID
     * @param codeList  CodeList
     * @param username  修改人
     */
    public void bulkApprovePricing(String channelId, List<String> codeList, String username) {
        if (StringUtils.isNotBlank(channelId) && CollectionUtils.isNotEmpty(codeList)) {

            JongoUpdate jongoUpdate = new JongoUpdate();
            // usPlatforms.P8.priceClientMsrp 和 usPlatforms.P8.priceClientRetail 必须大于0才能设置approvePricing=1
            jongoUpdate.setQuery("{\"channelId\":#,\"code\":{$in:#}" +
                    ",\"usPlatforms.P8.priceClientMsrp\":{$exists:true},\"usPlatforms.P8.priceClientMsrp\":{$gt:0}" +
                    ",\"usPlatforms.P8.priceClientRetail\":{$exists:true},\"usPlatforms.P8.priceClientRetail\":{$gt:0}}");
            jongoUpdate.setQueryParameters(channelId, codeList);

            StringBuffer sb = new StringBuffer();
            sb.append("{\"approvePricing\":#,\"modifier\":#,\"modified\":#}");
            List<Object> updateParameters = new ArrayList<>();
            updateParameters.add("1");
            updateParameters.add(username);
            updateParameters.add(DateTimeUtil.getNow());

            jongoUpdate.setUpdate("{$set:" + sb.toString() + "}");
            jongoUpdate.setUpdateParameters(updateParameters.toArray());

            WriteResult writeResult = cmsBtFeedInfoDao.updateMulti(jongoUpdate, channelId);
            $info(String.format("(%s)ApprovePricing Feed, 结果: %s", username, JacksonUtil.bean2Json(writeResult)));
        }
    }

}
