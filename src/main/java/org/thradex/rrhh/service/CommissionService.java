package org.thradex.rrhh.service;

import org.thradex.model.RhPerson;
import org.thradex.model.RhShift;

public interface CommissionService {
	
	public RhShift get(int id);
	
	public RhShift save(RhShift rhShift, RhPerson rhPerson);
	
	public RhShift update(RhShift rhShift);
	
	public void checkFinish();
	
}
