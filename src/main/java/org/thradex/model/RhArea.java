package org.thradex.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the rh_area database table.
 * 
 */
@Entity
@Table(name="rh_area")
@NamedQuery(name="RhArea.findAll", query="SELECT r FROM RhArea r")
public class RhArea implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(length=128)
	private String correo;

	@Column(length=255)
	private String descripcion;

//	@Column(name="FLAG_ACT")
//	private int flagAct;
	
	@Column(name="FLAG_EMAIL")
	private int flagEmail;

	@Column(length=128)
	private String name;

	@Column(length=128)
	private int level;
	
	//bi-directional many-to-one association to RhCompany
	@ManyToOne
	@JoinColumn(name="ID_COMPANY")
	private RhCompany rhCompany;

	@ManyToOne
	@JoinColumn(name="ID_PARENT")
	private RhArea areaParent;
	
	@ManyToOne
	@JoinColumn(name="ID_STATUS")
	private RhStatus rhStatus;
	
	@ManyToOne
	@JoinColumn(name="ID_TYPE")
	private RhType rhType;		
	
	@Transient
	private boolean flagTT;
	
	@Transient
	private List<RhStaff> listStaff;
	
	public RhArea() {
	}
	public RhArea(int id) {
		this.id = id;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public List<RhStaff> getListStaff() {
		return listStaff;
	}
	public void setListStaff(List<RhStaff> listStaff) {
		this.listStaff = listStaff;
	}
	public boolean isFlagTT() {
		return flagTT;
	}

	public void setFlagTT(boolean flagTT) {
		this.flagTT = flagTT;
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
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getFlagEmail() {
		return this.flagEmail;
	}

	public void setFlagEmail(int flagEmail) {
		this.flagEmail = flagEmail;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RhCompany getRhCompany() {
		return this.rhCompany;
	}

	public void setRhCompany(RhCompany rhCompany) {
		this.rhCompany = rhCompany;
	}

	public RhArea getAreaParent() {
		return areaParent;
	}

	public void setAreaParent(RhArea areaPArent) {
		this.areaParent = areaPArent;
	}

}