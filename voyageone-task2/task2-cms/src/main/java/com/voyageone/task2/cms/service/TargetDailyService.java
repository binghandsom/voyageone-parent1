package com.voyageone.task2.cms.service;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Item;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ExcelUtils;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.ListUtils;
import com.voyageone.components.tmall.service.TbSaleService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sales_Sku;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/9/25.
 * @version 2.0.0
 */
@Service
public class TargetDailyService extends BaseCronTaskService {

    @Autowired
    TbSaleService tbSaleService;

    @Autowired
    ProductService productService;

    SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "TargetDailyService";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        List<Item> forShelvedList = null;
        List<Item> soldOutList = null;
        List<Item> violationOffShelfList = null;

        forShelvedList = getInventoryProduct("for_shelved");
        soldOutList = getInventoryProduct("sold_out");
        violationOffShelfList = getInventoryProduct("violation_off_shelf");

        List<TargetDailyBean> targetDailyBeans = getProductInfo(forShelvedList,"off-shelved");
        targetDailyBeans.addAll(getProductInfo(soldOutList, "sold out"));
        targetDailyBeans.addAll(getProductInfo(violationOffShelfList, "violation off shelf"));

        Map<String,TargetDailyBean> temp = new HashMap<>();
        targetDailyBeans.forEach(targetDailyBean -> temp.put(targetDailyBean.getSku(),targetDailyBean));
        targetDailyBeans=new ArrayList<TargetDailyBean>();
        final List<TargetDailyBean> finalTargetDailyBeans = targetDailyBeans;
        temp.forEach((s, targetDailyBean) -> finalTargetDailyBeans.add(targetDailyBean));

