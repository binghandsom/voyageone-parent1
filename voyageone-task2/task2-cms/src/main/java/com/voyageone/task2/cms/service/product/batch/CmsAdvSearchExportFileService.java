package com.voyageone.task2.cms.service.product.batch;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.*;
import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.CmsBtExportTaskService;
import com.voyageone.service.impl.cms.ImagesService;
import com.voyageone.service.impl.cms.PlatformService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.search.CmsAdvSearchQueryService;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchExportMQMessageBody;
import com.voyageone.service.model.cms.CmsBtExportTaskModel;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author JiangJusheng
 * @version 2.0.0, 2016/08/18
 */
@Service
public class CmsAdvSearchExportFileService extends BaseService {

    // excel cell的内容长度限制
    private final static int CELL_LENGTH_LIMIT = 2000;
    // DB检索页大小
    private final static int SELECT_PAGE_SIZE = 100;

    /*group级导出时和平台无关的固定列：英文和中文列头名称*/
    private final static String[] _GROUP_STATIC_COLS = {"model", "mainCode", "productNameEn", "originalTitleCn", "supplier", "category", "brand", "feedCategory"};
    private final static String[] _GROUP_STATIC_COLS_ZN = {"款号", "主商品编码", "产品名称英语", "产品名称中文", "供应商", "主类目", "品牌", "feed类目"};

    //common.fields.origSizeType
    /*code级导出时和平台无关的固定列：英文和中文列头名称*/
    private final static String[] _CODE_STATIC_COLS = {"code", "productNameEn", "originalTitleCn", "supplier", "category", "brand", "skuCounts", "quantity", "freeTags", "feedCategory"};
    private final static String[] _CODE_STATIC_COLS_ZN = {"商品编码", "产品名称英语", "产品名称中文", "供应商", "主类目", "品牌", "SKU数", "库存", "自由标签", "feed类目"};

    /*sku级导出时和平台无关的固定列：英文和中文列头名称*/
    private final static String[] _SKU_STATIC_COLS = {"code", "barcode", "clientSKU", "productNameEn", "originalTitleCn", "clientSKU", "clientSize", "size", "inventory", "clientPriceMsrp", "clientPriceRetail", "clientPriceCost", "weightCalc"};
    private final static String[] _SKU_STATIC_COLS_ZN = {"sku", "条形码", "商品编码", "产品名称英语", "产品名称中文", "客户原始SKU", "客户原始Size", "转换后Size", "库存", "客户建议售价", "客户指导价", "客户成本价", "重量（lb）"};

    // 产品数据（code级）固定输出列，用于过滤自定义显示列中相同项目
    private final static String[] _prodCol = {"code", "brand", "category", "productNameEn", "originalTitleCn", "mainCode", "model", "quantity", "color"};
    /*聚美上新SKU导出列*/
    private final static String[] _shoemetroColJMSKU = {"Child SKU", "Brand", "Parent SKU", "Color", "Size", "VO Price", "Final RMB Price", "URL Link", "Inventory"};
    /*报备数据导出文件列*/
    private final static String[] _filingSkuCol = {"SKU", "Code", "model", "SIZE", "欧码", "英文标题", "中文标题", "产品图片链接", "性别", "材质", "产地", "颜色", "品牌", "重量", "UPC", "英文描述", "中文描述", "类目", "HSCode", "HSCodePU", "Price (RMB)"};
    private static Map<String, String> lockStatusMap = null;
    @Autowired
    private ProductService productService;
    @Autowired
    private CmsAdvSearchQueryService advSearchQueryService;
    @Autowired
    private ImagesService imagesService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private PlatformService platformService;
    @Autowired
    private CmsBtExportTaskService cmsBtExportTaskService;
    @Autowired
    private TagService tagService;

    public List<CmsBtOperationLogModel_Msg> export(AdvSearchExportMQMessageBody messageBody) throws Exception {
        $debug("高级检索 文件下载任务 param=" + JacksonUtil.bean2Json(messageBody));
        CmsBtExportTaskModel taskModel = cmsBtExportTaskService.getExportById(messageBody.getCmsBtExportTaskId());
        Map<String, Object> messageMap = messageBody.getSearchValue();
        Map<String, String> channelIdMap = messageBody.getChannelIdMap();
        CmsSearchInfoBean2 searchValue;
        try {
            searchValue = JacksonUtil.json2Bean(JacksonUtil.bean2Json(messageMap), CmsSearchInfoBean2.class);
        } catch (Exception exp) {
            $error("高级检索 文件下载任务 查询参数不正确", exp);
            // 更新任务状态，然后结束
            taskModel.setStatus(2);
            taskModel.setComment("查询参数不正确");
            taskModel.setModified(new Date());
            cmsBtExportTaskService.update(taskModel);
            /*return;*/
            throw new BusinessException("高级检索 文件下载任务 查询参数不正确", exp);
        }

        $debug("高级检索 文件下载任务 开始执行...");
        String channleId = (String) messageMap.get("_channleId");
        String language = (String) messageMap.get("_language");
        String userName = (String) messageMap.get("_userName");
        Map<String, Object> sessionBean = (Map<String, Object>) messageMap.get("_sessionBean");
        if (channleId == null || language == null || userName == null || sessionBean == null) {
            $error("高级检索 文件下载任务  缺少参数");
            taskModel.setStatus(2);
            taskModel.setComment("缺少参数");
            taskModel.setModified(new Date());
            cmsBtExportTaskService.update(taskModel);
            /*return;*/
            throw new BusinessException(String.format("高级检索 文件下载任务 缺少参数, channelId=%s, language=%s, userName=%s, sesseionBean=%s", channleId, language, userName, JacksonUtil.bean2Json(sessionBean)));
        }

        /**
         * 不满足条件而未导出的记录，key-value分别对应 code/skuCode/group-未提出提示信息
         * 开始生成excel
         * */
        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();
        try {
            String fileName = createExcelFile(searchValue, (List<String>) messageMap.get("_selCodeList"), channleId, sessionBean, userName, language, failList, channelIdMap);
            taskModel.setFileName(fileName);
            taskModel.setStatus(1); // 成功
        } catch (Throwable exp) {
            $error("高级检索 文件下载任务 创建文件时出错 " + exp.getMessage(), exp);
            // 更新任务状态，然后结束
            taskModel.setStatus(2);
            taskModel.setComment("创建文件时出错 " + exp.getMessage());
            taskModel.setModified(new Date());
            cmsBtExportTaskService.update(taskModel);
            throw new BusinessException("高级检索 文件下载任务 创建文件时出错", exp);
        }
        taskModel.setModified(new Date());
        cmsBtExportTaskService.update(taskModel);
        return failList;
    }

