package com.voyageone.web2.cms.views.search;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.daoext.cms.CmsBtImagesDaoExt;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.bean.search.index.CmsSearchInfoBean2;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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
    @Autowired
    private CmsAdvanceSearchService searchIndexService;
    @Autowired
    private CmsBtImagesDaoExt cmsBtImagesDaoExt;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;

    // DB检索页大小
    private final static int SELECT_PAGE_SIZE = 2000;
    // Excel 文件最大行数
    private final static int MAX_EXCEL_REC_COUNT = 10000;

    private final static String[] _DynCol = { "Numiid", "Category", "MSRP", "RetailPrice", "SalePrice" };
    private final static String[] _prodCol = { "code", "brand", "category", "productNameEn", "originalTitleCn", "model", "quantity", "color" };


    /**
     * 获取数据文件内容
     */
    public byte[] getCodeExcelFile(CmsSearchInfoBean2 searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean, String language)
            throws IOException, InvalidFormatException {

        String templatePath = null;
        if (searchValue.getFileType() == 1) {
            templatePath = Properties.readValue(CmsProperty.Props.SEARCH_ADVANCE_EXPORT_TEMPLATE_PRODUCT);
        } else if (searchValue.getFileType() == 2) {
            templatePath = Properties.readValue(CmsProperty.Props.SEARCH_ADVANCE_EXPORT_TEMPLATE_GROUP);
        } else if (searchValue.getFileType() == 3) {
            templatePath = Properties.readValue(CmsProperty.Props.SEARCH_ADVANCE_EXPORT_TEMPLATE_SKU);
        }

        // 获取product列表
        List<String> prodCodeList = searchIndexService.getProductCodeList(searchValue, userInfo, cmsSessionBean);
        long recCount = prodCodeList.size();

        if (searchValue.getFileType() == 2) {
            Integer cartId = searchValue.getCartId();
            if (cartId == null) {
                cartId = 0;
            }
            prodCodeList = searchIndexService.getGroupCodeList(prodCodeList, userInfo, searchValue, cartId);
            recCount = prodCodeList.size();
        }

        int pageCount;
        if ((int) recCount % SELECT_PAGE_SIZE > 0) {
            pageCount = (int) recCount / SELECT_PAGE_SIZE + 1;
        } else {
            pageCount = (int) recCount / SELECT_PAGE_SIZE;
        }

        $info("准备生成 Item 文档 [ %s ]", recCount);
        $info("准备打开文档 [ %s ]", templatePath);
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{'common.fields.code':{$in:#}}");
        queryObject.setParameters(prodCodeList);
        String searchItemStr = CmsAdvanceSearchService.searchItems.concat((String) cmsSessionBean.getAttribute("_adv_search_props_searchItems"));
        if (searchValue.getFileType() == 3) {
            // 要输出sku级信息
            searchItemStr += "common.skus;common.fields.model;common.fields.color;common.fields.catPath;";
        } else if (searchValue.getFileType() == 2) {
            // 要输出group级信息
            searchItemStr += "common.fields.model;common.fields.catPath;";
        } else if (searchValue.getFileType() == 1) {
            searchItemStr += "common.skus;common.fields.model;common.fields.color;common.fields.catPath;";
        }

        queryObject.setProjectionExt(searchItemStr.split(";"));
        queryObject.setSort(advSearchQueryService.setSortValue(searchValue, cmsSessionBean));

        // 店铺(cart/平台)列表
        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(userInfo.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, language);

        InputStream inputStream = new FileInputStream(templatePath);
        Workbook book = WorkbookFactory.create(inputStream);
        try {
            if (searchValue.getFileType() == 1) {
                writeHead(book, cmsSessionBean, cartList);
            } else if (searchValue.getFileType() == 2) {
                writeGroupHead(book, cartList);
            } else if (searchValue.getFileType() == 3) {
                writeSkuHead(book, cartList);
            }

            for (int i = 0; i < pageCount; i++) {
                queryObject.setSkip(i * SELECT_PAGE_SIZE);
                queryObject.setLimit(SELECT_PAGE_SIZE);
                List<CmsBtProductBean> items = productService.getBeanList(userInfo.getSelChannelId(), queryObject);
                if (items.size() == 0) {
                    break;
                }

                // 每页开始行
                int startRowIndex = i * SELECT_PAGE_SIZE + 1;
                boolean isContinueOutput = false;
                if (searchValue.getFileType() == 1) {
                    isContinueOutput = writeRecordToFile(book, items, cmsSessionBean, userInfo.getSelChannelId(), cartList, startRowIndex);
                } else if (searchValue.getFileType() == 2) {
                    isContinueOutput = writeRecordToGroupFile(book, items, userInfo.getSelChannelId(), cartList, startRowIndex);
                } else if (searchValue.getFileType() == 3) {
                    isContinueOutput = writeRecordToSkuFile(book, items, cartList, startRowIndex);
                }
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

    /**
     * code级数据下载，设置每列标题(包含动态列, 要过滤重复列)
     */
    private void writeHead(Workbook book, CmsSessionBean cmsSession, List<TypeChannelBean> cartList) {
        List<Map<String, String>> customProps = (List<Map<String, String>>) cmsSession.getAttribute("_adv_search_customProps");
        List<Map<String, String>> commonProps = (List<Map<String, String>>) cmsSession.getAttribute("_adv_search_commonProps");
        List<Map<String, String>> salesProps = (List<Map<String, String>>) cmsSession.getAttribute("_adv_search_selSalesType");
        Sheet sheet = book.getSheetAt(0);
        Row row = FileUtils.row(sheet, 0);

        CellStyle style = row.getCell(0).getCellStyle();
        // 固定列长度
        int index = 8;
        for (TypeChannelBean cartObj : cartList) {
            for (String prop : _DynCol) {
                FileUtils.cell(row, index++, style).setCellValue(cartObj.getName() + prop);
            }
        }
        FileUtils.cell(row, index++, style).setCellValue("Images");
        FileUtils.cell(row, index++, style).setCellValue("Lock");

        if (commonProps != null) {
            for (Map<String, String> prop : commonProps) {
                if (ArrayUtils.contains(_prodCol, prop.get("propId"))) {
                    continue;
                }
                FileUtils.cell(row, index++, style).setCellValue(StringUtils.null2Space2((prop.get("propName"))));
            }
        }

        if (customProps != null) {
            for (Map<String, String> prop : customProps) {
                FileUtils.cell(row, index++, style).setCellValue(StringUtils.null2Space2(prop.get("feed_prop_translation")));
                FileUtils.cell(row, index++, style).setCellValue(StringUtils.null2Space2(prop.get("feed_prop_translation")) + "(en)");
            }
        }
        if (salesProps != null) {
            for (Map<String, String> prop : salesProps) {
                FileUtils.cell(row, index++, style).setCellValue(prop.get("name"));
            }
        }
    }

    /**
     * group级数据下载，设置每列标题(包含动态列)
     */
    private void writeGroupHead(Workbook book, List<TypeChannelBean> cartList) {
        Sheet sheet = book.getSheetAt(0);
        Row row = FileUtils.row(sheet, 0);
        CellStyle style = row.getCell(0).getCellStyle();

        int index = 5;
        for (TypeChannelBean cartObj : cartList) {
            for (String prop : _DynCol) {
                FileUtils.cell(row, index++, style).setCellValue(cartObj.getName() + prop);
            }
        }
    }

    /**
     * sku级数据下载，设置每列标题(包含动态列)
     */
    private void writeSkuHead(Workbook book, List<TypeChannelBean> cartList) {
        Sheet sheet = book.getSheetAt(0);
        Row row = FileUtils.row(sheet, 0);
        CellStyle style = row.getCell(0).getCellStyle();

        int index = 13;
        for (TypeChannelBean cartObj : cartList) {
            for (String prop : _DynCol) {
                FileUtils.cell(row, index++, style).setCellValue(cartObj.getName() + prop);
            }
        }
        FileUtils.cell(row, index++, style).setCellValue("Lock");
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
    private boolean writeRecordToFile(Workbook book, List<CmsBtProductBean> items, CmsSessionBean cmsSession, String channelId, List<TypeChannelBean> cartList, int startRowIndex) {
        boolean isContinueOutput = true;
        List<Map<String, String>> customProps = (List<Map<String, String>>) cmsSession.getAttribute("_adv_search_customProps");
        List<Map<String, String>> commonProps = (List<Map<String, String>>) cmsSession.getAttribute("_adv_search_commonProps");
        List<Map<String, Object>> salesProps = (List<Map<String, Object>>) cmsSession.getAttribute("_adv_search_selSalesType");
        CellStyle unlock = FileUtils.createUnLockStyle(book);

        // 先取得各产品feed原图url
        List<String> codeList = new ArrayList<>();
        for (CmsBtProductBean item : items) {
            if (item.getCommon() == null) {
                continue;
            }
            CmsBtProductModel_Field fields = item.getCommon().getFields();
            if (fields == null) {
                continue;
            }
            codeList.add(fields.getCode());
        }
        List<Map> imgList = cmsBtImagesDaoExt.selectImagesByCode(channelId, codeList);
        codeList = null;
        Map<String, String> codeImgMap = new HashMap<>(imgList.size());
        for (Map imgItem : imgList) {
            codeImgMap.put((String) imgItem.get("code"), (String) imgItem.get("original_url"));
        }

        // 现有表格的列，请参照本工程目录下 /contents/cms/file_template/productList-template.xlsx
        Sheet sheet = book.getSheetAt(0);
        for (CmsBtProductBean item : items) {
            if (item.getCommon() == null) {
                continue;
            }
            CmsBtProductModel_Field fields = item.getCommon().getFields();
            if (fields == null) {
                continue;
            }

            Row row = FileUtils.row(sheet, startRowIndex ++);
            // 最大行限制
            if (startRowIndex + 1 > MAX_EXCEL_REC_COUNT - 1) {
                isContinueOutput = false;
                FileUtils.cell(row, 0, unlock).setCellValue("未完，存在未抽出数据！");
                break;
            }
            int index = 0;

            // 内容输出
            FileUtils.cell(row, index++, unlock).setCellValue(fields.getCode());
            FileUtils.cell(row, index++, unlock).setCellValue(fields.getBrand());
            FileUtils.cell(row, index++, unlock).setCellValue(fields.getStringAttribute("catPath"));
            FileUtils.cell(row, index++, unlock).setCellValue(fields.getProductNameEn());
            FileUtils.cell(row, index++, unlock).setCellValue(fields.getOriginalTitleCn());
            FileUtils.cell(row, index++, unlock).setCellValue(fields.getModel());
            FileUtils.cell(row, index++, unlock).setCellValue(fields.getQuantity());
            FileUtils.cell(row, index++, unlock).setCellValue(fields.getColor());

            for (TypeChannelBean cartObj : cartList) {
                CmsBtProductModel_Platform_Cart ptfObj = item.getPlatform(Integer.parseInt(cartObj.getValue()));
                if (ptfObj == null) {
                    // 没有设值时也要输出,不然就会错位
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                    continue;
                }

                FileUtils.cell(row, index++, unlock).setCellValue(ptfObj.getpNumIId());
                FileUtils.cell(row, index++, unlock).setCellValue(ptfObj.getpCatPath());
                FileUtils.cell(row, index++, unlock).setCellValue(getOutputPrice(ptfObj.getpPriceMsrpSt(), ptfObj.getpPriceMsrpEd()));
                FileUtils.cell(row, index++, unlock).setCellValue(getOutputPrice(ptfObj.getpPriceRetailSt(), ptfObj.getpPriceRetailEd()));
                FileUtils.cell(row, index++, unlock).setCellValue(getOutputPrice(ptfObj.getpPriceSaleSt(), ptfObj.getpPriceSaleEd()));
            }
            FileUtils.cell(row, index++, unlock).setCellValue(codeImgMap.get(fields.getCode()));
            FileUtils.cell(row, index++, unlock).setCellValue(item.getLock());

            if (commonProps != null) {
                for (Map<String, String> prop : commonProps) {
                    String propId = prop.get("propId");
                    if (ArrayUtils.contains(_prodCol, propId)) {
                        continue;
                    }
                    Object value = fields.getAttribute(propId);
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
            if (salesProps != null) {
                CmsBtProductModel_Sales salesData = item.getSales();
                String key = null;
                for (Map<String, Object> prop : salesProps) {
                    key = (String) prop.get("value");
                    key = key.substring(6);
                    Integer salesVal = (Integer) salesData.getSubNode(key.split("\\."));
                    if (salesVal == null) {
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                    } else {
                        FileUtils.cell(row, index++, unlock).setCellValue(salesVal);
                    }
                }
            }
        }

        return isContinueOutput;
    }

    /**
     * group单位，文件输出
     *
     * @param book          输出Excel文件对象
     * @param items         待输出DB数据
     * @param startRowIndex 开始
     * @return boolean 是否终止输出
     */
    private boolean writeRecordToGroupFile(Workbook book, List<CmsBtProductBean> items, String channelId, List<TypeChannelBean> cartList, int startRowIndex) {
        boolean isContinueOutput = true;
        CellStyle unlock = FileUtils.createUnLockStyle(book);

        // 先取得各产品grooup信息
        List<String> codeList = new ArrayList<>();
        for (CmsBtProductBean item : items) {
            if (item.getCommon() == null) {
                continue;
            }
            CmsBtProductModel_Field fields = item.getCommon().getFields();
            if (fields == null) {
                continue;
            }
            codeList.add(fields.getCode());
        }
        List<Integer> cartIdList = new ArrayList<>();
        for (TypeChannelBean cartObj : cartList) {
            cartIdList.add(NumberUtils.toInt(cartObj.getValue()));
        }

        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{'channelId':#,'mainProductCode':{$in:#},'cartId':{$in:#}}");
        queryObject.setParameters(channelId, codeList, cartIdList);
        queryObject.setProjection("{'_id':0,'cartId':1,'mainProductCode':1,'numIId':1,'priceMsrpSt':1,'priceMsrpEd':1,'priceRetailSt':1,'priceRetailEd':1,'priceSaleSt':1,'priceSaleEd':1}");
        List<CmsBtProductGroupModel> grpList = cmsBtProductGroupDao.select(queryObject, channelId);

        // 现有表格的列，请参照本工程目录下 /contents/cms/file_template/groupList-template.xlsx
        Sheet sheet = book.getSheetAt(0);
        for (CmsBtProductBean item : items) {
            if (item.getCommon() == null) {
                continue;
            }
            CmsBtProductModel_Field fields = item.getCommon().getFields();
            if (fields == null) {
                continue;
            }
            Row row = FileUtils.row(sheet, startRowIndex ++);
            // 最大行限制
            if (startRowIndex + 1 > MAX_EXCEL_REC_COUNT - 1) {
                isContinueOutput = false;
                FileUtils.cell(row, 0, unlock).setCellValue("未完，存在未抽出数据！");
                break;
            }
            int index = 0;

            // 内容输出
            FileUtils.cell(row, index++, unlock).setCellValue(fields.getModel());
            FileUtils.cell(row, index++, unlock).setCellValue(fields.getBrand());
            FileUtils.cell(row, index++, unlock).setCellValue(fields.getStringAttribute("catPath"));
            FileUtils.cell(row, index++, unlock).setCellValue(fields.getProductNameEn());
            FileUtils.cell(row, index++, unlock).setCellValue(fields.getOriginalTitleCn());

            for (TypeChannelBean cartObj : cartList) {
                CmsBtProductModel_Platform_Cart ptfObj = item.getPlatform(Integer.parseInt(cartObj.getValue()));
                if (ptfObj == null) {
                    // 没有设值时也要输出,不然就会错位
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                    continue;
                }
                CmsBtProductGroupModel grpModel = null;
                for (CmsBtProductGroupModel grpObj : grpList) {
                    if (grpObj.getMainProductCode().equals(fields.getCode()) && grpObj.getCartId().equals(Integer.parseInt(cartObj.getValue()))) {
                        grpModel = grpObj;
                        break;
                    }
                }
                if (grpModel == null) {
                    // 没有设值时也要输出,不然就会错位
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                } else {
                    FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(grpModel.getNumIId()));
                    FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(ptfObj.getpCatPath()));
                    FileUtils.cell(row, index++, unlock).setCellValue(getOutputPrice(grpModel.getPriceMsrpSt(), grpModel.getPriceMsrpEd()));
                    FileUtils.cell(row, index++, unlock).setCellValue(getOutputPrice(grpModel.getPriceRetailSt(), grpModel.getPriceRetailEd()));
                    FileUtils.cell(row, index++, unlock).setCellValue(getOutputPrice(grpModel.getPriceSaleSt(), grpModel.getPriceSaleEd()));
                }
            }
        }

        return isContinueOutput;
    }

    /**
     * sku单位，文件输出
     *
     * @param book          输出Excel文件对象
     * @param items         待输出DB数据
     * @param cartList
     * @param startRowIndex 开始
     * @return boolean 是否终止输出
     */
    private boolean writeRecordToSkuFile(Workbook book, List<CmsBtProductBean> items, List<TypeChannelBean> cartList, int startRowIndex) {
        boolean isContinueOutput = true;
        CellStyle unlock = FileUtils.createUnLockStyle(book);

        // 现有表格的列，请参照本工程目录下 /contents/cms/file_template/skuList-template.xlsx
        Sheet sheet = book.getSheetAt(0);
        for (CmsBtProductBean item : items) {
            if (item.getCommon() == null) {
                continue;
            }
            CmsBtProductModel_Field fields = item.getCommon().getFields();
            if (fields == null) {
                continue;
            }
            List<CmsBtProductModel_CommonSku> skuList = item.getCommon().getSkus();
            if (skuList == null || skuList.isEmpty()) {
                continue;
            }

            // 最大行限制
            if (startRowIndex + 1 > MAX_EXCEL_REC_COUNT - 1) {
                isContinueOutput = false;
                Row row = FileUtils.row(sheet, startRowIndex);
                FileUtils.cell(row, 0, unlock).setCellValue("未完，存在未抽出数据！");
                break;
            }
            int index = 0;

            // 内容输出
            for (CmsBtProductModel_CommonSku skuItem : skuList) {
                Row row = FileUtils.row(sheet, startRowIndex++);
                FileUtils.cell(row, index++, unlock).setCellValue(skuItem.getSkuCode());
                FileUtils.cell(row, index++, unlock).setCellValue(skuItem.getBarcode());
                FileUtils.cell(row, index++, unlock).setCellValue(skuItem.getClientSkuCode());
                FileUtils.cell(row, index++, unlock).setCellValue(fields.getBrand());
                FileUtils.cell(row, index++, unlock).setCellValue(fields.getStringAttribute("catPath"));
                FileUtils.cell(row, index++, unlock).setCellValue(fields.getModel());
                FileUtils.cell(row, index++, unlock).setCellValue(fields.getCode());
                FileUtils.cell(row, index++, unlock).setCellValue(fields.getColor());
                FileUtils.cell(row, index++, unlock).setCellValue(skuItem.getClientSize());
                FileUtils.cell(row, index++, unlock).setCellValue(skuItem.getSize());
                FileUtils.cell(row, index++, unlock).setCellValue(skuItem.getClientMsrpPrice());
                FileUtils.cell(row, index++, unlock).setCellValue(skuItem.getClientRetailPrice());
                FileUtils.cell(row, index++, unlock).setCellValue(skuItem.getClientNetPrice());

                for (TypeChannelBean cartObj : cartList) {
                    CmsBtProductModel_Platform_Cart ptfObj = item.getPlatform(Integer.parseInt(cartObj.getValue()));
                    if (ptfObj == null) {
                        // 没有设值时也要输出,不然就会错位
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                        continue;
                    }
                    List<BaseMongoMap<String, Object>> innerSkus = ptfObj.getSkus();
                    if (innerSkus == null) {
                        // 没有设值时也要输出,不然就会错位
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                        continue;
                    }
                    for (BaseMongoMap prop : innerSkus) {
                        if (skuItem.getSkuCode().equals(prop.getStringAttribute("skuCode"))) {
                            FileUtils.cell(row, index++, unlock).setCellValue(ptfObj.getpNumIId());
                            FileUtils.cell(row, index++, unlock).setCellValue(ptfObj.getpCatPath());
                            FileUtils.cell(row, index++, unlock).setCellValue(prop.getStringAttribute("priceMsrp"));
                            FileUtils.cell(row, index++, unlock).setCellValue(prop.getStringAttribute("priceRetail"));
                            FileUtils.cell(row, index++, unlock).setCellValue(prop.getStringAttribute("priceSale"));
                        }
                    }
                }
                FileUtils.cell(row, index++, unlock).setCellValue(item.getLock());
            }
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
