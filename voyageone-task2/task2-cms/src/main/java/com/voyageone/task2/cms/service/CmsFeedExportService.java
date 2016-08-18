package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.CmsBtExportTaskService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtExportTaskModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.beanutils.BeanUtils;

import java.io.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author james.li on 2016/6/28.
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_FeedExportJob)
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
        Map<String, Object> searchValue = JacksonUtil.jsonToMap(cmsBtExportTaskModel.getParameter());
        Long cnt = feedInfoService.getCnt(cmsBtExportTaskModel.getChannelId(), searchValue);
        $info("导出的产品数"+cnt);
        List<String> files = new ArrayList<>();

        String fileName = String.format("%s-%s.xlsx", cmsBtExportTaskModel.getChannelId(), DateTimeUtil.getLocalTime(8, "yyyyMMddHHmmss"));
        files.add(fileName);

        long pageCnt = cnt / pageSize + (cnt % pageSize == 0 ? 0 : 1);
        JongoQuery queryObject = new JongoQuery();
        queryObject.setQuery(feedInfoService.getSearchQuery(searchValue));
        int rowIndexCode = 2;
        int rowIndexSku = 2;
        try {
            OutputStream outputStream = new FileOutputStream(outPath + fileName);
            InputStream inputStream = new FileInputStream(templatePath);
            Workbook book = WorkbookFactory.create(inputStream);

            for (int pageNum = 1; pageNum <= pageCnt; pageNum++) {
                $info("导出第" + pageNum + "页");
                queryObject.setSkip((pageNum - 1) * pageSize);
                queryObject.setLimit(pageSize);
                List<CmsBtFeedInfoModel> cmsBtFeedInfoModels = feedInfoService.getList(cmsBtExportTaskModel.getChannelId(), queryObject);
                rowIndexCode = writeCode(cmsBtFeedInfoModels, book, rowIndexCode);
                $info("code写到第" + rowIndexCode + "行");
                rowIndexSku = writeSku(cmsBtFeedInfoModels, book, rowIndexSku);
                $info("sku写到第" + rowIndexSku + "行");
                if(rowIndexSku > maxRowCnt){
                    book.write(outputStream);
                    outputStream.close();
                    inputStream.close();
                    fileName = String.format("%s-%s.xlsx", cmsBtExportTaskModel.getChannelId(), DateTimeUtil.getLocalTime(8, "yyyyMMddHHmmss"));
                    files.add(fileName);
                    outputStream = new FileOutputStream(outPath + fileName);
                    inputStream = new FileInputStream(templatePath);
                    book = WorkbookFactory.create(inputStream);
                    rowIndexCode = 2;
                    rowIndexSku = 2;
                }
            }
            book.write(outputStream);
            cmsBtExportTaskModel.setStatus(1);
            cmsBtExportTaskModel.setFileName(files.stream().collect(Collectors.joining(",")));
            cmsBtExportTaskModel.setComment("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            cmsBtExportTaskModel.setComment(CommonUtil.getMessages(e));
            cmsBtExportTaskModel.setStatus(2);
        }catch (Exception e){
            e.printStackTrace();
            cmsBtExportTaskModel.setComment(CommonUtil.getMessages(e));
            cmsBtExportTaskModel.setStatus(2);
        }

        cmsBtExportTaskService.update(cmsBtExportTaskModel);
    }

    private int writeCode(List<CmsBtFeedInfoModel> cmsBtFeedInfoModels, Workbook book, int rowIndex) throws Exception{
        Sheet sheet = book.getSheetAt(0);

        Row styleRow = FileUtils.row(sheet, 2);

        CellStyle unlock = styleRow.getCell(0).getCellStyle();

        for (CmsBtFeedInfoModel item : cmsBtFeedInfoModels) {

            Row row = FileUtils.row(sheet, rowIndex);

            int cellIndex = 0;
            FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getCode());
            FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getBrand());
            FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getCategory());
            FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getName());
            FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getShortDescription());
            String longDescription = item.getLongDescription() == null ? "":item.getLongDescription();
            if(longDescription.length() > 2000) longDescription = longDescription.substring(0,2000);
            FileUtils.cell(row, cellIndex++, unlock).setCellValue(longDescription);
            FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getModel());
            FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getQty());
            FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getMaterial());
            FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getColor());
            FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getOrigin());
            FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getProductType());
            FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getSizeType());
            FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getClientProductURL());

            Map<String, Object> price = priceRange(item);
            if(price.get("ClientMSRP") instanceof Double){
                FileUtils.cell(row, cellIndex++, unlock).setCellValue((Double)price.get("ClientMSRP"));
            }else{
                FileUtils.cell(row, cellIndex++, unlock).setCellValue((String)price.get("ClientMSRP"));
            }
            if(price.get("ClientRetail") instanceof Double){
                FileUtils.cell(row, cellIndex++, unlock).setCellValue((Double)price.get("ClientRetail"));
            }else{
                FileUtils.cell(row, cellIndex++, unlock).setCellValue((String)price.get("ClientRetail"));
            }
            if(price.get("ClientNet") instanceof Double){
                FileUtils.cell(row, cellIndex++, unlock).setCellValue((Double)price.get("ClientNet"));
            }else{
                FileUtils.cell(row, cellIndex++, unlock).setCellValue((String)price.get("ClientNet"));
            }

            String images = item.getImage().stream().collect(Collectors.joining("\n"));
            FileUtils.cell(row, cellIndex++, unlock).setCellValue(images);

            rowIndex++;
        }

        return rowIndex;
    }
    private int writeSku(List<CmsBtFeedInfoModel> cmsBtFeedInfoModels, Workbook book, int rowIndex) throws Exception{
        Sheet sheet = book.getSheetAt(1);

        Row styleRow = FileUtils.row(sheet, 2);

        CellStyle unlock = styleRow.getCell(0).getCellStyle();
        for (CmsBtFeedInfoModel cmsBtFeedInfoModel : cmsBtFeedInfoModels) {

            for(CmsBtFeedInfoModel_Sku item:cmsBtFeedInfoModel.getSkus()) {
                Row row = FileUtils.row(sheet, rowIndex);

                int cellIndex = 0;
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getSku());
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getBarcode());
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getClientSku());
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(cmsBtFeedInfoModel.getBrand());
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(cmsBtFeedInfoModel.getCategory());
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(cmsBtFeedInfoModel.getName());
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(cmsBtFeedInfoModel.getShortDescription());
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(cmsBtFeedInfoModel.getCode());
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(cmsBtFeedInfoModel.getModel());
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getQty());
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(cmsBtFeedInfoModel.getMaterial());
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(cmsBtFeedInfoModel.getColor());
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(cmsBtFeedInfoModel.getOrigin());
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(cmsBtFeedInfoModel.getProductType());
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(cmsBtFeedInfoModel.getSizeType());
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(cmsBtFeedInfoModel.getClientProductURL());
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getPriceClientMsrp());
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getPriceClientRetail());
                FileUtils.cell(row, cellIndex++, unlock).setCellValue(item.getPriceNet());
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
