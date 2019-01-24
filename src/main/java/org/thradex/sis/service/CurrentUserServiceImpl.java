package org.thradex.sis.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thradex.model.SisRol;
import org.thradex.model.SisUser;
import org.thradex.sis.dao.UserDAO;
import org.thradex.util.ConstantsSis;

@Service
@Transactional()
public class CurrentUserServiceImpl implements CurrentUserService{
	
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);
	
	@Autowired
	private UserService userService;	

	@Autowired
	private MenuService menuService;
	
	@Autowired
	private SessionFactory sessionFactory;
	
//	@Resource(name="userDao")
	@Autowired
	private UserDAO userDao;
	
	@Override
	public SisUser validUser(HttpSession session){
		
//		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
			if(session.getAttribute("currentUser") == null || session.getAttribute("listMenu") == null){
				SisUser dbUser = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
				session.setAttribute("currentUser", dbUser);
				session.setAttribute("listMenu",  menuService.listMenuNavigator(dbUser));
				return dbUser;
			}else{
				return (SisUser) session.getAttribute("currentUser");
			}
//		}else{
//			throw new CustomGenericException("USER01", "No user found on the session.");
//		}
	}
	
@Override
public void updatePersonUser(HttpSession session, int idUser, boolean simulating, int idRealUser){
		
		SisUser user = getUser(idUser);
		user.setSimulating(simulating);
		user.setIdRealUser(idRealUser);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(),auth.getCredentials(),getAuthorities(user.getListRol()));
		SecurityContextHolder.getContext().setAuthentication(newAuth);
		
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
		session.setAttribute("currentUser", user);
		session.setAttribute("listMenu",  menuService.listMenuNavigator(user));
	}
	
//	public Collection<? extends GrantedAuthority> getAuthorities(Integer role) {
//		List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
//		return authList;
//	}
//	
//	public List<String> getRoles(Integer role) {
//
//		List<String> roles = new ArrayList<String>();
//
//		if (role.intValue() == 1) {
//			roles.add("ROLE_MODERATOR");
//			roles.add("ROLE_ADMIN");
//		} else if (role.intValue() == 2) {
//			roles.add("ROLE_MODERATOR");
//		}
//		return roles;
//	}
//	
//	public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
//		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//		
//		for (String role : roles) {
//			authorities.add(new SimpleGrantedAuthority(role));
//		}
//		return authorities;
//	}

	public Collection<GrantedAuthority> getAuthorities(Set<SisRol> roles) {
		   // Create a list of grants for this user
		   List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		   Iterator<SisRol> iterator = roles.iterator();
			while (iterator.hasNext()) {
				SisRol rol = iterator.next();
				authList.add(new SimpleGrantedAuthority(rol.getNameRol()));
			}
		   return authList;
	}

	public SisUser getUser(int idUser){
			SisUser user = userDao.searchDatabase(idUser);
			return user;				
	}
}
