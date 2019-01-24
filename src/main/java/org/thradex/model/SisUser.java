package org.thradex.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the sis_user database table.
 * 
 */
@Entity
@Table(name="sis_user")
@NamedQuery(name="SisUser.findAll", query="SELECT s FROM SisUser s")
public class SisUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_USER", unique=true, nullable=false)
	private int id;

	@Column(name="ACT_CUENTA")
	private int actCuenta;

	@Column(length=2)
	private String autoriza;

	private int estado;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_REG")
	private Date fechaReg;

	@Column(name="PASS_TEMP", length=100)
	private String passTemp;

	@Column(length=50)
	private String password;

	@Column(nullable=false, length=50)
	private String username;
	
	private String hashmap;
	
	@Column(name="DATE_ACT")
	private Date dateAct;
	
	@Column(name="TYPE_HASH")
	private int typeHash;

	//bi-directional many-to-one association to RhPerson
	@ManyToOne
	@JoinColumn(name="ID_PERSON")
	private RhPerson rhPerson;

	//bi-directional many-to-one association to RhPerson
	
	//bi-directional many-to-one association to SisUserRol
//	@OneToMany(mappedBy="sisUser")
//	private List<SisUserRol> sisUserRols;
	

//	@ManyToMany(targetEntity=SisRol.class,fetch=FetchType.EAGER)
//	@JoinTable(name = "sis_user_rol", joinColumns = { @JoinColumn(name = "ID_USER") }, inverseJoinColumns = { @JoinColumn(name = "ID_ROL") })
//	private List<SisRol> listRol;
	@Transient
	private Set<SisRol> listRol;
	
	@Transient
	private String lastPass;
	
	@Transient
	private String validPass;
	
	@Transient
	private boolean simulating;
	
	@Transient
	private int idRealUser;
	
	@Transient
	private int idCompany;
	
	@Transient
	private int idArea;
	
	@Transient
	private int idStaff;
	
	
	public SisUser() {
	}

	public SisUser(int id) {
		super();
		this.id = id;
	}

	public boolean isSimulating() {
		return simulating;
	}

	public void setSimulating(boolean simulating) {
		this.simulating = simulating;
	}

	public int getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(int idCompany) {
		this.idCompany = idCompany;
	}

	public int getIdArea() {
		return idArea;
	}

	public void setIdArea(int idArea) {
		this.idArea = idArea;
	}

	public int getIdStaff() {
		return idStaff;
	}

	public void setIdStaff(int idStaff) {
		this.idStaff = idStaff;
	}

	public String getHashmap() {
		return hashmap;
	}

	public void setHashmap(String hashmap) {
		this.hashmap = hashmap;
	}

	public Date getDateAct() {
		return dateAct;
	}

	public void setDateAct(Date dateAct) {
		this.dateAct = dateAct;
	}

	public int getTypeHash() {
		return typeHash;
	}

	public void setTypeHash(int typeHash) {
		this.typeHash = typeHash;
	}

	public int getIdRealUser() {
		return idRealUser;
	}

	public void setIdRealUser(int idRealUser) {
		this.idRealUser = idRealUser;
	}

	public String getLastPass() {
		return lastPass;
	}

	public void setLastPass(String lastPass) {
		this.lastPass = lastPass;
	}

	public String getValidPass() {
		return validPass;
	}

	public void setValidPass(String validPass) {
		this.validPass = validPass;
	}

	public Set<SisRol> getListRol() {
		return listRol;
	}

	public void setListRol(Set<SisRol> listRol) {
		this.listRol = listRol;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getActCuenta() {
		return this.actCuenta;
	}

	public void setActCuenta(int actCuenta) {
		this.actCuenta = actCuenta;
	}

	public String getAutoriza() {
		return this.autoriza;
	}

	public void setAutoriza(String autoriza) {
		this.autoriza = autoriza;
	}

	public int getEstado() {
		return this.estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public Date getFechaReg() {
		return this.fechaReg;
	}

	public void setFechaReg(Date fechaReg) {
		this.fechaReg = fechaReg;
	}

	public String getPassTemp() {
		return this.passTemp;
	}

	public void setPassTemp(String passTemp) {
		this.passTemp = passTemp;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public RhPerson getRhPerson() {
		return this.rhPerson;
	}

	public void setRhPerson(RhPerson rhPerson) {
		this.rhPerson = rhPerson;
	}

//	public List<SisUserRol> getSisUserRols() {
//		return this.sisUserRols;
//	}
//
//	public void setSisUserRols(List<SisUserRol> sisUserRols) {
//		this.sisUserRols = sisUserRols;
//	}
//
//	public SisUserRol addSisUserRol(SisUserRol sisUserRol) {
//		getSisUserRols().add(sisUserRol);
//		sisUserRol.setSisUser(this);
//
//		return sisUserRol;
//	}
//
//	public SisUserRol removeSisUserRol(SisUserRol sisUserRol) {
//		getSisUserRols().remove(sisUserRol);
//		sisUserRol.setSisUser(null);
//
//		return sisUserRol;
//	}

}