package org.thradex.rrhh.service;

import java.util.List;

import org.thradex.model.RhArea;
import org.thradex.model.RhPerson;
import org.thradex.model.RhStaff;

public interface StaffService {
	
	public List<RhStaff> list();
	
	public RhStaff get();
	
	public RhStaff getMng(RhStaff rhStaff);
	
	public List<RhStaff> list(RhStaff rhStaff);
	
	public List<RhStaff> list(RhPerson rhPerson);
	
	public RhStaff get(RhPerson rhPerson);
	
	public List<RhStaff> list(RhArea rhArea);
}
