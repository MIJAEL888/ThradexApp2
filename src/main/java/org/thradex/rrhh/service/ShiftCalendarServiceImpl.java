package org.thradex.rrhh.service;

import org.springframework.transaction.annotation.Transactional;
import org.thradex.exception.CustomGenericException;
import org.thradex.model.RhPerson;
import org.thradex.model.RhSchedulePerson;
import org.thradex.model.RhScheduleScale;
import org.thradex.model.RhShift;
import org.thradex.model.RhStaff;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ShiftCalendarServiceImpl implements ShiftCalendarService {
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);
	

	@Autowired
	private TypeDAO typeDAO;
	
	@Autowired
	private StatusDAO statusDAO;
	
	@Autowired
	private UtilRrhhService utilService;

	@Autowired
	private SchedulePersonDAO schedulePersonDAO;
	
	@Autowired
	private ScheduleDayDAO scheduleDayDAO;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private ScheduleScaleDAO scheduleScaleDAO;
	
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private ShiftDAO shiftDAO;
	
	@Autowired
	private FileDAO fileDAO;
	
	/*
	 * (non-Javadoc)
	 * @see org.thradex.rrhh.service.ScheduleScaleService#deleteScheduleScale(int)
	 * 
	 * Methods for get calendar.
	 * 
	 */
	
	public List<RhPerson> listRhPerson(RhPerson rhPerson) {
		log.info("Getting list RhSchedulePerson");
		RhStatus rhStatus = statusDAO.getRhStatus("PERSON", 1);
		List<RhPerson> rhPersons = personService.list(rhPerson.getRhCompany(), rhStatus);
		if(personService.isManager(rhPerson)){
			List<RhPerson> rhPersons1 =  personService.list(rhPerson);
			for (RhPerson rhPerson2 : rhPersons1) {
				rhPersons.add(rhPerson2);
				if(personService.isManager(rhPerson2)){
					rhPersons.addAll(personService.list(rhPerson2));
				}
			}
		}else{
			RhStaff rhStaff = staffService.get(rhPerson);
			rhPersons = personService.list(rhStaff.getRhAreaPosition().getRhArea());		
		}
		
		return removeDuplicates(rhPersons);
	}
	
	
	public List<RhType> listEvents(){
		List<RhType> rhTypes = new ArrayList<RhType>();  
		
		for (RhType rhType : typeDAO.getRhType("SHIFT")) {
			if(rhType.getCode() == 1 || 
					rhType.getCode() == 14 || 
					rhType.getCode() == 15 || 
					rhType.getCode() == 2 ||
					rhType.getCode() == 3 || 
					rhType.getCode() == 4 || 
					rhType.getCode() == 5 || 
					rhType.getCode() == 6){
				rhTypes.add(rhType);
			}
		}
		
		return rhTypes;
	}
	
	
	public RhShift getRhShift(Integer idShift){
		RhShift rhShift = shiftDAO.getRhShift(idShift);
		rhShift.setRhFiles(fileDAO.getRhFile(rhShift));
		rhShift.setRhShifts(shiftDAO.list(rhShift));
		for (RhShift rhShiftChild : rhShift.getRhShifts()) {
			rhShiftChild.setRhFiles(fileDAO.getRhFile(rhShiftChild));
		}
		return rhShift;
	}
	
	
	public RhScheduleScale getRhScheduleScale(Integer idScale){
		return scheduleScaleDAO.get(idScale);
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
		Date now = utilService.getNowDate();
		Date fromDate  = new Date();
		Date toDate = new Date();
		List<Map<String, Object>>  list = new ArrayList<Map<String,Object>>();
		
		try {
			fromDate  = format.parse(from);
			toDate = format.parse(to);
			RhType rhTypeStatic = typeDAO.getRhType("RH_SCHEDULE", 1); // static Schedule
			RhType rhTypeDynamic = typeDAO.getRhType("RH_SCHEDULE", 2); // dynamic Schedule
			RhStatus rhStatusActive = statusDAO.getRhStatus("RH_SCH_PERSON", 1); // Active schedule person.
			
			List<RhPerson> rhPersons = listRhPerson(rhPerson);
			for (RhPerson rhPerson2 : rhPersons) {
				RhSchedulePerson rhSchedulePerson = schedulePersonDAO.get(rhPerson2 , rhStatusActive);
				if(rhSchedulePerson != null ) {
					List<RhShift> rhShifts = shiftDAO.list(rhPerson2, fromDate, toDate);
					for (RhShift rhShift : rhShifts) {
						if(rhShift.getRhType().getCode() == 1 || 
								rhShift.getRhType().getCode() == 14 || 
								rhShift.getRhType().getCode() == 15 || 
								rhShift.getRhType().getCode() == 2 ||
								rhShift.getRhType().getCode() == 3 || 
								rhShift.getRhType().getCode() == 4 || 
								rhShift.getRhType().getCode() == 5 || 
								rhShift.getRhType().getCode() == 6){
							
							DateTime timeStart = new DateTime(rhShift.getDateStart());
							DateTime timeEnd = new DateTime(rhShift.getDateFinish());
							
							Map<String, Object> map =  new HashMap<String, Object>();
							
							map.put("title", rhShift.getRhPerson().getShortname() 
									+ "-" + rhShift.getRhType().getName() + " "
									+ " ("
									+convertIntToString2(timeStart.getHourOfDay())+":"
									+convertIntToString2(timeStart.getMinuteOfHour()) + " - "	
									+convertIntToString2(timeEnd.getHourOfDay())+":"
									+convertIntToString2(timeEnd.getMinuteOfHour()) +")");
							
							map.put("start", format.format(timeStart.toDate())+"T"
									+convertIntToString2(timeStart.getHourOfDay())+":"
									+convertIntToString2(timeStart.getMinuteOfHour()) );
							
							map.put("end", format.format(timeEnd.toDate())+"T"
									+convertIntToString2(timeEnd.getHourOfDay())+":"
									+convertIntToString2(timeEnd.getMinuteOfHour()) );
							map.put("color", rhShift.getRhType().getColor());
							map.put("id", rhShift.getId());
							map.put("idPerson", rhShift.getRhPerson().getId());
							map.put("idScale", 0);
							list.add(map);
						}
					}
					
					if(rhSchedulePerson.getRhSchedule().getRhType().getId() == rhTypeStatic.getId()){ //if is a static schedule 
						
					}else if(rhSchedulePerson.getRhSchedule().getRhType().getId() == rhTypeDynamic.getId()){ //if is a dynamic schedule 
						List<RhScheduleScale> rhScheduleScales = scheduleScaleDAO.list2(rhSchedulePerson, now, toDate);
						for (RhScheduleScale rhScheduleScale : rhScheduleScales) {
							DateTime time = new DateTime(rhScheduleScale.getDate());
							DateTime timeEnd = time.plusHours(rhScheduleScale.getRhScheduleDay().getTotalHours());
							
							Map<String, Object> map =  new HashMap<String, Object>();
							
							map.put("title", rhScheduleScale.getRhSchedulePerson().getRhPerson().getShortname() 
									+ "-SCALE "
									+ " ("
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
				}
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<Map<String, Object>> listScheduleScale(RhPerson rhPerson, Integer idPerson, Integer idEvent,
			String from, String to) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date now = utilService.getNowDate();
		Date fromDate  = new Date();
		Date toDate = new Date();
		List<Map<String, Object>>  list = new ArrayList<Map<String,Object>>();
		
		try {
			fromDate  = format.parse(from);
			toDate = format.parse(to);
			RhType rhTypeStatic = typeDAO.getRhType("RH_SCHEDULE", 1); // static Schedule
			RhType rhTypeDynamic = typeDAO.getRhType("RH_SCHEDULE", 2); // dynamic Schedule
			RhStatus rhStatusActive = statusDAO.getRhStatus("RH_SCH_PERSON", 1); // Active schedule person.
			
			List<RhPerson> rhPersons = new ArrayList<RhPerson>();
			if(idPerson != null && idPerson != 0){
				rhPersons.add(personService.get(idPerson));
			}else{
				rhPersons = listRhPerson(rhPerson);
			}
			
			for (RhPerson rhPerson2 : rhPersons) {
				RhSchedulePerson rhSchedulePerson = schedulePersonDAO.get(rhPerson2 , rhStatusActive);
				if(rhSchedulePerson != null ) {
					List<RhShift> rhShifts = shiftDAO.list(rhPerson2, fromDate, toDate);
					for (RhShift rhShift : rhShifts) {
						if(idEvent != null && idEvent != 0){
							if(rhShift.getRhType().getCode() == idEvent){
								
								DateTime timeStart = new DateTime((rhShift.getDateStartShift() == null) ? rhShift.getDateStart() : rhShift.getDateStartShift());
								DateTime timeEnd = new DateTime((rhShift.getDateFinishShift() == null) ? rhShift.getDateFinish() : rhShift.getDateFinishShift());
								
								Map<String, Object> map =  new HashMap<String, Object>();
								
								map.put("title", rhShift.getRhPerson().getShortname() 
										+ "-" + rhShift.getRhType().getName() + " "
										+ " ("
										+convertIntToString2(timeStart.getHourOfDay())+":"
										+convertIntToString2(timeStart.getMinuteOfHour()) + " - "	
										+convertIntToString2(timeEnd.getHourOfDay())+":"
										+convertIntToString2(timeEnd.getMinuteOfHour()) +")");
								
								map.put("start", format.format(timeStart.toDate())+"T"
										+convertIntToString2(timeStart.getHourOfDay())+":"
										+convertIntToString2(timeStart.getMinuteOfHour()) );
								
								map.put("end", format.format(timeEnd.toDate())+"T"
										+convertIntToString2(timeEnd.getHourOfDay())+":"
										+convertIntToString2(timeEnd.getMinuteOfHour()) );
								map.put("color", rhShift.getRhType().getColor());
								map.put("id", rhShift.getId());
								map.put("idPerson", rhShift.getRhPerson().getId());
								map.put("idScale", 0);
								list.add(map);
							}
						}else{
							if(rhShift.getRhType().getCode() == 1 || 
									rhShift.getRhType().getCode() == 14 || 
									rhShift.getRhType().getCode() == 15 || 
									rhShift.getRhType().getCode() == 2 ||
									rhShift.getRhType().getCode() == 3 || 
									rhShift.getRhType().getCode() == 4 || 
									rhShift.getRhType().getCode() == 5 || 
									rhShift.getRhType().getCode() == 6){
								
								DateTime timeStart = new DateTime((rhShift.getDateStartShift() == null) ? rhShift.getDateStart() : rhShift.getDateStartShift());
								DateTime timeEnd = new DateTime((rhShift.getDateFinishShift() == null) ? rhShift.getDateFinish() : rhShift.getDateFinishShift());
								
								Map<String, Object> map =  new HashMap<String, Object>();
								
								map.put("title", rhShift.getRhPerson().getShortname() 
										+ "-" + rhShift.getRhType().getName() + " "
										+ " ("
										+convertIntToString2(timeStart.getHourOfDay())+":"
										+convertIntToString2(timeStart.getMinuteOfHour()) + " - "	
										+convertIntToString2(timeEnd.getHourOfDay())+":"
										+convertIntToString2(timeEnd.getMinuteOfHour()) +")");
								
								map.put("start", format.format(timeStart.toDate())+"T"
										+convertIntToString2(timeStart.getHourOfDay())+":"
										+convertIntToString2(timeStart.getMinuteOfHour()) );
								
								map.put("end", format.format(timeEnd.toDate())+"T"
										+convertIntToString2(timeEnd.getHourOfDay())+":"
										+convertIntToString2(timeEnd.getMinuteOfHour()) );
								map.put("color", rhShift.getRhType().getColor());
								map.put("id", rhShift.getId());
								map.put("idPerson", rhShift.getRhPerson().getId());
								map.put("idScale", 0);
								list.add(map);
							}
						}
					}
					
					if (idEvent != null && idEvent != 0){
						if(idEvent != 100){
							if(rhSchedulePerson.getRhSchedule().getRhType().getId() == rhTypeStatic.getId()){ //if is a static schedule 
								
							}else if(rhSchedulePerson.getRhSchedule().getRhType().getId() == rhTypeDynamic.getId()){ //if is a dynamic schedule 
								List<RhScheduleScale> rhScheduleScales = scheduleScaleDAO.list(rhSchedulePerson, now, toDate);
								for (RhScheduleScale rhScheduleScale : rhScheduleScales) {
									DateTime time = new DateTime(rhScheduleScale.getDate());
									DateTime timeEnd = time.plusHours(rhScheduleScale.getRhScheduleDay().getTotalHours());
									
									Map<String, Object> map =  new HashMap<String, Object>();
									
									map.put("title", rhScheduleScale.getRhSchedulePerson().getRhPerson().getShortname() 
											+ "-SCALE "
											+ " ("
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
									map.put("id", 0);
									map.put("idPerson", rhScheduleScale.getRhSchedulePerson().getRhPerson().getId());
									map.put("idScale", rhScheduleScale.getId());
									list.add(map);
								}
							}
						}
					}else{
							if(rhSchedulePerson.getRhSchedule().getRhType().getId() == rhTypeStatic.getId()){ //if is a static schedule 
								
							}else if(rhSchedulePerson.getRhSchedule().getRhType().getId() == rhTypeDynamic.getId()){ //if is a dynamic schedule 
								List<RhScheduleScale> rhScheduleScales = scheduleScaleDAO.list2(rhSchedulePerson, now, toDate);
								for (RhScheduleScale rhScheduleScale : rhScheduleScales) {
									DateTime time = new DateTime(rhScheduleScale.getDate());
									DateTime timeEnd = time.plusHours(rhScheduleScale.getRhScheduleDay().getTotalHours());
									
									Map<String, Object> map =  new HashMap<String, Object>();
									
									map.put("title", rhScheduleScale.getRhSchedulePerson().getRhPerson().getShortname() 
											+ "-SCALE "
											+ " ("
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
									map.put("id", 0);
									map.put("idPerson", rhScheduleScale.getRhSchedulePerson().getRhPerson().getId());
									map.put("idScale", rhScheduleScale.getId());
									list.add(map);
								}
							}
						}
				}
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
	private String convertIntToString2(int num){
		String newNum =  num+"";
		if (newNum.length() == 1) 
			return "0"+newNum;
		else 
			return newNum;
	}

	public List<RhPerson> removeDuplicates(List<RhPerson> rhPersons){
		List<RhPerson> rhPersons2 = new ArrayList<RhPerson>();
		for (RhPerson rhPerson : rhPersons) {
			boolean isOnList  = false;
			for (RhPerson rhPerson2 : rhPersons2) {
				if(rhPerson.getId() == rhPerson2.getId()) isOnList = true;
			}
			if(!isOnList){
				rhPersons2.add(rhPerson);
			}
		}
		
		return rhPersons2;
	}

	
}