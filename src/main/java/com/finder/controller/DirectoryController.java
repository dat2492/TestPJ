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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DirectoryController {
	
	
	@GetMapping("/finderInfo/{id}/{folder}/check")
    public String dashboardPage(Model model,@PathVariable("folder") String folder,@PathVariable("id") String id,HttpServletRequest hsr,HttpServletRequest request) {
		HttpSession session = hsr.getSession();
    	String user = session.getAttribute("user").toString();
    	String path = "/home/"+user+"/samba/nsr/"+folder+"/";
    	session.setAttribute("folderID", id);
    	session.setAttribute("folder", folder);
    	request.getSession().setAttribute("path", path);
    	System.out.println(path);
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
