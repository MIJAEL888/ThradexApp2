package org.thradex.rrhh.service;

import org.springframework.transaction.annotation.Transactional;
import org.thradex.model.RhPerson;
import org.thradex.model.RhSchedulePerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhShiftDetail;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;
import org.thradex.rrhh.dao.SchedulePersonDAO;
import org.thradex.rrhh.dao.ShiftDetailDAO;
import org.thradex.rrhh.dao.ShiftPerioDAO;
import org.thradex.rrhh.dao.StatusDAO;
import org.thradex.rrhh.dao.TypeDAO;
import org.thradex.util.ConstantsSis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ShiftDetailServiceImpl implements ShiftDetailService{
	
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);

	@Autowired
	private ShiftDetailDAO shiftDetailDAO;
	
	@Autowired
	private TypeDAO typeDAO;
	
	@Autowired
	private StatusDAO statusDAO;
	
	@Autowired
	private ShiftPerioDAO shiftPerioDAO;
	
	@Autowired
	private SchedulePersonDAO schedulePersonDAO;
	
	@Autowired
	private UtilRrhhService utilRrhhService;
	
	
	public RhShiftDetail get(int idRhShiftDetail) {
		return shiftDetailDAO.get(idRhShiftDetail);
	}

	
	public List<RhShiftDetail> list(RhShift rhShift) {
		return shiftDetailDAO.list(rhShift);
	}

	
	public List<RhShiftDetail> listDayNull() {
		return shiftDetailDAO.listDayNull();
	}
	
	
	public List<RhShiftDetail> list(RhType typeDetail, RhPerson rhPerson, RhShiftPeriod rhShiftPeriod){
		return shiftDetailDAO.list(typeDetail, rhPerson, rhShiftPeriod);
	}
	
	
	public RhShiftDetail save(RhShiftDetail rhShiftDetail) {
		return shiftDetailDAO.save(rhShiftDetail);
	}

	
	public void update(RhShiftDetail rhShiftDetail) {
		shiftDetailDAO.update(rhShiftDetail);
	}
	
	
	public void delete(RhShift rhShift){
		shiftDetailDAO.delete(rhShift);
	}
	
	public RhShiftDetail registerRhShiftDetail(RhShift rhShift, RhType rhTypeCharge, RhType rhTypeRate, 
			DateTime dateTimeStart, DateTime dateTimeFinish, boolean takenBreak){
		int total = Math.abs(Minutes.minutesBetween(dateTimeStart, dateTimeFinish).getMinutes());
		RhShiftDetail rhShiftDetail1 = new RhShiftDetail();
		
		if(takenBreak){
			total = total - 60;
			rhShiftDetail1.setComment("-1 Hour for Breack");
		}
		rhShiftDetail1.setRhShift(rhShift);
		rhShiftDetail1.setRhType(rhTypeCharge);
		rhShiftDetail1.setRhTypeRate(rhTypeRate);
		rhShiftDetail1.setTimeStart(dateTimeStart.toDate());
		rhShiftDetail1.setTimeFinish(dateTimeFinish.toDate());
		rhShiftDetail1.setTotal(total);
		rhShiftDetail1.setTotalRate((int) (rhTypeRate.getValue() * total));
		rhShiftDetail1.setRhTypeDay(getDay(dateTimeStart.getDayOfWeek()));
		rhShiftDetail1.setRhTypeShiftDay(getShiftDay(dateTimeStart.getHourOfDay()));
		save(rhShiftDetail1);
		return rhShiftDetail1;
	}
	public RhShiftDetail registerRhShiftDetailNight(RhShift rhShift, RhType rhTypeCharge, RhType rhTypeRate, RhType rhTypeRateNight, 
			DateTime dateTimeStart, DateTime dateTimeFinish, boolean takenBreak){
		int total = Math.abs(Minutes.minutesBetween(dateTimeStart, dateTimeFinish).getMinutes());
		RhShiftDetail rhShiftDetail1 = new RhShiftDetail();
		if(takenBreak){
			total = total - 60;
			rhShiftDetail1.setComment("-1 Hour for Breack");
		}
		rhShiftDetail1.setRhShift(rhShift);
		rhShiftDetail1.setRhType(rhTypeCharge);
		rhShiftDetail1.setRhTypeRate(rhTypeRate);
		rhShiftDetail1.setTimeStart(dateTimeStart.toDate());
		rhShiftDetail1.setTimeFinish(dateTimeFinish.toDate());
		rhShiftDetail1.setTotal(total);
		rhShiftDetail1.setTotalRate((int) (rhTypeRate.getValue() * total * (60.0/rhTypeRateNight.getValue())));
		rhShiftDetail1.setRhTypeDay(getDay(dateTimeStart.getDayOfWeek()));
		rhShiftDetail1.setRhTypeShiftDay(getShiftDay(dateTimeStart.getHourOfDay()));
		save(rhShiftDetail1);
		return rhShiftDetail1;
	}
	
	public RhType getDay(int day){
		return typeDAO.getRhType("DAY", day);
	}
	
	public RhType getShiftDay(int startHour){
		if(startHour >= 6 && startHour < 22){
			return typeDAO.getRhType("SHIFT_DAY", 1);
		}else{
			return typeDAO.getRhType("SHIFT_DAY", 2);
		}
	}

	/**
	 * @param rhShift
	 * @param takenBreak
	 * Methods to process PE Rates 
	 */
	public void processPe(RhShift rhShift, boolean takenBreak) {
		if (rhShift.getRhTypeCharge().getCode() == 1) {// NORMAL
			processPeNormal(rhShift, takenBreak);
		}else if (rhShift.getRhTypeCharge().getCode() == 3 || rhShift.getRhTypeCharge().getCode() == 5) {// CONPENSATION// EXTRA
			processPeExtra(rhShift);
		}else if (rhShift.getRhTypeCharge().getCode() == 2 || rhShift.getRhTypeCharge().getCode() == 4) {// DECONPENSATION// DISCOUNT
			processPeDiscount(rhShift, takenBreak);
		}else {
			log.error("NO REGISTER RATE FOR THIS SHIFT" + rhShift.getId());
		}
	}
	
	public void processPeNormal(RhShift rhShift, boolean takenBreak){
		RhType rhTypeRate1 = typeDAO.getRhType("SH_RATE", 1);
		DateTime dateTimeStart = new DateTime(rhShift.getDateStartShift());
		DateTime dateTimeFinish = new DateTime(rhShift.getDateFinishShift());
		registerRhShiftDetail(rhShift, 
				rhShift.getRhTypeCharge(), 
				rhTypeRate1,
				dateTimeStart,
				dateTimeFinish,
				takenBreak
				);
	}
	
	public void processPeExtra(RhShift rhShift){
		RhType rhTypeRate1 = typeDAO.getRhType("SH_RATE", 1); // 1:1
		RhType rhTypeRate2 = typeDAO.getRhType("SH_RATE", 2);// 0.25 2 horas 
		RhType rhTypeRate3 = typeDAO.getRhType("SH_RATE", 3);// 0.35 +3 horas
		int min2FH = 120;
		DateTime dateTimeStart = new DateTime(rhShift.getDateStartShift());
		DateTime dateTimeFinish = new DateTime(rhShift.getDateFinishShift());
		int total = Minutes.minutesBetween(dateTimeStart, dateTimeFinish).getMinutes();
		registerRhShiftDetail(rhShift, 
				rhShift.getRhTypeCharge(), 
				rhTypeRate1,
				dateTimeStart,
				dateTimeFinish,
				false);

		// minutes higher the 2 hours
		if(total > min2FH){
			registerRhShiftDetail(rhShift, 
					rhShift.getRhTypeCharge(), 
					rhTypeRate2,
					dateTimeStart,
					dateTimeStart.plusMinutes(min2FH),
					false);
			
			registerRhShiftDetail(rhShift, 
					rhShift.getRhTypeCharge(), 
					rhTypeRate3,
					dateTimeStart.plusMinutes(min2FH),
					dateTimeFinish,
					false);
		}else{
			registerRhShiftDetail(rhShift, 
					rhShift.getRhTypeCharge(), 
					rhTypeRate2,
					dateTimeStart,
					dateTimeFinish,
					false);
		}
	}
	
	public void processPeDiscount(RhShift rhShift, boolean takenBreak){
		RhType rhTypeRate1 = typeDAO.getRhType("SH_RATE", 1);
		DateTime dateTimeStart = new DateTime(rhShift.getDateStartShift());
		DateTime dateTimeFinish = new DateTime(rhShift.getDateFinishShift());
		RhShiftDetail rhShiftDetail = registerRhShiftDetail(rhShift, 
				rhShift.getRhTypeCharge(), 
				rhTypeRate1,
				dateTimeStart,
				dateTimeFinish,
				false);
		if(rhShift.getRhTypeCharge().getCode() == 4){//DECONPENSATION
			RhSchedulePerson rhSchedulePerson = schedulePersonDAO.get(rhShift.getRhPerson());
			int poolCompensation = rhSchedulePerson.getCompensation();
			poolCompensation = poolCompensation - rhShiftDetail.getTotalRate();
			rhSchedulePerson.setCompensation(poolCompensation);
			schedulePersonDAO.update(rhSchedulePerson);
		}
	}
	
	/**
	 * @param rhShift
	 * @param takenBreak
	 * @return
	 * 
	 * Methods to process rates BRA
	 * 
	 */
	public void processBra(RhShift rhShift, boolean takenBreak) {
		RhSchedulePerson rhSchedulePerson = schedulePersonDAO.get(rhShift.getRhPerson());
		RhType rhType = typeDAO.getRhType("RH_SCHEDULE", 2); // dynamic schedule
		if(rhSchedulePerson.getRhSchedule().getRhType().getId() == rhType.getId())
			processBraDynamic(rhShift, takenBreak);
		else
			processBraStatic(rhShift, takenBreak);
	}
	
	public void processBraStatic(RhShift rhShift, boolean takenBreak) {
		if (rhShift.getRhTypeCharge().getCode() == 1) {// NORMAL
			processBraStaticNormal(rhShift, takenBreak);
		}else if (rhShift.getRhTypeCharge().getCode() == 3 || rhShift.getRhTypeCharge().getCode() == 5) {// CONPENSATION// EXTRA
			processBraStaticExtra(rhShift, takenBreak);
		}else if (rhShift.getRhTypeCharge().getCode() == 2 || rhShift.getRhTypeCharge().getCode() == 4) {// DECONPENSATION// DISCOUNT
			processBraStaticDiscount(rhShift, takenBreak);
		}else {
			log.error("NO REGISTER RATE FOR THIS SHIFT" + rhShift.getId());
		}
	}
	
	
	public void processBraStaticNormal(RhShift rhShift, boolean takenBreak){
		RhType rhTypeRate1 = typeDAO.getRhType("SH_RATE", 1);
		DateTime dateTimeStart = new DateTime(rhShift.getDateStartShift());
		DateTime dateTimeFinish = new DateTime(rhShift.getDateFinishShift());
		registerRhShiftDetail(rhShift, 
				rhShift.getRhTypeCharge(), 
				rhTypeRate1,
				dateTimeStart,
				dateTimeFinish,
				takenBreak);
	}
	
	public void processBraStaticExtra(RhShift rhShift, boolean takenBreak){
		RhType rhTypeRate1 = typeDAO.getRhType("SH_RATE", 1); // 1:1
		RhType rhTypeRate2 = typeDAO.getRhType("SH_RATE", 4); // 0.5 2 HOURS
		RhType rhTypeRate3 = typeDAO.getRhType("SH_RATE", 5); // 1.0 +3 HOURS
//		RhType rhTypeRate4 = typeDAO.getRhType("SH_RATE", 6); // 0.2 NIGHT % 
//		RhType rhTypeRate5 = typeDAO.getRhType("SH_RATE", 7); // 52.5 NIGHT HOUR
		DateTime dateTimeStart = new DateTime(rhShift.getDateStartShift());
		DateTime dateTimeFinish = new DateTime(rhShift.getDateFinishShift());
		int total = Minutes.minutesBetween(dateTimeStart, dateTimeFinish).getMinutes();
		registerRhShiftDetail(rhShift, 
				rhShift.getRhTypeCharge(), 
				rhTypeRate1,
				dateTimeStart,
				dateTimeFinish,
				takenBreak);
		int min2FH = 120;
		if(total > min2FH){
			registerRhShiftDetail(rhShift, 
					rhShift.getRhTypeCharge(), 
					rhTypeRate2,
					dateTimeStart,
					dateTimeStart.plusMinutes(min2FH),
					false);
			
			registerRhShiftDetail(rhShift, 
					rhShift.getRhTypeCharge(), 
					rhTypeRate3,
					dateTimeStart.plusMinutes(min2FH),
					dateTimeFinish,
					false);
		}else{
			registerRhShiftDetail(rhShift, 
					rhShift.getRhTypeCharge(), 
					rhTypeRate2,
					dateTimeStart,
					dateTimeFinish,
					false);
		}
	}
	
	public void processBraStaticDiscount(RhShift rhShift, boolean takenBreak){
		RhType rhTypeRate1 = typeDAO.getRhType("SH_RATE", 1);
		DateTime dateTimeStart = new DateTime(rhShift.getDateStartShift());
		DateTime dateTimeFinish = new DateTime(rhShift.getDateFinishShift());
		registerRhShiftDetail(rhShift, 
				rhShift.getRhTypeCharge(), 
				rhTypeRate1,
				dateTimeStart,
				dateTimeFinish,
				false);
	}
	

	public void processBraDynamic(RhShift rhShift, boolean takenBreak) {
		if (rhShift.getRhTypeCharge().getCode() == 1) {// NORMAL
			processBraDynamicNormal(rhShift, takenBreak);
		}else if (rhShift.getRhTypeCharge().getCode() == 3 || rhShift.getRhTypeCharge().getCode() == 5) {// CONPENSATION - EXTRA
			processBraDynamicExtra(rhShift, takenBreak);
		}else if (rhShift.getRhTypeCharge().getCode() == 2 || rhShift.getRhTypeCharge().getCode() == 4) {// DECONPENSATION - DISCOUNT
			processBraDynamicDiscount(rhShift, takenBreak);
		}else {
			log.error("NO REGISTER RATE FOR THIS SHIFT" + rhShift.getId());
		}
	}

	
	public void processBraDynamicExtraSaturday(RhShift rhShift, boolean takenBreak){
		DateTime dateTimeStart = new DateTime(rhShift.getDateStartShift());
		DateTime dateTimeFinish = new DateTime(rhShift.getDateFinishShift());
		RhType rhTypeExtra = typeDAO.getRhType("SHIFT_DETAIL", 5);
		RhType rhTypeRate7 = typeDAO.getRhType("SH_RATE", 9); // Extra Saturday 
		log.info("processBraDynamicExtraSaturday " + dateTimeStart.toString());
		// Check Saturday Extra
		if(dateTimeStart.getDayOfWeek() == 6 ||  dateTimeFinish.getDayOfWeek() == 6){
			if(dateTimeStart.getDayOfWeek() == 6 &&  dateTimeFinish.getDayOfWeek() == 6){
				log.info("day saturday " + dateTimeStart.toString());
				registerRhShiftDetail(rhShift, 
						rhTypeExtra, 
						rhTypeRate7,
						dateTimeStart,
						dateTimeFinish,
						takenBreak);
				
			}else if(dateTimeStart.getDayOfWeek() == 6){
				DateTime dateTimeFinish1 = new DateTime(dateTimeStart.getYear(), dateTimeStart.getMonthOfYear(), 
						dateTimeStart.getDayOfMonth(), 23, 59);
				registerRhShiftDetail(rhShift, 
						rhTypeExtra, 
						rhTypeRate7,
						dateTimeStart,
						dateTimeFinish1,
						takenBreak);
			}else if(dateTimeFinish.getDayOfWeek() == 6){
				DateTime dateTimeStart1 = new DateTime(dateTimeFinish.getYear(), dateTimeFinish.getMonthOfYear(), 
						dateTimeFinish.getDayOfMonth(), 0, 0);
				registerRhShiftDetail(rhShift, 
						rhTypeExtra, 
						rhTypeRate7,
						dateTimeStart1,
						dateTimeFinish,
						takenBreak);
			}
		}
	}
	public void processBraDynamicExtraSunday(RhShift rhShift, boolean takenBreak){
		DateTime dateTimeStart = new DateTime(rhShift.getDateStartShift());
		DateTime dateTimeFinish = new DateTime(rhShift.getDateFinishShift());
		RhType rhTypeExtra = typeDAO.getRhType("SHIFT_DETAIL", 5);
		RhType rhTypeRate8 = typeDAO.getRhType("SH_RATE", 8); // Extra Sunday
		
		// Check Saturday Extra
		if(dateTimeStart.getDayOfWeek() == 7 ||  dateTimeFinish.getDayOfWeek() == 7){
			if(dateTimeStart.getDayOfWeek() == 7 &&  dateTimeFinish.getDayOfWeek() == 7){
				registerRhShiftDetail(rhShift, 
						rhTypeExtra, 
						rhTypeRate8,
						dateTimeStart,
						dateTimeFinish,
						takenBreak);
				
			}else if(dateTimeStart.getDayOfWeek() == 7){
				DateTime dateTimeFinish1 = new DateTime(dateTimeStart.getYear(), dateTimeStart.getMonthOfYear(), 
						dateTimeStart.getDayOfMonth(), 23, 59);
				registerRhShiftDetail(rhShift, 
						rhTypeExtra, 
						rhTypeRate8,
						dateTimeStart,
						dateTimeFinish1,
						takenBreak);
			}else if(dateTimeFinish.getDayOfWeek() == 7){
				DateTime dateTimeStart1 = new DateTime(dateTimeFinish.getYear(), dateTimeFinish.getMonthOfYear(), 
						dateTimeFinish.getDayOfMonth(), 0, 0);
				registerRhShiftDetail(rhShift, 
						rhTypeExtra, 
						rhTypeRate8,
						dateTimeStart1,
						dateTimeFinish,
						takenBreak);
			}
		}
		
	}
	public void processBraDynamicExtraNight(RhShift rhShift, boolean takenBreak){
		DateTime dateTimeStart = new DateTime(rhShift.getDateStartShift());
		DateTime dateTimeFinish = new DateTime(rhShift.getDateFinishShift());
		RhType rhTypeExtra = typeDAO.getRhType("SHIFT_DETAIL", 5);
		RhType rhTypeRate4 = typeDAO.getRhType("SH_RATE", 6); // 0.2 NIGHT % 
		RhType rhTypeRate5 = typeDAO.getRhType("SH_RATE", 7); // 52.5 NIGHT HOUR
		
		if(dateTimeStart.getDayOfMonth() == dateTimeFinish.getDayOfMonth()){ //Same day
			if(dateTimeStart.getHourOfDay() >= 0 && dateTimeStart.getHourOfDay() < 6){//start between 0 to 6 hours 
				if(dateTimeFinish.getHourOfDay() < 6){
					registerRhShiftDetailNight(rhShift, 
							rhTypeExtra, 
							rhTypeRate4,
							rhTypeRate5,
							dateTimeStart,
							dateTimeFinish,
							takenBreak);
				}else{
					DateTime dateTimeFinish1 = new DateTime(dateTimeStart.getYear(), dateTimeStart.getMonthOfYear(), 
							dateTimeStart.getDayOfMonth(), 6, 0);
					registerRhShiftDetailNight(rhShift, 
							rhTypeExtra, 
							rhTypeRate4,
							rhTypeRate5,
							dateTimeStart,
							dateTimeFinish1,
							takenBreak);
				}
			}else if(dateTimeStart.getHourOfDay() >= 6 && dateTimeStart.getHourOfDay() < 22){//start between 6 to 22 hours 
				if(dateTimeFinish.getHourOfDay() >= 22){
					DateTime dateTimeStart1 = new DateTime(dateTimeFinish.getYear(), dateTimeFinish.getMonthOfYear(), 
							dateTimeFinish.getDayOfMonth(), 22, 0);
					registerRhShiftDetailNight(rhShift, 
							rhTypeExtra, 
							rhTypeRate4,
							rhTypeRate5,
							dateTimeStart1,
							dateTimeFinish,
							takenBreak);
				}
			}else{//start between 22 to 23:59 hours 
				registerRhShiftDetailNight(rhShift, 
						rhTypeExtra, 
						rhTypeRate4,
						rhTypeRate5,
						dateTimeStart,
						dateTimeFinish,
						takenBreak);
			}
		}else{//start and finish different day
			
			if(dateTimeStart.getHourOfDay() < 22) {
				DateTime dateTimeStart1 = new DateTime(dateTimeStart.getYear(), dateTimeStart.getMonthOfYear(), 
						dateTimeStart.getDayOfMonth(), 22, 0);
				if(dateTimeFinish.getHourOfDay() > 6){
					DateTime dateTimeFinish1 = new DateTime(dateTimeFinish.getYear(), dateTimeFinish.getMonthOfYear(), 
							dateTimeFinish.getDayOfMonth(), 6, 0);
					registerRhShiftDetailNight(rhShift, 
							rhTypeExtra, 
							rhTypeRate4,
							rhTypeRate5,
							dateTimeStart1,
							dateTimeFinish1,
							takenBreak);
				}else{
					registerRhShiftDetailNight(rhShift, 
							rhTypeExtra, 
							rhTypeRate4,
							rhTypeRate5,
							dateTimeStart1,
							dateTimeFinish,
							takenBreak);
				}
			}else{
				if(dateTimeFinish.getHourOfDay() > 6){
					DateTime dateTimeFinish1 = new DateTime(dateTimeFinish.getYear(), dateTimeFinish.getMonthOfYear(), 
							dateTimeFinish.getDayOfMonth(), 6, 0);
					registerRhShiftDetailNight(rhShift, 
							rhTypeExtra, 
							rhTypeRate4,
							rhTypeRate5,
							dateTimeStart,
							dateTimeFinish1,
							takenBreak);
				}else{
					registerRhShiftDetailNight(rhShift, 
							rhTypeExtra, 
							rhTypeRate4,
							rhTypeRate5,
							dateTimeStart,
							dateTimeFinish,
							takenBreak);
				}
			}
		}
		
	}
	public void processBraDynamicNormal(RhShift rhShift, boolean takenBreak){
		DateTime dateTimeStart = new DateTime(rhShift.getDateStartShift());
		DateTime dateTimeFinish = new DateTime(rhShift.getDateFinishShift());
		RhType rhTypeRate1 = typeDAO.getRhType("SH_RATE", 1); // 1:1
		
		registerRhShiftDetail(rhShift, 
				rhShift.getRhTypeCharge(), 
				rhTypeRate1,
				dateTimeStart,
				dateTimeFinish,
				takenBreak);
		// Check Saturday Extra
		processBraDynamicExtraSaturday(rhShift, takenBreak);
		// Check Sunday Extra
		processBraDynamicExtraSunday(rhShift, takenBreak);
		// check night rate
		processBraDynamicExtraNight(rhShift, takenBreak);
	}
	
	public void processBraDynamicExtra(RhShift rhShift, boolean takenBreak){
		RhType rhTypeRate1 = typeDAO.getRhType("SH_RATE", 1); // total time

		DateTime dateTimeStart = new DateTime(rhShift.getDateStartShift());
		DateTime dateTimeFinish = new DateTime(rhShift.getDateFinishShift());
		
		registerRhShiftDetail(rhShift, 
				rhShift.getRhTypeCharge(), 
				rhTypeRate1,
				dateTimeStart,
				dateTimeFinish,
				takenBreak);
		
		// Check Saturday Extra
		processBraDynamicExtraSaturday(rhShift, takenBreak);
		// Check Sunday Extra
		processBraDynamicExtraSunday(rhShift, takenBreak);
		// check night rate
		processBraDynamicExtraNight(rhShift, takenBreak);
	}
	
	public void processBraDynamicDiscount(RhShift rhShift, boolean takenBreak){
		RhType rhTypeRate1 = typeDAO.getRhType("SH_RATE", 1);
		DateTime dateTimeStart = new DateTime(rhShift.getDateStartShift());
		DateTime dateTimeFinish = new DateTime(rhShift.getDateFinishShift());
		registerRhShiftDetail(rhShift, 
				rhShift.getRhTypeCharge(), 
				rhTypeRate1,
				dateTimeStart,
				dateTimeFinish,
				false);
	}

	
	public void process(RhShift rhShift, boolean takenBreak) {
		if (rhShift.getRhPerson().getRhCompany().getSisRegion().getSisCountry().getId() == 1){//brasil
			processBra(rhShift, takenBreak);
		}else if(rhShift.getRhPerson().getRhCompany().getSisRegion().getSisCountry().getId() == 2){//peru
			processPe(rhShift, takenBreak);
		}else{
			log.error("NO SPECIFY NATIONALITY TO PROCESS SHIFT DETAIL...");
		}
	}
	
	
	public void processCero(RhShift rhShift){
		RhType rhTypeRate1 = typeDAO.getRhType("SH_RATE", 1);
		RhShiftDetail rhShiftDetail1 = new RhShiftDetail();
		rhShiftDetail1.setRhShift(rhShift);
		rhShiftDetail1.setRhType(rhShift.getRhTypeCharge());
		rhShiftDetail1.setRhTypeRate(rhTypeRate1);
		rhShiftDetail1.setTimeStart(rhShift.getDateStart());
		rhShiftDetail1.setTimeFinish(rhShift.getDateFinish());
		rhShiftDetail1.setTotal(0);
		rhShiftDetail1.setTotalRate(0);
		rhShiftDetail1.setComment("Process 0");
		save(rhShiftDetail1);
	}

	
	public RhShiftDetail proccessAbroadJob(RhShift rhShift) {
		RhShiftDetail rhShiftDetail1 = null;
		RhSchedulePerson rhSchedulePerson = schedulePersonDAO.get(rhShift.getRhPerson());
		DateTime startTime = new DateTime(rhShift.getDateStart());
		DateTime finishTime = new DateTime(rhShift.getDateFinish());
		int diffDays = Days.daysBetween(startTime, finishTime).getDays();
		if (diffDays > rhSchedulePerson.getRhSchedule().getMinCommission()) {
			rhShiftDetail1 = new RhShiftDetail();
			int daysComm = diffDays/3;
			RhType rhTypeRate1 = typeDAO.getRhType("SH_RATE", 1);
			rhShiftDetail1.setRhShift(rhShift);
			rhShiftDetail1.setRhType(rhShift.getRhTypeCharge());
			rhShiftDetail1.setRhTypeRate(rhTypeRate1);
			rhShiftDetail1.setTimeStart(rhShift.getDateStart());
			rhShiftDetail1.setTimeFinish(rhShift.getDateFinish());
			rhShiftDetail1.setTotal(daysComm * rhSchedulePerson.getRhSchedule().getWorkHours() * 60);
			rhShiftDetail1.setTotalRate(rhTypeRate1.getValueInt() * rhShiftDetail1.getTotal());
			save(rhShiftDetail1);
		}
		return rhShiftDetail1;
	}
	
	/*
	 * Add detail to list of RhShift
	 */
	
	public List<RhShift> addDetail(List<RhShift> rhShifts){
		List<RhShift> list = new ArrayList<RhShift>();
		RhType type1 = typeDAO.getRhType("SHIFT_DETAIL", 1); // pay
		RhType type2 = typeDAO.getRhType("SHIFT_DETAIL", 2); // discount
		RhType type3 = typeDAO.getRhType("SHIFT_DETAIL", 3); // COMPENSATION
		RhType type4 = typeDAO.getRhType("SHIFT_DETAIL", 4); // DECOMPENSATION
		RhType type5 = typeDAO.getRhType("SHIFT_DETAIL", 5); // EXTRA
		Long tot1 = 0L;
		Long tot2 = 0L;
		Long tot3 = 0L;
		Long tot4 = 0L;
		Long tot5 = 0L;
		for (RhShift rhShift : rhShifts) {
			Map<String, Object> detail = new HashMap<String, Object>();
			Map<String, Object> map1 = shiftDetailDAO.sumTotal(rhShift, type1);
			tot1 = (Long) ((map1 == null) ? 0L : map1.get("totalRate")) ;  
			Map<String, Object> map2 = shiftDetailDAO.sumTotal(rhShift, type2);
			tot2 = (Long) ((map2 == null) ? 0L : map2.get("totalRate")) ;  
			Map<String, Object> map3 = shiftDetailDAO.sumTotal(rhShift, type3);
			tot3 = (Long) ((map3 == null) ? 0L : map3.get("totalRate")) ;  
			Map<String, Object> map4 = shiftDetailDAO.sumTotal(rhShift, type4);
			tot4 = (Long) ((map4 == null) ? 0L : map4.get("totalRate")) ;  
			Map<String, Object> map5 = shiftDetailDAO.sumTotal(rhShift, type5);
			tot5 = (Long) ((map5 == null) ? 0L : map5.get("totalRate")) ;  
			
			detail.put("PAY",  tot1);
			detail.put("EXTRA", tot5);
			detail.put("DISCOUNT", tot2);
			detail.put("COMPENSATION", tot3);
			detail.put("DECOMPENSATION", tot4);
			rhShift.setDetail(detail);
			list.add(rhShift);
		}
		return list;
	}
	
	
	public List<RhShift> addDetailChild(List<RhShift> rhShifts){
		RhType rhTypeRate = typeDAO.getRhType("SH_RATE", 1);
		RhType type1 = typeDAO.getRhType("SHIFT_DETAIL", 1); // pay
		RhType type2 = typeDAO.getRhType("SHIFT_DETAIL", 2); // discount
		RhType type3 = typeDAO.getRhType("SHIFT_DETAIL", 3); // COMPENSATION
		RhType type4 = typeDAO.getRhType("SHIFT_DETAIL", 4); // DECOMPENSATION
		RhType type5 = typeDAO.getRhType("SHIFT_DETAIL", 5); // EXTRA
		Long tot1 = 0L;
		Long tot2 = 0L;
		Long tot3 = 0L;
		Long tot4 = 0L;
		Long tot5 = 0L;
		Long tot6 = 0L;
		Long tot7 = 0L;
		Long tot8 = 0L;
		Long tot9 = 0L;
		Long tot10 = 0L;
		for (RhShift rhShift : rhShifts) {
			Map<String, Object> detail = new HashMap<String, Object>();
			Map<String, Object> map1 = shiftDetailDAO.sumTotalChild(rhShift, type1);
			tot1 = (Long) ((map1 == null) ? 0L : map1.get("totalRate")) ;  
			Map<String, Object> map2 = shiftDetailDAO.sumTotalChild(rhShift, type2);
			tot2 = (Long) ((map2 == null) ? 0L : map2.get("totalRate")) ;  
			Map<String, Object> map3 = shiftDetailDAO.sumTotalChild(rhShift, type3);
			tot3 = (Long) ((map3 == null) ? 0L : map3.get("totalRate")) ;  
			Map<String, Object> map4 = shiftDetailDAO.sumTotalChild(rhShift, type4);
			tot4 = (Long) ((map4 == null) ? 0L : map4.get("totalRate")) ;  
			Map<String, Object> map5 = shiftDetailDAO.sumTotalChild(rhShift, type5);
			tot5 = (Long) ((map5 == null) ? 0L : map5.get("totalRate")) ;  
			
			Map<String, Object> map6 = shiftDetailDAO.sumTotalChild(rhShift, type1, rhTypeRate);
			tot6 = (Long) ((map6 == null) ? 0L : map6.get("totalRate")) ;  
			Map<String, Object> map7 = shiftDetailDAO.sumTotalChild(rhShift, type2, rhTypeRate);
			tot7 = (Long) ((map7 == null) ? 0L : map7.get("totalRate")) ;  
			Map<String, Object> map8 = shiftDetailDAO.sumTotalChild(rhShift, type3, rhTypeRate);
			tot8 = (Long) ((map8 == null) ? 0L : map8.get("totalRate")) ;  
			Map<String, Object> map9 = shiftDetailDAO.sumTotalChild(rhShift, type4, rhTypeRate);
			tot9 = (Long) ((map9 == null) ? 0L : map9.get("totalRate")) ;  
			Map<String, Object> map10 = shiftDetailDAO.sumTotalChild(rhShift, type5, rhTypeRate);
			tot10 = (Long) ((map10 == null) ? 0L : map10.get("totalRate")) ;  
			
			detail.put("PAY_P", Math.round((tot1/60.0)*100)/100.0);
			detail.put("EXTRA_P", Math.round((tot5/60.0)*100)/100.0);
			detail.put("DISCOUNT_P", Math.round((tot2/60.0)*100)/100.0);
			detail.put("COMPENSATION_P", Math.round((tot3/60.0)*100)/100.0);
			detail.put("DECOMPENSATION_P", Math.round((tot4/60.0)*100)/100.0);
			detail.put("PAY", Math.round((tot6/60.0)*100)/100.0);
			detail.put("EXTRA", Math.round((tot10/60.0)*100)/100.0);
			detail.put("DISCOUNT", Math.round((tot7/60.0)*100)/100.0);
			detail.put("COMPENSATION", Math.round((tot8/60.0)*100)/100.0);
			detail.put("DECOMPENSATION", Math.round((tot9/60.0)*100)/100.0);
			
			rhShift.setDetail(detail);
		}
		return rhShifts;
	}
	
	
	public List<RhPerson> addDetail(List<RhPerson> rhPersons, RhShiftPeriod rhShiftPeriod){
		RhType type1 = typeDAO.getRhType("SHIFT_DETAIL", 1); // pay
		RhType type2 = typeDAO.getRhType("SHIFT_DETAIL", 2); // discount
		RhType type3 = typeDAO.getRhType("SHIFT_DETAIL", 3); // COMPENSATION
		RhType type4 = typeDAO.getRhType("SHIFT_DETAIL", 4); // DECOMPENSATION
		RhType type5 = typeDAO.getRhType("SHIFT_DETAIL", 5); // EXTRA
		Long tot1 = 0L;
		Long tot2 = 0L;
		Long tot3 = 0L;
		Long tot4 = 0L;
		Long tot5 = 0L;
		for (RhPerson rhPerson : rhPersons) {
			Map<String, Object> detail = new HashMap<String, Object>();
			Map<String, Object> map1 = shiftDetailDAO.sumTotal(rhPerson, type1, rhShiftPeriod);
			tot1 = (Long) ((map1 == null) ? 0L : map1.get("totalRate")) ;  
			Map<String, Object> map2 = shiftDetailDAO.sumTotal(rhPerson, type2, rhShiftPeriod);
			tot2 = (Long) ((map2 == null) ? 0L : map2.get("totalRate")) ;  
			Map<String, Object> map3 = shiftDetailDAO.sumTotal(rhPerson, type3, rhShiftPeriod);
			tot3 = (Long) ((map3 == null) ? 0L : map3.get("totalRate")) ;  
			Map<String, Object> map4 = shiftDetailDAO.sumTotal(rhPerson, type4, rhShiftPeriod);
			tot4 = (Long) ((map4 == null) ? 0L : map4.get("totalRate")) ;  
			Map<String, Object> map5 = shiftDetailDAO.sumTotal(rhPerson, type5, rhShiftPeriod);
			tot5 = (Long) ((map5 == null) ? 0L : map5.get("totalRate")) ;  
			
			detail.put("PAY", utilRrhhService.getStringHours(tot1.intValue()));
			detail.put("EXTRA", utilRrhhService.getStringHours(tot5.intValue()));
			detail.put("DISCOUNT", utilRrhhService.getStringHours(tot2.intValue()));
			detail.put("COMPENSATION", utilRrhhService.getStringHours(tot3.intValue()));
			detail.put("DECOMPENSATION", utilRrhhService.getStringHours(tot4.intValue()));
			rhPerson.setDetail(detail);
		}
		return rhPersons;
	}
	
	/*
	 * Detail shift for the dash board
	 * 
	 */
	
	public Map<String, Object> getMapDetail(RhPerson rhPerson){
		RhSchedulePerson rhSchedulePerson = schedulePersonDAO.get(rhPerson); 
		RhShiftPeriod rhShiftPeriodCurrent = shiftPerioDAO.get(rhPerson.getRhCompany(), 
				statusDAO.getRhStatus("SH_PERIOD", 1));
		RhShiftPeriod rhShiftPeriodLast = shiftPerioDAO.get(rhPerson.getRhCompany(), 
				statusDAO.getRhStatus("SH_PERIOD", 4));
		double factMinutes = 60.00D;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("WORK_HOUR_C",shiftDetailDAO.sumTotal(typeDAO.getRhType("SHIFT_DETAIL", 1),
				rhPerson, rhShiftPeriodCurrent)/factMinutes);
		map.put("WORK_HOUR_L", (double) shiftDetailDAO.sumTotal(typeDAO.getRhType("SHIFT_DETAIL", 1), 
				rhPerson, rhShiftPeriodLast)/factMinutes);
		map.put("EXTRA_HOUR_C", (double) shiftDetailDAO.sumTotal(typeDAO.getRhType("SHIFT_DETAIL", 5), 
				rhPerson, rhShiftPeriodLast)/factMinutes);
		map.put("EXTRA_HOUR_L", (double) shiftDetailDAO.sumTotal(typeDAO.getRhType("SHIFT_DETAIL", 5), 
				rhPerson, rhShiftPeriodLast)/factMinutes);
		map.put("DISCOUNT_C", (double) shiftDetailDAO.sumTotal(typeDAO.getRhType("SHIFT_DETAIL", 2), 
				rhPerson, rhShiftPeriodLast)/factMinutes);
		map.put("DISCOUNT_L", (double) shiftDetailDAO.sumTotal(typeDAO.getRhType("SHIFT_DETAIL", 2), 
				rhPerson, rhShiftPeriodLast)/factMinutes);
		map.put("COMENSATION_C", (double) shiftDetailDAO.sumTotal(typeDAO.getRhType("SHIFT_DETAIL", 3), 
				rhPerson, rhShiftPeriodLast)/factMinutes);
		map.put("COMENSATION_L", (double) shiftDetailDAO.sumTotal(typeDAO.getRhType("SHIFT_DETAIL", 3), 
				rhPerson, rhShiftPeriodLast)/factMinutes);
		map.put("POOL_COMENSATION", rhSchedulePerson.getCompensation()/factMinutes);
		return map;
	}
	
	
	public Integer getTotalTime(RhShift rhShift){
		RhType rhTypeRate = typeDAO.getRhType("SH_RATE", 1);
		Integer total = shiftDetailDAO.getTotal(rhShift, rhTypeRate);
		if (total ==  null ) return 0;
		else return total;
	}
	
	
	public Integer getTotalProcessedTime(RhShift rhShift){
		Integer total = shiftDetailDAO.getSumTotal(rhShift);
		if (total ==  null ) return 0;
		else return total;
	}
	
	
	public List<RhShiftDetail> list(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod){
		return shiftDetailDAO.list(rhPerson, rhShiftPeriod);
	}
	
	
	public List<RhShiftDetail> list(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod, RhStatus rhStatusSis){
		return shiftDetailDAO.list(rhPerson, rhShiftPeriod, rhStatusSis);
	}
	
	
	public List<RhShiftDetail> list(RhShiftPeriod rhShiftPeriod){
		return shiftDetailDAO.list(rhShiftPeriod);
	}
	
	
	public List<RhShiftDetail> listMng(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod){
		return shiftDetailDAO.listMng(rhPerson, rhShiftPeriod);
	}
}