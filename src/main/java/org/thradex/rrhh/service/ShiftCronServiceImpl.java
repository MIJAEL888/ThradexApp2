package org.thradex.rrhh.service;

import org.thradex.util.ConstantsSis;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;


public class ShiftCronServiceImpl implements ShiftCronService{
	
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);
	
	@Autowired
	private AbroadJobService abroadJobService;
	
	@Autowired
	private LicenseService licenseService;
	
	@Autowired
	private ScheduleScaleService scheduleScaleService;
	
	@Autowired
	private CommissionService commissionService;
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private ShiftPeriodService periodService ; 
	
	@Autowired
	private ShiftService shiftService;
	
	@PostConstruct
    public void onStartup() {
//		shiftService.updateDayDetail();
//		shiftService.updateDetail();
		//shiftService.updateDetail2();
//		shiftService.updateResponsible();
//		shiftService.updateTypeCharge();
		periodService.updatePeriod();
		//scheduleScaleService.createScale();
		//scheduleScaleService.checkActiveSessions();
    }

	/*
	 * Method run every hour and it checks:
	 * - All session are close automatic after 5 hours
	 * - 
	 * - 
	 */
	
	@Scheduled(cron="0 0 * * * ?") //(0 0 * * * ?) second, minute, hour, day of month, month, day(s) of week
	public void runEveryHour() {
		//when commission finished we will calculate the compensation time
		//abroadJobService.checkFinish();
		//licenseService.checkFinish();
		//scheduleScaleService.checkActiveSessions();
		//commissionService.checkFinish();
		//permissionService.checkFinish();
	}
	
	/*
	 * Method run at 02:00 hours and it checks:
	 * - Create Shift Scale for the day for each user.
	 * - 
	 * 
	 */
	
	@Scheduled(cron="0 0 2 * * ?") // (0 0 2 * * ?) second, minute, hour, day of month, month, day(s) of week
	public void runMidNight() {
		//periodService.updatePeriod();
		//scheduleScaleService.createScale();
		//scheduleScaleService.checkAbsentSessions();
	}
	
}