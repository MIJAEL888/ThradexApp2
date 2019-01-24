package org.thradex.rrhh.service;

import org.springframework.transaction.annotation.Transactional;
import org.thradex.exception.CustomGenericException;
import org.thradex.model.RhPerson;
import org.thradex.model.RhScheduleDay;
import org.thradex.model.RhSchedulePerson;
import org.thradex.model.RhScheduleScale;
import org.thradex.model.RhShift;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;
import org.thradex.rrhh.dao.FileDAO;
import org.thradex.rrhh.dao.ScheduleDayDAO;
import org.thradex.rrhh.dao.SchedulePersonDAO;
import org.thradex.rrhh.dao.ScheduleScaleDAO;
import org.thradex.rrhh.dao.ShiftDAO;
import org.thradex.rrhh.dao.StatusDAO;
import org.thradex.rrhh.dao.TypeDAO;
import org.thradex.util.ConstantsSis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ScheduleScaleServiceImpl implements ScheduleScaleService {
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);
	
	@Autowired
	private ShiftDAO shiftDAO;

	@Autowired
	private TypeDAO typeDAO;
	
	@Autowired
	private StatusDAO statusDAO;
	
	@Autowired
	private UtilRrhhService utilService;
	
	@Autowired
	private ShiftPeriodService shiftPeriodService;

	@Autowired
	private ShiftDetailService shiftDetailService;

	@Autowired
	private ShiftDetailService2 shiftDetailService2;
	
	@Autowired
	private SchedulePersonDAO schedulePersonDAO;
	
	@Autowired
	private ScheduleDayDAO scheduleDayDAO;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private ScheduleScaleDAO scheduleScaleDAO;
	
	@Autowired
	private AbroadJobService abroadJobService;
	
	@Autowired
	private LicenseService licenseService;
	
	@Autowired
	private FileDAO fileDAO;
	
	@Autowired
	private MailRhService mailRhService;
	
	/* (non-Javadoc)
	 * @see org.thradex.rrhh.service.ShiftService#validRegisterShift(org.thradex.model.RhPerson, String)
	 * 
	 * verify if the user can register their shift on the system.
	 * Return:
	 * 1. NO SCHEDULEPERSON 
	 * 2. INVALID IP
	 * 3. BEFORE TIME START
	 * 4. Can START THE SHIFT
	 * 5. Can close the shift
	 * 
	 */
	
	public RhShift validRegisterScale(RhPerson rhPerson, String ip) {
		Date nowDate = utilService.getNowDate(rhPerson);
		RhSchedulePerson rhSchedulePerson = schedulePersonDAO.get(rhPerson);
//		log.info("ip to proccess : " + ip);
		if(rhSchedulePerson == null || rhSchedulePerson.getRhSchedule().getRhType().getCode() == 3){
			return new RhShift(6);
		}else{
			if(rhSchedulePerson.getFlagCheckIp() == 1 && !utilService.validIp(ip)){
				return new RhShift(2);
			}else{
				RhShift rhShift = shiftDAO.getRhShift(rhPerson, statusDAO.getRhStatus("SH_SCALE", 1), // active session
						typeDAO.getRhType("SHIFT", 1));
				
				if (rhShift == null) { // validate for start session 
					RhShift rhShiftScale = shiftDAO.getRhShift(rhPerson, statusDAO.getRhStatus("SH_SCALE", 3), // registered session
							 typeDAO.getRhType("SHIFT", 1), nowDate);
					
					if (rhShiftScale == null)
						//verify why it is null
						return new RhShift(1);
					else{ // validate the time
	//					RhShift rhShiftReg = new RhShift();
	//					rhShiftScale.setRhScheduleScale(rhScheduleScale);
	//					DateTime startTime = new DateTime(nowTime.getYear(), nowTime.getMonthOfYear(),
	//							nowTime.getDayOfMonth(), rhScheduleScale.getRhScheduleDay().getStartHour(), 
	//							rhScheduleScale.getRhScheduleDay().getStartMin());
	//					DateTime finishTime = startTime.plusHours(rhScheduleScale.getRhScheduleDay().getTotalHours());
						
						DateTime nowTime = new DateTime(nowDate);
						DateTime startTime = new DateTime(rhShiftScale.getDateStart());
	//					DateTime finishTime = new DateTime(rhShiftScale.getDateFinish());
						
	//					rhShiftReg.setDateStart(startTime.toDate());
	//					rhShiftReg.setDateFinish(finishTime.toDate());
						
						if(nowTime.isAfter(startTime.minusHours(1))){
							rhShiftScale.setValidate(4);
							return rhShiftScale;
						}else{
							rhShiftScale.setValidate(3);
							return rhShiftScale;
						}
					}
				}else{ // validate for finish session 
					rhShift.setValidate(5);
					return rhShift;
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.thradex.rrhh.service.ShiftService#validRegisterShift(org.thradex.model.RhPerson, String)
	 * 
	 * verify if the user can register their shift on the system.
	 * Return:
	 * 1. NO ALERT
	 * 2. LATE
	 * 3. ABSENT
	 * 
	 */
	
	public RhShift registerStartScale(RhPerson rhPerson, int idRhShift, String ip) {
		RhShift rhShift = shiftDAO.getRhShift(idRhShift);
		
		RhScheduleScale rhScheduleScale = rhShift.getRhScheduleScale();
		Date nowDate = utilService.getNowDate(rhPerson);
		DateTime nowTime = new DateTime(nowDate);
		DateTime scheduleStart = new DateTime(rhShift.getDateStart());
		
		int difMinutes = Minutes.minutesBetween(scheduleStart, nowTime).getMinutes();
		rhShift.setRhStatus(statusDAO.getRhStatus("SH_SCALE", 1));
		rhShift.setDateStartShift(nowTime.toDate());
		rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 2));
		rhShift.setRhPersonResp(rhShift.getRhPerson());
		shiftDAO.updateRhShift(rhShift);
		rhShift.setValidate(1);
		// PENDING: Verify if the person has a permission for this day
		// LATE VALIDATION
		if(difMinutes > rhScheduleScale.getRhSchedulePerson().getRhSchedule().getToleranceAbsence()){
			RhShift rhShiftAlert = new RhShift();
			rhShiftAlert.setRhTypePerm(typeDAO.getRhType("SH_ALERT", 1)); // ABSENT
			rhShiftAlert.setRhType(typeDAO.getRhType("SHIFT", 13));
			rhShiftAlert.setRhTypeCharge(typeDAO.getRhType("SHIFT_DETAIL", 2));
			rhShiftAlert.setDuration(difMinutes);
			rhShiftAlert.setDateStart(scheduleStart.toDate());
			rhShiftAlert.setDateFinish(nowTime.toDate());
			rhShiftAlert.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 1));
			rhShiftAlert.setDateReg(nowDate);
			rhShiftAlert.setRhShiftParent(rhShift);
			rhShiftAlert.setRhPerson(rhShift.getRhPerson());
			rhShiftAlert.setRhPersonMng(rhShift.getRhPersonMng());
			rhShiftAlert.setRhShiftPeriod(rhShift.getRhShiftPeriod());
			rhShiftAlert.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
			rhShiftAlert.setRhPersonResp(rhShift.getRhPerson());
			rhShiftAlert = shiftDAO.saveRhShift(rhShiftAlert);
			rhShiftAlert.setValidate(2);
			mailRhService.sendMailAlert(rhShiftAlert);
//			mailRhService.sendMailAlertMng(rhShiftAlert);
			return rhShiftAlert;
			
		}else if(difMinutes > rhScheduleScale.getRhSchedulePerson().getRhSchedule().getToleranceDelay()){
			RhShift rhShiftAlert = new RhShift();
			rhShiftAlert.setRhTypePerm(typeDAO.getRhType("SH_ALERT", 1)); // ABSENT
			rhShiftAlert.setRhType(typeDAO.getRhType("SHIFT", 10));
			rhShiftAlert.setRhTypeCharge(typeDAO.getRhType("SHIFT_DETAIL", 2));
			rhShiftAlert.setDuration(difMinutes);
			rhShiftAlert.setDateStart(scheduleStart.toDate());
			rhShiftAlert.setDateFinish(nowTime.toDate());
			rhShiftAlert.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 1));
			rhShiftAlert.setDateReg(nowDate);
			rhShiftAlert.setRhShiftParent(rhShift);
			rhShiftAlert.setRhPerson(rhShift.getRhPerson());
			rhShiftAlert.setRhPersonMng(rhShift.getRhPersonMng());
			rhShiftAlert.setRhShiftPeriod(rhShift.getRhShiftPeriod());
			rhShiftAlert.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
			rhShiftAlert.setRhPersonResp(rhShift.getRhPerson());
			rhShiftAlert = shiftDAO.saveRhShift(rhShiftAlert);
			rhShiftAlert.setValidate(2);
			mailRhService.sendMailAlert(rhShiftAlert);
