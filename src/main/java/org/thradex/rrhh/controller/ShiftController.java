package org.thradex.rrhh.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.thradex.model.RhFile;
import org.thradex.model.RhShift;
import org.thradex.model.SisUser;
import org.thradex.rrhh.service.AbroadJobService;
import org.thradex.rrhh.service.CommissionService;
import org.thradex.rrhh.service.ExtraHourService;
import org.thradex.rrhh.service.LicenseService;
import org.thradex.rrhh.service.PermissionService;
import org.thradex.rrhh.service.PersonService;
import org.thradex.rrhh.service.ScheduleJobService;
import org.thradex.rrhh.service.ScheduleScaleService;
import org.thradex.rrhh.service.ShiftService;
import org.thradex.rrhh.service.UtilRrhhService;
import org.thradex.sis.service.CurrentUserService;
import org.thradex.util.ConstantsSis;
import org.thradex.util.PropertiesSis;

@Controller
@RequestMapping("/shift")
@SessionAttributes({"formExtraHour", "formAbroadJob", "formSchedJob", "formPermission", "formLicense", "formCommission",
		"formProcess", "formProcessPermission", "formProcessExtraHour", "formProcessLicense"})
public class ShiftController {

	protected static Logger log = Logger.getLogger(ConstantsSis.LOG_CONTROLLER);
	
	@Autowired
	private CurrentUserService currentUserService;
	
	@Autowired
	private ExtraHourService extraHourService;
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private LicenseService licenseService;
	
	@Autowired
	private ScheduleJobService scheduleJobService;
	
	@Autowired
	private AbroadJobService abroadJobService;
	
	@Autowired
	private CommissionService commissionService;

	@Autowired
	private PersonService personService;
	
	@Autowired
	private UtilRrhhService utilService;
	
	@Autowired
	private ShiftService shiftService;
	
	@Autowired
	private ScheduleScaleService scheduleScaleService;
	
	@ModelAttribute("formExtraHour")
	private RhShift createFormExtraHour(){
		return new RhShift();
	}
	
	@RequestMapping(value = "/getPage", method = RequestMethod.GET)
	public String getPage(@RequestParam(required=false) Integer option, Model model, HttpSession session, HttpServletRequest request){
		
		SisUser user = currentUserService.validUser(session);
		Date nowDate = utilService.getNowDate(user.getRhPerson());
		model.addAttribute("dateNow", nowDate);
		
		model.addAttribute("formExtraHour", new RhShift());
		model.addAttribute("formAbroadJob", new RhShift());
		model.addAttribute("formScheduleJob", new RhShift());
		model.addAttribute("formPermission", new RhShift());
		model.addAttribute("formLicense", new RhShift());
		model.addAttribute("formCommission", new RhShift());
		
		model.addAttribute("listPermission", permissionService.listPermission());
		model.addAttribute("listLicense", licenseService.listLicense());
		model.addAttribute("listRhPersons", personService.list(user.getRhPerson()));
		model.addAttribute("listCountry", abroadJobService.listCountry());
		
		model.addAttribute("listShifts", shiftService.listShiftPending(user.getRhPerson()));
//		model.addAttribute("listShiftsMng", shiftService.listShiftActive(2, user.getRhPerson()));
		
		model.addAttribute("listShiftsHistory", shiftService.listShiftProcessed(user.getRhPerson()));
		
//		model.addAttribute("option", option);
		// COPIED FROM SHIFTSCALE
//		model.addAttribute("dateNow", nowDate);
		model.addAttribute("shiftScale", scheduleScaleService.validRegisterScale(user.getRhPerson(), request.getRemoteAddr()));
		model.addAttribute("shiftScheJob", scheduleJobService.validRegister(user.getRhPerson(), request.getRemoteAddr()));
		model.addAttribute("activeSession", shiftService.listShiftActive(user.getRhPerson()));
		model.addAttribute("plannedSession", shiftService.listShiftPlanned(user.getRhPerson()));
		model.addAttribute("mapDetail", shiftService.getMapDetail(user.getRhPerson()));
		
		return "html/rrhh/shift/shiftMngt";
	}
	

