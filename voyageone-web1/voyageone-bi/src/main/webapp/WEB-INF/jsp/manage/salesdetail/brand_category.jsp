<!-- define -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!-- End define -->
						<div class="row" style="padding-left:15px;padding-right:15px">
							<div  class="col-md-6"  style="padding-left: 1px; padding-right: 1px;" id="sales_brand_block">
								<div class="panel panel-grey margin-bottom-5 rounded">
									<div class="panel-heading" >	             		
					              	  	<h3 class="panel-title">
					              	  		<div class="row">
					              	  			<div class="col-lg-2">Brand</div>
					              	  			<div class="col-lg-9"></div>
					              	  			<div class="col-lg-1 col-md-pull-0" style="padding-top: 5px;"><span id="brand_window_ctrl" class="glyphicon glyphicon-resize-horizontal" style="float:right;cursor: pointer;"></span></div>
					              	  		</div>            	
					              	  	</h3>
						            </div>
						            <div class="panel-body" id="brand_body" style="padding-top:1px;padding-bottom:5px;">
					              	  	<!-- 按品牌销售[面包屑] -->		            	
						            	<div style="height:20px"><span id="brand_breadcrumb"></span></div>
						            	<!-- 按品牌销售[图形] -->
						            	<div id="sales_brand_chart"  style="height:300px"></div>
				                  	</div>
				                  	<div class="panel-footer" style="padding:5px 0px">
		                                <div class="row">
					                  		<!-- 按品牌销售[控制按钮] -->
		                                	<div style="padding-right:35px"><span id="brand_table_ctl" class="glyphicon glyphicon-list-alt" style="float:right; cursor: pointer;"></span></div>
		                                </div>
										<div id="sales_brand_table" style="display: none;">
											<div class="margin-bottom-5"></div>
											<!-- 按品牌销售[表格数据] -->
				                  			<table id="sales_brand_grid_table"></table>
				                  			<div id="sales_brand_grid_pager"></div>
				                  		</div>
		                            </div>
								</div>
							</div>
							<div  class="col-md-6"  style="padding-left: 1px; padding-right: 1px;" id="sales_category_block">
								<div class="panel panel-grey margin-bottom-5 rounded" >
									<div class="panel-heading">	             		
					              	  	<h3 class="panel-title">
					              	  		<div class="row">
					              	  			<div class="col-lg-2">Category</div>
					              	  			<div class="col-lg-9"></div>
					              	  			<div class="col-lg-1 col-md-pull-0" style="padding-top: 5px;"><span id="category_window_ctrl" class="glyphicon glyphicon-resize-horizontal" style="float:right;cursor: pointer;"></span></div>
					              	  		</div>            	
					              	  	</h3>
						            </div>
						            <div class="panel-body" id="category_body" style="padding-top:1px;padding-bottom:5px;">
										<!-- 按分类销售[面包屑] -->		            	
						            	<div style="height:20px"><span id="category_breadcrumb"></span></div>
						            	<!-- 按分类销售[图形] -->
						            	<div id="sales_category_chart"  style="height:300px"></div>
				                  	</div>
				                  	<div class="panel-footer" style="padding:5px 0px">
		                                <div class="row">
					                  		<!-- 按分类销售[控制按钮] -->
		                                	<div style="padding-right:35px"><span id="category_table_ctl" class="glyphicon glyphicon-list-alt" style="float:right; cursor: pointer;"></span></div>
		                                </div>
										<div id="sales_category_table" style="display: none;">
											<div class="margin-bottom-5"></div>
											<!-- 按分类销售[表格数据] -->
				                  			<table id="sales_category_grid_table"></table>
				                  			<div id="sales_category_grid_pager"></div>
				                  		</div>
		                            </div>
								</div>
							</div>
						</div>

<!-- 销售模块[分类][品牌]js载入 -->
<script charset="UTF-8" type="text/javascript" src="<%=webRootPath%>/resource/js/module/salesdetail/brand.js"></script>
<script charset="UTF-8" type="text/javascript" src="<%=webRootPath%>/resource/js/module/salesdetail/category.js"></script>