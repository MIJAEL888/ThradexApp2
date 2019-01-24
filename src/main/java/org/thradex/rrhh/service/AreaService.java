package org.thradex.rrhh.service;

import java.util.List;

import org.thradex.model.RhArea;

public interface AreaService {
	
	public RhArea getAreaById(int idArea);
	public List<RhArea> lsAreaByIdCompany(int idCompany);
	

}