        creatDaily(targetDailyBeans);
    }

    private List<Item> getInventoryProduct(String type){
        List<Item> allList = new ArrayList<>();
        List<Item> rsList;
        Long pageNo = 1L;
        do {
            try {
                // 查询上架
                rsList = tbSaleService.getInventoryProduct(ChannelConfigEnums.Channel.TARGET.getId(), CartEnums.Cart.TG.getId(), type, pageNo++, 200L);
            } catch (ApiException apiExp) {
                $error(String.format("调用淘宝API获取上架商品时API出错 channelId=%s, cartId=%s", ChannelConfigEnums.Channel.TARGET.getId(), CartEnums.Cart.TG.getId()), apiExp);
                break;
            } catch (Exception exp) {
                $error(String.format("调用淘宝API获取上架商品时出错 channelId=%s, cartId=%s", ChannelConfigEnums.Channel.TARGET.getId(), CartEnums.Cart.TG.getId()), exp);
                break;
            }
            if (rsList != null && rsList.size() > 0) {
                allList.addAll(rsList);
            }
        } while (rsList != null && rsList.size() == 200);
        return allList;
    }

    private List<TargetDailyBean> getProductInfo(List<Item> numIids, String type) {

        List<TargetDailyBean> targetDailyBeans = new ArrayList<>();
        numIids.forEach(item -> {
            List<CmsBtProductModel> productModels = productService.getProductByNumIid("018", item.getNumIid().toString(), 23);
            productModels.forEach(cmsBtProductModel -> {
                cmsBtProductModel.getCommon().getSkus().forEach(cmsBtProductModel_sku -> {
                    TargetDailyBean targetDailyBean = new TargetDailyBean();
                    targetDailyBean.setSku(cmsBtProductModel_sku.getSkuCode());
                    targetDailyBean.setQty(cmsBtProductModel_sku.getQty());
                    targetDailyBean.setUpc(cmsBtProductModel_sku.getBarcode());
                    targetDailyBean.setTitle(item.getTitle());
                    targetDailyBean.setNumIid(item.getNumIid() + "");

                    CmsBtProductModel_Sales_Sku skuSale = getSales(targetDailyBean.getSku(),cmsBtProductModel);
                    if(skuSale != null){
                        targetDailyBean.setSales7(skuSale.getSkuSum7());
                        targetDailyBean.setSales30(skuSale.getSkuSum30());
                        targetDailyBean.setSalesAll(skuSale.getSkuSumAll());
                    }
                    targetDailyBean.setComment(type);
                    if(item.getDelistTime() != null){
                        targetDailyBean.setDelistTime(ss.format(item.getDelistTime()));
                    }
                    targetDailyBeans.add(targetDailyBean);
                });
            });
        });

        return targetDailyBeans;
    }

    private CmsBtProductModel_Sales_Sku getSales(String sku, CmsBtProductModel cmsBtProductModel){
        CmsBtProductModel_Sales_Sku salesSku = null;
        if(cmsBtProductModel.getSales() != null && !ListUtils.isNull(cmsBtProductModel.getSales().getSkus())){
            salesSku = cmsBtProductModel.getSales().getSkus()
                    .stream()
                    .filter(cmsBtProductModel_sales_sku -> cmsBtProductModel_sales_sku.getSkuCode().equalsIgnoreCase(sku))
                    .findFirst()
                    .orElse(null);
        }
        return salesSku;
    }
    private void creatDaily(List<TargetDailyBean> targetDailys) {
        String outPath = "/usr/web/contents/cms/feed_export/";
        String fileName = String.format("Daily_OffLine_Report_%s.xlsx", DateTimeUtil.getLocalTime(-6, "yyyyMMdd_HHmmss"));
        try {
            OutputStream outputStream = new FileOutputStream(outPath + fileName);
            Workbook book = new SXSSFWorkbook(1000);
            writeHead(book);
            Sheet sheet = book.getSheetAt(0);
            sheet.setColumnWidth(1, 256*73+184);
            sheet.setColumnWidth(2, 256*17+184);
            sheet.setColumnWidth(3, 256 * 17 + 184);
            sheet.setColumnWidth(8, 256 * 20 + 184);
            sheet.setColumnWidth(9, 256 * 17 + 184);
            for (int pageNum = 1; pageNum <= targetDailys.size(); pageNum++) {


                XSSFCellStyle unlock = (XSSFCellStyle) book.createCellStyle();
                unlock.setBorderBottom(CellStyle.BORDER_THIN);
                unlock.setBorderTop(CellStyle.BORDER_THIN);
                unlock.setBorderLeft(CellStyle.BORDER_THIN);
                unlock.setBorderRight(CellStyle.BORDER_THIN);

                Row row = FileUtils.row(sheet, pageNum);
                int cellIndex = 0;
                TargetDailyBean item = targetDailys.get(pageNum-1);
                ExcelUtils.setCellValue(row, cellIndex++, item.getSku(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, item.getTitle(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, item.getUpc(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, item.getNumIid(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, item.getQty(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, item.getSales7(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, item.getSales30(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, item.getSalesAll(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, item.getDelistTime(), unlock);
                ExcelUtils.setCellValue(row, cellIndex++, item.getComment(), unlock);
            }
            book.write(outputStream);
            outputStream.close();

            String time = DateTimeUtil.getLocalTime(-6,"hh:mm a M/d/YYYY");
            StringBuffer content = new StringBuffer();

            content.append("Please see the current offline report for Target Tmall store as of "+time+"(US Central Time).");
            content.append("<br><br>");
            content.append("For technical questions, please contact us at <a href=\"mailto:target-tmall-reports-support@voyageone.cn\">target-tmall-reports-support@voyageone.cn</a> ");
            content.append("<br><br>");
            content.append("Best Regards");
            content.append("<br>");
            content.append("VoyageOne Auto Report");
            List<String> files = new ArrayList<>();
            files.add(outPath + fileName);
            Mail.sendReport("TARGET_DAILY", "Daily OffLine Report" + DateTimeUtil.getLocalTime(-6, "YYYY-MM-dd HH:mm:ss"), content.toString(), files);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeHead(Workbook book) {
        String productHeadEn[] = {"SKU", "Title", "UPC", "TM_numiid", "Inventory","Last 7Days Sales" ,"Last 30Days Sales","Total sales","Delist Time(CN)","comment"};
        Sheet sheet = book.createSheet("sku");
        Row rowEn = sheet.createRow(0);

        XSSFCellStyle cellStyleEn = (XSSFCellStyle) book.createCellStyle();
        cellStyleEn.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyleEn.setBorderTop(CellStyle.BORDER_THIN);
        cellStyleEn.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyleEn.setBorderRight(CellStyle.BORDER_THIN);
        cellStyleEn.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyleEn.setFillPattern(CellStyle.SOLID_FOREGROUND);
//        rowEn.setRowStyle(cellStyle);
        for (int i = 0; i < productHeadEn.length; i++) {
            ExcelUtils.setCellValue(rowEn, i, productHeadEn[i], cellStyleEn);
        }
    }

    class TargetDailyBean {
        String sku;
        String title;
        String numIid;
        String upc;
        Integer qty;
        String comment;
        Integer sales7;
        Integer sales30;
        Integer salesAll;
        String delistTime;

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getNumIid() {
            return numIid;
        }

        public void setNumIid(String numIid) {
            this.numIid = numIid;
        }

        public Integer getQty() {
            return qty;
        }

        public void setQty(Integer qty) {
            if(qty == null) qty = 0;
            this.qty = qty;
        }

        public String getUpc() {
            return upc;
        }

        public void setUpc(String upc) {
            this.upc = upc;
        }

        public String getComment() {
            return comment;

        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Integer getSales7() {
            if(sales7 == null) sales7 = 0;
            return sales7;
        }

        public void setSales7(Integer sales7) {
            this.sales7 = sales7;
        }

        public Integer getSales30() {
            if(sales30 == null) sales30 = 0;
            return sales30;
        }

        public void setSales30(Integer sales30) {
            this.sales30 = sales30;
        }

        public Integer getSalesAll() {
            if(salesAll == null) salesAll = 0;
            return salesAll;
        }

        public void setSalesAll(Integer salesAll) {
            this.salesAll = salesAll;
        }

        public String getDelistTime() {
            return delistTime;
        }

        public void setDelistTime(String delistTime) {
            this.delistTime = delistTime;
        }
    }
}
