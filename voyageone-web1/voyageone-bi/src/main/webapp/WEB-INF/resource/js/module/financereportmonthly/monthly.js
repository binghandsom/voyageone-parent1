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
    data_null_check:''
};

var textStyle = {fontSize: 9};

var objmonthly_bean = null;
var monthly_page = 1;
var monthly_record_count = 0;

function monthly_init() {
    //init 产品
    init_monthly_report_table();

    //请求
    doGetFinancialReportMonthlyReportDataReq();
}

//表格数据刷新
function init_monthly_report_table() {
    // 表格控件
    var grid_selector = "#monthly_report_table";
    // 表格翻页控件
    var pager_selector = "#monthly_report_gridPager";

    $(grid_selector).jqGrid({
        datatype: "local",
        width: 978,
        height: 1000,
        colNames: [
            'Year',
            'Month',
            'Channel',
            'Weight LB(Order)',
            'Bill Weight(Order)',
            'Weight LB(Express)',
            'Bill Weight(Express)',
            'Duties & Taxes RMB',
            'Express Fee',
            'Mail Fee',
            'Ground Handling Fee',
            'Storage Charges'
        ],
        colModel: [
            {name: 'year_calc', index: 'year_calc', width: 50, editable: false, sortable: false, frozen: true},
            {name: 'month_calc', width: 50, editable: false, sortable: false, frozen: true},
            {name: 'order_channel_name', width: 150, editable: false, sortable: false, frozen: true},
            {name: 'actual_shipped_weightLB', width: 200, editable: false, sortable: false},
            {name: 'shipped_weightLB', width: 150, editable: false, sortable: false},
            {name: 'goods_weight_lb', width: 150, editable: false, sortable: false},
            {name: 'express_weight_lb', width: 150, editable: false, sortable: false},
            {name: 'tax_actual', width: 100, editable: false, sortable: false},
            {name: 'transpotation_amount', width: 100, editable: false, sortable: false},
            {name: 'mail_fee', width: 100, editable: false, sortable: false},
            {name: 'ground_handling_fee', width: 100, editable: false, sortable: false},
            {name: 'storage_charges', width: 100, editable: false, sortable: false}
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
function doGetFinancialReportMonthlyReportDataReq() {
    post_monthly();
}

function post_monthly() {
    $("#gbox_monthly_report_table .loading").css("display", "block");
    finance_search_cond.report_page = monthly_page;
    bigdata.post(rootPath + "/manage/getDataReportMonthly.html", finance_search_cond, doGetDataReportMonthlyDataReq_end, '');
}

function doGetDataReportMonthlyDataReq_end(json) {
    // 表格控件
    var grid_selector = "#monthly_report_table";
    // 请求后数据缓存
    objmonthly_bean = json.monthlyReportDisBean;
    monthly_page = json.monthly_page;
    monthly_record_count = json.monthly_record_count;
    //产品
    refresh_monthly_report_table();
    // end loading
    $("#gbox_monthly_report_table .loading").css("display", "none");
}

function refresh_monthly_report_table() {
    refresh_monthly_report_table_data();
}

//表格数据刷新
function refresh_monthly_report_table_data() {
    grid_data = [];
    monthlyBillBean = objmonthly_bean;
    for (var i = 0; i < monthlyBillBean.length; i++) {
        row = monthlyBillBean[i];
        grid_data.push(
            {
                'year_calc': row.year_calc,
                'month_calc': row.month_calc,
                'order_channel_name': row.order_channel_name,
                'actual_shipped_weightLB': row.actual_shipped_weightLB,
                'shipped_weightLB': row.shipped_weightLB,
                'goods_weight_lb': row.goods_weight_lb,
                'express_weight_lb': row.express_weight_lb,
                'tax_actual': row.tax_actual,
                'transpotation_amount': row.transpotation_amount,
                'mail_fee': row.mail_fee,
                'ground_handling_fee': row.ground_handling_fee,
                'storage_charges': row.storage_charges
            });
    }
    // 表格控件
    var grid_selector = "#monthly_report_table";
    // 表格数据清空
    $(grid_selector).clearGridData();
    $(grid_selector).setGridParam({
        data: grid_data
    }).trigger("reloadGrid");
}