//			mailRhService.sendMailAlertMng(rhShiftAlert);
			return rhShiftAlert;
		}

		return rhShift;
	}
	
	
	public RhShift registerFinishScale(int idRhScale, String ip) {
		boolean takenBreak = false;
		RhShift rhShift = shiftDAO.getRhShift(idRhScale);
		RhScheduleScale rhScheduleScale = rhShift.getRhScheduleScale();
		if(rhScheduleScale.getRhSchedulePerson().getRhSchedule().getFlagBreak() == 1) takenBreak = true;
		Date nowDate = utilService.getNowDate(rhShift.getRhPerson());
//		DateTime nowTime = new DateTime(nowDate);
		DateTime startShift = new DateTime(rhShift.getDateStartShift());
		DateTime finishShift = new DateTime(nowDate);
		DateTime schedulefinish = new DateTime(rhShift.getDateFinish());
		
		int difMinutesShift = Minutes.minutesBetween(startShift, finishShift).getMinutes();
		if (takenBreak) difMinutesShift = difMinutesShift - 60;
			
		rhShift.setDuration(difMinutesShift);
		rhShift.setDateFinishShift(nowDate);
		rhShift.setRhStatus(statusDAO.getRhStatus("SH_SCALE",2));
		rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
		rhShift.setRhPersonResp(null);
		rhShift.setDateProcess(nowDate);
		
		rhShift.setValidate(1);
		int difMinutes = Minutes.minutesBetween(schedulefinish, finishShift).getMinutes();
		
		if(difMinutes < rhScheduleScale.getRhSchedulePerson().getRhSchedule().getToleranceLeave()){//leave duty
			RhShift rhShiftAlert = new RhShift();
			rhShiftAlert.setRhTypePerm(typeDAO.getRhType("SH_ALERT", 1)); // leave duty
			rhShiftAlert.setRhType(typeDAO.getRhType("SHIFT", 11));
			rhShiftAlert.setRhTypeCharge(typeDAO.getRhType("SHIFT_DETAIL", 2));
			rhShiftAlert.setDuration(Math.abs(difMinutes));
			rhShiftAlert.setDateStart(finishShift.toDate());
			rhShiftAlert.setDateFinish(schedulefinish.toDate());
			rhShiftAlert.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 1));
			rhShiftAlert.setDateReg(nowDate);
			rhShiftAlert.setRhShiftParent(rhShift);
			rhShiftAlert.setRhPerson(rhShift.getRhPerson());
			rhShiftAlert.setRhPersonMng(rhShift.getRhPersonMng());
			rhShiftAlert.setRhShiftPeriod(rhShift.getRhShiftPeriod());
			rhShiftAlert.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
			rhShiftAlert.setRhPersonResp(rhShift.getRhPerson());
			rhShiftAlert = shiftDAO.saveRhShift(rhShiftAlert);
			rhShiftAlert.setValidate(2);
			
			shiftDAO.updateRhShift(rhShift);
			shiftDetailService.process(rhShift, takenBreak); // pay normal 
			shiftDetailService2.process(rhShift, takenBreak); // pay normal 
			
			mailRhService.sendMailAlert(rhShiftAlert);
//			mailRhService.sendMailAlertMng(rhShiftAlert);
			return rhShiftAlert;
		}else if(difMinutes > rhScheduleScale.getRhSchedulePerson().getRhSchedule().getToleranceFinish()){
			RhShift rhShiftAlert = new RhShift();
			rhShiftAlert.setRhTypePerm(typeDAO.getRhType("SH_ALERT", 1)); // ADITIONAL TIME
			rhShiftAlert.setRhType(typeDAO.getRhType("SHIFT", 12));
			rhShiftAlert.setRhTypeCharge(typeDAO.getRhType("SHIFT_DETAIL", 5));
			rhShiftAlert.setDuration(difMinutes);
			rhShiftAlert.setDateStart(schedulefinish.toDate());
			rhShiftAlert.setDateFinish(finishShift.toDate());
			rhShiftAlert.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 1));
			rhShiftAlert.setDateReg(nowDate);
			rhShiftAlert.setRhShiftParent(rhShift);
			rhShiftAlert.setRhPerson(rhShift.getRhPerson());
			rhShiftAlert.setRhPersonMng(rhShift.getRhPersonMng());
			rhShiftAlert.setRhShiftPeriod(rhShift.getRhShiftPeriod());
			rhShiftAlert.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
			rhShiftAlert.setRhPersonResp(rhShift.getRhPerson());
			rhShiftAlert = shiftDAO.saveRhShift(rhShiftAlert);
			rhShiftAlert.setValidate(2);
			
			shiftDAO.updateRhShift(rhShift);
			
			rhShift.setDateFinishShift(rhShift.getDateFinish());
			shiftDetailService.process(rhShift, takenBreak);
			shiftDetailService2.process(rhShift, takenBreak);
			
			mailRhService.sendMailAlert(rhShiftAlert);
