package org.thradex.rrhh.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.RhStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


@Repository
public class StatusDAOImpl implements StatusDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	
	public RhStatus getRhStatus(String category, int code) {
		return  (RhStatus) openSession()
				.createQuery("from RhStatus rs where rs.category = :category "
						+ " and rs.code = :code ")
				.setParameter("category", category)
				.setParameter("code", code)
				.uniqueResult();
	}

	
	public RhStatus getRhStatus(int id) {
		return  (RhStatus) openSession()
				.createQuery("from RhStatus rs where rs.id = :id ")
				.setParameter("id", id)
				.uniqueResult();
	}

	
	public RhStatus getRhStatus(String category) {
		return  (RhStatus) openSession()
				.createQuery("from RhStatus rs where rs.category = :category ")
				.setParameter("category", category)
				.uniqueResult();
	}


}
