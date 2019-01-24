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
@Table(name="rh_schedule_day")
@NamedQuery(name="RhScheduleDay.findAll", query="SELECT r FROM RhScheduleDay r")
public class RhScheduleDay implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String name;
	
	@Column(name="START_HOUR")
	private int startHour;
	
	@Column(name="START_MIN")
	private int startMin;
	
	@Column(name="FINISH_HOUR")
	private int finishHour;
	
	@Column(name="FINISH_MIN")
	private int finishMin;
	
	@Column(name="TOTAL_HOURS")
	private int totalHours;
	
	@Column(name="WORKED_HOURS")
	private int workedHours;
	
	@Column(name="FLAG_BREAK")
	private int flagBreak;
	
	private int day;
	
	private String color;
	
	@ManyToOne
	@JoinColumn(name="ID_RH_SCHEDULE")
	private RhSchedule rhSchedule;
	
	@Transient
	private String startTime;
	
	@Transient
	private String endTime;
	
	@Transient
	private String duration;

	public RhScheduleDay() {
		super();
	}

	public RhScheduleDay(int id) {
		super();
		this.id = id;
	}

	public int getFlagBreak() {
		return flagBreak;
	}

	public void setFlagBreak(int flagBreak) {
		this.flagBreak = flagBreak;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDuration() {
		Double d = new Double( this.totalHours);
		return  d.intValue()+ ":00";
	}

	public String getStartTime() {
		String sh =  this.startHour+"";
		if (sh.length() == 1) 
			sh = "0"+sh;
		String sm =  this.startMin+"";
		if (sm.length() == 1) 
			sm = "0"+sm;
		return sh + ":" + sm;
	}
	public String getEndTime() {
		String fh =  this.finishHour+"";
		if (fh.length() == 1) 
			fh = "0"+fh;
		String fm =  this.finishMin+"";
		if (fm.length() == 1) 
			fm= "0"+fm;
		return fh + ":" + fm;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(int totalHours) {
		this.totalHours = totalHours;
	}

	public int getWorkedHours() {
		return workedHours;
	}

	public void setWorkedHours(int workedHours) {
		this.workedHours = workedHours;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public int getStartMin() {
		return startMin;
	}

	public void setStartMin(int startMin) {
		this.startMin = startMin;
	}

	public int getFinishHour() {
		return finishHour;
	}

	public void setFinishHour(int finishHour) {
		this.finishHour = finishHour;
	}

	public int getFinishMin() {
		return finishMin;
	}

	public void setFinishMin(int finishMin) {
		this.finishMin = finishMin;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public RhSchedule getRhSchedule() {
		return rhSchedule;
	}

	public void setRhSchedule(RhSchedule rhSchedule) {
		this.rhSchedule = rhSchedule;
	}
}
