package com.example.services;

import java.util.Set;
import com.example.StoreTestUtil;
import com.example.domain.models.Product;
import com.example.domain.models.ProductPrice;
import com.example.domain.models.ProductPriceAdjustment;
import com.example.domain.models.Sale;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SalesServiceTest extends AbstractServiceTest {

    @Autowired
    SaleService saleService;

    @Autowired
    ProductService productService;

    @Test
    public void testScan_withValidSku_willReturnSaleWithScannedProduct() {
        Sale emptySale = createSaleWithNoProducts();
        Product product = createTestProduct();

        emptySale = saleService.scan(emptySale.getId(), product.getSku());
        assertNotNull(emptySale.getSaleDetails());
    }

    @Test
    public void testScan_withQuantitiesForcingPriceAdjustment_willCalculateCorrectPrice() {
        Sale sale = createSaleWithNoProducts();

        Product product = createProduct(StoreTestUtil.createStringWithLength(3));

        ProductPriceAdjustment priceAdjustment = createProductPriceAdjustment(product, 10, -0.20);
        Set<ProductPriceAdjustment> priceAdjustments = product.getProductPriceAdjustments();
        priceAdjustments.add(priceAdjustment);

        ProductPrice price = createProductPrice(product, 2.0);
        Set<ProductPrice> prices = product.getProductPrices();
        prices.add(price);

        product = productRepository.save(product);

        for (int i = 0; i < 11; i++) {
            sale = saleService.scan(sale.getId(), product.getSku());
        }

        assertTrue(sale.getTotal() == 20.0);
    }
}
