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
    $('#update').button();
    $("#update").click(function(){
        initGetCond();
    })
}


//画面检索条件初期化
function initSearchParam() {
    //日期控件初期设置
    initSearchDate();

    //	[检索]按钮
    $('#search').button();
    $("#search").click(function () {
        initGetCond();
        checkFinanceUploadDataParam();
    })

    //	[检索]按钮
    $('#update').button();
    $("#update").click(function(){
        initGetCond();
        updateFinanceUploadDataParam();
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

}

// 画面初期显示内容取得
function doPageInitDataReq() {
    // 画面初期值请求
    bigdata.post(rootPath + "/manage/getUserChannelList.html", finance_search_cond, doPageInitDataReq_end, '');
}

//画面初期值取得结束
function doPageInitDataReq_end(json) {
    // 店铺信息获得
    channels = json.cmbChannels;
    // 店铺初期化
    initchannels(channels);
    // 画面检索条件取得
    initGetCond();
    $('#container').trigger("search_page_loaded");
}


// 店铺初期化
//		channels：店铺信息
function initchannels(channels) {
    // 既存项目清空
    $("#search_channel_list").empty();

    for (var i = 0; i < channels.length; i++) {
        // 该渠道店铺信息取得
        var code = channels[i].code;
        var name = channels[i].name;
        $("#search_channel_list").append("<li class='list-group-item'><label class='checkbox inline' style='margin-top: 1px;margin-bottom: 1px;font-weight:500'><input type='checkbox' id='channel_selectall_sub' value='" + code + "'  onclick='setSelectAll(\"channel_selectall\");' checked/>" + name + "</label></li>");
    }
}

// 画面检索条件取得
function initGetCond() {
    // 日期
    finance_search_cond.invoice_num = $("#search_invoice_num").val();
    finance_search_cond.tracking_no = $("#search_tracking_no").val();
    // 运单号
    finance_search_cond.main_waybill_num = $("#search_main_waybill_num").val();
    // 店铺订单编号
    finance_search_cond.pay_in_warrant_num = $("#search_pay_in_warrant_num").val();
    // 系统订单编号
    finance_search_cond.error_type_id = $("#search_error_type_id").val();
    // 错误处理状态
    finance_search_cond.explanation_error_status = $("#search_error_status").val();

    return true;
}

