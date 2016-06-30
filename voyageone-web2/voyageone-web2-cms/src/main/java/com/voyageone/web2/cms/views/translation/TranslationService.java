package com.voyageone.web2.cms.views.translation;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.ProductTransDistrBean;
import com.voyageone.service.impl.cms.CustomWordService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.ProductTranslationBean;
import com.voyageone.web2.cms.bean.TranslateTaskBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by lewis on 15-12-16.
 */
@Service
public class TranslationService extends BaseAppService {

    @Autowired
    private FeedInfoService feedInfoService;
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductGroupService productGroupService;

    @Autowired
    protected CustomWordService customWordService;

    private static String[] RET_FIELDS = {
            "prodId",
            "fields.code",
            "fields.productNameEn",
            "fields.longDesEn",
            "fields.shortDesEn",
            "fields.longTitle",
            "fields.middleTitle",
            "fields.shortTitle",
            "fields.longDesCn",
            "fields.shortDesCn",
            "fields.originalDesCn",
            "fields.originalTitleCn",
            "fields.model",
            "fields.images1", "fields.images2", "fields.images3", "fields.images4", "fields.images5", "fields.images6",
            "fields.translator",
            "fields.translateStatus",
            "fields.clientProductUrl",
            "modified"};

    /**
     * 获取当前用户未完成的任务.
     *
     * @param userInfo UserSessionBean
     * @throws BusinessException
     */
    public TranslateTaskBean getUndoneTasks(UserSessionBean userInfo) throws BusinessException {
        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), -24);
        String translateTimeStr = DateTimeUtil.format(date, null);

        String tasksQueryStr = String.format("{'fields.status':{'$nin':['New']},'fields.translateStatus':'0','fields.isMasterMain':1," +
                "'fields.translator':'%s','fields.translateTime':{'$gt':'%s'}}", userInfo.getUserName(), translateTimeStr);

        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(tasksQueryStr);
        queryObject.setProjectionExt(RET_FIELDS);

        // 取得所有未翻译的主商品
        List<CmsBtProductModel> cmsBtProductModels = productService.getList(userInfo.getSelChannelId(), queryObject);

        List<ProductTranslationBean> translateTaskBeanList = buildTranslateTaskBeen(userInfo.getSelChannelId(), cmsBtProductModels);
        TranslateTaskBean translateTaskBean = new TranslateTaskBean();
        translateTaskBean.setProductTranslationBeanList(translateTaskBeanList);
        translateTaskBean.setProdListTotal(cmsBtProductModels.size());
        translateTaskBean.setTotalDoneCount(this.getTotalDoneCount(userInfo.getSelChannelId()));
        translateTaskBean.setUserDoneCount(this.getDoneTaskCount(userInfo.getSelChannelId(), userInfo.getUserName()));
        translateTaskBean.setTotalUndoneCount(this.getTotalUndoneCount(userInfo.getSelChannelId()));
        translateTaskBean.setTotalDistributeUndoneCount(this.getTotalDistributionUndoneCount(userInfo.getSelChannelId()));

        return translateTaskBean;
    }

    /**
     * 用户获取任务列表，自动分发任务.
     *
     * @param userInfo UserSessionBean
     * @param translateTaskBean TranslateTaskBean
     */
    public TranslateTaskBean assignTask(UserSessionBean userInfo,TranslateTaskBean translateTaskBean) {

        ProductTransDistrBean productTransDistrBean = new ProductTransDistrBean();

        if (!StringUtils.isEmpty(translateTaskBean.getSortCondition())){
            String sortField = "fields." + translateTaskBean.getSortCondition();
            productTransDistrBean.setSortStr("{\"" + sortField + "\":" + (StringUtils.isEmpty(translateTaskBean.getSortRule()) ? "1" : translateTaskBean.getSortRule()) + "}");
        }

        productTransDistrBean.setTranslator(userInfo.getUserName());
        productTransDistrBean.setTranslateTimeHDiff(48);
        productTransDistrBean.setDistributeRule(translateTaskBean.getDistributeRule());
        if (translateTaskBean.getDistributeCount() > 0){
            productTransDistrBean.setLimit(translateTaskBean.getDistributeCount());
        }
        //设定返回值.
        productTransDistrBean.setProjectionArr(RET_FIELDS);

        List<CmsBtProductModel> cmsBtProductModels = productService.translateDistribute(userInfo.getSelChannelId(), productTransDistrBean);

        TranslateTaskBean result = new TranslateTaskBean();
        result.setProductTranslationBeanList(buildTranslateTaskBeen(userInfo.getSelChannelId(), cmsBtProductModels));
        result.setProdListTotal(cmsBtProductModels.size());
        result.setTotalDoneCount(this.getTotalDoneCount(userInfo.getSelChannelId()));
        result.setUserDoneCount(this.getDoneTaskCount(userInfo.getSelChannelId(), userInfo.getUserName()));
        result.setTotalUndoneCount(this.getTotalUndoneCount(userInfo.getSelChannelId()));
        result.setTotalDistributeUndoneCount(this.getTotalDistributionUndoneCount(userInfo.getSelChannelId()));
        return result;
    }

    /**
     * 暂时保存翻译任务.
     */
    public TranslateTaskBean saveTask(UserSessionBean userInfo, ProductTranslationBean taskBean, String transSts) {
        // check翻译数据是否正确
        if("1".equalsIgnoreCase(transSts)){
            verifyParameter(taskBean);
        }

        Map<String, Object> updObj = new HashMap<>();
        updObj.put("fields.translator", userInfo.getUserName());
        updObj.put("fields.translateStatus", transSts);
        updObj.put("fields.translateTime", DateTimeUtil.getNow(DateTimeUtil.DEFAULT_DATETIME_FORMAT));
        updObj.put("fields.originalTitleCn", taskBean.getLongTitle());
        updObj.put("fields.longTitle", taskBean.getLongTitle());
        updObj.put("fields.middleTitle", taskBean.getMiddleTitle());
        updObj.put("fields.shortTitle", taskBean.getShortTitle());
        updObj.put("fields.longDesCn", taskBean.getLongDesCn());
        updObj.put("fields.originalDesCn", taskBean.getLongDesCn());
        updObj.put("fields.shortDesCn", taskBean.getShortDesCn());

        productService.updateTranslation(userInfo.getSelChannelId(), taskBean.getProductCode(), updObj, userInfo.getUserName());

        TranslateTaskBean translateTaskBean = new TranslateTaskBean();
        translateTaskBean.setModifiedTime(DateTimeUtil.getNowTimeStamp());
        translateTaskBean.setTotalDoneCount(this.getTotalDoneCount(userInfo.getSelChannelId()));
        translateTaskBean.setUserDoneCount(this.getDoneTaskCount(userInfo.getSelChannelId(), userInfo.getUserName()));
        translateTaskBean.setTotalUndoneCount(this.getTotalUndoneCount(userInfo.getSelChannelId()));
        translateTaskBean.setTotalDistributeUndoneCount(this.getTotalDistributionUndoneCount(userInfo.getSelChannelId()));
        return translateTaskBean;
    }

