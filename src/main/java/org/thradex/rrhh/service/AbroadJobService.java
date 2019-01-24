package org.thradex.rrhh.service;

import org.thradex.model.RhPerson;
import org.thradex.model.RhSchedulePerson;
import org.thradex.model.RhShift;
import org.thradex.model.SisCountry;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AbroadJobService {
	
	public RhShift get(int id);
	
//	public List<RhShift> list(int type, RhPerson rhPerson);
	
//	public List<RhShift> listReport(int option, RhPerson rhPerson, RhShiftPeriod rhShiftPeriod);
	
	public RhShift save(RhShift rhShift, RhPerson rhPerson);
	
//	public RhShift update(RhShift rhShift);
	
	public List<SisCountry> listCountry();
	
	public List<Map<String, Object>> listRegion(int idCountry);
	
	public void checkFinish();
	
	public void process(RhShift rhAbroadJob, Date start, Date finish, RhSchedulePerson rhSchedulePerson);
}
