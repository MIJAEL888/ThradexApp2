package org.thradex.rrhh.service;

import java.util.List;
import java.util.Map;

import org.thradex.model.EvenUsuario;
import org.thradex.model.RhCompany;
import org.thradex.model.RhPerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhShiftDetail;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.ValorCosto;

public interface ShiftReportService {
	
	List<RhCompany> listRhCompanies();
	
	List<Map<String, Object>> listMapRhShiftPeriods(int idRhCompany);
	
	List<Map<String, Object>> listMapRhPerson(int idRhCompany);
	
	List<RhShift> listPending(Integer idPerido, Integer idPerson, RhPerson rhPersonMng);
	
	List<RhShift> listProcessed( Integer idPerido, Integer idPerson, RhPerson rhPersonMng);
	
	List<RhShiftDetail> listShiftDetail(Integer idPeriod, Integer idPerson, RhPerson rhPersonMng);
	
	List<RhPerson> listRhPerson(RhPerson rhPerson);
	
	List<RhShiftPeriod> listRhShiftPeriods(int idRhCompany);
	
	List<RhPerson> listRhPerson(int idRhCompany);

	List<RhShift> listAllActive(RhPerson rhPerson);
	
	List<RhShift> listAllPlanned(RhPerson rhPerson);
	
	List<RhPerson> listShiftSummary(Integer idPeriod, RhPerson rhPersonMng);
	
	void exportReportShift(int idRhPeriod);
	
	void exportReportShiftDetail(int idRhPeriod);
	
	void exportReportShiftDetailOne(int idRhPeriod);
	
	void exportReportShiftDetailOne2(int idRhPeriod);
	
	void exportReportShift2(int idRhPeriod);
	
	List<EvenUsuario> listEvenUsuario();
	
	List<ValorCosto> lisValorCosto();
}
