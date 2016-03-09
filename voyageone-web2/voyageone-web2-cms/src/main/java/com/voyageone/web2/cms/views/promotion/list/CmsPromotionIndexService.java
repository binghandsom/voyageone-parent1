package com.voyageone.web2.cms.views.promotion.list;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.TypeChannel;
import com.voyageone.common.util.FileUtils;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.cms.wsdl.dao.CmsPromotionCodeDao;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionCodeModel;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionSkuModel;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.PromotionCodeGetRequest;
import com.voyageone.web2.sdk.api.request.PromotionDeleteRequest;
import com.voyageone.web2.sdk.api.request.PromotionPutRequest;
import com.voyageone.web2.sdk.api.request.PromotionsGetRequest;
import com.voyageone.web2.sdk.api.util.SdkBeanUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @Autowired
    CmsPromotionCodeDao cmsPromotionCodeDao;

    /**
     * 获取该channel的category类型.
     *
     * @param channelId
     * @param language
     * @return
     */
    public Map<String, Object> init(String channelId, String language) {
        Map<String, Object> result = new HashMap<>();

        result.put("platformTypeList", TypeChannel.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, language));
        result.put("promotionStatus", TypeConfigEnums.MastType.promotionStatus.getList(language));

        return result;
    }

    public CmsBtPromotionModel queryById(Integer promotionId) {
        PromotionsGetRequest request = new PromotionsGetRequest();
        request.setPromotionId(promotionId);
        List<CmsBtPromotionModel> models = voApiClient.execute(request).getCmsBtPromotionModels();
        if (models != null && models.size() == 1) {
            return models.get(0);
        } else {
            return null;
        }
    }

    public List<CmsBtPromotionModel> queryByCondition(Map<String, Object> conditionParams) {
        PromotionsGetRequest request = new PromotionsGetRequest();
        SdkBeanUtils.copyProperties(conditionParams, request);
        return voApiClient.execute(request).getCmsBtPromotionModels();
    }

    public int addOrUpdate(CmsBtPromotionModel cmsBtPromotionModel) {
        PromotionPutRequest request = new PromotionPutRequest();
        request.setCmsBtPromotionModel(cmsBtPromotionModel);
        try {
            if (cmsBtPromotionModel.getPromotionId() != null) {
                return voApiClient.execute(request).getModifiedCount();
            } else {
                return voApiClient.execute(request).getInsertedCount();
            }
        } catch (ApiException e) {
            throw new BusinessException(e.getErrCode() + ":" + e.getErrMsg(), e.getErrMsg());
        }
    }

    public int deleteById(Integer promotionId) {
        PromotionDeleteRequest request = new PromotionDeleteRequest();
        request.setPromotionId(promotionId);
        return voApiClient.execute(request).getRemovedCount();
    }

    public byte[] getCodeExcelFile(Integer promotionId) throws IOException, InvalidFormatException {

//        String templatePath = readValue(CmsConstants.Props.CODE_TEMPLATE);
        String templatePath = Properties.readValue(CmsConstants.Props.PROMOTION_EXPORT_TEMPLATE);

        PromotionCodeGetRequest request = new PromotionCodeGetRequest();
        Map<String, Object> param = new HashMap<>();
        param.put("promotionId", promotionId);
        request.setParam(param);

        List<CmsBtPromotionCodeModel> promotionCodes = cmsPromotionCodeDao.getPromotionCodeSkuList(param);
//        List<CmsBtPromotionCodeModel> promotionCodes = voApiClient.execute(request).getCodeList();


        $info("准备生成 Item 文档 [ %s ]", promotionCodes.size());
        $info("准备打开文档 [ %s ]", templatePath);

        try (InputStream inputStream = new FileInputStream(templatePath);
             Workbook book = WorkbookFactory.create(inputStream)) {

            int rowIndex = 1;
            for (int i = 0; i < promotionCodes.size(); i++) {
                boolean isContinueOutput = writeRecordToFile(book, promotionCodes.get(i), rowIndex);
                // 超过最大行的场合
                if (!isContinueOutput) {
                    break;
                }
                rowIndex += promotionCodes.get(i).getSkus() != null ? promotionCodes.get(i).getSkus().size() : 0;
            }

            $info("文档写入完成");

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
     * @param book          输出Excel文件对象
     * @param item          待输出DB数据
     * @param startRowIndex 开始
     * @return boolean 是否终止输出
     */
    private boolean writeRecordToFile(Workbook book, CmsBtPromotionCodeModel item, int startRowIndex) {
        boolean isContinueOutput = true;
        Sheet sheet = book.getSheetAt(0);

        Row styleRow = FileUtils.row(sheet, 1);

        CellStyle unlock = styleRow.getCell(0).getCellStyle();

        if(item.getSkus() != null && item.getSkus().size() > 0) {
            for(CmsBtPromotionSkuModel sku:item.getSkus()){
                Row row = FileUtils.row(sheet, startRowIndex);

                FileUtils.cell(row, CmsConstants.CellNum.catPathCellNum, unlock).setCellValue(item.getCatPath());

                FileUtils.cell(row, CmsConstants.CellNum.numberIdCellNum, unlock).setCellValue(item.getNumIid());

                FileUtils.cell(row, CmsConstants.CellNum.groupIdCellNum, unlock).setCellValue(item.getModelId());

                FileUtils.cell(row, CmsConstants.CellNum.groupNameCellNum, unlock).setCellValue(item.getProductModel());

                FileUtils.cell(row, CmsConstants.CellNum.productIdCellNum, unlock).setCellValue(item.getProductId());

                FileUtils.cell(row, CmsConstants.CellNum.productCodeCellNum, unlock).setCellValue(item.getProductCode());

                FileUtils.cell(row, CmsConstants.CellNum.productNameCellNum, unlock).setCellValue(item.getProductName());

                FileUtils.cell(row, CmsConstants.CellNum.skuCellNum, unlock).setCellValue(sku.getProductSku());

                if(item.getMsrpUS() != null){
                    FileUtils.cell(row, CmsConstants.CellNum.msrpUSCellNum, unlock).setCellValue(item.getMsrpUS());
                }
                if(item.getMsrp() != null){
                    FileUtils.cell(row, CmsConstants.CellNum.msrpRMBCellNum, unlock).setCellValue(item.getMsrp());
                }
                if(item.getRetailPrice() != null){
                    FileUtils.cell(row, CmsConstants.CellNum.retailPriceCellNum, unlock).setCellValue(item.getRetailPrice());
                }
                if(item.getSalePrice() != null){
                    FileUtils.cell(row, CmsConstants.CellNum.salePriceCellNum, unlock).setCellValue(item.getSalePrice());
                }
                if(item.getPromotionPrice() != null){
                    FileUtils.cell(row, CmsConstants.CellNum.promotionPriceCellNum, unlock).setCellValue(item.getPromotionPrice());
                }
                if(item.getInventory() != null){
                    FileUtils.cell(row, CmsConstants.CellNum.inventoryCellNum, unlock).setCellValue(item.getInventory());
                }

                FileUtils.cell(row, CmsConstants.CellNum.image1CellNum, unlock).setCellValue(item.getImage_url_1());

                FileUtils.cell(row, CmsConstants.CellNum.image2CellNum, unlock).setCellValue(item.getImage_url_2());

                FileUtils.cell(row, CmsConstants.CellNum.image3CellNum, unlock).setCellValue(item.getImage_url_3());

                FileUtils.cell(row, CmsConstants.CellNum.timeCellNum, unlock).setCellValue(item.getTime());

                FileUtils.cell(row, CmsConstants.CellNum.property1CellNum, unlock).setCellValue(item.getProperty1());

                FileUtils.cell(row, CmsConstants.CellNum.property2CellNum, unlock).setCellValue(item.getProperty2());

                FileUtils.cell(row, CmsConstants.CellNum.property3CellNum, unlock).setCellValue(item.getProperty3());

                FileUtils.cell(row, CmsConstants.CellNum.property4CellNum, unlock).setCellValue(item.getProperty4());

                startRowIndex++;
            }
        }

        return isContinueOutput;
    }
}
