package org.thradex.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="rh_shift_period")
public class RhShiftPeriod implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
		
	private int year;
	
	private int month; 
	
	private boolean processed;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Column(name="START")
	private Date dateStart;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Column(name="FINISH")
	private Date dateFinish;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Column(name="FIRST_DAY")
	private Date fistDay;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Column(name="LAST_DAY")
	private Date lastDay;
	
	@ManyToOne
	@JoinColumn(name="ID_TYPE")
	private RhType rhType;
	
	@ManyToOne
	@JoinColumn(name="ID_STATUS")
	private RhStatus rhStatus;
	
	@ManyToOne
	@JoinColumn(name="ID_COMPANY")
	private RhCompany rhCompany;
	
	@Transient
	private String label;

	public int getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public String getLabel() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
		return getMonth() + "-" + getYear() + " ("+
				formatter.format(getDateStart()).toString() 
				+ " - " + 
				formatter.format(getDateFinish()).toString() + ")";
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Date getFistDay() {
		return fistDay;
	}

	public void setFistDay(Date fistDay) {
		this.fistDay = fistDay;
	}

	public Date getLastDay() {
		return lastDay;
	}

	public void setLastDay(Date lastDay) {
		this.lastDay = lastDay;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateFinish() {
		return dateFinish;
	}

	public void setDateFinish(Date dateFinish) {
		this.dateFinish = dateFinish;
	}

	public RhType getRhType() {
		return rhType;
	}

	public void setRhType(RhType rhType) {
		this.rhType = rhType;
	}

	public RhStatus getRhStatus() {
		return rhStatus;
	}

	public void setRhStatus(RhStatus rhStatus) {
		this.rhStatus = rhStatus;
	}

	public RhCompany getRhCompany() {
		return rhCompany;
	}

	public void setRhCompany(RhCompany rhCompany) {
		this.rhCompany = rhCompany;
	}
	
}
