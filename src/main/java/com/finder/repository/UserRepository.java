package com.finder.repository;

import org.springframework.data.repository.CrudRepository;

import com.finder.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	User findByUsername(String username);
}
