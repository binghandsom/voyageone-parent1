package com.voyageone.web2.cms.views.pop.history;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import com.voyageone.service.impl.cms.product.CmsBtPriceLogService;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.web2.base.BaseAppService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by jonasvlag on 16/7/5.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
class CmsPriceLogService extends BaseAppService {

    @Autowired
    private CmsBtPriceLogService priceLogService;

    byte[] exportExcel(String sku, String code, String cart, String channelId) {

        List<CmsBtPriceLogModel> data = priceLogService.getList(sku, code, cart, channelId);

        try (ByteInputStream inputStream = new ByteInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.createSheet();

            Row row = sheet.createRow(0);

            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("CHANNEL ID");
            row.createCell(1).setCellValue("PRODUCT ID");
            row.createCell(2).setCellValue("CART ID");
            row.createCell(3).setCellValue("CODE");
            row.createCell(4).setCellValue("SKU");
            row.createCell(5).setCellValue("MSRP PRICE");
            row.createCell(6).setCellValue("RETAIL PRICE");
            row.createCell(7).setCellValue("SALE PRICE");
            row.createCell(8).setCellValue("CLIENT MSRP PRICE");
            row.createCell(9).setCellValue("CLIENT RETAIL PRICE");
            row.createCell(10).setCellValue("CLIENT NET PRICE");
            row.createCell(11).setCellValue("COMMENT");
            row.createCell(12).setCellValue("CREATED");
            row.createCell(13).setCellValue("CREATER");
            row.createCell(14).setCellValue("MODIFIED");
            row.createCell(15).setCellValue("MODIFIER");

            int rowIndex = 0;

            for (CmsBtPriceLogModel model : data) {

                row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(model.getId());
                row.createCell(1).setCellValue(model.getChannelId());
                row.createCell(1).setCellValue(model.getProductId());
                row.createCell(2).setCellValue(model.getCartId());
                row.createCell(3).setCellValue(model.getCode());
                row.createCell(4).setCellValue(model.getSku());
                row.createCell(5).setCellValue(model.getMsrpPrice());
                row.createCell(6).setCellValue(model.getRetailPrice());
                row.createCell(7).setCellValue(model.getSalePrice());
                row.createCell(8).setCellValue(model.getClientMsrpPrice());
                row.createCell(9).setCellValue(model.getClientRetailPrice());
                row.createCell(10).setCellValue(model.getClientNetPrice());
                row.createCell(11).setCellValue(model.getComment());
                row.createCell(12).setCellValue(model.getCreated());
                row.createCell(13).setCellValue(model.getCreater());
                row.createCell(14).setCellValue(model.getModified());
                row.createCell(15).setCellValue(model.getModifier());
            }

            return inputStream.getBytes();

        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}
