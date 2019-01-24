package org.thradex.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tt_types database table.
 * 
 */
@Entity
@Table(name="sis_ip_office")
@NamedQuery(name="SisIpOffice.findAll", query="SELECT t FROM SisIpOffice t")
public class SisIpOffice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String ip;
	
	private String description;

	public SisIpOffice() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}