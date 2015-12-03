<!-- JS Global Compulsory -->
<script type="text/javascript" src="<%=assetsPath%>/plugins/jquery-1.10.2.min.js"></script>      
<script type="text/javascript" src="<%=assetsPath%>/plugins/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="<%=assetsPath%>/plugins/bootstrap/js/bootstrap.min.js"></script>

<!-- 第三方lib导入 -->
<!-- 	百度echarts载入 -->
<script type="text/javascript" src="<%=webRootPath%>/resource/lib/echarts/build/dist/echarts-all.js"></script>
<script type="text/javascript" src="<%=webRootPath%>/resource/lib/echarts/build/dist/theme/macarons.js"></script>

<!-- 	jqGrid载入 -->
<%--<script type="text/javascript" src="<%=webRootPath%>/resource/lib/jqGrid/js/jquery-ui-custom.min.js"></script> --%>
<script type="text/javascript" src="<%=webRootPath%>/resource/lib/jquery-ui/assets/js/jquery-ui-1.10.0.custom.min.js"></script>
<script type="text/javascript" src="<%=webRootPath%>/resource/lib/jqGrid/js/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="<%=webRootPath%>/resource/lib/jqGrid/js/i18n/grid.locale-en.js"></script>
<script type="text/javascript" src="<%=webRootPath%>/resource/lib/jqGrid/plugins/jquery.contextmenu.js"></script>

<!-- 	datetimePicker载入 -->
<script type="text/javascript" src="<%=webRootPath%>/resource/lib/datetimepicker/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="<%=webRootPath%>/resource/lib/datetimepicker/js/locales/bootstrap-datetimepicker.fr.js"></script>
<!-- mCustomScrollbar 导入 -->
<script type="text/javascript" src="<%=webRootPath%>/resource/lib/malihu-custom-scrollbar-plugin-master/jquery.mCustomScrollbar.concat.min.js"></script>
<!-- bootstrap-select 导入 -->
<script type="text/javascript" src="<%=webRootPath%>/resource/lib/bootstrap-select/dist/js/bootstrap-select.js"></script>
<%-- <script type="text/javascript" src="<%=webRootPath%>/resource/lib/jquery-ui/assets/js/bootstrap.min.js"></script> --%>
<script type="text/javascript" src="<%=webRootPath%>/resource/lib/jquery-download/jquery.fileDownload.js"></script>

<script type="text/javascript" >
    (function($){
        $(window).load(function(){
            $(".mmCustomScrollbar").mCustomScrollbar({
			    theme:"minimal-dark",
			    scrollInertia:0,
			    mouseWheel:{
					scrollAmount:40,
				}
			});
        }); 
    })(jQuery);
</script>

<!-- 共通函数载入 -->
<script type="text/javascript" src="<%=webRootPath%>/resource/js/common/common.js"></script>
<!-- 自定义组件载入 -->
<script type="text/javascript" src="<%=webRootPath%>/resource/js/common/components.js"></script>
<!-- echarts共通变数载入 -->
<script charset="UTF-8" type="text/javascript" src="<%=webRootPath%>/resource/js/common/echarts_common.js"></script>

<!-- JS Implementing Plugins -->           



<!-- JS Page Level -->           
<script type="text/javascript" src="<%=assetsPath%>/js/app.js"></script>
<script type="text/javascript">
    jQuery(document).ready(function() {
        App.init();


    });
</script>
<script type="text/javascript">

        var menunum = '<%=request.getParameter("menuno")%>';
        if (menunum != "null"){
	        var menu0 = menunum.substr(0,1);
	        var menu1 = "#menu" + menunum.substr(0,1) + "00";
	        var menu2 = menu1 + "_collapse";
	        var menu3 = "#menu" + menunum;
	        //change css
	        $(menu1).attr("class","list-group-item list-toggle active");
	        $(menu2).attr("class","collapse in");
	        $(menu3).attr("class","active");
        }

		var rootPath = "<%=webRootPath%>";
</script>
<!--[if lt IE 9]>
    <script src="<%=assetsPath%>/plugins/respond.js"></script>
<![endif]-->