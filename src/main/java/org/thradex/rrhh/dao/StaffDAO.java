package org.thradex.rrhh.dao;

import java.util.List;

import org.thradex.model.RhArea;
import org.thradex.model.RhPerson;
import org.thradex.model.RhStaff;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;

public interface StaffDAO {
	
	public RhStaff getRhStaff(int id);
	
	public List<RhStaff> list(RhPerson rhPerson, RhStatus rhStatus);
	
	public List<RhStaff> listStaff(RhArea rhArea, RhStatus rhStatus, RhType rhTypeAP);
	
	public List<RhStaff> listMng(RhArea rhArea, RhStatus rhStatus, RhType rhTypeAP);
	
	public List<RhStaff> listStaffMng(RhArea rhArea, RhStatus rhStatus, RhType rhTypeAP);
	
	public List<RhStaff> listStaff(RhArea rhArea, RhStatus rhStatus);
	
	public RhStaff get(RhPerson rhPerson, RhType rhType, RhStatus rhStatus);
}
