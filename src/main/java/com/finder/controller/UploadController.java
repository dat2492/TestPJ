package com.finder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finder.model.MyUploadForm;

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

@Controller
public class UploadController {

	@RequestMapping(value = "/uploadMultiFile", method = RequestMethod.GET)
    public String uploadMultiFileHandler(Model model) {
 
        MyUploadForm myUploadForm = new MyUploadForm();
        model.addAttribute("myUploadForm", myUploadForm);
 
        return "upload";
    }
	
	@RequestMapping(value = "/uploadMultiFile", method = RequestMethod.POST)
    public String uploadMultiFileHandlerPOST(HttpServletRequest request, //
            Model model, //
            @ModelAttribute("myUploadForm") MyUploadForm myUploadForm,HttpServletRequest hsr) {
 
        return this.doUpload(request, model, myUploadForm,hsr);
 
    }
	
	private String doUpload(HttpServletRequest request, Model model, //
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
                } catch (Exception e) {
                    System.out.println("Error Write file: " + name);
                    failedFiles.add(name);
                }
            }
        }
        model.addAttribute("uploadedFiles", uploadedFiles);
        model.addAttribute("failedFiles", failedFiles);
        return "uploadResult";
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
			request.setAttribute("message",
			        "Please select a file to upload");
			return "uploadStatus";
		}

		try {
			String directory = UPLOADED_FOLDER;
            File dir = new File(directory);
            if (!dir.exists()) dir.mkdirs();
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER
			        + file.getOriginalFilename());
			Files.write(path, bytes);
			ProcessBuilder pb = new ProcessBuilder("gpg", "--passphrase", "1234567890", "--batch", "--quiet", "--yes", "-ba", path.toString());
      	  Process p = pb.start();
			request.setAttribute("message",
			        "You have successfully uploaded '"
			                + file.getOriginalFilename() + "'");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "hello";

	}


}
