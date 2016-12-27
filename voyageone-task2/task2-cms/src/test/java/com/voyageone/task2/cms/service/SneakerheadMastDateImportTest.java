package com.voyageone.task2.cms.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.ExcelUtils;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.bean.cms.CmsBtPromotionGroupsBean;
import com.voyageone.service.dao.cms.mongo.CmsBtImageGroupDao;
import com.voyageone.service.impl.cms.ImageGroupService;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.impl.cms.SizeChartService;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel_Image;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModelSizeMap;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created by james on 2016/12/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:context-cms-test.xml")
public class SneakerheadMastDateImportTest {

    public final static String cms_mt_sizechartrelation = "H:\\sneaker\\尺码表整理12.21.-1.xlsx";
    public final static String cms_mt_sizechart = "H:\\sneaker\\尺码图整理12.21-1.xlsx";
    public final static String sellerCat = "H:\\sneaker\\店铺内分类\\sellerCat.xlsx";

    @Autowired
    SizeChartService sizeChartService;
    @Autowired
    private CmsBtImageGroupDao cmsBtImageGroupDao;
    @Autowired
    private MongoSequenceService commSequenceMongoService;

    @Test
    public void importSizechartrelation() {
        try (InputStream inputStream = new FileInputStream(cms_mt_sizechartrelation)) {
            List<CmsBtSizeChartModel> cmsBtSizeChartModels = resolveSizeChartModel(inputStream);
            cmsBtSizeChartModels.forEach(cmsBtSizeChartModel -> {
                CmsBtSizeChartModel temp = sizeChartService.insert("928", "SneakerheadMastDateImportTest", cmsBtSizeChartModel.getSizeChartName(), cmsBtSizeChartModel.getBrandName(),cmsBtSizeChartModel.getProductType(),cmsBtSizeChartModel.getSizeType());
                sizeChartService.sizeChartDetailSizeMapSave(temp.getChannelId(),"SneakerheadMastDateImportTest",temp.getSizeChartId(),cmsBtSizeChartModel.getSizeMap());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void importImageGroup() {
        try (InputStream inputStream = new FileInputStream(cms_mt_sizechart)) {
            List<CmsBtImageGroupModel> cmsBtImageGroupModels = resolveImageGroupModelModel(inputStream);
            cmsBtImageGroupModels.forEach(cmsBtSizeChartModel -> {
                cmsBtSizeChartModel.setCreater("SneakerheadMastDateImportTest");
                cmsBtSizeChartModel.setModifier("SneakerheadMastDateImportTest");
                Long sizeChartId = commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_IMAGE_GROUP_ID);
                cmsBtSizeChartModel.setImageGroupId(sizeChartId);
                cmsBtImageGroupDao.update(cmsBtSizeChartModel);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<CmsBtImageGroupModel> resolveImageGroupModelModel(InputStream xls) throws Exception {
        Map<String, CmsBtImageGroupModel> models = new HashMap<>();
        Workbook wb = new XSSFWorkbook(xls);
        Sheet sheet1 = wb.getSheetAt(2);
        int rowNum = 0;
        for (Row row : sheet1) {
            try {
                rowNum++;
                // 跳过第一行
                if (rowNum == 1) {
                    continue;
                }

                if (row.getCell(0) == null || StringUtil.isEmpty(row.getCell(0).getStringCellValue())) {
                    break;
                }
                String channelId = ExcelUtils.getString(row, 0);
                String cartId = ExcelUtils.getString(row, 5,"#");
                String imageGroupName = ExcelUtils.getString(row, 3);
                String imageType = ExcelUtils.getString(row, 2,"#");
                String viewType = ExcelUtils.getString(row, 1,"#");
                String brandName = ExcelUtils.getString(row, 6);
                String sizeType = ExcelUtils.getString(row, 9);
                String productType = ExcelUtils.getString(row, 7);
                String originUrl = ExcelUtils.getString(row, 11);
                String platformUrl =  ExcelUtils.getString(row, 12);
                CmsBtImageGroupModel_Image cmsBtImageGroupModel_Image = new CmsBtImageGroupModel_Image();
                cmsBtImageGroupModel_Image.setOriginUrl(originUrl);
                cmsBtImageGroupModel_Image.setPlatformUrl(platformUrl);
                cmsBtImageGroupModel_Image.setErrorMsg(null);
                cmsBtImageGroupModel_Image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_SUCCESS));
                CmsBtImageGroupModel cmsBtImageGroupModel = models.get(imageGroupName);
                if (cmsBtImageGroupModel == null) {
                    cmsBtImageGroupModel = new CmsBtImageGroupModel();
                    cmsBtImageGroupModel.setActive(1);
                    cmsBtImageGroupModel.setChannelId(channelId);
                    cmsBtImageGroupModel.setCartId(Integer.parseInt(cartId));
                    cmsBtImageGroupModel.setImageGroupName(imageGroupName);
                    cmsBtImageGroupModel.setBrandName(Arrays.asList(brandName.split(",")));
                    cmsBtImageGroupModel.setSizeType(Arrays.asList(sizeType.split(",")));
                    cmsBtImageGroupModel.setProductType(Arrays.asList(productType.split(",")));
                    cmsBtImageGroupModel.setImageType(Integer.parseInt(imageType));
                    //1:PC端 2:APP端
                    cmsBtImageGroupModel.setViewType(Integer.parseInt(viewType));
                    cmsBtImageGroupModel.setImage(new ArrayList<>());
                    models.put(imageGroupName, cmsBtImageGroupModel);
                }
                cmsBtImageGroupModel.getImage().add(cmsBtImageGroupModel_Image);
            } catch (Exception e) {
                throw new BusinessException(String.format("第%d行数据格式不对 (%s)", rowNum, e.toString()));
            }
        }
        List<CmsBtImageGroupModel> list = new ArrayList<>();
        models.forEach((s, cmsBtImageGroupModel) -> list.add(cmsBtImageGroupModel));
        return list;
    }

    private List<CmsBtSizeChartModel> resolveSizeChartModel(InputStream xls) throws Exception {
        Map<String, CmsBtSizeChartModel> models = new HashMap<>();
        Workbook wb = new XSSFWorkbook(xls);
        Sheet sheet1 = wb.getSheetAt(1);
        int rowNum = 0;
        for (Row row : sheet1) {
            try {
                rowNum++;
                // 跳过第一行
                if (rowNum == 1) {
                    continue;
                }

                if (row.getCell(0) == null || StringUtil.isEmpty(row.getCell(0).getStringCellValue())) {
                    break;
                }
                String sizeChartName = ExcelUtils.getString(row, 2);
                String brandName = ExcelUtils.getString(row, 3).toLowerCase();
                String sizeType = ExcelUtils.getString(row, 6);
                String productType = ExcelUtils.getString(row, 4);
                String originalSize = ExcelUtils.getString(row, 8);
                String adjustSize = ExcelUtils.getString(row, 9);
//                String usual = ExcelUtils.getString(row, 9);
                CmsBtSizeChartModelSizeMap cmsBtSizeChartModelSizeMap = new CmsBtSizeChartModelSizeMap();
                cmsBtSizeChartModelSizeMap.setAdjustSize(adjustSize);
                cmsBtSizeChartModelSizeMap.setOriginalSize(originalSize);
//                cmsBtSizeChartModelSizeMap.setUsual(usual);
                CmsBtSizeChartModel cmsBtSizeChartModel = models.get(sizeChartName+brandName);
                if (cmsBtSizeChartModel == null) {
                    cmsBtSizeChartModel = new CmsBtSizeChartModel();
                    cmsBtSizeChartModel.setActive(1);
                    cmsBtSizeChartModel.setSizeChartName(sizeChartName);
                    cmsBtSizeChartModel.setBrandName(Arrays.asList(brandName.split(",")));
                    cmsBtSizeChartModel.setSizeType(Arrays.asList(sizeType.split(",")));
                    cmsBtSizeChartModel.setProductType(Arrays.asList(productType.split(",")));
                    cmsBtSizeChartModel.setSizeMap(new ArrayList<>());
                    models.put(sizeChartName+brandName, cmsBtSizeChartModel);
                }
                cmsBtSizeChartModel.getSizeMap().add(cmsBtSizeChartModelSizeMap);
            } catch (Exception e) {
                throw new BusinessException(String.format("第%d行数据格式不对 (%s)", rowNum, e.toString()));
            }
        }
        List<CmsBtSizeChartModel> list = new ArrayList<>();
        models.forEach((s, cmsBtSizeChartModel) -> list.add(cmsBtSizeChartModel));
        return list;
    }
}
