package com.finder.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
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
        		String line = "mkdir -m  550 /home/"+part1+"/samba/ & chown "+part1+":"+part1+" /home/"+part1+"/samba/ & "
        			+	"mkdir -m  775 /home/"+part1+"/samba/nsr/ & chown "+part1+":"+part1+" /home/"+part1+"/samba/nsr/ & "
        				+ "mkdir -m  775 /home/"+part1+"/samba/free/ & chown "+part1+":"+part1+" /home/"+part1+"/samba/free/ & "
        						+ "mkdir -m  777 /home/"+part1+"/samba/.verify/ & chown "+part1+":"+part1+" /home/"+part1+"/samba/.verify/ & "
        								+ "mkdir -m  700 /home/tmp/"+part1+"/ & chown "+part1+":"+part1+" /home/tmp/"+part1+"/ & "
        										+ "mkdir -m  700 /home/tmp/csv/"+part1+"/ & chown "+part1+":"+part1+" /home/tmp/csv/"+part1+"/";
        		CommandLine commandLine = CommandLine.parse(line);
        		DefaultExecutor executor = new DefaultExecutor();
        		executor.setExitValue(1);
        		int exitValue = executor.execute(commandLine);

        	}
        	return "redirect:/options";
        }
    }
        return "login";
    }
}

