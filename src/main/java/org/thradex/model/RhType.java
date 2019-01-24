package org.thradex.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the rh_type database table.
 * 
 */
@Entity
@Table(name="rh_type")
@NamedQuery(name="RhType.findAll", query="SELECT r FROM RhType r")
public class RhType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(length=45)
	private String name;
	
	@Column(name="CATEG")
	private String category;

	private int code;
	
	private String color;
	
	private Float value;
	
	@ManyToOne
	@JoinColumn(name="PARENT")
	private RhType rhTypeParent;
	
	public RhType() {
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public RhType getRhTypeParent() {
		return rhTypeParent;
	}

	public void setRhTypeParent(RhType rhTypeParent) {
		this.rhTypeParent = rhTypeParent;
	}

	public int getValueInt(){
		return Math.round(value);
	}
	
	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}