//			mailRhService.sendMailAlertMng(rhShiftAlert);
			return rhShiftAlert;
		}
		shiftDAO.updateRhShift(rhShift);
		shiftDetailService.process(rhShift, takenBreak);
		shiftDetailService2.process(rhShift, takenBreak);
		return rhShift;
	}
	
	
	public void registerFinishScaleRUM(int idRhScale, String dEnd)  throws ParseException{
		SimpleDateFormat formato= new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat horaFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		boolean takenBreak = false;
		RhShift rhShift = shiftDAO.getRhShift(idRhScale);
		RhScheduleScale rhScheduleScale = rhShift.getRhScheduleScale();
		if(rhScheduleScale.getRhSchedulePerson().getRhSchedule().getFlagBreak() == 1) takenBreak = true;
		Date nowDate = utilService.getNowDate(rhShift.getRhPerson());
		DateTime startShift = new DateTime(rhShift.getDateStartShift());
//		DateTime finishShift = new DateTime(nowDate);
		DateTime finishShift=new DateTime(horaFormat.parse(formato.format(nowDate)+" "+dEnd).getTime());
		
		DateTime schedulefinish = new DateTime(rhShift.getDateFinish());
		
		int difMinutesShift = Minutes.minutesBetween(startShift, finishShift).getMinutes();
		if (takenBreak) difMinutesShift = difMinutesShift - 60;
			
		rhShift.setDuration(difMinutesShift);
//		rhShift.setDateFinishShift(nowDate);
		rhShift.setDateFinishShift(finishShift.toDate());
		rhShift.setRhStatus(statusDAO.getRhStatus("SH_SCALE",2));
		rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
		rhShift.setRhPersonResp(null);
//		rhShift.setDateProcess(nowDate);
		rhShift.setDateProcess(finishShift.toDate());
		
		rhShift.setValidate(1);
		int difMinutes = Minutes.minutesBetween(schedulefinish, finishShift).getMinutes();
		
		if(difMinutes < rhScheduleScale.getRhSchedulePerson().getRhSchedule().getToleranceLeave()){//leave duty
			RhShift rhShiftAlert = new RhShift();
			rhShiftAlert.setRhTypePerm(typeDAO.getRhType("SH_ALERT", 1)); // leave duty
			rhShiftAlert.setRhType(typeDAO.getRhType("SHIFT", 11));
			rhShiftAlert.setRhTypeCharge(typeDAO.getRhType("SHIFT_DETAIL", 2));
			rhShiftAlert.setDuration(Math.abs(difMinutes));
			rhShiftAlert.setDateStart(finishShift.toDate());
			rhShiftAlert.setDateFinish(schedulefinish.toDate());
			rhShiftAlert.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 1));
			
			rhShiftAlert.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 4));
			rhShiftAlert.setDateStartShift(rhShift.getDateStart());
			rhShiftAlert.setDateFinishShift(rhShift.getDateFinish());
			rhShiftAlert.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
			rhShiftAlert.setRhPersonResp(rhShift.getRhPersonMng());
			rhShiftAlert.setReason("REASON CREATE FOR PANEL ADMINISTRATOR TYPE: "+rhShiftAlert.getRhType().getName());			
			
//			rhShiftAlert.setDateReg(nowDate);
			rhShiftAlert.setDateReg(finishShift.toDate());
			rhShiftAlert.setRhShiftParent(rhShift);
			rhShiftAlert.setRhPerson(rhShift.getRhPerson());
			rhShiftAlert.setRhPersonMng(rhShift.getRhPersonMng());
			rhShiftAlert.setRhShiftPeriod(rhShift.getRhShiftPeriod());
			rhShiftAlert.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
			rhShiftAlert.setRhPersonResp(rhShift.getRhPerson());
			rhShiftAlert = shiftDAO.saveRhShift(rhShiftAlert);
			rhShiftAlert.setValidate(2);
			
			shiftDAO.updateRhShift(rhShift);
			shiftDetailService.process(rhShift, takenBreak); // pay normal 
			shiftDetailService2.process(rhShift, takenBreak); // pay normal 
			
			mailRhService.sendMailAlert(rhShiftAlert);
//			mailRhService.sendMailAlertMng(rhShiftAlert);
//			return rhShiftAlert;
		}else if(difMinutes > rhScheduleScale.getRhSchedulePerson().getRhSchedule().getToleranceFinish()){
			RhShift rhShiftAlert = new RhShift();
			rhShiftAlert.setRhTypePerm(typeDAO.getRhType("SH_ALERT", 1)); // ADITIONAL TIME
			rhShiftAlert.setRhType(typeDAO.getRhType("SHIFT", 12));
			rhShiftAlert.setRhTypeCharge(typeDAO.getRhType("SHIFT_DETAIL", 5));
			rhShiftAlert.setDuration(difMinutes);
			rhShiftAlert.setDateStart(schedulefinish.toDate());
			rhShiftAlert.setDateFinish(finishShift.toDate());
			rhShiftAlert.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 1));
			
			rhShiftAlert.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 4));
			rhShiftAlert.setDateStartShift(rhShift.getDateStart());
			rhShiftAlert.setDateFinishShift(rhShift.getDateFinish());
			rhShiftAlert.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
			rhShiftAlert.setRhPersonResp(rhShift.getRhPersonMng());
			rhShiftAlert.setReason("REASON CREATE FOR PANEL ADMINISTRATOR TYPE: "+rhShiftAlert.getRhType().getName());
			
//			rhShiftAlert.setDateReg(nowDate);
			rhShiftAlert.setDateReg(finishShift.toDate());
			rhShiftAlert.setRhShiftParent(rhShift);
			rhShiftAlert.setRhPerson(rhShift.getRhPerson());
			rhShiftAlert.setRhPersonMng(rhShift.getRhPersonMng());
			rhShiftAlert.setRhShiftPeriod(rhShift.getRhShiftPeriod());
			rhShiftAlert.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
			rhShiftAlert.setRhPersonResp(rhShift.getRhPerson());
			rhShiftAlert = shiftDAO.saveRhShift(rhShiftAlert);
			rhShiftAlert.setValidate(2);
			
			shiftDAO.updateRhShift(rhShift);
			
			rhShift.setDateFinishShift(rhShift.getDateFinish());
			shiftDetailService.process(rhShift, takenBreak);
			shiftDetailService2.process(rhShift, takenBreak);
			
			mailRhService.sendMailAlert(rhShiftAlert);
//			mailRhService.sendMailAlertMng(rhShiftAlert);
//			return rhShiftAlert;
		}else{
			shiftDAO.updateRhShift(rhShift);
			shiftDetailService.process(rhShift, takenBreak);
			shiftDetailService2.process(rhShift, takenBreak);
		}
//		return rhShift;
	}
	
	
	public void registerStartScaleRUM(RhPerson rhPerson, int idRhShift, String dStart) throws ParseException {
		SimpleDateFormat formato= new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat horaFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		RhShift rhShift = shiftDAO.getRhShift(idRhShift);
		
		RhScheduleScale rhScheduleScale = rhShift.getRhScheduleScale();
		Date nowDate = utilService.getNowDate(rhPerson);
//		DateTime nowTime = new DateTime(nowDate);
		DateTime nowTime = new DateTime(horaFormat.parse(formato.format(nowDate)+" "+dStart).getTime());
		DateTime scheduleStart = new DateTime(rhShift.getDateStart());
		
		int difMinutes = Minutes.minutesBetween(scheduleStart, nowTime).getMinutes();
		rhShift.setRhStatus(statusDAO.getRhStatus("SH_SCALE", 1));
		rhShift.setDateStartShift(nowTime.toDate());
		rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 2));
		rhShift.setRhPersonResp(rhShift.getRhPerson());
		shiftDAO.updateRhShift(rhShift);
		rhShift.setValidate(1);
		// PENDING: Verify if the person has a permission for this day
		// LATE VALIDATION
		if(difMinutes > rhScheduleScale.getRhSchedulePerson().getRhSchedule().getToleranceAbsence()){
			RhShift rhShiftAlert = new RhShift();
			rhShiftAlert.setRhTypePerm(typeDAO.getRhType("SH_ALERT", 1)); // ABSENT
			rhShiftAlert.setRhType(typeDAO.getRhType("SHIFT", 13));
			rhShiftAlert.setRhTypeCharge(typeDAO.getRhType("SHIFT_DETAIL", 2));
			rhShiftAlert.setDuration(difMinutes);
			rhShiftAlert.setDateStart(scheduleStart.toDate());
			rhShiftAlert.setDateFinish(nowTime.toDate());
//			rhShiftAlert.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 1));
			
			rhShiftAlert.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 4));
			rhShiftAlert.setDateStartShift(rhShift.getDateStart());
			rhShiftAlert.setDateFinishShift(rhShift.getDateFinish());
			rhShiftAlert.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
			rhShiftAlert.setRhPersonResp(rhShift.getRhPersonMng());
			rhShiftAlert.setReason("REASON CREATE FOR PANEL ADMINISTRATOR TYPE: "+rhShiftAlert.getRhType().getName());
			
			rhShiftAlert.setDateReg(nowDate);
			rhShiftAlert.setRhShiftParent(rhShift);
			rhShiftAlert.setRhPerson(rhShift.getRhPerson());
			rhShiftAlert.setRhPersonMng(rhShift.getRhPersonMng());
			rhShiftAlert.setRhShiftPeriod(rhShift.getRhShiftPeriod());
			rhShiftAlert.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
			rhShiftAlert.setRhPersonResp(rhShift.getRhPerson());
			rhShiftAlert = shiftDAO.saveRhShift(rhShiftAlert);
			rhShiftAlert.setValidate(2);
			mailRhService.sendMailAlert(rhShiftAlert);
