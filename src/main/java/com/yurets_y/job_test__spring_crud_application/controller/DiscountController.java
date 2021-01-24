package com.yurets_y.job_test__spring_crud_application.controller;

import com.yurets_y.job_test__spring_crud_application.entity.Discount;
import com.yurets_y.job_test__spring_crud_application.service.DiscountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DiscountController {

    private DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping("/discounts")
    public ResponseEntity getAll() {
        return ResponseEntity.ok(discountService.getAll());
    }

    @GetMapping("/discount/{id}")
    public ResponseEntity getOne(
            @PathVariable Long id
    ) {
        if(!discountService.existsById(id))
            return ResponseEntity.notFound().build();

        Discount discount = discountService.getById(id);
        return ResponseEntity.ok(discount);
    }

    @PostMapping("/add_discount")
    public ResponseEntity<?> addDiscount(
            @RequestBody Discount discount
    ) {
        if (discount == null) {
            return ResponseEntity.badRequest().body("Discount must not be null");
        }
        discount = discountService.saveDiscount(discount);
        return ResponseEntity.ok(discount);

    }

    @PutMapping("update_discount")
    public ResponseEntity<?> updateDiscount(
            @RequestParam("id") Discount discountFromDb,
            @RequestBody Discount discountFromServer

    ) {
        if (discountFromDb == null && discountFromServer == null) {
            return ResponseEntity.badRequest().body("Wrong parameters");
        }

        discountFromDb.setName(discountFromServer.getName());
        discountFromDb.setValue(discountFromServer.getValue());
        discountFromServer = discountService.saveDiscount(discountFromDb);
        return ResponseEntity.ok(discountFromServer);

    }

    @DeleteMapping("delete_discount")
    public ResponseEntity<?> deleteDiscount(
            @RequestParam("id") Discount discount
    ) {
        if (discount == null) {
            return ResponseEntity.notFound().build();
        }
        discountService.deleteDiscount(discount.getId());
        return ResponseEntity.ok(discount);

    }
}
