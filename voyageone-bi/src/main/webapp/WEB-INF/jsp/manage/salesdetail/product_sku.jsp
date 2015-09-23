<!-- define -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
							<div class="panel panel-grey margin-bottom-5 rounded" >
								<div class="panel-heading">
				              	  	<h3 class="panel-title">
				              	  		<div class="row">
				              	  			<div class="col-lg-8" id="product_sku_title"></div>
				              	  			<div class="col-lg-3">
					              	  			<div id="product_sku_radioset" class="ui-buttonset" style="margin-top:-1px;float:right">
									                <input type="radio" id="product_radioset" name="product_sku_radioset" checked="checked" class="ui-helper-hidden-accessible">
									                <label for="product_radioset" class="ui-button ui-widget ui-state-default ui-button-text-only ui-corner-left ui-state-active" role="button" aria-disabled="false" aria-pressed="true">
														<span class="ui-button-text">Product</span>
									                </label>
									                <input type="radio" id="sku_radioset" name="product_sku_radioset" class="ui-helper-hidden-accessible">
									            	<label for="sku_radioset" class="ui-button ui-widget ui-state-default ui-button-text-only" role="button" aria-disabled="false" aria-pressed="false">
									            		<span class="ui-button-text">SKU</span>
									            	</label>
									           </div>
				              	  			</div>
				              	  			<div class="col-lg-1 col-md-pull-0"><span id="product_window_ctrl" class="glyphicon glyphicon-minus" style="float:right;cursor: pointer;"></span></div>
				              	  		</div>            	
				              	  	</h3>
					            </div>
					            <div class="panel-body" id="product_body" style="padding:10px 0px;">
									<div id="sales_product_table">
										<!-- 按Product销售[表格数据] -->
										<table id="grid-results"></table>
			                  			<table id="sales_product_grid_table"></table>
			                  			<div id="sales_product_grid_pager"></div>
			                  		</div>
	                            </div>
							</div>

<!-- 销售模块[产品]js载入 -->
<script charset="UTF-8" type="text/javascript" src="<%=webRootPath%>/resource/js/module/salesdetail/product_sku.js"></script>
