package com.sarc.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/")
    public String hello() {
        return "SARC API is running";
    }
}
