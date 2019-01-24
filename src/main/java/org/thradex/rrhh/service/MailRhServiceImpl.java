package org.thradex.rrhh.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.thradex.model.RhPerson;
import org.thradex.model.RhShift;
import org.thradex.service.MailService;
import org.thradex.util.ConstantsSis;

@Service
public class MailRhServiceImpl implements MailRhService{
	
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);
	
	@Autowired
	private MessagesRh messagesRh;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired
	private MailService mailService;
	
	
	@Async
	public void sendMailAsigation(RhShift rhShift){
		try {
			
			RhPerson rhPersonTo = rhShift.getRhPerson();
			RhPerson rhPersonFrom = rhShift.getRhPersonMng();
			String subject = messagesRh.getSubjectMailAsignation(rhShift.getRhType().getName(), rhPersonTo.getFullname());
			
			Set<String> listTO = new HashSet<String>();
			listTO.add(rhPersonTo.getEmail());
			
			Set<String> listCC = new HashSet<String>();
			listTO.add(rhPersonFrom.getEmail());
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("nameTo", rhPersonTo.getFullname());
			model.put("message", messagesRh.getMsgMailAsignation(rhShift.getRhType().getName(), rhPersonFrom.getFullname()));
			model.put("rhShift", rhShift);
			model.put("date", new DateTool());
			String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "rrhh/emailTemplate3.vm", "UTF-8", model);
			
			mailService.sendMail(listTO, listCC, new HashSet<String>(), subject, text.toString(), rhShift.getFiles());
		} catch (Exception e) {
			log.error("Error sending email for RhShift id : " + rhShift.getId());
			e.printStackTrace();
		}
		
	}
	
	
	@Async
	public void sendMailRequest(RhShift rhShift){
		try {
			RhPerson rhPersonTo = rhShift.getRhPersonMng();
			RhPerson rhPersonFrom = rhShift.getRhPerson();
			String subject = messagesRh.getSubjectMailRequest(rhShift.getRhType().getName(), rhPersonFrom.getFullname());
			
			Set<String> listTO = new HashSet<String>();
			listTO.add(rhPersonTo.getEmail());
			
			Set<String> listCC = new HashSet<String>();
			listTO.add(rhPersonFrom.getEmail());
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("nameTo", rhPersonTo.getFullname());
			model.put("message", messagesRh.getMsgMailRequest(rhShift.getRhType().getName(), rhPersonFrom.getFullname()));
			model.put("rhShift", rhShift);
			model.put("date", new DateTool());
			String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "rrhh/emailTemplate3.vm", "UTF-8", model);
			
			mailService.sendMail(listTO, listCC, new HashSet<String>(), subject, text.toString(), rhShift.getFiles());
		} catch (Exception e) {
			log.error("Error sending email for RhShift id : " + rhShift.getId());
			e.printStackTrace();
		}
		
	}
	
	
	@Async
	public void sendMailProcess(RhShift rhShift){
		try {
			RhPerson rhPersonTo = rhShift.getRhPerson();
			RhPerson rhPersonFrom = rhShift.getRhPersonMng();
			String subject = messagesRh.getSubjectMailProcess(rhShift.getRhType().getName(), rhPersonTo.getFullname());
			
			Set<String> listTO = new HashSet<String>();
			listTO.add(rhPersonTo.getEmail());
			
			Set<String> listCC = new HashSet<String>();
			listTO.add(rhPersonFrom.getEmail());
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("nameTo", rhPersonTo.getFullname());
			model.put("message", messagesRh.getMsgMailProcess(rhShift.getRhType().getName(), rhPersonFrom.getFullname()));
			model.put("rhShift", rhShift);
			model.put("date", new DateTool());
			String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "rrhh/emailTemplate3.vm", "UTF-8", model);
			
			mailService.sendMail(listTO, listCC, new HashSet<String>(), subject, text.toString(), rhShift.getFiles());
		} catch (Exception e) {
			log.error("Error sending email for RhShift id : " + rhShift.getId());
			e.printStackTrace();
		}
		
	}
	
	
	@Async
	public void sendMailAlert(RhShift rhShift){
		try {
			RhPerson rhPersonTo = rhShift.getRhPerson();
			String subject = messagesRh.getSubjectMailAlert(rhShift.getRhType().getName(), rhPersonTo.getFullname());
			
			Set<String> listTO = new HashSet<String>();
//			listTO.add(rhPersonTo.getEmail());
			listTO.add(rhShift.getRhPersonMng().getEmail());
//			listTO.add("cristhian.cajahuaringa@thradex.com");
			
			Map<String, Object> model = new HashMap<String, Object>();
//			model.put("nameTo", rhPersonTo.getFullname());
			model.put("nameTo", rhShift.getRhPersonMng().getFullname());
//			model.put("message", messagesRh.getMsgMailAlert(rhShift.getRhType().getName(), rhPersonTo.getFullname()));
			model.put("message", messagesRh.getMsgMailAlertMng(rhShift.getRhType().getName(), rhPersonTo.getFullname()));
			model.put("rhShift", rhShift);
			model.put("date", new DateTool());
			String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "rrhh/emailTemplate3.vm", "UTF-8", model);
			
			mailService.sendMail(listTO, new HashSet<String>(), new HashSet<String>(), subject, text.toString(), rhShift.getFiles());
		} catch (Exception e) {
			log.error("Error sending email for RhShift id : " + rhShift.getId());
			e.printStackTrace();
		}
	}
	
	
	@Async
	public void sendMailAlertMng(RhShift rhShift){
		try {
			RhPerson rhPersonTo = rhShift.getRhPersonMng();
			String subject = messagesRh.getSubjectMailAlert(rhShift.getRhType().getName(), rhPersonTo.getFullname());
			
			Set<String> listTO = new HashSet<String>();
			listTO.add(rhPersonTo.getEmail());
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("nameTo", rhPersonTo.getFullname());
			model.put("message", messagesRh.getMsgMailAlertMng(rhShift.getRhType().getName(), rhPersonTo.getFullname()));
			model.put("rhShift", rhShift);
			model.put("date", new DateTool());
			String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "rrhh/emailTemplate3.vm", "UTF-8", model);
			
			mailService.sendMail(listTO, new HashSet<String>(), new HashSet<String>(), subject, text.toString(), rhShift.getFiles());
		} catch (Exception e) {
			log.error("Error sending email for RhShift id : " + rhShift.getId());
			e.printStackTrace();
		}
	}
	
	
	@Async
	public void sendMailJustified(RhShift rhShift){
		try {
			RhPerson rhPersonTo = rhShift.getRhPersonMng();
			RhPerson rhPersonFrom = rhShift.getRhPerson();
			String subject = messagesRh.getSubjectMailJustification(rhShift.getRhType().getName(), rhPersonFrom.getFullname());
			
			Set<String> listTO = new HashSet<String>();
			listTO.add(rhPersonTo.getEmail());
//			listTO.add("cristhian.cajahuaringa@thradex.com");
			
			Set<String> listCC = new HashSet<String>();
			listTO.add(rhPersonFrom.getEmail());
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("nameTo", rhPersonTo.getFullname());
//			model.put("message", messagesRh.getMsgMailJustification(rhShift.getRhType().getName(), rhPersonFrom.getFullname()));
			model.put("message", messagesRh.getMsgMailJustification2(rhShift.getRhType().getName(), rhPersonFrom.getFullname(),rhShift.getReason()));
			model.put("rhShift", rhShift);
			model.put("date", new DateTool());
			String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "rrhh/emailTemplate3.vm", "UTF-8", model);
			
			mailService.sendMail(listTO, listCC, new HashSet<String>(), subject, text.toString(), rhShift.getFiles());
		} catch (Exception e) {
			log.error("Error sending email for RhShift id : " + rhShift.getId());
			e.printStackTrace();
		}
		
	}
}
