package com.yurets_y.job_test__spring_crud_application.repository;

import com.yurets_y.job_test__spring_crud_application.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo  extends JpaRepository<Product,Long> {

}
