package com.finder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finder.model.FinderInfo;
import com.finder.repository.FinderRepository;

@Service
public class FinderServiceImpl implements FinderService {
	@Autowired
    private FinderRepository finderRepository;

    @Override
    public Iterable<FinderInfo> findAll() {
        return finderRepository.findAll();
    }

    @Override
    public List<FinderInfo> search(int q) {
        return finderRepository.findById(q);
    }
    
    @Override
    public List<FinderInfo> kwSearch(String area, String recepti, String date) {
        return finderRepository.findByAreaAndReceptiAndDate(area, recepti, date);
    }

    @Override
    public List<FinderInfo> UserFilter(String username) {
        return finderRepository.findByUsername(username);
    }
    
    @Override
    public FinderInfo findOne(int id) {
        return finderRepository.findOne(id);
    }

    @Override
    public void save(FinderInfo contact) {
    	finderRepository.save(contact);
    }

    @Override
    public void delete(int id) {
    	finderRepository.delete(id);
    }

    @Override
	public FinderInfo findByRecepti(String recepti) {
		return finderRepository.findByRecepti(recepti);
	}

	@Override
	public FinderInfo findByUsernameAndFolderpath(String username, String folderpath) {		
		return finderRepository.findByUsernameAndFolderpath(username, folderpath);
	}

}
