package com.voyageone.task2.cms.service;

import com.csvreader.CsvWriter;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.dao.DealImportDao;
import com.voyageone.task2.cms.dao.ImagesDao;
import com.voyageone.task2.cms.dao.ProductImportDao;
import com.voyageone.task2.cms.dao.SkuImportDao;
import com.voyageone.task2.cms.model.JmBtDealImportModel;
import com.voyageone.task2.cms.model.JmBtImagesModel;
import com.voyageone.task2.cms.model.JmBtProductImportModel;
import com.voyageone.task2.cms.model.JmBtSkuImportModel;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by jerry on 2016/01/23.
 */
@Service
public class ImportExcelFileService extends BaseTaskService {

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Autowired
    private ProductImportDao productImportDao;
    @Autowired
    private SkuImportDao skuImportDao;
    @Autowired
    private DealImportDao dealImportDao;
    @Autowired
    private ImagesDao imagesDao;

    private DefaultTransactionDefinition def = new DefaultTransactionDefinition();

    private final String import_excel_file_path = "import_excel_file_path";

    // 上传Excel最大Sheet数
    private final int maxSheetCount = 4;
    private final String productSheetName = "Product";
    private final String skuSheetName = "SKU";
    private final String dealSheetName = "Product_Deal";
    private final String imageSheetName = "IMAGE";

    private final String productSheetHead = "店铺_Channel";
    private final String skuSheetHead = "Channel";
    private final String imageSheetHead = "seq";
    private final String dealSheetHead = "店铺_Channel";

    // Image Type Extend
    private final String imageTypeExtMain = "main_image";
    private final String imageTypeExtDeal = "deal_image";
    private final String imageTypeExtMobile = "mobile_image";

    // Image Type
    private final int imageTypeMain = 1;
    private final int imageTypeDeal = 2;
    private final int imageTypeMobile = 7;

    private final String GBKCharset = "GBK";

    // 专场文件前缀
    private final String preFixSpecialActivity = "S_";

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "ImportExcelFileJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {

