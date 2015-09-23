<!-- define -->
<%@ include  file="/WEB-INF/jsp/common/include/define_express.jsp"%>
<!-- End define -->
<head>
    <title>Unify | Error</title>

    <!-- commoncss -->
    <%@ include  file="/WEB-INF/jsp/common/include/commoncss.jsp"%>
    <!-- End commoncss -->

    <!-- please add css start -->
    <!-- CSS Page Style -->    
    <link rel="stylesheet" href="<%=assetsPath%>/css/pages/page_404_error.css">
    <!-- please add css end -->


</head> 

<body>
     

<div class="wrapper">
    <!--=== Header ===-->    
    <div class="header">
        <!-- Topbar -->
        <%@ include  file="/WEB-INF/jsp/common/include/topbar_out.jsp"%>
        <!-- End Topbar -->        
        

    </div>
    <!--=== End Header ===-->    



    <!--=== Content Part ===-->
    <div class="container content">		
        <!--Error Block-->
        <div class="row">
            <div class="col-md-8 col-md-offset-2">
                <div class="error-v1">
                    <span class="error-v1-title">500</span>
                    <span>That's an error!</span>
                    <p>Something is wrong here. Please contack Administrator.</p>
                    <a class="btn-u btn-bordered" href="<%=webRootPath%>/manage/login.html">Back Home</a>
                </div>
            </div>
        </div>
        <!--End Error Block-->
    </div>	
    <!--=== End Content Part ===-->

    <!--=== Footer ===-->
    <%@ include  file="/WEB-INF/jsp/common/include/footer.jsp"%>
    <!--=== End Footer ===-->

    <!--=== Copyright ===-->
    <%@ include  file="/WEB-INF/jsp/common/include/copyright.jsp"%>
    <!--=== End Copyright ===-->
</div><!--/wrapper-->

<%@ include  file="/WEB-INF/jsp/common/include/commonjs.jsp"%>

</body>
</html> 