package org.thradex.model;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import java.util.List;


/**
 * The persistent class for the sis_rol database table.
 * 
 */
@Entity
@Table(name="sis_menu")
@NamedQuery(name="SisMenu.findAll", query="SELECT s FROM SisMenu s")
public class SisMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;
	
	@Column(name="ID_APLICACION")
	private int idAplication;
	
	private String label;

	private int level;

	private String url;
	
	private String icon;
	
	@Column(name="FLAG_ACT")
	private int flagAct;
	
	private int order;
	
	private Integer type;

	@ManyToOne
	@JoinColumn(name="ID_PARENT")
	private SisMenu sisMenuParent;
	
	@ManyToMany(targetEntity=SisRol.class,fetch=FetchType.EAGER)
	@JoinTable(name = "sis_menu_rol", 
		joinColumns = { @JoinColumn(name = "ID_MENU") }, 
		inverseJoinColumns = { @JoinColumn(name = "ID_ROL") })
	@Where(clause="ID_APLICACION=2")
	private List<SisRol> listRol; 
	
	@Transient
	List<SisMenu> listChild; 
	
	@Transient
	private String rolString;
	
	public SisMenu() {
	}

	public SisMenu(int id) {
		super();
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRolString() {
		return rolString;
	}

	public void setRolString(String rolString) {
		this.rolString = rolString;
	}

	public List<SisMenu> getListChild() {
		return listChild;
	}

	public void setListChild(List<SisMenu> listChild) {
		this.listChild = listChild;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getFlagAct() {
		return flagAct;
	}

	public void setFlagAct(int flagAct) {
		this.flagAct = flagAct;
	}

	public int getId() {
		return id;
	}

	public SisMenu getSisMenuParent() {
		return sisMenuParent;
	}

	public void setSisMenuParent(SisMenu sisMenuParent) {
		this.sisMenuParent = sisMenuParent;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SisRol> getListRol() {
		return listRol;
	}

	public void setListRol(List<SisRol> listRol) {
		this.listRol = listRol;
	}
	
}