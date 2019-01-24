package org.thradex.report.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thradex.model.EvenUsuario;
import org.thradex.model.ValorCosto;
import org.thradex.rrhh.service.ShiftReportService;

@Controller
@RequestMapping("/Report")
public class ReportEventoUsuario {
	
	@Autowired
	private ShiftReportService shiftReportService;
	
	@RequestMapping(value = "/registroAsistencia", method = RequestMethod.GET)
	private String getEventxUsuario(HttpSession session, HttpServletRequest request,Model model){
		List<EvenUsuario> lsEvenUsuario= shiftReportService.listEvenUsuario();
		String categorias="";
		String porProcesar="";		
		String porJustificar="";
		for (EvenUsuario evenUsuario : lsEvenUsuario){
//			categorias+=",'"+evenUsuario.getNombrePersona()+"'";
			categorias+=","+evenUsuario.getNombrePersona();
			porProcesar+=","+evenUsuario.getProcesar();
			porJustificar+=","+evenUsuario.getJustificar();
		}
		
//		System.out.println("1=> "+categorias.substring(1));
//		System.out.println("2=> "+porProcesar.substring(1));
//		System.out.println("3=> "+porJustificar.substring(1));
//		System.out.println("4=> "+lsEvenUsuario.size());
		
		model.addAttribute("lsEvenUsuario",lsEvenUsuario);
		
		model.addAttribute("categorias",categorias.substring(1));
//		model.addAttribute("categorias2","categories: ["+categorias.substring(1)+"]");
		
		model.addAttribute("porProcesar",porProcesar.substring(1));
		model.addAttribute("porJustificar",porJustificar.substring(1));
		return "html/rrhh/report/reportEventxUsuario";
	}
	
	@RequestMapping(value = "/valoresDeCostos", method = RequestMethod.GET)
	private String getValoresCostos(HttpSession session, HttpServletRequest request,Model model){
		List<ValorCosto> lsValorCosto= shiftReportService.lisValorCosto();
		String tipo="";
		String aprobado="";
		String reportado="";
		for (ValorCosto valorCosto : lsValorCosto) {
			tipo+=","+valorCosto.getTipo();
			aprobado+=","+valorCosto.getEfectivo();
			reportado+=","+valorCosto.getReportado();
		}
		
//		System.out.println("1=> "+tipo.substring(1));
//		System.out.println("2=> "+aprobado.substring(1));
//		System.out.println("3=> "+reportado.substring(1));
		
		model.addAttribute("tipo",tipo.substring(1));
		model.addAttribute("aprobado",aprobado.substring(1));
		model.addAttribute("reportado",reportado.substring(1));
		
		return "html/rrhh/report/reportValoresDeCostos";
	}
}
