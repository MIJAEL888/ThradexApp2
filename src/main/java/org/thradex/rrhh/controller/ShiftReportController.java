package org.thradex.rrhh.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thradex.model.RhFile;
import org.thradex.model.RhShiftReport;
import org.thradex.model.SisUser;
import org.thradex.rrhh.service.ShiftReportService;
import org.thradex.rrhh.service.ShiftService;
import org.thradex.sis.service.CurrentUserService;
import org.thradex.util.ConstantsSis;
import org.thradex.util.PropertiesSis;

@Controller
@RequestMapping("/shiftReport")
public class ShiftReportController {

	protected static Logger log = Logger.getLogger(ConstantsSis.LOG_CONTROLLER);
	
	@Autowired
	private CurrentUserService currentUserService;
	
//	@Autowired
//	private ShiftPeriodService shiftPeriodService;
	
	@Autowired
	private ShiftReportService shiftReportService;
	
	@Autowired
	private ShiftService shiftService;
	
	@RequestMapping(value = "/getPage", method = RequestMethod.GET)
	public String getPage(Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		model.addAttribute("formShiftReport", new RhShiftReport());
		model.addAttribute("formShiftReportRh", new RhShiftReport());
		model.addAttribute("listCompany", shiftReportService.listRhCompanies());
		model.addAttribute("listPeriod", shiftReportService.listRhShiftPeriods(user.getRhPerson().getRhCompany().getId()));
		model.addAttribute("listPerson", shiftReportService.listRhPerson(user.getRhPerson()));
		model.addAttribute("listActiveShifts", shiftReportService.listAllActive(user.getRhPerson()));
		model.addAttribute("listPlannedShifts", shiftReportService.listAllPlanned(user.getRhPerson()));
		model.addAttribute("activeSession", shiftService.listShiftActive(user.getRhPerson()));
		model.addAttribute("plannedSession", shiftService.listShiftPlanned(user.getRhPerson()));
		
		return "html/rrhh/shift/shiftReport";
	}
	
	@RequestMapping(value = "/updatePendingMng", method = RequestMethod.GET)
	public String updatePendingMng(@RequestParam(required=false) Integer idPeriod,
			@RequestParam(required=false) Integer idPerson,	Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		model.addAttribute("listShifts", shiftReportService.listPending(idPeriod, idPerson, user.getRhPerson()));
		return "html/rrhh/shift/tables :: reportPending";
	}
	
	@RequestMapping(value = "/updateProcessedMng", method = RequestMethod.GET)
	public String updateProcessedMng(@RequestParam(required=false) Integer idPeriod,
			@RequestParam(required=false) Integer idPerson,	Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		model.addAttribute("listShifts", shiftReportService.listProcessed(idPeriod, idPerson, user.getRhPerson()));
		return "html/rrhh/shift/tables :: reportProcessed";
	}
	
	@RequestMapping(value = "/updateSummaryMng", method = RequestMethod.GET)
	public String updateSummaryMng(@RequestParam(required=false) Integer idPeriod,
			@RequestParam(required=false) Integer idPerson,	Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		model.addAttribute("listSumPerson", shiftReportService.listShiftSummary(idPeriod, user.getRhPerson()));
		return "html/rrhh/shift/tables :: reportSummary";
	}
	
	@RequestMapping(value = "/updatePending", method = RequestMethod.GET)
	public String updatePending(@RequestParam(required=false) Integer idPeriod,
			@RequestParam(required=false) Integer idPerson,	Model model, HttpSession session, HttpServletRequest request){
		model.addAttribute("listShifts", shiftReportService.listPending(idPeriod, idPerson, null));
		return "html/rrhh/shift/tables :: reportPending";
	}
	
	@RequestMapping(value = "/updateProcessed", method = RequestMethod.GET)
	public String updateProcessed(@RequestParam(required=false) Integer idPeriod,
			@RequestParam(required=false) Integer idPerson,	Model model, HttpSession session, HttpServletRequest request){
		model.addAttribute("listShifts", shiftReportService.listProcessed(idPeriod, idPerson, null));
		return "html/rrhh/shift/tables :: reportProcessed";
	}
	
	@RequestMapping(value = "/updateSummary", method = RequestMethod.GET)
	public String updateSummary(@RequestParam(required=false) Integer idPeriod,
				Model model, HttpSession session, HttpServletRequest request){
		model.addAttribute("listSumPerson", shiftReportService.listShiftSummary(idPeriod, null));
		return "html/rrhh/shift/tables :: reportSummary";
	}
	
