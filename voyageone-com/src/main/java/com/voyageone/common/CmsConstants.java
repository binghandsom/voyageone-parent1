package com.voyageone.common;

public class CmsConstants {

    // 从上新的任务表中一次数据抽出最大件数
    public static final int PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE = 200;

    // 有效销售平台cartId的最小值
    public static final int ACTIVE_CARTID_MIN = 20;

    /**
     * 产品状态
     */
    public enum ProductStatus {
        Pending, 			// 等待中
        Ready, 				// 准备中
        Approved
    }

    /**
     * platform的发布状态
     */
    public enum PlatformStatus {
        WaitingPublish,		// 等待上新
        OnSale, 			// 在售
        InStock,			// 在库,
        Violation           // 违规下架
    }


    /**
     * platformActive
     */
    public enum PlatformActive {
        // 变更后
        ToOnSale,		// 在售
        ToInStock 		// 在库
    }

    public interface ChannelConfig {

        // 全店统一属性结构
        String SAME_ATTR = "SAME_ATTR";

        /** 价格相关配置 start **/
        // 调用价格计算时, 指定渠道使用什么方式计算价格
        String PRICE_CALCULATOR = "PRICE_CALCULATOR";
        // 使用的配置项, 指示计算商品价格时, 使用体系计算价格
        String PRICE_CALCULATOR_SYSTEM = "SYSTEM";
        // 使用的配置项, 指示计算商品价格时, 使用配置表中配置的固定公式计算价格
        String PRICE_CALCULATOR_FORMULA = "FORMULA";

        // 价格计算公式使用, 该店铺是否自动同步MSRP价格
        String AUTO_SYNC_PRICE_MSRP = "AUTO_SYNC_PRICE_MSRP";

        // 价格计算公式使用, 该店铺是否自动同步价格
        String AUTO_SYNC_PRICE_SALE = "AUTO_SYNC_PRICE_SALE";

        // 价格是否同步到各平台活动
        String AUTO_SYNC_PRICE_PROMOTION = "AUTO_SYNC_PRICE_PROMOTION";

        // 强制击穿阈值(例如: 10 表示的是10%)
        String MANDATORY_BREAK_THRESHOLD = "MANDATORY_BREAK_THRESHOLD";

        // 发货方式
        String SHIPPING_TYPE = "SHIPPING_TYPE";

        // 店铺级别MSRP价格计算公式
        String PRICE_MSRP_CALC_FORMULA = "PRICE_MSRP_CALC_FORMULA";

        // 店铺级别指导价格计算公式
        String PRICE_RETAIL_CALC_FORMULA = "PRICE_RETAIL_CALC_FORMULA";

        /** 价格相关配置 end **/

        // 第三方原始价格单位
        String CLIENT_PRICE_UNIT = "CLIENT_PRICE_UNIT";

        // 特价宝id
        String TEJIABAO_ID = "TEJIABAO_ID";

        // 各个平台渠道导入master数据的PlatformActive初始值
        String PLATFORM_ACTIVE = "PLATFORM_ACTIVE";

        // Feed导入Master时变更需要自动同步的平台列表
        String AUTO_SYNC_CARTS = "AUTO_SYNC_CARTS";

        // 全店操作配置最小间隔时间
        String STORE_OPERATION_INTERVAL_TIME = "STORE_OPERATION_INTERVAL_TIME";

        // 原始图片的判断存在依据
        String IMAGE_COMPARE_RULE = "IMAGE_COMPARE_RULE";

        String IMAGE_UPLOAD_SERVICE = "IMAGE_UPLOAD_SERVICE";

        // Feed导入Master时，在Product更新的情况下，是否更新Feed节点下面的数据
        String FEED_UPDATE_FLG = "FEED_UPDATE_FLG";

        //橱窗图片本地保存位置
        String SHELVES_IMAGE_PATH = "SHELVES_IMAGE_PATH";

        String PRICE_SX_KEY="PRICE_SX";
        String PRICE_SALE_KEY="PRICE_SALE";
        String PRICE_RETAIL_KEY ="PRICE_RETAIL";
        String PRICE_TEJIABAO_IS_OPEN_KEY="PRICE_TEJIABAO_IS_OPEN";
        String PRICE_TEJIABAO_KEY="PRICE_TEJIABAO";

        String PRICE_RETAIL_PRICE_CODE = ".retail_price";
        String PRICE_SALE_PRICE_CODE = ".sale_price";
        String PRICE_SX_PRICE_CODE = ".sx_price";//
        String PRICE_TEJIABAO_IS_OPEN_CODE = ".tejiabao_open";
        String PRICE_TEJIABAO_PRICE_CODE = ".tejiabao_price";

        // 全链路库存管理相关
        String SCITEM = "SCITEM"; // 后端货品 (val1: 是否使用了全链路库存管理, val2: 商家仓库编码)

        // 别名相关
        String ALIAS = "ALIAS";
        String COLOR_ALIAS = ".color_alias";

        // 新品店铺内分类
        String SELLER_CAT = "SELLER_CAT";

        // 透明图
        String TRANSPARENT_IMAGE = "TRANSPARENT_IMAGE";

