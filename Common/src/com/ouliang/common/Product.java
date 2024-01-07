package com.ouliang.common;

import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 2L;
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Product(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
