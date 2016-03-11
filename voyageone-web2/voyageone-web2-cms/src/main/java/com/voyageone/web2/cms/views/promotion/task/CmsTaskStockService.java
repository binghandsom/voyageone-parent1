package com.voyageone.web2.cms.views.promotion.task;
;
import com.voyageone.cms.service.model.CmsBtFeedInfoModel;
import com.voyageone.common.configs.TypeChannel;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.dao.CmsBtStockSeparateIncrementItemDao;
import com.voyageone.web2.cms.dao.CmsBtStockSeparateItemDao;
import com.voyageone.web2.cms.dao.CmsBtStockSeparatePlatformInfoDao;
import com.voyageone.web2.cms.dao.WmsBtLogicInventoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by jeff.duan on 2016/03/04.
 */
@Service
public class CmsTaskStockService extends BaseAppService {

    @Autowired
    private CmsBtStockSeparatePlatformInfoDao cmsBtStockSeparatePlatformInfoDao;

    @Autowired
    private CmsBtStockSeparateItemDao cmsBtStockSeparateItemDao;

    @Autowired
    private CmsBtStockSeparateIncrementItemDao cmsBtStockSeparateIncrementItemDao;

    @Autowired
    private WmsBtLogicInventoryDao wmsBtLogicInventoryDao;

    /**
     * 取得库存隔离数据各种状态的数量
     *
     * @param param 客户端参数
     * @return 某种状态的数量
     */
    public List<Map<String,Object>>  getStockStatusCount(Map param){
//        Map<String, Object> sqlParam = new HashMap<String, Object>();
//        sqlParam.put("taskId", taskId);
//        sqlParam.put("status", status);
//        return cmsBtStockSeparateItemDao.selectStockSeparateItemCnt(sqlParam);

        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("sql", getStockStatusCountSql(param));
        List<Map<String,Object>> statusCountList = cmsBtStockSeparateItemDao.selectStockSeparateItemBySqlMap(sqlParam);
        return statusCountList;
    }

    /**
     * 取得符合条件的sku数量
     *
     * @param param 客户端参数
     * @return 某种状态的数量
     */
    public int getStockSkuCount(Map param){
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("sql", getStockSkuCountSql(param));
        List<Object> countInfo = cmsBtStockSeparateItemDao.selectStockSeparateItemBySqlObject(sqlParam);
        return  Integer.parseInt(String.valueOf(countInfo.get(0)));
    }

    /**
     * 取得属性列表
     * 属性列表数据结构
     *    {"value":"property1", "name":"品牌", "logic":"", "type":"", "show":false},
     *    {"value":"property2", "name":"英文短描述", "logic":"Like", "type":"", "show":false},
     *    {"value":"property3", "name":"性别", "logic":"", "type":"", "show":false}，
     *    {"value":"property4", "name":"Size", "logic":"", "type":"int", "show":false}...
     *
     * @param param 客户端参数
     * @return 获取取得属性列表
     */
    public List<Map<String,Object>> getPropertyList(Map param){
        List<Map<String,Object>> propertyList = new ArrayList<Map<String,Object>>();

        List<TypeChannelBean> dynamicPropertyList = TypeChannel.getTypeList("dynamicProperty", (String) param.get("channelId"));
        for (TypeChannelBean dynamicProperty : dynamicPropertyList) {
            if (((String) param.get("lang")).equals(dynamicProperty.getLang_id())) {
                Map<String, Object> propertyItem = new HashMap<String, Object>();
                propertyItem.put("property", dynamicProperty.getValue());
                propertyItem.put("name", dynamicProperty.getName());
                propertyItem.put("logic", dynamicProperty.getAdd_name1());
                propertyItem.put("type", dynamicProperty.getAdd_name2());
                propertyItem.put("show", false);
                propertyItem.put("value", "");
                propertyList.add(propertyItem);
            }
        }
        return propertyList;
    }

    /**
     * 取得任务对应平台信息列表
     * 平台信息列表数据结构
     *    {"cartId":"23", "cartName":"天猫国际"},
*         {"cartId":"27", "cartName":"聚美优品"}...
     *
     * @param param 客户端参数
     * @return 获取取得属性列表
     */
    public List<Map<String,Object>> getPlatformList(Map param){
        return cmsBtStockSeparatePlatformInfoDao.selectStockSeparatePlatform(param);
    }

