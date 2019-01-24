package org.thradex.rrhh.dao;

import org.thradex.model.RhStatus;

public interface StatusDAO {
	
	public RhStatus getRhStatus(String category, int code);
	
	public RhStatus getRhStatus(int id);
	
	public RhStatus getRhStatus(String category);
}
