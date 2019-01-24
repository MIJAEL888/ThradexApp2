package org.thradex.sis.dao;


import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.thradex.model.RhPerson;
import org.thradex.model.RhStaff;
import org.thradex.model.SisRol;
import org.thradex.model.SisUser;
import org.thradex.rrhh.service.StaffService;
import org.thradex.util.ConstantsSis;

@Repository
public class UserDAOImpl implements UserDAO {
	
	Logger log = Logger.getLogger(ConstantsSis.LOG_DAO);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private StaffService staffService;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public SisUser getUser(String username) {
		 return (SisUser) openSession().createQuery("from SisUser u where u.username = :username and u.estado = 1")
				 .setParameter("username", username).uniqueResult();
	}

	@Override
	public SisUser getUser(int idUser) {
		 return (SisUser) openSession().createQuery("from SisUser u where u.id = :idUser and u.estado = 1")
				 .setParameter("idUser", idUser).uniqueResult();
	}
	
	 @Override
	public SisUser searchDatabase(int idUser) {
		 SisUser user = (SisUser) openSession().createQuery("from SisUser u where u.id = :idUser and u.estado = 1")
		 .setParameter("idUser", idUser).uniqueResult();

		 List<RhStaff> listStaff1 = staffService.list(user.getRhPerson());
		 user.getRhPerson().setListStaff(listStaff1);

//		 List<RhStaff> listStaff = user.getRhPerson().getRhStaffs();
		 List<RhStaff> listStaff = user.getRhPerson().getListStaff();
		 Set<SisRol> listRol = new HashSet<SisRol>();
		 for (int i = 0; i < listStaff.size(); i++) {
			RhStaff staff = listStaff.get(i);
			//log.info(staff.getRhPerson().getName());
			List<SisRol> rols = staff.getRhAreaPosition().getListRol();
			Iterator<SisRol> iterator = rols.iterator();
			while (iterator.hasNext()) {
				SisRol sisRol = iterator.next();
				listRol.add(sisRol);
			}
		}
		 user.setListRol(listRol);
		 return user;
	 }
	 
	 @Override
	public SisUser getUser(RhPerson person){
		 SisUser user = (SisUser) openSession().createQuery("from SisUser u where u.rhPerson.id = :idPerson and u.estado = 1")
		 .setParameter("idPerson", person.getId()).uniqueResult();
		 return user;
	 }

}
