package org.thradex.rrhh.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thradex.model.RhArea;
import org.thradex.rrhh.dao.AreaDAO;
import org.thradex.util.ConstantsSis;

@Service
@Transactional
public class AreaServiceImpl implements AreaService{
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);
	
	@Autowired
	private AreaDAO areaDAO;
	
	
	public RhArea getAreaById(int idArea){
		return areaDAO.getAreaById(idArea);
	}
	
	public List<RhArea> lsAreaByIdCompany(int idCompany){
	 return areaDAO.lsAreaByIdCompany(idCompany);	
	}
}
