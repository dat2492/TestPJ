package com.finder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finder.model.User;
import com.finder.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
    private UserRepository userRepository;
	@Override
    public void save(User contact) {
    	userRepository.save(contact);
    }
	
	public User checkUser(String username) {
    	return userRepository.findByUsername(username);
    }
}
