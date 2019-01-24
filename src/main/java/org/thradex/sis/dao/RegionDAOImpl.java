package org.thradex.sis.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.SisCountry;
import org.thradex.model.SisRegion;
import org.thradex.model.SisStatus;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


@Repository
public class RegionDAOImpl implements RegionDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public SisRegion get(int id) {
		return (SisRegion) openSession()
			.createQuery("from SisRegion r where r.id = :id")
			.setParameter("id", id)
			.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SisRegion> list(SisStatus sisStatus) {
		return openSession()
			.createQuery("from SisRegion r where r.sisStatus.id = :idStatus")
			.setParameter("idStatus", sisStatus.getId())
			.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> listMap(SisStatus sisStatus) {
		return openSession()
			.createQuery("SELECT new map(r.name as NAME, r.id as ID) "
					+ "FROM SisRegion r "
					+ "WHERE r.sisStatus.id = :idStatus ")
			.setParameter("idStatus", sisStatus.getId())
			.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> listMap(SisStatus sisStatus, SisCountry sisCountry) {
		return openSession()
			.createQuery("SELECT new map(r.name as NAME, r.id as ID) "
					+ "FROM SisRegion r "
					+ "WHERE r.sisStatus.id = :idStatus AND "
					+ "r.sisCountry.id = :idCountry ")
			.setParameter("idStatus", sisStatus.getId())
			.setParameter("idCountry", sisCountry.getId())
			.list();
	}
}
