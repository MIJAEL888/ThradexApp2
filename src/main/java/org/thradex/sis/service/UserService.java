package org.thradex.sis.service;

import org.thradex.model.RhPerson;
import org.thradex.model.SisUser;

public interface UserService {
	
	public SisUser getUser(String login);

	public SisUser getUser(int idUser);
	
	public SisUser getUser(RhPerson person);
}
