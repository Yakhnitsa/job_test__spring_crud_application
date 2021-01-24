package com.yurets_y.job_test__spring_crud_application.service;

import com.yurets_y.job_test__spring_crud_application.entity.Discount;
import com.yurets_y.job_test__spring_crud_application.repository.DiscountRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountService {

    private DiscountRepo discountRepo;

    public DiscountService(DiscountRepo discountRepo) {
        this.discountRepo = discountRepo;
    }

    public List<Discount> getAll(){
        return discountRepo.findAll();
    }

    public Boolean existsById(Long id){
        return discountRepo.existsById(id);
    }

    public Discount getById(Long id){
        return discountRepo.getOne(id);
    }

    public Discount saveDiscount(Discount discount){
        if(discount == null){
            throw new NullPointerException("Error while saving Discount, discount must not be null");
        }
        return discountRepo.save(discount);
    }
    public void deleteDiscount(Long id){
        discountRepo.deleteById(id);
    }

}
