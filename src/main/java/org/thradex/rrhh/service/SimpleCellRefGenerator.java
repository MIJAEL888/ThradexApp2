package org.thradex.rrhh.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.jxls.command.CellRefGenerator;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.thradex.model.RhPerson;
import org.thradex.util.ConstantsSis;

public class SimpleCellRefGenerator implements CellRefGenerator{
	Logger log = Logger.getLogger(ConstantsSis.LOG_SERVICE);
	@Override
	@SuppressWarnings("unchecked")
	public CellRef generateCellRef(int index, Context context) {
		List<RhPerson> persons = (List<RhPerson>) context.getVar("persons");
		String sheetName = "Sheet";
		try{
			RhPerson rhPerson = persons.get(index);
			sheetName = rhPerson.getName() + "_" + rhPerson.getSurname();
		}catch(Exception e){
//			e.printStackTrace();
		}
		log.info("sheet name " + sheetName);
		 return new CellRef(sheetName + "!B2");
	}

}
