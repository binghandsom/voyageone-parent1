package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.cms.bean.promotion.task.StockIncrementExcelBean;
import com.voyageone.web2.cms.dao.CmsBtStockSeparateIncrementItemDao;
import com.voyageone.web2.cms.dao.CmsBtStockSeparateIncrementTaskDao;
import com.voyageone.web2.cms.dao.CmsBtStockSeparatePlatformInfoDao;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by morse.lu on 2016/3/23.
 */
@Service
public class CmsTaskStockIncrementDetailService extends BaseAppService {

    @Autowired
    private CmsBtStockSeparateIncrementItemDao cmsBtStockSeparateIncrementItemDao;

    @Autowired
    private CmsBtStockSeparateIncrementTaskDao cmsBtStockSeparateIncrementTaskDao;

    @Autowired
    private CmsBtStockSeparatePlatformInfoDao cmsBtStockSeparatePlatformInfoDao;

    @Autowired
    private CmsTaskStockService cmsTaskStockService;

    /** 0：按动态值进行增量隔离 */
    private static final String TYPE_DYNAMIC = "0";
    /** 1：按固定值进行增量隔离 */
    private static final String TYPE_FIX_VALUE = "1";

    /**
     * 按固定值进行增量隔离
     */
    private static final String FIXED = "1";

    /**
     * Excel的Title部可调配库存显示文字
     */
    private static final String USABLESTOCK = "可调配库存";
    /**
     * Excel的Title部固定值增量显示文字
     */
    private static final String FIXED_TEXT = "固定值增量";
    /**
     * Excel固定值更新时显示文字
     */
    private static final String YES = "yes";

    /**
     * 增量库存隔离数据是否移到history表
     *
     * @param subTaskId 任务id
     * @return 增量库存隔离数据是否移到history表
     */
    public boolean isHistoryExist(String subTaskId) {
        return (cmsBtStockSeparateIncrementItemDao.selectStockSeparateIncrementItemHistoryCnt(new HashMap<String, Object>() {{this.put("subTaskId", subTaskId);}}) != 0) ? true : false;
    }

    /**
     * 根据子任务id取得任务信息
     *
     * @param subTaskId 子任务id
     * @param channelId 渠道id
     * @param lang 语言
     *
     * @return 任务信息
     */
    public Map<String, Object> getTaskInfo(String subTaskId, String channelId, String lang){
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("subTaskId", subTaskId);
        sqlParam.put("channelId", channelId);
        sqlParam.put("lang", lang);
        List<Map<String, Object>> stockSeparateIncrementTask = cmsBtStockSeparateIncrementTaskDao.selectStockSeparateIncrementTask(sqlParam);
        if (stockSeparateIncrementTask == null || stockSeparateIncrementTask.size() == 0) {
            return null;
        } else {
            return stockSeparateIncrementTask.get(0);
        }
    }

    /**
     * 任务id/渠道id权限check
     *
     * @param taskId 任务id
     * @param cartId 平台id
     * @param channelId 渠道id
     * @param lang 语言
     * @return 子任务id/渠道id权限check结果（false:没有权限,true:有权限）
     */
    public boolean hasAuthority(String taskId, String cartId, String channelId, String lang){

        // 根据任务id/平台id取得渠道信息
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("taskId", taskId);
        sqlParam.put("cartId", cartId);
        sqlParam.put("channelId", channelId);
        sqlParam.put("lang", lang);
        List<Map<String, Object>> stockSeparatePlatform = cmsBtStockSeparatePlatformInfoDao.selectStockSeparatePlatform(sqlParam);
        // 没有渠道数据的情况下，一般情况下不可能
        if (stockSeparatePlatform == null || stockSeparatePlatform.size() == 0) {
            return false;
        }
        // 子任务对应的渠道和当前渠道不一致的情况，视为没有权限
        if (!channelId.equals(stockSeparatePlatform.get(0).get("channel_id"))) {
            return false;
        }

        return true;
    }


