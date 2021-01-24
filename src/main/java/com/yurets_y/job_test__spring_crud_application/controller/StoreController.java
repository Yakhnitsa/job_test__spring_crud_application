package com.yurets_y.job_test__spring_crud_application.controller;


import com.yurets_y.job_test__spring_crud_application.dto.IdCountDto;
import com.yurets_y.job_test__spring_crud_application.entity.Discount;
import com.yurets_y.job_test__spring_crud_application.entity.Product;
import com.yurets_y.job_test__spring_crud_application.entity.UserAccount;
import com.yurets_y.job_test__spring_crud_application.service.DiscountService;
import com.yurets_y.job_test__spring_crud_application.service.ProductService;
import com.yurets_y.job_test__spring_crud_application.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class StoreController {

    private ProductService productService;

    private UserService userService;

    private DiscountService discountService;

    public StoreController(ProductService productService,
                           UserService userService,
                           DiscountService discountService) {
        this.productService = productService;
        this.userService = userService;
        this.discountService = discountService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("add_product")
    public ResponseEntity<?> addProduct(
            @RequestBody Product product
    ) {
        if (product.getId() != null) {
            return ResponseEntity.badRequest().body("You try to use post request to save existing record");
        }
        Product storedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(storedProduct);
    }

    @PutMapping("update_product")
    public ResponseEntity<?> updateProduct(
            @RequestParam Long id,
            @RequestBody Product product
    ) {

        if(productService.existsById(id)){
            return ResponseEntity.badRequest().body("product id not found");
        }
        Product prodFromDb = productService.getById(id);

        prodFromDb.setName(product.getName());
        prodFromDb.setPrice(product.getPrice());
        prodFromDb.setCount(product.getCount());
        prodFromDb.setDiscount(product.getDiscount());
        prodFromDb = productService.saveProduct(prodFromDb);

        return ResponseEntity.ok(prodFromDb);
    }

    @DeleteMapping("delete_product")
    public ResponseEntity<?> deleteProduct(
            @RequestParam Long id
    ) {
        if (productService.existsById(id)) {
            productService.deleteProductById(id);
            return ResponseEntity.ok(id);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/pay")
    @ResponseBody
    public ResponseEntity testPost(
            @RequestParam Long id,
            @RequestBody List<IdCountDto> dtoList
    ) {
        if (!userService.containUser(id)) {
            return ResponseEntity.notFound().build();
        }
        UserAccount userAccount = userService.getUserAccountById(id);

        Map<Long, Integer> idAndCountMap = dtoList.stream()
                .collect(Collectors.toMap(IdCountDto::getId, IdCountDto::getCount));

        List<Product> products = productService.findByIdList(idAndCountMap.keySet());

        Map<Product, Integer> productsNCountMap = new HashMap<>();
        products.forEach(product -> {
            productsNCountMap.put(product, idAndCountMap.get(product.getId()));
        });

        if (!isProductsEnough(productsNCountMap)) {
            return ResponseEntity.badRequest().body("Not enough count of products in store");
        }

        //Проверка на наличие необходимой суммы денег
        Long moneyRequired = getProductsTotalPrice(productsNCountMap);

        if (moneyRequired > userAccount.getMoneyAmount()) {
            String message = notEnoughMoneyMessage(userAccount.getMoneyAmount(), moneyRequired);
            return ResponseEntity.status(402).body(message);
        }
        // Проведение транзакции
        productService.buyProducts(idAndCountMap);
        userAccount.setMoneyAmount(userAccount.getMoneyAmount() - moneyRequired);
        userService.saveUserAccount(userAccount);

        return ResponseEntity.ok(productsNCountMap);
    }

    @PostMapping("add_product_discount")
    public ResponseEntity<?> addDiscount(
            @RequestParam("product_id") Long productId,
            @RequestParam("discount_id") Long discountId
    ) {
        if(!productService.existsById(productId)){
            return ResponseEntity.badRequest().body("product id not found");
        }
        if(!discountService.existsById(discountId)){
            return ResponseEntity.badRequest().body("discount id not found");
        }
        Product product = productService.getById(productId);
        Discount discount = discountService.getById(discountId);

        product.setDiscount(discount);
        product = productService.saveProduct(product);
        return ResponseEntity.ok(product);
    }


    private String notEnoughMoneyMessage(Long moneyHas, Long moneyRequires) {
        return String.format("Not enough money on account: \n" +
                        "money on account: %.2f, required amount: %.2f",
                (float) moneyHas / 100,
                (float) moneyRequires / 100);
    }

    private Long getProductsTotalPrice(Map<Product, Integer> productsAndCount) {
        return productsAndCount.entrySet().stream()
                .map(entry -> {
                    Product product = entry.getKey();
                    Float priceWithDiscount = product.getDiscount() == null ?
                            product.getPrice() : product.getPrice() * (1 - product.getDiscount().getValue());
                    return priceWithDiscount * entry.getValue();
                }).mapToLong(Float::longValue).sum();
    }

    private Boolean isProductsEnough(Map<Product, Integer> productsMap) {

        for (Map.Entry<Product, Integer> entry : productsMap.entrySet()) {
            if (entry.getKey().getCount() < entry.getValue())
                return false;
        }
        return true;
    }
}
