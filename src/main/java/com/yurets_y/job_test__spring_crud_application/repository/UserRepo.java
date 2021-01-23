package com.yurets_y.job_test__spring_crud_application.repository;

import com.yurets_y.job_test__spring_crud_application.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends JpaRepository<UserAccount,Long> {

    boolean existsByUserName(String userName);

    boolean existsById(Long userId);
}
