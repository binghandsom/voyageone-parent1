package com.voyageone.batch.wms;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public interface WmsConstants {

    /**
     * 数据有效性
     */
    final class ACTIVE {
        //可用
        public final static String  USABLE = "1";
        //不可用
        public final static String  Disabled = "0";
    }

    /**
     * 库存分配时标志位设置（WmsSetAllotInventoryJob）
     */
    final class SYN_FLG {
        //syn_flg更新忽略
        public final static String IGONRE = "4";
        //syn_flg更新ItemDetails成功
        public final static String SUCCESS = "1";
        //syn_flg初期值
        public final static String INITAL  = "0";
    }

    /**
     * 库存分配时SKU错误邮件（WmsSetAllotInventoryJob）
     */
    final class EmailSetAllotInventoryErrorSku {

        // 表格式
        public final static String TABLE = "<div><span>%s</span>"
                + "<table><tr>"
                + "<th></th><th>Shop</th><th>OrderNum</th><th>Tid</th><th>DetailItem</th><th>ReservationId</th><th>Product</th><th><font color='red'>SKU</font></th>"
                + "</tr>%s</table></div>";
        // 行格式
        public final static String ROW = "<tr>"
                + "<td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td>"
                + "</tr>";
        // 邮件名
        public final static String SUBJECT = "[Wms] 订单中存在错误的SKU";
        // 概要说明
        public final static String HEAD = "以下订单中存在错误的SKU，请确认";
        // 件数
        public final static String COUNT = "错误总数：%s";

    }

    /**
     * 库存分配时拆单警告邮件（WmsSetAllotInventoryJob）
     */
    final class EmailSetAllotInventoryErrorSpilt {

        // 表格式
        public final static String TABLE = "<div><span>%s</span>"
                + "<table><tr>"
                + "<th></th><th>Shop</th><th>OrderNum</th><th>WebID</th><th><font color='red'>Store</font></th>"
                + "</tr>%s</table></div>";
        // 行格式
        public final static String ROW = "<tr>"
                + "<td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td>"
                + "</tr>";
        // 邮件名
        public final static String SUBJECT = "%s需拆分订单一览";
        // 概要说明
        public final static String HEAD = "<font color='red'>以下订单中存在不同的仓库，需要拆分，请确认</font>";
        // 件数
        public final static String COUNT = "拆分总数：%s";

    }

    /**
     * 同步Synship错误邮件（WmsSyncChangeToSynShipJob）
     */
    final class EmailWmsSyncChangeToSynShipJob {

        // 表格式
        public final static String TABLE = "<div><span>%s</span>"
                + "<table><tr>"
                + "<th></th><th>Channel</th><th>OrderNum</th><th>ReservationId</th><th>ReservationStatus</th><th>ShipChannel</th><th>ErrorMessage</th>"
                + "</tr>%s</table></div>";
        // 行格式
        public final static String ROW = "<tr>"
                + "<td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td>"
                + "</tr>";
        // 邮件名
        public final static String SUBJECT = "[Wms] 同步到Synship发生错误";
        // 概要说明
        public final static String HEAD = "以下物品同步到Synship时发生错误，请确认";
        // 件数
        public final static String COUNT = "错误总数：%s";

    }



    /**
     * 解析第三方【库存文件】警告邮件（WmsThirdFileDataProcessingJob）
     */
    final class EmailClientInventoryError {

        // 表格式
        public final static String TABLE = "<div><span>%s</span>"
                + "<table><tr>"
                + "<th></th><th>OrderChannelId</th><th>ClientSku</th><th>Qty</th><th>StoreId</th><th>ItemCode</th><th>Sku</th><th>Size</th>"
                + "</tr>%s</table></div>";
        // 行格式
        public final static String ROW = "<tr>"
                + "<td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td>"
                + "</tr>";
        // 邮件名
        public final static String SUBJECT_NOT_EXISTS_SIZEMAPPING = "[Wms] 临时库存表中在SizeMapping表中不存在的记录";
        // 概要说明
        public final static String HEAD_NOT_EXISTS_SIZEMAPPING = "以下是临时库存表中在SizeMapping表中不存在的记录，请确认";
        // 邮件名
        public final static String SUBJECT_NOT_EXISTS_ITEMDEAILS = "[Wms] 临时库存表中在ItemDetails表中不存在的记录";
        // 概要说明
        public final static String HEAD_NOT_EXISTS_ITEMDEAILS = "以下是临时库存表中在ItemDetails表中不存在的记录，请确认";
        // 件数
        public final static String COUNT = "错误总数：%s";

    }

    /**
     * 仓库名称（为了对应synship：国内仓库统称为TM，虚拟仓库统称为CH）
     */
    final  class StoreName {
        // 国内仓库
        public final static String CN = "TM";
        // 虚拟仓库k
        public final static String CH = "CH";
    }

    /**
     * 产品种类
     */
    final class ProductType {

        public final static String SHOES = "Shoes";
    }

    /**
     * 产品材质
     */
    final class Material {

        public final static String LEATHER = "Leather";
        public final static String RUBBER = "Rubber";
    }

    /**
     * 产品材质
     */
    final class CurrencyUnit {

        public final static String RMB = "RMB";
        public final static String USD = "USD";
    }

    /**
     * CloseDayFlg
     */
    final  class CloseDayFlg {
        public final static String Process = "0";
        public final static String Sim = "1";
        public final static String Done = "2";
    }

    //第三方库存更新方式 FULL: 全量； INCREACE: 增量
    final class updateClientInventoryConstants {
        public final static String FULL = "FULL";
        public final static String INCREACE = "INCREACE";
        //和com_mt_third_party_config表中的prop_name对应
        public final static String NAMESPACE= "ca_url_namespace";
        public final static String URL = "ca_url_api_inventory";
        public final static String ACCOUNTID = "ca_account_id";
        public final static String DEVELOPERKEY = "ca_developer_key";
        public final static String PASSWORD = "ca_password";
        public final static String LABELNAME = "ca_label_name";
        public final static String DATERANGEFIELD = "ca_daterangefield";
        public final static String POSTACTION = "ca_inventory_action";
        public final static String POSTITEMACTION ="ca_inventory_item_action";
        public final static String FULLUPDATECONFIG= "ca_full_update_config";
        public final static String PAGESIZE= "ca_update_pagesize";
        public final static String INVENTORYFULLUPDATECONFIG= "inventory_full_update_config";
        public final static String INVENTORYPAGESIZE= "inventory_update_pagesize";
        public final static String CENTERCODE = "ca_inventory_center_code";
    }

    /**
     * ShipChannel
     */
    final  class ShipChannel {
        public final static String GZ = "GZ";
        public final static String HK = "HK";
        public final static String PA = "PA";
        public final static String SYB = "SYB";
        public final static String TM = "TM";
    }


    //斯伯丁日报标题用
    final class spaldingReportTitle{
        //销售订单标题
        public static final HashMap<String, String> SALES_ORDER_TITLE = new HashMap<String, String>() {
            {
                put("SalesType", "销售订单");
                put("Warehouse", "0503");
                put("Ordertype", "0");
                put("Site","01");
            }
        };
        //退货订单(退回到上海仓库)标题
        public static final HashMap<String, String> RETURN_TP_TITLE = new HashMap<String, String>() {
            {
                put("SalesType", "退货订单");
                put("Warehouse", "0504");
                put("Ordertype", "1");
                put("Site","01");
            }
        };
        //退货订单(退回到福建仓库)标题
        public static final HashMap<String, String> RETURN_SPALDING_TITLE = new HashMap<String, String>() {
            {
                put("SalesType", "退货订单");
                put("Warehouse", "0504");
                put("Ordertype", "2");
                put("Site","01");
            }
        };
        //特殊物品销售订单标题
        public static final HashMap<String, String> SPECIAL_SALES_ORDER_TITLE = new HashMap<String, String>() {
            {
                put("SalesType", "销售订单");
                put("Warehouse", "0201");
                put("Ordertype", "4");
                put("Site","01");
            }
        };
    }



    //斯伯丁日报标题
    final class spaldingReportTitleHead {
        //销售订单标题
        public static final String[] SALES_ORDER_TITLE_HEAD={"OrderType","DocumentID","CustAccount","InventSiteId","InventLocationId",
                "ShippingDateRequested","E-ComSalesId","ItemId","Size","Qty","SalesPrice","SalesUnit","LineAmount","Transcost","Discount"};


        //退货订单标题
        public static final String[]  RETURN_TP_TITLE_HEAD = {"OrderType","DocumentID","CustAccount","ReasonCode","InventSiteId","InventLocationId",
                "ShippingDateRequested","E-ComSalesId","ItemId","Size","Qty","SalesPrice","SalesUnit","LineAmount","Transcost","Discount"};
    }





        //斯伯丁日报文件用
    final class spaldingReportFileName {
        //初始化List
        public static final  List<String> FILE_NAME_A = new ArrayList() {{
            add("a");
            add("b");
            add("c");
            add("d");
        }};
    }

    //斯伯丁日报模板文件
    final class spaldingReportTemplateFile {
        public static final  String SPALDING_REPORT_TEMPLATE = "wms.spalding.template.file";
    }

    //FTP,SFTP服务器设置取得用
    final class ftpValues {
        //ftp连接URL
        public static final String FTP_URL = "ftp_address";
        // ftp连接port
        public static final String FTP_PORT = "ftp_port";
        // ftp连接usernmae
        public static final String FTP_USERNAME = "ftp_uid";
        // ftp连接password
        public static final String FTP_PASSWORD = "ftp_pwd";
        // ftp连接password
        public static final String FTP_MAX_NEXT_TIME = "ftp_max_next_time";
        //ftp连接类型
        public static final String FTP_TYPE = "ftp_type";
        //ftp下载文件路径
        public static final String FTP_DOWNLOAD_FILE_PATH = "download_file_path";
    }

    // 库存同步类型
    final class QuantityUpdateType {
        public static final  String PRODUCT = "p";
        public static final  String SKU = "s";
    }

    //斯伯丁日报Size变化用
    final class spaldingReportSizeChange {
        //销售订单标题
        public static final HashMap<String, String> SIZE_LIST = new HashMap<String, String>() {
            {
                put("XXL", "2XL");
                put("XXXL", "3XL");
                put("XXXXL", "4XL");
                put("XXXXXL", "5XL");
                put("OneSize", "");
            }
        };
    }

    // 斯伯丁报表特殊商品类型
    final class specialType {
        public static final  String BACKBOARD = "special_goods_backboard";
        public static final  String BALL = "special_goods_ball";
    }

    // BCBG入库文件
    final class INVENTORY_INCOMING {
        public static final  String HEAD = "H";
        public static final  String DETAIL = "D";
    }

    //库存同步类型 0: 强制；  1: 全量； 2: 增量
    final class InventorySynType {
        public final static String FORCE = "0";
        public final static String FULL = "1";
        public final static String INCREACE = "2";
    }

    // 库存再分配
    final class ALLOT_INVENTORY_AGAIN {
        public static final  String INVENTORY_MANAGER = "inventory_manager";
        public static final  String STORE_AREA = "store_area";
    }


    //BCBG年报标题
    final class bcbgReportTitleHead {
        //销售订单标题
        public static final String[] PHYSICALS_TITLE_HEAD={"WERKS","DATE","LGORT","EAN/UPC","PRICE",
                "MENGE","RECRODTYPE","PRICEFLAG","ARTICLE"};
    }

    //库存同步类型 1: 不同区域；  2: 不同库存管理
    final class ORDER_SPLIT {
        public final static String AREA = "1";
        public final static String INVENTORY = "2";
    }

    /**
     * 斯伯丁第三方仓库发货日报
     */
    public final static class SPThirdWarehouseReportItems {
        // 模板Sheet
        public final static int TEMPLATE_SHEET = 0;

        // 增加标准行
        public final static int OLD_ROWS = 1;

        // 增加起始行数
        public final static int ADD_START_ROWS = 2;

        // 开始列
        public final static int Column_Start = 0;
        // 结束列
        public final static int Column_End = 15;

        //序号
        public final static int Column_Index = 0;
        //付款时间
        public final static int Column_Order_date_time = 1;
        //官网订单号
        public final static int Column_Source_order_id = 2;
        // 	内部订单号
        public final static int Column_Order_number = 3;
        //购买物品
        public final static int Column_Sku = 4;
        // 	数量
        public final static int Column_Qty = 5;
        // 	物品类型
        public final static int Column_Product = 6;
        // 	收货人姓名
        public final static int Column_Ship_name = 7;
        // 	手机
        public final static int Column_Ship_phone = 8;
        // 	省
        public final static int Column_Ship_state = 9;
        // 	市
        public final static int Column_Ship_city = 10;
        // 	详细地址
        public final static int Column_Ship_address = 11;
        // 	备注
        public final static int Column_Customer_comments = 12;
        // 	承运方
        public final static int Tracking_type = 13;
        // 	运单号
        public final static int Tracking_no = 14;

    }

    /**
     * 斯伯丁第三方仓库发货日报邮件
     */
    final class EmailSPThirdWarehouseReport {

        // 邮件名
        public final static String SUBJECT = "斯伯丁第三方仓库发货日报【%s】 ";
        // 概要说明
        public final static String HEAD = "<font color='black'>从%s至%s官网共有%s单需要发货，请按附件中所列订单信息发货，谢谢。</font>";

    }

    /**
     * SP第三方仓库发货文件错误记录邮件（EmailSpThirdReportErrShipping）
     */
    final class EmailSpThirdReportErrShipping {

        // 表格式
        public final static String TABLE = "<div><span>%s</span>"
                + "<table><tr>"
                + "<th></th><th>WebID</th><th>OrderNum</th><th>Sku</th><th>TrackingType</th><th>TrackingNo</th>"
                + "<th>OrderDateTime</th><th>ErrReason</th>"
                + "</tr>%s</table></div>";
        // 行格式
        public final static String ROW = "<tr>"
                + "<td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td>"
                + "<td>%s</td><td>%s</td>"
                + "</tr>";
        // 邮件名
        public final static String SUBJECT = "%s第三方仓库发货文件错误记录一览";
        // 概要说明
        public final static String HEAD = "<font color='red'>%s文件中，以下订单数据解析时有错误，请确认</font>";
        // 件数
        public final static String COUNT = "订单总数：%s";

    }


    /**
     * SP第三方仓库发货文件错误理由（SpThirdReportErrReason）
     */
    final class SpThirdReportErrReason {
        // 承运方为空
        public final static String TRACHING_TYPE_NULL = "承运方为空,运单号不为空，数据没有保持一致";
        // 承运方不是规定的物流公司
        public final static String TRACHING_TYPE_ERR = "承运方不是规定的物流公司";
        // 运单号为空
        public final static String TRACHING_NO_NULL = "运单号为空,承运方不为空，数据没有保持一致";
        // 运单号中间有空格
        public final static String TRACHING_NO_ERR = "运单号中间有空格";
        // 订单不存在
        public final static String ORDER_NO_ERR = "订单不存在";
        // 该订单已经有运单号
        public final static String ClIENT_TRACKING_ERR = "该订单已经有运单号";
    }

}
