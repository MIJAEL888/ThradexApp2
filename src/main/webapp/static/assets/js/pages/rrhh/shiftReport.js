$(document).ready(function(){
	
	formatTableShift('#datatablesPending');
	formatTableShift('#datatablesProcessed');
	formatTableShift2('#datatablesSession');
	formatTableShift2('#datatablesActive');
	formatTableShift2('#datatablesPlanned');
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
      $($.fn.dataTable.tables(true)).DataTable()
      .columns.adjust()
      .responsive.recalc();
   });  
	
	$('#formShiftReportRh').find('#idRhCompany').change(function () {
		 if ($(this).val() != 0) {
			 $.getJSON("../shiftReport/period",{idCompany :  $(this).val()},function(data){
				var option ='<option value="0" >-- SELECT AN OPTION --</option>';
				$.map(data, function(item) {
					option += '<option value="' + item.id + '">' + item.label + '</option>';
				});
				$('#formShiftReportRh').find('#idRhShiftPeriod').html(option);
			});
			 
			 $.getJSON("../shiftReport/person",{idCompany :  $(this).val()},function(data){
				var option ='<option value="0" >-- SELECT AN OPTION --</option>';
				$.map(data, function(item) {
					option += '<option value="' + item.id + '">' + item.label + '</option>';
				});
				$('#formShiftReportRh').find('#idRhPerson').html(option);
			});
		 }
		 
	});
	
	$('#formShiftReportRh').find('#idRhShiftPeriod').change(function () {
		 if ($(this).val() != 0) {
			 var idPeriod = $(this).val();
			 $('#formShiftReportRh').find('#idRhPerson').val(0);
			 updatePanelPending(idPeriod, 0);
			 updatePanelProcessed(idPeriod, 0);
			 updatePanelDetail(idPeriod, 0);
			 updatePanelSummary(idPeriod);
//btn-primary 
			 $('.export-rh').prop('disabled', false);
			 $('.export-rh').removeClass('btn-default');
			 $('.export-rh').addClass('btn-primary');
		 }
	});
	
	$('#formShiftReportRh').find('#idRhPerson').change(function () {
		 var idPeriod = $('#formShiftReportRh').find('#idRhShiftPeriod').val();
		 if(idPeriod == 0){
			 $(this).val(0);
			 alert("Please select first a period.");
		 }else if ($(this).val() != 0) {
			 var idPerson = $(this).val();
			 updatePanelPending(idPeriod, idPerson);
			 updatePanelProcessed(idPeriod, idPerson);
			 updatePanelDetail(idPeriod, idPerson);
		 }
	});
	
	$('#formShiftReport').find('#idRhShiftPeriod').change(function () {
		 if ($(this).val() != 0) {
			 var idPeriod = $(this).val();
			 $('#formShiftReport').find('#idRhPerson').val(0);
			 updatePanelPendingMng(idPeriod, 0);
			 updatePanelProcessedMng(idPeriod, 0);
			 updatePanelDetailMng(idPeriod, 0);
			 updatePanelSummaryMng(idPeriod);
		 }
	});
	
	$('#formShiftReport').find('#idRhPerson').change(function () {
		 var idPeriod = $('#formShiftReport').find('#idRhShiftPeriod').val();
		 if(idPeriod == 0){
			 $(this).val(0);
			 alert("Please select first a period.");
		 }else if ($(this).val() != 0) {
			 var idPerson = $(this).val();
			 updatePanelPendingMng(idPeriod, idPerson);
			 updatePanelProcessedMng(idPeriod, idPerson);
			 updatePanelDetailMng(idPeriod, idPerson);
			 $('.export-mng').prop('disabled', false);
		 }
	});
});

function exportRerportShiftRh(option){
	 var idPeriod = $('#formShiftReportRh').find('#idRhShiftPeriod').val();
	 if(idPeriod != 0){
		 if(option == 1){
			 exportRerportShiftDetail(idPeriod);
		 }else if(option == 2){
			 exportRerportShiftDetailOne(idPeriod);
		 }else if(option == 3){
			 exportRerportShift2(idPeriod);
		 }else{
			 exportRerportShift(idPeriod);
		 }
		 
	 }else{
		 alert("Please select first a period and person.");
	 }
}

