package org.thradex.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the rh_company_type database table.
 * 
 */
@Entity
@Table(name="rh_company_type")
@NamedQuery(name="RhCompanyType.findAll", query="SELECT r FROM RhCompanyType r")
public class RhCompanyType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;

	//bi-directional many-to-one association to RhType
	@ManyToOne
	@JoinColumn(name="ID_TYPE", nullable=false)
	private RhType rhType;

	//bi-directional many-to-one association to RhCompany
	@ManyToOne
	@JoinColumn(name="ID_COMPANY", nullable=false)
	private RhCompany rhCompany;

	public RhCompanyType() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RhType getRhType() {
		return this.rhType;
	}

	public void setRhType(RhType rhType) {
		this.rhType = rhType;
	}

	public RhCompany getRhCompany() {
		return this.rhCompany;
	}

	public void setRhCompany(RhCompany rhCompany) {
		this.rhCompany = rhCompany;
	}

}