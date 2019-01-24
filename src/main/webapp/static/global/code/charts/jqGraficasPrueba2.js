var chart1; // globally available
var data;
var xAxisData[];
var seriesData[];


$(document).ready(function() {
	data = $.getJSON("../dash/prueba", function(mapa){});
	
	chart1 = new Highcharts.Chart({
		chart: {
			renderTo: 'container',
            type: 'bar'
         },
         title: {
            text: 'Tickets Pendientes Resolucion'
         },
         xAxis: {
            categories: ['DELPHOS', 'WORKFLOW', 'OITS', 'OITS-CONSUL', 'ACCESOS']
        	//categories: []
         },
         yAxis: {
            title: {
               text: ''
            }
         },
         series: [{
            name: 'Tickets',
            data: [1, 9, 4, 7, 3],
            //data: [data],
            visible: true
         }]
      });
   });