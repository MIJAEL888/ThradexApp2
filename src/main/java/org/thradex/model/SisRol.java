package org.thradex.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sis_rol database table.
 * 
 */
@Entity
@Table(name="sis_rol")
@NamedQuery(name="SisRol.findAll", query="SELECT s FROM SisRol s")
public class SisRol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_ROL", unique=true, nullable=false)
	private int idRol;

	private String name;
	
	@Column(length=128)
	private String description;

	@Column(name="ID_APLICACION", nullable=false)
	private int idAplicacion;

	@Column(name="NAME_ROL", nullable=false, length=64)
	private String nameRol;

	//bi-directional many-to-one association to SisUserRol
//	@OneToMany(mappedBy="sisRol")
//	private List<SisUserRol> sisUserRols;

	public SisRol() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIdRol() {
		return this.idRol;
	}

	public void setIdRol(int idRol) {
		this.idRol = idRol;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIdAplicacion() {
		return this.idAplicacion;
	}

	public void setIdAplicacion(int idAplicacion) {
		this.idAplicacion = idAplicacion;
	}

	public String getNameRol() {
		return this.nameRol;
	}

	public void setNameRol(String nameRol) {
		this.nameRol = nameRol;
	}
//
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
//		sisUserRol.setSisRol(this);
//
//		return sisUserRol;
//	}
//
//	public SisUserRol removeSisUserRol(SisUserRol sisUserRol) {
//		getSisUserRols().remove(sisUserRol);
//		sisUserRol.setSisRol(null);
//
//		return sisUserRol;
//	}

}