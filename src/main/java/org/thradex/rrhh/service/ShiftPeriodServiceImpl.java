package org.thradex.rrhh.service;

import org.springframework.transaction.annotation.Transactional;
import org.thradex.model.RhCompany;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.RhStatus;
import org.thradex.rrhh.dao.CompanyDAO;
import org.thradex.rrhh.dao.ShiftPerioDAO;
import org.thradex.rrhh.dao.StatusDAO;
import org.thradex.rrhh.dao.TypeDAO;
import org.thradex.util.ConstantsSis;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ShiftPeriodServiceImpl implements ShiftPeriodService {
	
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);
	
	@Autowired
	private ShiftPerioDAO shiftPerioDAO;
	
	@Autowired
	private UtilRrhhService utilRrhhService;
	
//	@Autowired
//	private ScheduleScaleService scheduleScaleService;
//	
	@Autowired
	private StatusDAO statusDAO;
	
	@Autowired
	private TypeDAO typeDAO;
	
	@Autowired
	private CompanyDAO companyDAO;
	
	
	public RhShiftPeriod get(Integer id){
		return shiftPerioDAO.get(id);
	}
	
	
	public RhShiftPeriod get(Date date, RhCompany rhCompany){
		return shiftPerioDAO.get(date, rhCompany);
	}
	
	
	public RhShiftPeriod getLimit(Date date, RhCompany rhCompany){
		return shiftPerioDAO.getLimit(date, rhCompany);
	}
	
	
	
	public List<RhShiftPeriod> list(RhCompany rhCompany, RhStatus rhStatus){
		return shiftPerioDAO.list(rhCompany, rhStatus);
	}
	
	
	public void updatePeriod(){
		Date date = utilRrhhService.getNowDate();
		DateTime dateNow = new DateTime(date);
		RhStatus rhStatusCurrent = statusDAO.getRhStatus("SH_PERIOD",1); //current
//		RhStatus rhStatusRegisteret = statusDAO.getRhStatus("SH_PERIOD",2); //registered
		RhStatus rhStatusFinished = statusDAO.getRhStatus("SH_PERIOD",3); //current
		RhStatus rhStatusLast = statusDAO.getRhStatus("SH_PERIOD",4); //current
		
		List<RhCompany> companies = companyDAO.list(typeDAO.getRhType("RH_COMP", 1));
		
		for (RhCompany rhCompany : companies) {
			RhShiftPeriod rhShiftPeriodActive = get(date, rhCompany);
			RhShiftPeriod rhShiftPeriodCurrent = shiftPerioDAO.get(rhCompany, rhStatusCurrent);
			RhShiftPeriod rhShiftPeriodLast = shiftPerioDAO.get(rhCompany, rhStatusLast);
			if(rhShiftPeriodActive.getId() != rhShiftPeriodCurrent.getId()){ // if is different update status RhPeriod
				rhShiftPeriodActive.setRhStatus(rhStatusCurrent);
				shiftPerioDAO.update(rhShiftPeriodActive);
				
				rhShiftPeriodCurrent.setRhStatus(rhStatusLast);
				shiftPerioDAO.update(rhShiftPeriodCurrent);
				
				rhShiftPeriodLast.setRhStatus(rhStatusFinished);
				shiftPerioDAO.update(rhShiftPeriodLast);
			}
			DateTime lastDayLastPeriod = new DateTime(rhShiftPeriodLast.getLastDay());
			if(dateNow.isAfter(lastDayLastPeriod) && !rhShiftPeriodLast.isProcessed()){
//				scheduleScaleService.processAlertsLastPeriod(rhShiftPeriodLast);
//				rhShiftPeriodLast.setProcessed(true);;
//				shiftPerioDAO.update(rhShiftPeriodLast);
			}
		}
	}
	
}