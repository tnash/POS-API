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

/**
 * Represents pricing adjustments based on quantity
 */
@Entity
@Table(name = "PRODUCT_PRICE_ADJUSTMENTS")
@EqualsAndHashCode(exclude={"id", "product"})
public class ProductPriceAdjustment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity_to_apply_adjustment")
    private Integer quantityToApplyAdjustment;

    @Column(name = "price_adjustment_amount")
    private Double priceAdjustmentAmount;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantityToApplyAdjustment() {
        return quantityToApplyAdjustment;
    }

    public void setQuantityToApplyAdjustment(Integer quantityToApplyAdjustment) {
        this.quantityToApplyAdjustment = quantityToApplyAdjustment;
    }

    public Double getPriceAdjustmentAmount() {
        return priceAdjustmentAmount;
    }

    public void setPriceAdjustmentAmount(Double priceAdjustmentAmount) {
        this.priceAdjustmentAmount = priceAdjustmentAmount;
    }

    public static Builder getBuilder(Product product, Integer quantityToApplyAdjustment, Double priceAdjustmentAmount) {
        return new Builder(product).priceAdjustmentAmount(priceAdjustmentAmount).quantityToApplyAdjustment(quantityToApplyAdjustment);
    }

    public static class Builder {

        private ProductPriceAdjustment built;

        Builder(Product product) {
            built = new ProductPriceAdjustment();
            built.product = product;
        }

        public ProductPriceAdjustment build() {
            return built;
        }

        public Builder quantityToApplyAdjustment(int quantity) {
            built.quantityToApplyAdjustment = quantity;
            return this;
        }

        public Builder priceAdjustmentAmount(double amount) {
            built.priceAdjustmentAmount = amount;
            return this;
        }
    }
}
