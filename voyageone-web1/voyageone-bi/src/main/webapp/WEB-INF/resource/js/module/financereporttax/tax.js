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
    pay_in_warrant_num:'',
    data_null_check:''
};

var textStyle = {fontSize: 9};

var objtax_bean = null;
var tax_page = 1;
var tax_record_count = 0;

function tax_init() {
    //init 产品
    init_tax_report_table();

    //请求
    doGetFinancialReportTaxReportDataReq();
}

//表格数据刷新
function init_tax_report_table() {
    // 表格控件
    var grid_selector = "#tax_report_table";
    // 表格翻页控件
    var pager_selector = "#tax_report_gridPager";

    $(grid_selector).jqGrid({
        datatype: "local",
        height: 1500,
        colNames: [
            'Year',
            'Month',
            'Channel',
            'Tracking No.',
            'Synship No.',
            'B/L No.',
            'VO Order No.',
            'Client Order No.',
            'Tax Bill ID',
            'Weight LB(Order)',
            'Bill Weight(Order)',
            'Weight LB(Express)',
            'Bill Weight(Express)',
            'Ship Date',
            'Web Order No.',
            'Duties & Taxes RMB',
            'Duties & Taxes USD',
            'Exchange Rate'
        ],
        colModel: [
            {name: 'year_calc', index: 'year_calc', width: 50, editable: false, sortable: false, frozen: true},
            {name: 'month_calc', width: 50, editable: false, sortable: false, frozen: true},
            {name: 'order_channel_name', width: 150, editable: false, sortable: false, frozen: true},
            {name: 'tracking_no', width: 150, editable: false, sortable: false, frozen: true},
            {name: 'syn_ship_no', width: 150, editable: false, sortable: false},
            {name: 'main_waybill_num', width: 150, editable: false, sortable: false},
            {name: 'source_order_id', width: 150, editable: false, sortable: false},
            {name: 'client_order_number', width: 150, editable: false, sortable: false},
            {name: 'pay_in_warrant_num', width: 150, editable: false, sortable: false},
            {name: 'actual_shipped_weightLB', width: 200, editable: false, sortable: false},
            {name: 'shipped_weightLB', width: 150, editable: false, sortable: false},
            {name: 'goods_weight_lb', width: 150, editable: false, sortable: false},
            {name: 'express_weight_lb', width: 150, editable: false, sortable: false},
            {name: 'ship_date', width: 100, editable: false, sortable: false},
            {name: 'order_number', width: 100, editable: false, sortable: false},
            {name: 'tax_actual', width: 100, editable: false, sortable: false},
            {name: 'tax_actual_usd', width: 100, editable: false, sortable: false},
            {name: 'exchange_rate', width: 100, editable: false, sortable: false}
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
function doGetFinancialReportTaxReportDataReq() {
    post_tax();
}

function post_tax() {
    $("#gbox_tax_report_table .loading").css("display", "block");
    finance_search_cond.report_page = tax_page;
    bigdata.post(rootPath + "/manage/getFinanceReportTax.html", finance_search_cond, doGetFinanceReportTaxDataReq_end, '');
}

function doGetFinanceReportTaxDataReq_end(json) {
    // 表格控件
    var grid_selector = "#tax_report_table";
    // 请求后数据缓存
    objtax_bean = json.taxDetailReportDisBean;
    tax_page = json.report_page;
    tax_record_count = json.tax_record_count;
    //产品
    refresh_tax_report_table();
    // end loading
    $("#gbox_tax_report_table .loading").css("display", "none");
}

function refresh_tax_report_table() {
    refresh_tax_report_table_data();
}

//表格数据刷新
function refresh_tax_report_table_data() {
    grid_data = [];
    taxReportBean = objtax_bean;
    for (var i = 0; i < taxReportBean.length; i++) {
        row = taxReportBean[i];
        grid_data.push(
            {
                'year_calc': row.year_calc,
                'month_calc': row.month_calc,
                'order_channel_name': row.order_channel_name,
                'tracking_no': row.tracking_no,
                'syn_ship_no': row.syn_ship_no,
                'main_waybill_num': row.main_waybill_num,
                'source_order_id': row.source_order_id,
                'client_order_number': row.client_order_number,
                'pay_in_warrant_num': row.pay_in_warrant_num,
                'actual_shipped_weightLB': row.actual_shipped_weightLB,
                'shipped_weightLB': row.shipped_weightLB,
                'goods_weight_lb': row.goods_weight_lb,
                'express_weight_lb': row.express_weight_lb,
                'ship_date': row.ship_date,
                'order_number': row.order_number,
                'tax_actual': row.tax_actual,
                'tax_actual_usd': row.tax_actual_usd,
                'exchange_rate': row.exchange_rate
            });
    }
    // 表格控件
    var grid_selector = "#tax_report_table";
    // 表格数据清空
    $(grid_selector).clearGridData();
    $(grid_selector).setGridParam({
        data: grid_data
    }).trigger("reloadGrid");

}
