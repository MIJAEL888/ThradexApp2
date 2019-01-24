package org.thradex.rrhh.service;

import java.util.List;

import org.thradex.model.RhPerson;
import org.thradex.model.RhShift;

public interface ScheduleJobService {
	
	public RhShift registerStart(RhPerson rhPerson, int idRhShift, String ip);
	
	public RhShift registerFinish(int idRhShift, String ip);
	
	public RhShift validRegister(RhPerson rhPerson, String ip);
	
	public RhShift save(RhPerson rhPerson,  RhShift rhShift);
	
	public List<RhShift> listSession(RhPerson rhPerson);
	
}
