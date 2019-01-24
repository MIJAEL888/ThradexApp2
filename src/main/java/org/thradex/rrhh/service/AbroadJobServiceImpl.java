package org.thradex.rrhh.service;

import org.springframework.transaction.annotation.Transactional;
import org.thradex.model.RhPerson;
import org.thradex.model.RhSchedulePerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhType;
import org.thradex.model.SisCountry;
import org.thradex.rrhh.dao.FileDAO;
import org.thradex.rrhh.dao.ShiftDAO;
import org.thradex.rrhh.dao.StatusDAO;
import org.thradex.rrhh.dao.TypeDAO;
import org.thradex.sis.dao.CountryDAO;
import org.thradex.sis.dao.RegionDAO;
import org.thradex.sis.dao.StatusSisDAO;
import org.thradex.util.ConstantsSis;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class AbroadJobServiceImpl implements AbroadJobService {
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
	private CountryDAO countryDAO;
	
	@Autowired
	private RegionDAO regionDAO;
	
	@Autowired
	private ShiftPeriodService shiftPeriodService;
	
	@Autowired
	private StatusSisDAO statusSisDAO;
	
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

	
	public RhShift save(RhShift rhShift, RhPerson rhPerson) {
		Date now = utilService.getNowDate(rhPerson);
		int gapMin = utilService.getMinutes(rhShift.getDateStart(), rhShift.getDateFinish());
		if (rhShift.isToPay()){//to  pay
			rhShift.setRhTypeCharge(typeDAO.getRhType("SHIFT_DETAIL", 5));
		}else {// compensate
			rhShift.setRhTypeCharge(typeDAO.getRhType("SHIFT_DETAIL", 3));
		}
		rhShift.setDuration(gapMin);
		rhShift.setDateReg(now);
		rhShift.setRhPersonResp(null);
		rhShift.setDateStartShift(rhShift.getDateStart());
		rhShift.setDateFinishShift(rhShift.getDateFinish());
		rhShift.setRhStatus(statusDAO.getRhStatus("SH_ABROADJOB", 1)); //PENDING
		rhShift.setRhPersonMng(rhPerson);
		rhShift.setRhType(typeDAO.getRhType("SHIFT", 6));
		rhShift.setRhShiftPeriod(shiftPeriodService.get(rhShift.getDateStart(), 
				rhPerson.getRhCompany()));
		rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 4));
		rhShift = shiftDAO.saveRhShift(rhShift);
		utilService.saveFiles(rhShift);
		//when abroadJob is completed process detail
		mailRhService.sendMailAsigation(rhShift);
		return rhShift;
	}

	
	public List<SisCountry> listCountry() {
		return countryDAO.list();
	}
	
	
	public List<Map<String, Object>> listRegion(int idCountry){
		SisCountry sisCountry = countryDAO.get(idCountry);
		return regionDAO.listMap(statusSisDAO.getSisStatus("SIS_REGION", 1), sisCountry);
	}

	/* (non-Javadoc)
	 * @see org.thradex.rrhh.service.abroadJobService#checkFinish()
	 * 
	 * Update the shift's status 
	 * don't process any shit's detail.
	 * 
	 */
	
	public void checkFinish() {
		log.info("checking finish abroadJob.. ");
		// check abroadJob registered 
		List<RhShift> rhAbroadJobsReg = shiftDAO.list(statusDAO.getRhStatus("SH_ABROADJOB", 1), //abroadJob state register
				typeDAO.getRhType("SHIFT", 6));
		List<RhShift> rhAbroadJobsAct = shiftDAO.list(statusDAO.getRhStatus("SH_ABROADJOB", 2), //abroadJob state register
				typeDAO.getRhType("SHIFT", 6));
		
		for (RhShift rhShift : rhAbroadJobsReg) {
			
			Date now = utilService.getNowDate(rhShift.getRhPerson());
			DateTime nowTime =  new DateTime(now);
			DateTime startTime = new DateTime(rhShift.getDateStart());
			DateTime finishTime = new DateTime(rhShift.getDateFinish());
			
			if(finishTime.isBefore(nowTime)){
				rhShift.setRhStatus(statusDAO.getRhStatus("SH_ABROADJOB", 3));
				rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
				rhShift.setComment("This abroadJob has benn not procceded every day... ");
				rhShift.setRhPersonResp(null);
				rhShift.setRhShiftPeriod(shiftPeriodService.get(now, rhShift.getRhPerson().getRhCompany()));
				shiftDAO.updateRhShift(rhShift);
				if(rhShift.getRhPerson().getRhCompany().getSisRegion().getSisCountry().getId() != rhShift.getSisRegion().getSisCountry().getId())
				shiftDetailService.proccessAbroadJob(rhShift);
				
			}else if(nowTime.isAfter(startTime) && nowTime.isBefore(finishTime)){
				rhShift.setRhStatus(statusDAO.getRhStatus("SH_ABROADJOB", 2));
				rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 2));
				rhShift.setComment("This abroadJob has benn Activated... ");
				shiftDAO.updateRhShift(rhShift);
			}
		}
		
		for (RhShift rhShift : rhAbroadJobsAct) {
			Date now = utilService.getNowDate(rhShift.getRhPerson());
			DateTime nowTime =  new DateTime(now);
			DateTime finishTime = new DateTime(rhShift.getDateFinish());
			if (finishTime.isAfter(nowTime)) {
				rhShift.setRhStatus(statusDAO.getRhStatus("SH_ABROADJOB", 3));
				rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
				rhShift.setComment("This abroadJob finished... ");
				rhShift.setDateProcess(now);
				rhShift.setRhPersonResp(null);
				rhShift.setRhShiftPeriod(shiftPeriodService.get(now, rhShift.getRhPerson().getRhCompany()));
				shiftDAO.updateRhShift(rhShift);
				
				if(rhShift.getRhPerson().getRhCompany().getSisRegion().getSisCountry().getId() != rhShift.getSisRegion().getSisCountry().getId())
				shiftDetailService.proccessAbroadJob(rhShift);
			}
			
		}
		
	}

	
	public void process(RhShift rhAbroadJob, Date start, Date finish, RhSchedulePerson rhSchedulePerson) {
		boolean takenBreak = false;
		if(rhSchedulePerson.getRhSchedule().getFlagBreak() == 1) takenBreak = true;
		
		Date now = utilService.getNowDate(rhAbroadJob.getRhPerson());
		RhType rhTypeCharge = typeDAO.getRhType("SHIFT_DETAIL", 1);
		
		RhShift rhShift = new RhShift();
		rhShift.setDateStart(start);
		rhShift.setDateFinish(finish);
		rhShift.setDateStartShift(start);
		rhShift.setDateFinishShift(finish);
		rhShift.setRhTypeCharge(rhTypeCharge);
//		rhShift.setRhTypePerm(rhLicense.getRhTypePerm());
		rhShift.setRhPerson(rhAbroadJob.getRhPerson());
		rhShift.setRhType(typeDAO.getRhType("SHIFT", 9)); //SHIFT WORK ABROAD
		rhShift.setRhStatus(statusDAO.getRhStatus("SH_ABROADJOB", 4));
		rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
		rhShift.setRhPersonMng(rhAbroadJob.getRhPersonMng());
		rhShift.setDateReg(now);
		rhShift.setDateProcess(now);
		rhShift.setRhTypePerm(typeDAO.getRhType("SHIFT", 6)); // sub type abroadJob
		rhShift.setRhShiftPeriod(shiftPeriodService.get(now, rhAbroadJob.getRhPerson().getRhCompany()));
		rhShift.setComment("Atomatic processing abroad job... ");
		rhShift.setRhShiftParent(rhAbroadJob);
		
		shiftDAO.saveRhShift(rhShift);
		
		shiftDetailService.process(rhShift, takenBreak);
		shiftDetailService2.process(rhShift, takenBreak);
	}
	
	
}