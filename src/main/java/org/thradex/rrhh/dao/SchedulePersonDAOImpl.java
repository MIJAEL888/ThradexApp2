package org.thradex.rrhh.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.RhPerson;
import org.thradex.model.RhSchedulePerson;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


@Repository
public class SchedulePersonDAOImpl implements SchedulePersonDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public void update(RhSchedulePerson rhSchedulePerson){
		openSession().update(rhSchedulePerson);
	}
	
	public RhSchedulePerson save(RhSchedulePerson rhSchedulePerson){
		rhSchedulePerson.setId(openSession().save(rhSchedulePerson).hashCode());
		return rhSchedulePerson;
	}

	public RhSchedulePerson get(RhPerson rhPerson) {
		return (RhSchedulePerson) openSession()
				.createQuery("FROM RhSchedulePerson WHERE rhPerson.id = :idPerson")
				.setParameter("idPerson", rhPerson.getId())
				.uniqueResult();
	}
	
	public RhSchedulePerson get(RhPerson rhPerson, RhStatus rhStatus) {
		return (RhSchedulePerson) openSession()
				.createQuery("FROM RhSchedulePerson WHERE "
						+ " rhPerson.id = :idPerson AND"
						+ " rhStatus.id = :idRhStatus " )
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idPerson", rhPerson.getId())
				.uniqueResult();
	}
	
	public RhSchedulePerson get(RhPerson rhPerson, RhStatus rhStatus, RhType rhTypeSch) {
		return (RhSchedulePerson) openSession()
				.createQuery("FROM RhSchedulePerson WHERE "
						+ " rhPerson.id = :idPerson AND"
						+ " rhStatus.id = :idRhStatus AND"
						+ " rhSchedule.rhType.id = :idRhTypeSch" )
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhTypeSch", rhTypeSch.getId())
				.setParameter("idPerson", rhPerson.getId())
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<RhSchedulePerson> list(RhStatus rhStatus, RhType rhTypeSch) {
		//se hizo el cambio para que tome solo las personas que estan activas para que no genere rh_rshit de personas que no estan activas
		return  openSession()
				.createQuery("FROM RhSchedulePerson WHERE rhStatus.id = :idRhStatus"
						+ " AND rhSchedule.rhType.id = :idRhTypeSch AND rhPerson.rhStatus.id=52")
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhTypeSch", rhTypeSch.getId())
				.list();
		
//		return  openSession()
//				.createQuery("FROM RhSchedulePerson WHERE rhStatus.id = :idRhStatus"
//						+ " AND rhSchedule.rhType.id = :idRhTypeSch")
//				.setParameter("idRhStatus", rhStatus.getId())
//				.setParameter("idRhTypeSch", rhTypeSch.getId())
//				.list();
	}

}
