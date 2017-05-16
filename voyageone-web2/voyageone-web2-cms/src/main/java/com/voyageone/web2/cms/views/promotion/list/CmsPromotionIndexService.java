package com.voyageone.web2.cms.views.promotion.list;

import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.PageQueryParameters;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TestBean;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.CmsBtPromotionBean;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.bean.cms.CmsBtPromotionSkuBean;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.PlatformService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.promotion.PromotionCodeService;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.impl.cms.promotion.PromotionSkuService;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtPromotionSkusModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.web2.base.BaseViewService;
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
 * @author james
 * @version 2.0.0, 15/12/11
 */
@Service
public class CmsPromotionIndexService extends BaseViewService {

    @Autowired
    private PromotionCodeService promotionCodeService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private PromotionSkuService promotionSkuService;

    @Autowired
    private TagService serviceTag;
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

    public Map<String, Object> test(PageQueryParameters parameters) {
        String channel=parameters.getParameterValue("channelName");
        String channelStart=parameters.getParameterValue("channelStart");
        String channelEnd=parameters.getParameterValue("channelEnd");
        Map<String, Object> result = new HashMap<>();
        List<TestBean> testBeanList=new ArrayList<>();
        for(int i=0;i<10;i++)
        {
            TestBean testBean=new TestBean("channel"+i,channel,channelStart+"11",channelEnd+"12");
            testBeanList.add(testBean);
        }
        result.put("testBeanList",testBeanList);
      /*  result.put("platformTypeList", TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, language));
        result.put("promotionStatus", TypeConfigEnums.MastType.promotionStatus.getList(language));*/

        return result;
    }
    public Map<String, Object> initByPromotionId(int PromotionId,String channelId, String language) {
        Map<String, Object> result = new HashMap<>();
        CmsBtPromotionModel model = promotionService.getByPromotionId(PromotionId);
        List<CmsBtTagModel> listTagModel = serviceTag.getListByParentTagId(model.getRefTagId());
        result.put("tagList", listTagModel);
        result.put("platformUrl", platformService.getPlatformProductUrl(String.valueOf(model.getCartId())));
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
        return promotionService.saveOrUpdate(cmsBtPromotionBean);
    }

    public int delete(CmsBtPromotionBean cmsBtPromotionBean) {
        return promotionService.delete(cmsBtPromotionBean);
    }



