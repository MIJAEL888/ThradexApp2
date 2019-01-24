$(function () {
	/* initialize the external events
	-----------------------------------------------------------------*/
//   $( "#EXTERNAL-EVENTS" ).ACCORDION();
	$('#external-events .external-event').each(function() {
		var idPerson =  $(this).find('.idPerson').val();
		var idScale =  $(this).find('.idScale').val();
		var title = $.trim($(this).text());
		var startTime =  $(this).find('.startTime').val();
		var duration =  $(this).find('.duration').val();
		var color =  $(this).find('.color').val();
		//alert("" + color);
		
		// store data so the calendar knows to render an event upon drop
		$(this).data('event', {
			start:  startTime,
			duration: duration,
			idPerson: idPerson,
			idScale: idScale,
			editable: true,
			color: color,
			title: title, // use the element's text as the event title
			stick: true // maintain when user navigates (see docs on the renderEvent method)
		}).css({"background-color": color, "border-color": color});

		// make the event draggable using jQuery UI
		$(this).draggable({
			appendTo: "body",
	        helper: "clone",
			snap: true,
			zIndex: 999,
			revert: true,      // will cause the event to go back to its
			revertDuration: 0  //  original position after the drag
		});

	});
	
	/* initialize the calendar
	-----------------------------------------------------------------*/

	$('#calendar').fullCalendar({
		events: '../shiftScale/events',
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
		editable: false,
		droppable: true, // this allows things to be dropped onto the calendar
		eventDrop: function(event, delta, revertFunc) {
			//alert(event.title + " was dropped on " + event.start.format());
			 $('#loading').modal('show');
	    	$.ajax({
			      url: "../shiftScale/regScheduleScale",
			      global: false,
			      type: "GET",
			      data: ({idPerson : event.idPerson,
			    	  	  idScale: event.idScale, 
			    	  	  startDate: event.start.format()}),
			      dataType: "html",
			      success: function(data){
			    	 alert(data);
			      },
			      error: function (xhr, ajaxOptions, thrownError) {
			    	  alert(xhr.responseText);   
//			    	  $("#dialogInfoShift").dialog('open'); 
			      }    
				
			 });
	    
//	    	$.getJSON("../shiftScale/regShift",
//	    			{idPerson : event.idPerson,
//	    			idScale: event.idScale, 
//	    	  	  	startDate: event.start.format()},
//	    	  	function(data){
//	    	  	alert("data " + data);
//	    	  
//			});
	    	 $('#loading').modal('hide');
	    	return false;
		},
		drop: function(date) {
	        //alert("Dropped on " + date.format() + "  -  ");
	    },
	    eventReceive: function( event ) { 
	    	 $('#loading').modal('show');
	    	$.ajax({
			      url: "../shiftScale/regScheduleScale",
			      global: false,
			      type: "GET",
			      data: ({idPerson : event.idPerson,
			    	  	  idScale: event.idScale, 
			    	  	  startDate: event.start.format()}),
			      dataType: "html",
			      success: function(data){
			    	  event.id = data;
			          $('#calendar').fullCalendar('updateEvent', event);
			      },
			      error: function (xhr, ajaxOptions, thrownError) {
			    	  alert(xhr.responseText);   
//			    	  $("#dialogInfoShift").dialog('open'); 
			      }    
				
			 });
	    
//	    	$.getJSON("../shiftScale/regShift",
//	    			{idPerson : event.idPerson,
//	    			idScale: event.idScale, 
//	    	  	  	startDate: event.start.format()},
//	    	  	function(data){
//	    	  	alert("data " + data);
//	    	  
//			});
	    	 $('#loading').modal('hide');
	    	return false;
	    }, eventClick: function(event, element) {
	    	var r=confirm("Delete the shift: " + event.title);
            if (r===true){
            	$('#calendar').fullCalendar('removeEvents', event._id);

            	$.ajax({
  			      url: "../shiftScale/deleteScheduleScale",
  			      global: false,
  			      type: "GET",
  			      data: ({id : event.id}),
  			      dataType: "html",
  			      success: function(data){
  			    	  console.log(data);
  			      },
  			      error: function (xhr, ajaxOptions, thrownError) {
  			    	  console.log(xhr.responseText);   
//  			    	  $("#dialogInfoShift").dialog('open'); 
  			      }    
  				
  			 });
            }
	    }
	});
	
	$('#calendarStaff').fullCalendar({
		events: '../shiftScale/events',
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
		editable: false,
		droppable: true, // this allows things to be dropped onto the calendar
		eventDrop: function(event, delta, revertFunc) {
	    	return false;
		},
		drop: function(date) {
			return false;
	        //alert("Dropped on " + date.format() + "  -  ");
	    },
	    eventReceive: function( event ) { 
	    	return false;
	    }, eventClick: function(event, element) {
	    	return false;
	    }
	});
});

function processAlert(idShiftAlert){
	$('#loading').modal('show');
	$.get( "../shiftScale/getShiftJustificate/" + idShiftAlert, function( data ) {
		$("#process-alert").find(".modal-body").html(data);
		$('#process-alert input[type="radio"]').iCheck({
	        radioClass: 'iradio_minimal-grey',
//	        checkedClass: 'unchecked',
	        increaseArea: '20%' // optional
	        	
	    });
		validFomrAlert();
		$('#process-alert').modal('show');
		$('#loading').modal('hide');
	});
}

function justifyAlert(idShiftAlert){
	$('#loading').modal('show');
	$.get( "../shiftScale/getShiftAlert/" + idShiftAlert, function( data ) {
		$("#process-scale").find(".modal-body").html(data);
		$('#process-scale').find('#inputFiles').fileinput({
	        showUpload: false,
	        browseLabel: 'Browse',
	        maxFileCount: 10,
	        mainClass : 'input-medium'
	    });
		validFomrJust();
		$('#process-scale').modal('show');
		$('#loading').modal('hide');
	});
}

