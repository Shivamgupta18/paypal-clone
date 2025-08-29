package com.service.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.service.entity.User;
import com.service.repository.UserRepository;

@Service
public class UserServiceImpl implements UserServices {

	private UserRepository userRepository;
	@Override
	public User createUser(User user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}

	@Override
	public Optional<User> getUserById(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id);
	}

	@Override
	public List<User> getAllUser() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

}
