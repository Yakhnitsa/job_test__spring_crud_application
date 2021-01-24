package com.yurets_y.job_test__spring_crud_application.controller;

import com.yurets_y.job_test__spring_crud_application.entity.UserAccount;
import com.yurets_y.job_test__spring_crud_application.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserAccount> getAllUsers(){
        return userService.getAll();
    }

    @PostMapping("/create_user")
    public ResponseEntity<?> createUser( @RequestParam String userName){

        if(userService.userNameExists(userName)){
            return ResponseEntity.badRequest().body("User with such name is already exists");
        }

        UserAccount newUser = new UserAccount(userName);
        newUser = userService.saveUserAccount(newUser);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/add_money")
    public ResponseEntity<?> addMoneyToAccount(
            @RequestParam("id") UserAccount userFromDb,
            @RequestParam("name") String userName,
            @RequestParam("amount") Long moneyAmount
    ){
        if(moneyAmount < 0){
            return ResponseEntity.badRequest().body("refill sum must be positive number");
        }
        if(!userFromDb.getUserName().equals(userName)){
            return ResponseEntity.badRequest().body("User id don't match user name");
        }

        userFromDb.setMoneyAmount(userFromDb.getMoneyAmount() + moneyAmount);
        UserAccount savedUser = userService.saveUserAccount(userFromDb);
        return ResponseEntity.ok(savedUser);
    }
}
