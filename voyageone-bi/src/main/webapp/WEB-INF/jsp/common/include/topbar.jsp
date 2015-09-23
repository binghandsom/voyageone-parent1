
<div class="topbar">
	<div class="container">
		<a class="navbar-brand" href="#"> <img id="logo-header"
			src="<%=webRootPath%>/resource/images/voyageone_logo1.png" alt="Logo">
		</a>
		<!-- Topbar Navigation -->
		<ul class="nav navbar-nav loginbar pull-right">
			<li style="padding-bottom:0px"><span class="color-blue"><%=dis_name%></span></li>
			<li class="topbar-devider" style="padding-bottom:0px">&nbsp;</li>
	        <li style="padding-bottom:0px" class="dropdown" >
	            <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" style="padding:0px 2px;" >
					<i class="fa fa-user fa-fw"></i>
					<i class="fa fa-caret-down"></i>
	            </a>
	            <ul class="dropdown-menu" style="border-top:solid 2px #949494;min-width:160px;">
					<li><a href="<%=webRootPath%>/manage/changePassword.html"><i class="fa fa-user fa-fw"></i> Change Passowrd</a></li>
					<li><a href="<%=webRootPath%>/manage/logout.html"><i class="fa fa-sign-out fa-fw"></i>Logout</a></li>
	            </ul>
	        </li>
		</ul>
		<!-- End Topbar Navigation -->

	</div>
</div>