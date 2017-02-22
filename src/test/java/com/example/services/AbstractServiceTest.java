package com.example.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import com.example.StoreTestUtil;
import com.example.domain.models.Product;
import com.example.domain.models.ProductPrice;
import com.example.domain.models.ProductPriceAdjustment;
import com.example.domain.models.Sale;
import com.example.domain.models.SaleDetail;
import com.example.domain.repositories.ProductPriceAdjustmentRepository;
import com.example.domain.repositories.ProductPriceRepository;
import com.example.domain.repositories.ProductRepository;
import com.example.domain.repositories.SaleRepository;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import static org.joda.time.DateTime.now;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public abstract class AbstractServiceTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPriceRepository productPriceRepository;

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    ProductPriceAdjustmentRepository productPriceAdjustmentRepository;

    @Before
    public void setup() {
        saleRepository.deleteAll();
        productPriceRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Transactional
    Product createTestProduct() {
        Product product = createProduct(StoreTestUtil.createStringWithLength(2));

        ProductPriceAdjustment priceAdjustment = createProductPriceAdjustment(product, 3, -0.25);
        Set<ProductPriceAdjustment> priceAdjustments = product.getProductPriceAdjustments();
        priceAdjustments.add(priceAdjustment);

        ProductPrice price = createProductPrice(product, 1.25);
        Set<ProductPrice> prices = product.getProductPrices();
        prices.add(price);

        product = productRepository.save(product);
        return product;
    }

    Product createProduct(String sku) {
        return Product.getBuilder(sku)
                .build();
    }

    ProductPrice createProductPrice(Product product, double price) {
        ProductPrice productPrice = ProductPrice.getBuilder(product, price).build();
        return productPrice;
    }

    ProductPriceAdjustment createProductPriceAdjustment(Product product, int quantity, double price) {
        ProductPriceAdjustment productPriceAdjustment = ProductPriceAdjustment.getBuilder(product, quantity, price).build();
        return productPriceAdjustment;
    }

    Sale createSaleWithNoProducts() {
        DateTime saleDate = now();
        Sale sale = Sale.getBuilder(saleDate).build();
        sale = saleRepository.save(sale);
        return sale;
    }

    Sale createSaleWithProduct() {
        DateTime saleDate = now();
        Product product = createTestProduct();

        Sale sale = Sale.getBuilder(saleDate).build();

        ProductPrice[] productPriceAry = new ProductPrice[product.getProductPrices().size()];
        ProductPrice firstPrice = product.getProductPrices().toArray(productPriceAry)[0];
        SaleDetail saleDetail = SaleDetail.getBuilder(sale, firstPrice, 1).build();
        Set<SaleDetail> saleDetailList = sale.getSaleDetails();
        if (saleDetailList == null) {
            saleDetailList = new HashSet<>();
        }
        saleDetailList.add(saleDetail);
        sale.setSaleDetails(saleDetailList);
        sale = saleRepository.save(sale);
        return sale;
    }
}
