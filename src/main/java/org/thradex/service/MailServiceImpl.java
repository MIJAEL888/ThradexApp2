package org.thradex.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.thradex.util.ConstantsSis;

@Component
public class MailServiceImpl implements MailService{	
	
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);
	
	@Autowired
	private JavaMailSenderImpl mailSender;
	
//	@Autowired
//	private ApplicationContext appContext;
	
	@Override
	public void sendMail(Set<String> listTO, Set<String> listCC,Set<String> listBCC, 
			String subject, String msj, String fileName, InputStream inputStream) throws Exception{	
		
		MimeMessage message = mailSender.createMimeMessage();
		// use the true flag to indicate you need a multipart message
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		if (!listTO.isEmpty()) {
			String[] str = new String[listTO.size()];
			int i = 0;
			for (Iterator<String> iterator = listTO.iterator(); iterator.hasNext();) {
				str[i] =  iterator.next();
				i++;
			}
			helper.setTo(str);
		}else helper.setTo("mijael.palomino@thradex.com ");
		
		listCC = removeDuplicateEmail(listTO, listCC);
		if (!listCC.isEmpty()) {
			String[] strCC = new String[listCC.size()];
			int i = 0;
			for (Iterator<String> iterator2 = listCC.iterator(); iterator2.hasNext();) {
				strCC[i] =  iterator2.next();
				i++;
			}
			helper.setCc(strCC);
		}
		
		if (!listBCC.isEmpty()) {
			log.info("size setlist "+listCC);
			String[] strBcc = new String[listBCC.size()];
			int i = 0;
			for (Iterator<String> iterator2 = listBCC.iterator(); iterator2.hasNext();) {
				strBcc[i] =  iterator2.next();
				i++;
			}
			helper.setBcc(strBcc);
		}

		helper.setFrom("noc.ciena@thradex.com");
		helper.setSubject(subject);
		helper.setText(msj, true);	
//		helper.addInline("header", appContext.getResource("static/images/header.jpg"));
		byte[] bytes = null;
		try {
			bytes = IOUtils.toByteArray(inputStream);
			ByteArrayResource byteArray = new  ByteArrayResource(bytes);
			helper.addAttachment(fileName,byteArray);
		} catch (MessagingException m) {
			log.error("Error al comvertir en arra el stream!! ");
			m.printStackTrace();
			throw new Exception();
		}catch (IOException e) {
			e.printStackTrace();
			throw new Exception();
		}
		
		mailSender.send(message);
	}
		
	@Override
	public void sendMail(Set<String> listTO, Set<String> listCC, Set<String> listBCC, 
			String subject, String msj) throws Exception{	
		MimeMessage message = mailSender.createMimeMessage();
		// use the true flag to indicate you need a multipart message mailSender.
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		if (!listTO.isEmpty()) {
			String[] str = new String[listTO.size()];
			int i = 0;
			for (Iterator<String> iterator = listTO.iterator(); iterator.hasNext();) {
				str[i] =  iterator.next();
				i++;
			}
			helper.setTo(str);
		}else helper.setTo("claudio.saldanha@thradex.com ");
		
		listCC = removeDuplicateEmail(listTO, listCC);
		
		if (!listCC.isEmpty()) {
			String[] strCC = new String[listCC.size()];
			int i = 0;
			for (Iterator<String> iterator2 = listCC.iterator(); iterator2.hasNext();) {
				
				strCC[i] =  iterator2.next();
				i++;
			}
			helper.setCc(strCC);
		}
		
		if (!listBCC.isEmpty()) {
			log.info("size setlist "+listCC);
			String[] strBcc = new String[listBCC.size()];
			int i = 0;
			for (Iterator<String> iterator2 = listBCC.iterator(); iterator2.hasNext();) {
				strBcc[i] =  iterator2.next();
				i++;
			}
			helper.setBcc(strBcc);
		}
		helper.setFrom("noc.ciena@thradex.com");
		helper.setSubject(subject);
		helper.setText(msj, true);		
//		helper.addInline("header", appContext.getResource("static/global/images/header.jpg"));
		mailSender.send(message);
	}

	@Override
	public void sendMail(Set<String> listTO, Set<String> listCC, String subject, String msj,
			String fileName, InputStream inputStream) throws Exception{	
		
		MimeMessage message = mailSender.createMimeMessage();
		// use the true flag to indicate you need a multipart message
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		
		if (!listTO.isEmpty()) {
			log.info("size setlist "+listTO);
			String[] str = new String[listTO.size()];
			int i = 0;
			for (Iterator<String> iterator = listTO.iterator(); iterator.hasNext();) {
				str[i] =  iterator.next();
				i++;
			}
			helper.setTo(str);
		}else helper.setTo("claudio.saldanha@thradex.com ");
		
		listCC = removeDuplicateEmail(listTO, listCC);
		
		if (!listCC.isEmpty()) {
			log.info("size setlist "+listCC);
			String[] strCC = new String[listCC.size()];
			int i = 0;
			for (Iterator<String> iterator2 = listCC.iterator(); iterator2.hasNext();) {
				strCC[i] =  iterator2.next();
				i++;
			}
			helper.setCc(strCC);
		}
		helper.setFrom("noc.ciena@thradex.com");
		helper.setSubject(subject);
		helper.setText(msj, true);	
		byte[] bytes = null;
		try {
			bytes = IOUtils.toByteArray(inputStream);
			ByteArrayResource byteArray = new  ByteArrayResource(bytes);
			helper.addAttachment(fileName,byteArray);
		} catch (MessagingException m) {
			log.error("Error al comvertir en arra el stream!! ");
			m.printStackTrace();
			throw new Exception();
		}catch (IOException e) {
			e.printStackTrace();
			throw new Exception();
		}
		
		mailSender.send(message);
	}
		
	@Override
	public void sendMail(Set<String> listTO, Set<String> listCC, Set<String> listBCC, 
			String subject, String msj, List<Map<String, Object>> listAttach) throws Exception{	
		
		MimeMessage message = mailSender.createMimeMessage();
		// use the true flag to indicate you need a multipart message
		
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		
		if (!listTO.isEmpty()) {
			log.info("size setlist "+listTO);
			String[] str = new String[listTO.size()];
			int i = 0;
			for (Iterator<String> iterator = listTO.iterator(); iterator.hasNext();) {
				str[i] =  iterator.next();
				i++;
			}
			helper.setTo(str);
		}else helper.setTo("claudio.saldanha@thradex.com ");
		
		listCC = removeDuplicateEmail(listTO, listCC);
		
		if (!listCC.isEmpty()) {
			log.info("size setlist "+listCC);
			String[] strCC = new String[listCC.size()];
			int i = 0;
			for (Iterator<String> iterator2 = listCC.iterator(); iterator2.hasNext();) {
				strCC[i] =  iterator2.next();
				i++;
			}
			helper.setCc(strCC);
		}
		
		if (!listBCC.isEmpty()) {
			log.info("size setlist "+listCC);
			String[] strBcc = new String[listBCC.size()];
			int i = 0;
			for (Iterator<String> iterator2 = listBCC.iterator(); iterator2.hasNext();) {
				strBcc[i] =  iterator2.next();
				i++;
			}
			helper.setBcc(strBcc);
		}

		helper.setFrom("noc.ciena@thradex.com");
		helper.setSubject(subject);
		helper.setText(msj, true);	
//		helper.addInline("header", appContext.getResource("static/images/header.jpg"));
		
		for (int i = 0; i < listAttach.size(); i++) {
			HashMap<String, Object> attach = (HashMap<String, Object>) listAttach.get(i);
			byte[] bytes = null;
			if ( attach.get("stream") != null) {
				try {
					bytes = IOUtils.toByteArray((InputStream) attach.get("stream"));
					ByteArrayResource byteArray = new  ByteArrayResource(bytes);
					helper.addAttachment(attach.get("fileName").toString(), byteArray);
				} catch (MessagingException m) {
					log.error("Error al comvertir en arra el stream!! ");
					m.printStackTrace();
					throw new Exception();
				}catch (IOException e) {
					e.printStackTrace();
					throw new Exception();
				}
			}
		}
		mailSender.send(message);
	}
	
	@Override
	public void sendMail(Set<String> listTO, Set<String> listCC, Set<String> listBCC, 
			String subject, String msj, CommonsMultipartFile[] listAttach) throws Exception{	
		
		MimeMessage message = mailSender.createMimeMessage();
		// use the true flag to indicate you need a multipart message
		
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		
		if (!listTO.isEmpty()) {
			String[] str = new String[listTO.size()];
			int i = 0;
			for (Iterator<String> iterator = listTO.iterator(); iterator.hasNext();) {
				str[i] =  iterator.next();
				i++;
			}
			helper.setTo(str);
		}else helper.setTo("mijael.palomino@thradex.com ");
		
		listCC = removeDuplicateEmail(listTO, listCC);
		
		if (!listCC.isEmpty()) {
			String[] strCC = new String[listCC.size()];
			int i = 0;
			for (Iterator<String> iterator2 = listCC.iterator(); iterator2.hasNext();) {
				strCC[i] =  iterator2.next();
				i++;
			}
			helper.setCc(strCC);
		}
		
		if (!listBCC.isEmpty()) {
			String[] strBcc = new String[listBCC.size()];
			int i = 0;
			for (Iterator<String> iterator2 = listBCC.iterator(); iterator2.hasNext();) {
				strBcc[i] =  iterator2.next();
				i++;
			}
			helper.setBcc(strBcc);
		}

		helper.setFrom("noc.ciena@thradex.com");
		helper.setSubject(subject);
		helper.setText(msj, true);	
//		helper.addInline("header", appContext.getResource("static/images/header.jpg"));
		
		if(listAttach!=null){
			for (CommonsMultipartFile file : listAttach) {
				byte[] bytes = null;
				InputStream inputStream = file.getInputStream();
				if ( inputStream != null) {
					try {
						bytes = IOUtils.toByteArray(inputStream);
						ByteArrayResource byteArray = new  ByteArrayResource(bytes);
						helper.addAttachment(file.getOriginalFilename(), byteArray);
					} catch (MessagingException m) {
						log.error("Error al comvertir en arra el stream!! ");
						m.printStackTrace();
						throw new Exception();
					}catch (IOException e) {
						e.printStackTrace();
						throw new Exception();
					}
				}
			}
		}
		mailSender.send(message);
	}
	public Set<String> removeDuplicateEmail(Set<String> list1, Set<String> list2){
		Iterator<String> iterator1 = list1.iterator();
	    while(iterator1.hasNext()) {
	    	String string1 = iterator1.next();
	    	Iterator<String> iterator2 = list2.iterator();
	    	while (iterator2.hasNext()) {
				String string2 = iterator2.next();
				if (string1.equals(string2)) {
					list2.remove(string2);
				}
			}
	    }
		return list2;    
	}

}	

