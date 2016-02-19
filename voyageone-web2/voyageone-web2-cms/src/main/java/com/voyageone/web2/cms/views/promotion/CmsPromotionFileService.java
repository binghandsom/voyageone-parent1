package com.voyageone.web2.cms.views.promotion;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.CmsBtInventoryOutputTmpModel;
import com.voyageone.web2.sdk.api.request.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jerry.ji on 2015/12/15.
 * @version 2.0.0
 */
@Service
public class CmsPromotionFileService extends BaseAppService {

    // 下载Code级别文件名
    private String downloadCodeFileName = "Code_%s_%s.xlsx"; //"Code_YYYYMMDD_HHMMSS.xlsx";

    // 未结束提示信息
    private String notEndPromptMessage = "未完，存在未抽出数据！";

    // 其他用户使用提示信息
    private String otherUserProcessMessage = "其他用户正在使用，请5分钟后再试！";

    // DB检索页大小
//    private int select_pagesize = 10;
    private int select_pagesize = 50;

    // Excel 文件最大行数
//    private int max_excel_reccount = 500;
    private int max_excel_reccount = 1000;


    // Sku 文件单线程用
    ReentrantLock lock = new ReentrantLock();

    @Autowired
    VoApiDefaultClient voApiClient;

    /**
     * Code单位，文件生成
     *
     * @param params 输入参数
     * @return byte[]  下载用文件
     */
    public byte[] getCodeExcelFile(Map<String,Object> params) throws IOException, InvalidFormatException {

//        String templatePath = readValue(CmsConstants.Props.CODE_TEMPLATE);
        String templatePath = "D:/jiming/work/cms/templete/code-template.xlsx";

        String cartId = (String)params.get("cartId");
        String channelId = (String)params.get("channelId");

        long recCount = selectProductByCartIdRecCount(channelId, cartId);

        int pageCount = 0;
        if ((int) recCount % select_pagesize > 0) {
            pageCount =(int) recCount / select_pagesize + 1;
        } else {
            pageCount =(int) recCount / select_pagesize;
        }

        $info("准备生成 Item 文档 [ %s ]", recCount);
        $info("准备打开文档 [ %s ]", templatePath);

        try (InputStream inputStream = new FileInputStream(templatePath);
             Workbook book = WorkbookFactory.create(inputStream)) {

            for (int i = 0; i < pageCount; i++) {
//                List<CmsBtProductModel> items = cmsBtProductDao.selectProductByCartId(channelId, cartId, i, select_pagesize);
                List<CmsBtProductModel> items = selectProductByCartId(channelId, cartId, i, select_pagesize);

                if (items.size() == 0) {
                    break;
                }

                // 每页开始行
                int startRowIndex =  i * select_pagesize + 1;
                boolean isContinueOutput = writeRecordToFile(book, items, cartId, startRowIndex);
                // 超过最大行的场合
                if (!isContinueOutput) {
                    break;
                }
            }

            $info("文档写入完成");

            // TODO test
            try (FileOutputStream outputFileStream = new FileOutputStream("D:\\jiming\\work\\cms\\temp\\test.xlsx")) {

                book.write(outputFileStream);

                outputFileStream.flush();
                outputFileStream.close();
            }

            // 返回值设定
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                book.write(outputStream);

                $info("已写入输出流");

                return outputStream.toByteArray();
            }
        }
    }


    /**
     * selectProductByCartIdRecCount
     * @param channelId channel Id
     * @param cartId cart Id
     * @return count
     */
    private long selectProductByCartIdRecCount(String channelId, String cartId) {
        ProductsCountRequest requestModel = new ProductsCountRequest(channelId);
        requestModel.addProp("groups.platforms.cartId", Integer.valueOf(cartId));
        return voApiClient.execute(requestModel).getTotalCount();
    }

    private long selectMainGroupByCartIdRecCount(String channelId, String cartId) {
        ProductsCountRequest requestModel = new ProductsCountRequest(channelId);
        requestModel.addProp("groups.platforms.cartId", Integer.valueOf(cartId));
//        requestModel.addProp("groups.platforms.isMain", 1);
        requestModel.addProp("groups.platforms.isMain", 0);
        return voApiClient.execute(requestModel).getTotalCount();
    }

    /**
     * 调用SDK取得Code数据（含SKU）
     *
     * @param channelId
     * @param cartId
     * @param pageNo
     * @param pageSize
     * @return List<CmsBtProductModel>  Code数据（含SKU）
     */
    private List<CmsBtProductModel> selectProductByCartId(String channelId, String cartId, int pageNo, int pageSize) {
        //设置参数
        ProductsGetRequest requestModel = new ProductsGetRequest(channelId);

        requestModel.addProp("groups.platforms.cartId", Integer.valueOf(cartId));
        requestModel.setPageNo(pageNo + 1);
        requestModel.setPageSize(pageSize);
        requestModel.setFields("{\"prodId\":1,\"groups.platforms.cartId.$\":1,\"fields.model\":1,\"fields.code\":1,\"fields.productName\":1,\"batch_update.code_qty\":1,\"fields.salePriceStart\":1,\"fields.salePriceEnd\":1,\"catPath\":1,\"skus\":1}");

        //SDK取得Product 数据
        List<CmsBtProductModel> productList = voApiClient.execute(requestModel).getProducts();

        return productList;
    }

    /**
     * 调用SDK取得Model数据
     *
     * @param channelId
     * @param cartId
     * @param pageNo
     * @param pageSize
     * @return List<CmsBtProductModel>  Model数据
     */
    private List<CmsBtProductModel> selectModelByCartId(String channelId, String cartId, int pageNo, int pageSize) {
        //设置参数
        ProductsGetRequest requestModel = new ProductsGetRequest(channelId);

        requestModel.addProp("groups.platforms.cartId", Integer.valueOf(cartId));
//        requestModel.addProp("groups.platforms.isMain", 1);
        requestModel.addProp("groups.platforms.isMain", 0);
        requestModel.setPageNo(pageNo + 1);
        requestModel.setPageSize(pageSize);
        requestModel.setFields("{\"groups.platforms.cartId.$\":1,\"groups.salePriceStart\":1,\"groups.salePriceEnd\":1,\"prodId\":1,\"fields.model\":1}");

        //SDK取得Product 数据
        List<CmsBtProductModel> productList = voApiClient.execute(requestModel).getProducts();

        return productList;
    }

    /**
     * Code单位，文件输出
     *
     * @param book 输出Excel文件对象
     * @param items 待输出DB数据
     * @param cartId
     * @param startRowIndex 开始
     * @return boolean 是否终止输出
     */
    private boolean writeRecordToFile(Workbook book, List<CmsBtProductModel> items, String cartId, int startRowIndex) {
        boolean isContinueOutput = true;

        CellStyle unlock = createUnLockStyle(book);

            /*
             * 现有表格的列:
             * 0: No
             * 1: productId
             * 2: groupId
             * 3: numiid
             * 4: Model
             * 5: Code
             * 6: Product_Name
             * 7: Qty
             * 8: Sale_Price
             * 9: 类目Path
             */
        Sheet sheet = book.getSheetAt(0);

        for (int i = 0; i < items.size(); i++) {

            CmsBtProductModel item = items.get(i);

            Row row = row(sheet, startRowIndex);

            // 最大行限制
            if ( startRowIndex + 1 > max_excel_reccount -1) {
                isContinueOutput = false;

                cell(row, 0, unlock).setCellValue(notEndPromptMessage);

                break;
            }

            // 内容输出
            cell(row, 0, unlock).setCellValue(startRowIndex);

            cell(row, 1, unlock).setCellValue(item.getProdId());

            cell(row, 2, unlock).setCellValue(item.getGroups().getPlatformByCartId(Integer.valueOf(cartId)).getGroupId());

            cell(row, 3, unlock).setCellValue(item.getGroups().getPlatformByCartId(Integer.valueOf(cartId)).getNumIId());

            cell(row, 4, unlock).setCellValue(item.getFields().getModel());

            cell(row, 5, unlock).setCellValue(item.getFields().getCode());

            cell(row, 6, unlock).setCellValue(item.getFields().getProductNameEn());

            cell(row, 7, unlock).setCellValue(StringUtils.null2Space2(String.valueOf(item.getBatchField().getCodeQty())));

            cell(row, 8, unlock).setCellValue(getOutputPrice(item.getFields().getPriceSaleSt(), item.getFields().getPriceSaleEd()));

            cell(row, 9, unlock).setCellValue(item.getCatPath());

            startRowIndex = startRowIndex + 1;
        }

        return isContinueOutput;
    }

    /**
     * Excel 行对象取得
     *
     * @param sheet Excel Sheet对象
     * @param rowIndex Excel RowIndex
     * @return Row Excel Row对象
     */
    private Row row(Sheet sheet, int rowIndex) {

        Row row = sheet.getRow(rowIndex);

        if (row == null) row = sheet.createRow(rowIndex);

        return row;
    }

    /**
     * Excel Cell对象取得
     *
     * @param row Excel Row对象
     * @param index Excel CellIndex
     * @param cellStyle Excel Cell样式
     * @return Row Excel Cell对象
     */
    private Cell cell(Row row, int index, CellStyle cellStyle) {

        Cell cell = row.getCell(index);

        if (cell == null) cell = row.createCell(index);

        if (cellStyle != null) cell.setCellStyle(cellStyle);

        return cell;
    }

    /**
     * Excel Cell样式取得
     *
     * @param book Excel Book对象
     * @return CellStyle Excel Cell样式
     */
    private CellStyle createUnLockStyle(Workbook book) {
        CellStyle cellStyle = book.createCellStyle();

        cellStyle.setLocked(false);

        return cellStyle;
    }

    /**
     * 金额输出
     *
     * @param strPrice 最小金额
     * @param endPrice 最大金额
     * @return String 输出金额
     */
    private String getOutputPrice(double strPrice, double endPrice) {
        String output = "";

        if (strPrice == endPrice) {
            output = String.valueOf(strPrice);
        } else {
            output = strPrice + "～" + endPrice;
        }

        return output;
    }

    /**
     * Sku单位，文件生成
     *
     * @param params 输入参数
     * @return byte[]  下载用文件
     */
    public byte[] getSkuExcelFile(Map<String,Object> params) throws IOException, InvalidFormatException, BusinessException {

        byte[] output = null;

//        Lock lock = new ReentrantLock();

        if (lock.tryLock()) {
            lock.lock();

            try {
                $info("Mysql SKU 数据插入");
                // Mysql 数据插入
                List<Object> dbInsertRet = getSkuExcelFileMysqlInsert(params);
                boolean dbInsert = (boolean)dbInsertRet.get(0);
                boolean isContinueOutput = (boolean)dbInsertRet.get(1);

                if (dbInsert) {
                    $info("EXCEL SKU 数据输出");
                    // Excel 文件输出
                    output = getSkuExcelFileOutput(isContinueOutput);
                }
            } finally {
                $info("EXCEL SKU 数据清空");
                // 临时数据清空
                voApiClient.execute(new PromotionSkuInventoryInfoDeleteRequest());

                lock.unlock();
            }
        } else {
            throw new BusinessException(otherUserProcessMessage);
        }

        return output;
    }

    /**
     * MongoDB产品数据取出，Mysql 插入
     *
     * @param params 输入参数
     * @return ret[0] 执行结果
     *          ret[1] 是否还有数据
     */
    private  List<Object> getSkuExcelFileMysqlInsert(Map<String,Object> params) {
        List<Object> retArr = new ArrayList<Object>();

        boolean ret = true;
        boolean isContinueOutput = false;

        String cartId = (String)params.get("cartId");
        String channelId = (String)params.get("channelId");

        long recCount = selectProductByCartIdRecCount(channelId, cartId);

        int pageCount = 0;
        if ((int) recCount % select_pagesize > 0) {
            pageCount =(int) recCount / select_pagesize + 1;
        } else {
            pageCount =(int) recCount / select_pagesize;
        }

        int skuCount = 0;
        for (int i = 0; i < pageCount; i++) {
//            List<CmsBtProductModel> items = cmsBtProductDao.selectProductByCartId(channelId, cartId, i, select_pagesize);
            List<CmsBtProductModel> items = selectProductByCartId(channelId, cartId, i, select_pagesize);

            if (items.size() == 0) {
                break;
            }

            skuCount = skuCount + getItemSkuCount(items);
            // 超过最大行的场合
            if (skuCount > max_excel_reccount) {
                isContinueOutput = true;
                break;
            }

            // Mysql 数据插入
            ret = insertSkuMysql(channelId, cartId, items);
            if(!ret) {
                break;
            }
        }

        retArr.add(ret);
        retArr.add(isContinueOutput);

        return retArr;
    }

    /**
     * Code 对应SKU数量取出
     *
     * @param items MongoDB中Code数据
     * @return ret sku数量
     */
    private int getItemSkuCount(List<CmsBtProductModel> items) {
        int ret = 0;
        for (int i = 0; i < items.size(); i++) {
            CmsBtProductModel cmsBtProductModel = items.get(i);
            ret = ret + cmsBtProductModel.getSkus().size();
        }

        return ret;
    }

    /**
     * 对应SKU，MysqlDB插入
     *
     * @param channelId
     * @param cartId
     * @param items MongoDB中Code数据
     * @return ret 执行结果
     */
    private boolean insertSkuMysql(String channelId, String cartId, List<CmsBtProductModel> items) {
        boolean ret = false;

        List<Object> insertStringRet = getSkuMysqlInsertString(channelId, cartId, items);
        int insertRecCount = (int)insertStringRet.get(0);
        String insertRecString = (String)insertStringRet.get(1);

        PromotionSkuInventoryInfoInsertRequest request=new PromotionSkuInventoryInfoInsertRequest();
        request.setInsertRecString(insertRecString);
        ret =voApiClient.execute(request).isInsert();

        return ret;
    }

    /**
     * 对应SKU，Mysql插入用Sql文取得
     *
     * @param channelId
     * @param cartId
     * @param items MongoDB中Code数据
     * @return ret[0] 待插入记录数量
     *          ret[1] 待插入DBSql文
     */
    private List<Object> getSkuMysqlInsertString(String channelId, String cartId, List<CmsBtProductModel> items) {
        List<Object> retArr = new ArrayList<Object>();

        int retCount = 0;
        StringBuffer retStr = new StringBuffer();
        // order_channel_id
        // prodId,
        // groupId
        // numIid
        // model
        // code
        // skuCode
        // productName
        // priceSale
        // created
        String insertItemFormat = "('%s','%s','%s','%s','%s','%s','%s','%s','%s',%s)";

        for (int i = 0; i < items.size(); i++) {
            CmsBtProductModel cmsBtProductModel = items.get(i);
            List<CmsBtProductModel_Sku> skuList = cmsBtProductModel.getSkus();

            for (int j = 0; j < skuList.size(); j++) {
                CmsBtProductModel_Sku sku = skuList.get(j);

                String insertItemStr = String.format(insertItemFormat,
                        channelId,
                        cmsBtProductModel.getProdId(),
                        cmsBtProductModel.getGroups().getPlatformByCartId(Integer.valueOf(cartId)).getGroupId(),
                        cmsBtProductModel.getGroups().getPlatformByCartId(Integer.valueOf(cartId)).getNumIId(),
                        cmsBtProductModel.getFields().getModel(),
                        cmsBtProductModel.getFields().getCode(),
                        sku.getSkuCode(),
                        cmsBtProductModel.getFields().getProductNameEn(),
                        sku.getPriceSale(),
                        "now()"
                        );

                if (StringUtils.isEmpty(retStr.toString())) {
                    retStr.append(insertItemStr);
                } else {
                    retStr.append(",");
                    retStr.append(insertItemStr);
                }

                retCount = retCount + 1;
            }
        }

        retArr.add(retCount);
        retArr.add(retStr.toString());

        return retArr;
    }

    /**
     * Sku单位，文件生成(根据wms_bt_inventory_center_output_tmp的数据，做成)
     * @param isContinueOutput 是否超过最大行
     * @return byte[]  下载用文件
     */
    private byte[] getSkuExcelFileOutput(boolean isContinueOutput) throws IOException, InvalidFormatException {
//        String templatePath = readValue(CmsConstants.Props.CODE_TEMPLATE);
        String templatePath = "D:/jiming/work/cms/templete/sku-template.xlsx";

        long recCount = voApiClient.execute(new PromotionSkuInventoryInfoGetCountRequest()).getTotalCount();

        int pageCount = 0;
        if ((int) recCount % select_pagesize > 0) {
            pageCount =(int) recCount / select_pagesize + 1;
        } else {
            pageCount =(int) recCount / select_pagesize;
        }

        $info("准备生成 Item 文档 [ %s ]", recCount);
        $info("准备打开文档 [ %s ]", templatePath);

        try (InputStream inputStream = new FileInputStream(templatePath);
             Workbook book = WorkbookFactory.create(inputStream)) {
            // 最终行Index
            int endRowIndex = 0;
            for (int i = 0; i < pageCount; i++) {
                PromotionSkuInventoryInfoGetRequest request=new PromotionSkuInventoryInfoGetRequest();
                request.setPageNo(i);
                request.setPageSize(select_pagesize);
                List<CmsBtInventoryOutputTmpModel> items = voApiClient.execute(request).getModels();

                if (items.size() == 0) {
                    break;
                }

                // 每页开始行
                int startRowIndex =  i * select_pagesize + 1;
                boolean writeResult = writeRecordToFileForSku(book, items, startRowIndex);

                endRowIndex = startRowIndex + items.size();

                // 异常的场合
                if (!writeResult) {
                    break;
                }
            }

            // 未结束行输出
            if (isContinueOutput) {
                writeRecordToFileForSkuNotEnd(book, endRowIndex);
            }

            $info("文档写入完成");

            // TODO test
            try (FileOutputStream outputFileStream = new FileOutputStream("D:\\jiming\\work\\cms\\temp\\testSku.xlsx")) {

                book.write(outputFileStream);

                outputFileStream.flush();
                outputFileStream.close();
            }

            // 返回值设定
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                book.write(outputStream);

                $info("已写入输出流");

                return outputStream.toByteArray();
            }
        }
    }

    /**
     * Sku单位，文件输出
     *
     * @param book 输出Excel文件对象
     * @param items 待输出DB数据
     * @param startRowIndex 开始
     * @return boolean 执行结果
     */
    private boolean writeRecordToFileForSku(Workbook book, List<CmsBtInventoryOutputTmpModel> items, int startRowIndex) {
        boolean ret = true;

        CellStyle unlock = createUnLockStyle(book);

            /*
             * 现有表格的列:
             * 0: No
             * 1: productId
             * 2: groupId
             * 3: numiid
             * 4: Model
             * 5: Code
             * 6: Product_Name
             * 7: Qty
             * 8: Sale_Price
             * 9: 类目Path
             */
        Sheet sheet = book.getSheetAt(0);

        for (int i = 0; i < items.size(); i++) {

            CmsBtInventoryOutputTmpModel item = items.get(i);

            Row row = row(sheet, startRowIndex);

            // 内容输出
            cell(row, 0, unlock).setCellValue(startRowIndex);

            cell(row, 1, unlock).setCellValue(item.getProdId());

            cell(row, 2, unlock).setCellValue(item.getGroupId());

            cell(row, 3, unlock).setCellValue(item.getNumIid());

            cell(row, 4, unlock).setCellValue(item.getModel());

            cell(row, 5, unlock).setCellValue(item.getCode());

            cell(row, 6, unlock).setCellValue(item.getSkuCode());

            cell(row, 7, unlock).setCellValue(item.getProductName());

            cell(row, 8, unlock).setCellValue(item.getQtyOrgin());

            cell(row, 9, unlock).setCellValue(item.getPriceSale());

            startRowIndex = startRowIndex + 1;
        }

        return ret;
    }

    /**
     * Sku单位，文件未完输出
     *
     * @param book 输出Excel文件对象
     * @param startRowIndex 开始
     * @return boolean 执行结果
     */
    private boolean writeRecordToFileForSkuNotEnd(Workbook book, int startRowIndex) {
        boolean ret = true;

        CellStyle unlock = createUnLockStyle(book);

        Sheet sheet = book.getSheetAt(0);

        Row row = row(sheet, startRowIndex);

        cell(row, 0, unlock).setCellValue(notEndPromptMessage);

        return ret;
    }

    /**
     * Model单位，文件生成
     *
     * @param params 输入参数
     * @return byte[]  下载用文件
     */
    public byte[] getModelExcelFile(Map<String,Object> params) throws IOException, InvalidFormatException {

//        String templatePath = readValue(CmsConstants.Props.CODE_TEMPLATE);
        String templatePath = "D:/jiming/work/cms/templete/model-template.xlsx";

        String cartId = (String)params.get("cartId");
        String channelId = (String)params.get("channelId");

        long recCount = selectMainGroupByCartIdRecCount(channelId, cartId);

        int pageCount = 0;
        if ((int) recCount % select_pagesize > 0) {
            pageCount =(int) recCount / select_pagesize + 1;
        } else {
            pageCount =(int) recCount / select_pagesize;
        }

        $info("准备生成 Item 文档 [ %s ]", recCount);
        $info("准备打开文档 [ %s ]", templatePath);

        try (InputStream inputStream = new FileInputStream(templatePath);
             Workbook book = WorkbookFactory.create(inputStream)) {

            for (int i = 0; i < pageCount; i++) {
//                List<CmsBtProductModel> items = cmsBtProductDao.selectModelByCartId(channelId, cartId, i, select_pagesize);
                List<CmsBtProductModel> items = selectModelByCartId(channelId, cartId, i, select_pagesize);

                if (items.size() == 0) {
                    break;
                }

                // 每页开始行
                int startRowIndex =  i * select_pagesize + 1;
                boolean isContinueOutput = writeRecordToFileForModel(book, items, cartId, startRowIndex);
                // 超过最大行的场合
                if (!isContinueOutput) {
                    break;
                }
            }

            $info("文档写入完成");

            // TODO test
            try (FileOutputStream outputFileStream = new FileOutputStream("D:\\jiming\\work\\cms\\temp\\testModel.xlsx")) {

                book.write(outputFileStream);

                outputFileStream.flush();
                outputFileStream.close();
            }

            // 返回值设定
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                book.write(outputStream);

                $info("已写入输出流");

                return outputStream.toByteArray();
            }
        }
    }

    /**
     * Model单位，文件输出
     *
     * @param book 输出Excel文件对象
     * @param items 待输出DB数据
     * @param startRowIndex 开始
     * @return boolean 是否终止输出
     */
    private boolean writeRecordToFileForModel(Workbook book, List<CmsBtProductModel> items, String cartId, int startRowIndex) {
        boolean isContinueOutput = true;

        CellStyle unlock = createUnLockStyle(book);

            /*
             * 现有表格的列:
             * 0: No
             * 1: groupId
             * 2: numiid
             * 3: Model
             * 4: Qty
             * 5: Sale_Price
             */
        Sheet sheet = book.getSheetAt(0);

        for (int i = 0; i < items.size(); i++) {

            CmsBtProductModel item = items.get(i);

            Row row = row(sheet, startRowIndex);

            // 最大行限制
            if ( startRowIndex + 1 > max_excel_reccount -1) {
                isContinueOutput = false;

                cell(row, 0, unlock).setCellValue(notEndPromptMessage);

                break;
            }

            // 内容输出
            cell(row, 0, unlock).setCellValue(startRowIndex);

            cell(row, 1, unlock).setCellValue(item.getGroups().getPlatformByCartId(Integer.valueOf(cartId)).getGroupId());

            cell(row, 2, unlock).setCellValue(item.getGroups().getPlatformByCartId(Integer.valueOf(cartId)).getNumIId());

            cell(row, 3, unlock).setCellValue(item.getFields().getModel());

            cell(row, 4, unlock).setCellValue(StringUtils.null2Space2(String.valueOf(item.getGroups().getPlatformByCartId(Integer.valueOf(cartId)).getQty())));

            cell(row, 5, unlock).setCellValue(getOutputPrice(item.getGroups().getSalePriceStart(), item.getGroups().getSalePriceEnd()));

            startRowIndex = startRowIndex + 1;
        }

        return isContinueOutput;
    }

    /**
     * 下载文件名生成（Code_YYYYMMDD_HHMMSS.dat）
     *
     */
    private String createPostFileNameForDailySales(int timeZone) {
        String localTime = DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), timeZone);
        Date localTimeForDate = DateTimeUtil.parse(localTime);

        String date = DateTimeUtil.format(localTimeForDate, DateTimeUtil.DATE_TIME_FORMAT_3);
        String time = DateTimeUtil.format(localTimeForDate, DateTimeUtil.DATE_TIME_FORMAT_4);

        return String.format(downloadCodeFileName, date, time);
    }
}
