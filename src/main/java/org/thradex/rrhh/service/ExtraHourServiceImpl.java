package org.thradex.rrhh.service;

import org.springframework.transaction.annotation.Transactional;
import org.thradex.model.RhPerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhShiftPeriod;
import org.thradex.rrhh.dao.FileDAO;
import org.thradex.rrhh.dao.ShiftDAO;
import org.thradex.rrhh.dao.StatusDAO;
import org.thradex.rrhh.dao.TypeDAO;
import org.thradex.util.ConstantsSis;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ExtraHourServiceImpl implements ExtraHourService {
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);

//	private RhType typeExtraHour;
	
	@Autowired
	private UtilRrhhService utilService;
	
	@Autowired
	private ShiftDAO shiftDAO;
	
	@Autowired
	private StatusDAO statusDAO;
	
	@Autowired
	private TypeDAO typeDAO;
	
	@Autowired
	private ShiftPeriodService shiftPeriodService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private ShiftDetailService shiftDetailService;
	
	@Autowired
	private ShiftDetailService2 shiftDetailService2;
	
	@Autowired
	private FileDAO fileDAO;
	
	@Autowired
	private MailRhService mailRhService;
	
	
	
	public RhShift get(int id) {
		RhShift rhShift = shiftDAO.getRhShift(id);
		rhShift.setRhFiles(fileDAO.getRhFile(rhShift));
		return rhShift;
	}
	public List<RhShift> listPending(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod){
		return shiftDAO.listMng(rhPerson,  statusDAO.getRhStatus("SH_EXTRAH", 1), typeDAO.getRhType("SHIFT", 4), rhShiftPeriod); 
	}
	
	
	public RhShift save(RhShift rhShift, RhPerson rhPerson) {
		Date now = utilService.getNowDate(rhPerson);
		
		int gapMin = utilService.getMinutes(rhShift.getDateStart(), rhShift.getDateFinish());
		log.info("Reason : " + rhShift.getReason());
		rhShift.setDuration(gapMin);
		rhShift.setDateReg(now);
		rhShift.setRhPerson(rhPerson);
		rhShift.setRhStatus(statusDAO.getRhStatus("SH_EXTRAH", 1)); //PENDING
		rhShift.setRhPersonMng(personService.getManager(rhPerson));
		rhShift.setRhType(typeDAO.getRhType("SHIFT", 4));
		rhShift.setRhShiftPeriod(shiftPeriodService.get(rhShift.getDateStart(), 
				rhPerson.getRhCompany()));
		rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1)); //PENDING
		rhShift.setRhPersonResp(personService.getManager(rhPerson));
		rhShift = shiftDAO.saveRhShift(rhShift);
		utilService.saveFiles(rhShift);
		mailRhService.sendMailRequest(rhShift);
		return rhShift;
	}

	
	public RhShift update(RhShift rhShift) {
		Date now = utilService.getNowDate(rhShift.getRhPerson());
		rhShift.setRhPersonResp(null);
		if (rhShift.isApproved()) {//APPROVED
			rhShift.setDateStartShift(rhShift.getDateStart());
			rhShift.setDateFinishShift(rhShift.getDateFinish());
			
			if (rhShift.isToPay()){//to  pay
				rhShift.setRhTypeCharge(typeDAO.getRhType("SHIFT_DETAIL", 5));
			}else {// compensate
				rhShift.setRhTypeCharge(typeDAO.getRhType("SHIFT_DETAIL", 3));
			}
			rhShift.setRhStatus(statusDAO.getRhStatus("SH_EXTRAH", 2)); //APPROVED
			rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
			rhShift.setDateProcess(now);
			rhShift.setRhShiftPeriod(shiftPeriodService.getLimit(now, rhShift.getRhPerson().getRhCompany()));
			shiftDAO.updateRhShift(rhShift);
			
			shiftDetailService.process(rhShift, false);
			shiftDetailService2.process(rhShift, false);
		}
		else{//DISAPPROVED
			rhShift.setRhTypeCharge(typeDAO.getRhType("SHIFT_DETAIL", 1));
			rhShift.setRhStatus(statusDAO.getRhStatus("SH_EXTRAH", 3));
			rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
			rhShift.setDateProcess(now);
			//no process any bcs
			shiftDAO.updateRhShift(rhShift);
			shiftDetailService.processCero(rhShift);
			shiftDetailService2.processCero(rhShift);
		}
		mailRhService.sendMailProcess(rhShift);
		return rhShift;
	}

}