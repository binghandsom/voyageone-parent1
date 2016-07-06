package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.TypeChannels;
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
 * InventoryCenterLogicService
 *
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
    };

    /**
     * HsCode信息检索
     */
    public Map<String, Object> searchHsCodeInfo(String lang, String channelId, String userName, Map param) {
        //返回数据类型
        Map<String, Object> data = new HashMap<>();
        //商品税号设置状态
        data.put("taskSummary", getTaskSummary(channelId, userName));
        /**
         *个人设置税号成果搜索
         * hsCodeStatus(0未设置 1已设置)
         * searchCondition(模糊查询(ProductCode，商品名称))
         */
        //税号设置状态
        String hsCodeStatus = (String) param.get("hsCodeStatus");
        //模糊查询(ProductCode，商品名称)
        String condition = (String) param.get("searchCondition");
        //显示第几页
        int curr = (Integer) param.get("curr");
        //每页显示的数目
        int size = (Integer) param.get("size");
        //设置税一览的信息
        data.put("hsCodeList", getTotalHsCodeList(channelId, userName, hsCodeStatus, condition, curr, size, RET_FIELDS));
        //取得总页数
        data.put("total", countByQuery(channelId, userName, hsCodeStatus, condition));
        //税号个人
        data.put("hsCodeValue", TypeChannels.getTypeWithLang("hsCodePrivate", channelId, lang));
        //返回数据类型
        return data;
    }

    /**
     * 设置税一览的信息
     */
    private Object getTotalHsCodeList(String channelId, String userName, String hsCodeStatus, String condition, int curr, int size, String[] retFields) {
        String parameter = getSearchQuery(channelId, userName, hsCodeStatus, "1", condition);
        JomgoQuery queryObject = new JomgoQuery();
        //取得收索的条件
        queryObject.setQuery(parameter);
        queryObject.setProjectionExt(retFields);
        queryObject.setLimit(size);
        queryObject.setSkip((curr - 1) * size);
        return cmsBtProductDao.select(queryObject, channelId);
    }

    /**
     * 取得总页数
     */
    private long countByQuery(String channelId, String userName, String hsCodeStatus, String condition) {
        String parameter = getSearchQuery(channelId, userName, hsCodeStatus, "1", condition);
        return cmsBtProductDao.countByQuery(parameter, channelId);
    }

    /**
     * 获取任务
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
        //HsCodeCheck
        checkHsCode(hsCodeTaskCnt, lang);
        //根据获取任务数去取得对应的code
        List<CmsBtProductModel> hsCodeList = getHsCodeInfo(channelId, "0", "", hsCodeTaskCnt, RET_FIELDS, qtyOrder, code);
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
            updateHsCodeInfo(channelId, allCodeList, userName, "0", "", hsCodeSetTime);
        }
        //商品税号设置状态
        data.put("taskSummary", getTaskSummary(channelId, userName));
        //税号个人
        data.put("hsCodeValue", TypeChannels.getTypeWithLang("hsCodePrivate", channelId, lang));
        //等待设置税一览
        data.put("hsCodeList", getTotalHsCodeList(channelId, userName, "0", "", curr, size, RET_FIELDS));
        //返回数据类型
        return data;
    }

    /**
     * 获取任务
     */
    private List<CmsBtProductModel> getHsCodeInfo(String channelId, String hsCodeStatus, String userName
            , int hsCodeTaskCnt, String[] retFields, String qtyOrder, String code) {
        String parameter = getSearchQuery(channelId, userName, hsCodeStatus, "1", code);
        String qtyOrderValue = "{common.fields.quantity:" + qtyOrder + "}";
        JomgoQuery queryObject = new JomgoQuery();
        //取得收索的条件
        queryObject.setQuery(parameter);
        if (!StringUtils.isEmpty(qtyOrder)) {
            queryObject.setSort(qtyOrderValue);
        }
        queryObject.setProjectionExt(retFields);
        queryObject.setLimit(hsCodeTaskCnt);
        return cmsBtProductDao.select(queryObject, channelId);
    }

    /**
     * 获取任务更新
     */
    private void updateHsCodeInfo(String channelId, List<String> allCodeList, String userName, String hsCodeStatus, String hsCodeSetter, String hsCodeSetTime) {

        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("common.fields.hsCodePrivate", userName);
        updateMap.put("common.fields.hsCodeStatus", hsCodeStatus);
        updateMap.put("common.fields.hsCodeSetter", hsCodeSetter);
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
     * 获取任务最好是加上最大值check，默认为10，最大不能超过50
     */
    private void checkHsCode(int hsCodeTaskCnt, String lang) {
        //选择个数判断
        if (hsCodeTaskCnt > 50) {
            // 类目选择check
            throw new BusinessException("获取任务最好是加上最大值check，默认为10，最大不能超过50", 50);
        }
    }

    /**
     * 根据codeList取得cms_bt_product_group相关的code
     */
    private List<String> getAllCodeList(List<String> codeList, String channelId, int cartId) {
        List<String> allCodeList = new ArrayList<>();
        // 获取产品对应的group信息
        for (String allCode : codeList) {
            //循环取得allCode
            CmsBtProductGroupModel groupModel = productGroupService.selectProductGroupByCode(channelId, allCode, cartId);
            if (!groupModel.getProductCodes().isEmpty()) {
                //循环取单条code
                for (String code : groupModel.getProductCodes()) {
                    allCodeList.add(code);
                }
            }

        }
        return allCodeList;
    }

    /**
     * 保存任务
     */
    public Map<String, Object> saveHsCodeInfo(String channelId, String userName, Map param) {
        //返回数据类型
        Map<String, Object> data = new HashMap<>();
        String code = (String) param.get("code");
        String hsCodeSetter = (String) param.get("hsCodeSetter");
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
        updateHsCodeInfo(channelId, allCodeList, userName, hsCodeStatus, hsCodeSetter, hsCodeSetTime);
        //商品税号设置状态
        data.put("taskSummary", getTaskSummary(channelId, userName));
        return data;
    }

    /**
     * 商品税号设置状态
     */
    private Map<String, Object> getTaskSummary(String channelId, String userName) {
        /**
         * 商品税号设置状态,传入的参数
         * channelId(当前用户信息)
         * hsCodePrivate(userName)
         * hsCodeStatus(0未设置 1已设置)
         * translateStatus(0未设置 1已设置)
         * hsCodeSetter(null未设置 notNull已设置)
         */
        Map<String, Object> taskSummary = new HashMap<>();
        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), EXPIRE_HOURS);
        String hsCodeTimeStr = DateTimeUtil.format(date, null);
        //未分配的任务
        String queryStr = String.format("{'common.fields.translateStatus':'1'," +
                "'common.fields.isMasterMain':1," +
                "'common.fields.hsCodeStatus':'0'," +
                " '$or': [{'common.fields.hsCodeSetter':''} ,  " +
                "{'common.fields.hsCodeSetTime':{'$lte':'%s'}} , " +
                "{'common.fields.hsCodePrivate':{'$exists' : false}}, " +
                "{'common.fields.hsCodeSetTime':{'$exists' : false}}]}", hsCodeTimeStr);
        //未设置总数:等待设置税号的商品总数（包含 未分配+已分配但是已过期）
        taskSummary.put("notAssignedTotalHsCodeCnt", cmsBtProductDao.countByQuery(queryStr, channelId));

        //已分配但未完成的任务
        queryStr = String.format("{'common.fields.isMasterMain':1," +
                "'common.fields.translateStatus':'1'," +
                "'common.fields.hsCodeStatus':'0'," +
                "'common.fields.hsCodeSetTime':{'$exists' : true}," +
                "'common.fields.hsCodePrivate':{'$ne' : ''}," +
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
                "'common.fields.hsCodePrivate':'%s'}", userName);
        //个人设置税号商品译数:当前用户税号设置总数
        taskSummary.put("setPersonalTotalHsCodeCnt", cmsBtProductDao.countByQuery(queryStr, channelId));

        return taskSummary;
    }

    /**
     * getSearchQuery
     */
    private String getSearchQuery(String channelId, String userName, String hsCodeStatus
            , String translateStatus, String condition) {
        StringBuilder sbQuery = new StringBuilder();
        //hsCodePrivate
        if (!StringUtils.isEmpty(userName)) {
            sbQuery.append(MongoUtils.splicingValue("common.fields.hsCodePrivate", userName));
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
        //channelId
        sbQuery.append(MongoUtils.splicingValue("channelId", channelId));
        sbQuery.append(",");
        //code
        sbQuery.append(MongoUtils.splicingValue("common.fields.isMasterMain", 1));
        return "{" + sbQuery.toString() + "}";
    }
}
