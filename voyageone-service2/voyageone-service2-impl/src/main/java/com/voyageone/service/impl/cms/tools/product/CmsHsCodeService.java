package com.voyageone.service.impl.cms.tools.product;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author gjl 2016/6/7
 * @version 2.0.0
 */
@Service
public class CmsHsCodeService extends BaseService {

    private static final int EXPIRE_HOURS = -48;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    //获取任务数量Id
    private static int hsCodeTaskCntTypeId = 82;
    //获取任务数量名称
    private static String hsCodeTaskCntTypeName = "GetTheN0umberOfTasks";
    /**
     * 设定返回值.
     */
    private static String[] RET_FIELDS = {
            "common.fields.code",
            "common.catPath",
            "common.fields.productNameEn",
            "common.fields.originalTitleCn",
            "feed.catPath",
            "common.fields.materialCn",
            "common.fields.materialEn",
            "common.fields.hsCodePrivate",
            "common.fields.hsCodeCrop",
            "common.fields.images1",
            "common.fields.hsCodeStatus",
            "common.fields.hsCodeSetter",
            "common.fields.hsCodeSetTime",
            "common.fields.quantity",
    };

    /**
     * HsCode信息检索
     *
     * @param lang
     * @param channelId
     * @param userName
     * @param param
     * @return data
     */
    public Map<String, Object> searchHsCodeInfo(String lang, String channelId, String userName, Map param) {
        //返回数据类型
        Map<String, Object> data = new HashMap<>();
        //商品税号设置状态
        data.put("taskSummary", getTaskSummary(channelId, userName));
        //税号设置状态
        String hsCodeStatus = (String) param.get("hsCodeStatus");
        //模糊查询(ProductCode，商品名称)
        String condition = (String) param.get("searchCondition");
        //显示第几页
        int curr = (Integer) param.get("curr");
        //每页显示的数目
        int size = (Integer) param.get("size");
        //个人设置税号成果搜索用
        boolean idCondition = true;
        //设置税一览的信息
        data.put("hsCodeList", getTotalHsCodeList(channelId, userName, hsCodeStatus, condition, curr, size, RET_FIELDS, idCondition));
        //取得总页数
        data.put("total", countByQuery(channelId, userName, hsCodeStatus, condition, idCondition));
        //税号个人
        data.put("hsCodeValue", TypeChannels.getTypeWithLang("hsCodePrivate", channelId, lang));
        //获取任务数量
        data.put("hsCodeTaskCnt", Integer.parseInt(Types.getValue(hsCodeTaskCntTypeId, hsCodeTaskCntTypeName, lang)));
        //返回数据类型
        return data;
    }

    /**
     * 设置税一览的信息
     *
     * @param channelId
     * @param userName
     * @param hsCodeStatus
     * @param condition
     * @param curr
     * @param size
     * @param retFields
     * @param idCondition
     * @return getTotalHsCodeList
     */
    private Object getTotalHsCodeList(String channelId, String userName, String hsCodeStatus, String condition, int curr, int size, String[] retFields, boolean idCondition) {
        //已经翻译的标志位
        String translateStatus = "1";
        //检索条件
        String parameter = getSearchQuery(channelId, userName, hsCodeStatus, translateStatus, condition, idCondition);
        JomgoQuery queryObject = new JomgoQuery();
        //取得收索的条件
        queryObject.setQuery(parameter);
        //设定返回值
        queryObject.setProjectionExt(retFields);
        //每页检索个数
        queryObject.setLimit(size);
        //翻页检索
        queryObject.setSkip((curr - 1) * size);
        return cmsBtProductDao.select(queryObject, channelId);
    }

    /**
     * 取得总页数
     *
     * @param channelId
     * @param userName
     * @param hsCodeStatus
     * @param condition
     * @param idCondition
     * @return countByQuery
     */
    private long countByQuery(String channelId, String userName, String hsCodeStatus, String condition, boolean idCondition) {
        //已经翻译的标志位
        String translateStatus = "1";
        //检索条件
        String parameter = getSearchQuery(channelId, userName, hsCodeStatus, translateStatus, condition, idCondition);
        return cmsBtProductDao.countByQuery(parameter, channelId);
    }

