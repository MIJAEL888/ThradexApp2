package org.thradex.sis.service;

import org.springframework.transaction.annotation.Transactional;
import org.thradex.model.SisMenu;
import org.thradex.model.SisRol;
import org.thradex.model.SisUser;
import org.thradex.sis.dao.MenuDAO;
import org.thradex.util.ConstantsSis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class MenuServiceImpl implements MenuService {
	
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);
	
	@Autowired
	private MenuDAO menuDAO;

	@Override
	public List<SisMenu> listMenuNavigator(SisUser user) {
		List<SisMenu> listSisMenusCheck = new ArrayList<SisMenu>();
		List<SisMenu> listSisMenus = menuDAO.listMenu(1, 1);
		
		for (int i = 0; i < listSisMenus.size(); i++) {
			SisMenu sisMenu = listSisMenus.get(i);
			if(checkHasRole(sisMenu.getListRol(), user.getListRol())){
				List<SisMenu> listSisMenusCheck2 = new ArrayList<SisMenu>();
				List<SisMenu> listSisMenusChild2 =  menuDAO.listMenu(1, 2, sisMenu.getId());
				
				for (int j = 0; j < listSisMenusChild2.size(); j++) {
					SisMenu sisMenu2 = listSisMenusChild2.get(j);
					if(checkHasRole(sisMenu2.getListRol(), user.getListRol())){
						List<SisMenu> listSisMenusCheck3 = new ArrayList<SisMenu>();
						List<SisMenu> listSisMenusChild3 =  menuDAO.listMenu(1, 3, sisMenu2.getId());
						
						for (int k = 0; k < listSisMenusChild3.size(); k++) {
							SisMenu sisMenu3 = listSisMenusChild3.get(k);
							if(checkHasRole(sisMenu3.getListRol(), user.getListRol())){
								listSisMenusCheck3.add(sisMenu3);
							}
						}
						
						sisMenu2.setListChild(listSisMenusCheck3);
						listSisMenusCheck2.add(sisMenu2);
					}
				}
				
				sisMenu.setListChild(listSisMenusCheck2);
				listSisMenusCheck.add(sisMenu);
			}
		}
		return listSisMenusCheck;
	}
	
	public boolean checkHasRole(List<SisRol> sisRols1, Set<SisRol> sisRols2) {
		for (int i = 0; i < sisRols1.size(); i++) {
			for (SisRol sisRol : sisRols2) {
				if (sisRols1.get(i).getIdRol() == sisRol.getIdRol()) return true;
			}
		}
		return false;
	}

}