package org.thradex.rrhh.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.thradex.model.RhFile;
import org.thradex.rrhh.dao.FileDAO;
import org.thradex.util.ConstantsSis;
import org.thradex.util.PropertiesSis;

import java.io.File;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class FilesServiceImpl implements FilesService {
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);

	
	@Autowired
	private FileDAO fileDAO;
	
	
	
	public RhFile save(RhFile rhFile) {
		return fileDAO.save(rhFile);
	}
	
	
	public RhFile saveOnDisk(CommonsMultipartFile multipartFile) {
		RhFile rhFile = null;
		try {
			
			String path = PropertiesSis.PATH_SHIFT + multipartFile.getOriginalFilename();
			File dest = new File(path);
			multipartFile.transferTo(dest);
			
			rhFile = new RhFile();
			rhFile.setNameFile(multipartFile.getOriginalFilename());
			rhFile.setFullPath(path);
			rhFile.setRelativePath( PropertiesSis.PATH_SHIFT);
			
		} catch (Exception e) {
			log.error("error al subir el archivo " + multipartFile.getOriginalFilename());
			e.printStackTrace();
		}
		
		return rhFile;
	}

	
	public RhFile get(RhFile rhFile) {
		
		return null;
	}

	
}