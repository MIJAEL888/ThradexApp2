$(function () {

//	$('input[type="radio"]').iCheck({
//	      checkboxClass: 'icheckbox_minimal-grey',
//	      increaseArea: '20%' // optional
//		});
			
	
    $('#modal-responsive').on('hide.bs.modal', function () {
    	if(!$('#formScheduleJob').length) location.reload();
    })
    
    validateFormScheduleJob();
    
//    formatTable('#datatables');
});

function updateTable(type){
	
	switch (type) {
	case 1:
		$.get( "../scheduleJob/list/1", function( data ) {
			$("#divPanelBodyEH").html(data);
			formatTable('#datatables');
		});
		return true;
	case 2:
		$.get( "../scheduleJob/listProcess", function( data ) {
			$("#divPanelBodyEH").html(data);
			formatTable('#datatables');
		});
		return true;
	case 3:
		$.get( "../scheduleJob/list/3", function( data ) {
			$("#divPanelBodyEH").html(data);
			formatTable('#datatables');
		});
		return true;
	case 4:
		$.get( "../scheduleJob/search", function( data ) {
			$("#divPanelBodyEH").html(data);
		});
		return true;
	default:
		return true;
	}
}

function startScheduleJob(idRhShift){
	$.get( "../scheduleJob/registerStart/" + idRhShift, function( data ) {
		updatePanelSession('#panel-body-session');
	});
}

function finishScheduleJob(idRhShift){
	$.get( "../scheduleJob/registerFinish/" + idRhShift, function( data ) {
		updatePanelSession('#panel-body-session');
	});
}

function validateFormScheduleJob(){

	$('#formScheduleJob').find('#inputFiles').fileinput({
        showUpload: false,
        browseLabel: 'Browse',
        maxFileCount: 10,
        mainClass : 'input-medium'
    });
	
	$('#formScheduleJob').find('#datepicker2').datetimepicker({
		format: 'DD/MM/YYYY HH:mm',
    	stepping: 5,
    	minDate: moment().subtract(1, 'days'),
    	maxDate: moment().add(30, 'days')
	}).on('dp.change', function(e) {
    	$('#formScheduleJob').formValidation('revalidateField', 'dateStart');
    });
	
	$('#formScheduleJob').find('#datepicker').datetimepicker({
    	format: 'DD/MM/YYYY HH:mm',	
    	stepping: 5,
    	minDate: moment().subtract(1, 'days'),
    	maxDate: moment().add(30, 'days')
    }).on('dp.change', function(e) {
    	$('#formScheduleJob').formValidation('revalidateField', 'dateFinish');
    });
	
	 $('#formScheduleJob').formValidation({
    	 framework: 'bootstrap',
         icon: {
             valid: 'glyphicon glyphicon-ok',
             invalid: 'glyphicon glyphicon-remove',
             validating: 'glyphicon glyphicon-refresh'
         },
         fields: {
        	 dateStart: {
                 validators: {
                     notEmpty: {
                         message: $msg_error_date 
                     },
                     date: {
                         format: 'DD/MM/YYYY HH:mm',
                        max: 'dateFinish'
                     }
         
                 },
                 onSuccess: function(e, data) {
                     // data.fv is the plugin instance
                     // Revalidate the end date if it's not valid
                     if (!data.fv.isValidField('dateFinish')) {
                         data.fv.revalidateField('dateFinish');
                     }
                 }
             },
             dateFinish: {
                 validators: {
                     notEmpty: {
                         message: $msg_error_date
                     },
                     date: {
                         format:  'DD/MM/YYYY HH:mm',
                        min: 'dateStart'
                     }
                 },
                 onSuccess: function(e, data) {
                     // data.fv is the plugin instance
                     // Revalidate the start date if it's not valid
                     if (!data.fv.isValidField('dateStart')) {
                         data.fv.revalidateField('dateStart');
                     }
                 }
             },
             reason: {
                 validators: {
                     notEmpty: {
                         message: $msg_error_empty
                     }
                 }
             },
             'rhPerson.id': {
                 validators: {
                     selectCero: {
                         message: $msg_error_empty
                     }
                 }
             },
         }
    })
    .on('success.form.fv', function(e) {
        // Prevent form submission
        e.preventDefault();

        var $form = $(e.target);
        $form.find("input:file").each(function() {
            if($(this).val() == "") $(this).remove();
        });
        $form.ajaxSubmit({
            // You can change the url option to desired target
        	target : '#detailScheduleJob',
            url: $form.attr('action'),
            success: function(responseText, statusText, xhr, $form) {
                // Process the response returned by the server ...
                 console.log(responseText);
            	validateFormScheduleJob();
            	updatePanelTable();
            	updatePanelTableH();
            }
        });
    });
}
function formatTable(nameTable){
	var dt = $(nameTable).DataTable({
		"order": [[ 2, "desc" ]],
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
        	$.get( "../scheduleJob/" + tr.attr('id'), function( data ) {
        		 row.child(data).show();
        	});
           
        }
    } );
}
