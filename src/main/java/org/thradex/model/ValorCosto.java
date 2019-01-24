package org.thradex.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="valorcosto")
public class ValorCosto {
	
	@Id
	private int id;
	private String tipo;
	private double efectivo;
	private double reportado;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public double getEfectivo() {
		return efectivo;
	}
	public void setEfectivo(double efectivo) {
		this.efectivo = efectivo;
	}
	public double getReportado() {
		return reportado;
	}
	public void setReportado(double reportado) {
		this.reportado = reportado;
	}
	
	

}
