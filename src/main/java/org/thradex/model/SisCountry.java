package org.thradex.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sis_countries database table.
 * 
 */
@Entity
@Table(name="sis_countries")
@NamedQuery(name="SisCountry.findAll", query="SELECT s FROM SisCountry s")
public class SisCountry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(length=40)
	private String capital;

	@Column(length=40)
	private String name;

	@Column(length=40)
	private String nationality;


	public SisCountry() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCapital() {
		return this.capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNationality() {
		return this.nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

}