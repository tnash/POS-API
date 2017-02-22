package com.example.domain.models;

import javax.persistence.Column;
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

@Entity
@Table(name = "SALE_PRICE_ADJUSTMENT_DETAILS")
@EqualsAndHashCode(exclude={"id", "product", "sale"})
public class SalePriceAdjustmentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @Column(name = "quantity_to_adjust")
    private Integer quantityToAdjust;

    @Column(name = "price_adjustment_amount")
    private Double priceAdjustmentAmount;

    public SalePriceAdjustmentDetail() {
    }

    public SalePriceAdjustmentDetail(Sale sale, Product product, Integer quantityToAdjust, Double priceAdjustmentAmount) {
        this.sale = sale;
        this.product = product;
        this.quantityToAdjust = quantityToAdjust;
        this.priceAdjustmentAmount = priceAdjustmentAmount;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Integer getQuantityToAdjust() {
        return quantityToAdjust;
    }

    public void setQuantityToAdjust(Integer quantityToAdjust) {
        this.quantityToAdjust = quantityToAdjust;
    }

    public Double getPriceAdjustmentAmount() {
        return priceAdjustmentAmount;
    }

    public void setPriceAdjustmentAmount(Double priceAdjustmentAmount) {
        this.priceAdjustmentAmount = priceAdjustmentAmount;
    }

    public static class Builder {

        private SalePriceAdjustmentDetail built;

        Builder(Product product) {
            built = new SalePriceAdjustmentDetail();
            built.product = product;
        }

        public SalePriceAdjustmentDetail build() {
            return built;
        }

        public Builder quantityToAdjust(int quantity) {
            built.quantityToAdjust = quantity;
            return this;
        }

        public Builder priceAdjustmentAmount(double amount) {
            built.priceAdjustmentAmount = amount;
            return this;
        }
    }
}
