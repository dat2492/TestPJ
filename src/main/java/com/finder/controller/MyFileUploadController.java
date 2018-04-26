//package com.finder.controller;
//
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.util.ArrayList;
//import java.util.List;
// 
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import com.finder.model.MyUploadForm;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.multipart.MultipartFile;
// 
//@Controller
//public class MyFileUploadController {
// 
//
//  // GET: Hiển thị trang form upload
//    @RequestMapping(value = "/uploadMultiFile", method = RequestMethod.GET)
//    public String uploadMultiFileHandler(Model model) {
// 
//        MyUploadForm myUploadForm = new MyUploadForm();
//        model.addAttribute("myUploadForm", myUploadForm);
// 
//        return "upload";
//    }
// 
//    // POST: Sử lý Upload
//    @RequestMapping(value = "/uploadMultiFile", method = RequestMethod.POST)
//    public String uploadMultiFileHandlerPOST(HttpServletRequest request, //
//            Model model, //
//            @ModelAttribute("myUploadForm") MyUploadForm myUploadForm,HttpServletRequest hsr) {
// 
//        return this.doUpload(request, model, myUploadForm,hsr);
// 
//    }
// 
//    private String doUpload(HttpServletRequest request, Model model, //
//            MyUploadForm myUploadForm,HttpServletRequest hsr) {
//
//        // Thư mục gốc upload file.
//       HttpSession session = hsr.getSession();
//    	String user = session.getAttribute("user").toString();
//    	String folderpath = session.getAttribute("folderpath").toString();
//        String uploadRootPath = "/home/"+user+"/samba/"+folderpath+"/";
// 
//        File uploadRootDir = new File(uploadRootPath);
//        // Tạo thư mục gốc upload nếu nó không tồn tại.
//        if (!uploadRootDir.exists()) {
//            uploadRootDir.mkdirs();
//        }
//        MultipartFile[] fileDatas = myUploadForm.getFileDatas();
//        // 
//        List<File> uploadedFiles = new ArrayList<File>();
//        List<String> failedFiles = new ArrayList<String>();
// 
//        for (MultipartFile fileData : fileDatas) {
// 
//            // Tên file gốc tại Client.
//            String name = fileData.getOriginalFilename();
//            System.out.println("Client File Name = " + name);
// 
//            if (name != null && name.length() > 0) {
//                try {
//                    // Tạo file tại Server.
//                    File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);
// 
//                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
//                    stream.write(fileData.getBytes());
//                    stream.close();
//                    // 
//                    uploadedFiles.add(serverFile);
//                    ProcessBuilder pb = new ProcessBuilder("gpg", "--passphrase", "1234567890", "--batch", "--quiet", "--yes", "-ba", serverFile.toString());
//              	  Process p = pb.start();
//                    System.out.println("Write file: " + serverFile);
//                } catch (Exception e) {
//                    System.out.println("Error Write file: " + name);
//                    failedFiles.add(name);
//                }
//            }
//        }
//        model.addAttribute("uploadedFiles", uploadedFiles);
//        model.addAttribute("failedFiles", failedFiles);
//        return "uploadResult";
//    }
// 
//}
