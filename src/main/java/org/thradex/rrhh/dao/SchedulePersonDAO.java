package org.thradex.rrhh.dao;

import java.util.List;

import org.thradex.model.RhPerson;
import org.thradex.model.RhSchedulePerson;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;

public interface SchedulePersonDAO {
	
	public RhSchedulePerson get(RhPerson rhPerson);
	
	public List<RhSchedulePerson> list(RhStatus rhStatus, RhType rhTypeSch);
	
	public RhSchedulePerson get(RhPerson rhPerson, RhStatus rhStatus);
	
	public RhSchedulePerson get(RhPerson rhPerson, RhStatus rhStatus, RhType rhTypeSch);
	
	public void update(RhSchedulePerson rhSchedulePerson);
	
	public RhSchedulePerson save(RhSchedulePerson rhSchedulePerson);
	
}