function exportRerportShiftMng(option){
	 var idPeriod = $('#formShiftReport').find('#idRhShiftPeriod').val();
	 if(idPeriod != 0 ){
		 if(option == 1){
			 exportRerportShiftDetail(idPeriod);
		 }else if(option == 2){
			 exportRerportShiftDetailOne(idPeriod);
		 }else if(option == 3){
			 exportRerportShift2(idPeriod);
		 }else{
			 exportRerportShift(idPeriod);
		 }
	 }else{
		 alert("Please select first a period and person.");
	 }
}

function exportRerportShift(idPeriod){
	window.location="../shiftReport/exportReport?idPeriod=" + idPeriod;
}
function exportRerportShiftDetail(idPeriod){
	window.location="../shiftReport/exportReportDetail?idPeriod=" + idPeriod;
}

function exportRerportShiftDetailOne(idPeriod){
	window.location="../shiftReport/exportReportDetailOne?idPeriod=" + idPeriod;
}
function exportRerportShift2(idPeriod){
	window.location="../shiftReport/exportReportPE?idPeriod=" + idPeriod;
}

function updatePanelPending(idPeriod, idPerson){
//	alert("update session");
	blockUI('#divPanelPendig');
	$.get( "../shiftReport/updatePending", {idPeriod: idPeriod, idPerson: idPerson }, function( data ) {
		$('#divPanelPendig').html(data);
		formatTableShift('#datatablesPending');
		unblockUI('#divPanelPendig');
	});
}
function updatePanelProcessed(idPeriod, idPerson){
//	alert("update session");
	blockUI('#divPanelProcessed');
	$.get( "../shiftReport/updateProcessed", {idPeriod: idPeriod, idPerson: idPerson }, function( data ) {
		$('#divPanelProcessed').html(data);
		formatTableShift('#datatablesProcessed');
		unblockUI('#divPanelProcessed');
	});
}
function updatePanelDetail(idPeriod, idPerson){
//	alert("update session");
	blockUI('#divPanelDetail');
	$.get( "../shiftReport/updateDetail", {idPeriod: idPeriod, idPerson: idPerson }, function( data ) {
		$('#divPanelDetail').html(data);
		formatTableShift('#datatablesDetailed');
		unblockUI('#divPanelDetail');
	});
}

function updatePanelDetailMng(idPeriod, idPerson){
//	alert("update session");
	blockUI('#divPanelDetail');
	$.get( "../shiftReport/updateDetailMng", {idPeriod: idPeriod, idPerson: idPerson }, function( data ) {
		$('#divPanelDetail').html(data);
		formatTableShift('#datatablesDetailed');
		unblockUI('#divPanelDetail');
	});
}

function updatePanelSummary(idPeriod){
//	alert("update session");
	blockUI('#divPanelSummary');
	$.get( "../shiftReport/updateSummary", {idPeriod: idPeriod}, function( data ) {
		$('#divPanelSummary').html(data);
//		formatTableShift('#datatablesDetailed');
		unblockUI('#divPanelSummary');
	});
}

function updatePanelSummaryMng(idPeriod){
//	alert("update session");
	blockUI('#divPanelSummary');
	$.get( "../shiftReport/updateSummaryMng", {idPeriod: idPeriod}, function( data ) {
		$('#divPanelSummary').html(data);
//		formatTableShift('#datatablesSummary');
		unblockUI('#divPanelSummary');
	});
}

function updatePanelPendingMng(idPeriod, idPerson){
//	alert("update session");
	blockUI('#divPanelPendig');
	$.get( "../shiftReport/updatePendingMng", {idPeriod: idPeriod, idPerson: idPerson }, function( data ) {
		$('#divPanelPendig').html(data);
		formatTableShift('#datatablesPending');
		unblockUI('#divPanelPendig');
	});
}
function updatePanelProcessedMng(idPeriod, idPerson){
//	alert("update session");
	blockUI('#divPanelProcessed');
	$.get( "../shiftReport/updateProcessedMng", {idPeriod: idPeriod, idPerson: idPerson }, function( data ) {
		$('#divPanelProcessed').html(data);
		formatTableShift('#datatablesProcessed');
		unblockUI('#divPanelProcessed');
	});
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
        paging:         false,
        "bFilter": false,
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