//			mailRhService.sendMailAlertMng(rhShiftAlert);
//			return rhShiftAlert;
			
		}else if(difMinutes > rhScheduleScale.getRhSchedulePerson().getRhSchedule().getToleranceDelay()){
			RhShift rhShiftAlert = new RhShift();
			rhShiftAlert.setRhTypePerm(typeDAO.getRhType("SH_ALERT", 1)); // ABSENT
			rhShiftAlert.setRhType(typeDAO.getRhType("SHIFT", 10));
			rhShiftAlert.setRhTypeCharge(typeDAO.getRhType("SHIFT_DETAIL", 2));
			rhShiftAlert.setDuration(difMinutes);
			rhShiftAlert.setDateStart(scheduleStart.toDate());
			rhShiftAlert.setDateFinish(nowTime.toDate());
//			rhShiftAlert.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 1));
			
			rhShiftAlert.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 4));
			rhShiftAlert.setDateStartShift(rhShift.getDateStart());
			rhShiftAlert.setDateFinishShift(rhShift.getDateFinish());
			rhShiftAlert.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
			rhShiftAlert.setRhPersonResp(rhShift.getRhPersonMng());
			rhShiftAlert.setReason("REASON CREATE FOR PANEL ADMINISTRATOR TYPE: "+rhShiftAlert.getRhType().getName());
			
			rhShiftAlert.setDateReg(nowDate);
			rhShiftAlert.setRhShiftParent(rhShift);
			rhShiftAlert.setRhPerson(rhShift.getRhPerson());
			rhShiftAlert.setRhPersonMng(rhShift.getRhPersonMng());
			rhShiftAlert.setRhShiftPeriod(rhShift.getRhShiftPeriod());
			rhShiftAlert.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
			rhShiftAlert.setRhPersonResp(rhShift.getRhPerson());
			rhShiftAlert = shiftDAO.saveRhShift(rhShiftAlert);
			rhShiftAlert.setValidate(2);
			mailRhService.sendMailAlert(rhShiftAlert);
//			mailRhService.sendMailAlertMng(rhShiftAlert);
//			return rhShiftAlert;
		}

//		return rhShift;
	}
	
	
	
	public RhShift get(int idRhShift) {
		RhShift rhShift = shiftDAO.getRhShift(idRhShift);
		rhShift.setRhFiles(fileDAO.getRhFile(rhShift));
		return rhShift;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.thradex.rrhh.service.ScheduleScaleService#saveJustification(org.thradex.model.RhShift)
	 * Save justification reason and change the alert status
	 */
	
	public RhShift saveJustification(RhShift rhShift) {
		
		rhShift.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 4));
		rhShift.setDateStartShift(rhShift.getDateStart());
		rhShift.setDateFinishShift(rhShift.getDateFinish());
		rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
		rhShift.setRhPersonResp(rhShift.getRhPersonMng());
		shiftDAO.updateRhShift(rhShift);
		
		utilService.saveFiles(rhShift);
		
		mailRhService.sendMailJustified(rhShift);
		return rhShift.getRhShiftParent();
	}
	
	
	public List<RhShift> listSession(RhPerson rhPerson){
		return shiftDAO.listMng(rhPerson, statusDAO.getRhStatus("SH_SCALE", 1), typeDAO.getRhType("SHIFT", 1));
	}
	
	
	public RhShift processAlert(RhShift rhShift){
		Date nowDate = utilService.getNowDate(rhShift.getRhPerson());
		
		rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
		rhShift.setDateProcess(nowDate);
		rhShift.setRhPersonResp(null);
		rhShift.setRhShiftPeriod(shiftPeriodService.getLimit(nowDate, rhShift.getRhPerson().getRhCompany()));
		
		if (rhShift.getRhType().getCode() == 10) { // LATE
			if(rhShift.isApproved()){
				rhShift.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 2));
				shiftDAO.updateRhShift(rhShift);
				shiftDetailService.processCero(rhShift);
				shiftDetailService2.processCero(rhShift);
			}else{
				rhShift.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 3));
				shiftDAO.updateRhShift(rhShift);
				shiftDetailService.process(rhShift, false);
				shiftDetailService2.process(rhShift, false);
			}
		}else if (rhShift.getRhType().getCode() == 13){ //ABSENT
			if(rhShift.isApproved()){
				rhShift.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 2));
				shiftDAO.updateRhShift(rhShift);
				shiftDetailService.processCero(rhShift);
				shiftDetailService2.processCero(rhShift);
			}else{
				rhShift.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 3));
				shiftDAO.updateRhShift(rhShift);
				shiftDetailService.process(rhShift, false);
				shiftDetailService2.process(rhShift, false);
			}
		}else if (rhShift.getRhType().getCode() == 12){ // ADITIONAL TIME
			if(rhShift.isApproved()){
				rhShift.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 2));
				shiftDAO.updateRhShift(rhShift);
				shiftDetailService.process(rhShift, false);
				shiftDetailService2.process(rhShift, false);
			}else{
				rhShift.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 3));
				RhShift rhShiftParent = rhShift.getRhShiftParent();
				rhShiftParent.setDateFinishShift(rhShiftParent.getDateFinish());
				shiftDAO.updateRhShift(rhShift);
				shiftDAO.updateRhShift(rhShiftParent);
				shiftDetailService.processCero(rhShift);
				shiftDetailService2.processCero(rhShift);
			}
		}else if (rhShift.getRhType().getCode() == 11){ // LEAVE ON DUTY
			if(rhShift.isApproved()){
				rhShift.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 2));
				shiftDAO.updateRhShift(rhShift);
				shiftDetailService.processCero(rhShift);
				shiftDetailService2.processCero(rhShift);
			}else{
				rhShift.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 3));
				shiftDAO.updateRhShift(rhShift);
				shiftDetailService.process(rhShift, false);
				shiftDetailService2.process(rhShift, false);
			}
		}else if (rhShift.getRhType().getCode() == 14){ //ABSENT ALL DAY
			if(rhShift.isApproved()){
				
				rhShift.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 2));
