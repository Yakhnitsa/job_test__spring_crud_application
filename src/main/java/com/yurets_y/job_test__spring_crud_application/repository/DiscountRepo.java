package com.yurets_y.job_test__spring_crud_application.repository;

import com.yurets_y.job_test__spring_crud_application.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepo extends JpaRepository<Discount,Long> {
}
