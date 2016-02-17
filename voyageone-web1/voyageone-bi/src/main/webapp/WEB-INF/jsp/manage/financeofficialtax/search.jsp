<!-- define -->
<%@ page language="java" pageEncoding="UTF-8" %>
<!-- End define -->
<div class="panel panel-grey margin-bottom-5 rounded">
    <div class="panel-heading">
        <h3 class="panel-title"><input type="checkbox" id="channel_selectall"  onclick="selectAll('channel_selectall');" checked/> channels</h3>
    </div>
    <div class="mmCustomScrollbar" style="padding: 0px;max-height:250px;">
        <ul class="list-group text-left text-muted" id="search_channel_list" style="margin-bottom: 0px;">
        </ul>
    </div>
</div>
<div class="panel panel-grey margin-bottom-5 rounded">
    <div class="panel-heading">
        <h3 class="panel-title"><input type="checkbox" id="port_selectall"  onclick="selectAll('port_selectall');" checked/> ports</h3>
    </div>
    <div class="mmCustomScrollbar" style="padding: 0px;max-height:250px;">
        <ul class="list-group text-left text-muted" id="search_port_list" style="margin-bottom: 0px;">
        </ul>
    </div>
</div>
<div class="panel panel-grey margin-bottom-5 rounded">
    <div class="panel-heading">
        <h3 class="panel-title">Date</h3>
    </div>
    <div class="panel-body" style="padding-top:5px;padding-bottom:1px;">
        <form class="form-horizontal" role="form">
            <div class="form-group">
                <div class="col-sm-3">
                    <label class="control-label">Date From:</label>
                </div>
                <div class="col-sm-9" style="padding-left: 5px;padding-top: 10px">
                    <input type="text" value="" id="search_date_from" class="form-control" readonly>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-3">
                    <label class="control-label">Date To:</label>
                </div>
                <div class="col-sm-9" style="padding-left: 5px;padding-top: 10px">
                    <input type="text" value="" id="search_date_to" class="form-control" readonly>
                </div>
            </div>
        </form>
    </div>
</div>
<div class="panel panel-grey margin-bottom-5 rounded">
    <div class="panel-heading">
        <h3 class="panel-title">Detail Search</h3>
    </div>
    <div class="panel-body" style="padding-top:5px;padding-bottom:1px;">
        <form class="form-horizontal" role="form">
            <div class="form-group">
                <div class="col-sm-12">
                    <label class="control-label">Tracking No.:</label>
                </div>
                <div class="col-sm-12" style="padding-left: 5px;">
                    <input type="text" value="" id="search_tracking_no" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label class="control-label">B/L No.:</label>
                </div>
                <div class="col-sm-12" style="padding-left: 5px;">
                    <input type="text" value="" id="search_main_waybill_num" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label class="control-label">Order No.:</label>
                    <input type="text" value="" id="search_order_number" class="form-control">
                </div>
                <div class="col-sm-12" style="padding-left: 5px;">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label class="control-label">Source Order No.:</label>
                </div>
                <div class="col-sm-12" style="padding-left: 5px;">
                    <input type="text" value="" id="search_source_order_id" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <li class='list-group-item'><label class='checkbox inline' style='margin-top: 1px;margin-bottom: 1px;font-weight:500'><input type='checkbox' id='data_null_sub' checked/>AMT IS NOT NULL</label></li>
            </div>
        </form>
    </div>
</div>

<div class="panel" style="float:right">
    <div class="input-group">
        <button id="search"
                class="ui-button-primary ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only rounded"
                role="button" aria-disabled="false">
            <span class="ui-button-text">Search</span>
        </button>
        &nbsp;
        <button id="export"
                class="ui-button-primary ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only rounded"
                role="button" aria-disabled="false">
            <span class="ui-button-text">Export</span>
        </button>
    </div>
</div>

<!-- 检索条件载入 -->
<!-- 检索条件 -->
<script charset="UTF-8" type="text/javascript" src="<%=webRootPath%>/resource/js/module/financeofficialtax/search.js"></script>
	                  	