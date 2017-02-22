package com.example.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.example.domain.models.Product;
import com.example.domain.models.ProductPrice;
import com.example.domain.models.ProductPriceAdjustment;
import com.example.domain.repositories.ProductPriceAdjustmentRepository;
import com.example.domain.repositories.ProductPriceRepository;
import com.example.domain.repositories.ProductRepository;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * Support the management of products
 */
@Service
public class ProductService extends BaseService<Product, Long> {
    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductPriceAdjustmentRepository productPriceAdjustmentRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          ProductPriceRepository productPriceRepository,
                          ProductPriceAdjustmentRepository productPriceAdjustmentRepository) {
        this.productPriceRepository = productPriceRepository;
        this.productRepository = productRepository;
        this.productPriceAdjustmentRepository = productPriceAdjustmentRepository;
    }

    @Override
    protected CrudRepository<Product, Long> getRepository() {
        return productRepository;
    }

    @Override
    public Product get(Long id) {
        return super.get(id);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public Product save(Product object) {
        return super.save(object);
    }

    public Set<Product> findAll() {
        Iterable<Product> allProducts = productRepository.findAll();
        Set<Product> products = Sets.newHashSet(allProducts);
        return products;
    }

    public Product findBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    /**
     * Return the price(s) for the product.
     *
     * @param productId
     * @return
     */
    public Set<ProductPrice> pricesForProduct(Long productId) {
        Iterable<ProductPrice> allPrices = productPriceRepository.findAllByProductId(productId);
        Set<ProductPrice> prices = Sets.newHashSet(allPrices);
        return prices;
    }

    /**
     * Return a Map of price adjustments where the key is the quantity of product to adjust and the
     * value is the dollar amount to adjust for each product item.
     *
     * @param productId
     * @param totalQuantity
     * @return Map containing the price adjustments for a product, if any, based on the number of
     * product items
     */
    public Map<Integer, Double> priceAdjustmentForProduct(Long productId, int totalQuantity) {
        Map<Integer, Double> quantityToAdjustmentMap = new HashMap<>();

        List<ProductPriceAdjustment> adjustments = productPriceAdjustmentRepository.findAllByProductId(productId);
        if (adjustments == null) {
            return quantityToAdjustmentMap;
        }
        for (ProductPriceAdjustment adjustment : adjustments) {
            if (totalQuantity >= adjustment.getQuantityToApplyAdjustment()) {
                int remainder = totalQuantity % adjustment.getQuantityToApplyAdjustment();
                if (remainder == 0) {
                    quantityToAdjustmentMap.put(totalQuantity, adjustment.getPriceAdjustmentAmount());
                } else {
                    int quantityToAdjust = (int) ((totalQuantity - remainder));
                    quantityToAdjustmentMap.put(quantityToAdjust, adjustment.getPriceAdjustmentAmount());
                }
            }
        }
        return quantityToAdjustmentMap;
    }

}
