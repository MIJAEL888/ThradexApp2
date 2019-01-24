package org.thradex.rrhh.service;

import org.springframework.transaction.annotation.Transactional;
import org.thradex.model.RhStaff;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;
import org.thradex.rrhh.dao.StaffDAO;
import org.thradex.rrhh.dao.StatusDAO;
import org.thradex.rrhh.dao.TypeDAO;
import org.thradex.model.RhArea;
import org.thradex.model.RhPerson;
import org.thradex.util.ConstantsSis;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);

	
	@Autowired 
	private StaffDAO staffDAO;
	
	@Autowired
	private StatusDAO statusDAO;
	
	@Autowired
	private TypeDAO typeDAO;
	
	@Override
	public List<RhStaff> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RhStaff get() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.thradex.rrhh.service.PersonService#getRhPersonManager(org.thradex.model.RhPerson)
	 * Get the person's manager according his main Staff position 
	 * Return:
	 * Manager RhPerson 
	 */
	@Override
	public RhStaff getMng(RhStaff rhStaff) {
		RhType rhTypeMng = typeDAO.getRhType("AREA_POS", 1);
		RhStatus rhStatus = statusDAO.getRhStatus("STAFF", 1);
		
		if (rhStaff.getRhAreaPosition().getRhType().getId() == rhTypeMng.getId()) { //don use rhPositions 
			RhPerson rhPerson = rhStaff.getRhPerson();
			RhPerson rhPersonMng = rhStaff.getRhPerson();
			RhStaff rhStaffMng = rhStaff;
			while (rhPerson.getId() == rhPersonMng.getId()) {
				List<RhStaff> l = staffDAO.listMng(
						rhStaffMng.getRhAreaPosition().getRhArea().getAreaParent(), rhStatus, rhTypeMng);
				if (!l.isEmpty()) {
					rhStaffMng = l.get(0);
					rhPersonMng = rhStaffMng.getRhPerson();
				}else break;
			}
			return rhStaffMng;
		}else { // others
			List<RhStaff> l = staffDAO.listMng(
					rhStaff.getRhAreaPosition().getRhArea(),  rhStatus, rhTypeMng);
			if (l.isEmpty()) {
				l = staffDAO.listMng(
						rhStaff.getRhAreaPosition().getRhArea().getAreaParent(),  rhStatus, rhTypeMng);
				if (!l.isEmpty()) {
					return l.get(0);
				}else
					return null;
			}else{
				return l.get(0);
			}
		}
	}

	/*(non-Javadoc)
	 * @see org.thradex.rrhh.service.StaffService#list(org.thradex.model.RhStaff)
	 * List Staff Subordinates.
	 */
	@Override
	public List<RhStaff> list(RhStaff rhStaff) {
		List<RhStaff> listRhStaffs = new ArrayList<RhStaff>();
		
		RhType rhTypeMng = typeDAO.getRhType("AREA_POS", 1); // staff 
		RhStatus rhStatus = statusDAO.getRhStatus("STAFF", 1);
		
		if(rhTypeMng.getId() == rhStaff.getRhAreaPosition().getRhType().getId()){
			listRhStaffs = staffDAO.listStaff(rhStaff.getRhAreaPosition().getRhArea(), 
					rhStatus, rhTypeMng);
			listRhStaffs.addAll(staffDAO.listStaffMng(rhStaff.getRhAreaPosition().getRhArea(), rhStatus, rhTypeMng));
			
		}
		
		return listRhStaffs;
	}
	
	/* (non-Javadoc)
	 * @see org.thradex.rrhh.service.StaffService#list(org.thradex.model.RhPerson)
	 * List all the persons staff.
	 */
	@Override
	public List<RhStaff> list(RhPerson rhPerson) {
		RhStatus rhStatus = statusDAO.getRhStatus("STAFF", 1);
		return staffDAO.list(rhPerson, rhStatus);
	}

	@Override
	public RhStaff get(RhPerson rhPerson) {
		RhStatus rhStatus = statusDAO.getRhStatus("STAFF", 1);
		RhType rhType = typeDAO.getRhType("STAFF", 1);
		return staffDAO.get(rhPerson, rhType, rhStatus);
	}

	@Override
	public List<RhStaff> list(RhArea rhArea) {
		RhStatus rhStatus = statusDAO.getRhStatus("STAFF", 1);
		return staffDAO.listStaff(rhArea, rhStatus);
	}
	
}