package org.thradex.rrhh.service;

import org.springframework.transaction.annotation.Transactional;
import org.thradex.model.RhPerson;
import org.thradex.model.RhSchedulePerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;
import org.thradex.rrhh.dao.FileDAO;
import org.thradex.rrhh.dao.ShiftDAO;
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
public class LicenseServiceImpl implements LicenseService {
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
	private PersonService personService;
	
	@Autowired
	private ShiftPeriodService shiftPeriodService;
	
	@Autowired
	private ShiftDetailService shiftDetailService;
	
	@Autowired
	private ShiftDetailService2 shiftDetailService2;
	
	@Autowired
	private FileDAO fileDAO;
	
	@Autowired
	private MailRhService mailRhService;
	
	
	public List<RhType> listLicense(){
		return typeDAO.getRhType("LICENSE");
	}
	
	
	public RhType getTypeLicense(int idType){
		return typeDAO.getRhType(idType);
	}
	
	
	public RhShift get(int id) {
		RhShift rhShift =  shiftDAO.getRhShift(id);
		rhShift.setRhFiles(fileDAO.getRhFile(rhShift));
		return rhShift;
	}

	
	public RhShift save(RhShift rhShift, RhPerson rhPerson) {
		Date now = utilService.getNowDate(rhPerson);
//		DateTime dateTimeStart = new DateTime(rhShift.getDateStart());
//		DateTime dateTimeFinish = dateTimeStart.plusDays(rhShift.getDurationHour());
//		rhShift.setDateFinish(dateTimeFinish.toDate());
		
		int gapMin = utilService.getMinutes(rhShift.getDateStart(), rhShift.getDateFinish());
		
		rhShift.setDuration(gapMin);
		rhShift.setDateReg(now);
		rhShift.setRhPerson(rhPerson);
		rhShift.setRhPersonResp(personService.getManager(rhPerson));
		rhShift.setRhType(typeDAO.getRhType("SHIFT", 5));
		rhShift.setRhStatus(statusDAO.getRhStatus("SH_LICENSE", 1)); //PENDING
		rhShift.setRhPersonMng(personService.getManager(rhPerson));
		rhShift.setRhShiftPeriod(shiftPeriodService.get(rhShift.getDateStart(), rhPerson.getRhCompany()));
		rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
		rhShift.setRhTypePerm(typeDAO.getRhType(rhShift.getRhTypePerm().getId()));
		rhShift.setRhTypeCharge(rhShift.getRhTypePerm().getRhTypeParent());
		rhShift = shiftDAO.saveRhShift(rhShift);
		utilService.saveFiles(rhShift);
		mailRhService.sendMailRequest(rhShift);
		return rhShift;
	}

	
	public RhShift update(RhShift rhShift) {
		if (rhShift.isApproved()) {//APPROVED
			rhShift.setDateStartShift(rhShift.getDateStart());
			rhShift.setDateFinishShift(rhShift.getDateFinish());
			rhShift.setRhTypeCharge(rhShift.getRhTypePerm().getRhTypeParent());
			rhShift.setRhStatus(statusDAO.getRhStatus("SH_LICENSE", 2)); //APPROVED
			rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 4)); //PLANNED
			rhShift.setRhPersonResp(null);
			shiftDAO.updateRhShift(rhShift);
			shiftDetailService.processCero(rhShift);
			shiftDetailService2.processCero(rhShift);
			//THE RATIOS IS MANAGED FOR THE THREAD WHICH CHECK THE REGULAR SHIFTS 
		}
		else{//DISAPPROVED
			rhShift.setRhStatus(statusDAO.getRhStatus("SH_LICENSE", 3));
			rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
			rhShift.setRhPersonResp(null);
			//no process any RATIO
			shiftDAO.updateRhShift(rhShift);
			shiftDetailService.processCero(rhShift);
			shiftDetailService2.processCero(rhShift);
		}
		mailRhService.sendMailProcess(rhShift);
		return rhShift;
	}

	
	public RhShift get(Date date, RhStatus rhStatus) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.thradex.rrhh.service.LicenseService#checkFinish()
	 * 
	 * Update the shift's status 
	 * don't process any shit's detail.
	 * 
	 */
	
	public void checkFinish() {
		log.info("checking finish License.. ");
		// check commission registered 
		List<RhShift> rhLicensePending = shiftDAO.list(statusDAO.getRhStatus("SH_SIS_STATUS", 4), // PLANNED AND APROVED
				statusDAO.getRhStatus("SH_LICENSE", 2), typeDAO.getRhType("SHIFT", 5));
		List<RhShift> rhLecenseActive = shiftDAO.list(statusDAO.getRhStatus("SH_SIS_STATUS", 2), // ACTIVE AND  APROVED
				statusDAO.getRhStatus("SH_LICENSE", 2), typeDAO.getRhType("SHIFT", 5));
		for (RhShift rhShift : rhLicensePending) {
			Date now = utilService.getNowDate(rhShift.getRhPerson());
			DateTime nowTime =  new DateTime(now);
			DateTime startTime = new DateTime(rhShift.getDateStart());
			DateTime finishTime = new DateTime(rhShift.getDateFinish());
			
			if(finishTime.isBefore(nowTime)){
				rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
				rhShift.setRhPersonResp(null);
				rhShift.setComment("This License has benn not procceded every day... ");
				
				shiftDAO.updateRhShift(rhShift);
			}else if(nowTime.isAfter(startTime) && nowTime.isBefore(finishTime)){
				rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 2));
				rhShift.setComment("This License has benn Activated... ");
				rhShift.setRhPersonResp(rhShift.getRhPerson());
				shiftDAO.updateRhShift(rhShift);
			}
		}
		
		for (RhShift rhShift : rhLecenseActive) {
			Date now = utilService.getNowDate(rhShift.getRhPerson());
			DateTime nowTime =  new DateTime(now);
			DateTime finishTime = new DateTime(rhShift.getDateFinish());
			if (finishTime.isAfter(nowTime)) {
				rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
				rhShift.setComment("This License finisheds... ");
				rhShift.setDateProcess(now);
				rhShift.setRhPersonResp(null);
				shiftDAO.updateRhShift(rhShift);
			}
			
		}
		
	}

	
	public void process(RhShift rhLicense, Date start, Date finish, RhSchedulePerson rhSchedulePerson) {
		boolean takenBreak = false;
		if(rhSchedulePerson.getRhSchedule().getFlagBreak() == 1) takenBreak = true;
		Date now = utilService.getNowDate(rhLicense.getRhPerson());
		RhShift rhShift = new RhShift();
		rhShift.setDateStart(start);
		rhShift.setDateFinish(finish);
		rhShift.setDateStartShift(start);
		rhShift.setDateFinishShift(finish);
		rhShift.setRhTypeCharge(rhLicense.getRhTypeCharge());
		rhShift.setRhTypePerm(rhLicense.getRhTypePerm());
		rhShift.setRhPerson(rhLicense.getRhPerson());
		rhShift.setRhType(typeDAO.getRhType("SHIFT", 8)); // LICENSE SHIFT 
		rhShift.setRhTypePerm(typeDAO.getRhType("SHIFT", 5)); // sub type license
		rhShift.setRhStatus(statusDAO.getRhStatus("SH_LICENSE", 4));
		rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
		rhShift.setRhPersonMng(rhLicense.getRhPersonMng());
		rhShift.setDateReg(now);
		rhShift.setDateProcess(now);
		rhShift.setRhShiftPeriod(shiftPeriodService.get(now, rhLicense.getRhPerson().getRhCompany()));
		rhShift.setComment("Atomatic processing license... ");
		rhShift.setRhShiftParent(rhLicense);
		
		shiftDAO.saveRhShift(rhShift);
		
		shiftDetailService.process(rhShift, takenBreak);
		shiftDetailService2.process(rhShift, takenBreak);
		// si es compensacion reducir de la bosa de compensacion....
	}
	
	
}