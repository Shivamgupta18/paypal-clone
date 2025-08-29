package com.service.services;

import java.util.List;
import java.util.Optional;

import com.service.entity.User;

public interface UserServices {

	User createUser(User user);
	Optional<User> getUserById(Long id);
	List<User> getAllUser();
}
