package com.kcc.groo.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@GetMapping("/api/test")
	public String apiTest () {
		return "api test success";
	}

}
