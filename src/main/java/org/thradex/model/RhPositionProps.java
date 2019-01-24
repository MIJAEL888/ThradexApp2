package org.thradex.model;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the rh_position database table.
 * 
 */
@Entity
@Table(name="rh_postion_props")
@NamedQuery(name="RhPositionProps.findAll", query="SELECT r FROM RhPositionProps r")
public class RhPositionProps implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;

	private String property;
	
	private String type;
	
	@ManyToOne
	@JoinColumn(name="ID_AREA_POSITION", nullable=false)
	private RhAreaPosition rhAreaPosition;
	
	public RhPositionProps() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public RhAreaPosition getRhAreaPosition() {
		return rhAreaPosition;
	}

	public void setRhAreaPosition(RhAreaPosition rhAreaPosition) {
		this.rhAreaPosition = rhAreaPosition;
	}
	
	
}