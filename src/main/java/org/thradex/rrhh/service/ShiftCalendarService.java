package org.thradex.rrhh.service;


import java.util.List;
import java.util.Map;

import org.thradex.model.RhPerson;
import org.thradex.model.RhScheduleScale;
import org.thradex.model.RhShift;
import org.thradex.model.RhType;

public interface ShiftCalendarService {
	
	public List<RhPerson> listRhPerson(RhPerson rhPerson);
	
	public void deleteScheduleScale(int idRhScheduleScale);

	public void createScheduleScale(int idRhPerson, int idRhScheduleDay, String startDate);
	
	public RhShift getRhShift(Integer idShift);
	
	public RhScheduleScale getRhScheduleScale(Integer idScale);
	
	public List<RhType> listEvents();
	
	public List<Map<String, Object>> listScheduleScale(RhPerson rhPerson, String from, String to);
	
	public List<Map<String, Object>> listScheduleScale(RhPerson rhPerson, Integer idPerson, Integer idEvent, String from, String to);
}
