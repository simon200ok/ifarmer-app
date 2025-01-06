package com.ifarmr.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vapid")
public class VapidKeyController {

    @Value("${vapid.public.key}")
    private String publicKey;

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/public-key")
    public String getPublicKey() {
        return publicKey;
    }
}