function startScale(idScheduleScale){
	$('#loading').modal('show');
	$.get( "../shiftScale/registerStart/" + idScheduleScale, function( data ) {
		$("#process-scale").find(".modal-body").html(data);
		$('#process-scale').find('#inputFiles').fileinput({
	        showUpload: false,
	        browseLabel: 'Browse',
	        maxFileCount: 10,
	        mainClass : 'input-medium'
	    });
		validFomrJust();
		$('#process-scale').modal('show');
		$('#loading').modal('hide');
		updatePanelSession('#panel-body-session');
	});
}

function finishScale(idShiftScale){
	$('#loading').modal('show');
	$.get( "../shiftScale/registerFinish/" + idShiftScale, function( data ) {
		$("#process-scale").find(".modal-body").html(data);
		$('#process-scale').find('#inputFiles').fileinput({
	        showUpload: false,
	        browseLabel: 'Browse',
	        maxFileCount: 10,
	        mainClass : 'input-medium'
	    });
		validFomrJust();
		$('#process-scale').modal('show');
		$('#loading').modal('hide');
		updatePanelSession('#panel-body-session');
	});
}

function validFomrJust(){
	
	$('#formJustifShift')
    .formValidation({
    	 framework: 'bootstrap',
         icon: {
             valid: 'glyphicon glyphicon-ok',
             invalid: 'glyphicon glyphicon-remove',
             validating: 'glyphicon glyphicon-refresh'
         },
         fields: {
             reason: {
                 validators: {
                     notEmpty: {
                         message: $msg_error_empty
                     }
                 }
             },
         }
    })
    .on('success.form.fv', function(e) {
        e.preventDefault();
        $('#loading').modal('show');
        var $form = $(e.target), fv = $form.data('formValidation');
        $form.find("input:file").each(function() {
            if($(this).val() == "") $(this).remove();
        });
        $form.ajaxSubmit({
//        	dataType: 'json',
//        	target : '#detailModalExtraHour',
            url: $form.attr('action'),
            success: function(responseText, statusText, xhr, $form) {
            	$("#process-scale").find(".modal-body").html(responseText);
            	$('#loading').modal('hide');
            	updatePanelTable();
            	updatePanelTableH();
            }
        });
    });
}

function validFomrAlert(){
	
	$('#formShiftAlert')
    .formValidation({
    	 framework: 'bootstrap',
         icon: {
             valid: 'glyphicon glyphicon-ok',
             invalid: 'glyphicon glyphicon-remove',
             validating: 'glyphicon glyphicon-refresh'
         },
         fields: {
             response: {
                 validators: {
                     notEmpty: {
                         message: $msg_error_empty
                     }
                 }
             },
             approved:{
            	 validators: {
                     notEmpty: {
                         message: 'Pick an option.'
                     }
                 }
             }
         }
    })
    .on('success.form.fv', function(e) {
        e.preventDefault();
        $('#loading').modal('show');
        var $form = $(e.target), fv = $form.data('formValidation');;
        $form.ajaxSubmit({
//        	dataType: 'json',
//        	target : '#detailModalExtraHour',
            url: $form.attr('action'),
            success: function(responseText, statusText, xhr, $form) {
            	$("#process-alert").find(".modal-body").html(responseText);
            	$('#loading').modal('hide');
            	$('#process-alert').modal('hide');
            	updatePanelTable();
            	updatePanelTableH();
            }
        });
    })
    .find('input[name="approved"]')
    // Called when the radios/checkboxes are changed
    .on('ifChanged', function(e) {
        // Get the field name
        var field = $(this).attr('name');
        $('#formShiftAlert').formValidation('revalidateField', field);
    })
    .end();
}


function updateTime(idShift){
	$.get( "../register/ajaxShiftRegTime?idShit=" + idShift, function( data ) {
		$("#modalShiftRUM").html(data);
//		alert(data);
//		$("#process-scale").find(".modal-body").html(data);
//		$('#process-scale').find('#inputFiles').fileinput({
//	        showUpload: false,
//	        browseLabel: 'Browse',
//	        maxFileCount: 10,
//	        mainClass : 'input-medium'
//	    });
//		validFomrJust();
//		$('#process-scale').modal('show');
//		$('#loading').modal('hide');
//		updatePanelSession('#panel-body-session');
	});
	
	$('#modalTimeUpdate').modal('show');
	
}
function toPendingShift(idShift){
	if (confirm("Are you sure to reverse this shift To state planned? Deleted records that can not be recovered.")) {
		$.get("../register/toPendingShift?idShift=" + idShift,function(data){
			$('#loading').modal('show');
			location.href="../register/getPage";
		});
	}	
}


//function formatTableShift(nameTable){
//	var dt = $(nameTable).DataTable({
//		"order": [[ 3, "desc" ]],
//		scrollY:        '60vh',
//        scrollCollapse: true,
//        paging:         false,
//		 "lengthChange": false,
//		 responsive:  {
//				details: false
//			},
//		 "columnDefs": [
//            { "orderable": false,  visible: false, "targets": 0}
//          ]
//	});
//	
//    $(nameTable + ' tbody').on( 'click', 'tr td.details-control', function () {
//        var tr = $(this).closest('tr');
//        var row = dt.row( tr );
//        
//        if ( row.child.isShown() ) {
//            tr.removeClass( 'details' );
//            row.child.hide();
//        } else {
//            tr.addClass( 'details' );
//        	$.get( "../shift/" + tr.attr('id'), function( data ) {
//        		 row.child(data).show();
//        	});
//           
//        }
//    } );
//}
