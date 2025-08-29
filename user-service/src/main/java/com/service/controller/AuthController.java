package com.service.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.dto.JwtResponse;
import com.service.dto.LoginRequest;
import com.service.dto.SignupRequest;
import com.service.entity.User;
import com.service.repository.UserRepository;
import com.service.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	
	public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignupRequest request){
		 Optional<User> exisitngUser=   userRepository.findByEmail(request.getEmail());
		    if(exisitngUser.isPresent()) {
		    	return ResponseEntity.badRequest().body("User already exists");
		    }
		    User user=new User();
		    user.setName(request.getName());
		    user.setEmail(request.getEmail());
		    user.setRole("ROLE_USER");
		    user.setPassword(passwordEncoder.encode(request.getPassword()));
		    userRepository.save(user);
		    User savedUser=userRepository.save(user);
		    return ResponseEntity.ok("User registered successfully");
	}
	
	@PostMapping("/login")
	public ResponseEntity<?>  login(@RequestBody LoginRequest request){
	Optional<User> useropt=	userRepository.findByEmail(request.getEmail());
	if(useropt.isEmpty()) {
		return ResponseEntity.status(401).body("User not found");
	}
	User user=useropt.get();
	if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
		return ResponseEntity.status(401).body("Invalid credential");
	}
	
	Map<String, Object> claims=new HashMap<>();
	claims.put("role", user.getRole());
	
	String token=jwtUtil.generateToken(claims, user.getEmail());
	return ResponseEntity.ok(new JwtResponse(token));
	}
	
}
