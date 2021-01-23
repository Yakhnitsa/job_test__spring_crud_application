package com.yurets_y.job_test__spring_crud_application.dto;

public class IdCountDto {

    private Long id;

    private Integer count;

    public IdCountDto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
