package org.thradex.sis.dao;

import org.thradex.model.RhPerson;
import org.thradex.model.SisUser;

public interface UserDAO {
	
	public SisUser getUser(String username);
	
	public SisUser getUser(int idUser);
	
	public SisUser searchDatabase(int idUser);
	
	public SisUser getUser(RhPerson person);
	
}