    /**
     * 取得增量库存隔离数据各种状态的数量
     *
     * @param param 客户端参数
     * @return 某种状态的数量
     */
    public List<Map<String,Object>>  getStockStatusCount(Map param){
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        // 各种状态统计数量的Sql
        sqlParam.put("sql", getStockStatusCountSql(param));
        List<Map<String,Object>> statusCountList = cmsBtStockSeparateIncrementItemDao.selectStockSeparateItemBySqlMap(sqlParam);
        return statusCountList;
    }

    /**
     * 取得增量库存隔离明细列表
     * 例：
     *          {"model":"35265", "code":"35265465", "sku":"256354566-9", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":"50", "incrementQty":"50", "status":"未进行", "fixFlg":false},
     *          {"model":"35265", "code":"35265465", "sku":"256354566-10", "property1":"Puma", "property2":"Puma Suede Classic +Puma Suede Classic+", "property3":"women", "property4":"10", "qty":"80", "incrementQty":"80", "status":"未进行", "fixFlg":false},
     *          {"model":"35265", "code":"35265465", "sku":"256354566-11", "property1":"Puma", "property2":"Puma Suede Classic+", "property3":"women", "property4":"10", "qty":"20", "incrementQty":"20", "status":"增量成功", "fixFlg":false},
     *                    ...
     *
     *
     * @param param 客户端参数
     * @return 增量库存隔离明细列表
     */
    public List<Map<String,Object>> getStockList(Map param){
        // 增量库存隔离明细列表
        List<Map<String,Object>> stockList = new ArrayList<Map<String,Object>>();

        // 获取当页表示的库存隔离数据
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        // 库存隔离明细一页表示的Sku的Sql
        sqlParam.put("sql", getStockPageSkuSql(param));
        List<Map<String,Object>> stockAllList = cmsBtStockSeparateIncrementItemDao.selectStockSeparateItemBySqlMap(sqlParam);

        for (Map<String,Object> stockInfo : stockAllList) {
            String model = (String) stockInfo.get("product_model");
            String code = (String) stockInfo.get("product_code");
            String sku = (String) stockInfo.get("sku");
            String property1 = (String) stockInfo.get("property1");
            String property2 = (String) stockInfo.get("property2");
            String property3 = (String) stockInfo.get("property3");
            String property4 = (String) stockInfo.get("property4");
            String qty = String.valueOf(stockInfo.get("qty"));
            String incrementQty = String.valueOf(stockInfo.get("increment_qty"));
            String fixFlg = String.valueOf(stockInfo.get("fix_flg"));
            String statusName = String.valueOf(stockInfo.get("status_name"));
            // 画面上一行的数据
            Map<String, Object> lineInfo = new HashMap<String, Object>();
            stockList.add(lineInfo);
            lineInfo.put("model", model);
            lineInfo.put("code", code);
            lineInfo.put("sku", sku);
            lineInfo.put("property1", property1);
            lineInfo.put("property2", property2);
            lineInfo.put("property3", property3);
            lineInfo.put("property4", property4);
            lineInfo.put("qty", qty);
            lineInfo.put("incrementQty", incrementQty);
            if (TYPE_FIX_VALUE.equals(fixFlg)) {
                lineInfo.put("fixFlg", true);
            } else {
                lineInfo.put("fixFlg", false);
            }
            lineInfo.put("status", statusName);
        }
        return stockList;
    }


    /**
     * 取得各种状态统计数量的Sql
     *
     * @param param 客户端参数
     * @return 各种状态统计数量的Sql
     */
    private String getStockStatusCountSql(Map param){
        String sql = "select status,count(*) as count from voyageone_cms2.cms_bt_stock_separate_increment_item" + (String) param.get("tableNameSuffix");
        sql += cmsTaskStockService.getWhereSql(param, false);
        sql += " group by status";
        return sql;
    }

