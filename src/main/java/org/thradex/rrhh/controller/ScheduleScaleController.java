package org.thradex.rrhh.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.thradex.model.RhShift;
import org.thradex.model.SisUser;
import org.thradex.rrhh.service.ScheduleScaleService;
import org.thradex.rrhh.service.UtilRrhhService;
import org.thradex.sis.service.CurrentUserService;
import org.thradex.util.ConstantsSis;

@Controller
@RequestMapping("shiftScale")
@SessionAttributes({"formShift", "formShiftAlert"})
public class ScheduleScaleController {

	protected static Logger log = Logger.getLogger(ConstantsSis.LOG_CONTROLLER);
	
	@Autowired
	private CurrentUserService currentUserService;
	
	@Autowired
	private ScheduleScaleService scheduleScaleService;
	
	@Autowired
	private UtilRrhhService utilService;
	
	@RequestMapping(value = "/getPage", method = RequestMethod.GET)
	public String getPage(Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		Date nowDate = utilService.getNowDate(user.getRhPerson());
		model.addAttribute("dateNow", nowDate);
		model.addAttribute("shiftScale", scheduleScaleService.validRegisterScale(user.getRhPerson(), request.getRemoteAddr()));
		model.addAttribute("activeSession", scheduleScaleService.listSession(user.getRhPerson()));
		model.addAttribute("listShifts", scheduleScaleService.listSession(user.getRhPerson()));
		return "html/rrhh/scale/scaleMngt";
	}
	
	@RequestMapping(value = "/getScale", method = RequestMethod.GET)
	public String getScale(Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		Date nowDate = utilService.getNowDate(user.getRhPerson());
		model.addAttribute("dateNow", nowDate);
		model.addAttribute("listPerson", scheduleScaleService.listRhSchedulePerson(user.getRhPerson()));
		return "html/rrhh/scale/scaleCalendar";
	}
	
	@RequestMapping(value = "/registerStart/{id}", method = RequestMethod.GET)
	public String registerStartScale(@PathVariable("id") int idRhShift, 
			Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		model.addAttribute("formShift",  scheduleScaleService.registerStartScale(user.getRhPerson(), 
				idRhShift, request.getRemoteAddr()));
		return "html/rrhh/scale/process :: messageStart";
	}
	
	@RequestMapping(value = "/registerFinish/{id}", method = RequestMethod.GET)
	public String registerFinishScale(@PathVariable("id") int idRhShift, 
			Model model, HttpSession session, HttpServletRequest request){
		model.addAttribute("formShift",  
				scheduleScaleService.registerFinishScale(idRhShift, request.getRemoteAddr()));
		return "html/rrhh/scale/process :: messageStart";
	}
	
	@RequestMapping(value = "/getShiftAlert/{id}", method = RequestMethod.GET)
	public String getShiftAlert(@PathVariable("id") int idRhShift, 
			Model model, HttpSession session, HttpServletRequest request){
		model.addAttribute("formShift",  scheduleScaleService.get(idRhShift));
		return "html/rrhh/scale/process :: formAlert";
	}
	
	@RequestMapping(value = "/justificate", method = RequestMethod.POST)
	public String updateExtrahour(@ModelAttribute("formShift") RhShift rhShift,
			Model model, HttpSession session, HttpServletRequest request){
		model.addAttribute("rhShift", scheduleScaleService.saveJustification(rhShift));
		return "html/rrhh/scale/detail :: detailDl";
	}
	
	@RequestMapping(value = "/getShiftJustificate/{id}", method = RequestMethod.GET)
	public String getShiftJustification(@PathVariable("id") int idRhShift, 
			Model model, HttpSession session, HttpServletRequest request){
		
		model.addAttribute("formShiftAlert",  scheduleScaleService.get(idRhShift));
		return "html/rrhh/scale/process :: formProcess";
	}
	
	@RequestMapping(value = "/proceessAlert", method = RequestMethod.POST)
	public String proceessAlert(@ModelAttribute("formShiftAlert") RhShift rhShift,
			Model model, HttpSession session, HttpServletRequest request){
		model.addAttribute("rhShift",  scheduleScaleService.processAlert(rhShift));
		return "html/rrhh/scale/process :: formProcess";
	}
	
	@RequestMapping(value = "/regScheduleScale", method = RequestMethod.GET)
	public String regScheduleScale(@RequestParam int idPerson, @RequestParam int idScale,  
			@RequestParam String startDate, Model model,HttpSession session, HttpServletRequest request) throws Exception{
		scheduleScaleService.createScheduleScale(idPerson, idScale, startDate);
		model.addAttribute("message", "Created ok");
		
		return "html/rrhh/scale/detail :: messageScale";
	}
	
	@RequestMapping(value = "/deleteScheduleScale", method = RequestMethod.GET)
	public String deleteScheduleScale(@RequestParam int id, Model model,HttpSession session, HttpServletRequest request) throws Exception{
		
		scheduleScaleService.deleteScheduleScale(id);
		model.addAttribute("message", "The scale has been deleted successfully.");
		return "html/rrhh/scale/detail :: messageScale";
	}
	
	@RequestMapping(value = "/events", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> events(@RequestParam String start, @RequestParam String end,  
			HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		return  scheduleScaleService.listScheduleScale(user.getRhPerson(), start,end);
	} 
}