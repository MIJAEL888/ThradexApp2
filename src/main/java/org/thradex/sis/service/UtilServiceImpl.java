package org.thradex.sis.service;

import org.springframework.transaction.annotation.Transactional;
import org.thradex.sis.dao.UtilDAO;
import org.thradex.util.ConstantsSis;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UtilServiceImpl implements UtilService {
	
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);

	@Autowired
	private UtilDAO utilDAO;
	
	@Override
	public Date getNowDate() {
		return utilDAO.getNowDate();
	}
}