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
        Onsale, 			// 在售
        Instock 			// 在库
    }


    /**
     * platformActive
     */
    public enum PlatformActive {
        Onsale, 		// 在售
        Instock  		// 在库
    }

	/**
	 * workload type
     */
    public enum WorkloadType {
        Sx,             // 上新 / 全属性更新
        UpdProdImage,   // 更新商品图片
        UpdDesc,        // 更新商品描述
        SetOnsale,      // 上架
        SetInstock      // 下架
    }

    public interface channelConfig {
        String SAME_ATTR = "SAME_ATTR";
        String CLIENT_PRICE_UNIT = "CLIENT_PRICE_UNIT";
    }
}
