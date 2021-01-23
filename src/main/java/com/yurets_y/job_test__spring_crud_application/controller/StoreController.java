package com.yurets_y.job_test__spring_crud_application.controller;


import com.yurets_y.job_test__spring_crud_application.dto.IdCountDto;
import com.yurets_y.job_test__spring_crud_application.entity.Product;
import com.yurets_y.job_test__spring_crud_application.entity.UserAccount;
import com.yurets_y.job_test__spring_crud_application.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class StoreController {

    private ProductService productService;

    public StoreController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("/pay")
    @ResponseBody
    public ResponseEntity testPost(
            @RequestParam(required = false, name="id") UserAccount userAccount,
            @RequestBody(required = false) List<IdCountDto> dtoList
    ){

        Map<Long,Integer> idAndCountMap = dtoList.stream()
                .collect(Collectors.toMap(IdCountDto::getId,IdCountDto::getCount));

        List<Product> products = productService.findByIdList(idAndCountMap.keySet());
        /*
            TODO - Проверить наличие товаров в нужном количестве
            - Свести сумму всех товаров
            - Проверить наличие необходимого количества средств на счету, и вернуть сколько не хватает
            - Провести операцию покупки товаров, сохранить операцию
            - Снять деньги со счета покупателя
            - Сохранить пользователя после транзакции
            - Вернуть данные про купленные товары в ответе

         */

        return ResponseEntity.ok("All is done");
    }
}