    public byte[] getCodeExcelFile(Integer promotionId,String channelId) throws IOException, InvalidFormatException {

//        String templatePath = readValue(CmsConstants.Props.CODE_TEMPLATE);
        String templatePath = Properties.readValue(CmsProperty.Props.PROMOTION_EXPORT_TEMPLATE);

        CmsBtPromotionModel cmsBtPromotionModel = promotionService.getByPromotionIdOrgChannelId(promotionId, channelId);
        List<CmsBtPromotionCodesBean> promotionCodes = promotionCodeService.getPromotionCodeListByIdOrgChannelId2(promotionId, channelId);

        $info("准备生成 Item 文档 [ %s ]", promotionCodes.size());
        $info("准备打开文档 [ %s ]", templatePath);

        try (InputStream inputStream = new FileInputStream(templatePath);
             Workbook book = WorkbookFactory.create(inputStream)) {

            Integer rowIndex = 1;
            for (int i=0 ;i<promotionCodes.size();i++) {
                CmsBtPromotionCodesBean promotionCode = promotionCodes.get(i);
                $info(String.format("导出活动%d/%d",i+1, promotionCodes.size()));
                promotionCode.setCartId(cmsBtPromotionModel.getCartId());
//                promotionCodes.get(i).setChannelId(promotionCodes.get().getChannelId());
                rowIndex = writeRecordToFile(book, promotionCode, rowIndex);
                // 超过最大行的场合
//                rowIndex += promotionCode.getSkus() != null ? promotionCode.getSkus().size() : 0;
            }

            writeCodeRecordToFile(book, promotionCodes);

            $info("文档写入完成");

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
    private Integer writeRecordToFile(Workbook book, CmsBtPromotionCodesBean item, Integer startRowIndex) {
        Sheet sheet = book.getSheet("sku");

        Row styleRow = FileUtils.row(sheet, 1);

        CellStyle unlock = styleRow.getCell(0).getCellStyle();

        Map<String, Object> param = new HashMap<>();
        param.put("promotionId", item.getPromotionId());
        param.put("productCode", item.getProductCode());
        List<CmsBtPromotionSkusModel> cmsBtPromotionSkusList = promotionSkuService.getListByWhere(param);

        if(cmsBtPromotionSkusList != null && cmsBtPromotionSkusList.size() > 0) {
            item.setSalePrice(cmsBtPromotionSkusList.get(0).getSalePrice().doubleValue());
            item.setMsrpUS(cmsBtPromotionSkusList.get(0).getMsrpUsd().doubleValue());
            item.setMsrp(cmsBtPromotionSkusList.get(0).getMsrpRmb().doubleValue());
            item.setRetailPrice(cmsBtPromotionSkusList.get(0).getRetailPrice().doubleValue());
            item.setSalePrice(cmsBtPromotionSkusList.get(0).getSalePrice().doubleValue());
            item.setPromotionPrice(cmsBtPromotionSkusList.get(0).getPromotionPrice().doubleValue());
            for (CmsBtPromotionSkusModel sku : cmsBtPromotionSkusList) {
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

                if(sku.getMsrpUsd() != null){
                    FileUtils.cell(row, CmsConstants.CellNum.msrpUSCellNum, unlock).setCellValue(sku.getMsrpUsd().doubleValue());
                }
                if(sku.getMsrpRmb() != null){
                    FileUtils.cell(row, CmsConstants.CellNum.msrpRMBCellNum, unlock).setCellValue(sku.getMsrpRmb().doubleValue());
                }
                if(sku.getRetailPrice() != null){
                    FileUtils.cell(row, CmsConstants.CellNum.retailPriceCellNum, unlock).setCellValue(sku.getRetailPrice().doubleValue());
                }
                if(sku.getSalePrice() != null){
                    FileUtils.cell(row, CmsConstants.CellNum.salePriceCellNum, unlock).setCellValue(sku.getSalePrice().doubleValue());
                }
                if(sku.getPromotionPrice() != null){
                    FileUtils.cell(row, CmsConstants.CellNum.promotionPriceCellNum, unlock).setCellValue(sku.getPromotionPrice().doubleValue());
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

                FileUtils.cell(row, CmsConstants.CellNum.size, unlock).setCellValue(sku.getSize());

                startRowIndex++;
            }
        }

        return startRowIndex;
    }

    private boolean writeCodeRecordToFile(Workbook book, List<CmsBtPromotionCodesBean> items) {
        Sheet sheet = book.getSheet("code");

        Row styleRow = FileUtils.row(sheet, 1);

        CellStyle unlock = styleRow.getCell(0).getCellStyle();
        int startRowIndex = 1;

        if(!ListUtils.isNull(items)) {
            for (CmsBtPromotionCodesBean item : items) {
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

                FileUtils.cell(row, CmsConstants.CellNum.tagCellNum-1, unlock).setCellValue(item.getTag());

                if(item.getMsrpUS() != null){
                    FileUtils.cell(row, CmsConstants.CellNum.msrpUSCellNum-1, unlock).setCellValue(item.getMsrpUS());
                }
                if(item.getMsrp() != null){
                    FileUtils.cell(row, CmsConstants.CellNum.msrpRMBCellNum-1, unlock).setCellValue(item.getMsrp());
                }
                if(item.getRetailPrice() != null){
                    FileUtils.cell(row, CmsConstants.CellNum.retailPriceCellNum-1, unlock).setCellValue(item.getRetailPrice());
                }
                if(item.getSalePrice() != null){
                    FileUtils.cell(row, CmsConstants.CellNum.salePriceCellNum-1, unlock).setCellValue(item.getSalePrice());
                }
                if(item.getPromotionPrice() != null){
                    FileUtils.cell(row, CmsConstants.CellNum.promotionPriceCellNum-1, unlock).setCellValue(item.getPromotionPrice());
                }
                if(item.getInventory() != null){
                    FileUtils.cell(row, CmsConstants.CellNum.inventoryCellNum-1, unlock).setCellValue(item.getInventory());
                }

                FileUtils.cell(row, CmsConstants.CellNum.image1CellNum-1, unlock).setCellValue(item.getImage_url_1());

                FileUtils.cell(row, CmsConstants.CellNum.image2CellNum-1, unlock).setCellValue(item.getImage_url_2());

                FileUtils.cell(row, CmsConstants.CellNum.image3CellNum-1, unlock).setCellValue(item.getImage_url_3());

                FileUtils.cell(row, CmsConstants.CellNum.timeCellNum-1, unlock).setCellValue(item.getTime());

                FileUtils.cell(row, CmsConstants.CellNum.property1CellNum-1, unlock).setCellValue(item.getProperty1());

                FileUtils.cell(row, CmsConstants.CellNum.property2CellNum-1, unlock).setCellValue(item.getProperty2());

                FileUtils.cell(row, CmsConstants.CellNum.property3CellNum-1, unlock).setCellValue(item.getProperty3());

                FileUtils.cell(row, CmsConstants.CellNum.property4CellNum-1, unlock).setCellValue(item.getProperty4());

                startRowIndex++;
            }
        }

        return true;
    }
}
