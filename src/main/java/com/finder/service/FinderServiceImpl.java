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
        return finderRepository.findByIdIs(q);
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
}
