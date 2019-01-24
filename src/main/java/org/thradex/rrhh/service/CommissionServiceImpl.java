package org.thradex.rrhh.service;

import org.springframework.transaction.annotation.Transactional;
import org.thradex.model.RhPerson;
import org.thradex.model.RhShift;
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
public class CommissionServiceImpl implements CommissionService {
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
	private ShiftPeriodService shiftPeriodService;
	
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
		log.info("saving new commmission");
		int gapMin = utilService.getMinutes(rhShift.getDateStart(), rhShift.getDateFinish());
		
		rhShift.setDuration(gapMin);
		rhShift.setDateReg(now);
		rhShift.setRhTypeCharge(typeDAO.getRhType("SHIFT_DETAIL", 1));
		rhShift.setRhPersonResp(null);
		rhShift.setDateStartShift(rhShift.getDateStart());
		rhShift.setDateFinishShift(rhShift.getDateFinish());
		rhShift.setRhStatus(statusDAO.getRhStatus("SH_COMMISSION", 4)); //PENDING
		rhShift.setRhPersonMng(rhPerson);
		rhShift.setRhType(typeDAO.getRhType("SHIFT", 15));
		rhShift.setRhShiftPeriod(shiftPeriodService.get(rhShift.getDateStart(), rhPerson.getRhCompany()));
		rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
		
		rhShift = shiftDAO.saveRhShift(rhShift);
		utilService.saveFiles(rhShift);
		//when abroadJob is completed process detail
//		Days.daysBetween(startTime, finishTime);
		mailRhService.sendMailAsigation(rhShift);
		return rhShift;
	}

	
	public RhShift update(RhShift rhShift) {
		if (rhShift.isApproved()) { //APPROVED
			rhShift.setRhStatus(statusDAO.getRhStatus("SH_COMMISSION", 2)); 
			
		}
		else rhShift.setRhStatus(statusDAO.getRhStatus("SH_COMMISSION", 2)); //DISAPPROVED
		return rhShift;
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
		List<RhShift> rhAbroadJobsReg = shiftDAO.list(statusDAO.getRhStatus("SH_COMMISSION", 1), //abroadJob state register
				typeDAO.getRhType("SHIFT", 15));
		List<RhShift> rhAbroadJobsAct = shiftDAO.list(statusDAO.getRhStatus("SH_COMMISSION", 2), //abroadJob state register
				typeDAO.getRhType("SHIFT", 15));
		
		for (RhShift rhShift : rhAbroadJobsReg) {
			Date now = utilService.getNowDate(rhShift.getRhPerson());
			DateTime nowTime =  new DateTime(now);
			DateTime startTime = new DateTime(rhShift.getDateStart());
			DateTime finishTime = new DateTime(rhShift.getDateFinish());
			
			if(finishTime.isBefore(nowTime)){
				rhShift.setRhStatus(statusDAO.getRhStatus("SH_COMMISSION", 3));
				rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
				rhShift.setComment("This commission has been completed already... ");
				rhShift.setRhPersonResp(null);
				shiftDAO.updateRhShift(rhShift);
				shiftDetailService.process(rhShift, false);
				shiftDetailService2.process(rhShift, false);
			}else if(nowTime.isAfter(startTime) && nowTime.isBefore(finishTime)){
				rhShift.setRhStatus(statusDAO.getRhStatus("SH_COMMISSION", 2));
				rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 2));
				rhShift.setComment("This commission has benn Activated... ");
				shiftDAO.updateRhShift(rhShift);
			}
		}
		
		for (RhShift rhShift : rhAbroadJobsAct) {
			Date now = utilService.getNowDate(rhShift.getRhPerson());
			DateTime nowTime =  new DateTime(now);
			DateTime finishTime = new DateTime(rhShift.getDateFinish());
			if (finishTime.isAfter(nowTime)) {
				rhShift.setRhStatus(statusDAO.getRhStatus("SH_COMMISSION", 3));
				rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
				rhShift.setComment("This commission has been completed ... ");
				rhShift.setDateProcess(now);
				rhShift.setRhPersonResp(null);
				shiftDAO.updateRhShift(rhShift);
				shiftDetailService.process(rhShift, false);
				shiftDetailService2.process(rhShift, false);
			}
			
		}
		
	}
	
}