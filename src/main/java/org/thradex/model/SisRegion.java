package org.thradex.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the sis_regions database table.
 * 
 */
@Entity
@Table(name="sis_regions")
@NamedQuery(name="SisRegion.findAll", query="SELECT s FROM SisRegion s")
public class SisRegion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(length=40)
	private String code;

	@Column(length=40)
	private String name;
	
	@Column(name="TIME_ZONE")
	private String timeZone;

	//bi-directional many-to-one association to SisCity
	@OneToMany(mappedBy="sisRegion")
	private List<SisCity> sisCities;

	//bi-directional many-to-one association to SisCountry
	@ManyToOne
	@JoinColumn(name="ID_COUNTRY")
	private SisCountry sisCountry;
	
	@ManyToOne
	@JoinColumn(name="ID_STATUS")
	private SisStatus sisStatus;

	public SisRegion() {
	}

	public SisStatus getSisStatus() {
		return sisStatus;
	}

	public void setSisStatus(SisStatus sisStatus) {
		this.sisStatus = sisStatus;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SisCity> getSisCities() {
		return this.sisCities;
	}

	public void setSisCities(List<SisCity> sisCities) {
		this.sisCities = sisCities;
	}

	public SisCity addSisCity(SisCity sisCity) {
		getSisCities().add(sisCity);
		sisCity.setSisRegion(this);

		return sisCity;
	}

	public SisCity removeSisCity(SisCity sisCity) {
		getSisCities().remove(sisCity);
		sisCity.setSisRegion(null);

		return sisCity;
	}

	public SisCountry getSisCountry() {
		return this.sisCountry;
	}

	public void setSisCountry(SisCountry sisCountry) {
		this.sisCountry = sisCountry;
	}

}