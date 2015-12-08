<!-- define -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!-- End define -->
		               	<div class="row">
							<div class="col-md-8" style='padding-right:0px'>
							    <div id="time_line"  class="panel panel-default"  style="height:310px">
							    </div>
							</div>
							<div class="col-md-4"  style='padding-left:0px;padding-right:14px;margin-left:-1px'>
							    <ul class="list-group" style="list-style-type:none;">
							    	<span  class="list-group-item"  style="height:40px;text-align:center;background-color:#ECECEC">
							    		<li style="width:30%; float:left;">&nbsp;</li><li style="width:30%; float:left;"><B>30Day</B></li><li style="width:40%; float:left;"><B>YTD</B></li>
							    	</span>
							        <a id="timeline_sum_qty" href="#" class="list-group-item active" style="height:55px;padding-top:20px;" role="timeline_sum">
							        	<li style="width:30%; float:left;">QTY:</li><li id="time_line_sum_qty_mtd" style="width:30%; float:left;text-align:right">0</li><li id="time_line_sum_qty_ytd" style="width:40%; float:left;;text-align:right">0</li>
							        </a>
							        <a id="timeline_sum_amt"  href="#" class="list-group-item" style="height:55px;padding-top:20px;" role="timeline_sum">
							        	<li style="width:30%; float:left;">Amount:</li><li id="time_line_sum_amt_mtd" style="width:30%; float:left;text-align:right">0</li><li id="time_line_sum_amt_ytd" style="width:40%; float:left;text-align:right">0</li>
							        </a>
							        <a id="timeline_sum_uv"  href="#" class="list-group-item" style="height:55px;padding-top:20px;" role="timeline_sum">
							        	<li style="width:30%; float:left;">UV:</li><li id="time_line_sum_uv_mtd" style="width:30%; float:left;text-align:right">0</li><li id="time_line_sum_uv_ytd" style="width:40%; float:left;text-align:right">0</li>
							        </a>
							        <a id="timeline_sum_order"  href="#" class="list-group-item" style="height:55px;padding-top:20px;" role="timeline_sum">
							        	<li style="width:30%; float:left;">Order:</li><li id="time_line_sum_order_mtd" style="width:30%; float:left;text-align:right">0</li><li id="time_line_sum_order_ytd" style="width:40%; float:left;text-align:right">0</li>
							        </a>
							        <a id="timeline_sum_orderprice"  href="#" class="list-group-item" style="height:55px;padding-top:20px;" role="timeline_sum">
							        	<li style="width:30%; float:left;">ATV:</li><li id="time_line_sum_orderprice_mtd" style="width:30%; float:left;text-align:right">0</li><li id="time_line_sum_orderprice_ytd" style="width:40%; float:left;text-align:right">0</li>
							        </a>
							    </ul>
							</div>		               	
		               	</div>
<!-- 销售模块[时间]js载入 -->
<script charset="UTF-8" type="text/javascript" src="<%=webRootPath%>/resource/js/module/saleshome/time_line.js"></script>