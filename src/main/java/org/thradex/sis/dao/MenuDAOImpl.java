package org.thradex.sis.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.SisMenu;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


@Repository
public class MenuDAOImpl implements MenuDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SisMenu> listMenu(int flagAct, int flagLevel, int idParent) {
		List<SisMenu> lSisMenus = openSession().createQuery("from SisMenu m where "
				+ " m.idAplication = 2 and "
				+ " m.flagAct = :flagAct and "
				+ " m.level = :flagLevel and "
				+ " m.sisMenuParent.id = :idParent "
				+ " ORDER BY m.order ")
				.setParameter("flagAct", flagAct)
				.setParameter("flagLevel", flagLevel)
				.setParameter("idParent", idParent)
				.list();
		return lSisMenus;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SisMenu> listMenu(int flagAct, int flagLevel) {
		List<SisMenu> lSisMenus = openSession().createQuery("from SisMenu m where "
				+ " m.idAplication = 2 and "
				+ " m.flagAct = :flagAct and "
				+ " m.level = :flagLevel "
				+ " ORDER BY m.order ")
				.setParameter("flagAct", flagAct)
				.setParameter("flagLevel", flagLevel)
				.list();
		return lSisMenus;
	}



}
