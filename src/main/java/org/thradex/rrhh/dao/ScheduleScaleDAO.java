package org.thradex.rrhh.dao;

import java.util.Date;
import java.util.List;

import org.thradex.model.RhPerson;
import org.thradex.model.RhSchedulePerson;
import org.thradex.model.RhScheduleScale;
import org.thradex.model.RhStatus;

public interface ScheduleScaleDAO {
	
	public RhScheduleScale get(int id);
	
	public RhScheduleScale get(RhPerson rhPerson, RhStatus rhStatus, Date date);
	
	public void update(RhScheduleScale rhScheduleScale);
	
	public RhScheduleScale save(RhScheduleScale rhScheduleScale);
	
	public RhScheduleScale get(RhPerson rhPerson, Date date);
	
	public void delete(RhScheduleScale rhScheduleScale);
	
	public List<RhScheduleScale> list(RhStatus rhStatus, Date date);
	
	public List<RhScheduleScale> list(Date start, Date end);
	
	public List<RhScheduleScale> list(RhSchedulePerson rhSchedulePerson, Date start, Date end);
	
	public List<RhScheduleScale> list2(RhSchedulePerson rhSchedulePerson, Date start, Date end);
}
