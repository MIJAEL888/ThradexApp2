package org.thradex.rrhh.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thradex.model.EvenUsuario;
import org.thradex.model.RhAreaPosition;
import org.thradex.model.RhCompany;
import org.thradex.model.RhPerson;
import org.thradex.model.RhShift;
import org.thradex.model.RhShiftPeriod;
import org.thradex.model.RhStaff;
import org.thradex.model.RhStatus;
import org.thradex.model.RhType;
import org.thradex.model.ValorCosto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


@Repository
public class ShiftDAOImpl implements ShiftDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	public RhShift getRhShift() {
		return new RhShift();
	}
	
	
	public RhShift saveRhShift(RhShift rhShift) {
		rhShift.setId((Integer) openSession().save(rhShift));
		return rhShift;
	}
	
	
	public void updateRhShift(RhShift rhShift) {
		openSession().update(rhShift);
	}

	
	public RhShift getRhShift(int idShift) {
		return openSession().get(RhShift.class, idShift);
	}

	/* (non-Javadoc)
	 * @see org.thradex.rrhh.dao.ShiftDAO#getRhShift(int, int, int)
	 * 
	 * type code
	 * 1=SHIFT 
	 * 2=PERMISION 
	 * 3=PROGRAMED JOB
	 * 4=EXTRA HOUR 
	 */
	
	public RhShift getRhShift(RhPerson rhPerson, RhStatus rhStatus,  RhType rhType) {
		return  (RhShift) openSession()
				.createQuery("from RhShift rs where rs.rhPerson.id = :idPerson "
			+ " and rs.rhStatus.id = :idRhStatus "
			+ " and rs.rhType.id = :idRhType " 
			+ " ORDER BY rs.id DESC")
		.setParameter("idPerson", rhPerson.getId())
		.setParameter("idRhStatus", rhStatus.getId())
		.setParameter("idRhType", rhType.getId())
		.setMaxResults(1)
		.uniqueResult();
	}

	
	public RhShift getRhShift(RhPerson rhPerson, RhStatus rhStatus,  RhType rhType, Date finishDate) {
		return  (RhShift) openSession()
				.createQuery("from RhShift rs where rs.rhPerson.id = :idPerson "
			+ " and rs.rhStatus.id = :idRhStatus "
			+ " and rs.rhType.id = :idRhType " 
			+ " and rs.dateFinish > :dateFinish "
			+ " ORDER BY rs.dateStart")
		.setParameter("idPerson", rhPerson.getId())
		.setParameter("idRhStatus", rhStatus.getId())
		.setParameter("idRhType", rhType.getId())
		.setParameter("dateFinish", finishDate)
		.setMaxResults(1)
		.uniqueResult();
		
		/*(RhShift) openSession()
				.createQuery("from RhShift rs where rs.rhPerson.id = :idPerson "
			+ " and rs.rhStatus.id = :idRhStatus "
			+ " and rs.rhType.id = :idRhType " 
			+ " and day(rs.dateStart) = :day "
			+ " and month(rs.dateStart) = :month "
			+ " and year(rs.dateStart) = :year "
			+ " ORDER BY rs.dateStart")
		.setParameter("idPerson", rhPerson.getId())
		.setParameter("idRhStatus", rhStatus.getId())
		.setParameter("idRhType", rhType.getId())
		.setParameter("day", dateTime.getDayOfMonth())
		.setParameter("month",  dateTime.getMonthOfYear())
		.setParameter("year", dateTime.getYear())
		.setMaxResults(1)
		.uniqueResult();
		*/
	}

	
	public RhShift get(RhPerson rhPerson, RhStatus rhStatus,  RhType rhType, Date date) {
		return  (RhShift) openSession()
				.createQuery("from RhShift rs where rs.rhPerson.id = :idPerson "
			+ " AND rs.rhStatus.id = :idRhStatus "
			+ " AND rs.rhType.id = :idRhType " 
			+ " AND rs.dateFinishShift > :dateFinish "
			+ " AND rs.dateStartShift < :dateStart "
			+ " ORDER BY rs.dateStart")
		.setParameter("idPerson", rhPerson.getId())
		.setParameter("idRhStatus", rhStatus.getId())
		.setParameter("idRhType", rhType.getId())
		.setParameter("dateFinish", date)
		.setParameter("dateStart", date)
		.setMaxResults(1)
		.uniqueResult();
		
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listMng(RhPerson rhPerson, RhStatus rhStatus,  RhType rhType){
		return openSession().createQuery("FROM RhShift WHERE "
				+ " rhPersonMng.id = :idRhPerson "
				+ " and rhStatus.id = :idRhStatus "
				+ " and rhType.id = :idRhType "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhType", rhType.getId())
				.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listMng(RhPerson rhPerson, RhStatus rhStatus,  RhType rhType, RhShiftPeriod rhShiftPeriod){
		return openSession().createQuery("FROM RhShift WHERE "
				+ " rhPersonMng.id = :idRhPerson "
				+ " AND rhStatus.id = :idRhStatus "
				+ " AND rhShiftPeriod.id = :idRhPeriod "
				+ " AND rhType.id = :idRhType "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhType", rhType.getId())
				.setParameter("idRhPeriod", rhShiftPeriod.getId())
				.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listMng(RhPerson rhPerson, RhStatus rhStatus){
		return openSession().createQuery("FROM RhShift WHERE "
				+ " rhPersonMng.id = :idRhPerson "
				+ " AND rhStatus.id = :idRhStatus "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listMng(RhPerson rhPerson, RhStatus rhStatus, RhShiftPeriod rhShiftPeriod){
		return openSession().createQuery("FROM RhShift WHERE "
				+ " rhPersonMng.id = :idRhPerson "
				+ " AND rhStatus.id = :idRhStatus "
				+ " AND rhShiftPeriod.id = :idRhPeriod "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhPeriod", rhShiftPeriod.getId())
				.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listMng(RhPerson rhPerson, RhStatus rhStatus, Date startDate){
		return openSession().createQuery("FROM RhShift WHERE "
				+ " rhPersonMng.id = :idRhPerson "
				+ " AND rhStatus.id = :idRhStatus "
				+ " AND dateReg > :startDate  "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("startDate", startDate)
				.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listMngBySis(RhPerson rhPerson, RhStatus rhStatusSis,  RhType rhType){
		return openSession().createQuery("FROM RhShift WHERE "
				+ " rhPersonMng.id = :idRhPerson "
				+ " and rhStatusSis.id = :idRhStatus "
				+ " and rhType.id = :idRhType "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatusSis.getId())
				.setParameter("idRhType", rhType.getId())
				.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listMngBySis(RhPerson rhPerson, RhStatus rhStatusSis,  RhType rhType, RhShiftPeriod rhShiftPeriod){
		return openSession().createQuery("FROM RhShift WHERE "
				+ " rhPersonMng.id = :idRhPerson "
				+ " and rhStatusSis.id = :idRhStatus "
				+ " AND	rhShiftPeriod.id = :idRhPeriod "
				+ " and rhType.id = :idRhType "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatusSis.getId())
				.setParameter("idRhType", rhType.getId())
				.setParameter("idRhPeriod", rhShiftPeriod.getId())
				.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> list(RhPerson rhPerson, RhStatus rhStatus,  RhType rhType){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " rhPerson.id = :idRhPerson "
				+ " and rhStatus.id = :idRhStatus "
				+ " and rhType.id = :idRhType "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhType", rhType.getId())
				.list();
		return rhShifts;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> list(RhPerson rhPerson, Date dateStart, Date dateFinish){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " rhPerson.id = :idRhPerson AND "
				+ " dateStart >= :start AND "
				+ " dateStart <= :end  ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("start", dateStart)
				.setParameter("end", dateFinish)
				.list();
		return rhShifts;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> list(RhPerson rhPerson, RhStatus rhStatus,  RhType rhType, RhShiftPeriod rhShiftPeriod){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " rhPerson.id = :idRhPerson "
				+ " and rhStatus.id = :idRhStatus "
				+ " and rhType.id = :idRhType "
				+ " AND	rhShiftPeriod.id = :idRhPeriod "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhType", rhType.getId())
				.setParameter("idRhPeriod", rhShiftPeriod.getId())
				.list();
		return rhShifts;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> list(RhStatus rhStatus,  RhType rhType, RhShiftPeriod rhShiftPeriod){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " rhStatus.id = :idRhStatus "
				+ " and rhType.id = :idRhType "
				+ " AND	rhShiftPeriod.id = :idRhPeriod "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhType", rhType.getId())
				.setParameter("idRhPeriod", rhShiftPeriod.getId())
				.list();
		return rhShifts;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> list(RhPerson rhPerson, RhStatus rhStatus){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " rhPerson.id = :idRhPerson "
				+ " AND rhStatus.id = :idRhStatus "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.list();
		return rhShifts;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> list(RhPerson rhPerson, RhStatus rhStatus, RhShiftPeriod rhShiftPeriod){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " rhPerson.id = :idRhPerson "
				+ " AND rhStatus.id = :idRhStatus "
				+ " AND	rhShiftPeriod.id = :idRhPeriod "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhPeriod", rhShiftPeriod.getId())
				.list();
		return rhShifts;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> list(RhPerson rhPerson, RhStatus rhStatus, Date startDate){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " rhPerson.id = :idRhPerson "
				+ " AND dateReg > :startDate "
				+ " AND rhStatus.id = :idRhStatus "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("startDate", startDate)
				.list();
		return rhShifts;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> list(RhPerson rhPerson, RhType rhType, RhStatus rhStatus, Date startDate) {
		return  openSession().createQuery("FROM RhShift WHERE "
				+ " rhPerson.id = :idRhPerson AND "
				+ " rhStatus.id = :idRhStatus AND "
				+ " dateReg > :startDate AND "
				+ " rhType.id = :idRhType "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("startDate", startDate)
				.setParameter("idRhType", rhType.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listBySis(RhPerson rhPerson, RhType rhType, RhStatus rhStatusSis,  Date startDate) {
		return  openSession().createQuery("FROM RhShift WHERE "
				+ " rhPerson.id = :idRhPerson AND "
				+ " rhStatusSis.id = :idRhStatusSis AND"
				+ " dateReg > :startDate AND "
				+ " rhType.id = :idRhType "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("startDate", startDate)
				.setParameter("idRhType", rhType.getId())
				.setParameter("idRhStatusSis", rhStatusSis.getId())
				.list();
	}
	

	
	@SuppressWarnings("unchecked")
	public List<RhShift> list(RhStatus rhStatus,  RhType rhType){
		return openSession().createQuery("FROM RhShift WHERE "
				+ " rhStatus.id = :idRhStatus "
				+ " and rhType.id = :idRhType ")
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhType", rhType.getId())
				.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> list(RhStatus rhStatusSis, RhStatus rhStatus,  RhType rhType){
		return openSession().createQuery("FROM RhShift WHERE "
				+ " rhStatus.id = :idRhStatus AND"
				+ " rhStatusSis.id = :idRhStatusSis AND"
				+ " rhType.id = :idRhType ")
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhStatusSis", rhStatus.getId())
				.setParameter("idRhType", rhType.getId())
				.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listBySis(RhPerson rhPerson, RhStatus rhStatusSis,  RhType rhType){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " rhPerson.id = :idRhPerson AND "
				+ " rhStatusSis.id = :idRhStatus AND "
				+ " rhType.id = :idRhType "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatusSis.getId())
				.setParameter("idRhType", rhType.getId())
				.list();
		return rhShifts;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listBySis(RhPerson rhPerson, RhStatus rhStatusSis){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " rhPersonResp.id = :idRhPerson AND "
				+ " rhStatusSis.id = :idRhStatus "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatusSis.getId())
				.list();
		return rhShifts;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listBySis2(RhPerson rhPerson, RhStatus rhStatusSis){
		List<RhShift> rhShiftFinal = new ArrayList<RhShift>();
		
		List<RhStaff> lsStaff= openSession().createQuery("FROM RhStaff WHERE rhPerson.id = :idPerson").setParameter("idPerson", rhPerson.getId()).list();				
		
		List<Integer> idAreaPositionManger= new ArrayList<Integer>();
		List<Integer> idArea= new ArrayList<Integer>();
		for (RhStaff rhStaff : lsStaff) {
			idAreaPositionManger.add(rhStaff.getRhAreaPosition().getId());
			idArea.add(rhStaff.getRhAreaPosition().getRhArea().getId());
		}
		
		List<RhAreaPosition> lsAreaPositionManger = openSession().createQuery("FROM RhAreaPosition WHERE id in (:idAreaPositionManger) AND rhType.id = 19 ").setParameterList("idAreaPositionManger", idAreaPositionManger).list();
		
		if(lsAreaPositionManger.size()>=1){//############# es manager
//			List<RhAreaPosition> lsAreaPosition = openSession().createQuery("FROM RhAreaPosition WHERE rhArea.id in (:idArea) AND rhType.id != 19 ").setParameterList("idArea", idArea).list();
			List<RhAreaPosition> lsAreaPosition = openSession().createQuery("FROM RhAreaPosition WHERE rhArea.id in (:idArea) ").setParameterList("idArea", idArea).list();
			
			List<Integer> idAreaPosition= new ArrayList<Integer>();
			for (RhAreaPosition rhAreaPosition : lsAreaPosition) {
				idAreaPosition.add(rhAreaPosition.getId());
			}
			
			List<RhStaff> lsStaff1= openSession().createQuery("FROM RhStaff WHERE rhAreaPosition.id in (:idAreaPosition) AND flagAct=1 AND rhPerson.rhStatus.id=52")
					.setParameterList("idAreaPosition", idAreaPosition).list();
			
			List<RhPerson> lsPersonFinal = new ArrayList<RhPerson>();
			for (RhStaff rhStaff : lsStaff1) {
				lsPersonFinal.add(rhStaff.getRhPerson());
			}
							
			List<RhPerson> lsRhPerson = openSession().createQuery("FROM RhPerson WHERE idManagerShift = :idManagerShift AND rhStatus.id = :idStatus ")
					.setParameter("idManagerShift", rhPerson.getId())
					.setParameter("idStatus", 52)
					.list();
			

			for (RhPerson rhPerson2 : lsRhPerson) {
				  int fPoint=0;
				  for (RhPerson rhPersonF : lsPersonFinal) {
						if(rhPerson2.getId()==rhPersonF.getId()){	
							fPoint=1;							
							break;							
						}
					}
					if(fPoint==0){lsPersonFinal.add(rhPerson2);}
			 }
			
			List<RhShift> lsPersonShift = openSession().createQuery("FROM RhShift WHERE "
					+ " (rhPerson.id = :idRhPerson OR "
					+ " rhPersonMng.id = :idRhPerson) AND "
					+ " rhStatusSis.id = :idRhStatus "
					+ " ORDER BY dateStart DESC ")
					.setParameter("idRhPerson", rhPerson.getId())
					.setParameter("idRhStatus", rhStatusSis.getId())
					.list();
			
			for (RhShift rhShift : lsPersonShift) {
				  int fPoint=0;
				  for (RhPerson rhPersonF : lsPersonFinal) {
						if(rhShift.getRhPerson().getId()==rhPersonF.getId()){	
							fPoint=1;							
							break;							
						}
					}
					if(fPoint==0){lsPersonFinal.add(rhShift.getRhPerson());}
			 }
			
			for (RhPerson rhPerson2 : lsPersonFinal){
				List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
						+ " rhPerson.id = :idRhPerson AND "
						+ " rhStatusSis.id = :idRhStatus "
						+ " ORDER BY dateStart DESC ")
						.setParameter("idRhPerson", rhPerson2.getId())
						.setParameter("idRhStatus", rhStatusSis.getId()).list();
//				rhShiftFinal.add(rhShifts);
				for (RhShift rhShift : rhShifts) {
					rhShiftFinal.add(rhShift);
				}
			}
		}else{//################ solo de el
			List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " rhPerson.id = :idRhPerson AND "
				+ " rhStatusSis.id = :idRhStatus "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatusSis.getId())
				.list();
			rhShiftFinal.addAll(rhShifts);
		}
		
		
		

		//ORIGINAL
//		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
//				+ " (rhPerson.id = :idRhPerson OR "
//				+ " rhPersonMng.id = :idRhPerson) AND "
//				+ " rhStatusSis.id = :idRhStatus "
//				+ " ORDER BY dateStart DESC ")
//				.setParameter("idRhPerson", rhPerson.getId())
//				.setParameter("idRhStatus", rhStatusSis.getId())
//				.list();
//		return rhShifts;
		//END ORIGINAL
		


		return rhShiftFinal;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listBySis2(RhPerson rhPerson, RhStatus rhStatusSis, RhStatus rhStatus){
		List<RhShift> rhShiftFinal = new ArrayList<RhShift>();
		
		List<RhStaff> lsStaff= openSession().createQuery("FROM RhStaff WHERE rhPerson.id = :idPerson").setParameter("idPerson", rhPerson.getId()).list();				
		
		List<Integer> idAreaPositionManger= new ArrayList<Integer>();
		List<Integer> idArea= new ArrayList<Integer>();
		for (RhStaff rhStaff : lsStaff) {
			idAreaPositionManger.add(rhStaff.getRhAreaPosition().getId());
			idArea.add(rhStaff.getRhAreaPosition().getRhArea().getId());
		}
		
		List<RhAreaPosition> lsAreaPositionManger = openSession().createQuery("FROM RhAreaPosition WHERE id in (:idAreaPositionManger) AND rhType.id = 19 ").setParameterList("idAreaPositionManger", idAreaPositionManger).list();
		
		if(lsAreaPositionManger.size()>=1){//############# es manager
//			List<RhAreaPosition> lsAreaPosition = openSession().createQuery("FROM RhAreaPosition WHERE rhArea.id in (:idArea) AND rhType.id != 19 ").setParameterList("idArea", idArea).list();
			List<RhAreaPosition> lsAreaPosition = openSession().createQuery("FROM RhAreaPosition WHERE rhArea.id in (:idArea) ").setParameterList("idArea", idArea).list();
			
			List<Integer> idAreaPosition= new ArrayList<Integer>();
			for (RhAreaPosition rhAreaPosition : lsAreaPosition) {
				idAreaPosition.add(rhAreaPosition.getId());
			}
			
			List<RhStaff> lsStaff1= openSession().createQuery("FROM RhStaff WHERE rhAreaPosition.id in (:idAreaPosition) AND flagAct=1 AND rhPerson.rhStatus.id=52")
					.setParameterList("idAreaPosition", idAreaPosition).list();
			
			List<RhPerson> lsPersonFinal = new ArrayList<RhPerson>();
			for (RhStaff rhStaff : lsStaff1) {
				lsPersonFinal.add(rhStaff.getRhPerson());
			}
							
			List<RhPerson> lsRhPerson = openSession().createQuery("FROM RhPerson WHERE idManagerShift = :idManagerShift AND rhStatus.id = :idStatus ")
					.setParameter("idManagerShift", rhPerson.getId())
					.setParameter("idStatus", 52)
					.list();
			

			for (RhPerson rhPerson2 : lsRhPerson) {
				  int fPoint=0;
				  for (RhPerson rhPersonF : lsPersonFinal) {
						if(rhPerson2.getId()==rhPersonF.getId()){	
							fPoint=1;							
							break;							
						}
					}
					if(fPoint==0){lsPersonFinal.add(rhPerson2);}
			 }
			
			List<RhShift> lsPersonShift = openSession().createQuery("FROM RhShift WHERE "
					+ " (rhPerson.id = :idRhPerson OR "
					+ " rhPersonMng.id = :idRhPerson) AND "
					+ " rhStatus.id = :idRhStatus AND"
					+ " rhStatusSis.id = :idRhStatusSis "
					+ " ORDER BY dateStart DESC ")
					.setParameter("idRhPerson", rhPerson.getId())
					.setParameter("idRhStatusSis", rhStatusSis.getId())
					.setParameter("idRhStatus", rhStatus.getId())
					.list();
			
			for (RhShift rhShift : lsPersonShift) {
				  int fPoint=0;
				  for (RhPerson rhPersonF : lsPersonFinal) {
						if(rhShift.getRhPerson().getId()==rhPersonF.getId()){	
							fPoint=1;							
							break;							
						}
					}
					if(fPoint==0){lsPersonFinal.add(rhShift.getRhPerson());}
			 }
			
			for (RhPerson rhPerson2 : lsPersonFinal){
				List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
						+ " rhPerson.id = :idRhPerson AND "
						+ " rhStatus.id = :idRhStatus AND"
						+ " rhStatusSis.id = :idRhStatusSis "
						+ " ORDER BY dateStart DESC ")
						.setParameter("idRhPerson", rhPerson2.getId())
						.setParameter("idRhStatusSis", rhStatusSis.getId())
						.setParameter("idRhStatus", rhStatus.getId())
						.list();

				for (RhShift rhShift : rhShifts) {
					rhShiftFinal.add(rhShift);
				}
			}
		}else{//################ solo de el
			List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
					+ " rhPerson.id = :idRhPerson AND "
					+ " rhStatus.id = :idRhStatus AND"
					+ " rhStatusSis.id = :idRhStatusSis "
					+ " ORDER BY dateStart DESC ")
					.setParameter("idRhPerson", rhPerson.getId())
					.setParameter("idRhStatusSis", rhStatusSis.getId())
					.setParameter("idRhStatus", rhStatus.getId())
					.list();
			rhShiftFinal.addAll(rhShifts);
		}
		
		
		

		
		//ORIGINAL
//		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
//				+ " (rhPerson.id = :idRhPerson OR "
//				+ " rhPersonMng.id = :idRhPerson) AND "
//				+ " rhStatus.id = :idRhStatus AND"
//				+ " rhStatusSis.id = :idRhStatusSis "
//				+ " ORDER BY dateStart DESC ")
//				.setParameter("idRhPerson", rhPerson.getId())
//				.setParameter("idRhStatusSis", rhStatusSis.getId())
//				.setParameter("idRhStatus", rhStatus.getId())
//				.list();
//		return rhShifts;
		//END ORIGINAL
		
		
		return rhShiftFinal;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listBySisClosed(RhPerson rhPerson, RhStatus rhStatusSis, RhStatus rhStatus ,RhShiftPeriod rhPeriodo){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " (rhPerson.id = :idRhPerson OR "
				+ " rhPersonMng.id = :idRhPerson) AND "
				+ " rhStatus.id = :idRhStatus AND"
				+ " rhStatusSis.id = :idRhStatusSis AND "
				+ " dateStart > :date1 AND "
				+ " dateStart < :date2 "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatusSis", rhStatusSis.getId())
				.setParameter("date1",rhPeriodo.getDateStart())
				.setParameter("date2", rhPeriodo.getDateFinish())
				.setParameter("idRhStatus", rhStatus.getId())
				.list();
		return rhShifts;
	}
	
	
	@SuppressWarnings("unchecked")
	public void shiftChildDelete(RhShift rhShift ,int idRhShift){
		SimpleDateFormat formatt= new SimpleDateFormat("yyyy-MM-dd");
		List<RhShift> lsRhShift= new ArrayList<RhShift>();
		try {
			lsRhShift=openSession().createQuery("FROM RhShift WHERE rhPerson.id = :idRhPerson AND DATE(dateStart) = :date1 ORDER BY  rhType.id DESC")
					.setParameter("idRhPerson", rhShift.getRhPerson().getId())
					.setParameter("date1",formatt.parse(formatt.format(rhShift.getDateStart()))).list();
			
			for (RhShift rhShift2 : lsRhShift) {//*borra todo los detalles
				int resFile=openSession().createQuery("delete from RhFile rhf where rhf.rhShift.id=:idRhshift").setParameter("idRhshift", rhShift2.getId()).executeUpdate();
				int resShiftDet=openSession().createQuery("delete RhShiftDetail rhsd where rhsd.rhShift.id=:idRhshift").setParameter("idRhshift", rhShift2.getId()).executeUpdate();
				int resShiftDet2=openSession().createQuery("delete from RhShiftDetail2 rhsd2 where rhsd2.rhShift.id=:idRhshift").setParameter("idRhshift", rhShift2.getId()).executeUpdate();
				if(rhShift2.getRhType().getId()!=6){
					int results=openSession().createQuery("delete from RhShift rhs where rhs.id=:idRhshift").setParameter("idRhshift", rhShift2.getId()).executeUpdate();
				}
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		int results=openSession().createQuery("delete from RhShift rhs where rhs.rhShiftParent.id=:idRhshift").setParameter("idRhshift", rhShift.getId()).executeUpdate();		
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listBySis(RhPerson rhPerson, RhStatus rhStatusSis,  RhShiftPeriod rhShiftPeriod){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " rhPersonResp.id = :idRhPerson AND "
				+ " rhStatusSis.id = :idRhStatus AND "
				+ " rhShiftPeriod.id = :idRhPeriod  "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatusSis.getId())
				.setParameter("idRhPeriod", rhShiftPeriod.getId())
				.list();
		return rhShifts;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listBySisProcess(RhPerson rhPerson, RhStatus rhStatusSis,  RhShiftPeriod rhShiftPeriod){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " rhPerson.id = :idRhPerson AND "
				+ " rhStatusSis.id = :idRhStatus AND "
				+ " rhShiftPeriod.id = :idRhPeriod  "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatusSis.getId())
				.setParameter("idRhPeriod", rhShiftPeriod.getId())
				.list();
		return rhShifts;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listBySisAll(RhPerson rhPerson, RhStatus rhStatusSis,  RhShiftPeriod rhShiftPeriod){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " (rhPerson.id = :idRhPerson OR "
				+ " rhPersonMng.id = :idRhPerson) AND "
				+ " rhPersonResp.id <> :idRhPerson AND "
				+ " rhStatusSis.id = :idRhStatus AND "
				+ " rhShiftPeriod.id = :idRhPeriod  "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatusSis.getId())
				.setParameter("idRhPeriod", rhShiftPeriod.getId())
				.list();
		return rhShifts;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listBySisAll(RhPerson rhPerson, RhStatus rhStatusSis){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " (rhPerson.id = :idRhPerson OR "
				+ " rhPersonMng.id = :idRhPerson) AND "
				+ " ( rhPersonResp IS NULL OR rhPersonResp.id <> :idRhPerson) AND "
				+ " rhStatusSis.id = :idRhStatus "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatusSis.getId())
				.list();
		return rhShifts;
	}
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listMngBySis(RhPerson rhPerson, RhStatus rhStatusSis,  RhShiftPeriod rhShiftPeriod){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " rhPersonMng.id = :idRhPerson AND "
				+ " rhStatusSis.id = :idRhStatus AND "
				+ " rhShiftPeriod.id = :idRhPeriod  "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatusSis.getId())
				.setParameter("idRhPeriod", rhShiftPeriod.getId())
				.list();
		return rhShifts;
	}
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listBySis(RhPerson rhPerson, RhStatus rhStatusSis,  RhType rhType, RhShiftPeriod rhShiftPeriod){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " rhPerson.id = :idRhPerson AND "
				+ " rhStatusSis.id = :idRhStatus AND "
				+ " rhShiftPeriod.id = :idRhPeriod AND "
				+ " rhType.id = :idRhType "
				+ " ORDER BY dateStart DESC ")
				.setParameter("idRhPerson", rhPerson.getId())
				.setParameter("idRhStatus", rhStatusSis.getId())
				.setParameter("idRhType", rhType.getId())
				.setParameter("idRhPeriod", rhShiftPeriod.getId())
				.list();
		return rhShifts;
	}

	
	@SuppressWarnings("unchecked")
	public List<RhShift> listBySis(RhStatus rhStatusSis, RhType rhType, RhShiftPeriod rhShiftPeriod){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " rhStatusSis.id = :idRhStatus AND "
				+ " rhShiftPeriod.id = :idRhPeriod AND "
				+ " rhType.id = :idRhType ")
				.setParameter("idRhStatus", rhStatusSis.getId())
				.setParameter("idRhType", rhType.getId())
				.setParameter("idRhPeriod", rhShiftPeriod.getId())
				.list();
		return rhShifts;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listBySis(RhStatus rhStatusSis, RhShiftPeriod rhShiftPeriod){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " rhStatusSis.id = :idRhStatus AND "
				+ " rhShiftPeriod.id = :idRhPeriod ")
				.setParameter("idRhStatus", rhStatusSis.getId())
				.setParameter("idRhPeriod", rhShiftPeriod.getId())
				.list();
		return rhShifts;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listBySis(RhStatus rhStatusSis, RhCompany rhCompany){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " rhStatusSis.id = :idRhStatus AND "
				+ " rhPerson.rhCompany.id = :idRhCompany ")
				.setParameter("idRhStatus", rhStatusSis.getId())
				.setParameter("idRhCompany", rhCompany.getId())
				.list();
		return rhShifts;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<EvenUsuario> listEvenUsuario(){
		List<EvenUsuario> lsEvenUsuario = openSession().createQuery("FROM EvenUsuario ").list();
		return lsEvenUsuario;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ValorCosto> lisValorCosto(){
		List<ValorCosto> lsValorCosto = openSession().createQuery("From ValorCosto").list();
		return lsValorCosto;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listBySis(RhStatus rhStatusSis, RhStatus rhStatus, RhCompany rhCompany){
		List<RhShift> rhShifts = openSession().createQuery("FROM RhShift WHERE "
				+ " rhStatusSis.id = :idRhStatusSis AND "
				+ " rhStatus.id = :idRhStatus AND "
				+ " rhPerson.rhCompany.id = :idRhCompany ")
				.setParameter("idRhStatusSis", rhStatusSis.getId())
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhCompany", rhCompany.getId())
				.list();
		return rhShifts;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> list(RhShift rhShiftParent){
		return openSession().createQuery("FROM RhShift WHERE "
				+ " rhShiftParent.id = :idRhShiftParent ")
				.setParameter("idRhShiftParent", rhShiftParent.getId())
				.list();
	}

	
	@SuppressWarnings("unchecked")
	public List<RhShift> list(RhStatus rhStatus, RhShiftPeriod rhShiftPeriod) {
		return  openSession().createQuery("FROM RhShift WHERE "
				+ " rhStatus.id = :idRhStatus AND "
				+ " rhShiftPeriod.id = :idRhShiftPeriod ")
				.setParameter("idRhStatus", rhStatus.getId())
				.setParameter("idRhShiftPeriod", rhShiftPeriod.getId())
				.list();
	}

	
	@SuppressWarnings("unchecked")
	public List<RhShift> list(RhStatus rhStatusSis, RhShiftPeriod rhShiftPeriod, RhPerson rhPerson) {
		return  openSession().createQuery("FROM RhShift WHERE "
				+ " rhStatusSis.id = :idRhStatus AND "
				+ " rhShiftPeriod.id = :idRhShiftPeriod AND "
				+ " rhPerson.id = :idRhPerson ")
				.setParameter("idRhStatus", rhStatusSis.getId())
				.setParameter("idRhShiftPeriod", rhShiftPeriod.getId())
				.setParameter("idRhPerson", rhPerson.getId())
				.list();
	}

	
	@SuppressWarnings("unchecked")
	public List<RhShift> listResponsibleNull(RhStatus rhStatusSis) {
		return  openSession().createQuery("FROM RhShift WHERE "
				+ " rhStatusSis.id = :idRhStatus AND "
				+ " rhPersonResp IS NULL ")
				.setParameter("idRhStatus", rhStatusSis.getId())
				.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listTypeChargeNull(){
		return  openSession().createQuery("FROM RhShift WHERE "
				+ " rhTypeCharge IS NULL ")
				.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listNotProcessed(RhStatus rhStatusSis){
		return  openSession().createQuery("FROM RhShift WHERE "
				+ " processed IS FALSE AND "
				+ " rhStatusSis.id = :idRhStatus ")
				.setParameter("idRhStatus", rhStatusSis.getId())
				.list();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<RhShift> listNotProcessed2(RhStatus rhStatusSis){
		return  openSession().createQuery("FROM RhShift WHERE "
//				+ " processed2 IS FALSE AND "
				+ " rhStatusSis.id = :idRhStatus ")
				.setParameter("idRhStatus", rhStatusSis.getId())
				.list();
	}
//	public RhShift getRhShift(int flagStatus, int idPerson, int type) {
//	return  (RhShift) openSession()
//			.createQuery("from RhShift rs where rs.rhPerson.id = :idPerson "
////					+ " and rs.flagAct = :flagAct "
//					+ " and rs.rhType.code = :type")
//			.setParameter("idPerson", idPerson)
////			.setParameter("flagAct", flagStatus)
//			.setParameter("type", type)
//			.uniqueResult();
//}

//public RhShift getRhShift(int flagStatus, int idPerson, int type, int day, int month, int year) {
//	return  (RhShift) openSession()
//			.createQuery("from RhShift rs where rs.rhPerson.id = :idPerson "
//					+ " and rs.flagAct = :flagAct "
//					+ " and rs.rhType.code = :type "
//					+ " and day(rs.dateStartShift) = :day "
//					+ " and month(rs.dateStartShift) = :month "
//					+ " and year(rs.dateStartShift) = :year ")
//			.setParameter("idPerson", idPerson)
//			.setParameter("flagAct", flagStatus)
//			.setParameter(":type", type)
//			.setParameter("day", day)
//			.setParameter("month", month)
//			.setParameter("year", year)
//			.uniqueResult();
//}

//public RhShift getRhShift(int flagStatus, int idPerson) {
//	return  (RhShift) openSession()
//			.createQuery("from RhShift rs where rs.rhPerson.id = :idPerson "
//					+ " and rs.flagAct = :flagAct ")
//			.setParameter("idPerson", idPerson)
//			.setParameter("flagAct", flagStatus)
//			.uniqueResult();
//}
}
