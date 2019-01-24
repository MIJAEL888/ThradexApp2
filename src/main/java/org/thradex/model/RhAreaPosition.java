package org.thradex.model;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.Where;
import org.hibernate.annotations.WhereJoinTable;

import java.util.List;


/**
 * The persistent class for the rh_area_position database table.
 * 
 */
@Entity
@Table(name="rh_area_position")
@NamedQuery(name="RhAreaPosition.findAll", query="SELECT r FROM RhAreaPosition r")
public class RhAreaPosition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false, name="ID_AREA_POS")
	private int id;

	private String name;
	
//	private String foco;

	//bi-directional many-to-one association to RhArea
	@ManyToOne
	@JoinColumn(name="ID_AREA")
	private RhArea rhArea;

	//bi-directional many-to-one association to RhPosition
//	@ManyToOne
//	@JoinColumn(name="ID_POSITION")
//	private RhPosition rhPosition;

	@ManyToOne
	@JoinColumn(name="ID_STATUS")
	private RhStatus rhStatus;
	
	@ManyToOne
	@JoinColumn(name="ID_TYPE")
	private RhType rhType;	
	
	@ManyToOne
	@JoinColumn(name="ID_TYPE_SPZ")
	private RhType rhTypeSpz;	
	
	//bi-directional many-to-one association to RhStaff
	@OneToMany(mappedBy="rhAreaPosition")
	private List<RhStaff> rhStaffs;
	
	@ManyToMany(targetEntity=SisRol.class)
	@JoinTable(name = "sis_areapos_rol", 
		joinColumns = { @JoinColumn(name = "ID_AREA_POS") }, 
		inverseJoinColumns = { @JoinColumn(name = "ID_ROL") })
	@WhereJoinTable(clause="ACT_CUENTA = 1")
	@Where(clause="ID_APLICACION=2")
	private List<SisRol> listRol;
	
	@OneToMany(mappedBy="rhAreaPosition")
	private List<RhPositionProps> listProps;
	
	@Transient
	private List<String> listRolPos;
	
	public RhAreaPosition() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public RhType getRhTypeSpz() {
		return rhTypeSpz;
	}

	public void setRhTypeSpz(RhType rhTypeSpz) {
		this.rhTypeSpz = rhTypeSpz;
	}

	public List<RhPositionProps> getListProps() {
		return listProps;
	}

	public void setListProps(List<RhPositionProps> listProps) {
		this.listProps = listProps;
	}

	public List<SisRol> getListRol() {
		return listRol;
	}

	public void setListRol(List<SisRol> listRol) {
		this.listRol = listRol;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RhArea getRhArea() {
		return this.rhArea;
	}

	public void setRhArea(RhArea rhArea) {
		this.rhArea = rhArea;
	}

	public List<RhStaff> getRhStaffs() {
		return this.rhStaffs;
	}

	public void setRhStaffs(List<RhStaff> rhStaffs) {
		this.rhStaffs = rhStaffs;
	}

	public List<String> getListRolPos() {
		return listRolPos;
	}

	public void setListRolPos(List<String> listRolPos) {
		this.listRolPos = listRolPos;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}