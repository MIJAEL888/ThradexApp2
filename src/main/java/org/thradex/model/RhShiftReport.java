package org.thradex.model;

import java.io.Serializable;

/**
 * The persistent class for the tt_types database table.
 * 
 */
/**
 * @author developer
 *
 */
public class RhShiftReport implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer idRhCompany;

	private Integer idRhPerson;
	
	private Integer idRhShiftPeriod;

	public RhShiftReport() {
	}

	public Integer getIdRhCompany() {
		return idRhCompany;
	}

	public void setIdRhCompany(Integer idRhCompany) {
		this.idRhCompany = idRhCompany;
	}

	public Integer getIdRhPerson() {
		return idRhPerson;
	}

	public void setIdRhPerson(Integer idRhPerson) {
		this.idRhPerson = idRhPerson;
	}

	public Integer getIdRhShiftPeriod() {
		return idRhShiftPeriod;
	}

	public void setIdRhShiftPeriod(Integer idRhShiftPeriod) {
		this.idRhShiftPeriod = idRhShiftPeriod;
	}
	
}