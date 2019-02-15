package org.thradex.rrhh.dao;

import java.util.List;

import org.thradex.model.RhCompany;
import org.thradex.model.RhCompanyType;
import org.thradex.model.RhType;

public interface CompanyDAO {
	
	RhCompany get(int id);
	
	List<RhCompanyType> listType(RhType rhType);
	
	List<RhCompany> list(RhType rhType);
	
	List<RhCompany> lsCompany();
}
