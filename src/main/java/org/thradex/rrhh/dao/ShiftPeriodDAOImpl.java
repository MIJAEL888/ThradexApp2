package org.thradex.rrhh.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.RhCompany;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;


@Repository
public class ShiftPeriodDAOImpl implements ShiftPerioDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	
	public RhShiftPeriod get(Date date, RhCompany rhCompany, RhStatus rhStatus, RhType rhType) {
		DateTime dateTime = new DateTime(date);
		DateTime date2 = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), 0, 0);
		return  (RhShiftPeriod) openSession()
				.createQuery("FROM RhShiftPeriod rp WHERE "
						+ "	rp.dateStart <= :date AND "
						+ "	rp.dateFinish >= :date AND "
						+ " rp.rhCompany.id = :idRhCompany AND "
						+ " rp.rhStatus.id = :idRhStatus AND "
						+ " rp.rhType.id = :idRhType ")
				.setParameter("date", date2.toDate())
				.setParameter("idSisRegion", rhCompany.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhType", rhType.getId())
				.uniqueResult();
	}

	
	public RhShiftPeriod get(Date date, RhCompany rhCompany) {
		DateTime dateTime = new DateTime(date);
		DateTime date2 = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), 0, 0);
		return  (RhShiftPeriod) openSession()
				.createQuery("FROM RhShiftPeriod rp WHERE "
						+ "	rp.dateStart <= :date AND "
						+ "	rp.dateFinish >= :date AND "
						+ " rp.rhCompany.id = :idRhCompany ")
				.setParameter("date", date2.toDate())
				.setParameter("idRhCompany", rhCompany.getId())
				.uniqueResult();
	}
	
	
	public RhShiftPeriod getLimit(Date date, RhCompany rhCompany) {
		DateTime dateTime = new DateTime(date);
		DateTime date2 = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), 0, 0);
		return  (RhShiftPeriod) openSession()
				.createQuery("FROM RhShiftPeriod rp WHERE "
						+ "	rp.fistDay <= :date AND "
						+ "	rp.lastDay >= :date AND "
						+ " rp.rhCompany.id = :idRhCompany ")
				.setParameter("date", date2.toDate())
				.setParameter("idRhCompany", rhCompany.getId())
				.uniqueResult();
	}
	
	public RhShiftPeriod get(int id) {
		return openSession().get(RhShiftPeriod.class, id);
	}

	
	public RhShiftPeriod get(RhCompany rhCompany, RhStatus rhStatus) {
		return  (RhShiftPeriod) openSession()
				.createQuery("FROM RhShiftPeriod rp WHERE "
						+ " rp.rhCompany.id = :idRhCompany AND "
						+ " rp.rhStatus.id = :idRhStatus ")
				.setParameter("idRhCompany", rhCompany.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.uniqueResult();
	}

	
	@SuppressWarnings("unchecked")
	public List<RhShiftPeriod> list(RhCompany rhCompany, RhStatus rhStatus) {
		// TODO Auto-generated method stub
		return openSession()
				.createQuery("FROM RhShiftPeriod rp WHERE "
						+ " rp.rhCompany.id = :idRhCompany AND "
						+ " rp.rhStatus.id = :idRhStatus ")
				.setParameter("idRhCompany", rhCompany.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<RhShiftPeriod> list(RhCompany rhCompany) {
		// TODO Auto-generated method stub
		return openSession()
				.createQuery("FROM RhShiftPeriod rp WHERE "
						+ " rp.rhCompany.id = :idRhCompany AND " +
						" rp.rhStatus != 32 " +
						" ORDER BY  rp.dateStart DESC ")
				.setParameter("idRhCompany", rhCompany.getId())
				.list();
	}

	public void update(RhShiftPeriod rhShiftPeriod) {
		openSession().update(rhShiftPeriod);
	}

}
