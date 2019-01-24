$(function () {
	validateformAbroadJob();
});

function validateformAbroadJob(){
	$('#formAbroadJob').find('#inputFiles').fileinput({
        showUpload: false,
        browseLabel: 'Browse',
        maxFileCount: 10,
        mainClass : 'input-medium'
    });
	
	$('#formAbroadJob').find('#datepicker2').datetimepicker({
		format: 'DD/MM/YYYY',
    	stepping: 5,
    	minDate: moment().add(1, 'days'),
    	maxDate: moment().add(60, 'days')
	}).on('dp.change', function(e) {
    	$('#formAbroadJob').formValidation('revalidateField', 'dateFinish');
    });
	
	$('#formAbroadJob').find('#datepicker').datetimepicker({
    	format: 'DD/MM/YYYY',	
    	stepping: 5,
    	minDate: moment().add(1, 'days'),
    	maxDate: moment().add(75, 'days')
    }).on('dp.change', function(e) {
    	$('#formAbroadJob').formValidation('revalidateField', 'dateStart');
    });
	
	$('#formAbroadJob').find('#idCountry').change(function () {
//		alert($(this).val());
		 if ($(this).val() != 0) {
			 $.getJSON("../shift/regions",{idCountry :  $(this).val()},function(data){
				var option ='<option value="0" >-- SELECT AN OPTION --</option>';
				$.map(data, function(item) {
					option += '<option value="' + item.ID + '">' + item.NAME + '</option>';
				});
				$('#formAbroadJob').find('#idRegion').html(option);
			});
		 }
	});
	
	$('#formAbroadJob')
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
             'rhPerson.id': {
                 validators: {
                     selectCero: {
                         message: $msg_error_select
                     }
                 }
             },
             'sisCountry.id': {
                 validators: {
                     selectCero: {
                         message: $msg_error_select
                     }
                 }
             },
             'sisRegion.id': {
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
        
        $form.ajaxSubmit({
//        	dataType: 'json',
        	target : '#detailModalAbroadJob',
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
            	validateformAbroadJob();
            	updatePanelTable();
            	updatePanelTableH();
            	
            }
        });
    });
}
