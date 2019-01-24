package org.thradex.rrhh.dao;


import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.thradex.util.ConstantsSis;

@Repository
public class UtilRrhhDAOImpl implements UtilRrhhDAO {
	
	Logger log = Logger.getLogger(ConstantsSis.LOG_DAO);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	public Date getNowDate() {
		return (Date) openSession().createSQLQuery("select now()").uniqueResult();
	}


}
