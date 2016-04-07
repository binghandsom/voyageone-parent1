package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.task.stock.StockIncrementExcelBean;
import com.voyageone.service.dao.cms.CmsBtStockSeparateIncrementItemDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparatePlatformInfoDao;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.service.dao.cms.CmsBtStockSeparateIncrementTaskDao;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.SheetUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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

    @Autowired
    private SimpleTransaction simpleTransaction;

    @Autowired
    private TransactionRunner transactionRunner;

    /** Excel重置方式导入 */
    private static final String EXCEL_IMPORT_DELETE_UPDATE = "3";
    /** Excel变更方式导入 */
    private static final String EXCEL_IMPORT_UPDATE = "2";

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

//    /**
//     * 增量任务/渠道id权限check
//     *
//     * @param taskId 任务id
//     * @param cartId 平台id
//     * @param channelId 渠道id
//     * @param lang 语言
//     * @return 增量任务/渠道id权限check结果（false:没有权限,true:有权限）
//     */
//    public boolean hasAuthority(String taskId, String cartId, String channelId, String lang){
//
//        // 根据任务id/平台id取得渠道信息
//        Map<String,Object> sqlParam = new HashMap<String,Object>();
//        sqlParam.put("taskId", taskId);
//        sqlParam.put("cartId", cartId);
//        sqlParam.put("channelId", channelId);
//        sqlParam.put("lang", lang);
//        List<Map<String, Object>> stockSeparatePlatform = cmsBtStockSeparatePlatformInfoDao.selectStockSeparatePlatform(sqlParam);
//        // 没有渠道数据的情况下，一般情况下不可能
//        if (stockSeparatePlatform == null || stockSeparatePlatform.size() == 0) {
//            return false;
//        }
//        // 增量任务对应的渠道和当前渠道不一致的情况，视为没有权限
//        if (!channelId.equals(stockSeparatePlatform.get(0).get("channel_id"))) {
//            return false;
//        }
//
//        return true;
//    }


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
        List<Map<String,Object>> statusCountList = cmsBtStockSeparateIncrementItemDao.selectStockSeparateIncrementItemBySql(sqlParam);
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
        List<Map<String,Object>> stockAllList = cmsBtStockSeparateIncrementItemDao.selectStockSeparateIncrementItemBySql(sqlParam);

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
     * 保存增量隔离库存明细
     *
     * @param param 客户端参数
     */
    public void saveItem(Map param){
        // 增量库存隔离明细
        Map<String, Object> stockInfo = (Map<String, Object>) param.get("stockInfo");
        // sku
        String sku = (String) stockInfo.get("sku");
        // 增量库存
        String incrementQty = (String) stockInfo.get("incrementQty");
        // 固定值隔离标志位
        boolean fixFlg = (boolean) stockInfo.get("fixFlg");

        // 取得任务id对应的Promotion是否未开始或者已经结束
        boolean promotionDuringFlg = isPromotionDuring((String) param.get("taskId"), (String) param.get("cartId"));
        if (!promotionDuringFlg) {
            throw new BusinessException("活动未开始或者已经结束，不能编辑数据！");
        }

        // 取得选择sku的增量库存隔离信息
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("subTaskId", (String) param.get("subTaskId"));
        sqlParam.put("sku", sku);
        sqlParam.put("tableNameSuffix", "");
        List<Map<String, Object>> stockSeparateIncrementList = cmsBtStockSeparateIncrementItemDao.selectStockSeparateIncrement(sqlParam);
        // 数据不存在的情况
        if (stockSeparateIncrementList == null || stockSeparateIncrementList.size() == 0) {
            throw new BusinessException("明细对象不存在！");
        }

        // 状态(DB)
        String statusDB = (String) stockSeparateIncrementList.get(0).get("status");
        // 增量库存(DB)
        String incrementQtyDB = String.valueOf(stockSeparateIncrementList.get(0).get("increment_qty"));
        // 固定值隔离标志位(DB)
        String fixFlgDB = String.valueOf(stockSeparateIncrementList.get(0).get("fix_flg"));

        // 增量库存隔离数据输入Check
        checksSparationIncrementInfo(stockInfo);

        simpleTransaction.openTransaction();
        try {
            // 画面的增量库存值 != DB的增量库存值 或者 画面的固定值隔离标志位！=  DB的固定值隔离标志位进行更新
            if (!incrementQty.equals(incrementQtyDB) || !(fixFlg ? TYPE_FIX_VALUE:TYPE_DYNAMIC).equals(fixFlgDB)) {
                // 状态为"0:未进行"以外的数据不能进行编辑
                if(!CmsTaskStockService.STATUS_READY.equals(statusDB)) {
                    throw new BusinessException("只有状态为未进行的明细才能进行编辑！");
                }
                Map<String, Object> sqlParam1 = new HashMap<String, Object>();
                sqlParam1.put("subTaskId", (String) param.get("subTaskId"));
                sqlParam1.put("sku", sku);
                sqlParam1.put("incrementQty", incrementQty);
                sqlParam1.put("fixFlg", fixFlg ? TYPE_FIX_VALUE:TYPE_DYNAMIC);
                int updateCnt = cmsBtStockSeparateIncrementItemDao.updateStockSeparateIncrementItem(sqlParam1);
                if (updateCnt != 1) {
                    throw new BusinessException("明细对象不存在！");
                }
            }
        } catch (BusinessException e) {
            simpleTransaction.rollback();
            throw e;
        } catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();
    }

    /**
     * 删除增量隔离库存明细
     *
     * @param taskId 任务id
     * @param subTaskId 子任务id
     * @param cartId 平台id
     * @param sku Sku
     */
    public void delItem(String taskId, String subTaskId, String cartId, String sku){
        // 取得任务id对应的Promotion是否未开始或者已经结束
        boolean promotionDuringFlg = isPromotionDuring(taskId, cartId);
        if (!promotionDuringFlg) {
            throw new BusinessException("活动未开始或者已经结束，不能删除数据！");
        }
        simpleTransaction.openTransaction();
        try {
            // 取得这条sku明细对应的库存隔离信息
            Map<String, Object> sqlParam = new HashMap<String, Object>();
            sqlParam.put("subTaskId", subTaskId);
            sqlParam.put("sku", sku);
            sqlParam.put("tableNameSuffix", "");
            List<Map<String, Object>> stockSeparateIncrementList = cmsBtStockSeparateIncrementItemDao.selectStockSeparateIncrement(sqlParam);
            // 数据不存在的情况
            if (stockSeparateIncrementList == null || stockSeparateIncrementList.size() == 0) {
                throw new BusinessException("明细对象不存在！");
            }

            // 状态（DB）
            String statusDB = (String) stockSeparateIncrementList.get(0).get("status");
            // 只有状态为 0：未进行的数据可以删除
            if(!CmsTaskStockService.STATUS_READY.equals(statusDB)) {
                throw new BusinessException("只有状态为未进行的明细才能进行删除！");
            }

            Map<String, Object> sqlParam1 = new HashMap<String, Object>();
            sqlParam.put("subTaskId", subTaskId);
            sqlParam.put("sku", sku);
            int delCount = cmsBtStockSeparateIncrementItemDao.deleteStockSeparateIncrementItem(sqlParam);
            if (delCount == 0) {
                throw new BusinessException("明细对象不存在！");
            }
        } catch (BusinessException e) {
            simpleTransaction.rollback();
            throw e;
        } catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();
    }

    /**
     * 启动/重刷增量库存隔离
     *
     * @param param 客户端参数
     */
    public void executeStockIncrementSeparation(Map param){
        // 取得任务id对应的Promotion是否未开始或者已经结束
        boolean promotionDuringFlg = isPromotionDuring((String) param.get("taskId"), (String) param.get("cartId"));
        if (!promotionDuringFlg) {
            throw new BusinessException("活动未开始或者已经结束，不能进行库存增量操作！");
        }
        simpleTransaction.openTransaction();
        try {
            Map<String, Object> sqlParam = new HashMap<String, Object>();
            // 更新状态为"1:等待增量"
            sqlParam.put("status", CmsTaskStockService.STATUS_WAITING_INCREMENT);
            sqlParam.put("modifier", param.get("userName"));
            // 更新条件
            sqlParam.put("subTaskId", param.get("subTaskId"));
            // 只有状态为"0:未进行"，"4:增量失败"的数据可以进行库存增量操作
            sqlParam.put("statusList", Arrays.asList(CmsTaskStockService.STATUS_READY, CmsTaskStockService.STATUS_INCREMENT_FAIL));
            int updateCnt = cmsBtStockSeparateIncrementItemDao.updateStockSeparateIncrementItem(sqlParam);
            if (updateCnt == 0) {
                throw new BusinessException("没有可以进行库存增量的数据！");
            }
        } catch (BusinessException e) {
            simpleTransaction.rollback();
            throw e;
        } catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();
    }


    /**
     *  增量库存隔离数据输入Check
     *
     * @param stockInfo 增量库存隔离数据
     */
    private void checksSparationIncrementInfo(Map<String,Object> stockInfo) throws BusinessException{
        // 增量库存值
        String incrementQty = (String) stockInfo.get("incrementQty");
        if (StringUtils.isEmpty(incrementQty) || !StringUtils.isDigit(incrementQty) || incrementQty.getBytes().length > 9 ) {
            throw new BusinessException("增量隔离库存必须输入小于10位的整数！");
        }
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
        sql += " t1.qty, t1.increment_qty, t1.fix_flg, t2.name as status_name from";
        sql += " (select * from voyageone_cms2.cms_bt_stock_separate_increment_item" + (String) param.get("tableNameSuffix") ;
        sql += cmsTaskStockService.getWhereSql(param, true);
        sql += " order by sku";
        String start = String.valueOf(param.get("start"));
        String length = String.valueOf(param.get("length"));
        sql += " limit " + start + "," + length + ") t1 ";
        sql +=" left join (select value,name from com_mt_value where type_id= '64' and lang_id = '" + param.get("lang") + "') t2 on t1.status = t2.value";
        return sql;
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
                            activityEnd = activityEnd + " 23:59:59";
                        }
                        if (DateTimeUtil.parse(activityEnd).compareTo(now) > 0) {
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
            // Title行
            writeExcelStockIncrementInfoHead(book, book.getXSSFWorkbook().getSheetAt(1), param);
            // 数据行
            writeExcelStockIncrementInfoRecord(book, book.getXSSFWorkbook().getSheetAt(1), param, resultData);

            // 格式copy用sheet删除
            book.removeSheetAt(1);

            $info("文档写入完成");

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
    private void writeExcelStockIncrementInfoHead(Workbook book, Sheet sheetModel, Map param) {

        Sheet sheet = book.getSheetAt(0);
        Drawing drawing = sheet.createDrawingPatriarch();
        CreationHelper helper = book.getCreationHelper();

        Row row = FileUtils.row(sheet, 0);
        CellStyle cellStyleProperty = sheetModel.getRow(0).getCell(4).getCellStyle(); // 属性的cellStyle

        List<Map> propertyList = (List<Map>) param.get("propertyList");
        String cartId = (String) param.get("cartId");
        String cartName = (String) param.get("cartName");

        // 内容输出
        int index = 0;
        FileUtils.cell(row, index++, cellStyleProperty).setCellValue("Model"); // Model
        FileUtils.cell(row, index++, cellStyleProperty).setCellValue("Code"); // Code
        FileUtils.cell(row, index++, cellStyleProperty).setCellValue("Sku"); // Sku

        // 属性
        for (Map property : propertyList) {
            FileUtils.cell(row, index++, cellStyleProperty).setCellValue((String) property.get("name"));

            Comment comment = drawing.createCellComment(helper.createClientAnchor());
            comment.setString(helper.createRichTextString((String) property.get("property")));
            comment.setRow(0);
            comment.setColumn(index - 1);
        }

        // 可调配库存
        FileUtils.cell(row, index++, cellStyleProperty).setCellValue(USABLESTOCK);

        // 平台
        CellStyle cellStylePlatform = sheetModel.getRow(0).getCell(0).getCellStyle(); // 平台的cellStyle
        FileUtils.cell(row, index++, cellStylePlatform).setCellValue(cartName);
        Comment comment = drawing.createCellComment(helper.createClientAnchor());
        comment.setString(helper.createRichTextString(cartId));
        comment.setRow(0);
        comment.setColumn(index - 1);

        // 固定值增量
        FileUtils.cell(row, index++, cellStylePlatform).setCellValue(FIXED_TEXT);

        // 筛选
        CellRangeAddress filter = new CellRangeAddress(0, 0, 0, index - 1);
        sheet.setAutoFilter(filter);
    }

    /**
     * 增量库存隔离Excel的数据写入
     */
    private void writeExcelStockIncrementInfoRecord(Workbook book, Sheet sheetModel, Map param, List<StockIncrementExcelBean> resultData) {

        Sheet sheet = book.getSheetAt(0);
        int lineIndex = 1; // 行号
        int colIndex = 0; // 列号
        Row row;

        CellStyle cellStyleDataLock = sheetModel.getRow(0).getCell(2).getCellStyle(); // 数据（锁定）的cellStyle
        CellStyle cellStyleData = sheetModel.getRow(0).getCell(3).getCellStyle(); // 数据（不锁定）的cellStyle

        List<Map> propertyList = (List<Map>) param.get("propertyList");
        int cntCol = 3 + propertyList.size() + 3; // 总列数
        double[] widthCol = new double[cntCol];

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
            FileUtils.cell(row, colIndex++, cellStyleDataLock).setCellValue(Double.valueOf(rowData.getQty().toPlainString()));
            // 平台
            FileUtils.cell(row, colIndex++, cellStyleData).setCellValue(Double.valueOf(rowData.getIncrement_qty().toPlainString()));

            // 固定值增量
            if (FIXED.equals(rowData.getFix_flg())) {
                // 按固定值进行增量隔离
                FileUtils.cell(row, colIndex++, cellStyleData).setCellValue(YES);
            }

            // 列宽
            if (lineIndex % 100 == 0 || lineIndex - 1 == resultData.size()) {
                for (int i = 0; i < cntCol; i++) {
                    double width = SheetUtil.getColumnWidth(sheet, i, false);
                    if (width > widthCol[i]) {
                        widthCol[i] = width;
                    }
                }
            }
        }

        // 设置列宽
        for (int i = 0; i < cntCol; i++) {
            cmsTaskStockService.setColumnWidth(sheet, i, widthCol[i]);
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
        String import_mode = (String) param.get("import_mode");
        Map<String, String> paramPlatformInfo = JacksonUtil.json2Bean((String) param.get("platformList"), Map.class);
        List<Map> paramPropertyList = JacksonUtil.json2Bean((String) param.get("propertyList"), List.class);

        // 取得任务id对应的Promotion是否未开始或者已经结束
        boolean promotionDuringFlg = isPromotionDuring(taskId, paramPlatformInfo.get("cartId"));
        if (!promotionDuringFlg) {
            throw new BusinessException("活动未开始或者已经结束，不能修改数据！");
        }

        // 取得增量库存隔离数据中是否存在状态为"0:未进行"以外的数据，则不允许导入
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("subTaskId", subTaskId);
        // 状态为"0:未进行"以外
        sqlParam.put("statusList", Arrays.asList( CmsTaskStockService.STATUS_WAITING_INCREMENT,
                CmsTaskStockService.STATUS_INCREASING,
                CmsTaskStockService.STATUS_INCREMENT_SUCCESS,
                CmsTaskStockService.STATUS_INCREMENT_FAIL,
                CmsTaskStockService. STATUS_REVERT));
        if (cmsBtStockSeparateIncrementItemDao.selectStockSeparateIncrementItemByStatus(sqlParam) != null ) {
            throw new BusinessException("此增量任务已经进行，不能修改数据！");
        }

        logger.info("增量库存隔离数据取得开始, sub_task_id=" + subTaskId);
        Map searchParam = new HashMap();
        searchParam.put("tableName", "voyageone_cms2.cms_bt_stock_separate_increment_item");
        searchParam.put("whereSql", " where sub_task_id= '" + subTaskId + "'");
        List<StockIncrementExcelBean> resultData = cmsBtStockSeparateIncrementItemDao.selectExcelStockIncrementInfo(searchParam);
        Map<String, StockIncrementExcelBean> mapSkuInDB = new HashMap<String, StockIncrementExcelBean>();
        for (StockIncrementExcelBean rowData : resultData) {
            mapSkuInDB.put(rowData.getSku(), rowData);
        }
        logger.info("增量库存隔离数据取得结束");

        logger.info("导入Excel取得并check的处理开始");
        List<StockIncrementExcelBean> insertData = new ArrayList<>(); // insert数据
        List<StockIncrementExcelBean> updateData = new ArrayList<>(); // update数据
        readExcel(file, import_mode, paramPropertyList, paramPlatformInfo, mapSkuInDB, insertData, updateData);
        logger.info("导入Excel取得并check的处理结束");

        if (insertData.size() > 0 || updateData.size() > 0) {
            // 有更新对象
            logger.info("更新开始");
            saveImportData(insertData, updateData, import_mode, subTaskId, (String) param.get("userName"), (String) param.get("channelId"));
            logger.info(String.format("更新结束,更新了%d件", insertData.size() + updateData.size()));
        } else {
            // 没有更新对象
            logger.info("没有更新对象");
            throw new BusinessException("没有更新对象");
        }

    }

    /**
     * 读取导入文件，并做check
     *
     * @param file              导入文件
     * @param import_mode       导入mode
     * @param paramPropertyList 属性list
     * @param paramPlatformInfo 平台信息
     * @param mapSkuInDB        cms_bt_stock_separate_increment_item的数据
     * @param insertData        insert数据
     * @param updateData        update数据
     */
    private void readExcel(MultipartFile file, String import_mode, List<Map> paramPropertyList, Map<String, String> paramPlatformInfo, Map<String, StockIncrementExcelBean> mapSkuInDB, List<StockIncrementExcelBean> insertData, List<StockIncrementExcelBean> updateData) {
        List<String> listExcelSku = new ArrayList<String>();

        Workbook wb;
        int colPlatform = -1;
        try {
            wb = WorkbookFactory.create(file.getInputStream());
        } catch (IOException | InvalidFormatException e) {
            throw new BusinessException("7000005");
        } catch (Exception e) {
            throw new BusinessException("7000005");
        }

        Sheet sheet = wb.getSheetAt(0);
        boolean isHeader = true;
        for (Row row : sheet) {
            if (isHeader) {
                // 第一行Title行
                isHeader = false;
                // Title行check
                colPlatform = checkHeader(row, paramPropertyList, paramPlatformInfo);
            } else {
                // 数据行
                checkRecord(row, sheet.getRow(0), import_mode, colPlatform, mapSkuInDB, insertData, updateData, listExcelSku);
            }
        }
    }

    /**
     * Title行check
     *
     * @param row 行
     * @param paramPropertyList 属性list
     * @param paramPlatformInfo 平台信息
     * @return 平台对应列号
     */
    private int checkHeader(Row row, List<Map> paramPropertyList, Map<String, String> paramPlatformInfo) {
        String messageModelErr = "请使用正确格式的excel导入文件";
        if (!"Model".equals(getCellValue(row, 0))
                || !"Code".equals(getCellValue(row, 1))
                || !"Sku".equals(getCellValue(row, 2))) {
            throw new BusinessException(messageModelErr);
        }

        // 属性列check
        List<String> listPropertyKey = new ArrayList<String>();
        paramPropertyList.forEach(paramProperty -> listPropertyKey.add((String) paramProperty.get("property")));

        List<String> propertyList = new ArrayList<String>();
        int index = 3;
        for (; index <= 6; index++) {
            String comment = getCellCommentValue(row, index);
            if (StringUtils.isEmpty(comment)) {
                // 注解为空
                if (propertyList.size() > 0) {
                    // 已经有属性列，属性列结束
                    break;
                } else {
                    // 没有属性列，此列也不是属性列，错误Excel
                    throw new BusinessException(messageModelErr);
                }
            }
            if (!listPropertyKey.contains(comment)) {
                // 注解不是设定属性
                throw new BusinessException(messageModelErr);
            } else {
                // 注解是设定属性
                if (propertyList.contains(comment)) {
                    // 已经存在相同属性列
                    throw new BusinessException(messageModelErr);
                } else {
                    propertyList.add(comment);
                }
            }
        }

        if (propertyList.size() != listPropertyKey.size()) {
            // Excel属性列少于设定属性列
            throw new BusinessException(messageModelErr);
        }

        if (!USABLESTOCK.equals(getCellValue(row, index++))) {
            throw new BusinessException(messageModelErr);
        }

        // 平台对应起始列号
        int colPlatform = index;

        // 平台列check
        String comment = getCellCommentValue(row, index++);
        if (StringUtils.isEmpty(comment)) {
            // 注解为空
            throw new BusinessException(messageModelErr);
        }
        if (!comment.equals(paramPlatformInfo.get("cartId"))) {
            // 注解不是设定平台
            throw new BusinessException(messageModelErr);
        }

        if (!FIXED_TEXT.equals(getCellValue(row, index))) {
            throw new BusinessException(messageModelErr);
        }

        return colPlatform;
    }

    /**
     * check数据，并返回保存对象
     *
     * @param row          数据行
     * @param rowHeader    Title行
     * @param import_mode  导入mode
     * @param colPlatform  平台对应列号
     * @param mapSkuInDB   cms_bt_stock_separate_increment_item的数据
     * @param insertData   insert数据
     * @param updateData   update数据
     * @param listExcelSku Excel输入的sku
     */
    private void checkRecord(Row row, Row rowHeader, String import_mode, int colPlatform, Map<String, StockIncrementExcelBean> mapSkuInDB, List<StockIncrementExcelBean> insertData, List<StockIncrementExcelBean> updateData, List<String> listExcelSku) {
        String model = getCellValue(row, 0); // Model
        String code = getCellValue(row, 1); // Code
        String sku = getCellValue(row, 2); // Sku

        // Model输入check
        if (StringUtils.isEmpty(model) || model.getBytes().length > 50) {
            throw new BusinessException("Model必须输入且长度小于50！" + "Sku=" + sku);
        }
        // Code输入check
        if (StringUtils.isEmpty(code) || code.getBytes().length > 50) {
            throw new BusinessException("Code必须输入且长度小于50！" + "Sku=" + sku);
        }
        // Sku输入check
        if (StringUtils.isEmpty(sku) || sku.getBytes().length > 50) {
            throw new BusinessException("Sku必须输入且长度小于50！" + "Sku=" + sku);
        }

        if (listExcelSku.contains(sku)) {
            throw new BusinessException("Sku重复输入！" + "Sku=" + sku);
        } else {
            listExcelSku.add(sku);
        }

        for (int index = 3; index <= colPlatform - 2; index++) {
            // 属性
            String property = getCellValue(row, index);
            if (StringUtils.isEmpty(property) || property.getBytes().length > 500) {
                throw new BusinessException(getCellValue(rowHeader, index) + "必须输入且长度小于500！" + "Sku=" + sku);
            }
        }

        // 可调配库存输入check
        String usableStock = getCellValue(row, colPlatform - 1);
        if (StringUtils.isEmpty(usableStock) || !StringUtils.isDigit(usableStock) || usableStock.getBytes().length > 9) {
            throw new BusinessException("可调配库存必须输入小于10位的整数！" + "Sku=" + sku);
        }

        // 增量隔离库存
        String increment_qty = getCellValue(row, colPlatform);
        if (StringUtils.isEmpty(increment_qty) || !StringUtils.isDigit(increment_qty) || increment_qty.getBytes().length > 9) {
            throw new BusinessException("增量隔离库存必须输入小于10位的整数！" + "Sku=" + sku);
        }

        // 固定值增量
        String fix = getCellValue(row, colPlatform + 1);
        if (StringUtils.isEmpty(fix)) {
            fix = TYPE_DYNAMIC;
        } else if (YES.equals(fix)) {
            fix = TYPE_FIX_VALUE;
        } else {
            throw new BusinessException("固定值增量请输入'" + YES + "'或不输入！" + "Sku=" + sku);
        }

        boolean isAddData = false;
        boolean isUpdateData = false;
        if (EXCEL_IMPORT_UPDATE.equals(import_mode)) {
            // 变更方式导入
            StockIncrementExcelBean beanInDB = mapSkuInDB.get(sku);
            if (beanInDB == null) {
                // DB不存在,新建数据
                isAddData = true;
            } else {
                // DB存在,更新数据
                if (!model.equals(beanInDB.getProduct_model())) {
                    throw new BusinessException("变更方式导入时,Model不能变更！" + "Sku=" + sku);
                }
                if (!code.equals(beanInDB.getProduct_code())) {
                    throw new BusinessException("变更方式导入时,Code不能变更！" + "Sku=" + sku);
                }
                if (!sku.equals(beanInDB.getSku())) {
                    throw new BusinessException("变更方式导入时,Sku不能变更！" + "Sku=" + sku);
                }

                for (int index = 3; index <= colPlatform - 2; index++) {
                    // 属性
                    String propertyNa = getCellCommentValue(rowHeader, index);
                    String property = getCellValue(row, index);
                    if (!property.equals(beanInDB.getProperty(propertyNa))) {
                        throw new BusinessException("变更方式导入时," + getCellValue(rowHeader, index) + "不能变更！" + "Sku=" + sku);
                    }
                }

                if (!usableStock.equals(beanInDB.getQty().toPlainString())) {
                    throw new BusinessException("变更方式导入时,可调配库存不能变更！" + "Sku=" + sku);
                }

                if (!increment_qty.equals(beanInDB.getIncrement_qty().toPlainString()) || !fix.equals(beanInDB.getFix_flg())) {
                    // 数据变更，是更新对象
                    isUpdateData = true;
                }
            }

        } else {
            // 重置方式导入
            isAddData = true;
        }

        if (isAddData || isUpdateData) {
            // 是更新对象
            StockIncrementExcelBean bean = new StockIncrementExcelBean();
            bean.setProduct_model(model);
            bean.setProduct_code(code);
            bean.setSku(sku);
            for (int index = 3; index <= colPlatform - 2; index++) {
                // 属性
                bean.setProperty(getCellCommentValue(rowHeader, index), getCellValue(row, index));
            }
            bean.setQty(new BigDecimal(usableStock));
            bean.setIncrement_qty(new BigDecimal(increment_qty));
            bean.setStatus("0");
            bean.setFix_flg(fix);

            if (isAddData) {
                insertData.add(bean);
            }
            if (isUpdateData) {
                updateData.add(bean);
            }
        }
    }

    /**
     * 返回单元格注解值
     *
     * @param row 行
     * @param col 列
     * @return 单元格注解值
     */
    private String getCellCommentValue(Row row, int col) {
        if (row == null) return null;
        if (row.getCell(col) == null) return null;
        if (row.getCell(col).getCellComment() == null) return null;
        return row.getCell(col).getCellComment().getString().getString();
    }

    /**
     * 返回单元格值
     *
     * @param row 行
     * @param col 列
     * @return 单元格值
     */
    private String getCellValue(Row row, int col) {
        return cmsTaskStockService.getCellValue(row, col);
    }

    /**
     * 导入文件数据更新
     *
     * @param insertData  insert对象
     * @param updateData  update对象
     * @param import_mode 导入方式
     * @param subTaskId   任务id
     * @param creater     创建者/更新者
     * @param channelId   渠道id
     */
    private void saveImportData(List<StockIncrementExcelBean> insertData, List<StockIncrementExcelBean> updateData, String import_mode, String subTaskId, String creater, String channelId) {
        try {
            transactionRunner.runWithTran(() -> {
                if (EXCEL_IMPORT_DELETE_UPDATE.equals(import_mode)) {
                    // 重置方式
                    // 此任务所有数据删除
                    cmsBtStockSeparateIncrementItemDao.deleteStockSeparateIncrementItem(new HashMap<String, Object>(){{this.put("subTaskId",subTaskId);}});
                    // 插入数据
                    insertImportData(insertData, subTaskId, creater, channelId);
                } else {
                    // 变更方式
                    // 更新数据
                    updateImportData(updateData, subTaskId, creater);
                    // 插入数据
                    insertImportData(insertData, subTaskId, creater, channelId);
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BusinessException("更新异常,请重新尝试！");
        }
    }

    /**
     * 导入文件数据插入更新
     *
     * @param insertData insert对象
     * @param subTaskId  任务id
     * @param creater    创建者/更新者
     * @param channelId  渠道id
     */
    private void insertImportData(List<StockIncrementExcelBean> insertData, String subTaskId, String creater, String channelId) {
        List<Map<String, Object>> listSaveData = new ArrayList<Map<String, Object>>();
        for (StockIncrementExcelBean bean : insertData) {
            Map<String, Object> mapSaveData;
            try {
                mapSaveData = PropertyUtils.describe(bean);
            } catch (Exception e) {
                throw new BusinessException("导入文件有数据异常");
            }

            mapSaveData.put("sub_task_id", subTaskId);
            mapSaveData.put("creater", creater);
            mapSaveData.put("channelId", channelId);

            listSaveData.add(mapSaveData);
            if (listSaveData.size() == 200) {
                cmsBtStockSeparateIncrementItemDao.insertStockSeparateIncrementItemFromExcel(listSaveData);
                listSaveData.clear();
            }
        }

        if (listSaveData.size() > 0) {
            cmsBtStockSeparateIncrementItemDao.insertStockSeparateIncrementItemFromExcel(listSaveData);
            listSaveData.clear();
        }
    }

    /**
     * 导入文件数据变更更新
     *
     * @param updateData update对象
     * @param subTaskId  任务id
     * @param creater    创建者/更新者
     */
    private void updateImportData(List<StockIncrementExcelBean> updateData, String subTaskId, String creater) {
        for (StockIncrementExcelBean bean : updateData) {
            Map<String, Object> mapSaveData =  new HashMap<String, Object>();
            mapSaveData.put("sub_task_id", subTaskId);
            mapSaveData.put("sku", bean.getSku());
            mapSaveData.put("incrementQty", bean.getIncrement_qty());
            mapSaveData.put("fixFlg", bean.getFix_flg());
            mapSaveData.put("modifier", creater);

            cmsBtStockSeparateIncrementItemDao.updateStockSeparateIncrementItem(mapSaveData);
        }
    }

}