    /**
     * 获取任务
     *
     * @param lang
     * @param channelId
     * @param userName
     * @param param
     * @return data
     */
    public Map<String, Object> getHsCodeInfo(String lang, String channelId, String userName, Map param) {
        //返回数据类型
        Map<String, Object> data = new HashMap<>();
        //循序(升序 降序)
        String qtyOrder = (String) param.get("order");
        //商品Code
        String code = (String) param.get("code");
        //获取任务数
        int hsCodeTaskCnt = (Integer) param.get("hsCodeTaskCnt");
        //显示第几页
        int curr = (Integer) param.get("curr");
        //每页显示的数目
        int size = (Integer) param.get("size");
        //主数据
        int cartId = 1;
        //hsCodeStatus
        String hsCodeStatus = "0";
        //用户名称
        String noUserName = "";
        //税号/品名/单位
        String hsCodePrivate = "";
        //模糊查询
        String condition = "";
        //模糊查询标志位
        boolean isCondition = false;
        //HsCodeCheck
        checkHsCode(hsCodeTaskCnt, lang, userName, channelId);
        //根据获取任务数去取得对应的code
        List<CmsBtProductModel> hsCodeList = getHsCodeInfo(channelId, hsCodeStatus, noUserName, hsCodeTaskCnt, RET_FIELDS, qtyOrder, code, isCondition);
        //取得codeList结果集
        List<String> codeList = new ArrayList<>();
        //取得获取任务的信息
        if (!hsCodeList.isEmpty()) {
            for (CmsBtProductModel model : hsCodeList) {
                codeList.add(model.getCommon().getFields().getCode());
            }
        }
        //根据获取任务的主code同步到master同一个Group下所有code
        List<String> allCodeList = getAllCodeList(codeList, channelId, cartId);
        //当前日期及时间
        String hsCodeSetTime = DateTimeUtil.getNowTimeStamp();
        //更新cms_bt_product表的hsCodeInfo
        if (!allCodeList.isEmpty()) {
            updateHsCodeInfo(channelId, allCodeList, userName, hsCodeStatus, hsCodePrivate, hsCodeSetTime);
        }
        //商品税号设置状态
        data.put("taskSummary", getTaskSummary(channelId, userName));
        //税号个人
        data.put("hsCodeValue", TypeChannels.getTypeWithLang("hsCodePrivate", channelId, lang));
        //等待设置税一览
        data.put("hsCodeList", getTotalHsCodeList(channelId, userName, hsCodeStatus, condition, curr, size, RET_FIELDS, isCondition));
        //返回数据类型
        return data;
    }

    /**
     * 获取任务
     *
     * @param channelId
     * @param hsCodeStatus
     * @param userName
     * @param hsCodeTaskCnt
     * @param retFields
     * @param qtyOrder
     * @param code
     * @param isCondition
     * @return 获取任务
     */
    private List<CmsBtProductModel> getHsCodeInfo(String channelId, String hsCodeStatus, String userName
            , int hsCodeTaskCnt, String[] retFields, String qtyOrder, String code, boolean isCondition) {
        String translateStatus = "1";
        String parameter = getSearchQuery(channelId, userName, hsCodeStatus, translateStatus, code, isCondition);
        JomgoQuery queryObject = new JomgoQuery();
        //取得收索的条件
        queryObject.setQuery(parameter);
        if (!StringUtils.isEmpty(qtyOrder)) {
            if (qtyOrder.equals("1")) {
                queryObject.setSort("{'common.fields.quantity' : 1}");
            } else {
                queryObject.setSort("{'common.fields.quantity' : -1}");
            }
        }
        queryObject.setProjectionExt(retFields);
        queryObject.setLimit(hsCodeTaskCnt);
        return cmsBtProductDao.select(queryObject, channelId);
    }

