package com.finder.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.finder.login.NTLMPassword;
import com.finder.model.FinderInfo;
import com.finder.model.User;
import com.finder.service.FinderService;
import com.finder.service.UserService;

@Controller
public class LoginController {
	@Autowired
	private UserService userService;
	
	@RequestMapping("/")
    public String home(){
        return "new";
    }
	
	@RequestMapping("/login")
    public String login(@ModelAttribute("user")User user) {
        return "login";
    }
    
	@RequestMapping(value="/access", method=RequestMethod.POST)
    public String access(HttpServletRequest request, 
            HttpServletResponse response,
            @ModelAttribute("user")User user,
            Model model) throws IOException{
		request.getSession().setAttribute("user", user.getUsername());
        request.getSession().setAttribute("pass", user.getPassword());
    	NTLMPassword ntlm = new NTLMPassword();
		String password2 = ntlm.encode(user.getPassword());
		ProcessBuilder pb = new ProcessBuilder("pdbedit","-u",user.getUsername(),"-w");
    	Process p = pb.start();
    	BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String readline;
        while ((readline = reader.readLine()) != null) {
        	String[] parts = readline.split(":");
        	String part1 = parts[0]; 
        	String part4 = parts[3];
        if (user.getUsername().equalsIgnoreCase(part1) &&
                password2.equalsIgnoreCase(part4)
                ) {
        	User userExists = userService.checkUser(part1);
        	if(userExists== null) {
        		userService.save(user);
        		Runtime rt = Runtime.getRuntime();
        		Process pr1 = rt.exec("mkdir -m  550 /home/"+part1+"/samba/");
        		Process pr2 = rt.exec("chown "+part1+":"+part1+" /home/"+part1+"/samba/");
        		Process pr3 = rt.exec("mkdir -m  775 /home/"+part1+"/samba/nsr/");
        		Process pr4 = rt.exec("chown "+part1+":"+part1+" /home/"+part1+"/samba/nsr/");
        		Process pr5 = rt.exec("mkdir -m  775 /home/"+part1+"/samba/free/ & chown");
        		Process pr6 = rt.exec("chown "+part1+":"+part1+" /home/"+part1+"/samba/free/");
        		Process pr7 = rt.exec("mkdir -m  777 /home/"+part1+"/samba/.verify/");
        		Process pr8 = rt.exec("chown "+part1+":"+part1+" /home/"+part1+"/samba/.verify/");
        		Process pr9 = rt.exec("mkdir -m  700 /home/tmp/"+part1+"/");
        		Process pr10 = rt.exec("chown "+part1+":"+part1+" /home/tmp/"+part1+"/");
        		Process pr11 = rt.exec("mkdir -m  700 /home/tmp/csv/"+part1+"/");
        		Process pr12 = rt.exec("chown "+part1+":"+part1+" /home/tmp/csv/"+part1+"/");
        	}
        	return "redirect:/options";
        }
    }
        return "login";
    }
}

