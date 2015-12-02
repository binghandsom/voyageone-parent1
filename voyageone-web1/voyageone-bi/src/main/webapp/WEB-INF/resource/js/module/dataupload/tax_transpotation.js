// 销售用js[时间]
// 图表实例化------------------
// srcipt标签式引入

var finance_search_cond = {
    // [时间]模块
    // 		检索时间
    time_start: '',
    time_end: '',
    // 检索条件
    // 		ShopID
    invoice_num: '',
    file_name: '',
    tax_page: 1,
    transpotation_page: 1,
    report_file_path:'',
    report_file_name:''
};

var textStyle = {fontSize: 9};

var objTax_bean = null;
var tax_page = 1;
var tax_record_count = 0;
var objTranspotation_bean = null;
var transpotation_page = 1;
var transpotation_record_count = 0;

function tax_transpotation_init() {
    $('#transpotation_kpi_radioset').buttonset();
    $("#transpotation_kpi_radioset :radio").click(function () {
        transpotation_page = 1;
        post_transpotation();
    });
    //init 产品
    init_tax_bill_table();
    init_transpotation_bill_table();

    //请求
    doGetFinancialBillTaxTranspotationDataReq();
}

//表格数据刷新
function init_tax_bill_table() {
    // 表格控件
    var grid_selector = "#tax_bill_table";
    // 表格翻页控件
    var pager_selector = "#tax_bill_gridPager";

    $(grid_selector).jqGrid({
        datatype: "local",
        width: 978,
        height: 950,
        colNames: [
            'Invoice No.',
            'File Name',
            'Document Path',
            'Upload Date'
        ],
        colModel: [
            {name: 'invoice_num', index: 'invoice_num', width: 150, editable: false, sortable: false, frozen: true},
            {name: 'file_name', width: 200, editable: false, sortable: false, frozen: true},
            {name: 'document_path', width: 460, editable: false, sortable: false, frozen: true},
            {
                name: 'file_upload_date',
                index: 'file_upload_date',
                width: 150,
                editable: false,
                sortable: false,
                frozen: true
            }
        ],
        rowNum: 30,//每页显示记录数
        pager: pager_selector,
        shrinkToFit: false,
        viewrecords: true, //是否显示行数
        scroll: true,
        ExpandColumn: 'Invoice No.',
        sortname: 'invoice_num',
        data: []
    });
    $(grid_selector).jqGrid('navGrid', pager_selector, {
        add: false,
        edit: false,
        del: false
    });
    $(grid_selector).jqGrid("setFrozenColumns");
}

//表格数据刷新
function init_transpotation_bill_table() {
    // 表格控件
    var grid_selector = "#transpotation_bill_table";
    // 表格翻页控件
    var pager_selector = "#transpotation_bill_gridPager";

    $(grid_selector).jqGrid({
        datatype: "local",
        width: 978,
        height: 950,
        colNames: [
            'Invoice No.',
            'File Name',
            'Document Path',
            'Upload Date'
        ],
        colModel: [
            {name: 'invoice_num', index: 'invoice_num', width: 150, editable: false, sortable: false, frozen: true},
            {name: 'file_name', width: 200, editable: false, sortable: false, frozen: true},
            {name: 'document_path', width: 460, editable: false, sortable: false, frozen: true},
            {
                name: 'file_upload_date',
                index: 'file_upload_date',
                width: 150,
                editable: false,
                sortable: false,
                frozen: true
            }
        ],
        rowNum: 30,//每页显示记录数
        pager: pager_selector,
        shrinkToFit: false,
        viewrecords: true, //是否显示行数
        scroll: true,
        ExpandColumn: 'Invoice No.',
        sortname: 'invoice_num',
        data: []

    });
    $(grid_selector).jqGrid('navGrid', pager_selector, {
        add: false,
        edit: false,
        del: false
    });
    $(grid_selector).jqGrid("setFrozenColumns");
}

function initGrid(grid_selector) {
    $(".jqgrow", grid_selector).contextMenu('contextMenu', {
        bindings: {
            'edit': function (t) {
                editRow();
            },
            'add': function (t) {
                addRow();
            },
            'del': function (t) {
                delRow();
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

    function delRow() {
        var grid = $(grid_selector);
        var rowKey = grid.getGridParam("selrow");
        if (rowKey) {
            grid.delGridRow(rowKey);
        }
        else {
            alert("No rows are selected");
        }
    }
}

//请求 
function doGetFinancialBillTaxTranspotationDataReq() {
    post_tax();
    post_transpotation();
}

function post_tax() {
    $("#gbox_tax_bill_table .loading").css("display", "block");
    finance_search_cond.tax_page = tax_page;
    bigdata.post(rootPath + "/manage/getDataUploadTaxData.html", finance_search_cond, doGetDataUploadTaxDataReq_end, '');
}

function post_transpotation() {
    $("#gbox_transpotation_bill_table .loading").css("display", "block");
    finance_search_cond.transpotation_page = transpotation_page;
    bigdata.post(rootPath + "/manage/getDataUploadTranspotationData.html", finance_search_cond, doGetDataUploadTranspotationDataReq_end, '');
}

function doGetDataUploadTaxDataReq_end(json) {
    // 表格控件
    var grid_selector = "#tax_bill_table";
    // 请求后数据缓存
    objTax_bean = json.taxBillBean;
    tax_page = json.tax_page;
    tax_record_count = json.tax_record_count;
    //产品
    refresh_tax_bill_table();
    // end loading
    $("#gbox_tax_bill_table .loading").css("display", "none");
    initGrid(grid_selector);
}


function doGetDataUploadTranspotationDataReq_end(json) {
    // 表格控件
    var grid_selector = "#transpotation_bill_table";
    // 请求后数据缓存
    objTranspotation_bean = json.transpotationBillBean;
    transpotation_page = json.transpotation_page;
    transpotation_record_count = json.transpotation_record_count;
    //产品
    refresh_transpotation_bill_table();
    // end loading
    $("#gbox_transpotation_bill_table .loading").css("display", "none");
    initGrid(grid_selector);
}


function refresh_tax_bill_table() {
    refresh_tax_bill_table_data();
}

//表格数据刷新
function refresh_tax_bill_table_data() {
    grid_data = [];
    taxBillBean = objTax_bean;
    for (var i = 0; i < taxBillBean.length; i++) {
        row = taxBillBean[i];
        grid_data.push(
            {
                'invoice_num': row.invoice_num,
                'file_name': row.file_name,
                'document_path': row.document_path,
                'file_upload_date': row.file_upload_date
            });
    }
    // 表格控件
    var grid_selector = "#tax_bill_table";
    // 表格数据清空
    $(grid_selector).clearGridData();
    $(grid_selector).setGridParam({
        data: grid_data
    }).trigger("reloadGrid");
}

function refresh_transpotation_bill_table() {
    refresh_transpotation_bill_table_data();
}

//表格数据刷新
function refresh_transpotation_bill_table_data() {
    grid_data = [];
    transpotationBillBean = objTranspotation_bean;
    for (var i = 0; i < transpotationBillBean.length; i++) {
        row = transpotationBillBean[i];
        grid_data.push(
            {
                'invoice_num': row.invoice_num,
                'file_name': row.file_name,
                'document_path': row.document_path,
                'file_upload_date': row.file_upload_date
            });
    }
    // 表格控件
    var grid_selector = "#transpotation_bill_table";
    // 表格数据清空
    $(grid_selector).clearGridData();
    $(grid_selector).setGridParam({
        data: grid_data
    }).trigger("reloadGrid");
}