    /**
     * 获取数据文件内容
     */
    private String createExcelFile(CmsSearchInfoBean2 searchValue, List<String> codeList, String channelId, Map<String
            , Object> cmsSessionBean, String userName, String language, List<CmsBtOperationLogModel_Msg> failList
            , Map<String, String> channelIdMap)
            throws IOException, InvalidFormatException {
        String fileName = null;

        switch (searchValue.getFileType()) {
            case 1:
                fileName = "productList_";
                break;
            case 2:
                fileName = "groupList_";
                break;
            case 3:
                fileName = "skuList_";
                break;
            case 4:
                fileName = "publishJMSkuList_";
                break;
            case 5:
                fileName = "filingList_";
                break;
        }

        String exportPath = Properties.readValue(CmsProperty.Props.SEARCH_ADVANCE_EXPORT_PATH);
        File pathFileObj = new File(exportPath);
        if (!pathFileObj.exists()) {
            $info("高级检索 文件下载任务 文件目录不存在 " + exportPath);
            if (!pathFileObj.mkdirs()) {
                $error("高级检索 文件下载任务 创建文件目录失败 " + exportPath);
                throw new BusinessException("创建文件目录失败 " + exportPath);
            }
        }

        // 获取product列表
        List<String> prodCodeList;
        if (searchValue.getFileType() == 2) {
            if (codeList == null || codeList.isEmpty()) {
                prodCodeList = advSearchQueryService.getGroupCodeList(searchValue, channelId);
            } else {
                $debug("仅导出选中的记录,再查询group");
                // 取得该店铺的所有平台
                List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");
                if (cartList == null || cartList.isEmpty()) {
                    $error("高级检索 文件下载任务 本店铺无平台数据！ channelId=" + channelId);
                    throw new BusinessException("本店铺无平台数据");
                }
                List<Integer> cartIdList = cartList.stream().map(tmItem -> NumberUtils.toInt(tmItem.getValue())).collect(Collectors.toList());

                // 找出选中商品的主商品
                JongoQuery grpQuyObject = new JongoQuery();
                grpQuyObject.setQuery("{'cartId':{$in:#},'productCodes':{$in:#}}");
                grpQuyObject.setParameters(cartIdList, codeList);
                grpQuyObject.setProjectionExt("mainProductCode");
                List<CmsBtProductGroupModel> grpList = productGroupService.getList(channelId, grpQuyObject);
                prodCodeList = grpList.stream().map(tmItem -> tmItem.getMainProductCode()).distinct().collect(Collectors.toList());
            }
        } else {
            if (codeList == null || codeList.isEmpty()) {
                prodCodeList = advSearchQueryService.getProductCodeList(searchValue, channelId, false, searchValue.getFileType());
            } else {
                $debug("仅导出选中的记录");
                prodCodeList = codeList;
            }
        }
        if (prodCodeList == null) {
            prodCodeList = new ArrayList<>(0);
        }
        long recCount = prodCodeList.size();

        int pageCount;
        if ((int) recCount % SELECT_PAGE_SIZE > 0) {
            pageCount = (int) recCount / SELECT_PAGE_SIZE + 1;
        } else {
            pageCount = (int) recCount / SELECT_PAGE_SIZE;
        }

        $info("准备生成 Item 文档 [ %s ]", recCount);

        JongoQuery queryObject = new JongoQuery();
        queryObject.setQuery("{'common.fields.code':{$in:#}}");
        queryObject.setParameters(prodCodeList);
//        String searchItemStr = CmsAdvSearchQueryService.searchItems;
//        if (cmsSessionBean.get("_adv_search_props_searchItems") != null) {
//            searchItemStr += (String) cmsSessionBean.get("_adv_search_props_searchItems");
//        }
//        if (!searchItemStr.endsWith(";"))
//            searchItemStr += ";";
//        if (searchValue.getFileType() == 3) {
//            // 要输出sku级信息
//            searchItemStr += "common.skus;common.fields.model;common.fields.color;feed.catPath;common.fields.origSizeType;common.fields.originalCode;";
//        } else if (searchValue.getFileType() == 2) {
//            // 要输出group级信息
//            searchItemStr += "common.fields.model;feed.catPath;common.fields.origSizeType;";
//        } else if (searchValue.getFileType() == 1) {
//            //code
//            searchItemStr += "common.fields.model;common.fields.color;feed.catPath;common.fields.origSizeType;";
//        } else if (searchValue.getFileType() == 4) {
//            searchItemStr += "common.skus.clientNetPrice;common.fields.color;common.fields.originalCode;platforms";
//        } else if (searchValue.getFileType() == 5) {
//            searchItemStr += "common.skus;common.fields.model;common.fields.isFiled;common.fields.hsCodeCross;common.fields.origin;common.fields.color;" +
//                    "common.fields.weightKG;common.fields.shortDesEn;common.fields.shortDesCn;common.fields.materialCn;common.fields.materialEn";
//        }
//
//        queryObject.setProjectionExt(searchItemStr.split(";"));

        // 店铺(cart/平台)列表
        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, language);
        if (lockStatusMap == null) {
            List<TypeBean> lockStatusList = TypeConfigEnums.MastType.procLockStatus.getList(language);
            lockStatusMap = lockStatusList.stream().collect(Collectors.toMap((p) -> p.getValue(), (p) -> p.getName()));
        }

        Workbook book = new SXSSFWorkbook(1000);

