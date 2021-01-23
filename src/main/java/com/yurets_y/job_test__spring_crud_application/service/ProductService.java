package com.yurets_y.job_test__spring_crud_application.service;


import com.yurets_y.job_test__spring_crud_application.entity.Product;
import com.yurets_y.job_test__spring_crud_application.repository.ProductRepo;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ProductService {
    private ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }

    public Product getById(Long productId){
        return productRepo.getOne(productId);
    }


    public int getProductCountById(Long productId){
        return productRepo.existsById(productId) ?
                productRepo.getOne(productId).getCount() : 0;
    }
}
