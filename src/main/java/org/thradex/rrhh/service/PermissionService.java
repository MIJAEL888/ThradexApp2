package org.thradex.rrhh.service;

import java.util.List;

import org.thradex.model.RhPerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhType;

public interface PermissionService {
	
	public RhType getTypePermission(int idType);
	
	public List<RhType> listPermission();
	
	public RhShift save(RhShift rhShift, RhPerson rhPerson);
	
	public RhShift update(RhShift rhShift);
	
	public void checkFinish();
}
