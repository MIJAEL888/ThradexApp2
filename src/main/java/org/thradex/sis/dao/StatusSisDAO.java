package org.thradex.sis.dao;

import org.thradex.model.SisStatus;

public interface StatusSisDAO {
	
	public SisStatus getSisStatus(String category, int code);
	
	public SisStatus getSisStatus(int id);
	
	public SisStatus getSisStatus(String category);
}
