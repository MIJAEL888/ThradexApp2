<%@ include file="/WEB-INF/pages/jsp/includes.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<jsp:include page="../head.jsp"/>
	
</head>
<body class="page-header-fixed page-sidebar-fixed">
<div>
    <div class="page-wrapper">
    	<!--BEGIN HEADER-->
       	<jsp:include page="../../sis/header.jsp"/>
        <!--END HEADER-->
         
        <!--END HORIZONTAL SIDEBAR--><!--BEGIN WRAPPER-->
        <div class="wrapper row-offcanvas row-offcanvas-left">
        	<!--BEGIN SIDEBAR-->
           <jsp:include page="../../sis/menuSideBar.jsp"></jsp:include>
            <!--END SIDERBAR-->
            
            <!--BEGIN CONTENT-->
            <div class="content">
<!--                 <section class="content-header"> -->
<!--                     <h1 class="pull-left">Sift Registration  -->
<!--                         <small>Shift Reg...</small> -->
<!--                     </h1> -->
<!--                     <div class="pull-right"> -->
<!--                         <ol class="breadcrumb"> -->
<!--                             <li><a href="#">Rrhh</a></li> -->
<!--                             <li class="active">Shift </li> -->
<!--                         </ol> -->
<!--                     </div> -->
<!--                 </section> -->
                <section class="main-content">
                <div class="panel">
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-12">
                          <div class="tabbable-custom">
                               <ul class="nav nav-tabs">
                                   <li class="active"><a href="#tab_5_1" data-toggle="tab">Regular Shift</a></li>
                                   <li ><a href="#tab_5_2" data-toggle="tab">Schedule Job</a></li>
                               </ul>
                               <div class="tab-content">
                                   <div id="tab_5_1" class="tab-pane active">
                                   <div class="row">
                                   
                                   	<div class="col-md-6">
                                   		<p>Today's Shift : </p>
                                   		
                                   		<p class="text-center">
                                            <button type="button" class="btn btn-primary btn-lg">Start</button>
                                            <button type="button" class="btn btn-primary btn-lg" disabled="disabled">Finish</button>
                                        </p>
                                      </div>
	                                   	<div class="col-md-6">
	                                   		<p class="clock">Current Time <strong class="plsm" id="hours"> </strong>
	                                   						<strong id="point">:</strong>
	                                   						<strong id="min"></strong>
	                                   						<strong id="point">:</strong>
	                                   						<strong id="sec"> </strong>
	                                   		</p>
	                                   		<ul class="list-inline clock text-left">
		                                        <li id="chours" class="fw700 fz30 ">00</li>
		                                        <li id="point" class="fw700 fz30 ">:</li>
		                                        <li id="cmin" class="fw700 fz30">00</li>
		                                        <li >Activity time.</li>
		                                    </ul>
	                                   	</div>
	                                   	</div>
                                   </div>
                                   <div id="tab_5_2" class="tab-pane">
                                  	<div class="row">
                                   
	                                   	<div class="col-md-6">
	                                   		<p>Today's Shift : </p>
	                                   		
	                                   		<p class="text-center">
	                                            <button type="button" class="btn btn-primary btn-lg">Start</button>
	                                            <button type="button" class="btn btn-primary btn-lg">Finish</button>
	                                        </p>
	                                      </div>
	                                   	<div class="col-md-6">
	                                   		<p>Current Time : </p>
	                                   		<ul class="list-inline clock text-left">
		                                        <li id="chours" class="fw700 fz30 ">00</li>
		                                        <li id="point" class="fw700 fz30 ">:</li>
		                                        <li id="cmin" class="fw700 fz30">00</li>
		                                        <li >Activity time.</li>
		                                    </ul>
	                                   	</div>
                                   </div>
                                   </div>
                               </div>
                           </div>
                        </div>
                    </div>
                </div>
                </div>
                </section>
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
	<jsp:include page="../scripts.jsp"/>
	<script type="text/javascript">

$(document).ready(function() {
	// Create two variable with the names of the months and days in an array
	var monthNames = [ "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" ];
	var dayNames= ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"]
	
	var newDate = new Date("<fmt:formatDate pattern='yyyy/MM/dd HH:mm:ss' value='${dateNow}'/>");
	
	newDate.setDate(newDate.getDate());      
	var seconds = newDate.getSeconds();
	var minutes = newDate.getMinutes();
	var hours = newDate.getHours();

	setInterval( function() {
		$("#sec").html(( seconds < 10 ? "0" : "" ) + seconds);
		$("#min").html(( minutes < 10 ? "0" : "" ) + minutes);
		$("#hours").html(( hours < 10 ? "0" : "" ) + hours);
		$('#Date').html(dayNames[newDate.getDay()] + " " + newDate.getDate() + ' ' + monthNames[newDate.getMonth()] + ' ' + newDate.getFullYear());
		seconds++;
		if (seconds == 60) {
			seconds = 0;
			minutes++;
			if (minutes == 60) {
				minutes = 0;
				hours++;
				if (hours == 24) {
					hours= 0;
					newDate.setDate(newDate.getDate() + 1);
				}
			}
		}
	},1000);
	<c:if test="${shift.flagAct == 1 }">
	
		var startDate = new Date("<fmt:formatDate pattern='yyyy/MM/dd HH:mm:ss' value='${shift.dateStart}'/>");
		startDate.setDate(startDate.getDate());      
		var cseconds =  Math.floor(((newDate - startDate) % 60000 ) / 1000);
		var cminutes = Math.floor(((newDate - startDate) % 3600000)/ 60000);
		var chours =  Math.floor((newDate - startDate) / 3600000);
		
		setInterval( function() {
			$("#cmin").html(( cminutes < 10 ? "0" : "" ) + cminutes);
			$("#chours").html(( chours < 10 ? "0" : "" ) + chours);
			cseconds++;
			if (cseconds == 60) {
				cseconds = 0;
				cminutes++;
				if (cminutes == 60) {
					cminutes = 0;
					chours++;
				}
			}
		},1000);
	</c:if>
});
// setTimeout("location.href = '../rrhhShift/getPage?flagReload=yes'", 360000);
</script>
</body>
</html>