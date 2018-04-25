package com.finder.service;


import com.finder.model.User;

public interface UserService {
	User checkUser(String username);
	void save(User contact);
}
