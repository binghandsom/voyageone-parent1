<!-- define -->
<%@ page language="java" pageEncoding="UTF-8" %>
<!-- End define -->
<style type="text/css">
    .ui-jqgrid .ui-jqgrid-bdiv {
        overflow-y: scroll
    }
</style>

<div class="panel panel-default">
    <div id="myTabContent" class="tab-content">
        <div class="tab-pane fade in active" id="tax" style="width:100%;height:100%">
            <div style="padding-right:5px;overflow:hidden">
                <table id="official_tax_report_table"></table>
                <div id="tax_report_gridPager"></div>
            </div>
        </div>
    </div>
</div>

<!-- 销售模块[品类][品牌]jjs载入 -->
<script charset="UTF-8" type="text/javascript"
        src="<%=webRootPath%>/resource/js/module/financeofficialtax/officialTax.js"></script>