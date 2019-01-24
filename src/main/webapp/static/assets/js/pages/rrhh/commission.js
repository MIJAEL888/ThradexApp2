$(function () {
	validateformCommission();
});

function validateformCommission(){
	$('#formCommission').find('#inputFiles').fileinput({
        showUpload: false,
        browseLabel: 'Browse',
        maxFileCount: 10,
        mainClass : 'input-medium'
    });
	
	$('#formCommission').find('#datepicker2').datetimepicker({
		format: 'DD/MM/YYYY HH:mm',
    	stepping: 5,
        sideBySide: true,
    	minDate: moment().add(1, 'days'),
    	maxDate: moment().add(60, 'days')
	}).on('dp.change', function(e) {
    	$('#formCommission').formValidation('revalidateField', 'dateFinish');
    });
	
	$('#formCommission').find('#datepicker').datetimepicker({
    	format: 'DD/MM/YYYY HH:mm',	
    	stepping: 5,
    	sideBySide: true,
    	minDate: moment().add(1, 'days'),
    	maxDate: moment().add(75, 'days')
    }).on('dp.change', function(e) {
    	$('#formCommission').formValidation('revalidateField', 'dateStart');
    });
	
	
	$('#formCommission')
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
             'rhPerson.id': {
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
        
        $form.ajaxSubmit({
//        	dataType: 'json',
        	target : '#detailModalCommission',
            url: $form.attr('action'),
            success: function(responseText, statusText, xhr, $form) {
//            	if(response.status == "SUCCESS"){
//            		$("#successExtra").show();
//            		
//            	}else{
//            		$("#errorExtra").show();
//            		$("#errorExtra").append(response.message);
//            	}
            	$('#loading').modal('hide');
            	validateformCommission();
            	updatePanelTable();
            	updatePanelTableH();
            }
        });
    });
}
