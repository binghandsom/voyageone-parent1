<!-- define -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!-- End define -->
                        <div class="panel panel-grey margin-bottom-5 rounded">
                            <div class="panel-heading">
                                <div><h3 class="panel-title">Channel:</h3></div>
                                <div class="input-group" style="padding-left: 30px"><select id="search_channel_id" class="selectpicker" data-live-search="false" data-size="7"></select></div>
                            </div>
                        </div>
                        <div class="panel panel-grey margin-bottom-5 rounded">
                            <div class="panel-heading">
                                <h3 class="panel-title"><input type="checkbox" id="shop_selectall"  onclick="selectAll('shop_selectall');" checked/> Shops</h3>
                            </div>
                            <div class="mmCustomScrollbar" style="padding: 0px;max-height:250px;">
                                <ul class="list-group text-left text-muted" id="search_shop_list" style="margin-bottom: 0px;">
                                </ul>
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
                                            <label class="control-label">Date:</label>
                                        </div>
                                        <div class="col-sm-9" style="padding-left: 5px;">
                                            <input type="text" value="" id="search_date" class="form-control" readonly>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div class="panel" style="float:right">
	                        <div class="input-group">
								<button id="search" class="ui-button-primary ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only rounded" role="button" aria-disabled="false">
									<span class="ui-button-text">Search</span>
								</button>
	                      	</div>
	                  	</div>
<!-- 检索条件载入 -->
<!-- 	检索条件 -->
<script charset="UTF-8" type="text/javascript" src="<%=webRootPath%>/resource/js/module/saleshome/search.js"></script>
	                  	