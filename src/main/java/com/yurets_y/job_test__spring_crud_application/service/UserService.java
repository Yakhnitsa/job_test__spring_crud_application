package com.yurets_y.job_test__spring_crud_application.service;

import com.yurets_y.job_test__spring_crud_application.entity.UserAccount;
import com.yurets_y.job_test__spring_crud_application.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean containUser(Long id){
        return userRepo.existsById(id);
    }

    public UserAccount getUserAccountById(Long id){
        return userRepo.findById(id).orElse(null);
    }

    public UserAccount saveUserAccount(UserAccount account){
        return userRepo.saveAndFlush(account);
    }

    public boolean userNameExists(String userName) {
        return userRepo.existsByUserName(userName);
    }
}