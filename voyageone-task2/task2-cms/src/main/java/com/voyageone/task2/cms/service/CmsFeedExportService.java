package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.util.*;
import com.voyageone.service.impl.cms.CmsBtExportTaskService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.CmsBtExportTaskModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.beanutils.BeanUtils;

import java.io.*;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author james.li on 2016/6/28.
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = CmsMqRoutingKey.CMS_BATCH_FeedExportJob)
public class CmsFeedExportService extends BaseMQCmsService {

    @Autowired
    FeedInfoService feedInfoService;

    @Autowired
    private CmsBtExportTaskService cmsBtExportTaskService;

    Integer pageSize = 200;

    String templatePath = CmsBtExportTaskService.templatePath;

    String outPath = CmsBtExportTaskService.savePath;

    Integer maxRowCnt = 10000;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

        $info("CmsFeedExportService start");
        $info("参数" + JacksonUtil.bean2Json(messageMap));
        CmsBtExportTaskModel cmsBtExportTaskModel = new CmsBtExportTaskModel();
        cmsBtExportTaskModel.setModified(new Date());
        cmsBtExportTaskModel.setModifier(getTaskName());
        messageMap.remove("created");
        messageMap.remove("modified");
        BeanUtils.populate(cmsBtExportTaskModel, messageMap);
        JongoQuery queryObject = new JongoQuery();
        Long cnt = 0L;
        Map<String, Object> searchValue = JacksonUtil.jsonToMap(cmsBtExportTaskModel.getParameter());
        String channelId = searchValue.get("orgChaId") == null ? cmsBtExportTaskModel.getChannelId() : searchValue.get("orgChaId").toString();

        if(searchValue.get("codeList") != null && searchValue.get("codeList") instanceof List){
            List<Map<String,Object>> codes = (List<Map<String, Object>>) searchValue.get("codeList");
            cnt = Long.valueOf(codes.size());
            List<String> codeList = codes.stream().map(stringObjectMap -> stringObjectMap.get("code").toString()).collect(Collectors.toList());
            queryObject.setQuery("{\"code\":{$in:#}}");
            queryObject.addParameters(codeList);
        }else{
            cnt = feedInfoService.getCnt(channelId, searchValue);
            queryObject.setQuery(feedInfoService.getSearchQuery(channelId, searchValue));
        }

        $info("导出的产品数" + cnt);
        List<String> files = new ArrayList<>();

        String fileName = String.format("%s-%s.xlsx", cmsBtExportTaskModel.getChannelId(), DateTimeUtil.getLocalTime(8, "yyyyMMddHHmmss"));
        files.add(fileName);

        long pageCnt = cnt / pageSize + (cnt % pageSize == 0 ? 0 : 1);

        int rowIndexCode = 2;
        int rowIndexSku = 2;
        try (OutputStream outputStream = new FileOutputStream(outPath + fileName)){
            $info(outPath + fileName);
//            InputStream inputStream = new FileInputStream(templatePath);
//            Workbook book = WorkbookFactory.create(inputStream);
            Workbook book = new SXSSFWorkbook(1000);
            writeHead(book);
            for (int pageNum = 1; pageNum <= pageCnt; pageNum++) {
                $info("导出第" + pageNum + "页");
                queryObject.setSkip((pageNum - 1) * pageSize);
                queryObject.setLimit(pageSize);
                List<CmsBtFeedInfoModel> cmsBtFeedInfoModels = feedInfoService.getList(channelId, queryObject);
                rowIndexCode = writeCode(cmsBtFeedInfoModels, book, rowIndexCode);
                $info("code写到第" + rowIndexCode + "行");
                rowIndexSku = writeSku(cmsBtFeedInfoModels, book, rowIndexSku);
                $info("sku写到第" + rowIndexSku + "行");
            }
            book.write(outputStream);
            outputStream.close();
            cmsBtExportTaskModel.setStatus(1);
            cmsBtExportTaskModel.setFileName(files.stream().collect(Collectors.joining(",")));
            cmsBtExportTaskModel.setComment("");
        }catch (Exception e){
            $error(e);
            e.printStackTrace();
            cmsBtExportTaskModel.setComment(CommonUtil.getMessages(e));
            cmsBtExportTaskModel.setStatus(2);
        }

