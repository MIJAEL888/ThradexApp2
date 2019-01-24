package org.thradex.rrhh.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thradex.model.SisUser;
import org.thradex.rrhh.service.UtilRrhhService;
import org.thradex.sis.service.CurrentUserService;
import org.thradex.util.ConstantsSis;

@Controller
@RequestMapping("/permission")
public class PermissionController {

	protected static Logger log = Logger.getLogger(ConstantsSis.LOG_CONTROLLER);
	
	@Autowired
	private CurrentUserService currentUserService;
	
	@Autowired
	private UtilRrhhService utilService;
	
	@RequestMapping(value = "/getPage", method = RequestMethod.GET)
	public String getPage(Model model, HttpSession session, HttpServletRequest request){
		log.info("/shift/getPermission + " + request.getRemoteAddr());
		SisUser user = currentUserService.validUser(session);
		Date nowDate = utilService.getNowDate(user.getRhPerson());
		model.addAttribute("dateNow", nowDate);
		return "html/rrhh/shift/shiftMngt";
	}
	
	@RequestMapping(value = "/registerStart/{id}", method = RequestMethod.GET)
	public String registerShiftScheJob(@PathVariable("id") int idRhScheduleJob, Model model, HttpSession session, HttpServletRequest request){
		log.info("/shift/registerStartSchedJob + " + request.getRemoteAddr());
		SisUser user = currentUserService.validUser(session);
		Date nowDate = utilService.getNowDate(user.getRhPerson());
		model.addAttribute("dateNow", nowDate);
		return "redirect: /shift/getPage";
	}
	
}