package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CustomPropBean;
import com.voyageone.service.bean.cms.feed.FeedCustomPropWithValueBean;
import com.voyageone.service.bean.cms.translation.TaskSummaryBean;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean_CommonFields;
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

    private static final String TASK_INCOMPLETE = "0";

    /**
     * 计算翻译任务的汇总信息
     */
    public TaskSummaryBean getTaskSummary(String channelId, String userName) throws BusinessException {
        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), EXPIRE_HOURS);
        String translateTimeStr = DateTimeUtil.format(date, null);

        TaskSummaryBean taskSummary = new TaskSummaryBean();

        //未分配的任务
        String queryStr = String.format("{'common.fields.translateStatus':'0'," +
                "'common.fields.isMasterMain':1," +
                " '$or': [{'common.fields.translator':''} ,  " +
                "{'common.fields.translateTime':{'$lte':'%s'}} , " +
                "{'common.fields.translator':{'$exists' : false}}, " +
                "{'common.fields.translateTime':{'$exists' : false}}]}", translateTimeStr);

        taskSummary.setUnassginedCount(cmsBtProductDao.countByQuery(queryStr, channelId));

        //已分配但未完成的任务
        queryStr = String.format("{'common.fields.isMasterMain':1," +
                "'common.fields.translateStatus':'0'," +
                "'common.fields.translator':{'$exists' : true}," +
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
    public TranslationTaskBean getTaskById(String channelId, String userName, int prodId) throws BusinessException {

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
        String queryStr = String.format("{'common.fields.isMasterMain':1," +
                "'common.fields.translateStatus':'0'," +
                "'common.fields.translator':'%s', " +
                "'common.fields.translateTime':{'$gt':'%s'} }", userName, translateTimeStr);

        long cnt = cmsBtProductDao.countByQuery(queryStr, channelId);

        if (cnt > 0) {
            throw new BusinessException("当前任务还未完成，不能领取新的任务！");
        }

        //再查是否有过期任务，优先分配过期任务
        queryStr = String.format("{'common.fields.isMasterMain':1," +
                "'common.fields.translateStatus':'0'," +
                "'common.fields.translator':'%s'}", userName);

        CmsBtProductModel product = cmsBtProductDao.selectOneWithQuery(queryStr, channelId);

        if (product == null) {
            //没有过期任务，按优先级参数分配
            JomgoQuery queryObj = new JomgoQuery();

            queryObj.addQuery("'common.fields.isMasterMain':1,'common.fields.translateStatus':'0'");

            if (!StringUtils.isNullOrBlank2(keyWord)) {
                queryObj.addQuery("'$or':[ {'common.fields.code':#},{'common.fields.productNameEn':{'$regex': #}}," +
                        "{'common.fields.originalTitleCn':{'$regex': #}}]");
                queryObj.addParameters(keyWord, keyWord, keyWord);
            }

            if (!StringUtils.isNullOrBlank2(priority)) {
                if ("quantity".equalsIgnoreCase(priority)) {
                    if ("asc".equalsIgnoreCase(sort)) {
                        queryObj.setSort("{'common.fields.quantity' : 1}");
                    } else {
                        queryObj.setSort("{'common.fields.quantity' : -1}");
                    }
                }
            }

            product = cmsBtProductDao.selectOneWithQuery(queryObj, channelId);
        }

        if (product == null) {
            throw new BusinessException("当前没有待领取的翻译任务！");
        }

        //修改任务状态
        else {
            Map<String, Object> rsMap = new HashMap<>();
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("prodId", product.getProdId());
            rsMap.put("common.fields.translator", userName);
            rsMap.put("common.fields.translateTime", DateTimeUtil.getNow());
            rsMap.put("modifier", userName);
            rsMap.put("modified", DateTimeUtil.getNow());
            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("$set", rsMap);

            cmsBtProductDao.update(channelId, queryMap, updateMap);

            return fillTranslationTaskBean(product);
        }
    }


    /**
     * 取当前任务
     */
    public TranslationTaskBean getCurrentTask(String channelId, String userName) throws BusinessException {
        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), EXPIRE_HOURS);
        String translateTimeStr = DateTimeUtil.format(date, null);

        String queryStr = String.format("{'common.fields.isMasterMain':1," +
                "'common.fields.translateStatus':'0'," +
                "'common.fields.translator':'%s', " +
                "'common.fields.translateTime':{'$gt':'%s'} }", userName, translateTimeStr);

        CmsBtProductModel product = cmsBtProductDao.selectOneWithQuery(queryStr, channelId);

        return fillTranslationTaskBean(product);
    }


    /**
     * 保存翻译任务
     */
    public TranslationTaskBean saveTask(TranslationTaskBean bean, String channelId, String userName, String status)
            throws BusinessException {

        String mainCode = bean.getProductCode();
        String queryStr = String.format("{'mainProductCode' : '%s', 'cartId':0}", mainCode);
        CmsBtProductGroupModel group = CmsBtProductGroupDao.selectOneWithQuery(queryStr, channelId);
        if (group != null) {
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

                rsMap.put("common.fields.translator", userName);
                rsMap.put("common.fields.translateTime", DateTimeUtil.getNow());

                if(!status.equals(TASK_INCOMPLETE)) {
                    rsMap.put("common.fields.translateStatus", status);
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

        }
        return getCurrentTask(channelId, userName);
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

        JomgoQuery queryObj = new JomgoQuery();

        queryObj.addQuery("'common.fields.isMasterMain':1,'common.fields.translator':#");
        queryObj.addParameters(userName);
        if (!StringUtils.isNullOrBlank2(keyWord)) {
            queryObj.addQuery("'$or':[ {'common.fields.code':#},{'common.fields.productNameEn':{'$regex': #}}," +
                    "{'common.fields.originalTitleCn':{'$regex': #}}]");
            queryObj.addParameters(keyWord, keyWord, keyWord);
        }

        if (StringUtils.isNullOrBlank2(status)) {
            queryObj.addQuery("'$or':[{'common.fields.translateStatus':'0','common.fields.translateTime':{'$gt':#}}, " +
                    "{'common.fields.translateStatus':'1'}]");
            queryObj.addParameters(translateTimeStr);
        } else if ("1".equals(status)) {
            queryObj.addQuery("'common.fields.translateStatus':'1'");
        } else if ("0".equals(status)) {
            queryObj.addQuery("'common.fields.translateStatus':'0'");
            queryObj.addQuery("'common.fields.translateTime':{'$gt':#}");
            queryObj.addParameters(translateTimeStr);
        }

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
                SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtil.DEFAULT_DATE_FORMAT);
                try {
                    Date translateTime = sdf.parse(fields.getTranslateTime());
                    map.put("translateTime", translateTime.getTime() / 1000);
                } catch (Exception e) {
                    map.put("translateTime", fields.getTranslateTime());
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

}
