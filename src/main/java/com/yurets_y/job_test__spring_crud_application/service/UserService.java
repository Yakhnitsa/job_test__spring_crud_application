package com.yurets_y.job_test__spring_crud_application.service;

import com.yurets_y.job_test__spring_crud_application.entity.UserAccount;
import com.yurets_y.job_test__spring_crud_application.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
//        if(!userIsValid(account)){
//            throw new RuntimeException("Error while user validation");
//        }
        return userRepo.saveAndFlush(account);
    }

    private boolean userIsValid(UserAccount account) {
        return account.getMoneyAmount() >= 0;
    }

    public boolean userNameExists(String userName) {
        return userRepo.existsByUserName(userName);
    }

    public List<UserAccount> getAll() {
        return userRepo.findAll();
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }
}
