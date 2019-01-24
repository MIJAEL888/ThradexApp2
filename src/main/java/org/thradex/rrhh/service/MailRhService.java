package org.thradex.rrhh.service;

import org.thradex.model.RhShift;

public interface MailRhService {

	public void sendMailAsigation(RhShift rhShift);
	
	public void sendMailRequest(RhShift rhShift);
	
	public void sendMailProcess(RhShift rhShift);
	
	public void sendMailAlert(RhShift rhShift);
	
	public void sendMailAlertMng(RhShift rhShift);
	
	public void sendMailJustified(RhShift rhShift);
	
}
