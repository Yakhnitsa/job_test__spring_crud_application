package com.yurets_y.job_test__spring_crud_application.repository;

import com.yurets_y.job_test__spring_crud_application.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo  extends JpaRepository<Product,Long> {

    //TODO not tested yet
    @Query("SELECT product.count FROM Product product " +
            "WHERE product.id = :id")
    Integer getProductCountById(Long id);

}
