package org.thradex.sis.dao;

import java.util.List;

import org.thradex.model.SisMenu;


public interface MenuDAO {
	
	public List<SisMenu> listMenu(int flagAct, int flagLevel, int idParent);
	
	public List<SisMenu> listMenu(int flagAct, int flagLevel);
	
}
