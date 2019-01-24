package org.thradex.rrhh.service;

import java.util.List;
import java.util.Map;

import org.thradex.model.RhPerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhShiftDetail;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;

public interface ShiftDetailService {
	
	public RhShiftDetail get(int idRhShiftDetail);
	
	public List<RhShiftDetail> list(RhShift rhShift);
	
	public List<RhShiftDetail> list(RhType typeDetail, RhPerson rhPerson, RhShiftPeriod rhShiftPeriod);
	
	public RhShiftDetail save(RhShiftDetail rhShiftDetail);
	
	public void update(RhShiftDetail rhShiftDetail);
	
	public void process(RhShift rhShift, boolean takenBreak);
	
	public void processCero(RhShift rhShift);
	
	public RhShiftDetail proccessAbroadJob(RhShift rhShift);
	
	public List<RhShift> addDetail(List<RhShift> rhShifts);

	public Map<String, Object> getMapDetail(RhPerson rhPerson);
	
	public Integer getTotalTime(RhShift rhShift);
	
	public Integer getTotalProcessedTime(RhShift rhShift);
	
	public List<RhShiftDetail> list(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod);
	
	public List<RhShiftDetail> list(RhShiftPeriod rhShiftPeriod);
	
	public List<RhShiftDetail> listMng(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod);
	
	public List<RhPerson> addDetail(List<RhPerson> rhPersons, RhShiftPeriod rhShiftPeriod);
	
	public List<RhShift> addDetailChild(List<RhShift> rhShifts);
	
	public List<RhShiftDetail> list(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod, RhStatus rhStatusSis);
	
	public List<RhShiftDetail> listDayNull();
	
	public void delete(RhShift rhShift);
	
}
