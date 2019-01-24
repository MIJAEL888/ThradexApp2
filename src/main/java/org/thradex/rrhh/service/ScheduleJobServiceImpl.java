package org.thradex.rrhh.service;

import org.springframework.transaction.annotation.Transactional;
import org.thradex.model.RhPerson;
import org.thradex.model.RhShift;
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
public class ScheduleJobServiceImpl implements ScheduleJobService {
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);
	
	@Autowired
	private ShiftDAO shiftDAO;
	
	@Autowired
	private UtilRrhhService utilService;
	
	@Autowired
	private TypeDAO typeDAO;
	
	@Autowired
	private StatusDAO statusDAO;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private ShiftDetailService shiftDetailService;
	
	@Autowired
	private ShiftDetailService2 shiftDetailService2;

	@Autowired
	private ShiftPeriodService shiftPeriodService;
	
	@Autowired
	private MailRhService mailRhService;
	
	/* (non-Javadoc)
	 * @see org.thradex.rrhh.service.ShiftService#validRegisterShift(org.thradex.model.RhPerson, String)
	 * 
	 * verify if the user can register their shift on the system.
	 * Return:
	 * 1. NO SCHEDULE JOB FOR 2DAY 
	 * 2. 
	 * 3. IS NOT TIME TO START YET
	 * 4. Can START THE SHIFT
	 * 5. Can close the shift
	 * 
	 */
	
	public RhShift validRegister(RhPerson rhPerson, String ip) {
		Date nowDate = utilService.getNowDate(rhPerson);
		RhShift rhShift = shiftDAO.getRhShift(rhPerson, statusDAO.getRhStatus("SH_SCHJOB", 2), // active session
				typeDAO.getRhType("SHIFT", 2));
		
		if (rhShift == null) { // validate for start session 
			RhShift rhShiftReg = shiftDAO.getRhShift(rhPerson, statusDAO.getRhStatus("SH_SCHJOB", 1),
					typeDAO.getRhType("SHIFT", 2), nowDate);
			if (rhShiftReg == null)
				return new RhShift(1);
			else{ // validate the time
				DateTime startTime = new DateTime(rhShiftReg.getDateStart());
				DateTime nowTime = new DateTime(nowDate);
				
				if(nowTime.isAfter(startTime.minusHours(1))){
					rhShiftReg.setValidate(4);
					return rhShiftReg;
				}else{
					rhShiftReg.setValidate(3);
					return rhShiftReg;
				}
			}
		}else{ // validate for finish session 
			rhShift.setValidate(5);
			return rhShift;
		}
	}

	
	public RhShift registerStart(RhPerson rhPerson, int idRhShift, String ip) {
		RhShift rhShift = shiftDAO.getRhShift(idRhShift);
		DateTime nowDate = new DateTime(utilService.getNowDate(rhPerson));
		
		rhShift.setDateStartShift(nowDate.toDate());
		rhShift.setRhStatus(statusDAO.getRhStatus("SH_SCHJOB", 2));
		rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 2));
		shiftDAO.updateRhShift(rhShift);
		
		return rhShift;
	}
	
	
	public RhShift registerFinish(int idRhShift, String ip) {
		RhShift rhShift = shiftDAO.getRhShift(idRhShift);
		DateTime nowDate = new DateTime(utilService.getNowDate(rhShift.getRhPerson()));
		
		rhShift.setDateFinishShift(nowDate.toDate());
		rhShift.setRhStatus(statusDAO.getRhStatus("SH_SCHJOB", 3));
		rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
		rhShift.setDateProcess(nowDate.toDate());
		rhShift.setRhPersonResp(null);
		shiftDAO.updateRhShift(rhShift);
		
		shiftDetailService.process(rhShift, false);
		shiftDetailService2.process(rhShift, false);
		return rhShift;
	}
	/*(non-Javadoc)
	 * @see org.thradex.rrhh.service.ScheduleJobService#save(org.thradex.model.RhPerson, org.thradex.model.RhScheduleJob)
	 * 
	 * - verify if the staff has another schedule in the same time
	 * - verify if the staff has a shift on this time
	 *  
	 */
	
	public RhShift save(RhPerson rhPerson, RhShift rhShift){
		Date now = utilService.getNowDate(rhPerson);
		int gapMin = utilService.getMinutes(rhShift.getDateStart(), rhShift.getDateFinish());
		
		if(rhShift.isToPay())
			rhShift.setRhTypeCharge(typeDAO.getRhType("SHIFT_DETAIL", 5));
		else
			rhShift.setRhTypeCharge(typeDAO.getRhType("SHIFT_DETAIL", 3));
		
		rhShift.setDuration(gapMin);
		rhShift.setDateReg(now);
		rhShift.setRhPerson(personService.get(rhShift.getRhPerson().getId()));
		rhShift.setRhStatus(statusDAO.getRhStatus("SH_SCHJOB", 1)); //RESGISTERED
		rhShift.setRhPersonMng(personService.getManager(rhPerson));
		rhShift.setRhType(typeDAO.getRhType("SHIFT", 2));
		rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 4));
		rhShift.setRhShiftPeriod(shiftPeriodService.get(rhShift.getDateStart(), 
				rhPerson.getRhCompany()));
		rhShift.setRhPersonResp(null);
		
		rhShift = shiftDAO.saveRhShift(rhShift);
		utilService.saveFiles(rhShift);
		mailRhService.sendMailAsigation(rhShift);
		return rhShift;
	}
	
	
	public List<RhShift> listSession(RhPerson rhPerson){
		return shiftDAO.listMng(rhPerson, statusDAO.getRhStatus("SH_SCHJOB", 2), typeDAO.getRhType("SHIFT", 2));
	}

