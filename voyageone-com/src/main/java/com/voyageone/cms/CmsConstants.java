package com.voyageone.cms;

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
        Waitingpublish,		// 等待上新
        Onsale, 			// 在售
        Instock 			// 在库
    }


}
