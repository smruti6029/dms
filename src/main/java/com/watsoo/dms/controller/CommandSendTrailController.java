package com.watsoo.dms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.watsoo.dms.dto.Response;
import com.watsoo.dms.service.CommandSendTrailService;

@RequestMapping("/api/command/trail")
@RestController
public class CommandSendTrailController {

	@Autowired
	private CommandSendTrailService commandSendTrailService;

	@GetMapping
	public ResponseEntity<?> getCommandTrailByVechileId(@RequestParam Long vechileId) {
		Response<?> response = commandSendTrailService.getAllCommandByVechileId(vechileId);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));

	}

}
