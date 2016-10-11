package com.voyageone.task2.cms.service.product;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
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
import com.voyageone.common.util.*;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.CmsBtExportTaskService;
import com.voyageone.service.impl.cms.ImagesService;
import com.voyageone.service.impl.cms.PlatformService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.search.CmsAdvSearchQueryService;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtExportTaskModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author JiangJusheng
 * @version 2.0.0, 2016/08/18
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_TASK_AdvSearch_FileDldJob)
public class CmsAdvSearchExportFileService extends BaseMQCmsService {

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

    // excel cell的内容长度限制
    private final static int CELL_LENGTH_LIMIT = 2000;
    // DB检索页大小
    private final static int SELECT_PAGE_SIZE = 2000;
    // Excel 文件最大行数
    private final static int MAX_EXCEL_REC_COUNT = 10000;

    // 各平台固定输出列
    private final static String[] _DynCol = { "URL", "Numiid", "Name", "Category", "MSRP", "RetailPrice", "SalePrice" };
    private final static String[] _DynColCN = { "URL", "Numiid", "商品名称", "类目", "官方建议售价(范围)", "指导售价(范围)", "最终售价(范围)" };
    private final static String[] _DynColJM = { "URL", "HashID", "Name", "Category", "MSRP", "RetailPrice", "SalePrice" };
    private final static String[] _DynColCNJM = { "URL", "HashID", "商品名称", "类目", "官方建议售价(范围)", "指导售价(范围)", "最终售价(范围)" };

    // 产品数据（code级）固定输出列，用于过滤自定义显示列中相同项目
    private final static String[] _prodCol = { "code", "brand", "category", "productNameEn", "originalTitleCn", "model", "quantity", "color" };

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        $debug("高级检索 文件下载任务 param=" + messageMap.toString());
        Integer taskId = (Integer) messageMap.get("_taskId");
        if (taskId == null) {
            $error("高级检索 文件下载任务 查询参数不正确 缺少ID");
            return;
        }
        CmsBtExportTaskModel taskModel = cmsBtExportTaskService.getExportById(taskId);
        if (taskModel == null) {
            $error("高级检索 文件下载任务 查询参数不正确 该任务不存在");
            return;
        }
        CmsSearchInfoBean2 searchValue = null;
        try {
            searchValue = JacksonUtil.json2Bean(JacksonUtil.bean2Json(messageMap), CmsSearchInfoBean2.class);
        } catch (Exception exp) {
            $error("高级检索 文件下载任务 查询参数不正确", exp);
            // 更新任务状态，然后结束
            taskModel.setStatus(2);
            taskModel.setComment("查询参数不正确");
            taskModel.setModified(new Date());
            cmsBtExportTaskService.update(taskModel);
            return;
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
            return;
        }

