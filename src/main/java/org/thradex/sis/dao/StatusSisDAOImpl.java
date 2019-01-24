package org.thradex.sis.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.SisStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


@Repository
public class StatusSisDAOImpl implements StatusSisDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public SisStatus getSisStatus(String category, int code) {
		return  (SisStatus) openSession()
				.createQuery("from SisStatus rs where rs.category = :category "
						+ " and rs.code = :code ")
				.setParameter("category", category)
				.setParameter("code", code)
				.uniqueResult();
	}

	@Override
	public SisStatus getSisStatus(int id) {
		return  (SisStatus) openSession()
				.createQuery("from SisStatus rs where rs.id = :id ")
				.setParameter("id", id)
				.uniqueResult();
	}

	@Override
	public SisStatus getSisStatus(String category) {
		return  (SisStatus) openSession()
				.createQuery("from SisStatus rs where rs.category = :category ")
				.setParameter("category", category)
				.uniqueResult();
	}


}
