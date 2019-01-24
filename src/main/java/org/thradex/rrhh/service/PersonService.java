package org.thradex.rrhh.service;

import java.util.List;

import org.thradex.model.RhArea;
import org.thradex.model.RhCompany;
import org.thradex.model.RhPerson;
import org.thradex.model.RhStatus;

public interface PersonService {
	
	public RhPerson	get(int idPerson);
	
	public List<RhPerson> list(int idPerson);
	
	public List<RhPerson> list(RhPerson rhPerson);
	
	public List<RhPerson> list(RhCompany rhCompany, RhStatus rhStatus);
	
	public RhPerson	getManager(RhPerson rhPerson);
	
	public boolean isManager(RhPerson rhPerson);
	
	public List<RhPerson> list(RhArea rhArea);
}
