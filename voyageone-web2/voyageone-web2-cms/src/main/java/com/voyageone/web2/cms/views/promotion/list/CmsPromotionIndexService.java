package com.voyageone.web2.cms.views.promotion.list;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.TypeChannel;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionCodeModel;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import com.voyageone.web2.sdk.api.request.PromotionCodeGetRequest;
import com.voyageone.web2.sdk.api.request.PromotionDeleteRequest;
import com.voyageone.web2.sdk.api.request.PromotionPutRequest;
import com.voyageone.web2.sdk.api.request.PromotionsGetRequest;
import com.voyageone.web2.sdk.api.util.SdkBeanUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james
 * @version 2.0.0, 15/12/11
 */
@Service
public class CmsPromotionIndexService extends BaseAppService {

    @Autowired
    VoApiDefaultClient voApiClient;


    /**
     * 获取该channel的category类型.
     * @param channelId
     * @param language
     * @return
     */
    public Map<String, Object> init (String channelId, String language) {
        Map<String, Object> result = new HashMap<>();

        result.put("platformTypeList", TypeChannel.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, language));
        result.put("promotionStatus", TypeConfigEnums.MastType.promotionStatus.getList(language));

        return result;
    }

    public CmsBtPromotionModel queryById(Integer promotionId) {
        PromotionsGetRequest request=new PromotionsGetRequest();
        request.setPromotionId(promotionId);
        List<CmsBtPromotionModel> models=voApiClient.execute(request).getCmsBtPromotionModels();
        if(models!=null&& models.size()==1){
            return models.get(0);
        }else {
            return null;
        }
    }

    public List<CmsBtPromotionModel> queryByCondition(Map<String, Object> conditionParams) {
        PromotionsGetRequest request=new PromotionsGetRequest();
        SdkBeanUtils.copyProperties(conditionParams, request);
        return voApiClient.execute(request).getCmsBtPromotionModels();
    }

    public int addOrUpdate(CmsBtPromotionModel cmsBtPromotionModel) {
        PromotionPutRequest request=new PromotionPutRequest();
        request.setCmsBtPromotionModel(cmsBtPromotionModel);
        if(cmsBtPromotionModel.getPromotionId()!=null){
            return voApiClient.execute(request).getModifiedCount();
        }else{
            return voApiClient.execute(request).getInsertedCount();
        }
    }

    public int deleteById(Integer promotionId) {
        PromotionDeleteRequest request=new PromotionDeleteRequest();
        request.setPromotionId(promotionId);
        return voApiClient.execute(request).getRemovedCount();
    }

    public byte[] getCodeExcelFile(Integer promotionId) throws IOException, InvalidFormatException {

//        String templatePath = readValue(CmsConstants.Props.CODE_TEMPLATE);
        String templatePath = "d:/promotion.xlsx";

        PromotionCodeGetRequest request=new PromotionCodeGetRequest();
        Map<String ,Object> param = new HashMap<>();
        param.put("promotionId",promotionId);
        request.setParam(param);
        List<CmsBtPromotionCodeModel> promotionCodes = voApiClient.execute(request).getCodeList();


        $info("准备生成 Item 文档 [ %s ]", promotionCodes.size());
        $info("准备打开文档 [ %s ]", templatePath);

        try (InputStream inputStream = new FileInputStream(templatePath);
             Workbook book = WorkbookFactory.create(inputStream)) {

            for (int i = 0; i < promotionCodes.size(); i++) {
                boolean isContinueOutput = writeRecordToFile(book, promotionCodes.get(i), i + 1);
                // 超过最大行的场合
                if (!isContinueOutput) {
                    break;
                }
            }

            $info("文档写入完成");

//            // TODO test
//            try (FileOutputStream outputFileStream = new FileOutputStream("d:/test.xlsx")) {
//
//                book.write(outputFileStream);
//
//                outputFileStream.flush();
//                outputFileStream.close();
//            }

            // 返回值设定
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                book.write(outputStream);

                $info("已写入输出流");

                return outputStream.toByteArray();
            }
        }
    }
    /**
     * Code单位，文件输出
     *
     * @param book 输出Excel文件对象
     * @param items 待输出DB数据
     * @param cartId
     * @param startRowIndex 开始
     * @return boolean 是否终止输出
     */
    private boolean writeRecordToFile(Workbook book, CmsBtPromotionCodeModel item, int startRowIndex) {
        boolean isContinueOutput = true;
        Sheet sheet = book.getSheetAt(0);

        Row row = row(sheet, 1);
        CellStyle unlock = row.getRowStyle();;

            /*
             * 现有表格的列:
             * 0: No
             * 1: productId
             * 2: groupId
             * 3: numiid
             * 4: Model
             * 5: Code
             * 6: Product_Name
             * 7: Qty
             * 8: Sale_Price
             * 9: 类目Path
             */


        row = row(sheet, startRowIndex);
        // 内容输出
        cell(row, 0, unlock).setCellValue(startRowIndex);

        cell(row, 1, unlock).setCellValue(item.getProductCode());

        cell(row, 2, unlock).setCellValue(item.getProductName());

        cell(row, 3, unlock).setCellValue(item.getCatPath());

        cell(row, 4, unlock).setCellValue(item.getNumIid());

        cell(row, 5, unlock).setCellValue(item.getMsrp());

        cell(row, 6, unlock).setCellValue(item.getRetailPrice());

        cell(row, 7, unlock).setCellValue(item.getSalePrice());

        cell(row, 8, unlock).setCellValue(item.getPromotionPrice());

        cell(row, 9, unlock).setCellValue(item.getTagPathName());

        return isContinueOutput;
    }

    /**
     * Excel Cell样式取得
     *
     * @param book Excel Book对象
     * @return CellStyle Excel Cell样式
     */
    private CellStyle createUnLockStyle(Workbook book) {
        CellStyle cellStyle = book.createCellStyle();

        cellStyle.setLocked(false);

        return cellStyle;
    }

    /**
     * Excel 行对象取得
     *
     * @param sheet Excel Sheet对象
     * @param rowIndex Excel RowIndex
     * @return Row Excel Row对象
     */
    private Row row(Sheet sheet, int rowIndex) {

        Row row = sheet.getRow(rowIndex);

        if (row == null) row = sheet.createRow(rowIndex);

        return row;
    }

    /**
     * Excel Cell对象取得
     *
     * @param row Excel Row对象
     * @param index Excel CellIndex
     * @param cellStyle Excel Cell样式
     * @return Row Excel Cell对象
     */
    private Cell cell(Row row, int index, CellStyle cellStyle) {

        Cell cell = row.getCell(index);

        if (cell == null) cell = row.createCell(index);

        if (cellStyle != null) cell.setCellStyle(cellStyle);

        return cell;
    }
}
