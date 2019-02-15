$(document).ready(function(){
	
	formatTableShift('#datatables');
	formatTableShift('#datatablesHistory');
	formatTableShift2('#datatablesSession');
	formatTableShift2('#datatablesPlanned');
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
//       alert("tab1");
       $($.fn.dataTable.tables(true)).DataTable()
       .columns.adjust()
       .responsive.recalc();
    });  
	
	/* initialize the calendar
	-----------------------------------------------------------------*/
	source = '../shiftCalendar/eventsOne?idPerson=' + 
					$('#idPerson').val() + '&idEvent=' + $('#idEvent').val();
	
	$('#calendar').fullCalendar({
		events: source,
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
		editable: false,
		droppable: true, // this allows things to be dropped onto the calendar
		eventDrop: function(event, delta, revertFunc) {
			//alert(event.title + " was dropped on " + event.start.format());
//			 $('#loading').modal('show');
//	    	$.ajax({
//			      url: "../shiftScale/regScheduleScale",
//			      global: false,
//			      type: "GET",
//			      data: ({idPerson : event.idPerson,
//			    	  	  idScale: event.idScale, 
//			    	  	  startDate: event.start.format()}),
//			      dataType: "html",
//			      success: function(data){
//			    	 alert(data);
//			      },
//			      error: function (xhr, ajaxOptions, thrownError) {
//			    	  alert(xhr.responseText);   
////			    	  $("#dialogInfoShift").dialog('open'); 
//			      }    
//				
//			 });
//	    
//	    	$.getJSON("../shiftScale/regShift",
//	    			{idPerson : event.idPerson,
//	    			idScale: event.idScale, 
//	    	  	  	startDate: event.start.format()},
//	    	  	function(data){
//	    	  	alert("data " + data);
//	    	  
//			});
//	    	 $('#loading').modal('hide');
	    	return false;
		},
		drop: function(date) {
	        //alert("Dropped on " + date.format() + "  -  ");
	    },
	    eventReceive: function( event ) { 
//	    	 $('#loading').modal('show');
//	    	$.ajax({
//			      url: "../shiftScale/regScheduleScale",
//			      global: false,
//			      type: "GET",
//			      data: ({idPerson : event.idPerson,
//			    	  	  idScale: event.idScale, 
//			    	  	  startDate: event.start.format()}),
//			      dataType: "html",
//			      success: function(data){
//			    	  event.id = data;
//			          $('#calendar').fullCalendar('updateEvent', event);
//			      },
//			      error: function (xhr, ajaxOptions, thrownError) {
//			    	  alert(xhr.responseText);   
////			    	  $("#dialogInfoShift").dialog('open'); 
//			      }    
//				
//			 });
	    
//	    	$.getJSON("../shiftScale/regShift",
//	    			{idPerson : event.idPerson,
//	    			idScale: event.idScale, 
//	    	  	  	startDate: event.start.format()},
//	    	  	function(data){
//	    	  	alert("data " + data);
//	    	  
//			});
//	    	 $('#loading').modal('hide');
	    	return false;
	    }, eventClick: function(event, element) {
	    	 $('#loading').modal('show');
        	$.ajax({
		      url: "../shiftCalendar/detailShift",
		      global: false,
		      type: "GET",
		      data: ({idShift : event.id, idScale: event.idScale}),
		      dataType: "html",
		      success: function(data){
		    	  $('#modal-info').find("#modal-info-body").html(data);
		    	  $('#modal-info').modal('show');
		    	  console.log(data);
		      },
		      error: function (xhr, ajaxOptions, thrownError) {
		    	  console.log(xhr.responseText);   
		      }    
  				
  			 });
        	 $('#loading').modal('hide');
	    }
	});
	
	$('#idPerson').change(function() {
//		alert("changin id person" );
		$('#calendar').fullCalendar('removeEventSource', source);
		$('#calendar').fullCalendar('removeEvents');
		source = '../shiftCalendar/eventsOne?idPerson=' + 
		$('#idPerson').val() + '&idEvent=' + $('#idEvent').val();
		$('#calendar').fullCalendar('addEventSource', source);
	});
	
	$('#idEvent').change(function() {
//		alert("changin id person" );
		$('#calendar').fullCalendar('removeEventSource', source);
		$('#calendar').fullCalendar('removeEvents');
		source = '../shiftCalendar/eventsOne?idPerson=' + 
		$('#idPerson').val() + '&idEvent=' + $('#idEvent').val();
		$('#calendar').fullCalendar('addEventSource', source);
	});
//	$(".panel-session").each(function (index, element) {
//        var me = $(this);
//        $("> .panel-heading > .actions > a > i", me).click(function (e) {
//            if ($(this).hasClass('ion-chevron-down')) {
//                $(">.panel-body", me).slideUp(200);
//                $(this).removeClass('ion-chevron-down').addClass('ion-chevron-up');
//            }
//            else if ($(this).hasClass('ion-chevron-up')) {
//                $(">.panel-body", me).slideDown(200);
//                $(this).removeClass('ion-chevron-up').addClass('ion-chevron-down');
//            }
//            else if ($(this).hasClass('fa-cog')) {
//                //Show modal
//            }
//            else if ($(this).hasClass('ion-refresh')) {
//                var reload = $(this).parent().parent().parent().next();
//                blockUI(reload);
//              
//                unblockUI(reload);
//                
//            }
//            else if ($(this).hasClass('ion-close')) {
//                me.remove();
//            }
//        });
//    });
});

