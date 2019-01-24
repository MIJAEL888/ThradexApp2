package org.thradex.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component("mainUtil")
public class MainUtil {
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public boolean validarCorreo(String correo){
		Pattern pat = null;
		Matcher mat = null;
		
		 pat = Pattern.compile("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$");
	     mat = pat.matcher(correo);
	     
	     if (mat.matches()){
	        return true;
	     }else{
	        return false;
	     }    
	}
	
	/**
	 * Validate hex with regular expression
	 * 
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public boolean validateMail(final String hex) {
		try {
			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(hex);
			return matcher.matches();
		} catch (Exception e) {
			return false;
		}
	
	}
	
	public void executeComand(String command) throws IOException{
		String s = null;
		Process p1 = Runtime.getRuntime().exec(command);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p1.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(p1.getErrorStream()));
        
        while ((s = stdInput.readLine()) != null) {
           log.info(s);
        }
        
        while ((s = stdError.readLine()) != null) {
           log.error(s, null);
		}
	}
	
	public void writeFile(String path, byte[] bytes) throws Exception{
		
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(new File(path), false);
			outputStream.write(bytes);
		}catch (Exception e) {
			throw e;
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	 
			}
		}
	}
	
	public byte[] loadFile(String path){
		
		File file = new File(path);
		byte[] fileBytes = new byte[(int)file.length()];
		
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			
			//Convert from file to bytes
			fileInputStream.read(fileBytes);
			fileInputStream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileBytes;
	}
	
}
