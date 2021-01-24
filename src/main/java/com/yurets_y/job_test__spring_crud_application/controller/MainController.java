package com.yurets_y.job_test__spring_crud_application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String getMain() {
        return "index";
    }

}
