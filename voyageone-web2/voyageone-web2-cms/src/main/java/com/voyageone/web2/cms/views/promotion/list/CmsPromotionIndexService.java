package com.voyageone.web2.cms.views.promotion.list;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.util.FileUtils;
import com.voyageone.service.bean.cms.CmsBtPromotionBean;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.impl.cms.promotion.PromotionCodeService;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtPromotionSkuBean;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.CmsConstants;
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
    private PromotionCodeService promotionCodeService;

    @Autowired
    private PromotionService promotionService;

    /**
     * 获取该channel的category类型.
     *
     * @param channelId chanelid
     * @param language 语言
     * @return mast数据
     */
    public Map<String, Object> init(String channelId, String language) {
        Map<String, Object> result = new HashMap<>();

        result.put("platformTypeList", TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, language));
        result.put("promotionStatus", TypeConfigEnums.MastType.promotionStatus.getList(language));

        return result;
    }

    public CmsBtPromotionModel queryById(Integer promotionId) {
        return promotionService.getByPromotionId(promotionId);
    }

//    public List<CmsBtPromotionBean> queryByCondition(Map<String, Object> conditionParams) {
//        if(Channels.isUsJoi(conditionParams.get("channelId").toString())){
//            conditionParams.put("orgChannelId", conditionParams.get("channelId"));
//            conditionParams.put("channelId", ChannelConfigEnums.Channel.VOYAGEONE.getId());
//        }
//        return promotionService.getByCondition(conditionParams);
//    }

    public int addOrUpdate(CmsBtPromotionBean cmsBtPromotionBean) {
        try {
            return promotionService.saveOrUpdate(cmsBtPromotionBean);
        } catch (Exception e) {
            throw new BusinessException("addOrUpdate", e);
        }
    }

    public int delete(CmsBtPromotionModel cmsBtPromotionModel) {
        return promotionService.delete(cmsBtPromotionModel);
    }

    public byte[] getCodeExcelFile(Integer promotionId,String channelId) throws IOException, InvalidFormatException {

//        String templatePath = readValue(CmsConstants.Props.CODE_TEMPLATE);
        String templatePath = Properties.readValue(CmsConstants.Props.PROMOTION_EXPORT_TEMPLATE);

        CmsBtPromotionModel cmsBtPromotionModel = promotionService.getByPromotionIdOrgChannelId(promotionId, channelId);
        List<CmsBtPromotionCodesBean> promotionCodes = promotionCodeService.getPromotionCodeListByIdOrgChannelId(promotionId, channelId);

        $info("准备生成 Item 文档 [ %s ]", promotionCodes.size());
        $info("准备打开文档 [ %s ]", templatePath);

        try (InputStream inputStream = new FileInputStream(templatePath);
             Workbook book = WorkbookFactory.create(inputStream)) {

            int rowIndex = 1;
            for (CmsBtPromotionCodesBean promotionCode : promotionCodes) {
                promotionCode.setCartId(cmsBtPromotionModel.getCartId());
//                promotionCodes.get(i).setChannelId(promotionCodes.get().getChannelId());
                boolean isContinueOutput = writeRecordToFile(book, promotionCode, rowIndex);
                // 超过最大行的场合
                if (!isContinueOutput) {
                    break;
                }
                rowIndex += promotionCode.getSkus() != null ? promotionCode.getSkus().size() : 0;
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
    private boolean writeRecordToFile(Workbook book, CmsBtPromotionCodesBean item, int startRowIndex) {
        Sheet sheet = book.getSheetAt(0);

        Row styleRow = FileUtils.row(sheet, 1);

        CellStyle unlock = styleRow.getCell(0).getCellStyle();

        if(item.getSkus() != null && item.getSkus().size() > 0) {
            for (CmsBtPromotionSkuBean sku : item.getSkus()) {
                Row row = FileUtils.row(sheet, startRowIndex);

                FileUtils.cell(row, CmsConstants.CellNum.cartIdCellNum, unlock).setCellValue(item.getCartId());

                FileUtils.cell(row, CmsConstants.CellNum.channelIdCellNum, unlock).setCellValue(item.getOrgChannelId());

                FileUtils.cell(row, CmsConstants.CellNum.catPathCellNum, unlock).setCellValue(item.getCatPath());

                FileUtils.cell(row, CmsConstants.CellNum.numberIdCellNum, unlock).setCellValue(item.getNumIid());

                FileUtils.cell(row, CmsConstants.CellNum.groupIdCellNum, unlock).setCellValue(item.getModelId());

                FileUtils.cell(row, CmsConstants.CellNum.groupNameCellNum, unlock).setCellValue(item.getProductModel());

                FileUtils.cell(row, CmsConstants.CellNum.productIdCellNum, unlock).setCellValue(item.getProductId());

                FileUtils.cell(row, CmsConstants.CellNum.productCodeCellNum, unlock).setCellValue(item.getProductCode());

                FileUtils.cell(row, CmsConstants.CellNum.productNameCellNum, unlock).setCellValue(item.getProductName());

                FileUtils.cell(row, CmsConstants.CellNum.skuCellNum, unlock).setCellValue(sku.getProductSku());

                FileUtils.cell(row, CmsConstants.CellNum.tagCellNum, unlock).setCellValue(item.getTag());

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

        return true;
    }
}
