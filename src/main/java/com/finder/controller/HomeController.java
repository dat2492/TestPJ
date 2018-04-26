package com.finder.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class HomeController {
	@Autowired
	private FinderService finderService;
	

	

	@RequestMapping("/")
    public String home(){
        return "index";
    }
	
	@RequestMapping("/login")
    public String login(@ModelAttribute("finderInfo")FinderInfo finderInfo) {
        return "login";
    }
    
	@RequestMapping(value="/access", method=RequestMethod.POST)
    public String access(HttpServletRequest request, 
            HttpServletResponse response,
            @ModelAttribute("finderInfo")FinderInfo finderInfo,
            Model model) throws IOException{
		request.getSession().setAttribute("user", finderInfo.getUsername());
        request.getSession().setAttribute("pass", finderInfo.getPassword());
    	NTLMPassword ntlm = new NTLMPassword();
		String password2 = ntlm.encode(finderInfo.getPassword());
		ProcessBuilder pb = new ProcessBuilder("pdbedit","-u",finderInfo.getUsername(),"-w");
    	Process p = pb.start();
    	BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String readline;
        while ((readline = reader.readLine()) != null) {
        	String[] parts = readline.split(":");
        	String part1 = parts[0]; 
        	String part4 = parts[3];
        if (finderInfo.getUsername().equalsIgnoreCase(part1) &&
                password2.equalsIgnoreCase(part4)
                ) {
        	return "new";
        }
    }
        return "login";
    }
	
	@GetMapping("/data")
	public String index(Model model) {
		model.addAttribute("datas", finderService.findAll());
		return "list";
	}
	

	@RequestMapping(value="/data/create", method=RequestMethod.POST)
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
		model.addAttribute("data", new FinderInfo());
		return "form";
	}

	@GetMapping("/data/{id}/edit")
	public String edit(@PathVariable int id, Model model) {
		model.addAttribute("data", finderService.findOne(id));
		return "form";
	}

	@PostMapping("/data/save")
	public String save(@Valid FinderInfo finderInfo, BindingResult result, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return "form";
		}
		finderService.save(finderInfo);
		redirect.addFlashAttribute("success", "Saved data successfully!");
		return "redirect:/data";
	}

	@GetMapping("/data/{id}/delete")
	public String delete(@PathVariable int id, RedirectAttributes redirect) {
		finderService.delete(id);
		redirect.addFlashAttribute("success", "Deleted data successfully!");
		return "redirect:/data";
	}

	@GetMapping("/data/search")
	public String search(@RequestParam("s") int s, Model model) {
		if (s==0) {
			return "redirect:/data";
		}

		model.addAttribute("datas", finderService.search(s));
		return "list";
	}
}
