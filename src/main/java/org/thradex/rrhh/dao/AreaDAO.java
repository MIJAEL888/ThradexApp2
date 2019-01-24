package org.thradex.rrhh.dao;

import java.util.List;

import org.thradex.model.RhArea;

public interface AreaDAO {
	
	public RhArea getAreaById(int idArea);
	public List<RhArea> lsAreaByIdCompany(int idCompany);
}
