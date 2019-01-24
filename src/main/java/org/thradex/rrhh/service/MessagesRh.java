package org.thradex.rrhh.service;

public interface MessagesRh {
	
	public String getMsgMailRequest(String type, String to);
	
	public String getMsgMailAsignation(String type, String to);
	
	public String getMsgMailJustification(String type, String to);
	public String getMsgMailJustification2(String type, String to,String reason);
	
	public String getMsgMailProcess(String type, String to); 
	
	public String getMsgMailAlert(String type, String to);
	
	public String getMsgMailAlertMng(String type, String to);
	
	public String getSubjectMailRequest(String type, String to);
	
	public String getSubjectMailAsignation(String type, String to);
	
	public String getSubjectMailJustification(String type, String to);
	
	public String getSubjectMailProcess(String type, String to); 
	
	public String getSubjectMailAlert(String type, String to);
	
}
