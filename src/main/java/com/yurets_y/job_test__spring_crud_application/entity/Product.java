package com.yurets_y.job_test__spring_crud_application.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Product name required")
    private String name;

    @Min(value = 0L, message = "Product price must be positive")
    private Integer price;

    @Min(value = 0L, message = "Product count must be positive")
    private Integer count;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="discount_id")
    private Discount discount;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public Long getId() {
        return id;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
}
