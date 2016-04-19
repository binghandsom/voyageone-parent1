package com.voyageone.web2.cms;

import com.voyageone.web2.base.BaseConstants;

/**
 * @author Edward
 * @version 2.0.0, 15/12/3
 */
public interface CmsConstants extends BaseConstants{

    /**
     * Session 内CMS信息存放的 Key
     */
    String SESSION_CMS = "voyageone.session.cms";

    /**
     * 默认的 CategoryType值 : TG
     */
    String DEFAULT_CATEGORY_TYPE = "TG";

    /**
     * 默认的 CartId值 : 23
     */
    Integer DEFAULT_CART_ID = 23;

    /**
     * Property 文件Key
     */
    interface Props {
        //  Code 文件模板文件
        String SEARCH_ADVANCE_EXPORT_TEMPLATE = "cms.search.advance.export.file";
        String PROMOTION_EXPORT_TEMPLATE = "cms.promotion.export.file";
        String CMS_JM_EXPORT_PATH="cms.jm.export.path";
        String CMS_JM_IMPORT_PATH ="cms.jm.import.path";
        String STOCK_EXPORT_TEMPLATE = "cms.stock.export.file";
    }
    interface CellNum {
        int cartIdCellNum = 0;
        int channelIdCellNum = 1;
        int catPathCellNum = 2;
        int numberIdCellNum = 3;
        int groupIdCellNum = 4;
        int groupNameCellNum = 5;
        int productIdCellNum = 6;
        int productCodeCellNum = 7;
        int productNameCellNum = 8;
        int skuCellNum = 9;
        int tagCellNum = 10;
        int msrpUSCellNum = 11;
        int msrpRMBCellNum = 12;
        int retailPriceCellNum = 13;
        int salePriceCellNum = 14;
        int promotionPriceCellNum = 15;
        int inventoryCellNum = 16;
        int image1CellNum = 17;
        int image2CellNum = 18;
        int image3CellNum = 19;
        int timeCellNum = 20;
        int property1CellNum = 21;
        int property2CellNum = 22;
        int property3CellNum = 23;
        int property4CellNum = 24;
    }

    /**
     * 产品状态
     */
    interface productStatus {
        String NEW = "New";
        String PENDING = "Pending";
        String READY = "Ready";
        String APPROVED = "Approved";
    }

    interface optionConfigType {

        String OPTION_DATA_SOURCE = "optConfig";

        String OPTION_DATA_SOURCE_CHANNEL = "optConfigChannel";
    }

    interface jmMasterPlatCode {
        String BRND = "0";
        String PRICE_UNIT = "1";
        String STOCK = "2";
    }
}
