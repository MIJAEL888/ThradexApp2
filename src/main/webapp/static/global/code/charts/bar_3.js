$(function () {
    var chart;
    $(document).ready(function() {
       // chart = new Highcharts.Chart({
    	var options = {
            chart: {
                renderTo: 'container2',
                type: 'column'
            },
            title: {
                text: 'Promedio'
            },
            subtitle: {
                text: ''
            },
            xAxis: {
                categories: [
                    'Energia',
                    'Ing. Trafico',
                    'Ing. Tx',
                    'ISP',
                    'NOC',
                    'Planta Externa',
                    'Radio Enlace',
                    'SAC',
                    'TI',
                    'WIMAX'
                ]
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'TICKETS'
                }
            },
            legend: {
                layout: 'vertical',
                backgroundColor: '#000000',
                align: 'left',
                verticalAlign: 'top',
                x: 100,
                y: 0,
                floating: true,
                shadow: true
            },
            tooltip: {
                formatter: function() {
                    return ''+
                        this.x +': '+ this.y +' Tickets';
                }
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },series: [{
                name: 'Equipos',
                data: []
    
            }, {
                name: 'Carrier',
                data: []
    
            }, {
                name: 'Telefonia',
                data: []
    
            }, {
                name: 'Acceso',
                data: []
    
            }]/*
                series: [{
                name: 'Equipos',
                data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4]
    
            }, {
                name: 'Carrier',
                data: [83.6, 78.8, 98.5, 93.4, 106.0, 84.5, 105.0, 104.3, 91.2]
    
            }, {
                name: 'Telefonia',
                data: [48.9, 38.8, 39.3, 41.4, 47.0, 48.3, 59.0, 59.6, 52.4]
    
            }, {
                name: 'Accesos',
                data: [42.4, 33.2, 34.5, 39.7, 52.6, 75.5, 57.4, 60.4, 47.6]
    
            }]
            
            series: [{
                name: 'Equipos',
                data: [49.9]
    
            }, {
                name: 'Carrier',
                data: [83.6]
    
            }, {
                name: 'Telefonia',
                data: [48.9]
    
            }, {
                name: 'Accesos',
                data: [42.4]
    
            }]
            */
        };
    	$.getJSON("../dash/areas", function(data){
    		$.each(data, function(index, item){
    			//CARGANDO DATOS DE SERIE Y CATEGORIAS (SERIES DEBE SER ENTEROS)
    			//options.xAxis.categories.push(item.nombre);
    			//alert("energia:"+parseInt(item.Energia));
    			//alert("sdfdf");
    			//alert("valor:"+options.series[0].data);
    			options.series[index].data.push(parseInt(item.Energia));
    			options.series[index].data.push(parseInt(item.Trafico));
    			options.series[index].data.push(parseInt(item.Transporte));
    			options.series[index].data.push(parseInt(item.ISP));
    			options.series[index].data.push(parseInt(item.NOC));
    			options.series[index].data.push(parseInt(item.Planta));
    			options.series[index].data.push(parseInt(item.Radio));
    			options.series[index].data.push(parseInt(item.SAC));
    			options.series[index].data.push(parseInt(item.TI));
    			options.series[index].data.push(parseInt(item.WIMAX));
    			
    			
    		});
    		chart = new Highcharts.Chart(options);
    	});
    });
   
    
    
});