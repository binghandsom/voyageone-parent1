<!-- define -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!-- End define -->
						<div class="row" style="padding-left:15px;padding-right:15px">
							<div  class="col-md-6"  style="padding-left: 1px; padding-right: 1px;" id="sales_color_block">
								<div class="panel panel-grey margin-bottom-5 rounded">
									<div class="panel-heading">
					              	  	<h3 class="panel-title">
					              	  		<div class="row">
					              	  			<div class="col-lg-2">Color</div>
					              	  			<div class="col-lg-9"></div>
					              	  			<div class="col-lg-1 col-md-pull-0" style="padding-top: 5px;"><span id="color_window_ctrl" class="glyphicon glyphicon-resize-horizontal" style="float:right;cursor: pointer;"></span></div>
					              	  		</div>            	
					              	  	</h3>
						            </div>
						            <div class="panel-body" id="color_body" style="padding-top:1px;padding-bottom:5px;">
					              	  	<!-- 按Color销售[面包屑] -->		            	
						            	<div style="height:20px"><span id="color_breadcrumb"></span></div>
						            	<!-- 按Color销售[图形] -->
						            	<div id="sales_color_chart"  style="height:300px"></div>
				                  	</div>
				                  	<div class="panel-footer" style="padding:5px 0px">
		                                <div class="row">
					                  		<!-- 按Color销售[控制按钮] -->
		                                	<div style="padding-right:35px"><span id="color_table_ctl" class="glyphicon glyphicon-list-alt" style="float:right; cursor: pointer;"></span></div>
		                                </div>
										<div id="sales_color_table" style="display: none;">
											<div class="margin-bottom-5"></div>
											<!-- 按Color销售[表格数据] -->
				                  			<table id="sales_color_grid_table"></table>
				                  			<div id="sales_color_grid_pager"></div>
				                  		</div>
		                            </div>
								</div>
							</div>
							<div  class="col-md-6"  style="padding-left: 1px; padding-right: 1px;" id="sales_size_block">
								<div class="panel panel-grey margin-bottom-5 rounded" >
									<div class="panel-heading" >	             		
					              	  	<h3 class="panel-title">
					              	  		<div class="row">
					              	  			<div class="col-lg-2">Size</div>
					              	  			<div class="col-lg-9"></div>
					              	  			<div class="col-lg-1 col-md-pull-0" style="padding-top: 5px;"><span id="size_window_ctrl" class="glyphicon glyphicon-resize-horizontal" style="float:right;cursor: pointer;"></span></div>
					              	  		</div>            	
					              	  	</h3>
						            </div>
						            <div class="panel-body" id="size_body" style="padding-top:1px;padding-bottom:5px;">
										<!-- 按Size销售[面包屑] -->		            	
						            	<div style="height:20px"><span id="size_breadcrumb"></span></div>
						            	<!-- 按Size销售[图形] -->
						            	<div id="sales_size_chart"  style="height:300px"></div>
				                  	</div>
				                  	<div class="panel-footer" style="padding:5px 0px">
		                                <div class="row">
					                  		<!-- 按Size销售[控制按钮] -->
		                                	<div style="padding-right:35px"><span id="size_table_ctl" class="glyphicon glyphicon-list-alt" style="float:right; cursor: pointer;"></span></div>
		                                </div>
										<div id="sales_size_table" style="display: none;">
											<div class="margin-bottom-5"></div>
											<!-- 按Size销售[表格数据] -->
				                  			<table id="sales_size_grid_table"></table>
				                  			<div id="sales_size_grid_pager"></div>
				                  		</div>
		                            </div>
								</div>
							</div>
						</div>

<!-- 销售模块[Size][Color]js载入 -->
<script charset="UTF-8" type="text/javascript" src="<%=webRootPath%>/resource/js/module/salesdetail/color.js"></script>
<script charset="UTF-8" type="text/javascript" src="<%=webRootPath%>/resource/js/module/salesdetail/size.js"></script>