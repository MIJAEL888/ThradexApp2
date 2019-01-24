package org.thradex.rrhh.dao;

import java.util.List;
import java.util.Map;

import org.thradex.model.RhPerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhShiftDetail;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;

public interface ShiftDetailDAO {
	
	public RhShiftDetail get(int id);
	
	public RhShiftDetail save(RhShiftDetail rhShiftDetail);
	
	public void update(RhShiftDetail rhShiftDetail);
	
	public List<RhShiftDetail> list(RhShift rhShift);
	
	public List<RhShiftDetail> list(RhType typeDetail, RhPerson rhPerson, RhShiftPeriod rhShiftPeriod);
	
	public Integer sumTotal(RhType typeDetail, RhPerson rhPerson, RhShiftPeriod rhShiftPeriod);
	
	public List<Map<String, Object>> sumTotal(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod);
	
	public Map<String, Object> sumTotal(RhShift rhShift, RhType typeDetail);
	
	public Integer getTotal(RhShift rhShift, RhType rhTypeRate);
	
	public Integer getSumTotal(RhShift rhShift);
	
	public List<RhShiftDetail> list(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod);
	
	public List<RhShiftDetail> list(RhShiftPeriod rhShiftPeriod);
	
	public List<RhShiftDetail> listMng(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod);
	
	public Map<String, Object> sumTotal(RhPerson rhPerson, RhType typeDetail, RhShiftPeriod rhShiftPeriod);
	
	public Map<String, Object> sumTotalChild(RhShift rhShift, RhType rhTypeDetail);
	
	public Map<String, Object> sumTotalChild(RhShift rhShift, RhType rhTypeDetail, RhType rhTypeRate);
	
	public List<RhShiftDetail> list(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod, RhStatus rhStatusSis);
	
	public List<RhShiftDetail> listDayNull();

	public void delete(RhShift rhShift);
	
}
