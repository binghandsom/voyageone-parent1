<!-- define -->
<%@ page language="java" pageEncoding="UTF-8" %>
<!-- End define -->
<div class="panel panel-grey margin-bottom-5 rounded">
    <div class="panel-heading">
        <h3 class="panel-title">Detail Search</h3>
    </div>
    <div class="panel-body" style="padding-top:5px;padding-bottom:1px;">
        <form class="form-horizontal" role="form">
            <div class="form-group">
                <div class="col-sm-12">
                    <label class="control-label">Invoice No.:</label>
                </div>
                <div class="col-sm-12" style="padding-left: 5px;">
                    <input type="text" value="" id="search_invoice_num" class="form-control">
                </div>
            </div>
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
                    <label class="control-label">Main Waybill No.:</label>
                </div>
                <div class="col-sm-12" style="padding-left: 5px;">
                    <input type="text" value="" id="search_main_waybill_num" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label class="control-label">Bill ID:</label>
                </div>
                <div class="col-sm-12" style="padding-left: 5px;">
                    <input type="text" value="" id="search_pay_in_warrant_num" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label class="control-label">Error Type:</label>
                </div>
                <div class="col-sm-12" style="padding-left: 5px;">
                    <input type="text" value="" id="search_error_type_id" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label class="control-label">Error Status:</label>
                </div>
                <div class="col-sm-12" style="padding-left: 5px;">
                    <input type="text" value="" id="search_error_status" class="form-control">
                </div>
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
        <button id="update"
                class="ui-button-primary ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only rounded"
                role="button" aria-disabled="false">
            <span class="ui-button-text">Update</span>
        </button>
    </div>
</div>
<!-- 检索条件载入 -->
<!-- 检索条件 -->
<script charset="UTF-8" type="text/javascript" src="<%=webRootPath%>/resource/js/module/financedatacheck/search.js"></script>
	                  	