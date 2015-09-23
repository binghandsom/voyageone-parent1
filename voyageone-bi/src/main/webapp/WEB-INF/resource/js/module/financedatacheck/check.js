// 销售用js[时间]
// 图表实例化------------------
// srcipt标签式引入

var finance_search_cond = {
    // 检索条件
    invoice_num: '',
    tracking_no: '',
    main_waybill_num: '',
    pay_in_warrant_num: '',
    error_type_id: '',
    explanation_error_status: ''
};

var textStyle = {fontSize: 9};

var objcheck_bean = null;
var check_page = 1;
var check_record_count = 0;
var objexplanation_bean = null;
var explanation_page = 1;
var explanation_record_count = 0;

function check_explanation_init() {
    $('#explanation_kpi_radioset').buttonset();
    $("#explanation_kpi_radioset :radio").click(function () {
        explanation_page = 1;
        post_explanation();
    });

    //init 产品
    init_check_upload_table();
    //init 产品
    init_explanation_table();

    //请求
    doGetFinancialCheckUploadDataReq();
}

//表格数据刷新
function init_check_upload_table() {
    // 表格控件
    var grid_selector = "#check_upload_table";
    // 表格翻页控件
    var pager_selector = "#check_upload_gridPager";

    $(grid_selector).jqGrid({
        datatype: "local",
        height: 800,
        width: 978,
        colNames: [
            'Invoice Num',
            'Main No.',
            'Related No(Bill)',
            'Related No(DB)',
            'Error Type',
            'Error Description'
        ],
        colModel: [
            {name: 'invoice_num', index: 'year_calc', width: 180, editable: false, sortable: false},
            {name: 'main_no', width: 150, editable: false, sortable: false},
            {name: 'bill_related_no', width: 150, editable: false, sortable: false},
            {name: 'db_related_no', width: 150, editable: false, sortable: false},
            {name: 'error_type_id', width: 150, editable: false, sortable: false},
            {name: 'error_description', width: 300, editable: false, sortable: false}
        ],
        viewrecords: true,
        rowNum: 30,
        rowList: [],
        pager: pager_selector,
        pginput: true,
        shrinkToFit: false,
        altRows: false
    });
    $(grid_selector).jqGrid('navGrid', pager_selector, {
        add: false,
        edit: false,
        del: false
    });
}

//表格数据刷新
function init_explanation_table() {
    // 表格控件
    var grid_selector = "#explanation_upload_table";
    // 表格翻页控件
    var pager_selector = "#explanation_upload_gridPager";

    $(grid_selector).jqGrid({
        datatype: "local",
        height: 800,
        width: 978,
        colNames: [
            'Invoice Num',
            'Main No.',
            'Related No(Bill)',
            'Related No(DB)',
            'Error Type',
            'Error Description',
            'Error Reason',
            'Error Action',
            'Error Order No.',
            'Related Order No.',
            'Error Tracking No.',
            'Related Tracking No.',
            'Error Main Waybill No.',
            'Related Main Waybill No.',
            'Error Process',
            'Error Status'
        ],
        colModel: [
            {name: 'invoice_num', index: 'year_calc', width: 180, editable: false, sortable: false},
            {name: 'main_no', width: 150, editable: false, sortable: false},
            {name: 'bill_related_no', width: 150, editable: false, sortable: false},
            {name: 'db_related_no', width: 150, editable: false, sortable: false},
            {name: 'error_type_id', width: 150, editable: false, sortable: false},
            {name: 'error_description', width: 300, editable: false, sortable: false},
            {name: 'error_reason', width: 500, editable: false, sortable: false},
            {name: 'error_deal', width: 500, editable: false, sortable: false},
            {name: 'order_num', width: 150, editable: false, sortable: false},
            {name: 'related_order_num', width: 150, editable: false, sortable: false},
            {name: 'tracking_no', width: 150, editable: false, sortable: false},
            {name: 'related_tracking_no', width: 150, editable: false, sortable: false},
            {name: 'main_waybill_num', width: 150, editable: false, sortable: false},
            {name: 'related_main_waybill_num', width: 150, editable: false, sortable: false},
            {name: 'error_process', width: 150, editable: false, sortable: false},
            {name: 'error_status', width: 150, editable: false, sortable: false}
        ],
        viewrecords: true,
        rowNum: 30,
        rowList: [],
        pager: pager_selector,
        pginput: true,
        shrinkToFit: false,
        altRows: false
    });
    $(grid_selector).jqGrid('navGrid', pager_selector, {
        add: false,
        edit: false,
        del: false
    });
}

function initGrid(grid_selector) {
    $(".jqgrow", grid_selector).contextMenu('contextMenu', {
        bindings: {
            'edit': function (t) {
                editRow();
            },
            'add': function (t) {
                addRow();
            }
        },
        onContextMenu: function (event, menu) {
            var rowId = $(event.target).parent("tr").attr("id");
            $(grid_selector).setSelection(rowId);
            return true;
        }
    });

    function addRow() {
        var grid = $(grid_selector);
        grid.editGridRow("new", {closeAfterAdd: true});
    }

    function editRow() {
        var grid = $(grid_selector);
        var rowKey = grid.getGridParam("selrow");
        if (rowKey) {
            grid.editGridRow(rowKey, {closeAfterEdit: true});
        }
        else {
            alert("No rows are selected");
        }
    }
}

