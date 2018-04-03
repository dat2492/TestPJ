package com.finder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finder.model.FinderInfo;
import com.finder.repository.FinderRepository;

@Service("finderService")
public interface FinderService {
	
	Iterable<FinderInfo> findAll();

    List<FinderInfo> search(int q);
    
    List<FinderInfo> UserFilter(String username);
    
    List<FinderInfo> kwSearch(String area, String recepti,String date);
    
    FinderInfo searchf(String username, String folderpath);
    
    FinderInfo findOne(int id);

    void save(FinderInfo contact);
}
