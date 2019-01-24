package org.thradex.rrhh.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.RhPerson;
import org.thradex.model.RhSchedulePerson;
import org.thradex.model.RhScheduleScale;
import org.thradex.model.RhStatus;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;


@Repository
public class ScheduleScaleDAOImpl implements ScheduleScaleDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	public RhScheduleScale get(int id) {
		return (RhScheduleScale) openSession()
				.createQuery("FROM RhScheduleScale WHERE id = :id ")
				.setParameter("id", id)
				.uniqueResult();
	}
	
	public RhScheduleScale get(RhPerson rhPerson, RhStatus rhStatus, Date date) {
		DateTime dateTime = new DateTime(date);
		return (RhScheduleScale) openSession()
				.createQuery("FROM RhScheduleScale WHERE rhSchedulePerson.id = :idPerson "
						+ " and day(date) = :day "
						+ " and month(date) = :month "
						+ " and year(date) = :year "
						+ " and rhStatus.id = :idStatus ")
				.setParameter("idPerson", rhPerson.getId())
				.setParameter("day", dateTime.getDayOfMonth())
				.setParameter("month", dateTime.getMonthOfYear())
				.setParameter("year", dateTime.getYear())
				.setParameter("idStatus", rhStatus.getId())
				.uniqueResult();
	}

	public RhScheduleScale get(RhPerson rhPerson, Date date) {
		DateTime dateTime = new DateTime(date);
		return (RhScheduleScale) openSession()
				.createQuery("FROM RhScheduleScale WHERE rhSchedulePerson.id = :idPerson "
						+ " and day(date) = :day "
						+ " and month(date) = :month "
						+ " and year(date) = :year ")
				.setParameter("idPerson", rhPerson.getId())
				.setParameter("day", dateTime.getDayOfMonth())
				.setParameter("month", dateTime.getMonthOfYear())
				.setParameter("year", dateTime.getYear())
				.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<RhScheduleScale> list(Date start, Date end) {
		return openSession()
				.createQuery("FROM RhScheduleScale WHERE  "
						+ " date >= :start AND "
						+ " date <= :end ")
				.setParameter("start", start )
				.setParameter("end", end)
				.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<RhScheduleScale> list(RhStatus rhStatus, Date date) {
		DateTime dateTime = new DateTime(date);
		return openSession()
				.createQuery("FROM RhScheduleScale WHERE "
						+ " day(date) = :day AND "
						+ " month(date) = :month AND "
						+ " year(date) = :year AND "
						+ " rhStatus.id = :idStatus ")
				.setParameter("day", dateTime.getDayOfMonth())
				.setParameter("month", dateTime.getMonthOfYear())
				.setParameter("year", dateTime.getYear())
				.setParameter("idStatus", rhStatus.getId())
				.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<RhScheduleScale> list(RhSchedulePerson rhSchedulePerson, Date start, Date end) {
		return openSession()
				.createQuery("FROM RhScheduleScale WHERE  "
						+ " date >= :start AND "
						+ " date <= :end AND "
						+ " rhSchedulePerson.id = :idPerson ")
				.setParameter("start", start )
				.setParameter("end", end)
				.setParameter("idPerson", rhSchedulePerson.getId())
				.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<RhScheduleScale> list2(RhSchedulePerson rhSchedulePerson, Date start, Date end) {
		return openSession()
				.createQuery("FROM RhScheduleScale WHERE  "
						+ " date > :start AND "
						+ " date <= :end AND "
						+ " rhSchedulePerson.id = :idPerson ")
				.setParameter("start", start )
				.setParameter("end", end)
				.setParameter("idPerson", rhSchedulePerson.getId())
				.list();
	}
	
	public void update(RhScheduleScale rhScheduleScale) {
		openSession().update(rhScheduleScale);
	}

	public RhScheduleScale save(RhScheduleScale rhScheduleScale) {
		rhScheduleScale.setId(openSession().save(rhScheduleScale).hashCode());
		return rhScheduleScale;
	}

	public void delete(RhScheduleScale rhScheduleScale) {
		openSession().delete(rhScheduleScale);
	}

}
