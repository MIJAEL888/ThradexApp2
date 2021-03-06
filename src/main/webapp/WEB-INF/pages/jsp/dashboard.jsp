<%@ include file="/WEB-INF/pages/jsp/includes.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<jsp:include page="head.jsp"/>
</head>
<body class="page-header-fixed page-sidebar-fixed">
<div>
    <div class="page-wrapper">
    	<!--BEGIN HEADER-->
       	<jsp:include page="sis/header.jsp"/>
        <!--END HEADER-->
         
        <!--END HORIZONTAL SIDEBAR--><!--BEGIN WRAPPER-->
        <div class="wrapper row-offcanvas row-offcanvas-left">
        	<!--BEGIN SIDEBAR-->
           <jsp:include page="sis/menuSideBar.jsp"></jsp:include>
            <!--END SIDERBAR-->
            
            <!--BEGIN CONTENT-->
            <div class="content">
                <section class="content-header">
                    <h1 class="pull-left">Welcome to AppManager 
<!--                         <small>Header small text goes here...</small> -->
                    </h1>
                    <div class="pull-right">
                        <ol class="breadcrumb">
                            <li class="active">Home</li>
                        </ol>
                    </div>
                </section>
                <section class="main-content"></section>
            </div>
            <!--END CONTENT-->
        </div>
        <!--END WRAPPER-->
        <!--BEGIN PAGE QUICK SIDEBAR-->
<!--         <div class="page-quick-sidebar-wrapper"> -->
<!--             <div class="page-quick-sidebar-content"> -->
<!--                 <div id="admin-setting"> -->
<!--                     <div class="admin-setting-content"> -->
<!--                         <ul class="admin-setting-list list-unstyled"> -->
<!--                             <li><h4>Theme Colors</h4> -->
<!--                                 <ul class="option theme-option list-unstyled list-inline"> -->
<!--                                     <li data-color="default" data-hover="tooltip" title="Dark" class="dark active"></li> -->
<!--                                     <li data-color="blue" data-hover="tooltip" title="Blue" class="blue"></li> -->
<!--                                     <li data-color="purple" data-hover="tooltip" title="Purple" class="purple"></li> -->
<!--                                     <li data-color="brown" data-hover="tooltip" title="Brown" class="brown"></li> -->
<!--                                     <li data-color="light" data-hover="tooltip" title="Light" class="light"></li> -->
<!--                                 </ul> -->
<!--                             </li> -->
<!--                             <li><h4>Layout</h4><select class="option layout-option form-control"> -->
<!--                                 <option value="fluid">Fluid</option> -->
<!--                                 <option value="boxed">Boxed</option> -->
<!--                             </select></li> -->
<!--                             <li><h4>Header</h4><select class="option header-option form-control"> -->
<!--                                 <option value="fixed">Fixed</option> -->
<!--                                 <option value="static">Static</option> -->
<!--                             </select></li> -->
<!--                             <li><h4>Sidebar</h4><select class="option sidebar-option form-control"> -->
<!--                                 <option value="fixed" selected="selected">Fixed</option> -->
<!--                                 <option value="static">Static</option> -->
<!--                             </select></li> -->
<!--                             <li><h4>Sidebar Position</h4><select class="option sidebar-position-option form-control"> -->
<!--                                 <option value="fixed">Left</option> -->
<!--                                 <option value="static">Right</option> -->
<!--                             </select></li> -->
<!--                         </ul> -->
<!--                     </div> -->
<!--                 </div> -->
<!--             </div> -->
<!--         </div> -->
        <!--END PAGE QUICK SIDEBAR-->
        </div>
</div>
	<jsp:include page="scripts.jsp"/>
</body>
</html>