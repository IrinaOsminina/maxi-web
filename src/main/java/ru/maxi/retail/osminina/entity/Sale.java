package ru.maxi.retail.osminina.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@JacksonXmlRootElement(localName = "SALES")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "SALES")
public class Sale {

    @Id
    @GeneratedValue
    Integer id;

    @JacksonXmlProperty(localName = "CARD_NUMBER")
    String cardNumber;

    @JacksonXmlProperty(localName = "DATE")
    Date date;

    @JacksonXmlElementWrapper(localName = "PRODUCTS")
    @JacksonXmlProperty(localName = "PRODUCT")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> products;

    public void setDate(Long unixDate) {
        this.date = new Date(unixDate);
    }
}
