package com.voyageone.common;

public class CmsConstants {

    /**
     * 产品状态
     */
    public enum ProductStatus {
        New, 				// 新建
        Pending, 			// 等待中
        Ready, 				// 准备中
        Approved, 			// 批准
        Deleted 			// 删除
    }

    /**
     * platform的发布状态
     */
    public enum PlatformStatus {
        WaitingPublish,		// 等待上新
        OnSale, 			// 在售
        InStock 			// 在库
    }


    /**
     * platformActive
     */
    public enum PlatformActive {
        // 变更后
        ToOnSale,		// 在售
        ToInStock 		// 在库
    }

	/**
	 * workload type
     */
    // TODO: 16/4/28 无人使用就删除了
    public enum WorkloadType {
        Sx,             // 上新 / 全属性更新
        UpdProdImage,   // 更新商品图片
        UpdDesc,        // 更新商品描述
        SetOnSale,      // 上架
        SetInStock      // 下架
    }

    public interface channelConfig {
        String SAME_ATTR = "SAME_ATTR";
        String CLIENT_PRICE_UNIT = "CLIENT_PRICE_UNIT";
    }

    public interface JmMasterPlatCode {
        String BRND = "0";
        String PRICE_UNIT = "1";
        String STOCK = "2";
    }

    public interface OptionConfigType {

        String OPTION_DATA_SOURCE = "optConfig";

        String OPTION_DATA_SOURCE_CHANNEL = "optConfigChannel";
    }

    public interface CellNum {
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
}
