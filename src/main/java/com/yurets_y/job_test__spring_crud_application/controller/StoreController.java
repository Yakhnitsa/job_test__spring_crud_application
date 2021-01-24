package com.yurets_y.job_test__spring_crud_application.controller;


import com.yurets_y.job_test__spring_crud_application.dto.IdCountDto;
import com.yurets_y.job_test__spring_crud_application.entity.Discount;
import com.yurets_y.job_test__spring_crud_application.entity.Product;
import com.yurets_y.job_test__spring_crud_application.entity.UserAccount;
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

    public StoreController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
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
            @RequestParam("id") Product prodFromDb,
            @RequestBody Product product
    ) {
//        if (prodFromDb == null) {
//            return ResponseEntity.badRequest().body("No record with such id");
//        }
//        if(prodFromDb.getId().equals(product.getId())){
//            return  ResponseEntity.badRequest().body("Product id do not match request parameter id");
//        }

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
    ){
        if(productService.existsById(id)){
            productService.deleteProductById(id);
            return ResponseEntity.ok(id);
        }
        return ResponseEntity.notFound().build();
    }

    //TODO not tested yet
    @PostMapping("/pay")
    @ResponseBody
    public ResponseEntity testPost(
            @RequestParam(required = false, name = "id") UserAccount userAccount,
            @RequestBody(required = false) List<IdCountDto> dtoList
    ) {

        Map<Long, Integer> idAndCountMap = dtoList.stream()
                .collect(Collectors.toMap(IdCountDto::getId, IdCountDto::getCount));

        List<Product> products = productService.findByIdList(idAndCountMap.keySet());

        Map<Product, Integer> productsNCountMap = new HashMap<>();
        products.forEach(product -> {
            productsNCountMap.put(product, idAndCountMap.get(product.getId()));
        });

        if (!isProductsEnough(productsNCountMap)){
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

        /*
            TODO - Проверить наличие товаров в нужном количестве
            + Свести сумму всех товаров
            + Проверить наличие необходимого количества средств на счету, и вернуть сколько не хватает
            + Провести операцию покупки товаров, сохранить операцию
            + Снять деньги со счета покупателя
            + Сохранить пользователя после транзакции
            + Вернуть данные про купленные товары в ответе
            - Протестировать всю єту хрень!!!

         */

        return ResponseEntity.ok(productsNCountMap);
    }

    @PostMapping("add_product_discount")
    public ResponseEntity<?> addDiscount(
            @RequestParam("product_id") Product product,
            @RequestParam("discount_id") Discount discount
    ) {
        if (product == null && discount == null) {
            return ResponseEntity.badRequest().body("Product id or discount id haven't found in database");
        }
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
            if(entry.getKey().getCount() < entry.getValue())
                return false;
        }
        return true;
    }
}
