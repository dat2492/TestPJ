//package com.finder.controller;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import javax.validation.Valid;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Service;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import org.springframework.web.bind.annotation.*;
//import com.finder.login.NTLMPassword;
//import com.finder.model.FinderInfo;
//import com.finder.service.*;
//import javax.xml.bind.*;
//
//@Controller
//
//public class HomeController {
//		
//	@Autowired
//	private FinderService finderService;	
//
//	@RequestMapping("/")
//    public String home(){
//        return "index";
//    }
//	
//	@RequestMapping("/login")
//    public String login(@ModelAttribute("finderInfo")FinderInfo finderInfo) {
//        return "login";
//    }
//    
//	@RequestMapping(value="/access", method=RequestMethod.POST)
//    public String access(HttpServletRequest request, 
//			HttpServletResponse response, @ModelAttribute("finderInfo") FinderInfo finderInfo, Model model)
//			throws IOException {
//			request.getSession().setAttribute("user", finderInfo.getUsername());
//			request.getSession().setAttribute("pass", finderInfo.getPassword());
//			NTLMPassword ntlm = new NTLMPassword();
//			String password2 = ntlm.encode(finderInfo.getPassword());
//			ProcessBuilder pb = new ProcessBuilder("pdbedit", "-u", finderInfo.getUsername(), "-w");
//			Process p = pb.start();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			String readline;
//			while ((readline = reader.readLine()) != null) {
//			String[] parts = readline.split(":");
//			String part1 = parts[0];
//			String part4 = parts[3];
//			if (finderInfo.getUsername().equalsIgnoreCase(part1) && password2.equalsIgnoreCase(part4)) {
//				return "new";
//			}
//		}
//		return "login";
//	}
//	
//	@GetMapping("/finderInfo")
//	public String index(Model model) {
//		model.addAttribute("finderInfos", finderService.findAll());
//		return "list";
//	}
//	
//
//	@RequestMapping(value="/finderInfo/create", method=RequestMethod.POST)
//	public String create(Model model,HttpServletRequest hsr,@ModelAttribute("finderInfo")FinderInfo finderInfo) {
//		NTLMPassword ntlm = new NTLMPassword();
//		HttpSession session = hsr.getSession();
//		String user = session.getAttribute("user").toString();
//       String pass = session.getAttribute("pass").toString();
//       String pass2 = ntlm.encode(pass);
//		model.addAttribute("username", user);
//		model.addAttribute("password", pass2);
//		model.addAttribute("area", finderInfo.getArea());
//		System.out.println(finderInfo.getArea());
//		model.addAttribute("finderInfo", new FinderInfo());
//		return "form";
//	}
//
//	@GetMapping("/finderInfo/{id}/edit")
//	public String edit(@PathVariable int id, Model model) {
//		model.addAttribute("finderInfo", finderService.findOne(id));
//		return "form";
//	}		
//	
//	
//	@PostMapping("/finderInfo/save")
//	public String save(@Valid FinderInfo finderInfo, HttpServletRequest hsr, BindingResult result, RedirectAttributes redirect, Model model) {				
//		FinderInfo receptiExists = finderService.findByRecepti(finderInfo.getRecepti());			
//		if (receptiExists != null) {
////			NTLMPassword ntlm = new NTLMPassword();
////			HttpSession session = hsr.getSession();
////			String user = session.getAttribute("user").toString();
////	       String pass = session.getAttribute("pass").toString();
////	       String pass2 = ntlm.encode(pass);
////			model.addAttribute("username", user);			//lay dua sang form
////			model.addAttribute("password", pass2);
//			model.addAttribute("area", finderInfo.getArea());
//			System.out.println(finderInfo.getArea());
//			model.addAttribute("finderInfo", new FinderInfo());
//			model.addAttribute("InvalidRecepti", "This recepti is used !");			
//			return "form";
//		}		
//		
//		HttpSession session = hsr.getSession();
//		String user = session.getAttribute("user").toString();		
//		FinderInfo folderPathExists = finderService.findByUsernameAndFolderpath(user, finderInfo.getFolderpath());
//		if (folderPathExists != null){	
//			model.addAttribute("area", finderInfo.getArea());
//			model.addAttribute("user", finderInfo.getUsername());			
//			model.addAttribute("finderInfo", new FinderInfo());
//			model.addAttribute("InvalidFolder", "This Folder already exist !");
//			return "form";
//		}		
//	    
//		if (result.hasErrors()) {
//			return "form";
//		}
//
////		finderService.save(finderInfo);
////		redirect.addFlashAttribute("Success", "Saved data successfully!");	 
////
////		  try {
////			File file = new File("/root/Desktop/file.xml");
////			JAXBContext jaxbContext = JAXBContext.newInstance(FinderInfo.class);			
////			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
////			
////			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
////
////			jaxbMarshaller.marshal(finderInfo, file);
////			jaxbMarshaller.marshal(finderInfo, System.out);
////
////		      }
////		  catch (JAXBException e) {
////			e.printStackTrace();
////		      }	
////		return "form";
//		return "redirect:/finderInfo";				
//	}
//	
//
//	@GetMapping("/finderInfo/{id}/delete")
//	public String delete(@PathVariable int id, RedirectAttributes redirect) {
//		finderService.delete(id);
//		redirect.addFlashAttribute("Success", "Deleted data successfully!");
//		return "redirect:/finderInfo";
//	}
//
//	@GetMapping("/finderInfo/search")
//	public String search(@RequestParam("s") int s, Model model) {
//		if (s==0) {
//			return "redirect:/finderInfo";
//		}
//		model.addAttribute("finderInfos", finderService.search(s));
//		return "list";
//	}
//}
