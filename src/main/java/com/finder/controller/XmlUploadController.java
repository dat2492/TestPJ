package com.finder.controller;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;



import com.finder.login.NTLMPassword;
import com.finder.model.FinderInfo;
import com.finder.model.MyUploadForm;
import com.finder.service.*;

@Controller
public class XmlUploadController {

	@Autowired
	private FinderService finderService;
	
	   // GET: Show upload form page.
	   @RequestMapping(value = "/uploadOneFile", method = RequestMethod.GET)
	   public String uploadOneFileHandler(Model model) {	 
	      MyUploadForm myUploadForm = new MyUploadForm();
	      model.addAttribute("myUploadForm", myUploadForm);	 
	      return "uploadOneFile";
	   }
	 
	   // POST: Do Upload
	   @RequestMapping(value = "/uploadOneFile", method = RequestMethod.POST)
	   public String uploadOneFileHandlerPOST(@ModelAttribute("finderInfo")FinderInfo finderInfo,HttpServletRequest request, Model model, MyUploadForm myUploadForm, HttpServletRequest hsr) {	 
	      return this.doUpload(finderInfo,request, model, myUploadForm, hsr);	 
	   }
	   
	   private String doUpload(@ModelAttribute("finderInfo")FinderInfo finderInfo, HttpServletRequest request, Model model, MyUploadForm myUploadForm, HttpServletRequest hsr) {
		 
		      // Root Directory.
			NTLMPassword ntlm = new NTLMPassword();
	       HttpSession session = hsr.getSession();
	    	String user = session.getAttribute("user").toString();
			String pass = session.getAttribute("pass").toString();
			String pass2 = ntlm.encode(pass);
			model.addAttribute("username", user);			
			model.addAttribute("password", pass2);
	    	String folderpath = myUploadForm.getFolderpath(); 			//lay tu html sang
	       String uploadRootPath = "/home/"+user+"/samba/nsr/"+folderpath+"/";
	       request.getSession().setAttribute("folderupload", folderpath);
	       
		      File uploadRootDir = new File(uploadRootPath);
		      // Create directory if it not exists.
		      if (!uploadRootDir.exists()) {
		         uploadRootDir.mkdirs();
		      }
		      MultipartFile[] fileDatas = myUploadForm.getFileDatas();		      
		      List<File> uploadedFiles = new ArrayList<File>();
		      List<String> failedFiles = new ArrayList<String>();
		 
		      for (MultipartFile fileData : fileDatas) {
		 
		         // Client File Name
		         String name = fileData.getOriginalFilename();
		         System.out.println("Client File Name = " + name);
		 
		         if (name != null && name.length() > 0) {
		            try {
		               // Create the file at server
		               File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);
		 
		               BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
		               stream.write(fileData.getBytes());
		               stream.close();
		               
		               uploadedFiles.add(serverFile);
		              request.getSession().setAttribute("filepath", serverFile);
		               System.out.println("Write file: " + serverFile);
		            } catch (Exception e) {
		               System.out.println("Error Write file: " + name);
		               failedFiles.add(name);
		            }
		         }
		      }
		     // model.addAttribute("uploadedFiles", uploadedFiles);
		     // model.addAttribute("failedFiles", failedFiles);


	//read XML 
    	   try {    		   
		  		File file = new File(session.getAttribute("filepath").toString());
		  		JAXBContext jaxbContext = JAXBContext.newInstance(FinderInfo.class);
		  		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		  		finderInfo = (FinderInfo) jaxbUnmarshaller.unmarshal(file);
		  		System.out.println(finderInfo.getArea());
		  		model.addAttribute("area", finderInfo.getArea());
		  		model.addAttribute("recepti", finderInfo.getRecepti());
		  		model.addAttribute("date", finderInfo.getDate());
		  		model.addAttribute("cause", finderInfo.getCause());
		  		model.addAttribute("estate", finderInfo.getEstate());
		  		model.addAttribute("lotnumber", finderInfo.getLotnumber());
		  		model.addAttribute("house", finderInfo.getHouse());
		  		model.addAttribute("folderpath", folderpath);
		  		System.out.println(folderpath);
		  	  } 
    	   		catch (JAXBException e) {
		  		e.printStackTrace();
		  	  }	  		
    	   	//	request.getSession().setAttribute("folderpath", uploadRootPath);
		      return "uploadResult";
	   }		      
	   
	 //and insert DB 
	   @RequestMapping("/finderInfo/saveupload")
		public String save(@Valid FinderInfo finderInfo, HttpServletRequest hsr, BindingResult result, RedirectAttributes redirect, Model model, HttpServletRequest request) throws IOException {
		   FinderInfo receptiExists = finderService.findByRecepti(finderInfo.getRecepti());		
			if (receptiExists != null) {
				model.addAttribute("area", finderInfo.getArea());			
				model.addAttribute("InvalidRecepti", "この受付番号は使用されています。別の受付番号を入力してください !");			
				return "uploadResult";
			}		

			
			if (result.hasErrors()) {
				return "uploadOneFile";
			}
			
			//System.out.println(finderInfo.getFolderpath());
			finderService.save(finderInfo);
			request.getSession().setAttribute("id", finderInfo.getId());
		       HttpSession session = hsr.getSession();
		    	String user = session.getAttribute("user").toString();
		    	String folderpath = session.getAttribute("folderupload").toString();
		    	String id = request.getSession().getAttribute("id").toString();
			try {
				String filexml = "/home/"+user+"/samba/nsr/"+folderpath+"/ID"+id+".xml";
				File file = new File(filexml);
				JAXBContext jaxbContext = JAXBContext.newInstance(FinderInfo.class);			
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();		
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.marshal(finderInfo, file);
				jaxbMarshaller.marshal(finderInfo, System.out);
			    ProcessBuilder pb = new ProcessBuilder("gpg", "--passphrase", "1234567890", "--batch", "--quiet", "--yes", "-ba", filexml);
			  	Process p = pb.start();
			      }
			  		catch (Exception e) {
			  			e.printStackTrace();
			      }
			
			ProcessBuilder pb = new ProcessBuilder("rm","-f",session.getAttribute("filepath").toString());
	       Process p = pb.start();
			
			return "redirect:/finderInfo";
	   }
}
