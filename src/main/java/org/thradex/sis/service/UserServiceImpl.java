package org.thradex.sis.service;

import org.springframework.transaction.annotation.Transactional;
import org.thradex.model.RhPerson;
import org.thradex.model.RhStaff;
import org.thradex.model.SisRol;
import org.thradex.model.SisUser;
import org.thradex.rrhh.service.StaffService;
import org.thradex.exception.CustomGenericException;
import org.thradex.sis.dao.UserDAO;
import org.thradex.util.ConstantsSis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private StaffService staffService;

	@Override
	public SisUser getUser(String username) throws CustomGenericException, UsernameNotFoundException {
		SisUser user = userDAO.getUser(username);
		if(user == null){
			throw new UsernameNotFoundException("Error in retrieving user"); 
		}else{
			 List<RhStaff> listStaff = staffService.list(user.getRhPerson());
			 user.getRhPerson().setListStaff(listStaff);
			 
			 Set<SisRol> listRol = new HashSet<SisRol>();

			 for (int i = 0; i < listStaff.size(); i++) {
				RhStaff staff = listStaff.get(i);
				List<SisRol> rols = staff.getRhAreaPosition().getListRol();
				Iterator<SisRol> iterator = rols.iterator();
				while (iterator.hasNext()) {
					SisRol sisRol = iterator.next();
					listRol.add(sisRol);
				}
				
			}
			 
			user.setListRol(listRol);
//			if(listRol.isEmpty()) throw new CustomGenericException("E003", "There is not any rol related to the User.");
		 
		}
		return user;
	}

	@Override
	public SisUser getUser(int idUser) throws CustomGenericException, UsernameNotFoundException {
		SisUser user = userDAO.getUser(idUser);
		if(user == null){
			throw new UsernameNotFoundException("Error in retrieving user"); 
		}else{
			 List<RhStaff> listStaff = staffService.list(user.getRhPerson());
			 user.getRhPerson().setListStaff(listStaff);
	//		 if(listStaff.isEmpty()) throw new CustomGenericException("E002", "There is not any staff related to the User."); 
			 Set<SisRol> listRol = new HashSet<SisRol>();
			 for (int i = 0; i < listStaff.size(); i++) {
			RhStaff staff = listStaff.get(i);
			List<SisRol> rols = staff.getRhAreaPosition().getListRol();
			Iterator<SisRol> iterator = rols.iterator();
			while (iterator.hasNext()) {
				SisRol sisRol = iterator.next();
				listRol.add(sisRol);
			}
		 }
		 
		 user.setListRol(listRol);
//		 if(listRol.isEmpty()) throw new CustomGenericException("E003", "There is not any rol related to the User.");
		 
		}
		return user;
	}
	
	@Override
	public SisUser getUser(RhPerson person){
		SisUser sisUser=userDAO.getUser(person);
		return sisUser;
	}
}