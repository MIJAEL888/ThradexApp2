package org.thradex.rrhh.dao;

import java.util.List;

import org.thradex.model.RhType;

public interface TypeDAO {
	
	public RhType getRhType(String category, int code);
	
	public RhType getRhType(int id);
	
	public List<RhType> getRhType(String category);
	
	public List<RhType> get(int code);
}
