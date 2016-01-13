        <div class="navbar navbar-default" role="navigation">
            <div class="container ">
                <!-- Brand and toggle get grouped for better mobile display -->
                <!-- <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-responsive-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="fa fa-bars"></span>
                    </button>
                </div> -->
                <div class="row">
				<div class="col-md-4"><h2 id="pageName" class="heading-sm" style="margin-top:15px;margin-bottom:-15px"></h2></div>
                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="col-md-8">
                <div class="collapse navbar-collapse navbar-responsive-collapse">
                    <ul class="nav navbar-nav">
                        <%
                        String user_menu_reportl_size = "";
                        try{
                        	Iterator<Entry<String, List<UserMenuBean>>> iterator = userMenuMap.entrySet().iterator();
 	                		while(iterator.hasNext()) {
 	                			Entry<String, List<UserMenuBean>> entry = iterator.next();
 	                			String pMenuName = entry.getKey();
 	                			out.print("<li class='dropdown active'>");
 	                			out.print("<a href='javascript:void(0);' class='dropdown-toggle' data-toggle='dropdown'>");
 	                			out.print(pMenuName);
 	                			out.print("</a>");
 	                			out.print("<ul class='dropdown-menu'>");
 	                			List<UserMenuBean> menuList = entry.getValue();
 	                			for(UserMenuBean bean:menuList) {
 	                				String code = bean.getCode();
 	                				String name = bean.getName();
 	                				String link = bean.getLink();
 	                				String url = webRootPath + link;
 		                			out.print("<li id=menu" + code + "'><a href='" + url + "'>" + name + "</a></li>");
 		                			if (bean.isSelect()) {
 		                				user_menu_reportl_size = bean.getReportSize();
 		                			}
 	                			}
 	                       		out.print("</ul>");
 	                       		out.print("</li>");
 	                		}

                		}catch(Exception e){
                			System.out.println("Get menu info error!");
                			e.printStackTrace();
                		}
						%>
                    </ul>
               </div>
               </div>
               </div>                
            </div>
        </div>