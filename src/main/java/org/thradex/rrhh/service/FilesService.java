package org.thradex.rrhh.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.thradex.model.RhFile;

public interface FilesService {
	
	public RhFile saveOnDisk(CommonsMultipartFile commonsMultipartFile);
	
	public RhFile save(RhFile rhFile);
	
	public RhFile get(RhFile rhFile);
	
}
