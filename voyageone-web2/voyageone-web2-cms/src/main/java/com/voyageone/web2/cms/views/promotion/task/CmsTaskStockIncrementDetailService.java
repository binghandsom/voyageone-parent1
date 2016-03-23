package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.cms.bean.promotion.task.StockIncrementExcelBean;
import com.voyageone.web2.cms.dao.CmsBtStockSeparateIncrementItemDao;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by morse.lu on 2016/3/23.
 */
@Service
public class CmsTaskStockIncrementDetailService extends BaseAppService {

    @Autowired
    private CmsBtStockSeparateIncrementItemDao cmsBtStockSeparateIncrementItemDao;

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
     * 取得where条件的Sql文
     *
     * @param param
     * @param statusFlg 使用状态条件Flg
     * @return where条件的Sql文
     */
    private String getWhereSql(Map param, boolean statusFlg) {
        String whereSql = " where ";
        // 任务Id
        whereSql += " sub_task_id = " + String.valueOf(param.get("taskId")) + " ";

        // 商品model
        if (!StringUtils.isEmpty((String) param.get("model"))) {
            whereSql += " and product_model = '" + escapeSpecialChar((String) param.get("model")) + "'";
        }

        // 商品code
        if (!StringUtils.isEmpty((String) param.get("code"))) {
            whereSql += " and product_code = '" + escapeSpecialChar((String) param.get("code")) + "'";
        }
        // sku
        if (!StringUtils.isEmpty((String) param.get("sku"))) {
            whereSql += " and sku = '" + escapeSpecialChar((String) param.get("sku")) + "'";
        }

        //状态
        if (statusFlg && !StringUtils.isEmpty((String) param.get("status"))) {
            whereSql += " and status = '" + (String) param.get("status") + "'";
        }

        for (Map<String, Object> property : (List<Map<String, Object>>) param.get("propertyList")) {
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
            // 存在~则加上大于等于和小于等于的条件(如果是数值型则转换为数值做比较)
            if (propertyValue.contains("~")) {
                String values[] = propertyValue.split("~");
                if ("int".equals(type)) {
                    // propertyName = "convert(" + propertyName + ",signed)";
                    propertyName = propertyName + "*1";
                }
                int end = 2;
                if (values.length < 2) {
                    end = values.length;
                }
                for (int i = 0; i < end; i++) {
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
            if (values != null && values.length > 0) {
                whereSql += " and ( " + propertySql.substring(0, propertySql.length() - 3) + " ) ";
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
    private String escapeSpecialChar(String value) {
        return value.replaceAll("\\\\", "\\\\\\\\").replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\"")
                .replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");
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

        param.put("whereSql", getWhereSql(param, true));
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
        Map platform = (Map) param.get("platformList");

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
        FileUtils.cell(row, index++, cellStylePlatform).setCellValue((String) platform.get("cartName"));
        Comment comment = drawing.createCellComment(helper.createClientAnchor());
        comment.setString(helper.createRichTextString((String) platform.get("cartId")));
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

}
