package org.thradex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="evenxusuario")
public class EvenUsuario {
	
	@Id
	@Column(name="ID_PERSON_RESP")
	private int idPersonResp;
	
	@Column(name="NombrePersona")
	private String nombrePersona;
	
	@Column(name="PorProcesar")
	private int procesar;
	
	@Column(name="PorJustificar")
	private int justificar;

	public int getIdPersonResp() {
		return idPersonResp;
	}

	public void setIdPersonResp(int idPersonResp) {
		this.idPersonResp = idPersonResp;
	}

	public String getNombrePersona() {
		return nombrePersona;
	}

	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}

	public int getProcesar() {
		return procesar;
	}

	public void setProcesar(int procesar) {
		this.procesar = procesar;
	}

	public int getJustificar() {
		return justificar;
	}

	public void setJustificar(int justificar) {
		this.justificar = justificar;
	}
	
}
