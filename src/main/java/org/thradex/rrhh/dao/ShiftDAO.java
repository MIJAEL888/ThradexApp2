package org.thradex.rrhh.dao;

import java.util.Date;
import java.util.List;

import org.thradex.model.EvenUsuario;
import org.thradex.model.RhCompany;
import org.thradex.model.RhPerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;
import org.thradex.model.ValorCosto;

public interface ShiftDAO {
	
	public RhShift saveRhShift(RhShift rhShift);
	
	public void updateRhShift(RhShift rhShift);
	
	public RhShift get( RhPerson rhPerson, RhStatus rhStatus, RhType rhType, Date date);
	
	public RhShift getRhShift(RhPerson rhPerson, RhStatus rhStatus, RhType rhType);
	
	public RhShift getRhShift(RhPerson rhPerson,RhStatus rhStatus, RhType rhType, Date date);
	
	public RhShift getRhShift();
	
	public RhShift getRhShift(int idShift);
	
	public List<RhShift> listMng( RhPerson rhPerson, RhStatus rhStatus, RhType rhType);
	
	public List<RhShift> listMng(RhPerson rhPerson, RhStatus rhStatus);
	
	public List<RhShift> list(RhPerson rhPerson, RhStatus rhStatus,  RhType rhType);
	
	public List<RhShift> list(RhPerson rhPerson, RhStatus rhStatus);
	
	public List<RhShift> list(RhPerson rhPerson, RhStatus rhStatus, Date startDate);
	
	public List<RhShift> list(RhPerson rhPerson, RhStatus rhStatus, RhShiftPeriod rhShiftPeriod);
	
	public List<RhShift> listMng(RhPerson rhPerson, RhStatus rhStatus, RhShiftPeriod rhShiftPeriod);
	
	public List<RhShift> listMng(RhPerson rhPerson, RhStatus rhStatus, Date startDate);
	
	public List<RhShift> list(RhPerson rhPerson, RhType rhType, RhStatus rhStatus,  Date startDate);
	
	public List<RhShift> list(RhStatus rhStatus,  RhType rhType);
	
	public List<RhShift> list(RhStatus rhStatusSis, RhStatus rhStatus,  RhType rhType);
	
	public List<RhShift> listBySis(RhPerson rhPerson, RhStatus rhStatusSis,  RhType rhType);
	
	public List<RhShift> listMngBySis( RhPerson rhPerson, RhStatus rhStatusSis, RhType rhType);
	
	public List<RhShift> listBySis(RhPerson rhPerson, RhType rhType, RhStatus rhStatusSis,  Date startDate);
	
	public List<RhShift> list(RhShift rhShiftParent);
	
	public List<RhShift> list(RhStatus rhStatusSis, RhShiftPeriod rhShiftPeriod);
	
	public List<RhShift> list(RhStatus rhStatusSis, RhShiftPeriod rhShiftPeriod, RhPerson rhPerson);
	
	public List<RhShift> list(RhPerson rhPerson, RhStatus rhStatus,  RhType rhType, RhShiftPeriod rhShiftPeriod);
	
	public List<RhShift> listMng(RhPerson rhPerson, RhStatus rhStatus,  RhType rhType, RhShiftPeriod rhShiftPeriod);
	
	public List<RhShift> listBySis(RhPerson rhPerson, RhStatus rhStatusSis,  RhType rhType, RhShiftPeriod rhShiftPeriod);
	
	public List<RhShift> listMngBySis( RhPerson rhPerson, RhStatus rhStatusSis, RhType rhType, RhShiftPeriod rhShiftPeriod);
	
	public List<RhShift> list(RhStatus rhStatus,  RhType rhType, RhShiftPeriod rhShiftPeriod);
	
	public List<RhShift> listBySis(RhStatus rhStatusSis,  RhType rhType, RhShiftPeriod rhShiftPeriod);
	
	public List<RhShift> listBySis(RhPerson rhPerson, RhStatus rhStatusSis,  RhShiftPeriod rhShiftPeriod);
	
	public List<RhShift> listBySisAll(RhPerson rhPerson, RhStatus rhStatusSis,  RhShiftPeriod rhShiftPeriod);
	
	public List<RhShift> listBySis(RhStatus rhStatusSis, RhShiftPeriod rhShiftPeriod);
	
	public List<RhShift> listBySisProcess(RhPerson rhPerson, RhStatus rhStatusSis,  RhShiftPeriod rhShiftPeriod);
	
	public List<RhShift> listMngBySis(RhPerson rhPerson, RhStatus rhStatusSis,  RhShiftPeriod rhShiftPeriod);
	
	public List<RhShift> listBySis(RhPerson rhPerson, RhStatus rhStatusSis);
	
	public List<RhShift> listBySisAll(RhPerson rhPerson, RhStatus rhStatusSis);
//	public List<RhShift> lsShiftxPerson(RhPerson rhPerson, RhStatus rhStatusSis);
	
	public List<RhShift> listResponsibleNull(RhStatus rhStatusSis);
	
	public List<RhShift> listTypeChargeNull();
	
	public List<RhShift> listNotProcessed(RhStatus rhStatusSis);
	
	public List<RhShift> listNotProcessed2(RhStatus rhStatusSis);
	
	public List<RhShift> listBySis(RhStatus rhStatusSis, RhCompany rhCompany);
	
	public List<EvenUsuario> listEvenUsuario();

	public List<ValorCosto> lisValorCosto();
	
	public List<RhShift> listBySis2(RhPerson rhPerson, RhStatus rhStatusSis);
	
	public List<RhShift> listBySis2(RhPerson rhPerson, RhStatus rhStatusSis, RhStatus rhStatus);
	public List<RhShift> listBySisClosed(RhPerson rhPerson, RhStatus rhStatusSis, RhStatus rhStatus ,RhShiftPeriod rhPeriodo);
	public void shiftChildDelete(RhShift rhShift ,int idRhShift) ;
	
	
	public List<RhShift> listBySis(RhStatus rhStatusSis, RhStatus rhStatus, RhCompany rhCompany);
	
	public List<RhShift> list(RhPerson rhPerson, Date dateStart, Date dateFinish);
	
}
