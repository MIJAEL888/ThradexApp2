package org.thradex.rrhh.service;

import java.util.Date;

import org.thradex.model.RhPerson;
import org.thradex.model.RhSchedulePerson;
import org.thradex.model.RhShift;

public interface UtilRrhhService {
	
	public Date getNowDate();
	
	public Date getNowDate(RhPerson rhPerson);
	
	public Date getNowDate(RhSchedulePerson rhSchedulePerson);
	
	public boolean validIp(String ip);
	
	public int getMinutes(Date startDate, Date finishDate);
	
	public int getHours(Date startDate, Date finishDate);
	
	public RhShift saveFiles(RhShift rhShift);
	
	public String getStringDay(Date startDate, Date finishDate);
	
	public String getStringHours(Integer minutes);
}
