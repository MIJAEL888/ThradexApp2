package org.thradex.rrhh.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.thradex.model.RhFile;
import org.thradex.model.RhPerson;
import org.thradex.model.RhSchedulePerson;
import org.thradex.model.RhShift;
import org.thradex.model.SisIpOffice;
import org.thradex.rrhh.dao.SchedulePersonDAO;
import org.thradex.rrhh.dao.UtilRrhhDAO;
import org.thradex.sis.dao.IpOfficeDAO;
import org.thradex.util.ConstantsSis;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UtilRrhhServiceImpl implements UtilRrhhService {
	
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);

	@Autowired
	private UtilRrhhDAO utilRrhhDAO;
	
	@Autowired
	private IpOfficeDAO ipOfficeDAO;
	
	@Autowired
	private SchedulePersonDAO schedulePersonDAO;
	
	@Autowired
	private FilesService filesService;

	
	@Override
	public Date getNowDate() {
		return utilRrhhDAO.getNowDate();
	}
	
	@Override
	public Date getNowDate(RhPerson rhPerson) {
		RhSchedulePerson rhSchedulePerson = schedulePersonDAO.get(rhPerson);
		return getNowDate(rhSchedulePerson);
	}
	
	@Override
	public Date getNowDate(RhSchedulePerson rhSchedulePerson) {
		
		Date nowDate = getNowDate();
		
		DateFormat converter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		DateFormat converter2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
		if(rhSchedulePerson != null && rhSchedulePerson.getSisRegion() != null){		//NOC
			converter.setTimeZone(TimeZone.getTimeZone(rhSchedulePerson.getSisRegion().getTimeZone()));
		}else{
			converter.setTimeZone(TimeZone.getTimeZone("America/Lima"));
		}
		
		try {
			nowDate = converter2.parse(converter.format(nowDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return nowDate;
	}
	
	@Override
	public boolean validIp(String ip) {
		// verify if IP is valid
		SisIpOffice sisIpOffice = ipOfficeDAO.getSisIpOffice(ip);
		if (sisIpOffice != null) return true;
		return false;
		
	}
	
	@Override
	public int getMinutes(Date startDate, Date finishDate){
		DateTime startDateTime = new DateTime(startDate);
		DateTime finishDateTime = new DateTime(finishDate);
		int gapMin = Minutes.minutesBetween(startDateTime, finishDateTime).getMinutes();
		return gapMin;
	}
	
	@Override
	public int getHours(Date startDate, Date finishDate){
		DateTime startDateTime = new DateTime(startDate);
		DateTime finishDateTime = new DateTime(finishDate);
		int gapMin = Hours.hoursBetween(startDateTime, finishDateTime).getHours();
		return gapMin;
	}

	@Override
	public RhShift saveFiles(RhShift rhShift) {
		if (rhShift.getFiles() != null) {
			for (CommonsMultipartFile file : rhShift.getFiles()) {
				RhFile rhFile = filesService.saveOnDisk(file);
				rhFile.setRhShift(rhShift);
				filesService.save(rhFile);
			}
		}
		return rhShift;
	}
	
	@Override
	public String getStringDay(Date startDate, Date finishDate){
		DateTime startDateTime = new DateTime(startDate);
		DateTime finishDateTime = new DateTime(finishDate);
		int days = Days.daysBetween(startDateTime, finishDateTime).getDays();
		return days + " Days";
//		int hour = 0;
//		int day = 0;
//		if (min < 60 * 24) return min + " M";
//		else{
//			hour = min/60;
//			if(hour < 24) {
//				min = min - (hour * 60);
//				return hour + " H : " + min + " M";
//			}else{
//				day = hour / 24;
//				hour = hour - (day * 24);
//				min = min - (day * 24 * 60) - (hour * 60);
//				return day + " D : " + hour + " H : " + min + " M";
//			}
//		}
	}
	
	@Override
	public String getStringHours(Integer minutes){
		DecimalFormat df = new DecimalFormat("#.##");
		if(minutes.intValue() == 0) return "0";
		else if(minutes.intValue() < 60 ) return minutes + " Min";
		else return df.format(minutes/60.0) + " Hours";	
	}
}