package com.voyageone.web2.cms.views.translation;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.ProductTransDistrBean;
import com.voyageone.service.dao.cms.CmsMtCustomWordDao;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.enums.CartType;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;
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
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductGroupService productGroupService;

    @Autowired
    protected CmsMtCustomWordDao customWordDao;

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
            "fields.model",
            "fields.images1",
            "fields.translator",
            "fields.translateStatus",
            "groups.platforms",
            "modified"};

    /**
     * 获取当前用户未完成的任务.
     *
     * @param userInfo
     * @return
     * @throws BusinessException
     */
    public TranslateTaskBean getUndoneTasks(UserSessionBean userInfo) throws BusinessException {
        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), -48);
        String translateTimeStr = DateTimeUtil.format(date, null);

        String tasksQueryStr = String.format("{'fields.status':{'$nin':['New']}," +
                "'groups.platforms':{$elemMatch: {'cartId': 0, 'isMain': 1}}," +
                "'fields.translateStatus':'0'," +
                "'fields.translator':'%s'," +
                "'fields.translateTime':{'$gt':'%s'}}", userInfo.getUserName(), translateTimeStr);

        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(tasksQueryStr);
        queryObject.setProjection(RET_FIELDS);

        // 取得所有未翻译的主商品
        List<CmsBtProductModel> cmsBtProductModels = productService.getList(userInfo.getSelChannelId(), queryObject);

        List<ProductTranslationBean> translateTaskBeanList = buildTranslateTaskBeen(userInfo.getSelChannelId(), cmsBtProductModels);
        TranslateTaskBean translateTaskBean = new TranslateTaskBean();
        translateTaskBean.setProductTranslationBeanList(translateTaskBeanList);
        translateTaskBean.setProdListTotal(cmsBtProductModels.size());
        translateTaskBean.setTotalDoneCount(this.getTotalDoneCount(userInfo.getSelChannelId()));
        translateTaskBean.setUserDoneCount(this.getDoneTaskCount(userInfo.getSelChannelId(),userInfo.getUserName()));
        translateTaskBean.setTotalUndoneCount(this.getTotalUndoneCount(userInfo.getSelChannelId()));
        return translateTaskBean;
    }

    /**
     * 用户获取任务列表，自动分发任务.
     *
     * @param userInfo
     * @param translateTaskBean
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
        translateTaskBean.setProdListTotal(cmsBtProductModels.size());
        result.setTotalDoneCount(this.getTotalDoneCount(userInfo.getSelChannelId()));
        result.setUserDoneCount(this.getDoneTaskCount(userInfo.getSelChannelId(), userInfo.getUserName()));
        result.setTotalUndoneCount(this.getTotalUndoneCount(userInfo.getSelChannelId()));
        return result;
    }

    /**
     * 暂时保存翻译任务.
     * @param userInfo
     * @param taskBean
     * @param transSts
     * @return
     */
    public TranslateTaskBean saveTask(UserSessionBean userInfo, ProductTranslationBean taskBean, String transSts) {

        // check翻译数据是否正确
        if("1".equalsIgnoreCase(transSts)){
            verifyParameter(taskBean);
        }

        // 先查询该商品对应的group信息
//        $debug("TranslationService.saveTask() 商品ProdId=" + taskBean.getProdId());
//
//
//        DBObject excObj = new BasicDBObject();
//        excObj.put("groups.platforms.cartId", 1);
//        excObj.put("groups.platforms.groupId", 1);
//        excObj.put("groups.platforms.isMain", 1);
//
//        List<DBObject> rslt = mongoDao.find("cms_bt_product_c" + userInfo.getSelChannel(), new BasicDBObject("prodId", taskBean.getProdId()), excObj);
//        int groupId = 0;
//        for (DBObject obj : rslt) {
//            DBObject groupObj = (DBObject) obj.get("groups");
//            if (groupObj != null) {
//                Object platObj = groupObj.get("platforms");
//                if (platObj != null) {
//                    for (Object obj2 : (List) platObj) {
//                        int cartId = (Integer) ((DBObject) obj2).get("cartId");
//                        int isMain = (Integer) ((DBObject) obj2).get("isMain");
//                        if (cartId == 1 && isMain == 1) {
//                            groupId = (Integer) ((DBObject) obj2).get("groupId");
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//
//        DBObject params = new BasicDBObject();
//        if (groupId == 0) {
//            $warn("TranslationService.saveTask() 无group信息 商品ProdId=" + taskBean.getProdId());
//            // 只更新该商品信息
//            params.put("prodId", taskBean.getProdId());
//        } else {
//            // 更新该group下的商品信息
//            DBObject pal4 = new BasicDBObject();
//            pal4.put("cartId", 1);
//            pal4.put("groupId", groupId);
//            DBObject pal3 = new BasicDBObject();
//            pal3.put("$elemMatch", pal4);
//            params.put("groups.platforms", pal3);
//        }

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

        productService.updateTranslation(userInfo.getSelChannelId(), taskBean.getGroupId(), updObj, userInfo.getUserName());

        TranslateTaskBean translateTaskBean = new TranslateTaskBean();
        translateTaskBean.setModifiedTime(DateTimeUtil.getNowTimeStamp());
        translateTaskBean.setTotalDoneCount(this.getTotalDoneCount(userInfo.getSelChannelId()));
        translateTaskBean.setUserDoneCount(this.getDoneTaskCount(userInfo.getSelChannelId(), userInfo.getUserName()));
        translateTaskBean.setTotalUndoneCount(this.getTotalUndoneCount(userInfo.getSelChannelId()));
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
     *
     * @param userInfo
     * @param reqBean
     * @return
     */
    public TranslateTaskBean searchUserTasks(UserSessionBean userInfo, Map reqBean) {
        String condition = (String) reqBean.get("searchCondition");
        int tranSts = NumberUtils.toInt((String) reqBean.get("tranSts"), -1);
        if (StringUtils.isEmpty(condition) && tranSts == -1) {
            return this.getUndoneTasks(userInfo);
        }

        String tasksQueryStr = String.format("{'groups.platforms.cartId':0,'fields.translator':'%s'", userInfo.getUserName());
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
        queryObject.setProjection(RET_FIELDS);
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
     * @param cmsBtProductModels
     * @return
     */
    private List<ProductTranslationBean> buildTranslateTaskBeen(String channelId, List<CmsBtProductModel> cmsBtProductModels) {
        List<ProductTranslationBean> translateTaskBeanList = new ArrayList<>(cmsBtProductModels.size());

        for (CmsBtProductModel productModel:cmsBtProductModels){
            ProductTranslationBean translationBean = new ProductTranslationBean();

            translationBean.setLongDescription(productModel.getFields().getLongDesEn());
            translationBean.setProductName(productModel.getFields().getProductNameEn());
            translationBean.setShortDescription(productModel.getFields().getShortDesEn());

            translationBean.setLongTitle(productModel.getFields().getLongTitle());
            translationBean.setMiddleTitle(productModel.getFields().getMiddleTitle());
            translationBean.setShortTitle(productModel.getFields().getShortTitle());
            translationBean.setLongDesCn(productModel.getFields().getLongDesCn());
            translationBean.setShortDesCn(productModel.getFields().getShortDesCn());
            translationBean.setModel(productModel.getFields().getModel());
            translationBean.setProductCode(productModel.getFields().getCode());
            translationBean.setProductImage(productModel.getFields().getImages1().get(0).getName());
            translationBean.setModifiedTime(productModel.getModified());
            translationBean.setProdId(productModel.getProdId());
            translationBean.setTranslator(productModel.getFields().getTranslator());
            translationBean.setTranSts(NumberUtils.toInt(productModel.getFields().getTranslateStatus()));

            // 设置该商品在MT上面的groupId
            for (CmsBtProductModel_Group_Platform platForm : productModel.getGroups().getPlatforms()) {
                if (platForm.getCartId() == CartType.MASTER.getCartId())
                    translationBean.setGroupId(platForm.getGroupId());
            }

            // 获取其他产品的图片
            List<CmsBtProductModel> listOtherProducts
                    = productGroupService.getProductIdsByGroupId(channelId, productModel.getGroups().getPlatforms().get(0).getGroupId(), true);
            List<Object> otherProducts = new ArrayList<>();
            for (CmsBtProductModel product : listOtherProducts) {
                otherProducts.add(product.getFields().getImages1().get(0));
            }
            translationBean.setOtherProducts(otherProducts);

            translateTaskBeanList.add(translationBean);

        }
        return translateTaskBeanList;
    }

    /**
     * 获取个人完成翻译数.
     * @param channelId
     * @param userName
     */
    private int getDoneTaskCount(String channelId, String userName) {
        String doneTaskCountQueryStr = String.format("{'fields.status':{'$nin':['New']},'fields.translateStatus':'1','fields.translator':'%s'}", userName);
        return ((Long)productService.getCnt(channelId, doneTaskCountQueryStr)).intValue();
    }

    /**
     * 获取所有完成的数量.
     * @param channelId
     */
    private int getTotalDoneCount(String channelId) {
        String totalDoneCountQueryStr = "{'fields.status':{'$nin':['New']},'fields.translateStatus':'1'}";
        return ((Long)productService.getCnt(channelId, totalDoneCountQueryStr)).intValue();
    }

    /**
     * 设定个人完成翻译数、完成翻译总数、待翻译总数.
     * @param channelId
     */
    private int getTotalUndoneCount(String channelId) {
        String totalUndoneCountQueryStr = "{'fields.status':{'$nin':['New']},'fields.translateStatus':{'$in':[null,'','0']}}";
        return ((Long)productService.getCnt(channelId, totalUndoneCountQueryStr)).intValue();
    }

    /**
     * 获取 feed info model.
     *
     * @param channelId
     * @param productCode
     * @return
     */
    public Map<String, String> getFeedAttributes(String channelId, String productCode) {

        CmsBtFeedInfoModel feedInfoModel = cmsBtFeedInfoDao.selectProductByCode(channelId, productCode);

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

        if (!StringUtils.isEmpty(feedInfoModel.getShort_description())) {
            feedAttributes.put("short_description", feedInfoModel.getShort_description());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getLong_description())) {
            feedAttributes.put("long_description", feedInfoModel.getLong_description());
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
     *
     * @param requestBean
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
     * @param chnId
     * @return
     */
    public Map<String, Object> getTransLenSet(String chnId) {
        Map<String, Object> setInfo = new HashMap<String, Object>();
        List<Map<String, Object>> rslt = customWordDao.selectTransLenSet(chnId);
        for (Map<String, Object> item : rslt) {
            String lenType = (String) item.get("lenType");
            item.remove("lenType");
            setInfo.put(lenType, item);
        }
        return setInfo;
    }

    /**
     * 获取当前用户未完成的任务.
     *
     * @param userInfo
     * @param prodCode
     */
    public void cancelUserTask(UserSessionBean userInfo, String prodCode) {
        if (StringUtils.isEmpty(prodCode)) {
            throw new BusinessException("cancelUserTask::没有参数");
        }
        Map paraMap = new HashMap<>(1);
        paraMap.put("fields.code", prodCode);
        Map rsMap = new HashMap<>(3);
        rsMap.put("fields.translateStatus", "0");
        rsMap.put("modifier", userInfo.getUserName());
        rsMap.put("modified", DateTimeUtil.getNowTimeStamp());

        cmsBtProductDao.update(userInfo.getSelChannelId(), paraMap, rsMap);
    }
}
