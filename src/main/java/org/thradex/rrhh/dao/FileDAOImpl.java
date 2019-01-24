package org.thradex.rrhh.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.RhFile;
import org.thradex.model.RhShift;

@Repository
public class FileDAOImpl implements FileDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public List<RhFile> getRhFile(RhShift rhShift) {
		return openSession().createQuery("FROM RhFile WHERE "
				+ " rhShift.id = :idRhShift ")
				.setParameter("idRhShift", rhShift.getId())
				.list();
	}

	public RhFile save(RhFile rhFile) {
		rhFile.setId(openSession().save(rhFile).hashCode());
		return rhFile;
	}

	public RhFile get(int idRhFile) {
		return openSession().get(RhFile.class, idRhFile);
	}
	
	

}
