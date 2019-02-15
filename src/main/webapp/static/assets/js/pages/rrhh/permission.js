$(document).ready(function(){
	
//	$('#formPermission').find('#datepicker').datetimepicker({
//		format: 'DD/MM/YYYY HH:mm',
//    	stepping: 5,
//    	minDate: moment().add(1, 'days'),
//    	maxDate: moment().add(60, 'days')
//	});
//	
//	$("#sliderPermission").slider();
//	
//	$('#formPermission').find('#datepicker2').datetimepicker({
//		format: 'DD/MM/YYYY HH:mm',
//    	stepping: 5,
//    	minDate: moment().add(1, 'days'),
//    	maxDate: moment().add(60, 'days')
//	}).on('dp.change', function(e) {
//    	$('#formPermission').formValidation('revalidateField', 'dateFinish');
//    });
	
//	$('#formPermission').find('#datepicker').datetimepicker({
//    	format: 'DD/MM/YYYY HH:mm',	
//    	stepping: 5,
//    	minDate: moment().add(1, 'days'),
//    	maxDate: moment().add(60, 'days')
//    }).on('dp.change', function(e) {
//    	$('#formPermission').formValidation('revalidateField', 'dateStart');
//    });

	validateFormPermission();
});

function validateFormPermission(){
	$('#formPermission').find('#inputFiles').fileinput({
        showUpload: false,
        browseLabel: 'Browse',
        maxFileCount: 10,
        mainClass : 'input-medium'
    });
	
	$('#formPermission').find('#datepicker').datetimepicker({
		format: 'DD/MM/YYYY HH:mm',
    	stepping: 5,
    	sideBySide: true,
    	minDate: moment(),
    	maxDate: moment().add(60, 'days')
	});
	
	$("#sliderPermission").bootstrapSlider().on("slide", function(slideEvt) {
		$("#durationHourPerm").val(slideEvt.value);
	});	
	
	
	$('#formPermission').find('#tipePerm').change(function (){
		 if ($(this).val() != 0) {
			 var request = $.ajax({
				  url: "../shift/messagePermission",
				  method: "GET",
				  data: { idType : $(this).val()},
				  dataType: "html"
				});
				 
				request.done(function( msg ) {
					$( "#messageTypePerm" ).html( msg );
				});
				 
				request.fail(function( jqXHR, textStatus ) {
					console.log("error: when get the permission message");
				});
		 }
	});
	
	$('#formPermission')
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
                     }
                 },
//                 onSuccess: function(e, data) {
//                     // data.fv is the plugin instance
//                     // Revalidate the end date if it's not valid
//                     if (!data.fv.isValidField('dateFinish')) {
//                         data.fv.revalidateField('dateFinish');
//                     }
//                 }
             },
//             dateFinish: {
//                 validators: {
//                     notEmpty: {
//                         message: $msg_error_date
//                     },
//                     date: {
//                         format:  'DD/MM/YYYY HH:mm',
//                        min: 'dateStart'
//                     }
//                 },
//                 onSuccess: function(e, data) {
//                     // data.fv is the plugin instance
//                     // Revalidate the start date if it's not valid
//                     if (!data.fv.isValidField('dateStart')) {
//                         data.fv.revalidateField('dateStart');
//                     }
//                 }
//             },
             reason: {
                 validators: {
                     notEmpty: {
                         message: $msg_error_empty
                     }
                 }
             },
             'rhTypePerm.id': {
                 validators: {
                	 selectCero: {
                         message: $msg_error_empty
                     }
                 }
             },
         }
    })
    .on('success.form.fv', function(e) {
        e.preventDefault();
        $('#loading').modal('show');
        var $form = $(e.target);
        $form.find("input:file").each(function() {
            if($(this).val() == "") $(this).remove();
        });
        //var id = $form.find("input:hidden").val();
        $form.ajaxSubmit({
//        	dataType: 'json',
        	target : '#detailModalPermission',
            url: $form.attr('action'),
            success: function(responseText, statusText, xhr, $form) {
            	$('#loading').modal('hide');s
            	validateFormPermission();

            	updatePanelTable();
            	updatePanelTableH();
            }
        });
    });
}

function processPermission(id){
	 $('#loading').modal('show');
	$.get( "../shift/process/"+id, function( data ) {
		$('#process-permission').find(".modal-body").html(data);
		
//		$("#divProcess").html(data);
		$('#process-permission input[type="radio"]').iCheck({
	        radioClass: 'iradio_minimal-grey',
	        increaseArea: '20%' // optional
	    });
//		$('#process-license input[type="radio"][name="approved"]').on('ifChecked', function(e) {
//			alert('test ' + $(this).val());
//	        if ($(this).val() == 'false') {
//	        	$('#process-extra input[type="radio"][name="toPay"]').iCheck('disable');;
//	        }else {
//	        	$('#process-extra input[type="radio"][name="toPay"]').iCheck('enable');
//	        }
//	    });
		processPermissionValid()
		$('#process-permission').modal('show');
		$('#loading').modal('hide');
	});
}

function processPermissionValid(){
	 $('#formProcessPermission')
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
            $('#loading').modal('show');
	        var $form = $(e.target);
            var id = $form.find("input:hidden").val();
	        $form.ajaxSubmit({
	            // You can change the url option to desired target
//	        	target : '#detailExtraHour',
	            url: $form.attr('action'),
	            success: function(responseText, statusText, xhr, $form) {
	                // Process the response returned by the server ...
	                // console.log(responseText);
	            	$('#process-permission').find(".modal-body").html(responseText);
	            	$('#process-permission').modal('hide');
                    $('#loading').modal('hide');
                    updateStatusButton(id);
	            	//updatePanelTable();
	            	// updatePanelTableH();
	            }
	        });
	    })
	    .find('input[name="approved"]')
        // Called when the radios/checkboxes are changed
        .on('ifChanged', function(e) {
            // Get the field name
            var field = $(this).attr('name');
            //$('#formProcessPermission').formValidation('revalidateField', field);
        })
        .end();
}