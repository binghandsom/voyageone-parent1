<!-- define -->
<%@ page language="java" pageEncoding="UTF-8" %>
<!-- End define -->
<div class="panel panel-grey margin-bottom-5 rounded">
    <div class="panel-heading">
        <h3 class="panel-title">Invoice Num</h3>
    </div>
    <div class="panel-body" style="padding-top:5px;padding-bottom:1px;">
        <form class="form-horizontal" role="form">
            <div class="form-group">
                <div class="col-sm-12">
                    <label class="control-label">Invoice Num:</label>
                </div>
                <div class="col-sm-12" style="padding-left: 5px;">
                    <input type="text" value="" id="search_invoice_num" class="form-control">
                </div>
            </div>
        </form>
    </div>
</div>
<div class="panel panel-grey margin-bottom-5 rounded">
    <div class="panel-heading">
        <h3 class="panel-title">File Name</h3>
    </div>
    <div class="panel-body" style="padding-top:5px;padding-bottom:1px;">
        <form class="form-horizontal" role="form">
            <div class="form-group">
                <div class="col-sm-12">
                    <label class="control-label">File Name:</label>
                </div>
                <div class="col-sm-12" style="padding-left: 5px;">
                    <input type="text" value="" id="search_file_name" class="form-control">
                </div>
            </div>
        </form>
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
<script charset="UTF-8" type="text/javascript" src="<%=webRootPath%>/resource/js/module/dataupload/search.js"></script>
	                  	