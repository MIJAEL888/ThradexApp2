package org.thradex.sis.service;

import java.util.List;

import org.thradex.model.SisMenu;
import org.thradex.model.SisUser;

public interface MenuService {
	
	public List<SisMenu> listMenuNavigator(SisUser sisUser);

}
