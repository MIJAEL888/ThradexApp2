package org.thradex.rrhh.service;

import java.util.Date;
import java.util.List;

import org.thradex.model.RhCompany;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.RhStatus;

public interface ShiftPeriodService {
	
	RhShiftPeriod get(Date date, RhCompany rhCompany);
	
	RhShiftPeriod getLimit(Date date, RhCompany rhCompany);
	
	void updatePeriod();
	
	List<RhShiftPeriod> list(RhCompany rhCompany, RhStatus rhStatus);

	List<RhShiftPeriod> list(RhCompany rhCompany);
	
	RhShiftPeriod get(Integer id);
}
