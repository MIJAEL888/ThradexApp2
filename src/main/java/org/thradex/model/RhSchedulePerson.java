package org.thradex.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="rh_schedule_person")
@NamedQuery(name="RhSchedulePerson.findAll", query="SELECT r FROM RhSchedulePerson r")
public class RhSchedulePerson implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private int compensation;
	
	@Column(name="FLAG_CHECK_IP")
	private int flagCheckIp;
	
	@ManyToOne
	@JoinColumn(name="ID_RH_PERSON")
	private RhPerson rhPerson;
	
	@ManyToOne
	@JoinColumn(name="ID_RH_SCHEDULE")
	private RhSchedule rhSchedule;

	@ManyToOne
	@JoinColumn(name="ID_STATUS")
	private RhStatus rhStatus;
	
	@ManyToOne
	@JoinColumn(name="ID_REGION")
	private SisRegion sisRegion;
	
	@Transient
	private List<RhScheduleDay> rhScheduleDays;
	
	public List<RhScheduleDay> getRhScheduleDays() {
		return rhScheduleDays;
	}

	public SisRegion getSisRegion() {
		return sisRegion;
	}

	public void setSisRegion(SisRegion sisRegion) {
		this.sisRegion = sisRegion;
	}

	public void setRhScheduleDays(List<RhScheduleDay> rhScheduleDays) {
		this.rhScheduleDays = rhScheduleDays;
	}

	public int getFlagCheckIp() {
		return flagCheckIp;
	}

	public void setFlagCheckIp(int flagCheckIp) {
		this.flagCheckIp = flagCheckIp;
	}

	public int getCompensation() {
		return compensation;
	}

	public void setCompensation(int compensation) {
		this.compensation = compensation;
	}

	public RhStatus getRhStatus() {
		return rhStatus;
	}

	public void setRhStatus(RhStatus rhStatus) {
		this.rhStatus = rhStatus;
	}

	public final int getId() {
		return id;
	}

	public final void setId(int id) {
		this.id = id;
	}

	public final RhPerson getRhPerson() {
		return rhPerson;
	}

	public final void setRhPerson(RhPerson rhPerson) {
		this.rhPerson = rhPerson;
	}

	public final RhSchedule getRhSchedule() {
		return rhSchedule;
	}

	public final void setRhSchedule(RhSchedule rhSchedule) {
		this.rhSchedule = rhSchedule;
	}
		
}
