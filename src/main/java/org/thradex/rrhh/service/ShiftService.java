package org.thradex.rrhh.service;

import java.util.List;
import java.util.Map;

import org.thradex.model.RhFile;
import org.thradex.model.RhPerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhShiftDetail;
import org.thradex.model.RhShiftPeriod;

public interface ShiftService {
	
	RhShift getRhShift(int idRhShift);
	
	List<RhShift> listShiftActive(RhPerson rhPerson);
	
	List<RhShift> listShiftPlanned(RhPerson rhPerson);
	List<RhShift> listShiftClosed(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod);
	void shiftChildDelete(int rhShift) ;
	void updateShiftRever(int rhShift);
	
	
	List<RhShift> listShiftPending(RhPerson rhPerson);
	
	List<RhShift> listShiftProcessed(RhPerson rhPerson);
	List<RhShift> listShiftProcessed(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod);

	RhFile getRhFile(int idRhFile);
	
	Map<String, Object> getMapDetail(RhPerson rhPerson);
	
	List<RhShiftDetail> listShitDetail(int option, RhPerson rhPerson);
	
//	List<RhShift> listSession(RhPerson rhPerson);
	
	void updateResponsible();
	
	void updateTypeCharge();
	
	void updateDayDetail();
	
	void updateDetail();
	
	void updateDetail2();
}
