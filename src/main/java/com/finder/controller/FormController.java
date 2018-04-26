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

	@GetMapping("/options")
    public String options(@ModelAttribute("finderInfo")FinderInfo finderInfo) {        
        return "new";
    }
	
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


	@RequestMapping("/finderInfo/save")
	public String save(@Valid FinderInfo finderInfo, BindingResult result, HttpServletRequest hsr, RedirectAttributes redirect, HttpServletRequest request, Model model) {
		request.getSession().setAttribute("area", finderInfo.getArea());
		request.getSession().setAttribute("recepti", finderInfo.getRecepti());
		request.getSession().setAttribute("date", finderInfo.getDate());
		request.getSession().setAttribute("cause", finderInfo.getCause());
		request.getSession().setAttribute("estate", finderInfo.getEstate());
		request.getSession().setAttribute("lotnumber", finderInfo.getLotnumber());
		request.getSession().setAttribute("house", finderInfo.getHouse());
		
		NTLMPassword ntlm = new NTLMPassword();
		HttpSession session = hsr.getSession();
		String user = session.getAttribute("user").toString();
		String pass = session.getAttribute("pass").toString();
		String pass2 = ntlm.encode(pass);
		
		FinderInfo receptiExists = finderService.findByRecepti(finderInfo.getRecepti());		
		if (receptiExists != null) {
			model.addAttribute("area", finderInfo.getArea());
			model.addAttribute("username", user);
			model.addAttribute("password", pass2);			
			model.addAttribute("finderInfo", new FinderInfo());
			model.addAttribute("InvalidRecepti", "この受付番号は使用されています。別の受付番号を入力してください !");			
			return "form";
		}
		
		FinderInfo folderPathExists = finderService.findByUsernameAndFolderpath(user, finderInfo.getFolderpath());
		if (folderPathExists != null){	
			model.addAttribute("area", finderInfo.getArea());
			model.addAttribute("username", user);
			model.addAttribute("password", pass2);		
			model.addAttribute("finderInfo", new FinderInfo());
			model.addAttribute("InvalidFolder", "このフォルダは存在してしまいました !");
			return "form";
		}
		
		if (result.hasErrors()) {
			model.addAttribute("area", finderInfo.getArea());
			model.addAttribute("username", user);
			model.addAttribute("password", pass2);		
			model.addAttribute("finderInfo", new FinderInfo());
			return "form";
		}
		
		request.getSession().setAttribute("folderpath", finderInfo.getFolderpath());			
		finderService.save(finderInfo);		
		request.getSession().setAttribute("id", finderInfo.getId());
		return "redirect:/uploadMultiFile";
	}	
}
