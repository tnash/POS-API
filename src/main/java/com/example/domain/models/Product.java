package com.example.domain.models;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;

/**
 * Domain model for Product.
 */
@Entity
@Table(name = "PRODUCTS")
@EqualsAndHashCode(exclude={"id", "productPrices", "productPriceAdjustments"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ProductPrice> productPrices;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ProductPriceAdjustment> productPriceAdjustments;

//    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
//    private Set<SalePriceAdjustmentDetail> salePriceAdjustmentDetails;

    private String sku;

    public Long getId() {
        return id;
    }

    public Set<ProductPrice> getProductPrices() {
        return productPrices;
    }

    public void setProductPrices(Set<ProductPrice> productPrices) {
        this.productPrices = productPrices;
    }

    public Set<ProductPriceAdjustment> getProductPriceAdjustments() {
        return productPriceAdjustments;
    }

    public void setProductPriceAdjustments(Set<ProductPriceAdjustment> productPriceAdjustments) {
        this.productPriceAdjustments = productPriceAdjustments;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

//    public Set<SalePriceAdjustmentDetail> getSalePriceAdjustmentDetails() {
//        return salePriceAdjustmentDetails;
//    }

//    public void setSalePriceAdjustmentDetails(Set<SalePriceAdjustmentDetail> salePriceAdjustmentDetails) {
//        this.salePriceAdjustmentDetails = salePriceAdjustmentDetails;
//    }

    public static Builder getBuilder(String sku) {
        return new Builder(sku);
    }

    public static class Builder {

        private Product built;

        public Builder(String sku) {
            built = new Product();
            built.setProductPrices(new HashSet<ProductPrice>());
            built.setProductPriceAdjustments(new HashSet<ProductPriceAdjustment>());
//            built.setSalePriceAdjustmentDetails(new HashSet<SalePriceAdjustmentDetail>());
            built.sku = sku;
        }

        public Product build() {
            return built;
        }

        public Builder sku(String sku) {
            built.sku = sku;
            return this;
        }

        public Builder productPrice(ProductPrice productPrice) {
            if (built.productPrices == null) {
                built.productPrices = new HashSet<ProductPrice>();
            }
            built.productPrices.add(productPrice);
            return this;
        }

        public Builder productPriceAdjustment(ProductPriceAdjustment productPriceAdjustment) {
            if (built.productPriceAdjustments == null) {
                built.productPriceAdjustments = new HashSet<ProductPriceAdjustment>();
            }
            built.productPriceAdjustments.add(productPriceAdjustment);
            return this;
        }
    }
}

