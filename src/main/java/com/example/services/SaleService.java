package com.example.services;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import com.example.domain.models.Product;
import com.example.domain.models.ProductPrice;
import com.example.domain.models.Sale;
import com.example.domain.models.SaleDetail;
import com.example.domain.models.SalePriceAdjustmentDetail;
import com.example.domain.repositories.SaleRepository;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * Support the management of sales
 */
@Service
public class SaleService extends BaseService<Sale, Long> {
    private SaleRepository saleRepository;
    private ProductService productService;

    @Autowired
    public SaleService(SaleRepository saleRepository, ProductService productService) {
        this.saleRepository = saleRepository;
        this.productService = productService;
    }

    @Override
    protected CrudRepository<Sale, Long> getRepository() {
        return saleRepository;
    }

    @Override
    public Sale get(Long id) {
        return super.get(id);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public Sale save(Sale sale) {
        return super.save(sale);
    }

    public Sale createSale() {
        DateTime dateTime = new DateTime();
        Timestamp timeStamp = new Timestamp(dateTime.getMillis());

        Sale newSale = new Sale();
        newSale.setSaleDate(timeStamp);
        newSale.setTotal(0.0);
        newSale = saleRepository.save(newSale);
        return newSale;
    }

    public Set<Sale> findAll() {
        Iterable<Sale> allSales = saleRepository.findAll();
        Set<Sale> sales = Sets.newHashSet(allSales);
        return sales;
    }

    public Sale scan(Long id, String sku) {
        Sale sale = get(id);
        if (sale == null) {
            throw new EmptyResultDataAccessException("Sale by identifier " + id + " not found", 1);
        } else {
            return addProductToSale(sale, sku);
        }
    }

    public Double saleTotal(Long id) {
        Sale sale = get(id);
        return calculateSaleTotal(sale);
    }

    protected Sale addProductToSale(Sale sale, String sku) {
        Set<SaleDetail> details = sale.getSaleDetails();
        if (details == null) {
            details = new HashSet<SaleDetail>();
        }
        Product product = productService.findBySku(sku);
        if (product == null) {
            throw new EmptyResultDataAccessException("Product by sku " + sku + " not found", 1);
        } else {
            Set<ProductPrice> pricesForProduct = productService.pricesForProduct(product.getId());
            ProductPrice productPrice = null;
            if (pricesForProduct != null) {
                ProductPrice[] priceAry = new ProductPrice[pricesForProduct.size()];
                productPrice = pricesForProduct.toArray(priceAry)[0]; // Assume we only have one price, use the first one returned
            }

            SaleDetail saleDetail = createOrUpdateSaleDetail(sale, details, product, productPrice);

            Set<SalePriceAdjustmentDetail> priceAdjustmentDetails = priceAdjustmentsForDetail(sale, saleDetail);
            sale = applyPriceAdjustmentsToSale(sale, priceAdjustmentDetails);
        }
        sale.setSaleDetails(details);
        sale.setTotal(calculateSaleTotal(sale));
        sale = saleRepository.save(sale);
        return sale;
    }

    private SaleDetail createOrUpdateSaleDetail(Sale sale, Set<SaleDetail> currentDetails, Product product, ProductPrice productPrice) {
        int currentQuantityOfProduct = calcQuantityOfProduct(currentDetails, product.getId());
        int totalQuantityOfProduct = currentQuantityOfProduct + 1;

        SaleDetail saleDetail = detailMatchingProductPrice(currentDetails,
                d -> d.getProductPrice().getProduct().getId().equals(product.getId()));
        if (saleDetail == null) {
            saleDetail = new SaleDetail();
            currentDetails.add(saleDetail);
        }
        saleDetail.setProductPrice(productPrice);
        saleDetail.setQuantity(totalQuantityOfProduct);
        saleDetail.setSale(sale);
        saleDetail.setSalePrice(productPrice.getPrice());
        return saleDetail;
    }

    private Set<SalePriceAdjustmentDetail> priceAdjustmentsForDetail(Sale sale, SaleDetail saleDetail) {
        Set<SalePriceAdjustmentDetail> priceAdjustments = new HashSet<SalePriceAdjustmentDetail>();

        Map<Integer, Double> quantityToAdjustmentMap = productService.priceAdjustmentForProduct(saleDetail.getProductPrice().getProduct().getId(), saleDetail.getQuantity());
        for (Map.Entry<Integer, Double> entry : quantityToAdjustmentMap.entrySet()) {
            priceAdjustments.add(new SalePriceAdjustmentDetail(sale, saleDetail.getProductPrice().getProduct(), entry.getKey(), entry.getValue()));
        }
        return priceAdjustments;
    }

    private Sale applyPriceAdjustmentsToSale(Sale sale, Set<SalePriceAdjustmentDetail> priceAdustments) {
        Set<SalePriceAdjustmentDetail> currentPriceAdjustments = sale.getPriceAdjustmentDetails();
        if (currentPriceAdjustments == null) {
            currentPriceAdjustments = new HashSet<SalePriceAdjustmentDetail>();
        }
        boolean adjustmentApplied = false;
        if (currentPriceAdjustments.size() == 0) {
            currentPriceAdjustments.addAll(priceAdustments);
        } else {
            for (SalePriceAdjustmentDetail detail : currentPriceAdjustments) {
                for (SalePriceAdjustmentDetail priceAdjustmentDetail : priceAdustments) {
                    if (detail.getProduct().getId().equals(priceAdjustmentDetail.getProduct().getId())) {
                        adjustmentApplied = applyPriceAdjustmentIfProductMatches(detail, priceAdjustmentDetail);
                    }
                }
                if (!adjustmentApplied) {
                    currentPriceAdjustments.add(detail);
                }
                adjustmentApplied = false;
            }
        }
        sale.setPriceAdjustmentDetails(currentPriceAdjustments);
        return sale;
    }

    private boolean isPriceAdjustmentForSameProduct(SalePriceAdjustmentDetail existingPriceAdjustmentDetail, SalePriceAdjustmentDetail newPriceAdjustmentDetail) {
        return existingPriceAdjustmentDetail.getProduct().getId().equals(newPriceAdjustmentDetail.getProduct().getId());
    }

    private boolean applyPriceAdjustmentIfProductMatches(SalePriceAdjustmentDetail saleAdjustmentDetail, SalePriceAdjustmentDetail newPriceAdjustmentDetail) {
        boolean adjustmentApplied = false;
        if (isPriceAdjustmentForSameProduct(saleAdjustmentDetail, newPriceAdjustmentDetail)) {
            saleAdjustmentDetail.setPriceAdjustmentAmount(newPriceAdjustmentDetail.getPriceAdjustmentAmount());
            saleAdjustmentDetail.setQuantityToAdjust(newPriceAdjustmentDetail.getQuantityToAdjust());
            adjustmentApplied = true;
        }
        return adjustmentApplied;
    }

    private Set<SaleDetail> addOrUpdateSaleDetail(Set<SaleDetail> details, SaleDetail newDetail) {
        if (details.size() == 0) {
            details.add(newDetail);
        }
        for (SaleDetail detail : details) {
            if (detail.getProductPrice().getId().equals(newDetail.getProductPrice().getId())) {
                detail = newDetail;
                break;
            }
        }
        return details;
    }

    private double calculateSaleTotal(Sale sale) {
        double totalSaleAmount = 0.0;
        Set<SaleDetail> saleDetails = sale.getSaleDetails();
        for (SaleDetail detail : saleDetails) {
            totalSaleAmount += detail.getQuantity() * detail.getProductPrice().getPrice();
        }

        Set<SalePriceAdjustmentDetail> priceAdjustmentDetails = sale.getPriceAdjustmentDetails();
        for (SalePriceAdjustmentDetail adjustment : priceAdjustmentDetails) {
            totalSaleAmount += adjustment.getPriceAdjustmentAmount() * adjustment.getQuantityToAdjust();
        }
        // round to 2 decimal places
        return Math.round(totalSaleAmount * 100.0) / 100.0;
    }

    private SaleDetail detailMatchingProductPrice(Set<SaleDetail> details,
                                                  Predicate<SaleDetail> tester) {
        for (SaleDetail detail : details) {
            if (tester.test(detail)) {
                return detail;
            }
        }
        return null;
    }

    private int calcQuantityOfProduct(Set<SaleDetail> saleDetails, Long productPriceId) {
        return totalQuantity(saleDetails,
                sd -> sd.getProductPrice().getId().equals(productPriceId),
                sd -> sd.getQuantity());
    }

    private Integer totalQuantity(Set<SaleDetail> saleDetails,
                                  Predicate<SaleDetail> tester,
                                  Function<SaleDetail, Integer> mapper) {
        Integer quantity = 0;
        for (SaleDetail sd : saleDetails) {
            if (tester.test(sd)) {
                quantity = mapper.apply(sd);
            }
        }
        return quantity;
    }
}