        /**开始写表头*/
        try {
            switch (searchValue.getFileType()) {
                case 1:
                    writeCodeHead(book, cmsSessionBean, cartList);
                    break;
                case 2:
                    writeGroupHead(book, cartList, cmsSessionBean);
                    break;
                case 3:
                    writeSkuHead(book, cartList, cmsSessionBean);
                    break;
                case 4:
                    writePublishJMSkuHead(book);
                    break;
                case 5:
                    writeFilingHead(book);
                    break;
            }

            Map<String, TypeChannelBean> productTypes = TypeChannels.getTypeMapWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, channelId, "cn");
            Map<String, TypeChannelBean> sizeTypes = TypeChannels.getTypeMapWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, channelId, "cn");

            // SKU导出时，startRowIndex可能行数会增加，因为一个code可有有多个sku
            int offset = 0;
            for (int i = 0; i < pageCount; i++) {
                $info(String.format("%d/%d", i + 1, pageCount));
                queryObject.setSkip(i * SELECT_PAGE_SIZE);
                queryObject.setLimit(SELECT_PAGE_SIZE);
                List<CmsBtProductBean> items = productService.getBeanList(channelId, queryObject);
                if (items.size() == 0) {
                    break;
                }
                items.forEach(cmsBtProductBean -> {
                    String productType = cmsBtProductBean.getCommon().getFields().getProductType();
                    if (!StringUtil.isEmpty(productType)) {
                        TypeChannelBean temp = productTypes.get(productType);
                        if (temp != null) {
                            cmsBtProductBean.getCommon().getFields().setProductTypeCn(temp.getName());
                        }
                    }

                    String sizeType = cmsBtProductBean.getCommon().getFields().getSizeType();
                    if (!StringUtil.isEmpty(sizeType)) {
                        TypeChannelBean temp = sizeTypes.get(sizeType);
                        if (temp != null) {
                            cmsBtProductBean.getCommon().getFields().setSizeTypeCn(temp.getName());
                        }
                    }
                });

                /**每页开始行   开始写excel*/
                int startRowIndex = i * SELECT_PAGE_SIZE + ((searchValue.getFileType() == 4 || searchValue.getFileType() == 5) ? 1 : 2);
                switch (searchValue.getFileType()) {
                    case 1:
                        writeRecordToFile(book, items, cmsSessionBean, channelId, cartList, startRowIndex, channelIdMap);
                        break;
                    case 2:
                        writeRecordToGroupFile(book, items, channelId, cartList, startRowIndex, channelIdMap, cmsSessionBean);
                        break;
                    case 3:
                        /**isContinueOutput暂时无用*/
                        offset += writeRecordToSkuFile(book, items, cartList, startRowIndex + offset, channelIdMap, cmsSessionBean);
                        break;
                    case 4:
                        /**isContinueOutput暂时无用*/
                        offset += writePublishJMSkuFile(book, items, startRowIndex + offset, failList);
                        break;
                    case 5:
                        offset += writeFilingToFile(book, items, startRowIndex + offset, failList);
                        break;
                }

            }

            $info("文档写入完成");

            // 返回值设定
            fileName += userName + "_" + DateTimeUtil.format(DateTimeUtilBeijing.getCurrentBeiJingDate(), DateTimeUtil.DEFAULT_DATETIME_FORMAT_1) + ".xlsx";
            OutputStream outputStream = new FileOutputStream(exportPath + fileName);
            try {
                book.write(outputStream);
                $info("已写入输出流");
            } finally {
                outputStream.close();
            }
        } catch (Exception e) {
            $error(CommonUtil.getMessages(e));
            $error(e);
        } finally {
            book.close();
        }
        return fileName;
    }

    /**
     * code级数据下载，设置每列标题(包含动态列, 要过滤重复列)
     */
    private void writeCodeHead(Workbook book, Map cmsSession, List<TypeChannelBean> cartList) {
        book.createSheet("code");
        Sheet sheet = book.getSheetAt(0);
        Row row1 = FileUtils.row(sheet, 0); // 第一行英文标题
        Row row2 = FileUtils.row(sheet, 1); // 第二行中文标题
        CellStyle style1 = book.createCellStyle();
        CellStyle style2 = book.createCellStyle();
        this.setHeadCellStyle(style1, "en");
        this.setHeadCellStyle(style2, "cn");
        //code级导除，非平台固定列表头
        int size = _CODE_STATIC_COLS.length;
        for (int i = 0; i < size; i++) {
            FileUtils.cell(row1, i, style1).setCellValue(_CODE_STATIC_COLS[i]);
            FileUtils.cell(row2, i, style2).setCellValue(_CODE_STATIC_COLS_ZN[i]);
        }
        List<Map<String, String>> customProps = (List<Map<String, String>>) cmsSession.get("_adv_search_customProps");
        List<Map<String, String>> commonProps = (List<Map<String, String>>) cmsSession.get("_adv_search_commonProps");
        List<Map<String, String>> salesProps = (List<Map<String, String>>) cmsSession.get("_adv_search_selSalesType");
        List<Map<String, String>> bidatasProps = (List<Map<String, String>>) cmsSession.get("_adv_search_selBiDataList");
        List<Map<String, String>> platformDataList = (List<Map<String, String>>) cmsSession.get("_adv_search_selPlatformDataList");

        // 固定列长度
        int index = size;
        FileUtils.cell(row1, index++, style1).setCellValue("Lock");

        if (commonProps != null) {
            for (Map<String, String> prop : commonProps) {
                if (ArrayUtils.contains(_prodCol, prop.get("propId"))) {
                    continue;
                }
                FileUtils.cell(row1, index++, style1).setCellValue(StringUtils.null2Space2((prop.get("propName"))));
            }
        }
        if (customProps != null) {
            for (Map<String, String> prop : customProps) {
                FileUtils.cell(row1, index++, style1).setCellValue(StringUtils.null2Space2(prop.get("feed_prop_translation")));
                FileUtils.cell(row1, index++, style1).setCellValue(StringUtils.null2Space2(prop.get("feed_prop_original")));
            }
        }
        if (salesProps != null) {
            for (Map<String, String> prop : salesProps) {
                FileUtils.cell(row1, index++, style1).setCellValue(prop.get("name"));
            }
        }
        if (bidatasProps != null) {
            for (Map<String, String> prop : bidatasProps) {
                FileUtils.cell(row1, index++, style1).setCellValue(prop.get("name"));
            }
        }
        if (platformDataList != null) {
            for (Map<String, String> prop : platformDataList) {
                FileUtils.cell(row1, index++, style1).setCellValue(prop.get("name"));
            }
        }
        // 固定列长度
        index = size;
        FileUtils.cell(row2, index++, style2).setCellValue("是否被锁定");

        if (commonProps != null) {
            for (Map<String, String> prop : commonProps) {
                if (ArrayUtils.contains(_prodCol, prop.get("propId"))) {
                    continue;
                }
                FileUtils.cell(row2, index++, style2).setCellValue(StringUtils.null2Space2((prop.get("propName"))));
            }
        }
        if (customProps != null) {
            for (Map<String, String> prop : customProps) {
                FileUtils.cell(row2, index++, style2).setCellValue(StringUtils.null2Space2(prop.get("feed_prop_translation")));
                FileUtils.cell(row2, index++, style2).setCellValue(StringUtils.null2Space2(prop.get("feed_prop_original")));
            }
        }
        if (salesProps != null) {
            for (Map<String, String> prop : salesProps) {
                FileUtils.cell(row2, index++, style2).setCellValue(prop.get("name"));
            }
        }
        if (bidatasProps != null) {
            for (Map<String, String> prop : bidatasProps) {
                FileUtils.cell(row2, index++, style2).setCellValue(prop.get("name"));
            }
        }
        if (platformDataList != null) {
            for (Map<String, String> prop : platformDataList) {
                if(prop.get("name").indexOf("是否销售")>-0) continue;
                FileUtils.cell(row2, index++, style2).setCellValue(prop.get("name"));
            }
        }
    }

    /**
     * group级数据下载，设置每列标题(包含动态列)
     */
    private void writeGroupHead(Workbook book, List<TypeChannelBean> cartList, Map cmsSession) {
        book.createSheet("group");
        Sheet sheet = book.getSheetAt(0);
        Row row1 = FileUtils.row(sheet, 0); // 第一行，英文标题
        Row row2 = FileUtils.row(sheet, 1); // 第二行，中文标题
        CellStyle style1 = book.createCellStyle();
        CellStyle style2 = book.createCellStyle();
        this.setHeadCellStyle(style1, "en");
        this.setHeadCellStyle(style2, "cn");
        int size = _GROUP_STATIC_COLS.length;
        for (int i = 0; i < size; i++) {
            FileUtils.cell(row1, i, style1).setCellValue(_GROUP_STATIC_COLS[i]);
            FileUtils.cell(row2, i, style2).setCellValue(_GROUP_STATIC_COLS_ZN[i]);
        }

        List<Map<String, String>> platformDataList = (List<Map<String, String>>) cmsSession.get("_adv_search_selPlatformDataList");

        int index = size;
        if (platformDataList != null) {
            for (Map<String, String> prop : platformDataList) {
                FileUtils.cell(row1, index++, style1).setCellValue(prop.get("name"));

            }
        }
        index = size;
        if (platformDataList != null) {
            for (Map<String, String> prop : platformDataList) {
                if(prop.get("name").indexOf("可售库存")>-0) continue;
                if(prop.get("name").indexOf("是否销售")>-0) continue;
                FileUtils.cell(row2, index++, style2).setCellValue(prop.get("name"));
            }
        }

    }

    /**
     * sku级数据下载，设置每列标题(包含动态列)
     */
    private void writeSkuHead(Workbook book, List<TypeChannelBean> cartList, Map cmsSession) {
        book.createSheet("sku");
        Sheet sheet = book.getSheetAt(0);
        Row row1 = FileUtils.row(sheet, 0); // 第一行，英文标题
        Row row2 = FileUtils.row(sheet, 1); // 第二行，中文标题
        CellStyle style1 = book.createCellStyle();
        CellStyle style2 = book.createCellStyle();
        this.setHeadCellStyle(style1, "en");
        this.setHeadCellStyle(style2, "cn");
        int size = _SKU_STATIC_COLS.length;
        for (int i = 0; i < size; i++) {
            FileUtils.cell(row1, i, style1).setCellValue(_SKU_STATIC_COLS[i]);
            FileUtils.cell(row2, i, style2).setCellValue(_SKU_STATIC_COLS_ZN[i]);
        }

        List<Map<String, String>> platformDataList = (List<Map<String, String>>) cmsSession.get("_adv_search_selPlatformDataList");

        int index = size;
        if (platformDataList != null) {
            for (Map<String, String> prop : platformDataList) {
                FileUtils.cell(row1, index++, style1).setCellValue(prop.get("name"));

            }
        }

        FileUtils.cell(row1, index++, style1).setCellValue("Lock");
        index = size;

        if (platformDataList != null) {
            for (Map<String, String> prop : platformDataList) {
                if(prop.get("name").indexOf("可售库存")>-0) continue;
                FileUtils.cell(row2, index++, style2).setCellValue(prop.get("name"));
            }
        }
        FileUtils.cell(row2, index++, style2).setCellValue("是否被锁定");
    }

    /**
     * 聚美上新SKU数据导出
     *
     * @param book
     */
    private void writePublishJMSkuHead(Workbook book) {
        book.createSheet("jmSkuList");
        Sheet sheet = book.getSheetAt(0);
        Row row = FileUtils.row(sheet, 0); // 第一行，英文标题
        CellStyle style = book.createCellStyle();
        this.setHeadCellStyle(style, "en");
        int size = _shoemetroColJMSKU.length;
        for (int i = 0; i < size; i++) {
            FileUtils.cell(row, i, style).setCellValue(_shoemetroColJMSKU[i]);
        }
    }

    /**
     * 生成报备文件文档头部
     *
     * @param workbook
     */
    public void writeFilingHead(Workbook workbook) {
        workbook.createSheet("filing");
        Sheet sheet = workbook.getSheetAt(0);
        Row row = FileUtils.row(sheet, 0);
        CellStyle style = workbook.createCellStyle();
        this.setHeadCellStyle(style, "en");
        int size = _filingSkuCol.length;
        for (int i = 0; i < size; i++) {
            FileUtils.cell(row, i, style).setCellValue(_filingSkuCol[i]);
        }
    }

    /**
     * 设置excel中英文title样式
     *
     * @param style
     * @param lang
     */
    private void setHeadCellStyle(CellStyle style, String lang) {
        if (style != null && org.apache.commons.lang.StringUtils.isNotBlank(lang)) {
            if ("en".equalsIgnoreCase(lang)) {
                style.setBorderBottom(CellStyle.BORDER_THIN);
                style.setBorderTop(CellStyle.BORDER_THIN);
                style.setBorderLeft(CellStyle.BORDER_THIN);
                style.setBorderRight(CellStyle.BORDER_THIN);
                style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            } else if ("cn".equalsIgnoreCase(lang)) {
                style.setBorderBottom(CellStyle.BORDER_THIN);
                style.setBorderTop(CellStyle.BORDER_THIN);
                style.setBorderLeft(CellStyle.BORDER_THIN);
                style.setBorderRight(CellStyle.BORDER_THIN);
                style.setFillForegroundColor(IndexedColors.TURQUOISE.getIndex());
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
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
     * @param channelIdMap
     * @return boolean 是否终止输出
     */
    private boolean writeRecordToFile(Workbook book, List<CmsBtProductBean> items, Map cmsSession, String channelId, List<TypeChannelBean> cartList, int startRowIndex, Map<String, String> channelIdMap) {
        boolean isContinueOutput = true;
        List<Map<String, String>> customProps = (List<Map<String, String>>) cmsSession.get("_adv_search_customProps");
        List<Map<String, String>> commonProps = (List<Map<String, String>>) cmsSession.get("_adv_search_commonProps");
        List<Map<String, Object>> salesProps = (List<Map<String, Object>>) cmsSession.get("_adv_search_selSalesType");
        List<Map<String, Object>> bidatasProps = (List<Map<String, Object>>) cmsSession.get("_adv_search_selBiDataList");
        List<Map<String, String>> platformDataList = (List<Map<String, String>>) cmsSession.get("_adv_search_selPlatformDataList");

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
        List<Map> imgList = imagesService.getImagesByCode(channelId, codeList);

        Map<String, Object> codeImgMap = new HashMap<>(imgList.size());
        for (Map imgItem : imgList) {
            String prodCode = (String) imgItem.get("code");
            String imgUrl = (String) imgItem.get("original_url");
            if (imgUrl.indexOf("http") != 0) {
                continue;
            }

            Integer imgCnt = (Integer) codeImgMap.get(prodCode + "_img_cnt");
            String urlTxt = (String) codeImgMap.get(prodCode);
            if (urlTxt != null) {
                imgCnt++;
                urlTxt = urlTxt + "\n" + imgUrl;
            } else {
                imgCnt = 0;
                urlTxt = (String) imgItem.get("original_url");
            }
            codeImgMap.put(prodCode, urlTxt);
            codeImgMap.put(prodCode + "_img_cnt", imgCnt);
        }

        // 现有表格的列，请参照本工程目录下 /contents/cms/file_template/productList-template.xlsx
        Sheet sheet = book.getSheetAt(0);
        CellStyle cs = book.createCellStyle();
        cs.setWrapText(true);
        int nowIdx = 0;
        Map<String, CmsBtTagBean> cachTag = new HashMap<>();
        for (CmsBtProductBean item : items) {
            if (item.getCommon() == null) {
                continue;
            }
            CmsBtProductModel_Field fields = item.getCommon().getFields();
            if (fields == null) {
                continue;
            }
            Row row = FileUtils.row(sheet, startRowIndex++);
            int index = 0;
            // 内容输出
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getCode()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getProductNameEn()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getOriginalTitleCn()));
            FileUtils.cell(row, index++, unlock).setCellValue(channelIdMap.get(item.getOrgChannelId()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(item.getCommon().getCatPath()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getBrand()));
            FileUtils.cell(row, index++, unlock).setCellValue(item.getCommon().getSkus().size());
            //sku取得库存
            Map<String, Integer> codesMap = new HashMap<>();
            item.getCommon().getSkus().forEach(sku->codesMap.put(sku.getSkuCode(), sku.getQty()));
            FileUtils.cell(row, index++, unlock).setCellValue(item.getCommon().getFieldsNotNull().getQuantity());

            // 取得自由标签
            List<CmsBtTagBean> tagModelList = new ArrayList<>();
            List<String> temp = new ArrayList<>();
            for (String tag : item.getFreeTags()) {
                if (cachTag.containsKey(tag)) {
                    tagModelList.add(cachTag.get(tag));
                } else {
                    temp.add(tag);
                }
            }
            if (temp.size() > 0) {
                List<CmsBtTagBean> ts = tagService.getTagPathNameByTagPath(channelId, temp);
                if (!ListUtils.isNull(ts)) {
                    for (CmsBtTagBean cmsBtTagBean : ts) {
                        cachTag.put(cmsBtTagBean.getTagPath(), cmsBtTagBean);
                        tagModelList.add(cmsBtTagBean);
                    }
                }
            }
            String tag = "";
            if (!ListUtils.isNull(tagModelList)) {
                tag = tagModelList.stream().map(CmsBtTagBean::getTagChildrenName).collect(Collectors.joining(","));
            }
            FileUtils.cell(row, index++, unlock).setCellValue(tag);
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(item.getFeed().getCatPath()));
            //平台锁状态
            FileUtils.cell(row, index++, unlock).setCellValue(getLockStatusTxt(item.getLock()));

            if (commonProps != null) {
                for (Map<String, String> prop : commonProps) {
                    String propId = prop.get("propId");
                    if (ArrayUtils.contains(_prodCol, propId)) {
                        continue;
                    }
                    if ("comment".equals(propId)) {
                        Object value = item.getCommon().getComment();
                        FileUtils.cell(row, index++, unlock).setCellValue(StringUtils.null2Space2(value == null ? "" : value.toString()));
                    } else if ("longDesEn".equals(propId) || "longDesCn".equals(propId)) {
                        // 项目长度可能会超过32767个字符，需要截取，否则会报错，目前只检查长描述英文/中文
                        String longDes = fields.getStringAttribute(propId);
                        if (longDes == null) {
                            longDes = "";
                        } else if (longDes.length() > CELL_LENGTH_LIMIT) {
                            longDes = longDes.substring(0, CELL_LENGTH_LIMIT);
                        }
                        FileUtils.cell(row, index++, unlock).setCellValue(longDes);
                    } else {
                        Object value = fields.getAttribute(propId);
                        FileUtils.cell(row, index++, unlock).setCellValue(StringUtils.null2Space2(value == null ? "" : value.toString()));
                    }
                }
            }

            if (customProps != null) {
                for (Map<String, String> prop : customProps) {
                    Object value = item.getFeed().getCnAtts().getAttribute(prop.get("feed_prop_original"));
                    FileUtils.cell(row, index++, unlock)
                            .setCellValue(StringUtils.null2Space2(value == null ? "" : value.toString()));
                    value = item.getFeed().getOrgAtts().getAttribute(prop.get("feed_prop_original"));
                    FileUtils.cell(row, index++, unlock)
                            .setCellValue(StringUtils.null2Space2(value == null ? "" : value.toString()));
                }
            }
            if (salesProps != null) {
                CmsBtProductModel_Sales salesData = item.getSales();
                String key;
                for (Map<String, Object> prop : salesProps) {
                    key = (String) prop.get("value");
                    key = key.substring(6);
                    Integer salesVal = null;
                    if (salesData.getSubNode(key.split("\\.")) instanceof Double)
                        salesVal = ((Double) salesData.getSubNode(key.split("\\."))).intValue();
                    else
                        salesVal = (Integer) salesData.getSubNode(key.split("\\."));

                    if (salesVal == null) {
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                    } else {
                        FileUtils.cell(row, index++, unlock).setCellValue(salesVal);
                    }
                }
            }
            if (bidatasProps != null) {
                BaseMongoMap biData = item.getBi();
                for (Map<String, Object> prop : bidatasProps) {
                    String key = (String) prop.get("value");
                    key = key.substring(3);
                    Object salesVal = biData.getSubNode(key.split("\\."));
                    if (salesVal == null) {
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                    } else {
                        FileUtils.cell(row, index++, unlock).setCellValue(salesVal.toString());
                    }
                }
            }

            /**平台级内容输出*/
            if (platformDataList != null) {
                Pattern patten = Pattern.compile("^platforms\\.P(\\d+)");
                for (Map<String, String> model : platformDataList) {
                    String key = model.get("value");
                    String _cartId = "0";
                    Matcher matcher = patten.matcher(key);

                    while (matcher.find()) {
                        _cartId = matcher.group(1);
                    }

                    CmsBtProductModel_Platform_Cart _platform = item.getPlatform(Integer.valueOf(_cartId));

                    if (_platform == null) {
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                        continue;
                    }

                    if("isSale".equals(key.substring(key.lastIndexOf(".") + 1))) continue;
                    if("qty".equals(key.substring(key.lastIndexOf(".") + 1))){
                        Integer qty = 0;
                        for (BaseMongoMap<String, Object> map : _platform.getSkus()) {
                            String sku = (String) map.get("skuCode");
                            Boolean isSale = (Boolean) map.get("isSale");
                            if(isSale !=null && isSale){
                                if (codesMap.get(sku) != null) {
                                    qty = qty + codesMap.get(sku);
                                }
                            }
                        }
                        FileUtils.cell(row, index++, unlock).setCellValue(qty);
                    }else{
                        index = contructPlatCell(key, row, index, unlock, _cartId, _platform, key.substring(key.lastIndexOf(".") + 1));
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
     * @param channelIdMap
     * @return boolean 是否终止输出
     */
    private boolean writeRecordToGroupFile(Workbook book, List<CmsBtProductBean> items, String channelId, List<TypeChannelBean> cartList, int startRowIndex, Map<String, String> channelIdMap, Map cmsSession) {
        boolean isContinueOutput = true;
        CellStyle unlock = FileUtils.createUnLockStyle(book);

        // 先取得各产品group信息
        List<String> codeList = new ArrayList<>();
        for (CmsBtProductBean item : items) {
            if (item.getCommon() == null) {
                continue;
            }
            if (item.getCommon().getFields() == null) {
                continue;
            }
            codeList.add(item.getCommon().getFields().getCode());
        }
        List<Integer> cartIdList = cartList.stream()
                .map(cartObj -> NumberUtils.toInt(cartObj.getValue()))
                .collect(Collectors.toList());

        JongoQuery queryObject = new JongoQuery();
        queryObject.setQuery("{'channelId':#,'mainProductCode':{$in:#},'cartId':{$in:#}}");
        queryObject.setParameters(channelId, codeList, cartIdList);
        queryObject.setProjection("{'_id':0,'cartId':1,'mainProductCode':1,'numIId':1,'priceMsrpSt':1,'priceMsrpEd':1,'priceRetailSt':1,'priceRetailEd':1,'priceSaleSt':1,'priceSaleEd':1}");
        List<CmsBtProductGroupModel> grpList = productGroupService.getList(channelId, queryObject);

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
            Row row = FileUtils.row(sheet, startRowIndex++);
            int index = 0;

            // 内容输出
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getModel()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(item.getPlatformNotNull(0).getMainProductCode()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getProductNameEn()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getOriginalTitleCn()));
            FileUtils.cell(row, index++, unlock).setCellValue(channelIdMap.get(item.getOrgChannelId()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(item.getCommon().getCatPath()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getBrand()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(item.getFeed().getCatPath()));

            List<Map<String, String>> platformDataList = (List<Map<String, String>>) cmsSession.get("_adv_search_selPlatformDataList");
            if (platformDataList != null) {
                Pattern patten = Pattern.compile("^platforms\\.P(\\d+)");
                for (Map<String, String> model : platformDataList) {
                    String key = model.get("value");
                    String _cartId = "0";
                    Matcher matcher = patten.matcher(key);

                    while (matcher.find()) {
                        _cartId = matcher.group(1);
                    }

                    CmsBtProductModel_Platform_Cart _platform = item.getPlatform(Integer.valueOf(_cartId));

                    if (_platform == null) {
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                        continue;
                    }
                    if("qty".equals(key.substring(key.lastIndexOf(".") + 1))) continue;
                    if("isSale".equals(key.substring(key.lastIndexOf(".") + 1))) continue;
                    index = contructPlatCell(key, row, index, unlock, _cartId, _platform, key.substring(key.lastIndexOf(".") + 1));

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
     * @param cartList      cartId列表
     * @param startRowIndex 开始
     * @param channelIdMap
     * @return 行数偏移量
     */
    private int writeRecordToSkuFile(Workbook book, List<CmsBtProductBean> items, List<TypeChannelBean> cartList, int startRowIndex, Map<String, String> channelIdMap, Map cmsSession) {
        int total = 0;
        List<CmsBtProductBean> products = new ArrayList<>();
        Map<String, Set<String>> codesMap = new HashMap<>();
        for (CmsBtProductBean item : items) {
            if (item.getCommon() == null) {
                continue;
            }
            CmsBtProductModel_Field fields = item.getCommon().getFields();
            if (fields == null) {
                continue;
            }
            List<CmsBtProductModel_Sku> skuList = item.getCommon().getSkus();
            if (skuList == null || skuList.isEmpty()) {
                Map<String, String> failMap = new HashMap<>();
                failMap.put(fields.getCode(), String.format("商品common.skus为空, code=%s", fields.getCode()));
                continue;
            }
            if (org.apache.commons.lang.StringUtils.isNotBlank(fields.getOriginalCode())) {
                codesMap.computeIfAbsent(item.getOrgChannelId(), k -> new HashSet<>());
                codesMap.get(item.getOrgChannelId()).add(fields.getOriginalCode());
            }
            products.add(item);
        }

        CellStyle unlock = FileUtils.createUnLockStyle(book);

        // 现有表格的列，请参照本工程目录下 /contents/cms/file_template/skuList-template.xlsx
        Sheet sheet = book.getSheetAt(0);
        for (CmsBtProductBean item : products) {
            CmsBtProductModel_Field fields = item.getCommon().getFields();
            List<CmsBtProductModel_Sku> skuList = item.getCommon().getSkus();

            // 内容输出
            for (CmsBtProductModel_Sku skuItem : skuList) {
                int index = 0;
                Row row = FileUtils.row(sheet, startRowIndex++);
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(skuItem.getSkuCode()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(skuItem.getBarcode()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getCode()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(item.getCommonNotNull().getFieldsNotNull().getProductNameEn()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(item.getCommonNotNull().getFieldsNotNull().getOriginalTitleCn()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(skuItem.getClientSkuCode()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(skuItem.getClientSize()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(skuItem.getSize()));
                FileUtils.cell(row, index++, unlock).setCellValue(skuItem.getQty() == null ? "0" : String.valueOf(skuItem.getQty()));
                if (skuItem.getClientMsrpPrice() == null) {
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                } else {
                    FileUtils.cell(row, index++, unlock).setCellValue(skuItem.getClientMsrpPrice());
                }
                if (skuItem.getClientRetailPrice() == null) {
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                } else {
                    FileUtils.cell(row, index++, unlock).setCellValue(skuItem.getClientRetailPrice());
                }
                if (skuItem.getClientNetPrice() == null) {
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                } else {
                    FileUtils.cell(row, index++, unlock).setCellValue(skuItem.getClientNetPrice());
                }
                // 重量
                if (skuItem.getWeight() == null) {
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                } else {
                    FileUtils.cell(row, index++, unlock).setCellValue(skuItem.getWeight());
                }

                List<Map<String, String>> platformDataList = (List<Map<String, String>>) cmsSession.get("_adv_search_selPlatformDataList");
                if (platformDataList != null) {
                    Pattern patten = Pattern.compile("^platforms\\.P(\\d+)");
                    for (Map<String, String> model : platformDataList) {
                        String key = model.get("value");
                        String _cartId = "0";
                        Matcher matcher = patten.matcher(key);

                        while (matcher.find()) {
                            _cartId = matcher.group(1);
                        }

                        CmsBtProductModel_Platform_Cart _platform = item.getPlatform(Integer.valueOf(_cartId));

                        if (_platform == null) {
                            FileUtils.cell(row, index++, unlock).setCellValue("");
                            continue;
                        }
                        if("qty".equals(key.substring(key.lastIndexOf(".") + 1))) continue;
                        if("isSale".equals(key.substring(key.lastIndexOf(".") + 1))){
                            FileUtils.cell(row, index++, unlock).setCellValue(skuItem.getIsSale() == 0 ? "No":"Yes");
                        }else {
                            index = contructPlatCell(key, row, index, unlock, _cartId, _platform, key.substring(key.lastIndexOf(".") + 1));
                        }
                    }
                }

                FileUtils.cell(row, index++, unlock).setCellValue(getLockStatusTxt(item.getLock()));
                total++;
            }
        }
        return total - SELECT_PAGE_SIZE;
    }

    /**
     * 构建excel内容中的平台信息
     * @param key
     * @param row
     * @param index
     * @param unlock
     * @param _cartId
     * @param _platform
     * @param attrName
     */
    private int contructPlatCell(String key, Row row, int index, CellStyle unlock, String _cartId, CmsBtProductModel_Platform_Cart _platform, String attrName) {

        if ("MallURL".equals(attrName)) {
            FileUtils.cell(row, index++, unlock).setCellValue(platformService.getPlatformProductUrl(_cartId, _platform.getpPlatformMallId()));
        } else if ("pMallId".equals(attrName)) {
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(_platform.getpPlatformMallId()));
        } else if ("URL".equals(attrName)) {
            if (CartEnums.Cart.JD.getId().equals(_cartId)
                    || CartEnums.Cart.JG.getId().equals(_cartId)
                    || CartEnums.Cart.JGY.getId().equals(_cartId)
                    || CartEnums.Cart.JGJ.getId().equals(_cartId)) {
                FileUtils.cell(row, index++, unlock).setCellValue(platformService.getPlatformProductUrl(_cartId, getJdPlatformSkuId(_platform)));
            } else
                FileUtils.cell(row, index++, unlock).setCellValue(platformService.getPlatformProductUrl(_cartId, _platform.getpNumIId()));
        } else if ("title".equals(attrName)) {
            FileUtils.cell(row, index++, unlock).setCellValue(getPlatformProdName(_cartId, _platform.getFieldsNotNull()));
        } else if ("pCatPath".equals(attrName)) {
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(_platform.getpCatPath()));
        } else if ("pPriceMsrpEd".equals(attrName)) {
            FileUtils.cell(row, index++, unlock).setCellValue(getOutputPrice(_platform.getpPriceMsrpSt(), _platform.getpPriceMsrpEd()));
        } else if ("pPriceRetailEd".equals(attrName)) {
            FileUtils.cell(row, index++, unlock).setCellValue(getOutputPrice(_platform.getpPriceRetailSt(), _platform.getpPriceRetailEd()));
        } else if ("pPriceSaleEd".equals(attrName)) {
            FileUtils.cell(row, index++, unlock).setCellValue(getOutputPrice(_platform.getpPriceSaleSt(), _platform.getpPriceSaleEd()));
        } else if ("lock".equals(attrName)) {
            FileUtils.cell(row, index++, unlock).setCellValue(getLockStatusTxt(StringUtil.isEmpty(_platform.getLock())?"0":"1"));
        } else {
            key = key.split(_cartId)[1].substring(1);
            Object salesVal = _platform.getSubNode(key.split("\\."));
            if (salesVal == null) {
                FileUtils.cell(row, index++, unlock).setCellValue("");
            } else {
                FileUtils.cell(row, index++, unlock).setCellValue(salesVal.toString());
            }
        }
        return index;
    }

    /**
     * 导出聚美上新SKU级数据
     *
     * @param book
     * @param items
     * @param startRowIndex
     * @param failList
     */
    private int writePublishJMSkuFile(Workbook book, List<CmsBtProductBean> items, int startRowIndex, List<CmsBtOperationLogModel_Msg> failList) {
        int total = 0;
        List<CmsBtProductBean> products = new ArrayList<>();
        Set<String> codes = new HashSet<>();
        for (CmsBtProductBean item : items) {
            CmsBtProductModel_Common common;
            CmsBtProductModel_Field fields;
            CmsBtProductModel_Platform_Cart cart;
            if ((common = item.getCommon()) == null || (fields = common.getFields()) == null) {
                continue;
            }
            if ((cart = item.getPlatform(Integer.valueOf(CartEnums.Cart.JM.getId()))) == null) {
                CmsBtOperationLogModel_Msg failMap = new CmsBtOperationLogModel_Msg();
                failMap.setSkuCode(fields.getCode());
                failMap.setMsg(String.format("商品没有聚美Platform, code=%s, cartId=%s", fields.getCode(), CartEnums.Cart.JM.getId()));
                failList.add(failMap);
                continue;
            }
            if (org.apache.commons.lang.StringUtils.isBlank(cart.getpPlatformMallId())) {
                CmsBtOperationLogModel_Msg failMap = new CmsBtOperationLogModel_Msg();
                failMap.setSkuCode(fields.getCode());
                failMap.setMsg(String.format("商品没有聚美PlatformMallId, code=%s, cartId=%s", fields.getCode(), CartEnums.Cart.JM.getId()));
                failList.add(failMap);
                continue;
            }
            if (CollectionUtils.isEmpty(cart.getSkus())) {
                CmsBtOperationLogModel_Msg failMap = new CmsBtOperationLogModel_Msg();
                failMap.setSkuCode(fields.getCode());
                failMap.setMsg(String.format("商品没有聚美skus, code=%s, cartId=%s", fields.getCode(), CartEnums.Cart.JM.getId()));
                failList.add(failMap);
                continue;
            }
            if (org.apache.commons.lang.StringUtils.isNotBlank(fields.getOriginalCode())) {
                codes.add(fields.getOriginalCode());
            }
            products.add(item);
        }
//        Map<SkuInventoryForCmsBean, Integer> skuInventoryMap = new HashMap<>();
//        if (!codes.isEmpty()) {
//            List<SkuInventoryForCmsBean> inventoryForCmsBeanList = inventoryDao.batchSelectInventory(ChannelConfigEnums.Channel.ShoeMetro.getId(), new ArrayList<>(codes));
//            if (CollectionUtils.isNotEmpty(inventoryForCmsBeanList)) {
//                for (SkuInventoryForCmsBean skuInventory : inventoryForCmsBeanList) {
//                    skuInventoryMap.put(skuInventory, skuInventory.getQty() == null ? Integer.valueOf(0) : skuInventory.getQty());
//                }
//            }
//        }
        String jmUrlPrefix = platformService.getPlatformProductUrl(CartEnums.Cart.JM.getId());
        // 写入导出数据
        Sheet sheet = book.getSheetAt(0);
        CellStyle unlock = FileUtils.createUnLockStyle(book);
        for (CmsBtProductBean item : products) {
            CmsBtProductModel_Common common = item.getCommon();
            CmsBtProductModel_Field fields = common.getFields();
            List<CmsBtProductModel_Sku> skus = common.getSkus();
            CmsBtProductModel_Platform_Cart cart = item.getPlatform(Integer.valueOf(CartEnums.Cart.JM.getId()));
            List<BaseMongoMap<String, Object>> platformSkus = cart.getSkus();
            // 内容输出
            for (BaseMongoMap<String, Object> skuMap : platformSkus) {
                Row row = FileUtils.row(sheet, startRowIndex++);
                int index = 0;
                String skuCode = org.apache.commons.lang3.StringUtils.trimToEmpty(skuMap.getStringAttribute("skuCode"));
                FileUtils.cell(row, index++, unlock).setCellValue(skuCode);
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getBrand()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getCode()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getColor()));
                // Size 循环common.skus
                String size = "";
                Double voPrice = null;
                for (CmsBtProductModel_Sku sku : skus) {
                    if (skuCode.equals(sku.getSkuCode())) {
                        size = org.apache.commons.lang3.StringUtils.trimToEmpty(sku.getSize());
                        voPrice = sku.getClientNetPrice();
                        break;
                    }
                }
                FileUtils.cell(row, index++, unlock).setCellValue(size);
                FileUtils.cell(row, index++, unlock).setCellValue(voPrice == null ? "" : String.valueOf(voPrice.doubleValue()));
                FileUtils.cell(row, index++, unlock).setCellValue(skuMap.getDoubleAttribute("priceSale"));

                // JmURL
                FileUtils.cell(row, index++, unlock).setCellValue(jmUrlPrefix + cart.getpPlatformMallId() + ".html");
                FileUtils.cell(row, index++, unlock).setCellValue(skuMap.getStringAttribute("qty") == null ? "0" : skuMap.getStringAttribute("qty"));
                total++;
            }
        }
        return total - SELECT_PAGE_SIZE;
    }

    /**
     * 报备文件导出
     *
     * @param workbook
     * @param items
     * @param startRowIndex
     * @param failList
     */
    private int writeFilingToFile(Workbook workbook, List<CmsBtProductBean> items, int startRowIndex, List<CmsBtOperationLogModel_Msg> failList) {
        int total = 0;
        List<CmsBtProductBean> products = new ArrayList<>();
        List<String> codes = new ArrayList<>();
        // 过滤选择的商品
        for (CmsBtProductBean item : items) {
            CmsBtProductModel_Common common = item.getCommon();
            if (common == null || CollectionUtils.isEmpty(common.getSkus()))
                continue;
            CmsBtProductModel_Field fields = common.getFields();
            if (fields == null)
                continue;
            Integer isFiled = fields.getIntAttribute("isFiled");
            if (isFiled.intValue() == 1) {
                CmsBtOperationLogModel_Msg failMap = new CmsBtOperationLogModel_Msg();
                failMap.setSkuCode(fields.getCode());
                failMap.setMsg(String.format("商品已经报备过，code=%s, isFiled=%s", fields.getCode(), isFiled));
                failList.add(failMap);
                continue; // 已经报备过，直接跳过
            }
            boolean skip = true;
            Map<String, CmsBtProductModel_Platform_Cart> platforms = item.getPlatforms();
            if (platforms != null && platforms.size() > 0) {
                for (CmsBtProductModel_Platform_Cart platform : platforms.values()) {
                    if (platform.getCartId() > 10 && platform.getCartId() < 900) {
                        if (CmsConstants.ProductStatus.Approved.name().equals(platform.getStatus())) {
                            skip = false;
                            break;
                        }
                    }
                }
            }
            if (skip) {
                CmsBtOperationLogModel_Msg failMap = new CmsBtOperationLogModel_Msg();
                failMap.setSkuCode(fields.getCode());
                failMap.setMsg(String.format("商品没有平台状态为Approved，code=%s", fields.getCode()));
                failList.add(failMap);
                continue; // 如果没有任何一个平台的pStatus=Approved，直接跳过
            }
            products.add(item);
            codes.add(fields.getCode());
        }
        CellStyle unlock = FileUtils.createUnLockStyle(workbook);
        Sheet sheet = workbook.getSheetAt(0);
        for (CmsBtProductBean item : products) {
            $info(item.getCommonNotNull().getFields().getCode());
            CmsBtProductModel_Field fields = item.getCommon().getFields();
            List<CmsBtProductModel_Sku> skuList = item.getCommon().getSkus();

            // 内容输出
            for (CmsBtProductModel_Sku skuItem : skuList) {
                int index = 0;
                Row row = FileUtils.row(sheet, startRowIndex++);
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(skuItem.getSkuCode()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getCode()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getModel()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(skuItem.getClientSize())); // 原始尺码
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(skuItem.getSize()));
                if (fields.getProductNameEn() != null && fields.getProductNameEn().length() > 2000)
                    fields.setProductNameEn(fields.getProductNameEn().substring(0, 2000));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getProductNameEn())); // 英文标题
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getOriginalTitleCn())); // 中文标题
                // 图片路径
                String imgPath = "";
                if (CollectionUtils.isNotEmpty(fields.getImages1()) && fields.getImages1().get(0) != null) {
                    imgPath = Constants.productForOtherSystemInfo.IMG_URL + fields.getImages1().get(0).getName();
                }
                FileUtils.cell(row, index++, unlock).setCellValue(imgPath); // 图片
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getSizeType())); // 使用人群
                if (fields.getMaterialEn() != null && fields.getMaterialEn().length() > 2000)
                    fields.setMaterialEn(fields.getMaterialEn().substring(0, 2000));
                if (fields.getMaterialCn() != null && fields.getMaterialCn().length() > 2000)
                    fields.setMaterialCn(fields.getMaterialCn().substring(0, 2000));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getMaterialEn()) + " | " + org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getMaterialCn())); // 材质
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getOrigin()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getColor()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getBrand()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getWeightKG() + ""));

                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(skuItem.getBarcode())); // UPC
                if (fields.getShortDesEn() != null && fields.getShortDesEn().length() > 2000)
                    fields.setShortDesEn(fields.getShortDesEn().substring(0, 2000));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getShortDesEn()));
                if (fields.getShortDesCn() != null && fields.getShortDesCn().length() > 2000)
                    fields.setShortDesCn(fields.getShortDesCn().substring(0, 2000));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getShortDesCn()));

                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(item.getCommon().getCatPath())); // 类目

                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getHsCodeCross())); // HSCode
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getHsCodePrivate())); // HSCodePU 个人税号
                double priceSale = 0d;
                Map<String, CmsBtProductModel_Platform_Cart> platforms = item.getPlatforms();
                for (CmsBtProductModel_Platform_Cart platform : platforms.values()) {
                    if (CmsConstants.ProductStatus.Approved.name().equals(platform.getStatus())) {
                        priceSale = platform.getSkus().get(0).getDoubleAttribute("priceSale");
                        break;
                    }
                }
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(String.valueOf(priceSale)));
                total++;
            }
        }

        return total - SELECT_PAGE_SIZE;
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

    /**
     * 转换锁定状态，从code转到文字
     */
    private String getLockStatusTxt(String code) {
        if (lockStatusMap == null || code == null) {
            return "";
        }
        String rs = lockStatusMap.get(code);
        if (rs == null) {
            rs = "";
        }
        return rs;
    }

    /**
     * 取得平台下的商品名
     */
    private String getPlatformProdName(String cartId, BaseMongoMap fieldObj) {
        String rs = null;
        // 设置平台下的商品名
        CartBean cartBean = Carts.getCart(cartId);
        if (PlatFormEnums.PlatForm.TM.getId().equals(cartBean.getPlatform_id())) {
            rs = org.apache.commons.lang3.StringUtils.trimToEmpty(fieldObj.getStringAttribute("title"));
        } else if (PlatFormEnums.PlatForm.JD.getId().equals(cartBean.getPlatform_id())) {
            rs = org.apache.commons.lang3.StringUtils.trimToEmpty(fieldObj.getStringAttribute("productTitle"));
        } else if (PlatFormEnums.PlatForm.JM.getId().equals(cartBean.getPlatform_id())) {
            rs = org.apache.commons.lang3.StringUtils.trimToEmpty(fieldObj.getStringAttribute("productLongName"));
        } else {
            rs = "";
        }
        return rs;
    }

    /**
     * 返回jsSkuId
     *
     * @param ptfObj
     * @return
     */
    private String getJdPlatformSkuId(CmsBtProductModel_Platform_Cart ptfObj) {
        for (BaseMongoMap<String, Object> map : ptfObj.getSkus()) {
            if (!StringUtils.isEmpty(map.getStringAttribute("jdSkuId"))
                    && Boolean.valueOf(map.getStringAttribute("isSale")))
                return map.getStringAttribute("jdSkuId");
        }
        return "";
    }

}
