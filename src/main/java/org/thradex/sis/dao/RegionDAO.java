package org.thradex.sis.dao;

import java.util.List;
import java.util.Map;

import org.thradex.model.SisCountry;
import org.thradex.model.SisRegion;
import org.thradex.model.SisStatus;

public interface RegionDAO {
	
	public SisRegion get(int id);
	
	public List<SisRegion> list(SisStatus sisStatus);
	
	public List<Map<String, Object>> listMap(SisStatus sisStatus);
	
	public List<Map<String, Object>> listMap(SisStatus sisStatus, SisCountry sisCountry);
}
