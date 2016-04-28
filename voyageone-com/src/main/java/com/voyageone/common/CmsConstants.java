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
}
