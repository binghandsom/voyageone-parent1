package com.voyageone.service.impl.cms;

import com.google.common.base.Joiner;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.feed.FeedCustomPropWithValueBean;
import com.voyageone.service.bean.cms.translation.TaskSummaryBean;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean_CommonFields;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean_CustomProps;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.feed.FeedCustomPropService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Feed;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    CmsBtFeedInfoDao cmsBtFeedInfoDao;

    @Autowired
    FeedCustomPropService customPropService;

    @Autowired
    ProductGroupService productGroupService;

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
                "{'common.fields.translateTime':{'$gt':'%s'}} , " +
                "{'common.fields.translator':{'$exists' : false}}, " +
                "{'common.fields.translateTime':{'$exists' : false}}]}", translateTimeStr);

        taskSummary.setUnassginedCount(Long.valueOf(cmsBtProductDao.countByQuery(queryStr, channelId)).intValue());

        //已分配但未完成的任务
        queryStr = String.format("{'common.fields.isMasterMain':1," +
                "'common.fields.translateStatus':'0'," +
                "'common.fields.translator':{'$exists' : true}," +
                "'common.fields.translator':{'$ne' : ''}," +
                "'common.fields.translateTime':{'$gt':'%s'}}", translateTimeStr);
        taskSummary.setImcompeleteCount(Long.valueOf(cmsBtProductDao.countByQuery(queryStr, channelId)).intValue());

        //完成翻译总商品数
        queryStr = "{'common.fields.isMasterMain':1," +
                "'common.fields.translateStatus':'1'}";
        taskSummary.setCompeleteCount(Long.valueOf(cmsBtProductDao.countByQuery(queryStr, channelId)).intValue());

        //个人完成翻译商品数
        queryStr = String.format("{'common.fields.isMasterMain':1," +
                "'common.fields.translateStatus':'1'," +
                "'common.fields.translator':'%s'}", userName);
        taskSummary.setUserCompeleteCount(Long.valueOf(cmsBtProductDao.countByQuery(queryStr, channelId)).intValue());
        return taskSummary;
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

    public TaskSummaryBean saveTask(TranslationTaskBean bean, String channelId, String userName) throws BusinessException {
        //获取产品组
        //String mainCode = bean.getProductCode();
        return null;
    }


    /**
     * 装填TranslationTaskBean
     */
    private TranslationTaskBean fillTranslationTaskBean(CmsBtProductModel product) {
        TranslationTaskBean translationTaskBean = new TranslationTaskBean();
        if (product != null) {
            String channelId = product.getChannelId();
            //装填Bean
            translationTaskBean.setProdId(product.getProdId());
            CmsBtProductModel_Field fields = product.getCommon().getFields();
            translationTaskBean.setProductCode(fields.getCode());
            TranslationTaskBean_CommonFields commonFields = new TranslationTaskBean_CommonFields();
            commonFields.setBrand(fields.getBrand());
            commonFields.setProductNameEn(fields.getProductNameEn());
            commonFields.setOriginalTitleCn(fields.getOriginalTitleCn());
            commonFields.setMaterialEn(fields.getMaterialEn());
            commonFields.setMaterialCn(fields.getMaterialCn());
            commonFields.setOrigin(fields.getOrigin());
            commonFields.setShortDesEn(fields.getShortDesEn());
            commonFields.setShortDesCn(fields.getShortDesCn());
            commonFields.setLongDesEn(fields.getLongDesEn());
            commonFields.setLongDesCn(fields.getLongDesCn());
            commonFields.setUsageEn(fields.getUsageEn());
            commonFields.setUsageCn(fields.getUsageCn());
            List<CmsBtProductModel_Field_Image> img1 = fields.getImages1();
            if (img1 != null && img1.size() > 0) {
                commonFields.setImages1(img1);
            }

            List<CmsBtProductModel_Field_Image> img2 = fields.getImages1();
            if (img2 != null && img2.size() > 0) {
                commonFields.setImages1(img1);
            }

            List<CmsBtProductModel_Field_Image> img3 = fields.getImages1();
            if (img3 != null && img3.size() > 0) {
                commonFields.setImages1(img3);
            }


            List<CmsBtProductModel_Field_Image> img4 = fields.getImages1();
            if (img4 != null && img4.size() > 0) {
                commonFields.setImages1(img4);
            }

            List<CmsBtProductModel_Field_Image> img5 = fields.getImages1();
            if (img5 != null && img5.size() > 0) {
                commonFields.setImages1(img5);
            }

            List<CmsBtProductModel_Field_Image> img6 = fields.getImages1();
            if (img6 != null && img6.size() > 0) {
                commonFields.setImages1(img6);
            }

            List<CmsBtProductModel_Field_Image> img7 = fields.getImages1();
            if (img7 != null && img7.size() > 0) {
                commonFields.setImages1(img7);
            }

            List<CmsBtProductModel_Field_Image> img8 = fields.getImages1();
            if (img8 != null && img8.size() > 0) {
                commonFields.setImages1(img8);
            }

            commonFields.setClientProductUrl(fields.getClientProductUrl());
            commonFields.setTranslator(fields.getTranslator());
            commonFields.setTranslateStatus(fields.getTranslateStatus());
            commonFields.setTranslateTime(fields.getTranslateTime());
            translationTaskBean.setCommonFields(commonFields);

            CmsBtProductModel_Feed productFeed = product.getFeed();
            BaseMongoMap<String, Object> cnAttrs = productFeed.getCnAtts();


            List<TranslationTaskBean_CustomProps> props = new ArrayList<>();

            //读feed_info
            CmsBtFeedInfoModel feedInfo = cmsBtFeedInfoDao.selectProductByCode(channelId, fields.getCode());
            Map<String, List<String>> feedAttr = feedInfo.getAttribute();

            //读cms_mt_feed_custom_prop
            List<FeedCustomPropWithValueBean> feedCustomPropList = customPropService.getPropList(channelId, feedInfo.getCategory());

            //去除掉feedCustomPropList中的垃圾数据
            if (feedCustomPropList != null && feedCustomPropList.size() > 0) {
                feedCustomPropList = feedCustomPropList.stream().filter(w -> (!StringUtils.isNullOrBlank2(w.getFeed_prop_translation()) &&
                        !StringUtils.isNullOrBlank2(w.getFeed_prop_original()))).collect(Collectors.toList());
            } else {
                feedCustomPropList = new ArrayList<>();
            }

            //合并feedAttr和feedCustomPropList
            for (String attrKey : feedAttr.keySet()) {
                List<String> valueList = feedAttr.get(attrKey);
                TranslationTaskBean_CustomProps prop = new TranslationTaskBean_CustomProps();
                prop.setFeedAttrEn(attrKey);
                String attrValue = Joiner.on(",").skipNulls().join(valueList);
                prop.setFeedAttrValueEn(attrValue);
                prop.setFeedAttrCn("");
                prop.setFeedAttrValueCn("");
                prop.setIsfeedAttr(true);

                if (feedCustomPropList.stream().filter(w -> w.getFeed_prop_original().equals(attrKey)).count() > 0) {
                    FeedCustomPropWithValueBean feedCustProp = feedCustomPropList.stream().filter(w -> w.getFeed_prop_original().equals(attrKey)).findFirst().get();
                    prop.setFeedAttrCn(feedCustProp.getFeed_prop_translation());
                    if (cnAttrs.keySet().stream().filter(w -> w.equals(attrKey)).count() > 0) {
                        //如果product已经保存过
                        String cnAttKey = cnAttrs.keySet().stream().filter(w -> w.equals(attrKey)).findFirst().get();
                        prop.setFeedAttrValueCn(cnAttrs.getStringAttribute(cnAttKey));
                    } else {
                        //取默认值
                        Map<String, List<String>> defaultValueMap = feedCustProp.getMapPropValue();
                        List<String> vList = defaultValueMap.get(attrValue);
                        if (vList != null) {
                            if (vList.stream().filter(w -> !StringUtils.isNullOrBlank2(w)).count() > 0) {
                                String cnAttValue = vList.stream().filter(w -> !StringUtils.isNullOrBlank2(w)).findFirst().get();
                                prop.setFeedAttrValueCn(cnAttValue);
                            }
                        }

                    }
                }

                props.add(prop);
            }

            //仅存在于cms_mt_feed_custom_prop中，不存在于feed attributes中的项目
            for (FeedCustomPropWithValueBean custProp : feedCustomPropList) {
                String feedKey = custProp.getFeed_prop_original();
                if (feedAttr.keySet().stream().filter(w -> w.equals(feedKey)).count() == 0) {
                    TranslationTaskBean_CustomProps prop = new TranslationTaskBean_CustomProps();
                    prop.setFeedAttrEn(feedKey);
                    prop.setFeedAttrValueEn("");
                    prop.setFeedAttrCn(custProp.getFeed_prop_translation());
                    prop.setFeedAttrValueCn("");
                    prop.setIsfeedAttr(false);

                    if (cnAttrs.keySet().stream().filter(w -> w.equals(feedAttr)).count() > 0) {
                        String cnAttKey = cnAttrs.keySet().stream().filter(w -> w.equals(feedAttr)).findFirst().get();
                        prop.setFeedAttrValueCn(cnAttrs.getStringAttribute(cnAttKey));
                    }
                    props.add(prop);
                }

            }
            translationTaskBean.setCustomProps(props);
        }
        return translationTaskBean;
    }

}
