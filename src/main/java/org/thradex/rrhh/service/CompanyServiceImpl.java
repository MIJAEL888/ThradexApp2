package org.thradex.rrhh.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thradex.model.RhCompany;
import org.thradex.rrhh.dao.CompanyDAO;
import org.thradex.util.ConstantsSis;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService{
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);
	
	@Autowired
	private CompanyDAO companyDAO;
	
	
	public List<RhCompany> lsCompany(){
	 return companyDAO.lsCompany();	
	}
}
