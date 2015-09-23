<!-- define -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 <style type="text/css">
    .bootstrap-select .dropdown-toggle {
	   width:130px;
	   padding-right: 15px;
	}
	.bootstrap-select.btn-group .dropdown-menu {
		max-width:150px;
	}
	
	.panel-heading a { 
		color:inherit;
	}
	
	
</style>
<!-- End define -->
 						<div class="panel-group" id="accordion" style="margin-bottom:10px">
					        <div class="panel panel-grey" style="border-color:#8D8D8D;">
					            <div class="panel-heading row">
				              	  			<div class="col-lg-1" style="padding-top:3px"><input type="checkbox" id="shop_selectall"  onclick="selectAll('shop_selectall');" checked/></div>
				              	  			<div class="col-lg-9" style="padding-left:6px"><a data-toggle="collapse" data-parent="#accordion" href="#collapseOne"><h3 class="panel-title">Shops</h3></a></div>
					            </div>
					            <div id="collapseOne" class="panel-collapse collapse in">
				                    <div class="panel-body mmCustomScrollbar" style="padding: 1px;height:250px;">
		                                <ul class="list-group text-left text-muted" id="search_shop_list" style="margin-bottom: 5px;">
		                                </ul>
		                            </div>
					            </div>
					        </div>
					        <div class="panel panel-grey" style="margin-top:0px">
					            <div class="panel-heading">
					                <a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">
				                    	<h3 class="panel-title">Property</h3>
				                    </a>
					            </div>
					            <div id="collapseTwo" class="panel-collapse collapse">
					                <div class="panel-body" style="padding:5px 5px;height:320px;">
										<div class="input-group" style="padding-bottom:5px;">
											<span class="input-group-addon" ><span class="glyphicon glyphicon-th-list"></span></span>
											<select id="search_category_id" class="selectpicker" data-live-search="true" title="Category"  data-size="7" multiple style="">
													<optgroup id="search_category_l1" label="LEVEL1"></optgroup>
													<optgroup id="search_category_l2" label="LEVEL2"></optgroup>
													<optgroup id="search_category_l3" label="LEVEL3"></optgroup>
											</select>
				                            <button id="search_category_clean"  type="button" class="btn-link" style="padding-top:6px;padding-left:1px">
				                           		<span class="glyphicon glyphicon-remove-circle"></span>
				                           	</button>
				                       	</div>
										<div class="input-group" style="padding-bottom:5px;">
				                            <span class="input-group-addon" ><span class="glyphicon glyphicon-th-large"></span></span>
				                            <select id="search_brand_id" class="selectpicker" data-live-search="true" title="Brand"  data-size="7" multiple></select>
				                            <button id="search_brand_clean"  type="button" class="btn-link" style="padding-top:6px;padding-left:1px">
				                           		<span class="glyphicon glyphicon-remove-circle"></span>
				                           	</button>
				                       	</div>
				                       	<div class="input-group" style="padding-bottom:5px;">
				                            <span class="input-group-addon" ><span class="glyphicon glyphicon-adjust"></span></span>
				                            <select id="search_color_id" class="selectpicker" data-live-search="true" title="Color"  data-size="6" multiple></select>
				                            <button id="search_color_clean"  type="button" class="btn-link" style="padding-top:6px;padding-left:1px">
				                           		<span class="glyphicon glyphicon-remove-circle"></span>
				                           	</button>
				                       	</div>
				                       	<div class="input-group" style="padding-bottom:5px;">
				                            <span class="input-group-addon" ><span class="glyphicon glyphicon-indent-right"></span></span>
				                            <select id="search_size_id" class="selectpicker" data-live-search="true" title="Size"  data-size="4" multiple></select>
				                            <button id="search_size_clean"  type="button" class="btn-link" style="padding-top:6px;padding-left:1px">
				                           		<span class="glyphicon glyphicon-remove-circle"></span>
				                           	</button>
				                       	</div>
				                       	<div class="input-group" style="padding-bottom:10px;margin-right: -10px;">
				                            <span class="input-group-addon" ><span class="glyphicon glyphicon glyphicon-barcode"></span></span>
				                            <input id="search_prod_id"  style="width:83%;vertical-align:middle;margin-top:-6px;height:30px" title="product code"/>
				                            <!-- <select id="search_prod_id" class="selectpicker" data-live-search="true" title="Product"  data-size="5" multiple></select> -->
				                            <button id="search_prod_clean"  type="button" class="btn-link" style="padding-top:6px;padding-left:1px">
				                           		<span class="glyphicon glyphicon-remove-circle"></span>
				                           	</button>
				                       	</div>
					                </div>
					            </div>
					        </div>
					    </div>                        
                        
                        <div class="panel panel-grey margin-bottom-5 rounded">
                            <div class="panel-heading">
                                <h3 class="panel-title">Date</h3>
                            </div>
                            <div class="panel-body" style="padding-top:5px;padding-bottom:0px;">
	                            <div>
		                            <select id="search_data_type" class="selectpicker show-tick form-control">
								          <option>Day</option>
								          <option>Month</option>
								          <option>Year</option>
								          <optgroup label="Fix"  data-subtext="">
								            <option>Yesterday</option>
								            <option>Last 7 Days</option>
								            <option selected>Last 30 days</option>
								          </optgroup>
									</select>
								</div>
	                            <div class="input-group" style="padding-top:5px;padding-bottom:0px;">
		                           <span id="search_date_start_span" class="input-group-addon" style="width:65px">Start</span>
									<input type="text" value="" id="search_date_start" class="form-control" readonly>		                           
		                       	</div>
	                            <div class="input-group" style="padding-top:0px;padding-bottom:5px;">
		                           <span id="search_date_end_span"  class="input-group-addon" style="width:65px">End</span>
									<input type="text" value="" id="search_date_end" class="form-control" readonly>		                           
		                       	</div>
                            </div>
                        </div>
                        
                       
                        <div class="panel margin-bottom-5" style="float:right">
	                        <div class="input-group">
								<button id="search_btn" class="ui-button-primary ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only rounded" role="button" aria-disabled="false">
									<span class="ui-button-text">Search</span>
								</button>
	                      	</div>
	                  	</div>
<!-- 检索条件载入 -->
<!-- 	检索条件 -->
<script charset="UTF-8" type="text/javascript" src="<%=webRootPath%>/resource/js/module/salesdetail/search.js"></script>
