package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.feed.FeedCustomPropWithValueBean;
import com.voyageone.service.bean.cms.translation.TaskSummaryBean;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean_CommonFields;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.feed.FeedCustomPropService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by Ethan Shi on 2016/6/28.
 *
 * @author Ethan Shi
 * @version 2.2.0
 * @since 2.2.0
 */

@Service
public class TranslationTaskService extends BaseService {

    private static final int  EXPIRE_HOURS = -48;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    CmsBtFeedInfoDao cmsBtFeedInfoDao;

    @Autowired
    FeedCustomPropService customPropService;

    /**
     * 计算翻译任务的汇总信息
     *
     * @param channelId
     * @param userName
     * @return
     * @throws BusinessException
     */
    public TaskSummaryBean getTaskSummary(String channelId, String userName) throws BusinessException
    {
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
        return  taskSummary;
    }

    public TranslationTaskBean getCurrentTask(String channelId, String userName) throws BusinessException
    {
        TranslationTaskBean translationTaskBean = new TranslationTaskBean();

        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), EXPIRE_HOURS);
        String translateTimeStr = DateTimeUtil.format(date, null);

        String queryStr = String.format("{'common.fields.isMasterMain':1," +
                "'common.fields.translateStatus':'0'," +
                "'common.fields.translator':'%s', "+
                "{'common.fields.translateTime':{'$gt':'%s'}} }", userName, translateTimeStr);

        CmsBtProductModel  product = cmsBtProductDao.selectOneWithQuery(queryStr, channelId);
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
//        commonFields.setUsageEn();
//        commonFields.setUsageCn(); //TODO
        List<CmsBtProductModel_Field_Image>  img1 = fields.getImages1();
        if(img1!= null  && img1.size() > 0) {
            commonFields.setImages1(img1.get(0).getName()); //TODO
        }
        else
        {
            commonFields.setImages1("");
        }
        commonFields.setClientProductUrl(fields.getClientProductUrl());
        commonFields.setTranslator(fields.getTranslator());
        commonFields.setTranslateStatus(fields.getTranslateStatus());
        commonFields.setTranslateTime(fields.getTranslateTime());
        translationTaskBean.setCommonFields(commonFields);

        //读feed_info
        CmsBtFeedInfoModel feedInfo = cmsBtFeedInfoDao.selectProductByCode(channelId, fields.getCode());
        Map<String, List<String>> feedAttr  = feedInfo.getAttribute();

        //读cms_mt_feed_custom_prop
        List<FeedCustomPropWithValueBean> feedCustomPropList = customPropService.getPropList(channelId, feedInfo.getCategory());

        //合并feedAttr和feedCustomPropList
//        for (String attrKey: feedAttr) {
//
//        }





        return  translationTaskBean;
    }

}
