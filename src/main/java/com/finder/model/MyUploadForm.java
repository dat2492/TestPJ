package com.finder.model;

//import javax.xml.bind.annotation.XmlElement;

import org.springframework.web.multipart.MultipartFile;
import com.finder.model.*;

public class MyUploadForm {

 
    // Upload files.
    private MultipartFile[] fileDatas;
    
 
    public MultipartFile[] getFileDatas() {
        return fileDatas;
    }
 
    public void setFileDatas(MultipartFile[] fileDatas) {
        this.fileDatas = fileDatas;
    }
    
   private String folderpath;
   
	public String getFolderpath() {
		return folderpath;
	}
	
	public void setFolderpath(String folderpath) {
		this.folderpath = folderpath;
	}
 
}
