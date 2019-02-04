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
	
	RhShiftDetail get(int idRhShiftDetail);
	
	List<RhShiftDetail> list(RhShift rhShift);
	
	List<RhShiftDetail> list(RhType typeDetail, RhPerson rhPerson, RhShiftPeriod rhShiftPeriod);
	
	RhShiftDetail save(RhShiftDetail rhShiftDetail);
	
	void update(RhShiftDetail rhShiftDetail);
	
	void process(RhShift rhShift, boolean takenBreak);
	
	void processCero(RhShift rhShift);
	
	RhShiftDetail proccessAbroadJob(RhShift rhShift);
	
	List<RhShift> addDetail(List<RhShift> rhShifts);

	Map<String, Object> getMapDetail(RhPerson rhPerson);
	
	Integer getTotalTime(RhShift rhShift);
	
	Integer getTotalProcessedTime(RhShift rhShift);
	
	List<RhShiftDetail> list(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod);
	
	List<RhShiftDetail> list(RhShiftPeriod rhShiftPeriod);
	
	List<RhShiftDetail> listMng(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod);
	
	List<RhPerson> addDetail(List<RhPerson> rhPersons, RhShiftPeriod rhShiftPeriod);
	
	List<RhShift> addDetailChild(List<RhShift> rhShifts);
	
	List<RhShiftDetail> list(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod, RhStatus rhStatusSis);
	
	List<RhShiftDetail> listDayNull();
	
	void delete(RhShift rhShift);
	
}
