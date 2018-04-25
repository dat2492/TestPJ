package com.finder.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finder.login.NTLMPassword;
import com.finder.model.FinderInfo;
import com.finder.service.*;

@Controller
public class FormController {
	@Autowired
	private FinderService finderService;

	@RequestMapping("/options")
	public String options(@ModelAttribute("finderInfo")FinderInfo finderInfo) {
		
		return "new";
	}
	
	@GetMapping("/finderInfo")
	public String index(Model model,HttpServletRequest hsr,@ModelAttribute("finderInfo")FinderInfo finderInfo) {
		HttpSession session = hsr.getSession();
		String user = session.getAttribute("user").toString();
		model.addAttribute("finderInfos", finderService.UserFilter(user));
		return "list";
	}
	

	@GetMapping("/finderInfo/create")
	public String create(Model model,HttpServletRequest hsr,@ModelAttribute("finderInfo")FinderInfo finderInfo) {
		NTLMPassword ntlm = new NTLMPassword();
		HttpSession session = hsr.getSession();
		String user = session.getAttribute("user").toString();
       String pass = session.getAttribute("pass").toString();
       String pass2 = ntlm.encode(pass);
		model.addAttribute("username", user);
		model.addAttribute("password", pass2);
		model.addAttribute("area", finderInfo.getArea());
		System.out.println(finderInfo.getArea());
		model.addAttribute("finderInfo", new FinderInfo());
		return "form";
	}


	@PostMapping("/finderInfo/save")
	public String save(@Valid FinderInfo finderInfo, BindingResult result, RedirectAttributes redirect,HttpServletRequest request) {
		if (result.hasErrors()) {
			return "form";
		}
		request.getSession().setAttribute("area", finderInfo.getArea());
		request.getSession().setAttribute("recepti", finderInfo.getRecepti());
		request.getSession().setAttribute("date", finderInfo.getDate());
		request.getSession().setAttribute("cause", finderInfo.getCause());
		request.getSession().setAttribute("estate", finderInfo.getEstate());
		request.getSession().setAttribute("lotnumber", finderInfo.getLotnumber());
		request.getSession().setAttribute("house", finderInfo.getHouse());
		request.getSession().setAttribute("folderpath", finderInfo.getFolderpath());
		finderService.save(finderInfo);
		request.getSession().setAttribute("id", finderInfo.getId());
		return "redirect:/uploadMultiFile";
	}

	
}
