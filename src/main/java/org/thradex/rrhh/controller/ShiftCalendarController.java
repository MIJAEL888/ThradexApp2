package org.thradex.rrhh.controller;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thradex.model.SisUser;
import org.thradex.rrhh.service.ShiftCalendarService;
import org.thradex.rrhh.service.ShiftReportService;
import org.thradex.rrhh.service.UtilRrhhService;
import org.thradex.sis.service.CurrentUserService;
import org.thradex.util.ConstantsSis;

@Controller
@RequestMapping("/shiftCalendar")
public class ShiftCalendarController {

	protected static Logger log = Logger.getLogger(ConstantsSis.LOG_CONTROLLER);
	
	@Autowired
	private CurrentUserService currentUserService;
	
	@Autowired
	private ShiftReportService shiftReportService;
	
	@Autowired
	private ShiftCalendarService shiftCalendarService;
	
	@Autowired
	private UtilRrhhService utilService;
	
	
	@RequestMapping(value = "/getPage", method = RequestMethod.GET)
	public String getPage(Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		Date nowDate = utilService.getNowDate(user.getRhPerson());
		model.addAttribute("dateNow", nowDate);
		model.addAttribute("listPerson", shiftCalendarService.listRhPerson(user.getRhPerson()));
		model.addAttribute("listEvents", shiftCalendarService.listEvents());
		return "html/rrhh/shift/shiftCalendar";
	}
	
	
	@RequestMapping(value = "/detailShift", method = RequestMethod.GET)
	public String detailShift(@RequestParam(required=false) Integer idShift,
			@RequestParam(required=false) Integer idScale,	Model model, HttpSession session, HttpServletRequest request){
//		SisUser user = currentUserService.validUser(session);
		if(idShift != null && idShift != 0){
			model.addAttribute("rhShift", shiftCalendarService.getRhShift(idShift));
			return "html/rrhh/shift/detail :: detailShift";
		}else if(idScale != null && idScale != 0){
			model.addAttribute("rhScale", shiftCalendarService.getRhScheduleScale(idScale));
			return "html/rrhh/shift/detail :: detailScale";
		}else{
			
			return "html/rrhh/shift/detail :: detailNone";
		}
	}
	
	@RequestMapping(value = "/updatePendingMng", method = RequestMethod.GET)
	public String updatePendingMng(@RequestParam(required=false) Integer idPeriod,
			@RequestParam(required=false) Integer idPerson,	Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		model.addAttribute("listShifts", shiftReportService.listPending(idPeriod, idPerson, user.getRhPerson()));
		return "html/rrhh/shift/tables :: reportPending";
	}
	@RequestMapping(value = "/regScheduleScale", method = RequestMethod.GET)
	public String regScheduleScale(@RequestParam int idPerson, @RequestParam int idScale,  
			@RequestParam String startDate, Model model,HttpSession session, HttpServletRequest request) throws Exception{
		shiftCalendarService.createScheduleScale(idPerson, idScale, startDate);
		model.addAttribute("message", "Created ok");
		
		return "html/rrhh/scale/detail :: messageScale";
	}
	
	@RequestMapping(value = "/deleteScheduleScale", method = RequestMethod.GET)
	public String deleteScheduleScale(@RequestParam int id, Model model,HttpSession session, HttpServletRequest request) throws Exception{
		
		shiftCalendarService.deleteScheduleScale(id);
		model.addAttribute("message", "The scale has been deleted successfully.");
		return "html/rrhh/scale/detail :: messageScale";
	}
	
	@RequestMapping(value = "/events", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> events(@RequestParam String start, @RequestParam String end,  
			HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		return  shiftCalendarService.listScheduleScale(user.getRhPerson(), start,end);
	} 
	
	@RequestMapping(value = "/eventsOne", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> eventsOne(@RequestParam(required=false) Integer idPerson, 
			@RequestParam(required=false) Integer idEvent, 
			@RequestParam String start, @RequestParam String end,  
			HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		return  shiftCalendarService.listScheduleScale(user.getRhPerson(), idPerson, idEvent, start,end);
	} 
	
	/*
	 * Ajax response
	 * 
	 */
	@RequestMapping(value = "/period", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> period(@RequestParam int idCompany){
		return shiftReportService.listMapRhShiftPeriods(idCompany);
	} 
	
	@RequestMapping(value = "/person", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> person(@RequestParam int idCompany){
		return shiftReportService.listMapRhPerson(idCompany);
	} 
	
	/*
	 * METHODS FOR FILE
	 * 
	 */
	
	public static void write(HttpServletResponse response, ByteArrayOutputStream bao) {
		   
		log.debug("Writing report to the stream");
		  try {
		   // Retrieve the output stream
		   ServletOutputStream outputStream = response.getOutputStream();
		   // Write to the output stream
		   bao.writeTo(outputStream);
		   // Flush the stream
		   outputStream.flush();
		   // Close the stream
		   outputStream.close();
		 
		  } catch (Exception e) {
			  log.error("Unable to write report to the output stream");
		  }
		 }
		  
	public static void write(HttpServletResponse response, byte[] byteArray) {
		   
			 log.debug("Writing report to the stream");
		  try {
		   // Retrieve the output stream
		   ServletOutputStream outputStream = response.getOutputStream();
		   // Write to the output stream
		   outputStream.write(byteArray);
		   // Flush the stream
		   outputStream.flush();
		   // Close the stream
		   outputStream.close();
		    
		  } catch (Exception e) {
			  log.error("Unable to write report to the output stream");
		  }
	} 
}