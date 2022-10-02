package com.foilen.etudes.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/user")
    public Object userDetails(Authentication authentication) {
        Map<String, Object> result = new HashMap<>();
        result.put("name", authentication.getName());
        return result;
    }

}
