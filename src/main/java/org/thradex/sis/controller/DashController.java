package org.thradex.sis.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thradex.sis.service.CurrentUserService;
import org.thradex.util.ConstantsSis;

@Controller
@RequestMapping("/dash")
public class DashController {

	protected static Logger log = Logger.getLogger(ConstantsSis.LOG_CONTROLLER);
	
	@Autowired
	private CurrentUserService currentUserService;
	
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String navigation(Model model, HttpSession session){
		log.info("dashcontroller");
//		 Collection<? extends GrantedAuthority> authList = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		
		currentUserService.validUser(session);
////		for (GrantedAuthority grantedAuthority : authList) {
//			log.info("permisos: " + grantedAuthority.getAuthority());
//		}
//		
		return "html/dashboard";
	}
	
}