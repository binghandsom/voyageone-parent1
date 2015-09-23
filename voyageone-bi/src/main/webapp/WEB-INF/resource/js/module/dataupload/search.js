

//画面初期化
function initSearchPage() {
    //画面检索条件初期化
    initSearchParam();
    // 画面控件初期值设定
    doPageInitDataReq();
    //	[检索]按钮
    $('#search').button();
    $("#search").click(function () {
        initGetCond();
    })

    //	[检索]按钮
    $('#export').button();
    $("#export").click(function(){
        initGetCond();
    })
}


//画面检索条件初期化
function initSearchParam() {
    //日期控件初期设置
    initSearchDate();

    //	[检索]按钮
    $('#search').button();
    $("#search").click(function(){
        initGetCond();
        uploadFinanceBillDataParam();
    })

    //	[检索]按钮
    $('#export').button();
    $("#export").click(function(){
        initGetCond();
        uploadFinanceBillDataParamExport();
    })
}

//日期控件初期设置
function initSearchDate() {
    var dayOption = {
        format: 'yyyy-mm-dd',
        startView: 2,
        minView: 2,
        maxView: 2,
        todayHighlight: 1,
        autoclose: 1
    };

    // 日
    $("#search_date_from").datetimepicker('remove');
    $("#search_date_from").datetimepicker(dayOption);
    $("#search_date_to").datetimepicker('remove');
    $("#search_date_to").datetimepicker(dayOption);
    // 初期值设定
    var startSearchDate = getDateStr(-90);
    $("#search_date_from").val(startSearchDate);
    var initSearchDate = getDateStr(-1);
    $("#search_date_to").val(initSearchDate);

}

// 画面初期显示内容取得
function doPageInitDataReq() {
    // 画面检索条件取得
    initGetCond();
    $('#container').trigger("search_page_loaded");
}

// 画面检索条件取得
function initGetCond() {
    // 日期
    finance_search_cond.time_start = $("#search_date_from").val();
    finance_search_cond.time_end = $("#search_date_to").val();
    // 账单编号
    finance_search_cond.invoice_num = $("#search_invoice_num").val();
    // 账单名称
    finance_search_cond.file_name = $("#search_file_name").val();
    return true;
}

