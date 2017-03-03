package com.voyageone.task2.cms.service.feed;

import com.csvreader.CsvReader;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.daoext.cms.CmsZzFeedOverstockPriceDaoExt;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.product.ProductPlatformService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsZzFeedOverstockPriceModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Common;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.OverStock;

/**
 * @author james.li on 2016/8/8.
 * @version 2.0.0
 */
@Service
public class OverStockPriceAnalysisService extends BaseCronTaskService {

    @Autowired
    private CmsZzFeedOverstockPriceDaoExt cmsZzFeedOverstockPriceDaoExt;

    @Autowired
    private ProductService productService;

    @Autowired
    private FeedInfoService feedInfoService;

    @Autowired
    private ProductPlatformService productPlatformService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "OverStockPriceAnalysisJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        List<CmsZzFeedOverstockPriceModel> overstockPrices = getRetailPriceList();

        overstockPrices.forEach(this::insertPrice);

        Map<String, Object> map = new HashMap<>();
        map.put("updFlg", 0);
        overstockPrices = cmsZzFeedOverstockPriceDaoExt.selectList(map);

        for (CmsZzFeedOverstockPriceModel overstockPrice : overstockPrices){
            updateMastPrice(overstockPrice);
        }

        backupFeedFile(FeedEnums.Name.file_id_import_sku);

    }

    public List<CmsZzFeedOverstockPriceModel> getRetailPriceList() {

        List<CmsZzFeedOverstockPriceModel> overstockPrices = new ArrayList<>();
        CsvReader reader;
        String fileName = Feeds.getVal1("024", FeedEnums.Name.file_id_import_sku);
        String filePath = Feeds.getVal1("024", FeedEnums.Name.feed_ftp_localpath);
        String fileFullName = String.format("%s/%s", filePath, fileName);

        String encode = Feeds.getVal1("024", FeedEnums.Name.feed_ftp_file_coding);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream  = new FileInputStream(fileFullName);
            reader = new CsvReader(fileInputStream, '\t', Charset.forName(encode));
//             Head读入
//            reader.readHeaders();
//            reader.getHeaders();

            // Body读入
            while (reader.readRecord()) {
                CmsZzFeedOverstockPriceModel overstockPrice = new CmsZzFeedOverstockPriceModel();
                int i = 0;
                overstockPrice.setSkuCode(reader.get(i++));
                overstockPrice.setCostPrice(reader.get(i++));
                overstockPrice.setFinalRmbPrice(reader.get(i++));
                overstockPrice.setMsrpPrice(reader.get(i++));
                overstockPrice.setUpdFlg(0);
                overstockPrices.add(overstockPrice);
            }
        } catch (FileNotFoundException e) {
            $info("OverStock价格列表不存在");
            return overstockPrices;

        } catch (Exception e) {
            e.printStackTrace();
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            return overstockPrices;
        }
        if(fileInputStream != null ) {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return overstockPrices;
    }

    private void insertPrice(CmsZzFeedOverstockPriceModel cmsZzFeedOverstockPriceModel) {
        cmsZzFeedOverstockPriceDaoExt.deleteBySku(cmsZzFeedOverstockPriceModel.getSkuCode());
        cmsZzFeedOverstockPriceDaoExt.insert(cmsZzFeedOverstockPriceModel);
    }

    private void updateMastPrice(CmsZzFeedOverstockPriceModel cmsZzFeedOverstockPriceModel) {
        CmsBtProductModel cmsBtProductModel = productService.getProductBySku(OverStock.getId(), cmsZzFeedOverstockPriceModel.getSkuCode());
        $info("code:" + cmsBtProductModel.getCommon().getFields().getCode());
        if (cmsBtProductModel != null) {
            CmsBtProductModel_Common common = cmsBtProductModel.getCommon();
            for (CmsBtProductModel_Sku sku : common.getSkus()) {
                if (sku.getSkuCode().equalsIgnoreCase(cmsZzFeedOverstockPriceModel.getSkuCode())) {
                    sku.setClientNetPrice(Double.parseDouble(cmsZzFeedOverstockPriceModel.getCostPrice()));
                    if(!StringUtils.isEmpty(cmsZzFeedOverstockPriceModel.getMsrpPrice())){
                        sku.setPriceMsrp(Double.parseDouble(cmsZzFeedOverstockPriceModel.getMsrpPrice()));
                    }
                    productService.updateProductCommon(OverStock.getId(), cmsBtProductModel.getProdId(), common, getTaskName(), false);
                    break;
                }
            }

            cmsBtProductModel.getPlatforms().forEach((s, cart) -> {
                if (cart.getCartId() != 0){
                    if(cart.getSkus() != null){
                        for (BaseMongoMap<String, Object> sku : cart.getSkus()) {
                            if (sku.getStringAttribute("skuCode").equalsIgnoreCase(cmsZzFeedOverstockPriceModel.getSkuCode())) {
                                sku.setAttribute("priceSale", Double.parseDouble(cmsZzFeedOverstockPriceModel.getFinalRmbPrice()));
                                if(!StringUtils.isEmpty(cmsZzFeedOverstockPriceModel.getMsrpPrice())){
                                    sku.setAttribute("priceMsrp", Double.parseDouble(cmsZzFeedOverstockPriceModel.getMsrpPrice()));
                                }
                                productPlatformService.updateProductPlatform(OverStock.getId(), cmsBtProductModel.getProdId(), cart, getTaskName(), "价格变更", false);
                                break;
                            }
                        }
                    }else{
                        $info("cartId = "+cart.getCartId()+"sku=null");
                    }
                }
            });
            cmsZzFeedOverstockPriceModel.setUpdFlg(1);
            cmsZzFeedOverstockPriceDaoExt.update(cmsZzFeedOverstockPriceModel);
        }

        CmsBtFeedInfoModel cmsBtFeedInfoModel = feedInfoService.getProductBySku(OverStock.getId(), cmsZzFeedOverstockPriceModel.getSkuCode());
        if(cmsBtFeedInfoModel != null){
            for (CmsBtFeedInfoModel_Sku sku : cmsBtFeedInfoModel.getSkus()) {
                if (sku.getSku().equalsIgnoreCase(cmsZzFeedOverstockPriceModel.getSkuCode())) {
                    sku.setPriceNet(Double.parseDouble(cmsZzFeedOverstockPriceModel.getCostPrice()));
                    if(!StringUtils.isEmpty(cmsZzFeedOverstockPriceModel.getMsrpPrice())){
                        sku.setPriceMsrp(Double.parseDouble(cmsZzFeedOverstockPriceModel.getMsrpPrice()));
                    }
                    cmsBtFeedInfoModel.setModifier(getTaskName());
                    cmsBtFeedInfoModel.setModified(DateTimeUtil.getNowTimeStamp());
                    feedInfoService.updateFeedInfo(cmsBtFeedInfoModel);
                    break;
                }
            }
        }
    }

    protected boolean backupFeedFile(FeedEnums.Name name) {
        $info("备份处理文件开始");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date_ymd = sdf.format(date);

        String filename = Feeds.getVal1(OverStock, FeedEnums.Name.feed_ftp_localpath) + "/" + StringUtils.null2Space(Feeds.getVal1(OverStock, name));
        String filename_backup = Feeds.getVal1(OverStock, FeedEnums.Name.feed_ftp_localpath) + "/" + date_ymd + "_"
                + StringUtils.null2Space(Feeds.getVal1(OverStock, name));
        File file = new File(filename);
        File file_backup = new File(filename_backup);

        if (!file.renameTo(file_backup)) {
//            $error("产品文件备份失败");
            $info(file_backup+"备份失败");
        }

        $info("备份处理文件结束");
        return true;
    }
}