    /**
     * 获取库存隔离明细
     *
     * @param param 客户端参数
     * @param skuList 当页的sku列表
     * @return 获取库存隔离明细列表
     */
    public List<Map<String,Object>> getCommonStockList(Map param, List<Object>skuList){
        // 库存隔离明细列表
        List<Map<String,Object>> stockList = new ArrayList<Map<String,Object>>();
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        List<Map<String,Object>> platformList = (List<Map<String,Object>>) param.get("platformList");
        sqlParam.put("taskId", param.get("taskId"));
        sqlParam.put("skuList", skuList);
        sqlParam.put("tableName", param.get("tableName"));
        sqlParam.put("lang", param.get("lang"));
        // 获取当页表示的库存隔离数据
        List<Map<String,Object>> stockAllList = cmsBtStockSeparateItemDao.selectStockSeparateItem(sqlParam);
        // sku临时变量
        String skuTemp = "";
        Map<String, Object> lineInfo = null;
        List<Map<String, Object>> linePlatformInfoList = null;
        // 平台列表的索引
        int  platformIndex = 0;
        for (Map<String,Object> stockInfo : stockAllList) {
            String model = (String) stockInfo.get("product_model");
            String code = (String) stockInfo.get("product_code");
            String currentSku = (String) stockInfo.get("sku");  //当前行的sku
            String cartId = String.valueOf(stockInfo.get("cart_id"));
            String property1 = (String) stockInfo.get("property1");
            String property2 = (String) stockInfo.get("property2");
            String property3 = (String) stockInfo.get("property3");
            String property4 = (String) stockInfo.get("property4");
            String qty = String.valueOf(stockInfo.get("qty"));
            String separateQty = String.valueOf(stockInfo.get("separate_qty"));
            String statusName = (String) stockInfo.get("status_name");
            // 加入一条新的库存隔离明细
            if (!currentSku.equals(skuTemp)) {
                // 加入隔离平台为"动态"的信息
                // 例：{"cartId":"20", "qty":"-1", "status":""},{"cartId":"23", "qty":"-1", "status":""}
                if (platformIndex > 0) {
                    for (int i = platformIndex; i < platformList.size(); i++) {
                        Map<String, Object> linePlatformInfo = new HashMap<String, Object>();
                        linePlatformInfo.put("cartId", (String) ((HashMap<String, Object>) platformList.get(platformIndex)).get("cartId"));
                        linePlatformInfo.put("qty", "-1");
                        linePlatformInfo.put("status", "");
                        linePlatformInfoList.add(linePlatformInfo);
                        platformIndex++;
                    }
                }
                skuTemp = currentSku;
                platformIndex = 0;
                lineInfo = new HashMap<String, Object>();
                linePlatformInfoList = new ArrayList<Map<String, Object>>();
                stockList.add(lineInfo);
                lineInfo.put("model", model);
                lineInfo.put("code", code);
                lineInfo.put("sku", currentSku);
                lineInfo.put("property1", property1);
                lineInfo.put("property2", property2);
                lineInfo.put("property3", property3);
                lineInfo.put("property4", property4);
                lineInfo.put("qty", qty);
                lineInfo.put("platformStock", linePlatformInfoList);
            }
            // 加入隔离平台为"动态"的信息
            // 例：{"cartId":"20", "qty":"-1", "status":""},{"cartId":"23", "qty":"-1", "status":""}
            while (!((HashMap<String, Object>) platformList.get(platformIndex)).get("cartId").equals(cartId)) {
                Map<String, Object> linePlatformInfo = new HashMap<String, Object>();
                linePlatformInfo.put("cartId", (String)((HashMap<String, Object>) platformList.get(platformIndex)).get("cartId"));
                linePlatformInfo.put("qty", "-1");
                linePlatformInfo.put("status", "");
                linePlatformInfoList.add(linePlatformInfo);
                platformIndex++;
            }
            // 加入平台库存隔离信息
            // 例：{"cartId":"27", "qty":"40", "status":"等待隔离"}
            if (((HashMap<String, Object>) platformList.get(platformIndex)).get("cartId").equals(cartId)) {
                Map<String, Object> linePlatformInfo = new HashMap<String, Object>();
                linePlatformInfo.put("cartId", cartId);
                linePlatformInfo.put("qty", separateQty);
                linePlatformInfo.put("status", statusName);
                linePlatformInfoList.add(linePlatformInfo);
            }
            platformIndex++;
        }

        return stockList;
    }