//    /**
//     * 从当前产品所在分组中拷贝已经翻译好的信息到当前类目.
//     *
//     * @return
//     * @throws BusinessException
//     */
//    public ProductTranslationBean copyFormMainProduct(String channelId,ProductTranslationBean translationBean) {
//
//        String tasksQueryStr = String.format("{'fields.status':{'$nin':['New']},'fields.translateStatus':'1','fields.model':'%s'}",translationBean.getModel());
//
//        JomgoQuery queryObject = new JomgoQuery();
//        queryObject.setQuery(tasksQueryStr);
//        queryObject.setProjection("fields.longTitle", "fields.middleTitle", "fields.shortTitle", "fields.longDesCn", "fields.shortDesCn");
//
//        CmsBtProductModel productModel = productService.getProductWithQuery(channelId, queryObject);
//
//        if (productModel == null){
//
//           throw new BusinessException("没用可参照的数据，请直接翻译！");
//        }
//
//        CmsBtProductModel_Field productFields = productModel.getFields();
//
//        translationBean.setLongTitle(productFields.getLongTitle());
//        translationBean.setMiddleTitle(productFields.getMiddleTitle());
//        translationBean.setShortTitle(productFields.getShortTitle());
//        translationBean.setLongDesCn(productFields.getLongDesCn());
//        translationBean.setShortDesCn(productFields.getShortDesCn());
//
//        return translationBean;
//    }

    /**
     * 根据条件获取当前用户所有任务.
     */
    public TranslateTaskBean searchUserTasks(UserSessionBean userInfo, Map reqBean) {
        String condition = (String) reqBean.get("searchCondition");
        int tranSts = NumberUtils.toInt((String) reqBean.get("tranSts"), -1);
        if (StringUtils.isEmpty(condition) && tranSts == -1) {
            return this.getUndoneTasks(userInfo);
        }

        String tasksQueryStr = String.format("{'fields.translator':'%s'", userInfo.getUserName());
        if (!StringUtils.isEmpty(condition)) {
            tasksQueryStr = tasksQueryStr + String.format(",$or:[{'fields.code':{$regex:'%s'}}," +
                            "{'fields.productNameEn':{$regex:'%s'}},{'fields.longDesEn':{$regex:'%s'}}," +
                            "{'fields.shortDesEn':{$regex:'%s'}},{'fields.longTitle':{$regex:'%s'}}," +
                            "{'fields.middleTitle':{$regex:'%s'}},{'fields.shortTitle':{$regex:'%s'}}," +
                            "{'fields.longDesCn':{$regex:'%s'}},{'fields.shortDesCn':{$regex:'%s'}}]",
                    condition, condition, condition, condition, condition, condition, condition, condition, condition);
        }
        if (tranSts == -1) {
            tasksQueryStr = tasksQueryStr + "}";
        } else {
            tasksQueryStr = tasksQueryStr + ",'fields.translateStatus':'" + tranSts + "'}";
        }

        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(tasksQueryStr);
        //设定返回值.
        queryObject.setProjectionExt(RET_FIELDS);
        long prodTotal = productService.getCnt(userInfo.getSelChannelId(), queryObject.getQuery());

        int pageNum = (Integer) reqBean.get("pageNum");
        int pageSize = (Integer) reqBean.get("pageSize");
        queryObject.setSkip((pageNum - 1) * pageSize);
        queryObject.setLimit(pageSize);

        List<CmsBtProductModel> cmsBtProductModels = productService.getList(userInfo.getSelChannelId(), queryObject);
        List<ProductTranslationBean> translateTaskBeanList = buildTranslateTaskBeen(userInfo.getSelChannelId(), cmsBtProductModels);

        TranslateTaskBean translateTaskBean = new TranslateTaskBean();
        translateTaskBean.setProductTranslationBeanList(translateTaskBeanList);
        translateTaskBean.setProdListTotal(prodTotal);
        return translateTaskBean;
    }

    /**
     * 组装task beans。
     */
    private List<ProductTranslationBean> buildTranslateTaskBeen(String channelId, List<CmsBtProductModel> cmsBtProductModels) {
        List<ProductTranslationBean> translateTaskBeanList = new ArrayList<>(cmsBtProductModels.size());

        for (CmsBtProductModel productModel:cmsBtProductModels){
            ProductTranslationBean translationBean = new ProductTranslationBean();

            translationBean.setLongDescription(productModel.getCommon().getFields().getLongDesEn());
            translationBean.setProductName(productModel.getCommon().getFields().getProductNameEn());
            translationBean.setShortDescription(productModel.getCommon().getFields().getShortDesEn());

            translationBean.setLongTitle(StringUtils.isEmpty(productModel.getCommon().getFields().getLongTitle()) ? productModel.getFields().getOriginalTitleCn() : productModel.getFields().getLongTitle());
            translationBean.setMiddleTitle(productModel.getCommon().getFields().getMiddleTitle());
            translationBean.setShortTitle(productModel.getCommon().getFields().getShortTitle());
            translationBean.setLongDesCn(StringUtils.isEmpty(productModel.getCommon().getFields().getLongDesCn()) ? productModel.getCommon().getFields().getOriginalDesCn(): productModel.getFields().getLongDesCn());
            translationBean.setShortDesCn(productModel.getCommon().getFields().getShortDesCn());
            translationBean.setModel(productModel.getCommon().getFields().getModel());
            translationBean.setProductCode(productModel.getCommon().getFields().getCode());
            translationBean.setClientProductUrl(productModel.getCommon().getFields().getClientProductUrl());

            // 设置商品图片
            translationBean.setProductImage(productModel.getCommon().getFields().getImages1().get(0).getName());
            List<List<String>> imageList = new ArrayList<>(4);
            List<CmsBtProductModel_Field_Image> images1 = productModel.getCommon().getFields().getImages1();
            List<CmsBtProductModel_Field_Image> images2 = productModel.getCommon().getFields().getImages2();
            List<CmsBtProductModel_Field_Image> images3 = productModel.getCommon().getFields().getImages3();
            List<CmsBtProductModel_Field_Image> images4 = productModel.getCommon().getFields().getImages4();
            List<CmsBtProductModel_Field_Image> images5 = productModel.getCommon().getFields().getImages5();
            List<CmsBtProductModel_Field_Image> images6 = productModel.getCommon().getFields().getImages6();
            List<String> images1Arr = new ArrayList<>(images1.size());
            List<String> images2Arr = new ArrayList<>(images2.size());
            List<String> images3Arr = new ArrayList<>(images3.size());
            List<String> images4Arr = new ArrayList<>(images4.size());
            List<String> images5Arr = new ArrayList<>(images5.size());
            List<String> images6Arr = new ArrayList<>(images6.size());
            for (CmsBtProductModel_Field_Image imageItem : images1) {
                images1Arr.add(imageItem.getName());
            }
            for (CmsBtProductModel_Field_Image imageItem : images2) {
                images2Arr.add(imageItem.getName());
            }
            for (CmsBtProductModel_Field_Image imageItem : images3) {
                images3Arr.add(imageItem.getName());
            }
            for (CmsBtProductModel_Field_Image imageItem : images4) {
                images4Arr.add(imageItem.getName());
            }
            for (CmsBtProductModel_Field_Image imageItem : images5) {
                images5Arr.add(imageItem.getName());
            }
            for (CmsBtProductModel_Field_Image imageItem : images6) {
                images6Arr.add(imageItem.getName());
            }
            imageList.add(images1Arr);
            imageList.add(images2Arr);
            imageList.add(images3Arr);
            imageList.add(images4Arr);
            imageList.add(images5Arr);
            imageList.add(images6Arr);
            translationBean.setProdImageList(imageList);

            translationBean.setModifiedTime(productModel.getModified());
            translationBean.setProdId(productModel.getProdId());
            translationBean.setTranslator(productModel.getCommon().getFields().getTranslator());
            translationBean.setTranSts(NumberUtils.toInt(productModel.getCommon().getFields().getTranslateStatus()));

//            // 设置该商品在MT上面的groupId
//            for (CmsBtProductModel_Group_Platform platForm : productModel.getGroups().getPlatforms()) {
//                if (platForm.getCartId() == CartType.MASTER.getCartId())
//                    translationBean.setGroupId(platForm.getGroupId());
//            }
//
//            // 获取其他产品的图片
//            List<CmsBtProductModel> listOtherProducts
//                    = productGroupService.getProductIdsByGroupId(channelId, productModel.getGroups().getPlatforms().get(0).getGroupId(), true);
//            List<Object> otherProducts = new ArrayList<>();
//            for (CmsBtProductModel product : listOtherProducts) {
//                otherProducts.add(product.getFields().getImages1().get(0));
//            }
//            translationBean.setOtherProducts(otherProducts);

            translateTaskBeanList.add(translationBean);

        }
        return translateTaskBeanList;
    }

    /**
     * 获取个人完成翻译数.
     */
    private int getDoneTaskCount(String channelId, String userName) {
        String doneTaskCountQueryStr = String.format("{'fields.status':{'$nin':['New']},'fields.translateStatus':'1','fields.translator':'%s','fields.isMasterMain':1}", userName);
        return ((Long)productService.getCnt(channelId, doneTaskCountQueryStr)).intValue();
    }

    /**
     * 获取所有完成的数量.
     */
    private int getTotalDoneCount(String channelId) {
        String totalDoneCountQueryStr = "{'fields.status':{'$nin':['New']},'fields.translateStatus':'1','fields.isMasterMain':1}";
        return ((Long)productService.getCnt(channelId, totalDoneCountQueryStr)).intValue();
    }

    /**
     * 设定个人完成翻译数、完成翻译总数、待翻译总数.
     */
    private int getTotalUndoneCount(String channelId) {
        String totalUndoneCountQueryStr = "{'fields.status':{'$nin':['New']},'fields.translateStatus':{'$in':[null,'','0']},'fields.translator':{'$in':[null,'','0']},'fields.isMasterMain':1}";
        return ((Long)productService.getCnt(channelId, totalUndoneCountQueryStr)).intValue();
    }
    /**
     * 已分配但未完成.
     */
    private int getTotalDistributionUndoneCount(String channelId) {
        String totalUndoneCountQueryStr = "{'fields.status':{'$nin':['New']},'fields.translateStatus':{'$in':[null,'','0']},'fields.translator':{'$nin':[null,'','0']},'fields.isMasterMain':1}";
        return ((Long)productService.getCnt(channelId, totalUndoneCountQueryStr)).intValue();
    }
    /**
     * 获取 feed info model.
     */
    public Map<String, String> getFeedAttributes(String channelId, String productCode) {

        CmsBtFeedInfoModel feedInfoModel = feedInfoService.getProductByCode(channelId, productCode);

        if (feedInfoModel == null) {
            //feed 信息不存在时异常处理.
            String errMsg = "channel id: " + channelId + " product code: " + productCode + " 对应的品牌方信息不存在！";

            $warn(errMsg);

        }

        Map<String, String> feedAttributes = new HashMap<>();

        if (!StringUtils.isEmpty(feedInfoModel.getCategory())) {
            feedAttributes.put("category", feedInfoModel.getCategory());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getCode())) {
            feedAttributes.put("code", feedInfoModel.getCode());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getName())) {
            feedAttributes.put("name", feedInfoModel.getName());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getModel())) {
            feedAttributes.put("model", feedInfoModel.getModel());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getColor())) {
            feedAttributes.put("color", feedInfoModel.getColor());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getOrigin())) {
            feedAttributes.put("origin", feedInfoModel.getOrigin());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getSizeType())) {
            feedAttributes.put("sizeType", feedInfoModel.getSizeType());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getBrand())) {
            feedAttributes.put("brand", feedInfoModel.getBrand());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getWeight())) {
            feedAttributes.put("weight", feedInfoModel.getWeight());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getShortDescription())) {
            feedAttributes.put("shortDescription", feedInfoModel.getShortDescription());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getLongDescription())) {
            feedAttributes.put("longDescription", feedInfoModel.getLongDescription());
        }

        if (!StringUtils.isEmpty(String.valueOf(feedInfoModel.getUpdFlg()))) {
            feedAttributes.put("updFlg", String.valueOf(feedInfoModel.getUpdFlg()));
        }

        Map<String, List<String>> attributes = feedInfoModel.getAttribute();

        Map<String, String> attributesMap = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : attributes.entrySet()) {

            StringBuilder valueStr = new StringBuilder();

            List<String> values = entry.getValue();

            if (values != null) {
                for (int i = 0; i < values.size(); i++) {
                    if (i < values.size() - 1) {
                        valueStr.append(values.get(i)).append("/");
                    } else {
                        valueStr.append(values.get(i));
                    }
                }
            }

            attributesMap.put(entry.getKey(), valueStr.toString());

        }

        feedAttributes.putAll(attributesMap);

        return feedAttributes;
    }

    /**
     * verifyParameter
     */
    private void verifyParameter(ProductTranslationBean requestBean){

        if (StringUtils.isEmpty(requestBean.getLongTitle())){

            throw new BusinessException("长标题不能为空");
        }
//        if (StringUtils.isEmpty(requestBean.getMiddleTitle())){
//
//            throw new BusinessException("中标题不能为空");
//        }
//        if (StringUtils.isEmpty(requestBean.getShortTitle())){
//
//            throw new BusinessException("短标题不能为空");
//        }
        if (StringUtils.isEmpty(requestBean.getLongDesCn())){

            throw new BusinessException("长描述不能为空");
        }
//        if (StringUtils.isEmpty(requestBean.getShortDesCn())){
//
//            throw new BusinessException("短描述不能为空");
//        }
    }

    /**
     * 获取翻译时标题和描述的长度设置
     */
    public Map<String, Map<String, Object>> getTransLenSet() {
        List<CmsChannelConfigBean> configBeans = CmsChannelConfigs.getConfigBeans(ChannelConfigEnums.Channel.NONE.getId(), CmsConstants.ChannelConfig.TRANS_LEN_SET);
        Map<String, Map<String, Object>> configMap = new HashMap<>();
        for (CmsChannelConfigBean config : configBeans) {
            Map<String, Object> value = new HashMap<>();
            value.put("minLen", config.getConfigValue1());
            value.put("maxLen", config.getConfigValue2());

            configMap.put(config.getConfigCode(), value);
        }
        return configMap;
    }

    /**
     * 获取当前用户未完成的任务.
     */
    public TranslateTaskBean cancelUserTask(UserSessionBean userInfo, String prodCode) {
        if (StringUtils.isEmpty(prodCode)) {
            throw new BusinessException("cancelUserTask::没有参数");
        }

        productService.updateTranslateStatus(userInfo.getSelChannelId(), prodCode, "0", userInfo.getUserName());

        TranslateTaskBean result = new TranslateTaskBean();
        result.setTotalDoneCount(this.getTotalDoneCount(userInfo.getSelChannelId()));
        result.setUserDoneCount(this.getDoneTaskCount(userInfo.getSelChannelId(), userInfo.getUserName()));
        result.setTotalUndoneCount(this.getTotalUndoneCount(userInfo.getSelChannelId()));
        return result;
    }
}
