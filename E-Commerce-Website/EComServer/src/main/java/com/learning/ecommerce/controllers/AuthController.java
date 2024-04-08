package com.learning.ecommerce.controllers;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import com.learning.ecommerce.dto.UserDto;
import com.learning.ecommerce.models.User;
import com.learning.ecommerce.services.AuthService;
import com.learning.ecommerce.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthService authService;
	@Autowired
	private JwtService jwtService;

//	@PostConstruct
//	public void initRoleAndUser() {
//		authService.initRoleAndUser();
//	}

	@PostMapping({"/signup"})
	public ResponseEntity<String> registerNewUser(@RequestBody UserDto userDto) {
		return authService.registerNewUser(userDto);
	}

	@GetMapping({"/forAdmin"})
	@PreAuthorize("hasRole('Admin')")
	public String forAdmin(){

		return "This URL is only accessible to the admin";
	}

	@GetMapping({"/forUser"})
	@PreAuthorize("hasRole('User')")
	public String forUser(){
		return "This URL is only accessible to the user";
	}



}
