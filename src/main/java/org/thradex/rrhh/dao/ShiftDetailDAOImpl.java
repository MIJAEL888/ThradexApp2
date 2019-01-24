package org.thradex.rrhh.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.RhPerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhShiftDetail;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;
import org.thradex.util.ConstantsSis;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


@Repository
public class ShiftDetailDAOImpl implements ShiftDetailDAO {
	Logger log = Logger.getLogger(ConstantsSis.LOG_DAO);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	
	public RhShiftDetail get(int id) {
		return openSession().get(RhShiftDetail.class, id);
	}

	
	public RhShiftDetail save(RhShiftDetail rhShiftDetail) {
		rhShiftDetail.setId(openSession().save(rhShiftDetail).hashCode());
		return rhShiftDetail;
	}

	
	public void update(RhShiftDetail rhShiftDetail) {
		openSession().update(rhShiftDetail);
	}

	
	public void delete(RhShift rhShift) {
		openSession().createQuery("DELETE RhShiftDetail sd WHERE "
				+ " sd.rhShift.id = :idRhShift ")
			.setParameter("idRhShift", rhShift.getId())
			.executeUpdate();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShiftDetail> list(RhShift rhShift) {
		return openSession().createQuery("FROM RhShiftDetail sd  WHERE "
				+ " sd.rhShift.id = :idRhShift ")
				.setParameter("idRhShift", rhShift.getId())
				.list();
	}

	
	@SuppressWarnings("unchecked")
	public List<RhShiftDetail> list(RhType typeDetail, RhPerson rhPerson, RhShiftPeriod rhShiftPeriod) {
		return openSession().createQuery("FROM RhShiftDetail sd  WHERE "
				+ " sd.rhType.id = :idType AND "
				+ " sd.rhShift.rhShiftPeriod.id = :idPeriod AND "
				+ " sd.rhShift.rhPerson.id = :idPerson")
				.setParameter("idType", typeDetail.getId())
				.setParameter("idPeriod", rhShiftPeriod.getId())
				.setParameter("idPerson", rhPerson.getId())
				.list();
	}
	
	
	public Integer sumTotal(RhType typeDetail, RhPerson rhPerson, RhShiftPeriod rhShiftPeriod) {
		Long num =  (Long) openSession().createQuery("SELECT sum(sd.totalRate) FROM RhShiftDetail AS sd WHERE"
				+ " sd.rhType.id = :idType AND "
				+ " sd.rhShift.rhShiftPeriod.id = :idPeriod AND "
				+ " sd.rhShift.rhPerson.id = :idPerson "
				+ " GROUP BY sd.rhType.id ")
				.setParameter("idType", typeDetail.getId())
				.setParameter("idPeriod", rhShiftPeriod.getId())
				.setParameter("idPerson", rhPerson.getId())
				.uniqueResult();
		if (num != null) {
			 return num.intValue();
		}else {
			return 0;
		}
		
	}

	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> sumTotal(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod) {
		return openSession().createQuery("SELECT new map(sd.rhType.code as code, "
				+ " sum(sd.totalRate) as totalRate) FROM RhShiftDetail sd  WHERE "
				+ " sd.rhShift.rhShiftPeriod.id = :idPeriod AND "
				+ " sd.rhShift.rhPerson.id = :idPerson"
				+ " GROUP BY sd.rhType.id ")
				.setParameter("idPeriod", rhShiftPeriod.getId())
				.setParameter("idPerson", rhPerson.getId())
				.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> sumTotal(RhShift rhShift, RhType typeDetail) {
		return (Map<String, Object>) openSession().createQuery("SELECT new map(sd.rhType.code as code, "
				+ " sd.rhType.name as name,"
				+ " sum(sd.totalRate) as totalRate) FROM RhShiftDetail sd  WHERE "
				+ " sd.rhShift.id = :idShift AND "
				+ " sd.rhType.id = :idType "
				+ " GROUP BY sd.rhType.id ")
				.setParameter("idShift", rhShift.getId())
//				.setParameter("idShiftParent", rhShift.getId())
				.setParameter("idType", typeDetail.getId())
				.uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> sumTotalChild(RhShift rhShift, RhType rhTypeDetail) {
		return (Map<String, Object>) openSession().createQuery("SELECT new map(sd.rhType.code as code, "
				+ " sd.rhType.name as name,"
				+ " sum(sd.totalRate) as totalRate) FROM RhShiftDetail sd  WHERE "
				+ " (sd.rhShift.id = :idShift OR "
				+ " sd.rhShift.rhShiftParent.id = :idShiftParent) AND "
				+ " sd.rhType.id = :idType "
				+ " GROUP BY sd.rhType.id ")
				.setParameter("idShift", rhShift.getId())
				.setParameter("idShiftParent", rhShift.getId())
				.setParameter("idType", rhTypeDetail.getId())
				.uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> sumTotalChild(RhShift rhShift, RhType rhTypeDetail, RhType rhTypeRate) {
		return (Map<String, Object>) openSession().createQuery("SELECT new map(sd.rhType.code as code, "
				+ " sd.rhType.name as name,"
				+ " sum(sd.totalRate) as totalRate) FROM RhShiftDetail sd  WHERE "
				+ " (sd.rhShift.id = :idShift OR "
				+ " sd.rhShift.rhShiftParent.id = :idShiftParent) AND "
				+ " sd.rhType.id = :idType AND "
				+ " sd.rhTypeRate.id = :idRhTypeRate "
				+ " GROUP BY sd.rhType.id ")
				.setParameter("idShift", rhShift.getId())
				.setParameter("idShiftParent", rhShift.getId())
				.setParameter("idType", rhTypeDetail.getId())
				.setParameter("idRhTypeRate", rhTypeRate.getId())
				.uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> sumTotal(RhPerson rhPerson, RhType typeDetail, RhShiftPeriod rhShiftPeriod) {
		return (Map<String, Object>) openSession().createQuery("SELECT new map(sd.rhType.code as code, "
				+ " sd.rhType.name as name,"
				+ " sum(sd.totalRate) as totalRate) FROM RhShiftDetail sd  WHERE "
				+ " sd.rhShift.rhPerson.id = :idPerson AND "
				+ " sd.rhShift.rhShiftPeriod.id = :idShiftPeriod AND "
				+ " sd.rhType.id = :idType "
				+ " GROUP BY sd.rhType.id ")
				.setParameter("idPerson", rhPerson.getId())
				.setParameter("idType", typeDetail.getId())
				.setParameter("idShiftPeriod", rhShiftPeriod.getId())
				.uniqueResult();
	}

	
	public Integer getTotal(RhShift rhShift, RhType rhTypeRate) {
		//openSession().createStoredProcedureCall()
		return (Integer) openSession().createQuery("SELECT total FROM RhShiftDetail sd  WHERE "
				+ " sd.rhShift.id = :idShift AND "
				+ " sd.rhTypeRate.id = :idTypeRate "
				+ " GROUP BY sd.rhType.id ")
				.setParameter("idShift", rhShift.getId())
				.setParameter("idTypeRate", rhTypeRate.getId())
				.uniqueResult();
	}
	
	public Integer getSumTotal(RhShift rhShift) {
		Long num =  (Long) openSession().createQuery("SELECT sum(sd.totalRate) FROM RhShiftDetail AS sd WHERE"
				+ " sd.rhShift.id = :idShift "
				+ " GROUP BY sd.rhShift.id ")
				.setParameter("idShift", rhShift.getId())
				.uniqueResult();
		if (num != null) {
			 return num.intValue();
		}else {
			return 0;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShiftDetail> list(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod){
			return openSession().createQuery("FROM RhShiftDetail WHERE "
					+ " rhShift.rhPerson.id = :idRhPerson AND "
					+ " rhShift.rhShiftPeriod.id = :idRhPeriod  "
					+ "  ")
					.setParameter("idRhPerson", rhPerson.getId())
					.setParameter("idRhPeriod", rhShiftPeriod.getId())
					.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShiftDetail> list(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod, RhStatus rhStatusSis){
			return openSession().createQuery("FROM RhShiftDetail WHERE "
					+ " rhShift.rhPerson.id = :idRhPerson AND "
					+ " rhShift.rhShiftPeriod.id = :idRhPeriod AND "
					+ " rhShift.rhStatusSis.id = :idRhStatusSis ")
					.setParameter("idRhPerson", rhPerson.getId())
					.setParameter("idRhPeriod", rhShiftPeriod.getId())
					.setParameter("idRhStatusSis", rhStatusSis.getId())
					.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShiftDetail> list(RhShiftPeriod rhShiftPeriod){
			return openSession().createQuery("FROM RhShiftDetail WHERE "
					+ " rhShift.rhShiftPeriod.id = :idRhPeriod  "
					+ " ")
					.setParameter("idRhPeriod", rhShiftPeriod.getId())
					.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShiftDetail> listMng(RhPerson rhPerson, RhShiftPeriod rhShiftPeriod){
			return openSession().createQuery("FROM RhShiftDetail WHERE "
					+ " rhShift.rhPersonMng.id = :idRhPerson AND "
					+ " rhShift.rhShiftPeriod.id = :idRhPeriod  "
					+ " ")
					.setParameter("idRhPerson", rhPerson.getId())
					.setParameter("idRhPeriod", rhShiftPeriod.getId())
					.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShiftDetail> listDayNull(){
			return openSession().createQuery("FROM RhShiftDetail WHERE "
					+ " rhTypeDay IS NULL OR"
					+ " rhTypeShiftDay IS NULL "
					+ " ")
					.list();
	}
}
