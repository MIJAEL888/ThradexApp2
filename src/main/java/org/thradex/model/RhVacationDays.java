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
import javax.persistence.Transient;

@Entity
@Table(name="rh_vacation_days")
@NamedQuery(name="RhVacationDays.findAll", query="SELECT r from RhVacationDays r")
public class RhVacationDays implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private int period;
	
	private int total;
	
	private int pending;
	
	private int taken;
	
	@ManyToOne
	@JoinColumn(name="ID_PERSON")
	private RhPerson rhPerson;
	
	@Column(name="FLAG_ACT")
	private int flagAct;
	
	@Column(name="FLAG_WARN")
	private int flagWarn;
	
	@Column(name="FLAG_ALERT")
	private int flagAlert;
	
	@Transient
	private RhStaff rhStaffAppro;
	
	@Transient
	private int daysLeft;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPending() {
		return pending;
	}

	public void setPending(int pending) {
		this.pending = pending;
	}

	public int getTaken() {
		return taken;
	}

	public void setTaken(int taken) {
		this.taken = taken;
	}

	public RhPerson getRhPerson() {
		return rhPerson;
	}

	public void setRhPerson(RhPerson rhPerson) {
		this.rhPerson = rhPerson;
	}

	public int getFlagAct() {
		return flagAct;
	}

	public void setFlagAct(int flagAct) {
		this.flagAct = flagAct;
	}

	public int getFlagWarn() {
		return flagWarn;
	}

	public void setFlagWarn(int flagWarn) {
		this.flagWarn = flagWarn;
	}

	public int getFlagAlert() {
		return flagAlert;
	}

	public void setFlagAlert(int flagAlert) {
		this.flagAlert = flagAlert;
	}

	public int getDaysLeft() {
		return daysLeft;
	}

	public void setDaysLeft(int daysLeft) {
		this.daysLeft = daysLeft;
	}

	public RhStaff getRhStaffAppro() {
		return rhStaffAppro;
	}

	public void setRhStaffAppro(RhStaff rhStaffAppro) {
		this.rhStaffAppro = rhStaffAppro;
	}

}
