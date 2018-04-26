package com.finder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finder.model.*;
import com.finder.repository.*;

@Service("validationService")

public class ValidationServiceImpl implements ValidationService {

	@Autowired
	private ValidationRepository validationRepository;
	
	@Override
	public FinderInfo findReceptiByRecepti(String recepti) {
		return validationRepository.findByRecepti(recepti);
	}

	@Override
	public void saveFinderInfo(FinderInfo recepti) {
		// TODO Auto-generated method stub
		
	}

}
