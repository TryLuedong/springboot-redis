package com.example.demo.controller;

import com.example.demo.Service.AccessLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestControler {

    @GetMapping("accessLimit")
    @AccessLimit(seconds = 3, maxCount = 5)
    public String testAccessLimit() {
        //xxxx
        return "qqqq";
    }
}