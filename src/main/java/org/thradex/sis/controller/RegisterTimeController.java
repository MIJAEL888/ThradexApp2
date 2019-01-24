package org.thradex.sis.controller;

import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thradex.model.RhShift;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.SisUser;
import org.thradex.rrhh.service.ScheduleScaleService;
import org.thradex.rrhh.service.ShiftPeriodService;
import org.thradex.rrhh.service.ShiftService;
import org.thradex.rrhh.service.UtilRrhhService;
import org.thradex.sis.service.CurrentUserService;

@Controller
@RequestMapping(value="/register")
public class RegisterTimeController {
	
	@Autowired
	private CurrentUserService currentUserService;
	
	@Autowired
	private ShiftService shiftService;
	
	@Autowired
	private ScheduleScaleService scheduleScaleService;
	
	@Autowired
	private ShiftPeriodService shiftPeriodService;
	
	@Autowired
	private UtilRrhhService utilService;
	
	@RequestMapping(value="/getPage")
	private String getPage(Model model,HttpSession session){
		SisUser user = currentUserService.validUser(session);
		model.addAttribute("activeSession", shiftService.listShiftActive(user.getRhPerson()));
		model.addAttribute("plannedSession", shiftService.listShiftPlanned(user.getRhPerson()));
		
		Date nowDate = utilService.getNowDate(user.getRhPerson());
		RhShiftPeriod periodo= shiftPeriodService.get(nowDate, user.getRhPerson().getRhCompany());
		
		model.addAttribute("closedSession", shiftService.listShiftClosed(user.getRhPerson() ,periodo));
		
		
		
//		System.out.println("== VAL ==== "+shiftService.listShiftPlanned(user.getRhPerson()).size());
		return "html/sis/regTimeMng";
	}
	
	@RequestMapping(value="/registerFinish",method=RequestMethod.POST)
	private String saveRegisterFinishSacale(@RequestParam(value="idRhshift") int idRhshift,
		@RequestParam(value="dStart",required=false) String dStart,
		@RequestParam(value="dEnd",required=false) String dEnd,
		HttpSession session) throws ParseException{
		SisUser user = currentUserService.validUser(session);
		if(dStart!=null && dStart!=""){ scheduleScaleService.registerStartScaleRUM(user.getRhPerson(), idRhshift, dStart); }
		if(dEnd!=null && dEnd!=""){ scheduleScaleService.registerFinishScaleRUM(idRhshift, dEnd); }
		
		return "redirect:/register/getPage";
	}
	
	@RequestMapping(value="/toPendingShift", method=RequestMethod.GET)
	@ResponseBody
	private int toPendingShift(@RequestParam(value="idShift",required=true) int idShift){
		shiftService.shiftChildDelete(idShift);		
		shiftService.updateShiftRever(idShift);
		return 1;
	}
	
//	AJAX
	@RequestMapping(value="/ajaxShiftRegTime" , method=RequestMethod.GET)
	private String getShitRegtimeajax(Model model,
	   @RequestParam(value="idShit") int _idShit){
		RhShift rhshift= shiftService.getRhShift(_idShit);
		model.addAttribute("rhshift", rhshift);
//		return rhshit;
		return "html/sis/regTimeMng :: rhshiftRUM";
	}

}
