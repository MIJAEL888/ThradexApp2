package org.thradex.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.thradex.exception.CustomGenericException;
import org.thradex.util.ConstantsSis;

@ControllerAdvice
public class GlobalExceptionController {

	static Logger log = Logger.getLogger(ConstantsSis.LOG_CONTROLLER);

	@ExceptionHandler(CustomGenericException.class)
	public ModelAndView handleAllException(HttpServletRequest req, Exception ex) {
		ex.printStackTrace();
		ModelAndView model = new ModelAndView("html/error/genericError");
		model.addObject("errCode", ex.getClass());
		model.addObject("errMsg", ex.getMessage());
		return model;
	}
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handleAllException(Exception ex) {
		ex.printStackTrace();
		ModelAndView model = new ModelAndView("html/error/genericError");
		model.addObject("errCode", ex.getClass());
		model.addObject("errMsg", ex.getMessage());
		return model;
	}

//	@ExceptionHandler(ServiceException.class)
//	public ModelAndView handleServicesException(HttpServletRequest req, Exception ex) {
//		ex.printStackTrace();
//		ModelAndView model = new ModelAndView("error/genericError");
//		model.addObject("errCode", ex.getClass());
//		model.addObject("errMsg", ex.getMessage());
// 
//		return model;
// 
//	}
//	  @ExceptionHandler(UserException.class)
//	    public String handleBusinessExceptionException(
//	    		UserException ex) {
//	        log.error("UserException" + ex.getMessage());
//
////	        ModelAndView model = new ModelAndView("/");
////	        model.addObject("errCode", ex.getClass());
////			model.addObject("errMsg", ex.getMessage());
//	        return "redirect:/";
//	    }
//	  
//  @ExceptionHandler(JsonException.class)
//	public  @ResponseBody BadStatus handleJsonlException(HttpServletRequest req, Exception ex) {
//		ex.printStackTrace();
//		return new BadStatus(ex.getMessage());
//	}
}
