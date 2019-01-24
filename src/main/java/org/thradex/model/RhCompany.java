package org.thradex.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the rh_company database table.
 * 
 */
@Entity
@Table(name="rh_company")
@NamedQuery(name="RhCompany.findAll", query="SELECT r FROM RhCompany r")
public class RhCompany implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(length=255)
	private String description;

	@Column(nullable=false, length=255)
	private String name;
	
	@Column(length=10)
	private String acron;
	
	@Column(name="TAX_ID")
	private String taxId;
	
	private String address;
	
	private String activity;
	
	private int flag;
	
	@ManyToOne
	@JoinColumn(name="ID_REGION")
	private SisRegion sisRegion;

	//bi-directional many-to-one association to RhArea
	@OneToMany(mappedBy="rhCompany")
	private List<RhArea> rhAreas;

	//bi-directional many-to-one association to RhCompanyType
	@OneToMany(mappedBy="rhCompany")
	private List<RhCompanyType> rhCompanyTypes;

	public RhCompany() {
	}
	public RhCompany(int id) {
		this.id =  id;
	}

	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public SisRegion getSisRegion() {
		return sisRegion;
	}
	public void setSisRegion(SisRegion sisRegion) {
		this.sisRegion = sisRegion;
	}
	public String getAcron() {
		return acron;
	}
	public void setAcron(String acron) {
		this.acron = acron;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RhArea> getRhAreas() {
		return this.rhAreas;
	}

	public void setRhAreas(List<RhArea> rhAreas) {
		this.rhAreas = rhAreas;
	}

	public RhArea addRhArea(RhArea rhArea) {
		getRhAreas().add(rhArea);
		rhArea.setRhCompany(this);

		return rhArea;
	}

	public RhArea removeRhArea(RhArea rhArea) {
		getRhAreas().remove(rhArea);
		rhArea.setRhCompany(null);

		return rhArea;
	}

	public List<RhCompanyType> getRhCompanyTypes() {
		return this.rhCompanyTypes;
	}

	public void setRhCompanyTypes(List<RhCompanyType> rhCompanyTypes) {
		this.rhCompanyTypes = rhCompanyTypes;
	}

	public RhCompanyType addRhCompanyType(RhCompanyType rhCompanyType) {
		getRhCompanyTypes().add(rhCompanyType);
		rhCompanyType.setRhCompany(this);

		return rhCompanyType;
	}

	public RhCompanyType removeRhCompanyType(RhCompanyType rhCompanyType) {
		getRhCompanyTypes().remove(rhCompanyType);
		rhCompanyType.setRhCompany(null);

		return rhCompanyType;
	}

}