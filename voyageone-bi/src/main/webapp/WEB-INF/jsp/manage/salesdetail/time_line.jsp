<!-- define -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!-- End define -->
    <style type="text/css">
        .ui-th-column-header {  text-align: center; }
        .panel-heading {padding:1px 15px;}
    </style>
		               	<div class="row" style="padding-left:15px;padding-right:15px">
			               	<div class="panel panel-grey margin-bottom-5 rounded" id="sales_time_block">
			               		<div class="panel-heading">	             		
				              	  	<h3 class="panel-title">
				              	  		<div class="row">
				              	  			<div class="col-lg-2">Time Line</div>
				              	  			<div class="col-lg-9" style="padding:0px">
												<div class="navbar-collapse collapse" style="padding:0px">
											        <div class="navbar-right">
											            <div class="btn-group">
											                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" style="margin-top:-5px;margin-bottom:-2px;background-color:#E8E8E8">KPI<span class="caret"></span></button>
											                <div class="dropdown-menu" style="color:#999;width:260px">
											                        <div class="col-lg-12" style="padding-top:10px">Column Chart:</div>
											                        <div class="col-lg-12" id="timeline_kpi_set">
											                               <label class="checkbox-inline">
																		      <input type="radio" name="timeline_kpi_radio" id="timeline_kpi_qty" value="qty" checked>QTY
																		   </label>
																		   <label class="checkbox-inline">
																		      <input type="radio" name="timeline_kpi_radio" id="timeline_kpi_amt" value="amt">AMT
																		   </label>
																		   <label class="checkbox-inline">
																		      <input type="radio" name="timeline_kpi_radio" id="timeline_kpi_order" value="order">Order
																		   </label>
											                        </div>
											                        <div class="col-lg-12" style="padding-top:10px">Line Chart:</div>
											                        <div class="col-lg-12" id="timeline_linekpi_set">
											                               <label class="checkbox-inline">
																		      <input type="radio" name="timeline_line_kpi_radio" id="timeline_linekpi_yoy" value="qty" checked>YOY
																		   </label>
																		   <label class="checkbox-inline">
																		      <input type="radio" name="timeline_line_kpi_radio" id="timeline_linekpi_mom" value="amt">MOM
																		   </label>
											                        </div>
											                        <div class="col-sm-12" style="text-align: right;padding-right:20px; padding-top:10px; padding-bottom:10px;">
											                            <button id="timeline_kpi_btn" class="btn btn-success btn-sm" style="padding-top:4px;padding-bottom:4px;padding-left:20px;padding-right:20px">OK</button>
											                        </div>
											                </div>
											            </div>
											        </div>
											    </div>
									        </div>
				              	  			<div class="col-lg-1 col-md-pull-0" style="padding-top: 5px;"><span id="time_window_ctrl" class="glyphicon glyphicon-minus" style="float:right; cursor: pointer;"></span></div>
				              	  		</div>            	
				              	  	</h3>
					            </div>
					            <div class="panel-body" id="time_body" style="padding-top:1px;padding-bottom:5px;">
				              	  	<!-- 按时间销售[面包屑] -->	
					            	<div style="height:20px"><span id="timeline_breadcrumb"></span></div>
					            	<!-- 按时间销售[图形] -->
					            	<div class="row">
						            	<div  class="col-md-9"  style="padding-left: 1px; padding-right: 1px;">
						            		<div id="sales_time_chart_qty"     style="height:210px"></div>
						            		<div id="sales_time_chart_order" style="height:130px"></div>
						            		<div id="sales_time_chart_uv" style="height:130px"></div>
						            	</div>
						            	 <div class="col-md-3" style="padding-left: 1px; padding-right: 1px;">
						            	 	<div id="sales_time_chart_qty_sum"     style="height:210px"></div>
						            		<div id="sales_time_chart_pie_pcMobel" style="height:130px;"></div>
						            		<div id="sales_time_chart_pie_shops" style="height:130px"></div>
						            	 </div>
					            	</div>
			                  	</div>
			                  	<div class="panel-footer" style="padding:5px 0px">
	                                <div class="row">
	                                	<!-- 按时间销售[控制按钮] -->
	                                	<div style="padding-right:35px"><span id="time_line_table_ctl" class="glyphicon glyphicon-list-alt" style="float:right; cursor: pointer;"></span></div>
	                                </div>
									<div id="sales_time_table" style="display: none;">
										<div class="margin-bottom-5"></div>
										<!-- 按时间销售[表格数据] -->
			                  			<table id="sales_time_grid_table"></table>
			                  			<div id="sales_time_grid_pager"></div>
			                  		</div>
	                            </div>
							</div>
		               	</div>
<!-- 销售模块[时间]js载入 -->
<script charset="UTF-8" type="text/javascript" src="<%=webRootPath%>/resource/js/module/salesdetail/time_line.js"></script>