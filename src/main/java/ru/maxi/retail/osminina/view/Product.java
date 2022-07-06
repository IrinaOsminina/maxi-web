package ru.maxi.retail.osminina.view;

public class Product {

    String productCode;

    String name;

    String count;


    public Product(String name, String productCode, String count) {
        this.name = name;
        this.productCode = productCode;
        this.count = count;
    }
}
