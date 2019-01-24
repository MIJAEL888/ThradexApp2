package org.thradex.sis.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.SisRol;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


@Repository
public class RoleDAOImpl implements RoleDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public SisRol getRole(int id) {
		SisRol role = getCurrentSession().load(SisRol.class, id);
		return role;
	}

}