        // 子店到LIKING主店的产品品牌方商品图(images1)以外的图片复制方式(0:不复制原图以外的图片 1:以UNION方式复制图片
        // 2:以总店的数据为准。只要总店有数据，那么总店为准。如果总店没有，子店有，那么子店的数据复制到总店)
        String LIKING_IMAGE_COPY_FLG = "LIKING_IMAGE_COPY_FLG";

        // App端启用开关(用于控制所有平台的)
        String APP_SWITCH = "APP_SWITCH";

        // 上新方式
        String SX_SMART = "SX_SMART";

        // 共通：当前是否是正式生产环境
        String IS_FACTORY = "IS_FACTORY";

        // hscode
        String HSCODE = "HSCODE";
        String SX_HSCODE = ".sx_hscode";

        // 产品分类是否从feed导入(1：从feed导入，0：不从feed导入运营手动添加)
        String PRODUCT_TYPE_FROM_FEED_FLG = "PRODUCT_TYPE_FROM_FEED_FLG";

        // 适用人群是否从feed导入(1：从feed导入，0：不从feed导入运营手动添加)
        String SIZE_TYPE_FROM_FEED_FLG = "SIZE_TYPE_FROM_FEED_FLG";

        // 新建product时是否自动设置PC端自拍商品图images6(1:自动设置  空，0:不设置)
        String AUTO_SET_IMAGES6_FLG = "AUTO_SET_IMAGES6_FLG";

        // 该店铺每次feed-master导入最大件数
        String FEED_IMPORT_MAX = "FEED_IMPORT_MAX";

        // 是否只修改价格(1:只修改价格 空,0:修改全部属性)
        String ONLY_UPDATE_PRICE_FLG = "ONLY_UPDATE_PRICE_FLG";

        // feed增加状态属性
        String AUTO_SET_FEED_IMPORT_FLG = "AUTO_SET_FEED_IMPORT_FLG";

        // 该店铺是否自动审批价格
        String PLATFORM_IMAGE_DIRECTORY_ID = "PLATFORM_IMAGE_DIRECTORY_ID";

        //feed的code按不同季度拆分
        String SPLIT_QUARTER_BY_CODE = "SPLIT_QUARTER_BY_CODE";

        // 店铺是否启动了供应链管理
        String START_SUPPLY_CHAIN = "START_SUPPLY_CHAIN";

        // 是否强制尺码转换的检查(1:强制尺码转换 空,0:不强制尺码转换)
        String SIZE_CONVERSION_FLG = "SIZE_CONVERSION_FLG";

        //被迫下架的商品是否自动上架
        String IS_FORCED_IN_STOCK_PRODUCT_AUTO_ON_SALE="IS_FORCED_IN_STOCK_PRODUCT_AUTO_ON_SALE";

        // 天猫无线端的共通模块, 是否由运营自己在天猫后台进行管理(1：运营自己天猫后台管理，空或0：上新程序设定)
        String TMALL_WIRELESS_COMMON_MODULE_BY_USER = "TMALL_WIRELESS_COMMON_MODULE_BY_USER";

        // feed导入时是否设置主类目
        String FEED_IS_SET_MAIN_CATEGORY = "FEED_IS_SET_MAIN_CATEGORY";

        // feed->mast 阈值 价格 和 重量
        String FEED_MAST_THRESHOLD = "FEED_MAST_THRESHOLD";

        // feed->mast 阈值 价格 和 重量
        String FEED_MAST_CONFIG = "FEED_MAST_CONFIG";

        // 需要拆分的主类目
        String CATEGORY_SINGLE = "CATEGORY_SINGLE";

        // usjoi导入的白名单
        String CATEGORY_WHITE = "CATEGORY_WHITE";

        // WMS->CMS推送平台库存，当Code库存为0时是否切换平台主商品
        String AUTO_SWITCH_MASTER_PRODUCT = "AUTO_SWITCH_MASTER_PRODUCT";
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
        String REIMPORT_FEED_CLEAR_COMMON_PROPERTY = "2";
        String REIMPORT_FEED_NOT_CLEAR_COMMON_PROPERTY = "3";
    }

    public interface OptionConfigType {

        String OPTION_DATA_SOURCE = "optConfig";

        String OPTION_DATA_SOURCE_CHANNEL = "optConfigChannel";

        String OPTION_DATA_SOURCE_SIZE_CHART = "sizeChartConfigChannel";
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
        int initNum = 0;        // 上新对象
        int okNum = 1;          // 上新成功
        int errorNum = 2;       // 上新失败
        int smartSx = 3;        // 智能上新
        int review = 4;         // 达尔文产品审核中
        int waitCnUpload = 5;  // 独立域名等待xml上传
    }

    public interface FeedUpdFlgStatus{
        int New = 9; //新品
        int Pending = 0; //等待导入
        int Succeed = 1; //成功
        int Fail = 2;     //导入失败
        int NotIMport = 3; //不导入
        int FeedErr = 8;  //Feed数据异常
        int FeedBlackList = 7;  //黑名单
    }

    /**
     * feed product更新接口的参数方式
     */
    public interface FeedProductUpdateType {
        int CMS_FEED = 0; //CMS feed 导入
        int VMS_FEED = 80; //VMS feed 导入
        int VMS_PRICE_INVENTORY = 81; //VMS price inventory 导入
    }
}
