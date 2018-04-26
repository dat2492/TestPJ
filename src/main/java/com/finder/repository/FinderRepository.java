package com.finder.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.finder.model.*;

@Repository("finderRepository")
public interface FinderRepository extends CrudRepository<FinderInfo, Integer> {
    List<FinderInfo> findById(int q);
    
    List<FinderInfo> findByAreaAndReceptiAndDate(String area, String recepti, String date);
    
    FinderInfo findByUsernameAndFolderpath(String username, String folderpath );
    
    List<FinderInfo> findByUsername(String username);
    List<FinderInfo> findByIdIs(int q);
    
	FinderInfo findByRecepti(String recepti);
}
