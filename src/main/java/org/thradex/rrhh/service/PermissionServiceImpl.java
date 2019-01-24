package org.thradex.rrhh.service;

import org.springframework.transaction.annotation.Transactional;
import org.thradex.model.RhPerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhType;
import org.thradex.rrhh.dao.ShiftDAO;
import org.thradex.rrhh.dao.ShiftPerioDAO;
import org.thradex.rrhh.dao.StatusDAO;
import org.thradex.rrhh.dao.TypeDAO;
import org.thradex.util.ConstantsSis;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);
	
	@Autowired
	private UtilRrhhService utilService;
	
	@Autowired
	private ShiftDAO shiftDAO;
	
	@Autowired
	private StatusDAO statusDAO;
	
	@Autowired
	private TypeDAO typeDAO;
	
	@Autowired
	private ShiftPerioDAO shiftPerioDAO;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private ShiftDetailService shiftDetailService;
	
	@Autowired
	private ShiftDetailService2 shiftDetailService2;
	
	@Autowired
	private MailRhService mailRhService;
	
	
	public List<RhType> listPermission(){
		return typeDAO.getRhType("PERMISSION");
	}
	
	
	public RhType getTypePermission(int idType){
		return typeDAO.getRhType(idType);
	}
	
	
	
	public RhShift save(RhShift rhShift, RhPerson rhPerson) {
		Date now = utilService.getNowDate(rhPerson);
		DateTime dateTimeStart = new DateTime(rhShift.getDateStart());
		DateTime dateTimeFinish = dateTimeStart.plusHours(rhShift.getDurationHour());
		rhShift.setDateFinish(dateTimeFinish.toDate());
		
		int gapMin = utilService.getMinutes(rhShift.getDateStart(), rhShift.getDateFinish());
		
		rhShift.setDuration(gapMin);
		rhShift.setDateReg(now);
		rhShift.setRhPerson(rhPerson);
		rhShift.setRhPersonResp(personService.getManager(rhPerson));
		rhShift.setRhType(typeDAO.getRhType("SHIFT", 3));
		rhShift.setRhStatus(statusDAO.getRhStatus("SH_PERMISSION", 1)); //PENDING
		rhShift.setRhPersonMng(personService.getManager(rhPerson));
		rhShift.setRhShiftPeriod(shiftPerioDAO.get(rhShift.getDateStart(), rhPerson.getRhCompany()));
		rhShift.setRhTypePerm(typeDAO.getRhType(rhShift.getRhTypePerm().getId()));
		rhShift.setRhTypeCharge(rhShift.getRhTypePerm().getRhTypeParent());
		rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
		
		rhShift = shiftDAO.saveRhShift(rhShift);
		utilService.saveFiles(rhShift);
		
		mailRhService.sendMailRequest(rhShift);
		return rhShift;
	}

	
	public RhShift update(RhShift rhShift) {
		Date now = utilService.getNowDate(rhShift.getRhPerson());
		rhShift.setDateProcess(now);
		
		if (rhShift.isApproved()) {//APPROVED
			
			rhShift.setDateStartShift(rhShift.getDateStart());
			rhShift.setDateFinishShift(rhShift.getDateFinish());
			rhShift.setRhTypeCharge(rhShift.getRhTypePerm().getRhTypeParent());
			rhShift.setRhStatus(statusDAO.getRhStatus("SH_PERMISSION", 2)); //APPROVED
			rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 4));
			rhShift.setRhPersonResp(null);
			shiftDAO.updateRhShift(rhShift);
			//MANAGE THE RATIOS
			shiftDetailService.process(rhShift, false);
			shiftDetailService2.process(rhShift, false);
		}
		else{//DISAPPROVED
			rhShift.setRhStatus(statusDAO.getRhStatus("SH_PERMISSION", 3));
			rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
			rhShift.setRhPersonResp(null);
			//no process any bcs
			shiftDAO.updateRhShift(rhShift);
			shiftDetailService.processCero(rhShift);
			shiftDetailService2.processCero(rhShift);
		}
		
		mailRhService.sendMailProcess(rhShift);
		return rhShift;
	}
	
	
	public void checkFinish() {
		log.info("checking finish License.. ");
		// check commission registered 
		List<RhShift> rhPermissionPending = shiftDAO.list(statusDAO.getRhStatus("SH_SIS_STATUS", 4), //PENDING APROVED
				statusDAO.getRhStatus("SH_PERMISSION", 2), typeDAO.getRhType("SHIFT", 3));
		List<RhShift> rhPermissionActive = shiftDAO.list(statusDAO.getRhStatus("SH_SIS_STATUS", 1), // ACTIVE APROVED
				statusDAO.getRhStatus("SH_PERMISSION", 2), typeDAO.getRhType("SHIFT", 3));
		for (RhShift rhShift : rhPermissionPending) {
			Date now = utilService.getNowDate(rhShift.getRhPerson());
			DateTime nowTime =  new DateTime(now);
			DateTime startTime = new DateTime(rhShift.getDateStart());
			DateTime finishTime = new DateTime(rhShift.getDateFinish());
			
			if(finishTime.isBefore(nowTime)){
				rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
				rhShift.setRhPersonResp(null);
				rhShift.setComment("This permisions has benn finished... ");
				
				shiftDAO.updateRhShift(rhShift);
			}else if(nowTime.isAfter(startTime) && nowTime.isBefore(finishTime)){
				rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 2));
				rhShift.setComment("This permission has benn Activated... ");
				rhShift.setRhPersonResp(rhShift.getRhPerson());
				shiftDAO.updateRhShift(rhShift);
			}
		}
		
		for (RhShift rhShift : rhPermissionActive) {
			Date now = utilService.getNowDate(rhShift.getRhPerson());
			DateTime nowTime =  new DateTime(now);
			DateTime finishTime = new DateTime(rhShift.getDateFinish());
			if (finishTime.isAfter(nowTime)) {
				rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
				rhShift.setComment("This permisions has benn finished...");
				rhShift.setDateProcess(now);
				rhShift.setRhPersonResp(null);
				shiftDAO.updateRhShift(rhShift);
			}
			
		}
		
	}
}