        cmsBtExportTaskService.update(cmsBtExportTaskModel);
    }

    private void writeHead(Workbook book){
        String productHeadEn[]={"productCode","brand","feedCategory","productNameEn","shortDesEn","longDesEn","model","quantity","materialEn","color","origin","productType","sizeType","buyURL","clientMSRP","clientRetailPrice","clientCost","Images","Error Message"};
        String productHeadCn[]={"商品编码","品牌","Feed类目","产品名称英语","简短描述英语","详情描述英语","款号","库存","材质英语","颜色/口味/香型等","产地","产品分类","适用人群","品牌方商品地址","海外官方价格","海外指导价格","海外成本价格","图片地址","异常消息"};

        Sheet sheet = book.createSheet("product");
        Row rowEn = sheet.createRow(0);
        Row rowCn = sheet.createRow(1);

        XSSFCellStyle cellStyleEn = (XSSFCellStyle) book.createCellStyle();
        cellStyleEn.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyleEn.setBorderTop(CellStyle.BORDER_THIN);
        cellStyleEn.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyleEn.setBorderRight(CellStyle.BORDER_THIN);
        cellStyleEn.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyleEn.setFillPattern(CellStyle.SOLID_FOREGROUND);

        XSSFCellStyle cellStyleCn = (XSSFCellStyle) book.createCellStyle();
        cellStyleCn.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyleCn.setBorderTop(CellStyle.BORDER_THIN);
        cellStyleCn.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyleCn.setBorderRight(CellStyle.BORDER_THIN);
        cellStyleCn.setFillForegroundColor(IndexedColors.TURQUOISE.getIndex());
        cellStyleCn.setFillPattern(CellStyle.SOLID_FOREGROUND);

//        rowEn.setRowStyle(cellStyle);
        for(int i=0;i<productHeadEn.length;i++){
            ExcelUtils.setCellValue(rowEn, i, productHeadEn[i], cellStyleEn);
            ExcelUtils.setCellValue(rowCn,i,productHeadCn[i],cellStyleCn);
        }

        String skuHeadEn[]={"sku","barcode","clientSKU","brand","feedCategory","productNameEn","shortDesEn","code","model","quantity","materialEn","color","origin","productType","sizeType","buyURL","clientMSRP","clientRetailPrice","clientCost","Error Message"};
        String skuHeadCn[]={"sku","条形码","客户原始SKU","品牌","Feed类目","产品名称英语","简短描述英语","商品编码","款号","库存","材质英语","颜色/口味/香型等","产地","产品分类","适用人群","品牌方商品地址","海外官方价格","海外指导价格","海外成本价格","异常消息"};

        sheet = book.createSheet("sku");
        rowEn = sheet.createRow(0);
        rowCn = sheet.createRow(1);
        for(int i=0;i<skuHeadEn.length;i++){
            ExcelUtils.setCellValue(rowEn, i, skuHeadEn[i], cellStyleEn);
            ExcelUtils.setCellValue(rowCn,i,skuHeadCn[i],cellStyleCn);
        }


    }
    private int writeCode(List<CmsBtFeedInfoModel> cmsBtFeedInfoModels, Workbook book, int rowIndex) throws Exception{
        Sheet sheet = book.getSheetAt(0);

        XSSFCellStyle unlock = (XSSFCellStyle) book.createCellStyle();
        unlock.setBorderBottom(CellStyle.BORDER_THIN);
        unlock.setBorderTop(CellStyle.BORDER_THIN);
        unlock.setBorderLeft(CellStyle.BORDER_THIN);
        unlock.setBorderRight(CellStyle.BORDER_THIN);
        for (CmsBtFeedInfoModel item : cmsBtFeedInfoModels) {

            Row row = FileUtils.row(sheet, rowIndex);
            int cellIndex = 0;
            ExcelUtils.setCellValue(row, cellIndex++, item.getCode(), unlock);
            ExcelUtils.setCellValue(row, cellIndex++, item.getBrand(), unlock);
            ExcelUtils.setCellValue(row, cellIndex++, item.getCategory(), unlock);
            ExcelUtils.setCellValue(row, cellIndex++, item.getName(), unlock);
            ExcelUtils.setCellValue(row, cellIndex++, item.getShortDescription(), unlock);
            String longDescription = item.getLongDescription() == null ? "":item.getLongDescription();
            if(longDescription.length() > 2000) longDescription = longDescription.substring(0,2000);
            ExcelUtils.setCellValue(row, cellIndex++, longDescription, unlock);
            ExcelUtils.setCellValue(row, cellIndex++, item.getModel(), unlock);
            ExcelUtils.setCellValue(row, cellIndex++, item.getQty(), unlock);
            ExcelUtils.setCellValue(row, cellIndex++, item.getMaterial(), unlock);
            ExcelUtils.setCellValue(row, cellIndex++, item.getColor(), unlock);
            ExcelUtils.setCellValue(row, cellIndex++, item.getOrigin(), unlock);
            ExcelUtils.setCellValue(row, cellIndex++, item.getProductType(), unlock);
            ExcelUtils.setCellValue(row, cellIndex++, item.getSizeType(), unlock);
            ExcelUtils.setCellValue(row, cellIndex++, item.getClientProductURL(), unlock);

            Map<String, Object> price = priceRange(item);
            ExcelUtils.setCellValue(row, cellIndex++, price.get("ClientMSRP"), unlock);
            ExcelUtils.setCellValue(row, cellIndex++, price.get("ClientRetail"), unlock);
            ExcelUtils.setCellValue(row, cellIndex++, price.get("ClientNet"), unlock);

            String images = item.getImage().stream().collect(Collectors.joining("\n"));
            ExcelUtils.setCellValue(row, cellIndex++, images, unlock);
            ExcelUtils.setCellValue(row, cellIndex, item.getUpdMessage(), unlock);

            rowIndex++;
        }

        return rowIndex;
    }
    private int writeSku(List<CmsBtFeedInfoModel> cmsBtFeedInfoModels, Workbook book, int rowIndex) throws Exception{
        Sheet sheet = book.getSheetAt(1);

        XSSFCellStyle unlock = (XSSFCellStyle) book.createCellStyle();
        unlock.setBorderBottom(CellStyle.BORDER_THIN);
        unlock.setBorderTop(CellStyle.BORDER_THIN);
        unlock.setBorderLeft(CellStyle.BORDER_THIN);
        unlock.setBorderRight(CellStyle.BORDER_THIN);
        for (CmsBtFeedInfoModel cmsBtFeedInfoModel : cmsBtFeedInfoModels) {

            for(CmsBtFeedInfoModel_Sku item:cmsBtFeedInfoModel.getSkus()) {
                Row row = FileUtils.row(sheet, rowIndex);

                int cellIndex = 0;
                ExcelUtils.setCellValue(row, cellIndex++, item.getSku(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, item.getBarcode(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, item.getClientSku(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, cmsBtFeedInfoModel.getBrand(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, cmsBtFeedInfoModel.getCategory(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, cmsBtFeedInfoModel.getName(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, cmsBtFeedInfoModel.getShortDescription(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, cmsBtFeedInfoModel.getCode(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, cmsBtFeedInfoModel.getModel(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, item.getQty(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, cmsBtFeedInfoModel.getMaterial(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, cmsBtFeedInfoModel.getColor(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, cmsBtFeedInfoModel.getOrigin(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, cmsBtFeedInfoModel.getProductType(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, cmsBtFeedInfoModel.getSizeType(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, cmsBtFeedInfoModel.getClientProductURL(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, item.getPriceClientMsrp(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, item.getPriceClientRetail(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, item.getPriceNet(), unlock);
                ExcelUtils.setCellValue(row, cellIndex, item.getErrInfo(), unlock);
                rowIndex++;
            }
        }

        return rowIndex;
    }




    private Map<String, Object> priceRange(CmsBtFeedInfoModel item) {
        Double minClientMSRP = null;
        Double maxClientMSRP = null;
        Double minClientRetail = null;
        Double maxClientRetail = null;
        Double minClientNet = null;
        Double maxClientNet = null;
        for(CmsBtFeedInfoModel_Sku sku:item.getSkus()) {
            if(minClientMSRP == null || minClientMSRP>sku.getPriceClientMsrp()){
                minClientMSRP = sku.getPriceClientMsrp();
            }
            if(maxClientMSRP == null || maxClientMSRP<sku.getPriceClientMsrp()){
                maxClientMSRP = sku.getPriceClientMsrp();
            }

            if(minClientRetail == null || minClientRetail>sku.getPriceClientRetail()){
                minClientRetail = sku.getPriceClientRetail();
            }
            if(maxClientRetail == null || maxClientRetail<sku.getPriceClientRetail()){
                maxClientRetail = sku.getPriceClientRetail();
            }

            if(minClientNet == null || minClientNet>sku.getPriceNet()){
                minClientNet = sku.getPriceNet();
            }
            if(maxClientNet == null || maxClientNet<sku.getPriceNet()){
                maxClientNet = sku.getPriceNet();
            }
        };
        Map<String, Object> price= new HashMap<>();
        price.put("ClientMSRP",minClientMSRP.compareTo(maxClientMSRP)==0?minClientMSRP:minClientMSRP+"-"+maxClientMSRP);
        price.put("ClientRetail",minClientRetail.compareTo(maxClientRetail)==0?minClientRetail:minClientRetail+"-"+maxClientRetail);
        price.put("ClientNet",minClientNet.compareTo(maxClientNet)==0?minClientNet:minClientNet+"-"+maxClientNet);
        return price;
    }


}
