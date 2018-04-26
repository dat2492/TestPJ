package com.finder.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finder.model.FinderInfo;

import com.finder.service.FinderService;
import com.finder.model.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

@Controller
public class UploadController {
	
@Autowired	
private FinderService finderService;

	@RequestMapping(value = "/uploadMultiFile", method = RequestMethod.GET)
    public String uploadMultiFileHandler(Model model) { 
		MyUploadForm myUploadForm = new MyUploadForm();
        model.addAttribute("myUploadForm", myUploadForm);
        return "upload";
    }
	
	@RequestMapping(value = "/uploadMultiFile", method = RequestMethod.POST)
    public String uploadMultiFileHandlerPOST(FinderInfo finderInfo, HttpServletRequest request, //
            Model model,  @ModelAttribute("myUploadForm") MyUploadForm myUploadForm,HttpServletRequest hsr) { 
        return this.doUpload(finderInfo, request, model, myUploadForm, hsr); 
    }
	
	private String doUpload(FinderInfo finderInfo, HttpServletRequest request, Model model, //
			MyUploadForm myUploadForm,HttpServletRequest hsr) {

        // Thư mục gốc upload file.
       HttpSession session = hsr.getSession();
    	String user = session.getAttribute("user").toString();
    	String folderpath = session.getAttribute("folderpath").toString();
       String uploadRootPath = "/home/"+user+"/samba/"+folderpath+"/";
 
        File uploadRootDir = new File(uploadRootPath);
        // Tạo thư mục gốc upload nếu nó không tồn tại.
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
        MultipartFile[] fileDatas = myUploadForm.getFileDatas();
        // 
        List<File> uploadedFiles = new ArrayList<File>();
        List<String> failedFiles = new ArrayList<String>();
 
        for (MultipartFile fileData : fileDatas) {
 
            // Tên file gốc tại Client.
            String name = fileData.getOriginalFilename();
            System.out.println("Client File Name = " + name);
 
            if (name != null && name.length() > 0) {
                try {
                    // Tạo file tại Server.
                    File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);
 
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(fileData.getBytes());
                    stream.close();
                    // 
                    uploadedFiles.add(serverFile);
                    ProcessBuilder pb = new ProcessBuilder("gpg", "--passphrase", "1234567890", "--batch", "--quiet", "--yes", "-ba", serverFile.toString());
              	  Process p = pb.start();
                    System.out.println("Write file: " + serverFile);
                }
                catch (Exception e) {
                    System.out.println("Error Write file: " + name);
                    failedFiles.add(name);
                }
            }
        }

        model.addAttribute("uploadedFiles", uploadedFiles);
        model.addAttribute("failedFiles", failedFiles);
        
       // pass info from form
		finderInfo.setArea(request.getSession().getAttribute("area").toString());
		finderInfo.setRecepti(request.getSession().getAttribute("recepti").toString());
		finderInfo.setDate(request.getSession().getAttribute("date").toString());
		finderInfo.setCause(request.getSession().getAttribute("cause").toString());
		finderInfo.setEstate(request.getSession().getAttribute("estate").toString());
		finderInfo.setLotnumber(request.getSession().getAttribute("lotnumber").toString());
		finderInfo.setHouse(request.getSession().getAttribute("house").toString());
		String id = request.getSession().getAttribute("id").toString();
		// make XML
	  try {
		String filexml = "/home/"+user+"/samba/"+folderpath+"/ID"+id+".xml";
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
        return "redirect:/finderInfo";        
    }	
	
	@GetMapping("/myUploadForm/delete")
	public String delete(HttpServletRequest hsr, RedirectAttributes redirect) {
		HttpSession session = hsr.getSession();
		finderService.delete(Integer.parseInt(session.getAttribute("id").toString()));
		return "redirect:/options";
	}
    
    @RequestMapping("/dropzone")
    public String home(){
        return "Dropzone";
    }

    @RequestMapping(value = "/upload2",
	        method = { RequestMethod.POST })
	public @ResponseBody Object upload(
	        @RequestParam("file") MultipartFile file,
	        HttpServletRequest request,HttpServletRequest hsr) {
    	HttpSession session = hsr.getSession();
    	String user = session.getAttribute("user").toString();
    	String folderpath = session.getAttribute("folderpath").toString();
       String UPLOADED_FOLDER = "/home/"+user+"/samba/"+folderpath+"/";
		System.out.println("upload() called");

		if (file.isEmpty()) {
			request.setAttribute("message", "Please select a file to upload");
			return "uploadStatus";
		}

		try {
			String directory = UPLOADED_FOLDER;
            File dir = new File(directory);
            if (!dir.exists()) dir.mkdirs();
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);
			ProcessBuilder pb = new ProcessBuilder("gpg", "--passphrase", "1234567890", "--batch", "--quiet", "--yes", "-ba", path.toString());
      	  Process p = pb.start();
			request.setAttribute("message", "You have successfully uploaded '" + file.getOriginalFilename() + "'");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "hello";

	}
}
