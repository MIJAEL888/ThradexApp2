package org.thradex.rrhh.dao;

import java.util.Date;
import java.util.List;

import org.thradex.model.RhSchedule;
import org.thradex.model.RhScheduleDay;

public interface ScheduleDayDAO {
	
	public RhScheduleDay get(int id);
	
	public RhScheduleDay get(Date date, RhSchedule rhSchedule);
	
	public List<RhScheduleDay> list(RhSchedule rhSchedule);
	
}
