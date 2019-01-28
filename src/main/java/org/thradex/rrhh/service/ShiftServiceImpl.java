package org.thradex.rrhh.service;

import org.springframework.transaction.annotation.Transactional;
import org.thradex.model.RhFile;
import org.thradex.model.RhPerson;
import org.thradex.model.RhSchedulePerson;
//import org.thradex.model.RhScheduleScale;
import org.thradex.model.RhShift;
import org.thradex.model.RhShiftDetail;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;
import org.thradex.rrhh.dao.FileDAO;
import org.thradex.rrhh.dao.SchedulePersonDAO;
import org.thradex.rrhh.dao.ShiftDAO;
import org.thradex.rrhh.dao.ShiftPerioDAO;
import org.thradex.rrhh.dao.StatusDAO;
import org.thradex.rrhh.dao.TypeDAO;
import org.thradex.util.ConstantsSis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ShiftServiceImpl implements ShiftService {
	
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);
	

	@Autowired
	private UtilRrhhService utilRrhhService;
	
	@Autowired
	private ShiftDAO shiftDAO;
	
	@Autowired
	private ShiftDetailService shiftDetailService;
	
//	@Autowired
//	private ShiftDetailService2 shiftDetailService2;
	
	@Autowired
	private ShiftPerioDAO shiftPerioDAO;
	
	@Autowired
	private StatusDAO statusDAO;
	
	@Autowired
	private TypeDAO typeDAO;
	
	@Autowired
	private FileDAO fileDAO;
	
	@Autowired
	private SchedulePersonDAO schedulePersonDAO;
	
	@Override
	public void updateResponsible(){
		List<RhShift> list = shiftDAO.listResponsibleNull(statusDAO.getRhStatus("SH_SIS_STATUS", 1));
		list.addAll(shiftDAO.listResponsibleNull(statusDAO.getRhStatus("SH_SIS_STATUS", 2)));
		for (RhShift rhShift : list) {
			if(rhShift.getRhType().getId() == 1 || 
					rhShift.getRhType().getId() == 2 || 
					rhShift.getRhType().getId() == 6 ||
					rhShift.getRhType().getId() == 15 ){// scale, abroad job, schedule job, commission 
				rhShift.setRhPersonResp(rhShift.getRhPerson());
			}else if(rhShift.getRhType().getId() == 3 || 
					rhShift.getRhType().getId() == 4 || 
					rhShift.getRhType().getId() == 5){ // permission, license, extra hour
				if(rhShift.getRhStatus().getCode() == 1){
					rhShift.setRhPersonResp(rhShift.getRhPersonMng());
				}
			}else if(rhShift.getRhType().getId() == 7 || 
					rhShift.getRhType().getId() == 10 || 
					rhShift.getRhType().getId() == 11 || 
					rhShift.getRhType().getId() == 12 || 
					rhShift.getRhType().getId() == 13 || 
					rhShift.getRhType().getId() == 14 ){//alerts
				if(rhShift.getRhStatus().getCode() == 1){//pending
					rhShift.setRhPersonResp(rhShift.getRhPerson());
				}else if(rhShift.getRhStatus().getCode() == 4){
					rhShift.setRhPersonResp(rhShift.getRhPersonMng());
				}
			}else if(rhShift.getRhType().getId() == 7 || 
					rhShift.getRhType().getId() == 10){
				rhShift.setRhPersonResp(rhShift.getRhPerson());
			}
			shiftDAO.updateRhShift(rhShift);
			log.info("Updated respondible shift id: " + rhShift.getId());
		}
	}
	
	@Override
	public void updateTypeCharge(){
		List<RhShift> list = shiftDAO.listTypeChargeNull();
		RhType rhTypeCharge1 = typeDAO.getRhType("SHIFT_DETAIL", 1); // worked time
		RhType rhTypeCharge2 = typeDAO.getRhType("SHIFT_DETAIL", 2); // discount time
//		RhType rhTypeCharge3 = typeDAO.getRhType("SHIFT_DETAIL", 3); // compensation time
//		RhType rhTypeCharge4 = typeDAO.getRhType("SHIFT_DETAIL", 4); // Decompensation time
		RhType rhTypeCharge5 = typeDAO.getRhType("SHIFT_DETAIL", 5); // extra time
//		RhType rhTypeCharge6 = typeDAO.getRhType("SHIFT_DETAIL", 6); // vacation time
		for (RhShift rhShift : list) {
			if(rhShift.getRhType().getCode() == 1 || 
					rhShift.getRhType().getCode() == 9 || 
					rhShift.getRhType().getCode() == 15){ // scale
				
				rhShift.setRhTypeCharge(rhTypeCharge1);
				
			}else if(rhShift.getRhType().getCode() == 2 || 
					rhShift.getRhType().getCode() == 6 || 
					rhShift.getRhType().getCode() == 4 ){// abroad job, schedule job, extra hour
				// provide for the user
			}else if(rhShift.getRhType().getCode() == 3 || 
					rhShift.getRhType().getCode() == 5 || 
					rhShift.getRhType().getCode() == 8){ // permission, license				
				rhShift.setRhTypeCharge(rhShift.getRhTypePerm().getRhTypeParent());
			}else if(rhShift.getRhType().getCode() == 10 || //late, absent, leave 
					rhShift.getRhType().getCode() == 11 || 
					rhShift.getRhType().getCode() == 13){//alerts
				rhShift.setRhTypeCharge(rhTypeCharge2);
			}else if(rhShift.getRhType().getCode() == 12){
				rhShift.setRhTypeCharge(rhTypeCharge5);
			}else if(rhShift.getRhType().getCode() == 14){
				if(rhShift.getRhStatus().getCode() == 3){//not approved 
					rhShift.setRhTypeCharge(rhTypeCharge2);
				}else if(rhShift.getRhStatus().getCode() == 2){//approved
					rhShift.setRhTypeCharge(rhTypeCharge1);
				}else{
					rhShift.setRhTypeCharge(rhTypeCharge2);
				}
			}
			shiftDAO.updateRhShift(rhShift);
			log.info("Updated Charge shift id: " + rhShift.getId());
		}
	}
	
	@Override
	public void updateDayDetail(){
		log.info("START updating Day");
		List<RhShiftDetail> rhShiftDetails = shiftDetailService.listDayNull();
		Integer day = 0;
		RhType rhTypeDay1 = typeDAO.getRhType("DAY", 1);
		RhType rhTypeDay2 = typeDAO.getRhType("DAY", 2);
		RhType rhTypeDay3 = typeDAO.getRhType("DAY", 3);
		RhType rhTypeDay4 = typeDAO.getRhType("DAY", 4);
		RhType rhTypeDay5 = typeDAO.getRhType("DAY", 5);
		RhType rhTypeDay6 = typeDAO.getRhType("DAY", 6);
		RhType rhTypeDay7 = typeDAO.getRhType("DAY", 7);
		RhType rhTypeDay8 = typeDAO.getRhType("DAY", 8);
		RhType rhTypeShiftDay1 = typeDAO.getRhType("SHIFT_DAY", 1);
		RhType rhTypeShiftDay2 = typeDAO.getRhType("SHIFT_DAY", 2);
		for (RhShiftDetail rhShiftDetail : rhShiftDetails) {
			DateTime dateTimeStart = new DateTime(rhShiftDetail.getTimeStart());
			day = dateTimeStart.getDayOfWeek();
			if(day == 1) rhShiftDetail.setRhTypeDay(rhTypeDay1);
			else if(day == 2) rhShiftDetail.setRhTypeDay(rhTypeDay2);
			else if(day == 3) rhShiftDetail.setRhTypeDay(rhTypeDay3);
			else if(day == 4) rhShiftDetail.setRhTypeDay(rhTypeDay4);
			else if(day == 5) rhShiftDetail.setRhTypeDay(rhTypeDay5);
			else if(day == 6) rhShiftDetail.setRhTypeDay(rhTypeDay6);
			else if(day == 7) rhShiftDetail.setRhTypeDay(rhTypeDay7);
			else if(day == 8) rhShiftDetail.setRhTypeDay(rhTypeDay8);
			
			if(dateTimeStart.getHourOfDay() >= 6 && dateTimeStart.getHourOfDay() < 22){
				rhShiftDetail.setRhTypeShiftDay(rhTypeShiftDay1);
			}else{
				rhShiftDetail.setRhTypeShiftDay(rhTypeShiftDay2);
			}
			
			shiftDetailService.update(rhShiftDetail);
		}
		
 	}
	
	@Override
	public void updateDetail(){
		log.info("START updating Detail");
		List<RhShift> list = shiftDAO.listNotProcessed(statusDAO.getRhStatus("SH_SIS_STATUS", 3)); // charge not null, status finished, not processed,
		for (RhShift rhShift : list) {
			RhSchedulePerson rhSchedulePerson = schedulePersonDAO.get(rhShift.getRhPerson());
			RhType rhType = typeDAO.getRhType("RH_SCHEDULE", 2); // dynamic schedule
			try {
				if(rhSchedulePerson.getRhSchedule().getRhType().getId() == rhType.getId()){
					if(rhShift.getRhType().getCode() == 1 || 
							rhShift.getRhType().getCode() == 2 ||
							rhShift.getRhType().getCode() == 8 || 
							rhShift.getRhType().getCode() == 9 || 
							rhShift.getRhType().getCode() == 14 || 
							rhShift.getRhType().getCode() == 15){
						shiftDetailService.delete(rhShift);
						shiftDetailService.process(rhShift, false);
						rhShift.setProcessed(true);
						shiftDAO.updateRhShift(rhShift);
						log.info("updating Detail Scale : " + rhShift.getId());
					}else if(rhShift.getRhType().getId() == 4 && rhShift.getRhStatus().getCode() == 2){
						shiftDetailService.delete(rhShift);
						shiftDetailService.process(rhShift, false);
						rhShift.setProcessed(true);
						shiftDAO.updateRhShift(rhShift);
						log.info("updating Detail Extra : " + rhShift.getId());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	@Override
	public void updateDetail2(){
		log.info("START updating Detail2");
		List<RhShift> list = shiftDAO.listNotProcessed2(statusDAO.getRhStatus("SH_SIS_STATUS", 3)); // charge not null, status finished, not processed,
		for (RhShift rhShift : list) {
//			boolean takenBreak = false;
//			RhSchedulePerson rhSchedulePerson = schedulePersonDAO.get(rhShift.getRhPerson());
			try {
				if(rhShift.getRhType().getCode() == 12 && rhShift.getRhStatus().getCode() == 2){
					RhShift shiftParent = new RhShift();
					shiftParent.setDateFinishShift(rhShift.getDateFinish());
					shiftDAO.updateRhShift(rhShift);
				}
				
				/*
				if(rhShift.getRhType().getCode() == 1 || rhShift.getRhType().getCode() == 8 || rhShift.getRhType().getCode() == 9){//SCALE
					
					RhScheduleScale rhScheduleScale = rhShift.getRhScheduleScale();
					try{
						if(rhScheduleScale.getRhSchedulePerson().getRhSchedule().getFlagBreak() == 1) takenBreak = true;
					}catch(Exception e){
						e.printStackTrace();
					}
					
					DateTime schedulefinish =  new DateTime(rhShift.getDateFinish());
					DateTime finishShift =  new DateTime(rhShift.getDateFinishShift());
					
					if(Minutes.minutesBetween(schedulefinish, finishShift).getMinutes() > 
						rhScheduleScale.getRhSchedulePerson().getRhSchedule().getToleranceFinish()){
						rhShift.setDateFinishShift(rhShift.getDateFinish());
						
						shiftDetailService2.delete(rhShift);
						shiftDetailService2.process(rhShift, takenBreak);
						rhShift.setProcessed2(true);
						shiftDAO.updateRhShift(rhShift);
						log.info("updating Detail Scale : " + rhShift.getId());
					}else{
						shiftDetailService2.delete(rhShift);
						shiftDetailService2.process(rhShift, takenBreak);
						rhShift.setProcessed2(true);
						shiftDAO.updateRhShift(rhShift);
						log.info("updating Detail Scale : " + rhShift.getId());
					}
						
				}else if(rhShift.getRhType().getCode() == 2 ){//schedule job
					shiftDetailService2.delete(rhShift);
					shiftDetailService2.process(rhShift, takenBreak);
					rhShift.setProcessed2(true);
					shiftDAO.updateRhShift(rhShift);
					log.info("updating Detail schdule job : " + rhShift.getId());
				}else if(rhShift.getRhType().getCode() == 3){//permission
					RhStatus rhStatus = statusDAO.getRhStatus("SH_PERMISSION", 2);
					if(rhStatus.getId() == rhShift.getRhStatus().getId()){
						shiftDetailService2.delete(rhShift);
						shiftDetailService2.process(rhShift, takenBreak);
						rhShift.setProcessed2(true);
						shiftDAO.updateRhShift(rhShift);
						log.info("updating Detail permission : " + rhShift.getId());
					}else{
						shiftDetailService2.delete(rhShift);
						shiftDetailService2.processCero(rhShift);
						rhShift.setProcessed2(true);
						shiftDAO.updateRhShift(rhShift);
					}
				}else if(rhShift.getRhType().getCode() == 4){//EEHH
					RhStatus rhStatus = statusDAO.getRhStatus("SH_EXTRAH", 2);
					if(rhStatus.getId() == rhShift.getRhStatus().getId()){
						shiftDetailService2.delete(rhShift);
						shiftDetailService2.process(rhShift, takenBreak);
						rhShift.setProcessed2(true);
						shiftDAO.updateRhShift(rhShift);
						log.info("updating Detail permission : " + rhShift.getId());
					}else{
						shiftDetailService2.delete(rhShift);
						shiftDetailService2.processCero(rhShift);
						rhShift.setProcessed2(true);
						shiftDAO.updateRhShift(rhShift);
					}
				}else if(rhShift.getRhType().getCode() == 5){//LICENSE
					
				}else if(rhShift.getRhType().getCode() == 10 || rhShift.getRhType().getCode() == 11 || rhShift.getRhType().getCode() == 13){//LATE
					RhStatus rhStatus = statusDAO.getRhStatus("SH_ALERT", 2);
					if(rhStatus.getId() == rhShift.getRhStatus().getId()){
						shiftDetailService2.delete(rhShift);
						shiftDetailService2.processCero(rhShift);
						rhShift.setProcessed2(true);
						shiftDAO.updateRhShift(rhShift);
					}else{
						shiftDetailService2.delete(rhShift);
						shiftDetailService2.process(rhShift, takenBreak);
						rhShift.setProcessed2(true);
						shiftDAO.updateRhShift(rhShift);
					}
				}else if(rhShift.getRhType().getCode() == 12){ // ADITIONAL TIME
					RhStatus rhStatus = statusDAO.getRhStatus("SH_ALERT", 2);
					if(rhStatus.getId() == rhShift.getRhStatus().getId()){
						shiftDetailService2.delete(rhShift);
						shiftDetailService2.process(rhShift, takenBreak);
						rhShift.setProcessed2(true);
						shiftDAO.updateRhShift(rhShift);
					}else{
						shiftDetailService2.delete(rhShift);
						shiftDetailService2.processCero(rhShift);
						rhShift.setProcessed2(true);
						shiftDAO.updateRhShift(rhShift);
					}
				}else if(rhShift.getRhType().getCode() == 14){// ABSENT ALL DAY
					shiftDetailService2.delete(rhShift);
					shiftDetailService2.process(rhShift, takenBreak);
					rhShift.setProcessed2(true);
					shiftDAO.updateRhShift(rhShift);
				}*/
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	@Override
	public RhShift getRhShift(int idRhShift){
		RhShift rhShift = shiftDAO.getRhShift(idRhShift);
//		rhShift.setRhFiles(fileDAO.getRhFile(rhShift));
		List<RhFile> RhFiles=fileDAO.getRhFile(rhShift);
		if(RhFiles.size()>0){
		 rhShift.setRhFiles(fileDAO.getRhFile(rhShift));
		}
		
		rhShift.setRhShifts(shiftDAO.list(rhShift));
		for (RhShift rhShiftChild : rhShift.getRhShifts()) {
			rhShiftChild.setRhFiles(fileDAO.getRhFile(rhShiftChild));
		}
		List<RhShiftDetail> rhShiftDetails = shiftDetailService.list(rhShift); 
		if(rhShiftDetails != null && ! rhShiftDetails.isEmpty()) rhShift.setRhShiftDetails(shiftDetailService.list(rhShift));
		else rhShift.setRhShiftDetails(null);
		
		return rhShift;
	}

	@Override
	public List<RhShift> listShiftActive(RhPerson rhPerson) {
		List<RhShift> listRhShift =  new ArrayList<RhShift>();
//		RhStatus rhStatusSisPending = statusDAO.getRhStatus("SH_SIS_STATUS", 1);
		RhStatus rhStatusSisActive = statusDAO.getRhStatus("SH_SIS_STATUS", 2);
		listRhShift.addAll(shiftDAO.listBySis2(rhPerson, rhStatusSisActive));
		listRhShift = addVariables(listRhShift);
		return listRhShift;
	}
	
	@Override
	public List<RhShift> listShiftPlanned(RhPerson rhPerson) {
		List<RhShift> listRhShift =  new ArrayList<RhShift>();
		RhStatus rhStatusSisPending = statusDAO.getRhStatus("SH_SIS_STATUS", 1);
		RhStatus rhStatus = statusDAO.getRhStatus("SH_SCALE", 3);
//		RhStatus rhStatusSisActive = statusDAO.getRhStatus("SH_SIS_STATUS", 2);
		listRhShift.addAll(shiftDAO.listBySis2(rhPerson, rhStatusSisPending, rhStatus));
//		listRhShift.addAll(shiftDAO.listBySisAll(rhPerson, rhStatusSisActive));
		listRhShift = addVariables(listRhShift);
		return listRhShift;
	}
	
	@Override
	public List<RhShift> listShiftClosed(RhPerson rhPerson ,RhShiftPeriod rhPeriodo) {
		List<RhShift> listRhShift =  new ArrayList<RhShift>();
		RhStatus rhStatusSisPending = statusDAO.getRhStatus("SH_SIS_STATUS", 3); 
		RhStatus rhStatus = statusDAO.getRhStatus("SH_SCALE", 2); //CLOSED
		listRhShift.addAll(shiftDAO.listBySisClosed(rhPerson, rhStatusSisPending, rhStatus ,rhPeriodo));
//		listRhShift = addVariables(listRhShift);
		return listRhShift;
	}	
	
	@Override
	public void shiftChildDelete(int rhShift) {
		RhShift rhshiftv = shiftDAO.getRhShift(rhShift);
		shiftDAO.shiftChildDelete(rhshiftv,rhShift);		
	}
	
	@Override
	public void updateShiftRever(int idRhShift){
		RhShift rhShift= shiftDAO.getRhShift(idRhShift);
		RhStatus rhStatusSis = statusDAO.getRhStatus("SH_SIS_STATUS", 1); //PENDING
		RhStatus rhStatus = statusDAO.getRhStatus("SH_SCALE", 3); // RESGISTERED
		rhShift.setRhStatus(rhStatus);
		rhShift.setRhStatusSis(rhStatusSis);
		rhShift.setDateStartShift(null);
		rhShift.setDateFinishShift(null);
		rhShift.setDateProcess(null);;
		rhShift.setRhPersonResp(rhShift.getRhPersonMng());
		shiftDAO.updateRhShift(rhShift);
	}
	
	@Override
	public List<RhShift> listShiftPending(RhPerson rhPerson) {
		List<RhShift> listRhShift =  new ArrayList<RhShift>();
		RhStatus rhStatusSisPending = statusDAO.getRhStatus("SH_SIS_STATUS", 1);
//		RhStatus rhStatusSisActive = statusDAO.getRhStatus("SH_SIS_STATUS", 2);
		listRhShift.addAll(shiftDAO.listBySis(rhPerson, rhStatusSisPending));
//		listRhShift.addAll(shiftDAO.listBySis(rhPerson, rhStatusSisActive));
		listRhShift.addAll(shiftDAO.listBySisAll(rhPerson, rhStatusSisPending));
//		listRhShift.addAll(shiftDAO.lsShiftxPerson(rhPerson, rhStatusSisPending)); // CCV
//		listRhShift.addAll(shiftDAO.listBySisAll(rhPerson, rhStatusSisActive));
		listRhShift = addVariables(listRhShift);
		return listRhShift;
	}
	
	
	@Override
	public List<RhShift> listShiftProcessed(RhPerson rhPerson) {
		List<RhShift> listRhShift =  new ArrayList<RhShift>();
		RhStatus rhStatusSisProcessed = statusDAO.getRhStatus("SH_SIS_STATUS", 3);
		RhShiftPeriod rhShiftPeriodCurrent = shiftPerioDAO.get(rhPerson.getRhCompany(), 
				statusDAO.getRhStatus("SH_PERIOD", 1));
		RhShiftPeriod rhShiftPeriodLast = shiftPerioDAO.get(rhPerson.getRhCompany(), 
				statusDAO.getRhStatus("SH_PERIOD", 4));
		listRhShift.addAll(shiftDAO.listBySisProcess(rhPerson, rhStatusSisProcessed, rhShiftPeriodCurrent));
		listRhShift.addAll(shiftDAO.listBySisProcess(rhPerson, rhStatusSisProcessed, rhShiftPeriodLast));
		listRhShift = addVariables(listRhShift);
		return shiftDetailService.addDetail(listRhShift);
	}

	@Override
	public List<RhShift> listShiftProcessed(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod) {
		RhStatus rhStatusSisProcessed = statusDAO.getRhStatus("SH_SIS_STATUS", 3);
		return shiftDAO.listBySisProcess(rhPerson, rhStatusSisProcessed, rhShiftPeriod);
	}
	
//	public List<RhShift> listSession(RhPerson rhPerson){
//		List<RhShift> listRhShift =  new ArrayList<RhShift>();
//		listRhShift.addAll(scheduleJobService.listSession(rhPerson));
//		listRhShift.addAll(scheduleScaleService.listSession(rhPerson));
//		return listRhShift;
//	}

	@Override
	public RhFile getRhFile(int idRhFile) {
		return fileDAO.get(idRhFile);
	}
	
	@Override
	public Map<String, Object> getMapDetail(RhPerson rhPerson){
		return shiftDetailService.getMapDetail(rhPerson);
	}
	
	@Override
	public List<RhShiftDetail> listShitDetail(int option, RhPerson rhPerson){
		List<RhShiftDetail> rhShiftDetails =  new ArrayList<>();
		RhShiftPeriod rhShiftPeriodCurrent = shiftPerioDAO.get(rhPerson.getRhCompany(),
				statusDAO.getRhStatus("SH_PERIOD", 1));
		switch (option) {
			case 1: // worked time
				rhShiftDetails.addAll(shiftDetailService.list(typeDAO.getRhType("SHIFT_DETAIL", 1), 
						rhPerson, rhShiftPeriodCurrent));
				return rhShiftDetails;
			case 2: // compensation and decompensation
				rhShiftDetails.addAll(shiftDetailService.list(typeDAO.getRhType("SHIFT_DETAIL", 3), 
						rhPerson, rhShiftPeriodCurrent));
				return rhShiftDetails;
			case 3: // Extra time
				rhShiftDetails.addAll(shiftDetailService.list(typeDAO.getRhType("SHIFT_DETAIL", 5), 
						rhPerson, rhShiftPeriodCurrent));
				return rhShiftDetails;
			case 4: // discount
				rhShiftDetails.addAll(shiftDetailService.list(typeDAO.getRhType("SHIFT_DETAIL", 2), 
						rhPerson, rhShiftPeriodCurrent));
				return rhShiftDetails;
			default:
				return rhShiftDetails;
		}
		
	}
	
	public List<RhShift> addVariables(List<RhShift> rhShifts){
		for (RhShift rhShift : rhShifts) {
			rhShift.setActiveTime(utilRrhhService.getStringDay(rhShift.getDateReg(), new Date()));
		}
		return rhShifts;
	}
	
}