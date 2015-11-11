/**
 * @Name:    translate.cms.zh.js
 * @Date:    2015/6/16
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {
    var mainApp = require ('components/app');
    require ('components/services/language.service');

    mainApp.config (["$translateProvider", "languageType",
        function ($translateProvider, languageType) {

            $translateProvider.translations (languageType.zh,
                {
                    CMS_TXT_MENU_PRICE_SETTING: '价格系数设定',
                    CMS_TXT_MENU_SYSTEM_SETTING: '系统设定',
                    CMS_TXT_MENU_MY_SETTING: '用户设定',
                    CMS_TXT_MENU_SETTING: '设定',
                    CMS_TXT_MENU_CATEGORY: '类目管理',
                    CMS_TXT_MENU_PROMOTION: '促销管理',
                    CMS_TXT_MENU_CREATE: '新建',
                    CMS_TXT_MENU_SEARCH: '检索',
                    CMS_TXT_MENU_EXPORT: '导出',
                    CMS_TXT_MENU_QUICK_SEARCH: '快速检索',
                    CMS_TXT_MENU_IMAGES: '图片管理',
                    CMS_TXT_CATEGORY: '类目',
                    CMS_BTN_ADD_CATEGORY: '添加新类目',
                    CMS_BTN_EFFECTIVE: '有效',
                    CMS_BTN_UN_EFFECTIVE: '无效',
                    CMS_TXT_TAB_NAME_AMERICAN: '美国',
                    CMS_TXT_TAB_NAME_CHINA: '中国',
                    CMS_TXT_TAB_NAME_STATISTICS: '统计',
                    CMS_TXT_TAB_NAME_PRICE_SETTINGS: '价格系数设定',
                    CMS_TXT_TAB_NAME_CUSTOM: '自定义',
                    CMS_TXT_NAME: '名称',
                    CMS_TXT_HEADER_TITLE: '标题',
                    CMS_TXT_URL_KEY: 'URL Key',
                    CMS_TXT_VISIBLE_ON_MENU: '菜单可见 ?',
                    CMS_TXT_ENABLE_FILTER: '过滤器有效 ?',
                    CMS_TXT_PUBLISH: '发布 ?',
                    CMS_TXT_TITLE_MAIN_CATEGORY: '主类目',
                    CMS_TXT_TITLE_AMAZON: '亚马逊设定',
                    CMS_TXT_TITLE_GOOGLE: '谷歌设定',
                    CMS_TXT_TITLE_PRICE_GRABBER: 'PriceGrabber设定',
                    CMS_TXT_SEO_TITLE: '标题 ',
                    CMS_TXT_SEO_DESCRIPTION: '描述',
                    CMS_TXT_SEO_KEY_WORDS: '关键字',
                    CMS_TXT_SEO_CANONICAL: 'Canonical',
                    CMS_BTN_CANCEL: '取消',
                    CMS_BTN_SAVE: '保存',
                    CMS_TXT_HS_CODE_SH: '上海行邮税号',
                    CMS_TXT_HS_CODE_GZ: '广州行邮税号',
                    CMS_TXT_SIZE_CHART: '尺码转换',
                    CMS_TXT_TITLE_TM: '天猫设定',
                    CMS_TXT_TITLE_JD: '京东设定',
                    CMS_TXT_BASE_PRICE: '基础价格',
                    CMS_TXT_PRICING_FACTOR: '价格系数',
                    CMS_TXT_EXCHANGE_RATE: '汇率',
                    CMS_TXT_OVER_HEAD_1: 'Over Head 1',
                    CMS_TXT_OVER_HEAD_2: 'Over Head 2',
                    CMS_TXT_SHIPPING_COMPENSATION: '运费补偿',
                    CMS_TXT_COMMENT: '备注',
                    CMS_TXT_US_SUB_CATEGORIES: '子类目',
                    CMS_TXT_CN_SUB_CATEGORIES: '子类目 (CN)',
                    CMS_TXT_US_MODELS: '模型',
                    CMS_TXT_CN_MODELS: '模型 (CN)',
                    CMS_TXT_US_PRODUCTS: '产品',
                    CMS_TXT_CN_PRODUCTS: '产品 (CN)',
                    CMS_TXT_DISPLAY_ORDER: '显示顺序',
                    CMS_TXT_PUBLISH_DATETIME: '发布日期',
                    CMS_TXT_CREATED_ON: '创建日期',
                    CMS_TXT_CREATED_BY: '创建者',
                    CMS_TXT_LAST_UPDATED_ON: '修改日期',
                    CMS_TXT_LAST_UPDATED_BY: '修改者',
                    CMS_BTN_SEARCH_GO: 'Go!',
                    CMS_BTN_BATCH_EDIT: '批量更新',
                    CMS_BTN_RESET_DISPLAY_ORDER: '重置显示顺序',
                    CMS_BTN_SET_PROPERTY: '设置属性',
                    CMS_TXT_SET_PROPERTY: '设置属性',
                    CMS_BTN_MOVE_TO_CATEGORY: '移动到其他类目',
                    CMS_BTN_COPY_TO_CATEGORY: '添加到其他类目',
                    CMS_BTN_SET_DISPLAY_ORDER: '设置显示顺序',
                    CMS_BTN_SET_APPROVED: '设置上新属性',
                    CMS_TXT_SET_APPROVED: '设置上新属性',
                    CMS_BTN_ADD_TO_PROMOTION: '添加到活动类目',
                    CMS_TXT_ADD_TO_PROMOTION: '添加到活动类目',
                    CMS_BTN_EXPORT_FILE: '导出文件',
                    CMS_BTN_FILTER_RESULTS: '过滤器设置',
                    CMS_BTN_CUSTOMIZE_COLUMNS: '自定义列设置',
                    CMS_TXT_REQUIRED_FLAG: '*',
                    CMS_BTN_ADD_MODEL: '添加新模型',
                    CMS_TXT_IS_EFFECTIVE: '有效 ?',
                    CMS_TXT_TITLE_SEO: 'SEO',
                    CMS_TXT_TITLE_HS_CODE: '行邮税号',
                    CMS_BTN_TITLE_SIZE_CHART: '尺码转换设定',
                    CMS_TXT_MODEL: 'Model',
                    CMS_TXT_PRIMARY_PRODUCT: '主商品',
                    CMS_TXT_PRODUCT_TYPE: '产品类型',
                    CMS_TXT_BRAND: '产品品牌',
                    CMS_TXT_SIZE_TYPE: '尺码类型',
                    CMS_TXT_CODE: '产品Code',
                    CMS_TXT_IS_HIGH_LIGHT_PRODUCT: '是否代表产品 ?',
                    CMS_TXT_IS_APPROVED_FOR: '各渠道销售标示',
                    CMS_TXT_IS_NEW_ARRIVAL: '是否新品 ?',
                    CMS_TXT_IS_REWARD_ELIGIBLE: '是否Reward Eligible ?',
                    CMS_TXT_IS_DISCOUNT_ELIGIBLE: '是否可打折产品 ?',
                    CMS_TXT_IS_PHONE_ORDER_ONLY: '是否只限手机订单 ?',
                    CMS_TXT_IS_APPROVED_DESCRIPTION: '是否编辑完成 ?',
                    CMS_TXT_COLOR_MAP: '主颜色',
                    CMS_TXT_COLOR: '颜色',
                    CMS_TXT_MADE_IN_COUNTRY: '产地',
                    CMS_TXT_MATERIAL_FABRIC: '材质',
                    CMS_TXT_ABSTRACT: '摘要',
                    CMS_TXT_ACCESSORY: '附件',
                    CMS_TXT_SHORT_DESCRIPTION: '简短描述',
                    CMS_TXT_LONG_DESCRIPTION: '详情描述',
                    CMS_TXT_ORDER_LIMIT_COUNT: '限购数量',
                    CMS_TXT_PROMOTION_TAG: '促销标签',
                    CMS_TXT_QUANTITY: '库存',
                    CMS_TXT_MSRP: 'MSRP',
                    CMS_TXT_US_OFFICIAL_PRICE: '美国官网售价',
                    CMS_TXT_US_OFFICIAL_SHIPPING_TYPE: '美国官网运送方式',
                    CMS_TXT_US_OFFICIAL_IS_ON_SALE: '美国官网是否On Sale ?',
                    CMS_TXT_US_OFFICIAL_IS_APPROVED: '美国官网是否Approved ?',
                    CMS_TXT_US_OFFICIAL_SNEAKER_RX_SALES_7_DAYS: '美国官网Sneaker RX7天的销售件数',
                    CMS_TXT_US_OFFICIAL_SNEAKER_RX_SALES_30_DAYS: '美国官网Sneaker RX30天的销售件数',
                    CMS_TXT_US_OFFICIAL_SNEAKER_RX_SALES_THIS_YEAR: '美国官网Sneaker RX今年的销售件数',
                    CMS_TXT_US_OFFICIAL_SNEAKER_SALES_7_DAYS: '美国官网Sneaker7天的销售件数',
                    CMS_TXT_US_OFFICIAL_SNEAKER_SALES_30_DAYS: '美国官网Sneaker30天的销售件数',
                    CMS_TXT_US_OFFICIAL_SNEAKER_SALES_THIS_YEAR: '美国官网Sneaker今年的销售件数',
                    CMS_TXT_US_OFFICIAL_SNEAKER_WS_SALES_7_DAYS: '美国官网Sneaker内部7天的销售件数',
                    CMS_TXT_US_OFFICIAL_SNEAKER_WS_SALES_30_DAYS: '美国官网Sneaker内部30天的销售件数',
                    CMS_TXT_US_OFFICIAL_SNEAKER_WS_SALES_THIS_YEAR: '美国官网Sneaker内部今年的销售件数',
                    CMS_TXT_US_OFFICIAL_SNEAKER_MOBILE_SALES_7_DAYS: '美国官网Sneaker移动端7天的销售件数',
                    CMS_TXT_US_OFFICIAL_SNEAKER_MOBILE_SALES_30_DAYS: '美国官网Sneaker移动端30天的销售件数',
                    CMS_TXT_US_OFFICIAL_SNEAKER_MOBILE_SALES_THIS_YEAR: '美国官网Sneaker移动端今年的销售件数',
                    CMS_TXT_US_AMAZON_PRICE: '美国亚马逊售价',
                    CMS_TXT_US_AMAZON_SHIPPING_TYPE: '美国亚马逊运送方式',
                    CMS_TXT_US_AMAZON_IS_ON_SALE: '美国亚马逊是否On Sale ?',
                    CMS_TXT_US_AMAZON_IS_APPROVED: '美国亚马逊是否Approved ?',
                    CMS_TXT_US_AMAZON_PRE_PUBLISH_DATE_TIME: '美国亚马逊预定发售时间',
                    CMS_TXT_US_AMAZON_SALES_7_DAYS: '美国亚马逊7天的销售件数',
                    CMS_TXT_US_AMAZON_SALES_30_DAYS: '美国亚马逊30天的销售件数',
                    CMS_TXT_US_AMAZON_SALES_THIS_YEAR: '美国亚马逊今年的销售件数',
                    CMS_TXT_CN_PRICE: '中国定价: $',
                    CMS_TXT_CN_PRICE_RMB: '中国指导售价: ￥',
                    CMS_TXT_CN_PRICE_ADJUSTMENT_RMB: '中国调整售价: ￥',
                    CMS_TXT_CN_PRICE_FINAL_RMB: '中国最终售价: ￥',
                    CMS_TXT_CN_SHIPPING_TYPE: '中国运送方式',
                    CMS_TXT_CN_IS_ON_SALE: '中国是否On Sale ?',
                    CMS_TXT_CN_IS_APPROVED: '中国是否Approved ?',
                    CMS_TXT_CN_PRE_PUBLISH_DATE_TIME: '中国预定发售时间',
                    CMS_TXT_CN_SALES_7_DAYS: '中国7天的销售件数',
                    CMS_TXT_CN_SALES_30_DAYS: '中国30天的销售件数',
                    CMS_TXT_CN_SALES_THIS_YEAR: '中国今年的销售件数',
                    CMS_TXT_REFERENCE_MSRP: '官方吊牌价: ￥',
                    CMS_TXT_REFERENCE_PRICE: '官方销售价: ￥',
                    CMS_TXT_FIRST_SALE_PRICE: '第一次售价: $',
                    CMS_TXT_EFFECTIVE_PRICE: '有效售价: $',
                    CMS_TXT_CN_OFFICIAL_PRICE_ADJUSTMENT_RMB: '中国官网调整售价: ￥',
                    CMS_TXT_CN_OFFICIAL_PRICE_FINAL_RMB: '中国官网最终售价: ￥',
                    CMS_TXT_CN_OFFICIAL_SHIPPING_TYPE: '中国官网运送方式',
                    CMS_TXT_CN_OFFICIAL_IS_ON_SALE: '中国官网是否On Sale ?',
                    CMS_TXT_CN_OFFICIAL_IS_APPROVED: '中国官网是否Approved ?',
                    CMS_TXT_CN_OFFICIAL_PRE_PUBLISH_DATE_TIME: '中国官网预定发售时间',
                    CMS_TXT_CN_OFFICIAL_SALES_7_DAYS: '中国官网7天的销售件数',
                    CMS_TXT_CN_OFFICIAL_SALES_30_DAYS: '中国官网30天的销售件数',
                    CMS_TXT_CN_OFFICIAL_SALES_THIS_YEAR: '中国官网今年的销售件数',
                    CMS_TXT_TB_PRICE_ADJUSTMENT_RMB: '淘宝调整售价: ￥',
                    CMS_TXT_TB_PRICE_FINAL_RMB: '淘宝最终售价: ￥',
                    CMS_TXT_TB_SHIPPING_TYPE: '淘宝运送方式',
                    CMS_TXT_TB_IS_ON_SALE: '淘宝是否On Sale ?',
                    CMS_TXT_TB_IS_APPROVED: '淘宝是否Approved ?',
                    CMS_TXT_TB_PRE_PUBLISH_DATE_TIME: '淘宝预定发售时间',
                    CMS_TXT_TB_SALES_7_DAYS: '淘宝7天的销售件数',
                    CMS_TXT_TB_SALES_30_DAYS: '淘宝30天的销售件数',
                    CMS_TXT_TB_SALES_THIS_YEAR: '淘宝今年的销售件数',
                    CMS_TXT_TM_PRICE_ADJUSTMENT_RMB: '天猫调整售价: ￥',
                    CMS_TXT_TM_PRICE_FINAL_RMB: '天猫最终售价: ￥',
                    CMS_TXT_TM_SHIPPING_TYPE: '天猫运送方式',
                    CMS_TXT_TM_IS_ON_SALE: '天猫是否On Sale ?',
                    CMS_TXT_TM_IS_APPROVED: '天猫是否Approved ?',
                    CMS_TXT_TM_PRE_PUBLISH_DATE_TIME: '天猫预定发售时间',
                    CMS_TXT_TM_SALES_7_DAYS: '天猫7天的销售件数',
                    CMS_TXT_TM_SALES_30_DAYS: '天猫30天的销售件数',
                    CMS_TXT_TM_SALES_THIS_YEAR: '天猫今年的销售件数',
                    CMS_TXT_TG_PRICE_ADJUSTMENT_RMB: '天猫国际调整售价: ￥',
                    CMS_TXT_TG_PRICE_FINAL_RMB: '天猫国际最终售价: ￥',
                    CMS_TXT_TG_SHIPPING_TYPE: '天猫国际运送方式',
                    CMS_TXT_TG_IS_ON_SALE: '天猫国际是否On Sale ?',
                    CMS_TXT_TG_IS_APPROVED: '天猫国际是否Approved ?',
                    CMS_TXT_TG_PRE_PUBLISH_DATE_TIME: '天猫国际预定发售时间',
                    CMS_TXT_TG_SALES_7_DAYS: '天猫国际7天的销售件数',
                    CMS_TXT_TG_SALES_30_DAYS: '天猫国际30天的销售件数',
                    CMS_TXT_TG_SALES_THIS_YEAR: '天猫国际今年的销售件数',
                    CMS_TXT_JD_PRICE_ADJUSTMENT_RMB: '京东调整售价: ￥',
                    CMS_TXT_JD_PRICE_FINAL_RMB: '京东最终售价: ￥',
                    CMS_TXT_JD_SHIPPING_TYPE: '京东运送方式',
                    CMS_TXT_JD_IS_ON_SALE: '京东是否On Sale ?',
                    CMS_TXT_JD_IS_APPROVED: '京东是否Approved ?',
                    CMS_TXT_JD_PRE_PUBLISH_DATE_TIME: '京东预定发售时间',
                    CMS_TXT_JD_SALES_7_DAYS: '京东7天的销售件数',
                    CMS_TXT_JD_SALES_30_DAYS: '京东30天的销售件数',
                    CMS_TXT_JD_SALES_THIS_YEAR: '京东今年的销售件数',
                    CMS_TXT_JG_PRICE_ADJUSTMENT_RMB: '京东国际调整售价: ￥',
                    CMS_TXT_JG_PRICE_FINAL_RMB: '京东国际最终售价: ￥',
                    CMS_TXT_JG_SHIPPING_TYPE: '京东国际运送方式',
                    CMS_TXT_JG_IS_ON_SALE: '京东国际是否On Sale ?',
                    CMS_TXT_JG_IS_APPROVED: '京东国际是否Approved ?',
                    CMS_TXT_JG_PRE_PUBLISH_DATE_TIME: '京东国际预定发售时间',
                    CMS_TXT_JG_SALES_7_DAYS: '京东国际7天的销售件数',
                    CMS_TXT_JG_SALES_30_DAYS: '京东国际30天的销售件数',
                    CMS_TXT_JG_SALES_THIS_YEAR: '京东国际今年的销售件数',
                    CMS_TXT_NEW_CATEGORY: '新建类目',
                    CMS_TXT_PARENT_CATEGORY: '父类目',
                    CMS_TXT_IS_UNISEX: '是否通用 ?',
                    CMS_TXT_SIZE_OFFSET: '通用尺码偏差',
                    CMS_TXT_WEIGHT: '重量',
                    CMS_TXT_LBS: '磅',
                    CMS_TXT_TAXABLE: '是否含税 ?',
                    CMS_TXT_BLOG_URL: 'Blog URL',
                    CMS_TXT_DISPLAY_IMAGE: '展示图片',
                    CMS_TXT_RMB: 'RMB',
                    CMS_TXT_PATH: '路径',
                    CMS_TXT_ATTACHMENT_NAME: '副标题',
                    CMS_TXT_SUB_TITLE_TAOBAO: '淘宝',
                    CMS_TXT_SUB_TITLE_TMALL: '天猫',
                    CMS_TXT_SUB_TITLE_TMALLG: '天猫国际',
                    CMS_TXT_IS_PRIMARY_CATEGORY: '是否主类目 ?',
                    CMS_TXT_SUB_TITLE_JD: '京东',
                    CMS_TXT_SUB_TITLE_JG: '京东国际',
                    CMS_BTN_REMOVE: '删除',
                    CMS_BTN_DECOUPLING: '去除关联',
                    CMS_TXT_ACTIONS: '操作',
                    CMS_TXT_MATERIAL_FABRIC_1: '材质 1',
                    CMS_TXT_MATERIAL_FABRIC_2: '材质 2',
                    CMS_TXT_MATERIAL_FABRIC_3: '材质 3',
                    CMS_TXT_ITEM_BOX_ANGEL_IMAGE: '产品图片数量',
                    CMS_TXT_AVAILABLE_DATE: '发售日期',
                    CMS_TXT_IS_OUTLETS_ON_SALE: '是否打折 ?',
                    CMS_TXT_NEW_MODEL: '新建Model',
                    CMS_TXT_TAB_NAME_US_PRICE: '美国售价',
                    CMS_TXT_TAB_NAME_CN_PRICE: '中国售价',
                    CMS_TXT_SHIPPING_TYPE: '运送方式',
                    CMS_TXT_PROFIT: '利润率',
                    CMS_TXT_PRICE: '售价',
                    CMS_TXT_AMAZON_PRICE: '亚马逊售价',
                    CMS_TXT_DISCOUNT: '折扣率',
                    CMS_TXT_CURRENT_PRICE: '当前售价',
                    CMS_TXT_NO_APPROVED_COMMENT: '未Approved的理由',
                    CMS_TXT_IS_APPROVED: '是否Approved ?',
                    CMS_TXT_IS_ON_SALE: '是否上架 ?',
                    CMS_TXT_USD: 'USD',
                    CMS_TXT_OFF_PERCENT: '% Off',
                    CMS_TXT_PRE_PUBLISH_TIME: '预上架时间',
                    CMS_TXT_PUBLISHED_INFO: '实际上架信息',
                    CMS_TXT_PRICE_HISTORY: '历史售价',
                    CMS_TXT_CN_REFERENCE: '中国参考售价',
                    CMS_TXT_SUB_TITLE_TB: 'TB',
                    CMS_TXT_SUB_TITLE_TM: 'TM',
                    CMS_TXT_SUB_TITLE_TG: 'TG',
                    CMS_TXT_SUB_TITLE_OFFICIAL: '官网',
                    CMS_TXT_CURRENT_FINAL_PRICE: '当前最终售价',
                    CMS_TXT_DEFAULT_PRICE: '默认售价',
                    CMS_TXT_ADJUSTMENT_PRICE: '调整售价',
                    CMS_TXT_FINAL_PRICE: '最终售价',
                    CMS_TXT_CURRENT: '当前售价: ',
                    CMS_TXT_SIZE_AND_STOCK: '尺码 & 库存',
                    CMS_TXT_TITLE_SIZE: '尺码',
                    CMS_TXT_TITLE_STOCK: '库存',
                    CMS_BTN_SETTING: '设定',
                    CMS_TXT_VIEW_DETAILS: '库存详情',
                    CMS_TXT_REFERENCE: '官方价格',
                    CMS_TXT_LAST_RECEIVED_ON: '最近入库时间',
                    CMS_TXT_TITLE_OTHER_INFO: '其他信息',
                    CMS_TXT_NEW_PROMOTION: '新建活动',
                    CMS_TXT_UPLOAD_IMAGES: '上传图片',
                    CMS_TXT_DOWNLOAD_IMAGES: '下载图片',
                    CMS_TXT_NOTIFICATIONS: '提示信息',
                    CMS_TXT_MSG_SAVE_SUCCESS:'保存成功',
                    CMS_TXT_MSG_SAVE_FAILED:'保存失败',
                    CMS_TXT_MSG_UPDATE_SUCCESS: '更新成功',
                    CMS_TXT_MSG_UPDATE_FAILED: '更新失败',
                    CMS_TXT_MSG_DELETE_SUCCESS:'删除成功',
                    CMS_TXT_MSG_DELETE_FAILED:'删除失败',
                    CMS_TXT_SUB_CATEGORIES: '子类目',
                    CMS_TXT_MODELS: 'Models',
                    CMS_TXT_PRODUCTS: '产品',
                    CMS_TXT_CATEGORIES: '类目',
                    CMS_BTN_REFRESH: '刷新',
                    CMS_TXT_IS_PRIMARY_PRODUCT: '是否主商品 ?',
                    CMS_BTN_SEARCH: '检索',
                    CMS_TXT_TITLE_COMPLEX_SEARCH: '混合检索',
                    CMS_TXT_SUB_CATEGORY_COUNT: '子类目数量',
                    CMS_TXT_MODEL_COUNT: 'Models数量',
                    CMS_TXT_PRODUCT_COUNT: '产品数量',
                    CMS_TXT_PRIMARY_CATEGORY: '主类目',
                    CMS_TXT_TITLE_ADVANCE_SEARCH: '高级检索',
                    CMS_TXT_PROMOTION: '活动',
                    CMS_TXT_ADD_PROMOTION: '添加活动',
                    CMS_TXT_CART: '销售渠道',
                    CMS_TXT_PREHEAT_DATE: '预热日期',
                    CMS_TXT_PRODUCT_SELECTION_OVER: '选品结束 ?',
                    CMS_TXT_ACTIVITY_CONFIRM_OVER: '活动确认 ?',
                    CMS_TXT_LOCATION_STOCK_OVER: '所库存结束 ?',
                    CMS_TXT_ACTIVITY_OVER: '活动结束',
                    CMS_TXT_SUB_PROMOTIONS: '子活动',
                    CMS_TXT_EFFECTIVE_DATE: '活动日期',
                    CMS_BTN_DISCOUNT_SETTING: '折扣设定',
                    CMS_BTN_DELETE: '删除',
                    CMS_BTN_COPY: '复制',
                    CMS_TXT_OUTLETS_SALE_PRICE: '折扣销售价格',
                    CMS_TXT_SALES_QUANTITY_IN_ACTIVITY: '活动销售数量',
                    CMS_TXT_SALES_PERCENT_IN_ACTIVITY: '活动销售百分比',
                    CMS_BTN_MOVE_TO_MODEL: '移动到其他款式',
                    CMS_TXT_REFERENCE_MSRP_WITHOUT_MARK: '官方吊牌价',
                    CMS_TXT_REFERENCE_PRICE_WITHOUT_MARK: '官方销售价',
                    CMS_TXT_CN_PRICE_FINAL_RMB_WITHOUT_MARK: '中国最终售价',
                    CMS_TXT_CN_PRICE_RMB_WITHOUT_MARK: '中国指导售价',
                    CMS_MSG_NO_PRODUCT_SELECTED: '请至少选择一件产品',
                    CMS_MSG_ACTIVITY_OVER_OR_UNEFFECTIVE_PROMOTION: 'This promotion is activity over or uneffective',
                    CMS_TXT_MENU_MASTER_CATEGORY: '主类目',
                    CMS_TXT_MASTER_CATEGORY_MATCH: '主类目匹配',
                    CMS_TXT_PROMOTION_HISTORY: '历史活动',
                    CMS_TXT_IMAGE:'图片',
                    CMS_TXT_BINDED:'已绑定',
                    CMS_TXT_OTHER_SIZE:'其他尺码',
                    CMS_TXT_TITLE_DISCOUNT_SETTING: '活动折扣设定',
                    CMS_TXT_TITLE_SIZE_CHART_SETTING: '尺码对照设定',
                    CMS_TXT_BOUND_SIZE_CHART: '已绑定尺码对照',
                    CMS_TXT_ADD_NEW_SIZE_CHART: '添加新尺码对照',
                    CMS_TXT_OTHER_SIZE_CHART: '尺码对照',
                    CMS_TXT_TITLE_SIZE_CHART_DETAIL: '尺码对照详情',
                    CMS_TXT_SIZE: '原始尺码',
                    CMS_TXT_CN_SIZE: '中国尺码',
                    CMS_TXT_EDIT: '操作',
                    CMS_BTN_ADD: '添加',
                    CMS_TXT_SIZE_CHART_MODEL: '尺码对照模板',
                    CMS_TXT_MODIFIER: '更新人',
                    CMS_TXT_MODIFIED: '更新时间',
                    CMS_TXT_PROPERTY_SETTING: '属性设定',
                    CMS_TXT_YES: '是',
                    CMS_TXT_NO: '否',
                    CMS_TXT_APPROVE_SETTING: '上新设定',
                    CMS_TXT_PRODUCT_APPROVED_SUCCESS: '产品的上新设定成功',
                    CMS_TXT_PRODUCT_IMAGE_UPLOAD: '产品图片上传',
                    CMS_BTN_PRODUCT_SELECT_FILE: '选择文件',
                    CMS_TXT_PRODUCT_IMG_TYPE_1: '产品',
                    CMS_TXT_PRODUCT_IMG_TYPE_2: '包装',
                    CMS_TXT_PRODUCT_IMG_TYPE_3: '角度',
                    CMS_TXT_PRODUCT_IMG_TYPE_4: '设计',
                    CMS_TXT_MASTER_CATEGORY_DEFAULT_PROPERTY_SETTING: '默认属性设定',
                    CMS_TXT_TITLE_NEW_SIZE_CHART: '新建尺码对照',
                    CMS_TXT_US_OFFICIAL_SNEAKER_RX_SALES_7_DAYS_PERCENT: '美国官网Sneaker RX7天的销售百分比',
                    CMS_TXT_US_OFFICIAL_SNEAKER_RX_SALES_30_DAYS_PERCENT: '美国官网Sneaker RX30天的销售百分比',
                    CMS_TXT_US_OFFICIAL_SNEAKER_RX_SALES_THIS_YEAR_PERCENT: '美国官网Sneaker RX今年的销售百分比',
                    CMS_TXT_US_OFFICIAL_SNEAKER_SALES_7_DAYS_PERCENT: '美国官网Sneaker7天的销售百分比',
                    CMS_TXT_US_OFFICIAL_SNEAKER_SALES_30_DAYS_PERCENT: '美国官网Sneaker30天的销售百分比',
                    CMS_TXT_US_OFFICIAL_SNEAKER_SALES_THIS_YEAR_PERCENT: '美国官网Sneaker今年的销售百分比',
                    CMS_TXT_US_OFFICIAL_SNEAKER_WS_SALES_7_DAYS_PERCENT: '美国官网Sneaker内部7天的销售百分比',
                    CMS_TXT_US_OFFICIAL_SNEAKER_WS_SALES_30_DAYS_PERCENT: '美国官网Sneaker内部30天的销售百分比',
                    CMS_TXT_US_OFFICIAL_SNEAKER_WS_SALES_THIS_YEAR_PERCENT: '美国官网Sneaker内部今年的销售百分比',
                    CMS_TXT_US_OFFICIAL_SNEAKER_MOBILE_SALES_7_DAYS_PERCENT: '美国官网Sneaker移动端7天的销售百分比',
                    CMS_TXT_US_OFFICIAL_SNEAKER_MOBILE_SALES_30_DAYS_PERCENT: '美国官网Sneaker移动端30天的销售百分比',
                    CMS_TXT_US_OFFICIAL_SNEAKER_MOBILE_SALES_THIS_YEAR_PERCENT: '美国官网Sneaker移动端今年的销售百分比',
                    CMS_TXT_US_AMAZON_SALES_30_DAYS_PERCENT: '美国亚马逊30天的销售百分比',
                    CMS_TXT_US_AMAZON_SALES_THIS_YEAR_PERCENT: '美国亚马逊今年的销售百分比',
                    CMS_TXT_CN_SALES_7_DAYS_PERCENT: '中国7天的销售百分比',
                    CMS_TXT_CN_SALES_30_DAYS_PERCENT: '中国30天的销售百分比',
                    CMS_TXT_CN_SALES_THIS_YEAR_PERCENT: '中国今年的销售百分比',
                    CMS_TXT_TB_SALES_7_DAYS_PERCENT: '淘宝7天的销售百分比',
                    CMS_TXT_TB_SALES_30_DAYS_PERCENT: '淘宝30天的销售百分比',
                    CMS_TXT_TB_SALES_THIS_YEAR_PERCENT: '淘宝今年的销售百分比',
                    CMS_TXT_TM_SALES_7_DAYS_PERCENT: '天猫7天的销售百分比',
                    CMS_TXT_TM_SALES_30_DAYS_PERCENT: '天猫30天的销售百分比',
                    CMS_TXT_TM_SALES_THIS_YEAR_PERCENT: '天猫今年的销售百分比',
                    CMS_TXT_TG_SALES_7_DAYS_PERCENT: '天猫国际7天的销售百分比',
                    CMS_TXT_TG_SALES_30_DAYS_PERCENT: '天猫国际30天的销售百分比',
                    CMS_TXT_TG_SALES_THIS_YEAR_PERCENT: '天猫国际今年的销售百分比',
                    CMS_TXT_JD_SALES_7_DAYS_PERCENT: '京东7天的销售百分比',
                    CMS_TXT_JD_SALES_30_DAYS_PERCENT: '京东30天的销售百分比',
                    CMS_TXT_JD_SALES_THIS_YEAR_PERCENT: '京东今年的销售百分比',
                    CMS_TXT_JG_SALES_7_DAYS_PERCENT: '京东国际7天的销售百分比',
                    CMS_TXT_JG_SALES_30_DAYS_PERCENT: '京东国际30天的销售百分比',
                    CMS_TXT_JG_SALES_THIS_YEAR_PERCENT: '京东国际今年的销售百分比',
                    CMS_TXT_CN_OFFICIAL_SALES_7_DAYS_PERCENT: '中国官网7天的销售百分比',
                    CMS_TXT_CN_OFFICIAL_SALES_30_DAYS_PERCENT: '中国官网30天的销售百分比',
                    CMS_TXT_CN_OFFICIAL_SALES_THIS_YEAR_PERCENT: '中国官网今年的销售百分比',
                    CMS_TXT_US_AMAZON_SALES_7_DAYS_PERCENT: '美国亚马逊7天的销售百分比',
                    CMS_TXT_TITLE_SET_CUSTOMIZE_COLUMNS: '设置自定义列',
                    CMS_TXT_BASE_ATTRIBUTE: '基础属性',
                    CMS_TXT_PRICE_ATTRIBUTE: '价格属性',
                    CMS_TXT_SALES_ATTRIBUTE: '销售属性',
                    CMS_TXT_TITLE_SHOW_ATTRIBUTE: '将上方的属性拖放到此处,将会在产品列表中展示',
                    CMS_TXT_TITLE_ADVANCE_SEARCH_CN: '高级检索 (中国)',
                    CMS_TXT_TITLE_STATISTICS_INFO: '统计信息',
                    CMS_TXT_CATEGORY_STATISTICS_INFO: '类目信息',
                    CMS_TXT_MODEL_STATISTICS_INFO: 'Model信息',
                    CMS_TXT_PRODUCT_STATISTICS_INFO: '产品信息',
                    CMS_TXT_CATEGORY_NO_MATCH: '类目未匹配',
                    CMS_TXT_CATEGORY_ATTRIBUTE_NO_SET: '类目属性未匹配',
                    CMS_TXT_PRODUCT_ATTRIBUTE_NO_FINISH: '产品属性编辑未完成(未翻译)',
                    CMS_TXT_PRODUCT_NO_APPROVE: '产品属性编辑完成未Approve',
                    CMS_TXT_PRODUCT_NO_PUBLISH: '产品Approved但未上新',
                    CMS_TXT_PRODUCT_NO_UPLOAD: '产品已上新但还没有更新',
                    CMS_TXT_PRODUCT_UPLOAD_FAIL: '产品上新失败',

                    CMS_TXT_TH_TYPE: '类型',
                    CMS_TXT_TH_VALUE: '值',
                    CMS_TXT_TH_REQUIRED: '必填',
                    CMS_TXT_TH_IGNORE: '忽略',
                    CMS_TXT_TH_CONDITION: '条件',

                    CMS_TXT_LABEL_FEED: '第三方品牌属性',
                    CMS_TXT_LABEL_OPTIONS: '主数据的可选项',
                    CMS_TXT_LABEL_VALUE: '文本值',
                    CMS_TXT_LABEL_CMS: 'CMS 属性',
                    CMS_TXT_LABEL_DEFAULT: '默认值',

                    CMS_MSG_CHANGE_IGNORE: '确定修改忽略属性么？',
                    CMS_MSG_CHANGE_SUCCESS: '修改成功！(｡・`ω´･)',
                    CMS_MSG_NO_MATCH_TYPE: '请在单选中选择任意一个可用来源！<br /> (/= _ =)/~┴┴',
                    CMS_MSG_NO_MATCH_VALUE: '请为属性设定一个值！<br />(/"≡ _ ≡)/~┴┴',
                    CMS_MSG_NO_CONDITION_PROP: '请为条件选择一个属性！<br />(╯‵□′)╯︵┻━┻',
                    CMS_MSG_NO_CONDITION_OP: '请为条件选择一个比较的操作！<br />(┙>∧<)┙へ┻┻',
                    CMS_MSG_NO_CONDITION_VALUE: '请为条件选择一个比较的值！<br /> （╯－＿－）╯╧╧',

                    CMS_TXT_PROMOTION_NAME :'名称',
                    CMS_TXT_PROMOTION_CART :'渠道',
                    CMS_TXT_PROMOTION_ATTACHMENT :'附件',
                    CMS_TXT_PROMOTION_COMMENT :'说明',
                    CMS_TXT_PROMOTION_PREHEAT_DATE :'预热时间',
                    CMS_TXT_PROMOTION_EFFECTIVE_DATE :'有效时间',
                    CMS_TXT_PROMOTION_PRODUCT_SELECTION_IS_OVER :'Product Selection Is Over',
                    CMS_TXT_PROMOTION_ACTIVITY_SIGNUP_IS_OVER :'Activity Sign Up Is Over',
                    CMS_TXT_PROMOTION_ISOLATION_STOCK_IS_OVER :'Isolation Stock Is Over',
                    CMS_TXT_PROMOTION_ACTIVITY_IS_OVER :'Activity Is Over',
                    CMS_TXT_PROMOTION_IS_EFFECTIVE :'是否有效',
                    CMS_TXT_PROMOTION_CODE :'Code',
                    CMS_TXT_PROMOTION_PRICE :'价格',
                    CMS_TXT_PROMOTION_DISCOUNT_PERCENT :'折扣',
                    CMS_TXT_PROMOTION_SALE_PRICE :'Sale Price',
                    CMS_TXT_PROMOTION_QUANTITY :'Quantity',
                    CMS_TXT_PROMOTION_SALES_QUANTITY :'Sales Quantity',
                    CMS_TXT_PROMOTION_SALES_PERCENT :'Sales Percent',
                               
                    CMS_TXT_CARD: '销售渠道',
                    CMS_TXT_CN_PUBLISH_STATUS: '中国是否发布',
                    CMS_TXT_CN_NUMIID: '中国 num_iid',
                    CMS_TXT_CN_COMMENT: '中国 备注',
                    CMS_TXT_CN_OFFICIAL_PUBLISH_STATUS: '中国官网是否发布',
                    CMS_TXT_CN_OFFICIAL_NUMIID: '中国官网num_iid',
                    CMS_TXT_CN_OFFICIAL_COMMENT: '中国官网 备注',
                    CMS_TXT_TB_PUBLISH_STATUS: '淘宝是否发布',
                    CMS_TXT_TB_NUMIID: '淘宝num_iid',
                    CMS_TXT_TB_COMMENT: '淘宝备注',
                    CMS_TXT_TM_PUBLISH_STATUS: '天猫是否发布',
                    CMS_TXT_TM_NUMIID: '天猫num_iid',
                    CMS_TXT_TM_COMMENT: '天猫备注',
                    CMS_TXT_TG_PUBLISH_STATUS: '天猫国际是否发布',
                    CMS_TXT_TG_NUMIID: '天猫国际num_iid',
                    CMS_TXT_TG_COMMENT: '天猫国际备注',
                    CMS_TXT_JD_PUBLISH_STATUS: '京东是否发布',
                    CMS_TXT_JD_NUMIID: '京东num_iid',
                    CMS_TXT_JD_COMMENT: '京东备注',
                    CMS_TXT_JG_PUBLISH_STATUS: '京东国际是否发布',
                    CMS_TXT_JG_NUMIID: '京东国际 num_iid',
                    CMS_TXT_JG_COMMENT: '京东国际备注',
                    CMS_TXT_STATISTICAL_DATA: '统计数据',

                    CMS_MSG_ADD_TO_PROMOTION_SUCCESS: '商品添加到 promotion 成功。',
                    CMS_MSG_MOVE_TO_MODEL_SUCCESS: '商品移动到 model 成功。',
                    CMS_MSG_SET_PRODUCT_PROPERTY_SUCCESS: '设置商品属性成功。',
                    CMS_MSG_NO_DATA: '无相关数据！',

                    CMS_BTN_NEW_DICT: '新字典项',
                    CMS_TXT_HEAD_DICT: '字典管理',
                    CMS_TXT_DICT_IS_URL: '需要上传天猫 ？',
                    CMS_BTN_NEW_DICT_VALUE: '新增字典值',
                    CMS_BTN_NEW_DICT_CUS_VALUE: '新增自定义',
                    CMS_TXT_SELECT_ONE: '请选择',
                    CMS_TXT_DICT_CUSTOM: '自定义项',
                    CMS_BTN_SET_PARAM: '修改参数',
                    CMS_MSG_DICT_NO_NAME: '→_→ 你就这么懒，填个名字不行么。',
                    CMS_MSG_DICT_NO_VALUE: 'ㄟ( ▔, ▔ )ㄏ 你不添加个值或者自定义的内容。我是不会让你过去的。',
                    CMS_MSG_DICT_NO_CUS: 'o(╯□╰)o 麻烦选择一项，并填充参数后，尝试保存。',
                    CMS_MSG_DICT_CUS_NO_VALUE: '参数 [ {{unValidParamName}} ] 没有设置任何值。',
                    CMS_MSG_DICT_UN_VALID_CUS: '提供的自定义内容无效',
                    CMS_MSG_DICT_VAL_NO_TYPE: '请选择值类型',
                    CMS_MSG_DICT_VAL_NO_VALUE: '请选择或填写一个内容',
                    CMS_MSG_DICT_VAL_TITLE: '值',

                    CMS_MSG_DICT_VAL_TXT: '文本',
                    CMS_MSG_DICT_VAL_CMS: 'CMS',
                    CMS_MSG_DICT_VAL_MASTER: '主数据',
                    CMS_MSG_DICT_VAL_DICT: '字典',
                    CMS_MSG_MASTER_PROP_VALUE_SETTING_CATEGORY_SWITCH_COMFIRM:'当前商品即将被切换到【{{categoryName}}】类目。<br />整个切换过程将持续十分钟左右，请十分钟后再访问当前产品 。<br />如果十分钟后仍不能正常访问当前产品，请联系IT。<br />确认切换请点击【是】<br />取消切换请点击【否】',
                    CMS_MSG_MASTER_PROP_VALUE_SETTING_CATEGORY_SAVE_COMFIRM:'当前商品属性信息即将被保存，确认请点击【是】，如果想要取消，请点击【否】。',
                    CMS_MSG_MASTER_PROP_VALUE_SETTING_CATEGORY_NODATA:'当前商品的类目信息还没切换完毕，请稍后再访问。<br />如果十分钟后仍不能正常访问，请联系IT。',
                    CMS_BTN_SET_MASTER_COM_PROPERTY: '设置主类目共通属性',
                    CMS_TXT_SET_MASTER_COM_PROPERTY: '设置主类目共通属性',
                    CMS_TXT_SET_MASTER_COM_PROPERTY_SHELVE:'商品状态',
                    CMS_TXT_SET_MASTER_COM_PROPERTY_START_TIME:'开始时间'
                });
        }]);
});
