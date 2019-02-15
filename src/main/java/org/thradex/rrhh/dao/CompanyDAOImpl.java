package org.thradex.rrhh.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.RhCompany;
import org.thradex.model.RhCompanyType;
import org.thradex.model.RhType;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


@Repository
public class CompanyDAOImpl implements CompanyDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	public RhCompany get(int id) {
		return openSession().get(RhCompany.class, id);
	}

	public List<RhCompany> list(RhType rhType) {
		List<RhCompany> rhCompanies = new ArrayList<RhCompany>();
		List<RhCompanyType> rhCompanyTypes = listType(rhType);
		for (RhCompanyType rhCompanyType : rhCompanyTypes) {
			rhCompanies.add(rhCompanyType.getRhCompany());
		}
		return rhCompanies;
	}

	@SuppressWarnings("unchecked")
	public List<RhCompanyType> listType(RhType rhType) {
		return openSession().createQuery("FROM RhCompanyType rc WHERE "
				+ " rc.rhType.id = :idRhType AND "
				+ " rc.rhCompany.flag = 1")
				.setParameter("idRhType", rhType.getId())
				.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<RhCompany> lsCompany(){
		return openSession().createQuery("FROM RhCompany").list();
	}

}
