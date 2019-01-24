package org.thradex.rrhh.service;


import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.thradex.model.RhPerson;
import org.thradex.model.RhSchedulePerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhShiftPeriod;

public interface ScheduleScaleService {
	
	public RhShift validRegisterScale(RhPerson rhPerson, String ip);
	
	public RhShift registerStartScale(RhPerson rhPerson, int idRhScheduleScale, String ip);
	public void registerStartScaleRUM(RhPerson rhPerson, int idRhShift, String dStart) throws ParseException;
	
	public RhShift registerFinishScale(int idRhScheduleScale, String ip);
	public void registerFinishScaleRUM(int idRhScale, String dEnd)  throws ParseException;
	
	public RhShift saveJustification(RhShift rhShift);
	
	public RhShift get(int idRhShift);
	
	public List<RhShift> listSession(RhPerson rhPerson);
	
	public RhShift processAlert(RhShift rhShift);
	
	public void createScale();
	
	public void checkActiveSessions();

	public void checkAbsentSessions();
	
	public void processAlertsLastPeriod(RhShiftPeriod rhShiftPeriod);
	
	public List<RhSchedulePerson> listRhSchedulePerson(RhPerson rhPerson);
	
	public void deleteScheduleScale(int idRhScheduleScale);

	public void createScheduleScale(int idRhPerson, int idRhScheduleDay, String startDate);
	
	public List<Map<String, Object>> listScheduleScale(RhPerson rhPerson, String from, String to);
	
	public List<Map<String, Object>> listScheduleScaleStaff(RhPerson rhPerson, String from, String to);
	
	public boolean isManager(RhPerson rhPerson);
}
