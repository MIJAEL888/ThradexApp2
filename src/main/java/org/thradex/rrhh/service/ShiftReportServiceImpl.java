package org.thradex.rrhh.service;

import org.springframework.transaction.annotation.Transactional;
import org.thradex.model.EvenUsuario;
import org.thradex.model.RhCompany;
import org.thradex.model.RhPerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhShiftDetail;
import org.thradex.model.RhShiftDetail2;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;
import org.thradex.model.ValorCosto;
import org.thradex.rrhh.dao.CompanyDAO;
import org.thradex.rrhh.dao.ShiftDAO;
import org.thradex.rrhh.dao.StatusDAO;
import org.thradex.rrhh.dao.TypeDAO;
import org.thradex.util.ConstantsSis;
import org.thradex.util.PropertiesSis;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jxls.area.XlsArea;
import org.jxls.command.Command;
import org.jxls.command.EachCommand;
import org.jxls.common.AreaRef;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.jxls.util.TransformerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ShiftReportServiceImpl implements ShiftReportService {
	
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);
	
	@Autowired
	private ShiftDAO shiftDAO;
	
	@Autowired
	private CompanyDAO companyDAO;
	
	@Autowired
	private TypeDAO typeDAO;
	
	@Autowired
	private StatusDAO statusDAO;
	
	@Autowired
	private ShiftPeriodService shiftPeriodService;
	
	@Autowired
	private ShiftDetailService shiftDetailService;
	
	@Autowired
	private ShiftDetailService2 shiftDetailService2;
	
	@Autowired
	private PersonService personService;

	@Autowired
	private UtilRrhhService utilRrhhService;
	
	/*
	 * METHODS FOR THE SHIFT REPORT
	 */
	
	public List<RhShift> listAllActive(RhPerson rhPerson){
		List<RhShift> listRhShift =  new ArrayList<RhShift>();
		RhStatus rhStatusSisActive = statusDAO.getRhStatus("SH_SIS_STATUS", 2);
		listRhShift.addAll(shiftDAO.listBySis(rhStatusSisActive, rhPerson.getRhCompany()));
		return listRhShift;
	}
	
	
	public List<EvenUsuario> listEvenUsuario(){
		List<EvenUsuario> lsEvenUsuario= new ArrayList<>();
		lsEvenUsuario=shiftDAO.listEvenUsuario();
		return lsEvenUsuario;
	}
	
	
	public List<ValorCosto> lisValorCosto(){
		List<ValorCosto> lsValorCosto= new ArrayList<>();
		lsValorCosto = shiftDAO.lisValorCosto();
		return lsValorCosto;
	}
	
	
	public List<RhShift> listAllPlanned(RhPerson rhPerson){
		List<RhShift> listRhShift =  new ArrayList<RhShift>();
		RhStatus rhStatusSisActive = statusDAO.getRhStatus("SH_SIS_STATUS", 1);
		RhStatus rhStatus = statusDAO.getRhStatus("SH_SCALE", 3);
		listRhShift.addAll(shiftDAO.listBySis(rhStatusSisActive, rhStatus, rhPerson.getRhCompany()));
		return listRhShift;
	}
	
	
	public List<RhShift> listPending(Integer idPeriod, Integer idPerson, RhPerson rhPersonMng){
		List<RhShift> listRhShift =  new ArrayList<RhShift>();
		RhStatus rhStatusSisPending = statusDAO.getRhStatus("SH_SIS_STATUS", 1);
//		RhStatus rhStatusSisActive = statusDAO.getRhStatus("SH_SIS_STATUS", 2);
		if(	idPeriod != null && idPeriod != 0 &&
			idPerson != null && idPerson != 0){
			RhShiftPeriod rhShiftPeriod = shiftPeriodService.get(idPeriod);
			RhPerson rhPerson =  personService.get(idPerson);
			listRhShift.addAll(shiftDAO.listBySis(rhPerson, rhStatusSisPending, rhShiftPeriod));
//			listRhShift.addAll(shiftDAO.listBySis(rhPerson, rhStatusSisActive, rhShiftPeriod));
		}else if(idPeriod != null && idPeriod != 0){
			RhShiftPeriod rhShiftPeriod = shiftPeriodService.get(idPeriod);
			if(rhPersonMng == null){
				listRhShift.addAll(shiftDAO.listBySis(rhStatusSisPending, rhShiftPeriod));
//				listRhShift.addAll(shiftDAO.listBySis(rhStatusSisActive, rhShiftPeriod));
			}else{
				listRhShift.addAll(shiftDAO.listBySis(rhPersonMng, rhStatusSisPending, rhShiftPeriod));
//				listRhShift.addAll(shiftDAO.listBySis(rhPersonMng, rhStatusSisActive, rhShiftPeriod));
				listRhShift.addAll(shiftDAO.listBySisAll(rhPersonMng, rhStatusSisPending, rhShiftPeriod));
//				listRhShift.addAll(shiftDAO.listBySisAll(rhPersonMng, rhStatusSisActive, rhShiftPeriod));
			}
		}
		listRhShift = addVariables(listRhShift);
		return listRhShift;
	}
	
	
	public List<RhShift> listProcessed(Integer idPeriod, Integer idPerson, RhPerson rhPersonMng){
		List<RhShift> listRhShift = new ArrayList<RhShift>();
		RhStatus rhStatusSisProcessed = statusDAO.getRhStatus("SH_SIS_STATUS", 3);
		if( idPeriod != null && idPeriod != 0 &&
			idPerson != null && idPerson != 0){
			RhShiftPeriod rhShiftPeriod = shiftPeriodService.get(idPeriod);
			RhPerson rhPerson = personService.get(idPerson);
			listRhShift.addAll(shiftDAO.listBySisProcess(rhPerson, rhStatusSisProcessed, rhShiftPeriod));
		}else if(idPeriod != null && idPeriod != 0){
			RhShiftPeriod rhShiftPeriod = shiftPeriodService.get(idPeriod);
			if(rhPersonMng == null){
				listRhShift.addAll(shiftDAO.listBySis(rhStatusSisProcessed, rhShiftPeriod));
			}else{
				listRhShift.addAll(shiftDAO.listBySisProcess(rhPersonMng, rhStatusSisProcessed, rhShiftPeriod));
				listRhShift.addAll(shiftDAO.listMngBySis(rhPersonMng, rhStatusSisProcessed, rhShiftPeriod));
			}
		}
		listRhShift = addVariables(listRhShift);
		return shiftDetailService.addDetail(listRhShift);
	}
	
	
	public List<RhShiftDetail> listShiftDetail(Integer idPeriod, Integer idPerson, RhPerson rhPersonMng){
		List<RhShiftDetail> listRhShiftDetail = new ArrayList<RhShiftDetail>();

		if( idPeriod != null && idPeriod != 0 &&
			idPerson != null && idPerson != 0){
			RhShiftPeriod rhShiftPeriod = shiftPeriodService.get(idPeriod);
			RhPerson rhPerson = personService.get(idPerson);
			listRhShiftDetail.addAll(shiftDetailService.list(rhPerson, rhShiftPeriod));
		}else if(idPeriod != null && idPeriod != 0){
			RhShiftPeriod rhShiftPeriod = shiftPeriodService.get(idPeriod);
			if(rhPersonMng == null){
				listRhShiftDetail.addAll(shiftDetailService.list(rhShiftPeriod));
			}else{
				listRhShiftDetail.addAll(shiftDetailService.list(rhPersonMng, rhShiftPeriod));
				listRhShiftDetail.addAll(shiftDetailService.listMng(rhPersonMng, rhShiftPeriod));
			}
		}
		return listRhShiftDetail;
	}
	
	
	public List<RhPerson> listShiftSummary(Integer idPeriod, RhPerson rhPersonMng){
		List<RhPerson> rhPersons = new ArrayList<RhPerson>();
		if(idPeriod != null && idPeriod != 0){
			RhShiftPeriod rhShiftPeriod = shiftPeriodService.get(idPeriod);
			
			if(rhPersonMng == null){
				rhPersons = listRhPerson(rhShiftPeriod.getRhCompany().getId());
				rhPersons = shiftDetailService.addDetail(rhPersons, rhShiftPeriod);
			}else{
				rhPersons = listRhPerson(rhPersonMng);
				rhPersons = shiftDetailService.addDetail(rhPersons, rhShiftPeriod);
			}
		}
		return rhPersons;
	}

	
	public List<RhCompany> listRhCompanies(){
		RhType rhType = typeDAO.getRhType("RH_COMP", 1);
		return companyDAO.list(rhType);
	}
	
	
	public List<RhShiftPeriod> listRhShiftPeriods(int idRhCompany){
		RhCompany rhCompany = companyDAO.get(idRhCompany);
		RhStatus rhStatus1 = statusDAO.getRhStatus("SH_PERIOD", 1);
		RhStatus rhStatus2 = statusDAO.getRhStatus("SH_PERIOD", 4);
		RhStatus rhStatus3 = statusDAO.getRhStatus("SH_PERIOD", 3);
		List<RhShiftPeriod> rhShiftPeriods = new ArrayList<RhShiftPeriod>();
		rhShiftPeriods.addAll(shiftPeriodService.list(rhCompany, rhStatus1));
		rhShiftPeriods.addAll(shiftPeriodService.list(rhCompany, rhStatus2));
		rhShiftPeriods.addAll(shiftPeriodService.list(rhCompany, rhStatus3));
		return rhShiftPeriods;
	}
	
	
	public List<RhPerson> listRhPerson(RhPerson rhPerson){
		List<RhPerson> rhPersons = personService.list(rhPerson);
		rhPersons.add(rhPerson);
		return rhPersons;
	}
	
	
	public List<RhPerson> listRhPerson(int idRhCompany){
		RhCompany rhCompany = companyDAO.get(idRhCompany);
		RhStatus rhStatus = statusDAO.getRhStatus("PERSON", 1);
		return personService.list(rhCompany, rhStatus);
	}
	
	
	public List<Map<String, Object>> listMapRhShiftPeriods(int idRhCompany){
		List<RhShiftPeriod> rhShiftPeriods = listRhShiftPeriods(idRhCompany);
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for (RhShiftPeriod rhShiftPeriod : rhShiftPeriods) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", rhShiftPeriod.getId());
			map.put("label", rhShiftPeriod.getLabel());
			listMap.add(map);
		}
		return listMap;
	}
	
	
	public List<Map<String, Object>> listMapRhPerson(int idRhCompany){
		List<RhPerson> rhPersons = listRhPerson(idRhCompany);
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for (RhPerson rhPerson : rhPersons) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", rhPerson.getId());
			map.put("label", rhPerson.getFullname());
			listMap.add(map);
		}
		return listMap;
	}
	
	public List<RhShift> addVariables(List<RhShift> rhShifts){
		for (RhShift rhShift : rhShifts) {
			rhShift.setTotalTime(utilRrhhService.getStringHours(shiftDetailService.getTotalTime(rhShift)));
			rhShift.setTotalProcessedTime(utilRrhhService.getStringHours(shiftDetailService.getTotalProcessedTime(rhShift))); 
			rhShift.setActiveTime(utilRrhhService.getStringDay(rhShift.getDateReg(), new Date()));
		}
		return rhShifts;
	} 
	
	/*
	 * Export excel
	 */
	
	public void exportReportShift(int idRhPeriod){
		RhShiftPeriod rhShiftPeriod = shiftPeriodService.get(idRhPeriod);
//		RhPerson rhPerson = personService.get(idRhPerson);
		RhStatus rhStatusSisProcessed = statusDAO.getRhStatus("SH_SIS_STATUS", 3);
		RhStatus rhStatusSisPending = statusDAO.getRhStatus("SH_SIS_STATUS", 1);
		List<RhPerson> rhPersons = listRhPerson(rhShiftPeriod.getRhCompany().getId());
//		rhPersons.get(0);
		for (RhPerson rhPerson2 : rhPersons) {
			List<RhShift> rhShiftsAll = shiftDAO.listBySisProcess(rhPerson2, rhStatusSisProcessed, rhShiftPeriod);
			
			List<RhShift> rhShifts = new ArrayList<RhShift>();
			for (RhShift rhShift : rhShiftsAll) {
				if (rhShift.getRhType().getCode() == 10 || rhShift.getRhType().getCode() == 11 || 
					rhShift.getRhType().getCode() == 12 || rhShift.getRhType().getCode() == 13) {
					// removing alerts
				}else {
					rhShift.setRhShifts(shiftDAO.list(rhShift));
					rhShifts.add(rhShift);
				}
			}
			rhShifts.addAll(shiftDAO.listBySisProcess(rhPerson2, rhStatusSisPending, rhShiftPeriod));
			shiftDetailService2.addDetailChild(rhShifts);
			rhPerson2.setRhShiftPeriod(rhShiftPeriod);
			rhPerson2.setRhShifts(rhShifts);
		}
		
		try (InputStream is = ShiftReportServiceImpl.class.getClassLoader().getResourceAsStream("templateShift.xls")) {
//			log.info(" is available : " + is.available());
            try (OutputStream os = new FileOutputStream(PropertiesSis.PATH_SHIFT_REPORT + "shift.xls")) {
            	 Transformer transformer = TransformerFactory.createTransformer(is, os);
                 XlsArea xlsArea = new XlsArea("Template!A1:H12", transformer);
                 XlsArea personArea = new XlsArea("Template!A1:H10", transformer);
                 EachCommand personEachCommand = new EachCommand("person", "persons", personArea, new SimpleCellRefGenerator());
                 XlsArea shiftArea = new XlsArea("Template!A9:H9", transformer);
                 Command shiftEachCommand = new EachCommand("shift", "person.rhShifts", shiftArea);
                 personArea.addCommand(new AreaRef("Template!A9:H9"), shiftEachCommand);
                 xlsArea.addCommand(new AreaRef("Template!A4:H10"), personEachCommand);
            	 Context context = new Context();
                 context.putVar("persons", rhPersons);
//                 log.info("Repo  " + xlsArea.getStartCellRef().getCellName());
                 xlsArea.applyAt(new CellRef(xlsArea.getStartCellRef().getCellName()), context);
                 log.info("Complete Repo ");
                 transformer.deleteSheet("Template");
                 transformer.write();
            } catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	  
	}
	
	/*
	 * Export excel
	 */
	
	public void exportReportShift2(int idRhPeriod){
		RhShiftPeriod rhShiftPeriod = shiftPeriodService.get(idRhPeriod);
//		RhPerson rhPerson = personService.get(idRhPerson);
		RhStatus rhStatusSisProcessed = statusDAO.getRhStatus("SH_SIS_STATUS", 3);
		RhStatus rhStatusSisPending = statusDAO.getRhStatus("SH_SIS_STATUS", 1);
		List<RhPerson> rhPersons = listRhPerson(rhShiftPeriod.getRhCompany().getId());
//		rhPersons.get(0);
		for (RhPerson rhPerson2 : rhPersons) {
			List<RhShift> rhShiftsAll = shiftDAO.listBySisProcess(rhPerson2, rhStatusSisProcessed, rhShiftPeriod);
			
			List<RhShift> rhShifts = new ArrayList<RhShift>();
			for (RhShift rhShift : rhShiftsAll) {
				if (rhShift.getRhType().getCode() == 10 || rhShift.getRhType().getCode() == 11 || 
					rhShift.getRhType().getCode() == 12 || rhShift.getRhType().getCode() == 13) {
					// removing alerts
				}else {
					rhShift.setRhShifts(shiftDAO.list(rhShift));
					rhShifts.add(rhShift);
				}
			}
			rhShifts.addAll(shiftDAO.listBySisProcess(rhPerson2, rhStatusSisPending, rhShiftPeriod));
			shiftDetailService.addDetailChild(rhShifts);
			rhPerson2.setRhShiftPeriod(rhShiftPeriod);
			rhPerson2.setRhShifts(rhShifts);
		}
		
		try (InputStream is = ShiftReportServiceImpl.class.getClassLoader().getResourceAsStream("templateShift2.xls")) {
//			log.info(" is available : " + is.available());
            try (OutputStream os = new FileOutputStream(PropertiesSis.PATH_SHIFT_REPORT + "shift2.xls")) {
            	 Transformer transformer = TransformerFactory.createTransformer(is, os);
                 XlsArea xlsArea = new XlsArea("Template!A1:L12", transformer);
                 XlsArea personArea = new XlsArea("Template!A1:L10", transformer);
                 EachCommand personEachCommand = new EachCommand("person", "persons", personArea, new SimpleCellRefGenerator());
                 XlsArea shiftArea = new XlsArea("Template!A9:L9", transformer);
                 Command shiftEachCommand = new EachCommand("shift", "person.rhShifts", shiftArea);
                 personArea.addCommand(new AreaRef("Template!A9:L9"), shiftEachCommand);
                 xlsArea.addCommand(new AreaRef("Template!A4:L10"), personEachCommand);
            	 Context context = new Context();
                 context.putVar("persons", rhPersons);
//                 log.info("Repo  " + xlsArea.getStartCellRef().getCellName());
                 xlsArea.applyAt(new CellRef(xlsArea.getStartCellRef().getCellName()), context);
                 log.info("Complete Repo ");
                 transformer.deleteSheet("Template");
                 transformer.write();
            } catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	  
	}
	
	
	public void exportReportShiftDetail(int idRhPeriod){
		RhShiftPeriod rhShiftPeriod = shiftPeriodService.get(idRhPeriod);
		List<RhPerson> rhPersons = listRhPerson(rhShiftPeriod.getRhCompany().getId());
		RhStatus rhStatusSisProcessed = statusDAO.getRhStatus("SH_SIS_STATUS", 3);
		
		for (RhPerson rhPerson2 : rhPersons) {
			List<RhShiftDetail> rhShiftDetails = shiftDetailService.list(rhPerson2, rhShiftPeriod, rhStatusSisProcessed);
			rhPerson2.setRhShiftDetails(rhShiftDetails);
			rhPerson2.setRhShiftPeriod(rhShiftPeriod);
		}
		try (InputStream is = ShiftReportServiceImpl.class.getClassLoader().getResourceAsStream("templateShiftDetail.xls")) {
            try (OutputStream os = new FileOutputStream(PropertiesSis.PATH_SHIFT_REPORT + "shiftDetailed.xls")) {
                 Transformer transformer = TransformerFactory.createTransformer(is, os);
                 XlsArea xlsArea = new XlsArea("Template!A1:K11", transformer);
                 XlsArea personArea = new XlsArea("Template!A1:K10", transformer);
                 EachCommand personEachCommand = new EachCommand("person", "persons", personArea, new SimpleCellRefGenerator());
                 XlsArea shiftArea = new XlsArea("Template!A9:K9", transformer);
                 Command shiftEachCommand = new EachCommand("shiftDetail", "person.rhShiftDetails", shiftArea);
                 personArea.addCommand(new AreaRef("Template!A9:K9"), shiftEachCommand);
                 xlsArea.addCommand(new AreaRef("Template!A9:K10"), personEachCommand);
            	 Context context = new Context();
                 context.putVar("persons", rhPersons);
                 xlsArea.applyAt(new CellRef(xlsArea.getStartCellRef().getCellName()), context);
                 transformer.deleteSheet("Template");
                 transformer.write();
            } catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	  
	}
	
	
	public void exportReportShiftDetailOne(int idRhPeriod){
		
		RhShiftPeriod rhShiftPeriod = shiftPeriodService.get(idRhPeriod);
		List<RhPerson> rhPersons = listRhPerson(rhShiftPeriod.getRhCompany().getId());
		RhStatus rhStatusSisProcessed = statusDAO.getRhStatus("SH_SIS_STATUS", 3);
		List<RhShiftDetail> rhShiftDetails = new ArrayList<RhShiftDetail>();
		for (RhPerson rhPerson2 : rhPersons) {
			rhShiftDetails.addAll(shiftDetailService.list(rhPerson2, rhShiftPeriod, rhStatusSisProcessed));
		}
		
		try (InputStream is = ShiftReportServiceImpl.class.getClassLoader().getResourceAsStream("templateShiftDetailOne.xls")) {
            try (OutputStream os = new FileOutputStream(PropertiesSis.PATH_SHIFT_REPORT + "shiftDetailedOne.xls")) {
            	Context context = new Context();
                context.putVar("shiftDetails", rhShiftDetails);
                context.putVar("shiftPeriod", rhShiftPeriod);
                JxlsHelper.getInstance().processTemplate(is, os, context);
            } catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	  
	}
	
	
	public void exportReportShiftDetailOne2(int idRhPeriod){
		
		RhShiftPeriod rhShiftPeriod = shiftPeriodService.get(idRhPeriod);
		List<RhPerson> rhPersons = listRhPerson(rhShiftPeriod.getRhCompany().getId());
		RhStatus rhStatusSisProcessed = statusDAO.getRhStatus("SH_SIS_STATUS", 3);
		List<RhShiftDetail2> rhShiftDetails = new ArrayList<RhShiftDetail2>();
		for (RhPerson rhPerson2 : rhPersons) {
			rhShiftDetails.addAll(shiftDetailService2.list(rhPerson2, rhShiftPeriod, rhStatusSisProcessed));
		}
		
		try (InputStream is = ShiftReportServiceImpl.class.getClassLoader().getResourceAsStream("templateShiftDetailOne.xls")) {
            try (OutputStream os = new FileOutputStream(PropertiesSis.PATH_SHIFT_REPORT + "shiftDetailedOne.xls")) {
            	Context context = new Context();
                context.putVar("shiftDetails", rhShiftDetails);
                context.putVar("shiftPeriod", rhShiftPeriod);
                JxlsHelper.getInstance().processTemplate(is, os, context);
            } catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	  
	}
}