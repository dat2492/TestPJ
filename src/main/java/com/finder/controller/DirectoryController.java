package com.finder.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DirectoryController {
	
	
	@GetMapping("/finderInfo/{folder}/check")
    public String dashboardPage(Model model,@PathVariable String folder,HttpServletRequest hsr,HttpServletRequest request) {
		HttpSession session = hsr.getSession();
    	String user = session.getAttribute("user").toString();
    	String path = "/home/"+user+"/samba/"+folder+"/";
    	request.getSession().setAttribute("path", path);
		File dir = new File(path);
		File[] files = dir.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return !name.toLowerCase().endsWith(".asc");
		    }
		});

        ArrayList filPaths = new ArrayList();
        for (File file : files) {
            filPaths.add(file.getName());
        }
        model.addAttribute("files", filPaths);

        return "folderCheck";
    }

}
