package com.service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.entity.User;
import com.service.services.UserServices;

@RestController
@RequestMapping("/api/users")
public class UserController {

	
	private UserServices userService;

	public UserController(UserServices userService) {
		super();
		this.userService = userService;
	}
	
	@PostMapping
	public ResponseEntity<?> createUser(@RequestBody User user){
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id){
	return  userService.getUserById((id)).map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUser(){
		return ResponseEntity.ok(userService.getAllUser());
	}
}