    /**
     * 取得增量库存隔离明细一页表示的Sku的Sql
     *
     * @param param 客户端参数
     * @return 库存增量隔离明细一页表示的Sku的Sql
     */
    private String getStockPageSkuSql(Map param){
        List<Map<String,Object>> platformList = (List<Map<String,Object>>) param.get("platformList");
        String sql = "select t1.product_model, t1.product_code, t1.sku, t1.property1, t1.property2, t1.property3, t1.property4, ";
        sql += " t1.qty, t1.increment_qty,t2.name as status_name from";
        sql += " (select * from voyageone_cms2.cms_bt_stock_separate_increment_item" + (String) param.get("tableNameSuffix") ;
        sql += cmsTaskStockService.getWhereSql(param, true);
        sql += " order by sku";
        String start = String.valueOf(param.get("start"));
        String length = String.valueOf(param.get("length"));
        sql += " limit " + start + "," + length + ") t1 ";
        sql +=" left join (select value,name from com_mt_value where type_id= '63' and lang_id = '" + param.get("lang") + "') t2 on t1.status = t2.value";
        return sql;
    }

//    /**
//     * 取得where条件的Sql文
//     *
//     * @param param
//     * @return where条件的Sql文
//     */
//    private String getWhereSql(Map param) {
//        String whereSql = " where ";
//        // 任务Id
//        whereSql += " sub_task_id = " + String.valueOf(param.get("taskId")) + " ";
//
//        // 商品model
//        if (!StringUtils.isEmpty((String) param.get("model"))) {
//            whereSql += " and product_model = '" + escapeSpecialChar((String) param.get("model")) + "'";
//        }
//
//        // 商品code
//        if (!StringUtils.isEmpty((String) param.get("code"))) {
//            whereSql += " and product_code = '" + escapeSpecialChar((String) param.get("code")) + "'";
//        }
//        // sku
//        if (!StringUtils.isEmpty((String) param.get("sku"))) {
//            whereSql += " and sku = '" + escapeSpecialChar((String) param.get("sku")) + "'";
//        }
//
//        //状态
//        if (!StringUtils.isEmpty((String) param.get("status"))) {
//            whereSql += " and status = '" + (String) param.get("status") + "'";
//        }
//
//        for (Map<String, Object> property : (List<Map<String, Object>>) param.get("propertyList")) {
//            // 动态属性名
//            String propertyName = (String) property.get("property");
//            // 动态属性值
//            String propertyValue = (String) property.get("value");
//            // 逻辑，支持：Like，默认为空白
//            String logic = (String) property.get("logic");
//            // 类型，支持：int，默认为空白
//            String type = (String) property.get("type");
//            if (StringUtils.isEmpty(propertyValue)) {
//                continue;
//            }
//            // 存在~则加上大于等于和小于等于的条件(如果是数值型则转换为数值做比较)
//            if (propertyValue.contains("~")) {
//                String values[] = propertyValue.split("~");
//                if ("int".equals(type)) {
//                    // propertyName = "convert(" + propertyName + ",signed)";
//                    propertyName = propertyName + "*1";
//                }
//                int end = 2;
//                if (values.length < 2) {
//                    end = values.length;
//                }
//                for (int i = 0; i < end; i++) {
//                    if (i == 0) {
//                        if (!StringUtils.isEmpty(values[i])) {
//                            whereSql += " and " + propertyName + " >= '" + escapeSpecialChar(values[i]) + "' ";
//                        }
//                    } else {
//                        if (!StringUtils.isEmpty(values[i])) {
//                            whereSql += " and " + propertyName + " <= '" + escapeSpecialChar(values[i]) + "' ";
//                        }
//                    }
//                }
//                continue;
//            }
//
//            // 按逗号分割则生成多个用 or 分割的条件
//            String values[] = propertyValue.split(",");
//            String propertySql = "";
//            for (String value : values) {
//                propertySql += propertyName;
//                if (logic.toLowerCase().equals("like")) {
//                    propertySql += " like '%" + escapeSpecialChar(value) + "%' or ";
//                } else {
//                    propertySql += " = '" + escapeSpecialChar(value) + "' or ";
//                }
//            }
//            if (values != null && values.length > 0) {
//                whereSql += " and ( " + propertySql.substring(0, propertySql.length() - 3) + " ) ";
//            }
//        }
//
//        return whereSql;
//    }

