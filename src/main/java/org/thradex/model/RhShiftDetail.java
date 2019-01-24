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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="rh_shift_detail")
public class RhShiftDetail implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	private int id;
		
	private int total;
	
	@Column(name="TOTAL_RATE")
	private int totalRate;
	
	private String comment;
	
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	@Column(name="DATE_REG")
	private Date dateReg;
	
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	@Column(name="TIME_START")
	private Date timeStart;
	
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	@Column(name="TIME_FINISH")
	private Date timeFinish;
	
	@ManyToOne
	@JoinColumn(name="ID_SHIFT")
	private RhShift rhShift;
	
	@ManyToOne
	@JoinColumn(name="ID_TYPE")
	private RhType rhType;
	
	@ManyToOne
	@JoinColumn(name="ID_TYPE_RATE")
	private RhType rhTypeRate;
	
	@ManyToOne
	@JoinColumn(name="ID_TYPE_DAY")
	private RhType rhTypeDay;
	
	@ManyToOne
	@JoinColumn(name="ID_TYPE_SHIFT_DAY")
	private RhType rhTypeShiftDay;
	
	@Transient
	private Double totalH;
	
	@Transient
	private Double totalRateH;
	
	
	public Double getTotalH() {
		return Math.round((this.total/60.0) * 100) / 100.0;
	}

	public void setTotalH(Double totalH) {
		this.totalH = totalH;
	}

	public Double getTotalRateH() {
		return Math.round((this.totalRate/60.0) * 100) / 100.0;
	}

	public void setTotalRateH(Double totalRateH) {
		this.totalRateH = totalRateH;
	}

	public Date getDateReg() {
		return dateReg;
	}

	public void setDateReg(Date dateReg) {
		this.dateReg = dateReg;
	}

	public RhType getRhTypeDay() {
		return rhTypeDay;
	}

	public void setRhTypeDay(RhType rhTypeDay) {
		this.rhTypeDay = rhTypeDay;
	}

	public RhType getRhTypeShiftDay() {
		return rhTypeShiftDay;
	}

	public void setRhTypeShiftDay(RhType rhTypeShiftDay) {
		this.rhTypeShiftDay = rhTypeShiftDay;
	}

	public int getTotalRate() {
		return totalRate;
	}

	public void setTotalRate(int totalRate) {
		this.totalRate = totalRate;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	public Date getTimeFinish() {
		return timeFinish;
	}

	public void setTimeFinish(Date timeFinish) {
		this.timeFinish = timeFinish;
	}

	public RhType getRhTypeRate() {
		return rhTypeRate;
	}

	public void setRhTypeRate(RhType rhTypeRate) {
		this.rhTypeRate = rhTypeRate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RhShift getRhShift() {
		return rhShift;
	}

	public void setRhShift(RhShift rhShift) {
		this.rhShift = rhShift;
	}

	public RhType getRhType() {
		return rhType;
	}

	public void setRhType(RhType rhType) {
		this.rhType = rhType;
	}
}
