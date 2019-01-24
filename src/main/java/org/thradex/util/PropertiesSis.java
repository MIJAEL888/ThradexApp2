package org.thradex.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties") 
public class PropertiesSis {
	
	public static String PATH_SHIFT; // "/opt/ThradexApp/shift/"; //D:\\tmp\\
	public static String PATH_SHIFT_REPORT;
	
	@Autowired
	public PropertiesSis(@Value("${path.shift}") String pathShift,
						 @Value("${path.shift.report}") String pathShiftReport){
		PropertiesSis.PATH_SHIFT =  pathShift;
		PropertiesSis.PATH_SHIFT_REPORT =  pathShiftReport;
	}
	
}
