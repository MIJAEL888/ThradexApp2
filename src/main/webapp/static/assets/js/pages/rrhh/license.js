$(document).ready(function(){
//	$('#formLicense').find('#datepicker2').datetimepicker({
//		format: 'DD/MM/YYYY',
//    	stepping: 5,
//    	minDate: moment().add(1, 'days'),
//    	maxDate: moment().add(30, 'days')
//	}).on('dp.change', function(e) {
//    	$('#formLicense').formValidation('revalidateField', 'dateFinish');
//    });
//	
//	$('#formLicense').find('#datepicker').datetimepicker({
//    	format: 'DD/MM/YYYY',	
//    	stepping: 5,
//    	minDate: moment().add(1, 'days'),
//    	maxDate: moment().add(30, 'days')
//    }).on('dp.change', function(e) {
//    	$('#formLicense').formValidation('revalidateField', 'dateStart');
//    });
	validateFormLicense();
	
});

function validateFormLicense(){
	$('#formLicense').find('#inputFiles').fileinput({
        showUpload: false,
        browseLabel: 'Browse',
        maxFileCount: 10,
        mainClass : 'input-medium'
    });
	
	$('#formLicense').find('#datepicker').datetimepicker({
		format: 'DD/MM/YYYY',
		stepping: 15,
		minDate: moment().add(0, 'days'),
		maxDate: moment().add(30, 'days')
	}).on('dp.change', function(e) {
    	$('#formLicense').formValidation('revalidateField', 'dateFinish');
	});
	
	$('#formLicense').find('#datepicker2').datetimepicker({
		format: 'DD/MM/YYYY',	
		stepping: 5,
		minDate: moment().add(0, 'days'),
		maxDate: moment().add(30, 'days')
	}).on('dp.change', function(e) {
		$('#formLicense').formValidation('revalidateField', 'dateStart');
	});
		
	$('#formLicense').find('#typeLic').change(function (){
		if ($(this).val() != 0) {
			var request = $.ajax({
				  url: "../shift/messageLicense",
				  method: "GET",
				  data: { idType : $(this).val()},
				  dataType: "html"
				});
				 
				request.done(function( msg ) {
					$( "#messageTypeLic" ).html( msg );
				});
				 
				request.fail(function( jqXHR, textStatus ) {
					console.log("error: when get the license message");
				});
		}
	});
	
//	$("#sliderLicense").bootstrapSlider().on("slide", function(slideEvt) {
//		$("#durationHourLic").val(slideEvt.value);
//	});	
	
	$('#formLicense')
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
                         format: 'DD/MM/YYYY',
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
                         format:  'DD/MM/YYYY',
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
             'rhTypePerm.id': {
                 validators: {
                	 selectCero: {
                         message: $msg_error_select
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
        
        var $start = $form.find('input[name=dateStart]');
        $start.val($start.val() + ' 00:00');
        
        var $finish = $form.find('input[name=dateFinish]');
        $finish.val($finish.val() + ' 23:59');
        alert('Start ' + $start.val());
        alert('Finish ' + $finish.val());
        $form.ajaxSubmit({
//        	dataType: 'json',
        	target : '#detailModalLicense',
            url: $form.attr('action'),
            success: function(responseText, statusText, xhr, $form) {
            	$('#loading').modal('hide');
            	validateFormLicense();
            	updatePanelTable();
            	updatePanelTableH();
            }
        });
    });
}

function processLicense(id){
	 $('#loading').modal('show');
	$.get( "../shift/process/"+id, function( data ) {
		$('#process-license').find(".modal-body").html(data);
		
//		$("#divProcess").html(data);
		$('#process-license input[type="radio"]').iCheck({
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
		processLicenseValid()
		$('#process-license').modal('show');
		$('#loading').modal('hide');
	});
}

function processLicenseValid(){
	 $('#formProcessLicense')
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
	            	$('#process-license').find(".modal-body").html(responseText);
	            	$('#process-license').modal('hide');
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
            //$('#formProcessLicense').formValidation('revalidateField', field);
        })
        .end();
}
