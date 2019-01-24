package org.thradex.sis.dao;

import java.util.List;

import org.thradex.model.SisCountry;

public interface CountryDAO {
	
	public SisCountry get(int id);
	
	public List<SisCountry> list();
	
}
