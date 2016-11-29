package com.voyageone.service.impl.cms.tools.product;

import com.voyageone.base.dao.mongodb.JongoAggregate;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CustomPropBean;
import com.voyageone.service.bean.cms.feed.FeedCustomPropWithValueBean;
import com.voyageone.service.bean.cms.translation.TaskSummaryBean;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean_CommonFields;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean_GroupProduct;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.feed.FeedCustomPropService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * Created by Ethan Shi on 2016/6/28.
 *
 * @author Ethan Shi
 * @version 2.2.0
 * @since 2.2.0
 */

@Service
public class TranslationTaskService extends BaseService {

    private static final int EXPIRE_HOURS = -48;
    @Autowired
    CmsBtFeedInfoDao cmsBtFeedInfoDao;
    @Autowired
    FeedCustomPropService customPropService;
    @Autowired
    ProductGroupService productGroupService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtProductGroupDao CmsBtProductGroupDao;
    @Autowired
    private ProductService productService;

    /**
     * 计算翻译任务的汇总信息
     */
    public TaskSummaryBean getTaskSummary(String channelId, String userName) throws BusinessException {
        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), EXPIRE_HOURS);
        String translateTimeStr = DateTimeUtil.format(date, null);

        TaskSummaryBean taskSummary = new TaskSummaryBean();

        //未分配的任务
        String queryStr = String.format("{'$and':[ {'lock':'0'},{'common.fields.isMasterMain':1}," +
                "{'common.fields.translateStatus': {'$ne' : '1' }}, " +
                "{'$or': [{'common.fields.translator':''} ,  " +
                "{'common.fields.translator':null}, " +
                "{'common.fields.translateTime':{'$lte':'%s'}} , " +
                "{'common.fields.translateTime':''}, " +
                "{'common.fields.translateTime': null}]}]}", translateTimeStr);

        taskSummary.setUnassginedCount(cmsBtProductDao.countByQuery(queryStr, channelId));

        //已分配但未完成的任务
        queryStr = String.format("{'common.fields.isMasterMain':1," +
                "'common.fields.translateStatus':'0'," +
                "'common.fields.translator':{'$ne' : null}," +
                "'common.fields.translator':{'$ne' : ''}," +
                "'common.fields.translateTime':{'$gt':'%s'}}", translateTimeStr);
        taskSummary.setIncompleteCount(cmsBtProductDao.countByQuery(queryStr, channelId));

        //完成翻译总商品数
        queryStr = "{'common.fields.isMasterMain':1," +
                "'common.fields.translateStatus':'1'}";
        taskSummary.setCompleteCount(cmsBtProductDao.countByQuery(queryStr, channelId));

        //个人完成翻译商品数
        queryStr = String.format("{'common.fields.isMasterMain':1," +
                "'common.fields.translateStatus':'1'," +
                "'common.fields.translator':'%s'}", userName);
        taskSummary.setUserCompleteCount(cmsBtProductDao.countByQuery(queryStr, channelId));
        return taskSummary;
    }


    /**
     * 按proId获取翻译任务详情
     */
    public TranslationTaskBean getTaskById(String channelId, String userName, long prodId) throws BusinessException {

        String queryStr = String.format("{'common.fields.isMasterMain':1, " +
                "'prodId':%s, " +
                "'common.fields.translator':'%s' " +
                " }", prodId, userName);

        CmsBtProductModel product = cmsBtProductDao.selectOneWithQuery(queryStr, channelId);

        return fillTranslationTaskBean(product);
    }


    /**
     * assignTask
     */
    public TranslationTaskBean assignTask(String channelId, String userName, String priority, String sort, String
            keyWord) throws BusinessException {
        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), EXPIRE_HOURS);
        String translateTimeStr = DateTimeUtil.format(date, null);

        //先查是否有任务未完成
        String queryStr = String.format("{'lock':'0','common.fields.isMasterMain':1," +
                "'common.fields.translateStatus':{$in:['0','2']}," +
                "'common.fields.translator':'%s', " +
                "'common.fields.translateTime':{'$gt':'%s'} }", userName, translateTimeStr);

        long cnt = cmsBtProductDao.countByQuery(queryStr, channelId);
        if (cnt > 0) {
            throw new BusinessException("当前任务还未完成，不能领取新的任务！");
        }

        //再查当前用户是否有过期任务，优先分配过期任务
        queryStr = String.format("{'lock':'0','common.fields.isMasterMain':1," +
                "'common.fields.translateStatus':{$in:['0','2']}," +
                "'common.fields.translator':'%s'}", userName);

        CmsBtProductModel product = cmsBtProductDao.selectOneWithQuery(queryStr, channelId);
        List<Map<String, Object>> mapList = null;
        if (product == null) {
            //没有过期任务，按分发规则分配
            List<JongoAggregate> aggregateList = new ArrayList<JongoAggregate>();
            aggregateList.add(new JongoAggregate("{ $match : {\"lock\":\"0\", \"common.fields.translateStatus\":\"0\"}}"));
            aggregateList.add(new JongoAggregate("{ $match : {$or : [{\"common.fields.translator\" : \"\"}, {\"common.fields.translateTime\" : {$lte : #}}, {\"common.fields.translator\" : null}, {\"common.fields.translateTime\" : null}, {\"common.fields.translateTime\" : \"\"}]}}", translateTimeStr));
            if (!StringUtils.isNullOrBlank2(keyWord)) {
                List<String> codeList = Arrays.asList(keyWord.split("\n"));
                if (codeList.size() == 1) {
                    aggregateList.add(new JongoAggregate("{ $match : {$or : [{\"common.fields.code\" : #}, {\"common.fields.productNameEn\" : {$regex : #}}, {\"common.fields.originalTitleCn\" : {$regex : #}}]}}", keyWord, keyWord, keyWord));
                } else {
                    aggregateList.add(new JongoAggregate("{ $match : {\"common.fields.code\" : {$in : #}}}", codeList));
                }
            }

            if (!StringUtils.isNullOrBlank2(priority)) {
                if ("quantity".equalsIgnoreCase(priority)) {
                    aggregateList.add(new JongoAggregate("{ $group : {_id : \"$platforms.P0.mainProductCode\", totalQuantity : {$sum : \"$common.fields.quantity\"}, codeCnt : {$sum : 1}}}"));
                    if ("asc".equalsIgnoreCase(sort)) {
                        aggregateList.add(new JongoAggregate("{ $sort : {\"totalQuantity\" : 1}}"));
                    } else {
                        aggregateList.add(new JongoAggregate("{ $sort : {\"totalQuantity\" : -1}}"));
                    }
                    aggregateList.add(new JongoAggregate("{ $limit : 1}"));
                    mapList = cmsBtProductDao.aggregateToMap(channelId, aggregateList);
                } else if ("priority".equalsIgnoreCase(priority)) {
                    aggregateList.remove(0);
                    aggregateList.add(0, new JongoAggregate("{ $match : {\"lock\" : \"0\", \"common.fields.translateStatus\":\"2\", \"common.fields.priorTranslateDate\" : {$nin : [null, \"\"]}}}"));
                    aggregateList.add(new JongoAggregate("{ $group : {_id : \"$platforms.P0.mainProductCode\", totalQuantity : {$sum : \"$common.fields.quantity\"}, codeCnt : {$sum : 1}}}"));
                    aggregateList.add(new JongoAggregate("{ $sort : {\"common.fields.priorTranslateDate\" : 1, \"totalQuantity\" : -1}}"));
                    aggregateList.add(new JongoAggregate("{ $limit : 1}"));
                    mapList = cmsBtProductDao.aggregateToMap(channelId, aggregateList);
                    // 如果优先翻译的数据,并且设置了翻译时间的获取不到,则获取未设置时间的任务
                    if (CollectionUtils.isEmpty(mapList)) {
                        aggregateList.remove(0);
                        aggregateList.add(0, new JongoAggregate("{ $match : {\"lock\" : \"0\", \"common.fields.translateStatus\":\"2\", \"common.fields.priorTranslateDate\" : {$in : [null, \"\"]}}}"));
                        aggregateList.add(new JongoAggregate("{ $sort : {\"totalQuantity\" : -1}}"));
                        mapList = cmsBtProductDao.aggregateToMap(channelId, aggregateList);
                        // 如果优先翻译的数据获取不到,则通过未翻译获取
                        if (CollectionUtils.isEmpty(mapList)) {
                            aggregateList.remove(0);
                            aggregateList.add(0, new JongoAggregate("{ $match : {\"lock\":\"0\", \"common.fields.translateStatus\":\"0\"}}"));
                            mapList = cmsBtProductDao.aggregateToMap(channelId, aggregateList);
                        }
                    }
                }
            }

            // 如果按照现有条件未获取到翻译任务,并且没有输入 模糊查询
            if (CollectionUtils.isEmpty(mapList) && !StringUtils.isNullOrBlank2(keyWord)) {
                throw new BusinessException("根据输入的模糊查询["+keyWord+"]无法获取未翻译任务!");
            }
        }
        if (product == null && (CollectionUtils.isEmpty(mapList) || mapList.get(0) == null)) {
            throw new BusinessException("当前没有待领取的翻译任务！");
        }

        if (product == null) {
            Map<String, Object> map = mapList.get(0);
            product = cmsBtProductDao.selectByCode( map.get("_id").toString(), channelId);
        }
        // 查找Group和同组商品信息
        String code = product.getPlatform(0).getMainProductCode();
        String queryGroupStr = String.format("{'mainProductCode' : '%s', 'cartId':0}", code);
        CmsBtProductGroupModel group = CmsBtProductGroupDao.selectOneWithQuery(queryGroupStr, channelId);
        if (group == null) {
            throw new BusinessException("无法找到主产品为[" + code + "]的产品组!");
        }
        // 有任务，修改任务状态
        Map<String, Object> rsMap = new HashMap<>();
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", product.getProdId());
        rsMap.put("common.fields.translator", userName);
        rsMap.put("common.fields.translateTime", DateTimeUtil.getNow());
        if (!"2".equals(product.getCommonNotNull().getFieldsNotNull().getTranslateStatus())) {
            rsMap.put("common.fields.translateStatus", '0');
        }

        rsMap.put("modifier", userName);
        rsMap.put("modified", DateTimeUtil.getNow());
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("$set", rsMap);

        cmsBtProductDao.update(channelId, queryMap, updateMap);
        //查找产品组
        List<CmsBtProductModel> productList = cmsBtProductDao.selectProductByCodes(group.getProductCodes(), channelId);
        return fillTranslationTaskBean(product, productList);
    }

    /**
     * 取当前任务
     */
    public TranslationTaskBean getCurrentTask(String channelId, String userName) throws BusinessException {
        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), EXPIRE_HOURS);
        String translateTimeStr = DateTimeUtil.format(date, null);

        String queryStr = String.format("{'common.fields.isMasterMain':1," +
                "'common.fields.translateStatus':{$in:['0', '2']}," +
                "'common.fields.translator':'%s', " +
                "'common.fields.translateTime':{'$gt':'%s'} }", userName, translateTimeStr);

        CmsBtProductModel product = cmsBtProductDao.selectOneWithQuery(queryStr, channelId);
        List<CmsBtProductModel> groupProducts = null;
        if (product != null) {
            String mainProductCode = product.getPlatformNotNull(0).getMainProductCode();
            String queryGroupStr = String.format("{'mainProductCode' : '%s', 'cartId':0}", mainProductCode);
            CmsBtProductGroupModel group = CmsBtProductGroupDao.selectOneWithQuery(queryGroupStr, channelId);
            groupProducts = cmsBtProductDao.selectProductByCodes(group.getProductCodes(), channelId);
        }
        return fillTranslationTaskBean(product, groupProducts);
    }


    /**
     * 保存翻译任务
     */
    public TranslationTaskBean saveTask(TranslationTaskBean bean, String channelId, String userName, String status)
            throws BusinessException {

        String mainCode = bean.getProductCode();
        String queryStr = String.format("{'mainProductCode' : '%s', 'cartId':0}", mainCode);
        CmsBtProductGroupModel group = CmsBtProductGroupDao.selectOneWithQuery(queryStr, channelId);

        if (group == null) {
            throw new BusinessException("无法找打主产品为[" + mainCode + "]的产品组!");
        }

        TranslationTaskBean_CommonFields cnFields = bean.getCommonFields();

        //读cms_mt_feed_custom_prop
        List<FeedCustomPropWithValueBean> feedCustomPropList = customPropService.getPropList(channelId, bean
                .getFeedCategory());

        //去除掉feedCustomPropList中的垃圾数据
        if (feedCustomPropList != null && !feedCustomPropList.isEmpty()) {
            feedCustomPropList = feedCustomPropList.stream()
                    .filter(w -> !StringUtils.isNullOrBlank2(w.getFeed_prop_translation()) && !StringUtils
                            .isNullOrBlank2(w.getFeed_prop_original()))
                    .collect(Collectors.toList());
        } else {
            feedCustomPropList = new ArrayList<>();
        }
        Set<String> custPropKeySet = feedCustomPropList.stream().map
                (FeedCustomPropWithValueBean::getFeed_prop_original).collect(Collectors.toSet());
        //根据feedCustomPropList精简cnProps
        List<CustomPropBean> cnProps = bean.getCustomProps();
        if (custPropKeySet != null && !custPropKeySet.isEmpty()) {
            cnProps = cnProps.stream().filter(w -> custPropKeySet.contains(w.getFeedAttrEn())).collect(Collectors.toList());
        }


        //查找产品组
        List<CmsBtProductModel> productList = cmsBtProductDao.selectProductByCodes(group.getProductCodes(),
                channelId);

        for (CmsBtProductModel product : productList) {
            Map<String, Object> rsMap = new HashMap<>();

            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("prodId", product.getProdId());

            rsMap.put("common.fields.shortDesCn", cnFields.getShortDesCn());
            rsMap.put("common.fields.longDesCn", cnFields.getLongDesCn());
            rsMap.put("common.fields.originalTitleCn", cnFields.getOriginalTitleCn());
            rsMap.put("common.fields.materialCn", cnFields.getMaterialCn());
            rsMap.put("common.fields.usageCn", cnFields.getUsageCn());
            rsMap.put("common.fields.origin", cnFields.getOrigin());
            rsMap.put("common.fields.translator", userName);
            rsMap.put("common.fields.translateTime", DateTimeUtil.getNow());
            // 设置翻译状态
            rsMap.put("common.fields.translateStatus", status);
            if ("2".equals(product.getCommonNotNull().getFieldsNotNull().getTranslateStatus())) {
                // 如果原来的翻译状态是'优先翻译'，则清空'优先翻译日期'
                rsMap.put("common.fields.priorTranslateDate", "");
            }

            rsMap.put("modifier", userName);
            rsMap.put("modified", DateTimeUtil.getNow());

            if (cnProps != null) {
//                    rsMap.put("feed.customIds", cnProps.stream().map(CustomPropBean::getFeedAttrEn)
//                            .collect(Collectors.toList()));
//                    rsMap.put("feed.customIdsCn", cnProps.stream().map
//                            (CustomPropBean::getFeedAttrCn).collect(Collectors.toList()));
                rsMap.put("feed.orgAtts", cnProps.stream().collect(toMap(CustomPropBean::getFeedAttrEn, CustomPropBean::getFeedAttrValueEn)));
                rsMap.put("feed.cnAtts", cnProps.stream().collect(toMap(CustomPropBean::getFeedAttrEn, CustomPropBean::getFeedAttrValueCn)));
            }

            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("$set", rsMap);

            cmsBtProductDao.update(channelId, queryMap, updateMap);

        }

        return getTaskById(channelId, userName, bean.getProdId());
    }


    /**
     * 检索用户的历史任务
     */
    public Map<String, Object> searchTask(int pageNum, int pageSize, String keyWord, String channelId, String
            userName, String status) throws BusinessException {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), EXPIRE_HOURS);
        String translateTimeStr = DateTimeUtil.format(date, null);

        JongoQuery queryObj = new JongoQuery();
        queryObj.addQuery("{'lock':'0'}");
        queryObj.addQuery("{'common.fields.isMasterMain':1}");
        queryObj.addQuery("{'common.fields.translator':#}");
        queryObj.addParameters(userName);
        if (!StringUtils.isNullOrBlank2(keyWord)) {
            queryObj.addQuery("{'$or':[ {'common.fields.code':#},{'common.fields.productNameEn':{'$regex': #}}," +
                    "{'common.fields.originalTitleCn':{'$regex': #}}]}");
            queryObj.addParameters(keyWord, keyWord, keyWord);
        }

        //不能有2个or
        if (StringUtils.isNullOrBlank2(status)) {
            queryObj.addQuery("{'$or':[{'common.fields.translateStatus':'0','common.fields.translateTime':{'$gt':#}},{'common.fields.translateStatus':'1'}]}");
            queryObj.addParameters(translateTimeStr);
        } else if ("1".equals(status)) {
            queryObj.addQuery("{'common.fields.translateStatus':'1'}");
        } else if ("0".equals(status)) {
            queryObj.addQuery("{'common.fields.translateStatus':'0'}");
            queryObj.addQuery("{'common.fields.translateTime':{'$gt':#}}");
            queryObj.addParameters(translateTimeStr);
        }

        queryObj.setSort("{'common.fields.translateTime' : -1}");

        queryObj.setSkip(pageSize * (pageNum - 1)).setLimit(pageSize);

        List<CmsBtProductModel> products = cmsBtProductDao.select(queryObj, channelId);

        long total = cmsBtProductDao.countByQuery(queryObj.getQuery(), queryObj.getParameters(), channelId);

        if (products != null && !products.isEmpty()) {
            for (CmsBtProductModel product : products) {
                HashMap<String, Object> map = new HashMap<>();
                CmsBtProductModel_Field fields = product.getCommon().getFields();

                map.put("prodId", product.getProdId());
                map.put("translateStatus", fields.getTranslateStatus() == null ? "0" : fields.getTranslateStatus());
                List<CmsBtProductModel_Field_Image> img1 = fields.getImages1();
                if (img1 != null && !img1.isEmpty()) {
                    map.put("image1", img1.get(0).getName());
                } else {
                    map.put("image1", "");
                }
                map.put("feedCategory", product.getFeed().getCatPath());
                map.put("code", fields.getCode());
                map.put("productName", StringUtils.isNullOrBlank2(fields.getOriginalTitleCn()) ? fields.getProductNameEn() : fields.getOriginalTitleCn());
                map.put("catPath", product.getCommon().getCatPath() == null ? "" : product.getCommon().getCatPath());
                map.put("translator", fields.getTranslator());
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtil.DEFAULT_DATETIME_FORMAT);
                    Date translateTime = sdf.parse(fields.getTranslateTime());
                    map.put("translateTime", translateTime.getTime());
                } catch (Exception e) {
                    map.put("translateTime", getString(fields.getTranslateTime()));
                }
                list.add(map);
            }
        }
        result.put("taskList", list);
        result.put("total", total);
        return result;
    }

    private String getString(String str) {
        return StringUtils.isNullOrBlank2(str) ? "" : str;
    }

    /**
     * 装填TranslationTaskBean
     */
    private TranslationTaskBean fillTranslationTaskBean(CmsBtProductModel product) {


        if (product != null) {
            TranslationTaskBean translationTaskBean = new TranslationTaskBean();
            //装填Bean
            translationTaskBean.setProdId(product.getProdId());
            translationTaskBean.setFeedCategory(getString(product.getFeed().getCatPath()));

            CmsBtProductModel_Field fields = product.getCommon().getFields();
            translationTaskBean.setCatPath(getString(product.getCommon().getCatPath()));
            translationTaskBean.setProductCode(getString(fields.getCode()));
            TranslationTaskBean_CommonFields commonFields = new TranslationTaskBean_CommonFields();
            commonFields.setBrand(getString(fields.getBrand()));
            commonFields.setProductNameEn(getString(fields.getProductNameEn()));
            commonFields.setOriginalTitleCn(getString(fields.getOriginalTitleCn()));
            commonFields.setMaterialEn(getString(fields.getMaterialEn()));
            commonFields.setMaterialCn(getString(fields.getMaterialCn()));
            commonFields.setOrigin(getString(fields.getOrigin()));
            commonFields.setShortDesEn(getString(fields.getShortDesEn()));
            commonFields.setShortDesCn(getString(fields.getShortDesCn()));
            commonFields.setLongDesEn(getString(fields.getLongDesEn()));
            commonFields.setLongDesCn(getString(fields.getLongDesCn()));
            commonFields.setUsageEn(getString(fields.getUsageEn()));
            commonFields.setUsageCn(getString(fields.getUsageCn()));
            List<CmsBtProductModel_Field_Image> img1 = fields.getImages1();
            if (img1 != null && !img1.isEmpty()) {
                commonFields.setImages1(img1);
            }

            List<CmsBtProductModel_Field_Image> img2 = fields.getImages1();
            if (img2 != null && !img2.isEmpty()) {
                commonFields.setImages3(img1);
            }

            List<CmsBtProductModel_Field_Image> img3 = fields.getImages1();
            if (img3 != null && !img3.isEmpty()) {
                commonFields.setImages3(img3);
            }

            List<CmsBtProductModel_Field_Image> img4 = fields.getImages1();
            if (img4 != null && !img4.isEmpty()) {
                commonFields.setImages4(img4);
            }

            List<CmsBtProductModel_Field_Image> img5 = fields.getImages1();
            if (img5 != null && !img5.isEmpty()) {
                commonFields.setImages5(img5);
            }

            List<CmsBtProductModel_Field_Image> img6 = fields.getImages1();
            if (img6 != null && !img6.isEmpty()) {
                commonFields.setImages6(img6);
            }

            List<CmsBtProductModel_Field_Image> img7 = fields.getImages1();
            if (img7 != null && !img7.isEmpty()) {
                commonFields.setImages7(img7);
            }

            List<CmsBtProductModel_Field_Image> img8 = fields.getImages1();
            if (img8 != null && !img8.isEmpty()) {
                commonFields.setImages8(img8);
            }

            commonFields.setClientProductUrl(getString(fields.getClientProductUrl()));
            commonFields.setTranslator(getString(fields.getTranslator()));
            commonFields.setTranslateStatus(getString(fields.getTranslateStatus()));
            commonFields.setTranslateTime(getString(fields.getTranslateTime()));
            translationTaskBean.setCommonFields(commonFields);

            List<CustomPropBean> props = productService.getCustomProp(product);
            translationTaskBean.setCustomProps(props);

            return translationTaskBean;
        }
        return null;
    }

    /**
     * 重载：装填TranslationTaskBean
     * @param product
     * @param groupProducts
     * @return
     * @author rex.wu
     */
    private TranslationTaskBean fillTranslationTaskBean(CmsBtProductModel product, List<CmsBtProductModel> groupProducts) {
        TranslationTaskBean translationTaskBean = this.fillTranslationTaskBean(product);
        if (translationTaskBean != null && CollectionUtils.isNotEmpty(groupProducts)) {
            List<TranslationTaskBean_GroupProduct> groupProductBeans = new ArrayList<TranslationTaskBean_GroupProduct>();
            TranslationTaskBean_GroupProduct groupProductBean = null;
            for (CmsBtProductModel groupModel:groupProducts) {
                groupProductBean = new TranslationTaskBean_GroupProduct();
                groupProductBean.setCode(groupModel.getCommon().getFields().getCode());
                groupProductBean.setIamge(groupModel.getCommon().getFields().getImages1().get(0).get("image1").toString());
                groupProductBean.setInventory(groupModel.getCommon().getFields().getQuantity());
                groupProductBeans.add(groupProductBean);
            }
            translationTaskBean.setGroupProducts(groupProductBeans);
        }
        return translationTaskBean;
    }

}