	@RequestMapping(value = "/getSession", method = RequestMethod.GET)
	public String getSession(@RequestParam(required=false) Integer option, Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		Date nowDate = utilService.getNowDate(user.getRhPerson());
		model.addAttribute("dateNow", nowDate);
		// COPIED FROM SHIFTSCALE
//		model.addAttribute("dateNow", nowDate);
		model.addAttribute("shiftScale", scheduleScaleService.validRegisterScale(user.getRhPerson(), request.getRemoteAddr()));
		model.addAttribute("shiftScheJob", scheduleJobService.validRegister(user.getRhPerson(), request.getRemoteAddr()));
		model.addAttribute("activeSession", scheduleScaleService.listSession(user.getRhPerson()));
//		model.addAttribute("listShifts", scheduleScaleService.listSession(user.getRhPerson()));
		return "html/rrhh/shift/process :: processShifts(script='true')";
	}
	
	@RequestMapping(value = "/getTable", method = RequestMethod.GET)
	public String getTable(Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		Date nowDate = utilService.getNowDate(user.getRhPerson());
		model.addAttribute("dateNow", nowDate);
		model.addAttribute("listShifts", shiftService.listShiftPending(user.getRhPerson()));
//		model.addAttribute("listShiftsMng", shiftService.listShiftActive(2, user.getRhPerson()));
		return "html/rrhh/shift/tables :: mainTable";
	}
	
	@RequestMapping(value = "/getTableHistory", method = RequestMethod.GET)
	public String getTableHistory(Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		Date nowDate = utilService.getNowDate(user.getRhPerson());
		model.addAttribute("dateNow", nowDate);
		model.addAttribute("listShiftsHistory", shiftService.listShiftProcessed(user.getRhPerson()));
		return "html/rrhh/shift/tables :: historyTable";
	}
	
//	@RequestMapping(value = "/newExtraHour", method = RequestMethod.POST)
//	public @ResponseBody JsonResponse create(@ModelAttribute("formExtraHour") RhShift rhShift,
//							Model model, HttpSession session, HttpServletRequest request){
//		SisUser user = currentUserService.validUser(session);
//		JsonResponse jsonResponse = new JsonResponse();
//		try {
//			extraHourService.save(rhShift, user.getRhPerson());
//			jsonResponse.setStatus("SUCCESS");
//		} catch (Exception e) {
//			jsonResponse.setStatus("ERROR");
//			jsonResponse.setMessage(e.getMessage());
//		}
//		return jsonResponse;
//	}
	
	@RequestMapping(value = "/newExtraHour", method = RequestMethod.POST)
	public String newExtraHour(@ModelAttribute("formExtraHour") RhShift rhShift,
							Model model, HttpSession session, HttpServletRequest request, BindingResult bindingResult){
		log.info(bindingResult.getFieldErrors().toString());
		SisUser user = currentUserService.validUser(session);
		model.addAttribute("formExtraHour", new RhShift());
		try {
			model.addAttribute("rhExtraHour", extraHourService.save(rhShift, user.getRhPerson()));
			model.addAttribute("error", false);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", true);
			model.addAttribute("errorMessage", e.getMessage());
		}
		return "html/rrhh/extraHour/forms :: newForm";
	}
	
	@RequestMapping(value = "/newPermission", method = RequestMethod.POST)
	public String newPermission(@ModelAttribute("formPermission") RhShift rhShift,
							Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		model.addAttribute("listPermission", permissionService.listPermission());
		model.addAttribute("formPermission", new RhShift());
		try {
			model.addAttribute("rhPermission", permissionService.save(rhShift, user.getRhPerson()));
			model.addAttribute("error", false);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", true);
			model.addAttribute("errorMessage", e.getMessage());
		}
		return "html/rrhh/permission/forms :: newForm";
	}
	
	@RequestMapping(value = "/newLicense", method = RequestMethod.POST)
	public String newLicense(@ModelAttribute("formLicense") RhShift rhShift,
							Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		model.addAttribute("listLicense", licenseService.listLicense());
		model.addAttribute("formLicense", new RhShift());
		try {
			model.addAttribute("rhLicense", licenseService.save(rhShift, user.getRhPerson()));
			model.addAttribute("error", false);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", true);
			model.addAttribute("errorMessage", e.getMessage());
		}
		return "html/rrhh/license/forms :: newForm";
	}
	