	@RequestMapping(value = "/updateDetailMng", method = RequestMethod.GET)
	public String updateDetailMng(@RequestParam(required=false) Integer idPeriod,
			@RequestParam(required=false) Integer idPerson,	Model model, HttpSession session, HttpServletRequest request){
		SisUser user = currentUserService.validUser(session);
		model.addAttribute("listShiftsDetail", shiftReportService.listShiftDetail(idPeriod, idPerson, user.getRhPerson()));
		return "html/rrhh/shift/tables :: reportDetailed";
	}
	
	@RequestMapping(value = "/updateDetail", method = RequestMethod.GET)
	public String updateDetail(@RequestParam(required=false) Integer idPeriod,
			@RequestParam(required=false) Integer idPerson,	Model model, HttpSession session, HttpServletRequest request){
		model.addAttribute("listShiftsDetail", shiftReportService.listShiftDetail(idPeriod, idPerson, null));
		return "html/rrhh/shift/tables :: reportDetailed";
	}
	
	@RequestMapping(value = "/exportReport", method = RequestMethod.GET)
	public void exportReport(@RequestParam(required=false) Integer idPeriod,
			Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception{
		shiftReportService.exportReportShift(idPeriod);
		InputStream  inputStream = new FileInputStream(PropertiesSis.PATH_SHIFT_REPORT + "shift.xls");
		byte[] array = IOUtils.toByteArray(inputStream);
		response.setContentType( "application/octet-stream" );
		response.setContentLength( array.length);
		response.setHeader( "Content-Disposition", "attachment; filename=\"ReportShift.xls\"" );
		write(response, array);
	}
	
	@RequestMapping(value = "/exportReportPE", method = RequestMethod.GET)
	public void exportReport2(@RequestParam(required=false) Integer idPeriod,
			Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception{
		shiftReportService.exportReportShift2(idPeriod);
		InputStream  inputStream = new FileInputStream(PropertiesSis.PATH_SHIFT_REPORT + "shift2.xls");
		byte[] array = IOUtils.toByteArray(inputStream);
		response.setContentType( "application/octet-stream" );
		response.setContentLength( array.length);
		response.setHeader( "Content-Disposition", "attachment; filename=\"ReportShiftPe.xls\"" );
		write(response, array);
	}
	
	@RequestMapping(value = "/exportReportDetail", method = RequestMethod.GET)
	public void exportReportDetail(@RequestParam(required=false) Integer idPeriod,
			Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception{
		shiftReportService.exportReportShiftDetail(idPeriod);
		InputStream  inputStream = new FileInputStream( PropertiesSis.PATH_SHIFT_REPORT + "shiftDetailed.xls");
		byte[] array = IOUtils.toByteArray(inputStream);
		response.setContentType( "application/octet-stream" );
		response.setContentLength( array.length);
		response.setHeader( "Content-Disposition", "attachment; filename=\"ReportDetailShift.xls\"" );
		write(response, array);
	} 
	
	@RequestMapping(value = "/exportReportDetailOne", method = RequestMethod.GET)
	public void exportReportDetailOne(@RequestParam(required=false) Integer idPeriod,
			Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception{
		shiftReportService.exportReportShiftDetailOne2(idPeriod);
		InputStream  inputStream = new FileInputStream( PropertiesSis.PATH_SHIFT_REPORT + "shiftDetailedOne.xls");
		byte[] array = IOUtils.toByteArray(inputStream);
		response.setContentType( "application/octet-stream" );
		response.setContentLength( array.length);
		response.setHeader( "Content-Disposition", "attachment; filename=\"ReportDetailShiftOne.xls\"" );
		write(response, array);
	} 
	
	@RequestMapping(value="/download/{id}", method=RequestMethod.GET)
	@ResponseBody
	public void getFileTeoNE(@PathVariable("id") int id, Model model, HttpSession httpSession,
							HttpServletResponse response) throws IOException{
		RhFile rhFile =  shiftService.getRhFile(id);	
		InputStream  inputStream = new FileInputStream(PropertiesSis.PATH_SHIFT_REPORT + rhFile.getNameFile());
		byte[] array = IOUtils.toByteArray(inputStream);
		response.setContentType( "application/octet-stream" );
		response.setContentLength( array.length);
		response.setHeader( "Content-Disposition", "attachment; filename=\"" + rhFile.getNameFile() + "\"" );
		write(response, array);
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