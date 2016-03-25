package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.service.dao.cms.CmsBtStockSeparateItemDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparatePlatformInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeff.duan on 2016/03/04.
 */
@Service
public class CmsTaskStockService extends BaseAppService {

    @Autowired
    private CmsBtStockSeparatePlatformInfoDao cmsBtStockSeparatePlatformInfoDao;

    @Autowired
    private CmsBtStockSeparateItemDao cmsBtStockSeparateItemDao;

    /**
     * 取得属性列表
     * 属性列表数据结构
     *    {"value":"property1", "name":"品牌", "logic":"", "show":false},
     *    {"value":"property2", "name":"英文短描述", "logic":"Like", "show":false},
     *    {"value":"property3", "name":"性别", "logic":"", "show":false}，
     *    {"value":"property4", "name":"Size", "logic":"", "show":false}...
     *
     * @param param 客户端参数
     * @return 获取取得属性列表
     */
    public List<Map<String,Object>> getPropertyList(Map param){
        List<Map<String,Object>> propertyList = new ArrayList<Map<String,Object>>();

        List<TypeChannelBean> dynamicPropertyList = TypeChannels.getTypeList("dynamicProperty", (String) param.get("channelId"));
        for (TypeChannelBean dynamicProperty : dynamicPropertyList) {
            Map<String,Object> propertyItem = new HashMap<String,Object>();
            propertyItem.put("value", dynamicProperty.getValue());
            propertyItem.put("name", dynamicProperty.getName());
            propertyItem.put("logic", dynamicProperty.getAdd_name1());
            propertyItem.put("show", false);
            propertyList.add(propertyItem);
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
     * @param platformList 任务对应平台信息列表
     * @param skuList 当页的sku列表
     * @return 获取库存隔离明细列表
     */
    public List<Map<String,Object>> getCommonStockList(Map param, List<Map<String, Object>> platformList, List<String>skuList){
        // 库存隔离明细列表
        List<Map<String,Object>> stockList = new ArrayList<Map<String,Object>>();

        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("taskId", param.get("taskId"));
        sqlParam.put("skuList", skuList);
        // 获取当页表示的库存隔离数据
        List<Map<String,Object>> stockAllList = cmsBtStockSeparateItemDao.selectStockSeparateItem(sqlParam);
        // sku临时变量
        String skuTemp = "";
        Map<String, Object> lineInfo = null;
        List<Map<String, Object>> linePlatformInfoList = new ArrayList<>();
        // 平台列表的索引
        int  platformIndex = 0;
        for (Map<String,Object> stockInfo : stockAllList) {
            String model = (String) stockInfo.get("product_model");
            String code = (String) stockInfo.get("product_code");
            String currentSku = (String) stockInfo.get("sku");  //当前行的sku
            String cartId = (String) stockInfo.get("cart_id");
            String property1 = (String) stockInfo.get("property1");
            String property2 = (String) stockInfo.get("property2");
            String property3 = (String) stockInfo.get("property3");
            String property4 = (String) stockInfo.get("property4");
            String qty = String.valueOf(stockInfo.get("qty"));
            String separateQty = String.valueOf(stockInfo.get("separate_qty"));
            String statusName = (String) stockInfo.get("status_name");
            // 加入一条新的库存隔离明细
            if (!currentSku.equals(skuTemp)) {
                skuTemp = currentSku;
                platformIndex = 0;
                lineInfo = new HashMap<>();
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
     * 实时库存状态
     *
     * @param param 客户端参数
     * @param platformList 任务对应平台信息列表
     * @param skuList 当页的sku列表
     * @return 实时库存状态列表
     */
    public List<Map<String,Object>> getRealStockList(Map param, List<Map<String, Object>> platformList, List<String>skuList){
        List<Map<String,Object>> realStockList = new ArrayList<>();
        return realStockList;
    }


    /**
     * 获取一页表示的Sku
     *
     * @param param 客户端参数
     * @return 一页表示的Sku
     */
    public List<String> getCommonStockPageSkuList(Map param) {
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("sql", getCommonStockPageSkuSql(param));
        return cmsBtStockSeparateItemDao.selectStockSeparateItemPageSku(sqlParam);
    }

    /**
     * 库存隔离数据是否移到history表
     *
     *
     * @param param 客户端参数
     * @return 获取取得属性列表
     */
    public boolean isHistoryExist(Map param){
        return (cmsBtStockSeparateItemDao.selectStockSeparateItemHistoryCnt(param) != 0);
    }

    /**
     * 取得一页表示的Sku的Sql
     *
     * @param param 客户端参数
     * @return 一页表示的Sku的Sql
     */
    private String getCommonStockPageSkuSql(Map param){
        String sql = "";
        sql = "select sku from ( select distinct sku as sku from " + (String) param.get("tableName");
        sql += getWhereSql(param) + " )t1 ";
        String start = "";
        String length = "";
        if (StringUtils.isEmpty((String) param.get("start1"))) {
            start = "0";
        }
        if (StringUtils.isEmpty((String) param.get("length1"))) {
            length = "10";
        }

        sql += " limit " + start + "," + length;
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
     * @param param Map
     * @return where条件的Sql文
     */
    private String getWhereSql(Map param){
        String whereSql = " where 1=1";
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
            whereSql += " and qtyFrom >= " + escapeSpecialChar((String) param.get("qtyFrom"));
        }
        // 可用库存（上限）
        if (!StringUtils.isEmpty((String) param.get("qtTo"))) {
            whereSql += " and qtTo <= " + escapeSpecialChar((String) param.get("qtTo"));
        }
        //状态
        if (!StringUtils.isEmpty((String) param.get("status"))) {
            whereSql += " and status = '" + (String) param.get("status") + "'";
        }

        if (param.get("propertyList") == null) {
            for (Map<String,Object> property : (List<Map<String,Object>>)param.get("propertyList")) {
                // 动态属性名
                String propertyName = (String) property.get("value");
                // 动态属性值
                String propertyValue = (String) param.get(propertyName);
                // 逻辑，例如：Like，默认为=
                String logic = (String) property.get("logic");
//                if (StringUtils.isEmpty(logic)) {
//                    logic = "=";
//                }
                // 存在~则加上大于等于和小于等于的条件
                if (propertyValue.contains("~")) {
                    String values[] = propertyValue.split("~");
                    for (int i = 0;i <  2; i++) {
                        if (i == 0) {
                            whereSql += " and" + propertyName + " >= " + values[i];
                        } else {
                            whereSql += " and" + propertyName + " <= " + values[i];
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
                if(values.length > 0) {
                    whereSql += " and " + propertySql.substring(0,propertySql.length()-3);
                }
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
