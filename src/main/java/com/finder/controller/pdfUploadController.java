package com.finder.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

@Controller
public class pdfUploadController {

    //Save the uploaded file to this folder
    
    @GetMapping("/pdfUpload")
    public String index() {
        return "certificationUpload";
    }

    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   Model model,HttpServletRequest request) {
    	if (file.isEmpty()) {
        	model.addAttribute("message", "Please select a file to upload");
            return "certificationUpload";
        }
    	String user = request.getSession().getAttribute("user").toString();
    	String tmpFolder="/home/"+user+"/tmp/";
    	 String folder = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf("."));
         System.out.println(folder);
         String file2 = "/home/"+user+"/samba/.verify/"+folder+"/"+file.getOriginalFilename();
        
        
        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get( tmpFolder+ file.getOriginalFilename());
            Files.write(path, bytes);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        Timer timer = new Timer();
		timer.schedule(new TimerTask() {
	    	  @Override
	    	  public void run() {
	    		  File f = new File(file2);
	              if(f.exists() && !f.isDirectory()) { 
	            	  ProcessBuilder pb = new ProcessBuilder("gpg","--verify",file2+".asc",tmpFolder+file.getOriginalFilename());
	  	  			try {
	  	           Process p = pb.start();
	  	  			String error  = loadStream(p.getErrorStream());
	  	  			System.out.println(error);
	  	  			int rc = p.waitFor();
	  	  			if(error.contains("不")){
	  	  				model.addAttribute("message2", "このPDFファイルは改竄されています。");
	  	  			}else {
	  	  				model.addAttribute("message1", "ok。");
	  	  			}
	  	  			
	  	  			}catch(Exception e) {
	  	  				e.printStackTrace();
	  	  			}
	              }else {
	            	  model.addAttribute("message3", "このPDFファイルはFinderサーバーで発行されたものではありません");
	              }
	           
	    	  }
    	}, 500);
		timer.schedule(new TimerTask() {
	    	  @Override
	    	  public void run() {
	    		  File dir = new File("/home/"+user+"/tmp/");
	    		  for(File file: dir.listFiles()) 
	    			    if (!file.isDirectory()) 
	    			        file.delete();
	    	  }
    	}, 700);
		try{
			Thread.sleep(1000);
			}catch(Exception e){}
        return "certificationUpload";
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
