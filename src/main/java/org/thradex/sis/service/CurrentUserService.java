package org.thradex.sis.service;

import javax.servlet.http.HttpSession;

import org.thradex.model.SisUser;

public interface CurrentUserService  {
	
	public SisUser validUser(HttpSession session);
	public void updatePersonUser(HttpSession session, int idUser, boolean simulating, int idRealUser);
	
}
