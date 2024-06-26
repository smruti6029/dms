package com.watsoo.dms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.watsoo.dms.dto.Response;
import com.watsoo.dms.service.RemarkService;



@RestController
@RequestMapping("/api/remarks")
public class RemarkController {

    @Autowired
    private RemarkService remarkService;

    @GetMapping
    public ResponseEntity<?> getAllRemarks() {
    	Response<?> customResponse = remarkService.getAllRemark();
		return new ResponseEntity<>(customResponse, HttpStatus.valueOf(customResponse.getResponseCode()));
    }

   
}

