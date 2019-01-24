package org.thradex.rrhh.service;

import org.thradex.model.RhPerson;
import org.thradex.model.RhSchedulePerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;

import java.util.Date;
import java.util.List;

public interface LicenseService {
	
	public List<RhType> listLicense();
	
	public RhType getTypeLicense(int idType);
	
	public RhShift get(int id);
	
	public RhShift save(RhShift rhShift, RhPerson rhPerson);
	
	public RhShift update(RhShift rhShift);
	
	public RhShift get(Date date, RhStatus rhStatus);
	
	public void process(RhShift rhLicense, Date start, Date finish, RhSchedulePerson rhSchedulePerson);
	
	public void checkFinish();
}
