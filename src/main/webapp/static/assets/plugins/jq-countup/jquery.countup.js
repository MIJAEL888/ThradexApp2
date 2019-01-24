
(function( $ ) {
 
    $.fn.countup = function(options ) {
 
    	// Bob's default settings:
        var defaults = {
            startDate: new Date(),
            nowDate: new Date(),
            clock: false,
        };
     
        var settings = $.extend( {}, defaults, options );
     
        return this.each(function(index, element) {
        	if(settings.clock){
//        		alert("clock true" )
        		var newDate = settings.nowDate;
        		newDate.setDate(newDate.getDate()); 
        		var startDate = settings.startDate;
        		startDate.setDate(startDate.getDate());      
        		var cseconds =  Math.floor(((newDate - startDate) % 60000 ) / 1000);
        		var cminutes = Math.floor(((newDate - startDate) % 3600000)/ 60000);
        		var chours =  Math.floor((newDate - startDate) / 3600000);
        		
        		setInterval( function() {
        			$(element).find("#cmin").html(( cminutes < 10 ? "0" : "" ) + cminutes);
        			$(element).find("#chours").html(( chours < 10 ? "0" : "" ) + chours);
        			cseconds++;
//        			 window.console.log("seconds" + cseconds);
        			if (cseconds == 60) {
        				cseconds = 0;
        				cminutes++;
        				if (cminutes == 60) {
        					cminutes = 0;
        					chours++;
        				}
        			}
        		},1000);
        	}else{
        		var newDate = settings.nowDate;
            	newDate.setDate(newDate.getDate());      
            	var seconds = newDate.getSeconds();
            	var minutes = newDate.getMinutes();
            	var hours = newDate.getHours();

            	setInterval( function() {
            		
            		$(element).find("#sec").html(( seconds < 10 ? "0" : "" ) + seconds);
            		$(element).find("#min").html(( minutes < 10 ? "0" : "" ) + minutes);
            		$(element).find("#hours").html(( hours < 10 ? "0" : "" ) + hours);
//            		$(this).find('#Date').html(dayNames[newDate.getDay()] + " " + newDate.getDate() + ' ' + monthNames[newDate.getMonth()] + ' ' + newDate.getFullYear());
            		seconds++;
//            		 window.console.log("seconds" + seconds);
            		if (seconds == 60) {
//            			alert("1 minute");
            			seconds = 0;
            			minutes++;
            			if (minutes == 60) {
            				minutes = 0;
            				hours++;
            				if (hours == 24) {
            					hours= 0;
            					newDate.setDate(newDate.getDate() + 1);
            				}
            			}
            		}
            	},1000);
        	}
        });
    };
 
}( jQuery ));


