package org.thradex.rrhh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

@Component
public class MessagesRhImpl implements MessagesRh{
	
	private final String MAIL_REQUEST = "mail.request";
	private final String MAIL_ASIGNATION = "mail.asignation";
	private final String MAIL_JUSTIFATION = "mail.justifation";
	private final String MAIL_PROCESS = "mail.process";
	private final String MAIL_ALERT = "mail.alert";
	private final String MAIL_ALERT2 = "mail.alert2";
	private final String MAIL_SUBJECT_REQUEST = "mail.subject.request";
	private final String MAIL_SUBJECT_ASIGNATION = "mail.subject.asignation";
	private final String MAIL_SUBJECT_JUSTIFATION = "mail.subject.justifation";
	private final String MAIL_SUBJECT_PROCESS = "mail.subject.process";
	private final String MAIL_SUBJECT_ALERT = "mail.subject.alert";
	
	@Autowired
	private MessageSourceAccessor messageSource;
	
	
	public String getMsgMailRequest(String type, String to) {
		return messageSource.getMessage(MAIL_REQUEST, new Object[] { type, to }); 
	}
	
	
	public String getMsgMailAsignation(String type, String to) {
		return messageSource.getMessage(MAIL_ASIGNATION, new Object[] { type, to }); 
	}
	
	
	public String getMsgMailJustification(String type, String to) {
		return messageSource.getMessage(MAIL_JUSTIFATION, new Object[] { type, to }); 
	}
	
	
	public String getMsgMailJustification2(String type, String to, String reason) {
		return messageSource.getMessage(MAIL_JUSTIFATION, new Object[] { type, to ,reason }); 
	}
	
	
	public String getMsgMailProcess(String type, String to) {
		return messageSource.getMessage(MAIL_PROCESS, new Object[] { type, to }); 
	}
	
	
	public String getMsgMailAlert(String type, String to) {
		return messageSource.getMessage(MAIL_ALERT, new Object[] { type, to }); 
	}
	
	
	public String getMsgMailAlertMng(String type, String to) {
		return messageSource.getMessage(MAIL_ALERT2, new Object[] { type, to }); 
	}
	
	
	public String getSubjectMailRequest(String type, String to) {
		return messageSource.getMessage(MAIL_SUBJECT_REQUEST, new Object[] { type, to }); 
	}
	
	
	public String getSubjectMailAsignation(String type, String to) {
		return messageSource.getMessage(MAIL_SUBJECT_ASIGNATION, new Object[] { type, to }); 
	}
	
	
	public String getSubjectMailJustification(String type, String to) {
		return messageSource.getMessage(MAIL_SUBJECT_JUSTIFATION, new Object[] { type, to }); 
	}
	
	
	public String getSubjectMailProcess(String type, String to) {
		return messageSource.getMessage(MAIL_SUBJECT_PROCESS, new Object[] { type, to }); 
	}
	
	
	public String getSubjectMailAlert(String type, String to) {
		return messageSource.getMessage(MAIL_SUBJECT_ALERT, new Object[] { type, to }); 
	}
}
