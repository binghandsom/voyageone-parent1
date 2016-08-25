package com.voyageone.web2.openapi.bi.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.ExcelUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.bi.BiVtSalesProductService;
import com.voyageone.service.impl.bi.BiVtSalesShopService;
import com.voyageone.web2.openapi.OpenApiBaseService;
import com.voyageone.web2.openapi.bi.constants.ExcelHeaderEnum;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DataServiceTB extends OpenApiBaseService {

    private String COLUMN_CONDITION_SHOP = "vt_sales_shop";

    @Autowired
    private BiVtSalesShopService vtSalesShopService;

    @Autowired
    private BiVtSalesProductService vtSalesProductService;

    public boolean saveStoreUrlData(Map<String, Object> data) {
        //check
        if (data == null) {
            throw new RuntimeException("data not found.");
        }
        if (data.get("url_data") == null) {
            throw new RuntimeException("url_data not found.");
        }
        if (data.get("shop_info") == null) {
            throw new RuntimeException("shop_info not found.");
        }
        //获取数据
        @SuppressWarnings("unchecked")
        Map<String, Object> shopInfo = (Map<String, Object>) data.get("shop_info");
        String urlData = String.valueOf(data.get("url_data"));
        Map<String, Object> jsonData = JacksonUtil.jsonToMap(urlData);

        //channelId
        String channelId = (String) shopInfo.get("channelCode");
        //eCommId
        int eCommId = (int) shopInfo.get("ecommId");
        //cartId
        String cartId = (String) shopInfo.get("ecommCode");


        //result Data
        List<String> listColumns = new ArrayList<>();
        List<List<String>> listValues = new ArrayList<>();
        if (eCommId == 1 || eCommId == 4) {
            // TM / TG
            paresTMUrlData(channelId, cartId, eCommId, jsonData, listColumns, listValues);
        } else if (eCommId == 5 || eCommId == 7) {
            // JD / JG
            paresJDUrlData(channelId, cartId, eCommId, jsonData, listColumns, listValues);
        }

        //saveListData
        vtSalesShopService.saveListData(channelId, listColumns, listValues);
        return true;
    }

    private void paresTMUrlData(String channelId, String cartId, int eCommId, Map<String, Object> jsonData, List<String> listColumns, List<List<String>> listValues) {
        //获取数据取得的项目List
        List<String> dataColumnInfoList = UtilResolveData.getKeysTM(jsonData);
        //获取Web取得的项目List
        List<Map<String, Object>> columnInfoList = vtSalesShopService.getListByEcommId(eCommId, COLUMN_CONDITION_SHOP);
        //获取DB columnName
        List<String> columnNameList = UtilResolveData.getColumnFromKeysTM(dataColumnInfoList, columnInfoList);
        listColumns.addAll(columnNameList);

        //获取DB Data
        List<List<String>> dataTransformInfoList = UtilResolveData.getInfoTM(channelId, cartId, jsonData);
        listValues.addAll(dataTransformInfoList);
    }

    /**
     * 解析Json，获得Column和Value
     */
    private void paresJDUrlData(String channelId, String cartId, int eCommId, Map<String, Object> jsonData, List<String> listColumns, List<List<String>> listValues) {

        listColumns.add("cart_id");
        listColumns.add("channel_id");
        listColumns.add("process_date");
        //初始化Web取得项目所对应的Value List
        List<List<String>> listValueDatas = UtilResolveData.getJdDateList(channelId, cartId, jsonData);
        BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);

        if (!listValueDatas.isEmpty()) {
            //获得Web取得项目所对应的数据库Table项目情况一览（cor_column_table_name，column_web_type，column_web_name）
            List<Map<String, Object>> columnInfoList = vtSalesShopService.getListByEcommId(eCommId, COLUMN_CONDITION_SHOP);

            //遍历Web取得项目所对应的数据库Table项目情况整理Map
            for (Map<String, Object> columnInfoMap : columnInfoList) {
                if (UtilResolveData.getInfoJD(columnInfoMap, jsonData, listValueDatas)) {
                    listColumns.add((String) columnInfoMap.get("corColumnTableName"));
                }
            }
        }

        listValues.addAll(listValueDatas);
    }


    public boolean saveProductFileData(Map<String, Object> shopInfo, String fileName, MultipartFile multipartFile) {
        //check
        if (shopInfo == null || shopInfo.isEmpty()) {
            throw new RuntimeException("shopItem not found.");
        }
        if (StringUtils.isEmpty(fileName)) {
            throw new RuntimeException("fileName not found.");
        }
        if (multipartFile == null) {
            throw new RuntimeException("file not found.");
        }
        //channelId
        String channelId = (String) shopInfo.get("channelCode");
        //eCommId
        int eCommId = (int) shopInfo.get("ecommId");
        //cartId
        String cartId = (String) shopInfo.get("ecommCode");

        //result Data
        List<String> listColumns = new ArrayList<>();
        List<List<String>> listValues = new ArrayList<>();
        if (eCommId == 1 || eCommId == 4) {
            // TM / TG
            // save excel file
            String newFileName = saveTBExcelFile(channelId, cartId, eCommId, fileName, multipartFile);

            $info("saveProductFileData file:" + newFileName);

            // pares Excel File
            listValues = paresTBExcelFile(channelId, cartId, eCommId, newFileName, listColumns);

        //} else if (eCommId == 5 || eCommId == 7) {
            // JD / JG
            //paresJDUrlData(shopId, eCommId, jsonData, listColumns, listValues);
        }

        //saveListData
        int insertRowCount = vtSalesProductService.saveListData(channelId, listColumns, listValues);
        $info("saveProductFileData insertRowCount:" + insertRowCount);
        return true;
    }

    private String saveTBExcelFile(String channelId, String cartId, int eCommId, String fileName, MultipartFile multipartFile) {
        //定义上传路径
        String path = Properties.readValue(ExcelHeaderEnum.BI_TB_PRODUCT_IMPORT_PATH);
        String newPath = path + "/" + channelId + "/";
        File localPath = new File(newPath);
        if (!localPath.exists()) {
            localPath.mkdir();
        }

        String deviceStr = "unknow";
        try (InputStream a = multipartFile.getInputStream();
            Workbook wb = WorkbookFactory.create(a)) {
            Sheet sheet = wb.getSheetAt(0);
            ExcelHeaderEnum.Device device = getDevice(sheet);
            deviceStr = device.getCnName();
        } catch (Exception e) {
            $warn("Read Excel Error", e);
        }

        //重命名上传后的文件名
        String newFileName = newPath + channelId + '_' + cartId + "_" + deviceStr + "_" + fileName;
        // new Local File
        File localFile = new File(newFileName);
        // delete File
        if (localFile.exists()) {
            localFile.delete();
        }
        try {
            multipartFile.transferTo(localFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // pares Excel File
        return newFileName;
    }

    private List<List<String>> paresTBExcelFile(String channelId, String cartId, int eCommId, String newFileName, List<String> listColumns) {
        List<List<String>> models = new ArrayList<>();

        // add static column
        listColumns.add("cart_id");
        listColumns.add("channel_id");
        listColumns.add("process_date");

        try (InputStream a = new FileInputStream(newFileName);
             Workbook wb = WorkbookFactory.create(a)) {

            Sheet sheet = wb.getSheetAt(0);

            String sheetName = sheet.getSheetName();

            String processDate = sheetName.substring(sheetName.indexOf("-") + 1, sheetName.indexOf("-") + 11).replace("-", "");

            ExcelHeaderEnum.Device device = null;
            try {
                device = getDevice(sheet);
            } catch (RuntimeException re) {
                $warn(re.getMessage());
            }

            if (device == null) {
                return models;
            }

            Row headRow = sheet.getRow(3);

            List<Integer> headIndexList = new ArrayList<>();
            // add other column
            for (int i = 0; i < headRow.getPhysicalNumberOfCells(); i++) {
                Cell headCell = headRow.getCell(i);
                String cnName = ExcelUtils.getString(headCell);
                if (StringUtil.isEmpty(cnName)) {
                    continue;
                }
                ExcelHeaderEnum.TBProductColumnDef productColumnDef = ExcelHeaderEnum.TBProductColumnDef.valueOfCnName(cnName);
                if (productColumnDef == null) {
                    continue;
                }

                String columnName;
                switch (device) {
                    case pc:
                        columnName = productColumnDef.getColumnPc();
                        break;
                    case mobile:
                        columnName = productColumnDef.getColumnMobile();
                        break;
                    case all:
                        columnName = productColumnDef.getColumn();
                        break;
                    default:
                        columnName = productColumnDef.getColumn();
                }
                if (columnName != null) {
                    listColumns.add(columnName);
                    headIndexList.add(i);
                }
            }

            for (int i = 4; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                String firstRowValue = ExcelUtils.getString(row, 0);

                if (StringUtils.isEmpty(firstRowValue)) {
                    continue;
                }

                List<String> model = new ArrayList<>();

                // add static column value
                model.add(cartId);
                model.add(channelId);
                model.add(processDate);

                // add other column value
                for (int index : headIndexList) {
                    String value = ExcelUtils.getString(row, index);
                    if (value == null) {
                        value = "0";
                    }
                    value = value.replace("%", "");
                    model.add(value);
                }

                models.add(model);
            }

        } catch (IOException | InvalidFormatException e) {
            throw new BusinessException("Read Excel Error", e);
        }
        return models;
    }

    private ExcelHeaderEnum.Device getDevice(Sheet sheet) {
        Row deviceRow = sheet.getRow(4);
        if (deviceRow == null) {
            throw new RuntimeException("device not found.");
        }
        String deviceRowValue = ExcelUtils.getString(deviceRow, 0);
        if (StringUtils.isEmpty(deviceRowValue)) {
            throw new RuntimeException("device not found.");
        }
        ExcelHeaderEnum.Device device = ExcelHeaderEnum.Device.valueOfCnName(deviceRowValue);
        if (device == null) {
            throw new RuntimeException(String.format("device[%s] not found.", deviceRowValue));
        }
        return device;
    }
}
