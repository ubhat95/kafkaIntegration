package com.uttam.kafka.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uttam.kafka.dto.User;
import com.uttam.kafka.service.UserService;

@RequestMapping("/user")
@RestController
@Component
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("/register")
	public Set<Integer> registerUser(@RequestBody List<User> users){
		return userService.registerUsers(users);
	}
}
