package org.thradex.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the rh_staff database table.
 * 
 */
@Entity
@Table(name="rh_staff")
@NamedQuery(name="RhStaff.findAll", query="SELECT r FROM RhStaff r")
public class RhStaff implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;

	private String foco;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_END")
	private Date dateEnd;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_REG")
	private Date dateReg;
	
	@ManyToOne
	@JoinColumn(name="ID_STATUS")
	private RhStatus rhStatus;
	
	@ManyToOne
	@JoinColumn(name="ID_TYPE")
	private RhType rhType;
	
	@Column(name="FLAG_ACT")
	private int flagAct;
//	
//	@Column(name="FLAG_MAIN")
//	private int flagMain;
	//bi-directional many-to-one association to RhAreaPosition
	@ManyToOne
	@JoinColumn(name="ID_AREA_POSITION")
	private RhAreaPosition rhAreaPosition;

	//bi-directional many-to-one association to RhPerson
	@ManyToOne
	@JoinColumn(name="ID_PERSON")
	private RhPerson rhPerson;

	//bi-directional many-to-one association to RhStatusStaff
//	@ManyToOne
//	@JoinColumn(name="ID_STATUS", nullable=false)
//	private RhStatusStaff rhStatusStaff;

	
	public RhAreaPosition getRhAreaPosition() {
		return rhAreaPosition;
	}

	public void setRhAreaPosition(RhAreaPosition rhAreaPosition) {
		this.rhAreaPosition = rhAreaPosition;
	}

	public RhStaff() {
	}
	
	public RhStaff(int id) {
		this.id =  id;
	}
	
	public RhStatus getRhStatus() {
		return rhStatus;
	}

	public void setRhStatus(RhStatus rhStatus) {
		this.rhStatus = rhStatus;
	}

	public RhType getRhType() {
		return rhType;
	}

	public void setRhType(RhType rhType) {
		this.rhType = rhType;
	}

	public String getFoco() {
		return foco;
	}

	public void setFoco(String foco) {
		this.foco = foco;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateEnd() {
		return this.dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public Date getDateReg() {
		return this.dateReg;
	}

	public void setDateReg(Date dateReg) {
		this.dateReg = dateReg;
	}


	public RhPerson getRhPerson() {
		return this.rhPerson;
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

	
	
}