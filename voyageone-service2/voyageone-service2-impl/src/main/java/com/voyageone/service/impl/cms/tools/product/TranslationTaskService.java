package com.voyageone.service.impl.cms.tools.product;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CustomPropBean;
import com.voyageone.service.bean.cms.feed.FeedCustomPropWithValueBean;
import com.voyageone.service.bean.cms.translation.TaskSummaryBean;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean_CommonFields;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean_GroupProduct;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * 翻译任务相关
 *
 * @author Ethan Shi
 * @version 2.2.0
 * @author Edward.lin(翻译任务重新修改)
 * @since 2.12.0
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
    CmsBtProductDao cmsBtProductDao;
    @Autowired
    ProductService productService;

    /**
     * 计算翻译任务的汇总信息
     * @param channelId 店铺Id
     * @param userName 操作者
     * @return TaskSummaryBean
     * @throws BusinessException
     */
    public TaskSummaryBean getTaskSummary(String channelId, String userName) throws BusinessException {
        TaskSummaryBean taskSummary = new TaskSummaryBean();

        //未分配的任务
        String queryStr = "{\"lock\": {$in: [\"0\", null]}" +
                ", \"common.fields.isMasterMain\":1" +
                ", \"common.fields.translateStatus\": {$ne : \"1\"}" +
                ", \"common.fields.translator\":{$in: [\"\", null]}}";
        taskSummary.setUnassginedCount(cmsBtProductDao.countByQuery(queryStr, channelId));

        //已分配但未完成的任务
        queryStr = "{\"lock\":{$in: [\"0\", \"1\", null]}" +
                ", \"common.fields.isMasterMain\":1" +
                ", \"common.fields.translateStatus\": {$ne : \"1\"}" +
                ", \"common.fields.translator\":{$nin : [\"\", null]}}";
        taskSummary.setIncompleteCount(cmsBtProductDao.countByQuery(queryStr, channelId));

        //完成翻译总商品数
        queryStr = "{\"lock\": {$in: [\"0\", \"1\", null]}" +
                ", \"common.fields.isMasterMain\":1" +
                ", \"common.fields.translateStatus\":\"1\"}";
        taskSummary.setCompleteCount(cmsBtProductDao.countByQuery(queryStr, channelId));

        //个人完成翻译商品数
        queryStr = String.format("{\"lock\": {$in: [\"0\", \"1\", null]}" +
                ", \"common.fields.isMasterMain\":1" +
                ", \"common.fields.translateStatus\":\"1\"" +
                ", \"common.fields.translator\": \"%s\"}", userName);
        taskSummary.setUserCompleteCount(cmsBtProductDao.countByQuery(queryStr, channelId));
        return taskSummary;
    }

    /**
     * 按proId获取翻译任务详情
     * @param channelId 店铺Id
     * @param userName 操作者
     * @param prodId 产品Id
     * @return TranslationTaskBean
     * @throws BusinessException
     */
    public TranslationTaskBean getTaskById(String channelId, String userName, long prodId) throws BusinessException {

        JongoQuery query = new JongoQuery();
        query.setQuery("{\"common.fields.isMasterMain\":1, \"prodId\": #, \"common.fields.translator\": #}");
        query.setParameters(prodId, userName);
        CmsBtProductModel product = cmsBtProductDao.selectOneWithQuery(query, channelId);
        List<CmsBtProductModel> groupProducts = null;
        if (product != null) {
            JongoQuery mainProductQuery = new JongoQuery();
            mainProductQuery.setQuery("{\"platforms.P0.mainProductCode\": #}");
            mainProductQuery.setParameters(product.getPlatformNotNull(0).getMainProductCode());
            groupProducts = cmsBtProductDao.select(mainProductQuery, channelId);
        }
        return fillTranslationTaskBean(product, groupProducts);
    }

    /**
     * 获取任务
     * @param channelId 店铺Id
     * @param userName 操作者
     * @param priority 分发规则
     * @param sort 按库存分发时,排序规则
     * @param keyWord 模糊查询
     * @return TranslationTaskBean
     * @throws BusinessException
     */
    public TranslationTaskBean assignTask(String channelId, String userName, String priority, String sort, String
            keyWord) throws BusinessException {
        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), EXPIRE_HOURS);
        String translateTimeStr = DateTimeUtil.format(date, null);

        // 先查该用户当前是否有任务未完成
        String queryStr = String.format("{\"lock\":\"0\"" +
                ", \"common.fields.isMasterMain\":1" +
                ", \"common.fields.translateStatus\":{$in:[\"0\",\"2\"]}" +
                ", \"common.fields.translator\":\"%s\"" +
                ", \"common.fields.translateTime\":{\"$gt\":\"%s\"}" +
                "}", userName, translateTimeStr);
        long cnt = cmsBtProductDao.countByQuery(queryStr, channelId);
        if (cnt > 0) {
            throw new BusinessException("当前任务还未完成，不能领取新的任务！");
        }

        // 再查该用户是否有过期未翻译完成的任务，优先分配过期任务
        queryStr = String.format("{\"lock\":\"0\"" +
                        ", \"common.fields.isMasterMain\":1" +
                        ", \"common.fields.translateStatus\":{$in:[\"0\",\"2\"]}" +
                        ", \"common.fields.translator\":\"%s\"}"
                , userName);
        CmsBtProductModel product = cmsBtProductDao.selectOneWithQuery(queryStr, channelId);

        // 分配新翻译任务(所有获取翻译任务,末尾都在找库存多优先的原则)
        if (product == null) {


            // 判断是否存在输入指定code列表获取翻译任务
            List<String> codeList = null;
            if (!StringUtils.isNullOrBlank2(keyWord)) {
                codeList = Arrays.asList(keyWord.split("\n"));
                codeList = getMainProductCode(channelId, codeList);
            }

            // 如果未选择翻译条件,则默认按照[优先翻译]原则获取翻译任务
            if (StringUtils.isNullOrBlank2(priority)) {
                priority = "priority";
            }

            // 根据主商品的[库存优选]原则获取翻译任务
            if ("quantity".equalsIgnoreCase(priority)) {

                // 获取翻译任务(主商品)
                JongoQuery query = new JongoQuery();
                query.setQuery("{\"lock\": \"0\"" +
                        ", \"common.fields.isMasterMain\": 1" +
                        ", \"common.fields.translateStatus\": {$in: [\"0\", \"2\", null]}" +
                        ", \"common.fields.translator\": {$in: [\"\", null]}" +
                        (codeList != null ? ", \"common.fields.code\": {$in: #}" : "") +
                        "}");
                if (codeList != null)
                    query.setParameters(codeList);
                if ("asc".equalsIgnoreCase(sort)) {
                    query.setSort("{\"common.fields.quantity\": 1}");
                } else {
                    query.setSort("{\"common.fields.quantity\": -1}");
                }
                query.setLimit(1);
                product = cmsBtProductDao.selectOneWithQuery(query, channelId);

            }
            // 先查询优先翻译并且有优先翻译日期的
            else if ("priority".equalsIgnoreCase(priority)) {

                // 优先获取设置了[优先翻译时间]的翻译任务(主商品)
                JongoQuery query = new JongoQuery();
                query.setQuery("{\"lock\" : \"0\"" +
                        ", \"common.fields.isMasterMain\": 1" +
                        ", \"common.fields.translateStatus\":\"2\"" +
                        ", \"common.fields.priorTranslateDate\" : {$nin : [null, \"\"]}" +
                        (codeList != null ? ", \"common.fields.code\": {$in: #}" : "") +
                        "}");
                if (codeList != null)
                    query.setParameters(codeList);
                query.setSort("{\"common.fields.priorTranslateDate\": 1, \"common.fields.quantity\": -1}");
                query.setLimit(1);
                product = cmsBtProductDao.selectOneWithQuery(query, channelId);

                // 如果不存在设置了[优先翻译时间]的翻译任务, 则根据设置了[优先顺序, 翻译状态:2->0]的顺序获取翻译任务
                if (product == null) {

                    // 获取翻译任务
                    query = new JongoQuery();
                    query.setQuery("{\"lock\" : \"0\"" +
                            ", \"common.fields.isMasterMain\": 1" +
                            ", \"common.fields.translateStatus\": {$in: [\"2\", \"0\"]}" +
                            ", \"common.fields.priorTranslateDate\" : {$in : [null, \"\"]}" +
                            (codeList != null ? ", \"common.fields.code\": {$in: #}" : "") +
                            "}");
                    if (codeList != null)
                        query.setParameters(codeList);
                    query.setSort("{\"common.fields.translateStatus\": -1, \"common.fields.quantity\": -1}");
                    query.setLimit(1);
                    product = cmsBtProductDao.selectOneWithQuery(query, channelId);
                }
            }
        }

        // 获取不到翻译任务
        if (product == null) {
            throw new BusinessException("当前没有待领取的翻译任务！");
        }

        // 有任务，修改任务状态
        JongoUpdate updateQuery = new JongoUpdate();
        updateQuery.setQuery("{\"platforms.P0.mainProductCode\": #}");
        updateQuery.setQueryParameters(product.getPlatform(0).getMainProductCode());
        updateQuery.setUpdate("{$set: {\"common.fields.translator\": #" +
                ", \"common.fields.translateTime\": #" +
                ", \"common.fields.translateStatus\": #" +
                ", \"modifier\": #" +
                ", \"modified\": #}}");
        updateQuery.setUpdateParameters(userName, DateTimeUtil.getNow()
                , "2".equals(product.getCommon().getFields().getTranslateStatus()) ? "2": "0"
                , userName, DateTimeUtil.getNow());
        cmsBtProductDao.updateMulti(updateQuery, channelId);

        //查找产品组
        JongoQuery mainProductQuery = new JongoQuery();
        mainProductQuery.setQuery("{\"platforms.P0.mainProductCode\": #}");
        mainProductQuery.setParameters(product.getPlatformNotNull(0).getMainProductCode());
        List<CmsBtProductModel> productList = cmsBtProductDao.select(mainProductQuery, channelId);
        return fillTranslationTaskBean(product, productList);
    }


    /**
     * 找出对应主商品
     * @param channelId
     * @param productCodes
     * @return
     */
    private List<String> getMainProductCode(String channelId, List<String> productCodes){

        JongoQuery queryObject = new JongoQuery();
        Criteria criteria = new Criteria("productCodes").in(productCodes).and("cartId").is(0);
        queryObject.setQuery(criteria);
        List<CmsBtProductGroupModel> cmsBtProductGroupModels = productGroupService.getList(channelId,queryObject);
        if(ListUtils.isNull(cmsBtProductGroupModels)){
            return null;
        }else{
            return cmsBtProductGroupModels.stream().map(CmsBtProductGroupModel::getMainProductCode).collect(Collectors.toList());
        }

    }
    /**
     * 取当前任务
     * @param channelId 店铺Id
     * @param userName 操作者
     * @return TranslationTaskBean
     * @throws BusinessException
     */
    public TranslationTaskBean getCurrentTask(String channelId, String userName) throws BusinessException {
        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), EXPIRE_HOURS);
        String translateTimeStr = DateTimeUtil.format(date, null);

        JongoQuery query = new JongoQuery();
        query.setQuery("{\"lock\": {$in: [\"0\", null]}" +
                ", \"common.fields.isMasterMain\":1" +
                ", \"common.fields.translateStatus\": {$ne : \"1\"}" +
                ", \"common.fields.translator\": #" +
                ", \"common.fields.translateTime\": {$gt: #}}");
        query.setParameters(userName, translateTimeStr);
        CmsBtProductModel product = cmsBtProductDao.selectOneWithQuery(query, channelId);
        List<CmsBtProductModel> groupProducts = null;
        if (product != null) {
            JongoQuery mainProductQuery = new JongoQuery();
            mainProductQuery.setQuery("{\"platforms.P0.mainProductCode\": #}");
            mainProductQuery.setParameters(product.getPlatformNotNull(0).getMainProductCode());
            groupProducts = cmsBtProductDao.select(mainProductQuery, channelId);
        }
        return fillTranslationTaskBean(product, groupProducts);
    }

    /**
     * 保存翻译任务
     * @param bean TranslationTaskBean
     * @param channelId 店铺Id
     * @param userName 操作者
     * @param status 翻译状态
     * @return TranslationTaskBean
     * @throws BusinessException
     */
    public TranslationTaskBean saveTask(TranslationTaskBean bean, String channelId, String userName, String status)
            throws BusinessException {

        String mainCode = bean.getProductCode();
        JongoQuery query = new JongoQuery();
        query.setQuery("{\"platforms.P0.mainProductCode\": #}");
        query.setParameters(mainCode);

        //查找产品组
        List<CmsBtProductModel> productList = cmsBtProductDao.select(query, channelId);
        if (productList == null || productList.size() == 0) {
            throw new BusinessException("无法找打主产品为[" + mainCode + "]的产品组!");
        }

        TranslationTaskBean_CommonFields cnFields = bean.getCommonFields();

//        //读cms_mt_feed_custom_prop
//        List<FeedCustomPropWithValueBean> feedCustomPropList = customPropService.getPropList(channelId, bean
//                .getFeedCategory());
//
//        //去除掉feedCustomPropList中的垃圾数据
//        if (feedCustomPropList != null && !feedCustomPropList.isEmpty()) {
//            feedCustomPropList = feedCustomPropList.stream()
//                    .filter(w -> !StringUtils.isNullOrBlank2(w.getFeed_prop_translation()) && !StringUtils
//                            .isNullOrBlank2(w.getFeed_prop_original()))
//                    .collect(Collectors.toList());
//        } else {
//            feedCustomPropList = new ArrayList<>();
//        }
//        Set<String> custPropKeySet = feedCustomPropList.stream().map
//                (FeedCustomPropWithValueBean::getFeed_prop_original).collect(Collectors.toSet());
        //根据feedCustomPropList精简cnProps
        List<CustomPropBean> cnProps = bean.getCustomProps();
//        if (custPropKeySet != null && !custPropKeySet.isEmpty()) {
//            cnProps = cnProps.stream().filter(w -> custPropKeySet.contains(w.getFeedAttrEn())).collect(Collectors.toList());
//        }

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
     * @param pageNum 起始页码
     * @param pageSize 每页显示行数
     * @param keyWord 模糊查询
     * @param channelId 店铺Id
     * @param userName 操作者
     * @param status 翻译状态
     * @return Map<String, Object>
     * @throws BusinessException
     */
    public Map<String, Object> searchTask(int pageNum, int pageSize, String keyWord, String channelId, String
            userName, String status) throws BusinessException {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), EXPIRE_HOURS);
        String translateTimeStr = DateTimeUtil.format(date, null);

        JongoQuery queryObj = new JongoQuery();
        queryObj.addQuery("{\"lock\":\"0\"}");
        queryObj.addQuery("{\"common.fields.isMasterMain\":1}");
        queryObj.addQuery("{\"common.fields.translator\":#}");
        queryObj.addParameters(userName);
        if (!StringUtils.isNullOrBlank2(keyWord)) {
            queryObj.addQuery("{$or:[ {\"common.fields.code\":#},{\"common.fields.productNameEn\":{$regex: #}}," +
                    "{\"common.fields.originalTitleCn\":{$regex: #}}]}");
            queryObj.addParameters(keyWord, keyWord, keyWord);
        }

        //不能有2个or
        if (StringUtils.isNullOrBlank2(status)) {
            queryObj.addQuery("{$or:[{\"common.fields.translateStatus\":\"0\",\"common.fields.translateTime\":{$gt:#}},{\"common.fields.translateStatus\":\"1\"}]}");
            queryObj.addParameters(translateTimeStr);
        } else if ("1".equals(status)) {
            queryObj.addQuery("{\"common.fields.translateStatus\":\"1\"}");
        } else if ("0".equals(status)) {
            queryObj.addQuery("{\"common.fields.translateStatus\":\"0\"}");
            queryObj.addQuery("{\"common.fields.translateTime\":{$gt:#}}");
            queryObj.addParameters(translateTimeStr);
        }

        queryObj.setSort("{\"common.fields.translateTime\" : -1}");

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
                    map.put("translateTime", StringUtils.toString(fields.getTranslateTime()));
                }
                list.add(map);
            }
        }
        result.put("taskList", list);
        result.put("total", total);
        return result;
    }

    /**
     * 装填TranslationTaskBean
     * @param product 产品信息
     * @return TranslationTaskBean
     */
    private TranslationTaskBean fillTranslationTaskBean(CmsBtProductModel product) {

        if (product != null) {
            TranslationTaskBean translationTaskBean = new TranslationTaskBean();
            //装填Bean
            translationTaskBean.setProdId(product.getProdId());
            translationTaskBean.setFeedCategory(StringUtils.toString(product.getFeed().getCatPath()));

            CmsBtProductModel_Field fields = product.getCommon().getFields();
            translationTaskBean.setCatPath(StringUtils.toString(product.getCommon().getCatPath()));
            translationTaskBean.setProductCode(StringUtils.toString(fields.getCode()));
            TranslationTaskBean_CommonFields commonFields = new TranslationTaskBean_CommonFields();
            commonFields.setBrand(StringUtils.toString(fields.getBrand()));
            commonFields.setProductNameEn(StringUtils.toString(fields.getProductNameEn()));
            commonFields.setOriginalTitleCn(StringUtils.toString(fields.getOriginalTitleCn()));
            commonFields.setMaterialEn(StringUtils.toString(fields.getMaterialEn()));
            commonFields.setMaterialCn(StringUtils.toString(fields.getMaterialCn()));
            commonFields.setOrigin(StringUtils.toString(fields.getOrigin()));
            commonFields.setShortDesEn(StringUtils.toString(fields.getShortDesEn()));
            commonFields.setShortDesCn(StringUtils.toString(fields.getShortDesCn()));
            commonFields.setLongDesEn(StringUtils.toString(fields.getLongDesEn()));
            commonFields.setLongDesCn(StringUtils.toString(fields.getLongDesCn()));
            commonFields.setUsageEn(StringUtils.toString(fields.getUsageEn()));
            commonFields.setUsageCn(StringUtils.toString(fields.getUsageCn()));
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

            commonFields.setClientProductUrl(StringUtils.toString(fields.getClientProductUrl()));
            commonFields.setTranslator(StringUtils.toString(fields.getTranslator()));
            commonFields.setTranslateStatus(StringUtils.toString(fields.getTranslateStatus()));
            commonFields.setTranslateTime(StringUtils.toString(fields.getTranslateTime()));
            translationTaskBean.setCommonFields(commonFields);

            List<CustomPropBean> props = productService.getCustomProp(product);
            translationTaskBean.setCustomProps(props);

            return translationTaskBean;
        }
        return null;
    }

    /**
     * 重载：装填TranslationTaskBean
     * @param product 产品信息
     * @param groupProducts 一组产品
     * @return TranslationTaskBean
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
