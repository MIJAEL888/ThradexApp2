package org.thradex.util;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

public class TestVoid {

	public static void main(String[] args){
		DateTime dateStart = new DateTime();
		DateTime dateFnish = dateStart.plusHours(2);
		
		System.out.println("diff min1: " + Minutes.minutesBetween(dateStart, dateFnish).getMinutes());
		System.out.println("diff min2: " + Minutes.minutesBetween(dateFnish, dateStart).getMinutes());
		int total =  480;
		int totalRate = 0;

		
		totalRate = (int) (0.2 * total * (60.0/52.5));
		
		System.out.println("total : " + totalRate);
	}
}
