package com.voyageone.service.impl.cms.jumei2;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ExceptionUtil;
import com.voyageone.common.util.excel.ExcelColumn;
import com.voyageone.common.util.excel.ExcelException;
import com.voyageone.common.util.excel.ExportExcelInfo;
import com.voyageone.common.util.excel.ExportFileExcelUtil;
import com.voyageone.service.dao.cms.CmsBtJmPromotionExportTaskDao;
import com.voyageone.service.daoext.cms.CmsBtJmProductImagesDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionExportTaskDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionSkuDaoExt;
import com.voyageone.service.impl.cms.jumei.enumjm.*;
import com.voyageone.service.model.cms.CmsBtJmProductImagesModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionExportTaskModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionExportTask3Service {
    @Autowired
    CmsBtJmPromotionExportTaskDao dao;
    @Autowired
    CmsBtJmPromotionExportTaskDaoExt daoExt;
    @Autowired
    CmsBtJmPromotionSkuDaoExt daoExtCmsBtJmPromotionSku;
    @Autowired
    CmsBtJmPromotionProductDaoExt daoExtCmsBtJmPromotionProduct;
    @Autowired
    CmsBtJmProductImagesDaoExt daoExtCmsBtJmProductImages;
    public CmsBtJmPromotionExportTaskModel get(int id) {
        return dao.select(id);
    }
    public List<CmsBtJmPromotionExportTaskModel> getByPromotionId(int promotionId) {
        return daoExt.getByPromotionId(promotionId);
    }

    public List<ExcelColumn> getProductExportColumn() {
        List<ExcelColumn> list = new ArrayList<>();
        list.add(new ExcelColumn("productCode", "cms_bt_jm_promotion_product", "商品代码"));
        list.add(new ExcelColumn("appId", "cms_bt_jm_promotion_product", "APP端模块ID"));
        list.add(new ExcelColumn("pcId", "cms_bt_jm_promotion_product", "PC端模块ID"));
        list.add(new ExcelColumn("limit", "cms_bt_jm_promotion_product", "Deal每人限购"));
        list.add(new ExcelColumn("promotion_tag", "cms_bt_jm_promotion_product", "专场标签（以|分隔）"));
        return list;
    }
    public List<ExcelColumn> getSkuExportColumn() {
        List<ExcelColumn> list = new ArrayList<>();
        list.add(new ExcelColumn("productCode", "cms_bt_jm_promotion_sku", "商品代码"));
        list.add(new ExcelColumn("skuCode", "cms_bt_jm_promotion_sku", "APP端模块ID"));
        list.add(new ExcelColumn("dealPrice", "cms_bt_jm_promotion_sku", "PC端模块ID"));
        list.add(new ExcelColumn("marketPrice", "cms_bt_jm_promotion_sku", "Deal每人限购"));
        return list;
    }
}

