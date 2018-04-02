package com.finder.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.finder.login.NTLMPassword;
import com.finder.model.FinderInfo;

@Controller
public class LoginController {
	@RequestMapping("/")
    public String home(){
        return "new";
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
}

