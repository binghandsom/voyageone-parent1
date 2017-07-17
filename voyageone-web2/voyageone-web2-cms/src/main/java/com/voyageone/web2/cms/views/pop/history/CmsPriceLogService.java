package com.voyageone.web2.cms.views.pop.history;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.product.CmsBtPriceLogService;
import com.voyageone.service.model.cms.mongo.product.CmsBtPriceLogFlatModel;
import com.voyageone.web2.base.BaseViewService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by jonasvlag on 16/7/5.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
class CmsPriceLogService extends BaseViewService {

    @Autowired
    private CmsBtPriceLogService priceLogService;

    byte[] exportExcel(String sku, String code, String cart, String channelId) {

        List<CmsBtPriceLogFlatModel> data = priceLogService.getList(sku, code, cart, channelId);

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            CreationHelper createHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet(StringUtils.isEmpty(sku) ? code : (code + " - " + sku));

            Row row = sheet.createRow(0);

            int cellIndex = 0;

            row.createCell(cellIndex++).setCellValue("ID");
            row.createCell(cellIndex++).setCellValue("CHANNEL ID");
            row.createCell(cellIndex++).setCellValue("PRODUCT ID");
            row.createCell(cellIndex++).setCellValue("CART ID");
            row.createCell(cellIndex++).setCellValue("CODE");
            row.createCell(cellIndex++).setCellValue("SKU");
            row.createCell(cellIndex++).setCellValue("MSRP PRICE");
            row.createCell(cellIndex++).setCellValue("RETAIL PRICE");
            row.createCell(cellIndex++).setCellValue("SALE PRICE");
            row.createCell(cellIndex++).setCellValue("CLIENT MSRP PRICE");
            row.createCell(cellIndex++).setCellValue("CLIENT RETAIL PRICE");
            row.createCell(cellIndex++).setCellValue("CLIENT NET PRICE");
            row.createCell(cellIndex++).setCellValue("COMMENT");
            row.createCell(cellIndex++).setCellValue("CREATED");
            row.createCell(cellIndex++).setCellValue("CREATER");
            row.createCell(cellIndex++).setCellValue("MODIFIED");
            row.createCell(cellIndex).setCellValue("MODIFIER");

            int rowIndex = 0;

            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));

            for (CmsBtPriceLogFlatModel model : data) {

                row = sheet.createRow(++rowIndex);

                cellIndex = 0;
                // id 列设置空
                row.createCell(cellIndex++).setCellValue("");
                row.createCell(cellIndex++).setCellValue(model.getChannelId());
                row.createCell(cellIndex++).setCellValue(model.getProductId());
                row.createCell(cellIndex++).setCellValue(model.getCartId());
                row.createCell(cellIndex++).setCellValue(model.getCode());
                row.createCell(cellIndex++).setCellValue(model.getSku());
                row.createCell(cellIndex++).setCellValue(model.getMsrpPrice());
                row.createCell(cellIndex++).setCellValue(model.getRetailPrice());
                row.createCell(cellIndex++).setCellValue(model.getSalePrice());
                row.createCell(cellIndex++).setCellValue(model.getClientMsrpPrice());
                row.createCell(cellIndex++).setCellValue(model.getClientRetailPrice());
                row.createCell(cellIndex++).setCellValue(model.getClientNetPrice());
                row.createCell(cellIndex++).setCellValue(model.getComment());

                Cell createdCell = row.createCell(cellIndex++);
                createdCell.setCellValue(model.getCreated());
                createdCell.setCellStyle(dateCellStyle);

                row.createCell(cellIndex++).setCellValue(model.getCreater());

                Cell modifiedCell = row.createCell(cellIndex++);
                modifiedCell.setCellValue(model.getModified());
                modifiedCell.setCellStyle(dateCellStyle);

                row.createCell(cellIndex).setCellValue(model.getModifier());
            }

            workbook.write(outputStream);

            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new BusinessException("写文档出现了错误");
        }
    }
}