	@RequestMapping(value = "/newScheduleJob", method = RequestMethod.POST)
	public String newScheduleJob(@ModelAttribute("formScheduleJob") RhShift rhShift,
							Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		model.addAttribute("formScheduleJob", new RhShift());
		model.addAttribute("listRhPersons", personService.list(user.getRhPerson()));
		try {
			model.addAttribute("rhScheduleJob", scheduleJobService.save(user.getRhPerson(), rhShift));
			model.addAttribute("error", false);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", true);
			model.addAttribute("errorMessage", e.getMessage());
		}
		return "html/rrhh/schedJob/forms :: newForm";
	}
	
	@RequestMapping(value = "/newAbroadJob", method = RequestMethod.POST)
	public String newAbroadJob(@ModelAttribute("formAbroadJob") RhShift rhShift,
							Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		
		model.addAttribute("listRhPersons", personService.list(user.getRhPerson()));
		model.addAttribute("listCountry", abroadJobService.listCountry());
		model.addAttribute("formAbroadJob", new RhShift());
		
		try {
			model.addAttribute("rhAbroadJob", abroadJobService.save(rhShift, user.getRhPerson()));
			model.addAttribute("error", false);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", true);
			model.addAttribute("errorMessage", e.getMessage());
		}
		return "html/rrhh/abroadJob/forms :: newForm";
	}
	
	@RequestMapping(value = "/newCommission", method = RequestMethod.POST)
	public String newCommission(@ModelAttribute("formCommission") RhShift rhShift,
							Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		
		model.addAttribute("listRhPersons", personService.list(user.getRhPerson()));
		model.addAttribute("formCommission", new RhShift());
		
		try {
			model.addAttribute("rhCommission", commissionService.save(rhShift, user.getRhPerson()));
			model.addAttribute("error", false);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", true);
			model.addAttribute("errorMessage", e.getMessage());
		}
		return "html/rrhh/commission/forms :: newForm";
	}
	
	
	@Secured({ "ROLE_SH_ADMIN", "ROLE_SH_MNGR"})
	@RequestMapping(value = "/updateExtrahour", method = RequestMethod.POST)
	public String updateExtrahour(@ModelAttribute("formProcessExtraHour") RhShift rhShift,
			Model model, HttpSession session, HttpServletRequest request){
		model.addAttribute("rhExtraHour", extraHourService.update(rhShift));
		return "html/rrhh/extraHour/detail :: detailDl";
	}
	
	@Secured({ "ROLE_SH_ADMIN", "ROLE_SH_MNGR"})
	@RequestMapping(value = "/updatePermission", method = RequestMethod.POST)
	public String updatePermission(@ModelAttribute("formProcessPermission") RhShift rhShift,
			Model model, HttpSession session, HttpServletRequest request){
		model.addAttribute("rhPermission", permissionService.update(rhShift));
		return "html/rrhh/permission/detail :: detailDl";
	}
	
	@Secured({ "ROLE_SH_ADMIN", "ROLE_SH_MNGR"})
	@RequestMapping(value = "/updateLicense", method = RequestMethod.POST)
	public String updateLicense(@ModelAttribute("formProcessLicense") RhShift rhShift,
			Model model, HttpSession session, HttpServletRequest request){
		model.addAttribute("rhLicense", licenseService.update(rhShift));
		return "html/rrhh/license/detail :: detailDl";
	}
	
	
	@RequestMapping(value = "/list/{type}", method = RequestMethod.GET)
	public String list(@PathVariable("type") int type, 
			Model model, HttpSession session, HttpServletRequest request){
//		SisUser user = currentUserService.validUser(session);
//		model.addAttribute("listShifts", 
//				shiftService.listShiftActive(type, user.getRhPerson()));
		return "html/rrhh/shift/tables :: mainTable";
	}
	 
