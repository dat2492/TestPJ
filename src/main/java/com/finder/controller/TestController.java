package com.finder.controller;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finder.download.MediaTypeUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;



@Controller
public class TestController {
	@Autowired
    private ServletContext servletContext;
	@PostMapping("/Test")
    public String index(@RequestParam("img_val") String biteToRead,HttpServletRequest request,RedirectAttributes redirect) throws ExecuteException, IOException {
		String user = request.getSession().getAttribute("user").toString();
		String id = request.getSession().getAttribute("folderID").toString();
		String folder = request.getSession().getAttribute("folder").toString();
		String path = request.getSession().getAttribute("path").toString();
		String mergedPDF= "/home/"+user+"/tmp/"+folder+".pdf";
		String sigPdf = "/home/"+user+"/tmp/証明.pdf";
		String directory= "/home/"+user+"/samba/.verify/"+folder+"/";
		String partSeparator = ",";
		if (biteToRead.contains(partSeparator)) {
			  String encodedImg = biteToRead.split(partSeparator)[1];
			  byte[] decodedImg = Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8));
			  Path destinationFile = Paths.get("/home/"+user+"/tmp/", folder+".png");
			  Files.write(destinationFile, decodedImg);
			}
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
	    	  @Override
	    	  public void run() {
		try {
		Document document = new Document(PageSize.A4, -5f, -5f, -5f, -5f);
		PdfWriter.getInstance(document, new FileOutputStream(sigPdf));
		document.open();
		Image image = Image.getInstance("/home/"+user+"/tmp/"+folder+".png");
		document.add(image);
		document.close();
		}catch(Exception e) {
			e.printStackTrace();	
		}
	    	  }
    	}, 3000);
		File dir = new File(path);
		File[] files = dir.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".pdf");
		    }
		});
		ArrayList filPaths = new ArrayList();
		for (File file : files) {
			filPaths.add(file.getAbsolutePath());
        }
		String formattedString = filPaths.toString()
			    .replace(",", "")  //remove the commas
			    .replace("[", "")  //remove the right bracket
			    .replace("]", "")  //remove the left bracket
			    .trim();           //remove trailing spaces from partially initialized arrays
		System.out.println(formattedString);
		timer.schedule(new TimerTask() {
	    	  @Override
	    	  public void run() {
		
    	try {
    		String line ="gs -q -dNOPAUSE -dBATCH -sDEVICE=pdfwrite -sOutputFile="+mergedPDF+" "+sigPdf+" "+formattedString;
    		Runtime rt = Runtime.getRuntime();
    		Process pr = rt.exec(line);
    		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    	  }
    	}, 4000);
		timer.schedule(new TimerTask() {
	    	  @Override
	    	  public void run() {
	              try {
	            	ProcessBuilder pb = new ProcessBuilder("gpg", "--passphrase", "1234567890", "--batch", "--quiet", "--yes", "-ba",mergedPDF);
					Process p = pb.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	  }
    	}, 5000);
		timer.schedule(new TimerTask() {
	    	  @Override
	    	  public void run() {
	              try {
	            	  
	            	  File dir = new File(directory);
	                if (!dir.exists()) dir.mkdirs();
	            	  ProcessBuilder pb = new ProcessBuilder("pdftk",mergedPDF,"attach_files",mergedPDF+".asc","output",directory+folder+".pdf");
						Process p = pb.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	  }
  	}, 6000);
		timer.schedule(new TimerTask() {
	    	  @Override
	    	  public void run() {
	              try {
	            	ProcessBuilder pb = new ProcessBuilder("gpg", "--passphrase", "1234567890", "--batch", "--quiet", "--yes", "-ba",directory+folder+".pdf");
					Process p = pb.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	  }
  	}, 7000);
		timer.schedule(new TimerTask() {
	    	  @Override
	    	  public void run() {
	    		  File dir2 = new File("/home/"+user+"/tmp/");
	    		  for(File file: dir2.listFiles()) 
	    			    if (!file.isDirectory()) 
	    			        file.delete();
	    	  }
	}, 8000);
		request.getSession().setAttribute("pdfLocation", directory+folder+".pdf");
//		String line ="";
//		CommandLine commandLine = CommandLine.parse(line);
//		DefaultExecutor executor = new DefaultExecutor();
//		executor.setExitValue(1);
//		int exitValue = executor.execute(commandLine);
		try{
			Thread.sleep(9000);
			}catch(Exception e){}
        return "redirect:/pdfDownload";
    }
	
    @RequestMapping("/pdfDownload")
    public ResponseEntity<InputStreamResource> downloadFile1(HttpServletRequest request) throws IOException {
    	String folder = request.getSession().getAttribute("folder").toString();
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, folder+".pdf");
         String pdfLocation = request.getSession().getAttribute("pdfLocation").toString();
        File file = new File(pdfLocation);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
 
        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(file.length()) //
                .body(resource);
        
    }
}
