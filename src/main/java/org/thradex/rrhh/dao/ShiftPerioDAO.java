package org.thradex.rrhh.dao;

import java.util.Date;
import java.util.List;

import org.thradex.model.RhCompany;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;

public interface ShiftPerioDAO {
	
	RhShiftPeriod get(Date date, RhCompany rhCompany, RhStatus rhStatus, RhType rhType);
	
	RhShiftPeriod getLimit(Date date, RhCompany rhCompany);
	
	RhShiftPeriod get(Date date, RhCompany rhCompany);
	
	RhShiftPeriod get(int id);
	
	List<RhShiftPeriod> list(RhCompany rhCompany, RhStatus rhStatus);

	List<RhShiftPeriod> list(RhCompany rhCompany);
	
	RhShiftPeriod get(RhCompany rhCompany, RhStatus rhStatus);
	
	void update(RhShiftPeriod rhShiftPeriod);
}
