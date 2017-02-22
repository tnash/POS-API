package com.example.domain.repositories;

import com.example.domain.models.SaleDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleDetailRepository extends CrudRepository<SaleDetail, Long> {
}