//				rhShift.setRhType(typeDAO.getRhType("SHIFT_DETAIL", 1));
				shiftDAO.updateRhShift(rhShift);
				shiftDetailService.processCero(rhShift);
				shiftDetailService2.processCero(rhShift);
			}else{
				rhShift.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 3));
				shiftDAO.updateRhShift(rhShift);
				shiftDetailService.process(rhShift, false);
				shiftDetailService2.process(rhShift, false);
			}
		}else{
			log.error("no type of alert... or NONE TYPE... ");
		}
		mailRhService.sendMailProcess(rhShift);
		return rhShift;
	}
	
	
	public void createScale() {
		log.info("Start creating scales... ");
		DateTime now = new DateTime();
		List<RhSchedulePerson> rhSchedulePersons = 
				schedulePersonDAO.list(statusDAO.getRhStatus("RH_SCH_PERSON", 1), 
										typeDAO.getRhType("RH_SCHEDULE", 1)) ;
		
		for (RhSchedulePerson rhSchedulePerson : rhSchedulePersons) {
			RhScheduleDay rhScheduleDay = scheduleDayDAO.get(now.toDate(), 
								rhSchedulePerson.getRhSchedule());
			RhScheduleScale rhScheduleScaleDay = scheduleScaleDAO.get(rhSchedulePerson.getRhPerson(),
					statusDAO.getRhStatus("RH_SCH_SCALE", 2), now.toDate());
			
			if (rhScheduleDay != null && rhScheduleScaleDay == null) {
				DateTime start = new DateTime(now.getYear(), now.getMonthOfYear(),
						now.getDayOfMonth(), rhScheduleDay.getStartHour(), rhScheduleDay.getStartMin()); //DateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour)
				DateTime finish = start.plusHours(rhScheduleDay.getTotalHours());
				
				//Permission approved and is on this date
				RhShift rhPermission = shiftDAO.get(rhSchedulePerson.getRhPerson(), statusDAO.getRhStatus("SH_PERMISSION", 2), 
						typeDAO.getRhType("SHIFT", 3), now.toDate());
				//Permission approved and is on this date
				RhShift rhLicense = shiftDAO.get(rhSchedulePerson.getRhPerson(), statusDAO.getRhStatus("SH_LICENSE", 2), 
						typeDAO.getRhType("SHIFT", 5), now.toDate());
				//Permission approved and is on this date
				RhShift rhAbroadJob = shiftDAO.get(rhSchedulePerson.getRhPerson(), statusDAO.getRhStatus("SH_ABROADJOB", 2), 
						typeDAO.getRhType("SHIFT", 6), now.toDate());
				//Permission approved and is on this date
				RhShift rhCommission = shiftDAO.get(rhSchedulePerson.getRhPerson(), statusDAO.getRhStatus("SH_COMMISSION", 1), 
						typeDAO.getRhType("SHIFT", 6), now.toDate());
				
				if (rhPermission != null) {//check permission
					DateTime startPerm = new DateTime(rhPermission.getDateStart());
					DateTime finishPerm = new DateTime(rhPermission.getDateFinish());
					
					if(start.equals(startPerm) && finishPerm.isBefore(finish)){
						RhScheduleScale rhScheduleScale = saveRhScheduleScale(now.toDate(), rhSchedulePerson, rhScheduleDay);
						saveRhScale(rhPermission.getDateFinishShift(), finish.toDate(), now.toDate(), 
								rhSchedulePerson, rhScheduleScale);
						
					}else if(finishPerm.isEqual(finish) && startPerm.isAfter(start)){
						RhScheduleScale rhScheduleScale =  saveRhScheduleScale(now.toDate(), rhSchedulePerson, rhScheduleDay);
						saveRhScale(start.toDate(), rhPermission.getDateStartShift(), now.toDate(), rhSchedulePerson, rhScheduleScale);
						
					}else if (startPerm.isAfter(start) && finishPerm.isBefore(finish)) {
						RhScheduleScale rhScheduleScale = saveRhScheduleScale(now.toDate(), rhSchedulePerson, rhScheduleDay);
						saveRhScale(start.toDate(), rhPermission.getDateStartShift(), now.toDate(), rhSchedulePerson, rhScheduleScale);
						saveRhScale(rhPermission.getDateFinishShift(), finish.toDate(), now.toDate(), rhSchedulePerson, rhScheduleScale);
						
					}
				}else if (rhCommission != null) {//check commission
					DateTime startPerm = new DateTime(rhCommission.getDateStart());
					DateTime finishPerm = new DateTime(rhCommission.getDateFinish());
					
					if(start.equals(startPerm) && finishPerm.isBefore(finish)){
						RhScheduleScale rhScheduleScale = saveRhScheduleScale(now.toDate(), rhSchedulePerson, rhScheduleDay);
						saveRhScale(rhCommission.getDateFinishShift(), finish.toDate(), now.toDate(), 
								rhSchedulePerson, rhScheduleScale);
						
					}else if(finishPerm.isEqual(finish) && startPerm.isAfter(start)){
						RhScheduleScale rhScheduleScale =  saveRhScheduleScale(now.toDate(), rhSchedulePerson, rhScheduleDay);
						saveRhScale(start.toDate(), rhCommission.getDateStartShift(), now.toDate(), rhSchedulePerson, rhScheduleScale);
						
					}else if (startPerm.isAfter(start) && finishPerm.isBefore(finish)) {
						RhScheduleScale rhScheduleScale = saveRhScheduleScale(now.toDate(), rhSchedulePerson, rhScheduleDay);
						saveRhScale(start.toDate(), rhCommission.getDateStartShift(), now.toDate(), rhSchedulePerson, rhScheduleScale);
						saveRhScale(rhCommission.getDateFinishShift(), finish.toDate(), now.toDate(), rhSchedulePerson, rhScheduleScale);
						
					}
				}else if (rhLicense != null) {//check license
					 saveRhScheduleScale(now.toDate(), rhSchedulePerson, rhScheduleDay);
					//create SH_LICENSE_DET no create a shift scale
					licenseService.process(rhLicense, start.toDate(), finish.toDate(), rhSchedulePerson);
					
				}else if (rhAbroadJob != null) {//check commission
					saveRhScheduleScale(now.toDate(), rhSchedulePerson, rhScheduleDay);
					//create Shift Commission no create a shift scale
					abroadJobService.process(rhAbroadJob, start.toDate(), finish.toDate(), rhSchedulePerson);
					
				}else{//no event register, normal procedure.
					RhScheduleScale rhScheduleScale = saveRhScheduleScale(now.toDate(), rhSchedulePerson, rhScheduleDay);
					saveRhScale(start.toDate(), finish.toDate(), now.toDate(), rhSchedulePerson, rhScheduleScale);
				}
			}else{
				log.error("There is no schedule for : " + rhSchedulePerson.getRhPerson().getFullname());
			}
		}
		
		List<RhScheduleScale> rhScheduleScales = 
				scheduleScaleDAO.list(statusDAO.getRhStatus("RH_SCH_SCALE", 1), now.toDate()) ;
		for (RhScheduleScale rhScheduleScale : rhScheduleScales) {
			RhSchedulePerson rhSchedulePerson = rhScheduleScale.getRhSchedulePerson();			
			RhScheduleDay rhScheduleDay = rhScheduleScale.getRhScheduleDay();
			DateTime start = new DateTime(now.getYear(), now.getMonthOfYear(),
					now.getDayOfMonth(), rhScheduleDay.getStartHour(), rhScheduleDay.getStartMin()); //DateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour)
			DateTime finish = start.plusHours(rhScheduleDay.getTotalHours());
			
			//Permission approved and is on this date
			RhShift rhPermission = shiftDAO.get(rhSchedulePerson.getRhPerson(), statusDAO.getRhStatus("SH_PERMISSION", 2), 
					typeDAO.getRhType("SHIFT", 3), now.toDate());
			//Permission approved and is on this date
			RhShift rhLicense = shiftDAO.get(rhSchedulePerson.getRhPerson(), statusDAO.getRhStatus("SH_LICENSE", 2), 
					typeDAO.getRhType("SHIFT", 5), now.toDate());
			//Permission approved and is on this date
			RhShift rhAbroadJob = shiftDAO.get(rhSchedulePerson.getRhPerson(), statusDAO.getRhStatus("SH_ABROADJOB", 2), 
					typeDAO.getRhType("SHIFT", 6), now.toDate());
			//Permission approved and is on this date
			RhShift rhCommission = shiftDAO.get(rhSchedulePerson.getRhPerson(), statusDAO.getRhStatus("SH_COMMISSION", 1), 
					typeDAO.getRhType("SHIFT", 6), now.toDate());
			
			if (rhPermission != null) {//check permission
				DateTime startPerm = new DateTime(rhPermission.getDateStart());
				DateTime finishPerm = new DateTime(rhPermission.getDateFinish());
				
				if(start.equals(startPerm) && finishPerm.isBefore(finish)){
					rhScheduleScale.setRhStatus(statusDAO.getRhStatus("RH_SCH_SCALE", 2));
					scheduleScaleDAO.update(rhScheduleScale);
					saveRhScale(rhPermission.getDateFinishShift(), finish.toDate(), now.toDate(), 
							rhSchedulePerson, rhScheduleScale);
					
				}else if(finishPerm.isEqual(finish) && startPerm.isAfter(start)){
					rhScheduleScale.setRhStatus(statusDAO.getRhStatus("RH_SCH_SCALE", 2));
					scheduleScaleDAO.update(rhScheduleScale);
					saveRhScale(start.toDate(), rhPermission.getDateStartShift(), now.toDate(), rhSchedulePerson, rhScheduleScale);
					
				}else if (startPerm.isAfter(start) && finishPerm.isBefore(finish)) {
					rhScheduleScale.setRhStatus(statusDAO.getRhStatus("RH_SCH_SCALE", 2));
					scheduleScaleDAO.update(rhScheduleScale);
					saveRhScale(start.toDate(), rhPermission.getDateStartShift(), now.toDate(), rhSchedulePerson, rhScheduleScale);
					saveRhScale(rhPermission.getDateFinishShift(), finish.toDate(), now.toDate(), rhSchedulePerson, rhScheduleScale);
					
				}
			}else if (rhCommission != null) {//check commission
				DateTime startPerm = new DateTime(rhCommission.getDateStart());
				DateTime finishPerm = new DateTime(rhCommission.getDateFinish());
				
				if(start.equals(startPerm) && finishPerm.isBefore(finish)){
					rhScheduleScale.setRhStatus(statusDAO.getRhStatus("RH_SCH_SCALE", 2));
					scheduleScaleDAO.update(rhScheduleScale);
					saveRhScale(rhCommission.getDateFinishShift(), finish.toDate(), now.toDate(), 
							rhSchedulePerson, rhScheduleScale);
					
				}else if(finishPerm.isEqual(finish) && startPerm.isAfter(start)){
					rhScheduleScale.setRhStatus(statusDAO.getRhStatus("RH_SCH_SCALE", 2));
					scheduleScaleDAO.update(rhScheduleScale);
					saveRhScale(start.toDate(), rhCommission.getDateStartShift(), now.toDate(), rhSchedulePerson, rhScheduleScale);
					
				}else if (startPerm.isAfter(start) && finishPerm.isBefore(finish)) {
					rhScheduleScale.setRhStatus(statusDAO.getRhStatus("RH_SCH_SCALE", 2));
					scheduleScaleDAO.update(rhScheduleScale);
					saveRhScale(start.toDate(), rhCommission.getDateStartShift(), now.toDate(), rhSchedulePerson, rhScheduleScale);
					saveRhScale(rhCommission.getDateFinishShift(), finish.toDate(), now.toDate(), rhSchedulePerson, rhScheduleScale);
					
				}
			}else if (rhLicense != null) {//check license
				rhScheduleScale.setRhStatus(statusDAO.getRhStatus("RH_SCH_SCALE", 2));
				scheduleScaleDAO.update(rhScheduleScale);
				//create SH_LICENSE_DET no create a shift scale
				licenseService.process(rhLicense, start.toDate(), finish.toDate(), rhScheduleScale.getRhSchedulePerson());
				
			}else if (rhAbroadJob != null) {//check commission
				rhScheduleScale.setRhStatus(statusDAO.getRhStatus("RH_SCH_SCALE", 2));
				scheduleScaleDAO.update(rhScheduleScale);
				//create Shift Commission no create a shift scale
				abroadJobService.process(rhAbroadJob, start.toDate(), finish.toDate(), rhScheduleScale.getRhSchedulePerson());
				
			}else{//no event register, normal procedure.
				rhScheduleScale.setRhStatus(statusDAO.getRhStatus("RH_SCH_SCALE", 2));
				scheduleScaleDAO.update(rhScheduleScale);
				saveRhScale(start.toDate(), finish.toDate(), now.toDate(), rhSchedulePerson, rhScheduleScale);
			}
		}
		
		log.info("Finish creating scales... ");
	}

	
	public void checkActiveSessions() {
		log.info("Start cheking active session... ");
		List<RhShift> rhShifts = shiftDAO.list(statusDAO.getRhStatus("SH_SCALE", 1), typeDAO.getRhType("SHIFT", 1));
		Date now = new Date();
		DateTime nowTime = new DateTime();
		DateTime finish = new DateTime();
		DateTime startShift = new DateTime();
		for (RhShift rhShift : rhShifts) {
			now = utilService.getNowDate(rhShift.getRhPerson());
			nowTime = new DateTime(now);
			finish = new DateTime(rhShift.getDateFinish());
			startShift = new DateTime(rhShift.getDateStartShift());
			log.info(" Hours.hoursBetween(finish, nowTime).getHours()  = " + Hours.hoursBetween(finish, nowTime).getHours());
			if(Hours.hoursBetween(finish, nowTime).getHours() > 4){
				log.info("Automatic close shift id: " + rhShift.getId());
				boolean takenBreak = false;
				if(rhShift.getRhScheduleScale() != null &&
						rhShift.getRhScheduleScale().getRhSchedulePerson().getRhSchedule().getFlagBreak() == 1) 
					takenBreak = true;
				
				rhShift.setDateFinishShift(rhShift.getDateFinish());
				rhShift.setDuration(Minutes.minutesBetween(startShift, finish).getMinutes());
				rhShift.setRhStatus(statusDAO.getRhStatus("SH_SCALE",2));
				rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
				rhShift.setRhPersonResp(null);
				rhShift.setComment("Atomatic close after 5 hours from finish time...");
				rhShift.setDateProcess(now);
				shiftDAO.updateRhShift(rhShift);
				shiftDetailService.process(rhShift, takenBreak);
				shiftDetailService2.process(rhShift, takenBreak);
			}
		}
		log.info("Finish cheking active session... ");
	}
	
	
	public void checkAbsentSessions() {
		log.info("Start cheking absent session... ");
		List<RhShift> rhShifts = shiftDAO.list(statusDAO.getRhStatus("SH_SCALE", 3), typeDAO.getRhType("SHIFT", 1));
		Date now = new Date();
		DateTime nowTime = new DateTime();
		DateTime finish = new DateTime();
		DateTime start = new DateTime();
		int difMinutes = 0;
		
		for (RhShift rhShift : rhShifts) {
			
			now = utilService.getNowDate(rhShift.getRhPerson());
			nowTime = new DateTime(now);
			finish = new DateTime(rhShift.getDateFinish());
			start = new DateTime(rhShift.getDateStart());
			difMinutes = Minutes.minutesBetween(start, finish).getMinutes();
			
			if (nowTime.isAfter(finish)){
//				rhShift.setDateFinishShift(rhShift.getDateFinish());
//				rhShift.setDateStartShift(rhShift.getDateStart());
//				rhShift.setDuration(0);
//				rhShift.setRhStatus(statusDAO.getRhStatus("SH_SCALE",2));
//				rhShift.setComment("Atomatic close Absente...");
//				rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
//				rhShift.setDateProcess(now);
//				shiftDAO.updateRhShift(rhShift);
//				shiftDetailService.process(typeDAO.getRhType("SHIFT_DETAIL", 1), rhShift);
				
//				RhShift rhShiftAlert = new RhShift();
				rhShift.setRhTypePerm(typeDAO.getRhType("SH_ALERT", 1)); // ABSENT
				rhShift.setRhType(typeDAO.getRhType("SHIFT", 14));
				rhShift.setDuration(difMinutes);
				rhShift.setDateStartShift(rhShift.getDateStart());
				rhShift.setDateFinishShift(rhShift.getDateFinish());
				rhShift.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 1));
				rhShift.setDateReg(now);
				rhShift.setRhShiftParent(rhShift);
				rhShift.setRhPerson(rhShift.getRhPerson());
				rhShift.setRhPersonResp(rhShift.getRhPerson());
				rhShift.setRhPersonMng(rhShift.getRhPersonMng());
				rhShift.setRhShiftPeriod(shiftPeriodService.get(rhShift.getDateStart(), 
						rhShift.getRhPerson().getRhCompany()));
				rhShift.setComment("Atomatic Absente all day Alert...");
				rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
				shiftDAO.updateRhShift(rhShift);
			}
		}
		log.info("Finish cheking active session... ");
	}
	
	
	public void processAlertsLastPeriod(RhShiftPeriod rhShiftPeriod){
		List<RhShift> lRhShiftsPending = new ArrayList<RhShift>();
		lRhShiftsPending.addAll(shiftDAO.list(statusDAO.getRhStatus("SH_ALERT", 1), rhShiftPeriod));
		lRhShiftsPending.addAll(shiftDAO.list(statusDAO.getRhStatus("SH_ALERT", 4), rhShiftPeriod));
//		RhType rhTypeDiscount = typeDAO.getRhType("SHIFT_DETAIL", 2);
//		RhType rhTypeExtra = typeDAO.getRhType("SHIFT_DETAIL", 5);
//		RhType rhTypePay = typeDAO.getRhType("SHIFT_DETAIL", 1);
		
		for (RhShift rhShift : lRhShiftsPending) {
			
			RhShift rhShiftClon = rhShift.clone();
			Date nowDate = utilService.getNowDate(rhShift.getRhPerson());
			rhShiftClon.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 3));
			rhShiftClon.setDateProcess(nowDate);
			rhShiftClon.setDateStartShift(rhShiftClon.getDateStart());
			rhShiftClon.setDateFinishShift(rhShiftClon.getDateFinish());
			rhShiftClon.setRhPersonResp(null);
			rhShiftClon.setRhShiftPeriod(rhShiftPeriod);
			
			if (rhShiftClon.getRhType().getCode() == 10) { // LATE
				rhShiftClon.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 5));
				shiftDAO.saveRhShift(rhShiftClon);
				shiftDetailService.process(rhShiftClon, false);
				shiftDetailService2.process(rhShiftClon, false);
			}else if (rhShiftClon.getRhType().getCode() == 13){ //ABSENT
				rhShiftClon.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 5));
				shiftDAO.saveRhShift(rhShiftClon);
				shiftDetailService.process(rhShiftClon, false);
				shiftDetailService2.process(rhShiftClon, false);
			}else if (rhShiftClon.getRhType().getCode() == 12){ // ADITIONAL TIME
				rhShiftClon.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 5));
				shiftDAO.saveRhShift(rhShiftClon);
			}else if (rhShiftClon.getRhType().getCode() == 11){ // LEAVE ON DUTY
				rhShiftClon.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 5));
				shiftDAO.saveRhShift(rhShiftClon);
				shiftDetailService.process(rhShiftClon, false);
				shiftDetailService2.process(rhShiftClon, false);
			}else if (rhShiftClon.getRhType().getCode() == 14){ //ABSENT ALL DAY
				rhShiftClon.setRhStatus(statusDAO.getRhStatus("SH_ALERT", 5));
				shiftDAO.saveRhShift(rhShiftClon);
				shiftDetailService.process(rhShiftClon, false);
				shiftDetailService2.process(rhShiftClon, false);
			}else{
				log.error("no type of alert... or NONE TYPE... ");
			}
		}
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.thradex.rrhh.service.ScheduleScaleService#deleteScheduleScale(int)
	 * 
	 * Methods for dynamic scales 
	 * 
	 */
	
	
	public List<RhSchedulePerson> listRhSchedulePerson(RhPerson rhPerson) {
		log.info("Getting list RhSchedulePerson");
		List<RhSchedulePerson> rhSchedulePersons = new ArrayList<RhSchedulePerson>();
		RhStatus rhStatus = statusDAO.getRhStatus("PERSON", 1);
		List<RhPerson> rhPersons = personService.list(rhPerson.getRhCompany(), rhStatus);
		
		RhType rhTypeDinamic = typeDAO.getRhType("RH_SCHEDULE", 2); // dynamic Schedule
		RhStatus rhStatusActive = statusDAO.getRhStatus("RH_SCH_PERSON", 1); // Active schedule person.
		for (RhPerson rhPerson2 : rhPersons) {
//			log.info("Sub full name " + rhPerson2.getFullname());
			RhSchedulePerson rhSchedulePerson = schedulePersonDAO.get(rhPerson2, rhStatusActive, rhTypeDinamic);
			if(rhSchedulePerson != null ) {
				rhSchedulePerson.setRhScheduleDays(scheduleDayDAO.list(rhSchedulePerson.getRhSchedule()));
				rhSchedulePersons.add(rhSchedulePerson);
			}
		}
		return rhSchedulePersons;
	}
	
	
	public void deleteScheduleScale(int idRhScheduleScale){
		RhScheduleScale rhScheduleScale = scheduleScaleDAO.get(idRhScheduleScale);
		DateTime date = new DateTime(rhScheduleScale.getDate());
		DateTime now = new DateTime(utilService.getNowDate(rhScheduleScale.getRhSchedulePerson().getRhPerson()));
		if (date.isAfter(now)) {
			scheduleScaleDAO.delete(rhScheduleScale);
		}else {
			log.error("Start date is a");
			throw new CustomGenericException("RH_SCH_SCALE 01 ","Error on the start date.");
		}
	}
	
	
	public void createScheduleScale(int idRhPerson, int idRhScheduleDay, String startDate){
		RhPerson rhPerson = personService.get(idRhPerson);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		DateTime date = new DateTime();
		DateTime now = new DateTime(utilService.getNowDate(rhPerson));
		try {
			date = new DateTime(formatter.parse(startDate));
			RhScheduleScale rhScheduleScale = scheduleScaleDAO.get(rhPerson, date.toDate());
			if (date.isBefore(now)) {
				log.error("Start date is a");
				throw new CustomGenericException("RH_SCH_SCALE 01 ","Error on the start date.");
			}else if (rhScheduleScale != null) {
				log.error("Double scale for the same person on one day");
				throw new CustomGenericException("RH_SCH_SCALE 02 ","Two scales on the same day");
			}else{
				rhScheduleScale = new RhScheduleScale();
				rhScheduleScale.setDate(date.toDate());
				rhScheduleScale.setRhSchedulePerson(schedulePersonDAO.get(rhPerson));
				rhScheduleScale.setRhScheduleDay(scheduleDayDAO.get(idRhScheduleDay));
				rhScheduleScale.setRhStatus(statusDAO.getRhStatus("RH_SCH_SCALE", 1));
				rhScheduleScale.setComment("Dynamic scale.. created from the web.");
				scheduleScaleDAO.save(rhScheduleScale);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			log.error("Error tring parse String to date... ");
		}
	}
	
	
	public List<Map<String, Object>> listScheduleScale(RhPerson rhPerson, String from, String to){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date fromDate  = new Date();
		Date toDate = new Date();
		List<Map<String, Object>>  list = new ArrayList<Map<String,Object>>();
		
		try {
			fromDate  = format.parse(from);
			toDate = format.parse(to);
			
			List<RhSchedulePerson> rhPersons = listRhSchedulePerson(rhPerson);
			for (RhSchedulePerson rhSchedulePerson : rhPersons) {
				List<RhScheduleScale> rhScheduleScales = scheduleScaleDAO.list(rhSchedulePerson, fromDate, toDate);
				
				for (RhScheduleScale rhScheduleScale : rhScheduleScales) {
					DateTime time = new DateTime(rhScheduleScale.getDate());
					DateTime timeEnd = time.plusHours(rhScheduleScale.getRhScheduleDay().getTotalHours());
					
					Map<String, Object> map =  new HashMap<String, Object>();
					
					map.put("title", rhScheduleScale.getRhSchedulePerson().getRhPerson().getShortname() + " ("
							+convertIntToString2(rhScheduleScale.getRhScheduleDay().getStartHour())+":"
							+convertIntToString2(rhScheduleScale.getRhScheduleDay().getStartMin()) + " - "	
							+convertIntToString2(rhScheduleScale.getRhScheduleDay().getFinishHour())+":"
							+convertIntToString2(rhScheduleScale.getRhScheduleDay().getFinishMin()) +")");
					
					map.put("start", format.format(rhScheduleScale.getDate())+"T"
							+convertIntToString2(rhScheduleScale.getRhScheduleDay().getStartHour())+":"
							+convertIntToString2(rhScheduleScale.getRhScheduleDay().getStartMin()) );
					
					map.put("end", format.format(timeEnd.toDate())+"T"
							+convertIntToString2(rhScheduleScale.getRhScheduleDay().getFinishHour())+":"
							+convertIntToString2(rhScheduleScale.getRhScheduleDay().getFinishMin()) );
					map.put("color", rhScheduleScale.getRhScheduleDay().getColor());
					map.put("id", rhScheduleScale.getId());
					map.put("idPerson", rhScheduleScale.getRhSchedulePerson().getRhPerson().getId());
					map.put("idScale", rhScheduleScale.getRhScheduleDay().getId());
					list.add(map);
				}
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<Map<String, Object>> listScheduleScaleStaff(RhPerson rhPerson, String from, String to){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date fromDate  = new Date();
		Date toDate = new Date();
		List<Map<String, Object>>  list = new ArrayList<Map<String,Object>>();
		
		try {
			fromDate  = format.parse(from);
			toDate = format.parse(to);
			RhSchedulePerson rhSchedulePerson = schedulePersonDAO.get(rhPerson);
			List<RhScheduleScale> rhScheduleScales = scheduleScaleDAO.list(rhSchedulePerson, fromDate, toDate);
			for (RhScheduleScale rhScheduleScale : rhScheduleScales) {
				DateTime time = new DateTime(rhScheduleScale.getDate());
				DateTime timeEnd = time.plusHours(rhScheduleScale.getRhScheduleDay().getTotalHours());
				
				Map<String, Object> map =  new HashMap<String, Object>();
				
				map.put("title", rhScheduleScale.getRhSchedulePerson().getRhPerson().getShortname() + " ("
						+convertIntToString2(rhScheduleScale.getRhScheduleDay().getStartHour())+":"
						+convertIntToString2(rhScheduleScale.getRhScheduleDay().getStartMin()) + " - "	
						+convertIntToString2(rhScheduleScale.getRhScheduleDay().getFinishHour())+":"
						+convertIntToString2(rhScheduleScale.getRhScheduleDay().getFinishMin()) +")");
				
				map.put("start", format.format(rhScheduleScale.getDate())+"T"
						+convertIntToString2(rhScheduleScale.getRhScheduleDay().getStartHour())+":"
						+convertIntToString2(rhScheduleScale.getRhScheduleDay().getStartMin()) );
				
				map.put("end", format.format(timeEnd.toDate())+"T"
						+convertIntToString2(rhScheduleScale.getRhScheduleDay().getFinishHour())+":"
						+convertIntToString2(rhScheduleScale.getRhScheduleDay().getFinishMin()) );
				map.put("color", rhScheduleScale.getRhScheduleDay().getColor());
				map.put("id", rhScheduleScale.getId());
				map.put("idPerson", rhScheduleScale.getRhSchedulePerson().getRhPerson().getId());
				map.put("idScale", rhScheduleScale.getRhScheduleDay().getId());
				list.add(map);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/*
	 * HELPER METHODS ONLY PRIVATE USE ONLY IN THIS CLASS
	 * 
	 */
	
	private RhShift saveRhScale(Date start, Date finish, Date now, RhSchedulePerson rhSchedulePerson, RhScheduleScale rhScheduleScale){
		RhShift rhShift = new RhShift();
		try {
			rhShift.setDateStart(start);
			rhShift.setDateFinish(finish);
			rhShift.setRhPerson(rhSchedulePerson.getRhPerson());
			rhShift.setRhTypeCharge(typeDAO.getRhType("SHIFT_DETAIL", 1));
			rhShift.setRhType(typeDAO.getRhType("SHIFT", 1)); //REGULAR SHIFT 
			rhShift.setRhStatus(statusDAO.getRhStatus("SH_SCALE", 3));
			rhShift.setRhStatusSis(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
			rhShift.setRhPersonMng(personService.getManager(rhSchedulePerson.getRhPerson()));
			rhShift.setDateReg(now);
			rhShift.setRhShiftPeriod(shiftPeriodService.get(rhShift.getDateStart(), rhSchedulePerson.getRhPerson().getRhCompany()));
			rhShift.setComment("Automatic Asgnation Shift... ");
			rhShift.setRhPersonResp(rhShift.getRhPerson());
			rhShift.setRhScheduleScale(rhScheduleScale);
		} catch (Exception e) {
			log.error("No proccessed info for " +rhSchedulePerson.getRhPerson().getFullname());
			e.printStackTrace();
		}

		return shiftDAO.saveRhShift(rhShift);
	}
	
	private RhScheduleScale saveRhScheduleScale(Date now, RhSchedulePerson rhSchedulePerson, RhScheduleDay rhScheduleDay){
		//Register Schedule Scale for avoid redundant info on database
		RhScheduleScale rhScheduleScale = new RhScheduleScale();
		rhScheduleScale.setDate(now);
		rhScheduleScale.setRhScheduleDay(rhScheduleDay);
		rhScheduleScale.setRhSchedulePerson(rhSchedulePerson);
		rhScheduleScale.setRhStatus(statusDAO.getRhStatus("RH_SCH_SCALE", 2));
		rhScheduleScale.setComment("Atomatic created at : " + now.toString());
		return scheduleScaleDAO.save(rhScheduleScale);
	}
	
	private String convertIntToString2(int num){
		String newNum =  num+"";
		if (newNum.length() == 1) 
			return "0"+newNum;
		else 
			return newNum;
			
	}

	
	public boolean isManager(RhPerson rhPerson){
		return personService.isManager(rhPerson);
	}
//	private DateTime getSatrtTimeScale(RhScheduleScale rhScheduleScale){
//		DateTime scheduleStart = new DateTime(rhScheduleScale.getDate());
//		scheduleStart = scheduleStart.plusHours(rhScheduleScale.getRhScheduleDay().getStartHour());
//		scheduleStart = scheduleStart.plusMinutes(rhScheduleScale.getRhScheduleDay().getStartMin());
//		return scheduleStart;
//	}

}