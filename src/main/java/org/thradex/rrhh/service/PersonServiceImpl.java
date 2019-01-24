package org.thradex.rrhh.service;

import org.springframework.transaction.annotation.Transactional;
import org.thradex.model.RhStaff;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;
import org.thradex.rrhh.dao.PersonDAO;
import org.thradex.rrhh.dao.TypeDAO;
import org.thradex.model.RhArea;
import org.thradex.model.RhCompany;
import org.thradex.model.RhPerson;
import org.thradex.util.ConstantsSis;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);

	@Autowired
	private PersonDAO personDAO;
	
	@Autowired 
	private StaffService staffService; 
	
//	@Autowired
//	private StatusDAO statusDAO;
	
	@Autowired
	private TypeDAO typeDAO;
	
	
	public RhPerson get(int id) {
		return personDAO.getRhPerson(id);
	}
	
	
	public List<RhPerson> list(int idPerson) {
		personDAO.getRhPerson(idPerson);
		return null;
	}

	
	public boolean isManager(RhPerson rhPerson){
		RhType rhType = typeDAO.getRhType("STAFF", 1); //main role
		RhType rhTypeMng = typeDAO.getRhType("AREA_POS", 1); // MANAGER
		if (rhPerson.getListStaff().isEmpty()) {
			rhPerson.setListStaff(staffService.list(rhPerson));
		}
		if (rhPerson.getListStaff().isEmpty()) {
			log.info("not found any staff for " + rhPerson.getFullname());
			return false;
		}else{
			for (RhStaff rhStaff : rhPerson.getListStaff()) {
				if (rhStaff.getRhType() != null && rhStaff.getRhAreaPosition().getRhType() != null && 
					rhStaff.getRhType().getId() == rhType.getId() && 
					rhStaff.getRhAreaPosition().getRhType().getId() == rhTypeMng.getId()) {
					return true;
				}else {
					log.error("No defined type role for : " + rhPerson.getFullname());
					return false;
				}
			}
			return false;
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.thradex.rrhh.service.PersonService#getRhPersonManager(org.thradex.model.RhPerson)
	 * Get the person's manager according his main Staff position 
	 * Return:
	 * Manager RhPerson 
	 */
	
	public RhPerson getManager(RhPerson rhPerson) {
		RhPerson rhPersonMng =  new RhPerson();
		RhType rhType = typeDAO.getRhType("STAFF", 1); //main role
		
		
		if(rhPerson.getIdManagerShift()!=null){// implemet logic idManagerShift
			rhPersonMng = get(rhPerson.getIdManagerShift()); 
		}else{
			if (rhPerson.getListStaff().isEmpty()) {
				rhPerson.setListStaff(staffService.list(rhPerson));
			}
			if (rhPerson.getListStaff().isEmpty()) {
				log.info("not found any staff for " + rhPerson.getFullname());
			}else{
				for (RhStaff rhStaff : rhPerson.getListStaff()) {
					if (rhStaff.getRhType() != null && 
						rhStaff.getRhType().getId() == rhType.getId()) {
						rhPersonMng =  staffService.getMng(rhStaff).getRhPerson();
					}else {
						log.error("No defined type role for : " + rhPerson.getFullname());
					}
				}
			}
			if(rhPersonMng != null && (rhPersonMng.getId() == 6 || rhPersonMng.getId() == 16)){
				rhPersonMng =  personDAO.getRhPerson(117);
			}
//			log.error("getRhPersonManager: The person " + rhPerson.getName() + " has not a manager..");
		}
		
		
		return rhPersonMng;
	}

	/* (non-Javadoc)
	 * @see org.thradex.rrhh.service.PersonService#list(org.thradex.model.RhPerson)
	 * List all the persons's subordinates 
	 */
	
	public List<RhPerson> list(RhPerson rhPerson) {
		RhType rhTypeMng = typeDAO.getRhType("AREA_POS", 1);
		List<RhStaff> listRhStaffsSub = new ArrayList<RhStaff>();
		List<RhPerson> listRhPersons = new ArrayList<RhPerson>(); 
		List<RhStaff> listRhStaffs = new ArrayList<RhStaff>();
		if (rhPerson.getListStaff().isEmpty()) {
			rhPerson.setListStaff(staffService.list(rhPerson));
		}
		listRhStaffs = rhPerson.getListStaff();
		for (RhStaff rhStaff : listRhStaffs){
			if (rhStaff.getRhAreaPosition().getRhType().getId() == rhTypeMng.getId()) {
				listRhStaffsSub.addAll(staffService.list(rhStaff)) ;
			}
		}
		for (RhStaff rhStaff1 : listRhStaffsSub) {
			listRhPersons.add(rhStaff1.getRhPerson());
		}
		return listRhPersons;
	}
	
	
	public List<RhPerson> list(RhCompany rhCompany, RhStatus rhStatus){
		return personDAO.list(rhCompany, rhStatus);
	}
	
	public List<RhPerson> list(RhArea rhArea) {
//		RhType rhTypeStaff = typeDAO.getRhType("AREA_POS", 2);
		List<RhPerson> listRhPersons = new ArrayList<RhPerson>(); 
		List<RhStaff> listRhStaffs = staffService.list(rhArea);
		for (RhStaff rhStaff1 : listRhStaffs) {
			listRhPersons.add(rhStaff1.getRhPerson());
		}
		return listRhPersons;
	}
	
}