            threads.add(new Runnable() {
                @Override
                public void run() {
                    importExcelFileMain(orderChannelID);
                }
            });

        }

        runWithThreadPool(threads, taskControlList);
    }

    /**
     * Excel导入主函数
     *
     */
    private void importExcelFileMain(String orderChannelID) {
        // 路径配置信息读取
        FtpBean filePathBean = formatSalesOrdersUploadFtpBean(orderChannelID);

        // 处理文件列表
        List<String> uploadFileList = FileUtils.getFileGroup2(filePathBean.getUpload_localpath(), ".xls");

        for (int i = 0; i < uploadFileList.size(); i++) {
            String filePath = filePathBean.getUpload_localpath() + File.separator + uploadFileList.get(i);

            if (isSpecialActivity(uploadFileList.get(i))) {
                importExcelFileForSpecialActivity(orderChannelID, filePath, uploadFileList.get(i), filePathBean);
            } else {
                importExcelFile(orderChannelID, filePath, uploadFileList.get(i), filePathBean);
            }
        }
    }

    /**
     * 专场文件判定
     *
     */
    private boolean isSpecialActivity(String fileName) {
        boolean ret = false;

        String preFix = fileName.substring(0, preFixSpecialActivity.length());
        if (preFixSpecialActivity.equals(preFix)) {
            ret = true;
        }

        return ret;
    }

    /**
     * 配置Bean生成
     *
     */
    private FtpBean formatSalesOrdersUploadFtpBean(String orderChannelID) {
        FtpBean ftpBean = new FtpBean();

        // 配置信息读取
        List<ThirdPartyConfigBean> ftpFilePaths = ThirdPartyConfigs.getThirdPartyConfigList(orderChannelID, import_excel_file_path);

        // 本地文件路径
        ftpBean.setUpload_localpath(ftpFilePaths.get(0).getProp_val1());
        // 本地文件备份路径
        ftpBean.setUpload_local_bak_path(ftpFilePaths.get(0).getProp_val2());

        return ftpBean;
    }

    /**
     * Excel 专场文件系统导入
     *
     */
    private void importExcelFileForSpecialActivity(String orderChannelID, String filePath, String fileName, FtpBean filePathBean) {
        boolean ret = true;
        // 异常返回
        List<ErrorContent> errList = new ArrayList<ErrorContent>();

        // Excel 内容缓存
        // Deal Sheet
        List<JmBtDealImportModel> dealList = new ArrayList<JmBtDealImportModel>();

        $info("上传的文档 [ %s ] 处理开始", fileName);
        try {
            File excelFile = new File(filePath);
            InputStream fileInputStream = new FileInputStream(excelFile);
            Workbook book = WorkbookFactory.create(fileInputStream);

            // Excel Sheet数检查
            $info("上传的文档 [ %s ] Sheet名检查", fileName);
            ret = chkSheet(book, fileName, errList);
            if (!ret ){
                $info("上传的文档 [ %s ] Sheet名检查异常", fileName);

            } else {

                // Deal Sheet读入
                $info("上传的文档 [ %s ] Deal Sheet读入", fileName);
                ret = readDealSheet(book, fileName, errList, dealList);
                if (!ret) {
                    $info("上传的文档 [ %s ] Deal Sheet读入异常", fileName);
                }
            }

            if (errList.size() > 0) {
                // 异常信息
                exportErrorContent(filePathBean, errList);
            } else {
                // 缓存内容DB追加
                $info("上传的文档 [ %s ] DB导入 专场货架更新", fileName);
                updateSpecialActivity(dealList, fileName, errList);
                if (errList.size() > 0) {
                    // 异常信息
                    exportErrorContent(filePathBean, errList);
                }
            }

            fileInputStream.close();

            $info("上传的文档 [ %s ] 处理结束", fileName);
        } catch (Exception e) {

            $error("importExcelFileForSpecialActivity file error = " + fileName, e);
            issueLog.log(e,
                    ErrorType.BatchJob,
                    SubSystem.CMS,
                    "importExcelFileForSpecialActivity file = " + fileName
            );
        } finally {
            moveFile(fileName, filePathBean);
        }
    }

    /**
     * 缓存内容DB追加
     *
     */
    private boolean updateSpecialActivity(
                             List<JmBtDealImportModel> dealList,
                             String fileName,
                             List<ErrorContent> errList) {
        boolean ret = true;

        for (int i = 0; i < dealList.size(); i++) {
            JmBtDealImportModel dealImportModel = dealList.get(i);
            ret = updateDealTableForSpecialActivity(dealImportModel);
            if (!ret) {
                String errorInfo = "deal id = [%s], product code = [%s] DB special activity update error";
                errorInfo = String.format(errorInfo, dealImportModel.getDealId(), dealImportModel.getProductCode());
                ErrorContent errorContent = getReadErrorContent(fileName, dealSheetName, 0, errorInfo);
                errList.add(errorContent);
            }
        }

        return ret;
    }

    /**
     * Excel文件系统导入
     *
     */
    private void importExcelFile(String orderChannelID, String filePath, String fileName, FtpBean filePathBean) {
        boolean ret = true;
        String dealId = "";
        // 异常返回
        List<ErrorContent> errList = new ArrayList<ErrorContent>();

        // Excel 内容缓存
        // Product Sheet
        List<JmBtProductImportModel> productList = new ArrayList<JmBtProductImportModel>();
        // Deal Sheet
        List<JmBtDealImportModel> dealList = new ArrayList<JmBtDealImportModel>();
        // Image Sheet
        List<JmBtImagesModel> imageList = new ArrayList<JmBtImagesModel>();
        // Sku Sheet
        List<JmBtSkuImportModel> skuList = new ArrayList<JmBtSkuImportModel>();

        $info("上传的文档 [ %s ] 处理开始", fileName);
        try {
            File excelFile = new File(filePath);
            InputStream fileInputStream = new FileInputStream(excelFile);
            Workbook book = WorkbookFactory.create(fileInputStream);

            // Excel Sheet数检查
            $info("上传的文档 [ %s ] Sheet名检查", fileName);
            ret = chkSheet(book, fileName, errList);
            if (!ret ){
                $info("上传的文档 [ %s ] Sheet名检查异常", fileName);

            } else {
                // Sku Sheet读入
                $info("上传的文档 [ %s ] Sku Sheet读入", fileName);
                ArrayList<Object> retArr = readSkuSheet(book, fileName, errList, skuList);
                ret = (boolean)retArr.get(0);
                dealId = (String)retArr.get(1);
                if (!ret) {
                    $info("上传的文档 [ %s ]Sku Sheet读入异常", fileName);
                }

                // Image Sheet读入
                $info("上传的文档 [ %s ] Image Sheet读入", fileName);
                ret = readImageSheet(book, fileName, errList, imageList);
                if (!ret) {
                    $info("上传的文档 [ %s ] Image Sheet读入异常", fileName);
                }

                // Deal Sheet读入
                $info("上传的文档 [ %s ] Deal Sheet读入", fileName);
                ret = readDealSheet(book, fileName, errList, dealList);
                if (!ret) {
                    $info("上传的文档 [ %s ] Deal Sheet读入异常", fileName);
                }

                // Product Sheet读入
                $info("上传的文档 [ %s ] Product Sheet读入", fileName);
                ret = readProductSheet(book, fileName, errList, productList, imageList);
                if (!ret) {
                    $info("上传的文档 [ %s ] Product Sheet读入异常", fileName);
                }
            }

            if (errList.size() > 0) {
                // 异常信息
                exportErrorContent(filePathBean, errList);
            } else {
                // 缓存内容DB追加
                $info("上传的文档 [ %s ] DB导入", fileName);
                insertDB(productList, dealList, imageList, skuList, fileName, errList);
                if (errList.size() > 0) {
                    // 异常信息
                    exportErrorContent(filePathBean, errList);
                }

                // 标志位关联更新
                $info("上传的文档 [ %s ] Product 同步", fileName);
                synProductImportSynFlg(orderChannelID, dealId);
            }

            fileInputStream.close();

            $info("上传的文档 [ %s ] 处理结束", fileName);
        } catch (Exception e) {

            $error("importExcelFile file error = " + fileName, e);
            issueLog.log(e,
                    ErrorType.BatchJob,
                    SubSystem.CMS,
                    "importExcelFile file = " + fileName
            );
        } finally {

            moveFile(fileName, filePathBean);
        }
    }

    /**
     * 处理文件移动
     *
     */
    private void moveFile(String fileName, FtpBean filePathBean) {
        try {
            // 文件移动
            $info("上传的文档 [ %s ] 备份目录夹移动", fileName);
            // 源文件
            String srcFile = filePathBean.getUpload_localpath() + "/" + fileName;
            // 目标文件
            String destFile = filePathBean.getUpload_local_bak_path() + "/" + fileName;
            $info("moveFile = " + srcFile + " " + destFile);
            FileUtils.moveFile(srcFile, destFile);
        } catch (Exception e) {
            $error("moveFile file error = " + fileName, e);
            issueLog.log(e,
                    ErrorType.BatchJob,
                    SubSystem.CMS,
                    "moveFile file error fileName = " + fileName);
        }
    }

    /**
     * product 标志位同步
     *
     */
    private void synProductImportSynFlg(String orderChannelID, String dealId) {

        // 同步Product 标志位，根据SkuImport
        syncProductImportSynFlgBySkuImport(orderChannelID, dealId);

        // 同步Product 标志位，根据DealImport
        syncProductImportSynFlgByDealImport(orderChannelID, dealId);

        // 同步Product 标志位，根据Image
        syncProductImportSynFlgByImage(orderChannelID, dealId);
    }

    /**
     * 缓存内容DB追加
     *
     */
    private boolean insertDB(List<JmBtProductImportModel> productList,
                List<JmBtDealImportModel> dealList,
                List<JmBtImagesModel> imageList,
                List<JmBtSkuImportModel> skuList,
                String fileName,
                List<ErrorContent> errList) {
            boolean ret = true;

        $info("上传的文档 [ %s ] DB导入 Product", fileName);
        for (int i = 0; i < productList.size(); i++) {
            JmBtProductImportModel productImportModel = productList.get(i);
            ret = insertProductTable(productImportModel);
            if (!ret) {
                String errorInfo = "product code = [%s] DB insert error";
                errorInfo = String.format(errorInfo, productImportModel.getProductCode());
                ErrorContent errorContent = getReadErrorContent(fileName, productSheetName, 0, errorInfo);
                errList.add(errorContent);
            }
        }

        $info("上传的文档 [ %s ] DB导入 Deal", fileName);
        for (int i = 0; i < dealList.size(); i++) {
            JmBtDealImportModel dealImportModel = dealList.get(i);
            ret = insertDealTable(dealImportModel);
            if (!ret) {
                String errorInfo = "deal id = [%s], product code = [%s] DB insert error";
                errorInfo = String.format(errorInfo, dealImportModel.getDealId(), dealImportModel.getProductCode());
                ErrorContent errorContent = getReadErrorContent(fileName, dealSheetName, 0, errorInfo);
                errList.add(errorContent);
            }
        }

        $info("上传的文档 [ %s ] DB导入 Image", fileName);
        for (int i = 0; i < imageList.size(); i++) {
//            $info("上传的文档 [ %s ] DB导入 Image index = [%s]", fileName, i);
            JmBtImagesModel imagesModel = imageList.get(i);
            ret = insertImagesTable(imagesModel);
            if (!ret) {
                String errorInfo = "image_key = [%s], origin_url = [%s] DB insert error";
                errorInfo = String.format(errorInfo, imagesModel.getImageKey(), imagesModel.getOriginUrl());
                ErrorContent errorContent = getReadErrorContent(fileName, imageSheetName, 0, errorInfo);
                errList.add(errorContent);
            }
        }

        $info("上传的文档 [ %s ] DB导入 Sku", fileName);
        for (int i = 0; i < skuList.size(); i++) {
//            $info("上传的文档 [ %s ] DB导入 Sku index = [%s]", fileName, i);
            JmBtSkuImportModel skuImportModel = skuList.get(i);
            ret = insertSkuTable(skuImportModel);
            if (!ret) {
                String errorInfo = "sku = [%s] DB insert error";
                errorInfo = String.format(errorInfo, skuImportModel.getSku());
                ErrorContent errorContent = getReadErrorContent(fileName, skuSheetName, 0, errorInfo);
                errList.add(errorContent);
            }
        }

        return ret;
    }

    /**
     * 同步Product 标志位，根据SkuImport
     *
     */
    private void syncProductImportSynFlgBySkuImport(String orderChannelID, String dealId) {
        JmBtSkuImportModel jmBtSkuImportModel = new JmBtSkuImportModel();
        jmBtSkuImportModel.setChannelId(orderChannelID);
        jmBtSkuImportModel.setDealId(dealId);
        jmBtSkuImportModel.setModifier(getTaskName());

        skuImportDao.updateProductImportInfoBySkuImport(jmBtSkuImportModel);
    }

    /**
     * 同步Product 标志位，根据DealImport
     *
     */
    private void syncProductImportSynFlgByDealImport(String orderChannelID, String dealId) {
        JmBtDealImportModel jmBtDealImportModel = new JmBtDealImportModel();
        jmBtDealImportModel.setChannelId(orderChannelID);
        jmBtDealImportModel.setDealId(dealId);
        jmBtDealImportModel.setModifier(getTaskName());

        dealImportDao.updateProductImportInfoByDealImport(jmBtDealImportModel);
    }

    /**
     * 同步Product 标志位，根据Image
     *
     */
    private void syncProductImportSynFlgByImage(String orderChannelID, String dealId) {

        imagesDao.updateProductImportInfoByImage(orderChannelID, dealId, getTaskName());
    }

    /**
     * Excel文件Sheet数检查
     *
     */
    private boolean chkSheet(Workbook book, String fileName, List<ErrorContent> errList) {
        boolean ret = true;

//        Sheet temp = book.getSheetAt(maxSheetCount - 1);
//        if (temp != null ){
//            ret = true;
//        }

        Sheet sheetTemp = book.getSheet(productSheetName);
        if (sheetTemp == null){
            ret = false;
        }

        if (ret) {
            sheetTemp = book.getSheet(skuSheetName);
            if (sheetTemp == null) {
                ret = false;
            }
        }

        if (ret) {
            sheetTemp = book.getSheet(dealSheetName);
            if (sheetTemp == null) {
                ret = false;
            }
        }

        if (ret) {
            sheetTemp = book.getSheet(imageSheetName);
            if (sheetTemp == null) {
                ret = false;
            }
        }

        if (!ret) {
            ErrorContent errorContent = getReadErrorContent(fileName, "", 0, "Sheet Name Chk Error [Product][SKU][Product_Deal][IMAGE]");
            errList.add(errorContent);
        }

        return ret;
    }

    /**
     * 异常Bean生成
     */
    private ErrorContent getReadErrorContent(String fileName, String sheetName, int rowNum) {
        ErrorContent errorContent = new ErrorContent();

        errorContent.setFileName(fileName);
        errorContent.setSheetName(sheetName);
        errorContent.setRowNum(rowNum);
        errorContent.setContent("Excel File Read Error");

        return errorContent;
    }

    /**
     * 异常Bean生成
     */
    private ErrorContent getReadErrorContent(String fileName, String sheetName, int rowNum, String content) {
        ErrorContent errorContent = new ErrorContent();

        errorContent.setFileName(fileName);
        errorContent.setSheetName(sheetName);
        errorContent.setRowNum(rowNum);
        errorContent.setContent(content);

        return errorContent;
    }

    /**
     * 产品图片状态判定
     *
     */
    private boolean isProductImagesUpload(JmBtProductImportModel productImportModel) {
        boolean ret = false;

        JmBtImagesModel jmBtImagesModelPara = new JmBtImagesModel();
        jmBtImagesModelPara.setChannelId(productImportModel.getChannelId());
        jmBtImagesModelPara.setImageKey(productImportModel.getProductCode());

        List<JmBtImagesModel> imagesList = imagesDao.getImagesBySynFlg(jmBtImagesModelPara);
        // 图片存在的场合
        if (imagesList.size() > 0) {
            // 图片状态为一种的场合（全部未上传，或全部已上传）
            if (imagesList.size() == 1) {
                JmBtImagesModel jmBtImagesModel = imagesList.get(0);

                // 上传完成的场合
                if (jmBtImagesModel.getSynFlg() == 1) {
                    ret = true;
                }
            }
        }

        return ret;
    }

    /**
     * prduct Sheet Head部判定
     *
     */
    private boolean isProductHeadRow(Row row) {
        boolean ret = false;

        if (productSheetHead.equals(ExcelUtils.getString(row, PruductSheetFormat.channel_id_index))) {
            ret = true;
        }

        return ret;
    }

    /**
     * Sku Sheet Head部判定
     *
     */
    private boolean isSkuHeadRow(Row row) {
        boolean ret = false;

        if (skuSheetHead.equals(ExcelUtils.getString(row, SkuSheetFormat.channel_id_index))) {
            ret = true;
        }

        return ret;
    }

    /**
     * Image Sheet Head部判定
     *
     */
    private boolean isImageHeadRow(Row row) {
        boolean ret = false;

        if (imageSheetHead.equals(ExcelUtils.getString(row, ImageSheetFormat.seq_index))) {
            ret = true;
        }

        return ret;
    }

    /**
     * Deal Sheet Head部判定
     *
     */
    private boolean isDealHeadRow(Row row) {
        boolean ret = false;

        if (dealSheetHead.equals(ExcelUtils.getString(row, DealSheetFormat.channel_id_index))) {
            ret = true;
        }

        return ret;
    }

    /**
     * 根据上传Excel，Image Bean生成
     */
    private List<JmBtImagesModel> getProductImageModel(Row row) {
        List<JmBtImagesModel> ret = new ArrayList<JmBtImagesModel>();

        try {
            // 产品主图
            int mainImageStrIndex = PruductSheetFormat.main_image_1_index;
            int mainImageEndIndex = PruductSheetFormat.main_image_6_index;
            int mainImageIndex = 1;
            for (int i = mainImageStrIndex; i <= mainImageEndIndex; i++) {
                String originUrl = ExcelUtils.getString(row, i);
                if (StringUtils.isEmpty(originUrl)) {
                    continue;
                }
                JmBtImagesModel imagesModel = getImageModelByProductRow(row);
                imagesModel.setImageType(imageTypeMain);
                imagesModel.setImageTypeExtend(imageTypeExtMain);
                imagesModel.setImageIndex(mainImageIndex);
                imagesModel.setOriginUrl(originUrl);
                ret.add(imagesModel);

                mainImageIndex = mainImageIndex + 1;
            }

            // 商品实拍图
            int dealImageStrIndex = PruductSheetFormat.deal_image_1_index;
            int dealImageEndIndex = PruductSheetFormat.deal_image_6_index;
            int dealImageIndex = 1;
            for (int i = dealImageStrIndex; i <= dealImageEndIndex; i++) {
                String originUrl = ExcelUtils.getString(row, i);
                if (StringUtils.isEmpty(originUrl)) {
                    continue;
                }
                JmBtImagesModel imagesModel = getImageModelByProductRow(row);
                imagesModel.setImageType(imageTypeDeal);
                imagesModel.setImageTypeExtend(imageTypeExtDeal);
                imagesModel.setImageIndex(dealImageIndex);
                imagesModel.setOriginUrl(originUrl);
                ret.add(imagesModel);

                dealImageIndex = dealImageIndex + 1;
            }

            // 竖图
            int mobileImageStrIndex = PruductSheetFormat.mobile_image_1_index;
            int mobileImageEndIndex = PruductSheetFormat.mobile_image_6_index;
            int mobileImageIndex = 1;
            for (int i = mobileImageStrIndex; i <= mobileImageEndIndex; i++) {
                String originUrl = ExcelUtils.getString(row, i);
                if (StringUtils.isEmpty(originUrl)) {
                    continue;
                }
                JmBtImagesModel imagesModel = getImageModelByProductRow(row);
                imagesModel.setImageType(imageTypeMobile);
                imagesModel.setImageTypeExtend(imageTypeExtMobile);
                imagesModel.setImageIndex(mobileImageIndex);
                imagesModel.setOriginUrl(originUrl);
                ret.add(imagesModel);

                mobileImageIndex = mobileImageIndex + 1;
            }
        } catch (Exception e) {
            $error("importExcelFile getProductImageModel error", e);
            ret = null;
        }
        return ret;
    }

    /**
     * 根据ProductRow，Image对象生成
     */
    private JmBtImagesModel getImageModelByProductRow(Row row) {
        JmBtImagesModel imagesModel = new JmBtImagesModel();
        imagesModel.setChannelId(ExcelUtils.getString(row, PruductSheetFormat.channel_id_index));
        imagesModel.setImageKey(ExcelUtils.getString(row, PruductSheetFormat.product_code_index, "#"));

        imagesModel.setSynFlg(0);
        imagesModel.setCreater(getTaskName());
        imagesModel.setModifier(getTaskName());

        return imagesModel;
    }

    /**
     * 根据上传Excel，Product Bean生成
     */
    private JmBtProductImportModel getProductModel(Row row) {
        JmBtProductImportModel productModel = new JmBtProductImportModel();
        try {
            productModel.setChannelId(ExcelUtils.getString(row, PruductSheetFormat.channel_id_index));
            productModel.setProductCode(ExcelUtils.getString(row, PruductSheetFormat.product_code_index, "#"));
            productModel.setDealId(ExcelUtils.getString(row, PruductSheetFormat.deal_id_index));

            String productDes = ExcelUtils.getString(row, PruductSheetFormat.product_des_index);
            // <img> 元素删除
            productModel.setProductDes(StringUtils.trimImgElement(productDes));

            productModel.setCategoryLv4Id(Integer.valueOf(ExcelUtils.getString(row, PruductSheetFormat.category_lv4_id_index, "#")));
            productModel.setBrandId(Integer.valueOf(ExcelUtils.getString(row, PruductSheetFormat.brand_id_index, "#")));
            productModel.setBrandName(ExcelUtils.getString(row, PruductSheetFormat.brand_name_index));
            productModel.setSizeType(ExcelUtils.getString(row, PruductSheetFormat.size_type_index));
            productModel.setProductName(ExcelUtils.getString(row, PruductSheetFormat.product_name_index));
            productModel.setForeignLanguageName(ExcelUtils.getString(row, PruductSheetFormat.foreign_language_name_index));
//        productModel.setFunctionIds(ExcelUtils.getString(row, PruductSheetFormat.function_ids_index));
            productModel.setFunctionIds("");

            productModel.setAttribute(ExcelUtils.getString(row, PruductSheetFormat.attribute_index));
            productModel.setAddressOfProduce(ExcelUtils.getString(row, PruductSheetFormat.address_of_produce_index));
            productModel.setHsCode(ExcelUtils.getString(row, PruductSheetFormat.hs_code_index, "#"));
            productModel.setSpecialNote(ExcelUtils.getString(row, PruductSheetFormat.special_note_index));

            productModel.setSynFlg("0");
            productModel.setCreater(getTaskName());
            productModel.setModifier(getTaskName());

            // 产品图片上传标志再设定
            if (isProductImagesUpload(productModel)) {
                productModel.setSynFlg("1");
            }
        } catch (Exception e) {
            $error("importExcelFile getProductModel error", e);
            productModel = null;
        }


        return productModel;
    }

    /**
     * 根据上传Excel，Sku Bean生成
     *
     */
    private JmBtSkuImportModel getSkuModel(Row row) {
        JmBtSkuImportModel skuModel = new JmBtSkuImportModel();
        try {

            skuModel.setChannelId(ExcelUtils.getString(row, SkuSheetFormat.channel_id_index));
            skuModel.setProductCode(ExcelUtils.getString(row, SkuSheetFormat.product_code_index, "#"));
            skuModel.setDealId(ExcelUtils.getString(row, SkuSheetFormat.deal_id_index));
            skuModel.setSku(ExcelUtils.getString(row, SkuSheetFormat.sku_index, "#"));
            skuModel.setUpcCode(ExcelUtils.getString(row, SkuSheetFormat.upc_code_index, "#"));
            skuModel.setAbroadPrice(CommonUtil.getRoundUp2Digits(Double.valueOf(ExcelUtils.getString(row, SkuSheetFormat.abroad_price_index))));
            skuModel.setDealPrice(CommonUtil.getRoundUp2Digits(ExcelUtils.getNum(row, SkuSheetFormat.deal_price_index)));
            skuModel.setMarketPrice(CommonUtil.getRoundUp2Digits(ExcelUtils.getNum(row, SkuSheetFormat.market_price_index)));
            skuModel.setSize(ExcelUtils.getString(row, SkuSheetFormat.size_index));
//        skuModel.setHscode(ExcelUtils.getString(row, SkuSheetFormat.hscode_index));
            skuModel.setHscode("");

            skuModel.setCreater(getTaskName());
            skuModel.setModifier(getTaskName());
        } catch (Exception e) {
            $error("importExcelFile getSkuModel error", e);
            skuModel = null;
        }


        return skuModel;
    }

    /**
     * 根据上传Excel，Image Bean生成
     *
     */
    private JmBtImagesModel getImageModel(Row row) {
        JmBtImagesModel imagesModel = new JmBtImagesModel();
        try {
            imagesModel.setChannelId(ExcelUtils.getString(row, ImageSheetFormat.channel_id_index));
            imagesModel.setImageKey(ExcelUtils.getString(row, ImageSheetFormat.image_key_index));
            imagesModel.setImageType(Integer.valueOf(ExcelUtils.getString(row, ImageSheetFormat.image_type_index, "#")));
            imagesModel.setImageTypeExtend(ExcelUtils.getString(row, ImageSheetFormat.image_type_extend_index));
            imagesModel.setImageIndex(Integer.valueOf(ExcelUtils.getString(row, ImageSheetFormat.image_index, "#")));
            imagesModel.setOriginUrl(ExcelUtils.getString(row, ImageSheetFormat.origin_url_index));
            imagesModel.setJmUrl(ExcelUtils.getString(row, ImageSheetFormat.jm_url_index));

            imagesModel.setSynFlg(0);
            imagesModel.setCreater(getTaskName());
            imagesModel.setModifier(getTaskName());
        } catch (Exception e) {
            $error("importExcelFile getImageModel error", e);
            imagesModel = null;
        }


        return imagesModel;
    }

    /**
     * 根据上传Excel，Deal Bean生成
     *
     */
    private JmBtDealImportModel getDealModel(Row row) {
        JmBtDealImportModel dealModel = new JmBtDealImportModel();
        try {
            dealModel.setChannelId(ExcelUtils.getString(row, DealSheetFormat.channel_id_index));
            dealModel.setProductCode(ExcelUtils.getString(row, DealSheetFormat.product_code_index, "#"));
            dealModel.setDealId(ExcelUtils.getString(row, DealSheetFormat.deal_id_index));
            dealModel.setStartTime(ExcelUtils.getString(row, DealSheetFormat.start_time_index));
            dealModel.setEndTime(ExcelUtils.getString(row, DealSheetFormat.end_time_index));
            //        dealModel.setStartTime("2015-01-01 11:11:00");
            //        dealModel.setEndTime("2015-01-01 11:11:00");

            dealModel.setUserPurchaseLimit(Integer.valueOf(ExcelUtils.getString(row, DealSheetFormat.user_purchase_limit_index, "#")));
            //        dealModel.setUserPurchaseLimit(2);

            dealModel.setShippingSystemId(Integer.valueOf(ExcelUtils.getString(row, DealSheetFormat.shipping_system_id_index, "#")));
            dealModel.setProductLongName(ExcelUtils.getString(row, DealSheetFormat.product_long_name_index));
            dealModel.setProductMediumName(ExcelUtils.getString(row, DealSheetFormat.product_medium_name_index));
            dealModel.setProductShortName(ExcelUtils.getString(row, DealSheetFormat.product_short_name_index));
            dealModel.setSearchMetaTextCustom(ExcelUtils.getString(row, DealSheetFormat.search_meta_text_custom_index));

            dealModel.setSynFlg(0);
            dealModel.setCreater(getTaskName());
            dealModel.setModifier(getTaskName());

            // 专场对应
            dealModel.setSpecialActivityId1(ExcelUtils.getString(row, DealSheetFormat.special_activity_id1_index));
            dealModel.setShelfId1(ExcelUtils.getString(row, DealSheetFormat.shelf_id1_index));
            dealModel.setSpecialActivityId2(ExcelUtils.getString(row, DealSheetFormat.special_activity_id2_index));
            dealModel.setShelfId2(ExcelUtils.getString(row, DealSheetFormat.shelf_id2_index));
            dealModel.setSpecialActivityId3(ExcelUtils.getString(row, DealSheetFormat.special_activity_id3_index));
            dealModel.setShelfId3(ExcelUtils.getString(row, DealSheetFormat.shelf_id3_index));
        }catch (Exception e) {
            $error("importExcelFile getDealModel error", e);
            dealModel = null;
        }

        return dealModel;
    }

    /**
     * Product Bean检查
     *
     */
    private boolean chkProductImportBean(JmBtProductImportModel productImportModel, String fileName, int rowNum, List<ErrorContent> errList) {
        boolean ret = true;
        StringBuffer errContent = new StringBuffer();

        // name
        if (!chkLength(productImportModel.getProductName(), PruductSheetFormat.product_name_length)) {
            errContent.append(" product_name_length chk error ");
            ret = false;
        }

        // foreign_language_name
        if (!chkLength(productImportModel.getForeignLanguageName(), PruductSheetFormat.foreign_language_name_length)) {
            errContent.append(" foreign_language_name_length chk error ");
            ret = false;
        }

        // address_of_produce
        if (!chkLength(productImportModel.getAddressOfProduce(), PruductSheetFormat.address_of_produce_length)) {
            errContent.append(" address_of_produce_length chk error ");
            ret = false;
        }

        // special_note 空检查
        if (StringUtils.isEmpty(productImportModel.getSpecialNote())) {
            errContent.append(" special_note is empty ");
            ret = false;
        }//             长度检查
        if (!chkLength(productImportModel.getSpecialNote(), PruductSheetFormat.special_note_length)) {
            errContent.append(" special_note_length chk error ");
            ret = false;
        }

        if(!ret) {
            ErrorContent errorContent = getReadErrorContent(fileName, productSheetName, rowNum, errContent.toString());
            errList.add(errorContent);
        }

        return ret;
    }

    /**
     * Product 主图检查
     *
     */
    private boolean chkProductImagesBean(List<JmBtImagesModel> imagesModelList, String fileName, int rowNum, List<ErrorContent> errList) {
        boolean ret = false;
        StringBuffer errContent = new StringBuffer();

        for (int i = 0; i < imagesModelList.size(); i++) {
            JmBtImagesModel imagesModel = imagesModelList.get(i);
            if (imageTypeMain == imagesModel.getImageType()) {
                ret = true;
                break;
            }
        }
        if (!ret) {
            errContent.append(" main_image is not exist ");

            ErrorContent errorContent = getReadErrorContent(fileName, productSheetName, rowNum, errContent.toString());
            errList.add(errorContent);
        }


        return ret;
    }

    /**
     * Deal Bean检查
     *
     */
    private boolean chkDealImportBean(JmBtDealImportModel dealImportModel, String fileName, int rowNum, List<ErrorContent> errList) {
        boolean ret = true;
        StringBuffer errContent = new StringBuffer();

        // product_long_name
        if (!chkLength(dealImportModel.getProductLongName(), DealSheetFormat.product_long_name_length)) {
            errContent.append(" product_long_name_length chk error ");
            ret = false;
        }

        // product_medium_name
        if (!chkLength(dealImportModel.getProductMediumName(), DealSheetFormat.product_medium_name_length)) {
            errContent.append(" product_medium_name_length chk error ");
            ret = false;
        }

        // product_short_name
        if (!chkLength(dealImportModel.getProductShortName(), DealSheetFormat.product_short_name_length)) {
            errContent.append(" product_short_name_length chk error ");
            ret = false;
        }

        // 日期检查
        if (!chkDate(dealImportModel.getStartTime(), dealImportModel.getEndTime())) {
            errContent.append(" start_time > end_time ");
            ret = false;
        }

        if(!ret) {
            ErrorContent errorContent = getReadErrorContent(fileName, dealSheetName, rowNum, errContent.toString());
            errList.add(errorContent);
        }
        return ret;
    }

    /**
     * Sku Bean检查
     *
     */
    private boolean chkSkuBean(JmBtSkuImportModel skuModel, String fileName, int rowNum, List<ErrorContent> errList) {
        boolean ret = true;
        StringBuffer errContent = new StringBuffer();

        // deal_price
        if (skuModel.getDealPrice() <= SkuSheetFormat.deal_price_min_value) {
            errContent.append(" deal_price <= " + SkuSheetFormat.deal_price_min_value);
            ret = false;
        }

        if(!ret) {
            ErrorContent errorContent = getReadErrorContent(fileName, skuSheetName, rowNum, errContent.toString());
            errList.add(errorContent);
        }
        return ret;
    }

    /**
     * Image Bean检查
     *
     */
    private boolean chkImagesBean(JmBtImagesModel imagesModel, String fileName, int rowNum, List<ErrorContent> errList) {
        boolean ret = true;
        StringBuffer errContent = new StringBuffer();

        // origin_url
        if (StringUtils.isEmpty(imagesModel.getOriginUrl())) {
            errContent.append(" origin_url is empty ");
            ret = false;
        }

        if(!ret) {
            ErrorContent errorContent = getReadErrorContent(fileName, imageSheetName, rowNum, errContent.toString());
            errList.add(errorContent);
        }
        return ret;
    }

    /**
     * Product Bean数据库追加
     *
     */
    private boolean insertProductTable(JmBtProductImportModel productImportModel) {
        boolean ret = true;

        try {
            ret = productImportDao.insertProductImportInfo(productImportModel);
        } catch (Exception e) {
            ret = false;
            $error("insertProductTable error productCode = " + productImportModel.getProductCode(), e);
            issueLog.log(e,
                    ErrorType.BatchJob,
                    SubSystem.CMS,
                    "Channel Id, Product code = " + productImportModel.getChannelId() + "," + productImportModel.getProductCode());
        }

        return ret;
    }

    /**
     * Sku Bean数据库追加
     *
     */
    private boolean insertSkuTable(JmBtSkuImportModel skuImportModel) {
        boolean ret = true;

        try {
            ret = skuImportDao.insertSkuImportInfo(skuImportModel);
        } catch (Exception e) {
            ret = false;
            $error("insertSkuTable error sku = " + skuImportModel.getSku(), e);
            issueLog.log(e,
                    ErrorType.BatchJob,
                    SubSystem.CMS,
                    "Channel Id, sku = " + skuImportModel.getChannelId() + "," + skuImportModel.getSku());
        }

        return ret;
    }

    /**
     * Deal Bean数据库追加
     *
     */
    private boolean insertDealTable(JmBtDealImportModel dealImportModel) {
        boolean ret = true;

        try {
            ret = dealImportDao.insertDealImportInfo(dealImportModel);
        } catch (Exception e) {
            ret = false;
            $error("insertDealTable error dealId, productCode = " + dealImportModel.getDealId() + " , " + dealImportModel.getProductCode(), e);
            issueLog.log(e,
                    ErrorType.BatchJob,
                    SubSystem.CMS,
                    "Channel Id, Deal Id, Product Code = " + dealImportModel.getChannelId() + "," + dealImportModel.getDealId() + "," + dealImportModel.getProductCode());
        }

        return ret;
    }

    /**
     * Deal Bean数据库追加
     *
     */
    private boolean updateDealTableForSpecialActivity(JmBtDealImportModel dealImportModel) {
        boolean ret = true;

        try {
            int recCount = dealImportDao.updateDealImportInfoForSpecialActivity(dealImportModel);
            if (recCount == 0) {
                ret = false;
                $error("updateDealTableForSpecialActivity error record not found. dealId, productCode = " + dealImportModel.getDealId() + " , " + dealImportModel.getProductCode());
                issueLog.log("updateDealTableForSpecialActivity",
                        "Channel Id, Deal Id, Product Code = " + dealImportModel.getChannelId() + "," + dealImportModel.getDealId() + "," + dealImportModel.getProductCode(),
                        ErrorType.BatchJob,
                        SubSystem.CMS);
            }
        } catch (Exception e) {
            ret = false;
            $error("updateDealTableForSpecialActivity error dealId, productCode = " + dealImportModel.getDealId() + " , " + dealImportModel.getProductCode(), e);
            issueLog.log(e,
                    ErrorType.BatchJob,
                    SubSystem.CMS,
                    "Channel Id, Deal Id, Product Code = " + dealImportModel.getChannelId() + "," + dealImportModel.getDealId() + "," + dealImportModel.getProductCode());
        }

        return ret;
    }

    /**
     * Images Bean数据库追加
     *
     */
    private boolean insertImagesTable(JmBtImagesModel imagesModel) {
        boolean ret = true;

        try {
            ret = imagesDao.insertImagesInfo(imagesModel);
        } catch (Exception e) {
            ret = false;
            $error("insertImagesTable error OriginUrl = " + imagesModel.getOriginUrl(), e);
            issueLog.log(e,
                    ErrorType.BatchJob,
                    SubSystem.CMS,
                    "Channel Id, Origin Url = " + imagesModel.getChannelId() + "," + imagesModel.getOriginUrl());
        }

        return ret;
    }

    /**
     * Images Bean数据库追加
     *
     */
    private boolean insertImagesTableForList(List<JmBtImagesModel> imagesModelList) {
        boolean ret = true;

        for(int i = 0; i < imagesModelList.size(); i++) {
            JmBtImagesModel imagesModel = imagesModelList.get(i);

            ret = imagesDao.insertImagesInfo(imagesModel);
            if (!ret) {
                break;
            }
        }

        return ret;
    }

    /**
     * prduct Sheet 读入
     *
     */
    private boolean readProductSheet(Workbook book, String fileName, List<ErrorContent> errList, List<JmBtProductImportModel> productList, List<JmBtImagesModel> imageList) {
        boolean ret = true;

        Sheet productSheet = book.getSheet(productSheetName);
        for (Row row : productSheet) {
            boolean retRow = true;

            if (isProductHeadRow(row)) {
                continue;
            }

            // 结束行判定
            if (StringUtils.isEmpty(ExcelUtils.getString(row, PruductSheetFormat.channel_id_index))) {
                $info("readProductSheet end rownum = " + row.getRowNum());
                break;
            }

            // Product bean 生成
            JmBtProductImportModel productImportModel = getProductModel(row);
            // Image bean 生成
            List<JmBtImagesModel> imagesModelList = getProductImageModel(row);

            // 读入异常的场合
            if (productImportModel == null || imagesModelList == null) {
                ret = false;
                retRow = false;

//                $info("readProductSheet error rownum = " + row.getRowNum());
                $info("readProductSheet error filename = " + fileName + " rownum = " + row.getRowNum());
                ErrorContent errorContent = getReadErrorContent(fileName, productSheetName, row.getRowNum());
                errList.add(errorContent);
            }

            if (retRow) {
                // 内容检查
                retRow = chkProductImportBean(productImportModel, fileName, row.getRowNum(), errList);
                if (!retRow) {
                    ret = false;
                }
            }

            if (retRow) {
                // 关联图片检查
                retRow = chkProductImagesBean(imagesModelList, fileName, row.getRowNum(), errList);
                if (!retRow) {
                    ret = false;
                }
            }

            if (retRow) {
                // Product数据库追加
//                ret = insertProductTable(productImportModel);
                productList.add(productImportModel);

                // Image数据库追加
//                ret = insertImagesTableForList(imagesModelList);
                imageList.addAll(imagesModelList);
            }
        }

        return ret;
    }

    /**
     * sku Sheet 读入
     *
     */
    private ArrayList<Object> readSkuSheet(Workbook book, String fileName, List<ErrorContent> errList, List<JmBtSkuImportModel> skuList) {
        ArrayList<Object> retArr = new ArrayList<Object>();
        boolean ret = true;
        String dealId = "";

        Sheet skuSheet = book.getSheet(skuSheetName);
        for (Row row : skuSheet) {
            boolean retRow = true;

            // 标题行判定
            if (isSkuHeadRow(row)) {
                continue;
            }

            // 结束行判定
            if (StringUtils.isEmpty(ExcelUtils.getString(row, SkuSheetFormat.channel_id_index))) {
                $info("readSkuSheet end rownum = " + row.getRowNum());
                break;
            }

            // Sku bean 生成
            JmBtSkuImportModel skuImportModel = getSkuModel(row);
            // 读入异常的场合
            if (skuImportModel == null) {
                ret = false;
                retRow = false;

//                $info("readSkuSheet error rownum = " + row.getRowNum());
                $info("readSkuSheet error filename = " + fileName + " rownum = " + row.getRowNum());
                ErrorContent errorContent = getReadErrorContent(fileName, skuSheetName, row.getRowNum());
                errList.add(errorContent);
            } else {
                dealId = skuImportModel.getDealId();
            }

            if (retRow) {
                // 内容检查
                retRow = chkSkuBean(skuImportModel, fileName, row.getRowNum(), errList);
                if (!retRow) {
                    ret = false;
                }
            }

            // 数据库追加
            if (retRow) {
//                ret = insertSkuTable(skuImportModel);
                skuList.add(skuImportModel);
            }
        }

        retArr.add(ret);
        retArr.add(dealId);

        return retArr;
    }

    /**
     * Image Sheet 读入
     *
     */
    private boolean readImageSheet(Workbook book, String fileName, List<ErrorContent> errList, List<JmBtImagesModel> imageList) {
        boolean ret = true;

        Sheet imageSheet = book.getSheet(imageSheetName);
        for (Row row : imageSheet) {
            boolean retRow = true;

            if (isImageHeadRow(row)) {
                continue;
            }

            // 结束行判定
            if (StringUtils.isEmpty(ExcelUtils.getString(row, ImageSheetFormat.seq_index))) {
                $info("readImageSheet end rownum = " + row.getRowNum());
                break;
            }

            // Images bean 生成
            JmBtImagesModel imagesModel = getImageModel(row);
            if (imagesModel == null) {
                ret = false;
                retRow = false;

//                $info("readImageSheet error rownum = " + row.getRowNum());
                $info("readImageSheet error filename = " + fileName + " rownum = " + row.getRowNum());
                ErrorContent errorContent = getReadErrorContent(fileName, imageSheetName, row.getRowNum());
                errList.add(errorContent);
            }

            // 内容检查
            if (retRow) {
                retRow = chkImagesBean(imagesModel, fileName, row.getRowNum(), errList);
                if (!retRow) {
                    ret = false;
                }
            }

            // 数据库追加
            if (retRow) {
//                ret = insertImagesTable(imagesModel);
                imageList.add(imagesModel);
            }
        }

        return ret;
    }

    /**
     * Deal Sheet 读入
     *
     */
    private boolean readDealSheet(Workbook book, String fileName, List<ErrorContent> errList, List<JmBtDealImportModel> dealList) {
        boolean ret = true;
        boolean isDataRow = false;

        Sheet dealSheet = book.getSheet(dealSheetName);
        for (Row row : dealSheet) {
            boolean retRow = true;

            if (!isDataRow) {
                if (isDealHeadRow(row)) {
                    isDataRow = true;
                    continue;
                }
            }

            // 结束行判定
            if (StringUtils.isEmpty(ExcelUtils.getString(row, DealSheetFormat.channel_id_index))) {
                $info("readDealSheet end rownum = " + row.getRowNum());
                break;
            }

            // Deal bean 生成
            JmBtDealImportModel dealImportModel = getDealModel(row);
            if (dealImportModel == null) {
                ret = false;
                retRow = false;

//                $info("readDealSheet error rownum = " + row.getRowNum());
                $info("readDealSheet error filename = " + fileName + " rownum = " + row.getRowNum());
                ErrorContent errorContent = getReadErrorContent(fileName, dealSheetName, row.getRowNum());
                errList.add(errorContent);
            }

            if (retRow) {
                // 内容检查
                retRow = chkDealImportBean(dealImportModel, fileName, row.getRowNum(), errList);
                if (!retRow) {
                    ret = false;
                }
            }

            // 数据库追加
            if (retRow) {
//                ret = insertDealTable(dealImportModel);
                dealList.add(dealImportModel);
            }
        }

        return ret;
    }

    /**
     * 异常信息输出
     *
     */
    private boolean exportErrorContent(FtpBean filePathBean, List<ErrorContent> errList) {

        boolean isSuccess = true;

        if (errList.size() > 0) {

            // 本地文件生成路径
            String uploadLocalPath = filePathBean.getUpload_localpath();
            try {

                File file = new File(uploadLocalPath + "/"+ "error" + DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_2) + ".csv");
                FileOutputStream fop = new FileOutputStream(file, true);
                CsvWriter csvWriter = new CsvWriter(fop, ',', Charset.forName("UTF-8"));

                // Body输出
                for (int i = 0 ; i < errList.size(); i++) {
                    ErrorContent errorContent = errList.get(i);

                    csvWriter.write(errorContent.getFileName());
                    csvWriter.write(errorContent.getSheetName());
                    csvWriter.write(String.valueOf(errorContent.getRowNum()));
                    csvWriter.write(errorContent.getContent());

                    csvWriter.endRecord();
                }

                csvWriter.flush();
                csvWriter.close();

            } catch (Exception e) {
                isSuccess = false;

                $error("createUploadFileForDailySales", e);

                issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
            }
        }

        return isSuccess;
    }

    /**
     * 输入项目长度检查（Byte 长度）
     *
     */
    private boolean chkLength(String content, int length){
        boolean ret = false;
        int byteLength = length * 2;

        if (StringUtils.getByteLength(content, GBKCharset) <= byteLength) {
            ret = true;
        }

        return ret;
    }

    /**
     * 日期大小检查
     *
     */
    private boolean chkDate(String dateStr, String dateEnd) {
        boolean ret = false;
        Date strDate = DateTimeUtil.parse(dateStr, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
        Date endDate = DateTimeUtil.parse(dateEnd, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
        if (strDate.before(endDate)) {
            ret = true;
        }

        return ret;
    }

    /**
     * Product 格式
     *
     */
    private class PruductSheetFormat {
        private static final int channel_id_index = 0;
        private static final int deal_id_index = 1;
        private static final int product_code_index = 2;

        private static final int product_name_index = 5;
        private static final int product_name_length = 100;

        private static final int foreign_language_name_index = 6;
        // 长度50，汉字，byte 100
        private static final int foreign_language_name_length = 50;

        private static final int category_lv4_id_index = 12;
        private static final int brand_name_index = 14;
        private static final int brand_id_index = 15;
        private static final int size_type_index = 16;
        private static final int attribute_index = 18;

        private static final int address_of_produce_index = 20;
        private static final int address_of_produce_length = 150;

        private static final int hs_code_index = 21;

        private static final int special_note_index = 22;
        private static final int special_note_length = 150;

        private static final int product_des_index = 23;

        private static final int function_ids_index = 0;

        // 白底方图（产品主图）
        private static final int main_image_1_index = 31;
        private static final int main_image_2_index = 32;
        private static final int main_image_3_index = 33;
        private static final int main_image_4_index = 34;
        private static final int main_image_5_index = 35;
        private static final int main_image_6_index = 36;

        // 竖图（手机端图）
        private static final int mobile_image_1_index = 37;
        private static final int mobile_image_2_index = 38;
        private static final int mobile_image_3_index = 39;
        private static final int mobile_image_4_index = 40;
        private static final int mobile_image_5_index = 41;
        private static final int mobile_image_6_index = 42;

        // 商品详情图（商品实拍图）
        private static final int deal_image_1_index = 43;
        private static final int deal_image_2_index = 44;
        private static final int deal_image_3_index = 45;
        private static final int deal_image_4_index = 46;
        private static final int deal_image_5_index = 47;
        private static final int deal_image_6_index = 48;
    }

    /**
     * Sku 格式
     *
     */
    private class SkuSheetFormat {
        private static final int channel_id_index = 0;
        private static final int deal_id_index = 1;
        private static final int product_code_index = 2;
        private static final int sku_index = 3;
        private static final int upc_code_index = 4;
        private static final int size_index = 6;
        private static final int abroad_price_index = 7;

        private static final int deal_price_index = 8;
        private static final int deal_price_min_value = 15;

        private static final int market_price_index = 9;

//        private static final int hscode_index = 5;
    }

    /**
     * Image 格式
     *
     */
    private class ImageSheetFormat {
        private static final int seq_index = 0;
        private static final int channel_id_index = 1;
        private static final int image_key_index = 2;
        private static final int image_type_index = 3;
        private static final int image_type_extend_index = 4;
        private static final int image_index = 5;
        private static final int origin_url_index = 6;
        private static final int jm_url_index = 7;
    }

    /**
     * Deal 格式
     *
     */
    private class DealSheetFormat {
        private static final int channel_id_index = 0;
        private static final int deal_id_index = 1;
        private static final int product_code_index = 2;
        private static final int start_time_index = 4;
        private static final int end_time_index = 5;
        // 仓库ID
        private static final int shipping_system_id_index = 7;
        private static final int user_purchase_limit_index = 8;

        private static final int product_long_name_index = 9;
        private static final int product_long_name_length = 130;

        private static final int product_medium_name_index = 10;
        private static final int product_medium_name_length = 35;

        private static final int product_short_name_index = 11;
        private static final int product_short_name_length = 15;

        private static final int search_meta_text_custom_index = 14;

        // 专场货架对应
        private static final int special_activity_id1_index = 15;
        private static final int shelf_id1_index = 16;
        private static final int special_activity_id2_index = 17;
        private static final int shelf_id2_index = 18;
        private static final int special_activity_id3_index = 19;
        private static final int shelf_id3_index = 20;
    }

    /**
     * 检查异常信息缓存
     *
     */
    private class ErrorContent{
        private String fileName;
        private String sheetName;
        private int rowNum;
        private String content;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getSheetName() {
            return sheetName;
        }

        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }

        public int getRowNum() {
            return rowNum;
        }

        public void setRowNum(int rowNum) {
            this.rowNum = rowNum;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