function updateTableShift(type){
	switch (type) {
	case 1:
		$.get( "../shift/list/1", function( data ) {
			$("#divPanelBodyShift").html(data);
			formatTableShift('#datatables');
		});
		return true;
	case 2:
		$.get( "../shift/listProcess", function( data ) {
			$("#divPanelBodyShift").html(data);
			formatTableShift('#datatables');
		});
		return true;
	case 3:
		$.get( "../shift/list/3", function( data ) {
			$("#divPanelBodyShift").html(data);
			formatTableShift('#datatables');
		});
		return true;
	case 4:
		$.get( "../shift/search", function( data ) {
			$("#divPanelBodyShift").html(data);
		});
		return true;
	default:
		return true;
	}
}

function processRequestShift(id){
	$.get( "../shift/process/"+id, function( data ) {
		$("#divProcess").html(data);
		$('#divProcess input[type="radio"]').iCheck({
	        radioClass: 'iradio_minimal-grey',
	        increaseArea: '20%' // optional
	    });
		$('#divProcess input[type="radio"][name="approved"]').on('ifChecked', function(e) {
//			alert('test ' + $(this).val());
	        if ($(this).val() == 'false') {
	        	$('#divProcess input[type="radio"][name="toPay"]').iCheck('disable');;
	        }else {
	        	$('#divProcess input[type="radio"][name="toPay"]').iCheck('enable');
	        }
	    });
		$('#modal-process').modal('show');
	});
}

function processFormValidation(){
	 $('#formProcess')
	    .formValidation({
	    	 framework: 'bootstrap',
	         icon: {
	             valid: 'glyphicon glyphicon-ok',
	             invalid: 'glyphicon glyphicon-remove',
	             validating: 'glyphicon glyphicon-refresh'
	         },
	         fields: {
	             comment: {
	                 validators: {
	                     notEmpty: {
	                         message: 'A short comment is required.'
	                     }
	                 }
	             },																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																													
	         }
	    })
	    .on('success.form.fv', function(e) {
	        // Prevent form submission
	        e.preventDefault();

	        var $form = $(e.target);
	        $form.ajaxSubmit({
	            // You can change the url option to desired target
	        	target : '#detailExtraHour',
	            url: $form.attr('action'),
	            success: function(responseText, statusText, xhr, $form) {
	                // Process the response returned by the server ...
	                // console.log(responseText);
	            }
	        });
	    });
}

function detailWorked(code){
	window.console.log("detailWorked");
	$('#loading').modal('show');
	$.get( "../shift/detail/"+code, function( data ) {
		$("#modal-info").find(".modal-body").html(data);
		
		$('#modal-info').modal('show');
		$('#loading').modal('hide');
	});
}
function updateStatusButton(id){
    $("#"+ id +" td:last-child").html("<span class='btn-label'><i class='ion-checkmark-circled'></i> Processed </span>");
}
function updatePanelTable(){
//	alert("update session");
	blockUI($("#divTabShift"));
	$.get( "../shift/getTable", function( data ) {
		$("#divTabShift").html(data);
		formatTableShift('#datatables');
		unblockUI("#divTabShift");
	});
}
function updatePanelTableH(){
//	alert("update session");
	blockUI($("#divTabShiftHistory"));
	$.get( "../shift/getTableHistory", function( data ) {
		$("#divTabShiftHistory").html(data);
		formatTableShift('#datatablesHistory');
		unblockUI("#divTabShiftHistory");
	});
}

function updatePanelSession(element){
//	alert("update session");
	blockUI($(element));
	$.get( "../shift/getSession", function( data ) {
		$(element).html(data);
		unblockUI(element);
	});
}

function togglePanel(element, toggle){
	var iElement = $(element).children(); 
	var me = $(toggle);
	 if ($(iElement).hasClass('ion-chevron-down')) {
         $(me).slideUp(200);
         $(iElement).removeClass('ion-chevron-down').addClass('ion-chevron-up');
     }
     else if ($(iElement).hasClass('ion-chevron-up')) {
         $(me).slideDown(200);
         $(iElement).removeClass('ion-chevron-up').addClass('ion-chevron-down');
     }
}

function formatTableShift(nameTable){
	var dt = $(nameTable).DataTable({
		"order": [[ 3, "desc" ]],
		scrollY:        '60vh',
        scrollCollapse: true,
        paging:         false,
		 "lengthChange": false,
		 responsive:  {
				details: false
			},
		 "columnDefs": [
            { "orderable": false,  visible: false, "targets": 0}
          ]
	});
	
    $(nameTable + ' tbody').on( 'click', 'tr td.details-control', function () {
        var tr = $(this).closest('tr');
        var row = dt.row( tr );
        
        if ( row.child.isShown() ) {
            tr.removeClass( 'details' );
            row.child.hide();
        } else {
            tr.addClass( 'details' );
        	$.get( "../shift/" + tr.attr('id'), function( data ) {
        		 row.child(data).show();
        	});
           
        }
    } );
}

function formatTableShift2(nameTable){
	var dt = $(nameTable).DataTable({
		"order": [[ 1, "desc" ]],
		scrollY:        '60vh',
        scrollCollapse: true,
        "bFilter": false,
        paging:         false,
		 "lengthChange": false,
		 responsive:  {
				details: false
			},
		 "columnDefs": [
            { "orderable": false,  visible: false, "targets": 0}
          ]
	});
	
    $(nameTable + ' tbody').on( 'click', 'tr td.details-control', function () {
        var tr = $(this).closest('tr');
        var row = dt.row( tr );
        
        if ( row.child.isShown() ) {
            tr.removeClass( 'details' );
            row.child.hide();
        } else {
            tr.addClass( 'details' );
        	$.get( "../shift/" + tr.attr('id'), function( data ) {
        		 row.child(data).show();
        	});
           
        }
    } );
}


//function detailCommpensation(){
//	window.console.log("detailCommpensation");
//}
//
//function detailExtraHour(){
//	window.console.log("detailExtraHour");
//}
//
//function detailDescount(){
//	window.console.log("detailDescount");
//}