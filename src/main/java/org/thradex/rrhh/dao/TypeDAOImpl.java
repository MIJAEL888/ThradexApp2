package org.thradex.rrhh.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.RhType;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


@Repository
public class TypeDAOImpl implements TypeDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	
	public RhType getRhType(String category, int code) {
		return  (RhType) openSession()
				.createQuery("from RhType rs where rs.category = :category "
						+ " and rs.code = :code ")
				.setParameter("category", category)
				.setParameter("code", code)
				.uniqueResult();
	}

	
	public RhType getRhType(int id) {
		return  (RhType) openSession()
				.createQuery("from RhType rs where rs.id = :id ")
				.setParameter("id", id)
				.uniqueResult();
	}

	
	@SuppressWarnings("unchecked")
	public List<RhType> getRhType(String category) {
		return   openSession()
				.createQuery("from RhType rs where rs.category = :category "
						+ " ORDER BY name")
				.setParameter("category", category)
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<RhType> getRhType(String category, RhType rhTypeParent) {
		return   openSession()
				.createQuery("from RhType rs where "
						+ " rs.category = :category AND "
						+ " rs.rhTypeParent.id = :idParent"
						+ " ORDER BY name")
				.setParameter("category", category)
				.setParameter("idParent", rhTypeParent.getId())
				.list();
	}

	
	@SuppressWarnings("unchecked")
	public List<RhType> get(int code) {
		return   openSession()
				.createQuery("from RhType rs where rs.code = :code ")
				.setParameter("code", code)
				.list();
	}

}
