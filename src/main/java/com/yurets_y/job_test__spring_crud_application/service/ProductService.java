package com.yurets_y.job_test__spring_crud_application.service;

import com.yurets_y.job_test__spring_crud_application.entity.Product;
import com.yurets_y.job_test__spring_crud_application.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    private ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getById(Long productId) {
        return productRepo.getOne(productId);
    }

    public int getProductCountById(Long productId) {
        return productRepo.existsById(productId) ?
                productRepo.getOne(productId).getCount() : 0;
    }

    public List<Product> findByIdList(Iterable<Long> idList) {
        return productRepo.findAllById(idList);
    }

    public List<Product> buyProducts(Map<Long, Integer> productsAndCount) {
        List<Product> products = findByIdList(productsAndCount.keySet());
        products.forEach(product -> {
            int count = productsAndCount.get(product.getId());
            if (count > product.getCount()) {
                throw new RuntimeException("Not enough product count " + product.getName());
            }
            product.setCount(product.getCount() - count);
        });
        return productRepo.saveAll(products);
    }

    public Product saveProduct(Product product) {
        if(product == null){
            throw new NullPointerException("Exception while saving product, product must not me null");
        }
        return productRepo.save(product);

    }

    public boolean existsById(Long id) {
        return productRepo.existsById(id);
    }

    public void deleteProductById(Long id) {
        productRepo.deleteById(id);
    }
}
