package com.example.domain.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;

/**
 * Represents the price for a quantity of product
 */
@Entity
@Table(name = "PRODUCT_PRICES")
@EqualsAndHashCode(exclude={"id", "product"})
public class ProductPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    private Double price;

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


    public static Builder getBuilder(Product product, double price) {
        return new Builder(product).price(price);
    }

    public static class Builder {

        private ProductPrice built;

        Builder(Product product) {
            built = new ProductPrice();
            built.product = product;
        }

        public ProductPrice build() {
            return built;
        }

        public Builder price(double price) {
            built.price = price;
            return this;
        }
    }
}
