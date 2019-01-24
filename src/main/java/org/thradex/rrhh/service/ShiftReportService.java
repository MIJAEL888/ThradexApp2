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
	
	public List<RhCompany> listRhCompanies();
	
	public List<Map<String, Object>> listMapRhShiftPeriods(int idRhCompany);
	
	public List<Map<String, Object>> listMapRhPerson(int idRhCompany);
	
	public List<RhShift> listPending(Integer idPerido, Integer idPerson, RhPerson rhPersonMng);
	
	public List<RhShift> listProcessed( Integer idPerido, Integer idPerson, RhPerson rhPersonMng);
	
	public List<RhShiftDetail> listShiftDetail(Integer idPeriod, Integer idPerson, RhPerson rhPersonMng);
	
	public List<RhPerson> listRhPerson(RhPerson rhPerson);
	
	public List<RhShiftPeriod> listRhShiftPeriods(int idRhCompany);
	
	public List<RhPerson> listRhPerson(int idRhCompany);

	public List<RhShift> listAllActive(RhPerson rhPerson);
	
	public List<RhShift> listAllPlanned(RhPerson rhPerson);
	
	public List<RhPerson> listShiftSummary(Integer idPeriod, RhPerson rhPersonMng);
	
	public void exportReportShift(int idRhPeriod);
	
	public void exportReportShiftDetail(int idRhPeriod);
	
	public void exportReportShiftDetailOne(int idRhPeriod);
	
	public void exportReportShiftDetailOne2(int idRhPeriod);
	
	public void exportReportShift2(int idRhPeriod);
	
	public List<EvenUsuario> listEvenUsuario();
	
	public List<ValorCosto> lisValorCosto();
}
