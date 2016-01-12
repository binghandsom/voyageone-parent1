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
    client_order_number: '',
    report_page: '',
    channel_ids: '',
    data_null_check: ''
};

var textStyle = {fontSize: 9};

var objcost_bean = null;
var cost_page = 1;
var cost_record_count = 0;

function cost_init() {
    //init 产品
    init_cost_report_table();

    //请求
    doGetFinancialReportCostReportDataReq();
}

//表格数据刷新
function init_cost_report_table() {
    // 表格控件
    var grid_selector = "#cost_report_table";
    // 表格翻页控件
    var pager_selector = "#cost_report_gridPager";

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
            'Web Order No.',
            'VO Order No.',
            'Client Order No.',
            'Weight LB(Order)',
            'Bill Weight(Order)',
            'Weight LB(Express)',
            'Bill Weight(Express)',
            'Ship Date',
            'Duties & Taxes RMB',
            'Express Fee',
            'Mail Fee',
            'Ground Handling Fee',
            'Storage Charges',
            'Identification Fee'
        ],
        colModel: [
            {name: 'year_calc', index: 'year_calc', width: 50, editable: false, sortable: false, frozen: true},
            {name: 'month_calc', width: 50, editable: false, sortable: false, frozen: true},
            {name: 'order_channel_name', width: 150, editable: false, sortable: false, frozen: true},
            {name: 'tracking_no', width: 150, editable: false, sortable: false, frozen: true},
            {name: 'syn_ship_no', width: 150, editable: false, sortable: false, frozen: true},
            {name: 'main_waybill_num', width: 150, editable: false, sortable: false},
            {name: 'source_order_id', width: 150, editable: false, sortable: false},
            {name: 'order_number', width: 150, editable: false, sortable: false},
            {name: 'client_order_number', width: 150, editable: false, sortable: false},
            {name: 'actual_shipped_weightLB', width: 200, editable: false, sortable: false},
            {name: 'shipped_weightLB', width: 150, editable: false, sortable: false},
            {name: 'goods_weight_lb', width: 150, editable: false, sortable: false},
            {name: 'express_weight_lb', width: 150, editable: false, sortable: false},
            {name: 'ship_date', width: 100, editable: false, sortable: false},
            {name: 'tax_actual', width: 100, editable: false, sortable: false},
            {name: 'transpotation_amount', width: 100, editable: false, sortable: false},
            {name: 'mail_fee', width: 100, editable: false, sortable: false},
            {name: 'ground_handling_fee', width: 100, editable: false, sortable: false},
            {name: 'storage_charges', width: 100, editable: false, sortable: false},
            {name: 'identification_fee', width: 100, editable: false, sortable: false}
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
    $(grid_selector).jqGrid('navGrid', pager_selector, {
        add: false,
        edit: false,
        del: false
    });
    $(grid_selector).jqGrid("setFrozenColumns");
}

//请求 
function doGetFinancialReportCostReportDataReq() {
    post_cost();
}

function post_cost() {
    $("#gbox_cost_report_table .loading").css("display", "block");
    finance_search_cond.report_page = cost_page;
    bigdata.post(rootPath + "/manage/getFinanceReportCost.html", finance_search_cond, doGetFinanceReportCostDataReq_end, '');
}

function doGetFinanceReportCostDataReq_end(json) {
    // 表格控件
    var grid_selector = "#cost_report_table";
    // 请求后数据缓存
    objcost_bean = json.detailReportDisBean;
    cost_page = json.report_page;
    cost_record_count = json.report_record_count;
    //产品
    refresh_cost_report_table();
    // end loading
    $("#gbox_cost_report_table .loading").css("display", "none");
}

function refresh_cost_report_table() {
    refresh_cost_report_table_data();
}

//表格数据刷新
function refresh_cost_report_table_data() {
    grid_data = [];
    costBillBean = objcost_bean;
    for (var i = 0; i < costBillBean.length; i++) {
        row = costBillBean[i];
        grid_data.push(
            {
                'year_calc': row.year_calc,
                'month_calc': row.month_calc,
                'order_channel_name': row.order_channel_name,
                'tracking_no': row.tracking_no,
                'syn_ship_no': row.syn_ship_no,
                'main_waybill_num': row.main_waybill_num,
                'source_order_id': row.source_order_id,
                'order_number': row.order_number,
                'client_order_number': row.client_order_number,
                'actual_shipped_weightLB': row.actual_shipped_weightLB,
                'shipped_weightLB': row.shipped_weightLB,
                'goods_weight_lb': row.goods_weight_lb,
                'express_weight_lb': row.express_weight_lb,
                'ship_date': row.ship_date,
                'tax_actual': row.tax_actual,
                'transpotation_amount': row.transpotation_amount,
                'mail_fee': row.mail_fee,
                'ground_handling_fee': row.ground_handling_fee,
                'storage_charges': row.storage_charges,
                'identification_fee': row.identification_fee
            });
    }
    // 表格控件
    var grid_selector = "#cost_report_table";
    // 表格数据清空
    $(grid_selector).clearGridData();
    $(grid_selector).setGridParam({
        data: grid_data
    }).trigger("reloadGrid");

}
