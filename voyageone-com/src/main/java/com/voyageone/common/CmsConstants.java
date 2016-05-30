package com.voyageone.common;

public class CmsConstants {

    // 从上新的任务表中一次数据抽出最大件数
    public static final int PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE = 100000;

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

    public interface ChannelConfig {

        // 全店统一属性结构
        String SAME_ATTR = "SAME_ATTR";

        // 该店铺是否自动审批价格
        String AUTO_APPROVE_PRICE = "AUTO_APPROVE_PRICE";

        // 第三方原始价格单位
        String CLIENT_PRICE_UNIT = "CLIENT_PRICE_UNIT";

        // 特价宝id
        String TEJIABAO_ID = "TEJIABAO_ID";

        // 翻译用到的长度check
        String TRANS_LEN_SET = "TRANS_LEN_SET";

        // 各个平台渠道导入master数据的PlatformActive初始值
        String PLATFORM_ACTIVE = "PLATFORM_ACTIVE";

        // 价格自动同步间隔天数
        String AUTO_SYN_DAY = "AUTO_SYN_DAY";

        // 强制击穿阈值(例如: 10 表示的是10%)
        String MANDATORY_BREAK_THRESHOLD = "MANDATORY_BREAK_THRESHOLD";

        // Feed导入Master时是否自动上新
        String AUTO_APPROVE_PRODUCT_CHANGE = "AUTO_APPROVE_PRODUCT_CHANGE";

        // 店铺级别MSRP价格计算公式
        String PRICE_MSRP_CALC_FORMULA = "PRICE_MSRP_CALC_FORMULA";

        // 店铺级别指导价格计算公式
        String PRICE_RETAIL_CALC_FORMULA = "PRICE_RETAIL_CALC_FORMULA";

        // 全店操作配置最小间隔时间
        String STORE_OPERATION_INTERVAL_TIME = "STORE_OPERATION_INTERVAL_TIME";

        // 原始图片的判断存在依据
        String IMAGE_COMPARE_RULE = "IMAGE_COMPARE_RULE";

        // 价格相关
        String PRICE = "PRICE";
        String PRICE_SX_PRICE = ".sx_price";
        String PRICE_TEJIABAO_OPEN = ".tejiabao_open";
        String PRICE_TEJIABAO_PRICE = ".tejiabao_price";
    }

    public interface DataAmount {
        String FEED_TO_MASTER_INSERT = "FEED_TO_MASTER_INSERT";
        String FEED_TO_MASTER_UPDATE = "FEED_TO_MASTER_UPDATE";
    }

    public interface ImageUploadStatus {
        String NOT_UPLOAD = "1";
        String WAITING_UPLOAD = "2";
        String UPLOAD_SUCCESS = "3";
        String UPLOAD_FAIL = "4";
    }

    public interface ImageType {
        String SIZE_CHART_IMAGE = "2";
        String BRAND_STORY_IMAGE = "3";
        String SHIPPING_DESCRIPTION_IMAGE = "4";
        String STORE_DESCRIPTION_IMAGE = "5";
    }

    public interface StoreOperationType {
        String REPUBLISH = "1";
        String REIMPORT_FEED_CLEAR_COMMON_PROPERTY = "2";
        String REIMPORT_FEED_NOT_CLEAR_COMMON_PROPERTY = "3";
        String PRICE_SYNCHRONIZATION = "4";
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
        int size = 25;
    }

    /**
     * 判断原始图片是否存在的依据
     */
    public interface IMAGE_COMPARE_RULE {
        String ORIGINAL_URL = "1";
        String ORIGINAL_IAMGE_NAME = "2";
    }

    /**
     * 上新的任务表中的上新状态值
     */
    public interface SxWorkloadPublishStatusNum {
        int initNum = 0;
        int okNum = 1;
        int errorNum = 2;
    }

}
