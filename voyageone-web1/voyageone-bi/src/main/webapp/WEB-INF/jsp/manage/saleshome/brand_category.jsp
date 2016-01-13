<!-- define -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!-- End define -->
    <style type="text/css">
    	.ui-jqgrid .ui-jqgrid-bdiv { overflow-y: scroll }
        .ui-th-column-header {  text-align: center; }
    </style>

		               	<div class="panel panel-default">
			               	<ul id="myTab" class="nav nav-tabs">
							   <li class="active"><a href="#product" data-toggle="tab">product</a></li>
							   <li><a href="#brand" data-toggle="tab">brand</a></li>
							   <li><a href="#category" data-toggle="tab">category</a></li>
							</ul>
							<div id="myTabContent" class="tab-content">
								<div class="tab-pane fade in active" id="product" style="width:100%;height:100%">
							   		<div class="margin-bottom-5 panel-default" style="text-align:center">Top 10 Product List</div>
							   		<div style="padding-right:5px;overflow:hidden">
								   		<table id="product_table"></table>
							   		</div>
							   </div>
							   <div class="tab-pane fade" id="brand" >
							      <div id="brand_kpi_radioset" class="ui-buttonset" style="padding-left:320px;padding-top:5px;">
					                <input type="radio" id="brand_qty" name="brand_radio" checked="checked" class="ui-helper-hidden-accessible">
					                <label for="brand_qty" class="ui-button ui-widget ui-state-default ui-button-text-only ui-corner-left ui-state-active" role="button" aria-disabled="false" aria-pressed="true">
										<span class="ui-button-text">QTY</span>
					                </label>
					                <input type="radio" id="brand_amt" name="brand_radio" class="ui-helper-hidden-accessible">
					            	<label for="brand_amt" class="ui-button ui-widget ui-state-default ui-button-text-only" role="button" aria-disabled="false" aria-pressed="false">
					            		<span class="ui-button-text">AMT</span>
					            	</label>
					                <input type="radio" id="brand_order" name="brand_radio" class="ui-helper-hidden-accessible">
					                <label for="brand_order" class="ui-button ui-widget ui-state-default ui-button-text-only ui-corner-right" role="button" aria-disabled="false" aria-pressed="false">
					                	<span class="ui-button-text">Order</span>
					                </label>
						           </div>
								   <div class="row">
								      	<div id="brand_bar_chart"   class="col-md-6" style="width:490px;height:350px"></div>
								      	<div id="brand_pop_chart"  class="col-md-6" style="width:490px;height:350px"></div>
								   </div>
							   </div>
							   <div class="tab-pane fade" id="category">
							   		<div id="category_kpi_radioset" class="ui-buttonset" style="padding-left:320px;padding-top:5px;">
						                <input type="radio" id="category_qty" name="category_radio" checked="checked" class="ui-helper-hidden-accessible">
						                <label for="category_qty" class="ui-button ui-widget ui-state-default ui-button-text-only ui-corner-left ui-state-active" role="button" aria-disabled="false" aria-pressed="true">
											<span class="ui-button-text">QTY</span>
						                </label>
						                <input type="radio" id="category_amt" name="category_radio" class="ui-helper-hidden-accessible">
						            	<label for="category_amt" class="ui-button ui-widget ui-state-default ui-button-text-only" role="button" aria-disabled="false" aria-pressed="false">
						            		<span class="ui-button-text">AMT</span>
						            	</label>
						                <input type="radio" id="category_order" name="category_radio" class="ui-helper-hidden-accessible">
						                <label for="category_order" class="ui-button ui-widget ui-state-default ui-button-text-only ui-corner-right" role="button" aria-disabled="false" aria-pressed="false">
						                	<span class="ui-button-text">Order</span>
						                </label>
						           </div>
								   <div class="row">
								      	<div id="category_bar_chart"   class="col-md-6" style="width:490px;height:350px"></div>
								      	<div id="category_pop_chart"  class="col-md-6" style="width:490px;height:350px"></div>
								   </div>
							   </div>
							</div>
						</div>

<!-- 销售模块[品类][品牌]jjs载入 -->
<script charset="UTF-8" type="text/javascript" src="<%=webRootPath%>/resource/js/module/saleshome/brand_category.js"></script>