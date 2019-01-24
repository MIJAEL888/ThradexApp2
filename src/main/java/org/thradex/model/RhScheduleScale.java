package org.thradex.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="rh_schedule_scale")
@NamedQuery(name="RhScheduleScale.findAll", query="SELECT r from RhScheduleScale r")
public class RhScheduleScale implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String comment;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATE")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date date;
	
	@ManyToOne
	@JoinColumn(name="ID_STATUS")
	private RhStatus rhStatus;

	@ManyToOne
	@JoinColumn(name="ID_SCHEDULE_PERSON")
	private RhSchedulePerson rhSchedulePerson;
	
	@ManyToOne
	@JoinColumn(name="ID_SCHEDULE_DAY")
	private RhScheduleDay rhScheduleDay;

	public RhScheduleDay getRhScheduleDay() {
		return rhScheduleDay;
	}

	public void setRhScheduleDay(RhScheduleDay rhScheduleDay) {
		this.rhScheduleDay = rhScheduleDay;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public final Date getDate() {
		return date;
	}

	public final void setDate(Date date) {
		this.date = date;
	}

	public RhSchedulePerson getRhSchedulePerson() {
		return rhSchedulePerson;
	}

	public void setRhSchedulePerson(RhSchedulePerson rhSchedulePerson) {
		this.rhSchedulePerson = rhSchedulePerson;
	}
	
}
