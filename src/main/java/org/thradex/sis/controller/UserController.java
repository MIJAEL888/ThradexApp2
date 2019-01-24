package org.thradex.sis.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.thradex.model.RhArea;
import org.thradex.model.RhCompany;
import org.thradex.model.RhPerson;
import org.thradex.model.SisUser;
import org.thradex.rrhh.service.AreaService;
import org.thradex.rrhh.service.CompanyService;
import org.thradex.rrhh.service.PersonService;
import org.thradex.sis.service.CurrentUserService;
import org.thradex.sis.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private CurrentUserService currentUserService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private UserService userService;
	
	//SIMULATION 
	@RequestMapping(value="/simulation",method=RequestMethod.GET)
	private String simulation(Model model ,HttpSession session){
		SisUser dbUser = currentUserService.validUser(session);		
		List<RhCompany> lsCompany=companyService.lsCompany();
		model.addAttribute("lsCompany", lsCompany);
		return "html/sis/simulation";
	}
	
	@RequestMapping(value="/simulationNew",method=RequestMethod.GET)
	private String simulationNew(HttpSession session,
			@RequestParam int idUser){
		SisUser dbUser = currentUserService.validUser(session);
		currentUserService.updatePersonUser(session, idUser, true,dbUser.getId());
		return "redirect:/shift/getPage";
	}
	
	@RequestMapping(value = "/simulationBack", method = RequestMethod.GET)
	public String simulationBack(Model model, HttpSession session, @RequestParam int idUser){
		currentUserService.updatePersonUser(session, idUser, false,0);
		return "redirect:/shift/getPage";
	}
	
	//AJAX
	@RequestMapping(value = "/loadArea", method = RequestMethod.GET)
	public String refreshItem(@RequestParam("idCom") int idCompany, Model model) {
	    List<RhArea> lisArea = areaService.lsAreaByIdCompany(idCompany);
	    model.addAttribute("lisArea", lisArea);
//	    return lisArea;
	    return "html/sis/simulation :: lisArea";
	}
	
	@RequestMapping(value = "/loadPerson", method = RequestMethod.POST)
	public String loadPerson(@RequestParam("area") int idArea, Model model) {
		RhArea rhArea= areaService.getAreaById(idArea);
	    List<RhPerson> lisPerson = personService.list(rhArea);
	    model.addAttribute("lisPerson", lisPerson);
	    return "html/sis/simulation :: lisPerson";
	}
	
	@RequestMapping(value="loadUser" , method = RequestMethod.GET)
	public String loadUserId(@RequestParam("idPer") int idPer, Model model){
		RhPerson rhPerson= personService.get(idPer);
		SisUser sisUser= userService.getUser(rhPerson);
		model.addAttribute("idUser", sisUser.getId());
		return "html/sis/simulation :: idUser";
	}
	
	
	
}
