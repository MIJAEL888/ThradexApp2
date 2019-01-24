package org.thradex.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sis_cities database table.
 * 
 */
@Entity
@Table(name="sis_cities")
@NamedQuery(name="SisCity.findAll", query="SELECT s FROM SisCity s")
public class SisCity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(length=20)
	private String latitude;

	@Column(length=20)
	private String longitude;

	@Column(length=60)
	private String name;

	//bi-directional many-to-one association to SisRegion
	@ManyToOne
	@JoinColumn(name="ID_REGION")
	private SisRegion sisRegion;

	public SisCity() {
	}
	public SisCity(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLatitude() {
		return this.latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SisRegion getSisRegion() {
		return this.sisRegion;
	}

	public void setSisRegion(SisRegion sisRegion) {
		this.sisRegion = sisRegion;
	}

}