    /**
     * Sql转义字符转换
     *
     * @param value 转义前字符串
     * @return 转义后字符串
     */
    private String escapeSpecialChar(String value) {
        return value.replaceAll("\\\\", "\\\\\\\\").replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\"")
                .replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");
    }

    /**
     * 某个任务对应的Promotion是否在进行中
     *
     * @param taskId 任务id
     * @param cartId 平台id
     * @return 是否在进行中
     */
    private boolean isPromotionDuring(String taskId, String cartId){

        // 取得任务下的平台平台信息
        Date now = DateTimeUtil.parse(DateTimeUtil.getNow());
        Map<String,Object> sqlParam = new HashMap<String,Object>();
        sqlParam.put("taskId", taskId);
        sqlParam.put("cartId", cartId);
        List<Map<String, Object>> platformInfoList = cmsBtStockSeparatePlatformInfoDao.selectStockSeparatePlatform(sqlParam);
        if (platformInfoList != null && platformInfoList.size() > 0) {
            Map<String, Object> platformInfo = platformInfoList.get(0);
            // Promotion开始时间
            String activityStart = (String) platformInfo.get("activity_start");
            if (!StringUtils.isEmpty(activityStart)) {
                if (activityStart.length() == 10) {
                    activityStart = activityStart + " 00:00:00";
                }
                if (DateTimeUtil.parse(activityStart).compareTo(now) <= 0) {
                    String activityEnd = (String) platformInfo.get("activity_end");
                    if (!StringUtils.isEmpty(activityEnd)) {
                        if (activityEnd.length() == 10) {
                            activityEnd = activityEnd + " 00:00:00";
                        }
                        if (DateTimeUtil.addDays(DateTimeUtil.parse(activityEnd), 1).compareTo(now) > 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 库存隔离Excel文档做成，数据流返回
     *
     * @param param 客户端参数
     * @return byte[] 数据流
     * @throws IOException
     * @throws InvalidFormatException
     */
    public byte[] getExcelFileStockIncrementInfo(Map param) throws IOException, InvalidFormatException {
        String templatePath = Properties.readValue(CmsConstants.Props.STOCK_EXPORT_TEMPLATE);

        param.put("whereSql", cmsTaskStockService.getWhereSql(param, true));
        List<StockIncrementExcelBean> resultData = cmsBtStockSeparateIncrementItemDao.selectExcelStockIncrementInfo(param);

        $info("准备打开文档 [ %s ]", templatePath);

        try (InputStream inputStream = new FileInputStream(templatePath);
             SXSSFWorkbook book = new SXSSFWorkbook(new XSSFWorkbook(inputStream))) {
            // Titel行
            writeExcelStockIncrementInfoHead(book.getXSSFWorkbook(), param);
            // 数据行
            writeExcelStockIncrementInfoRecord(book.getXSSFWorkbook(), param, resultData);

            // 自适应列宽
            List<Map> propertyList = (List<Map>) param.get("propertyList");
            int cntCol = 3 + propertyList.size() + 3;
            for (int i = 0; i < cntCol; i++) {
                book.getXSSFWorkbook().getSheetAt(0).autoSizeColumn(i);
            }

            // 格式copy用sheet删除
            book.getXSSFWorkbook().removeSheetAt(1);

            // 返回值设定
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                book.write(outputStream);

                $info("已写入输出流");

                return outputStream.toByteArray();
            }
        }
    }

    /**
     * 增量库存隔离Excel的第一行Title部写入
     */
    private void writeExcelStockIncrementInfoHead(Workbook book, Map param) {

        Sheet sheet = book.getSheetAt(0);
        Drawing drawing = sheet.createDrawingPatriarch();
        CreationHelper helper = book.getCreationHelper();

        Row row = FileUtils.row(sheet, 0);
        CellStyle cellStyleProperty = book.getSheetAt(1).getRow(0).getCell(4).getCellStyle(); // 属性的cellStyle

        List<Map> propertyList = (List<Map>) param.get("propertyList");
        String cartId = (String) param.get("cartId");
        String cartName = (String) param.get("cartName");

        // 内容输出
        int index = 3;

        // 属性
        for (Map property : propertyList) {
            FileUtils.cell(row, index++, cellStyleProperty).setCellValue((String) property.get("name"));

            Comment comment = drawing.createCellComment(helper.createClientAnchor());
            comment.setString(helper.createRichTextString((String) property.get("property")));
            row.getCell(index - 1).setCellComment(comment);
        }

        // 可调配库存
        FileUtils.cell(row, index++, cellStyleProperty).setCellValue(USABLESTOCK);

        // 平台
        CellStyle cellStylePlatform = book.getSheetAt(1).getRow(0).getCell(0).getCellStyle(); // 平台的cellStyle
        FileUtils.cell(row, index++, cellStylePlatform).setCellValue(cartName);
        Comment comment = drawing.createCellComment(helper.createClientAnchor());
        comment.setString(helper.createRichTextString(cartId));
        row.getCell(index - 1).setCellComment(comment);

        // 固定值增量
        FileUtils.cell(row, index++, cellStylePlatform).setCellValue(FIXED_TEXT);

        // 筛选
        CellRangeAddress filter = new CellRangeAddress(0, 0, 0, index - 1);
        sheet.setAutoFilter(filter);
    }

    /**
     * 增量库存隔离Excel的数据写入
     */
    private void writeExcelStockIncrementInfoRecord(Workbook book, Map param, List<StockIncrementExcelBean> resultData) {

        Sheet sheet = book.getSheetAt(0);
        int lineIndex = 1; // 行号
        int colIndex = 0; // 列号
        Row row;

        CellStyle cellStyleDataLock = book.getSheetAt(1).getRow(0).getCell(2).getCellStyle(); // 数据（锁定）的cellStyle
        CellStyle cellStyleData = book.getSheetAt(1).getRow(0).getCell(3).getCellStyle(); // 数据（不锁定）的cellStyle

        List<Map> propertyList = (List<Map>) param.get("propertyList");

        for (StockIncrementExcelBean rowData : resultData) {
            row = FileUtils.row(sheet, lineIndex++);
            colIndex = 0;

            FileUtils.cell(row, colIndex++, cellStyleDataLock).setCellValue(rowData.getProduct_model()); // Model
            FileUtils.cell(row, colIndex++, cellStyleDataLock).setCellValue(rowData.getProduct_code()); // Code
            FileUtils.cell(row, colIndex++, cellStyleDataLock).setCellValue(rowData.getSku()); // Sku

            // 属性
            for (Map property : propertyList) {
                FileUtils.cell(row, colIndex++, cellStyleDataLock).setCellValue(rowData.getProperty((String) property.get("property")));
            }

            // 可调配库存
            FileUtils.cell(row, colIndex++, cellStyleDataLock).setCellValue(rowData.getQty().toPlainString());
            // 平台
            FileUtils.cell(row, colIndex++, cellStyleData).setCellValue(rowData.getIncrement_qty().toPlainString());

            // 固定值增量
            if (FIXED.equals(rowData.getFix_flg())) {
                // 按固定值进行增量隔离
                FileUtils.cell(row, colIndex++, cellStyleData).setCellValue(YES);
            }
        }
    }

    /**
     * excel 导入
     *
     * @param param      客户端参数
     * @param file       导入文件
     * @param resultBean 返回内容
     */
    public void importExcelFileStockIncrementInfo(Map param, MultipartFile file, Map<String, Object> resultBean) {
        String taskId = (String) param.get("task_id");
        String subTaskId = (String) param.get("subTaskId");
        // 取得任务id对应的Promotion是否未开始或者已经结束
        boolean promotionDuringFlg = isPromotionDuring((String) param.get("taskId"), (String) param.get("cartId"));
        if (!promotionDuringFlg) {
            throw new BusinessException("活动未开始或者已经结束，不能修改数据！");
        }

        if (cmsBtStockSeparateIncrementItemDao.selectStockSeparateIncrementItemStatusCnt(new HashMap<String, Object>(){{this.put("subTaskId", subTaskId);}}) > 0 ) {
            // 如果在cms_bt_stock_separate_increment_item表中，这个增量任务有状态<>0:未进行的数据，则不允许导入
            throw new BusinessException("此增量任务已经进行，不能修改数据！");
        }



    }

}
