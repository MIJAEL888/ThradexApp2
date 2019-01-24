package org.thradex.rrhh.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.thradex.model.RhShift;
import org.thradex.model.SisUser;
import org.thradex.rrhh.service.PersonService;
import org.thradex.rrhh.service.ScheduleJobService;
import org.thradex.rrhh.service.UtilRrhhService;
import org.thradex.sis.service.CurrentUserService;
import org.thradex.util.ConstantsSis;

@Controller
@RequestMapping("/scheduleJob")
@SessionAttributes({"formScheduleJob"})
public class ScheduleJobController {

	protected static Logger log = Logger.getLogger(ConstantsSis.LOG_CONTROLLER);
	
	@Autowired
	private CurrentUserService currentUserService;
	
	@Autowired
	private ScheduleJobService scheduleJobService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private UtilRrhhService utilService;	
	
	@RequestMapping(value = "/getPage", method = RequestMethod.GET)
	public String getPage(Model model, HttpSession session, HttpServletRequest request){
		log.info("/shift/getPermission + " + request.getRemoteAddr());
		
		SisUser user = currentUserService.validUser(session);
		Date nowDate = utilService.getNowDate(user.getRhPerson());
		model.addAttribute("dateNow", nowDate);
		model.addAttribute("formScheduleJob", new RhShift());
		model.addAttribute("listRhPersons", personService.list(user.getRhPerson()));
		model.addAttribute("shiftScheJob", scheduleJobService.validRegister(user.getRhPerson(), request.getRemoteAddr()));
//		model.addAttribute("listScheduleJob", scheduleJobService.list(1, user.getRhPerson()));
		return "html/rrhh/schedJob/schedJobMngt";
	}
	
	@RequestMapping(value = "/registerStart/{id}", method = RequestMethod.GET)
	public String registerShiftScheJob(@PathVariable("id") int idRhShift, Model model, HttpSession session, HttpServletRequest request){
		log.info("/shift/registerStartSchedJob + " + request.getRemoteAddr());
		
		SisUser user = currentUserService.validUser(session);
		model.addAttribute("rhScheduleJob",
				scheduleJobService.registerStart(user.getRhPerson(), idRhShift, request.getRemoteAddr()));
		
		return "html/rrhh/schedJob/detail :: detailDl";
	}
	
	@RequestMapping(value = "/registerFinish/{id}", method = RequestMethod.GET)
	public String registerFinish(@PathVariable("id") int idRhShift, Model model, HttpSession session, HttpServletRequest request){
		log.info("/shift/registerStartSchedJob + " + request.getRemoteAddr());
		model.addAttribute("rhScheduleJob", 
				scheduleJobService.registerFinish(idRhShift, request.getRemoteAddr()));
		
		return "html/rrhh/schedJob/detail :: detailDl";
	}
	
	@Secured({ "ROLE_SH_ADMIN", "ROLE_SH_MNGR"})
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(@ModelAttribute("formScheduleJob") RhShift rhShift,
							Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		model.addAttribute("rhScheduleJob", scheduleJobService.save(user.getRhPerson(), rhShift));
		return "html/rrhh/schedJob/detail :: detailDl";
	}
	
	@RequestMapping(value = "/list/{type}", method = RequestMethod.GET)
	public String list(@PathVariable("type") int type, 
			Model model, HttpSession session, HttpServletRequest request){
//		SisUser user = currentUserService.validUser(session);
//		model.addAttribute("listScheduleJob", scheduleJobService.list(type, user.getRhPerson()));
		return "html/rrhh/extraHour/tables :: mainTable";
	}
}