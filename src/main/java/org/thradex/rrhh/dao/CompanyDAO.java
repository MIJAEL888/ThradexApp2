package org.thradex.rrhh.dao;

import java.util.List;

import org.thradex.model.RhCompany;
import org.thradex.model.RhCompanyType;
import org.thradex.model.RhType;

public interface CompanyDAO {
	
	public RhCompany get(int id);
	
	public List<RhCompanyType> listType(RhType rhType);
	
	public List<RhCompany> list(RhType rhType);
	
	public List<RhCompany> lsCompany();
}
