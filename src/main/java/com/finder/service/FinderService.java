package com.finder.service;

import java.util.List;
import com.finder.model.FinderInfo;

public interface FinderService {
	Iterable<FinderInfo> findAll();

    List<FinderInfo> search(int q);
    
    List<FinderInfo> UserFilter(String username);
    
    List<FinderInfo> kwSearch(String area, String recepti,String date);
    
    FinderInfo findOne(int id);

    void save(FinderInfo contact);

    void delete(int id);
    
    FinderInfo findByRecepti(String recepti); 
    
    FinderInfo findByUsernameAndFolderpath(String username, String folderpath);
}
