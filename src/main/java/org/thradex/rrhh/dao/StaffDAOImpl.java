package org.thradex.rrhh.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.RhArea;
import org.thradex.model.RhPerson;
import org.thradex.model.RhStaff;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;

@Repository
public class StaffDAOImpl implements StaffDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	
	public RhStaff getRhStaff(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@SuppressWarnings("unchecked")
	public List<RhStaff> listMng(RhArea rhArea, RhStatus rhStatus, RhType rhTypeAP) {
		return openSession()
				.createQuery("from RhStaff s where s.rhStatus.id = :idRhStatus "
						+ " and s.rhAreaPosition.rhArea.id = :idRhArea "
						+ " and s.rhAreaPosition.rhType.id = :idRhTypeAP ")
				.setParameter("idRhArea", rhArea.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhTypeAP", rhTypeAP.getId())
				.list();
	}

	
	@SuppressWarnings("unchecked")
	public List<RhStaff> listStaffMng(RhArea rhArea, RhStatus rhStatus, RhType rhTypeAP) {
		return openSession()
				.createQuery("from RhStaff s where s.rhStatus.id = :idRhStatus "
						+ " and s.rhAreaPosition.rhArea.areaParent.id = :idRhArea "
						+ " and s.rhAreaPosition.rhType.id = :idRhTypeAP ")
				.setParameter("idRhArea", rhArea.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhTypeAP", rhTypeAP.getId())
				.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhStaff> listStaff(RhArea rhArea, RhStatus rhStatus, RhType rhTypeAP) {
		return openSession()
				.createQuery("from RhStaff s where s.rhStatus.id = :idRhStatus "
						+ " and s.rhAreaPosition.rhArea.id = :idRhArea "
						+ " and s.rhAreaPosition.rhType.id <> :idRhTypeAP ")
				.setParameter("idRhArea", rhArea.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhTypeAP", rhTypeAP.getId())
				.list();
		}

	
	@SuppressWarnings("unchecked")
	public List<RhStaff> listStaff(RhArea rhArea, RhStatus rhStatus) {
		return openSession()
				.createQuery("from RhStaff s where s.rhStatus.id = :idRhStatus "
						+ " and s.rhAreaPosition.rhArea.id = :idRhArea ")
				.setParameter("idRhArea", rhArea.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.list();
		}
	
	
	@SuppressWarnings("unchecked")
	public List<RhStaff> list(RhPerson rhPerson, RhStatus rhStatus) {
		return openSession()
				.createQuery("from RhStaff s where s.rhStatus.id = :idRhStatus "
						+ " and s.rhPerson.id = :idRhPerson ")
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhPerson", rhPerson.getId())
				.list();
	}
	
	
	public RhStaff get(RhPerson rhPerson, RhType rhType, RhStatus rhStatus) {
		return (RhStaff) openSession()
				.createQuery("from RhStaff s WHERE "
						+ " s.rhStatus.id = :idRhStatus AND "
						+ " s.rhPerson.id = :idRhPerson AND "
						+ " s.rhType.id = :idRhType ")
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhType", rhType.getId())
				.setMaxResults(1)
				.uniqueResult();
	}
	
}
