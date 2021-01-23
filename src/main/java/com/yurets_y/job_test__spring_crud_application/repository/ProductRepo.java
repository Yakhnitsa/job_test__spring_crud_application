package com.yurets_y.job_test__spring_crud_application.repository;

import com.yurets_y.job_test__spring_crud_application.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo  extends JpaRepository<Product,Long> {

}
