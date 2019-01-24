package org.thradex.rrhh.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.RhCompany;
import org.thradex.model.RhPerson;
import org.thradex.model.RhStatus;

@Repository
public class PersonDAOImpl implements PersonDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	public RhPerson getRhPerson(int id) {
		return openSession().get(RhPerson.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<RhPerson> list(RhCompany rhCompany, RhStatus rhStatus) {
		return openSession().createQuery(" FROM RhPerson p WHERE "
				+ " p.rhCompany.id = :idRhCompany AND "
				+ " p.rhStatus.id = :idRhStatus "
				+ " ORDER BY p.name")
				.setParameter("idRhCompany", rhCompany.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.list();
	}
}
