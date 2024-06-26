package com.watsoo.dms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.watsoo.dms.dto.LoginRequest;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.dto.UserDto;
import com.watsoo.dms.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/adduser")
	public ResponseEntity<?> saveUser(@RequestBody UserDto userDto) {
		Response<?> customResponse = userService.saveUser(userDto);
		return new ResponseEntity<>(customResponse, HttpStatus.valueOf(customResponse.getResponseCode()));

	}

	@PostMapping("/login")
	public ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest) throws Exception {
		Response<?> loginResponse = userService.login(loginRequest);
		return new ResponseEntity<>(loginResponse, HttpStatus.valueOf(loginResponse.getResponseCode()));

	}

	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllUser(@RequestParam("userName") String userName) {
		Response<?> customResponse = userService.searchByName(userName);
		return new ResponseEntity<>(customResponse, HttpStatus.valueOf(customResponse.getResponseCode()));

	}

	@GetMapping("/getByid")
	public ResponseEntity<?> getUserById(@RequestParam("userId") Long userId) {
		Response<?> customResponse = userService.getById(userId);
		return new ResponseEntity<>(customResponse, HttpStatus.valueOf(customResponse.getResponseCode()));

	}

}
