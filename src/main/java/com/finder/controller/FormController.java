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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finder.login.NTLMPassword;
import com.finder.model.FinderInfo;
import com.finder.service.*;

@Controller
public class FormController {
	@Autowired
	private FinderService finderService;

	
	@GetMapping("/finderInfo")
	public String index(Model model,HttpServletRequest hsr,@ModelAttribute("finderInfo")FinderInfo finderInfo) {
		HttpSession session = hsr.getSession();
		String user = session.getAttribute("user").toString();
		model.addAttribute("username", user);
		model.addAttribute("finderInfos", finderService.findAll());
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

	@GetMapping("/finderInfo/{id}/edit")
	public String edit(@PathVariable int id, Model model) {
		model.addAttribute("finderInfo", finderService.findOne(id));
		return "form";
	}

	@PostMapping("/finderInfo/save")
	public String save(@Valid FinderInfo finderInfo, BindingResult result, RedirectAttributes redirect,HttpServletRequest request) {
		if (result.hasErrors()) {
			return "form";
		}
		request.getSession().setAttribute("folderpath", finderInfo.getFolderpath());
		finderService.save(finderInfo);
		redirect.addFlashAttribute("success", "Saved data successfully!");
		return "redirect:/uploadMultiFile";
	}

	@GetMapping("/finderInfo/{id}/delete")
	public String delete(@PathVariable int id, RedirectAttributes redirect) {
		finderService.delete(id);
		redirect.addFlashAttribute("success", "Deleted data successfully!");
		return "redirect:/finderInfo";
	}

	
}
