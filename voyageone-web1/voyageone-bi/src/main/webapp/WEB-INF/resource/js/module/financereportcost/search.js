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
    $("#search").click(function () {
        initGetCond();
        checkFinanceCostParam();
    })

    //	[检索]按钮
    $('#export').button();
    $("#export").click(function(){
        initGetCond();
        checkFinanceCostParamExport();
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
    var startSearchDate = getDateStr(-30);
    $("#search_date_from").val(startSearchDate);
    var initSearchDate = getDateStr(-1);
    $("#search_date_to").val(initSearchDate);

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
    finance_search_cond.time_start = $("#search_date_from").val();
    finance_search_cond.time_end = $("#search_date_to").val();
    // 运单号
    finance_search_cond.tracking_no = $("#search_tracking_no").val();
    // 店铺订单编号
    finance_search_cond.source_order_id = $("#search_source_order_id").val();
    // 系统订单编号
    finance_search_cond.order_number = $("#search_order_number").val();
    // 品牌订单编号
    finance_search_cond.client_order_number = $("#search_client_order_number").val();

    //
    str_channel_ids = '';
    checkname = 'channel_selectall';
    $("input[type='checkbox'][id='" + checkname + "_sub']:checked").each(function () {
        str_channel_ids = str_channel_ids + "'" + $(this).val() + "',";
    });
    if (str_channel_ids.length == 0) {
        alert("please select channels.");
        return false;
    }

    finance_search_cond.channel_ids = str_channel_ids.substr(0, str_channel_ids.length - 1);

    null_check = '';
    if ($("#data_null_sub").attr('checked') == "checked") {
        null_check = '1';
    }

    finance_search_cond.data_null_check = null_check;

    return true;
}

