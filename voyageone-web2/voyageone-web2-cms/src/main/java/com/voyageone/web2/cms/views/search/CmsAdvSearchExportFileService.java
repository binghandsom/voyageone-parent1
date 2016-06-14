package com.voyageone.web2.cms.views.search;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.bean.search.index.CmsSearchInfoBean2;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 15/12/14
 */
@Service
public class CmsAdvSearchExportFileService extends BaseAppService {

    @Autowired
    private ProductService productService;
    @Autowired
    private CmsAdvSearchQueryService advSearchQueryService;

    // DB检索页大小
    private final static int SELECT_PAGE_SIZE = 2000;
    // Excel 文件最大行数
    private final static int MAX_EXCEL_REC_COUNT = 10000;

    /**
     * 获取数据文件内容
     */
    public byte[] getCodeExcelFile(CmsSearchInfoBean2 searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean)
            throws IOException, InvalidFormatException {

        String templatePath = null;
        if (searchValue.getFileType() == 1) {
            templatePath = Properties.readValue(CmsProperty.Props.SEARCH_ADVANCE_EXPORT_TEMPLATE_PRODUCT);
        } else if (searchValue.getFileType() == 2) {
            templatePath = Properties.readValue(CmsProperty.Props.SEARCH_ADVANCE_EXPORT_TEMPLATE_GROUP);
        } else if (searchValue.getFileType() == 3) {
            templatePath = Properties.readValue(CmsProperty.Props.SEARCH_ADVANCE_EXPORT_TEMPLATE_SKU);
        }

        long recCount = productService.getCnt(userInfo.getSelChannelId(), advSearchQueryService.getSearchQuery(searchValue, cmsSessionBean, false));

        int pageCount;
        if ((int) recCount % SELECT_PAGE_SIZE > 0) {
            pageCount = (int) recCount / SELECT_PAGE_SIZE + 1;
        } else {
            pageCount = (int) recCount / SELECT_PAGE_SIZE;
        }

        $info("准备生成 Item 文档 [ %s ]", recCount);
        $info("准备打开文档 [ %s ]", templatePath);
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(advSearchQueryService.getSearchQuery(searchValue, cmsSessionBean, false));
        queryObject.setProjectionExt(CmsAdvanceSearchService.searchItems.concat((String) cmsSessionBean.getAttribute("_adv_search_props_searchItems")).split(";"));
        queryObject.setSort(advSearchQueryService.setSortValue(searchValue, cmsSessionBean));

        InputStream inputStream = new FileInputStream(templatePath);
        Workbook book = WorkbookFactory.create(inputStream);
        try {
            writeHead(book,cmsSessionBean);
            for (int i = 0; i < pageCount; i++) {

                queryObject.setSkip(i * SELECT_PAGE_SIZE);
                queryObject.setLimit(SELECT_PAGE_SIZE);
                List<CmsBtProductBean> items = productService.getBeanList(userInfo.getSelChannelId(), queryObject);
                if (items.size() == 0) {
                    break;
                }
                advSearchQueryService.getGroupExtraInfo(items, userInfo.getSelChannelId(), searchValue.getCartId(), false);

                // 每页开始行
                int startRowIndex = i * SELECT_PAGE_SIZE + 1;
                boolean isContinueOutput = writeRecordToFile(book, items, cmsSessionBean, startRowIndex);
                // 超过最大行的场合
                if (!isContinueOutput) {
                    break;
                }
            }
            $info("文档写入完成");

            // 返回值设定
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                book.write(outputStream);
                $info("已写入输出流");
                return outputStream.toByteArray();
            } finally {
                outputStream.close();
            }
        } finally {
            inputStream.close();
            book.close();
        }
    }

    private void  writeHead (Workbook book,CmsSessionBean cmsSession){
        List<Map<String, String>> customProps = (List<Map<String, String>>) cmsSession.getAttribute("_adv_search_customProps");
        List<Map<String, String>> commonProps = (List<Map<String, String>>) cmsSession.getAttribute("_adv_search_commonProps");
        Sheet sheet = book.getSheetAt(0);
        Row row = FileUtils.row(sheet, 0);

        CellStyle style = row.getCell(0).getCellStyle();

        int index = 16;
        if (commonProps != null) {
            for (Map<String, String> prop : commonProps) {
                FileUtils.cell(row, index++, style).setCellValue(StringUtils.null2Space2((prop.get("propName"))));
            }
        }

        if (customProps != null) {
            for (Map<String, String> prop : customProps) {
                FileUtils.cell(row, index++, style).setCellValue(StringUtils.null2Space2(prop.get("feed_prop_translation")));
                FileUtils.cell(row, index++, style).setCellValue(StringUtils.null2Space2(prop.get("feed_prop_translation")) + "(en)");
            }
        }
    }

    /**
     * Code单位，文件输出
     *
     * @param book          输出Excel文件对象
     * @param items         待输出DB数据
     * @param cmsSession    cmsSessionBean
     * @param startRowIndex 开始
     * @return boolean 是否终止输出
     */
    private boolean writeRecordToFile(Workbook book, List<CmsBtProductBean> items, CmsSessionBean cmsSession, int startRowIndex) {
        boolean isContinueOutput = true;
        List<Map<String, String>> customProps = (List<Map<String, String>>) cmsSession.getAttribute("_adv_search_customProps");
        List<Map<String, String>> commonProps = (List<Map<String, String>>) cmsSession.getAttribute("_adv_search_commonProps");
        CellStyle unlock = FileUtils.createUnLockStyle(book);

            /*
             * 现有表格的列:
             * 0: No
             * 1: productId
             * 2: num_iid
             * 3: Code
             * 4: Brand
             * 5: product_type
             * 6: size_type
             * 7: Product_Name
             * 8: Product_Name_Cn
             * 9: Qty
             * 10: msrp
             * 11: retail_price
             * 12: Sale_Price
             * 13: 类目Path
             */
        Sheet sheet = book.getSheetAt(0);

        for (CmsBtProductBean item : items) {
            Row row = FileUtils.row(sheet, startRowIndex);
            // 最大行限制
            if (startRowIndex + 1 > MAX_EXCEL_REC_COUNT - 1) {
                isContinueOutput = false;
                FileUtils.cell(row, 0, unlock).setCellValue("未完，存在未抽出数据！");
                break;
            }
            int index = 0;

            // 内容输出
            FileUtils.cell(row, index++, unlock).setCellValue(startRowIndex);
            FileUtils.cell(row, index++, unlock).setCellValue(item.getGroupBean().getGroupId());
            FileUtils.cell(row, index++, unlock).setCellValue(item.getProdId());
            FileUtils.cell(row, index++, unlock).setCellValue(item.getGroupBean().getNumIId());
            FileUtils.cell(row, index++, unlock).setCellValue(item.getFields().getCode());
            FileUtils.cell(row, index++, unlock).setCellValue(item.getFields().getBrand());
            FileUtils.cell(row, index++, unlock).setCellValue(item.getFields().getProductType());
            FileUtils.cell(row, index++, unlock).setCellValue(item.getFields().getSizeType());
            FileUtils.cell(row, index++, unlock).setCellValue(item.getFields().getProductNameEn());
            FileUtils.cell(row, index++, unlock).setCellValue(item.getFields().getLongTitle());
            FileUtils.cell(row, index++, unlock).setCellValue(StringUtils.null2Space2(String.valueOf(item.getFields().getQuantity())));
            FileUtils.cell(row, index++, unlock).setCellValue(getOutputPrice(item.getFields().getPriceMsrpSt(), item.getFields().getPriceMsrpEd()));
            FileUtils.cell(row, index++, unlock).setCellValue(getOutputPrice(item.getFields().getPriceRetailSt(), item.getFields().getPriceRetailEd()));
            FileUtils.cell(row, index++, unlock).setCellValue(getOutputPrice(item.getFields().getPriceSaleSt(), item.getFields().getPriceSaleEd()));
            FileUtils.cell(row, index++, unlock).setCellValue(item.getCatPath());
            FileUtils.cell(row, index++, unlock).setCellValue(StringUtils.null2Space2(item.getFields().getHsCodeCrop()));
            FileUtils.cell(row, index++, unlock).setCellValue(StringUtils.null2Space2(item.getFields().getHsCodePrivate()));

            if (commonProps != null) {
                for (Map<String, String> prop : commonProps) {
                    Object value = item.getFields().getAttribute(prop.get("propId"));
                    FileUtils.cell(row, index++, unlock).setCellValue(StringUtils.null2Space2(value == null ? "" : value.toString()));
                }
            }

            if (customProps != null) {
                for (Map<String, String> prop : customProps) {
                    Object value = item.getFeed().getCnAtts().getAttribute(prop.get("feed_prop_original"));
                    FileUtils.cell(row, index++, unlock).setCellValue(StringUtils.null2Space2(value == null ? "" : value.toString()));
                    value = item.getFeed().getOrgAtts().getAttribute(prop.get("feed_prop_original"));
                    FileUtils.cell(row, index++, unlock).setCellValue(StringUtils.null2Space2(value == null ? "" : value.toString()));
                }
            }

            startRowIndex = startRowIndex + 1;
        }

        return isContinueOutput;
    }

    /**
     * 金额输出
     *
     * @param strPrice 最小金额
     * @param endPrice 最大金额
     * @return String 输出金额
     */
    private String getOutputPrice(Double strPrice, Double endPrice) {
        String output = "";

        if (strPrice != null && endPrice != null) {
            if (strPrice.equals(endPrice)) {
                output = String.valueOf(strPrice);
            } else {
                output = strPrice + "～" + endPrice;
            }
        }
        return output;
    }

}
