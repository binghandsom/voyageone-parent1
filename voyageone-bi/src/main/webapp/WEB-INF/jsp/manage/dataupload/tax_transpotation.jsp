<!-- define -->
<%@ page language="java" pageEncoding="UTF-8" %>
<!-- End define -->
<style type="text/css">
    .ui-jqgrid .ui-jqgrid-bdiv {
        overflow-y: scroll
    }
</style>

<div class="panel panel-default">
    <ul id="myTab" class="nav nav-tabs">
        <li class="active"><a href="#tax" data-toggle="tab">Tax</a></li>
        <li><a href="#transpotation" data-toggle="tab">Transpotation</a></li>
    </ul>
    <div id="myTabContent" class="tab-content">
        <div class="tab-pane fade in active" id="tax" style="width:100%;height:100%">
            <div style="padding-right:5px;overflow:hidden">
                <table id="tax_bill_table"></table>
                <div id="tax_bill_gridPager"></div>
            </div>
        </div>
        <div class="tab-pane fade" id="transpotation">
            <div id="transpotation_kpi_radioset" class="ui-buttonset" style="width:100%;height:100%">
                <div style="padding-right:5px;overflow:hidden">
                    <table id="transpotation_bill_table"></table>
                    <div id="transpotation_bill_gridPager"></div>
                </div>
            </div>
        </div>

        <div class="contextMenu" id="contextMenu" style="display:none; width:400px;">
            <ul style="width: 400px; font-size: 65%;">
                <li id="add">
                    <span class="ui-icon ui-icon-plus" style="float:left"></span>
                    <span style="font-size:130%; font-family:Verdana">Add Row</span>
                </li>
                <li id="edit">
                    <span class="ui-icon ui-icon-pencil" style="float:left"></span>
                    <span style="font-size:130%; font-family:Verdana">Edit Row</span>
                </li>
                <li id="del">
                    <span class="ui-icon ui-icon-trash" style="float:left"></span>
                    <span style="font-size:130%; font-family:Verdana">Delete Row</span>
                </li>
            </ul>
        </div>
    </div>
</div>

<!-- 销售模块[品类][品牌]jjs载入 -->
<script charset="UTF-8" type="text/javascript"
        src="<%=webRootPath%>/resource/js/module/dataupload/tax_transpotation.js"></script>