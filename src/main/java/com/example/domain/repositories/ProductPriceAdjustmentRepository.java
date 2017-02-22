package com.example.domain.repositories;

import java.util.List;
import com.example.domain.models.ProductPriceAdjustment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPriceAdjustmentRepository extends CrudRepository<ProductPriceAdjustment, Long> {
    List<ProductPriceAdjustment> findAllByProductId(Long productId);
}
