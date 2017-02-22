package com.example.domain.models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;

/**
 * Represents the details of a sale
 */
@Entity
@Table(name = "SALE_DETAILS")
@EqualsAndHashCode(exclude={"id", "sale", "productPrice"})
public class SaleDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "sale_id", insertable = false, updatable = false)
    private Long saleId;

    @Column(name = "sale_price")
    private Double salePrice;

    @Column(name = "quantity")
    private Integer quantity;

    @JsonIgnore
    @ManyToOne
    private Sale sale;

    @ManyToOne
    private ProductPrice productPrice;

    public Long getId() {
        return id;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public ProductPrice getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(ProductPrice productPrice) {
        this.productPrice = productPrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public static Builder getBuilder(Sale sale, ProductPrice productPrice, int quantity) {
        return new Builder(sale).quantity(quantity).productPrice(productPrice);
    }

    public static class Builder {

        private SaleDetail built;

        Builder(Sale sale) {
            built = new SaleDetail();
            built.sale = sale;
        }

        public SaleDetail build() {
            return built;
        }

        public Builder quantity(int quantity) {
            built.quantity = quantity;
            return this;
        }

        public Builder productPrice(ProductPrice productPrice) {
            built.productPrice = productPrice;
            return this;
        }
    }

}
