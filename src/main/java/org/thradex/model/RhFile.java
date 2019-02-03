package org.thradex.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="rh_file")
public class RhFile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="NAME_FILE")
	private String nameFile;
	
	@Column(name="FULL_PATH")
	private String fullPath;

	@Column(name="RELATIVE_PATH")
	private String relativePath;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_RH_SHIFT")
	private RhShift rhShift;
	
	
	public RhFile() {
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getNameFile() {
		return nameFile;
	}


	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}


	public String getFullPath() {
		return fullPath;
	}


	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}


	public String getRelativePath() {
		return relativePath;
	}


	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}


	public RhShift getRhShift() {
		return rhShift;
	}


	public void setRhShift(RhShift rhShift) {
		this.rhShift = rhShift;
	}

}