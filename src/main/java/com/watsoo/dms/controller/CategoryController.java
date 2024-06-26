package com.watsoo.dms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watsoo.dms.dto.Response;
import com.watsoo.dms.service.CategoryService;

@RequestMapping("/api/category")
@RestController
public class CategoryController {
	
	
	@Autowired
	private CategoryService categoryService;
	
	
	@GetMapping("/getall")
	public ResponseEntity<?> getAllCategory() {
		Response<?> response = categoryService.getAllCategory();
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	
}
