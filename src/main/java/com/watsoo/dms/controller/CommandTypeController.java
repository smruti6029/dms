package com.watsoo.dms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watsoo.dms.dto.CommandTypeDTO;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.service.CommandTypeService;

@RestController
@RequestMapping("/api/command-types")
public class CommandTypeController {

	@Autowired
	private CommandTypeService commandTypeService;

	@PostMapping
	public ResponseEntity<?> createCommandType(@RequestBody CommandTypeDTO commandTypeDTO) {
		Response<?> response = commandTypeService.createCommandType(commandTypeDTO);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}
	@GetMapping("/{id}")
	public ResponseEntity<?> getCommandTypeById(@PathVariable Integer id) {
		Response<?> response = commandTypeService.getCommandTypeById(id);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	@GetMapping
	public ResponseEntity<?> getAllCommandTypes() {
		Response<?> response = commandTypeService.getAllCommandTypes();
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

}