    /**
     * 获取一页表示的Sku
     *
     * @param param 客户端参数
     * @return 一页表示的Sku
     */
    public List<Object> getCommonStockPageSkuList(Map param) {
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("sql", getCommonStockPageSkuSql(param));
        List<Object> skuList = cmsBtStockSeparateItemDao.selectStockSeparateItemBySqlObject(sqlParam);
        return  skuList;
    }

    /**
     * 库存隔离数据是否移到history表
     *
     *
     * @param param 客户端参数
     * @return 获取取得属性列表
     */
    public boolean isHistoryExist(Map param){
        return (cmsBtStockSeparateItemDao.selectStockSeparateItemHistoryCnt(param) !=  0) ? true : false;
    }

    /**
     * 实时库存状态
     *
     * @param param 客户端参数
     * @param skuList 当页的sku列表
     * @return 实时库存状态列表
     */
    public List<Map<String,Object>> getRealStockList(Map param, List<Object>skuList){
        List<Map<String,Object>> realStockList = new ArrayList<Map<String,Object>>();

        for (Object sku:skuList) {
            // 取得某个sku的逻辑库存
            int logicStock = getLogicStock((String) sku);

            // 取得所有平台的隔离库存
            Map<String,Object> sqlParam = new HashMap<String,Object>();
            sqlParam.put("sku", sku);
            sqlParam.put("status", "2"); //隔离成功
            int stockSeparateSuccessAll = cmsBtStockSeparateItemDao.selectStockSeparateSuccessQty(sqlParam);

            // 取得所有平台的增量隔离库存
            int stockSeparateIncrementSuccessAll = cmsBtStockSeparateIncrementItemDao.selectStockSeparateIncrementSuccessQty(sqlParam);

            // 取得隔离期间各个隔离平台的销售数量

        }
        return realStockList;
    }

    /**
     * 取得某个sku的逻辑库存
     *
     * @param sku sku
     * @return 某个sku的逻辑库存
     */
    public int getLogicStock(String sku){
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("sku", sku);
        int logicStock = wmsBtLogicInventoryDao.selectLogicInventoryCnt(sqlParam);
        return  logicStock;
    }




    /**
     * 取得一页表示的Sku的Sql
     *
     * @param param 客户端参数
     * @return 一页表示的Sku的Sql
     */
    private String getCommonStockPageSkuSql(Map param){
        String sql = "select sku from ( select distinct sku as sku from " + (String) param.get("tableName");
        sql += getWhereSql(param, true) + " order by product_code,sku) t1 ";
        String start = String.valueOf(param.get("start1"));
        String length = String.valueOf(param.get("length1"));
        sql += " limit " + start + "," + length;
        return sql;
    }

    /**
     * 取得各种状态数量的Sql
     *
     * @param param 客户端参数
     * @return 各种状态数量的Sql
     */
    private String getStockStatusCountSql(Map param){
//        String sql = "select count(*) as count from " + (String) param.get("tableName");
        String sql = "select status,count(*) as count from " + (String) param.get("tableName");
        sql += getWhereSql(param, false);
        sql += " group by status";
        return sql;
    }

    /**
     * 取得Sku的数量
     *
     * @param param 客户端参数
     * @return 一页表示的Sku的Sql
     */
    private String getStockSkuCountSql(Map param){
        String sql = "select count(distinct sku) as count from " + (String) param.get("tableName");
        sql += getWhereSql(param, true);
        return sql;
    }

//    /**
//     * 取得获取库存隔离明细Sql
//     *
//     * @param param
//     * @return 获取库存隔离明细Sql
//     */
//    private String getCommonStockSql(Map param){
//        String sql = "";
//        sql = "SELECT product_code,sku,cart_id,property1,property2,property3,property4,qty,separate_qty,status from cms_bt_stock_separate_item ";
//        return sql;
//    }

