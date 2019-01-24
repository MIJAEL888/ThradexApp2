package org.thradex.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.thradex.util.ConstantsSis;

@Controller
@RequestMapping("/auth")
public class LoginLogoutController {

	static Logger log = Logger.getLogger(ConstantsSis.LOG_CONTROLLER);
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLoginPage(	@RequestParam(value = "error", required = false, defaultValue = "false") boolean error,
			ModelMap model, HttpSession session) {
		log.debug("Received request to show login page");
		// Add an error message to the model if login is unsuccessful
		// The 'error' parameter is set to true based on the when the
		// authentication has failed.
		// We declared this under the authentication-failure-url attribute
		// inside the spring-security.xml
		/*
		 * See below: <form-login login-page="/krams/auth/login"
		 * authentication-failure-url="/krams/auth/login?error=true"
		 * default-target-url="/krams/main/common"/>
		 */
		model.put("error", false);
		if (error == true) {
			// Assign an error message
			model.put("error", true);
		}

		// This will resolve to /WEB-INF/jsp/loginpage.jsp
		return "html/login";
	}
		  
	 /**
	 * Handles and retrieves the denied JSP page. This is shown whenever a
	 * regular user tries to access an admin only page.
	 * 
	 * @return the name of the JSP page
	 */
	@RequestMapping(value = "/denied", method = RequestMethod.GET)
	public String getDeniedPage() {
		log.debug("Received request to show denied page");
		// This will resolve to /WEB-INF/jsp/deniedpage.jsp
		return "html/deniedpage";
	}
	
	@RequestMapping(value = "/expiredSession", method = RequestMethod.GET)
	public String expiredSession() {
		log.debug("Received request to show denied page");
		// This will resolve to /WEB-INF/jsp/deniedpage.jsp
		return "redirect:/auth/login";
	}
}
