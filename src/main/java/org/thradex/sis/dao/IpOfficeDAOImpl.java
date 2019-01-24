package org.thradex.sis.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.SisIpOffice;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


@Repository
public class IpOfficeDAOImpl implements IpOfficeDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public SisIpOffice getSisIpOffice(String ip) {
		return (SisIpOffice) openSession()
				.createQuery("from SisIpOffice p where p.ip = :ip")
				.setParameter("ip", ip)
				.uniqueResult();
	}

}
