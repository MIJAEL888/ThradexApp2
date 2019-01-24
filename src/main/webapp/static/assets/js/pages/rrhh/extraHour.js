$(document).ready(function(){
	
	validateFormExtra();
});

function validateFormExtra(){
	$('#formExtraHour').find('#inputFiles').fileinput({
        showUpload: false,
        browseLabel: 'Browse',
        maxFileCount: 10,
        mainClass : 'input-medium'
    });
	
	$('#formExtraHour').find('#datepicker2').datetimepicker({
		format: 'DD/MM/YYYY HH:mm',
    	stepping: 5,
    	sideBySide: true,
    	minDate: moment().subtract(30, 'days'),
    	maxDate: moment()
	}).on('dp.change', function(e) {
    	$('#formExtraHour').formValidation('revalidateField', 'dateFinish');
    });
	
	$('#formExtraHour').find('#datepicker').datetimepicker({
    	format: 'DD/MM/YYYY HH:mm',	
    	stepping: 5,
    	sideBySide: true,
    	minDate: moment().subtract(30, 'days'),
    	maxDate: moment()
    }).on('dp.change', function(e) {
    	$('#formExtraHour').formValidation('revalidateField', 'dateStart');
    });
	
	$('#formExtraHour')
    .formValidation({
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
        	target : '#detailModalExtraHour',
            url: $form.attr('action'),
            success: function(responseText, statusText, xhr, $form) {
//            	if(response.status == "SUCCESS"){
//            		$("#successExtra").show();
//            		
//            	}else{
//            		$("#errorExtra").show();
//            		$("#errorExtra").append(response.message);
//            	}
            	validateFormExtra();
            	$('#loading').modal('hide');
            	updatePanelTable();
            	updatePanelTableH();
            	
            }
        });
    });
}

function processExtraHour(id){
	 $('#loading').modal('show');
	$.get( "../shift/process/"+id, function( data ) {
		$('#process-extra').find(".modal-body").html(data);
		
//		$("#divProcess").html(data);
		$('#process-extra input[type="radio"]').iCheck({
	        radioClass: 'iradio_minimal-grey',
	        increaseArea: '20%' // optional
	    });
//		$('#process-extra input[type="radio"][name="approved"]').on('ifChecked', function(e) {
////			alert('test ' + $(this).val());
//	       
//	    });
		processExtraHourValid()
		$('#process-extra').modal('show');
		$('#loading').modal('hide');
	});
}

function processExtraHourValid(){
	 $('#formProcessExtraHour')
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
	                         message: 'A short comment is required.'
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
	        // Prevent form submission
	        e.preventDefault();

	        var $form = $(e.target);
	        $form.ajaxSubmit({
	            // You can change the url option to desired target
//	        	target : '#detailExtraHour',
	            url: $form.attr('action'),
	            success: function(responseText, statusText, xhr, $form) {
	                // Process the response returned by the server ...
	                // console.log(responseText);
	            	$('#process-extra').find(".modal-body").html(responseText);
//	            	$('#process-extra').modal('hide');
	            	updatePanelTable();
	            	updatePanelTableH();
	            }
	        });
	    })
	    .find('input[name="approved"]')
        // Called when the radios/checkboxes are changed
        .on('ifChanged', function(e) {
        	if ($(this).val() == 'false') {
 	        	$('#formProcessExtraHour input[type="radio"][name="toPay"]').iCheck('disable');;
 	        }else {
 	        	$('#formProcessExtraHour input[type="radio"][name="toPay"]').iCheck('enable');
 	        }
            // Get the field name
            var field = $(this).attr('name');
            $('#formProcessExtraHour').formValidation('revalidateField', field);
        })
        .end();
}

