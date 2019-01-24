package org.thradex.rrhh.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.RhSchedule;
import org.thradex.model.RhScheduleDay;
import org.thradex.util.ConstantsSis;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;


@Repository
public class ScheduleDayDAOImpl implements ScheduleDayDAO {
	
	Logger log = Logger.getLogger(ConstantsSis.LOG_DAO);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	public RhScheduleDay get(Date date, RhSchedule rhSchedule) {
		DateTime now = new DateTime(date);
		return (RhScheduleDay) openSession()
				.createQuery("FROM RhScheduleDay WHERE day = :day "
						+ " AND rhSchedule.id = :idRhSchedule")
				.setParameter("day", now.getDayOfWeek())
				.setParameter("idRhSchedule", rhSchedule.getId())
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<RhScheduleDay> list(RhSchedule rhSchedule) {
		return 	openSession()
				.createQuery("FROM RhScheduleDay WHERE "
						+ " rhSchedule.id = :idRhSchedule")
				.setParameter("idRhSchedule", rhSchedule.getId())
				.list();
	}

	public RhScheduleDay get(int id) {
		return openSession().get(RhScheduleDay.class, id);
	}
}