    /**
     * 取得where条件的Sql文
     *
     * @param param
     * @param statusFlg 状态Flg
     * @return where条件的Sql文
     */
    private String getWhereSql(Map param, boolean statusFlg){
        String whereSql = " where 1=1 ";
        // 任务Id
        whereSql += " and task_id = " + String.valueOf(param.get("taskId")) + " ";

        // 商品code
        if (!StringUtils.isEmpty((String) param.get("code"))) {
            whereSql += " and product_code = '" + escapeSpecialChar((String) param.get("code")) + "'";
        }
        // sku
        if (!StringUtils.isEmpty((String) param.get("sku"))) {
            whereSql += " and sku = '" + escapeSpecialChar((String) param.get("sku")) + "'";
        }
        // 可用库存（下限）
        if (!StringUtils.isEmpty((String) param.get("qtyFrom"))) {
            whereSql += " and qty >= " + escapeSpecialChar((String) param.get("qtyFrom"));
        }
        // 可用库存（上限）
        if (!StringUtils.isEmpty((String) param.get("qtyTo"))) {
            whereSql += " and qty <= " + escapeSpecialChar((String) param.get("qtyTo"));
        }
        //状态
        if (statusFlg && !StringUtils.isEmpty((String) param.get("status"))) {
            whereSql += " and status = '" + (String) param.get("status") + "'";
        }

        for (Map<String,Object> property : (List<Map<String,Object>>)param.get("propertyList")) {
            // 动态属性名
            String propertyName = (String) property.get("property");
            // 动态属性值
            String propertyValue = (String) property.get("value");
            // 逻辑，支持：Like，默认为空白
            String logic = (String) property.get("logic");
            // 类型，支持：int，默认为空白
            String type = (String) property.get("type");
            if (StringUtils.isEmpty(propertyValue)) {
                continue;
            }
            // 存在~则加上大于等于和小于等于的条件
            if (propertyValue.contains("~")) {
                String values[] = propertyValue.split("~");
                for (int i = 0;i <  2; i++) {
                    if ("int".equals(type)) {
                        propertyName = "convert(" + propertyName + ",signed)";
                    }
                    if (i == 0) {
                        if (!StringUtils.isEmpty(values[i])) {
                            whereSql += " and " + propertyName + " >= '" + escapeSpecialChar(values[i]) + "' ";
                        }
                    } else {
                        if (!StringUtils.isEmpty(values[i])) {
                            whereSql += " and " + propertyName + " <= '" + escapeSpecialChar(values[i]) + "' ";
                        }
                    }
                }
                continue;
            }

            // 按逗号分割则生成多个用 or 分割的条件
            String values[] = propertyValue.split(",");
            String propertySql = "";
            for (String value : values) {
                propertySql += propertyName;
                if (logic.toLowerCase().equals("like")) {
                    propertySql += " like '%" + escapeSpecialChar(value) + "%' or ";
                } else {
                    propertySql += " = '" + escapeSpecialChar(value) + "' or ";
                }
            }
            if(values != null && values.length > 0 ) {
                whereSql += " and ( " + propertySql.substring(0,propertySql.length()-3) + " ) ";
            }
        }

        return whereSql;
    }

    /**
     * Sql转义字符转换
     *
     * @param value 转义前字符串
     * @return 转义后字符串
     */
    private String escapeSpecialChar(String value){
        return value.replaceAll("\\\\", "\\\\\\\\").replaceAll("'","\\\\'").replaceAll("\"","\\\\\"")
                .replaceAll("%","\\\\%").replaceAll("_","\\\\_");
    }

//    /**
//     * 状态文字转换
//     *
//     * @param status 转义前状态
//     * @return 转换后的状态文字
//     */
//    private String getStatus(String status) {
//        String changedStatus = "";
//        if ("0".equals(status)) {
//            changedStatus = "未进行";
//        } else if ("1".equals(status)) {
//            changedStatus = "等待隔离";
//        } else if ("2".equals(status)) {
//            changedStatus = "隔离成功";
//        } else if ("3".equals(status)) {
//            changedStatus = "隔离失败";
//        } else if ("4".equals(status)) {
//            changedStatus = "等待还原";
//        } else if ("5".equals(status)) {
//            changedStatus = "还原成功";
//        } else if ("6".equals(status)) {
//            changedStatus = "还原失败";
//        } else if ("7".equals(status)) {
//            changedStatus = "再修正";
//        }
//
//        return changedStatus;
//    }

}
