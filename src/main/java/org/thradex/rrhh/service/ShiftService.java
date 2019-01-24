package org.thradex.rrhh.service;

import java.util.List;
import java.util.Map;

import org.thradex.model.RhFile;
import org.thradex.model.RhPerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhShiftDetail;
import org.thradex.model.RhShiftPeriod;

public interface ShiftService {
	
	public RhShift getRhShift(int idRhShift);
	
	public List<RhShift> listShiftActive(RhPerson rhPerson);
	
	public List<RhShift> listShiftPlanned(RhPerson rhPerson);
	public List<RhShift> listShiftClosed(RhPerson rhPerson,RhShiftPeriod rhPeriodo);
	public void shiftChildDelete(int rhShift) ;
	public void updateShiftRever(int rhShift);
	
	
	public List<RhShift> listShiftPending(RhPerson rhPerson);
	
	public List<RhShift> listShiftProcessed(RhPerson rhPerson);
	
	public RhFile getRhFile(int idRhFile);
	
	public Map<String, Object> getMapDetail(RhPerson rhPerson);
	
	public List<RhShiftDetail> listShitDetail(int option, RhPerson rhPerson);
	
//	public List<RhShift> listSession(RhPerson rhPerson);
	
	public void updateResponsible();
	
	public void updateTypeCharge();
	
	public void updateDayDetail();
	
	public void updateDetail();
	
	public void updateDetail2();
}
