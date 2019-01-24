package org.thradex.rrhh.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.RhArea;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


@Repository
public class AreaDAOImpl implements AreaDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	public RhArea getAreaById(int idArea){
		return openSession().get(RhArea.class, idArea);
	}
//
//	public List<RhCompany> list(RhType rhType) {
//		List<RhCompany> rhCompanies = new ArrayList<RhCompany>();
//		List<RhCompanyType> rhCompanyTypes = listType(rhType);
//		for (RhCompanyType rhCompanyType : rhCompanyTypes) {
//			rhCompanies.add(rhCompanyType.getRhCompany());
//		}
//		return rhCompanies;
//	}

//	@SuppressWarnings("unchecked")
//	public List<RhCompanyType> listType(RhType rhType) {
//		return openSession().createQuery("FROM RhCompanyType rc WHERE "
//				+ " rc.rhType.id = :idRhType ")
//				.setParameter("idRhType", rhType.getId())
//				.list();
//	}
	
	@SuppressWarnings("unchecked")
	public List<RhArea> lsAreaByIdCompany(int idCompany){
		return openSession().createQuery("FROM RhArea ra WHERE ra.rhCompany.id = :idCompany")
				.setParameter("idCompany", idCompany).list();
	}

}
