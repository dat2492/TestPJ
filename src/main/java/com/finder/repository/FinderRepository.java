package com.finder.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.finder.model.*;

public interface FinderRepository extends CrudRepository<FinderInfo, Integer> {

    List<FinderInfo> findById(int q);
    List<FinderInfo> findByIdIs(int q);
    FinderInfo findByRecepti(String recepti);
    
    List<FinderInfo> findByAreaAndReceptiAndDate(String area, String recepti, String date);
    
    FinderInfo findByUsernameAndFolderpath(String username, String folderpath );
    
    List<FinderInfo> findByUsername(String username);
    

}
