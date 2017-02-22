package com.example.domain.repositories;

import java.util.Set;
import com.example.domain.models.ProductPrice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPriceRepository extends CrudRepository<ProductPrice, Long> {
    Set<ProductPrice> findAllByProductId(Long productId);
}
