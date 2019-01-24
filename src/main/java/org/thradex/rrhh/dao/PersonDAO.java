package org.thradex.rrhh.dao;

import java.util.List;

import org.thradex.model.RhCompany;
import org.thradex.model.RhPerson;
import org.thradex.model.RhStatus;

public interface PersonDAO {
	
	public RhPerson getRhPerson(int id);
	
	public List<RhPerson> list(RhCompany rhCompany, RhStatus rhStatus);
	
}
