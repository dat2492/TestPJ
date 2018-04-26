package com.finder.annotation;

import javax.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.finder.repository.FinderRepository;


public class UniqueReceptiValidator implements ConstraintValidator<UniqueRecepti, String> {

	@Autowired
	private FinderRepository finderRepository;
	
	@Override
	public void initialize(UniqueRecepti constraintAnnotaion) {		
		
	}

	@Override
	public boolean isValid(String recepti, ConstraintValidatorContext context) {		
		if (finderRepository == null){
			return true;
		}
		return finderRepository.findByRecepti(recepti) == null;
	}

}
