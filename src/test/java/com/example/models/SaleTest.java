package com.example.models;

import java.util.ArrayList;
import java.util.HashSet;
import com.example.StoreTestUtil;
import com.example.domain.models.Product;
import com.example.domain.models.ProductPrice;
import com.example.domain.models.ProductPriceAdjustment;
import com.example.domain.models.Sale;
import com.example.domain.models.SaleDetail;
import org.joda.time.DateTime;
import org.junit.Test;
import static org.joda.time.DateTime.now;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SaleTest {
    private DateTime saleDate = now();

    @Test
    public void buildWithMandatoryInformation() {
        Sale built = Sale.getBuilder(saleDate).build();

        assertNull(built.getId());
        assertTrue(built.getSaleDetails().size() == 0);
        assertTrue(built.getTotal() == 0.0);
        assertEquals(saleDate.getMillis(), built.getSaleDate().getTime());
    }

    @Test
    public void buildWithAllInformation() {
        Product product = Product.getBuilder(StoreTestUtil.createStringWithLength(2))
                .build();

        ProductPrice price = ProductPrice.getBuilder(product, 1.0).build();
        ProductPriceAdjustment priceAdjustment = ProductPriceAdjustment.getBuilder(product, 3, .25).build();
        product.setProductPrices(new HashSet<ProductPrice>());
        product.getProductPrices().add(price);
        product.getProductPriceAdjustments().add(priceAdjustment);

        Sale sale = Sale.getBuilder(saleDate).build();

        SaleDetail saleDetail = SaleDetail.getBuilder(sale, price, 1).build();
        sale.getSaleDetails().add(saleDetail);

        assertNull(sale.getId());
        assertNotNull(sale.getSaleDetails());
        assertTrue(sale.getTotal() == 0.0);
        assertEquals(saleDate.getMillis(), sale.getSaleDate().getTime());
    }
}
