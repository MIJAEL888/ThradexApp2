package org.thradex.sis.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.thradex.model.SisRol;
import org.thradex.model.SisUser;
import org.thradex.util.ConstantsSis;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	static Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);
	
	@Autowired
	private UserService userService;	

	@Override
	public UserDetails loadUserByUsername(String username) {
		
		SisUser domainUser = userService.getUser(username);
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;

		return new User(
				domainUser.getUsername(), 
				domainUser.getPassword(), 
				enabled, 
				accountNonExpired, 
				credentialsNonExpired, 
				accountNonLocked,
				getAuthorities(domainUser.getListRol())
		);
	}
	
	public User currentUserDetails(){
		SecurityContextHolder.getContext().getAuthentication().getName();
		return null;
	}
	
	 public static Collection<GrantedAuthority> getAuthorities(Set<SisRol> roles) {
		   // Create a list of grants for this user
		   List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		    
		   Iterator<SisRol> iterator = roles.iterator();
			while (iterator.hasNext()) {
				SisRol rol = iterator.next();
				log.info("ROL: " + rol.getName());
				authList.add(new SimpleGrantedAuthority(rol.getName()));
			}
			
		   return authList;
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

}
