package com.yurets_y.job_test__spring_crud_application.controller;

import com.yurets_y.job_test__spring_crud_application.entity.UserAccount;
import com.yurets_y.job_test__spring_crud_application.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserAccount> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(
            @PathVariable("id") Long userId

    ) {
        if (userService.containUser(userId)) {
            return ResponseEntity.ok(userService.getUserAccountById(userId));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create_user")
    public ResponseEntity<?> createUser(@RequestParam String userName) {

        if (userService.userNameExists(userName)) {
            return ResponseEntity.badRequest().body("User with such name is already exists");
        }

        UserAccount newUser = new UserAccount(userName);
        newUser = userService.saveUserAccount(newUser);
        return ResponseEntity.ok(newUser);
    }

    @PutMapping("update_user")
    public ResponseEntity<?> updateUser(
            @RequestParam Long id,
            @RequestBody UserAccount newUser
    ) {
        if (!userService.containUser(id)) {
            return ResponseEntity.badRequest()
                    .body(String.format("Account with id \"%s\" not found", id));
        }
        if (!newUser.getId().equals(id)) {
            String error = String.format("Request parameter id: \"%s\" don't match account id: \"%s\"",
                    id, newUser.getId());
            return ResponseEntity.badRequest().body(error);
        }
        UserAccount userFromDb = userService.getUserAccountById(id);
        userFromDb.setUserName(newUser.getUserName());
        userFromDb = userService.saveUserAccount(userFromDb);
        return ResponseEntity.ok(userFromDb);
    }

    @DeleteMapping("delete_user")
    public ResponseEntity<?> deleteUser(
            @RequestParam Long id
    ) {
        if (userService.containUser(id)) {
            userService.deleteUser(id);
            return ResponseEntity.ok(id);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/add_money")
    public ResponseEntity<?> addMoneyToAccount(
            @RequestParam("id") UserAccount userFromDb,
            @RequestParam("name") String userName,
            @RequestParam("amount") Long moneyAmount
    ) {
        if (moneyAmount < 0) {
            return ResponseEntity.badRequest().body("refill sum must be positive number");
        }
        if (!userFromDb.getUserName().equals(userName)) {
            return ResponseEntity.badRequest().body("User id don't match user name");
        }

        userFromDb.setMoneyAmount(userFromDb.getMoneyAmount() + moneyAmount);
        UserAccount savedUser = userService.saveUserAccount(userFromDb);
        return ResponseEntity.ok(savedUser);
    }
}
