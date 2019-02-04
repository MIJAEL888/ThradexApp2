package org.thradex.rrhh.dao;

import java.util.List;

import org.thradex.model.RhPerson;
import org.thradex.model.RhSchedulePerson;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;

public interface SchedulePersonDAO {
	
	RhSchedulePerson get(RhPerson rhPerson);
	
	List<RhSchedulePerson> list(RhStatus rhStatus, RhType rhTypeSch);
	
	RhSchedulePerson get(RhPerson rhPerson, RhStatus rhStatus);
	
	RhSchedulePerson get(RhPerson rhPerson, RhStatus rhStatus, RhType rhTypeSch);
	
	void update(RhSchedulePerson rhSchedulePerson);
	
	RhSchedulePerson save(RhSchedulePerson rhSchedulePerson);
	
}
