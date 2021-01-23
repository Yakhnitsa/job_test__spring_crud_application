package com.yurets_y.job_test__spring_crud_application.controller;


import com.yurets_y.job_test__spring_crud_application.service.ProductService;
import com.yurets_y.job_test__spring_crud_application.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    private UserService userService;

    private ProductService productService;

    //TODO delete all
    public MainController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String getMain(){
        return "index";
    }

    //TODO Delete test method before send

    @GetMapping("/test")
    @ResponseBody
    public ResponseEntity testGet(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer count,
            @RequestParam(required = false) String other
    ){

        int productCount = productService.getProductCountById(id);
        return ResponseEntity.ok(productCount);
    }

    @PostMapping("/test")
    @ResponseBody
    public ResponseEntity testPost(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer count,
            @RequestParam(required = false) String other
    ){


        return ResponseEntity.ok("All is done");
    }
}
