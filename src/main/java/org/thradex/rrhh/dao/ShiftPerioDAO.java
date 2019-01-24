package org.thradex.rrhh.dao;

import java.util.Date;
import java.util.List;

import org.thradex.model.RhCompany;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;

public interface ShiftPerioDAO {
	
	public RhShiftPeriod get(Date date, RhCompany rhCompany, RhStatus rhStatus, RhType rhType);
	
	public RhShiftPeriod getLimit(Date date, RhCompany rhCompany);
	
	public RhShiftPeriod get(Date date, RhCompany rhCompany);
	
	public RhShiftPeriod get(int id);
	
	public List<RhShiftPeriod> list(RhCompany rhCompany, RhStatus rhStatus);
	
	public RhShiftPeriod get(RhCompany rhCompany, RhStatus rhStatus);
	
	public void update(RhShiftPeriod rhShiftPeriod);
}
