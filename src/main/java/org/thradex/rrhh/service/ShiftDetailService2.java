package org.thradex.rrhh.service;

import java.util.List;
import java.util.Map;

import org.thradex.model.RhPerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhShiftDetail2;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;

public interface ShiftDetailService2 {
	
	public RhShiftDetail2 get(int idRhShiftDetail2);
	
	public List<RhShiftDetail2> list(RhShift rhShift);
	
	public List<RhShiftDetail2> list(RhType typeDetail, RhPerson rhPerson, RhShiftPeriod rhShiftPeriod);
	
	public RhShiftDetail2 save(RhShiftDetail2 RhShiftDetail2);
	
	public void update(RhShiftDetail2 RhShiftDetail2);
	
	public void process(RhShift rhShift, boolean takenBreak);
	
	public void processCero(RhShift rhShift);
	
	public RhShiftDetail2 proccessAbroadJob(RhShift rhShift);
	
	public List<RhShift> addDetail(List<RhShift> rhShifts);

	public Map<String, Object> getMapDetail(RhPerson rhPerson);
	
	public Integer getTotalTime(RhShift rhShift);
	
	public Integer getTotalProcessedTime(RhShift rhShift);
	
	public List<RhShiftDetail2> list(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod);
	
	public List<RhShiftDetail2> list(RhShiftPeriod rhShiftPeriod);
	
	public List<RhShiftDetail2> listMng(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod);
	
	public List<RhPerson> addDetail(List<RhPerson> rhPersons, RhShiftPeriod rhShiftPeriod);
	
	public List<RhShift> addDetailChild(List<RhShift> rhShifts);
	
	public List<RhShiftDetail2> list(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod, RhStatus rhStatusSis);
	
	public List<RhShiftDetail2> listDayNull();
	
	public void delete(RhShift rhShift);
	
}