        try {
            String fileName = createExcelFile(searchValue, (List<String>) messageMap.get("_selCodeList"), channleId, sessionBean, userName, language);
            taskModel.setFileName(fileName);
            taskModel.setStatus(1); // 成功
        } catch (Throwable exp) {
            $error("高级检索 文件下载任务 创建文件时出错 " + exp.getMessage(), exp);
            // 更新任务状态，然后结束
            taskModel.setStatus(2);
            taskModel.setComment("创建文件时出错 " + exp.getMessage());
        }
        taskModel.setModified(new Date());
        cmsBtExportTaskService.update(taskModel);
    }

    /**
     * 获取数据文件内容
     */
    private String createExcelFile(CmsSearchInfoBean2 searchValue, List<String> codeList, String channelId, Map<String, Object> cmsSessionBean, String userName, String language)
            throws IOException, InvalidFormatException {
        String fileName = null;
        String templatePath = null;
        if (searchValue.getFileType() == 1) {
            fileName = "productList_";
            templatePath = Properties.readValue(CmsProperty.Props.SEARCH_ADVANCE_EXPORT_TEMPLATE_PRODUCT);
        } else if (searchValue.getFileType() == 2) {
            fileName = "groupList_";
            templatePath = Properties.readValue(CmsProperty.Props.SEARCH_ADVANCE_EXPORT_TEMPLATE_GROUP);
        } else if (searchValue.getFileType() == 3) {
            fileName = "skuList_";
            templatePath = Properties.readValue(CmsProperty.Props.SEARCH_ADVANCE_EXPORT_TEMPLATE_SKU);
        }
        if (templatePath == null || !new File(templatePath).exists()) {
            $error("高级检索 文件下载任务 创建文件时出错 模板文件不存在 " + templatePath);
            throw new BusinessException("模板文件不存在 " + templatePath);
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
        List<String> prodCodeList = null;
        if (searchValue.getFileType() == 2) {
            if (codeList == null) {
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
            if (codeList == null) {
                prodCodeList = advSearchQueryService.getProductCodeList(searchValue, channelId);
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
        $info("准备打开文档 [ %s ]", templatePath);
        JongoQuery queryObject = new JongoQuery();
        queryObject.setQuery("{'common.fields.code':{$in:#}}");
        queryObject.setParameters(prodCodeList);
        String searchItemStr = CmsAdvSearchQueryService.searchItems;
        if (cmsSessionBean.get("_adv_search_props_searchItems") != null) {
            searchItemStr += (String) cmsSessionBean.get("_adv_search_props_searchItems");
        }
        if (searchValue.getFileType() == 3) {
            // 要输出sku级信息
            searchItemStr += "common.skus;common.fields.model;common.fields.color;";
        } else if (searchValue.getFileType() == 2) {
            // 要输出group级信息
            searchItemStr += "common.fields.model;";
        } else if (searchValue.getFileType() == 1) {
            searchItemStr += "common.fields.model;common.fields.color;";
        }

        queryObject.setProjectionExt(searchItemStr.split(";"));
        queryObject.setSort(advSearchQueryService.getSortValue(searchValue));

        // 店铺(cart/平台)列表
        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, language);
        if (lockStatusMap == null) {
            List<TypeBean> lockStatusList = TypeConfigEnums.MastType.procLockStatus.getList(language);
            lockStatusMap = lockStatusList.stream().collect(Collectors.toMap((p) -> p.getValue(), (p) -> p.getName()));
        }

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
                List<CmsBtProductBean> items = productService.getBeanList(channelId, queryObject);
                if (items.size() == 0) {
                    break;
                }

                // 每页开始行
                int startRowIndex = i * SELECT_PAGE_SIZE + 2;
                boolean isContinueOutput = false;
                if (searchValue.getFileType() == 1) {
                    isContinueOutput = writeRecordToFile(book, items, cmsSessionBean, channelId, cartList, startRowIndex);
                } else if (searchValue.getFileType() == 2) {
                    isContinueOutput = writeRecordToGroupFile(book, items, channelId, cartList, startRowIndex);
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
            fileName += userName + "_" + DateTimeUtil.format(DateTimeUtilBeijing.getCurrentBeiJingDate(), DateTimeUtil.DEFAULT_DATETIME_FORMAT_1) + ".xlsx";
            OutputStream outputStream = new FileOutputStream(exportPath + fileName);
            try {
                book.write(outputStream);
                $info("已写入输出流");
            } finally {
                outputStream.close();
            }
        } finally {
            inputStream.close();
            book.close();
        }
        return fileName;
    }

    /**
     * code级数据下载，设置每列标题(包含动态列, 要过滤重复列)
     */
    private void writeHead(Workbook book, Map cmsSession, List<TypeChannelBean> cartList) {
        List<Map<String, String>> customProps = (List<Map<String, String>>) cmsSession.get("_adv_search_customProps");
        List<Map<String, String>> commonProps = (List<Map<String, String>>) cmsSession.get("_adv_search_commonProps");
        List<Map<String, String>> salesProps = (List<Map<String, String>>) cmsSession.get("_adv_search_selSalesType");
        List<Map<String, String>> bidatasProps = (List<Map<String, String>>) cmsSession.get("_adv_search_selBiDataList");
        Sheet sheet = book.getSheetAt(0);
        // 第一行，英文标题
        Row row = FileUtils.row(sheet, 0);
        CellStyle style = null;
        if (row.getCell(0) != null) {
            style = row.getCell(0).getCellStyle();
        }
        // 固定列长度
        int index = 8;
        for (TypeChannelBean cartObj : cartList) {
            if (CartEnums.Cart.JM.getId().equals(cartObj.getValue())) {
                for (String prop : _DynColJM) {
                    FileUtils.cell(row, index++, style).setCellValue(cartObj.getName() + prop);
                }
            } else {
                for (String prop : _DynCol) {
                    FileUtils.cell(row, index++, style).setCellValue(cartObj.getName() + prop);
                }
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
        if (bidatasProps != null) {
            for (Map<String, String> prop : bidatasProps) {
                FileUtils.cell(row, index++, style).setCellValue(prop.get("name"));
            }
        }

        // 第二行，中文标题
        row = FileUtils.row(sheet, 1);
        if (row.getCell(0) == null) {
            style = null;
        } else {
            style = row.getCell(0).getCellStyle();
        }
        // 固定列长度
        index = 8;
        for (TypeChannelBean cartObj : cartList) {
            if (CartEnums.Cart.JM.getId().equals(cartObj.getValue())) {
                for (String prop : _DynColCNJM) {
                    FileUtils.cell(row, index++, style).setCellValue(cartObj.getName() + prop);
                }
            } else {
                for (String prop : _DynColCN) {
                    FileUtils.cell(row, index++, style).setCellValue(cartObj.getName() + prop);
                }
            }
        }

        FileUtils.cell(row, index++, style).setCellValue("商品原图地址");
        FileUtils.cell(row, index++, style).setCellValue("是否被锁定");

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
        if (bidatasProps != null) {
            for (Map<String, String> prop : bidatasProps) {
                FileUtils.cell(row, index++, style).setCellValue(prop.get("name"));
            }
        }
    }

    /**
     * group级数据下载，设置每列标题(包含动态列)
     */
    private void writeGroupHead(Workbook book, List<TypeChannelBean> cartList) {
        Sheet sheet = book.getSheetAt(0);
        // 第一行，英文标题
        Row row = FileUtils.row(sheet, 0);
        CellStyle style = row.getCell(0).getCellStyle();

        int index = 5;
        for (TypeChannelBean cartObj : cartList) {
            if (CartEnums.Cart.JM.getId().equals(cartObj.getValue())) {
                for (String prop : _DynColJM) {
                    FileUtils.cell(row, index++, style).setCellValue(cartObj.getName() + prop);
                }
            } else {
                for (String prop : _DynCol) {
                    FileUtils.cell(row, index++, style).setCellValue(cartObj.getName() + prop);
                }
            }
        }

        // 第二行，中文标题
        row = FileUtils.row(sheet, 1);
        style = row.getCell(0).getCellStyle();

        index = 5;
        for (TypeChannelBean cartObj : cartList) {
            if (CartEnums.Cart.JM.getId().equals(cartObj.getValue())) {
                for (String prop : _DynColCNJM) {
                    FileUtils.cell(row, index++, style).setCellValue(cartObj.getName() + prop);
                }
            } else {
                for (String prop : _DynColCN) {
                    FileUtils.cell(row, index++, style).setCellValue(cartObj.getName() + prop);
                }
            }
        }
    }

    /**
     * sku级数据下载，设置每列标题(包含动态列)
     */
    private void writeSkuHead(Workbook book, List<TypeChannelBean> cartList) {
        Sheet sheet = book.getSheetAt(0);
        // 第一行，英文标题
        Row row = FileUtils.row(sheet, 0);
        CellStyle style = row.getCell(0).getCellStyle();

        int index = 13;
        for (TypeChannelBean cartObj : cartList) {
            if (CartEnums.Cart.JM.getId().equals(cartObj.getValue())) {
                for (String prop : _DynColJM) {
                    FileUtils.cell(row, index++, style).setCellValue(cartObj.getName() + prop);
                }
            } else {
                for (String prop : _DynCol) {
                    FileUtils.cell(row, index++, style).setCellValue(cartObj.getName() + prop);
                }
            }
        }
        FileUtils.cell(row, index++, style).setCellValue("Lock");

        // 第二行，中文标题
        row = FileUtils.row(sheet, 1);
        style = row.getCell(0).getCellStyle();

        index = 13;
        for (TypeChannelBean cartObj : cartList) {
            if (CartEnums.Cart.JM.getId().equals(cartObj.getValue())) {
                for (String prop : _DynColCNJM) {
                    FileUtils.cell(row, index++, style).setCellValue(cartObj.getName() + prop);
                }
            } else {
                for (String prop : _DynColCN) {
                    FileUtils.cell(row, index++, style).setCellValue(cartObj.getName() + prop);
                }
            }
        }
        FileUtils.cell(row, index++, style).setCellValue("是否被锁定");
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
    private boolean writeRecordToFile(Workbook book, List<CmsBtProductBean> items, Map cmsSession, String channelId, List<TypeChannelBean> cartList, int startRowIndex) {
        boolean isContinueOutput = true;
        List<Map<String, String>> customProps = (List<Map<String, String>>) cmsSession.get("_adv_search_customProps");
        List<Map<String, String>> commonProps = (List<Map<String, String>>) cmsSession.get("_adv_search_commonProps");
        List<Map<String, Object>> salesProps = (List<Map<String, Object>>) cmsSession.get("_adv_search_selSalesType");
        List<Map<String, Object>> bidatasProps = (List<Map<String, Object>>) cmsSession.get("_adv_search_selBiDataList");
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
                imgCnt ++;
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
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getCode()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getBrand()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(item.getCommon().getCatPath()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getProductNameEn()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getOriginalTitleCn()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getModel()));
            if (fields.getQuantity() == null) {
                FileUtils.cell(row, index++, unlock).setCellValue("");
            } else {
                FileUtils.cell(row, index++, unlock).setCellValue(fields.getQuantity());
            }
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getColor()));

            for (TypeChannelBean cartObj : cartList) {
                CmsBtProductModel_Platform_Cart ptfObj = item.getPlatform(Integer.parseInt(cartObj.getValue()));
                if (ptfObj == null) {
                    // 没有设值时也要输出,不然就会错位
                    for (int i = 0; i < _DynCol.length; i ++) {
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                    }
                    continue;
                }
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(ptfObj.getpNumIId())) {
                    if (CartEnums.Cart.JM.getId().equals(cartObj.getValue())) {
                        FileUtils.cell(row, index++, unlock).setCellValue(platformService.getPlatformProductUrl(cartObj.getValue()) + ptfObj.getpNumIId() + ".html");
                    } else {
                        FileUtils.cell(row, index++, unlock).setCellValue(platformService.getPlatformProductUrl(cartObj.getValue()) + ptfObj.getpNumIId());
                    }
                } else {
                    FileUtils.cell(row, index++, unlock).setCellValue("");
                }
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(ptfObj.getpNumIId()));

                // 设置平台下的商品名
                FileUtils.cell(row, index++, unlock).setCellValue(getPlatformProdName(cartObj.getValue(), ptfObj.getFieldsNotNull()));

                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(ptfObj.getpCatPath()));
                FileUtils.cell(row, index++, unlock).setCellValue(getOutputPrice(ptfObj.getpPriceMsrpSt(), ptfObj.getpPriceMsrpEd()));
                FileUtils.cell(row, index++, unlock).setCellValue(getOutputPrice(ptfObj.getpPriceRetailSt(), ptfObj.getpPriceRetailEd()));
                FileUtils.cell(row, index++, unlock).setCellValue(getOutputPrice(ptfObj.getpPriceSaleSt(), ptfObj.getpPriceSaleEd()));
            }
            nowIdx = index++;
            Cell cell = FileUtils.cell(row, nowIdx, unlock);
            cell.setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty((String) codeImgMap.get(fields.getCode())));
            cell.setCellStyle(cs);

            Integer imgCnt = (Integer) codeImgMap.get(fields.getCode() + "_img_cnt");
            if (imgCnt != null && imgCnt > 1) {
                row.setHeightInPoints(imgCnt * sheet.getDefaultRowHeightInPoints());
            }

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
        }
        sheet.autoSizeColumn(nowIdx);

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
            Row row = FileUtils.row(sheet, startRowIndex ++);
            // 最大行限制
            if (startRowIndex + 1 > MAX_EXCEL_REC_COUNT - 1) {
                isContinueOutput = false;
                FileUtils.cell(row, 0, unlock).setCellValue("未完，存在未抽出数据！");
                break;
            }
            int index = 0;

            // 内容输出
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getModel()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getBrand()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(item.getCommon().getCatPath()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getProductNameEn()));
            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getOriginalTitleCn()));

            for (TypeChannelBean cartObj : cartList) {
                CmsBtProductModel_Platform_Cart ptfObj = item.getPlatform(Integer.parseInt(cartObj.getValue()));
                if (ptfObj == null) {
                    // 没有设值时也要输出,不然就会错位
                    for (int i = 0; i < _DynCol.length; i ++) {
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                    }
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
                    for (int i = 0; i < _DynCol.length; i ++) {
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                    }
                } else {
                    if (org.apache.commons.lang3.StringUtils.isNotEmpty(grpModel.getNumIId())) {
                        if (CartEnums.Cart.JM.getId().equals(cartObj.getValue())) {
                            FileUtils.cell(row, index++, unlock).setCellValue(platformService.getPlatformProductUrl(cartObj.getValue()) + grpModel.getNumIId() + ".html");
                        } else {
                            FileUtils.cell(row, index++, unlock).setCellValue(platformService.getPlatformProductUrl(cartObj.getValue()) + grpModel.getNumIId());
                        }
                    } else {
                        FileUtils.cell(row, index++, unlock).setCellValue("");
                    }
                    FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(grpModel.getNumIId()));

                    // 设置平台下的商品名
                    FileUtils.cell(row, index++, unlock).setCellValue(getPlatformProdName(cartObj.getValue(), ptfObj.getFieldsNotNull()));

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
            List<CmsBtProductModel_Sku> skuList = item.getCommon().getSkus();
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

            // 内容输出
            for (CmsBtProductModel_Sku skuItem : skuList) {
                int index = 0;
                Row row = FileUtils.row(sheet, startRowIndex++);
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(skuItem.getSkuCode()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(skuItem.getBarcode()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(skuItem.getClientSkuCode()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getBrand()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(item.getCommon().getCatPath()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getModel()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getCode()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(fields.getColor()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(skuItem.getClientSize()));
                FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(skuItem.getSize()));
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

                for (TypeChannelBean cartObj : cartList) {
                    CmsBtProductModel_Platform_Cart ptfObj = item.getPlatform(Integer.parseInt(cartObj.getValue()));
                    if (ptfObj == null) {
                        // 没有设值时也要输出,不然就会错位
                        for (int i = 0; i < _DynCol.length; i ++) {
                            FileUtils.cell(row, index++, unlock).setCellValue("");
                        }
                        continue;
                    }
                    List<BaseMongoMap<String, Object>> innerSkus = ptfObj.getSkus();
                    if (innerSkus == null) {
                        // 没有设值时也要输出,不然就会错位
                        for (int i = 0; i < _DynCol.length; i ++) {
                            FileUtils.cell(row, index++, unlock).setCellValue("");
                        }
                        continue;
                    }
                    for (BaseMongoMap prop : innerSkus) {
                        if (skuItem.getSkuCode().equals(prop.getStringAttribute("skuCode"))) {
                            if (org.apache.commons.lang3.StringUtils.isNotEmpty(ptfObj.getpNumIId())) {
                                if (cartObj.getValue().equals(CartEnums.Cart.JM.getId())) {
                                    FileUtils.cell(row, index++, unlock).setCellValue(platformService.getPlatformProductUrl(cartObj.getValue()) + ptfObj.getpNumIId() + ".html");
                                } else {
                                    FileUtils.cell(row, index++, unlock).setCellValue(platformService.getPlatformProductUrl(cartObj.getValue()) + ptfObj.getpNumIId());
                                }
                            } else {
                                FileUtils.cell(row, index++, unlock).setCellValue("");
                            }
                            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(ptfObj.getpNumIId()));

                            // 设置平台下的商品名
                            FileUtils.cell(row, index++, unlock).setCellValue(getPlatformProdName(cartObj.getValue(), ptfObj.getFieldsNotNull()));

                            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(ptfObj.getpCatPath()));
                            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(prop.getStringAttribute("priceMsrp")));
                            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(prop.getStringAttribute("priceRetail")));
                            FileUtils.cell(row, index++, unlock).setCellValue(org.apache.commons.lang3.StringUtils.trimToEmpty(prop.getStringAttribute("priceSale")));
                        }
                    }
                }
                FileUtils.cell(row, index++, unlock).setCellValue(getLockStatusTxt(item.getLock()));
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

    private static Map<String, String> lockStatusMap = null;

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

}