	@Secured({ "ROLE_SH_ADMIN", "ROLE_SH_MNGR"})
	@RequestMapping(value = "/listProcess", method = RequestMethod.GET)
	public String listProcess(	Model model, HttpSession session, HttpServletRequest request){
//		SisUser user = currentUserService.validUser(session);
	
//		model.addAttribute("listShifts", 
//				shiftService.listShiftActive(2, user.getRhPerson()));
		return "html/rrhh/shift/tables :: processTable";
	}
	
//	@RequestMapping(value = "/search", method = RequestMethod.GET)
//	public String search(@PathVariable("type") int type, 
//			Model model, HttpSession session, HttpServletRequest request){
//		SisUser user = currentUserService.validUser(session);
//		model.addAttribute("listShifts", 
//				extraHourService.list(type, user.getRhPerson()));
//		return "html/rrhh/shift/tables :: processTable";
//	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String get(@PathVariable("id") int id, 
			Model model, HttpSession session, HttpServletRequest request){
		RhShift shift = shiftService.getRhShift(id);
		model.addAttribute("rhShift", shift);
//		return "html/rrhh/shift/detail :: detailTable";
		switch (shift.getRhType().getCode()) {
		case 1:
			model.addAttribute("rhShift", shift);
			return "html/rrhh/scale/detail :: detailTable";
		case 2:
			model.addAttribute("rhScheduleJob", shift);
			return "html/rrhh/schedJob/detail :: detailTable";//SHCEDULE JOB
		case 3:
			model.addAttribute("rhPermission", shift);
			return "html/rrhh/permission/detail :: detailTable";//PERMISSION
		case 4:
			model.addAttribute("rhExtraHour", shift);
			return "html/rrhh/extraHour/detail :: detailTable";//EXTRA HOUR
		case 5:
			model.addAttribute("rhLicense", shift);
			return "html/rrhh/license/detail :: detailTable";//LICENSE
		case 6:
			model.addAttribute("rhAbroadJob", shift);
			return "html/rrhh/abroadJob/detail :: detailTable";//abroadJob 
		case 7:
			model.addAttribute("rhShift", shift);
			return "html/rrhh/scale/detail :: detailTableAlert";
		case 8:
			model.addAttribute("rhShift", shift);
			return "html/rrhh/scale/detail :: detailTable";
		case 9:
			model.addAttribute("rhShift", shift);
			return "html/rrhh/scale/detail :: detailTable";
		case 10:
			model.addAttribute("rhShift", shift);
			return "html/rrhh/scale/detail :: detailTableAlert";
		case 11:
			model.addAttribute("rhShift", shift);
			return "html/rrhh/scale/detail :: detailTableAlert";
		case 12:
			model.addAttribute("rhShift", shift);
			return "html/rrhh/scale/detail :: detailTableAlert";
		case 13:
			model.addAttribute("rhShift", shift);
			return "html/rrhh/scale/detail :: detailTableAlert";
		case 14:
			model.addAttribute("rhShift", shift);
			return "html/rrhh/scale/detail :: detailTableAlert";
		case 15:
			model.addAttribute("rhCommission", shift);
			return "html/rrhh/commission/detail :: detailTable";
		default:
			model.addAttribute("rhShift", shift);
			return "html/rrhh/shift/detail :: detailTable";
		}
	}
	
	@Secured({ "ROLE_SH_ADMIN", "ROLE_SH_MNGR"})
	@RequestMapping(value = "/process/{id}", method = RequestMethod.GET)
	public String process(@PathVariable("id") int id, 
			Model model, HttpSession session, HttpServletRequest request){
		RhShift shift = shiftService.getRhShift(id);
//		model.addAttribute("formProcess", extraHourService.get(id));
		switch (shift.getRhType().getCode()) {
		case 1:
//			model.addAttribute("rhScale", shift);
			return "html/rrhh/scale/forms :: formProcess";
		case 2:
//			model.addAttribute("rhScheduleJob", shift);
			return "html/rrhh/schedJob/forms :: formProcess";//SHCEDULE JOB
		case 3:
			model.addAttribute("formProcessPermission", shift);
			return "html/rrhh/permission/forms :: formProcess";//PERMISSION
		case 4:
			model.addAttribute("formProcessExtraHour", shift);
			return "html/rrhh/extraHour/forms :: formProcess";//EXTRA HOUR
		case 5:
			model.addAttribute("formProcessLicense", shift);
			return "html/rrhh/license/forms :: formProcess";//LICENSE
		case 6:
//			model.addAttribute("rhAbroadJob", shift);
			return "html/rrhh/abroadJob/forms :: formProcess";//abroadJob
		default:
//			model.addAttribute("rhShift", shift);
			return "html/rrhh/shift/forms :: formProcess";
		}
//		return "html/rrhh/extraHour/process :: form";
	}
	
	@RequestMapping(value = "/detail/{code}", method = RequestMethod.GET)
	public String detail(@PathVariable("code") int code, 
			Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		model.addAttribute("listShiftsDetail", shiftService.listShitDetail(code, user.getRhPerson()));
		return "html/rrhh/shift/detail :: detailInfo";
	}
	
	@RequestMapping(value = "/regions", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> regions(@RequestParam int idCountry){
		return  abroadJobService.listRegion(idCountry);
	} 
	
	@RequestMapping(value = "/messageLicense", method = RequestMethod.GET)
	public String messageLicense(@RequestParam("idType") int idType, 
			Model model, HttpSession session, HttpServletRequest request){
		model.addAttribute("rhType", licenseService.getTypeLicense(idType));
		return "html/rrhh/license/process :: messageType";
	}
	
	@RequestMapping(value = "/messagePermission", method = RequestMethod.GET)
	public String messagePermission(@RequestParam("idType") int idType, 
			Model model, HttpSession session, HttpServletRequest request){
		model.addAttribute("rhType", permissionService.getTypePermission(idType));
		return "html/rrhh/permission/process :: messageType";
	}
	
	@RequestMapping(value="/download/{id}", method=RequestMethod.GET)
	@ResponseBody
	public void getFileTeoNE(@PathVariable("id") int id, Model model, HttpSession httpSession,
							HttpServletResponse response) throws IOException{
		RhFile rhFile =  shiftService.getRhFile(id);	
		InputStream  inputStream = new FileInputStream(PropertiesSis.PATH_SHIFT + rhFile.getNameFile());
		byte[] array = IOUtils.toByteArray(inputStream);
		response.setContentType( "application/octet-stream" );
		response.setContentLength( array.length);
		response.setHeader( "Content-Disposition", "attachment; filename=\"" + rhFile.getNameFile() + "\"" );
		write(response, array);
	}
	
	
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
	
//	@RequestMapping(value = "/schedule/{id}", method = RequestMethod.GET)
//	public String getSchedJob(@PathVariable("id") int id, 
//			Model model, HttpSession session, HttpServletRequest request){
//		model.addAttribute("rhScheduleJob", shiftService.getRhShift(id));
//		return "html/rrhh/schedJob/detail :: detailTable";
//	}
//	@RequestMapping(value = "/permission/{id}", method = RequestMethod.GET)
//	public String getPermission(@PathVariable("id") int id, 
//			Model model, HttpSession session, HttpServletRequest request){
//		model.addAttribute("rhPermission", shiftService.getRhShift(id));
//		return "html/rrhh/permission/detail :: detailTable";
//	}
//	@RequestMapping(value = "/extraHour/{id}", method = RequestMethod.GET)
//	public String getExtraHour(@PathVariable("id") int id, 
//			Model model, HttpSession session, HttpServletRequest request){
//		model.addAttribute("rhExtraHour", shiftService.getRhShift(id));
//		return "html/rrhh/extraHour/detail :: detailTable";
//	}
//	@RequestMapping(value = "/license/{id}", method = RequestMethod.GET)
//	public String getLicense(@PathVariable("id") int id, 
//			Model model, HttpSession session, HttpServletRequest request){
//		model.addAttribute("rhLicense", shiftService.getRhShift(id));
//		return "html/rrhh/license/detail :: detailTable";
//	}
//	@RequestMapping(value = "/abroadJob/{id}", method = RequestMethod.GET)
//	public String getAbroadJob(@PathVariable("id") int id, 
//			Model model, HttpSession session, HttpServletRequest request){
//		model.addAttribute("rhAbroadJob", shiftService.getRhShift(id));
//		return "html/rrhh/abroadJob/detail :: detailTable";
//	}
}