package org.thradex.rrhh.service;

import java.util.Date;
import java.util.List;

import org.thradex.model.RhCompany;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.RhStatus;

public interface ShiftPeriodService {
	
	public RhShiftPeriod get(Date date, RhCompany rhCompany);
	
	public RhShiftPeriod getLimit(Date date, RhCompany rhCompany);
	
	public void updatePeriod();
	
	public List<RhShiftPeriod> list(RhCompany rhCompany, RhStatus rhStatus);
	
	public RhShiftPeriod get(Integer id);
}
