package com.finder.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class certificationController {
	
	
	@GetMapping("/finderInfo/certification")
    public String dashboardPage(Model model,HttpServletRequest hsr) throws IOException {
		HttpSession session = hsr.getSession();
		String path = session.getAttribute("path").toString();
		String user = session.getAttribute("user").toString();
		model.addAttribute("username", user);
		model.addAttribute("path", path);
		Path path2 = Paths.get(path);
		BasicFileAttributes attr = Files.readAttributes(path2, BasicFileAttributes.class);
		model.addAttribute("creationTime", attr.creationTime());
		File dir = new File(path);
		File[] files = dir.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return !name.toLowerCase().endsWith(".asc");
		    }
		});
		ArrayList filPaths = new ArrayList();
		ArrayList filPaths2 = new ArrayList();
		ArrayList filPaths3 = new ArrayList();
		ArrayList filPaths4 = new ArrayList();
        for (File file : files) {
        	try {
	        	
	        	
        	ProcessBuilder pb = new ProcessBuilder("gpg","--verify",file.getAbsolutePath()+".asc",file.getAbsolutePath());
			Process p = pb.start();
			String error  = loadStream(p.getErrorStream());
			int rc = p.waitFor();
			if(error.contains("不")){
				filPaths.add(file.getName());
				Path path3 = Paths.get(file.getAbsolutePath()+".asc");
				BasicFileAttributes attr2 = Files.readAttributes(path3, BasicFileAttributes.class);
				model.addAttribute("creationTime1", attr2.creationTime());
				model.addAttribute("files", filPaths);
			}else {
				filPaths2.add(file.getName());
				Path path4 = Paths.get(file.getAbsolutePath()+".asc");
				BasicFileAttributes attr3 = Files.readAttributes(path4, BasicFileAttributes.class);
				model.addAttribute("creationTime2", attr3.creationTime());
		        model.addAttribute("files2", filPaths2);
			}
        	}catch(Exception e) {
        		e.printStackTrace();
        	}
        }

        return "certification";
    }
	private static String loadStream(InputStream s) throws Exception
	{
	    BufferedReader br = new BufferedReader(new InputStreamReader(s));
	    StringBuilder sb = new StringBuilder();
	    String line;
	    while((line=br.readLine()) != null)
	    sb.append(line).append("\n");
	    return sb.toString();
	}

}
