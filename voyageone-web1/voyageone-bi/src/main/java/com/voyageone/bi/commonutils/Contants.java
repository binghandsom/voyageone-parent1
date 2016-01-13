package com.voyageone.bi.commonutils;

public final class Contants {
	public static final String encoding = "UTF-8";
	
//	//装箱单文件名
//	public static final String DOWNLOAD_FILE_DELIVERY = "delivery";
//	public static final String DOWNLOAD_FILE_EMS = "ems";
//	public static final String DOWNLOAD_FILE_ENTRY_RECORD = "entry_record";
//	// CX用报关单
//	public static final String DOWNLOAD_FILE_DECLARATION = "declaration";
//	// 广州渠道 报关清单
//	public static final String DOWNLOAD_FILE_GZ_B2CORDER = "gz_b2c_order";
//	// 广州渠道 报关清单2
//	public static final String DOWNLOAD_FILE_GZ_B2CORDER2 = "gz_b2c_order2";
//	// 广州渠道 报关清单 商检
//	public static final String DOWNLOAD_FILE_SHANGJIAN_GZ = "shangjian_gz";
//	// 广州渠道 报关清单
//	public static final String DOWNLOAD_FILE_GZ_DECLARELIST = "gz_b2c_declarelist";
//	// transactionItems
//	public static final String DOWNLOAD_FILE_TRANSACTIONITEMS = "transactionItems";
//	// invoiceAndPackingList
//	public static final String DOWNLOAD_FILE_INVOICEANDPACKINGLIST = "invoiceAndPackingList";
//
//	// 模版基于 订单出库单
//	public static final String DOWNLOAD_FILE_KJEPORT = "Kjeport";
//	
//	//装箱单文件名
//	public static final String DOWNLOAD_FILE_RESERVATION = "ReservationList";
//	
//	// 运单文件名
//	public static final String DOWNLOAD_FILE_WAYBILL_WITHCARD = "waybill_withcard";
//	public static final String DOWNLOAD_FILE_WAYBILL_WITHOUTCARD = "waybill_withoutcard";
//	public static final String DOWNLOAD_FILE_WAYBILL_SF = "waybill_china_sf";
//	public static final String DOWNLOAD_FILE_WAYBILL_YT = "waybill_china_yt";
//	public static final String DOWNLOAD_FILE_WAYBILL_ST = "waybill_china_st";
//	
//	// tt_keyvalue表关键字
//	// 运单号（无身份证）最大值
//	public static final String KEYVALUE_TRACKINGNOWITHOUTCARD_MAX = "TRACKINGNOWITHOUTCARD_MAX";
//	// 运单号（无身份证）当前值
//	public static final String KEYVALUE_TRACKINGNOWITHOUTCARD_VALUE = "TRACKINGNOWITHOUTCARD_VALUE";
//	
//	// 硬件名称
//	public static final String HARDWARE_PRINTER_LASER = "printer_laser";
//	public static final String HARDWARE_PRINTER_STYLUS_SF = "printer_stylus_sf";
//	public static final String HARDWARE_PRINTER_STYLUS_YT = "printer_stylus_yt";
//	public static final String HARDWARE_PRINTER_ZEBRA = "printer_zebra";
//	public static final String HARDWARE_SCALE = "scale";
//	
//	//EMS打单红包热敏单打印数据模板.xls 固定输入列
//	//收件人公司
//	public static final String SHIP_COMPANY = "EMS";
//	//内件性质
//	public static final String PRODUCT_TYPE = "物品";
//	//留白二
//	public static final String COUSTOMER_CODE = "客户代码：XY-19（速递）";
//	
//	// 快递公司
//	public static final String SHIP_COMPANY_SF = "SF";
//	public static final String SHIP_COMPANY_YT = "YT";
//	
//	//进境清单 页面设置
//	public static final int ENTRY_RECORD_PAGE_MAX_ROW = 19;
//	public static final int ENTRY_RECORD_PAGE_MAX_COL = 15;
//	public static final int ENTRY_RECORD_PAGE_ROW = 14;
//	
//	// 默认到货口岸Code
//	public static final String DEFAULT_PORT_CODE = "0";
//	// 默认订单Code
//	public static final String DEFAULT_ORDER_STATUS_CODE = "0";
//	
//	// 运单一览表显示行数
//	public static final String SHIPMENT_LIST_PAGESIZE = "20";
//	// 订单一览表显示行数
//	public static final String ORDER_LIST_PAGESIZE = "20";
//	// 身份证一览表显示行数
//	public static final String IDCARD_LIST_PAGESIZE = "20";
//	// Reservation一览表显示行数
//	public static final String RESERVATION_LIST_PAGESIZE = "20";
//	
//	// 上传文件最大异常出力件数
//	public static final int UPLOAD_FILE_ERRDSP_NUM = 50;
//	
//	// 画面ID
//	// 运单一览画面
//	public static final String JSP_SHIPMENT_LIST = "ShipmentList";
//	// 货箱一览画面
//	public static final String JSP_PACKAGE_LIST = "PackageList";
//	// 物品一览画面
//	public static final String JSP_PACKAGE_ITEMS_LIST = "PackageItemsList";
//	
//	// 操作错误
//	// 装箱时错误（同一货箱，多次扫描）
//	public static final String OPT_ERROR_1 = "Scaned";
//	// 装箱时错误（其他货箱中已被装箱）
//	public static final String OPT_ERROR_2 = "In shipment[%s] package[%s].";
//	public static final String OPT_ERROR_3 = "From shipment[%s] package[%s] del, To shipment[%s] package[%s] insert";
//	
//	// 装箱时错误（其他货箱中已被装箱）
////	public static final String SEARCH_SYN_NO_ERROR_1 = "This waybill[%s] information is not found, please confirm the waybill number is correct.";
//	public static final String SEARCH_SYN_NO_ERROR_1 = "未查到此运单[%s]信息，请确认运单号码是否正确。";
//	public static final String SEARCH_SYN_NO_ERROR_2 = "运单号未输入，请确认。";
//	public static final String SEARCH_SYN_NO_ERROR_3 = "运单号查询失败，请稍后再试。";
//	
//	// 装箱时错误信息
//	//		扫描Ordernum
//	public static final String ERR_PACKING_ISSCANED = "OrderNum[%s] is printed.";//"订单[%s]相关面单已被打印"
//	public static final String ERR_PACKING_NOTEXIST = "OrderNum[%s] does not exist.";//"订单[%s]不存在"
//	public static final String ERR_PACKING_INPACKAGE = "OrderNum[%s] is in the package.";//"订单[%s]已在本箱中" 多次扫描错误
////	public static final String ERR_PACKING_ISPACKED = "OrderNum is in the shipment[%s] box[%s].Whether the shift to the box?";//"运单[%s] 货箱[%s]已装箱。是否删除追加到新的箱中？"
//	public static final String ERR_PACKING_ISPACKED = "OrderNum[%s] has been packaged, in the shipment[%s] package[%s].";//订单[%s]，在运单[%s] 货箱[%s]已装箱。
//	public static final String ERR_PACKING_ISPACKED_SAME_SHIPMENT = "OrderNum[%s] is in the same shipment. package is [%s].Do you want to pack in this package?";//订单[%s]，货箱[%s]已装箱。是否移到当前货箱中？
//	public static final String ERR_ITEM_OVERWEIGHT = "PackageItem is overweight.";//"包裹超重"
//	public static final String ERR_PACKAGE_OVERWEIGHT = "Package is overweight.";//"货箱超重"
//	public static final String ERR_ORDER_ISLOCKED = "OrderNum is locked.";//订单锁定
//	public static final String ERR_ORDER_SE_ISCANCELLED = "OrderNum[%s] has been cancelled in SE.";//订单SE中取消
//	public static final String ERR_ORDER_ISNOTPACKBLE = "OrderNum status not correct, do not pack. OrderNum status is [%s]";//订单不可打包
//	public static final String ERR_ORDER_ISNOTSENDABLE_HK = "[Ship Channel] must be [HK] and [Shipping Method] must be [SF].";//不能通过本系统发送
//	public static final String ERR_ORDER_ISNOTSENDABLE_GZ = "[Ship Channel] must be [%s].";//不能通过本系统发送
//	public static final String ERR_ORDER_ADDRESS = "Because cannot print SF bill, can not packable.";//不能打印顺丰面单，不能装箱。
//	public static final String ERR_BILL_ISNOTPRINT = "Bill is not printed. Please print bill.";//面单未打印
//	public static final String ERR_ORDER_ISMULTIBAG = "Multiple items to one receipent, do not pack."; // 该人有复数个包裹
//	public static final String ERR_ORDER_ISNOTHAVEIDCARD = "No ID Card, do not pack."; // 该人没有身份证
//	public static final String ERR_ORDER_ISNOTAPPROVEDIDCARD = "ID Card not approved, do not pack."; // 该人身份证未验证
//	public static final String ERR_ORDER_ISOVERPRICE = "Order amount exceeds 1000RMB, do not pack."; // 该包裹价格大于1000RMB
//	public static final String ERR_ORDER_ISNOTSMALLTICKETFILECREATE = "Error generating receipt, please try again in a moment."; // 订单小票没有生成，请几分钟后再试。
//	public static final String ERR_ORDER_ISNOTHAVE_POSTTAXNUM = "Order is not have posttaxnum, can not packable."; // 该订单没有行邮税号
//	
//	// 		EMS请求时错误信息
//	public static final String ERR_EMS_TIMEOUT = "EMS service request timeout.Please try again."; // ems 请求超时
//	public static final String ERR_EMS_REQFAILURE = "EMS request failure. Please contact administrator."; // ems服务 请求失败
//	public static final String ERR_EMS_ORDERTIME_FORMAT = "Order time format error. Please contact administrator."; // 订单时间格式不正确，请联系管理员
//	public static final String ERR_EMS_ORDERSMALLTICKETFILENOTFOUND = "Receipt file not found. Please contact administrator."; // 购物小票未找到
//	public static final String ERR_EMS_IDCARDFILENOTFOUND = "Id card file not found. Please contact administrator."; // 身份证未找到
//	
//	//		扫描Reservation_id
//	public static final String ERR_PACKING_RES_NOTEXIST = "Reservation_Id[%s] is not exist.";//"Reservation[%s]不存在"
//	public static final String ERR_RES_ISNOTSENDABLE = "[Ship Channel] must be [%s].The current [Ship Channel] is [%s].Do you want to continue packing?";//不能通过本系统发送
//	public static final String ERR_RES_ISNOTPACKBLE = "Reservation status is not correct, can not packable. Reservation status is [%s]";//订单不可打包
//	public static final String ERR_PACKING_RES_INPACKAGE = "ReservationId[%s] is in the package.";//"订单[%s]已在本箱中" 多次扫描错误
////	public static final String ERR_PACKING_RES_ISPACKED = "ReservationId is in the shipment[%s] box[%s].Whether the shift to the box?";//"运单[%s] 货箱[%s]已装箱。是否删除追加到新的箱中？"
//	public static final String ERR_PACKING_RES_ISPACKED = "ReservationId[%s] has been packaged, in the shipment[%s] package[%s].";//ReservationId[%s] 在运单[%s] 货箱[%s]已装箱。
//	public static final String ERR_PACKING_RES_ISPACKED_SAME_SHIPMENT = "ReservationId[%s] is in the same shipment. Package is [%s].Do you want to pack in this package?";//ReservationId[%s]， 货箱[%s]已装箱。是否移到当前货箱中？
//	public static final String ERR_PACKING_RES_SE_ISCANCELLED = "OrderNum[%s] : ReservationId[%s] has been cancelled in SE.";//订单SE中取消
//	
//	// 打印装箱一览时错误信息
//	public static final String ERR_PACKAGEITEMLIST_PARAMATER = "Analyze Paramater Error";//无打印数据
//	public static final String ERR_PACKAGEITEMLIST_NOPRINTDATA = "No packageitem's print data";//无打印数据
//	public static final String ERR_PACKAGELIST_NOPRINTDATA = "No package's print data";//无打印数据
//	public static final String ERR_PACKAGEITEMLIST_DB = "Get Package's status DB access Error";//无打印数据
//	public static final String ERR_PACKAGEITEMLIST_END_PACKING = "EndPacking DB access Error";//无打印数据
//
//	// 用户信息更新时信息
//	//		异常
//	public static final String ERR_UPDATE_USER_PARAMATER = "Analyze Paramater Error.";//参数异常
//	public static final String ERR_UPDATE_USER_PASSWORD = "Update the user password Error.";//更新异常
//	public static final String ERR_UPDATE_USER_PASSWORD_OTHER = "Update the user password, an unknown exception.";//其他异常
//	public static final String ERR_CHECK_USER_OLD_PASSWORD = "The old password is incorrect!";//旧密码不正确
//	// 		正常
//	public static final String UPDATE_USER_PASSWORD_SUCCESS = "Update the user password success!";//正常
//	
//	// 硬件更新时信息
//	//		异常
//	public static final String ERR_UPDATE_HARDWARE_PARAMATER = "Analyze Paramater Error";//参数异常
//	public static final String ERR_UPDATE_HARDWARE = "Update Paramater Error";//更新异常
//	public static final String ERR_UPDATE_HARDWARE_OTHER = "Update the hardware, an unknown exception";//其他异常
//	// 		正常
//	public static final String UPDATE_HARDWARE_SUCCESS = "Update the hardware success";//正常
//	
//	// shipped时信息
//	// 		po_number 未输入
//	public static final String ERR_SHIPPED_PO_NUMBER_NOTINPUT = "The shipment's po_number is not input.";
//	// 		contract_ref 未输入
//	public static final String ERR_SHIPPED_CONTRACT_REF_NOTINPUT = "The shipment's contract_ref is not input.";
//	// 		ship_price 未输入
//	public static final String ERR_SHIPPED_SHIPPRICE_NOTINPUT = "The shipment's ship_price is not input.";
//	// 		tracking_no未输入
//	public static final String ERR_SHIPPED_TRACKINGNO_NOTINPUT = "The shipment's tracking_no is not input.";
//	// 		有未关闭货箱
//	public static final String ERR_SHIPPED_UNCLOSED_PACKAGE = "The shipment have unclosed package.";
//	// 		没有货箱
//	public static final String ERR_SHIPPED_DONOT_HAVE_CLOSED_PACKAGE = "The shipment don`t have closed package.";
//	//		货箱Tracking No 未输入
//	public static final String ERR_SHIPPED_PKG_TRACKINGNO_NOTINPUT = "The package[%s]'s tracking no is not input.";
//	
//	// end package是信息
//	// 		重量 未输入
//	public static final String ERR_ENDPACKING_WEIGHT_NOTINPUT = "The package's actual weight is not input.";
//	//		Tracking No 未输入
//	public static final String ERR_ENDPACKING_TRACKINGNO_NOTINPUT = "The package's tracking no is not input.";
//	
//	// 身份证验证返回
//	//		通过的场合
//	public static final String ID_CARD_APPROVED_SUCCESS = "身份证验证成功。";
//	public static final String ID_CARD_FORCE_APPROVED_SUCCESS = "强制身份证验证成功。";
//	public static final String ID_CARD_APPROVED_FAILURE = "身份证验证失败，请联络管理员。";
//	public static final String ID_CARD_APPROVED_ID_EXIST = "身份证验证失败，该人ID已存在系统中验证通过。[OpId = %s]";
//	public static final String ID_CARD_APPROVED_ID_EXIST_REPEAT = "该人已存在系统中验证通过。无需重复验证";
//	//		拒绝的场合
//	public static final String ID_CARD_REFUSED_SUCCESS = "身份证拒绝成功。";
//	public static final String ID_CARD_REFUSED_FAILURE = "身份证拒绝失败，请联络管理员。";
//	public static final String ID_CARD_REFUSED_FAILURE_SAMEID = "身份证拒绝失败，相同姓名，电话的身份证已经存在。";
//	//		删除的场合
//	public static final String ID_CARD_DELETE_SUCCESS = "身份证删除成功。";
//	public static final String ID_CARD_DELETE_FAILURE = "身份证删除失败，请联络管理员。";
//	public static final String ID_CARD_DELETE_FAILURE_SAMEID = "身份证删除失败，相同姓名，电话的身份证已经存在。";
//	
//	public static final String ID_CARD_REFUSED_DELETE_DISABLE = "身份证已验证，并且关联订单已发货，不能修改。";
//	public static final String ID_CARD_UPDATE_OLDREC = "该记录已被他人更新，请刷新页面后再操作。";
//	
//	// 订单明细请求返回
//	public static final String ORDER_SEARCH_ERROR = "Search order info error。";
//	public static final String ORDER_DETAIL_LIST_NOTHING = "The order detail's record count is zero。";
//	
//	// 订单明细请求返回(从Reservation取得)
//	public static final String ORDER_RESERVATION_LIST_NOTHING = "The order reservation's record count is zero。";
//	
//	// SENotes请求返回
//	public static final String SE_NOTES_SEARCH_ERROR = "Search SE Notes error。";
//	public static final String SE_NOTES_LIST_NOTHING = "The Order's SE Notes is empty。";
//	
//	// 订单备注 Code Key
//	public final static String ID_COMMENT_KIND = "ID_COMMENT_KIND";
//	public final static String ORDER_COMMENT_KIND = "ORDER_COMMENT_KIND";
//	
//	// CODE 表对应
//	// 默认物品项目重量
//	public final static String DEFAULT_I_WT = "DEFAULT_I_WT";
//	// 运单状态Code Key
//	public final static String SHIP_STU_KEY = "S_STU";
//	// 到货口岸Code key
//	public final static String PORT_KEY = "PORT";
//	// 订单状态Code key
//	public final static String ORDER_STATUS_KEY = "ORD_STU";
//	// 顺丰支付 Code key
//	public final static String SF_PAY_KEY = "SF_PAY";
//	// 顺丰/电商特惠省列表
//	public final static String SF_E_LIST_KEY = "SF_E_LIST";
//	// 顺丰/电商特惠市列表
//	public final static String SF_E_LIST_CITY_KEY = "SF_E_LIST_CITY";
//	// 顺丰/电商特惠市列表（排除）
//	public final static String SF_E_LIST_CITY_EX_KEY = "SF_E_LIST_CITY_EX";
//	// 顺丰/电商特惠城市列表：上海发货
//	public final static String SF_E_LIST_NAME_SH = "TM";
//	// 顺丰/电商特惠城市列表：深圳发货
//	public final static String SF_E_LIST_NAME_HK = "HK";
//	// 顺丰/电商特惠城市列表：PA也是深圳发货
//	public final static String SF_E_LIST_NAME_PA = "PA";
//	// 运单渠道
//	public final static String SHIP_CHANNEL = "S_CHANNEL";
//	// 快递公司
//	public final static String EXP = "EXP";
//	// 大客户代码
//	public final static String EMS_CUSTOMER = "EMS_CUSTOMER";
//	// 美元 换算人民币 比率
//	public final static String MONEY_CHG_RMB = "MONEY_CHG_RMB";
//	// 身份证审核状态
//	public final static String IDCARD_STATUS = "IDCARD_STATUS";
//	// RESERVATION状态
//	public final static String RESERVATION_STATUS = "REV_STU";
//	// RESERVATION仓库
//	public final static String RESERVATION_STORE = "REV_STORE";
//	// 申报价值
//	public final static String DEC_VAL = "DEC_VAL";
//	// SE订单状态
//	public final static String SE_STU = "SE_STU";
//	
//	// 顺丰API信息
//	public final static String SF_API_KEY = "SF_API";
//	public final static String SF_API_TRUE_URL = "TRUE_URL";
//	public final static String SF_API_TRUE_CODE = "TRUE_CODE";
//	public final static String SF_API_TRUE_VALIDATE = "TRUE_VALIDATE";
//	
//	// 顺丰全球顺API信息
//	public final static String SF_API_GLOBAL_KEY = "SF_GLOBAL_API";
//	
//	// EMS API信息
//	public final static String EMS_API_KEY = "EMS_API";
//	public final static String EMS_API_USER = "USER";
//	public final static String EMS_API_PASSWORD = "PASSWORD";
//	public final static String EMS_API_VALID_KEY = "KEY";
//	public final static String EMS_API_EMS_CUSTOMER = "EMS_CUSTOMER";
//	public final static String EMS_API_URL = "URL";
//
//	// 快递类型
//	// -- 顺丰 --
//	public final static String BILL_TYPE_KIND_SF_1_SFBK = "1"; // 顺丰标快（不使用）
//	public final static String BILL_TYPE_KIND_SF_2_SFTH = "2"; // 顺丰特惠（不使用）
//	public final static String BILL_TYPE_KIND_SF_3_DSTH = "3"; // 顺丰电商特惠（陆运有优惠）
//	public final static String BILL_TYPE_KIND_SF_7_DSSP = "7"; // 顺丰电商速配（我们默认的）
//	public final static String BILL_TYPE_KIND_SF_17_GLOB = "17"; // 全球顺
//	// -- 圆通 --
//	public final static String BILL_TYPE_KIND_YT_1_BZ = "1"; // 圆通标准
//	
//	// 执行状态
//	public final static String EXEC_STATUS_OK = "OK";
//	public final static String EXEC_STATUS_ERROR_OTHER = "ERROR_OTHER";
//	public final static String EXEC_STATUS_ERROR_ADDRESS = "ERROR_ADDRESS";
//
//	// 内件名称 分割符
//	public final static String DESCRIPTION_SPLIT = "，";
//	public final static String DESCRIPTION_INNER_SPLIT = " ";
//	public final static String DESCRIPTION_BRAND_SPLIT = " ";
//	
//	// 快递类型分割符
//	public final static String SHIPPING_SPLIT = " ";
//	
//	// 空格分割符
//	public final static String SPACE_SPLIT = " ";
//	
//	// 上传订单 请求件数
//	public final static int ORDER_REQ_NUM = 50;
//	// 上传文件名分割符
//	public final static String UPLOAD_FILE_SPLIT = "\\.";
//	// 上传文件名扩展符
//	public final static String UPLOAD_FILE_SUFFIX = "csv";
//	// 上传文件固定列数
//	public final static int UPLOAD_FILE_FIXED_COLUMNS = 20;
//	
//	// Check规则
//	public final static String CHK_RULE_ISNEED = "NEED";
//	public final static String CHK_RULE_ISDIGIT = "DIGIT";
//	public final static String CHK_RULE_ISNUMERIC = "NUMERIC";
//	public final static String CHK_RULE_CHANNEL = "CHANNEL";
//	
//	// CHK结果
//	public final static String CHK_TRUE = "0";
//	public final static String CHK_ERROR = "1";
//	
//	// 上传异常信息
//	// 空文件
//	public final static String ERR_UPLOAD_BLANK = "upload csv file check: content is nothing";
//	// 文件扩展名
//	public final static String ERR_UPLOAD_FILETYPE = "upload csv file check: file type error";
//	// 未入力上传文件
//	public final static String ERR_UPLOAD_FILE_NOTINPUT = "upload csv file check: No File";
//	// 文件格式不正确（列数不正确）
//	public final static String ERR_UPLOAD_FILE_FORMAT = "upload csv file check: columns number is error";
//	// 上传文件服务异常
//	public final static String ERR_UPLOAD_FILE_WEBSERVICE = "upload csv file check: webservice error";
//	// 连接异常
//	public final static String ERR_UPLOAD_FILE_CONNECT = "upload csv file check: connect error";
//	
//	// 默认OrderChannel选择项
//	public final static String DEFAULT_ORDER_CHANNEL_CODE = "000";
//	public final static String DEFAULT_ORDER_CHANNEL_TEXT = "Order Channel";
//	
//	// Web service timeout
//	public final static int WEB_SERVICE_CON_TIMEOUT = 2000;
//	public final static int WEB_SERVICE_READ_TIMEOUT = 60000;
//	// Web sevice 调用结果
//	public final static String RESULT_OK = "OK";
//	public final static String RESULT_ERR = "ERR";
//	
//	// 北京时区
//	public final static int BEIJIN_TIME_ZONE = 8;
//	// *
//	public final static String ASTERISK = "*";
//	
//	// 身份证状态/是否锁定（有/无）
//	// 		显示
//	public final static String ALL = "ALL";
//	public final static String YES = "Y";
//	public final static String NO = "N";
//	
//	//		检索用值
//	public final static String ALL_VALUE = "0";
//	public final static String YES_VALUE = "1";
//	public final static String NO_VALUE = "2";
//	
//	// ems 运单号 固定文言
//	public final static String EMS_NO_STR = "BE34";
//	public final static String EMS_NO_END = "US";	
//	// ems 编码位数
//	public final static int EMS_NO_LEN = 7;
//	// ems 请求超时设值
//	public final static int EMS_TIMEOUT = 5000;
//	
//	// prop file key
//	public final static String PROP_ID_CARD_IMG_PATH = "idcard.save.image.path";
//	public final static String PROP_ID_CARD_ZIP_PATH = "idcard.save.image.path.zip";
//	public final static String PROP_RECEIPT_JPG_PATH = "receipt_jpg_path";
//	public final static String PROP_RECEIPT_ZIP_PATH = "receipt_zip_path";	
//	public final static String PROP_UPDATE_SHIPMETHOD_PATH = "ship.webservice.url.updateShipmethodUrl";
//	
//	// 正面图片文件名
//	public final static String FRONT_ID_CARD_FILE = "A.jpg";
//	
	// Ajax 请求返回值
	public final static String AJAX_RESULT_OK = "1";
	public final static String AJAX_RESULT_FALSE = "0";
//	
//	// Chart 最终层标志
//	//		最终层
//	public final static String CHART_END_LEVEL_YES = "1";
//	//		中间层
//	public final static String CHART_END_LEVEL_NO = "0";
//	
//	// Chart 数据加载标志
//	//		加载
//	public final static String CHART_LOAD_DATA_YES = "1";
//	//		不加载
//	public final static String CHART_LOAD_DATA_NO = "0";
//	
//	// Chart End Category标志
//	//		叶子Category
//	public final static String CHART_ISLEAF_CATEGORY_YES = "1";
//	//		父亲Category
//	public final static String CHART_ISLEAF_CATEGORY_NO = "0";
//	
//	// 运单搜索显示
//	public final static String ORDERTRACKING_SEARCH_SHOW = "1";
//	// 运单搜索不显示
//	public final static String ORDERTRACKING_SEARCH_NOTSHOW = "0";
//	
//	// 身份证来源
//	public final static String IDCARD_SOURCE_PC = "1";
//	public final static String IDCARD_SOURCE_SHORTURL = "2";
//	public final static String IDCARD_SOURCE_WEIXIN = "3";
//	
//	// 收件人情报变更
//	public static final String RECIPIENTS_TITLE = "[Synship]身份证上传时的提交信息与下单时（%s）的收件信息不一致";
//	public static final String RECIPIENTS_CONTENT = "Hi, 客户上传身份证时提交的收件人手机号/收件人姓名与下单时（%s）的收件信息不一致，请及时确认同步SE中的数据！";
//	public static final String RECIPIENTS_PRE_TITLE = "[Synship]身份证上传时的提交信息与预售时（%s）的收件信息不一致";
//	public static final String RECIPIENTS_PRE_CONTENT = "Hi, 客户上传身份证时提交的收件人手机号/收件人姓名与预售时（%s）的收件信息不一致，请确保客户在正式下单时会使用此信息！";
//
//	// 美国面单打印
//	public final static String BILL_FILEUS_PRINT = "1";
//	// 美国面单不打印
//	public final static String BILL_FILEUS_NOPRINT = "0";
//	
//	// 身份证上传时的错误信息
//	public static final String CREDITBIND_ERROR_EXIST = "您提交的资料已经通过审核，如果需要修改，请联系客服。";
//	public static final String CREDITBIND_ERROR_SMALL = "您上传的图片为零字节，请重新选择提交。";
//	public static final String CREDITBIND_ERROR_LARGE = "您上传的图片大小超过5M了，请重新选择提交。";
//	public static final String CREDITBIND_ERROR_TYPE = "您上传的文件不是图片，请重新选择提交。";
//	public static final String CREDITBIND_ERROR_NULL = "您上传的文件为空文件，请重新选择提交。";
//	public static final String CREDITBIND_ERROR_SAME = "您上传的身份证正反面是一样的，请重新选择提交。";
//	public static final String CREDITBIND_ERROR_UPLOAD = "身份证图片上传失败，请稍候再试。";
//	public static final String CREDITBIND_ERROR_COMMIT = "您的资料提交失败，请稍候再试。";
//	public static final String CREDITBIND_ERROR_COMMPRESS = "您的图片资料添加水印失败，请确认提交图片是否正确。";
//	public static final String CREDITBIND_SUCCESS_COMMIT = "我们已收到您提交的清关资料，如果您提交的信息与下单时的不一致，将无法有效地进行清关，请及时联系客服修改下单信息，谢谢。";
//	public static final String CREDITBIND_SUCCESS_CHANGE_COMMIT = "我们已收到您提交的清关资料，我们将以您提交的【手机号：%s；姓名：%s；】为收件信息进行发货，如有疑问，请联系客服，谢谢。";
//	
//	// 强制验证通过
//	public final static String FORCE_APPROVED_YES = "1";
//	public final static String FORCE_APPROVED_NO = "0";
//	
//	// 订单绑定有无
//	public final static String ORDERS_BIND_YES = "1";
//	public final static String ORDERS_BIND_NO = "0";
//
//	// 订单锁定有无
//	public final static String ORDERS_LOCK_YES = "1";
//	public final static String ORDERS_LOCK_NO = "0";
//	
//	// 身份证绑定有无
//	public final static String IDCARD_BIND_YES = "1";
//	public final static String IDCARD_BIND_NO = "0";
//	
//	// 短链接类型
//	public static final String SHORTURL_ID_CARD_UPLOAD = "1";
//	public static final String SHORTURL_TRACKING_INFO = "2";
//	public static final String SHORTURL_PRE_SALE = "9";
//	
//	//ChannelId
//	public static final String ORDER_CHANNEL_SN = "001";
//	public static final String ORDER_CHANNEL_PA = "002";
//	public static final String ORDER_CHANNEL_JC = "004";
//	public static final String ORDER_CHANNEL_UN = "999";
//	
//	// 水印文字
//	public static final String LOGO_WORD = "　海　关　清　关　专　用  ";
//	// 文字字体
//	public static final String FONT_TYPE = "微软雅黑";
//	
//	// 装箱区分
//	//		正常装箱
//	public static final String PKG_UPDATE_FIRST = "0";
//	//		移箱
//	public static final String PKG_UPDATE_BOX_CHANGE = "1";
//	//		渠道变化（例如：GZ -> TM)
//	public static final String PKG_UPDATE_CHG_CHANNEL = "2";
//	
//	/**
//	 * OrderTask.ajaxSaveRemark 方法的返回信息
//	 */
//	public static final String AJAX_SAVE_REMARK_ARGU_IS_NULL = "参数不能为空。";
//	/**
//	 * OrderTask.ajaxSaveRemark 方法的返回信息
//	 */
//	public static final String AJAX_SAVE_REMARK_UPDATE_FAIL = "未能更新数据。";
//	/**
//	 * OrderTask.ajaxSaveRemark 方法的返回信息
//	 */
//	public static final String AJAX_SAVE_REMARK_UPDATE_ERROR = "数据更新出现错误。";
//	
//	/**
//	 * Code 表对应。短信内容
//	 */
//	public static final String SMS_CONTENT = "SMS_CONTENT";
//	
//	/**
//	 * Code 表对应。短信配置信息
//	 */
//	public static final String SMS_INFO = "SMS_INFO";
//	public static final String SMS_PASSWORD = "PASSWORD";
//	public static final String SMS_USER = "USER";
//	public static final String SMS_WORDS = "WORDS";
//	public static final String SMS_COST = "COST";
//	public static final String SMS_LIMIT = "LIMIT";
//	
//	// chart显示对应内容		
//	public static final String TITLE_KIND_CATEGORY = "category";
//	public static final String TITLE_KIND_MODEL = "model";
//	public static final String TITLE_KIND_PRODUCT = "product";
//	public static final String TITLE_KIND_SKU = "sku";
}
