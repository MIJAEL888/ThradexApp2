package org.thradex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ThradexController {
	@RequestMapping("/")
	public String welcomeHandler() {
		return "redirect:/auth/login";
	}
}