//请求 
function doGetFinancialCheckUploadDataReq() {
    post_check();
    post_explanation();
}

//请求
function doUpdateFinancialCheckUploadDataReq() {

    if (check_record_count > 0) {
        post_check();
        if (check_record_count > 0) {
            bigdata.alert("Upload数据仍有错误，无法提交。");
        } else {
            post_update();
            doGetDataCheckUploadDataReq_end;
        }
    } else {
        post_update();
        doGetDataCheckUploadDataReq_end;
    }
}

function post_check() {
    $("#gbox_check_upload_table .loading").css("display", "block");
    finance_search_cond.error_page = check_page;
    bigdata.post(rootPath + "/manage/getDataCheckUpload.html", finance_search_cond, doGetDataCheckUploadDataReq_end, '');
}

function post_explanation() {
    $("#gbox_explanation_upload_table .loading").css("display", "block");
    finance_search_cond.explanation_page = explanation_page;
    finance_search_cond.explanation_error_status = $("#search_error_status").val();
    bigdata.post(rootPath + "/manage/getDataCheckUploadExplanation.html", finance_search_cond, doGetDataCheckUploadExplanationDataReq_end, '');
}

function post_update() {
    $("#gbox_check_upload_table .loading").css("display", "block");
    $("#gbox_explanation_upload_table .loading").css("display", "block");
    finance_search_cond.error_page = check_page;
    finance_search_cond.explanation_page = explanation_page;
    finance_search_cond.explanation_error_status = $("#search_error_status").val();
    bigdata.post(rootPath + "/manage/getDataCheckUpdate.html", finance_search_cond, doGetDataCheckUploadExplanationDataReq_end, '');
}

function doGetDataCheckUploadDataReq_end(json) {
    // 表格控件
    var grid_selector = "#check_upload_table";
    // 请求后数据缓存
    objcheck_bean = json.billErrorBean;
    check_page = json.error_page;
    check_record_count = json.error_record_count;
    //产品
    refresh_check_upload_table();
    // end loading
    $("#gbox_check_upload_table .loading").css("display", "none");
    initGrid(grid_selector);
}

function doGetDataCheckUploadExplanationDataReq_end(json) {
    // 表格控件
    var grid_selector = "#explanation_upload_table";
    // 请求后数据缓存
    objexplanation_bean = json.billErrorExplanationBean;
    explanation_page = json.explanation_page;
    explanation_record_count = json.explanation_record_count;
    //产品
    refresh_explanation_upload_table();
    // end loading
    $("#gbox_explanation_upload_table .loading").css("display", "none");
    initGrid(grid_selector);
}

function refresh_check_upload_table() {
    refresh_check_upload_table_data();
}

function refresh_explanation_upload_table() {
    refresh_explanation_upload_table_data();
}

//表格数据刷新
function refresh_check_upload_table_data() {
    grid_data = [];
    checkBillBean = objcheck_bean;
    for (var i = 0; i < checkBillBean.length; i++) {
        row = checkBillBean[i];
        grid_data.push(
            {
                'invoice_num': row.invoice_num,
                'main_no': row.main_no,
                'bill_related_no': row.bill_related_no,
                'db_related_no': row.db_related_no,
                'error_type_id': row.error_type_id,
                'error_description': row.error_description
            });
    }
    // 表格控件
    var grid_selector = "#check_upload_table";
    // 表格数据清空
    $(grid_selector).clearGridData();
    $(grid_selector).setGridParam({
        data: grid_data
    }).trigger("reloadGrid");

}

//表格数据刷新
function refresh_explanation_upload_table_data() {
    grid_data = [];
    explanationBillBean = objexplanation_bean;
    for (var i = 0; i < explanationBillBean.length; i++) {
        row = explanationBillBean[i];
        grid_data.push(
            {
                'invoice_num': row.invoice_num,
                'main_no': row.main_no,
                'bill_related_no': row.bill_related_no,
                'db_related_no': row.db_related_no,
                'error_type_id': row.error_type_id,
                'error_description': row.error_description,
                'error_reason': row.error_reason,
                'error_deal': row.error_deal,
                'order_num': row.order_num,
                'related_order_num': row.related_order_num,
                'tracking_no': row.tracking_no,
                'related_tracking_no': row.related_tracking_no,
                'main_waybill_num': row.main_waybill_num,
                'related_main_waybill_num': row.related_main_waybill_num,
                'error_process': row.error_process,
                'error_status': row.error_status
            });
    }
    // 表格控件
    var grid_selector = "#explanation_upload_table";
    // 表格数据清空
    $(grid_selector).clearGridData();
    $(grid_selector).setGridParam({
        data: grid_data
    }).trigger("reloadGrid");

}
