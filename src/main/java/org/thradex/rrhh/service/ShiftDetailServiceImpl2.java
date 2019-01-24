package org.thradex.rrhh.service;

import org.springframework.transaction.annotation.Transactional;
import org.thradex.model.RhPerson;
import org.thradex.model.RhSchedulePerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhShiftDetail2;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;
import org.thradex.rrhh.dao.SchedulePersonDAO;
import org.thradex.rrhh.dao.ShiftDetailDAO2;
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
public class ShiftDetailServiceImpl2 implements ShiftDetailService2 {
	
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);

	@Autowired
	private ShiftDetailDAO2 shiftDetailDAO2;
	
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
	
	
	public RhShiftDetail2 get(int idRhShiftDetail) {
		return shiftDetailDAO2.get(idRhShiftDetail);
	}

	
	public List<RhShiftDetail2> list(RhShift rhShift) {
		return shiftDetailDAO2.list(rhShift);
	}

	
	public List<RhShiftDetail2> listDayNull() {
		return shiftDetailDAO2.listDayNull();
	}
	
	
	public List<RhShiftDetail2> list(RhType typeDetail, RhPerson rhPerson, RhShiftPeriod rhShiftPeriod){
		return shiftDetailDAO2.list(typeDetail, rhPerson, rhShiftPeriod);
	}
	
	
	public RhShiftDetail2 save(RhShiftDetail2 rhShiftDetail) {
		return shiftDetailDAO2.save(rhShiftDetail);
	}

	
	public void update(RhShiftDetail2 rhShiftDetail) {
		shiftDetailDAO2.update(rhShiftDetail);
	}
	
	
	public void delete(RhShift rhShift){
		shiftDetailDAO2.delete(rhShift);
	}
	
	public RhShiftDetail2 registerRhShiftDetail2(RhShift rhShift, RhType rhTypeCharge, RhType rhTypeRate, 
			DateTime dateTimeStart, DateTime dateTimeFinish, 
			int total, int pTotal){
		RhShiftDetail2 rhShiftDetail1 = new RhShiftDetail2();
		rhShiftDetail1.setRhShift(rhShift);
		rhShiftDetail1.setRhType(rhTypeCharge);
		rhShiftDetail1.setRhTypeRate(rhTypeRate);
		rhShiftDetail1.setTimeStart(dateTimeStart.toDate());
		rhShiftDetail1.setTimeFinish(dateTimeFinish.toDate());
		rhShiftDetail1.setTotal(total);
		rhShiftDetail1.setTotalRate(pTotal);
		rhShiftDetail1.setRhTypeDay(getDay(dateTimeStart.getDayOfWeek()));
		rhShiftDetail1.setRhTypeShiftDay(getShiftDay(dateTimeStart.getHourOfDay()));
		save(rhShiftDetail1);
		return rhShiftDetail1;
	}
	
	public RhShiftDetail2 registerRhShiftDetail2(RhShift rhShift, RhType rhTypeCharge, RhType rhTypeRate, 
			 DateTime dateTimeStart, DateTime dateTimeFinish, RhType rhTypeDay, RhType rhTypeShiftDay, 
			int total, int pTotal){
		RhShiftDetail2 rhShiftDetail1 = new RhShiftDetail2();
		rhShiftDetail1.setRhShift(rhShift);
		rhShiftDetail1.setRhType(rhTypeCharge);
		rhShiftDetail1.setRhTypeRate(rhTypeRate);
		rhShiftDetail1.setTimeStart(dateTimeStart.toDate());
		rhShiftDetail1.setTimeFinish(dateTimeFinish.toDate());
		rhShiftDetail1.setTotal(total);
		rhShiftDetail1.setTotalRate(pTotal);
		rhShiftDetail1.setRhTypeDay(rhTypeDay);
		rhShiftDetail1.setRhTypeShiftDay(rhTypeShiftDay);
		save(rhShiftDetail1);
		return rhShiftDetail1;
	}
	public RhShiftDetail2 registerRhShiftDetail2Night(RhShift rhShift, RhType rhTypeCharge, RhType rhTypeRate, RhType rhTypeRateNight, 
			DateTime dateTimeStart, DateTime dateTimeFinish, boolean takenBreak){
		int total = Math.abs(Minutes.minutesBetween(dateTimeStart, dateTimeFinish).getMinutes());
		RhShiftDetail2 rhShiftDetail1 = new RhShiftDetail2();
	
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
		int total = Math.abs(Minutes.minutesBetween(dateTimeStart, dateTimeFinish).getMinutes());
		if(takenBreak){
			total = total - 60;
		}
		registerRhShiftDetail2(rhShift, 
				rhShift.getRhTypeCharge(), 
				rhTypeRate1,
				dateTimeStart,
				dateTimeFinish,
				total,
				total);
	}
	
	public void processPeExtra(RhShift rhShift){
		RhType rhTypeRate1 = typeDAO.getRhType("SH_RATE", 1); // 1:1
		RhType rhTypeRate2 = typeDAO.getRhType("SH_RATE", 2);// 0.25 2 horas 
		RhType rhTypeRate3 = typeDAO.getRhType("SH_RATE", 3);// 0.35 +3 horas
		int min2FH = 120;
		DateTime dateTimeStart = new DateTime(rhShift.getDateStartShift());
		DateTime dateTimeFinish = new DateTime(rhShift.getDateFinishShift());
		int total = Minutes.minutesBetween(dateTimeStart, dateTimeFinish).getMinutes();
		int pTotal  = total;
		
		// minutes higher the 2 hours
		if(total > min2FH){
			pTotal = (int) (total + (min2FH * rhTypeRate2.getValue()) + ((total - min2FH) + rhTypeRate3.getValue()));
		}else{
			pTotal = (int) (total + (total * rhTypeRate2.getValue()));
		}
		
		registerRhShiftDetail2(rhShift, 
				rhShift.getRhTypeCharge(), 
				rhTypeRate1,
				dateTimeStart,
				dateTimeFinish,
				total, 
				pTotal);
	}
	
	public void processPeDiscount(RhShift rhShift, boolean takenBreak){
		RhType rhTypeRate1 = typeDAO.getRhType("SH_RATE", 1);
		DateTime dateTimeStart = new DateTime(rhShift.getDateStartShift());
		DateTime dateTimeFinish = new DateTime(rhShift.getDateFinishShift());
		int total = Minutes.minutesBetween(dateTimeStart, dateTimeFinish).getMinutes();
		int pTotal  = total;
		RhShiftDetail2 rhShiftDetail = registerRhShiftDetail2(rhShift, 
				rhShift.getRhTypeCharge(), 
				rhTypeRate1,
				dateTimeStart,
				dateTimeFinish,
				total, 
				pTotal);
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
		int total = Minutes.minutesBetween(dateTimeStart, dateTimeFinish).getMinutes();
		if(takenBreak){
			total = total - 60;
		}
		int pTotal  = total;
		registerRhShiftDetail2(rhShift, 
				rhShift.getRhTypeCharge(), 
				rhTypeRate1,
				dateTimeStart,
				dateTimeFinish,
				total,
				pTotal);
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
		int pTotal  = total;
		int min2FH = 120;
		if(total > min2FH){
			pTotal = (int) (total + (min2FH * rhTypeRate2.getValue()) + ((total - min2FH) + rhTypeRate3.getValue()));
		}else{
			pTotal = (int) (total + (total * rhTypeRate2.getValue()));
		}
		registerRhShiftDetail2(rhShift, 
				rhShift.getRhTypeCharge(), 
				rhTypeRate1,
				dateTimeStart,
				dateTimeFinish,
				total,
				pTotal);
	}
	
	public void processBraStaticDiscount(RhShift rhShift, boolean takenBreak){
		RhType rhTypeRate1 = typeDAO.getRhType("SH_RATE", 1);
		DateTime dateTimeStart = new DateTime(rhShift.getDateStartShift());
		DateTime dateTimeFinish = new DateTime(rhShift.getDateFinishShift());
		int total = Minutes.minutesBetween(dateTimeStart, dateTimeFinish).getMinutes();
		int pTotal  = total;
		registerRhShiftDetail2(rhShift, 
				rhShift.getRhTypeCharge(), 
				rhTypeRate1,
				dateTimeStart,
				dateTimeFinish,
				total,
				pTotal);
	}
	

	public void processBraDynamic(RhShift rhShift, boolean takenBreak) {
		if (rhShift.getRhTypeCharge().getCode() == 1) {// NORMAL
			processBraDynamicNormal(rhShift, takenBreak);
		}else if (rhShift.getRhTypeCharge().getCode() == 3 || rhShift.getRhTypeCharge().getCode() == 5) {// CONPENSATION - EXTRA
			processBraDynamicNormal(rhShift, takenBreak);
		}else if (rhShift.getRhTypeCharge().getCode() == 2 || rhShift.getRhTypeCharge().getCode() == 4) {// DECONPENSATION - DISCOUNT
			processBraDynamicDiscount(rhShift, takenBreak);
		}else {
			log.error("NO REGISTER RATE FOR THIS SHIFT" + rhShift.getId());
		}
	}
	public void processBraDynamicNormal(RhShift rhShift, boolean takenBreak){
		log.info("Normal Dynamic ");
		DateTime dateTimeStart = new DateTime(rhShift.getDateStartShift());
		DateTime dateTimeFinish = new DateTime(rhShift.getDateFinishShift());
		
		RhType rhTypeRate1 = typeDAO.getRhType("SH_RATE", 1); // 1:1
		
		RhType rhTypeRate4 = typeDAO.getRhType("SH_RATE", 6); // 0.2 NIGHT % 
		RhType rhTypeRate5 = typeDAO.getRhType("SH_RATE", 7); // 52.5 NIGHT HOUR
		RhType rhTypeRate8 = typeDAO.getRhType("SH_RATE", 8); // Extra Sunday
		RhType rhTypeRate7 = typeDAO.getRhType("SH_RATE", 9); // Extra Saturday 
		
		int total = Minutes.minutesBetween(dateTimeStart, dateTimeFinish).getMinutes();
		int pTotal = total;
		if(takenBreak){
			total = total - 60;
		}

		if(dateTimeStart.getDayOfMonth() == dateTimeFinish.getDayOfMonth()){ //Same day
			log.info("Same day " + total);
			if(dateTimeStart.getHourOfDay() >= 0 && dateTimeStart.getHourOfDay() < 6){//start between 0 to 6 hours 
				if(dateTimeFinish.getHourOfDay() < 6){
					//night added
					pTotal = (int) (total + (rhTypeRate4.getValue() * total * (60.0/rhTypeRate5.getValue())));
					if(dateTimeStart.getDayOfWeek() == 6){//Saturday
						pTotal = (int) (pTotal + (total*rhTypeRate7.getValue()));
					}
					
					if(dateTimeStart.getDayOfWeek() == 7){ //sunday
						pTotal = (int) (pTotal + (total*rhTypeRate8.getValue()));
					}
					registerRhShiftDetail2(rhShift, 
							rhShift.getRhTypeCharge(), 
							rhTypeRate1,
							dateTimeStart,
							dateTimeFinish,
							getDay(dateTimeStart.getDayOfWeek()),
							getShiftDay(dateTimeStart.getHourOfDay()),
							total,
							pTotal);
					
				}else{
					
					DateTime dateTimeFinish1 = new DateTime(dateTimeStart.getYear(), dateTimeStart.getMonthOfYear(), 
							dateTimeStart.getDayOfMonth(), 6, 0);
					total = Minutes.minutesBetween(dateTimeStart, dateTimeFinish1).getMinutes();
					pTotal = total;
					pTotal = (int) (total + (rhTypeRate4.getValue() * total * (60.0/rhTypeRate5.getValue())));
					if(dateTimeStart.getDayOfWeek() == 6){//Saturday
						pTotal = (int) (pTotal + (total*rhTypeRate7.getValue()));
					}
					
					if(dateTimeStart.getDayOfWeek() == 7){ //sunday
						pTotal = (int) (pTotal + (total*rhTypeRate8.getValue()));
					}
					registerRhShiftDetail2(rhShift, 
							rhShift.getRhTypeCharge(), 
							rhTypeRate1,
							dateTimeStart,
							dateTimeFinish1,
							getDay(dateTimeStart.getDayOfWeek()),
							getShiftDay(dateTimeStart.getHourOfDay()),
							total,
							pTotal);

					total = Minutes.minutesBetween(dateTimeFinish1, dateTimeFinish).getMinutes();
					pTotal = total;
					if(dateTimeStart.getDayOfWeek() == 6){//Saturday
						pTotal = (int) (pTotal + (total*rhTypeRate7.getValue()));
					}
					if(dateTimeStart.getDayOfWeek() == 7){ //sunday
						pTotal = (int) (pTotal + (total*rhTypeRate8.getValue()));
					}
					registerRhShiftDetail2(rhShift, 
							rhShift.getRhTypeCharge(), 
							rhTypeRate1,
							dateTimeFinish1,
							dateTimeFinish,
							getDay(dateTimeStart.getDayOfWeek()),
							getShiftDay(dateTimeFinish1.getHourOfDay()),
							total,
							pTotal);
				}
			}else if(dateTimeStart.getHourOfDay() >= 6 && dateTimeStart.getHourOfDay() < 22){//start between 6 to 22 hours 
				if(dateTimeFinish.getHourOfDay() >= 22){
					
					DateTime dateTimeFinish1 = new DateTime(dateTimeFinish.getYear(), dateTimeFinish.getMonthOfYear(), 
							dateTimeFinish.getDayOfMonth(), 22, 0);
					
					total = Minutes.minutesBetween(dateTimeStart, dateTimeFinish1).getMinutes();
					pTotal = total;
					log.info("day of the week " + dateTimeStart.getDayOfWeek());
					if(dateTimeStart.getDayOfWeek() == 6){//Saturday
						pTotal = (int) (pTotal + (total*rhTypeRate7.getValue()));
						log.info("Saturday ptotal " + pTotal);
					}
					
					if(dateTimeStart.getDayOfWeek() == 7){ //sunday
						pTotal = (int) (pTotal + (total*rhTypeRate8.getValue()));
					}
					registerRhShiftDetail2(rhShift, 
							rhShift.getRhTypeCharge(), 
							rhTypeRate1,
							dateTimeStart,
							dateTimeFinish1,
							getDay(dateTimeStart.getDayOfWeek()),
							getShiftDay(dateTimeStart.getHourOfDay()),
							total,
							pTotal);

					total = Minutes.minutesBetween(dateTimeFinish1, dateTimeFinish).getMinutes();
					pTotal = total;
					pTotal = (int) (total + (rhTypeRate4.getValue() * total * (60.0/rhTypeRate5.getValue())));
					if(dateTimeStart.getDayOfWeek() == 6){//Saturday
						pTotal = (int) (pTotal + (total*rhTypeRate7.getValue()));
					}
					if(dateTimeStart.getDayOfWeek() == 7){ //sunday
						pTotal = (int) (pTotal + (total*rhTypeRate8.getValue()));
					}
					registerRhShiftDetail2(rhShift, 
							rhShift.getRhTypeCharge(), 
							rhTypeRate1,
							dateTimeFinish1,
							dateTimeFinish,
							getDay(dateTimeStart.getDayOfWeek()),
							getShiftDay(dateTimeFinish1.getHourOfDay()),
							total,
							pTotal);
				}else{
					total = Minutes.minutesBetween(dateTimeStart, dateTimeFinish).getMinutes();
					pTotal = total;
					log.info("day of the week " + dateTimeStart.getDayOfWeek());
					if(dateTimeStart.getDayOfWeek() == 6){//Saturday
						pTotal = (int) (pTotal + (total*rhTypeRate7.getValue()));
						log.info("Saturday ptotal " + pTotal);
					}
					
					if(dateTimeStart.getDayOfWeek() == 7){ //sunday
						pTotal = (int) (pTotal + (total*rhTypeRate8.getValue()));
					}
					registerRhShiftDetail2(rhShift, 
							rhShift.getRhTypeCharge(), 
							rhTypeRate1,
							dateTimeStart,
							dateTimeFinish,
							getDay(dateTimeStart.getDayOfWeek()),
							getShiftDay(dateTimeStart.getHourOfDay()),
							total,
							pTotal);
				}
				
			}else{//start between 22 to 23:59 hours 
				total = Minutes.minutesBetween(dateTimeStart, dateTimeFinish).getMinutes();
				pTotal = (int) (total + (rhTypeRate4.getValue() * total * (60.0/rhTypeRate5.getValue())));
				if(dateTimeStart.getDayOfWeek() == 6){//Saturday
					pTotal = (int) (pTotal + (total*rhTypeRate7.getValue()));
				}
				if(dateTimeStart.getDayOfWeek() == 7){ //sunday
					pTotal = (int) (pTotal + (total*rhTypeRate8.getValue()));
				}
				registerRhShiftDetail2(rhShift, 
						rhShift.getRhTypeCharge(), 
						rhTypeRate1,
						dateTimeStart,
						dateTimeFinish,
						getDay(dateTimeStart.getDayOfWeek()),
						getShiftDay(dateTimeStart.getHourOfDay()),
						total,
						pTotal);
			}
		}else{//start and finish different day
			
			if(dateTimeStart.getHourOfDay() < 22) {
				DateTime dateTimeStart1 = new DateTime(dateTimeStart.getYear(), dateTimeStart.getMonthOfYear(), 
						dateTimeStart.getDayOfMonth(), 22, 0);
				
				if(dateTimeFinish.getHourOfDay() > 6){
					DateTime dateTimeFinish1 = new DateTime(dateTimeFinish.getYear(), dateTimeFinish.getMonthOfYear(), 
							dateTimeFinish.getDayOfMonth(), 6, 0);
					total = Minutes.minutesBetween(dateTimeStart, dateTimeStart1).getMinutes();
					pTotal = total;
					if(dateTimeStart.getDayOfWeek() == 6){//Saturday
						pTotal = (int) (pTotal + (total*rhTypeRate7.getValue()));
					}
					if(dateTimeStart.getDayOfWeek() == 7){ //sunday
						pTotal = (int) (pTotal + (total*rhTypeRate8.getValue()));
					}
					registerRhShiftDetail2(rhShift, 
							rhShift.getRhTypeCharge(), 
							rhTypeRate1,
							dateTimeStart,
							dateTimeStart1,
							getDay(dateTimeStart.getDayOfWeek()),
							getShiftDay(dateTimeStart.getHourOfDay()),
							total,
							pTotal);
					total = Minutes.minutesBetween(dateTimeStart1, dateTimeFinish1).getMinutes();
					pTotal = total;
					pTotal = (int) (total + (rhTypeRate4.getValue() * total * (60.0/rhTypeRate5.getValue())));
					if(dateTimeStart.getDayOfWeek() == 6){//Saturday
						pTotal = (int) (pTotal + (total*rhTypeRate7.getValue()));
					}
					if(dateTimeStart.getDayOfWeek() == 7){ //sunday
						pTotal = (int) (pTotal + (total*rhTypeRate8.getValue()));
					}
					registerRhShiftDetail2(rhShift, 
							rhShift.getRhTypeCharge(), 
							rhTypeRate1,
							dateTimeStart1,
							dateTimeFinish1,
							getDay(dateTimeStart.getDayOfWeek()),
							getShiftDay(dateTimeStart1.getHourOfDay()),
							total,
							pTotal);
					
					total = Minutes.minutesBetween(dateTimeFinish1, dateTimeFinish).getMinutes();
					pTotal = total;
					if(dateTimeStart.getDayOfWeek() == 6){//Saturday
						pTotal = (int) (pTotal + (total*rhTypeRate7.getValue()));
					}
					if(dateTimeStart.getDayOfWeek() == 7){ //sunday
						pTotal = (int) (pTotal + (total*rhTypeRate8.getValue()));
					}
					registerRhShiftDetail2(rhShift, 
							rhShift.getRhTypeCharge(), 
							rhTypeRate1,
							dateTimeFinish1,
							dateTimeFinish,
							getDay(dateTimeStart.getDayOfWeek()),
							getShiftDay(dateTimeFinish1.getHourOfDay()),
							total,
							pTotal);
					
				}else{
					total = Minutes.minutesBetween(dateTimeStart, dateTimeStart1).getMinutes();
					pTotal = total;
					if(dateTimeStart.getDayOfWeek() == 6){//Saturday
						pTotal = (int) (pTotal + (total*rhTypeRate7.getValue()));
					}
					if(dateTimeStart.getDayOfWeek() == 7){ //sunday
						pTotal = (int) (pTotal + (total*rhTypeRate8.getValue()));
					}
					registerRhShiftDetail2(rhShift, 
							rhShift.getRhTypeCharge(), 
							rhTypeRate1,
							dateTimeStart,
							dateTimeStart1,
							getDay(dateTimeStart.getDayOfWeek()),
							getShiftDay(dateTimeStart.getHourOfDay()),
							total,
							pTotal);
					total = Minutes.minutesBetween(dateTimeStart1, dateTimeFinish).getMinutes();
					pTotal = total;
					pTotal = (int) (total + (rhTypeRate4.getValue() * total * (60.0/rhTypeRate5.getValue())));
					if(dateTimeStart.getDayOfWeek() == 6){//Saturday
						pTotal = (int) (pTotal + (total*rhTypeRate7.getValue()));
					}
					if(dateTimeStart.getDayOfWeek() == 7){ //sunday
						pTotal = (int) (pTotal + (total*rhTypeRate8.getValue()));
					}
					registerRhShiftDetail2(rhShift, 
							rhShift.getRhTypeCharge(), 
							rhTypeRate1,
							dateTimeStart1,
							dateTimeFinish,
							getDay(dateTimeStart.getDayOfWeek()),
							getShiftDay(dateTimeStart1.getHourOfDay()),
							total,
							pTotal);
				}
			}else{
				if(dateTimeFinish.getHourOfDay() > 6){
					DateTime dateTimeFinish1 = new DateTime(dateTimeFinish.getYear(), dateTimeFinish.getMonthOfYear(), 
							dateTimeFinish.getDayOfMonth(), 6, 0);
					total = Minutes.minutesBetween(dateTimeStart, dateTimeFinish1).getMinutes();
					pTotal = total;
					pTotal = (int) (total + (rhTypeRate4.getValue() * total * (60.0/rhTypeRate5.getValue())));
					if(dateTimeStart.getDayOfWeek() == 6){//Saturday
						pTotal = (int) (pTotal + (total*rhTypeRate7.getValue()));
					}
					if(dateTimeStart.getDayOfWeek() == 7){ //sunday
						pTotal = (int) (pTotal + (total*rhTypeRate8.getValue()));
					}
					registerRhShiftDetail2(rhShift, 
							rhShift.getRhTypeCharge(), 
							rhTypeRate1,
							dateTimeStart,
							dateTimeFinish1,
							getDay(dateTimeStart.getDayOfWeek()),
							getShiftDay(dateTimeStart.getHourOfDay()),
							total,
							pTotal);
					
					total = Minutes.minutesBetween(dateTimeFinish1, dateTimeFinish).getMinutes();
					pTotal = total;
					if(dateTimeStart.getDayOfWeek() == 6){//Saturday
						pTotal = (int) (pTotal + (total*rhTypeRate7.getValue()));
					}
					if(dateTimeStart.getDayOfWeek() == 7){ //sunday
						pTotal = (int) (pTotal + (total*rhTypeRate8.getValue()));
					}
					registerRhShiftDetail2(rhShift, 
							rhShift.getRhTypeCharge(), 
							rhTypeRate1,
							dateTimeFinish1,
							dateTimeFinish,
							getDay(dateTimeStart.getDayOfWeek()),
							getShiftDay(dateTimeFinish1.getHourOfDay()),
							total,
							pTotal);
				}else{
					total = Minutes.minutesBetween(dateTimeStart, dateTimeFinish).getMinutes();
					pTotal = total;
					if(dateTimeStart.getDayOfWeek() == 6){//Saturday
						pTotal = (int) (pTotal + (total*rhTypeRate7.getValue()));
					}
					if(dateTimeStart.getDayOfWeek() == 7){ //sunday
						pTotal = (int) (pTotal + (total*rhTypeRate8.getValue()));
					}
					registerRhShiftDetail2(rhShift, 
							rhShift.getRhTypeCharge(), 
							rhTypeRate1,
							dateTimeStart,
							dateTimeFinish,
							getDay(dateTimeStart.getDayOfWeek()),
							getShiftDay(dateTimeStart.getHourOfDay()),
							total,
							pTotal);
				}
			}
		}
	}
	
	
	public void processBraDynamicDiscount(RhShift rhShift, boolean takenBreak){
		RhType rhTypeRate1 = typeDAO.getRhType("SH_RATE", 1);
		DateTime dateTimeStart = new DateTime(rhShift.getDateStartShift());
		DateTime dateTimeFinish = new DateTime(rhShift.getDateFinishShift());
		int total = Minutes.minutesBetween(dateTimeStart, dateTimeFinish).getMinutes();
		int pTotal  = total;
		registerRhShiftDetail2(rhShift, 
				rhShift.getRhTypeCharge(), 
				rhTypeRate1,
				dateTimeStart,
				dateTimeFinish,
				total,
				pTotal);
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
		DateTime dateTimeStart = new DateTime(rhShift.getDateStart());
		RhShiftDetail2 rhShiftDetail1 = new RhShiftDetail2();
		rhShiftDetail1.setRhShift(rhShift);
		rhShiftDetail1.setRhType(rhShift.getRhTypeCharge());
		rhShiftDetail1.setRhTypeRate(rhTypeRate1);
		rhShiftDetail1.setTimeStart(rhShift.getDateStart());
		rhShiftDetail1.setTimeFinish(rhShift.getDateFinish());
		rhShiftDetail1.setTotal(0);
		rhShiftDetail1.setTotalRate(0);
		rhShiftDetail1.setRhTypeDay(getDay(dateTimeStart.getDayOfWeek()));
		rhShiftDetail1.setRhTypeShiftDay(getShiftDay(dateTimeStart.getHourOfDay()));
		rhShiftDetail1.setComment("Process 0");
		save(rhShiftDetail1);
	}

	
	public RhShiftDetail2 proccessAbroadJob(RhShift rhShift) {
		RhShiftDetail2 rhShiftDetail1 = null;
		RhSchedulePerson rhSchedulePerson = schedulePersonDAO.get(rhShift.getRhPerson());
		DateTime startTime = new DateTime(rhShift.getDateStart());
		DateTime finishTime = new DateTime(rhShift.getDateFinish());
		int diffDays = Days.daysBetween(startTime, finishTime).getDays();
		if (diffDays > rhSchedulePerson.getRhSchedule().getMinCommission()) {
			rhShiftDetail1 = new RhShiftDetail2();
			int daysComm = diffDays/3;
			RhType rhTypeRate1 = typeDAO.getRhType("SH_RATE", 1);
			rhShiftDetail1.setRhShift(rhShift);
			rhShiftDetail1.setRhType(typeDAO.getRhType("SHIFT_DETAIL", 3));
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
			Map<String, Object> map1 = shiftDetailDAO2.sumTotal(rhShift, type1);
			tot1 = (Long) ((map1 == null) ? 0L : map1.get("totalRate")) ;  
			Map<String, Object> map2 = shiftDetailDAO2.sumTotal(rhShift, type2);
			tot2 = (Long) ((map2 == null) ? 0L : map2.get("totalRate")) ;  
			Map<String, Object> map3 = shiftDetailDAO2.sumTotal(rhShift, type3);
			tot3 = (Long) ((map3 == null) ? 0L : map3.get("totalRate")) ;  
			Map<String, Object> map4 = shiftDetailDAO2.sumTotal(rhShift, type4);
			tot4 = (Long) ((map4 == null) ? 0L : map4.get("totalRate")) ;  
			Map<String, Object> map5 = shiftDetailDAO2.sumTotal(rhShift, type5);
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
//		RhType rhTypeRate = typeDAO.getRhType("SH_RATE", 1);
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
			Map<String, Object> map1 = shiftDetailDAO2.sumTotalChild(rhShift, type1);
			tot1 = (Long) ((map1 == null) ? 0L : map1.get("totalRate")) ;  
			Map<String, Object> map2 = shiftDetailDAO2.sumTotalChild(rhShift, type2);
			tot2 = (Long) ((map2 == null) ? 0L : map2.get("totalRate")) ;  
			Map<String, Object> map3 = shiftDetailDAO2.sumTotalChild(rhShift, type3);
			tot3 = (Long) ((map3 == null) ? 0L : map3.get("totalRate")) ;  
			Map<String, Object> map4 = shiftDetailDAO2.sumTotalChild(rhShift, type4);
			tot4 = (Long) ((map4 == null) ? 0L : map4.get("totalRate")) ;  
			Map<String, Object> map5 = shiftDetailDAO2.sumTotalChild(rhShift, type5);
			tot5 = (Long) ((map5 == null) ? 0L : map5.get("totalRate")) ;  
			
			Map<String, Object> map6 = shiftDetailDAO2.sumTotalChild2(rhShift, type1);
			tot6 = (Long) ((map6 == null) ? 0L : map6.get("totalRate")) ;  
			Map<String, Object> map7 = shiftDetailDAO2.sumTotalChild2(rhShift, type2);
			tot7 = (Long) ((map7 == null) ? 0L : map7.get("totalRate")) ;  
			Map<String, Object> map8 = shiftDetailDAO2.sumTotalChild2(rhShift, type3);
			tot8 = (Long) ((map8 == null) ? 0L : map8.get("totalRate")) ;  
			Map<String, Object> map9 = shiftDetailDAO2.sumTotalChild2(rhShift, type4);
			tot9 = (Long) ((map9 == null) ? 0L : map9.get("totalRate")) ;  
			Map<String, Object> map10 = shiftDetailDAO2.sumTotalChild2(rhShift, type5);
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
			Map<String, Object> map1 = shiftDetailDAO2.sumTotal(rhPerson, type1, rhShiftPeriod);
			tot1 = (Long) ((map1 == null) ? 0L : map1.get("totalRate")) ;  
			Map<String, Object> map2 = shiftDetailDAO2.sumTotal(rhPerson, type2, rhShiftPeriod);
			tot2 = (Long) ((map2 == null) ? 0L : map2.get("totalRate")) ;  
			Map<String, Object> map3 = shiftDetailDAO2.sumTotal(rhPerson, type3, rhShiftPeriod);
			tot3 = (Long) ((map3 == null) ? 0L : map3.get("totalRate")) ;  
			Map<String, Object> map4 = shiftDetailDAO2.sumTotal(rhPerson, type4, rhShiftPeriod);
			tot4 = (Long) ((map4 == null) ? 0L : map4.get("totalRate")) ;  
			Map<String, Object> map5 = shiftDetailDAO2.sumTotal(rhPerson, type5, rhShiftPeriod);
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
		map.put("WORK_HOUR_C",shiftDetailDAO2.sumTotal(typeDAO.getRhType("SHIFT_DETAIL", 1),
				rhPerson, rhShiftPeriodCurrent)/factMinutes);
		map.put("WORK_HOUR_L", (double) shiftDetailDAO2.sumTotal(typeDAO.getRhType("SHIFT_DETAIL", 1), 
				rhPerson, rhShiftPeriodLast)/factMinutes);
		map.put("EXTRA_HOUR_C", (double) shiftDetailDAO2.sumTotal(typeDAO.getRhType("SHIFT_DETAIL", 5), 
				rhPerson, rhShiftPeriodLast)/factMinutes);
		map.put("EXTRA_HOUR_L", (double) shiftDetailDAO2.sumTotal(typeDAO.getRhType("SHIFT_DETAIL", 5), 
				rhPerson, rhShiftPeriodLast)/factMinutes);
		map.put("DISCOUNT_C", (double) shiftDetailDAO2.sumTotal(typeDAO.getRhType("SHIFT_DETAIL", 2), 
				rhPerson, rhShiftPeriodLast)/factMinutes);
		map.put("DISCOUNT_L", (double) shiftDetailDAO2.sumTotal(typeDAO.getRhType("SHIFT_DETAIL", 2), 
				rhPerson, rhShiftPeriodLast)/factMinutes);
		map.put("COMENSATION_C", (double) shiftDetailDAO2.sumTotal(typeDAO.getRhType("SHIFT_DETAIL", 3), 
				rhPerson, rhShiftPeriodLast)/factMinutes);
		map.put("COMENSATION_L", (double) shiftDetailDAO2.sumTotal(typeDAO.getRhType("SHIFT_DETAIL", 3), 
				rhPerson, rhShiftPeriodLast)/factMinutes);
		map.put("POOL_COMENSATION", rhSchedulePerson.getCompensation()/factMinutes);
		return map;
	}
	
	
	public Integer getTotalTime(RhShift rhShift){
		RhType rhTypeRate = typeDAO.getRhType("SH_RATE", 1);
		Integer total = shiftDetailDAO2.getTotal(rhShift, rhTypeRate);
		if (total ==  null ) return 0;
		else return total;
	}
	
	
	public Integer getTotalProcessedTime(RhShift rhShift){
		Integer total = shiftDetailDAO2.getSumTotal(rhShift);
		if (total ==  null ) return 0;
		else return total;
	}
	
	
	public List<RhShiftDetail2> list(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod){
		return shiftDetailDAO2.list(rhPerson, rhShiftPeriod);
	}
	
	
	public List<RhShiftDetail2> list(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod, RhStatus rhStatusSis){
		return shiftDetailDAO2.list(rhPerson, rhShiftPeriod, rhStatusSis);
	}
	
	
	public List<RhShiftDetail2> list(RhShiftPeriod rhShiftPeriod){
		return shiftDetailDAO2.list(rhShiftPeriod);
	}
	
	
	public List<RhShiftDetail2> listMng(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod){
		return shiftDetailDAO2.listMng(rhPerson, rhShiftPeriod);
	}
}