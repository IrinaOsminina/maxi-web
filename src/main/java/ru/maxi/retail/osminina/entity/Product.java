package ru.maxi.retail.osminina.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue
    Integer id;

    @JacksonXmlProperty(localName = "PRODUCT_CODE")
    String productCode;

    @JacksonXmlProperty(localName = "NAME")
    String name;

    @JacksonXmlProperty(localName = "PRICE")
    BigDecimal price;

    @JacksonXmlProperty(localName = "COUNT")
    Integer count;

    public void setPrice(String price) {
        this.price = new BigDecimal(price.replaceAll(",","."));
    }

}
