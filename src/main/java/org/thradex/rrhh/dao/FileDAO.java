package org.thradex.rrhh.dao;

import java.util.List;

import org.thradex.model.RhFile;
import org.thradex.model.RhShift;

public interface FileDAO {
	
	public List<RhFile> getRhFile(RhShift rhShift);
	
	public RhFile save(RhFile rhFile);
	
	public RhFile get(int idRhFile);
	
}
