package org.thradex.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the rh_type database table.
 * 
 */
@Entity
@Table(name="rh_status")
@NamedQuery(name="RhStatus.findAll", query="SELECT r FROM RhStatus r")
public class RhStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(length=45)
	private String name;
	
	@Column(name="CATEG")
	private String category;

	private int code;
	
	//bi-directional many-to-one association to RhCompanyType
//	@OneToMany(mappedBy="rhType")
//	private List<RhCompanyType> rhCompanyTypes;

	
	public RhStatus() {
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

//	public List<RhCompanyType> getRhCompanyTypes() {
//		return this.rhCompanyTypes;
//	}
//
//	public void setRhCompanyTypes(List<RhCompanyType> rhCompanyTypes) {
//		this.rhCompanyTypes = rhCompanyTypes;
//	}
//
//	public RhCompanyType addRhCompanyType(RhCompanyType rhCompanyType) {
//		getRhCompanyTypes().add(rhCompanyType);
//		rhCompanyType.setRhType(this);
//
//		return rhCompanyType;
//	}
//
//	public RhCompanyType removeRhCompanyType(RhCompanyType rhCompanyType) {
//		getRhCompanyTypes().remove(rhCompanyType);
//		rhCompanyType.setRhType(null);
//
//		return rhCompanyType;
//	}

}