//	public List<RhShift> list(int option,RhPerson rhPerson) {
//		List<RhShift> rhShifts = new ArrayList<RhShift>();
//		DateTime dateTimeNow = new DateTime(utilService.getNowDate());
//		switch (option) {
//		case 1: //  pending  by user
//		
//			rhShifts.addAll(shiftDAO.listBySis(rhPerson, statusDAO.getRhStatus("SH_SIS_STATUS", 1),
//					typeDAO.getRhType("SHIFT", 2)));
//			rhShifts.addAll(shiftDAO.listBySis(rhPerson, statusDAO.getRhStatus("SH_SIS_STATUS", 2),
//					typeDAO.getRhType("SHIFT", 2)));
//			for (RhShift rhShift : rhShifts) {
//				rhShift.setRhPersonResp(rhShift.getRhPerson());
//			}
//			return rhShifts;
//		case 2: // pending to process
//			rhShifts.addAll(shiftDAO.listMngBySis(rhPerson, statusDAO.getRhStatus("SH_SIS_STATUS", 1),
//					typeDAO.getRhType("SHIFT", 2)));
//			rhShifts.addAll(shiftDAO.listMngBySis(rhPerson, statusDAO.getRhStatus("SH_SIS_STATUS", 2),
//					typeDAO.getRhType("SHIFT", 2)));
//			for (RhShift rhShift : rhShifts) {
//				rhShift.setRhPersonResp(rhShift.getRhPerson());
//			}
//			return rhShifts;
//		case 3: // last 35 days 
//			
//			dateTimeNow = dateTimeNow.minusDays(35);
//			rhShifts.addAll(shiftDAO.listBySis(rhPerson, typeDAO.getRhType("SHIFT", 2), 
//					statusDAO.getRhStatus("SH_SIS_STATUS", 3), dateTimeNow.toDate()));
//			return rhShifts;
//		default:
//			log.error("not option defined to show Shedule job...");
//			return rhShifts;
//		}
//	}
//	
//	public List<RhShift> listReport(int option,RhPerson rhPerson, RhShiftPeriod rhShiftPeriod) {
//		List<RhShift> rhShifts = new ArrayList<RhShift>();
//		switch (option) {
//		case 1: //  pending  by user
//			rhShifts.addAll(shiftDAO.listBySis(rhPerson, statusDAO.getRhStatus("SH_SIS_STATUS", 1),
//					typeDAO.getRhType("SHIFT", 2), rhShiftPeriod));
//			rhShifts.addAll(shiftDAO.listBySis(rhPerson, statusDAO.getRhStatus("SH_SIS_STATUS", 2),
//					typeDAO.getRhType("SHIFT", 2), rhShiftPeriod));
//			for (RhShift rhShift : rhShifts) {
//				rhShift.setRhPersonResp(rhPerson);
//			}
//			return rhShifts;
//		case 2: // pending to process
//			rhShifts.addAll(shiftDAO.listMngBySis(rhPerson, statusDAO.getRhStatus("SH_SIS_STATUS", 1),
//					typeDAO.getRhType("SHIFT", 2), rhShiftPeriod));
//			rhShifts.addAll(shiftDAO.listMngBySis(rhPerson, statusDAO.getRhStatus("SH_SIS_STATUS", 2),
//					typeDAO.getRhType("SHIFT", 2), rhShiftPeriod));
//			for (RhShift rhShift : rhShifts) {
//				rhShift.setRhPersonResp(rhShift.getRhPerson());
//			}
//			return rhShifts;
//		case 3: // last 35 days 
//			rhShifts.addAll(shiftDAO.listBySis(rhPerson, statusDAO.getRhStatus("SH_SIS_STATUS", 3),
//					typeDAO.getRhType("SHIFT", 2), rhShiftPeriod));
//			return rhShifts;
//		case 4: // last 35 days 
//			rhShifts.addAll(shiftDAO.listMngBySis(rhPerson, statusDAO.getRhStatus("SH_SIS_STATUS", 3),
//					typeDAO.getRhType("SHIFT", 2), rhShiftPeriod));
//			return rhShifts;
//			
//		case 5: //  pending  by user
//			
//			rhShifts.addAll(shiftDAO.listBySis(statusDAO.getRhStatus("SH_SIS_STATUS", 1),
//					typeDAO.getRhType("SHIFT", 2), rhShiftPeriod));
//			rhShifts.addAll(shiftDAO.listBySis(statusDAO.getRhStatus("SH_SIS_STATUS", 2),
//					typeDAO.getRhType("SHIFT", 2), rhShiftPeriod));
//			for (RhShift rhShift : rhShifts) {
//				rhShift.setRhPersonResp(rhShift.getRhPerson());
//			}
//			return rhShifts;
//		case 6: // last 35 days 
//			rhShifts.addAll(shiftDAO.listBySis(statusDAO.getRhStatus("SH_SIS_STATUS", 3),
//					typeDAO.getRhType("SHIFT", 2), rhShiftPeriod));
//			return rhShifts;	
//		case 7: //  pending  by user
//			rhShifts.addAll(shiftDAO.listBySis(rhPerson, statusDAO.getRhStatus("SH_SIS_STATUS", 1),
//					typeDAO.getRhType("SHIFT", 2), rhShiftPeriod));
//			rhShifts.addAll(shiftDAO.listBySis(rhPerson, statusDAO.getRhStatus("SH_SIS_STATUS", 2),
//					typeDAO.getRhType("SHIFT", 2), rhShiftPeriod));
//			for (RhShift rhShift : rhShifts) {
//				rhShift.setRhPersonResp(rhPerson);
//			}
//			return rhShifts;
//		default:
//			log.error("not option defined to show Shedule job...");
//			return rhShifts;
//		}
//	}
}