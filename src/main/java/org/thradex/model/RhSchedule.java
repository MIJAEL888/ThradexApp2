package org.thradex.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="rh_schedule")
@NamedQuery(name="RhSchedule.findAll", query="SELECT r FROM RhSchedule r")
public class RhSchedule implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String name;
	
	private String description;
	
	@Column(name="TOLERANCE_DELAY")
	private int toleranceDelay;
	
	@Column(name="TOLERANCE_ABSENCE")
	private int toleranceAbsence;
	
	@Column(name="TOLERANCE_FINISH")
	private int toleranceFinish;
	
	@Column(name="TOLERANCE_LEAVE")
	private int toleranceLeave;
	
	@Column(name="FLAG_BREAK")
	private int flagBreak;
	
//	@Column(name="FLAG_ACT")
//	private int flagAct;
	
	@Column(name="WORK_HOURS")
	private int workHours;
	
	@Column(name="MIN_COMMISSION")
	private int minCommission;
	
	@ManyToOne
	@JoinColumn(name="ID_TYPE")
	private RhType rhType;
	
//	@OneToMany(mappedBy="rhSchedule", fetch=FetchType.EAGER)
//	private List<RhScheduleDay> rhScheduleDays;
//	
	
	public int getId() {
		return id;
	}

	public int getFlagBreak() {
		return flagBreak;
	}

	public void setFlagBreak(int flagBreak) {
		this.flagBreak = flagBreak;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMinCommission() {
		return minCommission;
	}

	public void setMinCommission(int minCommission) {
		this.minCommission = minCommission;
	}

	public int getWorkHours() {
		return workHours;
	}

	public void setWorkHours(int workHours) {
		this.workHours = workHours;
	}

	public RhType getRhType() {
		return rhType;
	}

	public void setRhType(RhType rhType) {
		this.rhType = rhType;
	}

	public int getToleranceLeave() {
		return toleranceLeave;
	}

	public void setToleranceLeave(int toleranceLeave) {
		this.toleranceLeave = toleranceLeave;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public final int getToleranceDelay() {
		return toleranceDelay;
	}

	public final void setToleranceDelay(int toleranceDelay) {
		this.toleranceDelay = toleranceDelay;
	}

	public final int getToleranceAbsence() {
		return toleranceAbsence;
	}

	public final void setToleranceAbsence(int toleranceAbsence) {
		this.toleranceAbsence = toleranceAbsence;
	}

	public final int getToleranceFinish() {
		return toleranceFinish;
	}

	public final void setToleranceFinish(int toleranceFinish) {
		this.toleranceFinish = toleranceFinish;
	}
	
}
