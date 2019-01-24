package org.thradex.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public interface MailService {
	
	public void sendMail(Set<String> listTO, Set<String> listCC, Set<String> listBCC, String subject, String msj, String fileName, InputStream inputStream) throws Exception;
	
	public void sendMail(Set<String> listTO, Set<String> listCC, Set<String> listBCC, String subject, String msj) throws Exception;
	
	public void sendMail(Set<String> listTO, Set<String> listCC, Set<String> listBCC, String subject, String msj, List<Map<String, Object>> listAttach) throws Exception;

	public void sendMail(Set<String> listTO, Set<String> listCC, String subject, String msj, String fileName, InputStream inputStream) throws Exception;
	
	public void sendMail(Set<String> listTO, Set<String> listCC, Set<String> listBCC, String subject, String msj, CommonsMultipartFile[] listAttach) throws Exception;
}
