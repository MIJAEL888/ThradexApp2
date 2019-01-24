package org.thradex.sis.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.SisCountry;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


@Repository
public class CountryDAOImpl implements CountryDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public SisCountry get(int id) {
		return (SisCountry) openSession()
			.createQuery("FROM SisCountry c WHERE c.id = :id")
			.setParameter("id", id)
			.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SisCountry> list() {
		return openSession()
			.createQuery("from SisCountry ")
			.list();
	}
}
