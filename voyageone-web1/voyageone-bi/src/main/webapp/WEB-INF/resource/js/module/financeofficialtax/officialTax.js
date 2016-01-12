// 销售用js[时间]
// 图表实例化------------------
// srcipt标签式引入

var finance_search_cond = {
    // [时间]模块
    // 		检索时间
    time_start: '',
    time_end: '',
    // 检索条件
    tracking_no: '',
    source_order_id: '',
    order_number: '',
    report_page:'',
    channel_ids : '',
    port_ids : '',
    data_null_check:''
};

var textStyle = {fontSize: 9};

var objtax_bean = null;
var tax_page = 1;
var tax_record_count = 0;

function tax_init() {
    //init 产品
    init_official_tax_report_table();

    //请求
    doGetFinancialReportOfficialTaxReportDataReq();
}

//表格数据刷新
function init_official_tax_report_table() {
    // 表格控件
    var grid_selector = "#official_tax_report_table";
    // 表格翻页控件
    var pager_selector = "#tax_report_gridPager";

    $(grid_selector).jqGrid({
        datatype: "local",
        height: 1500,
        colNames: [
            'Year',
            'Month',
            'Channel',
            'Port',
            'Shipping Date',
            'Tracking No.',
            'Synship No.',
            'B/L No.',
            'Order No.',
            'Source Order No.',
            'Client Order No.',
            'SKU',
            'HS Code',
            'Rate(%)',
            'RMB Price(￥)',
            'Exchange Rate',
            'Declared Value($)',
            'Declared Quantity',
            'Custom Duties($)'
        ],
        colModel: [
            {name: 'year_calc', index: 'year_calc', width: 60, editable: false, sortable: false, frozen: true},
            {name: 'month_calc', width: 60, editable: false, sortable: false, frozen: true},
            {name: 'order_channel_name', width: 60, editable: false, sortable: false, frozen: true},
            {name: 'port_id', width: 60, editable: false, sortable: false, frozen: true},
            {name: 'ship_date', width: 150, editable: false, sortable: false},
            {name: 'tracking_no', width: 150, editable: false, sortable: false},
            {name: 'syn_ship_no', width: 150, editable: false, sortable: false},
            {name: 'main_waybill_num', width: 150, editable: false, sortable: false},
            {name: 'order_number', width: 150, editable: false, sortable: false},
            {name: 'source_order_id', width: 200, editable: false, sortable: false},
            {name: 'client_order_number', width: 150, editable: false, sortable: false},
            {name: 'sku', width: 150, editable: false, sortable: false},
            {name: 'hs_code_use', width: 150, editable: false, sortable: false},
            {name: 'rate(%)', width: 100, editable: false, sortable: false},
            {name: 'rmb_price', width: 150, editable: false, sortable: false},
            {name: 'exchange_rate', width: 150, editable: false, sortable: false},
            {name: 'declare_price($)', width: 150, editable: false, sortable: false},
            {name: 'declare_quantity', width: 150, editable: false, sortable: false},
            {name: 'custom_duties($)', width: 150, editable: false, sortable: false}
        ],
        viewrecords : true,
        rowNum:100,
        rowList:[],
        pager : pager_selector,
        pginput:true,
        autowidth:true,
        shrinkToFit:false,
        altRows: false
    });
    $(grid_selector).jqGrid("setFrozenColumns");
}

//请求 
function doGetFinancialReportOfficialTaxReportDataReq() {
    post_tax();
}

function post_tax() {
    $("#gbox_official_tax_report_table .loading").css("display", "block");
    finance_search_cond.report_page = tax_page;
    bigdata.post(rootPath + "/manage/getFinanceReportOfficialTax.html", finance_search_cond, doGetFinanceReportOfficialTaxDataReq_end, '');
}

function doGetFinanceReportOfficialTaxDataReq_end(json) {
    // 表格控件
    var grid_selector = "#official_tax_report_table";

    // 请求后数据缓存
    objtax_bean = json.officialTaxBean;
    tax_page = json.report_page;
    tax_record_count = json.report_record_count;
    //产品
    refresh_official_tax_report_table();
    // end loading
    $("#gbox_official_tax_report_table .loading").css("display", "none");
}

function refresh_official_tax_report_table() {
    refresh_official_tax_report_table_data();
}

//表格数据刷新
function refresh_official_tax_report_table_data() {
    grid_data = [];
    officialTaxReportBean = objtax_bean;
    for (var i = 0; i < officialTaxReportBean.length; i++) {
        row = officialTaxReportBean[i];
        grid_data.push(
            {
                'year_calc': row.year_calc,
                'month_calc': row.month_calc,
                'order_channel_name': row.order_channel_name,
                'port_id': row.port_id,
                'ship_date': row.ship_date,
                'tracking_no': row.tracking_no,
                'syn_ship_no': row.syn_ship_no,
                'main_waybill_num': row.main_waybill_num,
                'order_number': row.order_number,
                'source_order_id': row.source_order_id,
                'client_order_number': row.client_order_number,
                'sku': row.sku,
                'hs_code_use': row.hs_code_use,
                'rate(%)': row.rate + "%",
                'rmb_price': row.rmb_price,
                'exchange_rate': row.exchange_rate,
                'declare_price($)': row.declare_price,
                'declare_quantity': row.declare_quantity,
                'custom_duties($)': row.custom_duties
            });
    }
    // 表格控件
    var grid_selector = "#official_tax_report_table";
    // 表格数据清空
    $(grid_selector).clearGridData();
    $(grid_selector).setGridParam({
        data: grid_data
    }).trigger("reloadGrid");

}
