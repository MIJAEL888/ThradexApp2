package org.thradex.model;

import java.io.Serializable;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * The persistent class for the rh_person database table.
 * 
 */
/**
 * @author USUARIO
 *
 */
@Entity
@Table(name="rh_person")
@NamedQuery(name="RhPerson.findAll", query="SELECT r FROM RhPerson r")
public class RhPerson implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(length=200)
	private String email;
	
	@Column(name="PERSONAL_EMAIL")
	private String personalEmail;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_REG")
	private Date dateReg;

	@ManyToOne
	@JoinColumn(name="ID_STATUS")
	private RhStatus rhStatus;
	
	@ManyToOne
	@JoinColumn(name="ID_TYPE")
	private RhType rhType;

	@Column(name="MIDLE_NAME", length=45)
	private String midleName;

	@Column(length=15)
	private String mobile;

	@Column(nullable=false, length=100)
	private String name;

	@Column(name="NUM_DOC", length=45)
	private String numDoc;

	@Column(nullable=false, length=45)
	private String surname;

	@Column(length=15)
	private String telefono;

	@Column(name="WORK_PHONE", length=45)
	private String workPhone;
	
	private String skype;
	
	@Column(name="ID_TMW")
	private Integer idTmw;
	
	@Column(name="TITLE_TMW")
	private String titleTmw;
	
	@Column(name="DATE_BIRTH")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date dateBirth;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Column(name="START_CONTRACT")
	private Date startContract;
	
	@ManyToOne
	@JoinColumn(name="ID_TYPE_DOC", nullable=true)
	private RhType rhTypeDoc;
	
	@ManyToOne
	@JoinColumn(name="ID_COMPANY")
	private RhCompany rhCompany;
	
//	@ManyToOne
//	@JoinColumn(name="ID_REGION")
//	private SisRegion sisRegion;

	//bi-directional many-to-one association to RhStaff
//	@OneToMany(mappedBy="rhPerson",fetch=FetchType.EAGER)
//	@Where(clause=" FLAG_ACT = 1 AND FLAG_MAIN = 1 ")
	@OneToMany(mappedBy="rhPerson")
	private List<RhStaff> listStaff;

	//bi-directional many-to-one association to SisUser
	@OneToMany(mappedBy="rhPerson")
	private List<SisUser> listUser;

	@Column(name="ID_MANAGER_SHIFT")
	private Integer idManagerShift;
	
	@Transient
	private String fullname;
	
	@Transient
	private String shortname;
	
	@Transient
	private Map<String, Object> detail;
	
	@Transient
	private List<RhShift> rhShifts;
	
	@Transient
	private List<RhShiftDetail> rhShiftDetails;
	
	@Transient
	private RhShiftPeriod rhShiftPeriod;
	
	public RhPerson() {
	
	}

	public Map<String, Object> getDetail() {
		return detail;
	}

	public List<RhShiftDetail> getRhShiftDetails() {
		return rhShiftDetails;
	}

	public void setRhShiftDetails(List<RhShiftDetail> rhShiftDetails) {
		this.rhShiftDetails = rhShiftDetails;
	}

	public RhShiftPeriod getRhShiftPeriod() {
		return rhShiftPeriod;
	}

	public void setRhShiftPeriod(RhShiftPeriod rhShiftPeriod) {
		this.rhShiftPeriod = rhShiftPeriod;
	}

	public void setDetail(Map<String, Object> detail) {
		this.detail = detail;
	}

	public List<RhShift> getRhShifts() {
		return rhShifts;
	}

	public void setRhShifts(List<RhShift> rhShifts) {
		this.rhShifts = rhShifts;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getStartContract() {
		return startContract;
	}

	public void setStartContract(Date startContract) {
		this.startContract = startContract;
	}

	public String getShortname() {
		return name.substring(0, 1) + surname.substring(0, 1);
	}

	public String getPersonalEmail() {
		return personalEmail;
	}

	public RhStatus getRhStatus() {
		return rhStatus;
	}

	public void setRhStatus(RhStatus rhStatus) {
		this.rhStatus = rhStatus;
	}

	public RhType getRhTypeDoc() {
		return rhTypeDoc;
	}

	public void setRhTypeDoc(RhType rhTypeDoc) {
		this.rhTypeDoc = rhTypeDoc;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public RhCompany getRhCompany() {
		return rhCompany;
	}

	public void setRhCompany(RhCompany rhCompany) {
		this.rhCompany = rhCompany;
	}

	public Integer getIdTmw() {
		return idTmw;
	}

	public void setIdTmw(Integer idTmw) {
		this.idTmw = idTmw;
	}

	public String getTitleTmw() {
		return titleTmw;
	}

	public void setTitleTmw(String titleTmw) {
		this.titleTmw = titleTmw;
	}

	public void setPersonalEmail(String personalEmail) {
		this.personalEmail = personalEmail;
	}

	public String getNumDoc() {
		return numDoc;
	}

	public void setNumDoc(String numDoc) {
		this.numDoc = numDoc;
	}

	public Date getDateBirth() {
		return dateBirth;
	}

	public void setDateBirth(Date dateBirth) {
		this.dateBirth = dateBirth;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public RhType getRhType() {
		return rhType;
	}

	public void setRhType(RhType rhType) {
		this.rhType = rhType;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getSkype() {
		return skype;
	}

	public void setSkype(String skype) {
		this.skype = skype;
	}

	public String getFullname() {
		return name + " " + surname ;
	}

	public void setFullname(String fullname) {
		this.fullname += fullname;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateReg() {
		return this.dateReg;
	}

	public void setDateReg(Date dateReg) {
		this.dateReg = dateReg;
	}

	public String getMidleName() {
		return this.midleName;
	}

	public void setMidleName(String midleName) {
		this.midleName = midleName;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.fullname += name;
		this.name = name;
		
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.fullname += surname;
		this.surname = surname;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public List<RhStaff> getListStaff() {
		return listStaff;
	}

	public void setListStaff(List<RhStaff> listStaff) {
		this.listStaff = listStaff;
	}

	public List<SisUser> getListUser() {
		return listUser;
	}

	public void setListUser(List<SisUser> listUser) {
		this.listUser = listUser;
	}

	
	public Integer getIdManagerShift() {
		return idManagerShift;
	}

	public void setIdManagerShift(Integer idManagerShift) {
		this.idManagerShift = idManagerShift;
	}
	
	
}