    /**
     * 获取任务更新
     *
     * @param channelId
     * @param allCodeList
     * @param userName
     * @param hsCodeStatus
     * @param hsCodePrivate
     * @param hsCodeSetTime
     */
    private void updateHsCodeInfo(String channelId, List<String> allCodeList, String userName, String hsCodeStatus, String hsCodePrivate, String hsCodeSetTime) {

        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("common.fields.hsCodePrivate", hsCodePrivate);
        updateMap.put("common.fields.hsCodeStatus", hsCodeStatus);
        updateMap.put("common.fields.hsCodeSetter", userName);
        updateMap.put("common.fields.hsCodeSetTime", hsCodeSetTime);
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        for (String code : allCodeList) {
            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("common.fields.code", code);
            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
        }
        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, userName, "$set");
    }

    /**
     * 获取任务check
     *
     * @param hsCodeTaskCnt
     * @param lang
     * @param userName
     * @param channelId
     */
    private void checkHsCode(int hsCodeTaskCnt, String lang, String userName, String channelId) {
        Map<String, Object> taskSummary = new HashMap<>();
        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), EXPIRE_HOURS);
        String hsCodeTimeStr = DateTimeUtil.format(date, null);
        //个人完成税号商品数
        String queryStr = String.format("{'common.fields.isMasterMain':1," +
                "'common.fields.hsCodeStatus':'0'," +
                "'common.fields.translateStatus':'1'," +
                "'common.fields.hsCodeSetTime':{'$gte':'%s'}," +
                "'common.fields.hsCodeSetter':'%s'}", hsCodeTimeStr, userName);
        Long cnt = cmsBtProductDao.countByQuery(queryStr, channelId);
        if (cnt > 0) {
            // 商品税号任务已存在不能获取
            throw new BusinessException("7000091");
        }
        //获取任务
        String maxCnt = Types.getValue(hsCodeTaskCntTypeId, hsCodeTaskCntTypeName, lang);
        if (hsCodeTaskCnt > Integer.parseInt(maxCnt)) {
            // 获取任务数量不能超过maxCnt
            throw new BusinessException("7000092", maxCnt);
        }
    }

    /**
     * 根据codeList取得cms_bt_product_group相关的code
     *
     * @param codeList
     * @param channelId
     * @param cartId
     * @return allCodeList
     */
    private List<String> getAllCodeList(List<String> codeList, String channelId, int cartId) {
        List<String> allCodeList = new ArrayList<>();
        // 获取产品对应的group信息
        for (String allCode : codeList) {
            //循环取得allCode
            CmsBtProductGroupModel groupModel = productGroupService.selectProductGroupByCode(channelId, allCode, cartId);
            if (groupModel != null) {
                if (!groupModel.getProductCodes().isEmpty()) {
                    //循环取单条code
                    for (String code : groupModel.getProductCodes()) {
                        allCodeList.add(code);
                    }
                }
            } else {
                return codeList;
            }
        }
        return allCodeList;
    }

    /**
     * HsCode信息保存
     *
     * @param channelId
     * @param userName
     * @param param
     * @return data
     */
    public Map<String, Object> saveHsCodeInfo(String channelId, String userName, Map param) {
        //返回数据类型
        Map<String, Object> data = new HashMap<>();
        String code = (String) param.get("code");
        String hsCodePrivate = (String) param.get("hsCodePrivate");
        String hsCodeStatus = "1";
        //当前日期及时间
        String hsCodeSetTime = DateTimeUtil.getNowTimeStamp();
        //主数据
        int cartId = 1;
        List<String> codeList = new ArrayList<>();
        codeList.add(code);
        //根据获取任务的主code同步到master同一个Group下所有code
        List<String> allCodeList = getAllCodeList(codeList, channelId, cartId);
        //更新cms_bt_product表的hsCodeInfo
        updateHsCodeInfo(channelId, allCodeList, userName, hsCodeStatus, hsCodePrivate, hsCodeSetTime);
        //商品税号设置状态
        data.put("taskSummary", getTaskSummary(channelId, userName));
        return data;
    }

    /**
     * 商品税号设置状态
     *
     * @param channelId
     * @param userName
     * @return taskSummary
     */
    private Map<String, Object> getTaskSummary(String channelId, String userName) {
        Map<String, Object> taskSummary = new HashMap<>();
        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), EXPIRE_HOURS);
        String hsCodeTimeStr = DateTimeUtil.format(date, null);
        //未分配的任务
        String queryStr = String.format("{'$and':[ {'common.fields.translateStatus':'1'," +
                "'common.fields.isMasterMain':1," +
                "'common.fields.hsCodeStatus':'0'," +
                " '$or': [{'common.fields.hsCodeSetter':''}," +
                "{'common.fields.hsCodeSetter':null}," +
                "{'common.fields.hsCodeSetTime':{'$lte':'%s'}}," +
                "{'common.fields.hsCodeSetTime':''}," +
                "{'common.fields.hsCodeSetTime':null}]}]}", hsCodeTimeStr);
        //未设置总数:等待设置税号的商品总数（包含 未分配+已分配但是已过期）
        taskSummary.put("notAssignedTotalHsCodeCnt", cmsBtProductDao.countByQuery(queryStr, channelId));

        //已分配但未完成的任务
        queryStr = String.format("{'common.fields.isMasterMain':1," +
                "'common.fields.translateStatus':'1'," +
                "'common.fields.hsCodeStatus':'0'," +
                "'common.fields.hsCodeSetter':{'$ne' : ''}," +
                "'common.fields.hsCodeSetTime':{'$gt':'%s'}}", hsCodeTimeStr);
        //已分配但未完成总数:已经被分配，但是未过期（无法释放）的商品总数
        taskSummary.put("alreadyAssignedTotalHsCodeCnt", cmsBtProductDao.countByQuery(queryStr, channelId));

        //完成税号总商品数
        queryStr = "{'common.fields.isMasterMain':1," +
                "'common.fields.hsCodeStatus':'1'," +
                "'common.fields.translateStatus':'1'}";
        //设置税号商品总数:当前Channel已经被设置税号的商品总数
        taskSummary.put("setChannelTotalHsCodeCnt", cmsBtProductDao.countByQuery(queryStr, channelId));

        //个人完成税号商品数
        queryStr = String.format("{'common.fields.isMasterMain':1," +
                "'common.fields.hsCodeStatus':'1'," +
                "'common.fields.translateStatus':'1'," +
                "'common.fields.hsCodeSetter':'%s'}", userName);
        //个人设置税号商品译数:当前用户税号设置总数
        taskSummary.put("setPersonalTotalHsCodeCnt", cmsBtProductDao.countByQuery(queryStr, channelId));

        return taskSummary;
    }

    /**
     * 查询条件
     *
     * @param channelId
     * @param userName
     * @param hsCodeStatus
     * @param translateStatus
     * @param condition
     * @return 返回拼接的查询条件
     */
    private String getSearchQuery(String channelId, String userName, String hsCodeStatus
            , String translateStatus, String condition, boolean isCondition) {
        StringBuilder sbQuery = new StringBuilder();
        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), EXPIRE_HOURS);
        String hsCodeTimeStr = DateTimeUtil.format(date, null);

        //hsCodePrivate
        if (!StringUtils.isEmpty(userName)) {
            sbQuery.append(MongoUtils.splicingValue("common.fields.hsCodeSetter", userName));
            sbQuery.append(",");
        }
        //hsCodeStatus
        if (!StringUtils.isEmpty(hsCodeStatus)) {
            sbQuery.append(MongoUtils.splicingValue("common.fields.hsCodeStatus", hsCodeStatus));
            sbQuery.append(",");
        }
        //translateStatus
        if (!StringUtils.isEmpty(translateStatus)) {
            sbQuery.append(MongoUtils.splicingValue("common.fields.translateStatus", translateStatus));
            sbQuery.append(",");
        }
        //condition
        if (!StringUtils.isEmpty(condition)) {
            List<String> orSearch = new ArrayList<>();
            orSearch.add(MongoUtils.splicingValue("common.fields.code", condition, "$regex"));
            orSearch.add(MongoUtils.splicingValue("common.fields.productNameEn", condition, "$regex"));
            orSearch.add(MongoUtils.splicingValue("common.fields.originalTitleCn", condition, "$regex"));
            sbQuery.append(MongoUtils.splicingValue("", orSearch.toArray(), "$or"));
            sbQuery.append(",");
        }
        //个人设置税号成果搜索用
        if (isCondition && hsCodeStatus.equals("0")) {
            //hsCodeSetTime
            sbQuery.append(MongoUtils.splicingValue("common.fields.hsCodeSetTime", hsCodeTimeStr, "$gte"));
            sbQuery.append(",");
        }
        //channelId
        sbQuery.append(MongoUtils.splicingValue("channelId", channelId));
        sbQuery.append(",");
        //code
        sbQuery.append(MongoUtils.splicingValue("common.fields.isMasterMain", 1));
        return "{" + sbQuery.toString() + "}";
    }
}
