package com.example.domain.models;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import org.joda.time.DateTime;


/**
 * Represents the sale of products
 */
@Entity
@Table(name = "SALES")
@EqualsAndHashCode(exclude={"id", "saleDetails", "priceAdjustmentDetails"})
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "sale_date", nullable = false)
    private Timestamp saleDate;

    private Double total = 0.0;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<SaleDetail> saleDetails;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<SalePriceAdjustmentDetail> priceAdjustmentDetails;

    public Long getId() {
        return id;
    }

    public Timestamp getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Timestamp saleDate) {
        this.saleDate = saleDate;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Set<SaleDetail> getSaleDetails() {
        return saleDetails;
    }

    public void setSaleDetails(Set<SaleDetail> saleDetails) {
        this.saleDetails = saleDetails;
    }

    public Set<SalePriceAdjustmentDetail> getPriceAdjustmentDetails() {
        return priceAdjustmentDetails;
    }

    public void setPriceAdjustmentDetails(Set<SalePriceAdjustmentDetail> priceAdjustmentDetails) {
        this.priceAdjustmentDetails = priceAdjustmentDetails;
    }

    public static Builder getBuilder(DateTime saleDate) {
        return new Builder(saleDate);
    }

    public static class Builder {

        private Sale built;

        Builder(DateTime saleDate) {
            built = new Sale();
            built.setSaleDetails(new HashSet<SaleDetail>());
            built.saleDate = new Timestamp(saleDate.getMillis());
        }

        public Sale build() {
            return built;
        }

        public Builder saleDetail(SaleDetail saleDetail) {
            if (built.saleDetails == null) {
                built.saleDetails = new HashSet<SaleDetail>();
            }
            built.saleDetails.add(saleDetail);
            return this;
        }

        public Builder salePriceAdjustmentDetail(SalePriceAdjustmentDetail salePriceAdjustmentDetail) {
            if (built.priceAdjustmentDetails == null) {
                built.priceAdjustmentDetails = new HashSet<SalePriceAdjustmentDetail>();
            }
            built.priceAdjustmentDetails.add(salePriceAdjustmentDetail);
            return this;
        }
    }
}
